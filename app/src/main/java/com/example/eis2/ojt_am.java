package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.animation.LayoutTransition;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.view.View.GONE;
import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;
import static com.example.eis2.menu.txt_nama;

public class ojt_am extends AppCompatActivity {
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    EditText name, posisipekerjaan, jawab;
    ImageButton buttonshow1, buttonshow2, buttonshow3, buttonshow4, buttonshow5, buttonshow6, buttonshow7, buttonshow8, buttonshow9;
    ImageButton tidaksetuju1, tidaksetuju2, tidaksetuju3, tidaksetuju4, tidaksetuju5, tidaksetuju6, tidaksetuju7, tidaksetuju8, tidaksetuju9;
    LinearLayout cmlinear, saleslinear, suplylinear, warehouselinear, ictlinear, hrdlinear, fleetlinear, financeoperationallinear, reviewlinear;
    ImageButton setuju1, setuju2, setuju3, setuju4, setuju5, setuju6, setuju7, setuju8, setuju9;
    ImageButton selesai1, selesai2, selesai3, selesai4, selesai5, selesai6, selesai7, selesai8, selesai9;

    ImageButton setuju10, setuju11, setuju12, setuju13, setuju14, setuju15, setuju16, setuju17, setuju18, setuju19;
    ImageButton buttonshow10, buttonshow11, buttonshow12, buttonshow13, buttonshow14, buttonshow15, buttonshow16, buttonshow17, buttonshow18, buttonshow19;
    ImageButton tidaksetuju10, tidaksetuju11, tidaksetuju12, tidaksetuju13, tidaksetuju14, tidaksetuju15, tidaksetuju16, tidaksetuju17, tidaksetuju18, tidaksetuju19;
    ImageButton selesai10, selesai11, selesai12, selesai13, selesai14, selesai15, selesai16, selesai17, selesai18, selesai19;

