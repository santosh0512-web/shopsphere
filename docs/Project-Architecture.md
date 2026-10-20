# ShopSphere – Project Architecture

## Project Overview
ShopSphere is a Java-based e-commerce web application.

## Architecture
The project follows an MVC-style structure with DAO and Service layers.

- Model: Represents application data, such as Product, User, Cart and Address.
- View: JSP pages display the user interface.
- Controller: Servlets handle incoming HTTP requests and coordinate application actions.
- DAO: Handles database access.
- Service: Contains reusable business logic.
- Filter: Handles request filtering and access control.
- Listener: Tracks session-related events.

## Main Features
- User registration and login
- Product listing
- Shopping cart management
- Checkout and order confirmation
- Address management
- My Orders
- Admin product management

## Technology Stack
- Java
- Jakarta Servlets
- JSP
- Maven
- Apache Tomcat
- MySQL
- HTML and CSS

## Limitations
The implementation details and database configuration should be verified against the final source code before submission.
