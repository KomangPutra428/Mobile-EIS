package com.example.eis2.Item;

public class ptkmodel {
    private String no_pengajuan;
    private String id;
    private String submit_date;
    private String nik_pengajuan;
    private String jabatan_karyawan;
    private String level_jabatan;
    private String depo_ptk;
    private String dept_ptk;
    private String analisa;
    private String tenaga_kerja;
    private String status_atasan;
    private String status_hrd;

    private String status_cancel;

    public ptkmodel( String id, String submit_date, String nik_pengajuan, String jabatan_karyawan,
                     String level_jabatan, String depo_ptk,
                     String dept_ptk, String analisa, String tenaga_kerja, String status_atasan,
                     String status_hrd, String no_pengajuan, String status_cancel) {
        this.id = id;
        this.submit_date = submit_date;
        this.nik_pengajuan = nik_pengajuan;
        this.jabatan_karyawan = jabatan_karyawan;
        this.level_jabatan = level_jabatan;
        this.depo_ptk = depo_ptk;
        this.dept_ptk = dept_ptk;
        this.analisa = analisa;
        this.tenaga_kerja = tenaga_kerja;
        this.status_atasan = status_atasan;
        this.status_hrd = status_hrd;
        this.no_pengajuan = no_pengajuan;
        this.status_cancel = status_cancel;

    }

    public String getId() { return id; }
    public String getSubmit_date() { return submit_date; }
    public String getNik_pengajuan() { return nik_pengajuan; }
    public String getJabatan_karyawan() { return jabatan_karyawan; }
    public String getLevel_jabatan() { return level_jabatan; }

    public String getDepo_ptk() { return depo_ptk; }
    public String getDept_ptk() { return dept_ptk; }
    public String getAnalisa() { return analisa; }
    public String getTenaga_kerja() { return tenaga_kerja; }

    public String getStatus_atasan() { return status_atasan; }
    public String getStatus_hrd() { return status_hrd; }

    public String getNo_pengajuan() {
        return no_pengajuan;
    }

    public String getStatus_cancel() {
        return status_cancel;
    }

    @Override
    public String toString() {
        return jabatan_karyawan;
    }
}
