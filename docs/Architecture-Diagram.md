# ShopSphere - Architecture Diagram

- User opens the application in a browser.
- Tomcat 11 runs the web application.
- Filters handle authentication and admin access checks.
- Jakarta Servlets handle requests.
- The Service and DAO layers handle application logic and database operations.
- MySQL stores application data.
- JSP pages display responses to the user.
- HTTP sessions maintain user state.
