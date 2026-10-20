
package com.shopsphere.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.dao.DBConnection;
import com.shopsphere.model.Address;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = ((Number) session.getAttribute("userId")).intValue();

        try (Connection con = DBConnection.getConnection()) {

            List<Address> addresses = new ArrayList<>();

            String addressSql =
                    "SELECT address_id, user_id, address_line, city, state, " +
                    "pincode, address_type FROM addresses WHERE user_id = ?";

            try (PreparedStatement ps = con.prepareStatement(addressSql)) {
                ps.setInt(1, userId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Address address = new Address();
                        address.setAddressId(rs.getInt("address_id"));
                        address.setUserId(rs.getInt("user_id"));
                        address.setAddressLine(rs.getString("address_line"));
                        address.setCity(rs.getString("city"));
                        address.setState(rs.getString("state"));
                        address.setPincode(rs.getString("pincode"));
                        address.setAddressType(rs.getString("address_type"));
                        addresses.add(address);
                    }
                }
            }

            String cartSql =
                    "SELECT c.product_id, c.quantity, p.name, p.price, " +
                    "COALESCE(p.discount, 0) AS discount, p.stock " +
                    "FROM cart c JOIN products p ON c.product_id = p.product_id " +
                    "WHERE c.user_id = ? AND p.status = 1";

            List<java.util.Map<String, Object>> items = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            try (PreparedStatement ps = con.prepareStatement(cartSql)) {
                ps.setInt(1, userId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int quantity = rs.getInt("quantity");
                        int stock = rs.getInt("stock");
                        BigDecimal price = rs.getBigDecimal("price");
                        BigDecimal discount = rs.getBigDecimal("discount");

                        if (quantity <= 0 || stock < quantity) {
                            request.setAttribute(
                                    "error",
                                    "Cart mein kisi product ka stock insufficient hai."
                            );
                            continue;
                        }

                        // Assumption: discount percentage hai.
                        BigDecimal multiplier = BigDecimal.ONE.subtract(
                                discount.divide(
                                        new BigDecimal("100"), 4,
                                        java.math.RoundingMode.HALF_UP
                                )
                        );

                        BigDecimal finalPrice = price.multiply(multiplier)
                                .setScale(2, java.math.RoundingMode.HALF_UP);

                        BigDecimal lineTotal = finalPrice.multiply(
                                BigDecimal.valueOf(quantity)
                        );

                        java.util.Map<String, Object> item =
                                new java.util.HashMap<>();
                        item.put("productId", rs.getInt("product_id"));
                        item.put("name", rs.getString("name"));
                        item.put("quantity", quantity);
                        item.put("price", finalPrice);
                        item.put("lineTotal", lineTotal);

                        items.add(item);
                        total = total.add(lineTotal);
                    }
                }
            }

            request.setAttribute("addresses", addresses);
            request.setAttribute("items", items);
            request.setAttribute("total", total);

            if (items.isEmpty()) {
                request.setAttribute("error", "Cart empty hai ya products available nahi hain.");
            }

            request.getRequestDispatcher("/checkout.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Checkout load karte waqt database error.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = ((Number) session.getAttribute("userId")).intValue();

        try {
            int addressId = Integer.parseInt(request.getParameter("addressId"));
            String paymentMethod = request.getParameter("paymentMethod");

            if (!"COD".equals(paymentMethod) &&
                !"MOCK_ONLINE".equals(paymentMethod)) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid payment method."
                );
                return;
            }

            int orderId = placeOrder(userId, addressId, paymentMethod);

            response.sendRedirect(
                    request.getContextPath() + "/order-confirmation?id=" + orderId
            );

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid address or order information."
            );
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (SQLException e) {
            throw new ServletException("Order place nahi ho saka.", e);
        }
    }

    private int placeOrder(int userId, int addressId, String paymentMethod)
            throws SQLException {

        try (Connection con = DBConnection.getConnection()) {
            boolean oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);

            try {
                // Confirm address belongs to the logged-in user.
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT address_id FROM addresses " +
                        "WHERE address_id = ? AND user_id = ?")) {
                    ps.setInt(1, addressId);
                    ps.setInt(2, userId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new IllegalArgumentException(
                                    "Please apna valid saved address select karein."
                            );
                        }
                    }
                }

                String cartSql =
                        "SELECT c.product_id, c.quantity, p.price, " +
                        "COALESCE(p.discount, 0) AS discount, p.stock " +
                        "FROM cart c JOIN products p ON c.product_id = p.product_id " +
                        "WHERE c.user_id = ? AND p.status = 1 FOR UPDATE";

                List<java.util.Map<String, Object>> cartItems = new ArrayList<>();
                BigDecimal total = BigDecimal.ZERO;

                try (PreparedStatement ps = con.prepareStatement(cartSql)) {
                    ps.setInt(1, userId);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int quantity = rs.getInt("quantity");
                            int stock = rs.getInt("stock");

                            if (quantity <= 0 || stock < quantity) {
                                throw new IllegalArgumentException(
                                        "Kisi product ka stock insufficient hai. Cart update karke retry karein."
                                );
                            }

                            BigDecimal price = rs.getBigDecimal("price");
                            BigDecimal discount = rs.getBigDecimal("discount");

                            if (discount.compareTo(BigDecimal.ZERO) < 0 ||
                                discount.compareTo(new BigDecimal("100")) > 0) {
                                throw new IllegalArgumentException(
                                        "Product discount value invalid hai."
                                );
                            }

                            BigDecimal finalPrice = price.multiply(
                                    BigDecimal.ONE.subtract(
                                            discount.divide(
                                                    new BigDecimal("100"), 4,
                                                    java.math.RoundingMode.HALF_UP
                                            )
                                    )
                            ).setScale(2, java.math.RoundingMode.HALF_UP);

                            BigDecimal lineTotal = finalPrice.multiply(
                                    BigDecimal.valueOf(quantity)
                            );

                            java.util.Map<String, Object> item =
                                    new java.util.HashMap<>();
                            item.put("productId", rs.getInt("product_id"));
                            item.put("quantity", quantity);
                            item.put("price", finalPrice);

                            cartItems.add(item);
                            total = total.add(lineTotal);
                        }
                    }
                }

                if (cartItems.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Cart empty hai. Pehle products add karein."
                    );
                }

                int orderId;

                String orderSql =
                        "INSERT INTO orders " +
                        "(user_id, address_id, total_amount, discount, " +
                        "payment_method, payment_status, order_status) " +
                        "VALUES (?, ?, ?, 0.00, ?, ?, 'PLACED')";

                String paymentStatus = "COD".equals(paymentMethod)
                        ? "PENDING" : "PENDING";

                try (PreparedStatement ps = con.prepareStatement(
                        orderSql, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, userId);
                    ps.setInt(2, addressId);
                    ps.setBigDecimal(3, total);
                    ps.setString(4, paymentMethod);
                    ps.setString(5, paymentStatus);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Order ID generate nahi hua.");
                        }
                        orderId = keys.getInt(1);
                    }
                }

                String itemSql =
                        "INSERT INTO order_items " +
                        "(order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

                String stockSql =
                        "UPDATE products SET stock = stock - ? " +
                        "WHERE product_id = ? AND stock >= ?";

                try (PreparedStatement itemPs = con.prepareStatement(itemSql);
                     PreparedStatement stockPs = con.prepareStatement(stockSql)) {

                    for (java.util.Map<String, Object> item : cartItems) {
                        int productId = (Integer) item.get("productId");
                        int quantity = (Integer) item.get("quantity");
                        BigDecimal price = (BigDecimal) item.get("price");

                        itemPs.setInt(1, orderId);
                        itemPs.setInt(2, productId);
                        itemPs.setInt(3, quantity);
                        itemPs.setBigDecimal(4, price);
                        itemPs.addBatch();

                        stockPs.setInt(1, quantity);
                        stockPs.setInt(2, productId);
                        stockPs.setInt(3, quantity);

                        if (stockPs.executeUpdate() != 1) {
                            throw new IllegalArgumentException(
                                    "Stock change ho gaya. Cart refresh karke retry karein."
                            );
                        }
                    }

                    itemPs.executeBatch();
                }

                String paymentSql =
                        "INSERT INTO payments " +
                        "(order_id, payment_method, transaction_reference, " +
                        "amount, payment_status) VALUES (?, ?, ?, ?, ?)";

                String reference = "COD".equals(paymentMethod)
                        ? "COD-" + orderId
                        : "MOCK-" + orderId;

                try (PreparedStatement ps = con.prepareStatement(paymentSql)) {
                    ps.setInt(1, orderId);
                    ps.setString(2, paymentMethod);
                    ps.setString(3, reference);
                    ps.setBigDecimal(4, total);
                    ps.setString(5, "PENDING");
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM cart WHERE user_id = ?")) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                }

                con.commit();
                return orderId;

            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(oldAutoCommit);
            }
        }
    }
}
