package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.keteranganmodel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.dinas.txt_nomor_jab;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_nama;

public class absen_mobile extends AppCompatActivity {
    LocationManager locationManager;
    static TextView longitude;
    static TextView lat;
    static TextView date;
    static TextView nomor;
    private List<keteranganmodel> movieItemList;
    ProgressDialog pDialog;
    ListView list;
    static EditText namaedit;
    EditText periodeedit;
    SharedPreferences sharedPreferences;
    ImageButton jammasuk, jamkeluar, ceklokasi;
    DrawerLayout dLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_mobile);
        HttpsTrustManager.allowAllSSL();
        jammasuk = (ImageButton) findViewById(R.id.jammasuk);
        jamkeluar = (ImageButton) findViewById(R.id.jamkeluar);
        ceklokasi = (ImageButton) findViewById(R.id.ceklokasi);
        setNavigationDrawer();
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

        date = (TextView) findViewById(R.id.date);
        nomor = (TextView) findViewById(R.id.nomor);

        namaedit = (EditText) findViewById(R.id.namaedit);
        periodeedit = (EditText) findViewById(R.id.periodeedit);

        namaedit.setText(txt_nama.getText().toString());


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        date.setText(currentDateandTime);

        movieItemList = new ArrayList<>();
        list = findViewById(R.id.list);

        longitude = (TextView) findViewById(R.id.longitude);
        lat = (TextView) findViewById(R.id.lat);

        ceklokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + lat.getText().toString() + " " + longitude.getText().toString()));
                startActivity(browserIntent);
            }

        });

        final Handler handler = new Handler();
        Runnable refresh = new Runnable() {
            @Override
            public void run() {
                getLocation();
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(refresh, 3000);
        getBiodata();
    }

    private void getBiodata() {
        pDialog = new ProgressDialog(absen_mobile.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Absenmobile/index?nik_baru=" + nik_baru + "&shift_day=" + date.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);
                                nomor.setText(movieObject.getString("userid"));
                                System.out.println("hasil nomor = " + nomor.getText().toString());

                                absensi();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Maaf, ada kesalahan", Toast.LENGTH_SHORT).show();
                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };


        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        7200000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
                            absen_mobile.this);
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
                                    Intent intent = new Intent(absen_mobile.this, MainActivity.class);
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

    private void absensi() {
        movieItemList.clear();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/absensi/index?shift_day="+ date.getText().toString() +"&shift_day_2=" + date.getText().toString() + "&badgenumber=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final keteranganmodel movieItem = new keteranganmodel(
                                        movieObject.getString("shift_day"),
                                        movieObject.getString("F1"),
                                        movieObject.getString("depo_f1"),
                                        movieObject.getString("F4"),
                                        movieObject.getString("depo_f4"),
                                        movieObject.getString("ket_absensi"),
                                        movieObject.getString("name"));
                                movieItemList.add(movieItem);
                                hideDialog();

                                if(movieObject.getString("F1").equals("null") && movieObject.getString("F4").equals("null")){
                                    jammasuk.setImageResource(R.drawable.btn_masuk);
                                    jammasuk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(longitude.getText().toString().equals("long") && lat.getText().toString().equals("lat")){
                                                Toast.makeText(getApplicationContext(), "Maaf, Lokasi belum ditemukan", Toast.LENGTH_SHORT).show();
                                            } else {
                                                masuk();
                                            }
                                        }
                                    });
                                    jamkeluar.setImageResource(R.drawable.btn_keluarabu);
                                    jamkeluar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getApplicationContext(), "Maaf, tidak bisa absen pulang", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                if(!movieObject.getString("F1").equals("null")){
                                    jammasuk.setImageResource(R.drawable.btn_masukabu);
                                    jammasuk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getApplicationContext(), "Maaf, tidak bisa absen masuk", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    jamkeluar.setImageResource(R.drawable.btn_keluar);
                                    jamkeluar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(longitude.getText().toString().equals("long") && lat.getText().toString().equals("lat")){
                                                Toast.makeText(getApplicationContext(), "Maaf, Lokasi belum ditemukan", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Intent i = new Intent(absen_mobile.this, dailyactivity.class);
                                                startActivity(i);
                                            }
                                        }
                                    });
                                }

                                if(!movieObject.getString("F1").equals("null") && !movieObject.getString("F4").equals("null")){
                                    jammasuk.setImageResource(R.drawable.btn_masukabu);
                                    jammasuk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getApplicationContext(), "Maaf, tidak bisa absen masuk", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    jamkeluar.setImageResource(R.drawable.btn_keluarabu);
                                    jamkeluar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getApplicationContext(), "Maaf, tidak bisa absen pulang", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }
                            final kehadiran.ListViewAdapter adapter = new kehadiran.ListViewAdapter(movieItemList, getApplicationContext());
                            list.setAdapter(adapter);

                            Collections.sort(movieItemList, new Comparator<keteranganmodel>() {
                                public int compare(keteranganmodel o1, keteranganmodel o2) {
                                    if (o1.getShift_day() == null || o2.getShift_day() == null)
                                        return 0;
                                    return o2.getShift_day().compareTo(o1.getShift_day());
                                }
                            });


                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Maaf, anda belum pernah absen", Toast.LENGTH_SHORT).show();
                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void masuk(){
        pDialog = new ProgressDialog(absen_mobile.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/Absenmobile/index",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        absensimasuk();
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getApplicationContext(), "Maaf ada kesalahan", Toast.LENGTH_LONG).show();
            }
        })





        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            };

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                String nik_baru = sharedPreferences.getString(KEY_NIK ,null);
                String nama = namaedit.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String tanggal1 = sdf.format(new Date());
                String langitude = lat.getText().toString();
                String lon = longitude.getText().toString();

                params.put("nik", nik_baru);
                params.put("nama",nama);
                params.put("kat","MASUK");
                params.put("time", tanggal1);
                params.put("lat",langitude);
                params.put("lon",lon);

                return params;
            }



        };
        stringRequest2.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest2);
    }
    private void absensimasuk() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/Absenmobile/index_manualin",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                        absen_mobile.this.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "maaf ada kesalahan", Toast.LENGTH_LONG).show();
                    }
                }

        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            };

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String userid = nomor.getText().toString();
                String shift_day = date.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String in_manual = sdf.format(new Date());

                params.put("userid", userid);
                params.put("shift_day", shift_day);
                params.put("in_manual", in_manual);

                return params;
            }

        };

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        GPSTracker gpsTracker = new GPSTracker(absen_mobile.this);
        if(gpsTracker.canGetLocation()){
            lat.setText(String.valueOf(gpsTracker.getLatitude()));
            longitude.setText(String.valueOf(gpsTracker.getLongitude()));
        }else{
            gpsTracker.showSettingsAlert();
        }

    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

//        if (4 <= beta && beta <= 10) {
//            Intent broadcastIntent = new Intent();
//            broadcastIntent.setAction("restartservice");
//            broadcastIntent.setClass(this, Restarter.class);
//            this.sendBroadcast(broadcastIntent);
//        }
        super.onDestroy();
    }
}