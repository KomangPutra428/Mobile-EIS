package com.example.eis2;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_lokasi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eis2.Item.AndroidBug5497Workaround;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.approvaldinasfull;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class detail_daily extends AppCompatActivity {
    TextView tanggal_date, jam, keterangan, jam_realisasi, keterangan_realisasi;
    EditText jam_mulai, catatan, editalasan, pengganti_pekerjaan;
    Button batal, simpan;
    String latitude, longitude;
    AutoCompleteTextView status;
    LocationManager locationManager;
    ProgressDialog pDialog;
    LinearLayout timesession, realisasi;
    TextInputLayout start, finish, pengganti;
    TextInputLayout uraian, alasan, kategori;
    SharedPreferences sharedPreferences;

    EditText kategori_kegiatan;

    MaterialButton clear;

    String status_pekerjaan [] = {"Ya", "Tidak"};

    AutoCompleteTextView durasi_jam;

    String jams [] = {"1 Jam", "2 Jam",
            "3 Jam", "4 Jam",
            "5 Jam", "6 Jam",
            "7 Jam", "8 Jam",};

    MaterialToolbar dailynBar;
    NavigationView navigation;
    DrawerLayout drawer_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_daily);
        HttpsTrustManager.allowAllSSL();
//        AndroidBug5497Workaround.assistActivity(this);
        kategori_kegiatan = findViewById(R.id.kategori_kegiatan);
        uraian = findViewById(R.id.uraian);
        alasan = findViewById(R.id.alasan);
        editalasan = findViewById(R.id.editalasan);
        clear = findViewById(R.id.clear);
        kategori = findViewById(R.id.kategori);

        pengganti = findViewById(R.id.pengganti);
        pengganti_pekerjaan = findViewById(R.id.pengganti_pekerjaan);

        tanggal_date = findViewById(R.id.tanggal_date);
        status = findViewById(R.id.status);
        jam = findViewById(R.id.jam);
        keterangan = findViewById(R.id.keterangan);
        jam_mulai = findViewById(R.id.jam_mulai);
        durasi_jam = findViewById(R.id.durasijam);

        catatan = findViewById(R.id.catatan);
        final Handler handler2 = new Handler();
        Runnable refresh = new Runnable() {
            @Override
            public void run() {
                getLocation();
                handler2.postDelayed(this, 3000);
            }
        };
        handler2.postDelayed(refresh, 3000);

        batal = findViewById(R.id.batal);
        simpan = findViewById(R.id.simpan);
        timesession = findViewById(R.id.timesession);
        realisasi = findViewById(R.id.realisasi);

        jam_realisasi = findViewById(R.id.jam_realisasi);
        keterangan_realisasi = findViewById(R.id.keterangan_realisasi);

        start = findViewById(R.id.start);
        finish = findViewById(R.id.finish);

        dailynBar = findViewById(R.id.dailynBar);
        navigation = findViewById(R.id.navigation);
        drawer_layout = findViewById(R.id.drawer_layout);

        dailynBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(Gravity.LEFT);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catatan.setText("");
                editalasan.setText("");
                durasi_jam.setText("");
                pengganti_pekerjaan.setText("");
            }
        });

        durasi_jam.setAdapter(new ArrayAdapter<String>(detail_daily.this, android.R.layout.simple_dropdown_item_1line, jams));

        durasi_jam.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    durasi_jam.showDropDown();
            }
        });

        durasi_jam.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                durasi_jam.showDropDown();
                return false;
            }
        });

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
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
                            detail_daily.this);
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
                                    Intent intent = new Intent(detail_daily.this, MainActivity.class);
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
                    drawer_layout.closeDrawers(); // close the all open Drawer Views
                    return true;
                }
                return false;
            }
        });


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        status.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, status_pekerjaan));

        status.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    status.showDropDown();
            }
        });

        status.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                status.showDropDown();
                return false;
            }
        });


        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.getText().toString().equals("Ya")){
                    if(status.getText().toString().length() == 0){
                        status.setError("Pilih Status Pekerjaan");
                    } else if(jam_mulai.getText().toString().length() == 0){
                        jam_mulai.setError("Masukkan Jam Mulai");
                    } else if (durasi_jam.getText().toString().length() == 0){
                        durasi_jam.setError("Isi Durasi Jam");
                    } else if (catatan.getText().toString().length() == 0){
                        catatan.setError("Isi Uraian Pekerjaan");
                    } else if (latitude == null){
                        Toast.makeText(getApplicationContext(), "Lokasi belum ditemukan", Toast.LENGTH_SHORT).show();
                    } else {
                        pDialog = new ProgressDialog(detail_daily.this);
                        showDialog();
                        pDialog.setContentView(R.layout.progress_dialog);
                        pDialog.getWindow().setBackgroundDrawableResource(
                                android.R.color.transparent
                        );
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_realisasi",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        hideDialog();
                                        Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                                        finish();
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

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String tanggal1 = sdf.format(new Date());

                                String durasijam = durasi_jam.getText().toString();
                                String[] splited_text2 = durasijam.split(" ");
                                durasijam = splited_text2[0];
                                durasijam = durasijam.replace(" ", "");

                                final SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                                final String currentDateandTime = jam_mulai.getText().toString();
                                Date date = null;
                                try {
                                    date = sdf2.parse(currentDateandTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                final Calendar calendar = Calendar.getInstance();

                                calendar.setTime(date);
                                calendar.add(Calendar.HOUR, Integer.parseInt(durasijam));

                                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                                String strDate = dateFormat.format(calendar.getTime());

                                params.put("id", getIntent().getStringExtra("id"));

                                params.put("start_realisasi", jam_mulai.getText().toString());
                                params.put("end_realisasi", strDate);
                                params.put("ket_realisasi", catatan.getText().toString());
                                if(status.getText().toString().equals("Ya")){
                                    params.put("status", "1");
                                } else {
                                    params.put("status", "2");
                                }
                                params.put("pengganti", "Tidak Ada");

                                params.put("user_update", tanggal1);
                                params.put("lat", latitude);
                                params.put("lon", longitude);

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
                        RequestQueue requestQueue = Volley.newRequestQueue(detail_daily.this);
                        requestQueue.add(stringRequest);
                    }
                } else if(status.getText().toString().equals("Tidak")){
                    if(status.getText().toString().length() == 0){
                        status.setError("Pilih Status Pekerjaan");
                    } else if (editalasan.getText().toString().length() == 0){
                        editalasan.setError("Isi Alasan");
                    } else if (pengganti_pekerjaan.getText().toString().length() == 0){
                        pengganti_pekerjaan.setError("Isi Aktivitas Pengganti");
                    } else if (latitude == null){
                        Toast.makeText(getApplicationContext(), "Lokasi belum ditemukan", Toast.LENGTH_SHORT).show();
                    } else {
                        pDialog = new ProgressDialog(detail_daily.this);
                        showDialog();
                        pDialog.setContentView(R.layout.progress_dialog);
                        pDialog.getWindow().setBackgroundDrawableResource(
                                android.R.color.transparent
                        );
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_realisasi",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        hideDialog();
                                        Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                                        finish();
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

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String tanggal1 = sdf.format(new Date());

                                params.put("id", getIntent().getStringExtra("id"));

                                params.put("start_realisasi", "00:00:00");
                                params.put("end_realisasi", "00:00:00");
                                params.put("ket_realisasi", editalasan.getText().toString());
                                if(status.getText().toString().equals("Ya")){
                                    params.put("status", "1");
                                } else {
                                    params.put("status", "2");
                                }
                                params.put("pengganti", pengganti_pekerjaan.getText().toString());
                                params.put("user_update", tanggal1);
                                params.put("lat", latitude);
                                params.put("lon", longitude);

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
                        RequestQueue requestQueue = Volley.newRequestQueue(detail_daily.this);
                        requestQueue.add(stringRequest);
                    }
                }

            }
        });

        jam_mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(detail_daily.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        jam_mulai.setText(String.format("%02d:%02d:00", selectedHour, selectedMinute));

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Pilih Jam");
                mTimePicker.show();
            }
        });



        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_id?id=" + getIntent().getStringExtra("id"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                tanggal_date.setText(tanggalhari(movieObject.getString("date")));
                                jam.setText(movieObject.getString("nama_kategori") + " • " + movieObject.getString("start") + " - " + movieObject.getString("end"));
                                keterangan.setText(movieObject.getString("ket_plan"));
                                catatan.setText(movieObject.getString("ket_plan"));
                                jam_mulai.setText(movieObject.getString("start"));
                                kategori_kegiatan.setText(movieObject.getString("nama_kategori"));
                                jam_realisasi.setText("Realisasi : " + movieObject.getString("start_realisasi") + " - " + movieObject.getString("end_realisasi"));
                                keterangan_realisasi.setText(movieObject.getString("ket_realisasi"));

                                if(movieObject.getString("status").equals("0")){
                                    timesession.setVisibility(View.VISIBLE);
                                    realisasi.setVisibility(View.GONE);
                                } else {
                                    timesession.setVisibility(View.GONE);
                                    realisasi.setVisibility(View.VISIBLE);
                                }


                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                                Date startDate = simpleDateFormat.parse(movieObject.getString("start"));
                                Date endDate = simpleDateFormat.parse(movieObject.getString("end"));

                                long difference = endDate.getTime() - startDate.getTime();
                                if(difference<0)
                                {
                                    Date dateMax = simpleDateFormat.parse("24:00:00");
                                    Date dateMin = simpleDateFormat.parse("00:00:00");
                                    difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
                                }
                                int days = (int) (difference / (1000*60*60*24));
                                int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                                int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);

                                durasi_jam.setText(String.valueOf(hours) + " Jam");


                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Belum ada Pengajuan", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(status.getText().toString().equals("Ya")){
                    uraian.setVisibility(View.VISIBLE);
                    alasan.setVisibility(View.GONE);
                    pengganti.setVisibility(View.GONE);
                    start.setVisibility(View.VISIBLE);
                    finish.setVisibility(View.VISIBLE);
                    kategori.setVisibility(View.VISIBLE);
                } else {
                    uraian.setVisibility(View.GONE);
                    alasan.setVisibility(View.VISIBLE);
                    pengganti.setVisibility(View.VISIBLE);
                    start.setVisibility(View.GONE);
                    finish.setVisibility(View.GONE);
                    kategori.setVisibility(View.GONE);
                }
            }
        });


    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        GPSTracker gpsTracker = new GPSTracker(detail_daily.this);
        if(gpsTracker.canGetLocation()){
            longitude = (String.valueOf(gpsTracker.getLongitude()));
            latitude = (String.valueOf(gpsTracker.getLatitude()));
        }else{
            gpsTracker.showSettingsAlert();
        }

    }

    public static String tanggalhari(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        return convetDateFormat.format(date);
    }

    private void showDialog () {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog () {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}