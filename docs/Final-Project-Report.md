# ShopSphere – E-Commerce Web Application

## 1. Introduction
ShopSphere is a Java-based e-commerce web application that allows users to browse products, register, log in, manage a shopping cart, place orders, and view their order history.

## 2. Objectives
- Develop an online shopping application.
- Implement user registration and authentication.
- Provide product browsing and cart management.
- Support checkout, address selection, and order placement.
- Store application data in a MySQL database.
- Demonstrate Java web technologies, including Servlets and JSP.

## 3. Technologies Used
- Java
- Jakarta Servlets
- JSP
- Apache Tomcat 11
- Maven
- MySQL
- JDBC
- HTML and CSS

## 4. Main Modules
### User Module
Registration, login, logout, and session handling.

### Product Module
Product listing and product-related database operations.

### Shopping Cart Module
Adding products, updating quantities, and removing items.

### Checkout and Orders Module
Address selection, checkout, order placement, and order history.

### Administration Module
Admin access checks and product-management functionality.

## 5. System Architecture
The application follows a layered design involving browser pages, servlet controllers, filters, service classes, DAO classes, JSP views, and a MySQL database.

Refer to Architecture-Diagram.md for the architecture overview.

## 6. Database
The application uses the shopsphere MySQL database. Its schema includes tables for users, products, categories, cart items, addresses, orders, order items, payments, and wishlist data.

Refer to Database-Schema.md and database.sql for database documentation.

## 7. Testing
Manual test cases are documented in Test-Cases.md. Core application flows have been tested according to the results reported during development. Further verification should be performed before final submission.

## 8. Limitations
- The MOCK_ONLINE payment option simulates a payment flow and is not a real payment gateway.
- The application requires a correctly configured MySQL database and Tomcat server.
- Additional testing and review of supplementary syllabus demonstrations may be required.

## 9. Future Enhancements
- Integrate a real payment gateway.
- Improve product search and filtering.
- Add stronger reporting and order-management features.
- Improve automated testing and deployment.

## 10. Conclusion
ShopSphere demonstrates the development of a Java-based e-commerce application using Servlets, JSP, JDBC, Tomcat, Maven, and MySQL. It brings together key shopping workflows, including authentication, product browsing, cart management, checkout, and order history.

## References
- Java documentation
- Apache Tomcat documentation
- Apache Maven documentation
- MySQL documentation
