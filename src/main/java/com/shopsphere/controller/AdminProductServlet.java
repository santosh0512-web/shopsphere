
package com.shopsphere.controller;

import java.io.IOException;
import java.math.BigDecimal;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("products", productDAO.getAllProducts());

        request.getRequestDispatcher("/admin/products.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {
            if ("add".equals(action) || "update".equals(action)) {

                Product product = new Product();

                String categoryId = request.getParameter("categoryId");
                String name = request.getParameter("name");
                String brand = request.getParameter("brand");
                String description = request.getParameter("description");
                String price = request.getParameter("price");
                String discount = request.getParameter("discount");
                String stock = request.getParameter("stock");
                String imageUrl = request.getParameter("imageUrl");
                String status = request.getParameter("status");

                if (categoryId == null || name == null || name.trim().isEmpty()
                        || price == null || stock == null
                        || discount == null) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Required product fields are missing.");
                    return;
                }

                int parsedCategoryId = Integer.parseInt(categoryId);
                int parsedStock = Integer.parseInt(stock);
                BigDecimal parsedPrice = new BigDecimal(price);
                BigDecimal parsedDiscount = new BigDecimal(discount);

                if (parsedCategoryId <= 0 || parsedStock < 0
                        || parsedPrice.compareTo(BigDecimal.ZERO) < 0
                        || parsedDiscount.compareTo(BigDecimal.ZERO) < 0
                        || parsedDiscount.compareTo(new BigDecimal("100")) > 0) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid category, stock, price, or discount.");
                    return;
                }

                product.setCategoryId(parsedCategoryId);
                product.setName(name.trim());
                product.setBrand(brand);
                product.setDescription(description);
                product.setPrice(parsedPrice);
                product.setDiscount(parsedDiscount);
                product.setStock(parsedStock);
                product.setImageUrl(imageUrl);
                product.setStatus("1".equals(status) ? "1" : "0");

                if ("add".equals(action)) {
                    productDAO.addProduct(product);
                } else {
                    int productId = Integer.parseInt(
                            request.getParameter("productId"));
                    if (productId <= 0) {
                        response.sendError(
                                HttpServletResponse.SC_BAD_REQUEST,
                                "Invalid product ID.");
                        return;
                    }
                    product.setProductId(productId);
                    productDAO.updateProduct(product);
                }

            } else if ("delete".equals(action)) {

                int productId = Integer.parseInt(
                        request.getParameter("productId"));

                if (productId <= 0) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid product ID.");
                    return;
                }

                productDAO.deleteProduct(productId);

            } else {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid action.");
                return;
            }

            response.sendRedirect(
                    request.getContextPath() + "/admin/products");

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Please enter valid numeric values.");
        } catch (RuntimeException e) {
            getServletContext().log("Admin product operation failed", e);
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Product operation failed. Check server logs.");
        }
    }
}
