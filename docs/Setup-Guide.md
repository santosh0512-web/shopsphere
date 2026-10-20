# ShopSphere Setup Guide

## Requirements
- JDK 17
- Apache Maven
- Apache Tomcat 11
- MySQL Server 8

## Database setup

1. Start MySQL and create a database named `shopsphere`.
   For example, from Command Prompt:

   ```text
   mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS shopsphere;"
   ```

2. For a **new, empty development database only**, import
   `docs/database-schema.sql`
   from Command Prompt while in the project root:

   ```text
   mysql -u root -p shopsphere < docs\database-schema.sql
   ```

   This creates the tables and sample product catalogue without dropping
   existing tables or creating sample user accounts. Do not import it over an
   existing database because tables must be empty.
3. Register a customer through the application. To grant an account admin
   access, change its role using a MySQL account with database permissions:

   ```sql
   UPDATE users SET role = 'ADMIN' WHERE email = 'your-email@example.com';
   ```

4. Configure the environment variables below for the Tomcat process:

| Variable | Required | Default |
| --- | --- | --- |
| `SHOPSPHERE_DB_URL` | No | `jdbc:mysql://localhost:3306/shopsphere` |
| `SHOPSPHERE_DB_USER` | No | `root` |
| `SHOPSPHERE_DB_PASSWORD` | Yes | None |

The legacy names `DB_URL`, `DB_USER` and `DB_PASSWORD` are also accepted.
Use a MySQL account with access to the `shopsphere` database. Do not commit
credentials or put a real password in source code.

On Windows, add these variables through **System Properties > Environment
Variables** (use user variables when Tomcat runs as your user, or system
variables when it runs as a Windows service), then fully restart Tomcat so its
process picks them up.

## Build

From the project root in PowerShell:

```powershell
mvn clean package
```

The build creates `target\shopsphere.war`.

## Deploy

Copy `target\shopsphere.war` to Tomcat's `webapps` directory, then start or
restart Tomcat. If another ShopSphere app is already deployed there, back it up
before replacing it.

## Open the application

Visit `http://localhost:8080/shopsphere/`.

If the home page loads but product pages fail, check that MySQL is running, the
database schema was imported, and the Tomcat process has the correct database
environment variables. The database test endpoint is
`http://localhost:8080/shopsphere/db-test`.

`MOCK_ONLINE` is a simulated payment option; it does not charge real money.
