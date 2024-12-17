package com.example.eis2.Item;

public class serahterimaprojectmodel {
    private String nik_baru;
    private String nama_project;
    private String sdm_project;
    private String hasil_project;
    private String outstanding_project;
    private String deadline_project;

    public serahterimaprojectmodel(String nik_baru, String nama_project, String sdm_project,
                                    String hasil_project, String outstanding_project, String deadline_project){
        this.nik_baru=nik_baru;
        this.nama_project=nama_project;
        this.sdm_project=sdm_project;
        this.hasil_project=hasil_project;
        this.outstanding_project=outstanding_project;
        this.deadline_project=deadline_project;
    }

    public String getNik_baru() { return nik_baru; }
    public String getNama_project() { return nama_project; }
    public String getSdm_project() { return sdm_project; }
    public String getHasil_project() { return hasil_project; }
    public String getOutstanding_project() { return outstanding_project; }
    public String getDeadline_project() { return deadline_project; }
}
