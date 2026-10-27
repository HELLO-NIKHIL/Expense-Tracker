# Expense Tracker — Spring Boot + PostgreSQL

This is a small web app built in steps. A browser page lets you add and delete expenses, view totals, and download all records as an Excel workbook. Spring Boot handles the requests, and PostgreSQL stores the records.

## Project map

```text
src/main/java/org/example/
  Main.java                 starts Spring Boot
  Expense.java              one expense; JPA maps it to a database row
  ExpenseController.java    receives browser requests and returns pages
  ExpenseService.java       contains the app's expense operations
  ExpenseRepository.java    Spring Data interface for database access
  ExcelExportService.java   creates the downloadable .xlsx workbook
src/main/resources/
  application.properties    database connection settings
  templates/index.html      the page, rendered by Thymeleaf
  static/styles.css         page styling
compose.yaml                starts PostgreSQL in Docker
pom.xml                     dependencies and build settings
```

## Run it

You need a JDK matching the Java version in `pom.xml`, Maven, and Docker Desktop.

1. Open the project root in IntelliJ and reload the Maven project when prompted. Wait for Maven to download the libraries listed in `pom.xml`.
2. Start Docker Desktop.
3. In a terminal opened at the project root, start PostgreSQL:

   ```sh
   docker compose up -d
   ```

4. Run `org.example.Main` from IntelliJ, or from a terminal with Maven installed:

   ```sh
   mvn spring-boot:run
   ```

5. Open <http://localhost:8080> in your browser.

The Compose file creates a PostgreSQL database and a named volume. The volume keeps database records when the container stops. Stop the database with `docker compose down`; do not add `-v` if you want to keep its data.

## What happens when an expense is added?

The HTML form sends a `POST /expenses` request. `ExpenseController` validates the submitted fields and calls `ExpenseService`. The service calls `ExpenseRepository`, which Spring Data implements using JPA. JPA turns the `Expense` object into a database row. The browser is then redirected back to `/`, and the controller loads the saved expenses for Thymeleaf to display.

`BigDecimal` stores money without the binary rounding surprises of `double`. The form and Java validation both check the input. `spring.jpa.hibernate.ddl-auto=update` lets Hibernate create/update the development table from the `Expense` class; for a deployed app, database migrations should replace this setting.
