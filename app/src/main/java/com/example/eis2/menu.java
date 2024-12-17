package com.example.eis2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.example.eis2.Item.SharedPrefManager;
import com.example.eis2.Item.deptmodel;
import com.example.eis2.Item.namanikmodel;
import com.example.eis2.Item.statuscutikhusus;
import com.example.eis2.Item.statuscutitahunan;
import com.example.eis2.Item.statusdinasfullday;
import com.example.eis2.Item.statusdinasnonfullday;
import com.example.eis2.Item.statusizinfullday;
import com.example.eis2.Item.statusizinnonfullday;
import com.example.eis2.firebase.MyFirebaseServices;
import com.example.eis2.firebase.checking_foreground;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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


import cn.pedant.SweetAlert.SweetAlertDialog;
import in.myinnos.imagesliderwithswipeslibrary.Animations.DescriptionAnimation;
import in.myinnos.imagesliderwithswipeslibrary.SliderLayout;
import in.myinnos.imagesliderwithswipeslibrary.SliderTypes.BaseSliderView;
import in.myinnos.imagesliderwithswipeslibrary.SliderTypes.TextSliderView;
import in.myinnos.imagesliderwithswipeslibrary.Tricks.ViewPagerEx;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;
import static com.example.eis2.Item.LoginItem.KEY_JABATAN;
import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.izin.txt_nomor;


public class menu extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {
    SliderLayout image_place_holder;

    public static String branchLokasi, department;
    private InstallStateUpdatedListener listener;

    private AppUpdateInfo appUpdateInfo;  // Declare at the class level
    private static final int UPDATE_REQUEST_CODE = 123;

    private boolean reloadNeed = true;


    String statusUpdate;
    SweetAlertDialog warning;

    private static final String TAG = "PushNotification";
    private static final String CHANNEL_ID = "101";

    ImageButton biodata, izin, kehadiran, dinas, cuti, Shifting, payslip, resignbutton, qrcode, other, video;
    TextView izincount, izinnoncount, jumlahizin, dinascount, dinasnoncount, jumlahdinas,
            cutitahunancount, cutikhususcount, jumlahcuti, txt_jabatan2, txt_depart;
    public static TextView txt_alpha, text_perusahaan, text_jabatan, txt_lokasi, txt_dept, txt_nama, txt_nik;
    private List<namanikmodel> movieItemList;
    private List<deptmodel> dept;

    HashMap<String, String> url_maps;

    String nik_baru, level_jabatan_karyawan;

    private List<statusizinfullday> movieItemList1;
    private List<statusizinnonfullday> movieItemList2;

    private List<statusdinasfullday> movieItemList3;
    private List<statusdinasnonfullday> movieItemList4;

    private List<statuscutitahunan> movieItemList5;
    private List<statuscutikhusus> movieItemList6;

    SharedPreferences sharedPreferences;

    AppUpdateManager mUpdateManager;

    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_CODE_UPDATE = 123;

    private int installedVersionCode; // Version code of the installed app
    private int latestVersionCode;    // Latest version code fetched from the backend

    private static final String getURL = "https://hrd.tvip.co.id/mobile_eis_2/gambar.php";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        HttpsTrustManager.allowAllSSL();



        // Fetch the latest version code from your backend server


        other = (ImageButton) findViewById(R.id.other);
        biodata = (ImageButton) findViewById(R.id.biodata);
        izin = (ImageButton) findViewById(R.id.izin);
        kehadiran = (ImageButton) findViewById(R.id.kehadiran);
        dinas = (ImageButton) findViewById(R.id.dinas);
        cuti = (ImageButton) findViewById(R.id.cuti);
        resignbutton = (ImageButton) findViewById(R.id.resignbutton);
        qrcode = (ImageButton) findViewById(R.id.qr);
        Shifting = (ImageButton) findViewById(R.id.Shifting);
        payslip = (ImageButton) findViewById(R.id.slipgaji);
        video = findViewById(R.id.video);
        izincount = (TextView) findViewById(R.id.izincount);
        izincount.setVisibility(View.GONE);
        izinnoncount = (TextView) findViewById(R.id.izinnoncount);
        izinnoncount.setVisibility(View.GONE);
        txt_lokasi = (TextView) findViewById(R.id.txt_lokasi);
        txt_dept = (TextView) findViewById(R.id.txt_dept);
        txt_depart = (TextView) findViewById(R.id.txt_depart);

