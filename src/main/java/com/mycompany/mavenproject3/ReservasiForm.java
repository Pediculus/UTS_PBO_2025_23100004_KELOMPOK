package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXDatePicker; // <-- Add this import
import java.util.Date;
import java.time.LocalTime;
//use JSpinner for time


public class ReservasiForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JTextField codeField;
    private JTextField nameField;
    private JXDatePicker datePicker; // <-- Replaces dateField
    private JTextField detailField;
    private JTextField timeField;
    private JButton saveButton;
    private JButton removeButton;
    private JButton editButton;
    private int idCounter = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private List<Reservasi> reservasi;
    private Mavenproject3 mainWindow;
    private SoldForm soldform;

    public ReservasiForm(List<Reservasi> reservasi, Mavenproject3 mainWindow) {
        this.reservasi = reservasi;
        this.mainWindow = mainWindow;

        setTitle("AAA");
        setSize(600, 450);
        setLocationRelativeTo(null);

        String[] columnNames = {"Code", "Customer Name", "Date", "Time", "Detail"};
        tableModel = new DefaultTableModel(columnNames, 0);
        drinkTable = new JTable(tableModel);

        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Kode Reservasi"));
        codeField = new JTextField(10);
        formPanel.add(codeField);

        formPanel.add(new JLabel("Nama Customer:"));
        nameField = new JTextField(10);
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Waktu:"));
        timeField = new JTextField(10);
        formPanel.add(timeField);
        
        formPanel.add(new JLabel("Tanggal:"));
        datePicker = new JXDatePicker();
        datePicker.setDate(new Date());
        datePicker.setFormats("dd-MM-yyyy");
        formPanel.add(datePicker);

        formPanel.add(new JLabel("Detail Reservasi:"));
        detailField = new JTextField(10);
        formPanel.add(detailField);

        saveButton = new JButton("Simpan");
        removeButton = new JButton("Hapus");
        editButton = new JButton("Edit");
        formPanel.add(saveButton);
        formPanel.add(removeButton);
        formPanel.add(editButton);

        drinkTable.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                codeField.setText(drinkTable.getValueAt(selectedRow, 0).toString());
                nameField.setText(drinkTable.getValueAt(selectedRow, 1).toString());
                timeField.setText(drinkTable.getValueAt(selectedRow, 3).toString());
                datePicker.setDate(new Date()); // Placeholder, parse your date here if needed
                detailField.setText(drinkTable.getValueAt(selectedRow, 4).toString());
            }
        });

        saveButton.addActionListener(e -> {
            try {
                String code = codeField.getText();
                String name = nameField.getText();
                String detail = detailField.getText();
                Date date = datePicker.getDate();
                String time = timeField.getText();
                String formattedDate = sdf.format(date);

                Reservasi reserve = new Reservasi(code, name, date, time, detail);
                reservasi.add(reserve);
                
                tableModel.addRow(new Object[]{code, name, formattedDate, time, detail});
                clearFields();
                mainWindow.updateBannerText();
                soldform.refreshBox();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Harga & Stock harus berupa angka!");
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                reservasi.remove(selectedRow);
                tableModel.removeRow(selectedRow);
                clearFields();
                mainWindow.updateBannerText();
                soldform.refreshBox();
            } else {
                JOptionPane.showMessageDialog(this, "Pilih produk yang ingin dihapus!");
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    String newCode = codeField.getText();
                    String newName = nameField.getText();
                    String newTime = timeField.getText();
                    Date newDate = datePicker.getDate();
                    String formattedDate = sdf.format(newDate);
                    String newDetail = detailField.getText();

                    Reservasi selectedReservasi = reservasi.get(selectedRow);
                    selectedReservasi.setKodeReservasi(newCode);
                    selectedReservasi.setCustomerName(newName);
                    selectedReservasi.setTanggalReservasi(newDate);
                    selectedReservasi.setMasaWaktuReservasi(newTime);
                    selectedReservasi.setDetailReservasi(newDetail);

                    tableModel.setValueAt(newCode, selectedRow, 0);
                    tableModel.setValueAt(newName, selectedRow, 1);
                    tableModel.setValueAt(formattedDate, selectedRow, 2);
                    tableModel.setValueAt(newTime, selectedRow, 3);
                    tableModel.setValueAt(newDetail, selectedRow, 4);

                    clearFields();
                    mainWindow.updateBannerText();
                    soldform.refreshBox();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Harga & Stock harus berupa angka!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih produk yang ingin diubah!");
            }
        });

        add(new JScrollPane(drinkTable), BorderLayout.CENTER);
        add(formPanel, BorderLayout.SOUTH);

        loadProductData();
    }

    public void setSoldForm(SoldForm soldForm) {
        this.soldform = soldForm;
    }

    private void loadProductData() {
        for (Reservasi r : reservasi) {
            tableModel.addRow(new Object[]{
                r.getKodeReservasi(), r.getCustomerName(), r.getTanggalReservasi(), r.getMasaWaktuReservasi(), r.getDetailReservasi()
            });
        }
    }

    private void clearFields() {
        codeField.setText("");
        nameField.setText("");
        timeField.setText("");
        datePicker.setDate(new Date());
        detailField.setText("");
    }
}