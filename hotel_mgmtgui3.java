import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

public class hotel_mgmtgui3 extends JFrame implements ActionListener {

    JComboBox<String> itemBox;
    JTextField quantityField;
    JTextArea billArea;
    JButton orderBtn, clearBtn, exitBtn, printBtn;
    JTextField nameField;

    int total = 0;
    int dosaQty = 0, idliQty = 0, vadaQty = 0, puriQty = 0, chapQty = 0;

    public hotel_mgmtgui3() {
        setTitle("Hotel Management System");
        setSize(500, 500);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("Welcome to Hotel!");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(150, 10, 300, 30);
        add(title);

        JLabel itemLabel = new JLabel("Select Item:");
        itemLabel.setBounds(50, 60, 100, 30);
        add(itemLabel);

        String[] items = {"Dosa - Rs.100", "Idli - Rs.50", "Vada - Rs.30", "Puri - Rs.80", "Chappathi - Rs.40"};
        itemBox = new JComboBox<>(items);
        itemBox.setBounds(150, 60, 200, 30);
        add(itemBox);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(50, 100, 100, 30);
        add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(150, 100, 200, 30);
        add(quantityField);

        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setBounds(50, 140, 120, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(170, 140, 180, 30);
        add(nameField);

        orderBtn = new JButton("Add to Bill");
        orderBtn.setBounds(50, 180, 120, 30);
        orderBtn.addActionListener(this);
        add(orderBtn);

        clearBtn = new JButton("Clear Bill");
        clearBtn.setBounds(180, 180, 100, 30);
        clearBtn.addActionListener(this);
        add(clearBtn);

        exitBtn = new JButton("Exit");
        exitBtn.setBounds(290, 180, 100, 30);
        exitBtn.addActionListener(this);
        add(exitBtn);

        billArea = new JTextArea();
        billArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(billArea);
        scroll.setBounds(50, 220, 380, 180);
        add(scroll);

        printBtn = new JButton("Print Bill");
        printBtn.setBounds(180, 420, 120, 30);
        printBtn.addActionListener(this);
        add(printBtn);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == orderBtn) {
            int qty;
            try {
                qty = Integer.parseInt(quantityField.getText());
                if (qty <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity should be more than zero.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid quantity.");
                return;
            }

            int index = itemBox.getSelectedIndex();

            switch (index) {
                case 0:
                    dosaQty += qty;
                    total += qty * 100;
                    break;
                case 1:
                    idliQty += qty;
                    total += qty * 50;
                    break;
                case 2:
                    vadaQty += qty;
                    total += qty * 30;
                    break;
                case 3:
                    puriQty += qty;
                    total += qty * 80;
                    break;
                case 4:
                    chapQty += qty;
                    total += qty * 40;
                    break;
            }

            updateBill();
            quantityField.setText("");

        } else if (e.getSource() == clearBtn) {
            // Reset everything
            total = 0;
            dosaQty = idliQty = vadaQty = puriQty = chapQty = 0;
            billArea.setText("");
            nameField.setText("");
        } else if (e.getSource() == exitBtn) {
            System.exit(0);
        } else if (e.getSource() == printBtn) {
            // Automatically use the customer's name (if provided) or "Guest" if not.
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                name = "Guest";
            }

            // Prepare the bill content for PDF
            StringBuilder sb = new StringBuilder();
            sb.append("*************** BILL ***************\n");

            sb.append("Customer: ").append(name).append("\n");

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            sb.append("Date/Time: ").append(now.format(formatter)).append("\n");
            sb.append("--------------------------------\n");

            if (dosaQty > 0) sb.append("Dosa\tx").append(dosaQty).append("\t= Rs.").append(dosaQty * 100).append("\n");
            if (idliQty > 0) sb.append("Idli\tx").append(idliQty).append("\t= Rs.").append(idliQty * 50).append("\n");
            if (vadaQty > 0) sb.append("Vada\tx").append(vadaQty).append("\t= Rs.").append(vadaQty * 30).append("\n");
            if (puriQty > 0) sb.append("Puri\tx").append(puriQty).append("\t= Rs.").append(puriQty * 80).append("\n");
            if (chapQty > 0) sb.append("Chappathi\tx").append(chapQty).append("\t= Rs.").append(chapQty * 40).append("\n");

            sb.append("--------------------------------\n");
            sb.append("\t\tTotal = Rs.").append(total).append("\n");
            sb.append("********************************");

            try {
                // Create PDF
                Document document = new Document();
                // Output PDF file path
                String outputPath = "bill.pdf";
                PdfWriter.getInstance(document, new FileOutputStream(outputPath));
                document.open();

                // Add content to the PDF
                document.add(new Paragraph(sb.toString()));

                document.close();

                // Notify the user
                JOptionPane.showMessageDialog(this, "Bill saved as PDF successfully!");

                // Automatically open the generated PDF (optional)
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(new File(outputPath));
                }

            } catch (DocumentException | IOException ex) {
                JOptionPane.showMessageDialog(this, "Error generating PDF: " + ex.getMessage());
            }
        }
    }

    void updateBill() {
        StringBuilder sb = new StringBuilder();
        sb.append("*************** BILL ***************\n");

        String name = nameField.getText().trim();
        if (!name.isEmpty()) {
            sb.append("Customer: ").append(name).append("\n");
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        sb.append("Date/Time: ").append(now.format(formatter)).append("\n");
        sb.append("--------------------------------\n");

        if (dosaQty > 0) sb.append("Dosa\tx").append(dosaQty).append("\t= Rs.").append(dosaQty * 100).append("\n");
        if (idliQty > 0) sb.append("Idli\tx").append(idliQty).append("\t= Rs.").append(idliQty * 50).append("\n");
        if (vadaQty > 0) sb.append("Vada\tx").append(vadaQty).append("\t= Rs.").append(vadaQty * 30).append("\n");
        if (puriQty > 0) sb.append("Puri\tx").append(puriQty).append("\t= Rs.").append(puriQty * 80).append("\n");
        if (chapQty > 0) sb.append("Chappathi\tx").append(chapQty).append("\t= Rs.").append(chapQty * 40).append("\n");

        sb.append("--------------------------------\n");
        sb.append("\t\tTotal = Rs.").append(total).append("\n");
        sb.append("********************************");

        billArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        new hotel_mgmtgui3();
    }
}
