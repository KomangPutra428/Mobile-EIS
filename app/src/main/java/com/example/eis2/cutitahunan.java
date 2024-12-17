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
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.eis2.Item.getNo_pengajuan_full;
import com.example.eis2.Item.getNo_pengajuan_tahunan;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.eis2.AppController.TAG;
import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.cuti.sisa_cuti;
import static com.example.eis2.cuti.tahun_cuti;
import static com.example.eis2.cuti.tanggaldepan;
import static com.example.eis2.cuti.tanggalsekarang;
import static com.example.eis2.cuti.txt_nomor_jabatan;
import static com.example.eis2.dinas.txt_nomor_jab;
import static com.example.eis2.menu.permissions;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_nama;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class cutitahunan extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    ImageButton uploadbutton, pengajuan, tambah;
    SweetAlertDialog Success;
    RadioGroup opsi;
    private List<getNo_pengajuan_tahunan> no_pengajuan;
    RadioButton urgent, terencana;
    TextView option;
    ImageView gambar1, upload;
    private Calendar date;
    ProgressDialog pDialog;
    private SimpleDateFormat dateFormatter;
    EditText nopengajuan, tanggalcutitahunanfull, keterangan, tanggal;
    final int CODE_GALLERY_REQUEST = 999;
    Bitmap bitmap;
    ImageButton add;
    public int numberOfLines = 3;
    SharedPreferences sharedPreferences;
    private List<EditText> editTextList = new ArrayList<EditText>();
    private List<EditText> editTextList2 = new ArrayList<EditText>();
    DrawerLayout dLayout;

    TextView longitude1, latitude1;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;

    public void showDateTimePicker2() {
        final Calendar currentDate = Calendar.getInstance();
        Calendar twoDaysAgo = (Calendar) currentDate.clone();
        twoDaysAgo.add(Calendar.DATE, +7);

        date = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                tanggalcutitahunanfull.setText(dateFormatter.format(date.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(cutitahunan.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());
        datePickerDialog.show();

    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        Calendar twoDaysAgo = (Calendar) currentDate.clone();
        twoDaysAgo.add(Calendar.DATE, 0);

        date = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                tanggalcutitahunanfull.setText(dateFormatter.format(date.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(cutitahunan.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());
        datePickerDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutitahunan);
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

        nopengajuan = (EditText) findViewById(R.id.nopengajuan);
        tanggalcutitahunanfull = (EditText) findViewById(R.id.tanggal);
        keterangan = (EditText) findViewById(R.id.keterangan);
        nopengajuan.setFocusable(false);
        setNavigationDrawer();
        opsi = (RadioGroup) findViewById(R.id.option);
        opsi.setOnCheckedChangeListener(this);
        urgent = (RadioButton) findViewById(R.id.urgent);
        terencana = (RadioButton) findViewById(R.id.terencana);
        option = (TextView) findViewById(R.id.options);
        gambar1 = (ImageView) findViewById(R.id.gambar);
        nopengajuan = (EditText) findViewById(R.id.nopengajuan);

        uploadbutton = (ImageButton) findViewById(R.id.uploadbutton);
        tambah = (ImageButton) findViewById(R.id.tambah);

        uploadbutton.setVisibility(View.GONE);
        upload = (ImageView) findViewById(R.id.upload);


        gambar1.setVisibility(View.GONE);
        pengajuan = (ImageButton) findViewById(R.id.pengajuan);
        dateFormatter = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());

        final String sekarang = tanggalsekarang.getText().toString();
        final String tahundepan = tanggaldepan.getText().toString();

        System.out.println("Hasil =" + sekarang);
        System.out.println("Hasil =" + tahundepan);


        longitude1 = (TextView) findViewById(R.id.longitude);
        latitude1 = (TextView) findViewById(R.id.lat);

        final String a = sisa_cuti.getText().toString();
        final int i = Integer.parseInt(a);
        System.out.println("sisa cuti = " + i);

        editTextList2.add(tanggalcutitahunanfull);
        final int b = editTextList2.size();
        System.out.println("Joining = " + b);

        add = (ImageButton) findViewById(R.id.add);

        urgent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tanggalcutitahunanfull.setText("");
                }
            }
        });

        terencana.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tanggalcutitahunanfull.setText("");
                }
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

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String a = sisa_cuti.getText().toString();
                final int x = Integer.parseInt(a);

                int c = editTextList.size() + 1;
                System.out.println("rows = " + c);

                if (c == x) {
                    Toast.makeText(getApplicationContext(), "Maaf sudah tidak dapat melakukan pengajuan", Toast.LENGTH_SHORT).show();
                } else {
                    final LinearLayout ll = (LinearLayout) findViewById(R.id.container);
                    final EditText tanggal1 = new EditText(cutitahunan.this);
                    final ImageButton minus = new ImageButton(cutitahunan.this);
                    tanggal1.setBackgroundResource(R.drawable.tanggal);

                    tanggal1.setPadding(10, 0, 0, 0);

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

                    tanggal1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Calendar currentDate = Calendar.getInstance();
                            Calendar twoDaysAgo = (Calendar) currentDate.clone();
                            twoDaysAgo.add(Calendar.DATE, -3);

                            date = currentDate.getInstance();

                            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                    date.set(year, monthOfYear, dayOfMonth);

                                    tanggal1.setText(dateFormatter.format(date.getTime()));
                                }
                            };
                            DatePickerDialog datePickerDialog = new DatePickerDialog(cutitahunan.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                            datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());
                            datePickerDialog.show();
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
                                DatePickerDialog datePickerDialog = new DatePickerDialog(cutitahunan.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                                datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());
                                datePickerDialog.show();
                            }
                        });
                    } else if (terencana.isChecked()) {
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
                                DatePickerDialog datePickerDialog = new DatePickerDialog(cutitahunan.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                                datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());
                                datePickerDialog.show();
                            }
                        });
                    }
                }
            }
        });



        no_pengajuan = new ArrayList<>();

        tanggalcutitahunanfull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (urgent.isChecked()) {
                    showDateTimePicker();
                } else if (terencana.isChecked()) {
                    showDateTimePicker2();

                }
            }
        });
        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(cutitahunan.this,
                        permissions(),
                        CODE_GALLERY_REQUEST);
            }
        });

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
                String sekarang = tanggalsekarang.getText().toString();
                String tahundepan = tahun_cuti.getText().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = null;
                try {
                    date1 = sdf.parse(sekarang);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2 = null;
                try {
                    date2 = sdf.parse(tahundepan);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(cuti.hakcuti.getText().toString().equals("Anda Belum Mempunyai Hak Cuti Tahunan")){
                    Toast.makeText(getApplicationContext(), "Maaf, anda belum mempunyai hak cuti", Toast.LENGTH_SHORT).show();
                } else if (date1.before(date2)) {
                    Toast.makeText(getApplicationContext(), "Masa cuti anda belum berlaku", Toast.LENGTH_SHORT).show();
                } else if (i == 0) {
                    Toast.makeText(getApplicationContext(), "Waktu pengajuan sudah habis", Toast.LENGTH_SHORT).show();
                } else {
                    if (tanggalcutitahunanfull.getText().toString().length() == 0) {
                        tanggalcutitahunanfull.setError("Masukkan Tanggal!");
                    } else if (keterangan.getText().toString().length() == 0) {
                        keterangan.setError("Masukkan Keterangan!");
                    } else if (opsi.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getApplicationContext(), "Pilih kondisinya", Toast.LENGTH_SHORT).show();
                    } else {
                        if (urgent.isChecked()) {
                            if (gambar1.getDrawable() == null) {
                                Toast.makeText(getApplicationContext(), "Upload gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                            } else if (keterangan.getText().toString().length() == 0) {
                                keterangan.setError("Masukkan Keterangan!");
                            } else if (longitude1.getText().toString().equals("long") && (latitude1.getText().toString().equals("lat"))){
                                Toast.makeText(getApplicationContext(), "Lokasi belum ditemukan", Toast.LENGTH_SHORT).show();
                            } else {
                                if (editTextList.size() == 0) {
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
                            }
                        } else if (terencana.isChecked()) {
                            if (keterangan.getText().toString().length() == 0) {
                                keterangan.setError("Masukkan Keterangan!");
                            }
                            else if (longitude1.getText().toString().equals("long") && (latitude1.getText().toString().equals("lat"))){
                                Toast.makeText(getApplicationContext(), "Lokasi belum ditemukan", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (editTextList.size() == 0) {
                                    getNo2();
                                }else {
                                    for (int i = 0; i < editTextList.size(); i++) {
                                        final String tanggal = editTextList.get(i).getText().toString();
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

                }
            }
        });
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
            return true;
        }
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
                            cutitahunan.this);
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
                                    Intent intent = new Intent(cutitahunan.this, MainActivity.class);
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
    private void getLocation() {
        GPSTracker gpsTracker = new GPSTracker(cutitahunan.this);
        if(gpsTracker.canGetLocation()){
            latitude1.setText(String.valueOf(gpsTracker.getLatitude()));
            longitude1.setText(String.valueOf(gpsTracker.getLongitude()));
        }else{
            gpsTracker.showSettingsAlert();
        }
    }


    private void getNo() {
        pDialog = new ProgressDialog(cutitahunan.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        JsonObjectRequest stringRequest1 = new JsonObjectRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/nomor_pengajuan/index_cuti_tahunan", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject finalObject3 = jsonArray.getJSONObject(0);

                            getNo_pengajuan_tahunan nomor = new getNo_pengajuan_tahunan(
                                    finalObject3.getInt("no_pengajuan_tahunan"));
                            no_pengajuan.add(nomor);

                            getNo_pengajuan_tahunan item = no_pengajuan.get(no_pengajuan.size() - 1);
                            nopengajuan.setText(String.valueOf(item.getNo_pengajuan_tahunan() + 1));
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
                        Toast.makeText(cutitahunan.this, "Maaf, Ada Kesalahan", Toast.LENGTH_SHORT).show();
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
        stringRequest1.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);
    }

    private void getNo2() {
        pDialog = new ProgressDialog(cutitahunan.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        JsonObjectRequest stringRequest1 = new JsonObjectRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/nomor_pengajuan/index_cuti_tahunan", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject finalObject3 = jsonArray.getJSONObject(0);

                            getNo_pengajuan_tahunan nomor = new getNo_pengajuan_tahunan(
                                    finalObject3.getInt("no_pengajuan_tahunan"));
                            no_pengajuan.add(nomor);

                            getNo_pengajuan_tahunan item = no_pengajuan.get(no_pengajuan.size() - 1);
                            nopengajuan.setText(String.valueOf(item.getNo_pengajuan_tahunan() + 1));
                            terencanafirst();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(cutitahunan.this, "Maaf, Ada Kesalahan", Toast.LENGTH_SHORT).show();
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
        stringRequest1.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest1);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
            } else {
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
            Toast.makeText(cutitahunan.this, "Gambar sudah diupload", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.urgent) {
            uploadbutton.setVisibility(View.VISIBLE);
            upload.setVisibility(View.VISIBLE);
            gambar1.setVisibility(View.VISIBLE);
            option.setText("Urgent");

        }
        if (i == R.id.terencana) {
            uploadbutton.setVisibility(View.GONE);
            upload.setVisibility(View.GONE);
            gambar1.setVisibility(View.GONE);
            option.setText("Terencana");
        }
    }

    private void image() {


        final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload_tahunan.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");
                            if (status.contains("Success")) {
                                urgentfirst();
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

                String pengajuan = nopengajuan.getText().toString();
                String gambar = imagetoString(bitmap);
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);


                params.put("no_pengajuan_tahunan", pengajuan + "_" + nik_baru);
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

    private void terencanafirst() {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/cuti_tahunan/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (editTextList.size() == 0){
                            postnotif();
                        } else {
                            terencana();
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
                String pengajuan = nopengajuan.getText().toString();
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                String tanggal1 = tanggalcutitahunanfull.getText().toString();
                String jabatan = txt_nomor_jabatan.getText().toString();
                String options = option.getText().toString();
                String keterangan_tahunan = keterangan.getText().toString();
                String longitudee = longitude1.getText().toString();
                String lat = latitude1.getText().toString();

                params.put("no_pengajuan_tahunan", pengajuan);
                params.put("nik_sisa_cuti", nik_baru);
                params.put("jabatan_cuti_tahunan", jabatan);
                params.put("start_cuti_tahunan", convertFormat(tanggal1));

                params.put("ket_tambahan_tahunan", keterangan_tahunan);
                params.put("opsi_cuti_tahunan", options);
                params.put("status_cuti_tahunan", "0");
                params.put("status_cuti_tahunan_2", "0");

                params.put("dok_cuti_tahunan", "");
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

    private void  terencana() {

        for (int i = 0; i < editTextList.size(); i++) {
            final String tanggal = editTextList.get(i).getText().toString();
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/cuti_tahunan/index",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            postnotif();
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
                    String pengajuan = nopengajuan.getText().toString();
                    String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                    String jabatan = txt_nomor_jabatan.getText().toString();
                    String options = option.getText().toString();
                    String keterangan_tahunan = keterangan.getText().toString();
                    String longitudee = longitude1.getText().toString();
                    String lat = latitude1.getText().toString();

                    params.put("no_pengajuan_tahunan", pengajuan);
                    params.put("nik_sisa_cuti", nik_baru);
                    params.put("jabatan_cuti_tahunan", jabatan);
                    params.put("start_cuti_tahunan", convertFormat(tanggal));

                    params.put("ket_tambahan_tahunan", keterangan_tahunan);
                    params.put("opsi_cuti_tahunan", options);
                    params.put("status_cuti_tahunan", "0");
                    params.put("status_cuti_tahunan_2", "0");

                    params.put("dok_cuti_tahunan", "");
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

    private void urgentfirst() {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/cuti_tahunan/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(editTextList.size() == 0){
                            postnotif();
                        } else {
                            urgent();
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
                String pengajuan = nopengajuan.getText().toString();
                String nik_baru = sharedPreferences.getString(KEY_NIK ,null);
                String tanggal1 = tanggalcutitahunanfull.getText().toString();
                String jabatan = txt_nomor_jabatan.getText().toString();
                String options = option.getText().toString();
                String keterangan_tahunan = keterangan.getText().toString();
                String gambar = nopengajuan.getText().toString();
                String longitudee = longitude1.getText().toString();
                String lat = latitude1.getText().toString();

                params.put("no_pengajuan_tahunan", pengajuan);
                params.put("nik_sisa_cuti", nik_baru);
                params.put("jabatan_cuti_tahunan", jabatan);
                params.put("start_cuti_tahunan", convertFormat(tanggal1));

                params.put("dok_cuti_tahunan", gambar + "_" + nik_baru + ".jpeg");

                params.put("ket_tambahan_tahunan", keterangan_tahunan);
                params.put("opsi_cuti_tahunan", options);
                params.put("status_cuti_tahunan", "0");
                params.put("status_cuti_tahunan_2", "0");

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

    private void urgent() {

        for (int i = 0; i < editTextList.size(); i++) {
            final String tanggal = editTextList.get(i).getText().toString();
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/cuti_tahunan/index",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            postnotif();
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
                    String pengajuan = nopengajuan.getText().toString();
                    String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                    String jabatan = txt_nomor_jabatan.getText().toString();
                    String options = option.getText().toString();
                    String keterangan_tahunan = keterangan.getText().toString();
                    String gambar = nopengajuan.getText().toString();
                    String longitudee = longitude1.getText().toString();
                    String lat = latitude1.getText().toString();

                    params.put("no_pengajuan_tahunan", pengajuan);
                    params.put("nik_sisa_cuti", nik_baru);
                    params.put("jabatan_cuti_tahunan", jabatan);
                    params.put("start_cuti_tahunan", convertFormat(tanggal));

                    params.put("dok_cuti_tahunan", gambar + "_" + nik_baru + ".jpeg");

                    params.put("ket_tambahan_tahunan", keterangan_tahunan);
                    params.put("opsi_cuti_tahunan", options);
                    params.put("status_cuti_tahunan", "0");
                    params.put("status_cuti_tahunan_2", "0");

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

    private void postnotif() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/Notifikasi/index_token?no_jabatan_karyawan="+txt_nomor_jabatan.getText().toString()+"&lokasi_hrd=" + menu.txt_lokasi.getText().toString(),
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
                                        Success = new SweetAlertDialog(cutitahunan.this, SweetAlertDialog.SUCCESS_TYPE);
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
                                        Success = new SweetAlertDialog(cutitahunan.this, SweetAlertDialog.SUCCESS_TYPE);
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
                                params.put("body", "Terdapat pengajuan CUTI TAHUNAN   a/n " + txt_nama.getText().toString() + ", menunggu approval."+  " *) Masa berlaku s/d " + currentDateandTime);

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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(cutitahunan.this);
                        requestQueue2.add(stringRequest2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Success = new SweetAlertDialog(cutitahunan.this, SweetAlertDialog.SUCCESS_TYPE);
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

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.setCancelable(false);
            pDialog.show();
    }
    private String imagetoString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imageType = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageType, Base64.DEFAULT);
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