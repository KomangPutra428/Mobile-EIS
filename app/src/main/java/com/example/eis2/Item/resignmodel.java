package com.example.eis2.Item;

public class resignmodel {
    private String id;
    private String nik_baru;
    private String submit_date;
    private String tanggal_pengajuan;
    private String tanggal_efektif_resign;
    private String status_atasan;
    private String status_atasan_2;

    public resignmodel(String id, String nik_baru, String submit_date, String tanggal_pengajuan, String tanggal_efektif_resign,
                       String status_atasan, String status_atasan_2) {
        this.id = id;
        this.nik_baru = nik_baru;
        this.submit_date = submit_date;
        this.tanggal_pengajuan = tanggal_pengajuan;
        this.tanggal_efektif_resign = tanggal_efektif_resign;
        this.status_atasan = status_atasan;
        this.status_atasan_2 = status_atasan_2;
    }

    public String getId() { return id; }
    public String getNik_baru() { return nik_baru; }
    public String getSubmit_date() { return submit_date; }
    public String getTanggal_pengajuan() { return tanggal_pengajuan; }
    public String getTanggal_efektif_resign() { return tanggal_efektif_resign; }
    public String getStatus_atasan() { return status_atasan; }
    public String getStatus_atasan_2() { return status_atasan_2; }
}
