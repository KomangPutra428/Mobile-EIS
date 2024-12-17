package com.example.eis2.Item;

public class vaksin_pojo {
    private String type;
    private String dokumen;

    public vaksin_pojo(String type, String dokumen){
        this.type = type;
        this.dokumen = dokumen;
    }

    public String getType() { return type; }
    public String getDokumen() { return dokumen; }
}
