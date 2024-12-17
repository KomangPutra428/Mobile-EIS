package com.example.eis2.Item;

public class DataModel2 {
    String nama;
    String nik;
    String depo;

    public DataModel2(String nama, String nik, String depo) {
        this.nama = nama;
        this.nik = nik;
        this.depo = depo;
    }


    public String getNama() { return nama; }

    public String getNik() { return nik; }

    public String getDepo() { return depo; }
}
