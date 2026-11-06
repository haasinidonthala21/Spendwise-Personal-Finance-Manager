import java.time.LocalDate;

/** Two sample test cases for TransactionManager. Run: java TestCases */
public class TestCases {
    static int failed = 0;

    static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS: " : "FAIL: ") + name);
        if (!ok) failed++;
    }

    public static void main(String[] args) {
        // Test Case 1: Add transactions and verify totals
        TransactionManager m1 = new TransactionManager();
        m1.add(LocalDate.of(2026, 11, 1), Transaction.Type.INCOME, "Salary", "November salary", 5000);
        m1.add(LocalDate.of(2026, 11, 2), Transaction.Type.EXPENSE, "Food", "Groceries", 1200);
        m1.add(LocalDate.of(2026, 11, 3), Transaction.Type.EXPENSE, "Travel", "Metro pass", 300);
        check("TC1: total income is 5000", m1.totalIncome() == 5000);
        check("TC1: total expense is 1500", m1.totalExpense() == 1500);
        check("TC1: balance is 3500", m1.balance() == 3500);

        // Test Case 2: Update, delete and invalid input
        TransactionManager m2 = new TransactionManager();
        Transaction t = m2.add(LocalDate.of(2026, 11, 5), Transaction.Type.EXPENSE, "Food", "Dinner", 800);
        m2.update(t.getId(), t.getDate(), Transaction.Type.EXPENSE, "Food", "Dinner", 500);
        check("TC2: update changes amount to 500", m2.findById(t.getId()).getAmount() == 500);
        check("TC2: delete removes the record", m2.delete(t.getId()) && m2.getAll().isEmpty());
        boolean threw = false;
        try { m2.add(LocalDate.now(), Transaction.Type.EXPENSE, "Food", "Bad", -50); }
        catch (IllegalArgumentException e) { threw = true; }
        check("TC2: negative amount is rejected", threw);

        System.out.println(failed == 0 ? "All tests passed." : failed + " test(s) failed.");
    }
}
