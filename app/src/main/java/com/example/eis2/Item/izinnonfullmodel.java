package com.example.eis2.Item;

public class izinnonfullmodel {
    String tanggal_deadline,
            no_pengajuan_non_full,
            jenis_non_full,
            tanggal_absen,
            ket_tambahan_non_full,
            status_non_full,
            tanggal_approval_non_full,
            feedback_non_full,
            status_non_full_2,
            tanggal_approval_non_full_2,
            feedback_non_full_2;

    public izinnonfullmodel(String tanggal_deadline, String no_pengajuan_non_full, String jenis_non_full, String tanggal_absen,
                            String ket_tambahan_non_full, String status_non_full, String tanggal_approval_non_full, String feedback_non_full,
                            String status_non_full_2, String tanggal_approval_non_full_2, String feedback_non_full_2){
       this.tanggal_deadline = tanggal_deadline;
       this.no_pengajuan_non_full = no_pengajuan_non_full;
       this.jenis_non_full = jenis_non_full;
       this.tanggal_absen = tanggal_absen;
       this.ket_tambahan_non_full = ket_tambahan_non_full;
       this.status_non_full = status_non_full;
       this.tanggal_approval_non_full = tanggal_approval_non_full;
       this.feedback_non_full = feedback_non_full;
       this.status_non_full_2 = status_non_full_2;
       this.tanggal_approval_non_full_2 = tanggal_approval_non_full_2;
       this.feedback_non_full_2 = feedback_non_full_2;
    }
    public String getTanggal_deadline() {
        return tanggal_deadline;
    }
    public String getNo_pengajuan_non_full() {
        return no_pengajuan_non_full;
    }
    public String getJenis_non_full() { return jenis_non_full; }
    public String getTanggal_absen() {return tanggal_absen; }

    public String getKet_tambahan_non_full() { return ket_tambahan_non_full; }
    public String getStatus_non_full() { return status_non_full; }
    public String getTanggal_approval_non_full() { return tanggal_approval_non_full; }
    public String getFeedback_non_full() { return feedback_non_full; }

    public String getStatus_non_full_2() { return status_non_full_2; }
    public String getTanggal_approval_non_full_2() { return tanggal_approval_non_full_2; }
    public String getFeedback_non_full_2() { return feedback_non_full_2; }
}
