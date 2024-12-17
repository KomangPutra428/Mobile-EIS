package com.example.eis2.Item;

public class sdmmodel {
    private String jabatan;
    private String jumlah;
    private String jeniskelamin;
    private String promosi;
    private String mutasi;
    private String demosi;
    private String suratperingatan1;
    private String suratperingatan2;
    private String suratperingatan3;
    private String keterangan;

    public sdmmodel(String jabatan,
                         String jumlah,
                         String jeniskelamin,
                         String promosi,
                         String mutasi,
                         String demosi,
                         String suratperingatan1,
                         String suratperingatan2,
                         String suratperingatan3,
                         String keterangan) {
        this.jabatan=jabatan;
        this.jumlah = jumlah;
        this.jeniskelamin = jeniskelamin;
        this.promosi = promosi;
        this.mutasi = mutasi;

        this.demosi=demosi;
        this.suratperingatan1 = suratperingatan1;
        this.suratperingatan2 = suratperingatan2;
        this.suratperingatan3 = suratperingatan3;
        this.keterangan = keterangan;
    }

    public String getJabatan() { return jabatan; }
    public String getJumlah() { return jumlah; }
    public String getJeniskelamin() { return jeniskelamin; }
    public String getPromosi() { return promosi; }
    public String getMutasi() { return mutasi; }

    public String getDemosi() { return demosi; }
    public String getSuratperingatan1() { return suratperingatan1; }
    public String getSuratperingatan2() { return suratperingatan2; }
    public String getSuratperingatan3() { return suratperingatan3; }
    public String getKeterangan() { return keterangan; }
}
