package com.example.eis2.Item;

public class rekomendasimodel {
    private String dd_id;
    private String di_no_ktp;
    private String dd_tanggal_lahir;
    private String di_posisi;
    private String departement;
    private String di_nama_lengkap;
    private String di_cv_pelamar;
    private String di_nilai_cv_calon_karyawan;
    private String di_nilai_validitas_calon_karyawan;
    private String status;

    public rekomendasimodel(String dd_id, String di_no_ktp, String dd_tanggal_lahir, String di_posisi, String departement,
                            String di_nama_lengkap, String di_cv_pelamar,
                            String di_nilai_cv_calon_karyawan, String di_nilai_validitas_calon_karyawan, String status) {
        this.dd_id = dd_id;
        this.di_no_ktp = di_no_ktp;
        this.dd_tanggal_lahir = dd_tanggal_lahir;
        this.di_posisi = di_posisi;
        this.departement = departement;
        this.di_nama_lengkap = di_nama_lengkap;
        this.di_cv_pelamar = di_cv_pelamar;
        this.di_nilai_cv_calon_karyawan = di_nilai_cv_calon_karyawan;
        this.di_nilai_validitas_calon_karyawan = di_nilai_validitas_calon_karyawan;
        this.status = status;
    }

    public String getDd_id() { return dd_id; }
    public String getDi_no_ktp() { return di_no_ktp; }
    public String getDd_tanggal_lahir() { return dd_tanggal_lahir; }
    public String getDi_posisi() { return di_posisi; }
    public String getDi_nama_lengkap() { return di_nama_lengkap; }
    public String getDepartement() { return departement; }

    public String getDi_cv_pelamar() { return di_cv_pelamar; }
    public String getDi_nilai_cv_calon_karyawan() { return di_nilai_cv_calon_karyawan; }
    public String getDi_nilai_validitas_calon_karyawan() { return di_nilai_validitas_calon_karyawan; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return di_nama_lengkap;
    }

}
