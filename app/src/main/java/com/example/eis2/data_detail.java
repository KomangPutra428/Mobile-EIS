package com.example.eis2;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.biodata.noUrut;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class data_detail extends AppCompatActivity {
    EditText tgllahir, tempatlahir, alamatKtp, notelp, notehp, email, alamatDomisili;

    SweetAlertDialog pDialog, Success;

    RadioButton laki, perempuan;

    AutoCompleteTextView statuspernikahan;
    SharedPreferences sharedPreferences;

    String item[]={
            "Menikah", "Belum Menikah"
    };

    MaterialButton batal, ubah;
    private Calendar date;

    DrawerLayout dLayout;


    private SimpleDateFormat dateFormatter;

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        Calendar twoDaysAgo = (Calendar) currentDate.clone();
        twoDaysAgo.add(Calendar.DATE, 0);

        date = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                tgllahir.setText(dateFormatter.format(date.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(data_detail.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(twoDaysAgo.getTimeInMillis());
        datePickerDialog.show();

    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_detail);

        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        tgllahir = findViewById(R.id.tgllahir);
        tempatlahir = findViewById(R.id.tempatlahir);
        alamatKtp = findViewById(R.id.alamatKtp);
        notelp = findViewById(R.id.notelp);
        notehp = findViewById(R.id.notehp);
        email = findViewById(R.id.email);
        alamatDomisili = findViewById(R.id.alamatDomisili);


        laki = findViewById(R.id.laki);
        perempuan = findViewById(R.id.perempuan);
        statuspernikahan = findViewById(R.id.statuspernikahan);

        statuspernikahan.setThreshold(100);

        batal = findViewById(R.id.batal);
        ubah = findViewById(R.id.ubah);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        toolbar.setTitle("Data Detail");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });
        setNavigationDrawer();

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tgllahir.getText().toString().length() == 0){
                    tgllahir.setError("Masukkan Tanggal Lahir");
                } else if(tempatlahir.getText().toString().length() == 0){
                    tempatlahir.setError("Masukkan Tempat Lahir");
                } else if (!laki.isChecked() && !perempuan.isChecked()) {
                    new SweetAlertDialog(data_detail.this, SweetAlertDialog.WARNING_TYPE)
                            .setContentText("Pilih Jenis Kelamin")
                            .setConfirmText("OK")
                            .show();
                } else if (statuspernikahan.getText().toString().length() == 0) {
                    statuspernikahan.setError("Masukkan Status Pernikahan");
                } else if (alamatKtp.getText().toString().length() == 0) {
                    alamatKtp.setError("Masukkan Alamat Sesuai KTP");
                } else if (alamatDomisili.getText().toString().length() == 0) {
                    alamatDomisili.setError("Masukkan Alamat Domisili");
                } else if (notehp.getText().toString().length() != 0){
                    if(notehp.getText().toString().length() < 10){
                        notehp.setError("Digit Nomor HP minimal 10");
                    } else if(notehp.getText().toString().length() > 13){
                        notehp.setError("Digit Nomor HP maksimal 13");
                    } else {
                        ubah.setEnabled(false);
                        updateDetail();
                    }
                } else if (notelp.getText().toString().length() != 0){
                    if(notelp.getText().toString().length() < 10){
                        notelp.setError("Digit Nomor Telepon minimal 10");
                    } else if(notelp.getText().toString().length() > 13){
                        notelp.setError("Digit Nomor Telepon maksimal 13");
                    }else {
                        ubah.setEnabled(false);
                        updateDetail();
                    }
                } else {
                    ubah.setEnabled(false);
                    updateDetail();
                }
            }
        });


        tgllahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        statuspernikahan.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item));


        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_detail_NoUrut?no_urut=" + noUrut,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                tgllahir.setText(convertFormat(movieObject.getString("tanggal_lahir")));
                                tempatlahir.setText(movieObject.getString("tempat_lahir"));

                                if(movieObject.getString("jenis_kelamin").equals("Laki - Laki")){
                                    laki.setChecked(true);
                                } else {
                                    perempuan.setChecked(true);
                                }

                                statuspernikahan.setText(movieObject.getString("status_pernikahan"));
                                alamatKtp.setText(movieObject.getString("alamat_ktp"));

                                notelp.setText(movieObject.getString("no_telp"));
                                notehp.setText(movieObject.getString("no_hp"));

                                email.setText(movieObject.getString("email"));
                                alamatDomisili.setText(movieObject.getString("alamat_domisili"));

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

    private void setNavigationDrawer() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        dLayout = (DrawerLayout) findViewById(R.id.dl_datadetail); // initiate a DrawerLayout
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
                            data_detail.this);
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
                                    Intent intent = new Intent(data_detail.this, MainActivity.class);
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

    private void updateDetail() {
        pDialog = new SweetAlertDialog(data_detail.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/master/karyawan/index_detail_NoUrut",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Success = new SweetAlertDialog(data_detail.this, SweetAlertDialog.SUCCESS_TYPE);
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
                        Success = new SweetAlertDialog(data_detail.this, SweetAlertDialog.SUCCESS_TYPE);
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

                params.put("tanggal_lahir", ReverseconvertFormat(tgllahir.getText().toString()));
                params.put("tempat_lahir", tempatlahir.getText().toString());

                if(laki.isChecked()){
                    params.put("jenis_kelamin", "Laki - Laki");
                } else {
                    params.put("jenis_kelamin", "Perempuan");
                }
                params.put("status_pernikahan", statuspernikahan.getText().toString());


                params.put("alamat_ktp", alamatKtp.getText().toString());
                if(notelp.getText().toString().length() == 0){
                    params.put("no_telp", "");
                } else {
                    params.put("no_telp", notelp.getText().toString());
                }

                if(notehp.getText().toString().length() == 0){
                    params.put("no_hp", "");
                } else {
                    params.put("no_hp", notehp.getText().toString());
                }

                if(email.getText().toString().length() == 0){
                    params.put("email", "");
                } else {
                    params.put("email", email.getText().toString());
                }

                params.put("alamat_domisili", alamatDomisili.getText().toString());

                return params;
            }

        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(data_detail.this);
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