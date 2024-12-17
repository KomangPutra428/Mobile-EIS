package com.example.eis2.Item;

public class jawaban8model {
    private String id;
    private String jawaban;
    private boolean isSelected;

    public jawaban8model(String id, String jawaban) {
        this.id = id;
        this.jawaban = jawaban;
    }

    public String getId() {
        return id;
    }

    public String getJawaban() {
        return jawaban;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
