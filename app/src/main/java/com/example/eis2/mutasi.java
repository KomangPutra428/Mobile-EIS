package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.example.eis2.Item.approvaldinasfull;
import com.example.eis2.Item.biodatamodel;
import com.example.eis2.Item.getNo_pengajuan_full;
import com.example.eis2.Item.leveljabatanmodel;
import com.google.android.material.navigation.NavigationView;
import com.example.eis2.SearchSpinner.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class mutasi extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    public static SearchableSpinner employee, departmentbaru, jabatanbaru, lokasibaru, ptbaru;

    ArrayList<String> Karyawan;
    ArrayList<String> departementaray;
    ArrayList<String> jabatanaray;
    ArrayList<String> lokasi2;
    ArrayList<String> pts;

    SharedPreferences sharedPreferences;
    DrawerLayout dLayout;

    ProgressDialog pDialog;

    public static EditText nik, nama, pt, department, lokasi, jab, rekomendasi, nopengajuan;
    RelativeLayout rotasilayout;
    ImageButton pengajuan;
    public static ImageView opsi;
    RadioGroup opsipilihan, option;
    public static RadioButton promosi, demosi, rotasi, panel, pjs;
    private Calendar date;
    private SimpleDateFormat dateFormatter;

    TextView level;
    static TextView nomorjabatan;
    static TextView nomorjabatanawal;
    TextView levelawal;

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();

        date = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                rekomendasi.setText(dateFormatter.format(date.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(mutasi.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutasi);
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

        employee = (SearchableSpinner) findViewById(R.id.karyawan);
        departmentbaru = (SearchableSpinner) findViewById(R.id.departmentbaru);
        jabatanbaru = (SearchableSpinner) findViewById(R.id.jabatanbaru);
        lokasibaru = (SearchableSpinner) findViewById(R.id.lokasibaru);
        ptbaru = (SearchableSpinner) findViewById(R.id.ptbaru);
        setNavigationDrawer();
        nomorjabatan = (TextView) findViewById(R.id.nomorjabatan);
        level = (TextView) findViewById(R.id.level);

        nomorjabatanawal = (TextView) findViewById(R.id.nomorjabatanawal);
        levelawal = (TextView) findViewById(R.id.levelawal);

        rekomendasi = (EditText) findViewById(R.id.rekomendasi);
        nopengajuan = (EditText) findViewById(R.id.nopengajuan);

        rotasilayout = (RelativeLayout) findViewById(R.id.rotasilayout);
        pengajuan = (ImageButton) findViewById(R.id.pengajuan);





        dateFormatter = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());

        promosi = (RadioButton) findViewById(R.id.promosi);
        demosi = (RadioButton) findViewById(R.id.demosi);
        rotasi = (RadioButton) findViewById(R.id.rotasi);

        panel = (RadioButton) findViewById(R.id.panel);
        pjs = (RadioButton) findViewById(R.id.pjs);

        opsi = (ImageView) findViewById(R.id.opsi);
        opsipilihan = (RadioGroup) findViewById(R.id.opsipilihan);

        option = (RadioGroup) findViewById(R.id.option);
        option.setOnCheckedChangeListener(this);
        opsipilihan.setOnCheckedChangeListener(this);


        nik = (EditText) findViewById(R.id.nik);
        nama = (EditText) findViewById(R.id.nama);
        pt = (EditText) findViewById(R.id.pt);
        department = (EditText) findViewById(R.id.department);
        lokasi  = (EditText) findViewById(R.id.lokasi);
        jab  = (EditText) findViewById(R.id.jab);

        rekomendasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        Karyawan = new ArrayList<>();
        departementaray = new ArrayList<>();
        jabatanaray = new ArrayList<>();
        lokasi2 = new ArrayList<>();
        pts = new ArrayList<>();

        spinnerKaryawan();
        getDepartment();
        getJabatan();
        getLokasi();
        getPT();

        pengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (promosi.isChecked()) {
                    if(employee.getSelectedItem().toString().equals("Pilih Karyawan")) {
                        Toast.makeText(getApplicationContext(), "Pilih karyawan terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }else if(ptbaru.getSelectedItem().toString().equals("Pilih Perusahaan")) {
                        Toast.makeText(getApplicationContext(), "Pilih Perusahaan terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else if(departmentbaru.getSelectedItem().toString().equals("Pilih departement")) {
                        Toast.makeText(getApplicationContext(), "Pilih Departement terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else if(jabatanbaru.getSelectedItem().toString().equals("Pilih jabatan")) {
                        Toast.makeText(getApplicationContext(), "Pilih jabatan terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else if(lokasibaru.getSelectedItem().toString().equals("Pilih Lokasi")) {
                        Toast.makeText(getApplicationContext(), "Pilih Lokasi terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else if(rekomendasi.getText().toString().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Isi tanggal", Toast.LENGTH_SHORT).show();
                    } else {
                        getNo();
                    }
                } else if (demosi.isChecked()){
                    if(employee.getSelectedItem().toString().equals("Pilih Karyawan")) {
                        Toast.makeText(getApplicationContext(), "Pilih karyawan terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }else if(ptbaru.getSelectedItem().toString().equals("Pilih Perusahaan")) {
                        Toast.makeText(getApplicationContext(), "Pilih Perusahaan terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else if(departmentbaru.getSelectedItem().toString().equals("Pilih departement")) {
                        Toast.makeText(getApplicationContext(), "Pilih Departement terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else if(jabatanbaru.getSelectedItem().toString().equals("Pilih jabatan")) {
                        Toast.makeText(getApplicationContext(), "Pilih jabatan terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else if(lokasibaru.getSelectedItem().toString().equals("Pilih Lokasi")) {
                        Toast.makeText(getApplicationContext(), "Pilih Lokasi terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else if(rekomendasi.getText().toString().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Isi tanggal", Toast.LENGTH_SHORT).show();
                    } else {
                        getNo();
                    }
                } else if (rotasi.isChecked()){
                    if(employee.getSelectedItem().toString().equals("Pilih Karyawan")) {
                        Toast.makeText(getApplicationContext(), "Pilih karyawan terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }else if(ptbaru.getSelectedItem().toString().equals("Pilih Perusahaan")) {
                        Toast.makeText(getApplicationContext(), "Pilih Perusahaan terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else if(departmentbaru.getSelectedItem().toString().equals("Pilih departement")) {
                        Toast.makeText(getApplicationContext(), "Pilih Departement terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else if(jabatanbaru.getSelectedItem().toString().equals("Pilih jabatan")) {
                        Toast.makeText(getApplicationContext(), "Pilih jabatan terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else if(lokasibaru.getSelectedItem().toString().equals("Pilih Lokasi")) {
                        Toast.makeText(getApplicationContext(), "Pilih Lokasi terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else if(rekomendasi.getText().toString().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Isi tanggal", Toast.LENGTH_SHORT).show();
                    } else {
                        getNo();
                    }
                }
            }
        });

        jabatanbaru.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jabatanbaru.setTitle("Pilih jabatan");
                String karyawan = jabatanbaru.getItemAtPosition(jabatanbaru.getSelectedItemPosition()).toString();
                if (karyawan.equals("Pilih jabatan")) {
                    System.out.println();
                } else {
                    String[] splited_text = karyawan.split(" \\(");
                    karyawan = splited_text[0];
                    karyawan = karyawan.replace("(", "");
                    System.out.println("hasil =" + karyawan);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/jabatan/index?no_jabatan_karyawan=" + karyawan,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        JSONArray movieArray = obj.getJSONArray("data");

                                        for (int i = 0; i < movieArray.length(); i++) {

                                            JSONObject movieObject = movieArray.getJSONObject(i);

                                            nomorjabatan.setText(movieObject.getString("no_jabatan_karyawan"));
                                            if(movieObject.getString("level_jabatan_karyawan").equals("1")){
                                                level.setText("Operator");
                                            }
                                            if(movieObject.getString("level_jabatan_karyawan").equals("2")){
                                                level.setText("Staff");
                                            }
                                            if(movieObject.getString("level_jabatan_karyawan").equals("3")){
                                                level.setText("Senior Staff");
                                            }
                                            if(movieObject.getString("level_jabatan_karyawan").equals("4")){
                                                level.setText("Junior Supervisor");
                                            }
                                            if(movieObject.getString("level_jabatan_karyawan").equals("5")){
                                                level.setText("Supervisor");
                                            }

                                            if(movieObject.getString("level_jabatan_karyawan").equals("6")){
                                                level.setText("Senior Supervisor");
                                            }
                                            if(movieObject.getString("level_jabatan_karyawan").equals("7")){
                                                level.setText("Assistant Manager");
                                            }
                                            if(movieObject.getString("level_jabatan_karyawan").equals("8")){
                                                level.setText("Manager");
                                            }
                                            if(movieObject.getString("level_jabatan_karyawan").equals("9")){
                                                level.setText("Senior Manager");
                                            }
                                            if(movieObject.getString("level_jabatan_karyawan").equals("10")){
                                                level.setText("Direksi");
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
                    RequestQueue requestQueue = Volley.newRequestQueue(mutasi.this);
                    requestQueue.add(stringRequest);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        employee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                employee.setTitle("Pilih karyawan");
                String karyawan = employee.getItemAtPosition(employee.getSelectedItemPosition()).toString();
                if (karyawan.equals("Pilih karyawan")) {
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

                                            nik.setText(movieObject.getString("nik_baru"));
                                            nama.setText(movieObject.getString("nama_karyawan_struktur"));
                                            jab.setText(movieObject.getString("jabatan_karyawan"));
                                            department.setText(movieObject.getString("dept_struktur"));
                                            lokasi.setText(movieObject.getString("lokasi_struktur"));
                                            pt.setText(movieObject.getString("perusahaan_struktur"));

                                            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + nik.getText().toString(),
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {

                                                            try {
                                                                JSONObject obj = new JSONObject(response);
                                                                JSONArray movieArray = obj.getJSONArray("data");

                                                                for (int i = 0; i < movieArray.length(); i++) {

                                                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                                                    nomorjabatanawal.setText(movieObject.getString("jabatan_struktur"));
                                                                    if(movieObject.getString("level_jabatan_karyawan").equals("1")){
                                                                        levelawal.setText("Operator");
                                                                    }
                                                                    if(movieObject.getString("level_jabatan_karyawan").equals("2")){
                                                                        levelawal.setText("Staff");
                                                                    }
                                                                    if(movieObject.getString("level_jabatan_karyawan").equals("3")){
                                                                        levelawal.setText("Senior Staff");
                                                                    }
                                                                    if(movieObject.getString("level_jabatan_karyawan").equals("4")){
                                                                        levelawal.setText("Junior Supervisor");
                                                                    }
                                                                    if(movieObject.getString("level_jabatan_karyawan").equals("5")){
                                                                        levelawal.setText("Supervisor");
                                                                    }

                                                                    if(movieObject.getString("level_jabatan_karyawan").equals("6")){
                                                                        levelawal.setText("Senior Supervisor");
                                                                    }
                                                                    if(movieObject.getString("level_jabatan_karyawan").equals("7")){
                                                                        levelawal.setText("Assistant Manager");
                                                                    }
                                                                    if(movieObject.getString("level_jabatan_karyawan").equals("8")){
                                                                        levelawal.setText("Manager");
                                                                    }
                                                                    if(movieObject.getString("level_jabatan_karyawan").equals("9")){
                                                                        levelawal.setText("Senior Manager");
                                                                    }
                                                                    if(movieObject.getString("level_jabatan_karyawan").equals("10")){
                                                                        levelawal.setText("Direksi");
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
                                            RequestQueue requestQueue = Volley.newRequestQueue(mutasi.this);
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
                    RequestQueue requestQueue = Volley.newRequestQueue(mutasi.this);
                    requestQueue.add(stringRequest);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AtomicLong mLastClickTime = new AtomicLong();
        ptbaru.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                ptbaru.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ptbaru.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            ptbaru.onTouch(v,event);
            ptbaru.setEnabled(false);

            return true;
        });

        lokasibaru.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                lokasibaru.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lokasibaru.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            lokasibaru.onTouch(v,event);
            lokasibaru.setEnabled(false);

            return true;
        });

        jabatanbaru.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                jabatanbaru.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    jabatanbaru.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            jabatanbaru.onTouch(v,event);
            jabatanbaru.setEnabled(false);

            return true;
        });

        departmentbaru.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                departmentbaru.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    departmentbaru.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            departmentbaru.onTouch(v,event);
            departmentbaru.setEnabled(false);

            return true;
        });

        employee.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                employee.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    employee.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            employee.onTouch(v,event);
            employee.setEnabled(false);

            return true;
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
                            mutasi.this);
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
                                    Intent intent = new Intent(mutasi.this, MainActivity.class);
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

    private void getPT() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/mobile_eis_2/perusahaan.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pts.add("Pilih PT");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String nik = jsonObject1.getString("perusahaan_nama");
                        pts.add(nik);
                    }
                    ptbaru.setTitle("Pilih PT");
                    ptbaru.setAdapter(new ArrayAdapter<String>(mutasi.this, android.R.layout.simple_spinner_dropdown_item, pts));
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/mobile_eis_2/depo.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    lokasi2.add("Pilih Lokasi");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String nik = jsonObject1.getString("depo_nama");
                        lokasi2.add(nik);
                    }
                    lokasibaru.setTitle("Pilih Lokasi");
                    lokasibaru.setAdapter(new ArrayAdapter<String>(mutasi.this, android.R.layout.simple_spinner_dropdown_item, lokasi2));
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

    private void getDepartment() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/mobile_eis_2/departement.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    departementaray.add("Pilih departement");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String nik = jsonObject1.getString("nama_departement");
                        departementaray.add(nik);
                    }
                    departmentbaru.setTitle("Pilih Departement");
                    departmentbaru.setAdapter(new ArrayAdapter<String>(mutasi.this, android.R.layout.simple_spinner_dropdown_item, departementaray));
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
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getJabatan() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/jabatan/index", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jabatanaray.add("Pilih jabatan");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String nik = jsonObject1.getString("jabatan_karyawan");
                        String leveljabatan = jsonObject1.getString("no_jabatan_karyawan");
                        jabatanaray.add(leveljabatan + " (" + nik + ")");
                    }

                    jabatanbaru.setTitle("Pilih jabatan");
                    jabatanbaru.setAdapter(new ArrayAdapter<String>(mutasi.this, android.R.layout.simple_spinner_dropdown_item, jabatanaray));
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

    private void spinnerKaryawan() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?lokasi_struktur=" + txt_lokasi.getText().toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Karyawan.add("Pilih karyawan");
                try {
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
                    employee.setTitle("Pilih karyawan");
                    employee.setAdapter(new ArrayAdapter<String>(mutasi.this, android.R.layout.simple_spinner_dropdown_item, Karyawan));
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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i ==R.id.promosi){
            opsipilihan.clearCheck();
            opsi.setVisibility(View.VISIBLE);
            panel.setVisibility(View.VISIBLE);
            pjs.setVisibility(View.VISIBLE);
            opsipilihan.setVisibility(View.VISIBLE);
            rotasilayout.setVisibility(View.GONE);
            pengajuan.setVisibility(View.GONE);
        }

        if (i ==R.id.panel){
            opsipilihan.setVisibility(View.VISIBLE);
            rotasilayout.setVisibility(View.VISIBLE);
            pengajuan.setVisibility(View.VISIBLE);
        }

        if (i ==R.id.pjs){
            opsipilihan.setVisibility(View.VISIBLE);
            rotasilayout.setVisibility(View.VISIBLE);
            pengajuan.setVisibility(View.VISIBLE);
        }

        if (i ==R.id.demosi){
            opsipilihan.clearCheck();
            opsi.setVisibility(View.GONE);
            panel.setVisibility(View.GONE);
            pjs.setVisibility(View.GONE);
            rotasilayout.setVisibility(View.VISIBLE);
            pengajuan.setVisibility(View.VISIBLE);
        }
        if (i ==R.id.rotasi){
            opsipilihan.clearCheck();
            opsi.setVisibility(View.GONE);
            panel.setVisibility(View.GONE);
            pjs.setVisibility(View.GONE);
            rotasilayout.setVisibility(View.VISIBLE);
            pengajuan.setVisibility(View.VISIBLE);
        }
    }


    private void getNo() {
            pDialog = new ProgressDialog(this);
            showDialog();
            pDialog.setContentView(R.layout.progress_dialog);
            pDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("data");
                                JSONObject finalObject3 = jsonArray.getJSONObject(jsonArray.length() -1);
                                int a =finalObject3.getInt("no_pengajuan");

                                final DecimalFormat decimalFormat = new DecimalFormat("0000");
                                nopengajuan.setText(String.valueOf(decimalFormat.format(a + 1)));
                                System.out.println("nomor =" + nopengajuan.getText().toString());
                                if(promosi.isChecked()) {
                                    postpromosi();
                                } else if (demosi.isChecked()){
                                    postdemosirotasi();
                                } else if (rotasi.isChecked()){
                                    postdemosirotasi();
                                }

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

    private void postdemosirotasi() {
        final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(lokasibaru.getSelectedItem().toString().equals(lokasi.getText().toString())){
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                            mutasi.this.finish();
                        } else {
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(mutasi.this, karyawan_project.class);
                            startActivity(i);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
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
                String jabatan_pengajuan = text_jabatan.getText().toString();
                String nik2 = nik.getText().toString();
                String nomor = nopengajuan.getText().toString();
                String namakaryawan = nama.getText().toString();
                String ptawal1 = pt.getText().toString();
                String departementawal = department.getText().toString();

                String lokasiawal = lokasi.getText().toString();
                String jabatan = nomorjabatanawal.getText().toString();
                String level2 = levelawal.getText().toString();

                String ptbaru2 = ptbaru.getSelectedItem().toString();
                String departementbaru = departmentbaru.getSelectedItem().toString();
                String lokasibaru2 = lokasibaru.getSelectedItem().toString();
                String jbtbaru = nomorjabatan.getText().toString();

                String levelbaru = level.getText().toString();

                String tanggalrekomendasi = rekomendasi.getText().toString();

                params.put("nik_pengajuan", nik_baru);
                params.put("jabatan_pengajuan", jabatan_pengajuan);
                params.put("nik_baru", nik2);
                params.put("no_pengajuan", nomor);

                params.put("opsi", "");

                params.put("nama_karyawan_mutasi", namakaryawan);
                params.put("pt_awal", ptawal1);
                params.put("dept_awal", departementawal);

                params.put("lokasi_awal", lokasiawal);
                params.put("jabatan_awal", jabatan);
                params.put("level_awal", level2);
                if(demosi.isChecked()) {
                    params.put("permintaan", "demosi");
                } else if(rotasi.isChecked()){
                    params.put("permintaan", "rotasi");
                }
                params.put("pt_baru", ptbaru2);
                params.put("dept_baru", departementbaru);
                params.put("lokasi_baru", lokasibaru2);
                params.put("jabatan_baru", jbtbaru);

                params.put("level_baru", levelbaru);
                params.put("rekomendasi_tanggal", convertFormat(tanggalrekomendasi));
                params.put("status_atasan", "0");
                params.put("status_1", "0");

                params.put("status_dokumen", "0");
                params.put("status_pengajuan", "0");
                params.put("nik_lama", nik2);

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

    private void postpromosi() {
        final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(lokasibaru.getSelectedItem().toString().equals(lokasi.getText().toString())){
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                            mutasi.this.finish();
                        } else {
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(mutasi.this, karyawan_project.class);
                            startActivity(i);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
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
                String jabatan_pengajuan = text_jabatan.getText().toString();
                String nik2 = nik.getText().toString();
                String nomor = nopengajuan.getText().toString();
                String namakaryawan = nama.getText().toString();
                String ptawal1 = pt.getText().toString();
                String departementawal = department.getText().toString();

                String lokasiawal = lokasi.getText().toString();
                String jabatan = nomorjabatanawal.getText().toString();
                String level2 = levelawal.getText().toString();

                String ptbaru2 = ptbaru.getSelectedItem().toString();
                String departementbaru = departmentbaru.getSelectedItem().toString();
                String lokasibaru2 = lokasibaru.getSelectedItem().toString();
                String jbtbaru = nomorjabatan.getText().toString();

                String levelbaru = level.getText().toString();

                String tanggalrekomendasi = rekomendasi.getText().toString();

                params.put("nik_pengajuan", nik_baru);
                params.put("jabatan_pengajuan", jabatan_pengajuan);
                params.put("nik_baru", nik2);
                params.put("no_pengajuan", nomor);

                if(panel.isChecked()) {
                    params.put("opsi", "Panel");
                } else if (pjs.isChecked()) {
                    params.put("opsi", "PLT");
                }

                params.put("nama_karyawan_mutasi", namakaryawan);
                params.put("pt_awal", ptawal1);
                params.put("dept_awal", departementawal);

                params.put("lokasi_awal", lokasiawal);
                params.put("jabatan_awal", jabatan);
                params.put("level_awal", level2);
                params.put("permintaan", "promosi");

                params.put("pt_baru", ptbaru2);
                params.put("dept_baru", departementbaru);
                params.put("lokasi_baru", lokasibaru2);
                params.put("jabatan_baru", jbtbaru);

                params.put("level_baru", levelbaru);
                params.put("rekomendasi_tanggal", convertFormat(tanggalrekomendasi));
                params.put("status_atasan", "0");
                params.put("status_1", "0");

                params.put("status_dokumen", "0");
                params.put("status_pengajuan", "0");
                params.put("nik_lama", nik2);

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


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public static String convertFormat(String inputDate) {
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