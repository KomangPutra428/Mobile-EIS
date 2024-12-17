package com.example.eis2.Item;

public class CDTmodel {
    private String id_full_day;
    private String no_pengajuan_full_day;
    private String nik_baru;
    private String nama_karyawan_struktur;
    private String tanggal_deadline;
    private String lokasi_struktur;
    private String jenis_full_day;
    private String tanggal_absen;
    private String karyawan_pengganti;
    private String ket_tambahan;
    private String upload_full_day;
    private String status_full_day;
    private String status_full_day_2;
    private String feedback_full_day;
    private String feedback_full_day_2;

    public CDTmodel(String id_full_day, String no_pengajuan_full_day, String nik_baru, String nama_karyawan_struktur, String tanggal_deadline,
                        String lokasi_struktur, String jenis_full_day, String tanggal_absen, String karyawan_pengganti,
                        String ket_tambahan, String upload_full_day, String status_full_day, String status_full_day_2, String feedback_full_day,
                        String feedback_full_day_2) {
        this.id_full_day=id_full_day;
        this.no_pengajuan_full_day = no_pengajuan_full_day;
        this.nik_baru = nik_baru;
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.tanggal_deadline = tanggal_deadline;
        this.lokasi_struktur = lokasi_struktur;
        this.jenis_full_day = jenis_full_day;
        this.tanggal_absen = tanggal_absen;
        this.karyawan_pengganti = karyawan_pengganti;
        this.ket_tambahan = ket_tambahan;
        this.upload_full_day = upload_full_day;
        this.status_full_day = status_full_day;
        this.status_full_day_2 = status_full_day_2;
        this.feedback_full_day = feedback_full_day;
        this.feedback_full_day_2 = feedback_full_day_2;


    }
    public String getId_full_day() {return id_full_day; }
    public String getNo_pengajuan_full_day() {return no_pengajuan_full_day; }
    public String getNik_baru() {return nik_baru; }
    public String getNama_karyawan_struktur() {return nama_karyawan_struktur; }
    public String getTanggal_deadline() {return tanggal_deadline; }
    public String getLokasi_struktur() {return lokasi_struktur; }
    public String getJenis_full_day() {return jenis_full_day; }
    public String getTanggal_absen() {return tanggal_absen; }
    public String getKaryawan_pengganti() {return karyawan_pengganti; }
    public String getKet_tambahan() {return ket_tambahan; }
    public String getUpload_full_day() {return upload_full_day; }
    public String getStatus_full_day() {return status_full_day; }
    public String getStatus_full_day_2() {return status_full_day_2; }

    public String getFeedback_full_day() {
        return feedback_full_day;
    }

    public String getFeedback_full_day_2() {
        return feedback_full_day_2;
    }
}