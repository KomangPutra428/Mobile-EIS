package com.example.eis2.Item;

public class serahterimasoftcopymodel {
    private String nik_baru;
    private String nama_softcopy;
    private String no_software;
    private String jenis_software;
    private String keterangan_software;

    public serahterimasoftcopymodel(String nik_baru, String nama_softcopy, String no_software,
                                    String jenis_software, String keterangan_software){
        this.nik_baru=nik_baru;
        this.nama_softcopy=nama_softcopy;
        this.no_software=no_software;
        this.jenis_software=jenis_software;
        this.keterangan_software=keterangan_software;

    }

    public String getNik_baru() { return nik_baru; }
    public String getNama_softcopy() { return nama_softcopy; }
    public String getNo_software() { return no_software; }
    public String getJenis_software() { return jenis_software; }
    public String getKeterangan_software() { return keterangan_software; }
}
