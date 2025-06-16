/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HAK_PHENG
 */
public class TransactionForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JTextField qtyField;
    private JComboBox<String> categoryField;
    private JComboBox<String> customerField;
    private JTextField priceField;
    private JTextField stockField;
    private JButton saveButton;
    private JButton removeButton;
    private JButton editButton;
    private JButton refreshBannerButton; // refresh banner
    private int idCounter = 3; 

    private List<Product> products;
    private List<Sold> sold;
    private List<Customer> customer;
    private List<Transaction> transaction;
    private Mavenproject3 mainWindow; //for main
    private ProductForm productform;
    private CustomerForm customerform;

    // Accept product list and reference to main window
    public TransactionForm(List<Product> products, List<Sold> sold, Mavenproject3 mainWindow, List<Customer> customer, List<Transaction> transaction) {
        this.products = products;
        this.sold = sold;
        this.mainWindow = mainWindow; // also for main
        this.customer = customer;

        setTitle("Form Transaksi");
        setSize(600, 450);
        setLocationRelativeTo(null);

        String[] columnNames = {"Nama Pelanggan", "Nama Produk", "Harga Jual", "Qty", "Harga Total"};
        tableModel = new DefaultTableModel(columnNames, 0);
        drinkTable = new JTable(tableModel);

        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Customer:"));
        customerField = new JComboBox<>();
        for (Customer c : customer) {
            customerField.addItem(c.getName());
        }
        formPanel.add(customerField);
        
        formPanel.add(new JLabel("Barang:"));
        categoryField = new JComboBox<>();
        for (Product p : products) {
            categoryField.addItem(p.getName());
        }
        formPanel.add(categoryField);
        
        formPanel.add(new JLabel("Stok Tersedia"));
        stockField = new JTextField(10);
        stockField.setEditable(false);
        formPanel.add(stockField);

        formPanel.add(new JLabel("Harga Jual:"));
        priceField = new JTextField(10);
        priceField.setEditable(false);
        formPanel.add(priceField);

        formPanel.add(new JLabel("Qty:"));
        qtyField = new JTextField(10);
        formPanel.add(qtyField);


        saveButton = new JButton("Simpan");
        removeButton = new JButton("Hapus");
        editButton = new JButton("Edit");
        refreshBannerButton = new JButton("Refresh Banner");
        formPanel.add(saveButton);

        categoryField.addActionListener(e -> {
            boxData();
        });
        
        // Add product
        saveButton.addActionListener(e -> {
            try {
                String customerName = customerField.getSelectedItem().toString();
                String itemName = categoryField.getSelectedItem().toString();
                double price = Double.parseDouble(priceField.getText());
                double totalPrice;
                int qty = Integer.parseInt(qtyField.getText());
                
                
                Product matchedProduct = null;
                for(Product p : products){
                    if(p.getName().equals(itemName)){
                        matchedProduct = p;
                        break;
                    }
                }
                
                if(matchedProduct == null){
                    JOptionPane.showMessageDialog(drinkTable, "Produk tidak ditemukan");
                    return;
                }
                if(qty > matchedProduct.getStock()){
                    JOptionPane.showMessageDialog(drinkTable, "Stok tidak mencukupi");
                    return;
                }
                
                matchedProduct.setStock(matchedProduct.getStock() - qty);
                
                Transaction transactions = new Transaction(customerName, itemName, price, qty, price*qty );
                transaction.add(transactions);
                tableModel.addRow(new Object[]{customerName, itemName, price, qty, price*qty});
                
                priceField.setText("");
                qtyField.setText("");
                
                
                mainWindow.updateBannerText();
                productform.refreshStock();
                boxData();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Harga & Qty harus berupa angka!");
            }
        });
        
        add(new JScrollPane(drinkTable), BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);

        loadProductData(); // Load products into table at start
    }
    
    public void setProductForm(ProductForm productForm) {
        this.productform = productForm;
    }
    
    public void setCustomerForm(CustomerForm customerform){
        this.customerform = customerform;
    }
    
    public void refreshBox(){
        
        customerField.removeAllItems();

        for (Customer c : customer) {
                customerField.addItem(c.getName());
        }
    }

    private void loadProductData() {
        for (Sold s : sold) {
            tableModel.addRow(new Object[]{
                s.getName(), s.getStock(), s.getPrice(), s.getQty()
            });
        }
    }
    
    public void boxData(){
        String selectedName = (String) categoryField.getSelectedItem();
        for (Product p : products) {
            if (p.getName().equals(selectedName)) {
                stockField.setText(String.valueOf(p.getStock()));
                priceField.setText(String.valueOf(p.getPrice()));
                break;
            }
        }
    }
}
