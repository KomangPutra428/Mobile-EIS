package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import java.security.AccessController;


public class pengumuman extends AppCompatActivity {
    Button nextbutton, previousbutton;
    ViewFlipper v_flipper;
    public static int gallery_grid_Images[] = {R.drawable.covid_1, R.drawable.covid_2, R.drawable.covid_3, R.drawable.covid_4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengumuman);
    }
}