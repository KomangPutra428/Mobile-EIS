package com.example.eis2.Item;

public class menuresignmodel {
    private String keterangan;
    private String status;

    public menuresignmodel(String keterangan, String status) {
        this.keterangan = keterangan;
        this.status = status;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getStatus() {
        return status;
    }
}
