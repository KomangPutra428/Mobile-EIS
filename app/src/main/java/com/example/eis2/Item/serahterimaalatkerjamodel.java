package com.example.eis2.Item;

public class serahterimaalatkerjamodel {
    private String nik_baru;
    private String alat_kerja;
    private String jumlah_alat_kerja;
    private String satuan_alat_kerja;
    private String kondisi_alat_kerja;
    private String keterangan_alat_kerja;

    public serahterimaalatkerjamodel(String nik_baru, String alat_kerja, String jumlah_alat_kerja,
                             String satuan_alat_kerja, String kondisi_alat_kerja, String keterangan_alat_kerja){
        this.nik_baru=nik_baru;
        this.alat_kerja=alat_kerja;
        this.jumlah_alat_kerja=jumlah_alat_kerja;
        this.satuan_alat_kerja=satuan_alat_kerja;
        this.kondisi_alat_kerja=kondisi_alat_kerja;
        this.keterangan_alat_kerja=keterangan_alat_kerja;

    }

    public String getNik_baru() { return nik_baru; }
    public String getAlat_kerja() { return alat_kerja; }
    public String getJumlah_alat_kerja() { return jumlah_alat_kerja; }
    public String getSatuan_alat_kerja() { return satuan_alat_kerja; }
    public String getKondisi_alat_kerja() { return kondisi_alat_kerja; }
    public String getKeterangan_alat_kerja() { return keterangan_alat_kerja; }
}
