package com.mycompany.mavenproject3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> customerField;
    private JButton addItemsButton;
    private JButton saveButton;
    private JButton viewDetailsButton;

    private int idCounter = 1;
    private List<Product> products;
    private List<Sold> sold;
    private List<Customer> customer;
    private List<Transaction> transaction;
    private Mavenproject3 mainWindow;
    private ProductForm productform;
    private CustomerForm customerform;

    private List<TransactionItem> selectedItems = new ArrayList<>();

    public TransactionForm(List<Product> products, List<Sold> sold, Mavenproject3 mainWindow, List<Customer> customer, List<Transaction> transaction) {
        this.products = products;
        this.sold = sold;
        this.mainWindow = mainWindow;
        this.customer = customer;
        this.transaction = transaction;

        setTitle("Form Transaksi");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"Transaction Time", "Customer", "Total Harga"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        drinkTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(drinkTable);
        add(scrollPane, BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = new JPanel(new FlowLayout());

        customerField = new JComboBox<>();
        for (Customer c : customer) {
            customerField.addItem(c.getName());
        }

        formPanel.add(new JLabel("Customer:"));
        formPanel.add(customerField);

        addItemsButton = new JButton("Tambah Item");
        formPanel.add(addItemsButton);

        saveButton = new JButton("Simpan");
        formPanel.add(saveButton);

        add(formPanel, BorderLayout.NORTH);

        drinkTable.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                
            }
        });
        
        // Add Item button logic
        addItemsButton.addActionListener(e -> {
            AddItemsDialog dialog = new AddItemsDialog(this, products);
            dialog.setVisible(true);
            selectedItems.clear();
            selectedItems.addAll(dialog.getSelectedItems());
        });

        // Save button logic
        saveButton.addActionListener(e -> {
            String customerName = (String) customerField.getSelectedItem();
            if (selectedItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tambahkan item terlebih dahulu.");
                return;
            }

            LocalDateTime time = LocalDateTime.now();
            Transaction newTransaction = new Transaction(idCounter++, time, customerName);

            for (TransactionItem item : selectedItems) {
                Product product = products.stream()
                        .filter(p -> p.getName().equals(item.getProductName()))
                        .findFirst().orElse(null);

                if (product != null && item.getQty() <= product.getStock()) {
                    product.setStock(product.getStock() - item.getQty());
                    newTransaction.addItem(item);
                }
            }

            transaction.add(newTransaction);
            selectedItems.clear();
            double total = newTransaction.getTotalPrice();
            String formatted = "Rp." + String.valueOf(total);
            // Add one row per transaction
            tableModel.addRow(new Object[]{
                    time,
                    customerName,
                    formatted

            });

            mainWindow.updateBannerText();
            productform.refreshStock();
        });

        // View Details button
        viewDetailsButton = new JButton("Lihat Detail");
        viewDetailsButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1 && selectedRow < transaction.size()) {
                Transaction t = transaction.get(selectedRow);
                StringBuilder msg = new StringBuilder("Detail Pesanan:\n");

                for (TransactionItem item : t.getItems()) {
                    msg.append("- ").append(item.getProductName())
                            .append(" x ").append(item.getQty())
                            .append(" @ ").append("Rp.")
                            .append(item.getPrice()).append(" = ")
                            .append("Rp.").append(item.getTotalPrice())
                            .append("\n");
                }

                JOptionPane.showMessageDialog(this, msg.toString(), "Detail Transaksi", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris transaksi terlebih dahulu.");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewDetailsButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadProductData(); // Optional: for preloaded transactions
    }

    public void setProductForm(ProductForm productForm) {
        this.productform = productForm;
    }

    public void setCustomerForm(CustomerForm customerform) {
        this.customerform = customerform;
    }

    public void refreshBox() {
        customerField.removeAllItems();
        for (Customer c : customer) {
            customerField.addItem(c.getName());
        }
    }

    private void loadProductData() {
        for (Sold s : sold) {
            tableModel.addRow(new Object[]{
                    s.getName(), s.getPrice() * s.getQty(), "Lihat"
            });
        }
    }
}
