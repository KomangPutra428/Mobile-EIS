package com.example.eis2.Item;

public class listba {
    private String no_pengajuan;
    private String jumlah_pengajuan;

    public listba(String no_pengajuan, String jumlah_pengajuan) {
        this.no_pengajuan = no_pengajuan;
        this.jumlah_pengajuan = jumlah_pengajuan;
    }

    public String getNo_pengajuan() { return no_pengajuan; }
    public String getJumlah_pengajuan() { return jumlah_pengajuan; }
}
