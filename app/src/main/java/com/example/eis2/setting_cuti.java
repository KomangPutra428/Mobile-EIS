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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.eis2.Item.sisacutimodel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;

public class setting_cuti extends AppCompatActivity {
    TextView nama, nik, depodept, sisacuti, jabatan, tanggalmasuk, tanggalresign, cutidapat, tahunnext;
    SharedPreferences sharedPreferences;
    private List<sisacutimodel> movieItemList3;
    Button update;
    ProgressDialog pDialog;
    DrawerLayout dLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_cuti);
        HttpsTrustManager.allowAllSSL();
        setNavigationDrawer();

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
        nama = findViewById(R.id.nama);
        nik = findViewById(R.id.nik);
        depodept = findViewById(R.id.depodept);
        sisacuti = findViewById(R.id.sisacuti);
        jabatan = findViewById(R.id.jabatan);
        tanggalmasuk = findViewById(R.id.tanggalmasuk);
        tanggalresign = findViewById(R.id.tanggalresign);
        cutidapat = findViewById(R.id.cutidapat);
        update = findViewById(R.id.update);
        tahunnext = findViewById(R.id.tahun);
        movieItemList3 = new ArrayList<>();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(setting_cuti.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_hakcuti",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                updatehakcuti();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                updatehakcuti();
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
                        int year = Integer.parseInt(year(tanggalresign.getText().toString())) - 1;
                        int sisa = Integer.parseInt(sisacuti.getText().toString());
                        int cutidapattes = Integer.parseInt(cutidapat.getText().toString());
                        int totalcuti = sisa + cutidapattes;

                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);
                        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);

                        int tahun = Integer.parseInt(year(tanggalmasuk.getText().toString())) + 1;
                        String tahundepan = String.valueOf(tahun);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");
                        Date date1 = null;
                        try {
                            date1 = sdf.parse(tanggalbulan(tanggalmasuk.getText().toString()) + " " + tahundepan);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date date2 = null;
                        try {
                            date2 = sdf.parse(formattedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(date1.compareTo(date2) > 0)
                        {
                            params.put("hak_cuti_utuh", String.valueOf(totalcuti));
                        }
                        else if(date1.compareTo(date2) < 0)
                        {
                            params.put("hak_cuti_utuh", String.valueOf(totalcuti));
                        }
                        else if(date1.compareTo(date2) == 0)
                        {
                            params.put("hak_cuti_utuh", String.valueOf(totalcuti));
                        }





                        params.put("tahun", tahunnext.getText().toString());
                        params.put("nik_sisa_cuti", nik.getText().toString());

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
                RequestQueue requestQueue = Volley.newRequestQueue(setting_cuti.this);
                requestQueue.add(stringRequest);

            }
        });

        getSisaCuti();
        getBiodata();
    }

    private void updatehakcuti() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statuscuti",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                        setting_cuti.this.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                        setting_cuti.this.finish();
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

                params.put("nik_baru", nik.getText().toString());

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
        RequestQueue requestQueue = Volley.newRequestQueue(setting_cuti.this);
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
                            setting_cuti.this);
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
                                    Intent intent = new Intent(setting_cuti.this, MainActivity.class);
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

    private void getSisaCuti() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/cuti/index?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                sisacutimodel movieItem = new sisacutimodel(
                                        movieObject.getString("id_cuti"),
                                        movieObject.getInt("hak_cuti_utuh"),
                                        movieObject.getString("tahun"),
                                        movieObject.getString("start_efektif_cuti"));

                                movieItemList3.add(movieItem);
                            }
                            sisacutimodel item = movieItemList3.get(movieItemList3.size() - 1);
                            sisacuti.setText(String.valueOf(item.getHak_cuti_utuh()));
                            tahunnext.setText(String.valueOf(item.getTahun()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cutidapat.setText("0");
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

    private void getBiodata() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        final String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);

                                nama.setText(movieObject.getString("nama_karyawan_struktur"));
                                depodept.setText(movieObject.getString("lokasi_struktur") +"/" + movieObject.getString("dept_struktur"));
                                jabatan.setText(movieObject.getString("jabatan_karyawan"));
                                tanggalmasuk.setText(convertFormat(movieObject.getString("join_date_struktur")));

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_nik?nik_baru=" + nik_baru,
                                        new Response.Listener<String>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject obj = new JSONObject(response);
                                                    JSONArray movieArray = obj.getJSONArray("data");
                                                    for (int i = 0; i < movieArray.length(); i++) {
                                                        JSONObject movieObject = movieArray.getJSONObject(i);

                                                        nik.setText(movieObject.getString("nik_baru"));
                                                        tanggalresign.setText(convertFormat(movieObject.getString("tanggal_efektif_resign")));

                                                        String start = tahun(movieObject.getString("tanggal_efektif_resign"))  +"-01-01";
                                                        String end = movieObject.getString("tanggal_efektif_resign");
                                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                                        LocalDate from = LocalDate.parse(start, formatter);
                                                        LocalDate to = LocalDate.parse(end, formatter);
                                                        int date = Integer.parseInt(number(movieObject.getString("tanggal_efektif_resign")));

                                                        Date c = Calendar.getInstance().getTime();
                                                        System.out.println("Current time => " + c);
                                                        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                                                        String formattedDate = df.format(c);

                                                        int year = Integer.parseInt(year(tanggalmasuk.getText().toString())) + 1;
                                                        String tahundepan = String.valueOf(year);

                                                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");
                                                        Date date1 = sdf.parse(tanggalbulan(tanggalmasuk.getText().toString()) + " " + tahundepan);
                                                        Date date2 = sdf.parse(formattedDate);

                                                        if(date1.compareTo(date2) > 0)
                                                        {
                                                            if ((1 <= date && date <= 15)){
                                                                String tanggal = String.valueOf(from.until(to, ChronoUnit.MONTHS));
                                                                cutidapat.setText(tanggal);
                                                            } else if((16 <= date && date <= 31)){
                                                                String tanggal = String.valueOf(from.until(to, ChronoUnit.MONTHS) + 1);
                                                                cutidapat.setText(tanggal);
                                                            }
                                                        }
                                                        else if(date1.compareTo(date2) < 0)
                                                        {
                                                            if ((1 <= date && date <= 15)){
                                                                String tanggal = String.valueOf(from.until(to, ChronoUnit.MONTHS));
                                                                cutidapat.setText(tanggal);
                                                            } else if((16 <= date && date <= 31)){
                                                                String tanggal = String.valueOf(from.until(to, ChronoUnit.MONTHS) + 1);
                                                                cutidapat.setText(tanggal);
                                                            }
                                                        }
                                                        else if(date1.compareTo(date2) == 0)
                                                        {
                                                            if ((1 <= date && date <= 15)){
                                                                String tanggal = String.valueOf(from.until(to, ChronoUnit.MONTHS));
                                                                cutidapat.setText(tanggal);
                                                            } else if((16 <= date && date <= 31)){
                                                                String tanggal = String.valueOf(from.until(to, ChronoUnit.MONTHS) + 1);
                                                                cutidapat.setText(tanggal);
                                                            }
                                                        }

//                                                        if(date1.compareTo(date2) > 0) {

//                                                        } else if(date1.compareTo(date2) < 0) {

//                                                        } else if(date1.compareTo(date2) == 0) {

//                                                        }


//                                                        if (strDate.before(new Date())) {
//                                                        } else{

//                                                        }
                                                    }
                                                } catch (JSONException | ParseException e) {
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
                                                7200000,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                        )
                                );
                                RequestQueue requestQueue = Volley.newRequestQueue(setting_cuti.this);
                                requestQueue.add(stringRequest);



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
                        7200000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
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

    public static String tahun(String inputDate) {
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy");
        return convetDateFormat.format(date);
    }

    public static String year(String inputDate) {
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy");
        return convetDateFormat.format(date);
    }

    public static String number(String inputDate) {
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd");
        return convetDateFormat.format(date);
    }

    public static String tanggalbulan(String inputDate) {
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMMM");
        return convetDateFormat.format(date);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

        
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}