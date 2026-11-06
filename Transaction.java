import java.time.LocalDate;

/** A single income or expense record. */
public class Transaction {
    public enum Type { INCOME, EXPENSE }

    private final int id;
    private LocalDate date;
    private Type type;
    private String category;
    private String description;
    private double amount;

    public Transaction(int id, LocalDate date, Type type, String category, String description, double amount) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.category = category;
        this.description = description;
        this.amount = amount;
    }

    public int getId() { return id; }
    public LocalDate getDate() { return date; }
    public Type getType() { return type; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }

    public void setDate(LocalDate date) { this.date = date; }
    public void setType(Type type) { this.type = type; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setAmount(double amount) { this.amount = amount; }
}
