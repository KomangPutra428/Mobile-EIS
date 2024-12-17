package com.example.eis2.Item;

public class cutikhususmodel {
    String tanggal_deadline,
            id_cuti_khusus,
            jenis_cuti_khusus,
            kondisi,
            tanggal_absen,
            ket_tambahan_khusus,
            status_cuti_khusus,
            tanggal_approval_cuti_khusus,
            feedback_cuti_khusus,
            status_cuti_khusus_2,
            tanggal_approval_cuti_khusus_2,
            feedback_cuti_khusus_2;

    public cutikhususmodel(String tanggal_deadline, String id_cuti_khusus, String jenis_cuti_khusus, String kondisi,
                           String tanggal_absen, String ket_tambahan_khusus, String status_cuti_khusus, String tanggal_approval_cuti_khusus,
                           String feedback_cuti_khusus, String status_cuti_khusus_2, String tanggal_approval_cuti_khusus_2,
                           String feedback_cuti_khusus_2) {
        this.tanggal_deadline = tanggal_deadline;
        this.id_cuti_khusus = id_cuti_khusus;
        this.jenis_cuti_khusus = jenis_cuti_khusus;
        this.kondisi = kondisi;
        this.tanggal_absen = tanggal_absen;
        this.ket_tambahan_khusus = ket_tambahan_khusus;
        this.status_cuti_khusus = status_cuti_khusus;
        this.tanggal_approval_cuti_khusus = tanggal_approval_cuti_khusus;
        this.feedback_cuti_khusus = feedback_cuti_khusus;
        this.status_cuti_khusus_2 = status_cuti_khusus_2;
        this.tanggal_approval_cuti_khusus_2 = tanggal_approval_cuti_khusus_2;
        this.feedback_cuti_khusus_2 = feedback_cuti_khusus_2;
    }

    public String getTanggal_deadline() { return tanggal_deadline; }
    public String getId_cuti_khusus() { return id_cuti_khusus; }
    public String getJenis_cuti_khusus() { return jenis_cuti_khusus; }
    public String getKondisi() { return kondisi; }
    public String getTanggal_absen() { return tanggal_absen; }
    public String getKet_tambahan_khusus() { return ket_tambahan_khusus; }
    public String getStatus_cuti_khusus() { return status_cuti_khusus; }
    public String getTanggal_approval_cuti_khusus() { return tanggal_approval_cuti_khusus; }
    public String getFeedback_cuti_khusus() { return feedback_cuti_khusus; }
    public String getStatus_cuti_khusus_2() { return status_cuti_khusus_2; }
    public String getTanggal_approval_cuti_khusus_2() { return tanggal_approval_cuti_khusus_2; }
    public String getFeedback_cuti_khusus_2() { return feedback_cuti_khusus_2; }

}
