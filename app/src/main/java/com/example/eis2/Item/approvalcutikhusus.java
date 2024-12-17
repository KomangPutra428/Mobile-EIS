package com.example.eis2.Item;

public class approvalcutikhusus {
    private String id_cuti_khusus;
    private String nik_baru;
    private String tanggal_pengajuan;
    private String nama_karyawan_struktur;
    private String start_cuti_khusus;
    private String lokasi_struktur;

    private String ket_tambahan_khusus;
    private String jenis_cuti_khusus;
    private String kondisi;
    private String status_cuti_khusus;
    private String feedback_cuti_khusus;

    public approvalcutikhusus(String id_cuti_khusus, String nik_baru, String tanggal_pengajuan, String nama_karyawan_struktur, String start_cuti_khusus,
                              String lokasi_struktur, String ket_tambahan_khusus, String jenis_cuti_khusus, String kondisi, String status_cuti_khusus,
                              String feedback_cuti_khusus){
        this.id_cuti_khusus=id_cuti_khusus;
        this.nik_baru = nik_baru;
        this.tanggal_pengajuan=tanggal_pengajuan;
        this.nama_karyawan_struktur=nama_karyawan_struktur;
        this.start_cuti_khusus=start_cuti_khusus;
        this.lokasi_struktur=lokasi_struktur;
        this.ket_tambahan_khusus=ket_tambahan_khusus;

        this.jenis_cuti_khusus=jenis_cuti_khusus;
        this.kondisi=kondisi;
        this.status_cuti_khusus=status_cuti_khusus;
        this.feedback_cuti_khusus=feedback_cuti_khusus;
    }
    public String getId_cuti_khusus() {return id_cuti_khusus; }
    public String getNik_baru() {return nik_baru; }

    public String getTanggal_pengajuan() {return tanggal_pengajuan; }
    public String getNama_karyawan_struktur() {return nama_karyawan_struktur; }
    public String getStart_cuti_khusus() {return start_cuti_khusus; }
    public String getLokasi_struktur() {return lokasi_struktur; }

    public String getKet_tambahan_khusus() {return ket_tambahan_khusus; }
    public String getJenis_cuti_khusus() {return jenis_cuti_khusus; }

    public String getKondisi() { return kondisi; }

    public String getStatus_cuti_khusus() {return status_cuti_khusus; }
    public String getFeedback_cuti_khusus() {return feedback_cuti_khusus; }
}
