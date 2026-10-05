package javaInventoryManagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<Product> products;
    private final String FILE_PATH = "inventory.csv";

    public InventoryManager() {
        products = new ArrayList<>();
        loadData(); // Load existing data from disk on startup
    }

    // Adds a product to memory after checking for duplicate IDs
    public void addProduct(Product product) throws IllegalArgumentException {
        for (Product p : products) {
            if (p.getId().equalsIgnoreCase(product.getId())) {
                throw new IllegalArgumentException("A product with ID '" + product.getId() + "' already exists!");
            }
        }
        products.add(product);
    }

    // Removes a product by its unique ID
    public void removeProduct(String id) {
        products.removeIf(p -> p.getId().equalsIgnoreCase(id));
    }

    public List<Product> getProducts() {
        return products;
    }

    // Explicit manual save to CSV file
    public void saveData() throws DataSaveException {
        try (FileWriter writer = new FileWriter(FILE_PATH, false)) {
            for (Product p : products) {
                writer.write(p.toCSVString() + "\n");
            }
        } catch (IOException e) {
            throw new DataSaveException("Error writing to text file: " + e.getMessage());
        }
    }

    // Load saved products from CSV on startup
    private void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; 
                
                String[] data = line.split(",");
                if (data.length >= 4) {
                    String id = data[0].trim();
                    String name = data[1].trim();
                    double price = Double.parseDouble(data[2].trim());
                    int stock = Integer.parseInt(data[3].trim());
                    
                    Product p = new Product(id, name, price, stock);
                    products.add(p);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading the inventory file: " + e.getMessage());
        }
    }
}