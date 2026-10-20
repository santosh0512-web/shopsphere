
package com.shopsphere.network;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.shopsphere.model.Product;

public class InventoryClient {

    public static void main(String[] args) {

        int productId = 1;

        try (Socket socket = new Socket("localhost", 5000)) {

            PrintWriter output = new PrintWriter(
                socket.getOutputStream(), true
            );

            // Request a serialized Product object
            output.println("GET_PRODUCT " + productId);

            ObjectInputStream objectInput =
                new ObjectInputStream(socket.getInputStream());

            // Read the serialized Product object
            Object firstResponse = objectInput.readObject();

            if (firstResponse instanceof Product) {

                Product product = (Product) firstResponse;

                System.out.println("Product received successfully!");
                System.out.println("Product ID: " + product.getProductId());
                System.out.println("Name: " + product.getName());
                System.out.println("Brand: " + product.getBrand());
                System.out.println("Price: " + product.getPrice());
                System.out.println("Stock: " + product.getStock());

            } else {
                System.out.println(
                    "No Product object received. Check product ID and server."
                );
            }

        } catch (Exception e) {
            System.out.println(
                "Client connection failed: " + e.getMessage()
            );
        }
    }
}
