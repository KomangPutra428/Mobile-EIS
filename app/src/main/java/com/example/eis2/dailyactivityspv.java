package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
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
import com.example.eis2.Item.EdittextListner;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.keteranganmodel;
import com.example.eis2.Item.pelanggaranmodel;
import com.google.android.material.navigation.NavigationView;
import com.example.eis2.SearchSpinner.SearchableSpinner;

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

import static android.view.View.GONE;
import static android.view.View.OVER_SCROLL_ALWAYS;
import static android.view.View.VISIBLE;
import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_nama;

public class dailyactivityspv extends AppCompatActivity {
    EditText namaedit, nik, keterangan, lokasiekternal;
    SharedPreferences sharedPreferences;
    Button finish, edit, update, batal;
    ProgressDialog pDialog;
    DrawerLayout dLayout;
    TextView longitude;
    TextView lat;
    LocationManager locationManager;
    RadioButton internal, eksternal;
    SearchableSpinner lokasiinternal;
    ArrayList<String> Lokasi = new ArrayList<>();
    ImageButton draft;
    TextView absen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyactivityspv);
        HttpsTrustManager.allowAllSSL();

        namaedit = findViewById(R.id.namaedit);
        nik = findViewById(R.id.nik);
        keterangan = findViewById(R.id.keterangan);
        finish = findViewById(R.id.simpan);
        edit = findViewById(R.id.edit);
        update = findViewById(R.id.update);
        batal = findViewById(R.id.batal);
        draft = findViewById(R.id.draft);

        internal = findViewById(R.id.internal);
        eksternal = findViewById(R.id.eksternal);

        lokasiekternal = findViewById(R.id.lokasiekternal);
        lokasiinternal = findViewById(R.id.lokasiinternal);

        longitude = (TextView) findViewById(R.id.longitude);
        lat = (TextView) findViewById(R.id.lat);

        namaedit.setText(txt_nama.getText().toString());

        absen = findViewById(R.id.absen);

        if(getIntent().getStringExtra("Dinas").equals("Dinas")){
            absen.setText("Daily Activity Dinas");
        }

        internal.setChecked(true);
        lokasiekternal.setVisibility(GONE);

        keterangan.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (keterangan.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            keterangan.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
                            keterangan.setOverScrollMode(OVER_SCROLL_ALWAYS);
                            return true;
                    }
                }
                return false;
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if(internal.isChecked()){
                            if (longitude.getText().toString().equals("long") && (lat.getText().toString().equals("lat"))){
                                Toast.makeText(getApplicationContext(), "Lokasi belum ditemukan", Toast.LENGTH_SHORT).show();
                            } else if (lokasiinternal.getSelectedItem().toString().equals("Pilih Lokasi")) {
                                Toast.makeText(getApplicationContext(), "Silahkan Pilih Lokasi", Toast.LENGTH_SHORT).show();
                            } else if(keterangan.getText().toString().length() == 0) {
                                Toast.makeText(getApplicationContext(), "Silahkan Isi Keterangan", Toast.LENGTH_SHORT).show();
                            } else {
                                if(getIntent().getStringExtra("Dinas").equals("Dinas")){
                                    pDialog = new ProgressDialog(dailyactivityspv.this);
                                    showDialog();
                                    pDialog.setContentView(R.layout.progress_dialog);
                                    pDialog.getWindow().setBackgroundDrawableResource(
                                            android.R.color.transparent
                                    );
                                    comparing_fullday();
                                } else {
                                    kehadiran();
                                }
                            }
                        } else if(eksternal.isChecked()){
                            if (longitude.getText().toString().equals("long") && (lat.getText().toString().equals("lat"))){
                                Toast.makeText(getApplicationContext(), "Lokasi belum ditemukan", Toast.LENGTH_SHORT).show();
                            } else if(lokasiekternal.getText().toString().length() == 0) {
                                Toast.makeText(getApplicationContext(), "Silahkan Isi Lokasi", Toast.LENGTH_SHORT).show();
                            } else if(keterangan.getText().toString().length() == 0) {
                                Toast.makeText(getApplicationContext(), "Silahkan Isi Keterangan", Toast.LENGTH_SHORT).show();
                            }  else {
                                if(getIntent().getStringExtra("Dinas").equals("Dinas")){
                                    pDialog = new ProgressDialog(dailyactivityspv.this);
                                    showDialog();
                                    pDialog.setContentView(R.layout.progress_dialog);
                                    pDialog.getWindow().setBackgroundDrawableResource(
                                            android.R.color.transparent
                                    );
                                    comparing_fullday();
                                } else {
                                    kehadiran();
                                }
                            }
                        }
            }
        });





        internal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    eksternal.setChecked(false);
                    lokasiekternal.setVisibility(GONE);
                    lokasiinternal.setVisibility(VISIBLE);


                }
            }
        });

        eksternal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    internal.setChecked(false);
                    lokasiinternal.setVisibility(GONE);
                    lokasiekternal.setVisibility(VISIBLE);

                }
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            OnGPS();
        } else {
            final Handler handler = new Handler();
            Runnable refresh = new Runnable() {
                @Override
                public void run() {
                    getLocation();
                    handler.postDelayed(this, 3000);
                }
            };
            handler.postDelayed(refresh, 3000);
        }

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

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        nik.setText(nik_baru);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        draft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dailyactivityspv.this, listtmp.class);
                startActivity(i);
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/lokasi/index", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Lokasi.add("Pilih Lokasi");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String lokasi = jsonObject1.getString("depo_nama");
                            if(!Lokasi.contains(lokasi)){
                                Lokasi.add(lokasi);
                            }
                        }
                    }
                    lokasiinternal.setTitle("Pilih Lokasi");
                    lokasiinternal.setAdapter(new ArrayAdapter<String>(dailyactivityspv.this, android.R.layout.simple_spinner_dropdown_item, Lokasi));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            dailyactivityspv.this);
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
                                    Intent intent = new Intent(dailyactivityspv.this, MainActivity.class);
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

    private void kehadiran(){
        pDialog = new ProgressDialog(dailyactivityspv.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/absensi/index?shift_day="+ formattedDate +"&shift_day_2="+ formattedDate +"&badgenumber="+ nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final String keteranganabsen = movieObject.getString("ket_absensi");

                                if(keteranganabsen.equals("AL")) {
                                    hideDialog();
                                    Toast.makeText(getApplicationContext(), "Silahkan Absen Terlebih dahulu", Toast.LENGTH_SHORT).show();
                                } else if(keteranganabsen.equals("LI")) {
                                    hideDialog();
                                    Toast.makeText(getApplicationContext(), "Jadwal Hari Ini Libur", Toast.LENGTH_SHORT).show();
                                } else {
                                    postketerangan();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        finish.setBackgroundColor(Color.parseColor("#D4D4D4"));
                        finish.setTextColor(Color.parseColor("#0F4C81"));
                        finish.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(), "Silahkan Absen Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                            }
                        });
                        hideDialog();


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

    private void comparing_fullday() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/dinas_full_day/index_dinas_hariini?nik_baru=" + nik_baru+ "&tanggal=" + currentDateandTime ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);
                                if(movieObject.getString("status_full_day").equals("0") || movieObject.getString("status_full_day").equals("1")){
                                    postketerangan();
                                } else if(movieObject.getString("status_full_day").equals("2")){
                                    comparing_nonfullday("Dinas Tidak Diapprove");
                                } else if(movieObject.getString("status_full_day").equals("3")){
                                    comparing_nonfullday("Dinas Sudah Hangus");
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        comparing_nonfullday("Anda belum mengajukan dinas");
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

    private void comparing_nonfullday(final String keterangan) {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/dinas_non_full_day/index_dinas_hariini?nik_baru=" + nik_baru+ "&tanggal=" + currentDateandTime ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);
                                if(movieObject.getString("status_non_full").equals("0") || movieObject.getString("status_non_full").equals("1")){
                                    postketerangan();
                                } else if(movieObject.getString("status_non_full").equals("2")){
                                    hideDialog();
                                    Toast.makeText(getApplicationContext(), "Dinas Tidak Diapprove", Toast.LENGTH_SHORT).show();
                                } else if(movieObject.getString("status_non_full").equals("3")){
                                    hideDialog();
                                    Toast.makeText(getApplicationContext(), "Dinas Sudah Hangus", Toast.LENGTH_SHORT).show();
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), keterangan, Toast.LENGTH_SHORT).show();
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


    private void postketerangan() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/Logactivity/index_tmp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "Data Sudah Disimpan", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(dailyactivityspv.this, listtmp.class);
                        keterangan.getText().clear();
                        lokasiinternal.setSelection(0);
                        lokasiekternal.getText().clear();
                        keterangan.clearFocus();
                        startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
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
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                params.put("nik", nik_baru);

                if(eksternal.isChecked()){
                    params.put("status_lokasi", "0");
                    params.put("lokasi", lokasiekternal.getText().toString());
                } else if (internal.isChecked()) {
                    params.put("status_lokasi", "1");
                    params.put("lokasi", lokasiinternal.getSelectedItem().toString());
                }

                params.put("keterangan", keterangan.getText().toString());
                params.put("lat", lat.getText().toString());
                params.put("lon", longitude.getText().toString());

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
        GPSTracker gpsTracker = new GPSTracker(dailyactivityspv.this);
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

        
        super.onDestroy();
    }
}