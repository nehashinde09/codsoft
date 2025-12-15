import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Represents a simple bank account
class BankAccount {
    private String accountHolder;
    private String accountNumber;
    private double balance;
    private List<String> transactionHistory;

    public BankAccount(String accountHolder, String accountNumber, double initialBalance) {
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        transactionHistory.add("Account created with initial balance ₹" + initialBalance);
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory.add("Deposited ₹" + amount);
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactionHistory.add("Withdrawn ₹" + amount);
        }
    }

    public double getBalance() {
        transactionHistory.add("Checked balance: ₹" + balance);
        return balance;
    }

    // Masked account number for privacy
    public String getMaskedAccountNumber() {
        if (accountNumber.length() > 5) {
            return accountNumber.substring(0, 3) + "****" + accountNumber.substring(accountNumber.length() - 2);
        }
        return "****"; // fallback if account number too short
    }

    public String getAccountDetails() {
        return "👤 Account Holder: " + accountHolder +
               "\n🏦 Account Number: " + getMaskedAccountNumber() +
               "\n💰 Current Balance: ₹" + balance;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }
}

// Swing based ATM UI
public class ATMInterface extends JFrame {
    private BankAccount account;
    private JTextArea outputArea;
    private JTextField amountField;
    private final String correctPin = "1234"; // demo PIN

    public ATMInterface() {
        // PIN Authentication at startup
        String pin = JOptionPane.showInputDialog(this, "Enter 4-digit PIN:");
        if (pin == null || pin.trim().isEmpty() || !correctPin.equals(pin)) {
            JOptionPane.showMessageDialog(this, "❌ Wrong or empty PIN! Access Denied.");
            System.exit(0);
        }

        account = new BankAccount("Neha Shinde", "COD12345", 5000);

        setTitle("CODSOFT ATM Interface");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout());

        amountField = new JTextField(10);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);

        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton balanceBtn = new JButton("Check Balance");
        JButton detailsBtn = new JButton("Account Details");
        JButton historyBtn = new JButton("Transaction History");

        inputPanel.add(depositBtn);
        inputPanel.add(withdrawBtn);
        inputPanel.add(balanceBtn);
        inputPanel.add(detailsBtn);
        inputPanel.add(historyBtn);

        add(inputPanel, BorderLayout.SOUTH);

        // Button actions
        depositBtn.addActionListener(e -> {
            try {
                double amt = Double.parseDouble(amountField.getText());
                if (amt <= 0) {
                    outputArea.append("❌ Invalid deposit amount!\n");
                } else {
                    account.deposit(amt);
                    outputArea.append("✅ Deposited ₹" + amt + "\n");
                }
            } catch (Exception ex) {
                outputArea.append("❌ Please enter a valid number!\n");
            }
        });

        withdrawBtn.addActionListener(e -> {
            try {
                double amt = Double.parseDouble(amountField.getText());
                if (amt <= 0) {
                    outputArea.append("❌ Invalid withdrawal amount!\n");
                } else if (amt > account.getBalance()) {
                    outputArea.append("❌ Insufficient balance!\n");
                } else {
                    account.withdraw(amt);
                    outputArea.append("✅ Withdrawn ₹" + amt + "\n");
                }
            } catch (Exception ex) {
                outputArea.append("❌ Please enter a valid number!\n");
            }
        });

        balanceBtn.addActionListener(e -> {
            outputArea.append("💰 Current Balance: ₹" + account.getBalance() + "\n");
        });

        detailsBtn.addActionListener(e -> {
            String pinCheck = JOptionPane.showInputDialog(this, "Enter PIN to view details:");
            if (pinCheck != null && correctPin.equals(pinCheck)) {
                outputArea.append(account.getAccountDetails() + "\n");
            } else {
                outputArea.append("❌ Access Denied!\n");
            }
        });

        historyBtn.addActionListener(e -> {
            String pinCheck = JOptionPane.showInputDialog(this, "Enter PIN to view history:");
            if (pinCheck != null && correctPin.equals(pinCheck)) {
                outputArea.append("\n📜 Transaction History:\n");
                for (String record : account.getTransactionHistory()) {
                    outputArea.append("- " + record + "\n");
                }
            } else {
                outputArea.append("❌ Access Denied!\n");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMInterface().setVisible(true));
    }
}
