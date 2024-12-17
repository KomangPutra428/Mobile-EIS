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

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Sampler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.Trace;
import com.example.eis2.Item.Utility;
import com.example.eis2.Item.cutikhususmodel;
import com.example.eis2.Item.kuisionermodel;
import com.example.eis2.Item.menuresignmodel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;

public class menu_kuisioner extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    TextView nama, nik, jabatan, depodept, tanggalmasuk, tanggalresign, textkritik, status, tvDot;
    List<kuisionermodel> kuisionermodels;
    RecyclerView kuisionerlist;
    ProgressDialog pDialog;
    ListViewAdapterKuisioner adapter;
    LinearLayout kritikan;
    DrawerLayout dLayout;
    Button selesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_kuisioner);
        tvDot = findViewById(R.id.tvDot);
        nama = findViewById(R.id.nama);
        nik = findViewById(R.id.nik);
        status = findViewById(R.id.status);
        depodept = findViewById(R.id.depodept);
        jabatan = findViewById(R.id.jabatan);
        tanggalmasuk = findViewById(R.id.tanggalmasuk);
        tanggalresign = findViewById(R.id.tanggalresign);
        kuisionerlist = findViewById(R.id.kuisionerlist);
        kritikan = findViewById(R.id.kritikan);
        textkritik = findViewById(R.id.textkritik);
        selesai = findViewById(R.id.selesai);

