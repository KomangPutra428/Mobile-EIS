package com.example.eis2.Item;

public class approvaldinasnonfullmodel {
    private String id_non_full;
    private String tanggal_pengajuan;
    private String nama_karyawan_struktur;
    private String lokasi_struktur;
    private String jenis_non_full;
    private String tanggal_non_full;
    private String ket_tambahan_non_full;
    private String status_non_full;
    private String feedback_non_full;

    public approvaldinasnonfullmodel(String id_non_full, String tanggal_pengajuan, String nama_karyawan_struktur, String lokasi_struktur,
                                     String jenis_non_full, String tanggal_non_full, String ket_tambahan_non_full, String status_non_full,
                                     String feedback_non_full){
        this.id_non_full = id_non_full;
        this.tanggal_pengajuan=tanggal_pengajuan;
        this.nama_karyawan_struktur=nama_karyawan_struktur;
        this.lokasi_struktur=lokasi_struktur;
        this.jenis_non_full=jenis_non_full;
        this.tanggal_non_full=tanggal_non_full;
        this.ket_tambahan_non_full=ket_tambahan_non_full;
        this.status_non_full=status_non_full;
        this.feedback_non_full=feedback_non_full;
    }
    public String getId_non_full() {return id_non_full; }
    public String getTanggal_pengajuan() { return tanggal_pengajuan; }
    public String getNama_karyawan_struktur() {return nama_karyawan_struktur; }
    public String getLokasi_struktur() {return lokasi_struktur; }

    public String getJenis_non_full() {return jenis_non_full; }
    public String getTanggal_non_full() {return tanggal_non_full; }
    public String getKet_tambahan_non_full() {return ket_tambahan_non_full; }
    public String getStatus_non_full() {return status_non_full; }

    public String getFeedback_non_full() {return feedback_non_full; }



}
