package com.example.eis2.Item;

public class DataModel {
    String nik;
    String nama;
    String jabatan;
    String departement;
    String lokasi;
    String tanggal;
    String jam;
    String idjam;


    public DataModel(String nik, String nama, String jabatan, String departement, String lokasi, String tanggal, String jam, String idjam) {
        this.nik = nik;
        this.nama = nama;
        this.jabatan = jabatan;
        this.departement = departement;
        this.lokasi = lokasi;
        this.tanggal = tanggal;
        this.jam = jam;
        this.idjam = idjam;

    }

    public String getNik() {
        return nik;
    }

    public String getNama() { return nama; }

    public String getJabatan() {
        return jabatan;
    }

    public String getDepartement() {
        return departement;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getJam() {
        return jam;
    }

    public String getIdjam() { return idjam; }
}

