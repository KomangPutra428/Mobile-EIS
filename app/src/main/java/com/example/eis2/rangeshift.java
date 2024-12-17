package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.eis2.Item.biodatamodel;
import com.example.eis2.Item.namanikmodel;
import com.example.eis2.SearchSpinner.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.cuti.txt_nomor_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class rangeshift extends AppCompatActivity {
    private List<biodatamodel> movieItemList;
    private List<namanikmodel> jabatan3;

    private SearchableSpinner spinner, spinner2;
    ArrayList<String> Karyawan;
    ArrayList<String> jam;

    EditText nik, nama, jabatan, departement, lokasi, tanggalawal, tanggal2;
    private Calendar date;
    private SimpleDateFormat dateFormatter;
    Button pengajuan2;
    SharedPreferences sharedPreferences;
    TextView textView54, textView55, textView67;
    ProgressDialog pDialog;
    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        Calendar twoDaysAgo = (Calendar) currentDate.clone();
        twoDaysAgo.add(Calendar.DATE, 0);
        date = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                tanggalawal.setText(dateFormatter.format(date.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(rangeshift.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());
        datePickerDialog.show();
    }

    public void showDateTimePicker2() {
        final Calendar currentDate = Calendar.getInstance();
        Calendar twoDaysAgo = (Calendar) currentDate.clone();
        twoDaysAgo.add(Calendar.DATE, 0);
        date = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                tanggal2.setText(dateFormatter.format(date.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(rangeshift.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());
        datePickerDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rangeshift);
        HttpsTrustManager.allowAllSSL();

        nik = (EditText) findViewById(R.id.nik);
        nama = (EditText) findViewById(R.id.nama);
        jabatan = (EditText) findViewById(R.id.jabatan);
        departement = (EditText) findViewById(R.id.departement);
        lokasi = (EditText) findViewById(R.id.lokasi);
        tanggalawal = (EditText) findViewById(R.id.tanggalawal);
        tanggal2 = (EditText) findViewById(R.id.tanggalakhir);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        pengajuan2 = (Button) findViewById(R.id.pengajuan2);
        textView54 = (TextView) findViewById(R.id.textView54);
        textView55 = (TextView) findViewById(R.id.textView55);
        textView67 = (TextView) findViewById(R.id.textView67);


        Karyawan = new ArrayList<>();
        jabatan3 = new ArrayList<>();

        spinner = (SearchableSpinner) findViewById(R.id.karyawan);
        spinner2 = (SearchableSpinner) findViewById(R.id.jam);
        getJam();
        jam = new ArrayList<>();


        pengajuan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner2.getSelectedItem().toString().equals("Pilih Jam")){
                    Toast.makeText(getApplicationContext(), "Silahkan pilih jam", Toast.LENGTH_SHORT).show();
                } else {
                    pDialog = new ProgressDialog(rangeshift.this);
                    showDialog();
                    pDialog.setContentView(R.layout.progress_dialog);
                    pDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    pengajuan();
                }
            }
        });

        tanggalawal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                String inputString1 = tanggalawal.getText().toString();
                String inputString2 = tanggal2.getText().toString();
                try {
                    Date date1 = myFormat.parse(inputString1);
                    Date date2 = myFormat.parse(inputString2);
                    long diff = date2.getTime() - date1.getTime();
                    String a = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                    textView54.setText(a);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        tanggal2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                String inputString1 = tanggalawal.getText().toString();
                String inputString2 = tanggal2.getText().toString();
                try {
                    Date date1 = myFormat.parse(inputString1);
                    Date date2 = myFormat.parse(inputString2);
                    long diff = date2.getTime() - date1.getTime();
                    String a = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                    textView54.setText(a);
                } catch (ParseException e) {
                    e.printStackTrace();
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
                textView55.setText(jam2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getLokasi();
        movieItemList = new ArrayList<>();

        tanggalawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        tanggal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker2();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner.setTitle("Pilih karyawan");
                String karyawan = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
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

                    RequestQueue requestQueue = Volley.newRequestQueue(rangeshift.this);
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

                RequestQueue requestQueue = Volley.newRequestQueue(rangeshift.this);
                requestQueue.add(stringRequest);

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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
                    spinner2.setAdapter(new ArrayAdapter<String>(rangeshift.this, android.R.layout.simple_spinner_dropdown_item, jam));
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
                    spinner.setAdapter(new ArrayAdapter<String>(rangeshift.this, android.R.layout.simple_spinner_dropdown_item, Karyawan));
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void pengajuan() {
        final int num3 = Integer.valueOf(textView54.getText().toString());
        int i;
        String date = tanggalawal.getText().toString();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (i = 0; i <= num3; i++) {
            System.out.println("test =" + i);
            final String tanggal = sdf.format(c.getTime());
            c.add(Calendar.DATE, 1);
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Tanggal = " + tanggal);

            final int finalI = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/shifting/index",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (finalI == num3){
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                rangeshift.this.finish();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideDialog();
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

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
                    String nik2 = nik.getText().toString();
                    String nama2 = nama.getText().toString();
                    String jabatan2 = textView67.getText().toString();
                    String dept2 = departement.getText().toString();
                    String lokasi2 = lokasi.getText().toString();
                    String jam = textView55.getText().toString();

                    params.put("user_submit", nik_baru);

                    params.put("nik_shift", nik2);
                    params.put("nama_karyawan_shift", nama2);
                    params.put("jabatan_karyawan_shift", jabatan2);
                    params.put("dept_karyawan_shift", dept2);
                    params.put("lokasi_karyawan_shift", lokasi2);

                    params.put("jam_kerja", jam);
                    params.put("start_periode", "0");
                    params.put("end_periode", "0");
                    params.put("tanggal_shift", tanggal);

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
                    )
            );
            RequestQueue requestQueue2 = Volley.newRequestQueue(this);
            requestQueue2.add(stringRequest2);
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
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

        
        super.onDestroy();
    }
}
