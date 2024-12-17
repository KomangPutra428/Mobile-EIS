package com.example.eis2.Item;

public class gratifikasimodel {
    String nama_karyawan_struktur;
    String nomorLaporan;
    String jabatan;
    String departement;
    String pemberi;
    String nominal;
    String keterangan;
    String fotobukti;
    String tglKejadian;

    public gratifikasimodel(String nama_karyawan_struktur, String nomorLaporan, String jabatan,
                            String departement, String pemberi, String nominal, String keterangan,
                            String fotobukti, String tglKejadian){
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.nomorLaporan = nomorLaporan;
        this.jabatan = jabatan;
        this.departement = departement;
        this.pemberi = pemberi;
        this.nominal = nominal;
        this.keterangan = keterangan;
        this.fotobukti = fotobukti;
        this.tglKejadian = tglKejadian;
    }

    public String getNama_karyawan_struktur() {
        return nama_karyawan_struktur;
    }

    public String getNomorLaporan() {
        return nomorLaporan;
    }

    public String getJabatan() {
        return jabatan;
    }

    public String getDepartement() {
        return departement;
    }

    public String getPemberi() {
        return pemberi;
    }

    public String getNominal() {
        return nominal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getFotobukti() {
        return fotobukti;
    }

    public String getTglKejadian() {
        return tglKejadian;
    }
}
