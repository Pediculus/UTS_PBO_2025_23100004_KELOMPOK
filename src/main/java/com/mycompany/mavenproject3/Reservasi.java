package com.mycompany.mavenproject3;

import java.util.Date;
import java.time.LocalTime;

/**
 *
 * @author HAK_PHENG
 */
public class Reservasi {
    private String kodeReservasi;
    private String customerName;
    private Date tanggalReservasi;
    private LocalTime masaWaktuReservasi;
    private String detailReservasi;

    public Reservasi(String kodeReservasi, String customerName, Date tanggalReservasi, LocalTime masaWaktuReservasi, String detailReservasi) {
        this.kodeReservasi = kodeReservasi;
        this.customerName = customerName;
        this.tanggalReservasi = tanggalReservasi;
        this.masaWaktuReservasi = masaWaktuReservasi;
        this.detailReservasi = detailReservasi;
    }

    public String getKodeReservasi() {
        return kodeReservasi;
    }

    public void setKodeReservasi(String kodeReservasi) {
        this.kodeReservasi = kodeReservasi;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getTanggalReservasi() {
        return tanggalReservasi;
    }

    public void setTanggalReservasi(Date tanggalReservasi) {
        this.tanggalReservasi = tanggalReservasi;
    }

    public LocalTime getMasaWaktuReservasi() {
        return masaWaktuReservasi;
    }

    public void setMasaWaktuReservasi(LocalTime masaWaktuReservasi) {
        this.masaWaktuReservasi = masaWaktuReservasi;
    }

    public String getDetailReservasi() {
        return detailReservasi;
    }

    public void setDetailReservasi(String detailReservasi) {
        this.detailReservasi = detailReservasi;
    }
}
