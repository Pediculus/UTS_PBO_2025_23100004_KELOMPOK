package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXDatePicker;
import java.util.Date;
import java.time.LocalTime;
import java.time.ZoneId;

public class ReservasiForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JXDatePicker datePicker;
    private JComboBox<String> ruangField;
    private JButton saveButton;
    private JButton removeButton;
    private JButton editButton;
    private JSpinner timeSpinner;

    private int idCounter = 1;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private List<Reservasi> reservasi;
    private List<Ruangan> ruang;
    private Mavenproject3 mainWindow;

    public ReservasiForm(List<Reservasi> reservasi, Mavenproject3 mainWindow, List<Ruangan> ruang) {
        this.reservasi = reservasi;
        this.ruang = ruang;
        this.mainWindow = mainWindow;

        setTitle("AAA");
        setSize(600, 450);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Customer Name", "Room", "Reserved at"};
        tableModel = new DefaultTableModel(columnNames, 0);
        drinkTable = new JTable(tableModel);

        JPanel formPanel = new JPanel();

        formPanel.add(new JLabel("Nama Customer:"));
        nameField = new JTextField(10);
        formPanel.add(nameField);

        formPanel.add(new JLabel("Ruangan:"));
        ruangField = new JComboBox<>();
        for (Ruangan r : ruang) {
            ruangField.addItem(r.getKode_ruang());
        }
        formPanel.add(ruangField);

        formPanel.add(new JLabel("Waktu:"));
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date());
        formPanel.add(timeSpinner);

        formPanel.add(new JLabel("Tanggal:"));
        datePicker = new JXDatePicker();
        datePicker.setDate(new Date());
        datePicker.setFormats("dd-MM-yyyy");
        formPanel.add(datePicker);

        saveButton = new JButton("Simpan");
        removeButton = new JButton("Hapus");
        editButton = new JButton("Edit");
        formPanel.add(saveButton);
        formPanel.add(removeButton);
        formPanel.add(editButton);

        drinkTable.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                nameField.setText(drinkTable.getValueAt(selectedRow, 1).toString());

                // Set date
                try {
                    Date date = sdf.parse(drinkTable.getValueAt(selectedRow, 2).toString());
                    datePicker.setDate(date);
                } catch (Exception e) {
                    datePicker.setDate(new Date());
                }

                // Set time
                try {
                    Date time = timeFormat.parse(drinkTable.getValueAt(selectedRow, 3).toString());
                    timeSpinner.setValue(time);
                } catch (Exception e) {
                    timeSpinner.setValue(new Date());
                }
            }
        });

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String room = ruangField.getSelectedItem().toString();
                Date date = datePicker.getDate();
                String formattedDate = sdf.format(date);
                Date timeDate = (Date) timeSpinner.getValue();
                LocalTime time = timeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                String reservedAt = formattedDate + " at " + time;

                Reservasi reserve = new Reservasi(idCounter++, name, date, time, null);
                reservasi.add(reserve);

                tableModel.addRow(new Object[]{reserve.getIdReservasi(), name, room, reservedAt});
                clearFields();
                mainWindow.updateBannerText();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Data tidak valid!");
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                reservasi.remove(selectedRow);
                tableModel.removeRow(selectedRow);
                clearFields();
                mainWindow.updateBannerText();
            } else {
                JOptionPane.showMessageDialog(this, "Pilih reservasi yang ingin dihapus!");
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    String newName = nameField.getText();
                    String newRoom = ruangField.getSelectedItem().toString();
                    Date newDate = datePicker.getDate();
                    Date newTimeDate = (Date) timeSpinner.getValue();
                    LocalTime newTime = newTimeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                    String formattedDate = sdf.format(newDate);

                    Reservasi selectedReservasi = reservasi.get(selectedRow);
                    selectedReservasi.setCustomerName(newName);
                    selectedReservasi.setTanggalReservasi(newDate);
                    selectedReservasi.setMasaWaktuReservasi(newTime);

                    tableModel.setValueAt(selectedReservasi.getIdReservasi(), selectedRow, 0);
                    tableModel.setValueAt(newName, selectedRow, 1);
                    tableModel.setValueAt(newRoom, selectedRow, 2);
                    tableModel.setValueAt(formattedDate + " at " + newTime, selectedRow, 3);

                    clearFields();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal mengedit data!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih reservasi yang ingin diubah!");
            }
        });

        add(new JScrollPane(drinkTable), BorderLayout.CENTER);
        add(formPanel, BorderLayout.SOUTH);

        loadProductData();
    }

    private void loadProductData() {
        for (Reservasi r : reservasi) {
            tableModel.addRow(new Object[]{
                r.getIdReservasi(),
                r.getCustomerName(),
                "-", // Room display only
                sdf.format(r.getTanggalReservasi()) + " at " + r.getMasaWaktuReservasi()
            });
        }
    }

    private void clearFields() {
        nameField.setText("");
        datePicker.setDate(new Date());
        timeSpinner.setValue(new Date());
    }
}
