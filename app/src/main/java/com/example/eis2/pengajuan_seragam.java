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
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.biodatamodel;
import com.example.eis2.Item.getNo_pengajuan_full;
import com.google.android.material.navigation.NavigationView;
import com.example.eis2.SearchSpinner.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.izin.txt_nomor;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class pengajuan_seragam extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private SearchableSpinner searchkaryawan, pilihseragam, keterangantext, listqty;
    ArrayList<String> Karyawan;
    ArrayList<String> Seragam;
    EditText nik, nama, jabatan, cabang, departement, kode, maks, nopengajuan;
    TextView harga, kodejabatan;
    ImageButton approval;
    private List<biodatamodel> karyawanbio;
    SharedPreferences sharedPreferences;
    DrawerLayout dLayout;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuan_seragam);
        HttpsTrustManager.allowAllSSL();

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
        kodejabatan = (TextView) findViewById(R.id.kodejabatan);
        Karyawan = new ArrayList<>();
        Seragam = new ArrayList<>();
        karyawanbio = new ArrayList<>();
        setNavigationDrawer();
        approval = (ImageButton) findViewById(R.id.pengajuan);

        searchkaryawan = (SearchableSpinner) findViewById(R.id.karyawan);
        pilihseragam = (SearchableSpinner) findViewById(R.id.jenisseragam);
        keterangantext = (SearchableSpinner) findViewById(R.id.keteranganpengajuan);
        listqty = (SearchableSpinner) findViewById(R.id.qty);

        String[] jenisizin = {"Seragam Habis Masa Pakai", "Seragam Hilang", "Seragam Rusak"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, jenisizin);
        keterangantext.setAdapter(adapter);

        String[] qty = {"1", "2", "3"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, qty);
        listqty.setAdapter(adapter2);

        nik = (EditText) findViewById(R.id.nik);
        nama = (EditText) findViewById(R.id.nama);
        jabatan = (EditText) findViewById(R.id.jabatan);
        cabang = (EditText) findViewById(R.id.lokasi);
        departement = (EditText) findViewById(R.id.department);

        harga = (TextView) findViewById(R.id.harga);

        nopengajuan = (EditText) findViewById(R.id.nopengajuan);
        kode = (EditText) findViewById(R.id.kode);
        maks = (EditText) findViewById(R.id.max);

        getLokasi();
        getSeragam();

        approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNo();
            }
        });

        searchkaryawan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchkaryawan.setTitle("Pilih karyawan");
                String karyawan = searchkaryawan.getItemAtPosition(searchkaryawan.getSelectedItemPosition()).toString();
                if (karyawan.equals("Pilih Karyawan")) {
                    System.out.println();
                } else {
                    String[] splited_text = karyawan.split(" \\(");
                    karyawan = splited_text[1];
                    karyawan = karyawan.replace(")", "");
                    System.out.println("hasil =" + karyawan);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + karyawan,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        JSONArray movieArray = obj.getJSONArray("data");

                                        for (int i = 0; i < movieArray.length(); i++) {

                                            JSONObject movieObject = movieArray.getJSONObject(i);

                                            biodatamodel movieItem = new biodatamodel(
                                                    movieObject.getString("nik_baru"),
                                                    movieObject.getString("nama_karyawan_struktur"),
                                                    movieObject.getString("jabatan_karyawan"),
                                                    movieObject.getString("dept_struktur"),
                                                    movieObject.getString("lokasi_struktur"),
                                                    movieObject.getString("perusahaan_struktur"),
                                                    movieObject.getString("status_karyawan_struktur"));

                                            karyawanbio.add(movieItem);

                                            nik.setText(movieItem.getBadgenumber());
                                            nama.setText(movieItem.getName());
                                            jabatan.setText(movieItem.getJabatan_karyawan());
                                            cabang.setText(movieItem.getLokasi_struktur());
                                            departement.setText(movieItem.getDept_struktur());

                                            kodejab();


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
                    RequestQueue requestQueue = Volley.newRequestQueue(pengajuan_seragam.this);
                    requestQueue.add(stringRequest);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        pilihseragam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pilihseragam.setTitle("Pilih Seragam");
                String seragam = pilihseragam.getItemAtPosition(pilihseragam.getSelectedItemPosition()).toString();
                if (seragam.equals("Pilih Seragam")) {
                    System.out.println();
                } else {
                    String[] splited_text = seragam.split(" \\(");
                    seragam = splited_text[0];
                    System.out.println("hasil =" + seragam);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/seragam/index?id_seragam=" + seragam,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getString("status").equals("true")) {
                                            JSONArray jsonArray = obj.getJSONArray("data");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                String kode1 = jsonObject1.getString("kode_seragam");
                                                String qty = jsonObject1.getString("qty_seragam");
                                                String price = jsonObject1.getString("harga_seragam");


                                                kode.setText(kode1);
                                                maks.setText(qty);
                                                harga.setText(price);


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
                    RequestQueue requestQueue = Volley.newRequestQueue(pengajuan_seragam.this);
                    requestQueue.add(stringRequest);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void kodejab() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + nik.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);
                                final String jabatan = movieObject.getString("jabatan_struktur");

                                kodejabatan.setText(jabatan);

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
        RequestQueue requestQueue = Volley.newRequestQueue(pengajuan_seragam.this);
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
                            pengajuan_seragam.this);
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
                                    Intent intent = new Intent(pengajuan_seragam.this, MainActivity.class);
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

    private void getNo() {
        pDialog = new ProgressDialog(this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Pengajuan_seragam/index", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject finalObject3 = jsonArray.getJSONObject(jsonArray.length() - 1);
                            int a = finalObject3.getInt("no_pengajuan");

                            final DecimalFormat decimalFormat = new DecimalFormat("0000");
                            nopengajuan.setText(String.valueOf(decimalFormat.format(a + 1)));
                            System.out.println("a" + nopengajuan.getText().toString());

                            postseragam();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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
        request.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(request);
    }

    private void postseragam() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/Pengajuan_seragam/index",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                        hideDialog();
                        pengajuan_seragam.this.finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "maaf ada kesalahan", Toast.LENGTH_SHORT).show();

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

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => "+c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                String nikpengajuan = sharedPreferences.getString(KEY_NIK, null);
                String pengajuan = nopengajuan.getText().toString();

                String keterangan = keterangantext.getSelectedItem().toString();
                String nik_baru = nik.getText().toString();
                String name = nama.getText().toString();
                String jabatankaryawan = kodejabatan.getText().toString();
                String departementkaryawan = departement.getText().toString();

                String lokasi = cabang.getText().toString();
                String code = kode.getText().toString();
                String seragam = pilihseragam.getSelectedItem().toString();
                String[] splited_text = seragam.split(" \\(");
                seragam = splited_text[0];

                String qty = listqty.getSelectedItem().toString();
                String price = harga.getText().toString();

                params.put("submit_date", formattedDate);
                params.put("nik_pengajuan", nikpengajuan);
                params.put("no_pengajuan", pengajuan);
                params.put("ket_pengajuan", keterangan);

                params.put("nik_baru", nik_baru);
                params.put("nama_karyawan_seragam", name);
                params.put("jabatan_karyawan_seragam", jabatankaryawan);
                params.put("dept_karyawan_seragam", departementkaryawan);

                params.put("lokasi_karyawan_seragam", lokasi);
                params.put("kode_seragam", code);
                params.put("nama_seragam", seragam);

                params.put("qty_seragam", qty);
                params.put("harga_satuan", price);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest2);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    private void getSeragam() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/seragam/index", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Seragam.add("Pilih Seragam");
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id_seragam");
                            String nama_seragam = jsonObject1.getString("nama_seragam");

                            Seragam.add(id + " (" + nama_seragam + ") ");

                        }
                    }
                    pilihseragam.setTitle("Pilih Seragam");
                    pilihseragam.setAdapter(new ArrayAdapter<String>(pengajuan_seragam.this, android.R.layout.simple_spinner_dropdown_item, Seragam));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void getLokasi(){
        String lokasi = txt_lokasi.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?lokasi_struktur=" + lokasi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Karyawan.add("Pilih Karyawan");
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String karyawan = jsonObject1.getString("nama_karyawan_struktur");
                            String nik = jsonObject1.getString("nik_baru");
                            String jabatan = jsonObject1.getString("dept_struktur");

                            Karyawan.add(karyawan + " (" + nik + ") ");
                            if(jabatan.equals("Board Of Director")){
                                Karyawan.remove(karyawan + " (" + nik + ") ");
                            }

                        }
                    }
                    searchkaryawan.setTitle("Pilih karyawan");
                    searchkaryawan.setAdapter(new ArrayAdapter<String>(pengajuan_seragam.this, android.R.layout.simple_spinner_dropdown_item, Karyawan));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Maaf ada kesalahan", Toast.LENGTH_SHORT).show();
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