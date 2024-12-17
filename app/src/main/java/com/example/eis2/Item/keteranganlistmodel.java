package com.example.eis2.Item;

public class keteranganlistmodel {
    String id;
    String list_pekerjaan;

    public keteranganlistmodel(String id, String list_pekerjaan){
        this.id = id;
        this.list_pekerjaan = list_pekerjaan;
    }

    public String getId() { return id; }
    public String getList_pekerjaan() { return list_pekerjaan; }
}
