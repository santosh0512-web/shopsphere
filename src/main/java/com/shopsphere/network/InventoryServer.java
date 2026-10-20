
package com.shopsphere.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;

public class InventoryServer {

    private static final int PORT = 5000;
    private static final ProductDAO productDAO = new ProductDAO();

    public static void main(String[] args) {

        System.out.println("ShopSphere Inventory Server starting...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server running on port " + PORT);
            System.out.println("Waiting for client...");

            while (true) {

                try (Socket socket = serverSocket.accept()) {

                    System.out.println("Client connected.");

                    BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                    );

                    String request = input.readLine();

                    if (request == null || request.trim().isEmpty()) {
                        PrintWriter output = new PrintWriter(
                            socket.getOutputStream(), true
                        );
                        output.println("ERROR: Empty request");
                        continue;
                    }

                    if (request.equals("PING")) {

                        PrintWriter output = new PrintWriter(
                            socket.getOutputStream(), true
                        );
                        output.println("ShopSphere server is working!");

                    } else if (request.startsWith("GET_PRODUCT ")) {

                        try {
                            int productId = Integer.parseInt(
                                request.substring("GET_PRODUCT ".length()).trim()
                            );

                            if (productId <= 0) {
                                PrintWriter output = new PrintWriter(
                                    socket.getOutputStream(), true
                                );
                                output.println("ERROR: Invalid product ID");
                                continue;
                            }

                            Product product =
                                productDAO.getProductById(productId);

                            if (product == null) {
                                PrintWriter output = new PrintWriter(
                                    socket.getOutputStream(), true
                                );
                                output.println("NOT_FOUND");
                            } else {
                                ObjectOutputStream objectOutput =
                                    new ObjectOutputStream(
                                        socket.getOutputStream()
                                    );

                                objectOutput.writeObject(product);
                                objectOutput.flush();

                                System.out.println(
                                    "Serialized product sent: "
                                    + product.getName()
                                );
                            }

                        } catch (NumberFormatException e) {
                            PrintWriter output = new PrintWriter(
                                socket.getOutputStream(), true
                            );
                            output.println("ERROR: Invalid product ID");
                        }

                    } else if (request.startsWith("GET_STOCK ")) {

                        try {
                            int productId = Integer.parseInt(
                                request.substring("GET_STOCK ".length()).trim()
                            );

                            int stock = productDAO.getStockByProductId(productId);

                            PrintWriter output = new PrintWriter(
                                socket.getOutputStream(), true
                            );

                            if (productId <= 0) {
                                output.println("ERROR: Invalid product ID");
                            } else if (stock == -1) {
                                output.println("Product not found");
                            } else {
                                output.println(
                                    "Stock for product ID "
                                    + productId + ": " + stock
                                );
                            }

                        } catch (NumberFormatException e) {
                            PrintWriter output = new PrintWriter(
                                socket.getOutputStream(), true
                            );
                            output.println("ERROR: Invalid product ID");
                        }

                    } else {

                        PrintWriter output = new PrintWriter(
                            socket.getOutputStream(), true
                        );
                        output.println("ERROR: Unknown command");
                    }

                } catch (Exception e) {
                    System.out.println(
                        "Client request failed: " + e.getMessage()
                    );
                }
            }

        } catch (Exception e) {
            System.out.println(
                "Server could not start: " + e.getMessage()
            );
        }
    }
}
