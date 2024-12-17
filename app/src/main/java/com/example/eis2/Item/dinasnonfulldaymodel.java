package com.example.eis2.Item;

public class dinasnonfulldaymodel {
    private String tanggal_deadline;
    private String no_pengajuan_full_day;
    private String nama_karyawan_struktur;
    private String jenis_full_day;
    private String tanggal_absen;
    private String ket_tambahan;
    private String status_full_day;
    private String tanggal_approval;
    private String feedback_full_day;
    private String status_full_day_2;
    private String tanggal_approval_2;
    private String feedback_full_day_2;

    public dinasnonfulldaymodel(String tanggal_deadline, String no_pengajuan_full_day, String nama_karyawan_struktur, String jenis_full_day, String tanggal_absen,
                                String ket_tambahan, String status_full_day, String tanggal_approval, String feedback_full_day,
                                String status_full_day_2, String tanggal_approval_2, String feedback_full_day_2){
        this.tanggal_deadline = tanggal_deadline;
        this.no_pengajuan_full_day = no_pengajuan_full_day;
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.jenis_full_day = jenis_full_day;
        this.tanggal_absen = tanggal_absen;
        this.ket_tambahan = ket_tambahan;

        this.status_full_day = status_full_day;
        this.tanggal_approval = tanggal_approval;
        this.feedback_full_day = feedback_full_day;

        this.status_full_day_2 = status_full_day_2;
        this.tanggal_approval_2 = tanggal_approval_2;
        this.feedback_full_day_2 = feedback_full_day_2;
    }
    public String getTanggal_deadline() {return tanggal_deadline; }
    public String getNo_pengajuan_full_day() {
        return no_pengajuan_full_day;
    }

    public String getNama_karyawan_struktur() {
        return nama_karyawan_struktur;
    }

    public String getJenis_full_day() {
        return jenis_full_day;
    }
    public String getTanggal_absen() {
        return tanggal_absen;
    }


    public String getKet_tambahan() {
        return ket_tambahan;
    }


    public String getStatus_full_day() {
        return status_full_day;
    }
    public String getTanggal_approval() {
        return tanggal_approval;
    }
    public String getFeedback_full_day() {
        return feedback_full_day;
    }

    public String getStatus_full_day_2() {
        return status_full_day_2;
    }
    public String getTanggal_approval_2() {
        return tanggal_approval_2;
    }
    public String getFeedback_full_day_2() {
        return feedback_full_day_2;
    }

}
