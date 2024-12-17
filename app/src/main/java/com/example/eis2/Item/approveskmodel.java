package com.example.eis2.Item;

public class approveskmodel {
    private String id;
    private String submit_date;
    private String no_urut;
    private String nik_baru;
    private String jabatan_karyawan;
    private String nama_karyawan_struktur;
    private String keperluan;
    private String analisa;
    private String status_atasan;
    private String status_hrd;
    private String lokasi_struktur;

    public approveskmodel(String id, String submit_date, String no_urut, String nik_baru, String jabatan_karyawan, String nama_karyawan_struktur, String keperluan,
                          String analisa, String status_atasan, String status_hrd, String lokasi_struktur){
        this.id = id;
        this.submit_date = submit_date;
        this.no_urut = no_urut;
        this.nik_baru = nik_baru;
        this.jabatan_karyawan = jabatan_karyawan;
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.keperluan = keperluan;
        this.analisa = analisa;
        this.status_atasan = status_atasan;
        this.status_hrd = status_hrd;
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.lokasi_struktur = lokasi_struktur;

    }

    public String getId() {
        return id;
    }

    public String getSubmit_date() { return submit_date; }

    public String getNo_urut() {
        return no_urut;
    }

    public String getNik_baru() {
        return nik_baru;
    }

    public String getJabatan_karyawan() { return jabatan_karyawan; }

    public String getNama_karyawan_struktur() {
        return nama_karyawan_struktur;
    }

    public String getKeperluan() {
        return keperluan;
    }

    public String getAnalisa() {
        return analisa;
    }

    public String getStatus_atasan() {
        return status_atasan;
    }

    public String getStatus_hrd() {
        return status_hrd;
    }

    public String getLokasi_struktur() {
        return lokasi_struktur;
    }

    @Override
    public String toString() {
        return nama_karyawan_struktur;
    }
}

