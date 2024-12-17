package com.example.eis2.Item;

public class mutasilistmodel {
    private String tanggal_pengajuan;
    private String id_mutasi_rotasi;
    private String permintaan;
    private String nik_baru;
    private String nama_karyawan_mutasi;
    private String lokasi_awal;
    private String status_atasan;
    private String status_manager;

    public mutasilistmodel(String tanggal_pengajuan, String id_mutasi_rotasi , String permintaan, String nik_baru, String nama_karyawan_mutasi, String lokasi_awal, String status_atasan, String status_manager){
        this.tanggal_pengajuan = tanggal_pengajuan;
        this.id_mutasi_rotasi = id_mutasi_rotasi;
        this.permintaan = permintaan;
        this.nik_baru = nik_baru;
        this.nama_karyawan_mutasi = nama_karyawan_mutasi;
        this.lokasi_awal = lokasi_awal;
        this.status_atasan = status_atasan;
        this.status_manager = status_manager;
    }

    public String getId_mutasi_rotasi() {
        return id_mutasi_rotasi;
    }

    public String getTanggal_pengajuan() { return tanggal_pengajuan; }

    public String getPermintaan() {
        return permintaan;
    }

    public String getNik_baru() {
        return nik_baru;
    }

    public String getNama_karyawan_mutasi() {
        return nama_karyawan_mutasi;
    }

    public String getLokasi_awal() {
        return lokasi_awal;
    }

    public String getStatus_atasan() {
        return status_atasan;
    }

    public String getStatus_manager() {
        return status_manager;
    }
}
