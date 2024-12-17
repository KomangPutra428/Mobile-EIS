package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.example.eis2.Item.DataModel;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.approvalcutikhusus;
import com.example.eis2.Item.biodatamodel;
import com.example.eis2.Item.namanikmodel;
import com.example.eis2.Item.rekapabsensiclass;
import com.google.android.material.navigation.NavigationView;
import com.example.eis2.SearchSpinner.SearchableSpinner;

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
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class createshift extends AppCompatActivity {
    private List<biodatamodel> movieItemList;
    private List<namanikmodel> jabatan3;
    DrawerLayout dLayout;

    private SearchableSpinner spinner, spinner2;
    ArrayList<String> Karyawan;
    ArrayList<String> jam;
    TextView textView67, textView30;
    EditText nik, nama, jabatan, departement, lokasi, tanggal;
    private Calendar date;
    private SimpleDateFormat dateFormatter;
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;
    ImageButton pengajuan;

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        Calendar twoDaysAgo = (Calendar) currentDate.clone();
        Calendar later = (Calendar) currentDate.clone();

        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK);

        if (day == Calendar.FRIDAY){
            twoDaysAgo.add(Calendar.DAY_OF_WEEK, 3);
            later.add(Calendar.DAY_OF_WEEK, 9);
        } else if (day == Calendar.SATURDAY){
            twoDaysAgo.add(Calendar.DAY_OF_WEEK, 2);
            later.add(Calendar.DAY_OF_WEEK, 8);
        }

        date = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                tanggal.setText(dateFormatter.format(date.getTime()));
                getIDUser();

            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(createshift.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(later.getTimeInMillis());

        datePickerDialog.show();

    }

    private void getIDUser() {
        pDialog = new ProgressDialog(createshift.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        String karyawan = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        String[] splited_text = karyawan.split(" \\(");
        karyawan = splited_text[1];
        karyawan = karyawan.replace(")", "");
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Absenmobile/index?nik_baru=" + karyawan + "&shift_day=" + convertFormat2(tanggal.getText().toString()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String userid = jsonObject1.getString("userid");
                        textView30.setText(userid);
                        System.out.println("Hasil user id=" + userid);
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Maaf, Ada kesalahan", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(createshift.this);
        requestQueue2.add(stringRequest2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createshift);
        HttpsTrustManager.allowAllSSL();

        nik = (EditText) findViewById(R.id.nik);
        nama = (EditText) findViewById(R.id.nama);
        jabatan = (EditText) findViewById(R.id.jabatan);
        departement = (EditText) findViewById(R.id.department);
        lokasi = (EditText) findViewById(R.id.lokasi);
        tanggal = (EditText) findViewById(R.id.mulaitanggal);
        pengajuan = (ImageButton) findViewById(R.id.pengajuan);

        textView30 = (TextView) findViewById(R.id.textView30);
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

        textView67 = (TextView) findViewById(R.id.textView67);


        dateFormatter = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
        getJam();

        Karyawan = new ArrayList<>();
        jam = new ArrayList<>();
        jabatan3 = new ArrayList<>();


        spinner = (SearchableSpinner) findViewById(R.id.karyawan);
        spinner2 = (SearchableSpinner) findViewById(R.id.jam);

        AtomicLong mLastClickTime = new AtomicLong();
        spinner.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                spinner.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    spinner.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            spinner.onTouch(v,event);
            spinner.setEnabled(false);

            return true;
        });

        spinner2.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                spinner2.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    spinner2.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            spinner2.onTouch(v,event);
            spinner2.setEnabled(false);

            return true;
        });


        pengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();
                int day = now.get(Calendar.DAY_OF_WEEK);
                if (day == Calendar.FRIDAY || day == Calendar.SATURDAY){
                    pDialog = new ProgressDialog(createshift.this);
                    showDialog();
                    pDialog.setContentView(R.layout.progress_dialog);
                    pDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    pengajuan();
                } else {
                    Toast.makeText(getApplicationContext(), "Tidak Bisa Di ajukan", Toast.LENGTH_SHORT).show();

                }
            }
        });


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String jam2 = spinner2.getItemAtPosition(spinner2.getSelectedItemPosition()).toString();
                String[] splited_text2 = jam2.split(" \\(");
                jam2 = splited_text2[0];
                jam2 = jam2.replace(")", "");
                System.out.println("id jam=" + jam2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getLokasi();
        movieItemList = new ArrayList<>();

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner.setTitle("Pilih karyawan");
                String karyawan = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                if(karyawan.equals("Pilih Karyawan")){
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
                                                    movieObject.getString("join_date_struktur"),
                                                    movieObject.getString("status_karyawan_struktur"));

                                            movieItemList.add(movieItem);

                                            nik.setText(movieItem.getBadgenumber());
                                            nama.setText(movieItem.getName());
                                            jabatan.setText(movieItem.getJabatan_karyawan());
                                            departement.setText(movieItem.getDept_struktur());
                                            lokasi.setText(movieItem.getLokasi_struktur());

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
                    RequestQueue requestQueue = Volley.newRequestQueue(createshift.this);
                    requestQueue.add(stringRequest);

                }

                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + karyawan,
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

                                        textView67.setText(movieObject.getString("jabatan_struktur"));
                                        jabatan3.add(movieItem);

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
                RequestQueue requestQueue = Volley.newRequestQueue(createshift.this);
                requestQueue.add(stringRequest);

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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
                            createshift.this);
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
                                    Intent intent = new Intent(createshift.this, MainActivity.class);
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

    private void pengajuan() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/shifting/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        update();
                    }
                },
                    new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            String nik_baru = sharedPreferences.getString(KEY_NIK, null);
            String nikbaru = nik.getText().toString();
            String namakaryawan = nama.getText().toString();
            String jabatankaryawan = textView67.getText().toString();
            String dept = departement.getText().toString();
            String lok = lokasi.getText().toString();
            String jam2 = spinner2.getItemAtPosition(spinner2.getSelectedItemPosition()).toString();
            String[] splited_text2 = jam2.split(" \\(");
            jam2 = splited_text2[0];
            jam2 = jam2.replace(")", "");

            String tanggalshift = tanggal.getText().toString();

            params.put("user_submit", nik_baru);

            params.put("nik_shift", nikbaru);
            params.put("nama_karyawan_shift", namakaryawan);
            params.put("jabatan_karyawan_shift", jabatankaryawan);
            params.put("dept_karyawan_shift", dept);
            params.put("lokasi_karyawan_shift", lok);

            params.put("jam_kerja", jam2);
            params.put("start_periode", "0");
            params.put("end_periode", "0");
            params.put("tanggal_shift", convertFormat2(tanggalshift));

            params.put("keterangan_shift", "");
            params.put("no_co_shift", "");

            return params;
        }

    };

                stringRequest2.setRetryPolicy(
                        new DefaultRetryPolicy(
                                500000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                ));
                RequestQueue requestQueue2 = Volley.newRequestQueue(createshift.this);
                requestQueue2.add(stringRequest2);
        }

    private void update() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/shifting/index_jam",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                        createshift.this.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                String jam2 = spinner2.getItemAtPosition(spinner2.getSelectedItemPosition()).toString();
                String[] splited_text2 = jam2.split(" \\(");
                jam2 = splited_text2[0];
                jam2 = jam2.replace(")", "");

                params.put("userid", textView30.getText().toString());
                params.put("shift_day", convertFormat2(tanggal.getText().toString()));
                params.put("waktu_shift", jam2);


                return params;
            }

        };

        stringRequest2.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue2 = Volley.newRequestQueue(createshift.this);
        requestQueue2.add(stringRequest2);
    }


    private void getJam() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/mobile_eis_2/jam.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jam.add("Pilih Jam");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id_shifting = jsonObject1.getString("id_shifting");
                            String waktu_masuk = jsonObject1.getString("waktu_masuk");
                            String waktu_keluar = jsonObject1.getString("waktu_keluar");

                            jam.add(id_shifting + " ("  + waktu_masuk + "-" + waktu_keluar + ")" );

                        }
                    spinner2.setTitle("Pilih Jam");
                    spinner2.setAdapter(new ArrayAdapter<String>(createshift.this, android.R.layout.simple_spinner_dropdown_item, jam));
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



    private void getLokasi() {
        String lokasi = txt_lokasi.getText().toString().trim();
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
                            Karyawan.add(karyawan + " (" + nik + ") ");

                        }
                    }
                    spinner.setTitle("Pilih karyawan");
                    spinner.setAdapter(new ArrayAdapter<String>(createshift.this, android.R.layout.simple_spinner_dropdown_item, Karyawan));
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

    public static String convertFormat2(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return convetDateFormat.format(date);
    }
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

        
        super.onDestroy();
    }
}