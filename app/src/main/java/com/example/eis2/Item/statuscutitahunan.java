package com.example.eis2.Item;

public class statuscutitahunan {
    private String lokasi_struktur;
    private String status_cuti_tahunan;

    public statuscutitahunan(String lokasi_struktur, String status_cuti_tahunan){
        this.lokasi_struktur = lokasi_struktur;
        this.status_cuti_tahunan = status_cuti_tahunan;
    }
    public String getLokasi_struktur() {return lokasi_struktur; }
    public String getStatus_cuti_tahunan() {
        return status_cuti_tahunan;
    }
}
