
package com.shopsphere.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;

public class InventoryServiceImpl extends UnicastRemoteObject
        implements InventoryService {

    private static final long serialVersionUID = 1L;

    private final ProductDAO productDAO;

    public InventoryServiceImpl() throws RemoteException {
        super();
        productDAO = new ProductDAO();
    }

    @Override
    public Product getProduct(int productId) throws RemoteException {
        try {
            return productDAO.getProductById(productId);
        } catch (RuntimeException e) {
            throw new RemoteException("Unable to fetch product.", e);
        }
    }

    @Override
    public int getStock(int productId) throws RemoteException {
        try {
            return productDAO.getStockByProductId(productId);
        } catch (RuntimeException e) {
            throw new RemoteException("Unable to fetch stock.", e);
        }
    }

    @Override
    public boolean updateStock(int productId, int newStock)
            throws RemoteException {

        if (productId <= 0 || newStock < 0) {
            return false;
        }

        try {
            Product product = productDAO.getProductById(productId);

            if (product == null) {
                return false;
            }

            product.setStock(newStock);
            return productDAO.updateProduct(product);

        } catch (RuntimeException e) {
            throw new RemoteException("Unable to update stock.", e);
        }
    }
}
