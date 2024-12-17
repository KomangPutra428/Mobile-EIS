package com.example.eis2.Item;

public class soalmodel {
    private String id;
    private String soal;
    private String Kondisi;

    public soalmodel(String id, String soal) {
        this.id = id;
        this.soal = soal;
    }

    public String getId() { return id; }
    public String getSoal() { return soal; }

    public void setKondisi(String kondisi) {
        Kondisi = kondisi;
    }

    public String getKondisi() {
        return Kondisi;
    }
}

