package com.example.eis2.Item;

public class namanikmodel {
    private String nama_karyawan_struktur,level_jabatan_karyawan,lokasi_struktur, jabatan_struktur, perusahaan_struktur;

    public namanikmodel(String nama_karyawan_struktur, String level_jabatan_karyawan,String lokasi_struktur,String jabatan_struktur,
                        String perusahaan_struktur) {
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.level_jabatan_karyawan = level_jabatan_karyawan;
        this.lokasi_struktur = lokasi_struktur;
        this.jabatan_struktur = jabatan_struktur;
        this.perusahaan_struktur = perusahaan_struktur;


    }
    public String getNama_karyawan_struktur() {
        return nama_karyawan_struktur;
    }
    public String getLevel_jabatan_karyawan() {
        return level_jabatan_karyawan;
    }
    public String getLokasi_struktur() {
        return lokasi_struktur;
    }
    public String getJabatan_struktur() {
        return jabatan_struktur;
    }

    public String getPerusahaan_struktur() { return perusahaan_struktur; }
}
