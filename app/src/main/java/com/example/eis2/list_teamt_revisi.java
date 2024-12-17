package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eis2.Item.HttpsTrustManager;

public class list_teamt_revisi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_teamt_revisi);
        HttpsTrustManager.allowAllSSL();

    }
}