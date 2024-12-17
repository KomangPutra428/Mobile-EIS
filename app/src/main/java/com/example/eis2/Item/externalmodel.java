package com.example.eis2.Item;

public class externalmodel {
    private String no_daily;
    private String toko;
    private String submit_date;

    private String lokasi;

    private String type_customer;
    private String ket;
    private boolean isSelected;

    public externalmodel(String no_daily, String toko, String submit_date, String lokasi, String type_customer, String ket) {
        this.no_daily = no_daily;
        this.toko = toko;
        this.submit_date = submit_date;
        this.lokasi = lokasi;
        this.type_customer = type_customer;
        this.ket = ket;
    }
    public String getNo_daily() { return no_daily; }
    public String getToko() {return toko;}

    public String getSubmit_date() {
        return submit_date;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getType_customer() {
        return type_customer;
    }

    public String getKet() {
        return ket;
    }


    public boolean getSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
