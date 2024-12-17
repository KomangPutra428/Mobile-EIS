package com.example.eis2.Item;

public class seragammodel {
    private String submit_date;
    private String no_pengajuan;
    private String ket_pengajuan;
    private String nik_baru;
    private String nama_karyawan_seragam;
    private String jabatan_karyawan_seragam;
    private String dept_karyawan_seragam;
    private String lokasi_karyawan_seragam;
    private String uniform;
    private String qty_seragam;
    private String total_harga;
    private String tanggal_distribusi;
    private String ket_tambahan;
    private String status_realisasi;
    private String status_distribusi;

    public seragammodel(String submit_date, String no_pengajuan, String ket_pengajuan, String nik_baru, String nama_karyawan_seragam, String jabatan_karyawan_seragam,
                        String dept_karyawan_seragam, String lokasi_karyawan_seragam, String uniform, String qty_seragam,
                        String total_harga, String tanggal_distribusi, String ket_tambahan, String status_realisasi, String status_distribusi){
        this.submit_date = submit_date;
        this.no_pengajuan = no_pengajuan;
        this.ket_pengajuan = ket_pengajuan;
        this.nik_baru = nik_baru;
        this.nama_karyawan_seragam = nama_karyawan_seragam;
        this.jabatan_karyawan_seragam = jabatan_karyawan_seragam;
        this.dept_karyawan_seragam = dept_karyawan_seragam;

        this.lokasi_karyawan_seragam = lokasi_karyawan_seragam;
        this.uniform = uniform;
        this.qty_seragam = qty_seragam;

        this.total_harga = total_harga;
        this.tanggal_distribusi = tanggal_distribusi;
        this.ket_tambahan = ket_tambahan;

        this.status_realisasi = status_realisasi;
        this.status_distribusi = status_distribusi;
    }

    public String getSubmit_date() { return submit_date; }
    public String getNo_pengajuan() { return no_pengajuan; }
    public String getKet_pengajuan() { return ket_pengajuan; }
    public String getNik_baru() { return nik_baru; }
    public String getNama_karyawan_seragam() { return nama_karyawan_seragam; }
    public String getJabatan_karyawan_seragam() { return jabatan_karyawan_seragam; }
    public String getDept_karyawan_seragam() { return dept_karyawan_seragam; }

    public String getLokasi_karyawan_seragam() { return lokasi_karyawan_seragam; }
    public String getUniform() { return uniform; }
    public String getQty_seragam() { return qty_seragam; }

    public String getTotal_harga() { return total_harga; }
    public String getTanggal_distribusi() { return tanggal_distribusi; }
    public String getKet_tambahan() { return ket_tambahan; }

    public String getStatus_realisasi() { return status_realisasi; }
    public String getStatus_distribusi() { return status_distribusi; }

    @Override public String toString() { return nama_karyawan_seragam; }

}
