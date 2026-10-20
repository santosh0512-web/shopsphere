
package com.shopsphere.controller;

import java.io.IOException;
import java.util.List;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.model.Cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            Object id = session.getAttribute("userId");

            if (!(id instanceof Integer)) {
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Please log in again.");
                return;
            }

            int userId = (Integer) id;
            List<Cart> cartItems = cartDAO.getCartItems(userId);

            request.setAttribute("cartItems", cartItems);
            request.getRequestDispatcher("/cart.jsp")
                   .forward(request, response);

        } catch (RuntimeException e) {
            getServletContext().log("Unable to load cart", e);
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load your cart.");
        }
    }
}
