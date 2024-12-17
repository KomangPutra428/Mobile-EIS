package com.example.eis2.Item;

public class pelanggaranmodel {
    private String no_surat;
    private String punishment_historical_violance;
    private String pelanggaran_historical_violance;
    private String warning_note_historical_violance;
    private String warning_start_historical_violance;
    private String warning_end_historical_violance;
    private String status_dokumen;

    public pelanggaranmodel(String no_surat, String punishment_historical_violance, String pelanggaran_historical_violance,
                            String warning_note_historical_violance, String warning_start_historical_violance,
                            String warning_end_historical_violance, String status_dokumen) {
        this.no_surat = no_surat;
        this.punishment_historical_violance = punishment_historical_violance;
        this.pelanggaran_historical_violance = pelanggaran_historical_violance;
        this.warning_note_historical_violance = warning_note_historical_violance;
        this.warning_start_historical_violance = warning_start_historical_violance;
        this.warning_end_historical_violance = warning_end_historical_violance;
        this.status_dokumen = status_dokumen;
    }

    public String getNo_surat() { return no_surat; }
    public String getPunishment_historical_violance() { return punishment_historical_violance; }
    public String getPelanggaran_historical_violance() { return pelanggaran_historical_violance; }
    public String getWarning_note_historical_violance() { return warning_note_historical_violance; }

    public String getWarning_start_historical_violance() { return warning_start_historical_violance; }
    public String getWarning_end_historical_violance() { return warning_end_historical_violance; }
    public String getStatus_dokumen() { return status_dokumen; }

}
