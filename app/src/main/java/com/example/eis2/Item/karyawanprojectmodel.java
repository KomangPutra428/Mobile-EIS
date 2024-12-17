package com.example.eis2.Item;

public class karyawanprojectmodel {
    String nik_pengajuan;
    String start_date;
    String end_date;
    String nama_karyawan_struktur;
    String nik_karyawan;
    String depo_karyawan;

    public karyawanprojectmodel(String nik_pengajuan, String start_date, String end_date, String nama_karyawan_struktur, String nik_karyawan, String depo_karyawan){
        this.nik_pengajuan = nik_pengajuan;
        this.start_date = start_date;
        this.end_date = end_date;
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.nik_karyawan = nik_karyawan;
        this.depo_karyawan = depo_karyawan;
    }

    public String getNik_pengajuan() {
        return nik_pengajuan;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getNama_karyawan_struktur() {
        return nama_karyawan_struktur;
    }

    public String getNik_karyawan() {
        return nik_karyawan;
    }

    public String getDepo_karyawan() {
        return depo_karyawan;
    }

    @Override
    public String toString() {
        return nama_karyawan_struktur;
    }

}
