import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Swing user interface for SpendWise - Personal Finance Manager. */
public class SpendWiseApp extends JFrame {
    private final TransactionManager manager = new TransactionManager();

    private final JTextField dateField = new JTextField(LocalDate.now().toString(), 10);
    private final JComboBox<Transaction.Type> typeBox = new JComboBox<>(Transaction.Type.values());
    private final JComboBox<String> categoryBox = new JComboBox<>(
            new String[]{"Food", "Rent", "Travel", "Shopping", "Bills", "Salary", "Other"});
    private final JTextField descField = new JTextField(12);
    private final JTextField amountField = new JTextField(8);
    private final JLabel summaryLabel = new JLabel();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Date", "Type", "Category", "Description", "Amount"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    public SpendWiseApp() {
        super("SpendWise - Personal Finance Manager");
        categoryBox.setEditable(true);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Date (yyyy-mm-dd):")); form.add(dateField);
        form.add(new JLabel("Type:")); form.add(typeBox);
        form.add(new JLabel("Category:")); form.add(categoryBox);
        form.add(new JLabel("Description:")); form.add(descField);
        form.add(new JLabel("Amount:")); form.add(amountField);

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton clearBtn = new JButton("Clear Form");
        JPanel buttons = new JPanel();
        buttons.add(addBtn); buttons.add(updateBtn); buttons.add(deleteBtn); buttons.add(clearBtn);

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER);
        north.add(buttons, BorderLayout.SOUTH);

        summaryLabel.setFont(summaryLabel.getFont().deriveFont(Font.BOLD, 14f));
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) fillFormFromSelection(); });

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(summaryLabel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addTransaction());
        updateBtn.addActionListener(e -> updateTransaction());
        deleteBtn.addActionListener(e -> deleteTransaction());
        clearBtn.addActionListener(e -> clearForm());

        refresh();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 480);
        setLocationRelativeTo(null);
    }

    private void addTransaction() {
        try {
            manager.add(readDate(), (Transaction.Type) typeBox.getSelectedItem(),
                    String.valueOf(categoryBox.getSelectedItem()), descField.getText(), readAmount());
            refresh(); clearForm();
        } catch (IllegalArgumentException ex) { error(ex.getMessage()); }
    }

    private void updateTransaction() {
        int id = selectedId();
        if (id < 0) { error("Select a row to update"); return; }
        try {
            manager.update(id, readDate(), (Transaction.Type) typeBox.getSelectedItem(),
                    String.valueOf(categoryBox.getSelectedItem()), descField.getText(), readAmount());
            refresh(); clearForm();
        } catch (IllegalArgumentException ex) { error(ex.getMessage()); }
    }

    private void deleteTransaction() {
        int id = selectedId();
        if (id < 0) { error("Select a row to delete"); return; }
        if (JOptionPane.showConfirmDialog(this, "Delete this transaction?", "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            manager.delete(id); refresh(); clearForm();
        }
    }

    private LocalDate readDate() {
        try { return LocalDate.parse(dateField.getText().trim()); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Date must be in yyyy-mm-dd format"); }
    }

    private double readAmount() {
        try { return Double.parseDouble(amountField.getText().trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Amount must be a number"); }
    }

    private int selectedId() {
        int row = table.getSelectedRow();
        return row < 0 ? -1 : (int) model.getValueAt(row, 0);
    }

    private void fillFormFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        dateField.setText(model.getValueAt(row, 1).toString());
        typeBox.setSelectedItem(model.getValueAt(row, 2));
        categoryBox.setSelectedItem(model.getValueAt(row, 3));
        descField.setText(model.getValueAt(row, 4).toString());
        amountField.setText(model.getValueAt(row, 5).toString());
    }

    private void clearForm() {
        dateField.setText(LocalDate.now().toString());
        descField.setText(""); amountField.setText("");
        table.clearSelection();
    }

    private void refresh() {
        model.setRowCount(0);
        for (Transaction t : manager.getAll())
            model.addRow(new Object[]{t.getId(), t.getDate(), t.getType(), t.getCategory(), t.getDescription(), t.getAmount()});
        summaryLabel.setText(String.format("Income: %.2f   |   Expenses: %.2f   |   Balance: %.2f",
                manager.totalIncome(), manager.totalExpense(), manager.balance()));
    }

    private void error(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SpendWiseApp().setVisible(true));
    }
}
