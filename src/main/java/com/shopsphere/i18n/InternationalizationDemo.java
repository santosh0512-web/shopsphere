
package com.shopsphere.i18n;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class InternationalizationDemo {

    public static void main(String[] args) {

        Locale[] locales = {
            Locale.US,
            Locale.UK,
            Locale.FRANCE,
            new Locale("hi", "IN")
        };

        double price = 2499.50;

        for (Locale locale : locales) {

            NumberFormat currencyFormat =
                NumberFormat.getCurrencyInstance(locale);

            System.out.println("Locale: " + locale);
            System.out.println(
                "Formatted price: " + currencyFormat.format(price)
            );
            System.out.println("----------------------");
        }

        ResourceBundle messages = ResourceBundle.getBundle(
            "com.shopsphere.i18n.Messages",
            Locale.US
        );

        System.out.println(messages.getString("welcome"));
    }
}
