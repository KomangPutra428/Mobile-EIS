package com.example.eis2.Item;

public class softcopymodel {
    private String softcopy;
    private String nopc;
    private String jenis;
    private String keterangan;


    public softcopymodel(String softcopy,
                         String nopc,
                         String jenis,
                         String keterangan) {
        this.softcopy = softcopy;
        this.nopc = nopc;
        this.jenis = jenis;
        this.keterangan = keterangan;
    }

    public String getSoftcopy() { return softcopy; }
    public String getNopc() { return nopc; }
    public String getJenis() { return jenis; }
    public String getKeterangan() { return keterangan; }
}
