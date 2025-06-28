/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author HAK_PHENG
 */
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
import javax.swing.table.DefaultTableModel;

public class RuanganForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JTextField codeField;
    private JComboBox<String> statusField;
    private JButton saveButton;
    private JButton removeButton;
    private JButton editButton;
    private int idCounter = 0; 

    private List<Product> products;
    private List<Customer> customers;
    private Mavenproject3 mainWindow; //for main
    private SoldForm soldform;
    private TransactionForm transactionform;
    private List<Ruangan> ruangan;
    
    public RuanganForm(List<Ruangan> ruangan, Mavenproject3 mainWindow){
        this.ruangan = ruangan;
        this.mainWindow = mainWindow;
        
        setTitle("AAA");
        setSize(600, 450);
        setLocationRelativeTo(null);
        
        String[] columnNames = {"Id", "Code", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        drinkTable = new JTable(tableModel);

        JPanel formPanel = new JPanel();

        formPanel.add(new JLabel("Code: "));
        codeField = new JTextField(10);
        formPanel.add(codeField);
        
        formPanel.add(new JLabel("Kategori:"));
        statusField = new JComboBox<>(new String[]{"Empty", "Reserved"});
        formPanel.add(statusField);

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
                
                codeField.setText(drinkTable.getValueAt(selectedRow, 1).toString());
                statusField.setSelectedItem(drinkTable.getValueAt(selectedRow, 2).toString());
            }
        });

        // Add product
        saveButton.addActionListener(e -> {
            try {
                String code = codeField.getText();
                boolean status = false;
                int chosen_one = statusField.getSelectedIndex();
                String reserved = statusField.getSelectedItem().toString();
                
                if(chosen_one == 1){
                    status = true;
                }
                else{
                    status = false;
                }
                
                
               
                Ruangan ruang = new Ruangan(idCounter++, code, status);
                ruangan.add(ruang);
                tableModel.addRow(new Object[]{idCounter, code, reserved});
                codeField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "No.Telp harus berupa angka!");
            }
        });

        // Remove product
        removeButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                ruangan.remove(selectedRow);
                tableModel.removeRow(selectedRow);
                codeField.setText("");
                
            } else {
                JOptionPane.showMessageDialog(this, "Pilih produk yang ingin dihapus!");
            }
        });

        // Edit product
        editButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    String newCode = codeField.getText();
                    boolean newStatus = false;
                    int new_chosen_one = statusField.getSelectedIndex();
                    String newReserved = statusField.getSelectedItem().toString();
                    
                    if(new_chosen_one == 1){
                        newStatus = true;
                    }
                    else{
                        newStatus = false;
                    }

                    Ruangan selectedProduct = ruangan.get(selectedRow);
                    selectedProduct.setKode_ruang(newCode);
                    selectedProduct.setReserved(newStatus);

                    // Update table row
                    tableModel.setValueAt(newCode, selectedRow, 1);
                    tableModel.setValueAt(newReserved, selectedRow, 2);

                    
                    codeField.setText("");

                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "No.Telp harus berupa angka!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih produk yang ingin diubah!");
            }
        });

        
        loadData();
        add(new JScrollPane(drinkTable), BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);

    }
    
    private void loadData() {
        for (Ruangan r : ruangan) {
            boolean status = r.isReserved();
            int chosen_one = statusField.getSelectedIndex();
            String ehe;
            
            if(status == true){
                ehe = "Reserved";
            }
            else{
                ehe = "Empty";
            }
            tableModel.addRow(new Object[]{
                r.getId_ruang(), r.getKode_ruang(), ehe
            });
        }
    }
}

