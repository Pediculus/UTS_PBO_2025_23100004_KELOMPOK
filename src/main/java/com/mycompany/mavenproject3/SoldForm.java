package com.mycompany.mavenproject3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class SoldForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JTextField qtyField;
    private JComboBox<String> categoryField;
    private JTextField priceField;
    private JTextField stockField;
    private JButton saveButton;
    private JButton removeButton;
    private JButton editButton;
    private JButton refreshBannerButton; // refresh banner
    private int idCounter = 3; 

    private List<Product> products;
    private List<Sold> sold;
    private Mavenproject3 mainWindow; //for main
    private ProductForm productform;

    // Accept product list and reference to main window
    public SoldForm(List<Product> products, List<Sold> sold, Mavenproject3 mainWindow) {
        this.products = products;
        this.sold = sold;
        this.mainWindow = mainWindow; // also for main

        setTitle("Form Penjualan");
        setSize(600, 450);
        setLocationRelativeTo(null);

        String[] columnNames = {"Nama", "Harga Jual", "Qty"};
        tableModel = new DefaultTableModel(columnNames, 0);
        drinkTable = new JTable(tableModel);

        JPanel formPanel = new JPanel();
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
                String name = categoryField.getSelectedItem().toString();
                double price = Double.parseDouble(priceField.getText());
                int qty = Integer.parseInt(qtyField.getText());
                
                Product matchedProduct = null;
                for(Product p : products){
                    if(p.getName().equals(name)){
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
                
                Sold solds = new Sold(name, price, qty);
                sold.add(solds);
                tableModel.addRow(new Object[]{name, price * qty, qty});
                
                priceField.setText("");
                qtyField.setText("");
                
                
                mainWindow.updateBannerText();
                productform.refreshStock();
                boxData();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Harga & Stock harus berupa angka!");
            }
        });
        
        
        
        
        add(new JScrollPane(drinkTable), BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);

        loadProductData(); // Load products into table at start
    }
    
    public void setProductForm(ProductForm productForm) {
        this.productform = productForm;
    }

    private void loadProductData() {
        for (Sold s : sold) {
            tableModel.addRow(new Object[]{
                s.getName(), s.getStock(), s.getPrice(), s.getQty()
            });
        }
    }
    
    public void refreshBox(){
        categoryField.removeAllItems();

        for (Product p : products) {
                categoryField.addItem(p.getName());
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
