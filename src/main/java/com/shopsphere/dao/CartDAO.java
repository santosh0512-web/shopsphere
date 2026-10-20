
package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.Cart;

public class CartDAO {

    // Add product to cart
    public boolean addToCart(int userId, int productId) {

        String stockSql =
            "SELECT stock FROM products WHERE product_id = ? AND status = 1";

        String cartSql =
            "SELECT quantity FROM cart WHERE user_id = ? AND product_id = ?";

        String updateSql =
            "UPDATE cart SET quantity = quantity + 1 " +
            "WHERE user_id = ? AND product_id = ?";

        String insertSql =
            "INSERT INTO cart (user_id, product_id, quantity) " +
            "VALUES (?, ?, 1)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                int stock;

                try (PreparedStatement ps = con.prepareStatement(stockSql)) {
                    ps.setInt(1, productId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }
                        stock = rs.getInt("stock");
                    }
                }

                int quantity = 0;
                boolean exists;

                try (PreparedStatement ps = con.prepareStatement(cartSql)) {
                    ps.setInt(1, userId);
                    ps.setInt(2, productId);

                    try (ResultSet rs = ps.executeQuery()) {
                        exists = rs.next();
                        if (exists) {
                            quantity = rs.getInt("quantity");
                        }
                    }
                }

                if (stock <= 0 || (exists && quantity >= stock)) {
                    con.rollback();
                    return false;
                }

                if (exists) {
                    try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                        ps.setInt(1, userId);
                        ps.setInt(2, productId);
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                        ps.setInt(1, userId);
                        ps.setInt(2, productId);
                        ps.executeUpdate();
                    }
                }

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to add product to cart.", e);
        }
    }

    // Get all cart items for a user
    public List<Cart> getCartItems(int userId) {

        List<Cart> items = new ArrayList<>();

        String sql =
            "SELECT c.product_id, c.quantity, p.name, p.price " +
            "FROM cart c " +
            "JOIN products p ON c.product_id = p.product_id " +
            "WHERE c.user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cart item = new Cart();

                    item.setProductId(rs.getInt("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setProductName(rs.getString("name"));
                    item.setPrice(rs.getDouble("price"));

                    items.add(item);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to fetch cart items.", e);
        }

        return items;
    }

    // Update product quantity
    public boolean updateQuantity(int userId, int productId, int quantity) {

        if (quantity <= 0) {
            return false;
        }

        String sql =
            "UPDATE cart c " +
            "JOIN products p ON c.product_id = p.product_id " +
            "SET c.quantity = ? " +
            "WHERE c.user_id = ? AND c.product_id = ? " +
            "AND ? <= p.stock AND p.status = 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.setInt(4, quantity);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to update cart quantity.", e);
        }
    }

    // Remove product from cart
    public boolean removeFromCart(int userId, int productId) {

        String sql =
            "DELETE FROM cart WHERE user_id = ? AND product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to remove cart item.", e);
        }
    }
}
