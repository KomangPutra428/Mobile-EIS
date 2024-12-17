package com.example.eis2.Item;

public class nonfullmodel {
    private String id_non_full;
    private String no_pengajuan_non_full;
    private String nik_baru;
    private String nama_karyawan_struktur;
    private String jabatan_non_full;
    private String lokasi_struktur;
    private String jenis_non_full;
    private String tanggal_absen;
    private String ket_tambahan_non_full;
    private String lat;
    private String lon;

    public nonfullmodel(String id_non_full, String no_pengajuan_non_full, String nik_baru, String nama_karyawan_struktur, String jabatan_non_full,
                        String lokasi_struktur, String jenis_non_full, String tanggal_absen, String ket_tambahan_non_full, String lat, String lon) {
        this.id_non_full=id_non_full;
        this.no_pengajuan_non_full = no_pengajuan_non_full;
        this.nik_baru = nik_baru;
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.jabatan_non_full = jabatan_non_full;
        this.lokasi_struktur = lokasi_struktur;
        this.jenis_non_full = jenis_non_full;
        this.tanggal_absen = tanggal_absen;
        this.ket_tambahan_non_full = ket_tambahan_non_full;
        this.lat = lat;
        this.lon = lon;
    }
    public String getId_non_full() {return id_non_full; }
    public String getNo_pengajuan_non_full() {return no_pengajuan_non_full; }
    public String getNik_baru() {return nik_baru; }
    public String getNama_karyawan_struktur() {return nama_karyawan_struktur; }

    public String getJabatan_non_full() {return jabatan_non_full; }
    public String getLokasi_struktur() {return lokasi_struktur; }
    public String getJenis_non_full() {return jenis_non_full; }
    public String getTanggal_absen() {return tanggal_absen; }

    public String getKet_tambahan_non_full() {return ket_tambahan_non_full; }

    public String getLat() {return lat; }
    public String getLon() {return lon; }

}
