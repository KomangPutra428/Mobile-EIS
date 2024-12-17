package com.example.eis2.Item;

public class approvalcutitahunan {
    private String id_sisa_cuti;
    private String tanggal_pengajuan;
    private String nama_karyawan_struktur;
    private String start_cuti_tahunan;
    private String lokasi_struktur;

    private String ket_tambahan_tahunan;
    private String opsi_cuti_tahunan;
    private String status_cuti_tahunan;
    private String feedback_cuti_tahunan;


    public approvalcutitahunan(String id_sisa_cuti, String tanggal_pengajuan, String nama_karyawan_struktur, String start_cuti_tahunan,
                               String lokasi_struktur, String ket_tambahan_tahunan,
                              String opsi_cuti_tahunan, String status_cuti_tahunan, String feedback_cuti_tahunan){
        this.id_sisa_cuti=id_sisa_cuti;
        this.tanggal_pengajuan=tanggal_pengajuan;
        this.nama_karyawan_struktur=nama_karyawan_struktur;
        this.start_cuti_tahunan=start_cuti_tahunan;
        this.lokasi_struktur=lokasi_struktur;
        this.ket_tambahan_tahunan=ket_tambahan_tahunan;

        this.opsi_cuti_tahunan=opsi_cuti_tahunan;
        this.status_cuti_tahunan=status_cuti_tahunan;
        this.feedback_cuti_tahunan=feedback_cuti_tahunan;
    }
    public String getId_sisa_cuti() {return id_sisa_cuti; }
    public String getTanggal_pengajuan() {return tanggal_pengajuan; }
    public String getNama_karyawan_struktur() {return nama_karyawan_struktur; }
    public String getStart_cuti_tahunan() {return start_cuti_tahunan; }
    public String getLokasi_struktur() {return lokasi_struktur; }

    public String getKet_tambahan_tahunan() {return ket_tambahan_tahunan; }

    public String getOpsi_cuti_tahunan() {return opsi_cuti_tahunan; }
    public String getStatus_cuti_tahunan() {return status_cuti_tahunan; }
    public String getFeedback_cuti_tahunan() {return feedback_cuti_tahunan; }
}

