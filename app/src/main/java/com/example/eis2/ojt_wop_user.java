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

public class ojt_wop_user extends AppCompatActivity {
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    EditText name, posisipekerjaan, jawab, onthejobtext;

    ImageButton buttonshow1, buttonshow2, buttonshow3, buttonshow4, buttonshow5,
            buttonshow6, buttonshow7, buttonshow8, buttonshow9, buttonshow10,
            buttonshow11, buttonshow12, buttonshow13, buttonshow14, buttonshow15,
            buttonshow16;

    ImageButton tidaksetuju1, tidaksetuju2, tidaksetuju3, tidaksetuju4, tidaksetuju5,
            tidaksetuju6, tidaksetuju7, tidaksetuju8, tidaksetuju9, tidaksetuju10,
            tidaksetuju11, tidaksetuju12, tidaksetuju13, tidaksetuju14, tidaksetuju15,
            tidaksetuju16;

    ImageButton setuju1, setuju2, setuju3, setuju4, setuju5,
            setuju6, setuju7, setuju8, setuju9, setuju10,
            setuju11, setuju12, setuju13, setuju14, setuju15,
            setuju16;

    ImageButton selesai1, selesai2, selesai3, selesai4, selesai5,
            selesai6, selesai7, selesai8, selesai9, selesai10,
            selesai11, selesai12, selesai13, selesai14, selesai15,
            selesai16;

    ProgressDialog pDialog;