//        kritikan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(status.getText().toString().equals("Closed")){
//                    new SweetAlertDialog(menu_kuisioner.this, SweetAlertDialog.SUCCESS_TYPE)
//                            .setTitleText("Kritik dan Saran")
//                            .setContentText("Anda sudah melakukan Kuisioner Kritik dan Saran")
//                            .show();
//                } else {
//                    Intent intent = new Intent(menu_kuisioner.this, list_soal.class);
//                    intent.putExtra("title", textkritik.getText().toString());
//                    startActivity(intent);
//                }
//
//
//            }
//        });



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
                            menu_kuisioner.this);
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
                                    Intent intent = new Intent(menu_kuisioner.this, MainActivity.class);
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

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatekuisioner();
            }
        });

        getBiodata();

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_nik?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);

                                nik.setText(movieObject.getString("nik_baru"));
                                tanggalresign.setText(convertFormat(movieObject.getString("tanggal_efektif_resign")));

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
                        7200000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void kuisioner() {
        kuisionermodels.clear();
        pDialog = new ProgressDialog(menu_kuisioner.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_kuisioner" ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                kuisionermodel movieItem = new kuisionermodel(
                                        movieObject.getString("id"),
                                        movieObject.getString("type"));

                                kuisionermodels.add(movieItem);
                                hideDialog();

                                adapter = new ListViewAdapterKuisioner(menu_kuisioner.this, (ArrayList<kuisionermodel>) kuisionermodels);
                                kuisionerlist.setLayoutManager(new LinearLayoutManager(menu_kuisioner.this));
                                kuisionerlist.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }

                            kuisionermodels.add(new kuisionermodel("", "KRITIK DAN SARAN"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Soal Belum Tersedia", Toast.LENGTH_SHORT).show();
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

    private void updatekuisioner() {
        pDialog = new ProgressDialog(menu_kuisioner.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statuskuisioner",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                        menu_kuisioner.this.finish();
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

                params.put("nik_baru", nik.getText().toString());

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
        RequestQueue requestQueue = Volley.newRequestQueue(menu_kuisioner.this);
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

                                nama.setText(movieObject.getString("nama_karyawan_struktur"));
                                depodept.setText(movieObject.getString("lokasi_struktur") +"/" + movieObject.getString("dept_struktur"));
                                jabatan.setText(movieObject.getString("jabatan_karyawan"));
                                tanggalmasuk.setText(convertFormat(movieObject.getString("join_date_struktur")));



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
                        7200000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public class ListViewAdapterKuisioner extends RecyclerView.Adapter<ListViewAdapterKuisioner.ViewProcessHolder> {

        Context context;
        private ArrayList<kuisionermodel> mItems; //memanggil modelData
        int counting = 0;

        public ListViewAdapterKuisioner(Context context, ArrayList<kuisionermodel> item) {
            this.context = context;
            this.mItems = item;
        }

        @Override
        public ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_kuisioner, parent, false); //memanggil layout list recyclerview

            ViewProcessHolder processHolder = new ViewProcessHolder(view);
            return processHolder;
        }

        @Override
        public void onBindViewHolder(final ViewProcessHolder holder, @SuppressLint("RecyclerView") final int position) {


            final kuisionermodel data = mItems.get(position);
            holder.id.setText(data.getId());
            holder.keterangan.setText(data.getType());

            if (position == 0) {
                holder.tvTopLine.setVisibility(View.INVISIBLE);
                holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_normal);
                holder.statustext.setVisibility(View.VISIBLE);
                holder.statustext.setText("Item Kuisioner");
            }

            if (position == mItems.size() - 1) {
                holder.line.setVisibility(View.INVISIBLE);
            }

            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            String nik_baru = sharedPreferences.getString(KEY_NIK, null);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_jawabankuisioner?nik_baru="+nik_baru+"&type_soal=" + data.getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("true")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject movieObject = jsonArray.getJSONObject(i);
                                holder.status.setText("Closed");
                                holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);


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
                            holder.status.setText("Open");
                            holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_normal);

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
            RequestQueue requestQueue = Volley.newRequestQueue(menu_kuisioner.this);
            requestQueue.add(stringRequest);

            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_kuisionerfinal?nik_baru="+nik_baru, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("true")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (holder.keterangan.getText().toString().equals("KRITIK DAN SARAN")) {
                                    holder.status.setText("Closed");
                                    holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
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
                            if (holder.keterangan.getText().toString().equals("KRITIK DAN SARAN")) {
                                status.setText("Open");
                            }

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
                            7200000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            RequestQueue requestQueue2 = Volley.newRequestQueue(menu_kuisioner.this);
            requestQueue2.add(stringRequest2);

        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewProcessHolder extends RecyclerView.ViewHolder {

            TextView id, keterangan, status, tvDot, tvTopLine, statustext, line;
            Context context;

            View itemView;


            public ViewProcessHolder(View itemView) {
                super(itemView);

                this.itemView = itemView;

                context = itemView.getContext();
                tvTopLine = (TextView) itemView.findViewById(R.id.tvTopLine);
                statustext = (TextView) itemView.findViewById(R.id.statustext);

                id = (TextView) itemView.findViewById(R.id.id);
                line = (TextView) itemView.findViewById(R.id.line);
                keterangan = (TextView) itemView.findViewById(R.id.keterangan);
                status = (TextView) itemView.findViewById(R.id.status);
                tvDot = (TextView) itemView.findViewById(R.id.tvDot);



                status.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if (keterangan.getText().toString().equals("KRITIK DAN SARAN")) {
                            if (status.getText().toString().equals("Closed")) {
                                selesai.setVisibility(View.VISIBLE);
                            } else if (status.getText().toString().equals("Open")) {
                                selesai.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(status.getText().toString().equals("Closed")){
                            selesai.setVisibility(View.VISIBLE);
                        } else if(status.getText().toString().equals("Open")){
                            selesai.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(status.getText().toString().equals("Closed")){
                            selesai.setVisibility(View.VISIBLE);
                        } else if(status.getText().toString().equals("Open")) {
                            selesai.setVisibility(View.GONE);
                        }
                    }
                });


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String idsoal = id.getText().toString();
                        String keterangansoal = keterangan.getText().toString();
                        String statussoal = status.getText().toString();
                        if(statussoal.equals("Closed")){
                            Intent intent = new Intent(view.getContext(), list_soal.class);
                            System.out.println("nomor id = " + idsoal);
                            intent.putExtra("id", idsoal);
                            intent.putExtra("title", keterangansoal);
//                            new SweetAlertDialog(menu_kuisioner.this, SweetAlertDialog.SUCCESS_TYPE)
//                                    .setTitleText(keterangansoal)
//                                    .setContentText("Anda sudah melakukan Kuisioner " + keterangansoal)
//                                    .show();

                        } else {
                            Intent intent = new Intent(view.getContext(), list_soal.class);
                            System.out.println("nomor id = " + idsoal);
                            intent.putExtra("id", idsoal);
                            intent.putExtra("title", keterangansoal);
                            context.startActivity(intent);
                        }

                    }
                });
            }

            public void setOnClickListener(View.OnClickListener onClickListener) {
                itemView.setOnClickListener(onClickListener);
            }
        }
    }

    public static String convertFormat(String inputDate) {
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return convetDateFormat.format(date);
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        kuisionermodels = new ArrayList<>();
        kuisioner();
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