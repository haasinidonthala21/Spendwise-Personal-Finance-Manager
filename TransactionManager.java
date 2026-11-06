import java.time.LocalDate;
import java.util.*;

/** Business logic for SpendWise: CRUD operations and summaries. No UI code here. */
public class TransactionManager {
    private final List<Transaction> transactions = new ArrayList<>();
    private int nextId = 1;

    // CREATE
    public Transaction add(LocalDate date, Transaction.Type type, String category, String description, double amount) {
        validate(date, category, amount);
        Transaction t = new Transaction(nextId++, date, type, category.trim(), description == null ? "" : description.trim(), amount);
        transactions.add(t);
        return t;
    }

    // READ
    public List<Transaction> getAll() { return new ArrayList<>(transactions); }

    public Transaction findById(int id) {
        for (Transaction t : transactions) if (t.getId() == id) return t;
        return null;
    }

    public List<Transaction> filterByCategory(String category) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions)
            if (t.getCategory().equalsIgnoreCase(category)) result.add(t);
        return result;
    }

    // UPDATE
    public boolean update(int id, LocalDate date, Transaction.Type type, String category, String description, double amount) {
        validate(date, category, amount);
        Transaction t = findById(id);
        if (t == null) return false;
        t.setDate(date);
        t.setType(type);
        t.setCategory(category.trim());
        t.setDescription(description == null ? "" : description.trim());
        t.setAmount(amount);
        return true;
    }

    // DELETE
    public boolean delete(int id) {
        return transactions.removeIf(t -> t.getId() == id);
    }

    // SUMMARIES
    public double totalIncome() { return total(Transaction.Type.INCOME); }
    public double totalExpense() { return total(Transaction.Type.EXPENSE); }
    public double balance() { return totalIncome() - totalExpense(); }

    public Map<String, Double> expenseByCategory() {
        Map<String, Double> map = new TreeMap<>();
        for (Transaction t : transactions)
            if (t.getType() == Transaction.Type.EXPENSE) map.merge(t.getCategory(), t.getAmount(), Double::sum);
        return map;
    }

    private double total(Transaction.Type type) {
        double sum = 0;
        for (Transaction t : transactions) if (t.getType() == type) sum += t.getAmount();
        return sum;
    }

    private void validate(LocalDate date, String category, double amount) {
        if (date == null) throw new IllegalArgumentException("Date is required");
        if (category == null || category.trim().isEmpty()) throw new IllegalArgumentException("Category is required");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero");
    }
}
