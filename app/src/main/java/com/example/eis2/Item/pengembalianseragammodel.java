package com.example.eis2.Item;

public class pengembalianseragammodel {
    private String submit_date;
    private String no_pengajuan;
    private String nik_pengajuan;
    private String ket_pengajuan;
    private String nik_baru;
    private String nama_karyawan_struktur;
    private String id_seragam;
    private String qty_seragam;
    private String harga_satuan;
    private String tanggal_pengembalian;
    private String ket_tambahan;

    public pengembalianseragammodel(String submit_date, String no_pengajuan, String nik_pengajuan, String ket_pengajuan, String nik_baru,
                                    String nama_karyawan_struktur, String id_seragam, String qty_seragam, String harga_satuan, String tanggal_pengembalian,
                                    String ket_tambahan) {
        this.submit_date = submit_date;
        this.no_pengajuan = no_pengajuan;
        this.nik_pengajuan = nik_pengajuan;
        this.ket_pengajuan = ket_pengajuan;
        this.nik_baru = nik_baru;
        this.nama_karyawan_struktur = nama_karyawan_struktur;
        this.id_seragam = id_seragam;
        this.qty_seragam = qty_seragam;
        this.harga_satuan = harga_satuan;
        this.tanggal_pengembalian = tanggal_pengembalian;
        this.ket_tambahan = ket_tambahan;
    }

    public String getSubmit_date() { return submit_date; }
    public String getNo_pengajuan() { return no_pengajuan; }
    public String getNik_pengajuan() { return nik_pengajuan; }
    public String getKet_pengajuan() { return ket_pengajuan; }

    public String getNik_baru() { return nik_baru; }
    public String getNama_karyawan_struktur() { return nama_karyawan_struktur; }
    public String getId_seragam() { return id_seragam; }
    public String getQty_seragam() { return qty_seragam; }

    public String getHarga_satuan() { return harga_satuan; }
    public String getTanggal_pengembalian() { return tanggal_pengembalian; }
    public String getKet_tambahan() { return ket_tambahan; }

    @Override
    public String toString() {
        return nama_karyawan_struktur;
    }
}
