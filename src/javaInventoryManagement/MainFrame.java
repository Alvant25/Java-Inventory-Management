package javaInventoryManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainFrame extends JFrame {
    private InventoryManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, priceField, stockField;

    public MainFrame() {
        // Initialize the controller
        manager = new InventoryManager();
        
        // Set up the main window
        setTitle("Inventory Management System");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set up the Table to display data
        String[] columns = {"ID", "Name", "Price", "Stock"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Set up the Input Panel at the bottom (7 rows, 2 columns for vertical layout)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2, 5, 5));

        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Stock:"));
        stockField = new JTextField();
        inputPanel.add(stockField);

        // Add Product Button
        JButton addButton = new JButton("Add Product");
        inputPanel.add(new JLabel("")); 
        inputPanel.add(addButton);

        // Delete Selected Product Button
        JButton deleteButton = new JButton("Delete Selected");
        inputPanel.add(new JLabel("")); 
        inputPanel.add(deleteButton);

        // Manual Save Button to explicitly trigger CSV persistence
        JButton saveButton = new JButton("Save to CSV");
        inputPanel.add(new JLabel("")); 
        inputPanel.add(saveButton);

        add(inputPanel, BorderLayout.SOUTH);

        // Action listener for adding a product
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        // Action listener for deleting a selected product from table
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedProduct();
            }
        });

        // Action listener for manual saving
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    manager.saveData();
                    JOptionPane.showMessageDialog(MainFrame.this, "Data successfully saved to inventory.csv!", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
                } catch (DataSaveException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Load existing data into the table on startup
        refreshTable(); 
    }

    private void addProduct() {
        try {
            // Validation: Check if any field is empty
            if (idField.getText().trim().isEmpty() || 
                nameField.getText().trim().isEmpty() || 
                priceField.getText().trim().isEmpty() || 
                stockField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this, "All fields must be filled out!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Read and parse inputs
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());

            // Validation: Check for negative values
            if (price < 0 || stock < 0) {
                JOptionPane.showMessageDialog(this, "Price and Stock cannot be negative numbers.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create model and add via controller (handles duplicate IDs)
            Product p = new Product(id, name, price, stock);
            manager.addProduct(p); 
            
            // Update the visual table
            refreshTable();
            
            // Clear input fields
            idField.setText("");
            nameField.setText("");
            priceField.setText("");
            stockField.setText("");
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be a valid number and Stock must be an integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Duplicate Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product from the table to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the ID of the selected product from the table model
        String idToDelete = tableModel.getValueAt(selectedRow, 0).toString();
        
        // Remove from manager list and refresh table
        manager.removeProduct(idToDelete);
        refreshTable();
        JOptionPane.showMessageDialog(this, "Product removed from memory. Remember to click 'Save to CSV' to update disk.", "Product Removed", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // Wipe current table rows
        List<Product> products = manager.getProducts();
        for (Product p : products) {
            Object[] row = {p.getId(), p.getName(), p.getPrice(), p.getStockQuantity()};
            tableModel.addRow(row);
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}