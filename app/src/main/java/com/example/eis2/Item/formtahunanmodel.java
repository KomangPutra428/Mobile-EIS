package com.example.eis2.Item;

public class formtahunanmodel {
    private String id_sisa_cuti;
    private String no_pengajuan_tahunan;
    private String tanggal_pengajuan;
    private String nik_baru;
    private String opsi_cuti_tahunan;
    private String tanggal_absen;
    private String ket_tambahan_tahunan;
    private String dok_cuti_tahunan;
    private String lat;
    private String lon;
    public formtahunanmodel(String id_sisa_cuti, String no_pengajuan_tahunan, String tanggal_pengajuan, String nik_baru,
                           String opsi_cuti_tahunan, String tanggal_absen, String ket_tambahan_tahunan, String dok_cuti_tahunan, String lat, String lon){
        this.id_sisa_cuti = id_sisa_cuti;
        this.no_pengajuan_tahunan=no_pengajuan_tahunan;
        this.tanggal_pengajuan=tanggal_pengajuan;
        this.nik_baru=nik_baru;
        this.opsi_cuti_tahunan=opsi_cuti_tahunan;
        this.tanggal_absen=tanggal_absen;
        this.ket_tambahan_tahunan=ket_tambahan_tahunan;
        this.dok_cuti_tahunan=dok_cuti_tahunan;
        this.lat = lat;
        this.lon = lon;
    }
    public String getId_sisa_cuti() { return id_sisa_cuti; }
    public String getNo_pengajuan_tahunan() { return no_pengajuan_tahunan; }
    public String getTanggal_pengajuan() { return tanggal_pengajuan; }
    public String getNik_baru() { return nik_baru; }

    public String getOpsi_cuti_tahunan() { return opsi_cuti_tahunan; }
    public String getTanggal_absen() { return tanggal_absen; }
    public String getKet_tambahan_tahunan() { return ket_tambahan_tahunan; }

    public String getDok_cuti_tahunan() { return dok_cuti_tahunan; }
    public String getLat() {return lat; }
    public String getLon() {return lon; }
}
