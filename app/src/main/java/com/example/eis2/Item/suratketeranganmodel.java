package com.example.eis2.Item;

public class suratketeranganmodel {
    String submit_date;
    String nik_baru;
    String keperluan;
    String analisa;
    String status_atasan;
    String status_hrd;

    public suratketeranganmodel(String submit_date, String nik_baru, String keperluan, String analisa, String status_atasan, String status_hrd) {
        this.submit_date = submit_date;
        this.nik_baru = nik_baru;
        this.keperluan = keperluan;
        this.analisa = analisa;
        this.status_atasan = status_atasan;
        this.status_hrd = status_hrd;

    }
    public String getSubmit_date() {
        return submit_date;
    }

    public String getNik_baru() {
        return nik_baru;
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

    public String getStatus_hrd() {return status_hrd; }
}
