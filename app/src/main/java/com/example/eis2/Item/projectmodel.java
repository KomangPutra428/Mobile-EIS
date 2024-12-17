package com.example.eis2.Item;

public class projectmodel {
    private String project;
    private String sdm;
    private String hasil;
    private String outstanding;
    private String deadline;

    public projectmodel(String project,
                         String sdm,
                         String hasil,
                         String outstanding,
                         String deadline) {
        this.project=project;
        this.sdm = sdm;
        this.hasil = hasil;
        this.outstanding = outstanding;
        this.deadline = deadline;
    }

    public String getProject() { return project; }
    public String getSdm() { return sdm; }
    public String getHasil() { return hasil; }
    public String getOutstanding() { return outstanding; }
    public String getDeadline() { return deadline; }
}
