package com.example.eis2.Item;

public class absensiteammodel {
    private String nik_baru;
    private String nama_karyawan_struktur;
    private String jabatan_struktur;
    private String lokasi_struktur;
    private String dept_struktur;

    public absensiteammodel(String nik_baru, String nama_karyawan_struktur, String jabatan_struktur, String lokasi_struktur, String dept_struktur) {
        this.nik_baru = nik_baru;
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.jabatan_struktur = jabatan_struktur;
        this.lokasi_struktur = lokasi_struktur;
        this.dept_struktur = dept_struktur;
    }


    public String getNikbaru() {return nik_baru; }

    public String getNama_karyawan_struktur() {
        return nama_karyawan_struktur;
    }

    public String getJabatan_struktur() {
        return jabatan_struktur;
    }

    public String getLokasi_struktur() {
        return lokasi_struktur;
    }

    public String getDept_struktur() { return dept_struktur; }

    @Override
    public String toString() {
        return nama_karyawan_struktur;
    }

}
