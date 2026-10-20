
package com.shopsphere.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.shopsphere.model.Product;

public class InventoryRMIClient {

    public static void main(String[] args) {

        try {
            Registry registry =
                LocateRegistry.getRegistry("localhost", 1099);

            InventoryService service =
                (InventoryService) registry.lookup("ShopSphereInventory");

            int productId = 1;

            Product product = service.getProduct(productId);

            if (product == null) {
                System.out.println("Product not found.");
            } else {
                System.out.println("Product received through RMI!");
                System.out.println("Product ID: " + product.getProductId());
                System.out.println("Name: " + product.getName());
                System.out.println("Brand: " + product.getBrand());
                System.out.println("Price: " + product.getPrice());
                System.out.println("Stock: " + product.getStock());
            }

            int stock = service.getStock(productId);

            if (stock >= 0) {
                System.out.println("Current stock: " + stock);
            } else {
                System.out.println("Stock information unavailable.");
            }

        } catch (Exception e) {
            System.out.println("RMI Client failed.");
            e.printStackTrace();
        }
    }
}
