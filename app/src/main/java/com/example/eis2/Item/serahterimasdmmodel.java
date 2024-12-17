package com.example.eis2.Item;

public class serahterimasdmmodel {
    private String nik_baru;
    private String jabatan_sdm;
    private String jumlah_sdm;
    private String jenis_kelamin_sdm;
    private String promosi_sdm;
    private String mutasi_sdm;
    private String demosi_sdm;
    private String sp1_sdm;
    private String sp2_sdm;
    private String sp3_sdm;
    private String keterangan_sdm;

    public serahterimasdmmodel(String nik_baru, String jabatan_sdm, String jumlah_sdm,
                                   String jenis_kelamin_sdm, String promosi_sdm, String mutasi_sdm,
                               String demosi_sdm, String sp1_sdm, String sp2_sdm, String sp3_sdm, String keterangan_sdm){
        this.nik_baru=nik_baru;
        this.jabatan_sdm=jabatan_sdm;
        this.jumlah_sdm=jumlah_sdm;
        this.jenis_kelamin_sdm=jenis_kelamin_sdm;
        this.promosi_sdm=promosi_sdm;
        this.mutasi_sdm=mutasi_sdm;
        this.demosi_sdm=demosi_sdm;
        this.sp1_sdm=sp1_sdm;
        this.sp2_sdm=sp2_sdm;
        this.sp3_sdm=sp3_sdm;
        this.keterangan_sdm=keterangan_sdm;
    }

    public String getNik_baru() { return nik_baru; }
    public String getJabatan_sdm() { return jabatan_sdm; }
    public String getJumlah_sdm() { return jumlah_sdm; }
    public String getJenis_kelamin_sdm() { return jenis_kelamin_sdm; }
    public String getPromosi_sdm() { return promosi_sdm; }
    public String getMutasi_sdm() { return mutasi_sdm; }
    public String getDemosi_sdm() { return demosi_sdm; }
    public String getSp1_sdm() { return sp1_sdm; }
    public String getSp2_sdm() { return sp2_sdm; }
    public String getSp3_sdm() { return sp3_sdm; }
    public String getKeterangan_sdm() { return keterangan_sdm; }
}
