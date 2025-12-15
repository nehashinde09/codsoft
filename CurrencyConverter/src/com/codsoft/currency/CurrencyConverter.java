package com.codsoft.currency;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * CODSOFT Currency Converter Application.
 * Combines Model, View, and Controller functionalities for a robust, feature-rich solution.
 */
public class CurrencyConverter extends JFrame {
    
    // --- UI Components (View) ---
    private final JTextField amountField;
    private final JComboBox<String> fromCurrency;
    private final JComboBox<String> toCurrency;
    private final JLabel resultLabel;
    private final JButton convertBtn;
    private final JButton swapBtn; 
    
    // --- Data (Model) ---
    private final CurrencyModel model;
    
    // --- Constructor (View Initialization and Controller Setup) ---
    public CurrencyConverter() {
        this.model = new CurrencyModel(); 

        // --- Frame Settings ---
        setTitle("Currency Converter");
        setSize(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout()); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Expanded currency options
        String[] currencyOptions = {"USD", "EUR", "INR", "GBP", "JPY"}; 

        // --- UI Component Creation ---
        
        // 1. Amount Field
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 3; 
        amountField = new JTextField(10);
        add(amountField, gbc);
        gbc.gridwidth = 1; // reset

        // 2. From Currency
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("From:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        fromCurrency = new JComboBox<>(currencyOptions);
        add(fromCurrency, gbc);
        
        // 3. Swap Button 
        gbc.gridx = 2; gbc.gridy = 1;
        swapBtn = new JButton("Swap 🔄");
        swapBtn.setBackground(new Color(255, 140, 0)); 
        swapBtn.setForeground(Color.WHITE);
        add(swapBtn, gbc);

        // 4. To Currency
        gbc.gridx = 3; gbc.gridy = 1;
        toCurrency = new JComboBox<>(currencyOptions);
        add(toCurrency, gbc);
        
        // 5. Convert Button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2; 
        convertBtn = new JButton("Convert");
        convertBtn.setBackground(new Color(0, 102, 204));
        convertBtn.setForeground(Color.WHITE);
        add(convertBtn, gbc);
        
        // 6. Result Label
        gbc.gridx = 2; gbc.gridy = 2;
        gbc.gridwidth = 2; 
        resultLabel = new JLabel("Result: ");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultLabel.setForeground(new Color(34, 139, 34)); 
        add(resultLabel, gbc);

        // --- Controller Setup ---
        
        // Convert Button Action Listener
        convertBtn.addActionListener(new Controller());
        
        // Swap Button Action Listener
        swapBtn.addActionListener(e -> {
            String from = fromCurrency.getSelectedItem().toString();
            String to = toCurrency.getSelectedItem().toString();
            
            // Swap the selections
            fromCurrency.setSelectedItem(to);
            toCurrency.setSelectedItem(from);
            
            System.out.println("[Console] Currencies swapped.");
        });

        // --- Finalize Frame ---
        setLocationRelativeTo(null); 
        setVisible(true);
        System.out.println("[Console] Currency Converter UI initialized.");
    }

    // --- 1. Model Class (Data & Logic) ---
    /**
     * Handles currency rates and conversion logic (Model component).
     */
    private class CurrencyModel {
        private final Map<String, Double> rates;

        public CurrencyModel() {
            rates = new HashMap<>();
            
            // Define base rates (These are hard-coded and not real-time, but sufficient for the task)
            double usdToInr = 83.50; 
            double eurToInr = 90.00; 
            double gbpToInr = 105.50;
            double usdToJpy = 150.00;
            
            // Base Rates (INR-based for cross-rate calculation)
            rates.put("USD_INR", usdToInr);
            rates.put("EUR_INR", eurToInr);
            rates.put("GBP_INR", gbpToInr);

            // Calculate JPY/INR
            double jpyToInr = usdToInr / usdToJpy;
            rates.put("JPY_INR", jpyToInr); 

            // Reverse Rates 
            rates.put("INR_USD", 1.0 / usdToInr);
            rates.put("INR_EUR", 1.0 / eurToInr);
            rates.put("INR_GBP", 1.0 / gbpToInr);
            rates.put("INR_JPY", 1.0 / jpyToInr);

            // Cross Rates
            rates.put("USD_EUR", usdToInr / eurToInr); 
            rates.put("EUR_USD", eurToInr / usdToInr); 
            rates.put("USD_JPY", usdToJpy);
            rates.put("JPY_USD", 1.0 / usdToJpy); 
            rates.put("EUR_GBP", eurToInr / gbpToInr);
            rates.put("GBP_EUR", gbpToInr / eurToInr);
            
            // Same currency rates
            rates.put("USD_USD", 1.0);
            rates.put("EUR_EUR", 1.0);
            rates.put("INR_INR", 1.0);
            rates.put("GBP_GBP", 1.0);
            rates.put("JPY_JPY", 1.0);
        }

        /** Performs the conversion based on stored rates. */
        public Double convert(double amount, String fromCurrency, String toCurrency) {
            String key = fromCurrency + "_" + toCurrency;
            
            if (rates.containsKey(key)) {
                double rate = rates.get(key);
                double converted = amount * rate;
                
                // Console output for conversion details
                System.out.printf("[Console] Conversion: %.2f %s = %.2f %s%n", amount, fromCurrency, converted, toCurrency);
                
                return converted;
            } else {
                // Handle unsupported cross rates not explicitly defined
                System.out.println("[Console] Unsupported conversion: " + key);
                return null;
            }
        }
    }

    // --- 2. Controller Class (Input Handler) ---
    /**
     * Handles user input and mediates between the Model and View (Controller component).
     */
    private class Controller implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // 1. Get input and check for empty field
                String amountStr = amountField.getText().trim();
                if (amountStr.isEmpty()) {
                    setResultText("Please enter an amount.");
                    return;
                }
                
                double amount = Double.parseDouble(amountStr);
                
                // Check for negative amount
                if (amount < 0) {
                    setResultText("Amount cannot be negative!");
                    System.err.println("[Console] Error: Amount is negative.");
                    return;
                }
                
                String from = fromCurrency.getSelectedItem().toString();
                String to = toCurrency.getSelectedItem().toString();

                // 2. Perform conversion using the Model
                Double convertedAmount = model.convert(amount, from, to);

                // 3. Update the View with the result
                if (convertedAmount != null) {
                    String resultText = String.format("%.2f %s", convertedAmount, to);
                    setResultText(resultText);
                    
                    // --- Improvement: Auto-clear amount field after successful conversion ---
                    amountField.setText("");
                    // -----------------------------------------------------------------------

                } else {
                    setResultText("Unsupported conversion.");
                }
            } catch (NumberFormatException ex) {
                // Handle invalid input
                setResultText("Invalid amount!");
                System.err.println("[Console] Error: Invalid amount entered.");
            }
        }
    }

    // --- 3. View Helper Method ---
    /**
     * Updates the result label and provides console feedback.
     */
    private void setResultText(String text) {
        resultLabel.setText("Result: " + text);
        System.out.println("[Console] UI result displayed: " + text);
    }


    // --- 4. Main Method ---
    public static void main(String[] args) {
        System.out.println("[Console] Application starting...");
        // Ensure GUI is initialized on the Event Dispatch Thread
        SwingUtilities.invokeLater(CurrencyConverter::new);
    }
}