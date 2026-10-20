package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        getEnvironmentVariable(
                "SHOPSPHERE_DB_URL", "DB_URL",
                "jdbc:mysql://localhost:3306/shopsphere");

    private static final String USER =
        getEnvironmentVariable("SHOPSPHERE_DB_USER", "DB_USER", "root");

    private static final String PASSWORD = getEnvironmentVariable(
            "SHOPSPHERE_DB_PASSWORD", "DB_PASSWORD", null);

    public static Connection getConnection() throws SQLException {
        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                    "Set SHOPSPHERE_DB_PASSWORD (or DB_PASSWORD) in the "
                    + "Tomcat process environment before starting the application.");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC driver was not found by the application.", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static String getEnvironmentVariable(
            String primaryName, String legacyName, String defaultValue) {
        String value = System.getenv(primaryName);
        if (value == null || value.isBlank()) {
            value = System.getenv(legacyName);
        }
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
