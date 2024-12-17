package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;

import static com.example.eis2.menu.txt_alpha;

public class riwayat extends AppCompatActivity {
    ImageButton project, mutasi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        HttpsTrustManager.allowAllSSL();

        project = (ImageButton) findViewById(R.id.project);
        mutasi = (ImageButton) findViewById(R.id.mutasi);

        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (riwayat.this, listkaryawanproject.class);
                startActivity(i);
            }
        });

        mutasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (riwayat.this, history_mutasi.class);
                startActivity(i);
            }
        });

    }
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());


        super.onDestroy();
    }
}