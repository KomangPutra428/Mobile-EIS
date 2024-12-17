package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.example.eis2.Item.Trace;
import com.example.eis2.Item.listba;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;

public class serahterima extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;
    DrawerLayout dLayout;
    RecyclerView rvTrace;
    private List<Trace> traceList = new ArrayList<>(10);
    private TraceListAdapter adapter;
    Button selesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serahterima);
        HttpsTrustManager.allowAllSSL();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        rvTrace = (RecyclerView) findViewById(R.id.rvTrace);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });
        selesai = findViewById(R.id.selesai);

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(serahterima.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statusterima?nik_baru="+nik_baru,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    JSONArray movieArray = obj.getJSONArray("data");

                                    for (int i = 0; i < movieArray.length(); i++) {

                                        JSONObject movieObject = movieArray.getJSONObject(i);
                                        if(movieObject.getString("status_sdm").equals("1")){
                                            updatedata();
                                        } else {
                                            hideDialog();
                                            Toast.makeText(getApplicationContext(), "Maaf, harap isi semua form", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getApplicationContext(), "Maaf, data belum ada", Toast.LENGTH_SHORT).show();
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
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });

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
                            serahterima.this);
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
                                    Intent intent = new Intent(serahterima.this, MainActivity.class);
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

    private void updatedata() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statusserahterima",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                        serahterima.this.finish();
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
                params.put("nik_baru", nik_baru);

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
        RequestQueue requestQueue = Volley.newRequestQueue(serahterima.this);
        requestQueue.add(stringRequest);
    }

    private void getData() {
        traceList.add(new Trace("Serah Terima", "Waiting"));
        traceList.add(new Trace("Alat Kerja & Inventaris",  "Open"));
        traceList.add(new Trace("Dokumen Hardcopy", "Open"));
        traceList.add(new Trace("Dokumen Softcopy", "Open"));
        traceList.add(new Trace("Project", "Open"));
        traceList.add(new Trace("Sumber Daya Manusia", "Open"));

        adapter = new serahterima.TraceListAdapter(serahterima.this, traceList);
        rvTrace.setLayoutManager(new LinearLayoutManager(serahterima.this));
        rvTrace.setAdapter(adapter);
    }


    public class TraceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;
        private List<Trace> traceList = new ArrayList<>(1);
        private static final int TYPE_TOP = 0x0000;
        private static final int TYPE_NORMAL= 0x0001;

        public TraceListAdapter(Context context, List<Trace> traceList) {
            inflater = LayoutInflater.from(context);
            this.traceList = traceList;
        }

        @Override
        public serahterima.TraceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(inflater.inflate(R.layout.list_item_kuisioner, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final serahterima.TraceListAdapter.ViewHolder itemHolder = (serahterima.TraceListAdapter.ViewHolder) holder;

            itemHolder.bindHolder(traceList.get(position));
            if (position == 0) {
                itemHolder.tvTopLine.setVisibility(View.INVISIBLE);
                itemHolder.tvDot.setBackgroundResource(R.drawable.timeline_progress);
                itemHolder.statustext.setVisibility(View.VISIBLE);
            }

            itemHolder.setIsRecyclable(false);


            if (position == 5) {
                itemHolder.line.setVisibility(View.INVISIBLE);
            }

            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            final String nik_baru = sharedPreferences.getString(KEY_NIK, null);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statusterima?nik_baru=" + nik_baru,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");

                                for (int i = 0; i < movieArray.length(); i++) {
                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                        if(itemHolder.keterangan.getText().toString().equals("Serah Terima")){
                                            itemHolder.status.setText("Closed");
                                            itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                        } else if(itemHolder.keterangan.getText().toString().equals("Alat Kerja & Inventaris")){
                                            itemHolder.status.setText("Waiting");
                                            itemHolder.tvDot.setBackgroundResource(R.drawable.timeline_progress);
                                        }

                                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statusterima?nik_baru=" + nik_baru,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    try {
                                                        JSONObject obj = new JSONObject(response);
                                                        JSONArray movieArray = obj.getJSONArray("data");

                                                        for (int i = 0; i < movieArray.length(); i++) {
                                                            JSONObject movieObject = movieArray.getJSONObject(i);
                                                            if(movieObject.getString("status_alat_kerja").equals("1")){
                                                                if(itemHolder.keterangan.getText().toString().equals("Alat Kerja & Inventaris")){
                                                                    itemHolder.status.setText("Closed");
                                                                    itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                                                }

                                                                if(itemHolder.keterangan.getText().toString().equals("Dokumen Hardcopy")){
                                                                    itemHolder.status.setText("Waiting");
                                                                    itemHolder.tvDot.setBackgroundResource(R.drawable.timeline_progress);
                                                                }
                                                            }
                                                        }
                                                        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statusterima?nik_baru=" + nik_baru,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {

                                                                        try {
                                                                            JSONObject obj = new JSONObject(response);
                                                                            JSONArray movieArray = obj.getJSONArray("data");

                                                                            for (int i = 0; i < movieArray.length(); i++) {
                                                                                JSONObject movieObject = movieArray.getJSONObject(i);
                                                                                if(movieObject.getString("status_hardcopy").equals("1")){
                                                                                    if(itemHolder.keterangan.getText().toString().equals("Dokumen Hardcopy")){
                                                                                        itemHolder.status.setText("Closed");
                                                                                        itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                                                                    }

                                                                                    if(itemHolder.keterangan.getText().toString().equals("Dokumen Softcopy")){
                                                                                        itemHolder.status.setText("Waiting");
                                                                                        itemHolder.tvDot.setBackgroundResource(R.drawable.timeline_progress);
                                                                                    }
                                                                                }

                                                                                StringRequest stringRequest4 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statusterima?nik_baru=" + nik_baru,
                                                                                        new Response.Listener<String>() {
                                                                                            @Override
                                                                                            public void onResponse(String response) {

                                                                                                try {
                                                                                                    JSONObject obj = new JSONObject(response);
                                                                                                    JSONArray movieArray = obj.getJSONArray("data");

                                                                                                    for (int i = 0; i < movieArray.length(); i++) {
                                                                                                        JSONObject movieObject = movieArray.getJSONObject(i);
                                                                                                        if(movieObject.getString("status_softcopy").equals("1")){
                                                                                                            if(itemHolder.keterangan.getText().toString().equals("Dokumen Softcopy")){
                                                                                                                itemHolder.status.setText("Closed");
                                                                                                                itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                                                                                            }

                                                                                                            if(itemHolder.keterangan.getText().toString().equals("Project")){
                                                                                                                itemHolder.status.setText("Waiting");
                                                                                                                itemHolder.tvDot.setBackgroundResource(R.drawable.timeline_progress);
                                                                                                            }
                                                                                                        }

                                                                                                    }
                                                                                                    StringRequest stringRequest5 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statusterima?nik_baru=" + nik_baru,
                                                                                                            new Response.Listener<String>() {
                                                                                                                @Override
                                                                                                                public void onResponse(String response) {

                                                                                                                    try {
                                                                                                                        JSONObject obj = new JSONObject(response);
                                                                                                                        JSONArray movieArray = obj.getJSONArray("data");

                                                                                                                        for (int i = 0; i < movieArray.length(); i++) {
                                                                                                                            JSONObject movieObject = movieArray.getJSONObject(i);
                                                                                                                            if(movieObject.getString("status_project").equals("1")){
                                                                                                                                if(itemHolder.keterangan.getText().toString().equals("Project")){
                                                                                                                                    itemHolder.status.setText("Closed");
                                                                                                                                    itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                                                                                                                }

                                                                                                                                if(itemHolder.keterangan.getText().toString().equals("Sumber Daya Manusia")){
                                                                                                                                    itemHolder.status.setText("Waiting");
                                                                                                                                    itemHolder.tvDot.setBackgroundResource(R.drawable.timeline_progress);
                                                                                                                                }
                                                                                                                            }

                                                                                                                        }
                                                                                                                        StringRequest stringRequest6 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statusterima?nik_baru=" + nik_baru,
                                                                                                                                new Response.Listener<String>() {
                                                                                                                                    @Override
                                                                                                                                    public void onResponse(String response) {

                                                                                                                                        try {
                                                                                                                                            JSONObject obj = new JSONObject(response);
                                                                                                                                            JSONArray movieArray = obj.getJSONArray("data");

                                                                                                                                            for (int i = 0; i < movieArray.length(); i++) {
                                                                                                                                                JSONObject movieObject = movieArray.getJSONObject(i);
                                                                                                                                                if(movieObject.getString("status_sdm").equals("1")){
                                                                                                                                                    if(itemHolder.keterangan.getText().toString().equals("Sumber Daya Manusia")){
                                                                                                                                                        itemHolder.status.setText("Closed");
                                                                                                                                                        itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
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
                                                                                                                        stringRequest6.setRetryPolicy(
                                                                                                                                new DefaultRetryPolicy(
                                                                                                                                        500000,
                                                                                                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                                                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                                                                                                )
                                                                                                                        );
                                                                                                                        RequestQueue requestQueue6 = Volley.newRequestQueue(serahterima.this);
                                                                                                                        requestQueue6.add(stringRequest6);


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
                                                                                                    stringRequest5.setRetryPolicy(
                                                                                                            new DefaultRetryPolicy(
                                                                                                                    500000,
                                                                                                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                                                                            )
                                                                                                    );
                                                                                                    RequestQueue requestQueue5 = Volley.newRequestQueue(serahterima.this);
                                                                                                    requestQueue5.add(stringRequest5);

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
                                                                                stringRequest4.setRetryPolicy(
                                                                                        new DefaultRetryPolicy(
                                                                                                500000,
                                                                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                                                        )
                                                                                );
                                                                                RequestQueue requestQueue4 = Volley.newRequestQueue(serahterima.this);
                                                                                requestQueue4.add(stringRequest4);

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
                                                        stringRequest3.setRetryPolicy(
                                                                new DefaultRetryPolicy(
                                                                        500000,
                                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                                )
                                                        );
                                                        RequestQueue requestQueue3 = Volley.newRequestQueue(serahterima.this);
                                                        requestQueue3.add(stringRequest3);

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
                                    stringRequest2.setRetryPolicy(
                                            new DefaultRetryPolicy(
                                                    500000,
                                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                            )
                                    );
                                    RequestQueue requestQueue2 = Volley.newRequestQueue(serahterima.this);
                                    requestQueue2.add(stringRequest2);

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
            RequestQueue requestQueue = Volley.newRequestQueue(serahterima.this);
            requestQueue.add(stringRequest);




        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }


        @Override
        public int getItemCount() {
            return traceList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_TOP;
            }
            return TYPE_NORMAL;
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            private TextView keterangan, status, statustext, line;
            private TextView tvTopLine, tvDot;
            public ViewHolder(View itemView) {
                super(itemView);
                keterangan = (TextView) itemView.findViewById(R.id.keterangan);
                statustext = (TextView) itemView.findViewById(R.id.statustext);
                status = (TextView) itemView.findViewById(R.id.status);
                tvTopLine = (TextView) itemView.findViewById(R.id.tvTopLine);
                tvDot = (TextView) itemView.findViewById(R.id.tvDot);
                line = (TextView) itemView.findViewById(R.id.line);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String textGet = keterangan.getText().toString();
                        String statusGet = status.getText().toString();

                        if(textGet.equals("Serah Terima")){
                            if(statusGet.equals("Closed")){
                                new SweetAlertDialog(serahterima.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Serah Terima")
                                        .setContentText("Serah Terima Sudah Di Ajukan")
                                        .setConfirmText("OK")
                                        .show();
                            } else if(statusGet.equals("Open")){

                            } else {
                                    Intent i = new Intent(serahterima.this, formserahterima.class);
                                    startActivity(i);
                            }
                        }

                        if(textGet.equals("Alat Kerja & Inventaris")){
                            if(statusGet.equals("Closed")){
                                new SweetAlertDialog(serahterima.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Alat Kerja & Inventaris")
                                        .setContentText("Alat Kerja & Inventaris Sudah Di Ajukan")
                                        .setConfirmText("OK")
                                        .show();
                            } else if(statusGet.equals("Open")){

                            } else {
                                new SweetAlertDialog(serahterima.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Anda ingin melewati form "+textGet+" ?")
                                        .setContentText("Klik iya untuk mengisi form " + textGet)
                                        .setCancelText("Tidak")
                                        .setConfirmText("Iya")
                                        .showCancelButton(true)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(final SweetAlertDialog sDialog) {
                                                pDialog = new ProgressDialog(serahterima.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                sDialog.cancel();

                                                StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statusalatkerja",
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                hideDialog();
                                                                traceList.clear();
                                                                getData();
                                                                Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
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
                                                        params.put("nik", nik_baru);


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

                                                RequestQueue requestQueue = Volley.newRequestQueue(serahterima.this);
                                                requestQueue.add(stringRequest);
                                            }
                                        })

                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                Intent i = new Intent(serahterima.this, listalatkerja.class);
                                                startActivity(i);
                                            }
                                        })
                                        .show();

                            }
                        }

                        if(textGet.equals("Dokumen Hardcopy")){
                            if(statusGet.equals("Closed")){
                                new SweetAlertDialog(serahterima.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Dokumen Hardcopy")
                                        .setContentText("Dokumen Hardcopy Sudah Di Ajukan")
                                        .setConfirmText("OK")
                                        .show();
                            } else if(statusGet.equals("Open")){

                            } else {
                                new SweetAlertDialog(serahterima.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Anda ingin melewati form "+textGet+" ?")
                                        .setContentText("Klik iya untuk mengisi form " + textGet)
                                        .setCancelText("Tidak")
                                        .setConfirmText("Iya")
                                        .showCancelButton(true)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(final SweetAlertDialog sDialog) {
                                                pDialog = new ProgressDialog(serahterima.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                sDialog.cancel();

                                                StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statushardcopy",
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                hideDialog();
                                                                traceList.clear();
                                                                getData();
                                                                Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
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
                                                        params.put("nik", nik_baru);


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

                                                RequestQueue requestQueue = Volley.newRequestQueue(serahterima.this);
                                                requestQueue.add(stringRequest);
                                            }
                                        })

                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                Intent i = new Intent(serahterima.this, listdokumenhardcopy.class);
                                                startActivity(i);
                                            }
                                        })
                                        .show();

                            }
                        }

                        if(textGet.equals("Dokumen Softcopy")){
                            if(statusGet.equals("Closed")){
                                new SweetAlertDialog(serahterima.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Dokumen Softcopy")
                                        .setContentText("Dokumen Softcopy Sudah Di Ajukan")
                                        .setConfirmText("OK")
                                        .show();
                            } else if(statusGet.equals("Open")){

                            } else {
                                new SweetAlertDialog(serahterima.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Anda ingin melewati form "+textGet+" ?")
                                        .setContentText("Klik iya untuk mengisi form " + textGet)
                                        .setCancelText("Tidak")
                                        .setConfirmText("Iya")
                                        .showCancelButton(true)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(final SweetAlertDialog sDialog) {
                                                pDialog = new ProgressDialog(serahterima.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                sDialog.cancel();

                                                StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statussoftcopy",
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                hideDialog();
                                                                traceList.clear();
                                                                getData();
                                                                Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
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
                                                        params.put("nik", nik_baru);


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

                                                RequestQueue requestQueue = Volley.newRequestQueue(serahterima.this);
                                                requestQueue.add(stringRequest);
                                            }
                                        })

                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                Intent i = new Intent(serahterima.this, listdokumensoftcopy.class);
                                                startActivity(i);
                                            }
                                        })
                                        .show();

                            }
                        }

                        if(textGet.equals("Project")){
                            if(statusGet.equals("Closed")){
                                new SweetAlertDialog(serahterima.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Project")
                                        .setContentText("Project Sudah Di Ajukan")
                                        .setConfirmText("OK")
                                        .show();
                            } else if(statusGet.equals("Open")){

                            } else {
                                new SweetAlertDialog(serahterima.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Anda ingin melewati form "+textGet+" ?")
                                        .setContentText("Klik iya untuk mengisi form " + textGet)
                                        .setCancelText("Tidak")
                                        .setConfirmText("Iya")
                                        .showCancelButton(true)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(final SweetAlertDialog sDialog) {
                                                pDialog = new ProgressDialog(serahterima.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                sDialog.cancel();

                                                StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statusproject",
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                hideDialog();
                                                                Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                                                                traceList.clear();
                                                                getData();
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
                                                        params.put("nik", nik_baru);


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

                                                RequestQueue requestQueue = Volley.newRequestQueue(serahterima.this);
                                                requestQueue.add(stringRequest);
                                            }
                                        })

                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                Intent i = new Intent(serahterima.this, listdokumenproject.class);
                                                startActivity(i);
                                            }
                                        })
                                        .show();

                            }
                        }

                        if(textGet.equals("Sumber Daya Manusia")){
                            if(statusGet.equals("Closed")){
                                new SweetAlertDialog(serahterima.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Sumber Daya Manusia")
                                        .setContentText("Sumber Daya Manusia Sudah Di Ajukan")
                                        .setConfirmText("OK")
                                        .show();
                            } else if(statusGet.equals("Open")){

                            } else {
                                new SweetAlertDialog(serahterima.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Anda ingin melewati form "+textGet+" ?")
                                        .setContentText("Klik iya untuk mengisi form " + textGet)
                                        .setCancelText("Tidak")
                                        .setConfirmText("Iya")
                                        .showCancelButton(true)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(final SweetAlertDialog sDialog) {
                                                pDialog = new ProgressDialog(serahterima.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                sDialog.cancel();

                                                StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statussdm",
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                hideDialog();
                                                                Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                                                                traceList.clear();
                                                                getData();
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
                                                        params.put("nik", nik_baru);


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

                                                RequestQueue requestQueue = Volley.newRequestQueue(serahterima.this);
                                                requestQueue.add(stringRequest);
                                            }
                                        })

                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                Intent i = new Intent(serahterima.this, listsdm.class);
                                                startActivity(i);
                                            }
                                        })
                                        .show();

                            }
                        }

                    }
                });

            }

            public void bindHolder(Trace trace) {
                keterangan.setText(trace.getAcceptTime());
                status.setText(trace.getAcceptStation());

            }
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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

    public static String tanggal(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "Tidak Ada Tanggal";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return convetDateFormat.format(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        traceList.clear();
        getData();
    }

    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

        
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}