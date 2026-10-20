package com.shopsphere.service;

import java.util.List;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;

public class ProductService {

    private final ProductDAO productDAO = new ProductDAO();

    public List<Product> getActiveProducts() {
        return productDAO.getActiveProducts();
    }
}