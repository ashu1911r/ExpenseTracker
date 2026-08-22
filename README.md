# Personal Expense Tracker

A full Spring Boot + Thymeleaf + PostgreSQL expense tracker.

## Features
- Dashboard for a selected month
- Add, edit and delete expenses
- Categories and category totals
- Monthly budgets
- Remaining budget calculation
- PostgreSQL persistence
- Server-side validation
- Responsive UI

## Requirements
- Java 21
- Maven 3.9+
- PostgreSQL 14+ (or a recent compatible version)

## 1. Create the database
Open PostgreSQL / pgAdmin and create a database named `expense_tracker`.

Or, from psql:
```sql
CREATE DATABASE expense_tracker;
```

Do not run `CREATE DATABASE` while already connected to `expense_tracker`.

## 2. Configure credentials
The default settings are:
- URL: jdbc:postgresql://localhost:5432/expense_tracker
- username: postgres
- password: postgres

If your PostgreSQL password is different, set an environment variable before running:
Windows PowerShell:
```powershell
$env:DB_PASSWORD="YOUR_POSTGRES_PASSWORD"
```

You can also change `src/main/resources/application.properties`.

## 3. Run
From the project folder:
```bash
mvn spring-boot:run
```

Or build a jar:
```bash
mvn clean package
java -jar target/personal-expense-tracker-1.0.0.jar
```

Then open:
http://localhost:8080

## 4. VS Code
Open the project folder in VS Code. Make sure Java Extension Pack is installed. Then use the terminal:
```bash
mvn spring-boot:run
```

## Notes
JPA uses `ddl-auto=update`, so the `expense` and `monthly_budget` tables are created automatically after the application connects to PostgreSQL.
