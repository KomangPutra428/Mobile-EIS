package com.example.eis2.Item;

public class shifting_model {
    String id_karyawan_shift, nik_baru, nama_karyawan_shift, dept_karyawan_shift, shift_day, waktu_masuk, waktu_keluar;

    public shifting_model(String id_karyawan_shift, String nik_baru, String nama_karyawan_shift, String dept_karyawan_shift, String shift_day, String waktu_masuk, String waktu_keluar) {
        this.id_karyawan_shift = id_karyawan_shift;
        this.nik_baru = nik_baru;
        this.nama_karyawan_shift = nama_karyawan_shift;
        this.dept_karyawan_shift = dept_karyawan_shift;
        this.shift_day = shift_day;
        this.waktu_masuk = waktu_masuk;
        this.waktu_keluar = waktu_keluar;
    }

    public String getId_karyawan_shift() { return id_karyawan_shift; }

    public String getNik_baru() { return nik_baru; }

    public String getNama_karyawan_shift() { return nama_karyawan_shift; }

    public String getDept_karyawan_shift() { return dept_karyawan_shift; }

    public String getShift_day() { return shift_day; }

    public String getWaktu_masuk() { return waktu_masuk; }

    public String getWaktu_keluar() { return waktu_keluar; }

    @Override public String toString() { return shift_day; }

}

