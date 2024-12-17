package com.example.eis2.Item;

public class anakModel {
    private String id_anak;
    private String urutan_anak;
    private String nama_anak;
    private String no_ktp_anak;
    private String tanggal_lahir_anak;
    private String tempat_lahir_anak;
    private String gol_darah_anak;
    private String agama_anak;
    private String suku_anak;

    private String kewarganegaraan_anak;

    private String pendidikan_anak;

    public anakModel(String id_anak, String urutan_anak,  String nama_anak, String no_ktp_anak, String tanggal_lahir_anak,
                     String tempat_lahir_anak, String gol_darah_anak, String agama_anak, String suku_anak, String kewarganegaraan_anak, String pendidikan_anak) {
        this.id_anak = id_anak;
        this.urutan_anak = urutan_anak;
        this.nama_anak = nama_anak;
        this.no_ktp_anak = no_ktp_anak;
        this.tanggal_lahir_anak = tanggal_lahir_anak;
        this.tempat_lahir_anak = tempat_lahir_anak;
        this.gol_darah_anak = gol_darah_anak;
        this.agama_anak = agama_anak;
        this.suku_anak = suku_anak;
        this.kewarganegaraan_anak = kewarganegaraan_anak;
        this.pendidikan_anak = pendidikan_anak;
    }

    public String getId_anak() {
        return id_anak;
    }

    public String getUrutan_anak() {
        return urutan_anak;
    }

    public String getNama_anak() {
        return nama_anak;
    }

    public String getNo_ktp_anak() {
        return no_ktp_anak;
    }

    public String getTanggal_lahir_anak() {
        return tanggal_lahir_anak;
    }

    public String getTempat_lahir_anak() {
        return tempat_lahir_anak;
    }

    public String getGol_darah_anak() {
        return gol_darah_anak;
    }

    public String getAgama_anak() {
        return agama_anak;
    }

    public String getSuku_anak() {
        return suku_anak;
    }

    public String getKewarganegaraan_anak() {
        return kewarganegaraan_anak;
    }

    public String getPendidikan_anak() {
        return pendidikan_anak;
    }
}
