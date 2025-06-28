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
    private JTextField codeField;
    private JTextField nameField;
    private JXDatePicker datePicker;
    private JTextField detailField;
    private JComboBox<String> ruangField;
    private JButton saveButton;
    private JButton removeButton;
    private JButton editButton;
    private JSpinner timeSpinner; // NEW

    private int idCounter = 0;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); // NEW

    private List<Reservasi> reservasi;
    private List<Ruangan> ruang;
    private Mavenproject3 mainWindow;
    private SoldForm soldform;

    public ReservasiForm(List<Reservasi> reservasi, Mavenproject3 mainWindow, List<Ruangan> ruang) {
        this.reservasi = reservasi;
        this.ruang = ruang;
        this.mainWindow = mainWindow;

        setTitle("AAA");
        setSize(600, 450);
        setLocationRelativeTo(null);

        String[] columnNames = {"Code", "Customer Name", "Room", "Reserved at"};
        tableModel = new DefaultTableModel(columnNames, 0);
        drinkTable = new JTable(tableModel);

        JPanel formPanel = new JPanel();

        formPanel.add(new JLabel("Kode Reservasi"));
        codeField = new JTextField(10);
        formPanel.add(codeField);

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
                String code = codeField.getText();
                String name = nameField.getText();
                String room = ruangField.getSelectedItem().toString();
                String detail = detailField.getText();
                Date date = datePicker.getDate();
                String formattedDate = sdf.format(date);
                Date timeDate = (Date) timeSpinner.getValue();
                LocalTime time = timeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                String reservedAt = formattedDate + " at " + time;

                Reservasi reserve = new Reservasi(code, name, date, time, detail);
                reservasi.add(reserve);

                tableModel.addRow(new Object[]{code, name, room, reservedAt});
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
                    String newCode = codeField.getText();
                    String newName = nameField.getText();
                    String newRoom = ruangField.getSelectedItem().toString();
                    String newDetail = detailField.getText();
                    Date newDate = datePicker.getDate();
                    Date newTimeDate = (Date) timeSpinner.getValue();
                    LocalTime newTime = newTimeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                    String formattedDate = sdf.format(newDate);


                    Reservasi selectedReservasi = reservasi.get(selectedRow);
                    selectedReservasi.setKodeReservasi(newCode);
                    selectedReservasi.setCustomerName(newName);
                    selectedReservasi.setKodeReservasi(newRoom);
                    selectedReservasi.setTanggalReservasi(newDate);
                    selectedReservasi.setMasaWaktuReservasi(newTime);
                    selectedReservasi.setDetailReservasi(newDetail);

                    tableModel.setValueAt(newCode, selectedRow, 0);
                    tableModel.setValueAt(newName, selectedRow, 1);
                    tableModel.setValueAt(formattedDate, selectedRow, 2);
                    tableModel.setValueAt(newRoom, selectedRow, 3);
                    tableModel.setValueAt(newTime, selectedRow, 4);

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

    public void setSoldForm(SoldForm soldForm) {
        this.soldform = soldForm;
    }

    private void loadProductData() {
        for (Reservasi r : reservasi) {
            tableModel.addRow(new Object[]{
                r.getKodeReservasi(),
                r.getCustomerName(),
                sdf.format(r.getTanggalReservasi()),
                r.getMasaWaktuReservasi(),
                r.getDetailReservasi()
            });
        }
    }

    private void clearFields() {
        codeField.setText("");
        nameField.setText("");
        detailField.setText("");
        datePicker.setDate(new Date());
        timeSpinner.setValue(new Date());
    }
}
