# SpendWise - Personal Finance Manager

A standalone Java Swing desktop application to track income and expenses with full CRUD support.

## Features
- Create: add income/expense transactions (date, type, category, description, amount)
- Read: view all transactions in a table with live income, expense and balance totals
- Update: select a row, edit the form, click "Update Selected"
- Delete: select a row and click "Delete Selected"
- Input validation (date format, positive amount, required category)

## Project Structure
| File | Purpose |
|------|---------|
| `src/Transaction.java` | Data model for one transaction |
| `src/TransactionManager.java` | Business logic (CRUD + summaries), separate from UI |
| `src/SpendWiseApp.java` | Swing user interface (main class) |
| `src/TestCases.java` | Two sample test cases |

## How to Run
```
cd src
javac *.java
java SpendWiseApp
```
Requires JDK 11 or later.

## Sample Test Cases
**Test Case 1 - Totals:** add Income 5000 (Salary), Expense 1200 (Food), Expense 300 (Travel).
Expected: Income 5000, Expenses 1500, Balance 3500.

**Test Case 2 - Update, Delete, Validation:** add Expense 800 (Food); update amount to 500 (amount becomes 500);
delete it (list becomes empty); try adding amount -50 (rejected with "Amount must be greater than zero").

Run automated checks: `java TestCases`

## Team Members
| Name      | Roll No.   |                               Role                                               |
|-----------|------------|----------------------------------------------------------------------------------|
|M.Varshini | 25WH1A05E7 |   SpendWiseApp.java (the UI). Understand how the form, table and buttons work.   |
|D.Haasini  | 25WH1A05F2 | TransactionManager.java and Transaction.java (the logic). Understand how add,    |
|           |            |  update and delete work.                                                         |
|Vaishnavi.C| 25WH1A05H0 | TestCases.java. Run the tests, write up the two test cases and take screenshots. |
|           |            | GitHub repository, README, and printing the hard copy.                           |

## Future Scope
Save/load data to file, monthly budgets, charts by category.
