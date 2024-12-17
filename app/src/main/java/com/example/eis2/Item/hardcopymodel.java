package com.example.eis2.Item;

public class hardcopymodel {
    private String hardcopy;
    private String jumlah;
    private String satuan;
    private String tempat;
    private String keterangan;

    public hardcopymodel(String hardcopy,
                          String jumlah,
                          String satuan,
                          String tempat,
                          String keterangan) {
        this.hardcopy=hardcopy;
        this.jumlah = jumlah;
        this.satuan = satuan;
        this.tempat = tempat;
        this.keterangan = keterangan;
    }

    public String getHardcopy() { return hardcopy; }

    public String getJumlah() {
        return jumlah;
    }

    public String getSatuan() {
        return satuan;
    }

    public String getTempat() {
        return tempat;
    }

    public String getKeterangan() {
        return keterangan;
    }
}
