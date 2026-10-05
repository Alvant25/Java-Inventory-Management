# Java Inventory Management System

A desktop inventory management application built in **Java** utilizing **Java Swing** for the graphical user interface and custom text file I/O streams for **CSV data persistence**. 

This project was developed as an academic and portfolio project focusing on **Object-Oriented Programming (OOP)** principles, input validation, and clean architecture patterns (MVC).

---

## Features

- **Graphical User Interface (GUI):** Built with Java Swing (`JFrame`, `JTable`, `GridLayout`) providing a clean layout for interaction.
- **Data Persistence:** Manual save functionality that writes structured records to an `inventory.csv` file, and automatically loads them back into memory on startup.
- **Robust Input Validation:** 
  - Prevents empty fields and negative values for prices and stock quantities.
  - Catches invalid number formats using custom exception handling.
  - Validates and blocks duplicate product IDs.
- **CRUD Operations:** Supports adding products, viewing them in a dynamic table, removing selected records, and updating local storage.
- **Custom Exceptions:** Implements custom exception classes (`DataSaveException`) for decoupled file I/O error management.

---

## Project Structure

The project follows a clean object-oriented package structure (`javaInventoryManagement`):
- `MainFrame.java`: The View layer responsible for rendering the Swing GUI, handling user actions, and displaying data tables.
- `InventoryManager.java`: The Controller / Business Logic layer that manages the in-memory product list, ID uniqueness checks, and CSV file reading/writing.
- `Product.java`: The Model layer representing product entities (`id`, `name`, `price`, `stockQuantity`) and CSV string serialization.
- `DataSaveException.java`: Custom exception for handling file output errors cleanly.

---

## Getting Started

### Prerequisites
- **Java Development Kit (JDK 8 or higher)** installed on your machine.
- An IDE compatible with Java projects (such as **Eclipse IDE**, IntelliJ IDEA, or NetBeans).

### Installation & Execution
1. Clone the repository or download the source code:
   ```bash
   git clone [https://github.com/Alvant25/Java-Inventory-Management.git](https://github.com/Alvant25/Java-Inventory-Management.git)
