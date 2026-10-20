# ShopSphere Database Schema

Database: shopsphere
Engine: MySQL

## users
user_id, name, email, password, mobile, role, status

## products
product_id, category_id, name, brand, description, price, discount, stock, image_url, status, created_at

## cart
user_id, product_id, quantity

## addresses
address_id, user_id, address_line, city, state, pincode, address_type

## orders
order_id, user_id, address_id, total_amount, discount, payment_method, payment_status, order_status, created_at

## order_items
order_id, product_id, quantity, price

## payments
order_id, payment_method, transaction_reference, amount, payment_status

## Notes
This document lists columns referenced in the application code. Verify actual column types, keys, constraints and defaults against the existing MySQL database before creating tables.

MOCK_ONLINE is a simulated payment method, not a real payment gateway.
