
package com.shopsphere.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.shopsphere.dao.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/order-confirmation")
public class OrderConfirmationServlet extends HttpServlet {

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

        try {
            int orderId = Integer.parseInt(request.getParameter("id"));

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(
                         "SELECT order_id, total_amount, payment_method, " +
                         "payment_status, order_status, created_at " +
                         "FROM orders WHERE order_id = ? AND user_id = ?")) {

                ps.setInt(1, orderId);
                ps.setInt(2, userId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND,
                                "Order nahi mila.");
                        return;
                    }

                    request.setAttribute("orderId", rs.getInt("order_id"));
                    request.setAttribute("total", rs.getBigDecimal("total_amount"));
                    request.setAttribute("paymentMethod", rs.getString("payment_method"));
                    request.setAttribute("paymentStatus", rs.getString("payment_status"));
                    request.setAttribute("orderStatus", rs.getString("order_status"));
                    request.setAttribute("createdAt", rs.getTimestamp("created_at"));
                }
            }

            request.getRequestDispatcher("/order-confirmation.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid order ID.");
        } catch (java.sql.SQLException e) {
            throw new ServletException("Order details load nahi ho sake.", e);
        }
    }
}
