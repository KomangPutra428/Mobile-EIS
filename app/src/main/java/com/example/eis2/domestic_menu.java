package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eis2.Item.HttpsTrustManager;

public class domestic_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domestic_menu);
        HttpsTrustManager.allowAllSSL();
    }
}