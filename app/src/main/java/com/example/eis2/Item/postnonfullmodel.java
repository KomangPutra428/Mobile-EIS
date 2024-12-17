package com.example.eis2.Item;

public class postnonfullmodel {
    Integer status_non_full, status_non_full_2;
    String nik_non_full, jabatan_non_full, jenis_non_full,
    tanggal_non_full, ket_tambahan;

    public postnonfullmodel(String nik_non_full, String jabatan_non_full, String jenis_non_full,
                            String tanggal_non_full, String ket_tambahan, Integer status_non_full, Integer status_non_full_2){

        this.nik_non_full = nik_non_full;
        this.jabatan_non_full = jabatan_non_full;
        this.jenis_non_full = jenis_non_full;

        this.tanggal_non_full = tanggal_non_full;
        this.ket_tambahan = ket_tambahan;
        this.status_non_full = status_non_full;
        this.status_non_full_2 = status_non_full_2;

    }
    public String getNik_non_full() {return nik_non_full; }
    public String getJabatan_non_full() {return jabatan_non_full; }
    public String getJenis_non_full() {return jenis_non_full; }

    public String getTanggal_non_full() {return tanggal_non_full; }
    public String getKet_tambahan() {return ket_tambahan; }
    public Integer getStatus_non_full() {return status_non_full; }
    public Integer getStatus_non_full_2() {return status_non_full_2; }

}
