package com.example.eis2.Item;

public class absenmanualmodel {
    private String id_absen;
    private String nik_baru;
    private String lokasi_struktur;
    private String jenis_absen;
    private String tanggal_absen;
    private String waktu_absen;
    private String ket_absen;
    private String status;
    private String tanggal;
    private String status_2;
    private String tanggal_2;

    public absenmanualmodel(String id_absen, String nik_baru, String lokasi_struktur, String jenis_absen, String tanggal_absen, String waktu_absen, String ket_absen, String status, String tanggal, String status_2, String tanggal_2) {
        this.id_absen = id_absen;
        this.nik_baru = nik_baru;
        this.lokasi_struktur = lokasi_struktur;
        this.jenis_absen = jenis_absen;
        this.tanggal_absen = tanggal_absen;
        this.waktu_absen = waktu_absen;
        this.ket_absen = ket_absen;
        this.status = status;
        this.tanggal = tanggal;
        this.status_2 = status_2;
        this.tanggal_2 = tanggal_2;
    }

    public String getNik_baru() { return nik_baru; }

    public String getId_absen() { return id_absen; }

    public String getLokasi_struktur() { return lokasi_struktur; }

    public String getJenis_absen() {return jenis_absen; }

    public String getTanggal_absen() {
        return tanggal_absen;
    }

    public String getWaktu_absen() {
        return waktu_absen;
    }

    public String getKet_absen() {
        return ket_absen;
    }

    public String getStatus() {
        return status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getStatus_2() {
        return status_2;
    }

    public String getTanggal_2() {
        return tanggal_2;
    }
}

