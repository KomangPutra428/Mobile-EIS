package com.example.eis2.Item;

public class rekapabsensiclass {
    String nik_baru;
    String nama_karyawan_struktur;
    String lokasi_struktur;
    int number;


    public rekapabsensiclass(String nik_baru, String nama_karyawan_struktur, String lokasi_struktur) {
        this.nik_baru = nik_baru;
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.lokasi_struktur= lokasi_struktur;

    }


    public String getNik_baru() { return nik_baru; }

    public String getNama_karyawan_struktur() {
        return nama_karyawan_struktur;
    }

    public String getLokasi_struktur() { return lokasi_struktur; }

    public int getNumber() {
        return number;
    }
}



