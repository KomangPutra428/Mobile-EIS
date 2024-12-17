package com.example.eis2;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.biodata.noUrut;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class data_pendidikan extends AppCompatActivity {
    RadioButton normalSd, paketSd, lulusSd, tidaklulusSd;
    EditText namaSekolahSd, tahunSd, NilaiRataSd;
    SharedPreferences sharedPreferences;

    RadioButton normalSMP, paketSMP, lulusSMP, tidaklulusSMP;
    EditText namaSekolahSMP, tahunSMP, NilaiRataSMP;
    SweetAlertDialog pDialog, Success;

    String URL;

    DrawerLayout dLayout;


    RadioButton normalSMA, paketSMA, lulusSMA, tidaklulusSMA;
    EditText namaSekolahSMA, jurusanSMA, tahunSMA, NilaiRataSMA;

    LinearLayout diploma, s1, s2, s3;

    TextView nama_st, jurusan_st, tahun_st, keterangan_st, ipk_st, tingkat_st;
    TextView nama_s1, jurusan_s1, tahun_s1, keterangan_s1, ipk_s1, tingkat_s1;
    TextView nama_s2, jurusan_s2, tahun_s2, keterangan_s2, ipk_s2, tingkat_s2;
    TextView nama_s3, jurusan_s3, tahun_s3, keterangan_s3, ipk_s3, tingkat_s3;

    MaterialButton batal, ubah, addPendidikan;

    Button hapusdiploma, hapuss1, hapuss2, hapuss3;
    Button Updatediploma, Updates1, Updates2, Updates3;

    Context context;
    ArrayList<String> pendidikan = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pendidikan);
        batal = findViewById(R.id.batal);
        ubah = findViewById(R.id.ubah);

        normalSd = findViewById(R.id.normalSd);
        paketSd = findViewById(R.id.paketSd);
        lulusSd = findViewById(R.id.lulusSd);
        tidaklulusSd = findViewById(R.id.tidaklulusSd);

        namaSekolahSd = findViewById(R.id.namaSekolahSd);
        tahunSd = findViewById(R.id.tahunSd);
        NilaiRataSd = findViewById(R.id.NilaiRataSd);

        normalSMP = findViewById(R.id.normalSMP);
        paketSMP = findViewById(R.id.paketSMP);
        lulusSMP = findViewById(R.id.lulusSMP);
        tidaklulusSMP = findViewById(R.id.tidaklulusSMP);

        namaSekolahSMP = findViewById(R.id.namaSekolahSMP);
        tahunSMP = findViewById(R.id.tahunSMP);
        NilaiRataSMP = findViewById(R.id.NilaiRataSMP);

        normalSMA = findViewById(R.id.normalSMA);
        paketSMA = findViewById(R.id.paketSMA);
        lulusSMA = findViewById(R.id.lulusSMA);
        tidaklulusSMA = findViewById(R.id.tidaklulusSMA);

        namaSekolahSMA = findViewById(R.id.namaSekolahSMA);
        jurusanSMA = findViewById(R.id.jurusanSMA);
        tahunSMA = findViewById(R.id.tahunSMA);
        NilaiRataSMA = findViewById(R.id.NilaiRataSMA);

        diploma = findViewById(R.id.diploma);
        nama_st = findViewById(R.id.nama_st);
        jurusan_st = findViewById(R.id.jurusan_st);
        tahun_st = findViewById(R.id.tahun_st);
        keterangan_st = findViewById(R.id.keterangan_st);
        ipk_st = findViewById(R.id.ipk_st);
        tingkat_st = findViewById(R.id.tingkat_st);


        s1 = findViewById(R.id.s1);
        nama_s1 = findViewById(R.id.nama_s1);
        jurusan_s1 = findViewById(R.id.jurusan_s1);
        tahun_s1 = findViewById(R.id.tahun_s1);
        keterangan_s1 = findViewById(R.id.keterangan_s1);
        ipk_s1 = findViewById(R.id.ipk_s1);
        tingkat_s1 = findViewById(R.id.tingkat_s1);

        s2 = findViewById(R.id.s2);
        nama_s2 = findViewById(R.id.nama_s2);
        jurusan_s2 = findViewById(R.id.jurusan_s2);
        tahun_s2 = findViewById(R.id.tahun_s2);
        keterangan_s2 = findViewById(R.id.keterangan_s2);
        ipk_s2 = findViewById(R.id.ipk_s2);
        tingkat_s2 = findViewById(R.id.tingkat_s2);

        s3 = findViewById(R.id.s3);
        nama_s3 = findViewById(R.id.nama_s3);
        jurusan_s3 = findViewById(R.id.jurusan_s3);
        tahun_s3 = findViewById(R.id.tahun_s3);
        keterangan_s3 = findViewById(R.id.keterangan_s3);
        ipk_s3 = findViewById(R.id.ipk_s3);
        tingkat_s3 = findViewById(R.id.tingkat_s3);

        hapusdiploma = findViewById(R.id.hapusdiploma);
        hapuss1 = findViewById(R.id.hapuss1);
        hapuss2 = findViewById(R.id.hapuss2);
        hapuss3 = findViewById(R.id.hapuss3);

        Updatediploma = findViewById(R.id.Updatediploma);
        Updates1 = findViewById(R.id.Updates1);
        Updates2 = findViewById(R.id.Updates2);
        Updates3 = findViewById(R.id.Updates3);

        addPendidikan = findViewById(R.id.addPendidikan);

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        toolbar.setTitle("Data Pendidikan");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });
        setNavigationDrawer();




        tahunSd.setFocusable(false);
        tahunSd.setLongClickable(false);
        tahunSd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YearPickerDialog yearPickerDialog = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                    @Override
                    public void onYearSelected(int year) {
                        TahunLulusSd(String.valueOf(year));
                    }

                    private void TahunLulusSd(final String sdGabung) {
                        YearPickerDialog yearPickerDialog2 = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                            @Override
                            public void onYearSelected(int year) {
                                if(Integer.parseInt(sdGabung) >= year){
                                    new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.WARNING_TYPE)
                                            .setContentText("Tahun Masuk Tidak Boleh Lebih Besar Dari Tahun Lulus")
                                            .setConfirmText("OK")
                                            .show();
                                } else {
                                    tahunSd.setText(sdGabung + " - " + String.valueOf(year));
                                }
                            }
                        });
                        yearPickerDialog2.show();
                        yearPickerDialog2.keterangan.setText("Tahun Lulus SD");

                    }
                });
                yearPickerDialog.show();
                yearPickerDialog.keterangan.setText("Tahun Masuk SD");


            }
        });


        tahunSMP.setFocusable(false);
        tahunSMP.setLongClickable(false);
        tahunSMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YearPickerDialog yearPickerDialog = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                    @Override
                    public void onYearSelected(int year) {
                        TahunLulusSMP(String.valueOf(year));
                    }

                    private void TahunLulusSMP(final String sdGabung) {
                        YearPickerDialog yearPickerDialog2 = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                            @Override
                            public void onYearSelected(int year) {
                                if(Integer.parseInt(sdGabung) >= year){
                                    new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.WARNING_TYPE)
                                            .setContentText("Tahun Masuk Tidak Boleh Lebih Besar Dari Tahun Lulus")
                                            .setConfirmText("OK")
                                            .show();
                                } else {
                                    tahunSMP.setText(sdGabung + " - " + String.valueOf(year));
                                }
                            }
                        });
                        yearPickerDialog2.show();
                        yearPickerDialog2.keterangan.setText("Tahun Lulus SMP");

                    }
                });
                yearPickerDialog.show();
                yearPickerDialog.keterangan.setText("Tahun Masuk SMP");


            }
        });


        tahunSMA.setFocusable(false);
        tahunSMA.setLongClickable(false);
        tahunSMA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YearPickerDialog yearPickerDialog = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                    @Override
                    public void onYearSelected(int year) {
                        TahunLulusSMA(String.valueOf(year));
                    }

                    private void TahunLulusSMA(final String sdGabung) {
                        YearPickerDialog yearPickerDialog2 = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                            @Override
                            public void onYearSelected(int year) {
                                if(Integer.parseInt(sdGabung) >= year){
                                    new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.WARNING_TYPE)
                                            .setContentText("Tahun Masuk Tidak Boleh Lebih Besar Dari Tahun Lulus")
                                            .setConfirmText("OK")
                                            .show();
                                } else {
                                    tahunSMA.setText(sdGabung + " - " + String.valueOf(year));
                                }
                            }
                        });
                        yearPickerDialog2.show();
                        yearPickerDialog2.keterangan.setText("Tahun Lulus SMA");

                    }
                });
                yearPickerDialog.show();
                yearPickerDialog.keterangan.setText("Tahun Masuk SMA");


            }
        });


        addPendidikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(data_pendidikan.this);
                dialog.setContentView(R.layout.data_institusi);
                dialog.setCancelable(false);
                dialog.show();

                final AutoCompleteTextView tingkatan = dialog.findViewById(R.id.tingkatan);
                final EditText nama_institusi = dialog.findViewById(R.id.nama_institusi);
                final EditText jurusan_institusi = dialog.findViewById(R.id.jurusan_institusi);

                final EditText tahun_lulus = dialog.findViewById(R.id.tahun_lulus);

                final RadioButton lulus_institusi = dialog.findViewById(R.id.lulus_institusi);
                RadioButton tidak_lulus_institusi = dialog.findViewById(R.id.tidak_lulus_institusi);

                final EditText nilai_ipk = dialog.findViewById(R.id.nilai_ipk);
                final EditText tingkat_institusi = dialog.findViewById(R.id.tingkat_institusi);

                MaterialButton batal = dialog.findViewById(R.id.batal);
                MaterialButton simpan = dialog.findViewById(R.id.simpan);

                tahun_lulus.setFocusable(false);
                tahun_lulus.setLongClickable(false);
                tahun_lulus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YearPickerDialog yearPickerDialog = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                            @Override
                            public void onYearSelected(int year) {
                                TahunLulusKuliah(String.valueOf(year));
                            }

                            private void TahunLulusKuliah(final String sdGabung) {
                                YearPickerDialog yearPickerDialog2 = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                                    @Override
                                    public void onYearSelected(int year) {
                                        if(Integer.parseInt(sdGabung) >= year){
                                            new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setContentText("Tahun Masuk Tidak Boleh Lebih Besar Dari Tahun Lulus")
                                                    .setConfirmText("OK")
                                                    .show();
                                        } else {
                                            tahun_lulus.setText(sdGabung + " - " + String.valueOf(year));
                                        }
                                    }
                                });
                                yearPickerDialog2.show();
                                yearPickerDialog2.keterangan.setText("Tahun Lulus Kuliah");

                            }
                        });
                        yearPickerDialog.show();
                        yearPickerDialog.keterangan.setText("Tahun Masuk Kuliah");


                    }
                });

                tingkatan.setAdapter(new ArrayAdapter<String>(data_pendidikan.this, android.R.layout.simple_dropdown_item_1line, pendidikan));

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                simpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(tingkatan.getText().toString().length() == 0){
                            tingkatan.setError("Masukkan Tingkatan");
                        } else {
                            pDialog = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Harap Menunggu");
                            pDialog.setCancelable(false);
                            pDialog.show();

                            if(tingkatan.getText().toString().equals("Diploma")){
                                URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_diploma";
                            } else if(tingkatan.getText().toString().equals("S1")){
                                URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s1_NoUrut";
                            } else if(tingkatan.getText().toString().equals("S2")){
                                URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s2_NoUrut";
                            } else if(tingkatan.getText().toString().equals("S3")){
                                URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s3_NoUrut";
                            }

                            StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            pDialog.dismissWithAnimation();
                                            Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                            Success.setContentText("Data Sudah Diupdate");
                                            Success.setCancelable(false);
                                            Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    dialog.dismiss();
                                                    getUniv();
                                                }
                                            });
                                            Success.show();


                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            pDialog.dismissWithAnimation();
                                            Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                            Success.setContentText("Data Sudah Diupdate");
                                            Success.setCancelable(false);
                                            Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    dialog.dismiss();
                                                    getUniv();
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

                                    if(tingkatan.getText().toString().equals("Diploma")){

                                        params.put("nik_baru", nik_baru);
                                        params.put("no_urut", noUrut);
                                        params.put("nama_st", nama_institusi.getText().toString());
                                        params.put("jurusan_st", jurusan_institusi.getText().toString());
                                        params.put("tahun_st", tahun_lulus.getText().toString());
                                        if(lulus_institusi.isChecked()){
                                            params.put("ket_st", "Lulus");
                                        } else if (tidak_lulus_institusi.isChecked()){
                                            params.put("ket_st", "Tidak Lulus");
                                        } else {
                                            params.put("ket_st", "");
                                        }
                                        params.put("ipk_st", nilai_ipk.getText().toString());
                                        params.put("tingkat_st", tingkatan.getText().toString());

                                    } else if(tingkatan.getText().toString().equals("S1")){

                                        params.put("nik_baru", nik_baru);
                                        params.put("no_urut", noUrut);
                                        params.put("nama_s1", nama_institusi.getText().toString());
                                        params.put("jurusan_s1", jurusan_institusi.getText().toString());
                                        params.put("tahun_s1", tahun_lulus.getText().toString());

                                        if(lulus_institusi.isChecked()){
                                            params.put("ket_s1", "Lulus");
                                        } else if (tidak_lulus_institusi.isChecked()){
                                            params.put("ket_s1", "Tidak Lulus");
                                        } else {
                                            params.put("ket_s1", "");
                                        }
                                        params.put("ipk_s1", nilai_ipk.getText().toString());
                                        params.put("tingkat_s1", tingkatan.getText().toString());

                                    } else if(tingkatan.getText().toString().equals("S2")){

                                        params.put("nik_baru", nik_baru);
                                        params.put("no_urut", noUrut);
                                        params.put("nama_s2", nama_institusi.getText().toString());
                                        params.put("jurusan_s2", jurusan_institusi.getText().toString());
                                        params.put("tahun_s2", tahun_lulus.getText().toString());

                                        if(lulus_institusi.isChecked()){
                                            params.put("ket_s2", "Lulus");
                                        } else if (tidak_lulus_institusi.isChecked()){
                                            params.put("ket_s2", "Tidak Lulus");
                                        } else {
                                            params.put("ket_s2", "");
                                        }

                                        params.put("ipk_s2", nilai_ipk.getText().toString());
                                        params.put("tingkat_s2", tingkatan.getText().toString());

                                    } else if(tingkatan.getText().toString().equals("S3")){

                                        params.put("nik_baru", nik_baru);
                                        params.put("no_urut", noUrut);
                                        params.put("nama_s3", nama_institusi.getText().toString());
                                        params.put("jurusan_s3", jurusan_institusi.getText().toString());
                                        params.put("tahun_s3", tahun_lulus.getText().toString());

                                        if(lulus_institusi.isChecked()){
                                            params.put("ket_s3", "Lulus");
                                        } else if (tidak_lulus_institusi.isChecked()){
                                            params.put("ket_s3", "Tidak Lulus");
                                        } else {
                                            params.put("ket_s3", "");
                                        }

                                        params.put("ipk_s3", nilai_ipk.getText().toString());
                                        params.put("tingkat_s3", tingkatan.getText().toString());

                                    }

                                    System.out.println("Params Pendidikan = " + params);




                                    return params;
                                }

                            };
                            stringRequest.setRetryPolicy(
                                    new DefaultRetryPolicy(
                                            500000,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                    ));
                            RequestQueue requestQueue = Volley.newRequestQueue(data_pendidikan.this);
                            requestQueue.add(stringRequest);
                        }
                    }
                });

            }
        });

        hapusdiploma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.setCancelable(false);
                pDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_diploma_NoUrut",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pDialog.dismissWithAnimation();
                                Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                Success.setContentText("Data Sudah Diupdate");
                                Success.setCancelable(false);
                                Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        getUniv();
                                    }
                                });
                                Success.show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismissWithAnimation();
                                Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                Success.setContentText("Data Sudah Diupdate");
                                Success.setCancelable(false);
                                Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        getUniv();
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
                        params.put("nama_st", "");
                        params.put("jurusan_st", "");
                        params.put("tahun_st", "");
                        params.put("ket_st", "");
                        params.put("ipk_st", "");
                        params.put("tingkat_st", "");


                        return params;
                    }

                };
                stringRequest.setRetryPolicy(
                        new DefaultRetryPolicy(
                                500000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        ));
                RequestQueue requestQueue = Volley.newRequestQueue(data_pendidikan.this);
                requestQueue.add(stringRequest);

            }
        });

        hapuss1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.setCancelable(false);
                pDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s1_NoUrut",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pDialog.dismissWithAnimation();
                                Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                Success.setContentText("Data Sudah Diupdate");
                                Success.setCancelable(false);
                                Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        getUniv();
                                    }
                                });
                                Success.show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismissWithAnimation();
                                Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                Success.setContentText("Data Sudah Diupdate");
                                Success.setCancelable(false);
                                Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        getUniv();
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
                        params.put("nama_s1", "");
                        params.put("jurusan_s1", "");
                        params.put("tahun_s1", "");
                        params.put("ket_s1", "");
                        params.put("ipk_s1", "");
                        params.put("tingkat_s1", "");


                        return params;
                    }

                };
                stringRequest.setRetryPolicy(
                        new DefaultRetryPolicy(
                                500000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        ));
                RequestQueue requestQueue = Volley.newRequestQueue(data_pendidikan.this);
                requestQueue.add(stringRequest);
            }
        });

        hapuss2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.setCancelable(false);
                pDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s2_NoUrut",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pDialog.dismissWithAnimation();
                                Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                Success.setContentText("Data Sudah Diupdate");
                                Success.setCancelable(false);
                                Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        getUniv();
                                    }
                                });
                                Success.show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismissWithAnimation();
                                Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                Success.setContentText("Data Sudah Diupdate");
                                Success.setCancelable(false);
                                Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        getUniv();
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
                        params.put("nama_s2", "");
                        params.put("jurusan_s2", "");
                        params.put("tahun_s2", "");
                        params.put("ket_s2", "");
                        params.put("ipk_s2", "");
                        params.put("tingkat_s2", "");


                        return params;
                    }

                };
                stringRequest.setRetryPolicy(
                        new DefaultRetryPolicy(
                                500000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        ));
                RequestQueue requestQueue = Volley.newRequestQueue(data_pendidikan.this);
                requestQueue.add(stringRequest);

            }
        });

        hapuss3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.setCancelable(false);
                pDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s3_NoUrut",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pDialog.dismissWithAnimation();
                                Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                Success.setContentText("Data Sudah Diupdate");
                                Success.setCancelable(false);
                                Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        getUniv();
                                    }
                                });
                                Success.show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismissWithAnimation();
                                Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                Success.setContentText("Data Sudah Diupdate");
                                Success.setCancelable(false);
                                Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        getUniv();
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
                        params.put("nama_s3", "");
                        params.put("jurusan_s3", "");
                        params.put("tahun_s3", "");
                        params.put("ket_s3", "");
                        params.put("ipk_s3", "");
                        params.put("tingkat_s3", "");


                        return params;
                    }

                };
                stringRequest.setRetryPolicy(
                        new DefaultRetryPolicy(
                                500000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        ));
                RequestQueue requestQueue = Volley.newRequestQueue(data_pendidikan.this);
                requestQueue.add(stringRequest);
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

                if(tahunSd.getText().toString().length() != 0){
                    String sd = tahunSd.getText().toString();
                    String[] splited_text = sd.split("-");
                    int gabungSd = Integer.parseInt(splited_text[0].trim());
                    int TamatSd = Integer.parseInt(splited_text[1].trim());
                    if(gabungSd >= TamatSd){
                        new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Tahun Masuk SD Tidak Boleh Lebih Besar Dari Tahun Lulus")
                                .setConfirmText("OK")
                                .show();
                    } else {
                        updatePendidikanAwal();
                    }
                } else if(tahunSMP.getText().toString().length() != 0){
                    String smp  = tahunSMP.getText().toString();
                    String[] splited_text2 = smp.split("-");
                    int gabungsmp = Integer.parseInt(splited_text2[0].trim());
                    int Tamatsmp = Integer.parseInt(splited_text2[1].trim());

                    if(gabungsmp >= Tamatsmp){
                        new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Tahun Masuk SMP Tidak Boleh Lebih Besar Dari Tahun Lulus")
                                .setConfirmText("OK")
                                .show();
                    } else {
                        updatePendidikanAwal();
                    }
                } else if(tahunSMA.getText().toString().length() != 0){
                    String sma = tahunSMA.getText().toString();
                    String[] splited_text3 = sma.split("-");
                    int gabungsma = Integer.parseInt(splited_text3[0].trim());
                    int Tamatsma = Integer.parseInt(splited_text3[1].trim());

                    if(gabungsma >= Tamatsma){
                        new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Tahun Masuk SMA Tidak Boleh Lebih Besar Dari Tahun Lulus")
                                .setConfirmText("OK")
                                .show();
                    } else {
                        updatePendidikanAwal();
                    }
                } else {
                    updatePendidikanAwal();
                }





            }
        });

        getPendidikan();
        getUniv();



    }

    private void getUniv() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        pendidikan.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_NoUrut?no_urut=" + noUrut,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                if(movieObject.getString("nama_st").equals("")){
                                    diploma.setVisibility(View.GONE);
                                } else {
                                    diploma.setVisibility(View.VISIBLE);
                                }

                                if(movieObject.getString("nama_s1").equals("")){
                                    s1.setVisibility(View.GONE);
                                } else {
                                    s1.setVisibility(View.VISIBLE);
                                }

                                if(movieObject.getString("nama_s2").equals("")){
                                    s2.setVisibility(View.GONE);
                                } else {
                                    s2.setVisibility(View.VISIBLE);
                                }

                                if(movieObject.getString("nama_s3").equals("")){
                                    s3.setVisibility(View.GONE);
                                } else {
                                    s3.setVisibility(View.VISIBLE);
                                }

                                if(movieObject.getString("nama_st").equals("")){
                                    pendidikan.add("Diploma");
                                }

                                if(movieObject.getString("nama_s1").equals("")){
                                    pendidikan.add("S1");
                                }

                                if(movieObject.getString("nama_s2").equals("")){
                                    pendidikan.add("S2");
                                }

                                if(movieObject.getString("nama_s3").equals("")){
                                    pendidikan.add("S3");
                                }

                                if(!movieObject.getString("nama_st").equals("") &&
                                        !movieObject.getString("nama_s1").equals("") &&
                                        !movieObject.getString("nama_s2").equals("") &&
                                        !movieObject.getString("nama_s3").equals("")){
                                    addPendidikan.setVisibility(View.GONE);
                                } else {
                                    addPendidikan.setVisibility(View.VISIBLE);
                                }

                                Updatediploma.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        {
                                            final Dialog dialog = new Dialog(data_pendidikan.this);
                                            dialog.setContentView(R.layout.data_institusi);
                                            dialog.setCancelable(false);
                                            dialog.show();

                                            TextInputLayout textinput_tingkatan = dialog.findViewById(R.id.textinput_tingkatan);

                                            final AutoCompleteTextView tingkatan = dialog.findViewById(R.id.tingkatan);
                                            final EditText nama_institusi = dialog.findViewById(R.id.nama_institusi);
                                            final EditText jurusan_institusi = dialog.findViewById(R.id.jurusan_institusi);

                                            final EditText tahun_lulus = dialog.findViewById(R.id.tahun_lulus);

                                            final RadioButton lulus_institusi = dialog.findViewById(R.id.lulus_institusi);
                                            RadioButton tidak_lulus_institusi = dialog.findViewById(R.id.tidak_lulus_institusi);

                                            final EditText nilai_ipk = dialog.findViewById(R.id.nilai_ipk);

                                            MaterialButton batal = dialog.findViewById(R.id.batal);
                                            MaterialButton simpan = dialog.findViewById(R.id.simpan);

                                            tingkatan.setEnabled(false);

                                            tingkatan.setText(tingkat_st.getText().toString());
                                            nama_institusi.setText(nama_st.getText().toString());
                                            jurusan_institusi.setText(jurusan_st.getText().toString());
                                            tahun_lulus.setText(tahun_st.getText().toString());


                                            textinput_tingkatan.setEndIconMode(TextInputLayout.END_ICON_NONE);


                                            if(keterangan_st.getText().toString().equals("Lulus")){
                                                lulus_institusi.setChecked(true);
                                            } else {
                                                tidak_lulus_institusi.setChecked(true);
                                            }
                                            nilai_ipk.setText(ipk_st.getText().toString());


                                            tahun_lulus.setFocusable(false);
                                            tahun_lulus.setLongClickable(false);
                                            tahun_lulus.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    YearPickerDialog yearPickerDialog = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                                                        @Override
                                                        public void onYearSelected(int year) {
                                                            TahunLulusKuliah(String.valueOf(year));
                                                        }

                                                        private void TahunLulusKuliah(final String sdGabung) {
                                                            YearPickerDialog yearPickerDialog2 = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                                                                @Override
                                                                public void onYearSelected(int year) {
                                                                    if(Integer.parseInt(sdGabung) >= year){
                                                                        new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.WARNING_TYPE)
                                                                                .setContentText("Tahun Masuk Tidak Boleh Lebih Besar Dari Tahun Lulus")
                                                                                .setConfirmText("OK")
                                                                                .show();
                                                                    } else {
                                                                        tahun_lulus.setText(sdGabung + " - " + String.valueOf(year));
                                                                    }
                                                                }
                                                            });
                                                            yearPickerDialog2.show();
                                                            yearPickerDialog2.keterangan.setText("Tahun Lulus Kuliah");

                                                        }
                                                    });
                                                    yearPickerDialog.show();
                                                    yearPickerDialog.keterangan.setText("Tahun Masuk Kuliah");


                                                }
                                            });

                                            tingkatan.setAdapter(new ArrayAdapter<String>(data_pendidikan.this, android.R.layout.simple_dropdown_item_1line, pendidikan));

                                            batal.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });

                                            simpan.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(tingkatan.getText().toString().length() == 0){
                                                        tingkatan.setError("Masukkan Tingkatan");
                                                    } else {
                                                        pDialog = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.PROGRESS_TYPE);
                                                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                        pDialog.setTitleText("Harap Menunggu");
                                                        pDialog.setCancelable(false);
                                                        pDialog.show();

                                                        if(tingkatan.getText().toString().equals("Diploma")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_diploma";
                                                        } else if(tingkatan.getText().toString().equals("S1")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s1_NoUrut";
                                                        } else if(tingkatan.getText().toString().equals("S2")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s2_NoUrut";
                                                        } else if(tingkatan.getText().toString().equals("S3")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s3_NoUrut";
                                                        }

                                                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        pDialog.dismissWithAnimation();
                                                                        Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                                                        Success.setContentText("Data Sudah Diupdate");
                                                                        Success.setCancelable(false);
                                                                        Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sDialog.dismissWithAnimation();
                                                                                getUniv();
                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                        Success.show();
                                                                    }
                                                                },
                                                                new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        pDialog.dismissWithAnimation();
                                                                        Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                                                        Success.setContentText("Data Sudah Diupdate");
                                                                        Success.setCancelable(false);
                                                                        Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sDialog.dismissWithAnimation();
                                                                                getUniv();
                                                                                dialog.dismiss();
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

                                                                if(tingkatan.getText().toString().equals("Diploma")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_st", nama_institusi.getText().toString());
                                                                    params.put("jurusan_st", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_st", tahun_lulus.getText().toString());
                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_st", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_st", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_st", "");
                                                                    }
                                                                    params.put("ipk_st", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_st", tingkatan.getText().toString());

                                                                } else if(tingkatan.getText().toString().equals("S1")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_s1", nama_institusi.getText().toString());
                                                                    params.put("jurusan_s1", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_s1", tahun_lulus.getText().toString());

                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_s1", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_s1", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_s1", "");
                                                                    }
                                                                    params.put("ipk_s1", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_s1", tingkatan.getText().toString());

                                                                } else if(tingkatan.getText().toString().equals("S2")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_s2", nama_institusi.getText().toString());
                                                                    params.put("jurusan_s2", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_s2", tahun_lulus.getText().toString());

                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_s2", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_s2", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_s2", "");
                                                                    }

                                                                    params.put("ipk_s2", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_s2", tingkatan.getText().toString());

                                                                } else if(tingkatan.getText().toString().equals("S3")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_s3", nama_institusi.getText().toString());
                                                                    params.put("jurusan_s3", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_s3", tahun_lulus.getText().toString());

                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_s3", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_s3", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_s3", "");
                                                                    }

                                                                    params.put("ipk_s3", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_s3", tingkatan.getText().toString());

                                                                }

                                                                System.out.println("Params Pendidikan = " + params);




                                                                return params;
                                                            }

                                                        };
                                                        stringRequest.setRetryPolicy(
                                                                new DefaultRetryPolicy(
                                                                        500000,
                                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                                ));
                                                        RequestQueue requestQueue = Volley.newRequestQueue(data_pendidikan.this);
                                                        requestQueue.add(stringRequest);
                                                    }
                                                }
                                            });

                                        }
                                    }
                                });

                                Updates1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        {
                                            final Dialog dialog = new Dialog(data_pendidikan.this);
                                            dialog.setContentView(R.layout.data_institusi);
                                            dialog.setCancelable(false);
                                            dialog.show();

                                            TextInputLayout textinput_tingkatan = dialog.findViewById(R.id.textinput_tingkatan);

                                            final AutoCompleteTextView tingkatan = dialog.findViewById(R.id.tingkatan);
                                            final EditText nama_institusi = dialog.findViewById(R.id.nama_institusi);
                                            final EditText jurusan_institusi = dialog.findViewById(R.id.jurusan_institusi);

                                            final EditText tahun_lulus = dialog.findViewById(R.id.tahun_lulus);

                                            final RadioButton lulus_institusi = dialog.findViewById(R.id.lulus_institusi);
                                            RadioButton tidak_lulus_institusi = dialog.findViewById(R.id.tidak_lulus_institusi);

                                            final EditText nilai_ipk = dialog.findViewById(R.id.nilai_ipk);

                                            MaterialButton batal = dialog.findViewById(R.id.batal);
                                            MaterialButton simpan = dialog.findViewById(R.id.simpan);

                                            tingkatan.setEnabled(false);

                                            tingkatan.setText(tingkat_s1.getText().toString());
                                            nama_institusi.setText(nama_s1.getText().toString());
                                            jurusan_institusi.setText(jurusan_s1.getText().toString());
                                            tahun_lulus.setText(tahun_s1.getText().toString());


                                            textinput_tingkatan.setEndIconMode(TextInputLayout.END_ICON_NONE);


                                            if(keterangan_s1.getText().toString().equals("Lulus")){
                                                lulus_institusi.setChecked(true);
                                            } else {
                                                tidak_lulus_institusi.setChecked(true);
                                            }
                                            nilai_ipk.setText(ipk_s1.getText().toString());


                                            tahun_lulus.setFocusable(false);
                                            tahun_lulus.setLongClickable(false);
                                            tahun_lulus.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    YearPickerDialog yearPickerDialog = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                                                        @Override
                                                        public void onYearSelected(int year) {
                                                            TahunLulusKuliah(String.valueOf(year));
                                                        }

                                                        private void TahunLulusKuliah(final String sdGabung) {
                                                            YearPickerDialog yearPickerDialog2 = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                                                                @Override
                                                                public void onYearSelected(int year) {
                                                                    if(Integer.parseInt(sdGabung) >= year){
                                                                        new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.WARNING_TYPE)
                                                                                .setContentText("Tahun Masuk Tidak Boleh Lebih Besar Dari Tahun Lulus")
                                                                                .setConfirmText("OK")
                                                                                .show();
                                                                    } else {
                                                                        tahun_lulus.setText(sdGabung + " - " + String.valueOf(year));
                                                                    }
                                                                }
                                                            });
                                                            yearPickerDialog2.show();
                                                            yearPickerDialog2.keterangan.setText("Tahun Lulus Kuliah");

                                                        }
                                                    });
                                                    yearPickerDialog.show();
                                                    yearPickerDialog.keterangan.setText("Tahun Masuk Kuliah");


                                                }
                                            });

                                            tingkatan.setAdapter(new ArrayAdapter<String>(data_pendidikan.this, android.R.layout.simple_dropdown_item_1line, pendidikan));

                                            batal.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });

                                            simpan.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(tingkatan.getText().toString().length() == 0){
                                                        tingkatan.setError("Masukkan Tingkatan");
                                                    } else {
                                                        pDialog = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.PROGRESS_TYPE);
                                                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                        pDialog.setTitleText("Harap Menunggu");
                                                        pDialog.setCancelable(false);
                                                        pDialog.show();

                                                        if(tingkatan.getText().toString().equals("Diploma")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_diploma";
                                                        } else if(tingkatan.getText().toString().equals("S1")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s1_NoUrut";
                                                        } else if(tingkatan.getText().toString().equals("S2")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s2_NoUrut";
                                                        } else if(tingkatan.getText().toString().equals("S3")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s3_NoUrut";
                                                        }

                                                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        pDialog.dismissWithAnimation();
                                                                        Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                                                        Success.setContentText("Data Sudah Diupdate");
                                                                        Success.setCancelable(false);
                                                                        Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                getUniv();
                                                                                sDialog.dismissWithAnimation();
                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                        Success.show();
                                                                    }
                                                                },
                                                                new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        pDialog.dismissWithAnimation();
                                                                        Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                                                        Success.setContentText("Data Sudah Diupdate");
                                                                        Success.setCancelable(false);
                                                                        Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sDialog.dismissWithAnimation();
                                                                                getUniv();
                                                                                dialog.dismiss();
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

                                                                if(tingkatan.getText().toString().equals("Diploma")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_st", nama_institusi.getText().toString());
                                                                    params.put("jurusan_st", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_st", tahun_lulus.getText().toString());
                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_st", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_st", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_st", "");
                                                                    }
                                                                    params.put("ipk_st", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_st", tingkatan.getText().toString());

                                                                } else if(tingkatan.getText().toString().equals("S1")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_s1", nama_institusi.getText().toString());
                                                                    params.put("jurusan_s1", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_s1", tahun_lulus.getText().toString());

                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_s1", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_s1", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_s1", "");
                                                                    }
                                                                    params.put("ipk_s1", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_s1", tingkatan.getText().toString());

                                                                } else if(tingkatan.getText().toString().equals("S2")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_s2", nama_institusi.getText().toString());
                                                                    params.put("jurusan_s2", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_s2", tahun_lulus.getText().toString());

                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_s2", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_s2", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_s2", "");
                                                                    }

                                                                    params.put("ipk_s2", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_s2", tingkatan.getText().toString());

                                                                } else if(tingkatan.getText().toString().equals("S3")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_s3", nama_institusi.getText().toString());
                                                                    params.put("jurusan_s3", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_s3", tahun_lulus.getText().toString());

                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_s3", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_s3", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_s3", "");
                                                                    }

                                                                    params.put("ipk_s3", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_s3", tingkatan.getText().toString());

                                                                }

                                                                System.out.println("Params Pendidikan = " + params);




                                                                return params;
                                                            }

                                                        };
                                                        stringRequest.setRetryPolicy(
                                                                new DefaultRetryPolicy(
                                                                        500000,
                                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                                ));
                                                        RequestQueue requestQueue = Volley.newRequestQueue(data_pendidikan.this);
                                                        requestQueue.add(stringRequest);
                                                    }
                                                }
                                            });

                                        }
                                    }
                                });

                                Updates2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        {
                                            final Dialog dialog = new Dialog(data_pendidikan.this);
                                            dialog.setContentView(R.layout.data_institusi);
                                            dialog.setCancelable(false);
                                            dialog.show();

                                            TextInputLayout textinput_tingkatan = dialog.findViewById(R.id.textinput_tingkatan);

                                            final AutoCompleteTextView tingkatan = dialog.findViewById(R.id.tingkatan);
                                            final EditText nama_institusi = dialog.findViewById(R.id.nama_institusi);
                                            final EditText jurusan_institusi = dialog.findViewById(R.id.jurusan_institusi);

                                            final EditText tahun_lulus = dialog.findViewById(R.id.tahun_lulus);

                                            final RadioButton lulus_institusi = dialog.findViewById(R.id.lulus_institusi);
                                            RadioButton tidak_lulus_institusi = dialog.findViewById(R.id.tidak_lulus_institusi);

                                            final EditText nilai_ipk = dialog.findViewById(R.id.nilai_ipk);

                                            MaterialButton batal = dialog.findViewById(R.id.batal);
                                            MaterialButton simpan = dialog.findViewById(R.id.simpan);

                                            tingkatan.setEnabled(false);

                                            tingkatan.setText(tingkat_s2.getText().toString());
                                            nama_institusi.setText(nama_s2.getText().toString());
                                            jurusan_institusi.setText(jurusan_s2.getText().toString());
                                            tahun_lulus.setText(tahun_s2.getText().toString());


                                            textinput_tingkatan.setEndIconMode(TextInputLayout.END_ICON_NONE);


                                            if(keterangan_s2.getText().toString().equals("Lulus")){
                                                lulus_institusi.setChecked(true);
                                            } else {
                                                tidak_lulus_institusi.setChecked(true);
                                            }
                                            nilai_ipk.setText(ipk_s2.getText().toString());


                                            tahun_lulus.setFocusable(false);
                                            tahun_lulus.setLongClickable(false);
                                            tahun_lulus.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    YearPickerDialog yearPickerDialog = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                                                        @Override
                                                        public void onYearSelected(int year) {
                                                            TahunLulusKuliah(String.valueOf(year));
                                                        }

                                                        private void TahunLulusKuliah(final String sdGabung) {
                                                            YearPickerDialog yearPickerDialog2 = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                                                                @Override
                                                                public void onYearSelected(int year) {
                                                                    if(Integer.parseInt(sdGabung) >= year){
                                                                        new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.WARNING_TYPE)
                                                                                .setContentText("Tahun Masuk Tidak Boleh Lebih Besar Dari Tahun Lulus")
                                                                                .setConfirmText("OK")
                                                                                .show();
                                                                    } else {
                                                                        tahun_lulus.setText(sdGabung + " - " + String.valueOf(year));
                                                                    }
                                                                }
                                                            });
                                                            yearPickerDialog2.show();
                                                            yearPickerDialog2.keterangan.setText("Tahun Lulus Kuliah");

                                                        }
                                                    });
                                                    yearPickerDialog.show();
                                                    yearPickerDialog.keterangan.setText("Tahun Masuk Kuliah");


                                                }
                                            });

                                            tingkatan.setAdapter(new ArrayAdapter<String>(data_pendidikan.this, android.R.layout.simple_dropdown_item_1line, pendidikan));

                                            batal.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });

                                            simpan.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(tingkatan.getText().toString().length() == 0){
                                                        tingkatan.setError("Masukkan Tingkatan");
                                                    } else {
                                                        pDialog = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.PROGRESS_TYPE);
                                                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                        pDialog.setTitleText("Harap Menunggu");
                                                        pDialog.setCancelable(false);
                                                        pDialog.show();

                                                        if(tingkatan.getText().toString().equals("Diploma")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_diploma";
                                                        } else if(tingkatan.getText().toString().equals("S1")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s1_NoUrut";
                                                        } else if(tingkatan.getText().toString().equals("S2")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s2_NoUrut";
                                                        } else if(tingkatan.getText().toString().equals("S3")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s3_NoUrut";
                                                        }

                                                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        pDialog.dismissWithAnimation();
                                                                        Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                                                        Success.setContentText("Data Sudah Diupdate");
                                                                        Success.setCancelable(false);
                                                                        Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sDialog.dismissWithAnimation();
                                                                                getUniv();
                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                        Success.show();
                                                                    }
                                                                },
                                                                new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        pDialog.dismissWithAnimation();
                                                                        Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                                                        Success.setContentText("Data Sudah Diupdate");
                                                                        Success.setCancelable(false);
                                                                        Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sDialog.dismissWithAnimation();
                                                                                getUniv();
                                                                                dialog.dismiss();
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

                                                                if(tingkatan.getText().toString().equals("Diploma")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_st", nama_institusi.getText().toString());
                                                                    params.put("jurusan_st", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_st", tahun_lulus.getText().toString());
                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_st", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_st", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_st", "");
                                                                    }
                                                                    params.put("ipk_st", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_st", tingkatan.getText().toString());

                                                                } else if(tingkatan.getText().toString().equals("S1")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_s1", nama_institusi.getText().toString());
                                                                    params.put("jurusan_s1", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_s1", tahun_lulus.getText().toString());

                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_s1", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_s1", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_s1", "");
                                                                    }
                                                                    params.put("ipk_s1", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_s1", tingkatan.getText().toString());

                                                                } else if(tingkatan.getText().toString().equals("S2")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_s2", nama_institusi.getText().toString());
                                                                    params.put("jurusan_s2", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_s2", tahun_lulus.getText().toString());

                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_s2", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_s2", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_s2", "");
                                                                    }

                                                                    params.put("ipk_s2", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_s2", tingkatan.getText().toString());

                                                                } else if(tingkatan.getText().toString().equals("S3")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_s3", nama_institusi.getText().toString());
                                                                    params.put("jurusan_s3", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_s3", tahun_lulus.getText().toString());

                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_s3", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_s3", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_s3", "");
                                                                    }

                                                                    params.put("ipk_s3", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_s3", tingkatan.getText().toString());

                                                                }

                                                                System.out.println("Params Pendidikan = " + params);




                                                                return params;
                                                            }

                                                        };
                                                        stringRequest.setRetryPolicy(
                                                                new DefaultRetryPolicy(
                                                                        500000,
                                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                                ));
                                                        RequestQueue requestQueue = Volley.newRequestQueue(data_pendidikan.this);
                                                        requestQueue.add(stringRequest);
                                                    }
                                                }
                                            });

                                        }
                                    }
                                });

                                Updates3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        {
                                            final Dialog dialog = new Dialog(data_pendidikan.this);
                                            dialog.setContentView(R.layout.data_institusi);
                                            dialog.setCancelable(false);
                                            dialog.show();

                                            TextInputLayout textinput_tingkatan = dialog.findViewById(R.id.textinput_tingkatan);

                                            final AutoCompleteTextView tingkatan = dialog.findViewById(R.id.tingkatan);
                                            final EditText nama_institusi = dialog.findViewById(R.id.nama_institusi);
                                            final EditText jurusan_institusi = dialog.findViewById(R.id.jurusan_institusi);

                                            final EditText tahun_lulus = dialog.findViewById(R.id.tahun_lulus);

                                            final RadioButton lulus_institusi = dialog.findViewById(R.id.lulus_institusi);
                                            RadioButton tidak_lulus_institusi = dialog.findViewById(R.id.tidak_lulus_institusi);

                                            final EditText nilai_ipk = dialog.findViewById(R.id.nilai_ipk);

                                            MaterialButton batal = dialog.findViewById(R.id.batal);
                                            MaterialButton simpan = dialog.findViewById(R.id.simpan);

                                            tingkatan.setEnabled(false);

                                            tingkatan.setText(tingkat_s3.getText().toString());
                                            nama_institusi.setText(nama_s3.getText().toString());
                                            jurusan_institusi.setText(jurusan_s3.getText().toString());
                                            tahun_lulus.setText(tahun_s3.getText().toString());


                                            textinput_tingkatan.setEndIconMode(TextInputLayout.END_ICON_NONE);


                                            if(keterangan_s3.getText().toString().equals("Lulus")){
                                                lulus_institusi.setChecked(true);
                                            } else {
                                                tidak_lulus_institusi.setChecked(true);
                                            }
                                            nilai_ipk.setText(ipk_s3.getText().toString());


                                            tahun_lulus.setFocusable(false);
                                            tahun_lulus.setLongClickable(false);
                                            tahun_lulus.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    YearPickerDialog yearPickerDialog = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                                                        @Override
                                                        public void onYearSelected(int year) {
                                                            TahunLulusKuliah(String.valueOf(year));
                                                        }

                                                        private void TahunLulusKuliah(final String sdGabung) {
                                                            YearPickerDialog yearPickerDialog2 = new YearPickerDialog(data_pendidikan.this, new YearPickerDialog.YearPickerListener() {
                                                                @Override
                                                                public void onYearSelected(int year) {
                                                                    if(Integer.parseInt(sdGabung) >= year){
                                                                        new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.WARNING_TYPE)
                                                                                .setContentText("Tahun Masuk Tidak Boleh Lebih Besar Dari Tahun Lulus")
                                                                                .setConfirmText("OK")
                                                                                .show();
                                                                    } else {
                                                                        tahun_lulus.setText(sdGabung + " - " + String.valueOf(year));
                                                                    }
                                                                }
                                                            });
                                                            yearPickerDialog2.show();
                                                            yearPickerDialog2.keterangan.setText("Tahun Lulus Kuliah");

                                                        }
                                                    });
                                                    yearPickerDialog.show();
                                                    yearPickerDialog.keterangan.setText("Tahun Masuk Kuliah");


                                                }
                                            });

                                            tingkatan.setAdapter(new ArrayAdapter<String>(data_pendidikan.this, android.R.layout.simple_dropdown_item_1line, pendidikan));

                                            batal.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });

                                            simpan.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(tingkatan.getText().toString().length() == 0){
                                                        tingkatan.setError("Masukkan Tingkatan");
                                                    } else {
                                                        pDialog = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.PROGRESS_TYPE);
                                                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                        pDialog.setTitleText("Harap Menunggu");
                                                        pDialog.setCancelable(false);
                                                        pDialog.show();

                                                        if(tingkatan.getText().toString().equals("Diploma")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_diploma";
                                                        } else if(tingkatan.getText().toString().equals("S1")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s1_NoUrut";
                                                        } else if(tingkatan.getText().toString().equals("S2")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s2_NoUrut";
                                                        } else if(tingkatan.getText().toString().equals("S3")){
                                                            URL = "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_s3_NoUrut";
                                                        }

                                                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        pDialog.dismissWithAnimation();
                                                                        Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                                                        Success.setContentText("Data Sudah Diupdate");
                                                                        Success.setCancelable(false);
                                                                        Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sDialog.dismissWithAnimation();
                                                                                getUniv();
                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                        Success.show();
                                                                    }
                                                                },
                                                                new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        pDialog.dismissWithAnimation();
                                                                        Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
                                                                        Success.setContentText("Data Sudah Diupdate");
                                                                        Success.setCancelable(false);
                                                                        Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sDialog.dismissWithAnimation();
                                                                                getUniv();
                                                                                dialog.dismiss();
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

                                                                if(tingkatan.getText().toString().equals("Diploma")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_st", nama_institusi.getText().toString());
                                                                    params.put("jurusan_st", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_st", tahun_lulus.getText().toString());
                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_st", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_st", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_st", "");
                                                                    }
                                                                    params.put("ipk_st", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_st", tingkatan.getText().toString());

                                                                } else if(tingkatan.getText().toString().equals("S1")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_s1", nama_institusi.getText().toString());
                                                                    params.put("jurusan_s1", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_s1", tahun_lulus.getText().toString());

                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_s1", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_s1", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_s1", "");
                                                                    }
                                                                    params.put("ipk_s1", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_s1", tingkatan.getText().toString());

                                                                } else if(tingkatan.getText().toString().equals("S2")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_s2", nama_institusi.getText().toString());
                                                                    params.put("jurusan_s2", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_s2", tahun_lulus.getText().toString());

                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_s2", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_s2", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_s2", "");
                                                                    }

                                                                    params.put("ipk_s2", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_s2", tingkatan.getText().toString());

                                                                } else if(tingkatan.getText().toString().equals("S3")){

                                                                    params.put("nik_baru", nik_baru);
                                                                    params.put("no_urut", noUrut);
                                                                    params.put("nama_s3", nama_institusi.getText().toString());
                                                                    params.put("jurusan_s3", jurusan_institusi.getText().toString());
                                                                    params.put("tahun_s3", tahun_lulus.getText().toString());

                                                                    if(lulus_institusi.isChecked()){
                                                                        params.put("ket_s3", "Lulus");
                                                                    } else if (tidak_lulus_institusi.isChecked()){
                                                                        params.put("ket_s3", "Tidak Lulus");
                                                                    } else {
                                                                        params.put("ket_s3", "");
                                                                    }

                                                                    params.put("ipk_s3", nilai_ipk.getText().toString());
                                                                    params.put("tingkat_s3", tingkatan.getText().toString());

                                                                }

                                                                System.out.println("Params Pendidikan = " + params);




                                                                return params;
                                                            }

                                                        };
                                                        stringRequest.setRetryPolicy(
                                                                new DefaultRetryPolicy(
                                                                        500000,
                                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                                ));
                                                        RequestQueue requestQueue = Volley.newRequestQueue(data_pendidikan.this);
                                                        requestQueue.add(stringRequest);
                                                    }
                                                }
                                            });

                                        }
                                    }
                                });



                                nama_st.setText(movieObject.getString("nama_st"));
                                jurusan_st.setText(movieObject.getString("jurusan_st"));
                                tahun_st.setText(movieObject.getString("tahun_st"));
                                keterangan_st.setText(movieObject.getString("ket_st"));
                                ipk_st.setText(movieObject.getString("ipk_st"));
                                tingkat_st.setText(movieObject.getString("tingkat_st"));

                                nama_s1.setText(movieObject.getString("nama_s1"));
                                jurusan_s1.setText(movieObject.getString("jurusan_s1"));
                                tahun_s1.setText(movieObject.getString("tahun_s1"));
                                keterangan_s1.setText(movieObject.getString("ket_s1"));
                                ipk_s1.setText(movieObject.getString("ipk_s1"));
                                tingkat_s1.setText(movieObject.getString("tingkat_s1"));

                                nama_s2.setText(movieObject.getString("nama_s2"));
                                jurusan_s2.setText(movieObject.getString("jurusan_s2"));
                                tahun_s2.setText(movieObject.getString("tahun_s2"));
                                keterangan_s2.setText(movieObject.getString("ket_s2"));
                                ipk_s2.setText(movieObject.getString("ipk_s2"));
                                tingkat_s2.setText(movieObject.getString("tingkat_s2"));

                                nama_s3.setText(movieObject.getString("nama_s3"));
                                jurusan_s3.setText(movieObject.getString("jurusan_s3"));
                                tahun_s3.setText(movieObject.getString("tahun_s3"));
                                keterangan_s3.setText(movieObject.getString("ket_s3"));
                                ipk_s3.setText(movieObject.getString("ipk_s3"));
                                tingkat_s3.setText(movieObject.getString("tingkat_s3"));




                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        diploma.setVisibility(View.GONE);
                        s1.setVisibility(View.GONE);
                        s2.setVisibility(View.GONE);
                        s3.setVisibility(View.GONE);

                        pendidikan.add("Diploma");
                        pendidikan.add("S1");
                        pendidikan.add("S2");
                        pendidikan.add("S3");
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

    private void setNavigationDrawer() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        dLayout = (DrawerLayout) findViewById(R.id.dl_datapendidikan); // initiate a DrawerLayout
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
                            data_pendidikan.this);
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
                                    Intent intent = new Intent(data_pendidikan.this, MainActivity.class);
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

    private void getPendidikan() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_NoUrut?no_urut=" + noUrut,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                if(movieObject.getString("status_sd").equals("Normal")){
                                    normalSd.setChecked(true);
                                } else if(movieObject.getString("status_sd").equals("Paket")){
                                    paketSd.setChecked(true);
                                }

                                namaSekolahSd.setText(movieObject.getString("nama_sd"));
                                tahunSd.setText(movieObject.getString("tahun_sd"));
                                NilaiRataSd.setText(movieObject.getString("nilai_sd"));

                                if(movieObject.getString("ket_sd").equals("Lulus")){
                                    lulusSd.setChecked(true);
                                } else if(movieObject.getString("ket_sd").equals("Tidak Lulus")){
                                    tidaklulusSd.setChecked(true);
                                }


                                if(movieObject.getString("status_smp").equals("Normal")){
                                    normalSMP.setChecked(true);
                                } else if(movieObject.getString("status_smp").equals("Paket")){
                                    paketSMP.setChecked(true);
                                }

                                namaSekolahSMP.setText(movieObject.getString("nama_smp"));
                                tahunSMP.setText(movieObject.getString("tahun_smp"));
                                NilaiRataSMP.setText(movieObject.getString("nilai_smp"));

                                if(movieObject.getString("ket_smp").equals("Lulus")){
                                    lulusSMP.setChecked(true);
                                } else if(movieObject.getString("ket_smp").equals("Tidak Lulus")){
                                    tidaklulusSMP.setChecked(true);
                                }


                                if(movieObject.getString("status_smk").equals("Normal")){
                                    normalSMA.setChecked(true);
                                } else if(movieObject.getString("status_smk").equals("Paket")){
                                    paketSMA.setChecked(true);
                                }

                                namaSekolahSMA.setText(movieObject.getString("nama_smk"));
                                jurusanSMA.setText(movieObject.getString("jurusan_smk"));
                                tahunSMA.setText(movieObject.getString("tahun_smk"));
                                NilaiRataSMA.setText(movieObject.getString("nilai_smk"));

                                if(movieObject.getString("ket_smk").equals("Lulus")){
                                    lulusSMA.setChecked(true);
                                } else if(movieObject.getString("ket_smk").equals("Tidak Lulus")){
                                    tidaklulusSMA.setChecked(true);
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

    private void updatePendidikanAwal() {
        pDialog = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_pendidikan_awal_NoUrut",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
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
                        Success = new SweetAlertDialog(data_pendidikan.this, SweetAlertDialog.SUCCESS_TYPE);
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
                if(normalSd.isChecked()){
                    params.put("status_sd", "Normal");
                } else if (paketSd.isChecked()) {
                    params.put("status_sd", "Paket");
                } else {
                    params.put("status_sd", "");
                }
                params.put("nama_sd", namaSekolahSd.getText().toString());
                params.put("tahun_sd", tahunSd.getText().toString());
                if(lulusSd.isChecked()){
                    params.put("ket_sd", "Lulus");
                } else if (tidaklulusSd.isChecked()){
                    params.put("ket_sd", "Tidak Lulus");
                } else {
                    params.put("ket_sd", "");
                }
                params.put("nilai_sd", NilaiRataSd.getText().toString());


                if(normalSMP.isChecked()){
                    params.put("status_smp", "Normal");
                } else if(paketSMP.isChecked()) {
                    params.put("status_smp", "Paket");
                } else {
                    params.put("status_smp", "");
                }
                params.put("nama_smp", namaSekolahSMP.getText().toString());
                params.put("tahun_smp", tahunSMP.getText().toString());
                if(lulusSMP.isChecked()){
                    params.put("ket_smp", "Lulus");
                } else if(tidaklulusSMP.isChecked()){
                    params.put("ket_smp", "Tidak Lulus");
                } else {
                    params.put("ket_smp", "");
                }
                params.put("nilai_smp", NilaiRataSMP.getText().toString());


                if(normalSMA.isChecked()){
                    params.put("status_smk", "Normal");
                } else if(paketSMA.isChecked()){
                    params.put("status_smk", "Paket");
                } else {
                    params.put("status_smk", "");
                }
                params.put("nama_smk", namaSekolahSMA.getText().toString());
                params.put("tahun_smk", tahunSMA.getText().toString());
                params.put("jurusan_smk", jurusanSMA.getText().toString());

                if(lulusSMA.isChecked()){
                    params.put("ket_smk", "Lulus");
                } else  if(tidaklulusSMA.isChecked()) {
                    params.put("ket_smk", "Tidak Lulus");
                } else {
                    params.put("ket_smk", "");
                }
                params.put("nilai_smk", NilaiRataSMA.getText().toString());


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
}