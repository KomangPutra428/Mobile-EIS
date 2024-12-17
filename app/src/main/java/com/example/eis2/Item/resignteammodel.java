package com.example.eis2.Item;

public class resignteammodel {
    private String id;
    private String nik_baru;
    private String alasan_resign;
    private String klarifikasi_resign;
    private String nama_karyawan_struktur;
    private String jabatan_karyawan;
    private String submit_date;
    private String tanggal_pengajuan;
    private String tanggal_efektif_resign;
    private String status_atasan;
    private String status_atasan_2;

    public resignteammodel(String id, String nik_baru, String alasan_resign, String klarifikasi_resign, String nama_karyawan_struktur, String jabatan_karyawan,
                           String submit_date, String tanggal_pengajuan, String tanggal_efektif_resign,
                       String status_atasan, String status_atasan_2) {
        this.id = id;
        this.nik_baru = nik_baru;
        this.alasan_resign = alasan_resign;
        this.klarifikasi_resign = klarifikasi_resign;
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.jabatan_karyawan = jabatan_karyawan;
        this.submit_date = submit_date;
        this.tanggal_pengajuan = tanggal_pengajuan;
        this.tanggal_efektif_resign = tanggal_efektif_resign;
        this.status_atasan = status_atasan;
        this.status_atasan_2 = status_atasan_2;
    }

    public String getId() { return id; }
    public String getNik_baru() { return nik_baru; }
    public String getAlasan_resign() { return alasan_resign; }
    public String getKlarifikasi_resign() { return klarifikasi_resign; }

    public String getNama_karyawan_struktur() { return nama_karyawan_struktur; }
    public String getJabatan_karyawan() { return jabatan_karyawan; }

    public String getSubmit_date() { return submit_date; }
    public String getTanggal_pengajuan() { return tanggal_pengajuan; }
    public String getTanggal_efektif_resign() { return tanggal_efektif_resign; }
    public String getStatus_atasan() { return status_atasan; }
    public String getStatus_atasan_2() { return status_atasan_2; }

    @Override
    public String toString() {
        return nama_karyawan_struktur;
    }

}
