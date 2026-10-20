
package com.shopsphere.controller;

import java.io.IOException;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    // Browser se login page open karne ke liye (GET)
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/login.jsp")
               .forward(request, response);
    }

    // Login form submit karne ke liye (POST)
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null
                || email.trim().isEmpty() || password.isEmpty()) {

            request.setAttribute(
                    "error", "Email and password are required.");

            request.getRequestDispatcher("/login.jsp")
                   .forward(request, response);
            return;
        }

        try {
            User user = userDAO.login(email.trim(), password);

            if (user != null) {

                HttpSession oldSession = request.getSession(false);

                if (oldSession != null) {
                    oldSession.invalidate();
                }

                HttpSession session = request.getSession(true);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("userName", user.getName());
                session.setAttribute("userRole", user.getRole());

                response.sendRedirect(
                        request.getContextPath() + "/products");

            } else {
                request.setAttribute(
                        "error", "Invalid email or password.");

                request.getRequestDispatcher("/login.jsp")
                       .forward(request, response);
            }

        } catch (RuntimeException e) {
            getServletContext().log("Login database error", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Login could not be completed. Please try again.");
        }
    }
}
