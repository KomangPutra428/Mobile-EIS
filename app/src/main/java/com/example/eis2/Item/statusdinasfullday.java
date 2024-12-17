package com.example.eis2.Item;

public class statusdinasfullday {
    private String lokasi_struktur;
    private String status_full_day;

    public statusdinasfullday(String lokasi_struktur, String status_full_day){
        this.lokasi_struktur = lokasi_struktur;
        this.status_full_day = status_full_day;
    }
    public String getLokasi_struktur() {return lokasi_struktur; }
    public String getStatus_full_day() {
        return status_full_day;
    }
}

