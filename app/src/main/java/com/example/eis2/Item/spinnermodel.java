package com.example.eis2.Item;

public class spinnermodel {
    private String nama_karyawan_struktur, nik_baru;

    public spinnermodel(String nama_karyawan_struktur, String nik_baru) {
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.nik_baru = nik_baru;
    }

        public String getNama_karyawan_struktur() {
            return nama_karyawan_struktur;
        }
        public String getNik_baru() {
            return nik_baru;
        }
    }
