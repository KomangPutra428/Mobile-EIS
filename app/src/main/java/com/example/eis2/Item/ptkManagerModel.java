package com.example.eis2.Item;

public class ptkManagerModel {
    private String id;
    private String no_ptk;
    private String submit_date;
    private String nik_pengajuan;
    private String jabatan_karyawan;
    private String depo_ptk;
    private String dept_ptk;
    private String analisa;
    private String tenaga_kerja;
    private String status_atasan;
    private String status_hrd;
    private String status_manager;

    private String tanggal_manager;

    public ptkManagerModel( String id, String submit_date, String nik_pengajuan, String jabatan_karyawan,
                            String depo_ptk,
                            String dept_ptk, String analisa, String tenaga_kerja, String status_atasan,
                            String status_hrd, String status_manager, String tanggal_manager, String no_ptk) {
        this.id = id;
        this.submit_date = submit_date;
        this.nik_pengajuan = nik_pengajuan;
        this.jabatan_karyawan = jabatan_karyawan;
        this.depo_ptk = depo_ptk;
        this.dept_ptk = dept_ptk;
        this.analisa = analisa;
        this.tenaga_kerja = tenaga_kerja;
        this.status_atasan = status_atasan;
        this.status_hrd = status_hrd;
        this.status_manager = status_manager;
        this.tanggal_manager = tanggal_manager;
        this.no_ptk = no_ptk;

    }

    public String getId() { return id; }
    public String getSubmit_date() { return submit_date; }
    public String getNik_pengajuan() { return nik_pengajuan; }
    public String getJabatan_karyawan() { return jabatan_karyawan; }

    public String getDepo_ptk() { return depo_ptk; }
    public String getDept_ptk() { return dept_ptk; }
    public String getAnalisa() { return analisa; }
    public String getTenaga_kerja() { return tenaga_kerja; }

    public String getStatus_atasan() { return status_atasan; }
    public String getStatus_hrd() { return status_hrd; }

    public String getTanggal_manager() {
        return tanggal_manager;
    }

    public String getStatus_manager() {
        return status_manager;
    }

    public String getNo_ptk() {
        return no_ptk;
    }

    @Override
    public String toString() {
        return jabatan_karyawan;
    }
}
