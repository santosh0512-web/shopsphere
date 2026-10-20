
package com.shopsphere.jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JndiDemo {
    public static void main(String[] args) {
        Hashtable<String, Object> environment = new Hashtable<>();

        environment.put(
            Context.INITIAL_CONTEXT_FACTORY,
            "com.sun.jndi.rmi.registry.RegistryContextFactory"
        );
        environment.put(Context.PROVIDER_URL, "rmi://localhost:1099");

        try {
            Context context = new InitialContext(environment);

            Object service = context.lookup("ShopSphereInventory");

            System.out.println("JNDI Lookup Successful!");
            System.out.println("Resource: " + service.getClass().getName());

            context.close();

        } catch (NamingException e) {
            System.out.println("JNDI lookup failed.");
            System.out.println("Start InventoryRMIServer first.");
            System.out.println("Reason: " + e.getMessage());
        }
    }
}
