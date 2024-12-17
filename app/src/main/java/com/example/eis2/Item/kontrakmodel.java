package com.example.eis2.Item;

public class kontrakmodel {
    String submit_date, no_urut, nik_baru, nama_karyawan_struktur, kontrak, tanggal_kontrak, start_date, end_date;

    public kontrakmodel(String submit_date, String no_urut, String nik_baru, String nama_karyawan_struktur, String kontrak,
                        String tanggal_kontrak, String start_date, String end_date) {
        this.submit_date = submit_date;
        this.no_urut = no_urut;
        this.nik_baru = nik_baru;
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.kontrak = kontrak;
        this.tanggal_kontrak = tanggal_kontrak;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public String getSubmit_date() { return submit_date; }
    public String getNo_urut() { return no_urut; }

    public String getNik_baru() { return nik_baru; }
    public String getNama_karyawan_struktur() { return nama_karyawan_struktur; }

    public String getKontrak() { return kontrak; }
    public String getTanggal_kontrak() { return tanggal_kontrak; }

    public String getStart_date() { return start_date; }
    public String getEnd_date() { return end_date; }
}
