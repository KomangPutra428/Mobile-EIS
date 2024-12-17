package com.example.eis2.Item;

public class serahterimamodels {
    private String id;
    private String submit_date;
    private String nik_baru;
    private String nama_karyawan_struktur;
    private String nik_penerima_1;
    private String status_penerima_1;
    private String tanggal_penerima_1;

    private String nik_penerima_2;
    private String status_penerima_2;
    private String tanggal_penerima_2;

    public serahterimamodels(String id, String submit_date, String nik_baru, String nama_karyawan_struktur,
                             String nik_penerima_1, String status_penerima_1, String tanggal_penerima_1,
                             String nik_penerima_2, String status_penerima_2, String tanggal_penerima_2){
        this.id=id;
        this.submit_date=submit_date;
        this.nik_baru=nik_baru;
        this.nama_karyawan_struktur=nama_karyawan_struktur;
        this.nik_penerima_1=nik_penerima_1;
        this.status_penerima_1=status_penerima_1;
        this.tanggal_penerima_1=tanggal_penerima_1;
        this.nik_penerima_2=nik_penerima_2;
        this.status_penerima_2=status_penerima_2;
        this.tanggal_penerima_2=tanggal_penerima_2;
    }

    public String getId() { return id; }
    public String getSubmit_date() { return submit_date; }
    public String getNik_baru() { return nik_baru; }

    public String getNama_karyawan_struktur() { return nama_karyawan_struktur; }

    public String getNik_penerima_1() { return nik_penerima_1; }
    public String getStatus_penerima_1() { return status_penerima_1; }
    public String getTanggal_penerima_1() { return tanggal_penerima_1; }

    public String getNik_penerima_2() { return nik_penerima_2; }
    public String getStatus_penerima_2() { return status_penerima_2; }
    public String getTanggal_penerima_2() { return tanggal_penerima_2; }

    @Override public String toString() { return nama_karyawan_struktur; }


}