    LinearLayout profile, sistem, targetlinear, deskripsilinear, onthejoblinear, observasi, armada, pengecekan, pencatatan, fifo, wa, qo,
            aqc, bongkarmuat, problem, evaluasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ojt_wop_user);
        HttpsTrustManager.allowAllSSL();

        setNavigationDrawer();
        name = (EditText) findViewById(R.id.name);
        posisipekerjaan = (EditText) findViewById(R.id.posisipekerjaan);
        jawab = (EditText) findViewById(R.id.jawab);
        onthejobtext = (EditText) findViewById(R.id.onthejobtext);

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

        profile = (LinearLayout) findViewById(R.id.profile);
        sistem = (LinearLayout) findViewById(R.id.sistem);
        targetlinear = (LinearLayout) findViewById(R.id.targetlinear);
        deskripsilinear = (LinearLayout) findViewById(R.id.deskripsilinear);
        onthejoblinear = (LinearLayout) findViewById(R.id.onthejoblinear);
        observasi = (LinearLayout) findViewById(R.id.observasi);
        armada = (LinearLayout) findViewById(R.id.armada);
        pengecekan = (LinearLayout) findViewById(R.id.pengecekan);
        pencatatan = (LinearLayout) findViewById(R.id.pencatatan);
        fifo = (LinearLayout) findViewById(R.id.fifo);
        wa = (LinearLayout) findViewById(R.id.wa);
        qo = (LinearLayout) findViewById(R.id.qo);
        aqc = (LinearLayout) findViewById(R.id.aqc);
        bongkarmuat = (LinearLayout) findViewById(R.id.bongkarmuat);
        problem = (LinearLayout) findViewById(R.id.problem);
        evaluasi = (LinearLayout) findViewById(R.id.evaluasi);

        double sizeInDP = 42;
        final int marginInDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP, getResources()
                        .getDisplayMetrics());

        double sizeInDP2 = 65;
        final int marginInDp2 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP2, getResources()
                        .getDisplayMetrics());

        double sizeInDP3 = 48;
        final int marginInDp3 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP3, getResources()
                        .getDisplayMetrics());



        getData();

        setuju1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju1.setVisibility(GONE);
                                tidaksetuju1.setVisibility(GONE);
                                selesai1.setVisibility(View.VISIBLE);
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
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        String jabatan = text_jabatan.getText().toString();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);
                        String lokasi = txt_lokasi.getText().toString();

                        params.put("id_materi", "33");
                        params.put("id_user", "1");
                        params.put("kode_materi", "2");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", lokasi);

                        params.put("remark_user", "Induction telah dijelaskan");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 34; i <= 39; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 39) {
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
                            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                            String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                            String jabatan = text_jabatan.getText().toString();
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);
                            String lokasi = txt_lokasi.getText().toString();

                            params.put("id_materi", String.valueOf(finalI));
                            params.put("id_user", "1");
                            params.put("kode_materi", "2");
                            params.put("nik", nik_baru);

                            params.put("jabatan", jabatan);
                            params.put("tanggal_pelaksana_peserta", formattedDate);
                            params.put("status_user", "1");
                            params.put("lokasi", lokasi);

                            params.put("remark_user", "Sistem dan ketentuan telah dijelaskan");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });


        setuju3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 40; i <= 45; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(finalI == 45) {
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
                            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                            String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                            String jabatan = text_jabatan.getText().toString();
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);
                            String lokasi = txt_lokasi.getText().toString();

                            params.put("id_materi", String.valueOf(finalI));
                            params.put("id_user", "1");
                            params.put("kode_materi", "2");
                            params.put("nik", nik_baru);

                            params.put("jabatan", jabatan);
                            params.put("tanggal_pelaksana_peserta", formattedDate);
                            params.put("status_user", "1");
                            params.put("lokasi", lokasi);

                            params.put("remark_user", "Target Telah dijelaskan");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju4.setVisibility(GONE);
                                tidaksetuju4.setVisibility(GONE);
                                selesai4.setVisibility(View.VISIBLE);
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
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        String jabatan = text_jabatan.getText().toString();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);
                        String lokasi = txt_lokasi.getText().toString();

                        params.put("id_materi", "46");
                        params.put("id_user", "1");
                        params.put("kode_materi", "2");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", lokasi);

                        params.put("remark_user", "Telah mempelajari job description");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                    final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                        setuju5.setVisibility(GONE);
                                        tidaksetuju5.setVisibility(GONE);
                                        selesai5.setVisibility(View.VISIBLE);
                                        hideDialog();
                                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
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
                            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                            String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                            String jabatan = text_jabatan.getText().toString();
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);

                            params.put("id_materi", "201");
                            params.put("id_user", "1");
                            params.put("kode_materi", "2");
                            params.put("nik", nik_baru);

                            params.put("jabatan", jabatan);
                            params.put("tanggal_pelaksana_peserta", formattedDate);
                            params.put("status_user", "1");
                            params.put("lokasi", txt_lokasi.getText().toString());

                            params.put("remark_user", "Pengenalan perusahaan telah dijelaskan");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                    requestQueue2.add(stringRequest2);
            }
        });

        setuju6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju6.setVisibility(GONE);
                                tidaksetuju6.setVisibility(GONE);
                                selesai6.setVisibility(View.VISIBLE);
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
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
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        String jabatan = text_jabatan.getText().toString();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "202");
                        params.put("id_user", "1");
                        params.put("kode_materi", "2");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", txt_lokasi.getText().toString());

                        params.put("remark_user", "Product Knowledge all SKU dan Perkenalan ke seluruh Team telah diperkenalkan");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju7.setVisibility(GONE);
                                tidaksetuju7.setVisibility(GONE);
                                selesai7.setVisibility(View.VISIBLE);
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
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
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        String jabatan = text_jabatan.getText().toString();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "203");
                        params.put("id_user", "1");
                        params.put("kode_materi", "2");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", txt_lokasi.getText().toString());

                        params.put("remark_user", "Pengenalan Armada Distribusi & Pabrikan telah dijelaskan");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju8.setVisibility(GONE);
                                tidaksetuju8.setVisibility(GONE);
                                selesai8.setVisibility(View.VISIBLE);
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
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
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        String jabatan = text_jabatan.getText().toString();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "204");
                        params.put("id_user", "1");
                        params.put("kode_materi", "2");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", txt_lokasi.getText().toString());

                        params.put("remark_user", "Training Proses Pengecekan IN dan OUT Armada distribusi dan Armada pabrikan telah dijelaskan");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju9.setVisibility(GONE);
                                tidaksetuju9.setVisibility(GONE);
                                selesai9.setVisibility(View.VISIBLE);
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
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
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        String jabatan = text_jabatan.getText().toString();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "205");
                        params.put("id_user", "1");
                        params.put("kode_materi", "2");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", txt_lokasi.getText().toString());

                        params.put("remark_user", "Training Proses Pencatatan & Pelaporan telah dijelaskan");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju10.setVisibility(GONE);
                                tidaksetuju10.setVisibility(GONE);
                                selesai10.setVisibility(View.VISIBLE);
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
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
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        String jabatan = text_jabatan.getText().toString();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "206");
                        params.put("id_user", "1");
                        params.put("kode_materi", "2");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", txt_lokasi.getText().toString());

                        params.put("remark_user", "Training proses FIFO & FEFO, Stock Opname telah dijelaskan");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju11.setVisibility(GONE);
                                tidaksetuju11.setVisibility(GONE);
                                selesai11.setVisibility(View.VISIBLE);
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
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
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        String jabatan = text_jabatan.getText().toString();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "207");
                        params.put("id_user", "1");
                        params.put("kode_materi", "2");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", txt_lokasi.getText().toString());

                        params.put("remark_user", "Training Input laporan Wa Web telah dilaksanakan");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju12.setVisibility(GONE);
                                tidaksetuju12.setVisibility(GONE);
                                selesai12.setVisibility(View.VISIBLE);
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
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
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        String jabatan = text_jabatan.getText().toString();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "208");
                        params.put("id_user", "1");
                        params.put("kode_materi", "2");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", txt_lokasi.getText().toString());

                        params.put("remark_user", "Training Input laporan Qo telah dilaksanakan");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju13.setVisibility(GONE);
                                tidaksetuju13.setVisibility(GONE);
                                selesai13.setVisibility(View.VISIBLE);
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
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
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        String jabatan = text_jabatan.getText().toString();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "209");
                        params.put("id_user", "1");
                        params.put("kode_materi", "2");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", txt_lokasi.getText().toString());

                        params.put("remark_user", "Training AQC (Aqua Quality Comitment),Kode pabrikan & Standar susunan produk telah dilaksanakan");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju14.setVisibility(GONE);
                                tidaksetuju14.setVisibility(GONE);
                                selesai14.setVisibility(View.VISIBLE);
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
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
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        String jabatan = text_jabatan.getText().toString();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "210");
                        params.put("id_user", "1");
                        params.put("kode_materi", "2");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", txt_lokasi.getText().toString());

                        params.put("remark_user", "Training Proses Bongkar muat Armada LASAH ,JUGRACK,DISTRIBUSI ( PRAKTEK LAPANGAN TERDOKUMENTASI ) telah dilaksanakan");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju15.setVisibility(GONE);
                                tidaksetuju15.setVisibility(GONE);
                                selesai15.setVisibility(View.VISIBLE);
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
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
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        String jabatan = text_jabatan.getText().toString();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "211");
                        params.put("id_user", "1");
                        params.put("kode_materi", "2");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", txt_lokasi.getText().toString());

                        params.put("remark_user", "Training Problem Solving (Penyelesaian / Penanganan Masalah ) telah dilaksanakan");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_wop_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/website/ojt/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setuju16.setVisibility(GONE);
                                tidaksetuju16.setVisibility(GONE);
                                selesai16.setVisibility(View.VISIBLE);
                                onthejobtext.setLongClickable(false);
                                onthejobtext.setFocusable(false);
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
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
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        String jabatan = text_jabatan.getText().toString();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);

                        params.put("id_materi", "212");
                        params.put("id_user", "1");
                        params.put("kode_materi", "2");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", txt_lokasi.getText().toString());

                        params.put("remark_user", onthejobtext.getText().toString());

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_wop_user.this);
                requestQueue2.add(stringRequest2);
            }
        });



        buttonshow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = profile.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    profile.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                profile.setLayoutParams(params);
                buttonshow1.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = profile.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    profile.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                profile.setLayoutParams(params);
                buttonshow1.setVisibility(View.VISIBLE);
            }
        });

        buttonshow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = sistem.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sistem.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                sistem.setLayoutParams(params);
                buttonshow2.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = sistem.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sistem.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                sistem.setLayoutParams(params);
                buttonshow2.setVisibility(View.VISIBLE);
            }
        });

        buttonshow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = targetlinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    targetlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                targetlinear.setLayoutParams(params);
                buttonshow3.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = targetlinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    targetlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                targetlinear.setLayoutParams(params);
                buttonshow3.setVisibility(View.VISIBLE);
            }
        });

        buttonshow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = deskripsilinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    deskripsilinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                deskripsilinear.setLayoutParams(params);
                buttonshow4.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = deskripsilinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    deskripsilinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                deskripsilinear.setLayoutParams(params);
                buttonshow4.setVisibility(View.VISIBLE);
            }
        });

        buttonshow5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = onthejoblinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    onthejoblinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                onthejoblinear.setLayoutParams(params);
                buttonshow5.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = onthejoblinear.getLayoutParams();
                params.height = marginInDp3;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    onthejoblinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                onthejoblinear.setLayoutParams(params);
                buttonshow5.setVisibility(View.VISIBLE);
            }
        });

        buttonshow6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = observasi.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    observasi.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                observasi.setLayoutParams(params);
                buttonshow6.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = observasi.getLayoutParams();
                params.height = marginInDp2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    observasi.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                observasi.setLayoutParams(params);
                buttonshow6.setVisibility(View.VISIBLE);
            }
        });

        buttonshow7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = armada.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    armada.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                armada.setLayoutParams(params);
                buttonshow7.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = armada.getLayoutParams();
                params.height = marginInDp3;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    armada.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                armada.setLayoutParams(params);
                buttonshow7.setVisibility(View.VISIBLE);
            }
        });

        buttonshow8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = pengecekan.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    pengecekan.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                pengecekan.setLayoutParams(params);
                buttonshow8.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = pengecekan.getLayoutParams();
                params.height = marginInDp3;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    pengecekan.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                pengecekan.setLayoutParams(params);
                buttonshow8.setVisibility(View.VISIBLE);
            }
        });

        buttonshow9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = pencatatan.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    pencatatan.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                pencatatan.setLayoutParams(params);
                buttonshow9.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = pencatatan.getLayoutParams();
                params.height = marginInDp3;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    pencatatan.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                pencatatan.setLayoutParams(params);
                buttonshow9.setVisibility(View.VISIBLE);
            }
        });

        buttonshow10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = fifo.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    fifo.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                fifo.setLayoutParams(params);
                buttonshow10.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = fifo.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    fifo.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                fifo.setLayoutParams(params);
                buttonshow10.setVisibility(View.VISIBLE);
            }
        });

        buttonshow11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = wa.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    wa.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                wa.setLayoutParams(params);
                buttonshow11.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = wa.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    wa.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                wa.setLayoutParams(params);
                buttonshow11.setVisibility(View.VISIBLE);
            }
        });

        buttonshow12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = qo.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    qo.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                qo.setLayoutParams(params);
                buttonshow12.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = qo.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    qo.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                qo.setLayoutParams(params);
                buttonshow12.setVisibility(View.VISIBLE);
            }
        });

        buttonshow13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = aqc.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    aqc.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                aqc.setLayoutParams(params);
                buttonshow13.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = aqc.getLayoutParams();
                params.height = marginInDp3;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    aqc.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                aqc.setLayoutParams(params);
                buttonshow13.setVisibility(View.VISIBLE);
            }
        });

        buttonshow14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = bongkarmuat.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bongkarmuat.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                bongkarmuat.setLayoutParams(params);
                buttonshow14.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = bongkarmuat.getLayoutParams();
                params.height = marginInDp2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bongkarmuat.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                bongkarmuat.setLayoutParams(params);
                buttonshow14.setVisibility(View.VISIBLE);
            }
        });

        buttonshow15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = problem.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    problem.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                problem.setLayoutParams(params);
                buttonshow15.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = problem.getLayoutParams();
                params.height = marginInDp3;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    problem.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                problem.setLayoutParams(params);
                buttonshow15.setVisibility(View.VISIBLE);
            }
        });

        buttonshow16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = evaluasi.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    evaluasi.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                evaluasi.setLayoutParams(params);
                buttonshow16.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = evaluasi.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    evaluasi.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                evaluasi.setLayoutParams(params);
                buttonshow16.setVisibility(View.VISIBLE);
            }
        });


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
        getAtasan();
        getBiodata();
    }

    private void getData() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

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
                                String status = movieObject.getString("status_user");
                                String remark = movieObject.getString("remark_user");

                                if(id_materi.equals("33")){
                                    if(status.equals("1")){
                                        setuju1.setVisibility(GONE);
                                        tidaksetuju1.setVisibility(GONE);
                                        selesai1.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("34")){
                                    if(status.equals("1")){
                                        setuju2.setVisibility(GONE);
                                        tidaksetuju2.setVisibility(GONE);
                                        selesai2.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("40")){
                                    if(status.equals("1")){
                                        setuju3.setVisibility(GONE);
                                        tidaksetuju3.setVisibility(GONE);
                                        selesai3.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("46")){
                                    if(status.equals("1")){
                                        setuju4.setVisibility(GONE);
                                        tidaksetuju4.setVisibility(GONE);
                                        selesai4.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("201")){
                                    if(status.equals("1")){
                                        setuju5.setVisibility(GONE);
                                        tidaksetuju5.setVisibility(GONE);
                                        selesai5.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("202")){
                                    if(status.equals("1")){
                                        setuju6.setVisibility(GONE);
                                        tidaksetuju6.setVisibility(GONE);
                                        selesai6.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("203")){
                                    if(status.equals("1")){
                                        setuju7.setVisibility(GONE);
                                        tidaksetuju7.setVisibility(GONE);
                                        selesai7.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("204")){
                                    if(status.equals("1")){
                                        setuju8.setVisibility(GONE);
                                        tidaksetuju8.setVisibility(GONE);
                                        selesai8.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("205")){
                                    if(status.equals("1")){
                                        setuju9.setVisibility(GONE);
                                        tidaksetuju9.setVisibility(GONE);
                                        selesai9.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("206")){
                                    if(status.equals("1")){
                                        setuju10.setVisibility(GONE);
                                        tidaksetuju10.setVisibility(GONE);
                                        selesai10.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("207")){
                                    if(status.equals("1")){
                                        setuju11.setVisibility(GONE);
                                        tidaksetuju11.setVisibility(GONE);
                                        selesai11.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("208")){
                                    if(status.equals("1")){
                                        setuju12.setVisibility(GONE);
                                        tidaksetuju12.setVisibility(GONE);
                                        selesai12.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("209")){
                                    if(status.equals("1")){
                                        setuju13.setVisibility(GONE);
                                        tidaksetuju13.setVisibility(GONE);
                                        selesai13.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("210")){
                                    if(status.equals("1")){
                                        setuju14.setVisibility(GONE);
                                        tidaksetuju14.setVisibility(GONE);
                                        selesai14.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("211")){
                                    if(status.equals("1")){
                                        setuju15.setVisibility(GONE);
                                        tidaksetuju15.setVisibility(GONE);
                                        selesai15.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("212")){
                                    if(status.equals("1")){
                                        setuju16.setVisibility(GONE);
                                        tidaksetuju16.setVisibility(GONE);
                                        selesai16.setVisibility(View.VISIBLE);
                                        onthejobtext.setLongClickable(false);
                                        onthejobtext.setFocusable(false);
                                        onthejobtext.setText(remark);
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
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
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
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
                            ojt_wop_user.this);
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
                                    Intent intent = new Intent(ojt_wop_user.this, MainActivity.class);
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