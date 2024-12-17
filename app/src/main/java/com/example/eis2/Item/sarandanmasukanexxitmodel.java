package com.example.eis2.Item;

public class sarandanmasukanexxitmodel {
    private String nomor_saran;
    private String saran;

    public sarandanmasukanexxitmodel(String nomor_saran, String saran) {
        this.nomor_saran = nomor_saran;
        this.saran = saran;

    }
    public String getNomor_saran() { return nomor_saran; }
    public void setNomor_saran(String nomor_saran) { this.nomor_saran = nomor_saran; }

    public String getSaran() { return saran; }
    public void setSaran(String saran) { this.saran = saran; }
}
