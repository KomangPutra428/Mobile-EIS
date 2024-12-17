package com.example.eis2.Item;

public class planmodels {
    private String id;
    private String date;
    private String start;
    private String start_realisasi;
    private String end;
    private String end_realisasi;
    private String ket_plan;
    private String ket_realisasi;
    private String status;
    private String pengganti;
    private String nama_kategori;
    private String draft;
    private boolean isSelected;

    public planmodels(String id, String date, String start, String start_realisasi, String end, String end_realisasi, String ket_plan, String ket_realisasi, String status, String pengganti, String nama_kategori, String draft) {
        this.id = id;
        this.date = date;
        this.start = start;
        this.start_realisasi = start_realisasi;
        this.end = end;
        this.end_realisasi = end_realisasi;
        this.ket_plan = ket_plan;
        this.ket_realisasi = ket_realisasi;
        this.status = status;
        this.pengganti = pengganti;
        this.nama_kategori = nama_kategori;
        this.draft = draft;
    }

    public String getId() {
        return id;
    }

    public String getDate() {return date;}
    public String getStart() {
        return start;
    }

    public String getStart_realisasi() {return start_realisasi;}

    public String getEnd_realisasi() {return end_realisasi;}

    public String getEnd() {
        return end;
    }
    public String getKet_plan() {
        return ket_plan;
    }
    public String getKet_realisasi() {return ket_realisasi;}
    public String getStatus() {return status;}
    public String getPengganti() {return pengganti;}

    public String getNama_kategori() {
        return nama_kategori;
    }

    public String getDraft() {
        return draft;
    }

    public boolean getSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
