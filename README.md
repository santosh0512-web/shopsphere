# ShopSphere

ShopSphere is a Java/JSP e-commerce web application backed by MySQL.

## Technology

- Java 17
- Jakarta Servlets and JSP
- Apache Tomcat 11
- MySQL 8
- Maven

## Run locally

1. Follow [the setup guide](docs/Setup-Guide.md) to create the database and
   configure the database environment variables for Tomcat.
2. Build the application:

   ```powershell
   mvn clean package
   ```

3. Deploy `target\shopsphere.war` to Tomcat's `webapps` directory and start or
   restart Tomcat.
4. Open <http://localhost:8080/shopsphere/>.

Never commit database passwords. The original local database dump is excluded
from Git; use the safe, non-destructive schema and catalogue script at
`docs/database-schema.sql` for a new development database.
