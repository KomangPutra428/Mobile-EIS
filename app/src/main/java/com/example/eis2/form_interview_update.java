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
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;

public class form_interview_update extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    TextView note, note2, note3, id, nama1, nama2, nama3;
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    EditText name, position, noktp, tanggaledit, usiakar, pewawancara1, pewawancara2, pewawancara3;
    EditText keterangankelebihan, keterangankekurangan, keterangancatatan;
    EditText pilihpendidikan;
    RadioGroup option, option_1, option2, option_21, option3, option31;
    RadioButton sangatbaik, baik, kurang, sangatkurang;
    ImageButton simpan, reset;
    private boolean isChecking = true;
    RadioButton sangatbaik2, baik2, kurang2, sangatkurang2;
    RadioButton disarankan, dipertimbangkan, tidakdisarankan;
    private int mCheckedId = R.id.sangatbaik;
    private int mCheckedId2 = R.id.sangatbaik2;
    private int mCheckedId3 = R.id.disarankan;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_interview_update);
        HttpsTrustManager.allowAllSSL();

        id = (TextView) findViewById(R.id.id);

        note = (TextView) findViewById(R.id.note);
        note.setPaintFlags(note.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        note2 = (TextView) findViewById(R.id.note2);
        note2.setPaintFlags(note2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        note3 = (TextView) findViewById(R.id.note3);
        note3.setPaintFlags(note3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        nama1 = (TextView) findViewById(R.id.nama1);
        nama2 = (TextView) findViewById(R.id.nama2);
        nama3 = (TextView) findViewById(R.id.nama3);

        sangatbaik = (RadioButton) findViewById(R.id.sangatbaik);
        baik = (RadioButton) findViewById(R.id.baik);
        kurang = (RadioButton) findViewById(R.id.kurang);
        sangatkurang = (RadioButton) findViewById(R.id.sangatkurang);

        simpan = (ImageButton) findViewById(R.id.simpan);
        reset = (ImageButton) findViewById(R.id.reset);
        sangatbaik2 = (RadioButton) findViewById(R.id.sangatbaik2);
        baik2 = (RadioButton) findViewById(R.id.baik2);
        kurang2 = (RadioButton) findViewById(R.id.kurang2);
        sangatkurang2 = (RadioButton) findViewById(R.id.sangatkurang2);

        disarankan = (RadioButton) findViewById(R.id.disarankan);
        dipertimbangkan = (RadioButton) findViewById(R.id.dipertimbangkan);
        tidakdisarankan = (RadioButton) findViewById(R.id.tidakdisarankan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        pilihpendidikan = (EditText) findViewById(R.id.pilihpendidikan);

        option = (RadioGroup) findViewById(R.id.option);
        option.setOnCheckedChangeListener(this);

        option_1 = (RadioGroup) findViewById(R.id.option_1);
        option_1.setOnCheckedChangeListener(this);

        option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    option_1.clearCheck();
                    mCheckedId = checkedId;
                }
                if(kurang.isChecked() && kurang2.isChecked()) {
                    disarankan.setVisibility(View.INVISIBLE);
                } else if(kurang.isChecked() && sangatkurang2.isChecked()) {
                    disarankan.setVisibility(View.INVISIBLE);
                } else if(sangatkurang.isChecked() && kurang2.isChecked()){
                    disarankan.setVisibility(View.INVISIBLE);
                    dipertimbangkan.setVisibility(View.VISIBLE);
                } else if(sangatkurang.isChecked() && sangatkurang2.isChecked()){
                    disarankan.setVisibility(View.INVISIBLE);
                    dipertimbangkan.setVisibility(View.INVISIBLE);
                } else {
                    disarankan.setVisibility(View.VISIBLE);
                    dipertimbangkan.setVisibility(View.VISIBLE);
                }
                isChecking = true;
            }
        });

        option_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    option.clearCheck();
                    mCheckedId = checkedId;
                }
                if(kurang.isChecked() && kurang2.isChecked()) {
                    disarankan.setVisibility(View.INVISIBLE);
                } else if(kurang.isChecked() && sangatkurang2.isChecked()) {
                    disarankan.setVisibility(View.INVISIBLE);
                } else if(sangatkurang.isChecked() && kurang2.isChecked()){
                    disarankan.setVisibility(View.INVISIBLE);
                    dipertimbangkan.setVisibility(View.VISIBLE);
                } else if(sangatkurang.isChecked() && sangatkurang2.isChecked()){
                    disarankan.setVisibility(View.INVISIBLE);
                    dipertimbangkan.setVisibility(View.INVISIBLE);
                } else {
                    disarankan.setVisibility(View.VISIBLE);
                    dipertimbangkan.setVisibility(View.VISIBLE);
                }
                isChecking = true;
            }
        });

        option2 = (RadioGroup) findViewById(R.id.option2);
        option2.setOnCheckedChangeListener(this);

        option_21 = (RadioGroup) findViewById(R.id.option_21);
        option_21.setOnCheckedChangeListener(this);

        option2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    option_21.clearCheck();
                    mCheckedId2 = checkedId;
                }
                if(kurang.isChecked() && kurang2.isChecked()) {
                    disarankan.setVisibility(View.INVISIBLE);
                } else if(kurang.isChecked() && sangatkurang2.isChecked()) {
                    disarankan.setVisibility(View.INVISIBLE);
                } else if(sangatkurang.isChecked() && kurang2.isChecked()){
                    disarankan.setVisibility(View.INVISIBLE);
                    dipertimbangkan.setVisibility(View.VISIBLE);
                } else if(sangatkurang.isChecked() && sangatkurang2.isChecked()){
                    disarankan.setVisibility(View.INVISIBLE);
                    dipertimbangkan.setVisibility(View.INVISIBLE);
                } else {
                    disarankan.setVisibility(View.VISIBLE);
                    dipertimbangkan.setVisibility(View.VISIBLE);
                }
                isChecking = true;
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keterangankelebihan.getText().toString().length() == 0) {
                    keterangankelebihan.setError("Harap Di isi");
                } else if (keterangankekurangan.getText().toString().length() == 0) {
                    keterangankekurangan.setError("Harap Di isi");
                } else {
                    updatehasil();
                }
            }
        });

        option_21.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    option2.clearCheck();
                    mCheckedId2 = checkedId;
                }
                if(kurang.isChecked() && kurang2.isChecked()) {
                    disarankan.setVisibility(View.INVISIBLE);
                } else if(kurang.isChecked() && sangatkurang2.isChecked()) {
                    disarankan.setVisibility(View.INVISIBLE);
                } else if(sangatkurang.isChecked() && kurang2.isChecked()){
                    disarankan.setVisibility(View.INVISIBLE);
                    dipertimbangkan.setVisibility(View.VISIBLE);
                } else if(sangatkurang.isChecked() && sangatkurang2.isChecked()){
                    disarankan.setVisibility(View.INVISIBLE);
                    dipertimbangkan.setVisibility(View.INVISIBLE);
                } else {
                    disarankan.setVisibility(View.VISIBLE);
                    dipertimbangkan.setVisibility(View.VISIBLE);
                }
                isChecking = true;
            }
        });

        option3 = (RadioGroup) findViewById(R.id.option3);
        option3.setOnCheckedChangeListener(this);

        option31 = (RadioGroup) findViewById(R.id.option31);
        option31.setOnCheckedChangeListener(this);

        option3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    option31.clearCheck();
                    mCheckedId3 = checkedId;
                }
                isChecking = true;
            }
        });

        option31.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    option3.clearCheck();
                    mCheckedId3 = checkedId;
                }
                isChecking = true;
            }
        });

        keterangankelebihan = (EditText) findViewById(R.id.keterangankelebihan);
        keterangankekurangan = (EditText) findViewById(R.id.keterangankekurangan);
        keterangancatatan = (EditText) findViewById(R.id.keterangancatatan);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keterangankelebihan.getText().clear();
                keterangankekurangan.getText().clear();
                keterangancatatan.getText().clear();
                baik.setChecked(true);
                baik2.setChecked(true);
                dipertimbangkan.setChecked(true);
            }
        });

        name = (EditText) findViewById(R.id.name);
        position = (EditText) findViewById(R.id.position);
        noktp = (EditText) findViewById(R.id.noktp);
        tanggaledit = (EditText) findViewById(R.id.tanggaledit);
        usiakar = (EditText) findViewById(R.id.usiakar);

        pewawancara1 = (EditText) findViewById(R.id.pewawancara1);
        pewawancara2 = (EditText) findViewById(R.id.pewawancara2);
        pewawancara3 = (EditText) findViewById(R.id.pewawancara3);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });

        getData();
        getDataNilai();
        setNavigationDrawer();
    }

    private void updatehasil() {
        pDialog = new ProgressDialog(form_interview_update.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/website/hasil/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                        form_interview_update.this.finish();
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
                params.put("id", id.getText().toString());

                if(sangatbaik.isChecked()){
                    params.put("nilai_pertama", "1");
                } else if(baik.isChecked()){
                    params.put("nilai_pertama", "2");
                } else if(kurang.isChecked()){
                    params.put("nilai_pertama", "3");
                } else if(sangatkurang.isChecked()){
                    params.put("nilai_pertama", "4");
                }

                if(sangatbaik2.isChecked()){
                    params.put("nilai_kedua", "1");
                } else if(baik2.isChecked()){
                    params.put("nilai_kedua", "2");
                } else if(kurang2.isChecked()){
                    params.put("nilai_kedua", "3");
                } else if(sangatkurang2.isChecked()){
                    params.put("nilai_kedua", "4");
                }

                params.put("kelebihan", keterangankelebihan.getText().toString());

                params.put("kekurangan", keterangankekurangan.getText().toString());
                params.put("catatan_khusus", keterangancatatan.getText().toString());

                if(disarankan.isChecked()){
                    params.put("kesimpulan", "1");
                } else if(dipertimbangkan.isChecked()){
                    params.put("kesimpulan", "2");
                } else if(tidakdisarankan.isChecked()){
                    params.put("kesimpulan", "3");
                }

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

    private void getDataNilai() {
        final String idkaryawan = getIntent().getStringExtra(KEY_NIK);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/website/hasil/index?no_ktp=" + idkaryawan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);
                                final String nilai_pertama = movieObject.getString("nilai_pertama");
                                final String nilai_kedua = movieObject.getString("nilai_kedua");
                                final String kesimpulan = movieObject.getString("kesimpulan");

                                final String kelebihan = movieObject.getString("kelebihan");
                                final String kekurangan = movieObject.getString("kekurangan");
                                final String catatan_khusus = movieObject.getString("catatan_khusus");

                                final String idnilai = movieObject.getString("id");

                                id.setText(idnilai);

                                if(nilai_pertama.equals("1")){
                                    sangatbaik.setChecked(true);
                                } else if(nilai_pertama.equals("2")){
                                    baik.setChecked(true);
                                } else if(nilai_pertama.equals("3")){
                                    kurang.setChecked(true);
                                } else if(nilai_pertama.equals("4")){
                                    sangatkurang.setChecked(true);
                                }

                                if(nilai_kedua.equals("1")){
                                    sangatbaik2.setChecked(true);
                                } else if(nilai_kedua.equals("2")){
                                    baik2.setChecked(true);
                                } else if(nilai_kedua.equals("3")){
                                    kurang2.setChecked(true);
                                } else if(nilai_kedua.equals("4")){
                                    sangatkurang2.setChecked(true);
                                }

                                if(kesimpulan.equals("1")){
                                    disarankan.setChecked(true);
                                } else if(kesimpulan.equals("2")){
                                    dipertimbangkan.setChecked(true);
                                } else if(kesimpulan.equals("3")){
                                    tidakdisarankan.setChecked(true);
                                }


                                keterangankelebihan.setText(kelebihan);
                                keterangankekurangan.setText(kekurangan);
                                keterangancatatan.setText(catatan_khusus);
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getData() {
        final String idkaryawan = getIntent().getStringExtra(KEY_NIK);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/website/jadwal/index?no_ktp=" + idkaryawan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);
                                final String nama = movieObject.getString("nama_lengkap");
                                final String posisi = movieObject.getString("posisi");
                                final String ktp = movieObject.getString("no_ktp");
                                final String tanggal = movieObject.getString("tanggal_interview");
                                String tgl_lahir = movieObject.getString("dd_tanggal_lahir");

                                nama1.setText(movieObject.getString("nama_pewawancara_1"));
                                nama2.setText(movieObject.getString("nama_pewawancara_2"));
                                nama3.setText(movieObject.getString("nama_pewawancara_"));


                                Calendar calendarBirthday = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                calendarBirthday.setTime(sdf.parse(tgl_lahir));
                                Calendar calendarNow = Calendar.getInstance();
                                int yearNow = calendarNow.get(Calendar.YEAR);
                                int yearBirthday = calendarBirthday.get(Calendar.YEAR);
                                int years = yearNow - yearBirthday;
                                usiakar.setText(String.valueOf(years));

                                name.setText(nama);
                                position.setText(posisi);
                                noktp.setText(ktp);
                                tanggaledit.setText(convertFormat(tanggal));

                                StringRequest stringRequest5 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/jabatan/index?no_jabatan_karyawan="+ position.getText().toString(),
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject obj = new JSONObject(response);
                                                    JSONArray movieArray = obj.getJSONArray("data");

                                                    for (int i = 0; i < movieArray.length(); i++) {

                                                        JSONObject movieObject = movieArray.getJSONObject(i);

                                                        position.setText(movieObject.getString("jabatan_karyawan"));

                                                    }
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
                                stringRequest5.setRetryPolicy(
                                        new DefaultRetryPolicy(
                                                7200000,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                        )
                                );
                                RequestQueue requestQueue5 = Volley.newRequestQueue(form_interview_update.this);
                                requestQueue5.add(stringRequest5);

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + nama1.getText().toString(),
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject obj = new JSONObject(response);
                                                    JSONArray movieArray = obj.getJSONArray("data");
                                                    for (int i = 0; i < movieArray.length(); i++) {
                                                        JSONObject movieObject = movieArray.getJSONObject(i);
                                                        movieObject.getString("nama_karyawan_struktur");

                                                        pewawancara1.setText(movieObject.getString("nama_karyawan_struktur"));

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
                                RequestQueue requestQueue = Volley.newRequestQueue(form_interview_update.this);
                                requestQueue.add(stringRequest);

                                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + nama2.getText().toString(),
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject obj = new JSONObject(response);
                                                    JSONArray movieArray = obj.getJSONArray("data");
                                                    for (int i = 0; i < movieArray.length(); i++) {
                                                        JSONObject movieObject = movieArray.getJSONObject(i);
                                                        movieObject.getString("nama_karyawan_struktur");

                                                        pewawancara2.setText(movieObject.getString("nama_karyawan_struktur"));

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
                                stringRequest2.setRetryPolicy(
                                        new DefaultRetryPolicy(
                                                7200000,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                        )
                                );
                                RequestQueue requestQueue2 = Volley.newRequestQueue(form_interview_update.this);
                                requestQueue2.add(stringRequest2);

                                StringRequest stringRequest3 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + nama3.getText().toString(),
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject obj = new JSONObject(response);
                                                    JSONArray movieArray = obj.getJSONArray("data");
                                                    for (int i = 0; i < movieArray.length(); i++) {
                                                        JSONObject movieObject = movieArray.getJSONObject(i);
                                                        movieObject.getString("nama_karyawan_struktur");

                                                        pewawancara3.setText(movieObject.getString("nama_karyawan_struktur"));

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
                                stringRequest3.setRetryPolicy(
                                        new DefaultRetryPolicy(
                                                7200000,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                        )
                                );
                                RequestQueue requestQueue3 = Volley.newRequestQueue(form_interview_update.this);
                                requestQueue3.add(stringRequest3);

                                getEducation();

                            }

                        } catch (JSONException | ParseException e) {
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

    private void getEducation() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/website/pendidikan/index?di_no_ktp=" + noktp.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                if (movieObject.getString("dp_status_smp").equals("null")) {
                                    pilihpendidikan.setText("SD");
                                } else if (movieObject.getString("dp_status_sma").equals("null")) {
                                    pilihpendidikan.setText("SMP");
                                } else if (movieObject.getString("dp_nama_st").equals("null")) {
                                    if (movieObject.getString("dp_nama_s1").equals("null")) {
                                        pilihpendidikan.setText("SMA");
                                    } else if (movieObject.getString("dp_nama_s2").equals("null")) {
                                        pilihpendidikan.setText("S1");
                                    } else if (movieObject.getString("dp_nama_s3").equals("null")) {
                                        pilihpendidikan.setText("S2");
                                    } else if (!movieObject.getString("dp_nama_s3").equals("null")) {
                                        pilihpendidikan.setText("S3");
                                    } else {
                                        pilihpendidikan.setText("SMA");
                                    }
                                } else if (movieObject.getString("dp_nama_s1").equals("null")) {
                                    pilihpendidikan.setText("D3");
                                } else if (movieObject.getString("dp_nama_s2").equals("null")) {
                                    pilihpendidikan.setText("S1");
                                } else if (movieObject.getString("dp_nama_s3").equals("null")) {
                                    pilihpendidikan.setText("S2");
                                } else {
                                    pilihpendidikan.setText("S3");
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                            form_interview_update.this);
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
                                    Intent intent = new Intent(form_interview_update.this, MainActivity.class);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

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
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());


        super.onDestroy();
    }
}