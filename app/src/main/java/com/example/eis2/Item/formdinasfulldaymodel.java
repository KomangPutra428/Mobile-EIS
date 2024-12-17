package com.example.eis2.Item;

public class formdinasfulldaymodel {
    private String id_full_day;
    private String no_pengajuan_full_day;
    private String nik_baru;
    private String jabatan_full_day;
    private String jenis_full_day;
    private String tanggal_absen;
    private String karyawan_pengganti;
    private String ket_tambahan;
    private String lat;
    private String lon;

    public formdinasfulldaymodel(String id_full_day, String no_pengajuan_full_day, String nik_baru, String jabatan_full_day,
                                 String jenis_full_day, String tanggal_absen, String karyawan_pengganti, String ket_tambahan,
                                 String lat, String lon){
        this.id_full_day = id_full_day;
        this.no_pengajuan_full_day=no_pengajuan_full_day;
        this.nik_baru=nik_baru;
        this.jabatan_full_day=jabatan_full_day;
        this.jenis_full_day=jenis_full_day;
        this.tanggal_absen=tanggal_absen;
        this.karyawan_pengganti=karyawan_pengganti;
        this.ket_tambahan=ket_tambahan;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId_full_day() { return id_full_day; }
    public String getNo_pengajuan_full_day() { return no_pengajuan_full_day; }
    public String getNik_baru() { return nik_baru; }
    public String getJabatan_full_day() { return jabatan_full_day; }

    public String getJenis_full_day() { return jenis_full_day; }
    public String getTanggal_absen() { return tanggal_absen; }
    public String getKaryawan_pengganti() { return karyawan_pengganti; }
    public String getKet_tambahan() { return ket_tambahan; }

    public String getLat() {return lat; }
    public String getLon() {return lon; }


}
