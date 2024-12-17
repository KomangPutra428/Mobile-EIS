package com.example.eis2.Item;

public class jadwalinterviewmodel {
    private String id_interview;
    private String posisi;
    private String no_ktp;
    private String nama_lengkap;
    private String tanggal_interview;
    private String jam_interview;
    private String lokasi_interview;
    private String status;
    private String nama_pewawancara_1;
    private String nama_pewawancara_2;
    private String nama_pewawancara_;
    private String kesimpulan;


    public jadwalinterviewmodel(String id_interview, String posisi, String no_ktp, String nama_lengkap,
                          String tanggal_interview, String jam_interview, String lokasi_interview, String status,
                                String nama_pewawancara_1, String nama_pewawancara_2, String nama_pewawancara_, String kesimpulan){

        this.id_interview = id_interview;
        this.posisi = posisi;
        this.no_ktp = no_ktp;
        this.nama_lengkap = nama_lengkap;
        this.tanggal_interview = tanggal_interview;
        this.jam_interview = jam_interview;
        this.lokasi_interview = lokasi_interview;
        this.status = status;
        this.nama_pewawancara_1 = nama_pewawancara_1;
        this.nama_pewawancara_2 = nama_pewawancara_2;
        this.nama_pewawancara_ = nama_pewawancara_;

        this.kesimpulan = kesimpulan;

    }

    public String getId_interview() { return id_interview; }
    public String getPosisi() { return posisi; }
    public String getNo_ktp() { return no_ktp; }
    public String getNama_lengkap() { return nama_lengkap; }

    public String getTanggal_interview() { return tanggal_interview; }
    public String getJam_interview() { return jam_interview; }
    public String getLokasi_interview() { return lokasi_interview; }
    public String getStatus() { return status; }
    public String getNama_pewawancara_1() {return nama_pewawancara_1;}
    public String getNama_pewawancara_2() { return nama_pewawancara_2; }
    public String getNama_pewawancara_() { return nama_pewawancara_; }
    public String getKesimpulan() { return kesimpulan; }

    @Override
    public String toString() {
        return nama_lengkap;
    }

}
