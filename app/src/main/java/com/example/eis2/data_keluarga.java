package com.example.eis2;

import static android.view.View.GONE;
import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.biodata.noUrut;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_lokasi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
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
import com.example.eis2.Item.Utility;
import com.example.eis2.Item.anakModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
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
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class data_keluarga extends AppCompatActivity {
    EditText nama_lengkap_keluarga, noKtpKeluarga, tgllahirkeluarga, sukukeluarga, tempatlahirkeluarga;
    AutoCompleteTextView golongandarahkeluarga, agamakeluarga, pendidikanterakhir_keluarga, pendidikanibu, pendidikanayah;
    RadioButton wni, wna;

    String golongandarah[]={
            "A", "B", "AB","O"
    };

    String agama[]={
            "Islam", "Kristen", "Khatolik", "Budha", "Hindu", "Khong Huchu"
    };

    String pendidikan[]={
            "SD", "SMP/MTS", "SMK/SMA/MA", "S1", "S2", "S3"
    };

    private Calendar date;
    private SimpleDateFormat dateFormatter;

    ListViewAnakAdapter adapter;

    SweetAlertDialog pDialog, Success;

    ListView anaklist;

    List<anakModel> anakModelList = new ArrayList<>();

    EditText namaayah, tgllahirayah, pekerjaaanAyah;
    RadioButton lakiAyah, PerempuanAyah;

    EditText namaibu, tgllahiribu, pekerjaaanibu;
    RadioButton lakiIbu, PerempuanIbu;

    EditText nama_lengkap_darurat, nomor_darurat, alamat_darurat;

    SharedPreferences sharedPreferences;
    DrawerLayout dLayout;
    MaterialButton batal, ubah, add;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_keluarga);

        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        nama_lengkap_keluarga = findViewById(R.id.nama_lengkap_keluarga);
        noKtpKeluarga = findViewById(R.id.noKtpKeluarga);
        tgllahirkeluarga = findViewById(R.id.tgllahirkeluarga);
        sukukeluarga = findViewById(R.id.sukukeluarga);
        tempatlahirkeluarga = findViewById(R.id.tempatlahirkeluarga);

        golongandarahkeluarga = findViewById(R.id.golongandarahkeluarga);
        agamakeluarga = findViewById(R.id.agamakeluarga);
        pendidikanterakhir_keluarga = findViewById(R.id.pendidikanterakhir_keluarga);

        namaayah = findViewById(R.id.namaayah);
        tgllahirayah = findViewById(R.id.tgllahirayah);
        pekerjaaanAyah = findViewById(R.id.pekerjaaanAyah);
        pendidikanayah = findViewById(R.id.pendidikanayah);

        lakiAyah = findViewById(R.id.lakiAyah);
        PerempuanAyah = findViewById(R.id.PerempuanAyah);


        namaibu = findViewById(R.id.namaibu);
        tgllahiribu = findViewById(R.id.tgllahiribu);
        pekerjaaanibu = findViewById(R.id.pekerjaaanibu);
        pendidikanibu = findViewById(R.id.pendidikanibu);

        lakiIbu = findViewById(R.id.lakiIbu);
        PerempuanIbu = findViewById(R.id.PerempuanIbu);

        nama_lengkap_darurat = findViewById(R.id.nama_lengkap_darurat);
        nomor_darurat = findViewById(R.id.nomor_darurat);
        alamat_darurat = findViewById(R.id.alamat_darurat);

        wni = findViewById(R.id.wni);
        wna = findViewById(R.id.wna);

        anaklist = findViewById(R.id.anaklist);

        batal = findViewById(R.id.batal);
        ubah = findViewById(R.id.ubah);

        add = findViewById(R.id.add);

        golongandarahkeluarga.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, golongandarah));
        agamakeluarga.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, agama));

        pendidikanayah.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, pendidikan));
        pendidikanibu.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, pendidikan));
        pendidikanterakhir_keluarga.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, pendidikan));

        golongandarahkeluarga.setThreshold(100);
        agamakeluarga.setThreshold(100);

        pendidikanayah.setThreshold(100);
        pendidikanibu.setThreshold(100);
        pendidikanterakhir_keluarga.setThreshold(100);


        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        getSuamiIstri(nik_baru);
        getAnak(nik_baru);
        getSusunanKeluarga(nik_baru);
        getDarurat(nik_baru);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(data_keluarga.this);
                dialog.setContentView(R.layout.data_anak);
                dialog.setCancelable(false);
                dialog.show();

                final EditText urutananak = dialog.findViewById(R.id.urutananak);
                final EditText nama_lengkap_anak = dialog.findViewById(R.id.nama_lengkap_anak);
                final EditText no_ktp_anak = dialog.findViewById(R.id.no_ktp_anak);
                final EditText tgl_lahir_anak = dialog.findViewById(R.id.tgl_lahir_anak);
                final EditText tempat_lahir_anak = dialog.findViewById(R.id.tempat_lahir_anak);

                final AutoCompleteTextView golongan_darah_anak = dialog.findViewById(R.id.golongan_darah_anak);
                final AutoCompleteTextView agama_anak = dialog.findViewById(R.id.agama_anak);
                final EditText suku_anak = dialog.findViewById(R.id.suku_anak);

                final RadioButton wni_anak = dialog.findViewById(R.id.wni_anak);
                RadioButton wna_anak = dialog.findViewById(R.id.wna_anak);

                final AutoCompleteTextView pendidikanterakhir_anak = dialog.findViewById(R.id.pendidikanterakhir_anak);
                MaterialButton batal = dialog.findViewById(R.id.batal);
                final MaterialButton tambah = dialog.findViewById(R.id.tambah);

                golongan_darah_anak.setAdapter(new ArrayAdapter<String>(data_keluarga.this, android.R.layout.simple_dropdown_item_1line, golongandarah));
                agama_anak.setAdapter(new ArrayAdapter<String>(data_keluarga.this, android.R.layout.simple_dropdown_item_1line, agama));
                pendidikanterakhir_anak.setAdapter(new ArrayAdapter<String>(data_keluarga.this, android.R.layout.simple_dropdown_item_1line, pendidikan));

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tambah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(no_ktp_anak.getText().toString().length() == 0){
                            tambah.setEnabled(false);
                            pDialog = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Harap Menunggu");
                            pDialog.setCancelable(false);
                            pDialog.show();

                            final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_anak",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            pDialog.dismissWithAnimation();
                                            Success = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.SUCCESS_TYPE);
                                            Success.setContentText("Data Sudah Diupdate");
                                            Success.setCancelable(false);
                                            Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                    String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                                                    getAnak(nik_baru);
                                                    dialog.dismiss();

                                                    adapter.clear();
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                            Success.show();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            pDialog.dismissWithAnimation();
                                            Success = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.SUCCESS_TYPE);
                                            Success.setContentText("Data Sudah Diupdate");
                                            Success.setCancelable(false);
                                            Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                    String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                                                    getAnak(nik_baru);
                                                    dialog.dismiss();

                                                    adapter.clear();
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                            Success.show();
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


                                    params.put("nik_baru", nik_baru);
                                    params.put("urutan_anak", urutananak.getText().toString());
                                    params.put("nama_anak", nama_lengkap_anak.getText().toString());
                                    params.put("no_ktp_anak", no_ktp_anak.getText().toString());

                                    params.put("tanggal_lahir_anak", ReverseconvertFormat(tgl_lahir_anak.getText().toString()));
                                    params.put("tempat_lahir_anak", tempat_lahir_anak.getText().toString());
                                    params.put("gol_darah_anak", golongan_darah_anak.getText().toString());
                                    params.put("agama_anak", agama_anak.getText().toString());

                                    params.put("suku_anak", suku_anak.getText().toString());
                                    if(wni_anak.isChecked()){
                                        params.put("kewarganegaraan_anak", "WNI");
                                    } else {
                                        params.put("kewarganegaraan_anak", "WNA");
                                    }

                                    params.put("pendidikan_anak", pendidikanterakhir_anak.getText().toString());

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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(data_keluarga.this);
                            requestQueue2.add(stringRequest2);
                        } else if (no_ktp_anak.getText().toString().length() < 16){
                            no_ktp_anak.setError("Digit KTP Maksimal 16");
                        } else {
                            tambah.setEnabled(false);
                            pDialog = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Harap Menunggu");
                            pDialog.setCancelable(false);
                            pDialog.show();

                            final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_anak",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            pDialog.dismissWithAnimation();
                                            Success = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.SUCCESS_TYPE);
                                            Success.setContentText("Data Sudah Diupdate");
                                            Success.setCancelable(false);
                                            Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                    String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                                                    getAnak(nik_baru);
                                                    dialog.dismiss();

                                                    adapter.clear();
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                            Success.show();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            pDialog.dismissWithAnimation();
                                            Success = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.SUCCESS_TYPE);
                                            Success.setContentText("Data Sudah Diupdate");
                                            Success.setCancelable(false);
                                            Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                    String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                                                    getAnak(nik_baru);
                                                    dialog.dismiss();

                                                    adapter.clear();
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                            Success.show();
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


                                    params.put("nik_baru", nik_baru);
                                    params.put("urutan_anak", urutananak.getText().toString());
                                    params.put("nama_anak", nama_lengkap_anak.getText().toString());
                                    params.put("no_ktp_anak", no_ktp_anak.getText().toString());

                                    params.put("tanggal_lahir_anak", ReverseconvertFormat(tgl_lahir_anak.getText().toString()));
                                    params.put("tempat_lahir_anak", tempat_lahir_anak.getText().toString());
                                    params.put("gol_darah_anak", golongan_darah_anak.getText().toString());
                                    params.put("agama_anak", agama_anak.getText().toString());

                                    params.put("suku_anak", suku_anak.getText().toString());
                                    if(wni_anak.isChecked()){
                                        params.put("kewarganegaraan_anak", "WNI");
                                    } else {
                                        params.put("kewarganegaraan_anak", "WNA");
                                    }

                                    params.put("pendidikan_anak", pendidikanterakhir_anak.getText().toString());

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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(data_keluarga.this);
                            requestQueue2.add(stringRequest2);
                        }

                    }
                });

                tgl_lahir_anak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar currentDate = Calendar.getInstance();
                        Calendar twoDaysAgo = (Calendar) currentDate.clone();
                        twoDaysAgo.add(Calendar.DATE, 0);

                        date = currentDate.getInstance();

                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth);

                                tgl_lahir_anak.setText(dateFormatter.format(date.getTime()));
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(data_keluarga.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(twoDaysAgo.getTimeInMillis());
                        datePickerDialog.show();
                    }
                });

            }
        });

        tgllahirkeluarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                Calendar twoDaysAgo = (Calendar) currentDate.clone();
                twoDaysAgo.add(Calendar.DATE, 0);

                date = currentDate.getInstance();

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);

                        tgllahirkeluarga.setText(dateFormatter.format(date.getTime()));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(data_keluarga.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(twoDaysAgo.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        tgllahirayah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                Calendar twoDaysAgo = (Calendar) currentDate.clone();
                twoDaysAgo.add(Calendar.DATE, 0);

                date = currentDate.getInstance();

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);

                        tgllahirayah.setText(dateFormatter.format(date.getTime()));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(data_keluarga.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(twoDaysAgo.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        tgllahiribu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                Calendar twoDaysAgo = (Calendar) currentDate.clone();
                twoDaysAgo.add(Calendar.DATE, 0);

                date = currentDate.getInstance();

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);

                        tgllahiribu.setText(dateFormatter.format(date.getTime()));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(data_keluarga.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(twoDaysAgo.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noKtpKeluarga.getText().toString().length() == 0){
                    ubah.setEnabled(false);
                    updateDataSuamiIstri();
                } else if (noKtpKeluarga.getText().toString().length() < 16){
                    noKtpKeluarga.setError("Digit KTP Maksimal 16");
                } else if (nomor_darurat.getText().toString().length() == 0){
                    ubah.setEnabled(false);
                    updateDataSuamiIstri();
                } else if (nomor_darurat.getText().toString().length() != 0){
                    if(nomor_darurat.getText().toString().length() < 10){
                        nomor_darurat.setError("Digit Nomor Telepon minimal 10");
                    } else if(nomor_darurat.getText().toString().length() > 13){
                        nomor_darurat.setError("Digit Nomor Telepon maksimal 13");
                    }else {
                        ubah.setEnabled(false);
                        updateDataSuamiIstri();
                    }
                } else {
                    ubah.setEnabled(false);
                    updateDataSuamiIstri();
                }

            }
        });

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        toolbar.setTitle("Data Keluarga");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });
        setNavigationDrawer();


    }

    private void setNavigationDrawer() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        dLayout = (DrawerLayout) findViewById(R.id.dl_datakeluarga); // initiate a DrawerLayout
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
                            data_keluarga.this);
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
                                    Intent intent = new Intent(data_keluarga.this, MainActivity.class);
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


    private void updateDataSuamiIstri() {
        pDialog = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_suamiistri_NoUrut",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        updateKeluarga();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        updateKeluarga();
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
                params.put("no_urut", noUrut);

                params.put("nama_keluarga", nama_lengkap_keluarga.getText().toString());
                params.put("no_ktp_keluarga", noKtpKeluarga.getText().toString());
                params.put("tanggal_lahir_keluarga", ReverseconvertFormat(tgllahirkeluarga.getText().toString()));
                params.put("tempat_lahir_keluarga", tempatlahirkeluarga.getText().toString());
                params.put("gol_darah_keluarga", golongandarahkeluarga.getText().toString());

                params.put("agama_keluarga", agamakeluarga.getText().toString());
                params.put("suku_keluarga", sukukeluarga.getText().toString());
                if(wni.isChecked()){
                    params.put("kewarganegaraan_keluarga", "WNI");
                } else if (wna.isChecked()) {
                    params.put("kewarganegaraan_keluarga", "WNA");
                } else {
                    params.put("kewarganegaraan_keluarga", "");
                }
                params.put("pendidikan_keluarga", pendidikanterakhir_keluarga.getText().toString());

                return params;
            }

        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(data_keluarga.this);
        requestQueue.add(stringRequest);

    }

    private void updateKeluarga() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_susunan_keluarga_NoUrut",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        updateDarurat();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        updateDarurat();
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
                params.put("no_urut", noUrut);

                params.put("nama_ayah", namaayah.getText().toString());
                params.put("tanggal_lahir_ayah", ReverseconvertFormat(tgllahirayah.getText().toString()));

                if(lakiAyah.isChecked()){
                    params.put("jenis_kelamin_ayah", "Laki - Laki");
                } else {
                    params.put("jenis_kelamin_ayah", "Perempuan");
                }


                params.put("pekerjaan_ayah", pekerjaaanAyah.getText().toString());
                params.put("pendidikan_ayah", pendidikanayah.getText().toString());

                params.put("nama_ibu", namaibu.getText().toString());
                params.put("tanggal_lahir_ibu", ReverseconvertFormat(tgllahiribu.getText().toString()));
                if(lakiIbu.isChecked()){
                    params.put("jenis_kelamin_ibu", "Laki - Laki");
                } else {
                    params.put("jenis_kelamin_ibu", "Perempuan");
                }
                params.put("pekerjaan_ibu", pekerjaaanibu.getText().toString());
                params.put("pendidikan_ibu", pendidikanibu.getText().toString());

                return params;
            }

        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(data_keluarga.this);
        requestQueue.add(stringRequest);
    }

    private void updateDarurat() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_darurat_NoUrut",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        Success = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.SUCCESS_TYPE);
                        Success.setContentText("Data Sudah Diupdate");
                        Success.setCancelable(false);
                        Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                            }
                        });
                        Success.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        Success = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.SUCCESS_TYPE);
                        Success.setContentText("Data Sudah Diupdate");
                        Success.setCancelable(false);
                        Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                            }
                        });
                        Success.show();
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
                params.put("no_urut", noUrut);

                params.put("nama_darurat", nama_lengkap_darurat.getText().toString());
                params.put("no_hp_darurat", nomor_darurat.getText().toString());
                params.put("alamat_darurat", alamat_darurat.getText().toString());


                return params;
            }

        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(data_keluarga.this);
        requestQueue.add(stringRequest);
    }

    private void getDarurat(String nik_baru) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_darurat_NoUrut?no_urut=" + noUrut,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);


                                nama_lengkap_darurat.setText(movieObject.getString("nama_darurat"));
                                nomor_darurat.setText(movieObject.getString("no_hp_darurat"));
                                alamat_darurat.setText(movieObject.getString("alamat_darurat"));







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

    private void getSusunanKeluarga(String nik_baru) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_susunanKeluarga_NoUrut?no_urut=" + noUrut,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                namaayah.setText(movieObject.getString("nama_ayah"));

                                if(movieObject.getString("tanggal_lahir_ayah").equals("0000-00-00")){
                                    tgllahirayah.setText("0000-00-00");
                                } else {
                                    tgllahirayah.setText(convertFormat(movieObject.getString("tanggal_lahir_ayah")));

                                }

                                if(movieObject.getString("jenis_kelamin_ayah").equals("Laki - Laki")){
                                    lakiAyah.setChecked(true);
                                } else {
                                    PerempuanAyah.setChecked(true);
                                }

                                pekerjaaanAyah.setText(movieObject.getString("pekerjaan_ayah"));
                                pendidikanayah.setText(movieObject.getString("pendidikan_ayah"));

                                namaibu.setText(movieObject.getString("nama_ibu"));
                                if(movieObject.getString("tanggal_lahir_ibu").equals("0000-00-00")){
                                    tgllahiribu.setText("0000-00-00");
                                } else {
                                    tgllahiribu.setText(convertFormat(movieObject.getString("tanggal_lahir_ibu")));
                                }

                                if(movieObject.getString("jenis_kelamin_ibu").equals("Laki - Laki")){
                                    lakiIbu.setChecked(true);
                                } else {
                                    PerempuanIbu.setChecked(true);
                                }

                                pekerjaaanibu.setText(movieObject.getString("pekerjaan_ibu"));
                                pendidikanibu.setText(movieObject.getString("pendidikan_ibu"));







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

    private void getAnak(String nik_baru) {
        adapter = new ListViewAnakAdapter(anakModelList, getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_anak_NoUrut?no_urut=" + noUrut,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        anaklist.setVisibility(View.VISIBLE);
                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final anakModel movieItem = new anakModel(
                                        movieObject.getString("id_anak"),
                                        movieObject.getString("urutan_anak"),
                                        movieObject.getString("nama_anak"),
                                        movieObject.getString("no_ktp_anak"),
                                        convertFormat(movieObject.getString("tanggal_lahir_anak")),
                                        movieObject.getString("tempat_lahir_anak"),
                                        movieObject.getString("gol_darah_anak"),
                                        movieObject.getString("agama_anak"),
                                        movieObject.getString("suku_anak"),
                                        movieObject.getString("kewarganegaraan_anak"),
                                        movieObject.getString("pendidikan_anak"));

                                anakModelList.add(movieItem);

                                anaklist.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                Utility.setListViewHeightBasedOnChildren(anaklist);



                            }



                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        anaklist.setVisibility(GONE);

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
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(data_keluarga.this);
        requestQueue.add(stringRequest);
    }

    private void getSuamiIstri(String nik_baru) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_suamiistri_NoUrut?no_urut=" + noUrut,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                nama_lengkap_keluarga.setText(movieObject.getString("nama_keluarga"));
                                noKtpKeluarga.setText(movieObject.getString("no_ktp_keluarga"));

                                if(movieObject.getString("tanggal_lahir_keluarga").equals("0000-00-00")){
                                    tgllahirkeluarga.setText("0000-00-00");
                                } else {
                                    tgllahirkeluarga.setText(convertFormat(movieObject.getString("tanggal_lahir_keluarga")));

                                }
                                tempatlahirkeluarga.setText(movieObject.getString("tempat_lahir_keluarga"));
                                sukukeluarga.setText(movieObject.getString("suku_keluarga"));

                                if(movieObject.getString("kewarganegaraan_keluarga").equals("WNI")){
                                    wni.setChecked(true);
                                } else if (movieObject.getString("kewarganegaraan_keluarga").equals("WNA")) {
                                    wna.setChecked(true);
                                }

                                golongandarahkeluarga.setText(movieObject.getString("gol_darah_keluarga"));
                                agamakeluarga.setText(movieObject.getString("agama_keluarga"));
                                pendidikanterakhir_keluarga.setText(movieObject.getString("pendidikan_keluarga"));

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

    public class ListViewAnakAdapter extends ArrayAdapter<anakModel> {

        private class ViewHolder {
            TextView anakke, namaanak, ktpanak,
                    tgllahiranak, tempatlahiranak, golongandarahanak, agamaanak,
                    sukuanak, kewarganegaraananak, pendidikananak;

            Button hapusdata, updateData;
        }

        List<anakModel> anakModelList;
        private final Context context;

        public ListViewAnakAdapter(List<anakModel> anakModelList, Context context) {
            super(context, R.layout.list_item_anak, anakModelList);
            this.anakModelList = anakModelList;
            this.context = context;

        }

        public int getCount() {
            return anakModelList.size();
        }

        @Override
        public int getViewTypeCount() {
            int count;
            if (anakModelList.size() > 0) {
                count = getCount();
            } else {
                count = 1;
            }
            return count;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public long getItemId(int position) {
            return 0;
        }


        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            final anakModel movieItem = anakModelList.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_anak, parent, false);

                viewHolder.anakke = convertView.findViewById(R.id.anakke);
                viewHolder.namaanak = convertView.findViewById(R.id.namaanak);
                viewHolder.ktpanak = convertView.findViewById(R.id.ktpanak);

                viewHolder.tgllahiranak = convertView.findViewById(R.id.tgllahiranak);
                viewHolder.tempatlahiranak = convertView.findViewById(R.id.tempatlahiranak);
                viewHolder.golongandarahanak = convertView.findViewById(R.id.golongandarahanak);
                viewHolder.agamaanak = convertView.findViewById(R.id.agamaanak);

                viewHolder.sukuanak = convertView.findViewById(R.id.sukuanak);
                viewHolder.kewarganegaraananak = convertView.findViewById(R.id.kewarganegaraananak);
                viewHolder.pendidikananak = convertView.findViewById(R.id.pendidikananak);

                viewHolder.hapusdata = convertView.findViewById(R.id.hapusdata);
                viewHolder.updateData = convertView.findViewById(R.id.updateData);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.anakke.setText(movieItem.getUrutan_anak());
            viewHolder.namaanak.setText(movieItem.getNama_anak());
            viewHolder.ktpanak.setText(movieItem.getNo_ktp_anak());

            viewHolder.tgllahiranak.setText(movieItem.getTanggal_lahir_anak());
            viewHolder.tempatlahiranak.setText(movieItem.getTempat_lahir_anak());
            viewHolder.golongandarahanak.setText(movieItem.getGol_darah_anak());
            viewHolder.agamaanak.setText(movieItem.getAgama_anak());

            viewHolder.sukuanak.setText(movieItem.getSuku_anak());
            viewHolder.kewarganegaraananak.setText(movieItem.getKewarganegaraan_anak());
            viewHolder.pendidikananak.setText(movieItem.getPendidikan_anak());

            viewHolder.hapusdata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.hapusdata.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.hapusdata.setEnabled(true);

                        }
                    }, 3000);
                    hapusAnak(movieItem.getId_anak());
                }
            });

            viewHolder.updateData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(data_keluarga.this);
                    dialog.setContentView(R.layout.data_anak);
                    dialog.setCancelable(false);
                    dialog.show();

                    final EditText urutananak = dialog.findViewById(R.id.urutananak);
                    final EditText nama_lengkap_anak = dialog.findViewById(R.id.nama_lengkap_anak);
                    final EditText no_ktp_anak = dialog.findViewById(R.id.no_ktp_anak);
                    final EditText tgl_lahir_anak = dialog.findViewById(R.id.tgl_lahir_anak);
                    final EditText tempat_lahir_anak = dialog.findViewById(R.id.tempat_lahir_anak);

                    final AutoCompleteTextView golongan_darah_anak = dialog.findViewById(R.id.golongan_darah_anak);
                    final AutoCompleteTextView agama_anak = dialog.findViewById(R.id.agama_anak);
                    final EditText suku_anak = dialog.findViewById(R.id.suku_anak);

                    final RadioButton wni_anak = dialog.findViewById(R.id.wni_anak);
                    RadioButton wna_anak = dialog.findViewById(R.id.wna_anak);

                    final AutoCompleteTextView pendidikanterakhir_anak = dialog.findViewById(R.id.pendidikanterakhir_anak);
                    MaterialButton batal = dialog.findViewById(R.id.batal);
                    final MaterialButton tambah = dialog.findViewById(R.id.tambah);

                    golongan_darah_anak.setAdapter(new ArrayAdapter<String>(data_keluarga.this, android.R.layout.simple_dropdown_item_1line, golongandarah));
                    agama_anak.setAdapter(new ArrayAdapter<String>(data_keluarga.this, android.R.layout.simple_dropdown_item_1line, agama));
                    pendidikanterakhir_anak.setAdapter(new ArrayAdapter<String>(data_keluarga.this, android.R.layout.simple_dropdown_item_1line, pendidikan));

                    golongan_darah_anak.setThreshold(100);
                    agama_anak.setThreshold(100);
                    pendidikanterakhir_anak.setThreshold(100);


                    urutananak.setText(movieItem.getUrutan_anak());
                    nama_lengkap_anak.setText(movieItem.getNama_anak());
                    no_ktp_anak.setText(movieItem.getNo_ktp_anak());
                    tgl_lahir_anak.setText(movieItem.getTanggal_lahir_anak());
                    tempat_lahir_anak.setText(movieItem.getTempat_lahir_anak());

                    pendidikanterakhir_anak.setText(movieItem.getPendidikan_anak());


                    golongan_darah_anak.setText(movieItem.getGol_darah_anak());
                    agama_anak.setText(movieItem.getAgama_anak());
                    suku_anak.setText(movieItem.getSuku_anak());

                    if(movieItem.getKewarganegaraan_anak().equals("WNI")){
                        wni_anak.setChecked(true);
                    } else {
                        wna_anak.setChecked(true);
                    }

                    tambah.setText("Ubah Update");

                    batal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    tambah.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(no_ktp_anak.getText().toString().length() == 0 || no_ktp_anak.getText().toString().equals("-")){
                                tambah.setEnabled(false);
                                pDialog = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Harap Menunggu");
                                pDialog.setCancelable(false);
                                pDialog.show();

                                final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_anak",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                pDialog.dismissWithAnimation();
                                                Success = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.SUCCESS_TYPE);
                                                Success.setContentText("Data Sudah Diupdate");
                                                Success.setCancelable(false);
                                                Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                                                        getAnak(nik_baru);
                                                        dialog.dismiss();

                                                        adapter.clear();
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                });
                                                Success.show();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                pDialog.dismissWithAnimation();
                                                Success = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.SUCCESS_TYPE);
                                                Success.setContentText("Data Sudah Diupdate");
                                                Success.setCancelable(false);
                                                Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                                                        getAnak(nik_baru);
                                                        dialog.dismiss();

                                                        adapter.clear();
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                });
                                                Success.show();
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

                                        params.put("id_anak", movieItem.getId_anak());

                                        params.put("nik_baru", nik_baru);
                                        params.put("urutan_anak", urutananak.getText().toString());
                                        params.put("nama_anak", nama_lengkap_anak.getText().toString());
                                        params.put("no_ktp_anak", no_ktp_anak.getText().toString());

                                        params.put("tanggal_lahir_anak", ReverseconvertFormat(tgl_lahir_anak.getText().toString()));
                                        params.put("tempat_lahir_anak", tempat_lahir_anak.getText().toString());
                                        params.put("gol_darah_anak", golongan_darah_anak.getText().toString());
                                        params.put("agama_anak", agama_anak.getText().toString());

                                        params.put("suku_anak", suku_anak.getText().toString());
                                        if(wni_anak.isChecked()){
                                            params.put("kewarganegaraan_anak", "WNI");
                                        } else {
                                            params.put("kewarganegaraan_anak", "WNA");
                                        }

                                        params.put("pendidikan_anak", pendidikanterakhir_anak.getText().toString());

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
                                RequestQueue requestQueue2 = Volley.newRequestQueue(data_keluarga.this);
                                requestQueue2.add(stringRequest2);
                            } else if(no_ktp_anak.getText().toString().length() < 16){
                                no_ktp_anak.setError("Digit KTP Maksimal 16");
                            } else {
                                tambah.setEnabled(false);
                                pDialog = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Harap Menunggu");
                                pDialog.setCancelable(false);
                                pDialog.show();

                                final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_anak",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                pDialog.dismissWithAnimation();
                                                Success = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.SUCCESS_TYPE);
                                                Success.setContentText("Data Sudah Diupdate");
                                                Success.setCancelable(false);
                                                Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                                                        getAnak(nik_baru);
                                                        dialog.dismiss();

                                                        adapter.clear();
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                });
                                                Success.show();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                pDialog.dismissWithAnimation();
                                                Success = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.SUCCESS_TYPE);
                                                Success.setContentText("Data Sudah Diupdate");
                                                Success.setCancelable(false);
                                                Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                                                        getAnak(nik_baru);
                                                        dialog.dismiss();

                                                        adapter.clear();
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                });
                                                Success.show();
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

                                        params.put("id_anak", movieItem.getId_anak());

                                        params.put("nik_baru", nik_baru);
                                        params.put("urutan_anak", urutananak.getText().toString());
                                        params.put("nama_anak", nama_lengkap_anak.getText().toString());
                                        params.put("no_ktp_anak", no_ktp_anak.getText().toString());

                                        params.put("tanggal_lahir_anak", ReverseconvertFormat(tgl_lahir_anak.getText().toString()));
                                        params.put("tempat_lahir_anak", tempat_lahir_anak.getText().toString());
                                        params.put("gol_darah_anak", golongan_darah_anak.getText().toString());
                                        params.put("agama_anak", agama_anak.getText().toString());

                                        params.put("suku_anak", suku_anak.getText().toString());
                                        if(wni_anak.isChecked()){
                                            params.put("kewarganegaraan_anak", "WNI");
                                        } else {
                                            params.put("kewarganegaraan_anak", "WNA");
                                        }

                                        params.put("pendidikan_anak", pendidikanterakhir_anak.getText().toString());

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
                                RequestQueue requestQueue2 = Volley.newRequestQueue(data_keluarga.this);
                                requestQueue2.add(stringRequest2);
                            }
                        }
                    });

                    tgl_lahir_anak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Calendar currentDate = Calendar.getInstance();
                            Calendar twoDaysAgo = (Calendar) currentDate.clone();
                            twoDaysAgo.add(Calendar.DATE, 0);

                            date = currentDate.getInstance();

                            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                    date.set(year, monthOfYear, dayOfMonth);

                                    tgl_lahir_anak.setText(dateFormatter.format(date.getTime()));
                                }
                            };
                            DatePickerDialog datePickerDialog = new DatePickerDialog(data_keluarga.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                            datePickerDialog.getDatePicker().setMaxDate(twoDaysAgo.getTimeInMillis());
                            datePickerDialog.show();
                        }
                    });

                }
            });







            return convertView;
        }
    }

    private void hapusAnak(final String id_anak) {
        pDialog = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_hapusAnak",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        Success = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.SUCCESS_TYPE);
                        Success.setContentText("Data Sudah Diupdate");
                        Success.setCancelable(false);
                        Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                                getAnak(nik_baru);

                                adapter.clear();
                                adapter.notifyDataSetChanged();
                            }
                        });
                        Success.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        Success = new SweetAlertDialog(data_keluarga.this, SweetAlertDialog.SUCCESS_TYPE);
                        Success.setContentText("Data Sudah Diupdate");
                        Success.setCancelable(false);
                        Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                                getAnak(nik_baru);

                                adapter.clear();
                                adapter.notifyDataSetChanged();
                            }
                        });
                        Success.show();
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

                params.put("id_anak", id_anak);
                params.put("nik_baru", nik_baru + "X");


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

    public static String ReverseconvertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
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
}