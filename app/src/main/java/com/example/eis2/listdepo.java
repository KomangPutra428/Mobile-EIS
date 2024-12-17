package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.eis2.menu.txt_alpha;

public class listdepo extends AppCompatActivity {
    ImageButton kedoya, Serang, Cikokol, Lodan, Kalibata, depok,
            Daan_Mogot, alam_sutera, balaraja, bintaro,
            cinangka, Cirendeu, Kelapa_dua, Lenteng_agung,
            Pluit, Manis, Rancamaya, Cikuda, pandegelang;
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listdepo);
        HttpsTrustManager.allowAllSSL();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });
        setNavigationDrawer();
        kedoya = (ImageButton) findViewById(R.id.kedoya);
        Serang = (ImageButton) findViewById(R.id.serang);
        Cikokol = (ImageButton) findViewById(R.id.cikokol);
        Lodan = (ImageButton) findViewById(R.id.lodan);
        Kalibata = (ImageButton) findViewById(R.id.kalibata);
        Daan_Mogot = (ImageButton) findViewById(R.id.daan_mogot);
        alam_sutera = (ImageButton) findViewById(R.id.alam_sutra);
        balaraja = (ImageButton) findViewById(R.id.balaraja);
        bintaro = (ImageButton) findViewById(R.id.bintaro);
        cinangka = (ImageButton) findViewById(R.id.cinangka2);
        Cirendeu = (ImageButton) findViewById(R.id.cirendeu);
        Kelapa_dua = (ImageButton) findViewById(R.id.kelapa_dua);
        Lenteng_agung = (ImageButton) findViewById(R.id.lt_agung);
        Pluit = (ImageButton) findViewById(R.id.pluit);
        Manis = (ImageButton) findViewById(R.id.manis);
        Rancamaya = (ImageButton) findViewById(R.id.rancamaya);
        Cikuda = (ImageButton) findViewById(R.id.cikuda);
        pandegelang = (ImageButton) findViewById(R.id.pandegelang);
        depok = (ImageButton) findViewById(R.id.depok);
        depok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://maps.app.goo.gl/4us7F9gVdsHR2xST8"));
                startActivity(i);
            }
        });

        kedoya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/PT.+Tirta+Varia+Intipratama/@-6.1800066,106.7575169,17z/data=!3m1!4b1!4m5!3m4!1s0x2e69f7067ca2990d:0x3c7d37d902d6d2fc!8m2!3d-6.1800066!4d106.7597109"));
                startActivity(i);
            }
        });

        Serang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/TVIP+SERANG/@-6.0694476,106.1213403,17z/data=!3m1!4b1!4m5!3m4!1s0x2e418df6ffa7778f:0xc23105627fe7550a!8m2!3d-6.0694476!4d106.1235343"));
                startActivity(i);
            }
        });

        Cikokol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/TVIP+CIKOKOL+%2FDEPO+AQUA/@-6.1817682,106.6413726,17z/data=!3m1!4b1!4m5!3m4!1s0x2e69f8d87c621c99:0x60696be684bbd920!8m2!3d-6.1817682!4d106.6435666"));
                startActivity(i);
            }
        });

        Lodan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/PT.+TIRTA+VARIA+INTIPRATAMA+-+LODAN/@-6.1277738,106.8111441,17z/data=!3m1!4b1!4m5!3m4!1s0x2e6a1dfc8d11761b:0xfda8959794284149!8m2!3d-6.1277738!4d106.8133381"));
                startActivity(i);
            }
        });

        Kalibata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/PT.+TIRTA+VARIA+INTIPRATAMA+-+KALIBATA/@-6.2583221,106.8577363,17z/data=!3m1!4b1!4m5!3m4!1s0x2e69f3b372abbedf:0xd62b902395e9f430!8m2!3d-6.2583221!4d106.8599303"));
                startActivity(i);
            }
        });

        Daan_Mogot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/PT.+TIRTA+VARIA+INTIPRATAMA+-+DAAN+MOGOT/@-6.1554918,106.7406526,17z/data=!3m1!4b1!4m5!3m4!1s0x2e69f7eba302eb41:0xbd29eb67df7470ca!8m2!3d-6.1554918!4d106.7428466"));
                startActivity(i);
            }
        });

        alam_sutera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/PT.+TIRTA+VARIA+INTIPRATAMA+-+ALAM+SUTERA/@-6.2379145,106.6449141,17z/data=!3m1!4b1!4m5!3m4!1s0x2e69fbe953c91f6f:0xd627cf2d948a6290!8m2!3d-6.2379145!4d106.6471081"));
                startActivity(i);
            }
        });

        balaraja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/TVIP+Balaraja/@-6.2124473,106.4434024,17z/data=!3m1!4b1!4m5!3m4!1s0x2e4201aa1802c4bb:0xfc5a6d938bdacf88!8m2!3d-6.2124473!4d106.4455964"));
                startActivity(i);
            }
        });

        bintaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/Depo+PT.TVIP+Bintaro/@-6.2770396,106.7032242,17z/data=!3m1!4b1!4m5!3m4!1s0x2e69fa93fc0631db:0xb7047b9cef3fe351!8m2!3d-6.2770396!4d106.7054182"));
                startActivity(i);
            }
        });

        cinangka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/Depo+TVIP+Cinangka/@-6.3668361,106.7402884,17z/data=!3m1!4b1!4m5!3m4!1s0x2e69ef416b0dd6eb:0x35fb4e6033c976d8!8m2!3d-6.3668361!4d106.7424824"));
                startActivity(i);
            }
        });

        Cirendeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/TVIP+Cirendeu/@-6.3120331,106.7686297,17z/data=!3m1!4b1!4m5!3m4!1s0x2e69efcd4c1c9101:0xfc68ae7071531af!8m2!3d-6.3120331!4d106.7708237"));
                startActivity(i);
            }
        });

        Kelapa_dua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/PT+Tirta+Varia+Intipratama+-+Kelapa+Dua/@-6.2675885,106.5924186,17z/data=!3m1!4b1!4m5!3m4!1s0x2e69fcf79a996b83:0x71120f27379725b8!8m2!3d-6.2675885!4d106.5946126"));
                startActivity(i);
            }
        });

        Lenteng_agung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/PT.+TVIP+Lenteng+Agung/@-6.311141,106.8354831,17z/data=!3m1!4b1!4m5!3m4!1s0x2e69ed5392e3a2bf:0xea381c07237b6853!8m2!3d-6.311141!4d106.8376771"));
                startActivity(i);
            }
        });

        Pluit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/PT.+Tirta+Varia+Intipratama+-+Pluit/@-6.1266228,106.7939045,17z/data=!3m1!4b1!4m5!3m4!1s0x2e6a1df29c009d49:0x89f15ded5b333c10!8m2!3d-6.1266228!4d106.7960985"));
                startActivity(i);
            }
        });

        Manis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/PT.+TIRTA+VARIA+INTIPRATAMA+-+MANIS/@-6.2145113,106.5713762,17z/data=!3m1!4b1!4m5!3m4!1s0x2e69fe7b2c5f91bf:0xf796feaf24cd8fa5!8m2!3d-6.2145113!4d106.5735702"));
                startActivity(i);
            }
        });

        Rancamaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/Pool+Rancamaya+TVIP+(CV+Terang+Berkat+Karunia)/@-6.6784606,106.8461043,17z/data=!3m1!4b1!4m5!3m4!1s0x2e69c910ba8fec2b:0x4d3562e1652b494a!8m2!3d-6.6784606!4d106.8482983"));
                startActivity(i);
            }
        });

        Cikuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/CV.MRT+(POOL+AQUA+CIKUDA+%2F+MRT)/@-6.4157081,106.9378445,17z/data=!3m1!4b1!4m5!3m4!1s0x2e6995dca1fe56eb:0x6f4a4a001d4c9a4a!8m2!3d-6.4157081!4d106.9400385"));
                startActivity(i);
            }
        });

        pandegelang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.google.com/maps/place/6%C2%B019'58.5%22S+106%C2%B003'21.4%22E/@-6.332859,106.0557428,49m/data=!3m1!1e3!4m5!3m4!1s0x0:0x0!8m2!3d-6.332902!4d106.05595?hl=en&shorturl=1"));
                startActivity(i);
            }
        });
    }

    private void setNavigationDrawer() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) findViewById(R.id.navigation); // initiate a Navigation View
        View headerView = navView.getHeaderView(0);
        final TextView hari = headerView.findViewById(R.id.hari);
        final TextView kondisi = headerView.findViewById(R.id.kondisi);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                hari.setText(currentDateandTime);
                handler.postDelayed(this, 1000);

                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

                if (timeOfDay >= 0 && timeOfDay < 9) {
                    kondisi.setText("Selamat Pagi");
                } else if (timeOfDay >= 9 && timeOfDay < 15) {
                    kondisi.setText("Selamat Siang");
                } else if (timeOfDay >= 15 && timeOfDay < 18) {
                    kondisi.setText("Selamat Sore");
                } else {
                    kondisi.setText("Selamat Malam");
                }
            }
        };
        runnable.run();
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
                if (itemId == R.id.nav_home) {
                    Intent i = new Intent(getApplicationContext(), menu.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                } else if (itemId == R.id.password) {
                    Intent i = new Intent(getApplicationContext(), setting.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                } else if (itemId == R.id.ketentuan) {
                    Intent i = new Intent(getApplicationContext(), ketentuan.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                } else if (itemId == R.id.nav_exit) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            listdepo.this);
                    alertDialogBuilder.setTitle("Anda yakin untuk Logout ?");
                    alertDialogBuilder
                            .setMessage("Klik Ya untuk keluar!")
                            .setCancelable(false)
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                public void onClick(DialogInterface dialog, int id) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    Intent intent = new Intent(listdepo.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else if (itemId == R.id.nav_calendar) {
                    Intent i = new Intent(getApplicationContext(), kalendar_event.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                }
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.commit(); // commit the changes
                    dLayout.closeDrawers(); // close the all open Drawer Views
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

        
        super.onDestroy();
    }
}