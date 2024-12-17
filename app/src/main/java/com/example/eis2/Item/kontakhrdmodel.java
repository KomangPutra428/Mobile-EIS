package com.example.eis2.Item;

public class kontakhrdmodel {
    String nama, area, telepon, scoop, link_gambar;

    public kontakhrdmodel(String nama, String area, String telepon, String scoop, String link_gambar){
        this.nama = nama;
        this.area = area;
        this.telepon = telepon;
        this.scoop = scoop;
        this.link_gambar = link_gambar;
    }

    public String getNama() {
        return nama;
    }

    public String getArea() {
        return area;
    }

    public String getTelepon() {
        return telepon;
    }

    public String getScoop() {
        return scoop;
    }

    public String getLink_gambar() {
        return link_gambar;
    }
}
