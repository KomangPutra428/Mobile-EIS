package com.example.eis2.Item;

public class mutasiapprovalmodel {
    private String id_mutasi_rotasi,nama_karyawan_mutasi,permintaan, pt_baru, dept_baru, lokasi_baru, nama_jabatan_baru;

    public mutasiapprovalmodel(String id_mutasi_rotasi, String nama_karyawan_mutasi,String permintaan,String pt_baru,
                        String dept_baru, String lokasi_baru, String nama_jabatan_baru) {
        this.id_mutasi_rotasi = id_mutasi_rotasi;
        this.nama_karyawan_mutasi = nama_karyawan_mutasi;
        this.permintaan = permintaan;
        this.pt_baru = pt_baru;
        this.dept_baru = dept_baru;
        this.lokasi_baru = lokasi_baru;
        this.nama_jabatan_baru = nama_jabatan_baru;
    }

    public String getId_mutasi_rotasi() {
        return id_mutasi_rotasi;
    }

    public String getNama_karyawan_mutasi() {
        return nama_karyawan_mutasi;
    }

    public String getPermintaan() {
        return permintaan;
    }

    public String getPt_baru() {
        return pt_baru;
    }

    public String getDept_baru() {
        return dept_baru;
    }

    public String getLokasi_baru() {
        return lokasi_baru;
    }

    public String getNama_jabatan_baru() {
        return nama_jabatan_baru;
    }
}