    LinearLayout profile, snd, Depo, sopdepo, operationaldepo, preseller, armada, penganansales, report, sm;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ojt_am);
        HttpsTrustManager.allowAllSSL();

        setNavigationDrawer();
        profile = (LinearLayout) findViewById(R.id.profile);
        snd = (LinearLayout) findViewById(R.id.snd);
        Depo = (LinearLayout) findViewById(R.id.Depo);
        sopdepo = (LinearLayout) findViewById(R.id.sopdepo);
        operationaldepo = (LinearLayout) findViewById(R.id.operationaldepo);
        preseller = (LinearLayout) findViewById(R.id.preseller);
        armada = (LinearLayout) findViewById(R.id.armada);
        penganansales = (LinearLayout) findViewById(R.id.penganansales);
        report = (LinearLayout) findViewById(R.id.report);
        sm = (LinearLayout) findViewById(R.id.sm);

        name = (EditText) findViewById(R.id.name);
        posisipekerjaan = (EditText) findViewById(R.id.posisipekerjaan);
        jawab = (EditText) findViewById(R.id.jawab);

        buttonshow1 = (ImageButton) findViewById(R.id.buttonshow1);
        buttonshow2 = (ImageButton) findViewById(R.id.buttonshow2);
        buttonshow3 = (ImageButton) findViewById(R.id.buttonshow3);
        buttonshow4 = (ImageButton) findViewById(R.id.buttonshow4);
        buttonshow5 = (ImageButton) findViewById(R.id.buttonshow5);
        buttonshow6 = (ImageButton) findViewById(R.id.buttonshow6);
        buttonshow7 = (ImageButton) findViewById(R.id.buttonshow7);
        buttonshow8 = (ImageButton) findViewById(R.id.buttonshow8);
        buttonshow9 = (ImageButton) findViewById(R.id.buttonshow9);
        buttonshow10 = (ImageButton) findViewById(R.id.buttonshow10);
        buttonshow11 = (ImageButton) findViewById(R.id.buttonshow11);
        buttonshow12 = (ImageButton) findViewById(R.id.buttonshow12);
        buttonshow13 = (ImageButton) findViewById(R.id.buttonshow13);
        buttonshow14 = (ImageButton) findViewById(R.id.buttonshow14);
        buttonshow15 = (ImageButton) findViewById(R.id.buttonshow15);
        buttonshow16 = (ImageButton) findViewById(R.id.buttonshow16);
        buttonshow17 = (ImageButton) findViewById(R.id.buttonshow17);
        buttonshow18 = (ImageButton) findViewById(R.id.buttonshow18);
        buttonshow19 = (ImageButton) findViewById(R.id.buttonshow19);


        setuju1 = (ImageButton) findViewById(R.id.setuju1);
        setuju2 = (ImageButton) findViewById(R.id.setuju2);
        setuju3 = (ImageButton) findViewById(R.id.setuju3);
        setuju4 = (ImageButton) findViewById(R.id.setuju4);
        setuju5 = (ImageButton) findViewById(R.id.setuju5);
        setuju6 = (ImageButton) findViewById(R.id.setuju6);
        setuju7 = (ImageButton) findViewById(R.id.setuju7);
        setuju8 = (ImageButton) findViewById(R.id.setuju8);
        setuju9 = (ImageButton) findViewById(R.id.setuju9);
        setuju10 = (ImageButton) findViewById(R.id.setuju10);
        setuju11 = (ImageButton) findViewById(R.id.setuju11);
        setuju12 = (ImageButton) findViewById(R.id.setuju12);
        setuju13 = (ImageButton) findViewById(R.id.setuju13);
        setuju14 = (ImageButton) findViewById(R.id.setuju14);
        setuju15 = (ImageButton) findViewById(R.id.setuju15);
        setuju16 = (ImageButton) findViewById(R.id.setuju16);
        setuju17 = (ImageButton) findViewById(R.id.setuju17);
        setuju18 = (ImageButton) findViewById(R.id.setuju18);
        setuju19 = (ImageButton) findViewById(R.id.setuju19);


        tidaksetuju1 = (ImageButton) findViewById(R.id.tidaksetuju1);
        tidaksetuju2 = (ImageButton) findViewById(R.id.tidaksetuju2);
        tidaksetuju3 = (ImageButton) findViewById(R.id.tidaksetuju3);
        tidaksetuju4 = (ImageButton) findViewById(R.id.tidaksetuju4);
        tidaksetuju5 = (ImageButton) findViewById(R.id.tidaksetuju5);
        tidaksetuju6 = (ImageButton) findViewById(R.id.tidaksetuju6);
        tidaksetuju7 = (ImageButton) findViewById(R.id.tidaksetuju7);
        tidaksetuju8 = (ImageButton) findViewById(R.id.tidaksetuju8);
        tidaksetuju9 = (ImageButton) findViewById(R.id.tidaksetuju9);
        tidaksetuju10 = (ImageButton) findViewById(R.id.tidaksetuju10);
        tidaksetuju11 = (ImageButton) findViewById(R.id.tidaksetuju11);
        tidaksetuju12 = (ImageButton) findViewById(R.id.tidaksetuju12);
        tidaksetuju13 = (ImageButton) findViewById(R.id.tidaksetuju13);
        tidaksetuju14 = (ImageButton) findViewById(R.id.tidaksetuju14);
        tidaksetuju15 = (ImageButton) findViewById(R.id.tidaksetuju15);
        tidaksetuju16 = (ImageButton) findViewById(R.id.tidaksetuju16);
        tidaksetuju17 = (ImageButton) findViewById(R.id.tidaksetuju17);
        tidaksetuju18 = (ImageButton) findViewById(R.id.tidaksetuju18);
        tidaksetuju19 = (ImageButton) findViewById(R.id.tidaksetuju19);


        selesai1 = (ImageButton) findViewById(R.id.selesai1);
        selesai2 = (ImageButton) findViewById(R.id.selesai2);
        selesai3 = (ImageButton) findViewById(R.id.selesai3);
        selesai4 = (ImageButton) findViewById(R.id.selesai4);
        selesai5 = (ImageButton) findViewById(R.id.selesai5);
        selesai6 = (ImageButton) findViewById(R.id.selesai6);
        selesai7 = (ImageButton) findViewById(R.id.selesai7);
        selesai8 = (ImageButton) findViewById(R.id.selesai8);
        selesai9 = (ImageButton) findViewById(R.id.selesai9);
        selesai10 = (ImageButton) findViewById(R.id.selesai10);

        selesai11 = (ImageButton) findViewById(R.id.selesai11);
        selesai12 = (ImageButton) findViewById(R.id.selesai12);
        selesai13 = (ImageButton) findViewById(R.id.selesai13);
        selesai14 = (ImageButton) findViewById(R.id.selesai14);
        selesai15 = (ImageButton) findViewById(R.id.selesai15);
        selesai16 = (ImageButton) findViewById(R.id.selesai16);
        selesai17 = (ImageButton) findViewById(R.id.selesai17);
        selesai18 = (ImageButton) findViewById(R.id.selesai18);
        selesai19 = (ImageButton) findViewById(R.id.selesai19);


        cmlinear = (LinearLayout) findViewById(R.id.cmlinear);
        saleslinear = (LinearLayout) findViewById(R.id.saleslinear);
        suplylinear = (LinearLayout) findViewById(R.id.suplylinear);
        warehouselinear = (LinearLayout) findViewById(R.id.warehouselinear);

        ictlinear = (LinearLayout) findViewById(R.id.ictlinear);
        hrdlinear = (LinearLayout) findViewById(R.id.hrdlinear);
        fleetlinear = (LinearLayout) findViewById(R.id.fleetlinear);
        financeoperationallinear = (LinearLayout) findViewById(R.id.financeoperationallinear);
        reviewlinear = (LinearLayout) findViewById(R.id.reviewlinear);

        getData();

        setuju1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 77; i <= 78; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final String nomor = String.valueOf(i);
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 78) {
                                        setuju1.setVisibility(GONE);
                                        tidaksetuju1.setVisibility(GONE);
                                        selesai1.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String nik_baru = getIntent().getStringExtra(KEY_NIK);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", nomor);
                            params.put("nik", nik_baru);
                            params.put("nama_pembimbing", txt_nama.getText().toString());
                            params.put("tanggal_pelaksana_pembimbing", formattedDate);

                            params.put("status_atasan", "1");
                            params.put("remark_atasan", "Sudah mempelajari fungsi Channel SOW");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 79; i <= 83; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final String nomor = String.valueOf(i);
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 83) {
                                        setuju2.setVisibility(GONE);
                                        tidaksetuju2.setVisibility(GONE);
                                        selesai2.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String nik_baru = getIntent().getStringExtra(KEY_NIK);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", nomor);
                            params.put("nik", nik_baru);
                            params.put("nama_pembimbing", txt_nama.getText().toString());
                            params.put("tanggal_pelaksana_pembimbing", formattedDate);

                            params.put("status_atasan", "1");
                            params.put("remark_atasan", "Sudah mempelajari Fungsi Sales Development");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 84; i <= 89; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final String nomor = String.valueOf(i);
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 89) {
                                        setuju3.setVisibility(GONE);
                                        tidaksetuju3.setVisibility(GONE);
                                        selesai3.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String nik_baru = getIntent().getStringExtra(KEY_NIK);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", nomor);
                            params.put("nik", nik_baru);
                            params.put("nama_pembimbing", txt_nama.getText().toString());
                            params.put("tanggal_pelaksana_pembimbing", formattedDate);

                            params.put("status_atasan", "1");
                            params.put("remark_atasan", "Sudah mempelajari Supply Chain");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 90; i <= 95; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final String nomor = String.valueOf(i);
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 95) {
                                        setuju4.setVisibility(GONE);
                                        tidaksetuju4.setVisibility(GONE);
                                        selesai4.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String nik_baru = getIntent().getStringExtra(KEY_NIK);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", nomor);
                            params.put("nik", nik_baru);
                            params.put("nama_pembimbing", txt_nama.getText().toString());
                            params.put("tanggal_pelaksana_pembimbing", formattedDate);

                            params.put("status_atasan", "1");
                            params.put("remark_atasan", "Sudah mempelajari Fungsi Warehouse Operational");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 96; i <= 101; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final String nomor = String.valueOf(i);
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 101) {
                                        setuju5.setVisibility(GONE);
                                        tidaksetuju5.setVisibility(GONE);
                                        selesai5.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String nik_baru = getIntent().getStringExtra(KEY_NIK);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", nomor);
                            params.put("nik", nik_baru);
                            params.put("nama_pembimbing", txt_nama.getText().toString());
                            params.put("tanggal_pelaksana_pembimbing", formattedDate);

                            params.put("status_atasan", "1");
                            params.put("remark_atasan", "Sudah mempelajari Fungsi ICT dan Internal Audit");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 102; i <= 107; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final String nomor = String.valueOf(i);
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 107) {
                                        setuju6.setVisibility(GONE);
                                        tidaksetuju6.setVisibility(GONE);
                                        selesai6.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String nik_baru = getIntent().getStringExtra(KEY_NIK);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", nomor);
                            params.put("nik", nik_baru);
                            params.put("nama_pembimbing", txt_nama.getText().toString());
                            params.put("tanggal_pelaksana_pembimbing", formattedDate);

                            params.put("status_atasan", "1");
                            params.put("remark_atasan", "Sudah mempelajari Fungsi HRD");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 108; i <= 113; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final String nomor = String.valueOf(i);
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 113) {
                                        setuju7.setVisibility(GONE);
                                        tidaksetuju7.setVisibility(GONE);
                                        selesai7.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String nik_baru = getIntent().getStringExtra(KEY_NIK);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", nomor);
                            params.put("nik", nik_baru);
                            params.put("nama_pembimbing", txt_nama.getText().toString());
                            params.put("tanggal_pelaksana_pembimbing", formattedDate);

                            params.put("status_atasan", "1");
                            params.put("remark_atasan", "Sudah mempelajari Fungsi Fleet dan GA");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju8.setVisibility(GONE);
                                tidaksetuju8.setVisibility(GONE);
                                selesai8.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        String nik_baru = getIntent().getStringExtra(KEY_NIK);
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "114");
                        params.put("nik", nik_baru);
                        params.put("nama_pembimbing", txt_nama.getText().toString());
                        params.put("tanggal_pelaksana_pembimbing", formattedDate);

                        params.put("status_atasan", "1");
                        params.put("remark_atasan", "Sudah mempelajari Fungsi Finance Operational");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju9.setVisibility(GONE);
                                tidaksetuju9.setVisibility(GONE);
                                selesai9.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        String nik_baru = getIntent().getStringExtra(KEY_NIK);
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "115");
                        params.put("nik", nik_baru);
                        params.put("nama_pembimbing", txt_nama.getText().toString());
                        params.put("tanggal_pelaksana_pembimbing", formattedDate);

                        params.put("status_atasan", "1");
                        params.put("remark_atasan", "Sudah mempelajari Review Induction");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju10.setVisibility(GONE);
                                tidaksetuju10.setVisibility(GONE);
                                selesai10.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        String nik_baru = getIntent().getStringExtra(KEY_NIK);
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "156");
                        params.put("nik", nik_baru);
                        params.put("nama_pembimbing", txt_nama.getText().toString());
                        params.put("tanggal_pelaksana_pembimbing", formattedDate);

                        params.put("status_atasan", "1");
                        params.put("remark_atasan", "Sudah mempelajari Induction profile perusahaan");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 157; i <= 163; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final String nomor = String.valueOf(i);
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 163) {
                                        setuju11.setVisibility(GONE);
                                        tidaksetuju11.setVisibility(GONE);
                                        selesai11.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String nik_baru = getIntent().getStringExtra(KEY_NIK);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", nomor);
                            params.put("nik", nik_baru);
                            params.put("nama_pembimbing", txt_nama.getText().toString());
                            params.put("tanggal_pelaksana_pembimbing", formattedDate);

                            params.put("status_atasan", "1");
                            params.put("remark_atasan", "Sudah mempelajari Induction SND TVIP");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 164; i <= 167; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final String nomor = String.valueOf(i);
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 167) {
                                        setuju12.setVisibility(GONE);
                                        tidaksetuju12.setVisibility(GONE);
                                        selesai12.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String nik_baru = getIntent().getStringExtra(KEY_NIK);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", nomor);
                            params.put("nik", nik_baru);
                            params.put("nama_pembimbing", txt_nama.getText().toString());
                            params.put("tanggal_pelaksana_pembimbing", formattedDate);

                            params.put("status_atasan", "1");
                            params.put("remark_atasan", "Sudah mempelajari Pengenalan Depo");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 168; i <= 177; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final String nomor = String.valueOf(i);
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 177) {
                                        setuju13.setVisibility(GONE);
                                        tidaksetuju13.setVisibility(GONE);
                                        selesai13.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String nik_baru = getIntent().getStringExtra(KEY_NIK);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", nomor);
                            params.put("nik", nik_baru);
                            params.put("nama_pembimbing", txt_nama.getText().toString());
                            params.put("tanggal_pelaksana_pembimbing", formattedDate);

                            params.put("status_atasan", "1");
                            params.put("remark_atasan", "Sudah mempelajari SOP Depo");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 185; i <= 190; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final String nomor = String.valueOf(i);
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 190) {
                                        setuju14.setVisibility(GONE);
                                        tidaksetuju14.setVisibility(GONE);
                                        selesai14.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String nik_baru = getIntent().getStringExtra(KEY_NIK);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", nomor);
                            params.put("nik", nik_baru);
                            params.put("nama_pembimbing", txt_nama.getText().toString());
                            params.put("tanggal_pelaksana_pembimbing", formattedDate);

                            params.put("status_atasan", "1");
                            params.put("remark_atasan", "Sudah mempelajari Operasional Depo");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 191; i <= 194; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final String nomor = String.valueOf(i);
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 194) {
                                        setuju15.setVisibility(GONE);
                                        tidaksetuju15.setVisibility(GONE);
                                        selesai15.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String nik_baru = getIntent().getStringExtra(KEY_NIK);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", nomor);
                            params.put("nik", nik_baru);
                            params.put("nama_pembimbing", txt_nama.getText().toString());
                            params.put("tanggal_pelaksana_pembimbing", formattedDate);

                            params.put("status_atasan", "1");
                            params.put("remark_atasan", "Sudah mempelajari Preseller");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 196; i <= 197; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final String nomor = String.valueOf(i);
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 197) {
                                        setuju16.setVisibility(GONE);
                                        tidaksetuju16.setVisibility(GONE);
                                        selesai16.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String nik_baru = getIntent().getStringExtra(KEY_NIK);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", nomor);
                            params.put("nik", nik_baru);
                            params.put("nama_pembimbing", txt_nama.getText().toString());
                            params.put("tanggal_pelaksana_pembimbing", formattedDate);

                            params.put("status_atasan", "1");
                            params.put("remark_atasan", "Sudah mempelajari Armada Distribusi");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );


                    final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                        setuju17.setVisibility(GONE);
                                        tidaksetuju17.setVisibility(GONE);
                                        selesai17.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String nik_baru = getIntent().getStringExtra(KEY_NIK);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", "198");
                            params.put("nik", nik_baru);
                            params.put("nama_pembimbing", txt_nama.getText().toString());
                            params.put("tanggal_pelaksana_pembimbing", formattedDate);

                            params.put("status_atasan", "1");
                            params.put("remark_atasan", "Sudah mempelajari Armada Distribusi");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                    requestQueue2.add(stringRequest2);
                }
        });

        setuju18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );


                final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju18.setVisibility(GONE);
                                tidaksetuju18.setVisibility(GONE);
                                selesai18.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        String nik_baru = getIntent().getStringExtra(KEY_NIK);
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "199");
                        params.put("nik", nik_baru);
                        params.put("nama_pembimbing", txt_nama.getText().toString());
                        params.put("tanggal_pelaksana_pembimbing", formattedDate);

                        params.put("status_atasan", "1");
                        params.put("remark_atasan", "Sudah mempelajari Action Plan & Sales Report");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_am.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );


                final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju19.setVisibility(GONE);
                                tidaksetuju19.setVisibility(GONE);
                                selesai19.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        String nik_baru = getIntent().getStringExtra(KEY_NIK);
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "200");
                        params.put("nik", nik_baru);
                        params.put("nama_pembimbing", txt_nama.getText().toString());
                        params.put("tanggal_pelaksana_pembimbing", formattedDate);

                        params.put("status_atasan", "1");
                        params.put("remark_atasan", "Sudah mempelajari Sharing SM & Presentation");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_am.this);
                requestQueue2.add(stringRequest2);
            }
        });

        double sizeInDP = 42;
        final int marginInDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP, getResources()
                        .getDisplayMetrics());

        double sizeInDP2 = 48;
        final int marginInDp2 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP2, getResources()
                        .getDisplayMetrics());
        buttonshow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = cmlinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    cmlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                cmlinear.setLayoutParams(params);
                buttonshow1.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = cmlinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    cmlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                cmlinear.setLayoutParams(params);
                buttonshow1.setVisibility(View.VISIBLE);
            }
        });

        buttonshow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = saleslinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    saleslinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                saleslinear.setLayoutParams(params);
                buttonshow2.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = saleslinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    saleslinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                saleslinear.setLayoutParams(params);
                buttonshow2.setVisibility(View.VISIBLE);
            }
        });

        buttonshow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = suplylinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    suplylinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                suplylinear.setLayoutParams(params);
                buttonshow3.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = suplylinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    suplylinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                suplylinear.setLayoutParams(params);
                buttonshow3.setVisibility(View.VISIBLE);
            }
        });

        buttonshow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = warehouselinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    warehouselinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                warehouselinear.setLayoutParams(params);
                buttonshow4.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = warehouselinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    warehouselinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                warehouselinear.setLayoutParams(params);
                buttonshow4.setVisibility(View.VISIBLE);
            }
        });

        buttonshow5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = ictlinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ictlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                ictlinear.setLayoutParams(params);
                buttonshow5.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = ictlinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ictlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                ictlinear.setLayoutParams(params);
                buttonshow5.setVisibility(View.VISIBLE);
            }
        });

        buttonshow6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = hrdlinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    hrdlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                hrdlinear.setLayoutParams(params);
                buttonshow6.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = hrdlinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    hrdlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                hrdlinear.setLayoutParams(params);
                buttonshow6.setVisibility(View.VISIBLE);
            }
        });

        buttonshow7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = fleetlinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    fleetlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                fleetlinear.setLayoutParams(params);
                buttonshow7.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = fleetlinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    fleetlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                fleetlinear.setLayoutParams(params);
                buttonshow7.setVisibility(View.VISIBLE);
            }
        });

        buttonshow8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = financeoperationallinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    financeoperationallinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                financeoperationallinear.setLayoutParams(params);
                buttonshow8.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = financeoperationallinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    financeoperationallinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                financeoperationallinear.setLayoutParams(params);
                buttonshow8.setVisibility(View.VISIBLE);
            }
        });

        buttonshow9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = reviewlinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    reviewlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                reviewlinear.setLayoutParams(params);
                buttonshow9.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = reviewlinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    reviewlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                reviewlinear.setLayoutParams(params);
                buttonshow9.setVisibility(View.VISIBLE);
            }
        });

        buttonshow10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = profile.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    profile.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                profile.setLayoutParams(params);
                buttonshow10.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = profile.getLayoutParams();
                params.height = marginInDp2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    profile.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                profile.setLayoutParams(params);
                buttonshow10.setVisibility(View.VISIBLE);
            }
        });

        buttonshow11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = snd.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    snd.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                snd.setLayoutParams(params);
                buttonshow11.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = snd.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    snd.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                snd.setLayoutParams(params);
                buttonshow11.setVisibility(View.VISIBLE);
            }
        });

        buttonshow12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = Depo.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Depo.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                Depo.setLayoutParams(params);
                buttonshow12.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = Depo.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Depo.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                Depo.setLayoutParams(params);
                buttonshow12.setVisibility(View.VISIBLE);
            }
        });

        buttonshow13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = sopdepo.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sopdepo.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                sopdepo.setLayoutParams(params);
                buttonshow13.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = sopdepo.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sopdepo.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                sopdepo.setLayoutParams(params);
                buttonshow13.setVisibility(View.VISIBLE);
            }
        });

        buttonshow14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = operationaldepo.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    operationaldepo.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                operationaldepo.setLayoutParams(params);
                buttonshow14.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = operationaldepo.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    operationaldepo.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                operationaldepo.setLayoutParams(params);
                buttonshow14.setVisibility(View.VISIBLE);
            }
        });

        buttonshow15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = preseller.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    preseller.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                preseller.setLayoutParams(params);
                buttonshow15.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = preseller.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    preseller.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                preseller.setLayoutParams(params);
                buttonshow15.setVisibility(View.VISIBLE);
            }
        });

        buttonshow16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = armada.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    armada.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                armada.setLayoutParams(params);
                buttonshow16.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = armada.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    armada.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                armada.setLayoutParams(params);
                buttonshow16.setVisibility(View.VISIBLE);
            }
        });

        buttonshow17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = penganansales.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    penganansales.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                penganansales.setLayoutParams(params);
                buttonshow17.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = penganansales.getLayoutParams();
                params.height = marginInDp2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    penganansales.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                penganansales.setLayoutParams(params);
                buttonshow17.setVisibility(View.VISIBLE);
            }
        });

        buttonshow18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = report.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    report.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                report.setLayoutParams(params);
                buttonshow18.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = report.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    report.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                report.setLayoutParams(params);
                buttonshow18.setVisibility(View.VISIBLE);
            }
        });

        buttonshow19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = sm.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sm.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                sm.setLayoutParams(params);
                buttonshow19.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = sm.getLayoutParams();
                params.height = marginInDp2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sm.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                sm.setLayoutParams(params);
                buttonshow19.setVisibility(View.VISIBLE);
            }
        });



        getBiodata();
        getAtasan();
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
    }

    private void getData() {
        String nik_baru = getIntent().getStringExtra(KEY_NIK);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/website/ojt/index?nik=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);
                                String id_materi = movieObject.getString("id_materi");
                                String status = movieObject.getString("status_atasan");

                                if(id_materi.equals("77")){
                                    if(status.equals("1")){
                                        setuju1.setVisibility(GONE);
                                        tidaksetuju1.setVisibility(GONE);
                                        selesai1.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("79")){
                                    if(status.equals("1")){
                                        setuju2.setVisibility(GONE);
                                        tidaksetuju2.setVisibility(GONE);
                                        selesai2.setVisibility(View.VISIBLE);

                                    }
                                }

                                if(id_materi.equals("84")){
                                    if(status.equals("1")){
                                        setuju3.setVisibility(GONE);
                                        tidaksetuju3.setVisibility(GONE);
                                        selesai3.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("90")){
                                    if(status.equals("1")){
                                        setuju4.setVisibility(GONE);
                                        tidaksetuju4.setVisibility(GONE);
                                        selesai4.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("96")){
                                    if(status.equals("1")){
                                        setuju5.setVisibility(GONE);
                                        tidaksetuju5.setVisibility(GONE);
                                        selesai5.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("102")){
                                    if(status.equals("1")){
                                        setuju6.setVisibility(GONE);
                                        tidaksetuju6.setVisibility(GONE);
                                        selesai6.setVisibility(View.VISIBLE);

                                    }
                                }

                                if(id_materi.equals("108")){
                                    if(status.equals("1")){
                                        setuju7.setVisibility(GONE);
                                        tidaksetuju7.setVisibility(GONE);
                                        selesai7.setVisibility(View.VISIBLE);

                                    }
                                }

                                if(id_materi.equals("114")){
                                    if(status.equals("1")){
                                        setuju8.setVisibility(GONE);
                                        tidaksetuju8.setVisibility(GONE);
                                        selesai8.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("115")){
                                    if(status.equals("1")){
                                        setuju9.setVisibility(GONE);
                                        tidaksetuju9.setVisibility(GONE);
                                        selesai9.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("156")){
                                    if(status.equals("1")){
                                        setuju10.setVisibility(GONE);
                                        tidaksetuju10.setVisibility(GONE);
                                        selesai10.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("157")){
                                    if(status.equals("1")){
                                        setuju11.setVisibility(GONE);
                                        tidaksetuju11.setVisibility(GONE);
                                        selesai11.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("164")){
                                    if(status.equals("1")){
                                        setuju12.setVisibility(GONE);
                                        tidaksetuju12.setVisibility(GONE);
                                        selesai12.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("168")){
                                    if(status.equals("1")){
                                        setuju13.setVisibility(GONE);
                                        tidaksetuju13.setVisibility(GONE);
                                        selesai13.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("185")){
                                    if(status.equals("1")){
                                        setuju14.setVisibility(GONE);
                                        tidaksetuju14.setVisibility(GONE);
                                        selesai14.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("191")){
                                    if(status.equals("1")){
                                        setuju15.setVisibility(GONE);
                                        tidaksetuju15.setVisibility(GONE);
                                        selesai15.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("196")){
                                    if(status.equals("1")){
                                        setuju16.setVisibility(GONE);
                                        tidaksetuju16.setVisibility(GONE);
                                        selesai16.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("198")){
                                    if(status.equals("1")){
                                        setuju17.setVisibility(GONE);
                                        tidaksetuju17.setVisibility(GONE);
                                        selesai17.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("199")){
                                    if(status.equals("1")){
                                        setuju18.setVisibility(GONE);
                                        tidaksetuju18.setVisibility(GONE);
                                        selesai18.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("200")){
                                    if(status.equals("1")){
                                        setuju19.setVisibility(GONE);
                                        tidaksetuju19.setVisibility(GONE);
                                        selesai19.setVisibility(View.VISIBLE);
                                    }
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

    private void getAtasan() {
        String nik_baru = getIntent().getStringExtra(KEY_NIK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);

                                jawab.setText(movieObject.getString("nama_atasan_struktur"));

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

    private void getBiodata() {
        String nik_baru = getIntent().getStringExtra(KEY_NIK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);

                                name.setText(movieObject.getString("nama_karyawan_struktur"));
                                posisipekerjaan.setText(movieObject.getString("jabatan_karyawan"));

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
                            ojt_am.this);
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
                                    Intent intent = new Intent(ojt_am.this, MainActivity.class);
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