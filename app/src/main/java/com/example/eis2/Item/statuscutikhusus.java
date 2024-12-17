package com.example.eis2.Item;

public class statuscutikhusus {
    private String lokasi_struktur;
    private String status_cuti_khusus;

    public statuscutikhusus(String lokasi_struktur, String status_cuti_khusus){
        this.lokasi_struktur = lokasi_struktur;
        this.status_cuti_khusus = status_cuti_khusus;
    }
    public String getLokasi_struktur() {return lokasi_struktur; }
    public String getStatus_cuti_khusus() {
        return status_cuti_khusus;
    }
}
