package com.shopsphere.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import com.shopsphere.dao.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/db-test")
public class DBTestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Database Test</title></head><body>");
        out.println("<h2>ShopSphere Database Connection Test</h2>");

        // Step 1: Check whether MySQL JDBC driver can load
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            out.println("<p>MySQL Driver loaded successfully!</p>");

        } catch (ClassNotFoundException e) {
            out.println("<p>MySQL Driver class not found!</p>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
            out.println("</body></html>");
            return;
        }

        // Step 2: Test database connection
        try (Connection connection = DBConnection.getConnection()) {

            if (connection != null && !connection.isClosed()) {
                out.println("<h3>Database connected successfully!</h3>");
                out.println("<p>Database: shopsphere</p>");
            }

        } catch (SQLException e) {
            out.println("<h3>Connection failed!</h3>");
            out.println("<p>Error: " + e.getMessage() + "</p>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        }

        out.println("</body></html>");
    }
}
