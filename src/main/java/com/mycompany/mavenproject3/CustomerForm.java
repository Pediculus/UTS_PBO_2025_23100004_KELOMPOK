/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author HAK_PHENG
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class CustomerForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField phoneField;
    private JButton saveButton;
    private JButton removeButton;
    private JButton editButton;
    private int idCounter = 0; 

    private List<Product> products;
    private List<Customer> customers;
    private Mavenproject3 mainWindow; //for main
    private SoldForm soldform;
    private TransactionForm transactionform;

    // Accept product list and reference to main window
    public CustomerForm(List<Product> products, List<Customer> customers, Mavenproject3 mainWindow) {
        this.products = products;
        this.customers = customers;
        this.mainWindow = mainWindow; // also for main
        

        setTitle("Form Customer");
        setSize(600, 450);
        setLocationRelativeTo(null);

        String[] columnNames = {"Id", "Nama", "Phone"};
        tableModel = new DefaultTableModel(columnNames, 0);
        drinkTable = new JTable(tableModel);

        JPanel formPanel = new JPanel();

        formPanel.add(new JLabel("Nama Customer: "));
        nameField = new JTextField(10);
        formPanel.add(nameField);

        formPanel.add(new JLabel("No.Telp :"));
        phoneField = new JTextField(10);
        formPanel.add(phoneField);

        saveButton = new JButton("Simpan");
        removeButton = new JButton("Hapus");
        editButton = new JButton("Edit");
        formPanel.add(saveButton);
        formPanel.add(removeButton);
        formPanel.add(editButton);

        // Table selection listener
        drinkTable.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                
                nameField.setText(drinkTable.getValueAt(selectedRow, 1).toString());
                phoneField.setText(drinkTable.getValueAt(selectedRow, 2).toString());
            }
        });

        // Add product
        saveButton.addActionListener(e -> {
            try {
                int phone = Integer.parseInt(phoneField.getText());
                String name = nameField.getText();
                

                Customer customer = new Customer(idCounter++, name, phone);
                customers.add(customer);
                tableModel.addRow(new Object[]{idCounter, name, phone});
                nameField.setText("");
                phoneField.setText("");
                transactionform.refreshBox();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "No.Telp harus berupa angka!");
            }
        });

        // Remove product
        removeButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                customers.remove(selectedRow);
                tableModel.removeRow(selectedRow);
                nameField.setText("");
                phoneField.setText("");
                transactionform.refreshBox();
            } else {
                JOptionPane.showMessageDialog(this, "Pilih produk yang ingin dihapus!");
            }
        });

        // Edit product
        editButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    int newPhone = Integer.parseInt(phoneField.getText());
                    String newName = nameField.getText();

                    Customer selectedProduct = customers.get(selectedRow);
                    selectedProduct.setPhone(newPhone);
                    selectedProduct.setName(newName);

                    // Update table row
                    tableModel.setValueAt(newPhone, selectedRow, 2);
                    tableModel.setValueAt(newName, selectedRow, 1);


                    phoneField.setText("");
                    nameField.setText("");
                    transactionform.refreshBox();
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "No.Telp harus berupa angka!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih produk yang ingin diubah!");
            }
        });

        

        add(new JScrollPane(drinkTable), BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);

        loadProductData(); // Load products into table at start
    }
    
    public void setTransactionForm(TransactionForm transactionform){
        this.transactionform = transactionform;
    }

    private void loadProductData() {
        for (Customer c : customers) {
            tableModel.addRow(new Object[]{
                c.getId(), c.getName(), c.getPhone()
            });
        }
    }
}
