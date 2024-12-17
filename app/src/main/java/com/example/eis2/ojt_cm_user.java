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

public class ojt_cm_user extends AppCompatActivity {
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    EditText name, posisipekerjaan, jawab;
    ImageButton buttonshow1, buttonshow2, buttonshow3, buttonshow4, buttonshow5, buttonshow6, buttonshow7, buttonshow8, buttonshow9, buttonshow10;
    ImageButton tidaksetuju1, tidaksetuju2, tidaksetuju3, tidaksetuju4, tidaksetuju5, tidaksetuju6, tidaksetuju7, tidaksetuju8, tidaksetuju9, tidaksetuju10;
    LinearLayout profile, cmlinear, saleslinear, suplylinear, warehouselinear,
            ictlinear, hrgalinear, financelinear, financeoperationallinear, reviewlinear;
    ImageButton setuju1, setuju2, setuju3, setuju4, setuju5, setuju6, setuju7, setuju8, setuju9, setuju10;
    ImageButton selesai1, selesai2, selesai3, selesai4, selesai5, selesai6, selesai7, selesai8, selesai9, selesai10;

    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ojt_cm_user);
        HttpsTrustManager.allowAllSSL();
        setNavigationDrawer();

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

        profile = (LinearLayout) findViewById(R.id.profile);
        cmlinear = (LinearLayout) findViewById(R.id.cmlinear);
        saleslinear = (LinearLayout) findViewById(R.id.saleslinear);
        suplylinear = (LinearLayout) findViewById(R.id.suplylinear);
        warehouselinear = (LinearLayout) findViewById(R.id.warehouselinear);

        getData();
        ictlinear = (LinearLayout) findViewById(R.id.ictlinear);
        hrgalinear = (LinearLayout) findViewById(R.id.hrgalinear);
        financelinear = (LinearLayout) findViewById(R.id.financelinear);
        financeoperationallinear = (LinearLayout) findViewById(R.id.financeoperationallinear);
        reviewlinear = (LinearLayout) findViewById(R.id.reviewlinear);

        double sizeInDP = 42;
        final int marginInDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP, getResources()
                        .getDisplayMetrics());

        double sizeInDP2 = 48;
        final int marginInDp2 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP2, getResources()
                        .getDisplayMetrics());

        setuju1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_cm_user.this);
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

                        params.put("id_materi", "116");
                        params.put("id_user", "1");
                        params.put("kode_materi", "5");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", lokasi);

                        params.put("remark_user", "Sudah dijelaskan oleh pihak HRD");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_cm_user.this);
                requestQueue2.add(stringRequest2);
            }
        });

        setuju2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_cm_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 117; i <= 118; i++) {
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
                                    if(finalI == 118) {
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
                            params.put("kode_materi", "5");
                            params.put("nik", nik_baru);

                            params.put("jabatan", jabatan);
                            params.put("tanggal_pelaksana_peserta", formattedDate);
                            params.put("status_user", "1");
                            params.put("lokasi", lokasi);

                            params.put("remark_user", "Pengenalan Channel SOW, AHS, dan Retail");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_cm_user.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_cm_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 119; i <= 123; i++) {
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
                                    if(finalI == 123) {
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
                            params.put("kode_materi", "5");
                            params.put("nik", nik_baru);

                            params.put("jabatan", jabatan);
                            params.put("tanggal_pelaksana_peserta", formattedDate);
                            params.put("status_user", "1");
                            params.put("lokasi", lokasi);

                            params.put("remark_user", "Pengenalan Sales Development");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_cm_user.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_cm_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 124; i <= 129; i++) {
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
                                    if(finalI == 129) {
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
                            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                            String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                            String jabatan = text_jabatan.getText().toString();
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);
                            String lokasi = txt_lokasi.getText().toString();

                            params.put("id_materi", String.valueOf(finalI));
                            params.put("id_user", "1");
                            params.put("kode_materi", "5");
                            params.put("nik", nik_baru);

                            params.put("jabatan", jabatan);
                            params.put("tanggal_pelaksana_peserta", formattedDate);
                            params.put("status_user", "1");
                            params.put("lokasi", lokasi);

                            params.put("remark_user", "Pengenalan Supply Chain");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_cm_user.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_cm_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 130; i <= 135; i++) {
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
                                    if(finalI == 135) {
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
                            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                            String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                            String jabatan = text_jabatan.getText().toString();
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);
                            String lokasi = txt_lokasi.getText().toString();

                            params.put("id_materi", String.valueOf(finalI));
                            params.put("id_user", "1");
                            params.put("kode_materi", "5");
                            params.put("nik", nik_baru);

                            params.put("jabatan", jabatan);
                            params.put("tanggal_pelaksana_peserta", formattedDate);
                            params.put("status_user", "1");
                            params.put("lokasi", lokasi);

                            params.put("remark_user", "Pengenalan Warehouse Operational");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_cm_user.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_cm_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 136; i <= 141; i++) {
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
                                    if(finalI == 141) {
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
                            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                            String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                            String jabatan = text_jabatan.getText().toString();
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);
                            String lokasi = txt_lokasi.getText().toString();

                            params.put("id_materi", String.valueOf(finalI));
                            params.put("id_user", "1");
                            params.put("kode_materi", "5");
                            params.put("nik", nik_baru);

                            params.put("jabatan", jabatan);
                            params.put("tanggal_pelaksana_peserta", formattedDate);
                            params.put("status_user", "1");
                            params.put("lokasi", lokasi);

                            params.put("remark_user", "Pengenalan ICT dan Internal audit");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_cm_user.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_cm_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 142; i <= 147; i++) {
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
                                    if(finalI == 147) {
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
                            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                            String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                            String jabatan = text_jabatan.getText().toString();
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedDate = df.format(c);
                            String lokasi = txt_lokasi.getText().toString();

                            params.put("id_materi", String.valueOf(finalI));
                            params.put("id_user", "1");
                            params.put("kode_materi", "5");
                            params.put("nik", nik_baru);

                            params.put("jabatan", jabatan);
                            params.put("tanggal_pelaksana_peserta", formattedDate);
                            params.put("status_user", "1");
                            params.put("lokasi", lokasi);

                            params.put("remark_user", "Pengenalan HRGA");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_cm_user.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_cm_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 148; i <= 153; i++) {
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
                                    if(finalI == 153) {
                                        setuju8.setVisibility(GONE);
                                        tidaksetuju8.setVisibility(GONE);
                                        selesai8.setVisibility(View.VISIBLE);
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
                            params.put("kode_materi", "5");
                            params.put("nik", nik_baru);

                            params.put("jabatan", jabatan);
                            params.put("tanggal_pelaksana_peserta", formattedDate);
                            params.put("status_user", "1");
                            params.put("lokasi", lokasi);

                            params.put("remark_user", "Pengenalan F & D");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_cm_user.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_cm_user.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                for (int i = 148; i <= 153; i++) {
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
                                    if(finalI == 153) {
                                        setuju8.setVisibility(GONE);
                                        tidaksetuju8.setVisibility(GONE);
                                        selesai8.setVisibility(View.VISIBLE);
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
                            params.put("kode_materi", "5");
                            params.put("nik", nik_baru);

                            params.put("jabatan", jabatan);
                            params.put("tanggal_pelaksana_peserta", formattedDate);
                            params.put("status_user", "1");
                            params.put("lokasi", lokasi);

                            params.put("remark_user", "Pengenalan F & D");

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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_cm_user.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });

        setuju9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_cm_user.this);
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

                        params.put("id_materi", "154");
                        params.put("id_user", "1");
                        params.put("kode_materi", "5");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", lokasi);

                        params.put("remark_user", "Pengenalan Finance Operational");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_cm_user.this);
                requestQueue2.add(stringRequest2);
            }
        });


        setuju10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(ojt_cm_user.this);
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

                        params.put("id_materi", "155");
                        params.put("id_user", "1");
                        params.put("kode_materi", "5");
                        params.put("nik", nik_baru);

                        params.put("jabatan", jabatan);
                        params.put("tanggal_pelaksana_peserta", formattedDate);
                        params.put("status_user", "1");
                        params.put("lokasi", lokasi);

                        params.put("remark_user", "mempelajari cara review OJT & SWOT SND");

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(ojt_cm_user.this);
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
                params.height = marginInDp2;
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
                ViewGroup.LayoutParams params = cmlinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    cmlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                cmlinear.setLayoutParams(params);
                buttonshow2.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = cmlinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    cmlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                cmlinear.setLayoutParams(params);
                buttonshow2.setVisibility(View.VISIBLE);
            }
        });

        buttonshow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = saleslinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    saleslinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                saleslinear.setLayoutParams(params);
                buttonshow3.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = saleslinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    saleslinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                saleslinear.setLayoutParams(params);
                buttonshow3.setVisibility(View.VISIBLE);
            }
        });

        buttonshow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = suplylinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    suplylinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                suplylinear.setLayoutParams(params);
                buttonshow4.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = suplylinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    suplylinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                suplylinear.setLayoutParams(params);
                buttonshow4.setVisibility(View.VISIBLE);
            }
        });

        buttonshow5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = warehouselinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    warehouselinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                warehouselinear.setLayoutParams(params);
                buttonshow5.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = warehouselinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    warehouselinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                warehouselinear.setLayoutParams(params);
                buttonshow5.setVisibility(View.VISIBLE);
            }
        });

        buttonshow6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = ictlinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ictlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                ictlinear.setLayoutParams(params);
                buttonshow6.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = ictlinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ictlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                ictlinear.setLayoutParams(params);
                buttonshow6.setVisibility(View.VISIBLE);
            }
        });

        buttonshow7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = hrgalinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    hrgalinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                hrgalinear.setLayoutParams(params);
                buttonshow7.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = hrgalinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    hrgalinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                hrgalinear.setLayoutParams(params);
                buttonshow7.setVisibility(View.VISIBLE);
            }
        });

        buttonshow8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = financelinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    financelinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                financelinear.setLayoutParams(params);
                buttonshow8.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = financelinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    financelinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                financelinear.setLayoutParams(params);
                buttonshow8.setVisibility(View.VISIBLE);
            }
        });

        buttonshow9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = financeoperationallinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    financeoperationallinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                financeoperationallinear.setLayoutParams(params);
                buttonshow9.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = financeoperationallinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    financeoperationallinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                financeoperationallinear.setLayoutParams(params);
                buttonshow9.setVisibility(View.VISIBLE);
            }
        });

        buttonshow10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = reviewlinear.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    reviewlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                reviewlinear.setLayoutParams(params);
                buttonshow10.setVisibility(View.INVISIBLE);
            }
        });
        tidaksetuju10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = reviewlinear.getLayoutParams();
                params.height = marginInDp;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    reviewlinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                reviewlinear.setLayoutParams(params);
                buttonshow10.setVisibility(View.VISIBLE);
            }
        });

        name = (EditText) findViewById(R.id.name);
        posisipekerjaan = (EditText) findViewById(R.id.posisipekerjaan);
        jawab = (EditText) findViewById(R.id.jawab);
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

                                if(id_materi.equals("116")){
                                    if(status.equals("1")){
                                        setuju1.setVisibility(GONE);
                                        tidaksetuju1.setVisibility(GONE);
                                        selesai1.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("117")){
                                    if(status.equals("1")){
                                        setuju2.setVisibility(GONE);
                                        tidaksetuju2.setVisibility(GONE);
                                        selesai2.setVisibility(View.VISIBLE);

                                    }
                                }

                                if(id_materi.equals("119")){
                                    if(status.equals("1")){
                                        setuju3.setVisibility(GONE);
                                        tidaksetuju3.setVisibility(GONE);
                                        selesai3.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("124")){
                                    if(status.equals("1")){
                                        setuju4.setVisibility(GONE);
                                        tidaksetuju4.setVisibility(GONE);
                                        selesai4.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("130")){
                                    if(status.equals("1")){
                                        setuju5.setVisibility(GONE);
                                        tidaksetuju5.setVisibility(GONE);
                                        selesai5.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("136")){
                                    if(status.equals("1")){
                                        setuju6.setVisibility(GONE);
                                        tidaksetuju6.setVisibility(GONE);
                                        selesai6.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("142")){
                                    if(status.equals("1")){
                                        setuju7.setVisibility(GONE);
                                        tidaksetuju7.setVisibility(GONE);
                                        selesai7.setVisibility(View.VISIBLE);

                                    }
                                }

                                if(id_materi.equals("148")){
                                    if(status.equals("1")){
                                        setuju8.setVisibility(GONE);
                                        tidaksetuju8.setVisibility(GONE);
                                        selesai8.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("154")){
                                    if(status.equals("1")){
                                        setuju9.setVisibility(GONE);
                                        tidaksetuju9.setVisibility(GONE);
                                        selesai9.setVisibility(View.VISIBLE);
                                    }
                                }

                                if(id_materi.equals("155")){
                                    if(status.equals("1")){
                                        setuju10.setVisibility(GONE);
                                        tidaksetuju10.setVisibility(GONE);
                                        selesai10.setVisibility(View.VISIBLE);
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
                            ojt_cm_user.this);
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
                                    Intent intent = new Intent(ojt_cm_user.this, MainActivity.class);
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