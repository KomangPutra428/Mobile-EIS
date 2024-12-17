package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.biodatamodel;
import com.example.eis2.Item.getNo_pengajuan_non_full;
import com.example.eis2.Item.namanikmodel;
import com.example.eis2.Item.sisacutimodel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.text_perusahaan;
import static com.example.eis2.menu.txt_alpha;

public class cuti extends AppCompatActivity {
    ImageButton cutikhusus, cutitahunan;
    public static TextView txt_location, txt_nomor_jabatan, perusahaan, tahun_cuti;
    TextView tanggaljoin;
    TextView hari_ini;
    static TextView hakcuti;
    public static TextView sisa_cuti, tanggalsekarang, tanggaldepan;
    private List<namanikmodel> movieItemList;
    private List<biodatamodel> movieItemList2;
    private List<sisacutimodel> movieItemList3;
    DrawerLayout dLayout;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuti);
        HttpsTrustManager.allowAllSSL();

        final String nik_baru = getIntent().getStringExtra(KEY_NIK);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });

        cutikhusus = (ImageButton) findViewById(R.id.khusus);
        cutitahunan = (ImageButton) findViewById(R.id.tahunan);
        setNavigationDrawer();
        hari_ini = (TextView) findViewById(R.id.hari_ini);
        hakcuti = (TextView) findViewById(R.id.hakcuti);

        tahun_cuti = (TextView) findViewById(R.id.tahun_cuti);
        sisa_cuti = (TextView) findViewById(R.id.sisa_cuti);
        perusahaan = (TextView) findViewById(R.id.perusahaan);
        txt_location = (TextView) findViewById(R.id.txt_location);
        txt_nomor_jabatan = (TextView) findViewById(R.id.txt_nomor_jabatan);
        tanggaljoin = (TextView) findViewById(R.id.tanggaljoin);
        tanggaldepan = (TextView)findViewById(R.id.tanggaldepan);
        tanggalsekarang = (TextView)findViewById(R.id.tanggalsekarang);

        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        String strdate1 = sdf1.format(c1.getTime());

        hari_ini.setText(strdate1);

        comparedate();

        if ("MPB PAKET".equalsIgnoreCase(text_perusahaan.getText().toString())) {
            cutikhusus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Maaf anda tidak bisa mengajukan cuti khusus", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            cutikhusus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(cuti.this, menucutikhusus.class);
                    startActivity(i);
                }
            });
        }

        gettanggaljoin();
        cutitahunan();

        getLokasi();
        movieItemList = new ArrayList<>();
        movieItemList2 = new ArrayList<>();
        movieItemList3 = new ArrayList<>();

    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
                            cuti.this);
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
                                    Intent intent = new Intent(cuti.this, MainActivity.class);
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

    private void cutitahunan() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK ,null);
        final int year = Calendar.getInstance().get(Calendar.YEAR ) -1;
        System.out.println("Test :" + year);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/cuti/index?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                sisacutimodel movieItem = new sisacutimodel(
                                        movieObject.getString("id_cuti"),
                                        movieObject.getInt("hak_cuti_utuh"),
                                        movieObject.getString("tahun"),
                                        movieObject.getString("start_efektif_cuti"));

                                movieItemList3.add(movieItem);
                            }
                            sisacutimodel item = movieItemList3.get(movieItemList3.size() - 1);
                            tahun_cuti.setText(String.valueOf(item.getStart_efektif_cuti()) + " =");
                            sisa_cuti.setText(String.valueOf(item.getHak_cuti_utuh()));

                            hakcuti.setText("Sisa Cuti Anda " + String.valueOf(item.getHak_cuti_utuh()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        hakcuti.setText("Anda Belum Mempunyai Hak Cuti Tahunan");
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

    private void gettanggaljoin() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK ,null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                biodatamodel movieItem = new biodatamodel(
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("jabatan_karyawan"),
                                        movieObject.getString("dept_struktur"),
                                        movieObject.getString("lokasi_struktur"),
                                        movieObject.getString("join_date_struktur"),
                                        movieObject.getString("status_karyawan_struktur"));

                                movieItemList2.add(movieItem);

                                tanggaljoin.setText(movieItem.getJoin_date_struktur());

                                String untildate = tanggaljoin.getText().toString();
                                SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
                                Calendar cal = Calendar.getInstance();
                                try {
                                    cal.setTime( dateFormat.parse(untildate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                cal.add( Calendar.YEAR, 1 );

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                String currentDateandTime = sdf.format(new Date());
                                tanggalsekarang.setText(currentDateandTime);

                                tanggaldepan.setText(movieItem.getJoin_date_struktur());

                                comparedate();

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
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void cnot(){
            cutitahunan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Maaf anda tidak bisa mengajukan cuti tahunan", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void comparedate(){

        if ("MPB PAKET".equalsIgnoreCase(text_perusahaan.getText().toString())) {
            cnot();
            } else {
                cutitahunan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(cuti.this, menucutitahunan.class);
                        startActivity(i);
                    }
                });
            }
        }


    private void getLokasi () {
        pDialog = new ProgressDialog(cuti.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK ,null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + nik_baru,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                namanikmodel movieItem = new namanikmodel(
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("level_jabatan_karyawan"),
                                        movieObject.getString("lokasi_struktur"),
                                        movieObject.getString("jabatan_struktur"),
                                        movieObject.getString("perusahaan_struktur")
                                );
                                movieItemList.add(movieItem);

                                txt_location.setText(movieItem.getLokasi_struktur());
                                txt_nomor_jabatan.setText(movieItem.getJabatan_struktur());
                                perusahaan.setText(movieItem.getPerusahaan_struktur());

                                hideDialog ();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Maaf ada kesalahan", Toast.LENGTH_SHORT).show();
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
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showDialog () {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog () {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());


        super.onDestroy();
    }
}

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String sekarang = tanggalsekarang.getText().toString();
//        String tahundepan = tanggaldepan.getText().toString();
//
//        Date date1 = formatter.parse(sekarang);
//        Date date2 = formatter.parse(tahundepan);
//
//        System.out.println("tanggal 1 = " + date1);
//        System.out.println("tanggal 2 = " + date2);

//        } else {
//            if (date1.compareTo(date2) <= 0) {
//                cutitahunan.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getApplicationContext(), "Maaf anda belum bisa mengajukan cuti tahunan", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }