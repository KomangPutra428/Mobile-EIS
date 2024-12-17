package com.example.eis2.Item;

public class sisacutimodel {
    private String id_cuti;
    private int hak_cuti_utuh;
    private String tahun;
    private String start_efektif_cuti;

    public sisacutimodel(String id_cuti, int hak_cuti_utuh, String tahun, String start_efektif_cuti) {
        this.id_cuti = id_cuti;
        this.hak_cuti_utuh = hak_cuti_utuh;
        this.tahun = tahun;
        this.start_efektif_cuti = start_efektif_cuti;


    }

    public String getId_cuti() { return id_cuti; }
    public int getHak_cuti_utuh() {return hak_cuti_utuh; }
    public String getTahun() {return tahun; }
    public String getStart_efektif_cuti() { return start_efektif_cuti; }
}
