
package com.shopsphere.urldemo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionDemo {

    public static void main(String[] args) {

        try {
            URL url = new URL("https://example.com");

            URLConnection connection = url.openConnection();

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            System.out.println("URL: " + url);
            System.out.println("Content Type: "
                    + connection.getContentType());

            try (
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
                )
            ) {
                String line;

                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            System.out.println("Unable to read URL content.");
            System.out.println("Reason: " + e.getMessage());
        }
    }
}
