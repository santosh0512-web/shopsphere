
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class ShopSphereAdmin extends JFrame {

    private final DefaultTableModel model;

    public ShopSphereAdmin() {
        setTitle("ShopSphere - Admin Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel header = new JPanel(new BorderLayout());
        header.add(new JLabel("  ShopSphere Admin Dashboard"),
                   BorderLayout.WEST);

        JButton addButton = new JButton("Add Product");
        JButton deleteButton = new JButton("Delete Product");

        JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(deleteButton);
        header.add(buttons, BorderLayout.EAST);

        String[] columns = {"ID", "Product Name", "Price", "Stock"};

        model = new DefaultTableModel(columns, 0);
        model.addRow(new Object[]{1, "Laptop", 55000.0, 10});
        model.addRow(new Object[]{2, "Keyboard", 1200.0, 25});

        JTable table = new JTable(model);

        addButton.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField price = new JTextField();
            JTextField stock = new JTextField();

            Object[] fields = {
                "Product Name:", name,
                "Price:", price,
                "Stock:", stock
            };

            int result = JOptionPane.showConfirmDialog(
                this, fields, "Add Product",
                JOptionPane.OK_CANCEL_OPTION
            );

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String productName = name.getText().trim();
                    double productPrice = Double.parseDouble(price.getText());
                    int productStock = Integer.parseInt(stock.getText());

                    if (productName.isEmpty()
                            || productPrice < 0 || productStock < 0) {
                        throw new IllegalArgumentException();
                    }

                    model.addRow(new Object[]{
                        model.getRowCount() + 1,
                        productName,
                        productPrice,
                        productStock
                    });

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Enter a valid name, price and stock.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int selected = table.getSelectedRow();

            if (selected >= 0) {
                model.removeRow(selected);
            } else {
                JOptionPane.showMessageDialog(
                    this, "Please select a product first."
                );
            }
        });

        add(header, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
                // Use the default Swing look and feel.
            }

            new ShopSphereAdmin().setVisible(true);
        });
    }
}
