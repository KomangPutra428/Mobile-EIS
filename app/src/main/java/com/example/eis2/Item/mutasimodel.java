package com.example.eis2.Item;

public class mutasimodel {
    private String no_pengajuan;
    private String permintaan;
    private String nama_karyawan_mutasi;
    private String pt_awal;
    private String dept_awal;
    private String lokasi_awal;
    private String jabatan_awal;
    private String jabatan_baru;
    private String nama_jabatan_baru;
    private String rekomendasi_tanggal;
    private String status_1;
    private String tanggal_approval;

    public mutasimodel(String no_pengajuan, String permintaan, String nama_karyawan_mutasi, String pt_awal,
                       String dept_awal, String lokasi_awal, String jabatan_awal, String jabatan_baru, String nama_jabatan_baru,
                       String rekomendasi_tanggal, String status_1, String tanggal_approval){
        this.no_pengajuan = no_pengajuan;
        this.permintaan = permintaan;
        this.nama_karyawan_mutasi = nama_karyawan_mutasi;
        this.pt_awal = pt_awal;
        this.dept_awal = dept_awal;
        this.lokasi_awal = lokasi_awal;
        this.jabatan_awal = jabatan_awal;
        this.jabatan_baru = jabatan_baru;
        this.nama_jabatan_baru = nama_jabatan_baru;
        this.rekomendasi_tanggal = rekomendasi_tanggal;
        this.status_1 = status_1;
        this.tanggal_approval = tanggal_approval;
    }

    public String getNo_pengajuan() { return no_pengajuan; }
    public String getPermintaan() { return permintaan; }
    public String getNama_karyawan_mutasi() { return nama_karyawan_mutasi; }
    public String getPt_awal() { return pt_awal; }

    public String getDept_awal() { return dept_awal; }
    public String getLokasi_awal() { return lokasi_awal; }

    public String getJabatan_awal() {
        return jabatan_awal;
    }

    public String getJabatan_baru() { return jabatan_baru; }

    public String getNama_jabatan_baru() { return nama_jabatan_baru; }

    public String getRekomendasi_tanggal() { return rekomendasi_tanggal; }
    public String getStatus_1() { return status_1; }
    public String getTanggal_approval() { return tanggal_approval; }

    @Override
    public String toString() {
        return nama_karyawan_mutasi;
    }
}
