package com.example.eis2.Item;

public class biodatamodel {
    private String badgenumber;
    private String name;
    private String jabatan_karyawan;
    private String dept_struktur;
    private String lokasi_struktur;
    private String join_date_struktur;
    private String status_karyawan_struktur;

    public biodatamodel(String badgenumber, String name, String jabatan_karyawan, String dept_struktur, String lokasi_struktur, String join_date_struktur, String status_karyawan_struktur) {
        this.badgenumber=badgenumber;
        this.name = name;
        this.jabatan_karyawan = jabatan_karyawan;
        this.dept_struktur = dept_struktur;
        this.lokasi_struktur = lokasi_struktur;
        this.join_date_struktur = join_date_struktur;
        this.status_karyawan_struktur = status_karyawan_struktur;
    }

    public String getBadgenumber() {return badgenumber; }
    public void setBadgenumber(String badgenumber) { this.badgenumber = badgenumber; }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public String getJabatan_karyawan() {
        return jabatan_karyawan;
    }
    public void setJabatan_karyawan(String jabatan_karyawan) {this.jabatan_karyawan = jabatan_karyawan; }

    public String getDept_struktur() {
        return dept_struktur;
    }
    public void setDept_struktur(String dept_struktur) {
        this.dept_struktur = dept_struktur;
    }

    public String getLokasi_struktur() {
        return lokasi_struktur;
    }
    public void setLokasi_struktur(String dept_struktur) {
        this.lokasi_struktur = lokasi_struktur;
    }

    public String getJoin_date_struktur() {
        return join_date_struktur;
    }
    public void setJoin_date_struktur(String join_date_struktur) {this.join_date_struktur = join_date_struktur; }

    public String getStatus_karyawan_struktur() {
        return status_karyawan_struktur;
    }
    public void setStatus_karyawan_struktur(String join_date_struktur) {this.status_karyawan_struktur = status_karyawan_struktur; }
}
