package com.example.eis2.Item;

public class jawabanexit {
    private String id_soal;
    private String jawaban_soal;
    private String keterangan;
    private String soal;
    private String jawaban;

    public jawabanexit(String id_soal, String jawaban_soal, String keterangan, String soal, String jawaban) {
        this.id_soal = id_soal;
        this.jawaban_soal = jawaban_soal;
        this.keterangan = keterangan;

        this.soal = soal;
        this.jawaban = jawaban;
    }

    public String getId_soal() {
        return id_soal;
    }

    public String getJawaban_soal() {
        return jawaban_soal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getSoal() {
        return soal;
    }

    public String getJawaban() {
        return jawaban;
    }
}
