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
import android.media.Image;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.eis2.Item.mutasiapprovalmodel;
import com.example.eis2.Item.mutasilistmodel;
import com.example.eis2.Item.namanikmodel;
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
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;

public class form_mutasi extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    private List<mutasiapprovalmodel> movieItemList;
    TextView no;
    EditText pengajuan, process, nik, name, pt, dpt, lokasi, awal, akhir;

    ImageButton approval;
    RadioButton diterima, tidakterima;
    RadioGroup option;
    ProgressDialog pDialog;
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_mutasi);
        HttpsTrustManager.allowAllSSL();
        movieItemList = new ArrayList<>();
        pengajuan = (EditText) findViewById(R.id.pengajuan);
        process = (EditText) findViewById(R.id.process);
        nik = (EditText) findViewById(R.id.nik);
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
        approval = (ImageButton) findViewById(R.id.approve);

        name = (EditText) findViewById(R.id.name);
        pt = (EditText) findViewById(R.id.pt);
        dpt = (EditText) findViewById(R.id.dpt);

        lokasi = (EditText) findViewById(R.id.lokasi);
        awal = (EditText) findViewById(R.id.awal);
        akhir = (EditText) findViewById(R.id.akhir);

        no = (TextView) findViewById(R.id.no);

        option = (RadioGroup) findViewById(R.id.option);
        option.setOnCheckedChangeListener(this);
        diterima = (RadioButton) findViewById(R.id.diterima);
        tidakterima = (RadioButton) findViewById(R.id.tidakterima);

        approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approve();
            }
        });

        getidentity();
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
                            form_mutasi.this);
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
                                    Intent intent = new Intent(form_mutasi.this, MainActivity.class);
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

    private void approve() {
        pDialog = new ProgressDialog(form_mutasi.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                        form_mutasi.this.finish();
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
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                String idmutasi = pengajuan.getText().toString();
                String status = no.getText().toString();

                params.put("id_mutasi_rotasi", idmutasi);
                params.put("status_atasan", status);


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

    private void getidentity() {
        final String idpengajuan = getIntent().getStringExtra(KEY_NIK);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi/index?id_mutasi_rotasi=" + idpengajuan,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");

                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);


                                    pengajuan.setText(movieObject.getString("id_mutasi_rotasi"));
                                    process.setText(movieObject.getString("permintaan"));
                                    nik.setText(movieObject.getString("nik_baru"));
                                    name.setText(movieObject.getString("nama_karyawan_mutasi"));

                                    pt.setText(movieObject.getString("pt_awal"));
                                    dpt.setText(movieObject.getString("dept_awal"));
                                    lokasi.setText(movieObject.getString("lokasi_awal"));
                                    awal.setText(movieObject.getString("jabatan_awal"));
                                    akhir.setText(movieObject.getString("jabatan_baru"));
                                    
                                    jabatanawal();
                                    jabatanakhir();




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
                    )
            );
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    private void jabatanakhir() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/jabatan/index?no_jabatan_karyawan=" + akhir.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                akhir.setText(movieObject.getString("jabatan_karyawan"));

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
        RequestQueue requestQueue = Volley.newRequestQueue(form_mutasi.this);
        requestQueue.add(stringRequest);
    }

    private void jabatanawal() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/jabatan/index?no_jabatan_karyawan=" + awal.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                awal.setText(movieObject.getString("jabatan_karyawan"));

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
        RequestQueue requestQueue = Volley.newRequestQueue(form_mutasi.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int i) {
        if (i == R.id.diterima) {
            no.setText("1");
        }
        if (i == R.id.tidakterima) {
            no.setText("2");

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