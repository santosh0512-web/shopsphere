
package com.shopsphere.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class InventoryRMIServer {

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.createRegistry(1099);

            InventoryService service = new InventoryServiceImpl();

            registry.rebind("ShopSphereInventory", service);

            System.out.println("ShopSphere RMI Server started.");
            System.out.println("RMI Registry running on port 1099.");
            System.out.println("Inventory service is ready.");

        } catch (Exception e) {
            System.out.println("RMI Server failed to start.");
            e.printStackTrace();
        }
    }
}
