package com.example.eis2.Item;

public class refundmodel {
    private String submit_date;
    private String no_pengajuan;
    private String nik;
    private String nama_karyawan_struktur;
    private String tanggal_absen;
    private String absen_awal;
    private String absen_akhir;
    private String ket;
    private String status_refund;
    private String status_ba;
    private String status_pengajuan;
    private String ket_pengajuan;

    public refundmodel(String submit_date, String no_pengajuan, String nik, String nama_karyawan_struktur,
                       String tanggal_absen, String absen_awal,
                       String absen_akhir, String ket, String status_refund, String status_ba, String status_pengajuan,
                       String ket_pengajuan) {
        this.submit_date = submit_date;
        this.no_pengajuan = no_pengajuan;
        this.nik = nik;
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.tanggal_absen = tanggal_absen;
        this.absen_awal = absen_awal;
        this.absen_akhir = absen_akhir;
        this.ket = ket;
        this.status_refund = status_refund;
        this.status_ba = status_ba;
        this.status_pengajuan = status_pengajuan;
        this.ket_pengajuan = ket_pengajuan;
    }

    public String getSubmit_date() { return submit_date; }
    public String getNo_pengajuan() { return no_pengajuan; }
    public String getNik() { return nik; }
    public String getNama_karyawan_struktur() { return nama_karyawan_struktur; }

    public String getTanggal_absen() { return tanggal_absen; }
    public String getAbsen_awal() { return absen_awal; }
    public String getAbsen_akhir() { return absen_akhir; }

    public String getKet() { return ket; }
    public String getStatus_refund() { return status_refund; }
    public String getStatus_ba() { return status_ba; }
    public String getStatus_pengajuan() { return status_pengajuan; }
    public String getKet_pengajuan() { return ket_pengajuan; }

    @Override public String toString() { return nama_karyawan_struktur; }

}
