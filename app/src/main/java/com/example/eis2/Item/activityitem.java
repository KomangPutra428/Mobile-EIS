package com.example.eis2.Item;

public class activityitem {
    private String id;
    private String submit_date;
    private String nik;
    private String status_lokasi;
    private String lokasi;
    private String keterangan;
    private String lat;
    private String lon;
    private boolean isSelected;

    public activityitem(String id, String submit_date,  String nik, String status_lokasi, String lokasi,
                        String keterangan, String lat, String lon) {
        this.id = id;
        this.submit_date = submit_date;
        this.nik = nik;
        this.status_lokasi = status_lokasi;
        this.lokasi = lokasi;
        this.keterangan = keterangan;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() { return id; }
    public String getSubmit_date() { return submit_date; }
    public String getNik() { return nik; }
    public String getStatus_lokasi() { return status_lokasi; }
    public String getLokasi() { return lokasi; }
    public String getKeterangan() { return keterangan; }
    public String getLat() { return lat; }
    public String getLon() { return lon; }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


}
