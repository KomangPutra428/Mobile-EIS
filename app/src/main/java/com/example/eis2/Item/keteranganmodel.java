package com.example.eis2.Item;



public class keteranganmodel {
    public int number;
    String shift_day, F1, depo_f1, F4, depo_f4, ket_absensi, name;


    public keteranganmodel(String shift_day, String F1, String depo_f1, String F4, String depo_f4, String ket_absensi, String name) {
        this.shift_day = shift_day;
        this.F1 = F1;
        this.depo_f1 = depo_f1;
        this.F4 = F4;
        this.depo_f4 = depo_f4;
        this.ket_absensi = ket_absensi;
        this.name = name;

    }

    public String getShift_day() {
        return shift_day;
    }

    public String getF1() {
        return F1;
    }

    public String getDepo_f1() {
        return depo_f1;
    }

    public String getF4() {
        return F4;
    }

    public String getDepo_f4() {
        return depo_f4;
    }

    public String getKet_absensi() {
        return ket_absensi;
    }

    public String getName() {
        return name;
    }

    public int getNumber(int number) {return this.number;}

    @Override public String toString() { return ket_absensi; }
}




