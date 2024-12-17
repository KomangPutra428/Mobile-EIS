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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.eis2.Item.menuresignmodel;
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

public class menu_exitinterview extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;
    DrawerLayout dLayout;
    TextView namalengkap, jabataninterview, depodeptinterview;
    TextView nama;
    static TextView nik;
    TextView jabatan;
    TextView depodept;
    TextView tanggalmasuk;
    TextView tanggalresign;
    RecyclerView listtodo;
    private List<menuresignmodel> kuisionermodels = new ArrayList<>(10);
    ListViewAdapterResign adapterResign;
    Button selesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_exitinterview);
        HttpsTrustManager.allowAllSSL();
        namalengkap = findViewById(R.id.namalengkap);
        jabataninterview = findViewById(R.id.jabataninterview);
        depodeptinterview = findViewById(R.id.depodeptinterview);
        listtodo = findViewById(R.id.listtodo);
        selesai = findViewById(R.id.selesai);

        nama = findViewById(R.id.nama);
        nik = findViewById(R.id.nik);
        jabatan = findViewById(R.id.jabatan);

        depodept = findViewById(R.id.depodept);
        tanggalmasuk = findViewById(R.id.tanggalmasuk);
        tanggalresign = findViewById(R.id.tanggalresign);

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

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(menu_exitinterview.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statusexit",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                menu_exitinterview.this.finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "Maaf ada kesalahan", Toast.LENGTH_LONG).show();
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

                        params.put("nik_baru", nik.getText().toString());

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
                RequestQueue requestQueue2 = Volley.newRequestQueue(menu_exitinterview.this);
                requestQueue2.add(stringRequest2);
            }
        });

        getInterview();
        getBiodata();

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
                            menu_exitinterview.this);
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
                                    Intent intent = new Intent(menu_exitinterview.this, MainActivity.class);
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

    private void isidata() {
        kuisionermodels.add(new menuresignmodel("Soal Exit Interview", "Waiting"));
        kuisionermodels.add(new menuresignmodel("Pesan & Kesan", "Open"));
        adapterResign = new ListViewAdapterResign(menu_exitinterview.this, kuisionermodels);
        listtodo.setLayoutManager(new LinearLayoutManager(menu_exitinterview.this));
        listtodo.setAdapter(adapterResign);
    }

    private void getInterview() {
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

                                namalengkap.setText(movieObject.getString("nama_karyawan_struktur"));
                                jabataninterview.setText(movieObject.getString("jabatan_karyawan"));
                                depodeptinterview.setText(movieObject.getString("lokasi_struktur") + " / " + movieObject.getString("dept_struktur"));

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

                                nama.setText(movieObject.getString("nama_karyawan_struktur"));
                                depodept.setText(movieObject.getString("lokasi_struktur") + "/" + movieObject.getString("dept_struktur"));
                                jabatan.setText(movieObject.getString("jabatan_karyawan"));
                                nik.setText(movieObject.getString("nik_baru"));
                                tanggalmasuk.setText(convertFormat(movieObject.getString("join_date_struktur")));
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_nik?nik_baru=" + nik.getText().toString(),
                                        new Response.Listener<String>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject obj = new JSONObject(response);
                                                    JSONArray movieArray = obj.getJSONArray("data");
                                                    for (int i = 0; i < movieArray.length(); i++) {
                                                        JSONObject movieObject = movieArray.getJSONObject(i);


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
                                                7200000,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                        )
                                );
                                RequestQueue requestQueue = Volley.newRequestQueue(menu_exitinterview.this);
                                requestQueue.add(stringRequest);


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
                        7200000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

        public class ListViewAdapterResign extends RecyclerView.Adapter<ListViewAdapterResign.ViewProcessHolder> {

            Context context;
            private ArrayList<menuresignmodel> mItems; //memanggil modelData

            public ListViewAdapterResign(Context context, List<menuresignmodel> item) {
                this.context = context;
                this.mItems = (ArrayList<menuresignmodel>) item;
            }

            @Override
            public ListViewAdapterResign.ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_kuisioner, parent, false); //memanggil layout list recyclerview

                ListViewAdapterResign.ViewProcessHolder processHolder = new ListViewAdapterResign.ViewProcessHolder(view);
                return processHolder;
            }

            @Override
            public void onBindViewHolder(final ListViewAdapterResign.ViewProcessHolder holder, @SuppressLint("RecyclerView") final int position) {


                final menuresignmodel data = mItems.get(position);
                holder.keterangan.setText(data.getKeterangan());
                holder.status.setText(data.getStatus());

                if (position == 0) {
                    holder.tvTopLine.setVisibility(View.INVISIBLE);
                    holder.tvDot.setBackgroundResource(R.drawable.timeline_progress);
                }

                if (position == mItems.size() - 1) {
                    holder.line.setVisibility(View.INVISIBLE);
                }

                holder.setIsRecyclable(false);

                final String nik_baru = getIntent().getStringExtra(KEY_NIK);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_cekexit?nik_baru="+nik_baru, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if(holder.keterangan.getText().toString().equals("Soal Exit Interview")) {
                                        holder.status.setText("Closed");
                                        holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                    }

                                    if (holder.keterangan.getText().toString().equals("Pesan & Kesan")) {
                                        holder.status.setText("Waiting");
                                        holder.tvDot.setBackgroundResource(R.drawable.timeline_progress);
                                    }

                                    String nik_baru_2 = getIntent().getStringExtra(KEY_NIK);
                                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_exitsarandanmasukan?nik_baru="+nik_baru_2, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (jsonObject.getString("status").equals("true")) {
                                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        if (holder.keterangan.getText().toString().equals("Pesan & Kesan")) {
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
                                    RequestQueue requestQueue2 = Volley.newRequestQueue(menu_exitinterview.this);
                                    requestQueue2.add(stringRequest2);

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
                                if(holder.keterangan.getText().toString().equals("Soal Exit Interview")) {
                                    holder.status.setText("Waiting");
                                    holder.tvDot.setBackgroundResource(R.drawable.timeline_progress);
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
                stringRequest.setRetryPolicy(
                        new DefaultRetryPolicy(
                                7200000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        )
                );
                RequestQueue requestQueue = Volley.newRequestQueue(menu_exitinterview.this);
                requestQueue.add(stringRequest);



            }


            @Override
            public long getItemId(int position) {
                return super.getItemId(position);
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @Override
            public int getItemCount() {
                return mItems.size();
            }


            public class ViewProcessHolder extends RecyclerView.ViewHolder {

                TextView id, keterangan, status, tvDot, tvTopLine, line;
                Context context;

                View itemView;


                public ViewProcessHolder(View itemView) {
                    super(itemView);

                    this.itemView = itemView;

                    context = itemView.getContext();

                    id = (TextView) itemView.findViewById(R.id.id);
                    line = (TextView) itemView.findViewById(R.id.line);

                    keterangan = (TextView) itemView.findViewById(R.id.keterangan);
                    status = (TextView) itemView.findViewById(R.id.status);
                    tvTopLine = (TextView) itemView.findViewById(R.id.tvTopLine);
                    tvDot = (TextView) itemView.findViewById(R.id.tvDot);

                    status.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            if(status.getText().toString().equals("Closed")){
                                selesai.setVisibility(View.VISIBLE);
                            } else {
                                selesai.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(status.getText().toString().equals("Closed")){
                                selesai.setVisibility(View.VISIBLE);
                            } else {
                                selesai.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if(status.getText().toString().equals("Closed")){
                                selesai.setVisibility(View.VISIBLE);
                            } else {
                                selesai.setVisibility(View.GONE);
                            }
                        }
                    });




                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            String keterangansoal = keterangan.getText().toString();
                            String statussoal = status.getText().toString();

                            if (keterangansoal.equals("Soal Exit Interview")) {
                                if (statussoal.equals("Closed")) {
                                    new SweetAlertDialog(menu_exitinterview.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText(keterangansoal)
                                            .setContentText("Anda sudah melakukan " + keterangansoal)
                                            .show();
                                } else  if (statussoal.equals("Open")){

                                } else {
                                    Intent intent = new Intent(view.getContext(), soalexitinterview.class);
                                    context.startActivity(intent);
                                }
                            }

                            if (keterangansoal.equals("Pesan & Kesan")) {
                                if (statussoal.equals("Closed")) {
                                    new SweetAlertDialog(menu_exitinterview.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText(keterangansoal)
                                            .setContentText("Anda sudah melakukan " + keterangansoal)
                                            .show();
                                } else  if (statussoal.equals("Open")){

                                } else {
                                    Intent intent = new Intent(view.getContext(), sarandanmasukanexit.class);
                                    context.startActivity(intent);
                                }
                            }
                        }
                    });
                }

                public void setOnClickListener(View.OnClickListener onClickListener) {
                    itemView.setOnClickListener(onClickListener);
                }
            }
        }
    @Override
    protected void onResume() {
        super.onResume();
        kuisionermodels.clear();
        isidata();
    }

    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());


        super.onDestroy();
    }
}