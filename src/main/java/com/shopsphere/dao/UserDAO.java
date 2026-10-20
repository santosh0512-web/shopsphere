package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.shopsphere.model.User;

public class UserDAO {

    // REGISTER NEW USER
    public boolean register(User user) {

        String sql = "INSERT INTO users "
                   + "(name, email, password, mobile, role, status) "
                   + "VALUES (?, ?, ?, ?, 'CUSTOMER', 1)";

        String hashedPassword = BCrypt.hashpw(
                user.getPassword(),
                BCrypt.gensalt(12)
        );

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, hashedPassword);
            ps.setString(4, user.getMobile());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {

            if ("23000".equals(e.getSQLState())) {
                return false;
            }

            throw new RuntimeException("Registration failed.", e);
        }
    }

    // LOGIN EXISTING USER
    public User login(String email, String password) {

        String sql = "SELECT user_id, name, email, password, mobile, "
                   + "role, status FROM users "
                   + "WHERE email = ? AND status = 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    String storedPassword = rs.getString("password");

                    if (storedPassword != null
                            && storedPassword.startsWith("$2")
                            && BCrypt.checkpw(password, storedPassword)) {

                        User user = new User();

                        user.setUserId(rs.getInt("user_id"));
                        user.setName(rs.getString("name"));
                        user.setEmail(rs.getString("email"));
                        user.setPassword(storedPassword);
                        user.setMobile(rs.getString("mobile"));
                        user.setRole(rs.getString("role"));
                        user.setStatus(rs.getBoolean("status"));

                        return user;
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Login failed.", e);
        }

        return null;
    }
}