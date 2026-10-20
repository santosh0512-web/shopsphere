
package com.shopsphere.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.shopsphere.model.Product;

public interface InventoryService extends Remote {

    Product getProduct(int productId) throws RemoteException;

    int getStock(int productId) throws RemoteException;

    boolean updateStock(int productId, int newStock)
            throws RemoteException;
}
