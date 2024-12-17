package com.example.eis2.Item;

public class soalcovidmodel {
    private String no_pertanyaan;
    private String pertanyaan;
    private String idpertanyaan;
    private String idjawaban;
    private boolean isSelected;


    public soalcovidmodel(String no_pertanyaan, String pertanyaan) {
        this.no_pertanyaan = no_pertanyaan;
        this.pertanyaan = pertanyaan;

    }

    public String getNo_pertanyaan() { return no_pertanyaan; }
    public String getPertanyaan() { return pertanyaan; }

    public String getIdjawaban() { return idjawaban; }
    public String getIdpertanyaan() { return idpertanyaan; }

    public void setIdjawaban(String idjawaban) { this.idjawaban = idjawaban; }
    public void setIdpertanyaan(String idpertanyaan) { this.idpertanyaan = idpertanyaan; }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
