package com.example.eis2.Item;

public class eventmodel {
    private String name;
    private String birth_date;

    public eventmodel(String name, String birth_date) {
        this.name = name;
        this.birth_date = birth_date;
    }

    public String getName() { return name; }
    public String getBirth_date() { return birth_date; }
}
