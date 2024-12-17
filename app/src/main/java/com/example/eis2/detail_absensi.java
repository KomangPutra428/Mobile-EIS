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
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.eis2.Item.keteranganmodel;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

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

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;
import static java.util.Calendar.DAY_OF_MONTH;

public class detail_absensi extends AppCompatActivity {
    EditText tanggalawal, tanggalakhir, periodeedit, namaedit;
    private List<keteranganmodel> movieItemList;
    EditText hadirtext, telattext, tidakhadirtext, sakittext, dinastext, liburtext,
            cdttext, cutitahunantext, cutikhusustext, pulangcepattext, tapintext, tapouttext;

    ImageButton ceklihat, cekdetail, tambah, hapus;
    ProgressDialog pDialog;
    DrawerLayout dLayout;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_absensi);
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
        tanggalawal = (EditText) findViewById(R.id.tanggalawal);
        tanggalakhir = (EditText) findViewById(R.id.tanggalakhir);
        periodeedit = (EditText) findViewById(R.id.periodeedit);
        namaedit = (EditText) findViewById(R.id.namaedit);
        setNavigationDrawer();

        tambah = (ImageButton) findViewById(R.id.tambah);
        hapus = (ImageButton) findViewById(R.id.hapus);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tanggalakhir.setVisibility(View.VISIBLE);
                hapus.setVisibility(View.VISIBLE);

            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tanggalakhir.setVisibility(View.GONE);
                hapus.setVisibility(View.GONE);

            }
        });

        hadirtext = (EditText) findViewById(R.id.hadirtext);
        telattext = (EditText) findViewById(R.id.telattext);
        tidakhadirtext = (EditText) findViewById(R.id.tidakhadirtext);

        sakittext = (EditText) findViewById(R.id.sakittext);
        dinastext = (EditText) findViewById(R.id.dinastext);
        liburtext = (EditText) findViewById(R.id.liburtext);

        cdttext = (EditText) findViewById(R.id.cdttext);
        cutitahunantext = (EditText) findViewById(R.id.cutitahunantext);
        cutikhusustext = (EditText) findViewById(R.id.cutikhusustext);

        pulangcepattext = (EditText) findViewById(R.id.pulangcepattext);
        tapintext = (EditText) findViewById(R.id.tapintext);
        tapouttext = (EditText) findViewById(R.id.tapouttext);

        ceklihat = (ImageButton) findViewById(R.id.ceklihat);
        cekdetail = (ImageButton) findViewById(R.id.cekdetail);

        SimpleDateFormat sdf = new SimpleDateFormat( "dd-MMMM-yyyy" );
        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.DAY_OF_MONTH, myCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        tanggalawal.setText(sdf.format(myCalendar.getTime()));

        SimpleDateFormat sdf2 = new SimpleDateFormat( "dd-MMMM-yyyy" );
        final Calendar myCalendar2 = Calendar.getInstance();
        tanggalakhir.setText( sdf2.format((new Date())));

        getName();
        cekdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(detail_absensi.this, kehadiran.class);
                startActivity(i);
            }
        });

        ceklihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");

                String awal = tanggalawal.getText().toString();
                String akhir = tanggalakhir.getText().toString();

                try {
                    // Parse the date strings into Date objects
                    Date date1 = sdf.parse(awal);
                    Date date2 = sdf.parse(akhir);

                    // Compare the dates
                    if (date1.compareTo(date2) > 0) {
                        Toast.makeText(detail_absensi.this, "Silahkan Masukkan Tanggal Dengan Benar", Toast.LENGTH_SHORT).show();
                    }  else {
                        periodeedit.setText(awal + " - " + akhir);
                        kehadiran();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//
//                String dtend = convertFormat(tanggalawal.getText().toString());
//                try {
//                    Date date = format.parse(dtend);
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
//
//                String dtend2 = convertFormat(tanggalakhir.getText().toString());
//                try {
//                    Date date2 = format.parse(dtend2);
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
//
//                if(date2.before(date)){
//                    movieItemList2.remove(movieItem2);
//                }
//

//
//
//                kehadiran();
            }
        });
        String awal = tanggalawal.getText().toString();
        String akhir = tanggalakhir.getText().toString();
        System.out.println("hasil1 =" + awal);
        System.out.println("hasil2 =" + akhir);


        periodeedit.setText(awal + " - " + akhir);

        final DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
            private void updateLabel() {
                String myFormat = "dd-MMMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                tanggalawal.setText(sdf.format(myCalendar.getTime()));
            }
        };
        tanggalawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(detail_absensi.this, datePickerDialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(DAY_OF_MONTH)).show();
            }
        });


        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
            private void updateLabel() {
                String myFormat = "dd-MMMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                tanggalakhir.setText(sdf.format(myCalendar2.getTime()));
                tanggalakhir.getText().toString();
            }
        };
        tanggalakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(detail_absensi.this, date2, myCalendar2
                        .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(DAY_OF_MONTH)).show();
            }
        });

        movieItemList = new ArrayList<>();
        kehadiran();

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
                            detail_absensi.this);
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
                                    Intent intent = new Intent(detail_absensi.this, MainActivity.class);
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

    private void getName() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        System.out.println("hasil =" + nik_baru);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);
                                namaedit.setText(movieObject.getString("nama_karyawan_struktur"));

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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void kehadiran (){
        pDialog = new ProgressDialog(detail_absensi.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        final String awal = tanggalawal.getText().toString().trim();
        final String akhir = tanggalakhir.getText().toString().trim();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/absensi/index?shift_day="+ convertFormat(awal) +"&shift_day_2="+ convertFormat(akhir) +"&badgenumber="+ nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            int number = 0;
                            int number1 = 0;
                            int number2 = 0;
                            int number3 = 0;
                            int number4 = 0;
                            int number5 = 0;
                            int number6 = 0;
                            int number7 = 0;
                            int number8 = 0;
                            int number9 = 0;
                            int number10 = 0;
                            int number11 = 0;

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                keteranganmodel movieItem = new keteranganmodel(
                                        movieObject.getString("shift_day"),
                                        movieObject.getString("F1"),
                                        movieObject.getString("depo_f1"),
                                        movieObject.getString("F4"),
                                        movieObject.getString("depo_f4"),
                                        movieObject.getString("ket_absensi"),
                                        movieObject.getString("name"));

                                if (movieObject.getString("ket_absensi").contains("HD"))
                                    number++;  {
                                    hadirtext.setText(String.valueOf(number));
                                }
                                if (movieObject.getString("ket_absensi").contains("TL"))
                                    number1++; {
                                    telattext.setText(String.valueOf(number1));
                                }
                                if (movieObject.getString("ket_absensi").contains("AL"))
                                    number2++; {
                                    tidakhadirtext.setText(String.valueOf(number2));
                                }
                                if (movieObject.getString("ket_absensi").contains("SA"))
                                    number3++; {
                                    sakittext.setText(String.valueOf(number3));
                                }
                                if (movieObject.getString("ket_absensi").contains("DN"))
                                    number4++; {
                                    dinastext.setText(String.valueOf(number4));
                                }
                                if (movieObject.getString("ket_absensi").contains("PC"))
                                    number5++; {
                                    pulangcepattext.setText(String.valueOf(number5));
                                }
                                if (movieObject.getString("ket_absensi").contains("TD F1"))
                                    number6++; {
                                    tapintext.setText(String.valueOf(number6));
                                }
                                if (movieObject.getString("ket_absensi").contains("TD F4"))
                                    number7++; {
                                    tapouttext.setText(String.valueOf(number7));
                                }
                                if (movieObject.getString("ket_absensi").contains("LI"))
                                    number8++; {
                                    liburtext.setText(String.valueOf(number8));
                                }
                                if (movieObject.getString("ket_absensi").contains("CD"))
                                    number9++; {
                                    cdttext.setText(String.valueOf(number9));
                                }
                                if (movieObject.getString("ket_absensi").contains("CU"))
                                    number10++; {
                                    cutitahunantext.setText(String.valueOf(number10));
                                }
                                if (movieObject.getString("ket_absensi").contains("CK"))
                                    number11++; {
                                    cutikhusustext.setText(String.valueOf(number11));
                                }
                                hideDialog();

                                movieItemList.add(movieItem);

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
                        Toast.makeText(getApplicationContext(), "Maaf, Anda Belum Pernah Absen", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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


}
