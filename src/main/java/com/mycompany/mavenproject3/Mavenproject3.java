package com.mycompany.mavenproject3;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Mavenproject3 extends JFrame implements Runnable {
    private List<Product> products; // Holds the list of products
    private List<Sold> sold;
    private List<Customer> customer;
    private List<Transaction> transaction;
    private List<Reservasi> reservasi;
    private ProductForm productform;
    private SoldForm soldform;
    private CustomerForm customerform;
    private TransactionForm transactionform;
    private ReservasiForm reservasiform;
    private String bannerText; // Text to scroll across the screen
    private int x; // Current X position of the scrolling text
    private int width; // Width of the window
    private BannerPanel bannerPanel; // Custom JPanel to paint scrolling text
    private JButton addProductButton;
    private JButton addSoldButton;
    private JButton addcustomerButton;
    private JButton addTransactionButton;
    private JButton addReservasiButton;

    public Mavenproject3() {
        // Initialize the product list
        products = new ArrayList<>();
        products.add(new Product(1, "P001", "Americano", "Coffee", 18000, 10));
        products.add(new Product(2, "P002", "Pandan Latte", "Coffee", 15000, 8));
        products.add(new Product(3, "P003", "Cheese", "Dairy", 15000, 8));
        
        sold = new ArrayList<>();
        
        customer = new ArrayList<>();
        customer.add(new Customer(1,"AAAAA",231235134));
        
        transaction = new ArrayList<>();
        
        reservasi = new ArrayList<>();
        
        setTitle("WK. STI Chill");
        setSize(600, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        
        // Create and add the banner panel
        bannerPanel = new BannerPanel();
        add(bannerPanel, BorderLayout.CENTER);

        
        // Set initial banner text based on products
        updateBannerText();

        
        // Create and add the "Kelola Produk" button
        JPanel bottomPanel = new JPanel();
        addProductButton = new JButton("Kelola Produk");
        addSoldButton = new JButton("Form Penjualan");
        addcustomerButton = new JButton("Form Customer");
        addTransactionButton = new JButton("Transaksi");
        addReservasiButton = new JButton("Reservasi");
        
        bottomPanel.add(addProductButton);
        bottomPanel.add(addSoldButton);
        bottomPanel.add(addcustomerButton);
        bottomPanel.add(addTransactionButton);
        bottomPanel.add(addReservasiButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        soldform = new SoldForm(products, sold, this); // Pass product list
        productform = new ProductForm(products, this); // Pass soldForm
        customerform = new CustomerForm(products, customer, this);
        transactionform = new TransactionForm(products, sold, this, customer, transaction); // Pass product list
        reservasiform = new ReservasiForm(reservasi, this);
        
        soldform.setProductForm(productform);
        productform.setSoldForm(soldform);
        transactionform.setProductForm(productform);
        transactionform.setCustomerForm(customerform);
        customerform.setTransactionForm(transactionform);
        reservasiform.setSoldForm(soldform);

        addProductButton.addActionListener(e -> {
            if(productform == null){
                productform = new ProductForm(products, this);
            }
            if(soldform == null){
                soldform = new SoldForm(products, sold, this);
            }
            productform.setVisible(true); 
        });
        
        addSoldButton.addActionListener(e -> {
            if(productform == null){
                productform = new ProductForm(products, this);
            }
            if(soldform == null){
                soldform = new SoldForm(products, sold, this);
            }
            soldform.setVisible(true);
        });
        
        addcustomerButton.addActionListener(e -> {
            if(productform == null){
                productform = new ProductForm(products, this);
            }
            if(soldform == null){
                soldform = new SoldForm(products, sold, this);
            }
            if(customerform == null){
                customerform = new CustomerForm(products, customer, this);
            }
            customerform.setVisible(true);
        });
        
        addTransactionButton.addActionListener(e -> {
            if(productform == null){
                productform = new ProductForm(products, this);
            }
            if(soldform == null){
                soldform = new SoldForm(products, sold, this);
            }
            if(customerform == null){
                customerform = new CustomerForm(products, customer, this);
            }
            if(transaction == null){
                transactionform = new TransactionForm(products, sold, this, customer, transaction);
            }
            transactionform.setVisible(true);
        });
        
        addReservasiButton.addActionListener(e ->{
            if(productform == null){
                productform = new ProductForm(products, this);
            }
            if(soldform == null){
                soldform = new SoldForm(products, sold, this);
            }
            if(customerform == null){
                customerform = new CustomerForm(products, customer, this);
            }
            if(transaction == null){
                transactionform = new TransactionForm(products, sold, this, customer, transaction);
            }
            reservasiform.setVisible(true);
        });
        

        setVisible(true);

        Thread thread = new Thread(this);
        thread.start();
    }

    // update text banner
    public void updateBannerText() {
        StringBuilder sb = new StringBuilder("Menu yang tersedia: ");

        for (Product p : products) {
            if (p.getStock() > 0) {
                sb.append(p.getName()).append(" | ");
            }
        }

        bannerText = sb.toString();
        bannerPanel.repaint(); // refresh the banner panel
    }


    // Panel for custom text painting
    class BannerPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString(bannerText, x, getHeight() / 2);
        }
    }

    //movement
    @Override
    public void run() {
        width = getWidth();
        while (true) {
            x += 5;
            if (x > width) {
                x = -getFontMetrics(new Font("Arial", Font.BOLD, 18)).stringWidth(bannerText);
            }
            bannerPanel.repaint();
            try {
                Thread.sleep(30); //speed
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    

    public static void main(String[] args) {
        // Start the main window with product list and banner update
        new Mavenproject3();
    }
}
