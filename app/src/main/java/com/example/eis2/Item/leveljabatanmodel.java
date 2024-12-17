package com.example.eis2.Item;

public class leveljabatanmodel {
    private String no_jabatan_karyawan;
    private String jabatan_karyawan;
    private String level_jabatan_karyawan;

    public leveljabatanmodel(String no_jabatan_karyawan, String jabatan_karyawan, String level_jabatan_karyawan) {
        this.no_jabatan_karyawan = no_jabatan_karyawan;
        this.jabatan_karyawan = jabatan_karyawan;
        this.level_jabatan_karyawan = level_jabatan_karyawan;

    }
    public String getNo_jabatan_karyawan() {return no_jabatan_karyawan; }
    public String getJabatan_karyawan() {return jabatan_karyawan; }
    public String getLevel_jabatan_karyawan() {return level_jabatan_karyawan; }

}
