package com.example.eis2.Item;

public class tanggalmodel {
    private String id;
    private String nik_baru;
    private String date;
    private boolean isSelected;

    public tanggalmodel(String id, String nik_baru, String date) {
        this.id = id;
        this.nik_baru = nik_baru;
        this.date = date;
    }
    public String getId() { return id; }
    public String getNik_baru() {return nik_baru;}
    public String getDate() {return date;}

    public boolean getSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