        cutitahunancount = (TextView) findViewById(R.id.cutitahunancount);
        cutitahunancount.setVisibility(View.GONE);
        cutikhususcount = (TextView) findViewById(R.id.cutikhususcount);
        cutikhususcount.setVisibility(View.GONE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }

        }
        mUpdateManager = AppUpdateManagerFactory.create(menu.this);

        checkForUpdate();

        dinascount = (TextView) findViewById(R.id.dinascount);
        dinascount.setVisibility(View.GONE);
        dinasnoncount = (TextView) findViewById(R.id.dinasnoncount);
        dinasnoncount.setVisibility(View.GONE);

        Drawable image = (Drawable) getResources().getDrawable(R.drawable.border_image);

        image_place_holder = (SliderLayout) findViewById(R.id.image_place_holder);

//        Intent intent = getIntent();
//
//        // Check if the intent contains data
//        if (intent != null && intent.getAction() != null && intent.getAction().equals("OPEN_ACTIVITY")) {
//            String extraValue = intent.getStringExtra("extra_key");
//            System.out.println("Extra = " + extraValue);
//        }
        FirebaseApp.initializeApp(menu.this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, getURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray movieArray = obj.getJSONArray("data");

                    for (int i = 0; i < movieArray.length(); i++) {
                        final JSONObject movieObject = movieArray.getJSONObject(i);
                        url_maps = new HashMap<String, String>();
                        String a = movieObject.getString("id");
                        String b = movieObject.getString("submit_date");
                        String c = "https://hrd.tvip.co.id/hris/uploads/pengumuman/" + movieObject.getString("gambar");
                        url_maps.put(a + " - " + b, c);
                        for (String name : url_maps.keySet()) {

                            TextSliderView textSliderView = new TextSliderView(menu.this);
                            textSliderView
                                    .descriptionSize(12)
                                    .image(url_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(menu.this);


                            //add your extra information
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle().putString("extra", name);

                            image_place_holder.addSlider(textSliderView);

                            image_place_holder.setPresetTransformer(SliderLayout.Transformer.DepthPage);
                            image_place_holder.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            image_place_holder.setCustomAnimation(new DescriptionAnimation());
                            image_place_holder.setDuration(4000);
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
                        Toast.makeText(getApplicationContext(), "Maaf, ada kesalahan", Toast.LENGTH_SHORT).show();
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
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image_place_holder.setClipToOutline(true);
        }

        txt_nik = (TextView) findViewById(R.id.txt_nik);
        txt_nama = (TextView) findViewById(R.id.txt_nama);
        txt_alpha = (TextView) findViewById(R.id.txt_jabatan);
        jumlahizin = (TextView) findViewById(R.id.jumlahizin);
        text_jabatan = (TextView) findViewById(R.id.text_jabatan);
        jumlahdinas = (TextView) findViewById(R.id.jumlahdinas);
        jumlahcuti = (TextView) findViewById(R.id.jumlahcuti);
        text_perusahaan = (TextView) findViewById(R.id.text_perusahaan);
        txt_jabatan2 = (TextView) findViewById(R.id.txt_jabatan2);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        txt_nik.setText(sharedPreferences.getString(KEY_NIK, null));

        movieItemList = new ArrayList<>();
        if (sharedPreferences.contains(KEY_NIK)) {
            getnama();
        }


        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        System.out.println("nik baru = " + nik_baru);

        getdept();

        biodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(menu.this, manu_profil.class);
                startActivity(i);
            }
        });
        izin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(menu.this, izin.class);
                i.putExtra(KEY_JABATAN, level_jabatan_karyawan);
                startActivity(i);
            }
        });
        kehadiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Intent i = new Intent(menu.this, detail_absensi.class);
                    startActivity(i);
                } else if ("2".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Intent i = new Intent(menu.this, detail_absensi.class);
                    startActivity(i);
                } else if ("3".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Intent i = new Intent(menu.this, detail_absensi.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(menu.this, spv_absensi.class);
                    startActivity(i);
                }
            }
        });
        dinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(menu.this, dinas.class);
                startActivity(i);
            }
        });
        cuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(menu.this, cuti.class);
                startActivity(i);
            }
        });
        Shifting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Intent i = new Intent(menu.this, shifting.class);
                    startActivity(i);
                } else if ("2".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Intent i = new Intent(menu.this, shifting.class);
                    startActivity(i);
                } else if ("3".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Intent i = new Intent(menu.this, shifting.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(menu.this, spv_shifting.class);
                    startActivity(i);
                }
            }
        });

        payslip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    String url = "https://payme.tvip.co.id/slip_eis/welcome";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else if ("2".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    String url = "https://payme.tvip.co.id/slip_eis/welcome";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else if ("3".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    String url = "https://payme.tvip.co.id/slip_eis/welcome";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {
                    Intent intent = new Intent(menu.this, menu_slipgaji.class);
                    startActivity(intent);
                }

            }
        });

        resignbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(menu.this, "Maaf, Fitur Resign sedang ada maintenance", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(menu.this, resign.class);
//                startActivity(intent);
            }
        });


        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, qrscanner.class);
                startActivity(intent);
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(menu.this, lainnya.class);
                startActivity(i);
            }
        });

        dept = new ArrayList<>();
        movieItemList1 = new ArrayList<>();
        movieItemList2 = new ArrayList<>();

        movieItemList3 = new ArrayList<>();
        movieItemList4 = new ArrayList<>();

        movieItemList5 = new ArrayList<>();
        movieItemList6 = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(
                menu.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                menu.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }

    private void checkForUpdate() {
        mUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            this.appUpdateInfo = appUpdateInfo;  // Initialize appUpdateInfo here
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Prompt the user to update
                showUpdateDialog();
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_NOT_AVAILABLE) {
                // App is up to date, continue as normal
            }
        }).addOnFailureListener(e -> {
            // Handle error in fetching update info
        });
    }

    private void showUpdateDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        menu.this);
                alertDialogBuilder.setIcon(R.drawable.icon);
                alertDialogBuilder.setTitle("Silahkan update versi aplikasi anda terlebih dahulu!");
                alertDialogBuilder
                        .setCancelable(false)
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int id) {
                                finishAffinity();
                                System.exit(0);
                            }
                        })
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User wants to update the app
                            // Start the update process
                            try {
                                mUpdateManager.startUpdateFlowForResult(
                                        appUpdateInfo,
                                        AppUpdateType.IMMEDIATE,
                                        menu.this,
                                        UPDATE_REQUEST_CODE);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        }, 1500);


    }

    private void postNotifCutiTahunan() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/Notifikasi/index_token?no_jabatan_karyawan=" + txt_nomor.getText().toString() + "&lokasi_hrd=" + txt_lokasi.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                postNotifikasi(movieObject.getString("device_token"));


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    private void postNotifikasi(String device_token) {
                        final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/Push_Notification/push_notif_eis.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

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

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                                params.put("device", device_token);
                                params.put("body", "Pengajuan Cuti Tahunan Atas Nama " + txt_nama.getText().toString() + " Siap Untuk Di Approve");

                                System.out.println("notif = " + params);


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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(menu.this);
                        requestQueue2.add(stringRequest2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void postNotifCutiKhusus() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/Notifikasi/index_token?no_jabatan_karyawan=" + txt_nomor.getText().toString() + "&lokasi_hrd=" + txt_lokasi.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                postNotifikasi(movieObject.getString("device_token"));


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    private void postNotifikasi(String device_token) {
                        final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/Push_Notification/push_notif_eis.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

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

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                                params.put("device", device_token);
                                params.put("body", "Pengajuan Cuti Khusus Atas Nama " + txt_nama.getText().toString() + " Siap Untuk Di Approve");

                                System.out.println("notif = " + params);


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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(menu.this);
                        requestQueue2.add(stringRequest2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void postNotifDinasNonFullDay() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/Notifikasi/index_token?no_jabatan_karyawan=" + txt_nomor.getText().toString() + "&lokasi_hrd=" + txt_lokasi.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                postNotifikasi(movieObject.getString("device_token"));


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    private void postNotifikasi(String device_token) {
                        final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/Push_Notification/push_notif_eis.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

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

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                                params.put("device", device_token);
                                params.put("body", "Pengajuan Dinas Non Full Day Atas Nama " + txt_nama.getText().toString() + " Siap Untuk Di Approve");

                                System.out.println("notif = " + params);


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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(menu.this);
                        requestQueue2.add(stringRequest2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void postNotifDinasFullDay() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/Notifikasi/index_token?no_jabatan_karyawan=" + txt_nomor.getText().toString() + "&lokasi_hrd=" + txt_lokasi.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                postNotifikasi(movieObject.getString("device_token"));


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    private void postNotifikasi(String device_token) {
                        final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/Push_Notification/push_notif_eis.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

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

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                                params.put("device", device_token);
                                params.put("body", "Pengajuan Dinas Full Day Atas Nama " + txt_nama.getText().toString() + " Siap Untuk Di Approve");

                                System.out.println("notif = " + params);


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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(menu.this);
                        requestQueue2.add(stringRequest2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void getdept() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + nik_baru,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                deptmodel movieItem = new deptmodel(
                                        movieObject.getString("jabatan_karyawan"));
                                txt_depart.setText(movieObject.getString("jabatan_karyawan"));

                                dept.add(movieItem);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

    private void getnama() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + nik_baru,
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
                                        movieObject.getString("perusahaan_struktur"));

                                txt_nama.setText(movieItem.getNama_karyawan_struktur());
                                txt_alpha.setText(movieItem.getLevel_jabatan_karyawan());
                                text_jabatan.setText(movieItem.getJabatan_struktur());
                                txt_lokasi.setText(movieItem.getLokasi_struktur());
                                text_perusahaan.setText(movieItem.getPerusahaan_struktur());

                                getToken(txt_lokasi.getText().toString(), movieObject.getString("szBranch"));

                                branchLokasi = movieObject.getString("szBranch");
                                department = movieObject.getString("nama_departement");

                                txt_alpha.setVisibility(View.INVISIBLE);
                                text_jabatan.setVisibility(View.INVISIBLE);
                                txt_lokasi.setVisibility(View.INVISIBLE);
                                text_perusahaan.setVisibility(View.INVISIBLE);

                                String nik = movieObject.getString("nik_baru");


                                int beta = Integer.parseInt(txt_alpha.getText().toString());
                                if (text_jabatan.getText().toString().equals("jabatan")) {
                                    getnama();
                                } else if (4 <= beta && beta <= 10) {
                                    getDetail();
                                }

                                if (4 <= beta && beta <= 7) {
                                    DateFormat dateFormat = new SimpleDateFormat("kk:mm:ss");
                                    Date date = new Date();
                                    System.out.println("jam = " + dateFormat.format(date));

                                    String dateStr = "20:00:00";
                                    SimpleDateFormat dateForm = new SimpleDateFormat("kk:mm:ss");
                                    Date convertedDate = new Date();
                                    try {
                                        convertedDate = dateForm.parse(dateStr);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    DateFormat dateFormat2 = new SimpleDateFormat("kk:mm:ss");

                                    String actual = dateFormat.format(date);
                                    String limit = dateFormat2.format(convertedDate);

                                    String[] parts = actual.split(":");
                                    Calendar cal1 = Calendar.getInstance();
                                    cal1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                                    cal1.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                                    cal1.set(Calendar.SECOND, Integer.parseInt(parts[2]));

                                    parts = limit.split(":");
                                    Calendar cal2 = Calendar.getInstance();
                                    cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                                    cal2.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                                    cal2.set(Calendar.SECOND, Integer.parseInt(parts[2]));

                                    cal2.add(Calendar.DATE, 1);

                                    if (cal1.after(cal2)) {
                                        Date c = Calendar.getInstance().getTime();
                                        System.out.println("Current time => " + c);

                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                        String formattedDate = df.format(c);
                                        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Absenmobile/index_keterangan?nik_baru=" + nik + "&tanggal=" + formattedDate,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {

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

                                        stringRequest2.setRetryPolicy(
                                                new DefaultRetryPolicy(
                                                        500000,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                ));
                                        RequestQueue requestQueue2 = Volley.newRequestQueue(menu.this);
                                        requestQueue2.add(stringRequest2);
                                    }
                                }

//                                if(!text_jabatan.getText().toString().equals("jabatan")){
//                                        int alpha = Integer.parseInt(txt_alpha.getText().toString());
//                                        if (4 <= alpha && alpha <= 10) {
//                                            Thread t = new Thread() {
//                                                @Override
//                                                public void run() {
//                                                    try {
//                                                        while (!isInterrupted()) {
//                                                            Thread.sleep(300000);
////                                                            7200000
//                                                            runOnUiThread(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    getDetail();
//                                                                    System.out.println("hasil =" + txt_alpha.getText().toString());
//                                                                }
//                                                            });
//                                                        }
//                                                    } catch (InterruptedException e) {
//                                                    }
//                                                }
//                                            };
//                                            t.start();
//                                        }
//                                }

                                if ("1".equalsIgnoreCase(txt_alpha.getText().toString())) {
                                    jumlahizin.setVisibility(View.INVISIBLE);
                                    jumlahdinas.setVisibility(View.INVISIBLE);
                                    jumlahcuti.setVisibility(View.INVISIBLE);
                                } else if ("2".equalsIgnoreCase(txt_alpha.getText().toString())) {
                                    jumlahizin.setVisibility(View.INVISIBLE);
                                    jumlahdinas.setVisibility(View.INVISIBLE);
                                    jumlahcuti.setVisibility(View.INVISIBLE);
                                } else if ("3".equalsIgnoreCase(txt_alpha.getText().toString())) {
                                    jumlahizin.setVisibility(View.INVISIBLE);
                                    jumlahdinas.setVisibility(View.INVISIBLE);
                                    jumlahcuti.setVisibility(View.INVISIBLE);
                                }

                                movieItemList.add(movieItem);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

    private void getDetail() {
        String lokasi = text_jabatan.getText().toString();
        if (!lokasi.equals("jabatan")) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/izin_full_day/index_atasan?jabatan_struktur=" + lokasi,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                int number1 = 0;

                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    statusizinfullday movieItem = new statusizinfullday(
                                            movieObject.getString("lokasi_struktur"),
                                            movieObject.getString("status_full_day"));

                                    if (!txt_lokasi.getText().toString().equals("Pusat")) {
                                        if (movieObject.getString("status_full_day").contains("0") && movieObject.getString("lokasi_struktur").equals(txt_lokasi.getText().toString()))
                                            number1++;
                                        {
                                            izincount.setText(String.valueOf(number1));
                                        }
                                    } else if (txt_lokasi.getText().toString().equals("Pusat")) {
                                        if (movieObject.getString("status_full_day").contains("0"))
                                            number1++;
                                        {
                                            izincount.setText(String.valueOf(number1));

                                        }
                                    }
                                    movieItemList1.add(movieItem);


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            izincount.setText("0");
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

            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/izin_non_full_day/index_atasan?jabatan_struktur=" + lokasi,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                int number2 = 0;

                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    statusizinnonfullday movieItem = new statusizinnonfullday(
                                            movieObject.getString("lokasi_struktur"),
                                            movieObject.getString("status_non_full"));
                                    if (!txt_lokasi.getText().toString().equals("Pusat")) {
                                        if (movieObject.getString("status_non_full").contains("0") && movieObject.getString("lokasi_struktur").equals(txt_lokasi.getText().toString()))
                                            number2++;
                                        {
                                            izinnoncount.setText(String.valueOf(number2));
                                        }
                                    } else if (txt_lokasi.getText().toString().equals("Pusat")) {
                                        if (movieObject.getString("status_non_full").contains("0"))
                                            number2++;
                                        {
                                            izinnoncount.setText(String.valueOf(number2));
                                        }
                                    }
                                    movieItemList2.add(movieItem);
                                }
                                jumlahizin();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            jumlahizin();
                            izinnoncount.setText("0");
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

            StringRequest stringRequest3 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/dinas_full_day/index_atasan?jabatan_struktur=" + lokasi,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                int number3 = 0;

                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    statusdinasfullday movieItem = new statusdinasfullday(
                                            movieObject.getString("lokasi_struktur"),
                                            movieObject.getString("status_full_day"));

                                    if (!txt_lokasi.getText().toString().equals("Pusat")) {
                                        if (movieObject.getString("status_full_day").contains("0") && movieObject.getString("lokasi_struktur").equals(txt_lokasi.getText().toString()))
                                            number3++;
                                        {
                                            dinascount.setText(String.valueOf(number3));
                                        }
                                    } else if (txt_lokasi.getText().toString().equals("Pusat")) {
                                        if (movieObject.getString("status_full_day").contains("0"))
                                            number3++;
                                        {
                                            dinascount.setText(String.valueOf(number3));
                                        }
                                    }
                                    movieItemList3.add(movieItem);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dinascount.setText("0");
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

            StringRequest stringRequest4 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/dinas_non_full_day/index_atasan?jabatan_struktur=" + lokasi,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                int number4 = 0;

                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    final statusdinasnonfullday movieItem = new statusdinasnonfullday(
                                            movieObject.getString("lokasi_struktur"),
                                            movieObject.getString("status_non_full"));

                                    if (!txt_lokasi.getText().toString().equals("Pusat")) {
                                        if (movieObject.getString("status_non_full").contains("0") && movieObject.getString("lokasi_struktur").equals(txt_lokasi.getText().toString()))
                                            number4++;
                                        {
                                            dinasnoncount.setText(String.valueOf(number4));
                                        }
                                    } else if (txt_lokasi.getText().toString().equals("Pusat")) {
                                        if (movieObject.getString("status_non_full").contains("0"))
                                            number4++;
                                        {
                                            dinasnoncount.setText(String.valueOf(number4));
                                        }
                                    }

                                    movieItemList4.add(movieItem);
                                }
                                jumlahdinas();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            jumlahdinas();
                            dinasnoncount.setText("0");
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

            StringRequest stringRequest5 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/cuti_tahunan/index_atasan?jabatan_struktur=" + lokasi,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                int number5 = 0;

                                for (int i = 0; i < movieArray.length(); i++) {
                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    statuscutitahunan movieItem = new statuscutitahunan(
                                            movieObject.getString("lokasi_struktur"),
                                            movieObject.getString("status_cuti_tahunan"));

                                    if (!txt_lokasi.getText().toString().equals("Pusat")) {
                                        if (movieObject.getString("status_cuti_tahunan").contains("0") && movieObject.getString("lokasi_struktur").equals(txt_lokasi.getText().toString()))
                                            number5++;
                                        {
                                            cutitahunancount.setText(String.valueOf(number5));
                                        }
                                    } else if (txt_lokasi.getText().toString().equals("Pusat")) {
                                        if (movieObject.getString("status_cuti_tahunan").contains("0"))
                                            number5++;
                                        {
                                            cutitahunancount.setText(String.valueOf(number5));
                                        }
                                    }

                                    movieItemList5.add(movieItem);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cutitahunancount.setText("0");
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

            StringRequest stringRequest6 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/cuti_khusus/index_atasan?jabatan_struktur=" + lokasi,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                int number6 = 0;

                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    statuscutikhusus movieItem = new statuscutikhusus(
                                            movieObject.getString("lokasi_struktur"),
                                            movieObject.getString("status_cuti_khusus"));

                                    if (movieObject.getString("status_cuti_khusus").contains("0")) {
                                        if (!txt_lokasi.getText().toString().equals("Pusat")) {
                                            if (movieObject.getString("lokasi_struktur").equals(txt_lokasi.getText().toString())) {
                                                number6++;
                                                cutikhususcount.setText(String.valueOf(number6));
                                            }
                                        } else {
                                            number6++;
                                            cutikhususcount.setText(String.valueOf(number6));
                                        }
                                    }


                                    movieItemList6.add(movieItem);
                                }
                                jumlahcuti();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            jumlahcuti();
                            cutikhususcount.setText("0");
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


            stringRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            stringRequest2.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            stringRequest3.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            stringRequest4.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            stringRequest5.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            stringRequest6.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
            requestQueue.add(stringRequest2);
            requestQueue.add(stringRequest3);
            requestQueue.add(stringRequest4);
            requestQueue.add(stringRequest5);
            requestQueue.add(stringRequest6);

        }
    }

    private void postNotifIzinNonFullDay() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/Notifikasi/index_token?no_jabatan_karyawan=" + txt_nomor.getText().toString() + "&lokasi_hrd=" + txt_lokasi.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                postNotifikasi(movieObject.getString("device_token"));


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    private void postNotifikasi(String device_token) {
                        final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/Push_Notification/push_notif_eis.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

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

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                                params.put("device", device_token);
                                params.put("body", "Pengajuan Izin Non Full Day Atas Nama " + txt_nama.getText().toString() + " Siap Untuk Di Approve");

                                System.out.println("notif = " + params);


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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(menu.this);
                        requestQueue2.add(stringRequest2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void postNotifIzinFullDay() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/Notifikasi/index_token?no_jabatan_karyawan=" + txt_nomor.getText().toString() + "&lokasi_hrd=" + txt_lokasi.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                postNotifikasi(movieObject.getString("device_token"));


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    private void postNotifikasi(String device_token) {
                        final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/Push_Notification/push_notif_eis.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

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

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                                params.put("device", device_token);
                                params.put("body", "Pengajuan Izin Full Day Atas Nama " + txt_nama.getText().toString() + " Siap Untuk Di Approve");

                                System.out.println("notif = " + params);


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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(menu.this);
                        requestQueue2.add(stringRequest2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onPause() {
        super.onPause();
//        int beta = Integer.parseInt(txt_alpha.getText().toString());
//
//        if (4 <= beta && beta <= 10) {
//            Intent broadcastIntent = new Intent();
//            broadcastIntent.setAction("restartservice");
//            broadcastIntent.setClass(this, Restarter.class);
//            this.sendBroadcast(broadcastIntent);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        image_place_holder.stopAutoCycle();
//        int beta = Integer.parseInt(txt_alpha.getText().toString());
//
//        if (4 <= beta && beta <= 10) {
//            Intent broadcastIntent = new Intent();
//            broadcastIntent.setAction("restartservice");
//            broadcastIntent.setClass(this, Restarter.class);
//            this.sendBroadcast(broadcastIntent);
//        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (reloadNeed) {
            getStatus();
        }


        this.reloadNeed = false;

        getnama();
        getDetail();


        statusUpdate = "lanjutkan";


    }

    private void getStatus() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + nik_baru,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);


                                if (movieObject.getString("status_update").equals("0")) {

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            menu.this);
                                    alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.icon));
                                    alertDialogBuilder
                                            .setTitle("Perhatian")
                                            .setMessage(Html.fromHtml("Anda belum melengkapi <b>Data Karyawan</b>. Silahkan lakukan update dan lengkapi data tersebut"))
                                            .setIcon(R.drawable.icon)
                                            .setCancelable(false)
                                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent intent = new Intent(menu.this, biodata.class);
                                                    startActivity(intent);
                                                    reloadNeed = true;
                                                }
                                            })
                                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    finish();
                                                    finishAffinity();
                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();


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

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        getDetail();
    }

    private void jumlahizin() {
        int num1 = Integer.valueOf(izinnoncount.getText().toString());
        int num2 = Integer.valueOf(izincount.getText().toString());
        int sum = num1 + num2;
        System.out.println("angka1 =" + num1);
        System.out.println("angka2 =" + num2);

        System.out.println("angka =" + sum);
        jumlahizin.setText(Integer.toString(sum));


    }

    private void jumlahdinas() {
        int num3 = (int) Double.parseDouble(dinascount.getText().toString());
        int num4 = (int) Double.parseDouble(dinasnoncount.getText().toString());

        int sum2 = num3 + num4;
        jumlahdinas.setText(Integer.toString(sum2));

    }

    private void jumlahcuti() {
        int num5 = (int) Double.parseDouble(cutikhususcount.getText().toString());
        int num6 = (int) Double.parseDouble(cutitahunancount.getText().toString());

        int sum3 = num5 + num6;
        System.out.println("Test Cuti =" + sum3);
        jumlahcuti.setText(Integer.toString(sum3));

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("Anda yakin untuk Keluar ?");
        alertDialogBuilder
                .setMessage("Klik Ya untuk keluar!")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void getToken(final String s, final String szBranch) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<String> task) {
                //If task is failed then
                if (!task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Failed to get the Token");
                }

                //Token
                String token = task.getResult();
                updateDeviceId(s, szBranch, token);

                Log.d(TAG, "onComplete: " + token);
            }
        });
    }

    private void updateDeviceId(final String lokasi, final String szBranch, final String token) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/master/Notifikasi/index_cekDeviceId",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("nik_baru", sharedPreferences.getString(KEY_NIK, null));
                params.put("nama_karyawan", txt_nama.getText().toString());
                params.put("kode_cabang", szBranch);

                params.put("lokasi", lokasi);
                params.put("device_brand", Build.BRAND);

                params.put("device_model", Build.MODEL);
                params.put("device_sdk", String.valueOf(Build.VERSION.SDK_INT));
                params.put("device_version", Build.VERSION.RELEASE);
                params.put("apps_version", "3.10.9");

                params.put("apps_last_open", currentDateandTime2);
                params.put("device_token", token);


                return params;
            }

        };
        stringRequest2.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue2 = Volley.newRequestQueue(menu.this);
        requestQueue2.getCache().clear();
        requestQueue2.add(stringRequest2);

    }

    public static String[] storage_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storage_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storage_permissions_33;
        } else {
            p = storage_permissions;
        }
        return p;
    }



}



