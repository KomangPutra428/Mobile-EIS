package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.TextViewFixTouchConsume;
import com.example.eis2.Item.approvalfulldaymodel;
import com.example.eis2.Item.biodatamodel;
import com.example.eis2.Item.fulldaymodel;
import com.example.eis2.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;

public class form_fullday extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    TextView no, tgl;
    ImageButton approval, download, cekmap;
    RadioButton diterima, tidakterima;
    RadioGroup opsiapproval;
    ProgressDialog pDialog;
    EditText pengajuan, namakaryawan, jenisizin, tidakhadir, karyawan, keterangan2, nikbaru, keterangantambahan;
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_fullday);
        HttpsTrustManager.allowAllSSL();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });

        no = (TextView) findViewById(R.id.no);
        tgl = (TextView) findViewById(R.id.tgl);
        setNavigationDrawer();
        pengajuan = (EditText) findViewById(R.id.pengajuan);
        namakaryawan = (EditText) findViewById(R.id.namakaryawan);
        jenisizin = (EditText) findViewById(R.id.jenisizin);
        tidakhadir = (EditText) findViewById(R.id.tidakhadir);
        karyawan = (EditText) findViewById(R.id.karyawan);
        nikbaru = (EditText) findViewById(R.id.nikbaru);

        keterangantambahan = (EditText) findViewById(R.id.keterangantambahan);


        keterangan2 = (EditText) findViewById(R.id.keterangan2);

        opsiapproval = (RadioGroup) findViewById(R.id.option);
        opsiapproval.setOnCheckedChangeListener(this);
        diterima = (RadioButton) findViewById(R.id.diterima);
        tidakterima = (RadioButton) findViewById(R.id.tidakterima);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        tgl.setText(currentDateandTime);

        approval = (ImageButton) findViewById(R.id.approve);

        download = (ImageButton) findViewById(R.id.download);
        cekmap = (ImageButton) findViewById(R.id.cekmap);


        approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(keterangantambahan.getText().toString().length() == 0){
                    keterangantambahan.setError("Masukkan Keterangan");
                } else {
                    approve();
                }
            }
        });

        getform();
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
                            form_fullday.this);
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
                                    Intent intent = new Intent(form_fullday.this, MainActivity.class);
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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.diterima) {
            no.setText("1");
        }
        if (i == R.id.tidakterima) {
            no.setText("2");

        }
    }

    private void getform() {
        final String idkaryawan = getIntent().getStringExtra(KEY_NIK);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/izin_full_day/index?id_full_day=" + idkaryawan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                        movieObject.getString("id_full_day");
                                        movieObject.getString("no_pengajuan_full_day");
                                        movieObject.getString("nik_baru");
                                        movieObject.getString("nama_karyawan_struktur");
                                        movieObject.getString("jabatan_full_day");
                                        movieObject.getString("lokasi_struktur");
                                        movieObject.getString("jenis_full_day");
                                        movieObject.getString("tanggal_absen");
                                        movieObject.getString("karyawan_pengganti");
                                        movieObject.getString("ket_tambahan");
                                        final String gambar = movieObject.getString("upload_full_day");
                                        final String lat = movieObject.getString("lat");
                                        final String lon = movieObject.getString("lon");

                                nikbaru.setText(movieObject.getString("nik_baru"));
                                pengajuan.setText(movieObject.getString("id_full_day"));
                                namakaryawan.setText(movieObject.getString("nama_karyawan_struktur"));
                                jenisizin.setText(movieObject.getString("jenis_full_day"));
                                tidakhadir.setText(movieObject.getString("tanggal_absen"));
                                karyawan.setText(movieObject.getString("karyawan_pengganti"));
                                keterangan2.setText(movieObject.getString("ket_tambahan"));


                                if(lat.equals("null") && (lon.equals("null"))){
                                    cekmap.setVisibility(View.INVISIBLE);
                                }

                                download.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hrd.tvip.co.id/eis/uploads/izin/full_day/" + gambar));
                                        startActivity(browserIntent);
                                    }
                                });

                                cekmap.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + lat + " " + lon));
                                        startActivity(browserIntent);
                                    }

                                });
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
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void approve() {
        pDialog = new ProgressDialog(form_fullday.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/izin_full_day/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                        form_fullday.this.finish();
                        postnotif();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (tidakterima.isChecked()) {
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                            form_fullday.this.finish();
                            postnotif();
                        } else if (diterima.isChecked()) {
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                            form_fullday.this.finish();
                            postnotif();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "maaf ada kesalahan", Toast.LENGTH_LONG).show();
                        }
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
                String nik = nikbaru.getText().toString();
                String tanggal2 = tidakhadir.getText().toString();
                String jenis = jenisizin.getText().toString();

                String idkaryawan = pengajuan.getText().toString();
                String status = no.getText().toString();
                String feedback1 = keterangantambahan.getText().toString();
                String tanggal = tgl.getText().toString();

                params.put("nik_baru", nik);
                params.put("tanggal_absen", tanggal2);
                params.put("jenis_full_day", jenis);

                params.put("id_full_day", idkaryawan);
                params.put("status_full_day", status);
                params.put("feedback_full_day", feedback1);
                params.put("tanggal_approval", tanggal);

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

    private void postnotif() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/Notifikasi/index_token_nik?nik_baru=" + nikbaru.getText().toString(),
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

                                hideDialog();


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

                                if (diterima.isChecked()) {
                                    params.put("body", "Selamat pengajuan IZIN FULL DAY anda sudah di APPROVE");
                                } else if (tidakterima.isChecked()) {
                                    params.put("body", "Maaf pengajuan IZIN FULL DAY anda NOT APPROVE");
                                }

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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(form_fullday.this);
                        requestQueue2.add(stringRequest2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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