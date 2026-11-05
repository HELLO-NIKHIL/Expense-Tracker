# Expense Tracker and Budget Planner

A Spring Boot web application for recording expenses, reviewing spending, setting monthly category budgets, and exporting expense records to Excel. The dashboard shows current-month and all-time totals, along with each category's spending, budget, remaining amount, and budget status.

## Features

- Add expenses with a name, category, amount, date, and optional note.
- View expenses newest first and delete individual records.
- See current-month and all-time spending totals.
- Set or update a monthly budget for a category and see whether spending is within the limit.
- Export all saved expenses as an `.xlsx` workbook.
- Validate expense and budget fields before saving. Amounts use `BigDecimal` and are limited to two decimal places.
- Persist expenses and budgets in PostgreSQL.

The available categories are Food, Transport, Shopping, Bills, Health, Entertainment, Education, and Other. Budgets apply to the current calendar month.

## Tech stack

- Java 27 and Spring Boot
- Spring MVC and Thymeleaf for the web interface
- Spring Data JPA and Hibernate for persistence
- PostgreSQL 17
- Apache POI for Excel workbook export
- Docker Compose for the local database

## Requirements

- JDK 27
- Maven
- Docker with Docker Compose

## Run locally

Run these commands from the `Expense-Tracker` project directory:

1. Start PostgreSQL:

   ```sh
   docker compose up -d
   ```

2. Start the Spring Boot application:

   ```sh
   mvn spring-boot:run
   ```

3. Open <http://localhost:8080>.

You can also open the project in IntelliJ, reload the Maven project, start the Compose database, and run `org.example.Main`.

The Compose service creates the `expense_tracker` database with the local development credentials `expense_user` / `expense_pass`. PostgreSQL data is stored in the named volume `expense_tracker_data`. Stop the database with `docker compose down`; keeping the volume preserves saved records.

## Database configuration

The application reads these environment variables, with the shown local defaults:

| Variable | Default |
| --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/expense_tracker` |
| `DB_USERNAME` | `expense_user` |
| `DB_PASSWORD` | `expense_pass` |

Hibernate uses `spring.jpa.hibernate.ddl-auto=update` to maintain the development schema from the JPA entities. For production, use managed secrets and database migrations.

## Main routes

| Method | Route | Purpose |
| --- | --- | --- |
| `GET` | `/` | Dashboard, expense list, totals, and category budgets |
| `POST` | `/expenses` | Add an expense |
| `POST` | `/expenses/{id}/delete` | Delete an expense |
| `POST` | `/budgets` | Save or update a current-month category budget |
| `GET` | `/expenses/export` | Download all expenses as `expenses.xlsx` |

## Project structure

```text
src/main/java/org/example/
  Main.java                 Spring Boot entry point
  Expense.java              Expense entity and field validation
  Budget.java               Monthly category budget entity
  CategorySummary.java       Category spending and budget summary
  ExpenseController.java    Web routes and dashboard model
  ExpenseService.java       Expense totals and budget operations
  ExpenseRepository.java    Expense database queries
  BudgetRepository.java     Budget database queries
  ExcelExportService.java   Excel workbook generation
src/main/resources/
  application.properties    Database and application settings
  templates/index.html      Thymeleaf dashboard
  static/styles.css         Responsive page styling
compose.yaml                Local PostgreSQL service
pom.xml                     Dependencies and build settings
```
