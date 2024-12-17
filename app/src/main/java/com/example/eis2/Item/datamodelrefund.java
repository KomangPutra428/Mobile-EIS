package com.example.eis2.Item;

public class datamodelrefund {
    String nik;
    String karyawan;
    String tanggal;
    String absenawal;
    String absenakhir;
    String keterangan;
    String image;

    public datamodelrefund(String nik, String karyawan, String tanggal, String absenawal, String absenakhir, String keterangan, String image) {
        this.nik = nik;
        this.karyawan = karyawan;
        this.tanggal = tanggal;
        this.absenawal = absenawal;
        this.absenakhir = absenakhir;
        this.keterangan = keterangan;
        this.image = image;
    }

    public String getNik() {
        return nik;
    }
    public String getKaryawan() {
        return karyawan;
    }
    public String getTanggal() {
        return tanggal;
    }
    public String getAbsenawal() {
        return absenawal;
    }
    public String getAbsenakhir() {
        return absenakhir;
    }
    public String getKeterangan() {
        return keterangan;
    }
    public String getImage() { return image; }
}
