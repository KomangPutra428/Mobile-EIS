package com.example.eis2.daily_activity_snd;

import static android.view.View.GONE;
import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_lokasi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
import com.example.eis2.Item.Trace;
import com.example.eis2.Item.planmodels;
import com.example.eis2.Item.externalmodel;
import com.example.eis2.Item.tanggalmodel;
import com.example.eis2.R;
import com.example.eis2.clearancesheet;
import com.example.eis2.dinasfullday;
import com.example.eis2.form_mutasi;
import com.example.eis2.list_resign;
import com.example.eis2.menu;
import com.example.eis2.mutasi;
import com.example.eis2.weekly_planner;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.installations.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

public class weekly_planner_new extends AppCompatActivity {

    MaterialButton add, hapus, mulai, akhiri;
    String langitude, longitude;


    String type_customer[] = {
            "NOO",
            "Existing"
    };

    String statuschecked;

    int checked;

    RelativeLayout errorLayout;

    String lokasi;

    ArrayList<externalmodel> externalmodels = new ArrayList<>();
    ListViewAdapterDraftNew adapterDraft;

    static String latitudetoko;
    static String longitudetoko;


    ProgressDialog pDialog;
    ArrayList<String> segment = new ArrayList<>();
    ArrayList<String> customer = new ArrayList<>();
    private Calendar date;

    ArrayList<String> address = new ArrayList<>();
    ArrayList<String> kelurahanList = new ArrayList<>();

    ArrayList<String> depo = new ArrayList<>();
    ArrayList<String> code_depo = new ArrayList<>();

    ArrayList<String> latitude_toko = new ArrayList<>();
    ArrayList<String> longitude_toko = new ArrayList<>();

    ArrayList<String> numbertoko = new ArrayList<>();

    RecyclerView draft;
    MaterialToolbar dailynBar;

    CheckBox checkall;
    ImageButton filtering;

    String tanggal;

    private SimpleDateFormat dateFormatter;
    LocationManager locationManager;
    private GPSTracker gpsTracker;
    private final Handler handler = new Handler();
    private Runnable refresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_planner_new);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        tanggal = sdf.format(new Date());
        dailynBar = findViewById(R.id.dailynBar);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        }

        dailynBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        gpsTracker = new GPSTracker(weekly_planner_new.this);

        getLocation();






        hapus = findViewById(R.id.hapus);
        add = findViewById(R.id.add);
        draft = findViewById(R.id.draft);
        checkall = findViewById(R.id.checkall);
        filtering = findViewById(R.id.filtering);

        mulai = findViewById(R.id.mulai);
        akhiri = findViewById(R.id.akhiri);


        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        errorLayout = findViewById(R.id.errorLayout);

        checkall.setText("Checklist All (0)");


        filtering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preventTwoClick(v);
                final Dialog dialog = new Dialog(weekly_planner_new.this);
                dialog.setContentView(R.layout.filter_tanggal);

                EditText edit_tanggal = dialog.findViewById(R.id.edit_tanggal);
                Button batal = dialog.findViewById(R.id.batal);
                Button ok = dialog.findViewById(R.id.ok);

                final Calendar currentDate = Calendar.getInstance();
                Calendar twoDaysAgo = (Calendar) currentDate.clone();
                twoDaysAgo.add(Calendar.DATE, 0);

                date = Calendar.getInstance();

                edit_tanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventTwoClick(v);
                        final Calendar currentDate = Calendar.getInstance();
                        Calendar twoDaysAgo = (Calendar) currentDate.clone();

                        date = Calendar.getInstance();

                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth );

                                edit_tanggal.setText(dateFormatter.format(date.getTime()));
                            }
                        };
                        DatePickerDialog datePickerDialog = new  DatePickerDialog(weekly_planner_new.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(twoDaysAgo.getTimeInMillis());
                        datePickerDialog.show();


                    }
                });

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventTwoClick(v);
                        dialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventTwoClick(v);
                        if(edit_tanggal.getText().toString().length() == 0){
                            edit_tanggal.setError("Wajib Di isi");
                        } else {
                            dialog.dismiss();
                            tanggal = convertFormat2(edit_tanggal.getText().toString());
                            getDraft();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String curdate = sdf.format(new Date());

                            if(curdate.equals(tanggal)){
                                checkall.setVisibility(View.VISIBLE);
                                add.setVisibility(View.VISIBLE);
                                hapus.setEnabled(true);
                                hapus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FB4141")));
                            } else {
                                checkall.setVisibility(GONE);
                                add.setVisibility(GONE);
                                hapus.setEnabled(false);
                                hapus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EDEDED")));
                            }

                        }
                    }
                });



                dialog.show();
            }
        });

        checkall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    statuschecked = "auto";
                    checked = 0;

