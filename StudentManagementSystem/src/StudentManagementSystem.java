import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class StudentManagementSystem extends JFrame {

    private JTextField nameField;
    private JTextField[] subjectFields = new JTextField[5];
    private JTextField[] internalMarks = new JTextField[5];
    private JTextField[] externalMarks = new JTextField[5];
    private JTextArea resultArea;

    // Frame size increased for a bigger structure
    private static final int FRAME_WIDTH = 1200; 
    private static final int FRAME_HEIGHT = 800; 

    public StudentManagementSystem() {

        setTitle("Student Grade Calculator | CodSoft");
        setSize(FRAME_WIDTH, FRAME_HEIGHT); // Structure made larger
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Using BorderLayout for the main frame only
        setLayout(new BorderLayout(15, 15)); 

        // ===== TITLE =====
        JLabel title = new JLabel("STUDENT GRADE CALCULATOR", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Font size increased
        title.setForeground(new Color(25, 118, 210));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // ===== MAIN PANEL (Uses BoxLayout for Top-Bottom arrangement) =====
        JPanel mainPanel = new JPanel();
        // BoxLayout for vertical stacking (Input Panel on Top, Result Panel at Bottom)
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20)); // Padding for main panel
        add(mainPanel, BorderLayout.CENTER);

        // --- 1. TOP PANEL (Input Details) ---
        JPanel topInputPanel = createInputPanel();
        topInputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400)); // Restrict height
        mainPanel.add(topInputPanel);

        // Separator space
        mainPanel.add(Box.createVerticalStrut(20));

        // --- 2. BOTTOM PANEL (Result Area) ---
        JPanel bottomResultPanel = createResultPanel();
        mainPanel.add(bottomResultPanel);

        // Actions
        // Find buttons within the Input Panel structure
        getButton(topInputPanel, "Calculate Result").addActionListener(this::calculateResult);
        getButton(topInputPanel, "Clear").addActionListener(e -> clearAll());
        getButton(topInputPanel, "Generate PDF").addActionListener(e -> generatePDF());
    }
    
    // Helper method to find a button by text
    private JButton getButton(Container parent, String text) {
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JPanel) {
                JButton found = getButton((JPanel) comp, text);
                if (found != null) return found;
            } else if (comp instanceof JButton && ((JButton) comp).getText().equals(text)) {
                return (JButton) comp;
            }
        }
        return null;
    }


    // ===== INPUT PANEL (TOP) CREATION =====
    private JPanel createInputPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBorder(new TitledBorder("Student Details"));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Increased padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Student name
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Student Full Name:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        nameField = new JTextField(25); // Increased text field size
        form.add(nameField, gbc);
        gbc.gridwidth = 1;

        // Headers
        gbc.gridy = 1;
        gbc.gridx = 0; form.add(new JLabel("Subject"), gbc);
        gbc.gridx = 1; form.add(new JLabel("Internal (30)"), gbc);
        gbc.gridx = 2; form.add(new JLabel("External (70)"), gbc);

        // Subject rows
        for (int i = 0; i < 5; i++) {
            gbc.gridy = i + 2;

            gbc.gridx = 0;
            subjectFields[i] = new JTextField(15);
            form.add(subjectFields[i], gbc);

            gbc.gridx = 1;
            internalMarks[i] = new JTextField(7);
            form.add(internalMarks[i], gbc);

            gbc.gridx = 2;
            externalMarks[i] = new JTextField(7);
            form.add(externalMarks[i], gbc);
        }

        leftPanel.add(form, BorderLayout.CENTER);

        // ===== BUTTONS (BELOW FORM) =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15)); // Increased button spacing

        JButton calcBtn = new JButton("Calculate Result");
        JButton pdfBtn = new JButton("Generate PDF");
        JButton clearBtn = new JButton("Clear");
        
        // Button style (unchanged from original)
        calcBtn.setBackground(new Color(76, 175, 80));
        calcBtn.setForeground(Color.WHITE);
        pdfBtn.setBackground(new Color(33, 150, 243));
        pdfBtn.setForeground(Color.WHITE);
        clearBtn.setBackground(new Color(244, 67, 54));
        clearBtn.setForeground(Color.WHITE);

        buttonPanel.add(calcBtn);
        buttonPanel.add(pdfBtn);
        buttonPanel.add(clearBtn);

        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return leftPanel;
    }

    // ===== RESULT PANEL (BOTTOM) CREATION =====
    private JPanel createResultPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new TitledBorder("Result / Report"));

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 16)); // Font size increased
        resultArea.setBackground(new Color(245, 245, 245));
        resultArea.setMargin(new Insets(15, 15, 15, 15)); // Increased margin

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(FRAME_WIDTH - 80, 250)); // Set preferred size for result area
        
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        
        return bottomPanel;
    }


    // ===== LOGIC (UNCHANGED) =====
    private void calculateResult(ActionEvent e) {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty() || !name.contains(" ")) {
                JOptionPane.showMessageDialog(this,
                        "Please enter FULL student name (First + Last)");
                return;
            }

            int total = 0;
            StringBuilder sb = new StringBuilder();
            sb.append("Student Name : ").append(name).append("\n\n");

            for (int i = 0; i < 5; i++) {
                String subject = subjectFields[i].getText().trim();
                if (subject.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter all subject names");
                    return;
                }

                int internal = Integer.parseInt(internalMarks[i].getText());
                int external = Integer.parseInt(externalMarks[i].getText());

                if (internal < 0 || internal > 30 || external < 0 || external > 70) {
                    JOptionPane.showMessageDialog(this,
                            "Marks out of range (Internal 30, External 70)");
                    return;
                }

                int subjectTotal = internal + external;
                total += subjectTotal;

                sb.append(subject)
                  .append(" : ")
                  .append(subjectTotal)
                  .append("/100\n");
            }

            double avg = total / 5.0;
            String grade, remark;

            if (avg >= 85) {
                grade = "A";
                remark = "Outstanding 🌟";
            } else if (avg >= 70) {
                grade = "B";
                remark = "Good 👍";
            } else if (avg >= 50) {
                grade = "C";
                remark = "Average 🙂";
            } else {
                grade = "D";
                remark = "Needs Improvement ❌";
            }

            sb.append("\nTotal Marks : ").append(total);
            sb.append("\nAverage     : ").append(String.format("%.2f", avg));
            sb.append("\nGrade       : ").append(grade);
            sb.append("\nRemark      : ").append(remark);

            resultArea.setText(sb.toString());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric marks");
        }
    }

    // ===== PDF (UNCHANGED) =====
    private void generatePDF() {
        if (resultArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please calculate result first");
            return;
        }

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(resultArea.getPrintable(null, null));

        if (job.printDialog()) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this,
                        "PDF generated successfully!");
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this,
                        "PDF generation failed");
            }
        }
    }

    private void clearAll() {
        nameField.setText("");
        resultArea.setText("");
        for (int i = 0; i < 5; i++) {
            subjectFields[i].setText("");
            internalMarks[i].setText("");
            externalMarks[i].setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new StudentManagementSystem().setVisible(true));
    }
}