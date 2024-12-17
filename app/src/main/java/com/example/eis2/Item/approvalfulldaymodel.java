package com.example.eis2.Item;

public class approvalfulldaymodel {
    private String id_full_day;
    private String no_pengajuan_full_day;
    private String tanggal_pengajuan;
    private String lokasi_struktur;
    private String nama_karyawan_struktur;
    private String jenis_full_day;
    private String start_full_day;
    private String karyawan_pengganti;
    private String ket_tambahan;
    private String status_full_day;
    private String feedback_full_day;

    public approvalfulldaymodel(String id_full_day, String no_pengajuan_full_day, String tanggal_pengajuan, String lokasi_struktur,
                                String nama_karyawan_struktur, String jenis_full_day, String start_full_day,
                                String karyawan_pengganti, String ket_tambahan, String status_full_day, String feedback_full_day){

        this.id_full_day=id_full_day;
        this.no_pengajuan_full_day = no_pengajuan_full_day;
        this.tanggal_pengajuan = tanggal_pengajuan;
        this.lokasi_struktur = lokasi_struktur;
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.jenis_full_day = jenis_full_day;
        this.start_full_day = start_full_day;
        this.karyawan_pengganti = karyawan_pengganti;
        this.ket_tambahan = ket_tambahan;
        this.status_full_day = status_full_day;
        this.feedback_full_day = feedback_full_day;
    }
    public String getId_full_day() {return id_full_day; }
    public String getNo_pengajuan_full_day() {return no_pengajuan_full_day; }
    public String getTanggal_pengajuan() {return tanggal_pengajuan; }
    public String getLokasi_struktur() {return lokasi_struktur; }


    public String getNama_karyawan_struktur() {return nama_karyawan_struktur; }
    public String getJenis_full_day() {return jenis_full_day; }
    public String getStart_full_day() {return start_full_day; }

    public String getKaryawan_pengganti() {return karyawan_pengganti; }
    public String getKet_tambahan() {return ket_tambahan; }
    public String getStatus_full_day() {return status_full_day; }

    public String getFeedback_full_day() {return feedback_full_day; }

    @Override public String toString() { return nama_karyawan_struktur; }




}