//                    for(int i = 0; i <= adapterDraft.getItemCount() - 1; i++){
//                        if(adapterDraft.getItem(i).getSelected() == true){
//
//                        }
//                    }

                    for(externalmodel person:externalmodels){
                        person.setSelected(true);
                        adapterDraft.notifyDataSetChanged();

                        adapterDraft = new ListViewAdapterDraftNew(weekly_planner_new.this, externalmodels);
                        draft.setItemViewCacheSize(externalmodels.size());
                        draft.setAdapter(adapterDraft);

                        checked ++;
                        System.out.println("Hasil Checked = " + checked);
                        checkall.setText("Checklist All" + " (" +String.valueOf(checked) + ")");





                    }


                } else if (!b) {
                    if(statuschecked.equals("manual")){
                        statuschecked = "auto";
                    } else {
                        checked = 0;
                        for(externalmodel person:externalmodels){
                            person.setSelected(false);
                            adapterDraft.notifyDataSetChanged();

                            adapterDraft = new ListViewAdapterDraftNew(weekly_planner_new.this, externalmodels);
                            draft.setItemViewCacheSize(externalmodels.size());
                            draft.setAdapter(adapterDraft);

                            checkall.setText("Checklist All" + " (" +"0" + ")");
                        }
                    }

                }

            }
        });


        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preventTwoClick(v);
                for(int i = 0; i <= adapterDraft.getItemCount() - 1; i++){
                    if(adapterDraft.getItem(i).getSelected() == true){
                        hapusData();
                        break;
                    } else if(i == adapterDraft.getItemCount() - 1){
                        new SweetAlertDialog(weekly_planner_new.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Silahkan Ceklis Data Terlebih Dahulu ")
                                .setConfirmText("OK")
                                .show();
                    }
                }
            }


        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preventTwoClick(v);





                final Dialog dialog = new Dialog(weekly_planner_new.this);
                dialog.setContentView(R.layout.add_daily_snd);
                dialog.show();

                segment.clear();
                customer.clear();
                address.clear();

                TextInputLayout depoLayout = dialog.findViewById(R.id.depoLayout);
                TextInputLayout editLayout = dialog.findViewById(R.id.editLayout);
                TextInputLayout pilihtoko_layout = dialog.findViewById(R.id.pilihtoko_layout);
                TextInputLayout kelurahanlayout = dialog.findViewById(R.id.kelurahanlayout);


                EditText edit_lokasi = dialog.findViewById(R.id.edit_lokasi);
                AutoCompleteTextView pilih_segment = dialog.findViewById(R.id.pilih_segment);
                AutoCompleteTextView pilih_toko = dialog.findViewById(R.id.pilih_toko);
                AutoCompleteTextView pilih_kelurahan = dialog.findViewById(R.id.pilih_kelurahan);


                EditText catatan = dialog.findViewById(R.id.catatan);

                AutoCompleteTextView pilih_depo = dialog.findViewById(R.id.pilih_depo);
                AutoCompleteTextView pilih_type = dialog.findViewById(R.id.pilih_type);

                Button batal= dialog.findViewById(R.id.batal);
                Button tambahkan= dialog.findViewById(R.id.tambahkan);

                catatan.addTextChangedListener(new TextWatcher() {

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
                            catatan.setText(s);
                            catatan.setSelection(catatan.length()); //fix reverse texting
                        }
                    }
                });



                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext() ,android.R.layout.simple_list_item_1, type_customer);

                //Getting the instance of AutoCompleteTextView

                pilih_type.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

                if(menu.txt_lokasi.getText().toString().equals("Pusat") || text_jabatan.getText().toString().equals("264") || text_jabatan.getText().toString().equals("503")){
                    editLayout.setVisibility(GONE);
                } else if(text_jabatan.getText().toString().equals("278")){
                    editLayout.setVisibility(GONE);
                } else if(text_jabatan.getText().toString().equals("267")){
                    editLayout.setVisibility(GONE);
                } else if(text_jabatan.getText().toString().equals("250")){
                    editLayout.setVisibility(GONE);
                } else if(text_jabatan.getText().toString().equals("251")){
                    editLayout.setVisibility(GONE);
                } else if(text_jabatan.getText().toString().equals("252")){
                    editLayout.setVisibility(GONE);
                } else if(text_jabatan.getText().toString().equals("418")){
                    editLayout.setVisibility(GONE);
                } else if(text_jabatan.getText().toString().equals("462")){
                    editLayout.setVisibility(GONE);
                } else if(text_jabatan.getText().toString().equals("467")){
                    editLayout.setVisibility(GONE);
                } else if(text_jabatan.getText().toString().equals("468")){
                    editLayout.setVisibility(GONE);
                } else if(text_jabatan.getText().toString().equals("253")){
                    editLayout.setVisibility(GONE);
                } else {
                    depoLayout.setVisibility(GONE);
                }

                pilih_toko.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String kategori_id = pilih_toko.getText().toString();
                        String[] splited_text = kategori_id.split(", ");
                        String id_toko = splited_text[1];
                        pDialog = new ProgressDialog(weekly_planner_new.this);
                        showDialog();
                        pDialog.setContentView(R.layout.progress_dialog);
                        pDialog.getWindow().setBackgroundDrawableResource(
                                android.R.color.transparent
                        );

                        StringRequest namatoko = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customerId?szId=" + id_toko,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        pDialog.dismiss();

                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            JSONArray movieArray = obj.getJSONArray("data");

                                            for (int i = 0; i < movieArray.length(); i++) {

                                                JSONObject movieObject = movieArray.getJSONObject(i);

                                                latitudetoko = movieObject.getString("szLatitude");
                                                longitudetoko = movieObject.getString("szLongitude");

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
                        namatoko.setRetryPolicy(
                                new DefaultRetryPolicy(
                                        500000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                )
                        );
                        RequestQueue requestToko = Volley.newRequestQueue(weekly_planner_new.this);
                        requestToko.add(namatoko);

                    }
                });



                pilih_kelurahan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        {
                            pDialog = new ProgressDialog(weekly_planner_new.this);
                            showDialog();
                            pDialog.setContentView(R.layout.progress_dialog);
                            pDialog.getWindow().setBackgroundDrawableResource(
                                    android.R.color.transparent
                            );

                            customer.clear();
                            pilih_toko.setText("");
                            String URL;
                            if(menu.txt_lokasi.getText().toString().equals("Pusat") || text_jabatan.getText().toString().equals("264") || text_jabatan.getText().toString().equals("503")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer_retail?szSoldToBranchId=" + lokasi +"&szSubDistrict=" + pilih_kelurahan.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("278")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer_retail?szSoldToBranchId=" + lokasi +"&szSubDistrict=" + pilih_kelurahan.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("267")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer_retail?szSoldToBranchId=" + lokasi +"&szSubDistrict=" + pilih_kelurahan.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("250")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer_retail?szSoldToBranchId=" + lokasi +"&szSubDistrict=" + pilih_kelurahan.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("251")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer_retail?szSoldToBranchId=" + lokasi +"&szSubDistrict=" + pilih_kelurahan.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("252")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer_retail?szSoldToBranchId=" + lokasi +"&szSubDistrict=" + pilih_kelurahan.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("418")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer_retail?szSoldToBranchId=" + lokasi +"&szSubDistrict=" + pilih_kelurahan.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("462")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer_retail?szSoldToBranchId=" + lokasi +"&szSubDistrict=" + pilih_kelurahan.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("467")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer_retail?szSoldToBranchId=" + lokasi +"&szSubDistrict=" + pilih_kelurahan.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("468")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer_retail?szSoldToBranchId=" + lokasi +"&szSubDistrict=" + pilih_kelurahan.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("253")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer_retail?szSoldToBranchId=" + lokasi +"&szSubDistrict=" + pilih_kelurahan.getText().toString();
                            } else {
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer_retail?szSoldToBranchId=" + menu.branchLokasi +"&szSubDistrict=" + pilih_kelurahan.getText().toString();
                            }

                            StringRequest datacustomer = new StringRequest(Request.Method.GET, URL,
                                    new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {
                                            pDialog.dismiss();
                                            customer.clear();
                                            numbertoko.clear();
                                            address.clear();
                                            latitude_toko.clear();
                                            longitude_toko.clear();
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (jsonObject.getString("status").equals("true")) {
                                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                    pilih_toko.setAdapter(new ArrayAdapter<String>(weekly_planner_new.this, android.R.layout.simple_expandable_list_item_1, customer));

                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                                        customer.add(jsonObject1.getString("szName")+ ", " + jsonObject1.getString("szId"));
                                                        address.add(jsonObject1.getString("szAddress"));

                                                        latitude_toko.add(jsonObject1.getString("szLatitude"));
                                                        longitude_toko.add(jsonObject1.getString("szLongitude"));


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
                                            hideDialog();
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
                            RequestQueue requestcustomer = Volley.newRequestQueue(weekly_planner_new.this);
                            requestcustomer.add(datacustomer);
                        }
                    }
                });

                pilih_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(pilih_type.getText().toString().equals("NOO")){
                            pilihtoko_layout.setVisibility(GONE);
                            kelurahanlayout.setVisibility(GONE);
                        } else {
                            pilihtoko_layout.setVisibility(View.VISIBLE);
                            if(pilih_segment.getText().toString().equals("RETAIL")){
                                kelurahanlayout.setVisibility(View.VISIBLE);
                                String URL_location;
                                if(menu.txt_lokasi.getText().toString().equals("Pusat") || text_jabatan.getText().toString().equals("264") || text_jabatan.getText().toString().equals("503")){
                                    URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                                } else if(text_jabatan.getText().toString().equals("278")){
                                    URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                                } else if(text_jabatan.getText().toString().equals("267")){
                                    URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                                } else if(text_jabatan.getText().toString().equals("250")){
                                    URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                                } else if(text_jabatan.getText().toString().equals("251")){
                                    URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                                } else if(text_jabatan.getText().toString().equals("252")){
                                    URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                                } else if(text_jabatan.getText().toString().equals("418")){
                                    URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                                } else if(text_jabatan.getText().toString().equals("462")){
                                    URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                                } else if(text_jabatan.getText().toString().equals("467")){
                                    URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                                } else if(text_jabatan.getText().toString().equals("468")){
                                    URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                                } else if(text_jabatan.getText().toString().equals("253")){
                                    URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                                } else {
                                    URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + menu.branchLokasi;
                                }
                                kelurahanListing6(URL_location);
                            } else {
                                kelurahanlayout.setVisibility(View.GONE);
                            }


                        }
                    }

                    private void kelurahanListing6(String url_location) {
                        {
                            pDialog = new ProgressDialog(weekly_planner_new.this);
                            showDialog();
                            pDialog.setContentView(R.layout.progress_dialog);
                            pDialog.getWindow().setBackgroundDrawableResource(
                                    android.R.color.transparent
                            );

                            StringRequest datasegment = new StringRequest(Request.Method.GET, url_location,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            pDialog.dismiss();
                                            pilih_toko.setText("");
                                            customer.clear();
                                            numbertoko.clear();
                                            address.clear();
                                            latitude_toko.clear();
                                            longitude_toko.clear();
                                            pilih_kelurahan.setText("");
                                            kelurahanList.clear();
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (jsonObject.getString("status").equals("true")) {
                                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                        kelurahanList.add(jsonObject1.getString("szSubDistrict"));

                                                        pilih_kelurahan.setAdapter(new ArrayAdapter<String>(weekly_planner_new.this, android.R.layout.simple_expandable_list_item_1, kelurahanList));

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
                            datasegment.setRetryPolicy(new DefaultRetryPolicy(
                                    500000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            RequestQueue requestsegment = Volley.newRequestQueue(weekly_planner_new.this);
                            requestsegment.add(datasegment);

                        }
                    }

                    private void kelurahanListing5(String url_location) {
                        {
                            pDialog = new ProgressDialog(weekly_planner_new.this);
                            showDialog();
                            pDialog.setContentView(R.layout.progress_dialog);
                            pDialog.getWindow().setBackgroundDrawableResource(
                                    android.R.color.transparent
                            );

                            StringRequest datasegment = new StringRequest(Request.Method.GET, url_location,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            pDialog.dismiss();
                                            pilih_toko.setText("");
                                            customer.clear();
                                            numbertoko.clear();
                                            address.clear();
                                            latitude_toko.clear();
                                            longitude_toko.clear();
                                            pilih_kelurahan.setText("");
                                            kelurahanList.clear();
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (jsonObject.getString("status").equals("true")) {
                                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                        kelurahanList.add(jsonObject1.getString("szSubDistrict"));

                                                        pilih_kelurahan.setAdapter(new ArrayAdapter<String>(weekly_planner_new.this, android.R.layout.simple_expandable_list_item_1, kelurahanList));

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
                            datasegment.setRetryPolicy(new DefaultRetryPolicy(
                                    500000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            RequestQueue requestsegment = Volley.newRequestQueue(weekly_planner_new.this);
                            requestsegment.add(datasegment);

                        }
                    }
                });

                pilih_segment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(pilih_segment.getText().toString().equals("RETAIL") && pilih_type.getText().toString().equals("Existing")){
                            kelurahanlayout.setVisibility(View.VISIBLE);
                            String URL_location;
                            if(menu.txt_lokasi.getText().toString().equals("Pusat") || text_jabatan.getText().toString().equals("264") || text_jabatan.getText().toString().equals("503")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("278")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("267")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("250")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("251")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("252")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("418")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("462")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("467")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("468")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("253")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else {
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + menu.branchLokasi;
                            }
                            kelurahanListing(URL_location);
                        } else {
                            kelurahanlayout.setVisibility(View.GONE);
                            pDialog = new ProgressDialog(weekly_planner_new.this);
                            showDialog();
                            pDialog.setContentView(R.layout.progress_dialog);
                            pDialog.getWindow().setBackgroundDrawableResource(
                                    android.R.color.transparent
                            );

                            customer.clear();
                            pilih_toko.setText("");
                            String URL;
                            if(menu.txt_lokasi.getText().toString().equals("Pusat") || text_jabatan.getText().toString().equals("264") || text_jabatan.getText().toString().equals("503")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer?szSoldToBranchId=" + lokasi +"&szclass=" + pilih_segment.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("278")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer?szSoldToBranchId=" + lokasi +"&szclass=" + pilih_segment.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("267")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer?szSoldToBranchId=" + lokasi +"&szclass=" + pilih_segment.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("250")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer?szSoldToBranchId=" + lokasi +"&szclass=" + pilih_segment.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("251")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer?szSoldToBranchId=" + lokasi +"&szclass=" + pilih_segment.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("252")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer?szSoldToBranchId=" + lokasi +"&szclass=" + pilih_segment.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("418")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer?szSoldToBranchId=" + lokasi +"&szclass=" + pilih_segment.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("462")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer?szSoldToBranchId=" + lokasi +"&szclass=" + pilih_segment.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("467")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer?szSoldToBranchId=" + lokasi +"&szclass=" + pilih_segment.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("468")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer?szSoldToBranchId=" + lokasi +"&szclass=" + pilih_segment.getText().toString();
                            } else if(text_jabatan.getText().toString().equals("253")){
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer?szSoldToBranchId=" + lokasi +"&szclass=" + pilih_segment.getText().toString();
                            } else {
                                URL = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer?szSoldToBranchId=" + menu.branchLokasi +"&szclass=" + pilih_segment.getText().toString();
                            }

                            StringRequest datacustomer = new StringRequest(Request.Method.GET, URL,
                                    new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {
                                            pDialog.dismiss();
                                            customer.clear();
                                            numbertoko.clear();
                                            address.clear();
                                            latitude_toko.clear();
                                            longitude_toko.clear();
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (jsonObject.getString("status").equals("true")) {
                                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                    pilih_toko.setAdapter(new ArrayAdapter<String>(weekly_planner_new.this, android.R.layout.simple_expandable_list_item_1, customer));

                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                                        customer.add(jsonObject1.getString("szName")+ ", " + jsonObject1.getString("szId"));
                                                        address.add(jsonObject1.getString("szAddress"));

                                                        latitude_toko.add(jsonObject1.getString("szLatitude"));
                                                        longitude_toko.add(jsonObject1.getString("szLongitude"));


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
                                            hideDialog();
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
                            RequestQueue requestcustomer = Volley.newRequestQueue(weekly_planner_new.this);
                            requestcustomer.add(datacustomer);
                        }


                    }

                    private void kelurahanListing(String url_location) {
                        pDialog = new ProgressDialog(weekly_planner_new.this);
                        showDialog();
                        pDialog.setContentView(R.layout.progress_dialog);
                        pDialog.getWindow().setBackgroundDrawableResource(
                                android.R.color.transparent
                        );

                        StringRequest datasegment = new StringRequest(Request.Method.GET, url_location,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        pDialog.dismiss();
                                        pilih_toko.setText("");
                                        customer.clear();
                                        numbertoko.clear();
                                        address.clear();
                                        latitude_toko.clear();
                                        longitude_toko.clear();
                                        pilih_kelurahan.setText("");
                                        kelurahanList.clear();
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getString("status").equals("true")) {
                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                    kelurahanList.add(jsonObject1.getString("szSubDistrict"));

                                                    pilih_kelurahan.setAdapter(new ArrayAdapter<String>(weekly_planner_new.this, android.R.layout.simple_expandable_list_item_1, kelurahanList));

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
                        datasegment.setRetryPolicy(new DefaultRetryPolicy(
                                500000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        RequestQueue requestsegment = Volley.newRequestQueue(weekly_planner_new.this);
                        requestsegment.add(datasegment);

                    }
                });

                pilih_depo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pDialog = new ProgressDialog(weekly_planner_new.this);
                        showDialog();
                        pDialog.setContentView(R.layout.progress_dialog);
                        pDialog.getWindow().setBackgroundDrawableResource(
                                android.R.color.transparent
                        );

                        if(pilih_segment.getText().toString().equals("RETAIL")){
                            kelurahanlayout.setVisibility(View.VISIBLE);
                            String URL_location;
                            if(menu.txt_lokasi.getText().toString().equals("Pusat") || text_jabatan.getText().toString().equals("264") || text_jabatan.getText().toString().equals("503")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("278")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("267")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("250")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("251")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("252")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("418")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("462")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("467")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("468")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else if(text_jabatan.getText().toString().equals("253")){
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + lokasi;
                            } else {
                                URL_location = "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kelurahan?kode_dms=" + menu.branchLokasi;
                            }
                            kelurahanListing10(URL_location);
                        } else {
                            kelurahanlayout.setVisibility(View.GONE);
                        }

                        if(menu.txt_lokasi.getText().toString().equals("Pusat") || text_jabatan.getText().toString().equals("264") || text_jabatan.getText().toString().equals("503")){
                            lokasi = code_depo.get(position).toString();
                        } else if(text_jabatan.getText().toString().equals("278")){
                            lokasi = code_depo.get(position).toString();
                        } else if(text_jabatan.getText().toString().equals("267")){
                            lokasi = code_depo.get(position).toString();
                        } else if(text_jabatan.getText().toString().equals("250")){
                            lokasi = code_depo.get(position).toString();
                        } else if(text_jabatan.getText().toString().equals("251")){
                            lokasi = code_depo.get(position).toString();
                        } else if(text_jabatan.getText().toString().equals("252")){
                            lokasi = code_depo.get(position).toString();
                        } else if(text_jabatan.getText().toString().equals("418")){
                            lokasi = code_depo.get(position).toString();
                        } else if(text_jabatan.getText().toString().equals("462")){
                            lokasi = code_depo.get(position).toString();
                        } else if(text_jabatan.getText().toString().equals("467")){
                            lokasi = code_depo.get(position).toString();
                        } else if(text_jabatan.getText().toString().equals("468")){
                            lokasi = code_depo.get(position).toString();
                        } else if(text_jabatan.getText().toString().equals("253")){
                            lokasi = code_depo.get(position).toString();
                        } else {
                            lokasi = menu.branchLokasi;
                        }

                        customer.clear();
                        pilih_toko.setText("");
                        StringRequest datacustomer = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customer?szSoldToBranchId=" + code_depo.get(position).toString() +"&szclass=" + pilih_segment.getText().toString(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        pDialog.dismiss();
                                        customer.clear();
                                        address.clear();
                                        latitude_toko.clear();
                                        numbertoko.clear();
                                        longitude_toko.clear();
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getString("status").equals("true")) {
                                                pilih_toko.setAdapter(new ArrayAdapter<String>(weekly_planner_new.this, android.R.layout.simple_expandable_list_item_1, customer));

                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                    customer.add(jsonObject1.getString("szName")+ ", " + jsonObject1.getString("szId"));
                                                    address.add(jsonObject1.getString("szAddress"));
                                                    latitude_toko.add(jsonObject1.getString("szLatitude"));
                                                    longitude_toko.add(jsonObject1.getString("szLongitude"));
                                                    numbertoko.add(String.valueOf(i));
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
                                        pDialog.dismiss();
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
                        RequestQueue requestcustomer = Volley.newRequestQueue(weekly_planner_new.this);
                        requestcustomer.add(datacustomer);

                    }

                    private void kelurahanListing10(String url_location) {
                        {
                            pDialog = new ProgressDialog(weekly_planner_new.this);
                            showDialog();
                            pDialog.setContentView(R.layout.progress_dialog);
                            pDialog.getWindow().setBackgroundDrawableResource(
                                    android.R.color.transparent
                            );

                            StringRequest datasegment = new StringRequest(Request.Method.GET, url_location,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            pDialog.dismiss();
                                            pilih_toko.setText("");
                                            customer.clear();
                                            numbertoko.clear();
                                            address.clear();
                                            latitude_toko.clear();
                                            longitude_toko.clear();
                                            pilih_kelurahan.setText("");
                                            kelurahanList.clear();
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (jsonObject.getString("status").equals("true")) {
                                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                        kelurahanList.add(jsonObject1.getString("szSubDistrict"));

                                                        pilih_kelurahan.setAdapter(new ArrayAdapter<String>(weekly_planner_new.this, android.R.layout.simple_expandable_list_item_1, kelurahanList));

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
                            datasegment.setRetryPolicy(new DefaultRetryPolicy(
                                    500000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            RequestQueue requestsegment = Volley.newRequestQueue(weekly_planner_new.this);
                            requestsegment.add(datasegment);

                        }
                    }
                });

                tambahkan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventTwoClick(v);
                        if(pilih_segment.getText().toString().equals("RETAIL") && pilih_type.getText().toString().equals("Existing")){
                            if(edit_lokasi.getText().toString().length() == 0){
                                edit_lokasi.setError("Isi Lokasi");
                            } else if(pilih_segment.getText().toString().length() == 0){
                                pilih_segment.setError("Pilih Segment");
                            } else if(pilih_type.getText().toString().length() == 0){
                                pilih_type.setError("Pilih Tipe Customer");
                            }  else if(catatan.getText().toString().length() == 0){
                                catatan.setError("Isi Catatan");
                            } else if(pilih_kelurahan.getText().toString().length() == 0){
                                pilih_kelurahan.setError("Isi Kelurahan");
                            } else {
                                if(!pilih_type.getText().toString().equals("NOO")){
                                    if(pilih_toko.getText().toString().length() == 0){
                                        pilih_toko.setError("Pilih Toko");
                                    }  else if(!customer.contains(pilih_toko.getText().toString())){
                                        pilih_toko.setText("");
                                        pilih_toko.setError("Isi Toko Dengan Benar");
                                    } else {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String currentDateandTime = sdf.format(new Date());

                                        Intent intent = new Intent(getApplicationContext(), market_insight.class);
                                        intent.putExtra("in", currentDateandTime);
                                        intent.putExtra("ket_plan", catatan.getText().toString());
                                        intent.putExtra("toko", pilih_toko.getText().toString());
                                        intent.putExtra("segment", pilih_segment.getText().toString());
                                        intent.putExtra("type_customer", pilih_type.getText().toString());



                                        if(menu.txt_lokasi.getText().toString().equals("Pusat") || text_jabatan.getText().toString().equals("264") || text_jabatan.getText().toString().equals("503")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("278")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("267")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        }  else if(text_jabatan.getText().toString().equals("250")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("251")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("252")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("418")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("462")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("467")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("468")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("253")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else {
                                            intent.putExtra("lokasi", edit_lokasi.getText().toString());
                                        }

                                        startActivity(intent);
                                        dialog.dismiss();
                                    }
                                } else {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String currentDateandTime = sdf.format(new Date());

                                    Intent intent = new Intent(getApplicationContext(), market_insight.class);
                                    intent.putExtra("in", currentDateandTime);
                                    intent.putExtra("ket_plan", catatan.getText().toString());
                                    intent.putExtra("toko", "");
                                    intent.putExtra("segment", pilih_segment.getText().toString());
                                    intent.putExtra("type_customer", pilih_type.getText().toString());



                                    if(menu.txt_lokasi.getText().toString().equals("Pusat") || text_jabatan.getText().toString().equals("264") || text_jabatan.getText().toString().equals("503")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("278")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("267")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("250")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("251")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("252")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("418")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("462")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("467")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("468")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("253")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else {
                                        intent.putExtra("lokasi", edit_lokasi.getText().toString());
                                    }

                                    startActivity(intent);
                                    dialog.dismiss();
                                }

                            }
                        } else {
                            if(edit_lokasi.getText().toString().length() == 0){
                                edit_lokasi.setError("Isi Lokasi");
                            } else if(pilih_segment.getText().toString().length() == 0){
                                pilih_segment.setError("Pilih Segment");
                            } else if(pilih_type.getText().toString().length() == 0){
                                pilih_type.setError("Pilih Tipe Customer");
                            }  else if(catatan.getText().toString().length() == 0){
                                catatan.setError("Isi Catatan");
                            } else {
                                if(!pilih_type.getText().toString().equals("NOO")){
                                    if(pilih_toko.getText().toString().length() == 0){
                                        pilih_toko.setError("Pilih Toko");
                                    }  else if(!customer.contains(pilih_toko.getText().toString())){
                                        pilih_toko.setText("");
                                        pilih_toko.setError("Isi Toko Dengan Benar");
                                    } else {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String currentDateandTime = sdf.format(new Date());

                                        Intent intent = new Intent(getApplicationContext(), market_insight.class);
                                        intent.putExtra("in", currentDateandTime);                                        intent.putExtra("ket_plan", catatan.getText().toString());
                                        intent.putExtra("toko", pilih_toko.getText().toString());
                                        intent.putExtra("segment", pilih_segment.getText().toString());
                                        intent.putExtra("type_customer", pilih_type.getText().toString());



                                        if(menu.txt_lokasi.getText().toString().equals("Pusat") || text_jabatan.getText().toString().equals("264") || text_jabatan.getText().toString().equals("503")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("278")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("267")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        }  else if(text_jabatan.getText().toString().equals("250")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("251")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("252")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("418")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("462")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("467")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("468")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else if(text_jabatan.getText().toString().equals("253")){
                                            intent.putExtra("lokasi", pilih_depo.getText().toString());
                                        } else {
                                            intent.putExtra("lokasi", edit_lokasi.getText().toString());
                                        }

                                        startActivity(intent);
                                        dialog.dismiss();
                                    }
                                } else {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String currentDateandTime = sdf.format(new Date());

                                    Intent intent = new Intent(getApplicationContext(), market_insight.class);
                                    intent.putExtra("in", currentDateandTime);                                    intent.putExtra("ket_plan", catatan.getText().toString());
                                    intent.putExtra("toko", "");
                                    intent.putExtra("segment", pilih_segment.getText().toString());
                                    intent.putExtra("type_customer", pilih_type.getText().toString());



                                    if(menu.txt_lokasi.getText().toString().equals("Pusat") || text_jabatan.getText().toString().equals("264") || text_jabatan.getText().toString().equals("503")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("278")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("267")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("250")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("251")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("252")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("418")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("462")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("467")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("468")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else if(text_jabatan.getText().toString().equals("253")){
                                        intent.putExtra("lokasi", pilih_depo.getText().toString());
                                    } else {
                                        intent.putExtra("lokasi", edit_lokasi.getText().toString());
                                    }

                                    startActivity(intent);
                                    dialog.dismiss();
                                }

                            }
                        }



                    }
                });

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventTwoClick(v);
                        dialog.dismiss();
                    }
                });

                edit_lokasi.setText(menu.txt_lokasi.getText().toString());





                StringRequest datasegment = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_classCustomer",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            segment.add(jsonObject1.getString("segment"));

                                            pilih_segment.setAdapter(new ArrayAdapter<String>(weekly_planner_new.this, android.R.layout.simple_expandable_list_item_1, segment));

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
                datasegment.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestsegment = Volley.newRequestQueue(weekly_planner_new.this);
                requestsegment.add(datasegment);


                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/mobile_eis_2/depo.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                depo.add(jsonObject1.getString("depo_nama"));
                                code_depo.add(jsonObject1.getString("kode_dms"));
                            }
                            pilih_depo.setAdapter(new ArrayAdapter<String>(weekly_planner_new.this, android.R.layout.simple_spinner_dropdown_item, depo));
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
                RequestQueue requestQueue = Volley.newRequestQueue(weekly_planner_new.this);
                requestQueue.add(stringRequest);







            }
        });

    }

    private void cekStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_StatusNik?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                if(movieObject.getString("status_kunjungan").equals("0")){

                                    mulai.setBackgroundColor(Color.parseColor("#808080"));
                                    akhiri.setBackgroundColor(Color.parseColor("#FB4141"));
                                    hapus.setVisibility(View.VISIBLE);
                                    add.setVisibility(View.VISIBLE);
                                    mulai.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                                    akhiri.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if(menu.txt_lokasi.getText().toString().equals("Pusat") || text_jabatan.getText().toString().equals("264") || text_jabatan.getText().toString().equals("503")){
                                                pDialog = new ProgressDialog(weekly_planner_new.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                cekLokasi2(langitude, longitude);
                                            } else if(text_jabatan.getText().toString().equals("278")){
                                                pDialog = new ProgressDialog(weekly_planner_new.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                cekLokasi2(langitude, longitude);
                                            } else if(text_jabatan.getText().toString().equals("267")){
                                                pDialog = new ProgressDialog(weekly_planner_new.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                cekLokasi2(langitude, longitude);
                                            } else if(text_jabatan.getText().toString().equals("250")){
                                                pDialog = new ProgressDialog(weekly_planner_new.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                cekLokasi2(langitude, longitude);
                                            } else if(text_jabatan.getText().toString().equals("251")){
                                                pDialog = new ProgressDialog(weekly_planner_new.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                cekLokasi2(langitude, longitude);
                                            } else if(text_jabatan.getText().toString().equals("252")){
                                                pDialog = new ProgressDialog(weekly_planner_new.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                cekLokasi2(langitude, longitude);
                                            } else if(text_jabatan.getText().toString().equals("418")){
                                                pDialog = new ProgressDialog(weekly_planner_new.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                cekLokasi2(langitude, longitude);
                                            } else if(text_jabatan.getText().toString().equals("462")){
                                                pDialog = new ProgressDialog(weekly_planner_new.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                cekLokasi2(langitude, longitude);
                                            } else if(text_jabatan.getText().toString().equals("467")){
                                                pDialog = new ProgressDialog(weekly_planner_new.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                cekLokasi2(langitude, longitude);
                                            } else if(text_jabatan.getText().toString().equals("468")){
                                                pDialog = new ProgressDialog(weekly_planner_new.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                cekLokasi2(langitude, longitude);
                                            } else if(text_jabatan.getText().toString().equals("253")){
                                                pDialog = new ProgressDialog(weekly_planner_new.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                cekLokasi2(langitude, longitude);
                                            } else {
                                                pDialog = new ProgressDialog(weekly_planner_new.this);
                                                showDialog();
                                                pDialog.setContentView(R.layout.progress_dialog);
                                                pDialog.getWindow().setBackgroundDrawableResource(
                                                        android.R.color.transparent
                                                );
                                                cekLokasi();
                                            }




                                        }
                                    });
                                } else {
                                    mulai.setBackgroundColor(Color.parseColor("#808080"));
                                    akhiri.setBackgroundColor(Color.parseColor("#808080"));

                                    hapus.setVisibility(GONE);
                                    add.setVisibility(GONE);

                                    mulai.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                                    akhiri.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
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
                        hideDialog();
                        hapus.setVisibility(GONE);
                        add.setVisibility(GONE);
                        mulai.setBackgroundColor(Color.parseColor("#0F4C81"));
                        akhiri.setBackgroundColor(Color.parseColor("#808080"));

                        mulai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                postStatus();

                            }
                        });

                        akhiri.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

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
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(weekly_planner_new.this);
        requestQueue.add(stringRequest);
    }

    private void getLocation() {

        refresh = new Runnable() {
            @Override
            public void run() {
                if (gpsTracker.canGetLocation()) {
                    langitude = String.valueOf(gpsTracker.getLatitude());
                    longitude = String.valueOf(gpsTracker.getLongitude());
                    System.out.println("longlat = " + langitude + ", " + longitude);
                } else {
                    gpsTracker.showSettingsAlert();
                }
                handler.postDelayed(this, 3000); // Refresh every 5 seconds
            }
        };
        handler.post(refresh);

    }


    private void cekLokasi2(String langitude, String longitude) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_nearest?latitude=" + langitude + "&longitude=" +longitude,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UpdateStatus();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new SweetAlertDialog(weekly_planner_new.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Anda Berada Diluar Radius Depo, Silahkan Masuki Area Depo Untuk Akhiri Kunjungan")
                                .setConfirmText("OK")
                                .show();
                        hideDialog();
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

    private void cekLokasi() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_depoGeoTag?depo_nama=" + txt_lokasi.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                Location startPoint = new Location("locationA");
                                startPoint.setLatitude(Double.valueOf(langitude));
                                startPoint.setLongitude(Double.valueOf(longitude));

                                //LOCATION DARI TOKO
                                Location endPoint = new Location("locationA");
                                endPoint.setLatitude(Double.valueOf(movieObject.getString("latitude")));
                                endPoint.setLongitude(Double.valueOf(movieObject.getString("longitude")));
                                double distance2 = startPoint.distanceTo(endPoint);
                                int value = (int) distance2;

                                System.out.println("Jarak Distance location Current Lat = " + langitude);
                                System.out.println("Jarak Distance location Current Long = " + longitude);

                                System.out.println("Jarak Distance location Toko Lat = " + movieObject.getString("latitude"));
                                System.out.println("Jarak Distance location Toko Long = " + movieObject.getString("longitude"));

                                System.out.println("Jarak Distance = " + String.valueOf(value));
                                //JIKA ANDA BERADA DI LUAR RADIUS
                                if (value > 400) {
                                    new SweetAlertDialog(weekly_planner_new.this, SweetAlertDialog.WARNING_TYPE)
                                            .setContentText("Anda Berada Diluar Radius Depo, Silahkan Masuki Area Depo Untuk Akhiri Kunjungan")
                                            .setConfirmText("OK")
                                            .show();
                                    hideDialog();
                                } else {
                                    UpdateStatus();
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
                        new SweetAlertDialog(weekly_planner_new.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Kesalahan Dalam Sistem")
                                .setConfirmText("OK")
                                .show();
                        hideDialog();
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
        // UpdateStatus();
    }

    private void UpdateStatus() {

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_updateKunjungan",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        SweetAlertDialog Success = new SweetAlertDialog(weekly_planner_new.this, SweetAlertDialog.SUCCESS_TYPE);
                        Success.setContentText("Kunjungan Diselesaikan");
                        Success.setCancelable(false);
                        Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                getDraft();
                            }
                        });
                        Success.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

                SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String curdate = sdf.format(new Date());

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String curdate2 = sdf2.format(new Date());

                params.put("nik_baru", nik_baru);
                params.put("tanggal_end", curdate2);
                params.put("tanggal", curdate);


                return params;
            }

        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(weekly_planner_new.this);
        requestQueue.add(stringRequest);
    }

    private void postStatus() {
        pDialog = new ProgressDialog(weekly_planner_new.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_DailyStatus",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SweetAlertDialog Success = new SweetAlertDialog(weekly_planner_new.this, SweetAlertDialog.SUCCESS_TYPE);
                        Success.setContentText("Kunjungan Dimulai");
                        Success.setCancelable(false);
                        Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                hideDialog();
                                sDialog.dismissWithAnimation();
                                getDraft();
                            }
                        });
                        Success.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

                SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String curdate = sdf.format(new Date());

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String curdate2 = sdf2.format(new Date());

                params.put("nik_baru", nik_baru);
                params.put("tanggal_start", curdate2);
                params.put("tanggal", curdate);


                return params;
            }

        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(weekly_planner_new.this);
        requestQueue.add(stringRequest);
    }

    private void hapusData() {


        for(int i = 0; i <= adapterDraft.getItemCount() - 1; i++){
            if(i == adapterDraft.getItemCount() - 1){
                SweetAlertDialog Success = new SweetAlertDialog(weekly_planner_new.this, SweetAlertDialog.SUCCESS_TYPE);
                Success.setContentText("Data Sudah Dihapus");
                Success.setCancelable(false);
                Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        getDraft();
                    }
                });
                Success.show();
            }
            if(adapterDraft.getItem(i).getSelected() == true){
                int finalI = i;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_hapus_transaksi",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

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


                        params.put("no_daily", adapterDraft.getItem(finalI).getNo_daily());


                        return params;
                    }

                };
                stringRequest.setRetryPolicy(
                        new DefaultRetryPolicy(
                                500000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        ));
                RequestQueue requestQueue = Volley.newRequestQueue(weekly_planner_new.this);
                requestQueue.add(stringRequest);
            }

        }
    }

    private void getDraft() {
        checkall.setChecked(false);
        externalmodels.clear();

        pDialog = new ProgressDialog(weekly_planner_new.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        adapterDraft = new ListViewAdapterDraftNew(weekly_planner_new.this, externalmodels);
        draft.setLayoutManager(new LinearLayoutManager(weekly_planner_new.this));
        draft.setAdapter(adapterDraft);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_header?nik=" + nik_baru + "&date=" + tanggal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        cekStatus();
                        checkall.setText("Checklist All (0)");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String curdate = sdf.format(new Date());

                        if(curdate.equals(tanggal)){
                            hapus.setEnabled(true);
                            hapus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FB4141")));
                        } else {
                            hapus.setEnabled(false);
                            hapus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EDEDED")));
                        }

                        draft.setVisibility(View.VISIBLE);
                        errorLayout.setVisibility(GONE);

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                externalmodel movieItem = new externalmodel(
                                        movieObject.getString("no_daily"),
                                        movieObject.getString("toko"),
                                        movieObject.getString("submit_date"),
                                        movieObject.getString("lokasi"),
                                        movieObject.getString("type_customer"),
                                        movieObject.getString("ket"));
                                externalmodels.add(movieItem);



                                adapterDraft.notifyDataSetChanged();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cekStatus();
                        hapus.setEnabled(false);
                        hapus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EDEDED")));
                        checkall.setText("Checklist All (0)");
                        draft.setVisibility(GONE);
                        errorLayout.setVisibility(View.VISIBLE);

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
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(weekly_planner_new.this);
        requestQueue.add(stringRequest);
    }

    public class ListViewAdapterDraftNew extends RecyclerView.Adapter<ListViewAdapterDraftNew.ViewProcessHolder> implements CompoundButton.OnCheckedChangeListener {
        Context context;
        private ArrayList<externalmodel> externalmodels;

        public ListViewAdapterDraftNew(Context context, ArrayList<externalmodel> externalmodels) {
            this.context = context;
            this.externalmodels = externalmodels;
            //        this.onItemClick = onItemCheckListener;
        }

        @Override
        public ListViewAdapterDraftNew.ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_draft_new, parent, false); //memanggil layout list recyclerview
            //ViewProcessHolder processHolder = new ViewProcessHolder(view);

            ListViewAdapterDraftNew.ViewProcessHolder processHolder = new ListViewAdapterDraftNew.ViewProcessHolder(view);
            return processHolder;
        }

        @Override
        public void onBindViewHolder(final ListViewAdapterDraftNew.ViewProcessHolder holder, @SuppressLint("RecyclerView") final int position) {

            final externalmodel data = externalmodels.get(position);

            holder.tanggal_date.setText(convertFormat(data.getSubmit_date()));
            holder.text_id.setText(data.getToko());
            holder.text_depo.setText(data.getLokasi());


            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_sub?no_daily=" + data.getNo_daily(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");

                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    if(movieObject.getString("brand").equals("TIDAK ADA KOMPETITOR")){
                                        holder.keterangan_brand.setText("TIDAK ADA KOMPETITOR");
                                        holder.text_feedback.setText(movieObject.getString("feedback"));
                                        holder.market_text.setText(movieObject.getString("market_activity"));


                                        holder.relative_sku.setVisibility(GONE);
                                        holder.relative_harganormal.setVisibility(GONE);
                                        holder.relative_diskon.setVisibility(GONE);
                                        holder.relative_cashback.setVisibility(GONE);
                                        holder.relative_harganet.setVisibility(GONE);
                                        holder.relative_hargajual.setVisibility(GONE);
                                        holder.relative_hargamargin.setVisibility(GONE);

                                       } else {
                                        holder.text_brand.setText(movieObject.getString("brand"));
                                        holder.text_sku.setText(movieObject.getString("sku"));
                                        holder.text_feedback.setText(movieObject.getString("feedback"));
                                        holder.market_text.setText(movieObject.getString("market_activity"));


                                        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                                        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
                                        formatRp.setCurrencySymbol("Rp. ");
                                        formatRp.setMonetaryDecimalSeparator(',');
                                        formatRp.setGroupingSeparator('.');
                                        kursIndonesia.setDecimalFormatSymbols(formatRp);

                                        holder.text_harga_normal.setText(kursIndonesia.format(Integer.parseInt(movieObject.getString("harga_beli_normal"))));
                                        holder.text_diskon.setText(kursIndonesia.format(Integer.parseInt(movieObject.getString("diskon"))));
                                        holder.text_cashback.setText(kursIndonesia.format(Integer.parseInt(movieObject.getString("cashback"))));
                                        holder.text_harga_net.setText(kursIndonesia.format(Integer.parseInt(movieObject.getString("harga_beli_net"))));
                                        holder.text_harga_jual.setText(kursIndonesia.format(Integer.parseInt(movieObject.getString("harga_jual"))));
                                        holder.text_margin.setText(kursIndonesia.format(Integer.parseInt(movieObject.getString("margin"))));
                                        holder.text_feedback.setText(movieObject.getString("feedback"));
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
            stringRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            RequestQueue requestQueue = Volley.newRequestQueue(weekly_planner_new.this);
            requestQueue.add(stringRequest);




            StringRequest namatoko = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_customerId?szId=" + data.getToko(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");

                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    holder.text_customer.setText(movieObject.getString("szName") + " - " + data.getType_customer());


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            holder.text_customer.setText(data.getKet() + " - " + data.getType_customer());

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
            namatoko.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            RequestQueue requestToko = Volley.newRequestQueue(weekly_planner_new.this);
            requestToko.add(namatoko);

            holder.selesai.setVisibility(GONE);


            holder.checktanggal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (externalmodels.get(position).getSelected()) {
                        externalmodels.get(position).setSelected(false);
                        checked --;
                        statuschecked = "manual";
                        checkall.setChecked(false);
                        checkall.setText("Checklist All" + " (" +String.valueOf(checked) + ")");
                    } else {
                        externalmodels.get(position).setSelected(true);
                        checked ++;
                        checkall.setText("Checklist All" + " (" +String.valueOf(checked) + ")");
                    }

                }
            });

            if(data.getSelected() == true){
                holder.checktanggal.setChecked(true);
            } else {
                holder.checktanggal.setChecked(false);
            }
        }

        @Override
        public int getItemCount() {
            return externalmodels.size();
        }

        public externalmodel getItem(int position) {
            return externalmodels.get(position);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }

        public class ViewProcessHolder extends RecyclerView.ViewHolder {


            TextView tanggal_date, market_text, text_customer, text_brand, text_sku, text_depo, text_id, keterangan_brand,
                    text_harga_normal, text_diskon, text_cashback, text_harga_net, text_harga_jual, text_margin, text_feedback;
            Context context;

            RelativeLayout relative_sku, relative_harganormal, relative_diskon, relative_cashback, relative_harganet, relative_hargajual, relative_hargamargin;

            CheckBox checktanggal;
            View itemView;

            ArrayList<planmodels> planmodelsList;

            MaterialButton selesai;


            public ViewProcessHolder(View itemView) {
                super(itemView);

                this.itemView = itemView;

                context = itemView.getContext();
                planmodelsList = new ArrayList<>();

                tanggal_date = (TextView) itemView.findViewById(R.id.tanggal_date);
                checktanggal = (CheckBox) itemView.findViewById(R.id.checktanggal);
                market_text = (TextView) itemView.findViewById(R.id.market_text);
                text_customer = (TextView) itemView.findViewById(R.id.text_customer);

                text_brand = (TextView) itemView.findViewById(R.id.text_brand);
                text_sku = (TextView) itemView.findViewById(R.id.text_sku);
                text_harga_normal = (TextView) itemView.findViewById(R.id.text_harga_normal);
                text_diskon = (TextView) itemView.findViewById(R.id.text_diskon);
                text_cashback = (TextView) itemView.findViewById(R.id.text_cashback);
                text_harga_net = (TextView) itemView.findViewById(R.id.text_harga_net);
                text_harga_jual = (TextView) itemView.findViewById(R.id.text_harga_jual);
                text_margin = (TextView) itemView.findViewById(R.id.text_margin);
                text_feedback = (TextView) itemView.findViewById(R.id.text_feedback);
                selesai = (MaterialButton) itemView.findViewById(R.id.selesai);

                relative_sku = (RelativeLayout) itemView.findViewById(R.id.relative_sku);
                relative_harganormal = (RelativeLayout) itemView.findViewById(R.id.relative_harganormal);
                relative_diskon = (RelativeLayout) itemView.findViewById(R.id.relative_diskon);
                relative_cashback = (RelativeLayout) itemView.findViewById(R.id.relative_cashback);
                relative_harganet = (RelativeLayout) itemView.findViewById(R.id.relative_harganet);
                relative_hargajual = (RelativeLayout) itemView.findViewById(R.id.relative_hargajual);
                relative_hargamargin = (RelativeLayout) itemView.findViewById(R.id.relative_hargamargin);

                keterangan_brand = (TextView) itemView.findViewById(R.id.keterangan_brand);

                text_depo = (TextView) itemView.findViewById(R.id.text_depo);
                text_id = (TextView) itemView.findViewById(R.id.text_id);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String curdate = sdf.format(new Date());

                if(curdate.equals(tanggal)){
                    checktanggal.setVisibility(View.VISIBLE);
                } else {
                    checktanggal.setVisibility(GONE);
                }
            }
        }
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

    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMMM yyyy · HH:mm:ss");
        return convetDateFormat.format(date);
    }

    public static String convertFormat2(String inputDate) {
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

    @Override
    protected void onResume() {
        super.onResume();
        getDraft();
    }

    public static void preventTwoClick(final View view){
        view.setEnabled(false);
        view.postDelayed(
                ()-> view.setEnabled(true),
                2000
        );
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        handler.removeCallbacks(refresh);
        if (gpsTracker != null) {
            gpsTracker.stopUsingGPS();
        }
    }
}