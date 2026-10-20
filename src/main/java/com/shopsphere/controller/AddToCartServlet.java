
package com.shopsphere.controller;

import java.io.IOException;

import com.shopsphere.dao.CartDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/add-to-cart")
public class AddToCartServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Login check
        if (session == null
                || session.getAttribute("userId") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            Object userIdObject = session.getAttribute("userId");

            if (!(userIdObject instanceof Integer)) {
                getServletContext().log(
                        "Invalid userId type in session: "
                        + userIdObject.getClass().getName());

                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Invalid login session. Please log in again.");
                return;
            }

            int userId = (Integer) userIdObject;

            String productIdParameter =
                    request.getParameter("productId");

            if (productIdParameter == null
                    || productIdParameter.trim().isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Product ID is required.");
                return;
            }

            int productId = Integer.parseInt(
                    productIdParameter.trim());

            boolean added = cartDAO.addToCart(userId, productId);

            if (added) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/products?added=true");
            } else {
                response.sendRedirect(
                        request.getContextPath()
                        + "/products?added=false");
            }

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product ID.");

        } catch (RuntimeException e) {
            getServletContext().log(
                    "Error while adding product to cart", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to add product to cart. Please try again.");
        }
    }
}
