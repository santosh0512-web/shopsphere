
package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.Product;

public class ProductDAO {

    // GET ALL PRODUCTS
    public List<Product> getAllProducts() {
        return findProducts(false);
    }

    public List<Product> getActiveProducts() {
        return findProducts(true);
    }

    private List<Product> findProducts(boolean activeOnly) {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT product_id, category_id, name, brand, "
                   + "description, price, discount, stock, image_url, "
                   + "status, created_at FROM products"
                   + (activeOnly ? " WHERE status = 1" : "");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();

                product.setProductId(rs.getInt("product_id"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setName(rs.getString("name"));
                product.setBrand(rs.getString("brand"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setDiscount(rs.getBigDecimal("discount"));
                product.setStock(rs.getInt("stock"));
                product.setImageUrl(rs.getString("image_url"));
                product.setStatus(rs.getString("status"));
                product.setCreatedAt(rs.getTimestamp("created_at"));

                products.add(product);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to fetch products.", e);
        }

        return products;
    }

    // ADD PRODUCT
    public boolean addProduct(Product product) {

        String sql = "INSERT INTO products "
                   + "(category_id, name, brand, description, price, "
                   + "discount, stock, image_url, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setBigDecimal(5, product.getPrice());
            ps.setBigDecimal(6, product.getDiscount());
            ps.setInt(7, product.getStock());
            ps.setString(8, product.getImageUrl());
            ps.setString(9, product.getStatus());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to add product.", e);
        }
    }

    // UPDATE PRODUCT
    public boolean updateProduct(Product product) {

        String sql = "UPDATE products SET category_id = ?, name = ?, "
                   + "brand = ?, description = ?, price = ?, "
                   + "discount = ?, stock = ?, image_url = ?, status = ? "
                   + "WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setBigDecimal(5, product.getPrice());
            ps.setBigDecimal(6, product.getDiscount());
            ps.setInt(7, product.getStock());
            ps.setString(8, product.getImageUrl());
            ps.setString(9, product.getStatus());
            ps.setInt(10, product.getProductId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to update product.", e);
        }
    }

    // DELETE PRODUCT
    public boolean deleteProduct(int productId) {

        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Unable to delete product. It may be linked to an order or cart.",
                    e
            );
        }
    }
    // GET STOCK BY PRODUCT ID
    public int getStockByProductId(int productId) {

        String sql = "SELECT stock FROM products WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stock");
                }

             return -1; // Product not found
            }

         } catch (SQLException e) {
            throw new RuntimeException("Unable to fetch product stock.", e);
        }
    }

    // GET PRODUCT BY ID
    public Product getProductById(int productId) {

        String sql = "SELECT product_id, category_id, name, brand, "
                   + "description, price, discount, stock, image_url, "
                   + "status, created_at FROM products "
                   + "WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Product product = new Product();

                    product.setProductId(rs.getInt("product_id"));
                    product.setCategoryId(rs.getInt("category_id"));
                    product.setName(rs.getString("name"));
                    product.setBrand(rs.getString("brand"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setDiscount(rs.getBigDecimal("discount"));
                    product.setStock(rs.getInt("stock"));
                    product.setImageUrl(rs.getString("image_url"));
                    product.setStatus(rs.getString("status"));
                    product.setCreatedAt(rs.getTimestamp("created_at"));

                    return product;
                }

                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Unable to fetch product.", e
            );
        }
    }

}
