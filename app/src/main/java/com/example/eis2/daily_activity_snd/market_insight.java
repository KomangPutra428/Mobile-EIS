package com.example.eis2.daily_activity_snd;

import static android.view.View.GONE;
import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.daily_activity_snd.weekly_planner_new.preventTwoClick;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.example.eis2.GPSTracker;
import com.example.eis2.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class market_insight extends AppCompatActivity {

    EditText text_market_daily;
    String codeReason;

    TextInputLayout otherLayout;

    private DecimalFormat decimalFormat;

    ProgressDialog pDialog;
    AutoCompleteTextView brand;

    AutoCompleteTextView edit_sku;
    EditText edit_harganormal;

    EditText edit_diskon;
    EditText edit_cashback;
    LocationManager locationManager;

    TextView hargabeli;
    EditText edit_hargajual;

    TextInputLayout textinput_brand, sku_layout;


    TextView margin;
    AutoCompleteTextView feedback, feedback_pelayanan, feedback_outlet;

    String langitude, longitude;

    Button batal;
    Button simpan;

    EditText otherCompetitor;

    ArrayList<String> brands = new ArrayList<>();
    ArrayList<String> skus = new ArrayList<>();

    ArrayList<String> feedbacks = new ArrayList<>();
    ArrayList<String> headerfeedbacks = new ArrayList<>();
    ArrayList<String> feedbacks_outlets = new ArrayList<>();

    int hargabelinet, hargamargin;

    EditText edit_qty;


    String harga_normal, potongan_diskon, kaskeluar, jual;

    ArrayList<String> GagalCheckin = new ArrayList<>();
    ArrayList<String> GagalCheckinCode = new ArrayList<>();

    CheckBox checkCompetitor;

    String[] items = {"Naik", "Stabil", "Turun"};
    AutoCompleteTextView aqua_minggu, kompetitor_minggu, aqua_minggu_reason, kompetitor_reason;

    ArrayList<String> alasanAqua = new ArrayList<>();
    ArrayList<String> alasanKompetitor = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_insight);
        hargabelinet = 0;
        hargamargin = 0;
        feedback_outlet = findViewById(R.id.feedback_outlet);
        checkCompetitor = findViewById(R.id.checkCompetitor);
        feedback_pelayanan = findViewById(R.id.feedback_pelayanan);

        aqua_minggu = findViewById(R.id.aqua_minggu);
        kompetitor_minggu = findViewById(R.id.kompetitor_minggu);

        aqua_minggu_reason = findViewById(R.id.aqua_minggu_reason);
        kompetitor_reason = findViewById(R.id.kompetitor_reason);

        aqua_minggu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(aqua_minggu.getText().toString().equals("Stabil")){
                    aqua_minggu_reason.setText("Penjualan Stabil");
                } else if (aqua_minggu.getText().toString().equals("Naik")){
                    aquaNaik();
                    aqua_minggu_reason.setText("");
                } else {
                    aquaTurun();
                    aqua_minggu_reason.setText("");
                }
            }
        });

        kompetitor_minggu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(kompetitor_minggu.getText().toString().equals("Stabil")){
                    kompetitor_reason.setText("Penjualan Stabil");
                } else if (kompetitor_minggu.getText().toString().equals("Naik")){
                    kompetitorNaik();
                    kompetitor_reason.setText("");
                } else {
                    kompetitorTurun();
                    kompetitor_reason.setText("");
                }
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items);
        aqua_minggu.setAdapter(adapter);
        kompetitor_minggu.setAdapter(adapter);


        edit_qty = findViewById(R.id.edit_qty);


        textinput_brand = findViewById(R.id.textinput_brand);
        sku_layout = findViewById(R.id.sku_layout);


        text_market_daily = findViewById(R.id.text_market_daily);
        brand = findViewById(R.id.brand);

        edit_sku = findViewById(R.id.edit_sku);
        edit_harganormal = findViewById(R.id.edit_harganormal);

        edit_diskon = findViewById(R.id.edit_diskon);
        edit_cashback = findViewById(R.id.edit_cashback);

        hargabeli = findViewById(R.id.hargabeli);
        edit_hargajual = findViewById(R.id.edit_hargajual);

        margin = findViewById(R.id.margin);
        feedback = findViewById(R.id.feedback);

        batal = findViewById(R.id.batal);
        simpan = findViewById(R.id.simpan);

        otherCompetitor=findViewById(R.id.otherCompetitor);
        otherLayout = findViewById(R.id.otherLayout);


        decimalFormat = new DecimalFormat("#,###,###,###,###");

        otherCompetitor.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }
            @Override
            public void afterTextChanged(Editable et) {
                String s=et.toString();
                if(!s.equals(s.toUpperCase()))
                {
                    s=s.toUpperCase();
                    otherCompetitor.setText(s);
                    otherCompetitor.setSelection(otherCompetitor.length()); //fix reverse texting
                }
            }
        });


        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preventTwoClick(v);
                finish();
            }
        });

        otherLayout.setVisibility(GONE);
        brand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(brand.getText().toString().contains("OTHER")){
                    otherLayout.setVisibility(View.VISIBLE);
                } else {
                    otherLayout.setVisibility(GONE);
                }
            }
        });


        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preventTwoClick(v);
                System.out.println("Margin Harga = " + hargamargin);
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                    if(checkCompetitor.isChecked()){
                        if (text_market_daily.getText().toString().length() == 0) {
                            text_market_daily.setError("Wajib di isi");
                        } else if (feedback_pelayanan.getText().toString().length() == 0) {
                            feedback_pelayanan.setError("Wajib di isi");
                        } else if (feedback.getText().toString().length() == 0) {
                                feedback.setError("Wajib di isi");
                        } else if (feedback_outlet.getText().toString().length() == 0) {
                            feedback_outlet.setError("Wajib di isi");
                        } else if (aqua_minggu_reason.getText().toString().length() == 0) {
                            aqua_minggu_reason.setError("Wajib di isi");
                        } else if (kompetitor_reason.getText().toString().length() == 0) {
                            kompetitor_reason.setError("Wajib di isi");
                        } else if (langitude == null || langitude.equals("0.0")) {
                            Toast.makeText(market_insight.this, "Lokasi Belum Ditemukan", Toast.LENGTH_SHORT).show();
                        } else {
                            if(getIntent().getStringExtra("type_customer").equals("NOO")){
                                postCustomer("0", "NOO", brand.getText().toString());
                            } else {
                                Location startPoint = new Location("locationA");
                                startPoint.setLatitude(Double.valueOf(langitude));
                                startPoint.setLongitude(Double.valueOf(longitude));

                                //LOCATION DARI TOKO
                                Location endPoint = new Location("locationA");
                                endPoint.setLatitude(Double.valueOf(weekly_planner_new.latitudetoko));
                                endPoint.setLongitude(Double.valueOf(weekly_planner_new.longitudetoko));
                                double distance2 = startPoint.distanceTo(endPoint);
                                int value = (int) distance2;

                                System.out.println("Jarak Distance location Current Lat = " + langitude);
                                System.out.println("Jarak Distance location Current Long = " + longitude);

                                System.out.println("Jarak Distance location Toko Lat = " + weekly_planner_new.latitudetoko);
                                System.out.println("Jarak Distance location Toko Long = " + weekly_planner_new.longitudetoko);

                                System.out.println("Jarak Distance = " + String.valueOf(value));

                                //JIKA ANDA BERADA DI LUAR RADIUS
                                if (value > 400) {
                                    showDialogCheckin();
                                } else {
                                    postCustomer("0", "In Radius", brand.getText().toString());
                                }
                            }
                        }
                    } else {
                        if (text_market_daily.getText().toString().length() == 0) {
                            text_market_daily.setError("Wajib di isi");
                        } else if (brand.getText().toString().length() == 0) {
                            brand.setError("Wajib di isi");
                        } else if (!brands.contains(brand.getText().toString())) {
                            brand.setText("");
                            brand.setError("Isi Data Dengan Benar");
                        } else if (edit_sku.getText().toString().length() == 0) {
                            edit_sku.setError("Wajib di isi");
                        } else if (edit_qty.getText().toString().length() == 0) {
                            edit_qty.setError("Wajib di isi");
                        } else if (edit_harganormal.getText().toString().length() == 0) {
                            edit_harganormal.setError("Wajib di isi");
                        } else if (edit_diskon.getText().toString().length() == 0) {
                            edit_diskon.setError("Wajib di isi");
                        } else if (edit_cashback.getText().toString().length() == 0) {
                            edit_cashback.setError("Wajib di isi");
                        } else if (edit_hargajual.getText().toString().length() == 0) {
                            edit_hargajual.setError("Wajib di isi");
                        } else if (feedback_pelayanan.getText().toString().length() == 0) {
                            feedback_pelayanan.setError("Wajib di isi");
                        } else if (feedback.getText().toString().length() == 0) {
                            feedback.setError("Wajib di isi");
                        } else if (feedback_outlet.getText().toString().length() == 0) {
                            feedback_outlet.setError("Wajib di isi");
                        } else if (aqua_minggu_reason.getText().toString().length() == 0) {
                            aqua_minggu_reason.setError("Wajib di isi");
                        } else if (kompetitor_reason.getText().toString().length() == 0) {
                            kompetitor_reason.setError("Wajib di isi");
                        } else if (hargamargin < 0) {
                            new SweetAlertDialog(market_insight.this, SweetAlertDialog.WARNING_TYPE)
                                    .setContentText("Harga margin tidak boleh lebih kecil dari harga beli net")
                                    .setConfirmText("OK")
                                    .show();
                        } else if (langitude == null || langitude.equals("0.0")) {
                            Toast.makeText(market_insight.this, "Lokasi Belum Ditemukan", Toast.LENGTH_SHORT).show();
                        } else {
                            if(brand.getText().toString().contains("OTHER")){
                                if(otherCompetitor.getText().toString().length() == 0){
                                    otherCompetitor.setError("Wajib Di isi");
                                } else {
                                    if(getIntent().getStringExtra("type_customer").equals("NOO")){
                                        postCustomer("0", "NOO", otherCompetitor.getText().toString());
                                    } else {
                                        Location startPoint = new Location("locationA");
                                        startPoint.setLatitude(Double.valueOf(langitude));
                                        startPoint.setLongitude(Double.valueOf(longitude));

                                        //LOCATION DARI TOKO
                                        Location endPoint = new Location("locationA");
                                        endPoint.setLatitude(Double.valueOf(weekly_planner_new.latitudetoko));
                                        endPoint.setLongitude(Double.valueOf(weekly_planner_new.longitudetoko));
                                        double distance2 = startPoint.distanceTo(endPoint);
                                        int value = (int) distance2;

                                        System.out.println("Jarak Distance location Current Lat = " + langitude);
                                        System.out.println("Jarak Distance location Current Long = " + longitude);

                                        System.out.println("Jarak Distance location Toko Lat = " + weekly_planner_new.latitudetoko);
                                        System.out.println("Jarak Distance location Toko Long = " + weekly_planner_new.longitudetoko);

                                        System.out.println("Jarak Distance = " + String.valueOf(value));

                                        //JIKA ANDA BERADA DI LUAR RADIUS
                                        if (value > 400) {
                                            showDialogCheckin();
                                        } else {
                                            postCustomer("0", "In Radius", otherCompetitor.getText().toString());
                                        }
                                    }

                                }
                            } else {
                                if(getIntent().getStringExtra("type_customer").equals("NOO")){
                                    postCustomer("0", "NOO", brand.getText().toString());
                                } else {
                                    Location startPoint = new Location("locationA");
                                    startPoint.setLatitude(Double.valueOf(langitude));
                                    startPoint.setLongitude(Double.valueOf(longitude));

                                    //LOCATION DARI TOKO
                                    Location endPoint = new Location("locationA");
                                    endPoint.setLatitude(Double.valueOf(weekly_planner_new.latitudetoko));
                                    endPoint.setLongitude(Double.valueOf(weekly_planner_new.longitudetoko));
                                    double distance2 = startPoint.distanceTo(endPoint);
                                    int value = (int) distance2;

                                    System.out.println("Jarak Distance location Current Lat = " + langitude);
                                    System.out.println("Jarak Distance location Current Long = " + longitude);

                                    System.out.println("Jarak Distance location Toko Lat = " + weekly_planner_new.latitudetoko);
                                    System.out.println("Jarak Distance location Toko Long = " + weekly_planner_new.longitudetoko);

                                    System.out.println("Jarak Distance = " + String.valueOf(value));

                                    //JIKA ANDA BERADA DI LUAR RADIUS
                                    if (value > 400) {
                                        showDialogCheckin();
                                    } else {
                                        postCustomer("0", "In Radius", brand.getText().toString());
                                    }
                                }

                            }
                        }
                    }
                }
            }
        });

        edit_harganormal.addTextChangedListener(new NumberTextWatcher());
        edit_diskon.addTextChangedListener(new NumberTextWatcherDiskon());
        edit_cashback.addTextChangedListener(new NumberTextWatcherCashBack());
        edit_hargajual.addTextChangedListener(new NumberTextWatcherJual());


        getCompetitor();
        getSKU();
        getFeedback("Feedback Pelayanan Dan Permintaan");
        getHeaderFeedback("Feedback Promo Dan Harga");
        getOutletFeedback("Feedback Issue Market Outlet");



        checkCompetitor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    textinput_brand.setEnabled(false);
                    sku_layout.setEnabled(false);
                    edit_qty.setEnabled(false);

                    otherCompetitor.setEnabled(false);
                    edit_sku.setEnabled(false);


                    edit_harganormal.setEnabled(false);
                    edit_diskon.setEnabled(false);
                    edit_cashback.setEnabled(false);
                    edit_hargajual.setEnabled(false);

                } else {
                    edit_qty.setEnabled(true);
                    textinput_brand.setEnabled(true);
                    sku_layout.setEnabled(true);

                    otherCompetitor.setEnabled(true);
                    edit_sku.setEnabled(true);

                    edit_harganormal.setEnabled(true);
                    edit_diskon.setEnabled(true);
                    edit_cashback.setEnabled(true);
                    edit_hargajual.setEnabled(true);

                }
            }
        });

    }

    private void kompetitorTurun() {
        alasanKompetitor.clear();
        StringRequest datacustomer = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_feedbackFooter?jenis=Penjualan Turun",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    alasanKompetitor.add(jsonObject1.getString("keterangan"));
                                    kompetitor_reason.setAdapter(new ArrayAdapter<String>(market_insight.this, android.R.layout.simple_expandable_list_item_1, alasanKompetitor));

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
        datacustomer.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestcustomer = Volley.newRequestQueue(market_insight.this);
        requestcustomer.add(datacustomer);
    }

    private void kompetitorNaik() {
        alasanKompetitor.clear();
        StringRequest datacustomer = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_feedbackFooter?jenis=Penjualan Naik",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    alasanKompetitor.add(jsonObject1.getString("keterangan"));
                                    kompetitor_reason.setAdapter(new ArrayAdapter<String>(market_insight.this, android.R.layout.simple_expandable_list_item_1, alasanKompetitor));

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
        datacustomer.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestcustomer = Volley.newRequestQueue(market_insight.this);
        requestcustomer.add(datacustomer);
    }

    private void aquaTurun() {
        alasanAqua.clear();
        StringRequest datacustomer = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_feedbackFooter?jenis=Penjualan Turun",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    alasanAqua.add(jsonObject1.getString("keterangan"));
                                    aqua_minggu_reason.setAdapter(new ArrayAdapter<String>(market_insight.this, android.R.layout.simple_expandable_list_item_1, alasanAqua));

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
        datacustomer.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestcustomer = Volley.newRequestQueue(market_insight.this);
        requestcustomer.add(datacustomer);
    }

    private void aquaNaik() {
        alasanAqua.clear();
        StringRequest datacustomer = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_feedbackFooter?jenis=Penjualan Naik",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    alasanAqua.add(jsonObject1.getString("keterangan"));
                                    aqua_minggu_reason.setAdapter(new ArrayAdapter<String>(market_insight.this, android.R.layout.simple_expandable_list_item_1, alasanAqua));

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
        datacustomer.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestcustomer = Volley.newRequestQueue(market_insight.this);
        requestcustomer.add(datacustomer);
    }

    private void getOutletFeedback(String feedback_issue_market_outlet) {
        StringRequest datacustomer = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_feedbackFooter?jenis="+feedback_issue_market_outlet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    feedbacks_outlets.add(jsonObject1.getString("keterangan"));
                                    feedback_outlet.setAdapter(new ArrayAdapter<String>(market_insight.this, android.R.layout.simple_expandable_list_item_1, feedbacks_outlets));

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
        datacustomer.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestcustomer = Volley.newRequestQueue(market_insight.this);
        requestcustomer.add(datacustomer);
    }

    private void getHeaderFeedback(String s) {
        StringRequest datacustomer = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_feedbackFooter?jenis="+s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    headerfeedbacks.add(jsonObject1.getString("keterangan"));
                                    feedback_pelayanan.setAdapter(new ArrayAdapter<String>(market_insight.this, android.R.layout.simple_expandable_list_item_1, headerfeedbacks));

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
        datacustomer.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestcustomer = Volley.newRequestQueue(market_insight.this);
        requestcustomer.add(datacustomer);
    }

    private void getFeedback(String s) {
        feedbacks.clear();
        StringRequest datacustomer = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_feedbackFooter?jenis="+s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    feedbacks.add(jsonObject1.getString("keterangan"));
                                    feedback.setAdapter(new ArrayAdapter<String>(market_insight.this, android.R.layout.simple_expandable_list_item_1, feedbacks));

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
        datacustomer.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestcustomer = Volley.newRequestQueue(market_insight.this);
        requestcustomer.add(datacustomer);
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getSKU() {
        StringRequest datacustomer = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_sku",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    skus.add(jsonObject1.getString("jenis_sku"));
                                    edit_sku.setAdapter(new ArrayAdapter<String>(market_insight.this, android.R.layout.simple_expandable_list_item_1, skus));

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
        datacustomer.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestcustomer = Volley.newRequestQueue(market_insight.this);
        requestcustomer.add(datacustomer);
    }

    private void showDialogCheckin() {
        final Dialog dialog = new Dialog(market_insight.this);
        dialog.setContentView(R.layout.gagal_checkin);
        dialog.setCancelable(false);
        dialog.show();
        GagalCheckin.clear();
        GagalCheckinCode.clear();

        AutoCompleteTextView alasangagalcheckin = dialog.findViewById(R.id.alasangagalcheckin);
        Button cancel = dialog.findViewById(R.id.cancel);
        Button simpan = dialog.findViewById(R.id.simpan);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(brand.getText().toString().contains("OTHER")){
                    if(alasangagalcheckin.getText().toString().length() == 0){
                        alasangagalcheckin.setError("Wajib Di isi");
                    } else {
                        postCustomer("1", alasangagalcheckin.getText().toString(), otherCompetitor.getText().toString());
                    }
                } else {
                    if(alasangagalcheckin.getText().toString().length() == 0){
                        alasangagalcheckin.setError("Wajib Di isi");
                    } else {
                        postCustomer("1", alasangagalcheckin.getText().toString(), brand.getText().toString());
                    }
                }

            }
        });

        alasangagalcheckin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                codeReason = GagalCheckinCode.get(position).toString();
            }
        });

        pDialog = new ProgressDialog(this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        StringRequest rest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_Check_In_Failed",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String id = jsonObject1.getString("szId");
                                    String jenis_istirahat = jsonObject1.getString("szName");
                                    GagalCheckin.add(jenis_istirahat);
                                    GagalCheckinCode.add(id);

                                }
                            }
                            alasangagalcheckin.setAdapter(new ArrayAdapter<String>(market_insight.this, R.layout.custom_expandable_list_item, GagalCheckin));
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
        rest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(market_insight.this);
        requestQueue.getCache().clear();
        requestQueue.add(rest);
    }


    private void getCompetitor() {
        StringRequest datacustomer = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_competitor",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    brands.add(jsonObject1.getString("produk"));
                                    brand.setAdapter(new ArrayAdapter<String>(market_insight.this, android.R.layout.simple_expandable_list_item_1, brands));

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
        datacustomer.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestcustomer = Volley.newRequestQueue(market_insight.this);
        requestcustomer.add(datacustomer);
    }

    private void postCustomer(String status_out, String kategori_reasonid, String brands){
        pDialog = new ProgressDialog(this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_DailyExternal",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        SweetAlertDialog Success = new SweetAlertDialog(market_insight.this, SweetAlertDialog.SUCCESS_TYPE);
                        Success.setContentText("Data Sudah Ditambahkan");
                        Success.setCancelable(false);
                        hideDialog();
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
                SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime = sdf.format(new Date());
                String id_toko;

                if(getIntent().getStringExtra("type_customer").equals("NOO")){
                    id_toko = "";
                } else {
                    String kategori_id = getIntent().getStringExtra("toko");
                    String[] splited_text = kategori_id.split(", ");
                    id_toko = splited_text[1];
                }


                params.put("nik_baru", nik_baru);
                params.put("lokasi", getIntent().getStringExtra("lokasi"));
                params.put("segment", getIntent().getStringExtra("segment"));
                params.put("id_customer", id_toko);
                params.put("ket_plan", getIntent().getStringExtra("ket_plan"));

                params.put("lat", langitude);
                params.put("lon", longitude);

                params.put("out_radius", status_out);
                params.put("ket_out_radius", kategori_reasonid);



                params.put("market_activity", text_market_daily.getText().toString());
                if(checkCompetitor.isChecked()){
                    params.put("brand", "TIDAK ADA KOMPETITOR");
                    params.put("sku", "TIDAK ADA KOMPETITOR");
                    params.put("harga_beli_normal", "0");
                    params.put("diskon", "0");
                    params.put("cashback", "0");
                    params.put("harga_beli_net", "0");
                    params.put("harga_jual", "0");
                    params.put("margin", "0");
                    params.put("qty", "0");

                } else {
                    params.put("brand", brands);
                    params.put("sku", edit_sku.getText().toString());
                    params.put("harga_beli_normal", edit_harganormal.getText().toString().replace(".", ""));
                    params.put("diskon", edit_diskon.getText().toString().replace(".", ""));
                    params.put("cashback", edit_cashback.getText().toString().replace(".", ""));
                    params.put("harga_beli_net", String.valueOf(hargabelinet));
                    params.put("harga_jual", edit_hargajual.getText().toString().replace(".", ""));
                    params.put("margin", String.valueOf(hargamargin));
                    params.put("qty", edit_qty.getText().toString());

                }

                params.put("feedback", feedback.getText().toString());
                params.put("feedback_2", feedback_pelayanan.getText().toString());
                params.put("feedback_3", feedback_outlet.getText().toString());
                params.put("feedback_4", aqua_minggu_reason.getText().toString());
                params.put("feedback_5", kompetitor_reason.getText().toString());

                SimpleDateFormat sdfclock = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String clocknow = sdfclock.format(new Date());

                params.put("in", getIntent().getStringExtra("in"));
                params.put("out", clocknow);


                params.put("type_customer", getIntent().getStringExtra("type_customer"));

                System.out.println(params);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(market_insight.this);
        requestQueue2.add(stringRequest2);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.setCancelable(false);
        pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void getLocation() {

        GPSTracker gpsTracker = new GPSTracker(market_insight.this);
        if(gpsTracker.canGetLocation()){
            langitude = (String.valueOf(gpsTracker.getLatitude()));
            longitude = (String.valueOf(gpsTracker.getLongitude()));
        }else{
            gpsTracker.showSettingsAlert();

        }

    }
    private class NumberTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            // No action needed before text changes
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            // No action needed when text is changing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Remove the existing TextWatcher to avoid infinite loop
            edit_harganormal.removeTextChangedListener(this);
            String originalText = editable.toString();
            String formattedText = formatNumber(originalText);
            edit_harganormal.setText(formattedText);
            edit_harganormal.setSelection(formattedText.length());
            edit_harganormal.addTextChangedListener(this);



            int harganormal = 0;
            int diskon = 0;
            int cashback = 0;

            try {
                if (originalText.length() > 0) {
                    harganormal = Integer.parseInt(originalText.replace(".", ""));
                }
            } catch (NumberFormatException e) {
                harganormal = 0;
                // For example, you can set harganormal to 0 or display an error message
            }

            try {
                if (edit_diskon.getText().length() > 0) {
                    diskon = Integer.parseInt(edit_diskon.getText().toString().replace(".", ""));
                }
            } catch (NumberFormatException e) {
                diskon = 0;
                // Handle the case where the input is not a valid number
            }

            try {
                if (edit_cashback.getText().length() > 0) {
                    cashback = Integer.parseInt(edit_cashback.getText().toString().replace(".", ""));
                }
            } catch (NumberFormatException e) {
                cashback = 0;
                // Handle the case where the input is not a valid number
            }


            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            kursIndonesia.setDecimalFormatSymbols(formatRp);

            if(edit_hargajual.getText().toString().length() == 0){
                hargamargin = 0;
            } else {
                hargamargin = Integer.parseInt(edit_hargajual.getText().toString().replace(".", "")) - hargabelinet;
            }

            hargabeli.setText(kursIndonesia.format(harganormal-diskon-cashback));
            hargabelinet = harganormal-diskon-cashback;
            margin.setText(kursIndonesia.format(hargamargin));
        }
    }

    private class NumberTextWatcherDiskon implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            // No action needed before text changes
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            // No action needed when text is changing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Remove the existing TextWatcher to avoid infinite loop
            edit_diskon.removeTextChangedListener(this);
            String originalText = editable.toString();
            String formattedText = formatNumber(originalText);
            edit_diskon.setText(formattedText);
            edit_diskon.setSelection(formattedText.length());
            edit_diskon.addTextChangedListener(this);

            int harganormal = 0;
            int diskon = 0;
            int cashback = 0;

            try {
                if (originalText.length() > 0) {
                    diskon = Integer.parseInt(originalText.replace(".", ""));
                }
            } catch (NumberFormatException e) {
                diskon = 0;
                // Handle the case where the input is not a valid number
            }

            try {
                if (edit_harganormal.getText().length() > 0) {
                    harganormal = Integer.parseInt(edit_harganormal.getText().toString().replace(".", ""));
                }
            } catch (NumberFormatException e) {
                harganormal = 0;
                // Handle the case where the input is not a valid number
            }

            try {
                if (edit_cashback.getText().length() > 0) {
                    cashback = Integer.parseInt(edit_cashback.getText().toString().replace(".", ""));
                }
            } catch (NumberFormatException e) {
                cashback = 0;
                // Handle the case where the input is not a valid number
            }

            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            kursIndonesia.setDecimalFormatSymbols(formatRp);



            if(edit_hargajual.getText().toString().length() == 0){
                hargamargin = 0;
            } else {
                hargamargin = Integer.parseInt(edit_hargajual.getText().toString().replace(".", "")) - hargabelinet;
            }

            hargabeli.setText(kursIndonesia.format(harganormal-diskon-cashback));
            hargabelinet = harganormal-diskon-cashback;
            margin.setText(kursIndonesia.format(hargamargin));
        }
    }

    private class NumberTextWatcherCashBack implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            // No action needed before text changes
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            // No action needed when text is changing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            edit_cashback.removeTextChangedListener(this);
            String originalText = editable.toString();
            String formattedText = formatNumber(originalText);
            edit_cashback.setText(formattedText);
            edit_cashback.setSelection(formattedText.length());
            edit_cashback.addTextChangedListener(this);

            int harganormal = 0;
            int diskon = 0;
            int cashback = 0;

            try {
                if (!originalText.isEmpty()) {
                    cashback = Integer.parseInt(originalText.replace(".", ""));
                }
            } catch (NumberFormatException e) {
                cashback = 0;
                // Handle the case where the input is not a valid number
            }

            try {
                if (!edit_harganormal.getText().toString().isEmpty()) {
                    harganormal = Integer.parseInt(edit_harganormal.getText().toString().replace(".", ""));
                }
            } catch (NumberFormatException e) {
                harganormal = 0;
                // Handle the case where the input is not a valid number
            }

            try {
                if (!edit_diskon.getText().toString().isEmpty()) {
                    diskon = Integer.parseInt(edit_diskon.getText().toString().replace(".", ""));
                }
            } catch (NumberFormatException e) {
                diskon = 0;
                // Handle the case where the input is not a valid number
            }


            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            kursIndonesia.setDecimalFormatSymbols(formatRp);



            if(edit_hargajual.getText().toString().length() == 0){
                hargamargin = 0;
            } else {
                hargamargin = Integer.parseInt(edit_hargajual.getText().toString().replace(".", "")) - hargabelinet;
            }

            hargabeli.setText(kursIndonesia.format(harganormal-diskon-cashback));
            hargabelinet = harganormal-diskon-cashback;
            margin.setText(kursIndonesia.format(hargamargin));
        }
    }

    private class NumberTextWatcherJual implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            // No action needed before text changes
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            // No action needed when text is changing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            edit_hargajual.removeTextChangedListener(this);
            String originalText = editable.toString();
            String formattedText = formatNumber(originalText);
            edit_hargajual.setText(formattedText);
            edit_hargajual.setSelection(formattedText.length());
            edit_hargajual.addTextChangedListener(this);

            try {
                if (!originalText.isEmpty()) {
                    int hargajual = Integer.parseInt(originalText.replace(".", ""));
                    hargamargin = hargajual - hargabelinet;
                }
            } catch (NumberFormatException e) {
                hargamargin = 0;
                e.printStackTrace(); // Log the error for debugging
            }

            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            kursIndonesia.setDecimalFormatSymbols(formatRp);

            margin.setText(kursIndonesia.format(hargamargin));
        }
    }


    private String formatNumber(String numberStr) {
        // Remove any existing thousand separators
        String cleanString = numberStr.replaceAll("\\.", "");

        // Parse the cleaned string to long
        long parsed;
        try {
            parsed = Long.parseLong(cleanString);
        } catch (NumberFormatException e) {
            parsed = 0; // Handle the case where the input is not a valid number
        }

        // Format the parsed long back to string with dots as thousand separators
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(Locale.getDefault());
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);

        return decimalFormat.format(parsed);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
    }



}