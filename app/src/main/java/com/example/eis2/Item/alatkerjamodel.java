package com.example.eis2.Item;

public class alatkerjamodel {
    private String alat_kerja;
    private String jumlah;
    private String satuan;
    private String kondisi;
    private String keterangan;

    public alatkerjamodel(String alat_kerja,
                          String jumlah,
                          String satuan,
                          String kondisi,
                          String keterangan) {
        this.alat_kerja=alat_kerja;
        this.jumlah = jumlah;
        this.satuan = satuan;
        this.kondisi = kondisi;
        this.keterangan = keterangan;
    }

    public String getAlat_kerja() {
        return alat_kerja;
    }

    public String getJumlah() {
        return jumlah;
    }

    public String getSatuan() {
        return satuan;
    }

    public String getKondisi() {
        return kondisi;
    }

    public String getKeterangan() {
        return keterangan;
    }
}
