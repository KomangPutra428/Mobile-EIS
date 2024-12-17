package com.example.eis2.Item;

public class formkhususmodel {
    private String id_cuti_khusus;
    private String no_pengajuan_khusus;
    private String tanggal_pengajuan;
    private String nik_baru;
    private String jenis_cuti_khusus;
    private String kondisi;
    private String tanggal_absen;
    private String ket_tambahan_khusus;
    private String dokumen_cuti_khusus;
    private String lat;
    private String lon;

    public formkhususmodel(String id_cuti_khusus, String no_pengajuan_khusus, String tanggal_pengajuan, String nik_baru,
                           String jenis_cuti_khusus, String kondisi, String tanggal_absen, String ket_tambahan_khusus,
                           String dokumen_cuti_khusus, String lat, String lon){
        this.id_cuti_khusus = id_cuti_khusus;
        this.no_pengajuan_khusus=no_pengajuan_khusus;
        this.tanggal_pengajuan=tanggal_pengajuan;
        this.nik_baru=nik_baru;
        this.jenis_cuti_khusus=jenis_cuti_khusus;
        this.kondisi=kondisi;
        this.tanggal_absen=tanggal_absen;
        this.ket_tambahan_khusus=ket_tambahan_khusus;
        this.dokumen_cuti_khusus=dokumen_cuti_khusus;
        this.lat = lat;
        this.lon = lon;
    }
    public String getId_cuti_khusus() { return id_cuti_khusus; }
    public String getNo_pengajuan_khusus() { return no_pengajuan_khusus; }
    public String getTanggal_pengajuan() { return tanggal_pengajuan; }
    public String getNik_baru() { return nik_baru; }

    public String getJenis_cuti_khusus() { return jenis_cuti_khusus; }
    public String getKondisi() { return kondisi; }
    public String getTanggal_absen() { return tanggal_absen; }
    public String getKet_tambahan_khusus() { return ket_tambahan_khusus; }

    public String getDokumen_cuti_khusus() { return dokumen_cuti_khusus; }
    public String getLat() {return lat; }
    public String getLon() {return lon; }
}
