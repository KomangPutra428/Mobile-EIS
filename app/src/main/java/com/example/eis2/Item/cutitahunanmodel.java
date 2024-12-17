package com.example.eis2.Item;

public class cutitahunanmodel {
    String tanggal_deadline,
            id_sisa_cuti,
            tanggal_absen,
            opsi_cuti_tahunan,
            ket_tambahan_tahunan,
            status_cuti_tahunan,
            tanggal_cuti_tahunan,
            feedback_cuti_tahunan,
            status_cuti_tahunan_2,
            tanggal_cuti_tahunan_2,
            feedback_cuti_tahunan_2;


    public cutitahunanmodel(String tanggal_deadline, String id_sisa_cuti, String tanggal_absen, String opsi_cuti_tahunan, String ket_tambahan_tahunan, String status_cuti_tahunan, String tanggal_cuti_tahunan, String feedback_cuti_tahunan, String status_cuti_tahunan_2, String tanggal_cuti_tahunan_2, String feedback_cuti_tahunan_2) {
        this.tanggal_deadline = tanggal_deadline;
        this.id_sisa_cuti = id_sisa_cuti;
        this.tanggal_absen = tanggal_absen;
        this.opsi_cuti_tahunan = opsi_cuti_tahunan;
        this.ket_tambahan_tahunan = ket_tambahan_tahunan;
        this.status_cuti_tahunan = status_cuti_tahunan;
        this.tanggal_cuti_tahunan =  tanggal_cuti_tahunan;
        this.feedback_cuti_tahunan = feedback_cuti_tahunan;
        this.status_cuti_tahunan_2 = status_cuti_tahunan_2;
        this.tanggal_cuti_tahunan_2 =  tanggal_cuti_tahunan_2;
        this.feedback_cuti_tahunan_2 = feedback_cuti_tahunan_2;
    }

    public String getTanggal_deadline() {
        return tanggal_deadline;
    }
    public String getId_sisa_cuti() {
        return id_sisa_cuti;
    }
    public String getTanggal_absen() { return tanggal_absen; }

    public String getOpsi_cuti_tahunan() { return opsi_cuti_tahunan; }

    public String getKet_tambahan_tahunan() { return ket_tambahan_tahunan; }
    public String getStatus_cuti_tahunan() {
        return status_cuti_tahunan;
    }
    public String getTanggal_cuti_tahunan() {
        return tanggal_cuti_tahunan;
    }
    public String getFeedback_cuti_tahunan() {
        return feedback_cuti_tahunan;
    }
    public String getStatus_cuti_tahunan_2() {
        return status_cuti_tahunan_2;
    }
    public String getTanggal_cuti_tahunan_2() {
        return tanggal_cuti_tahunan_2;
    }
    public String getFeedback_cuti_tahunan_2() {
        return feedback_cuti_tahunan_2;
    }
}
