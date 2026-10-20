
package com.shopsphere.controller;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter({"/admin/*", "/admin/products"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(
            jakarta.servlet.ServletRequest request,
            jakarta.servlet.ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null) {

            httpResponse.sendRedirect(
                    httpRequest.getContextPath() + "/login");
            return;
        }

        Object role = session.getAttribute("userRole");

        if (role == null
                || !"ADMIN".equalsIgnoreCase(role.toString())) {

            httpResponse.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied. Admin only.");
            return;
        }

        chain.doFilter(request, response);
    }
}
