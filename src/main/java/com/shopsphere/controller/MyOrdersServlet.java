
package com.shopsphere.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shopsphere.dao.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/my-orders")
public class MyOrdersServlet extends HttpServlet {

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

        List<Map<String, Object>> orders = new ArrayList<>();

        String sql = "SELECT order_id, total_amount, discount, payment_method, " +
                     "payment_status, order_status, created_at " +
                     "FROM orders WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> order = new HashMap<>();
                    order.put("orderId", rs.getInt("order_id"));
                    order.put("totalAmount", rs.getBigDecimal("total_amount"));
                    order.put("discount", rs.getBigDecimal("discount"));
                    order.put("paymentMethod", rs.getString("payment_method"));
                    order.put("paymentStatus", rs.getString("payment_status"));
                    order.put("orderStatus", rs.getString("order_status"));
                    order.put("createdAt", rs.getTimestamp("created_at"));
                    orders.add(order);
                }
            }

            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/my-orders.jsp")
                   .forward(request, response);

        } catch (java.sql.SQLException e) {
            throw new ServletException("Orders load nahi ho sake.", e);
        }
    }
}
