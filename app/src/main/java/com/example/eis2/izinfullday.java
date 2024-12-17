package com.example.eis2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.example.eis2.Item.getNo_pengajuan_full;
import com.example.eis2.Item.getNo_pengajuan_non_full;
import com.example.eis2.Item.postnonfullmodel;
import com.google.android.material.navigation.NavigationView;
import com.example.eis2.SearchSpinner.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import static com.example.eis2.AppController.TAG;
import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.izin.txt_jabatan;
import static com.example.eis2.izin.txt_nomor;
import static com.example.eis2.menu.permissions;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;
import static com.example.eis2.menu.txt_nama;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class  izinfullday extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    EditText nopengajuan2;
    SweetAlertDialog Success;
    private List<getNo_pengajuan_full> no_pengajuan;
    ProgressDialog pDialog;
    private SearchableSpinner spinner;
    ArrayList<String> Karyawan;
    RadioButton urgent,terencana;
    TextView longitude1, latitude1;
    RadioGroup opsi;
    EditText edittext, keterangan;
    final int CODE_GALLERY_REQUEST = 999;
    Bitmap bitmap;
    ImageView gambar1, upload;
    public int numberOfLines = 3;
    ImageButton add, pengajuan, uploadbutton;
    private Calendar date;
    private SimpleDateFormat dateFormatter;
    SharedPreferences sharedPreferences;
    private List<EditText> editTextList = new ArrayList<EditText>();
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    DrawerLayout dLayout;

    public void showDateTimePicker2(){
        final Calendar currentDate = Calendar.getInstance();
        Calendar twoDaysAgo = (Calendar) currentDate.clone();
        twoDaysAgo.add(Calendar.DATE, +7);

        date = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth );

                edittext.setText(dateFormatter.format(date.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new  DatePickerDialog(izinfullday.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());
        datePickerDialog.show();

    }
    public void showDateTimePicker(){
        final Calendar currentDate = Calendar.getInstance();
        Calendar twoDaysAgo = (Calendar) currentDate.clone();
        twoDaysAgo.add(Calendar.DATE, 0);

        date = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth );

                edittext.setText(dateFormatter.format(date.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new  DatePickerDialog(izinfullday.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());
        datePickerDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izinfullday);
        HttpsTrustManager.allowAllSSL();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });

        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        spinner = (SearchableSpinner) findViewById(R.id.karyawan);
        nopengajuan2 = (EditText) findViewById(R.id.nopengajuan);
        pengajuan = (ImageButton) findViewById(R.id.pengajuan);
        add = (ImageButton) findViewById(R.id.add);
        edittext = (EditText) findViewById(R.id.tanggal);
        keterangan = (EditText) findViewById(R.id.keterangan);

        upload = (ImageView) findViewById(R.id.upload);
        uploadbutton = (ImageButton) findViewById(R.id.uploadbutton);
        gambar1 = (ImageView) findViewById(R.id.gambar1);

        upload.setVisibility(View.GONE);
        uploadbutton.setVisibility(View.GONE);
        gambar1.setVisibility(View.GONE);
        setNavigationDrawer();
        ((RelativeLayout.LayoutParams) pengajuan.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.keterangan);
        nopengajuan2.setFocusable(false);
        opsi = (RadioGroup) findViewById(R.id.option);
        opsi.setOnCheckedChangeListener(this);
        urgent = (RadioButton) findViewById(R.id.urgent);
        terencana = (RadioButton) findViewById(R.id.terencana);
        no_pengajuan = new ArrayList<>();
        Karyawan = new ArrayList<>();
        dateFormatter = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());

        opsi.setOnCheckedChangeListener(this);
        urgent = (RadioButton) findViewById(R.id.urgent);
        terencana = (RadioButton) findViewById(R.id.terencana);

        longitude1 = (TextView) findViewById(R.id.longitude);
        latitude1 = (TextView) findViewById(R.id.lat);


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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner.setEnabled(true);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner.setEnabled(true);
            }
        });
        getLokasi();

        add = (ImageButton) findViewById(R.id.tambah);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout ll = (LinearLayout) findViewById(R.id.container);
                final EditText tanggal1 = new EditText(izinfullday.this);
                final ImageButton minus = new ImageButton(izinfullday.this);
                tanggal1.setBackgroundResource(R.drawable.tanggal);

                tanggal1.setPadding(10,0,0,0);

                double sizeInDP = 337.4649;
                int marginInDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP, getResources()
                                .getDisplayMetrics());

                double sizeInDP2 = 59.0463;
                int marginInDp2 = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP2, getResources()
                                .getDisplayMetrics());

                int sizeInDP3 = 60;
                int marginInDp3 = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, sizeInDP3, getResources()
                                .getDisplayMetrics());

                int sizeInDP4 = 27;
                int marginInDp4 = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, sizeInDP4, getResources()
                                .getDisplayMetrics());

                int sizeInDP5 = 295;
                int marginInDp5 = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, sizeInDP5, getResources()
                                .getDisplayMetrics());

                int sizeInDP6 = -30;
                int marginInDp6 = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, sizeInDP6, getResources()
                                .getDisplayMetrics());

                int sizeInDP7 = 10;
                int marginInDp7 = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, sizeInDP7, getResources()
                                .getDisplayMetrics());

                int sizeInDP8 = 27;
                int marginInDp8 = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, sizeInDP8, getResources()
                                .getDisplayMetrics());

                RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(marginInDp, marginInDp2);
                lparams.setMargins(0, 20, 30, 0);

                RelativeLayout.LayoutParams button = new RelativeLayout.LayoutParams(marginInDp8,marginInDp8);
                button.setMargins(marginInDp5, marginInDp6, 30, 0);

                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(1000, 115);
                p.setMargins(0, 20, 30, 0);

                tanggal1.setLayoutParams(p);
                tanggal1.setId(numberOfLines);
                tanggal1.setFocusable(false);
                tanggal1.setPadding(marginInDp7,0,0,0);
                tanggal1.setLayoutParams(lparams);
                numberOfLines++;
                editTextList.add(tanggal1);
                ll.addView(tanggal1);

                minus.setLayoutParams(button);
                minus.setBackgroundResource(R.drawable.btn_erase);

                minus.setId(numberOfLines);
                numberOfLines++;
                ll.addView(minus);

                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ll.removeView(tanggal1);
                        ll.removeView(minus);
                        editTextList.remove(tanggal1);
                    }
                });

                if (urgent.isChecked()) {
                    tanggal1.setOnClickListener(new View.OnClickListener() {
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

                                    tanggal1.setText(dateFormatter.format(date.getTime()));
                                }
                            };
                            DatePickerDialog datePickerDialog = new DatePickerDialog(izinfullday.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                            datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());
                            datePickerDialog.show();
                        }
                    });
                }
                    if (terencana.isChecked()) {
                    tanggal1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Calendar currentDate = Calendar.getInstance();
                            Calendar twoDaysAgo = (Calendar) currentDate.clone();
                            twoDaysAgo.add(Calendar.DATE, 7);

                            date = currentDate.getInstance();

                            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                    date.set(year, monthOfYear, dayOfMonth);

                                    tanggal1.setText(dateFormatter.format(date.getTime()));
                                }
                            };
                            DatePickerDialog datePickerDialog = new DatePickerDialog(izinfullday.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                            datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());
                            datePickerDialog.show();
                        }
                    });
                }
            }
        });

        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(izinfullday.this,
                        permissions(),
                        CODE_GALLERY_REQUEST);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            final Handler handler = new Handler();
            Runnable refresh = new Runnable() {
                @Override
                public void run() {
                    getLocation();
                    handler.postDelayed(this, 3000);
                }
            };
            handler.postDelayed(refresh, 3000);
        }

        pengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pengajuan.setEnabled(false);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        pengajuan.setEnabled(true);
                    }
                },1500);// set time as per your requirement
                if (opsi.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Pilih kondisinya", Toast.LENGTH_SHORT).show();
                } else if (edittext.getText().toString().length() == 0) {
                    edittext.setError("Masukkan Tanggal!");
                } else if (spinner.getSelectedItem().toString().equals("PILIH KARYAWAN")) {
                    Toast.makeText(getApplicationContext(), "pilih karyawan terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else {
                    if (urgent.isChecked()) {
                        if (gambar1.getDrawable() == null) {
                            Toast.makeText(getApplicationContext(), "Upload gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                        } else if (longitude1.getText().toString().equals("long") && (latitude1.getText().toString().equals("lat"))){
                            Toast.makeText(getApplicationContext(), "Lokasi belum ditemukan", Toast.LENGTH_SHORT).show();
                        } else if (keterangan.getText().toString().length() == 0) {
                            keterangan.setError("Masukkan Keterangan!");
                        } else if (editTextList.size() == 0) {
                            getNo();
                        }else {
                            for (int i = 0; i < editTextList.size(); i++) {
                                final String tanggal = editTextList.get(i).getText().toString();
                                if(tanggal.equals("null") || (tanggal == null) || tanggal.equals("")){
                                    Toast.makeText(getApplicationContext(), "Masukkan Tanggal", Toast.LENGTH_SHORT).show();
                                    break;
                                } else if (i == editTextList.size() -1){
                                    getNo();
                                }
                            }

                        }
                    } else if (terencana.isChecked()) {
                        if (longitude1.getText().toString().equals("long") && (latitude1.getText().toString().equals("lat"))){
                            Toast.makeText(getApplicationContext(), "Lokasi belum ditemukan", Toast.LENGTH_SHORT).show();
                        } else if (keterangan.getText().toString().length() == 0) {
                            keterangan.setError("Masukkan Keterangan!");
                        } else if (editTextList.size() == 0) {
                            getNo2();
                        } else {
                            System.out.println("Jumlah Baris = " + editTextList.size());
                            for (int i = 0; i < editTextList.size(); i++) {
                                final String tanggal = editTextList.get(i).getText().toString();
                                System.out.println("List Tanggal = " + tanggal);
                                if(tanggal.equals("null") || (tanggal == null) || tanggal.equals("")){
                                    Toast.makeText(getApplicationContext(), "Masukkan Tanggal", Toast.LENGTH_SHORT).show();
                                    break;
                                } else if (i == editTextList.size() -1){
                                    getNo2();
                                }
                            }

                        }
                    }
                }
            }
        });

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(urgent.isChecked()) {
                    showDateTimePicker();
                } else if(terencana.isChecked()) {
                    showDateTimePicker2();
                }
            }
        });
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
    @SuppressLint("MissingPermission")
    private void getLocation() {

        GPSTracker gpsTracker = new GPSTracker(izinfullday.this);
        if(gpsTracker.canGetLocation()){
            latitude1.setText(String.valueOf(gpsTracker.getLatitude()));
            longitude1.setText(String.valueOf(gpsTracker.getLongitude()));
        }else{
            gpsTracker.showSettingsAlert();

        }

    }

    private void getNo() {
        pDialog = new ProgressDialog(this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/nomor_pengajuan/index_full_day", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject finalObject3 = jsonArray.getJSONObject(0);

                            getNo_pengajuan_full nomor = new getNo_pengajuan_full(
                                    finalObject3.getInt("no_pengajuan_full_day"));
                            no_pengajuan.add(nomor);

                            getNo_pengajuan_full item = no_pengajuan.get(no_pengajuan.size() - 1);

                            nopengajuan2.setText(String.valueOf(item.getNo_pengajuan_full() + 1));
                            image();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "Maaf, Ada Kesalahan", Toast.LENGTH_SHORT).show();

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

    private void getNo2() {
        pDialog = new ProgressDialog(this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/nomor_pengajuan/index_full_day", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject finalObject3 = jsonArray.getJSONObject(0);

                            getNo_pengajuan_full nomor = new getNo_pengajuan_full(
                                    finalObject3.getInt("no_pengajuan_full_day"));
                            no_pengajuan.add(nomor);

                            getNo_pengajuan_full item = no_pengajuan.get(no_pengajuan.size() - 1);

                            nopengajuan2.setText(String.valueOf(item.getNo_pengajuan_full() + 1));

                            postfullfirstnogambar();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "Maaf, Ada Kesalahan", Toast.LENGTH_SHORT).show();
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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODE_GALLERY_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
            }else{
                Toast.makeText(getApplicationContext(), "You don't have permission to access gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(path);
                bitmap = BitmapFactory.decodeStream(inputStream);
                gambar1.setImageBitmap(bitmap);
                gambar1.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(izinfullday.this, "Gambar sudah diupload", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i ==R.id.urgent){
            upload.setVisibility(View.VISIBLE);
            uploadbutton.setVisibility(View.VISIBLE);
            gambar1.setVisibility(View.VISIBLE);
        }
        if (i ==R.id.terencana){
            upload.setVisibility(View.GONE);
            uploadbutton.setVisibility(View.GONE);
            gambar1.setVisibility(View.GONE);
        }

    }

    private void image() {
        final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");
                            if (status.contains("Success")) {
                                postfullfirst();
                            } else if (status.contains("Image not uploaded")){
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "Maaf ada kesalahan", Toast.LENGTH_SHORT).show();
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                String pengajuan = nopengajuan2.getText().toString();
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                String gambar = imagetoString(bitmap);

                params.put("no_pengajuan_full_day", pengajuan + "_" + nik_baru);
                params.put("foto", gambar);


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

    private void getLokasi() {
        String lokasi = txt_jabatan.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?lokasi_struktur=" + lokasi,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Karyawan.add("PILIH KARYAWAN");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String karyawan = null;
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            karyawan = jsonObject1.getString("nama_karyawan_struktur");
                            String nik = jsonObject1.getString("nik_baru");
                            String jabatan = jsonObject1.getString("dept_struktur");
                            Karyawan.add(karyawan + " (" + nik + ") ");
                            if(jabatan.equals("Board Of Director")){
                                Karyawan.remove(karyawan + " (" + nik + ") ");
                            }
                        }
                    }
                    spinner.setTitle("Pilih karyawan");
                    spinner.setAdapter(new ArrayAdapter<String>(izinfullday.this, android.R.layout.simple_spinner_dropdown_item, Karyawan));
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

    private void postfullfirst() {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/izin_full_day/index",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(editTextList.size() == 0){
                                postNotif();
                            } else {
                                postfull();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideDialog();
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
                    String pengajuan = nopengajuan2.getText().toString();
                    String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                    String tanggal = edittext.getText().toString();
                    String jabatan = txt_nomor.getText().toString();
                    String karyawan = spinner.getSelectedItem().toString();
                    String[] splited_text = karyawan.split(" \\(");
                    karyawan = splited_text[0];
                    String keterangan_sakit = keterangan.getText().toString();
                    String gambar = nopengajuan2.getText().toString();
                    String longitudee = longitude1.getText().toString();
                    String lat = latitude1.getText().toString();

                    params.put("no_pengajuan_full_day", pengajuan);
                    params.put("nik_full_day", nik_baru);
                    params.put("jabatan_full_day", jabatan);
                    params.put("jenis_full_day", "CD");

                    params.put("start_full_day", convertFormat(tanggal));
                    params.put("karyawan_pengganti", karyawan);
                    params.put("ket_tambahan", keterangan_sakit);
                    params.put("status_full_day", "0");

                    params.put("status_full_day_2", "0");
                    params.put("upload_full_day", gambar + "_" + nik_baru + ".jpeg");

                    params.put("lat", lat);
                    params.put("lon", longitudee);
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

    private void postfull() {

        for (int i = 0; i < editTextList.size(); i++) {
            final String tanggal = editTextList.get(i).getText().toString();
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/izin_full_day/index",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            postNotif();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideDialog();
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
                    String pengajuan = nopengajuan2.getText().toString();
                    String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                    String jabatan = txt_nomor.getText().toString();
                    String karyawan = spinner.getSelectedItem().toString();
                    String[] splited_text = karyawan.split(" \\(");
                    karyawan = splited_text[0];
                    String keterangan_sakit = keterangan.getText().toString();
                    String gambar = nopengajuan2.getText().toString();
                    String longitudee = longitude1.getText().toString();
                    String lat = latitude1.getText().toString();

                    params.put("no_pengajuan_full_day", pengajuan);
                    params.put("nik_full_day", nik_baru);
                    params.put("jabatan_full_day", jabatan);
                    params.put("jenis_full_day", "CD");

                    params.put("start_full_day", convertFormat(tanggal));
                    params.put("karyawan_pengganti", karyawan);
                    params.put("ket_tambahan", keterangan_sakit);
                    params.put("status_full_day", "0");

                    params.put("status_full_day_2", "0");
                    params.put("upload_full_day", gambar + "_" + nik_baru + ".jpeg");
                    params.put("lat", lat);
                    params.put("lon", longitudee);
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

    private void postfullfirstnogambar() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/izin_full_day/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(editTextList.size() == 0){
                            postNotif();
                        } else {
                            postnogambar();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
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
                String pengajuan = nopengajuan2.getText().toString();
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                String tanggal = edittext.getText().toString();
                String jabatan = txt_nomor.getText().toString();
                String karyawan = spinner.getSelectedItem().toString();
                String[] splited_text = karyawan.split(" \\(");
                karyawan = splited_text[0];
                String keterangan_sakit = keterangan.getText().toString();
                String longitudee = longitude1.getText().toString();
                String lat = latitude1.getText().toString();

                params.put("no_pengajuan_full_day", pengajuan);
                params.put("nik_full_day", nik_baru);
                params.put("jabatan_full_day", jabatan);
                params.put("jenis_full_day", "CD");

                params.put("start_full_day", convertFormat(tanggal));
                params.put("karyawan_pengganti", karyawan);
                params.put("ket_tambahan", keterangan_sakit);
                params.put("status_full_day", "0");

                params.put("status_full_day_2", "0");
                params.put("upload_full_day", "");
                params.put("lat", lat);
                params.put("lon", longitudee);
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

    private void postnogambar() {

        for (int i = 0; i < editTextList.size(); i++) {
            final String tanggal = editTextList.get(i).getText().toString();
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/izin_full_day/index",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            postNotif();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideDialog();
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
                    String pengajuan = nopengajuan2.getText().toString();
                    String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                    String jabatan = txt_nomor.getText().toString();
                    String karyawan = spinner.getSelectedItem().toString();
                    String[] splited_text = karyawan.split(" \\(");
                    karyawan = splited_text[0];
                    String keterangan_sakit = keterangan.getText().toString();
                    String longitudee = longitude1.getText().toString();
                    String lat = latitude1.getText().toString();

                    params.put("no_pengajuan_full_day", pengajuan);
                    params.put("nik_full_day", nik_baru);
                    params.put("jabatan_full_day", jabatan);
                    params.put("jenis_full_day", "CD");

                    params.put("start_full_day", convertFormat(tanggal));
                    params.put("karyawan_pengganti", karyawan);
                    params.put("ket_tambahan", keterangan_sakit);
                    params.put("status_full_day", "0");

                    params.put("status_full_day_2", "0");
                    params.put("upload_full_day", "");
                    params.put("lat", lat);
                    params.put("lon", longitudee);
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

    private void postNotif() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/Notifikasi/index_token?no_jabatan_karyawan="+txt_nomor.getText().toString()+"&lokasi_hrd=" + txt_lokasi.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                postNotifikasi(movieObject.getString("device_token"));


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    private void postNotifikasi(String device_token) {
                        final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/Push_Notification/push_notif_eis.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        hideDialog();
                                        Success = new SweetAlertDialog(izinfullday.this, SweetAlertDialog.SUCCESS_TYPE);
                                        Success.setContentText("Data Sudah Ditambahkan");
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
                                        hideDialog();
                                        Success = new SweetAlertDialog(izinfullday.this, SweetAlertDialog.SUCCESS_TYPE);
                                        Success.setContentText("Data Sudah Ditambahkan");
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

                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.DAY_OF_YEAR, 2);

                                Date futureDate = calendar.getTime();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");

                                String currentDateandTime = dateFormat.format(futureDate);

                                params.put("device", device_token);
                                params.put("body", "Terdapat pengajuan CDT a/n "  +   txt_nama.getText().toString() +   ", menunggu approval."+  " *) Masa berlaku s/d " + currentDateandTime);

                                System.out.println("notif = " + params);




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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(izinfullday.this);
                        requestQueue2.add(stringRequest2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Success = new SweetAlertDialog(izinfullday.this, SweetAlertDialog.SUCCESS_TYPE);
                        Success.setContentText("Data Sudah Ditambahkan");
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.setCancelable(false);
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private String imagetoString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imageType = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageType, Base64.DEFAULT);
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
                            izinfullday.this);
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
                                    Intent intent = new Intent(izinfullday.this, MainActivity.class);
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