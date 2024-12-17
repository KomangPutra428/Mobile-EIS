package com.example.eis2.Item;

public class approvaldinasfull {
    private String id_full_day;
    private String tanggal_pengajuan;
    private String nama_karyawan_struktur;
    private String lokasi_struktur;

    private String jenis_full_day;
    private String start_full_day;
    private String karyawan_pengganti;
    private String ket_tambahan;
    private String status_full_day;
    private String feedback_full_day;

    public approvaldinasfull(String id_full_day, String tanggal_pengajuan, String nama_karyawan_struktur, String lokasi_struktur, String jenis_full_day, String start_full_day,
                             String karyawan_pengganti, String ket_tambahan, String status_full_day, String feedback_full_day){
        this.id_full_day=id_full_day;
        this.tanggal_pengajuan=tanggal_pengajuan;
        this.jenis_full_day=jenis_full_day;
        this.nama_karyawan_struktur=nama_karyawan_struktur;
        this.lokasi_struktur=lokasi_struktur;
        this.start_full_day=start_full_day;
        this.karyawan_pengganti=karyawan_pengganti;
        this.ket_tambahan=ket_tambahan;
        this.status_full_day=status_full_day;
        this.feedback_full_day=feedback_full_day;
    }
    public String getId_full_day() {return id_full_day; }
    public String getTanggal_pengajuan() {return tanggal_pengajuan; }

    public String getNama_karyawan_struktur() { return nama_karyawan_struktur; }
    public String getLokasi_struktur() { return lokasi_struktur; }


    public String getJenis_full_day() {return jenis_full_day; }
    public String getStart_full_day() {return start_full_day; }

    public String getKaryawan_pengganti() {return karyawan_pengganti; }
    public String getKet_tambahan() {return ket_tambahan; }
    public String getStatus_full_day() {return status_full_day; }
    public String getFeedback_full_day() {return feedback_full_day; }

}
