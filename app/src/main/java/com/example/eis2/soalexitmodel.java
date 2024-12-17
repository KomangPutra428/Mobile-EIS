package com.example.eis2;

public class soalexitmodel {
    private String id;
    private String soal;
    private String jawaban;
    private String noJawaban;
    private String keteranganjawaban;

    public soalexitmodel(String id, String soal, String jawaban) {
        this.id = id;
        this.soal = soal;
        this.jawaban = jawaban;
    }

    public String getId() { return id; }
    public String getSoal() { return soal; }
    public String getJawaban() { return jawaban; }

    public String getNoJawaban() { return noJawaban; }
    public void setNoJawaban(String noJawaban) { this.noJawaban = noJawaban; }

    public String getKeteranganjawaban() { return keteranganjawaban; }
    public void setKeteranganjawaban(String keteranganjawaban) { this.keteranganjawaban = keteranganjawaban; }
}
