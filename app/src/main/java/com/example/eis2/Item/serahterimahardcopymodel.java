package com.example.eis2.Item;

public class serahterimahardcopymodel {
    private String nik_baru;
    private String nama_hardcopy;
    private String jumlah_hardcopy;
    private String satuan_hardcopy;
    private String tempat_hardcopy;
    private String keterangan_hardcopy;

    public serahterimahardcopymodel(String nik_baru, String nama_hardcopy, String jumlah_hardcopy,
                                     String satuan_hardcopy, String tempat_hardcopy, String keterangan_hardcopy){
        this.nik_baru=nik_baru;
        this.nama_hardcopy=nama_hardcopy;
        this.jumlah_hardcopy=jumlah_hardcopy;
        this.satuan_hardcopy=satuan_hardcopy;
        this.tempat_hardcopy=tempat_hardcopy;
        this.keterangan_hardcopy=keterangan_hardcopy;

    }

    public String getNik_baru() { return nik_baru; }
    public String getNama_hardcopy() { return nama_hardcopy; }
    public String getJumlah_hardcopy() { return jumlah_hardcopy; }
    public String getSatuan_hardcopy() { return satuan_hardcopy; }
    public String getTempat_hardcopy() { return tempat_hardcopy; }
    public String getKeterangan_hardcopy() { return keterangan_hardcopy; }
}
