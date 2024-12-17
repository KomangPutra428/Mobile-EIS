package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
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
import com.example.eis2.Item.absenmanualmodel;
import com.example.eis2.Item.approveskmodel;
import com.example.eis2.Item.mutasilistmodel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.PendingIntent.getActivity;
import static android.view.View.GONE;
import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class lainnya extends AppCompatActivity {
    ImageButton suratketerangan, uniform, kontak, mutasi, absen, interview, campaign, dailybutton, depo, selfassesemnt;
    TextView skcount, mutasicount, absenmanual;

    private List<approveskmodel> movieItemList7;
    private List<mutasilistmodel> movieItemList8;
    private List<absenmanualmodel> movieItemList9;

    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lainnya);
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
        skcount = (TextView) findViewById(R.id.skcount);
        mutasicount = (TextView) findViewById(R.id.mutasicount);
        absenmanual = (TextView) findViewById(R.id.absenmanual);
        campaign = (ImageButton) findViewById(R.id.campaign);


        interview = (ImageButton) findViewById(R.id.interview);

        suratketerangan = (ImageButton) findViewById(R.id.suratketerangan);
        uniform = (ImageButton) findViewById(R.id.uniform);
        kontak = (ImageButton) findViewById(R.id.kontak);
        mutasi = (ImageButton) findViewById(R.id.mutasi);
        absen = (ImageButton) findViewById(R.id.absen);
        dailybutton = (ImageButton) findViewById(R.id.dailybutton);
        depo = (ImageButton) findViewById(R.id.depo);
        selfassesemnt = (ImageButton) findViewById(R.id.selfassesemnt);

        setNavigationDrawer();
        if ("1".equalsIgnoreCase(txt_alpha.getText().toString())) {
            skcount.setVisibility(View.INVISIBLE);
            mutasicount.setVisibility(View.INVISIBLE);
            absenmanual.setVisibility(View.INVISIBLE);
        } else if ("2".equalsIgnoreCase(txt_alpha.getText().toString())) {
            skcount.setVisibility(View.INVISIBLE);
            mutasicount.setVisibility(View.INVISIBLE);
            absenmanual.setVisibility(View.INVISIBLE);
        } else if ("3".equalsIgnoreCase(txt_alpha.getText().toString())) {
            skcount.setVisibility(View.INVISIBLE);
            mutasicount.setVisibility(View.INVISIBLE);
            absenmanual.setVisibility(View.INVISIBLE);
        }

        movieItemList7 = new ArrayList<>();
        movieItemList8 = new ArrayList<>();
        movieItemList9 = new ArrayList<>();


        suratketerangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(lainnya.this, spv_surat_keterangan_kerja.class);
                startActivity(i);
            }
        });

        campaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(lainnya.this, campaign.class);
                startActivity(i);
            }
        });

        dailybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                if (nik_baru.equals("0100052000") || (nik_baru.equals("1601000500")) || (nik_baru.equals("0100032000")))  {
                    Intent i = new Intent(lainnya.this, menudaily.class);
                    startActivity(i);
                } else if (text_jabatan.getText().toString().equals("284")) {
                    Intent i = new Intent(lainnya.this, menudaily.class);
                    startActivity(i);
                } else if (menu.department.equals("Sales and Distribution") || menu.department.equals("Sales Development") || menu.department.equals("Sales Operation")) {
                    Intent i = new Intent(lainnya.this, dailyactivity_sales.class);
                    startActivity(i);
                } else if ("1".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Maaf, anda tidak punya akses", Toast.LENGTH_SHORT).show();
                } else if ("2".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Maaf, anda tidak punya akses", Toast.LENGTH_SHORT).show();
                } else if ("3".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Maaf, anda tidak punya akses", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(lainnya.this, menudaily.class);
                    startActivity(i);
                }
            }
        });

        selfassesemnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), menu_covid.class);
                startActivity(intent);

            }
        });



        interview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int beta = Integer.parseInt(txt_alpha.getText().toString());
                pDialog = new ProgressDialog(lainnya.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
               if (4 <= beta && beta <= 10){
                   hideDialog();
                   Intent intent = new Intent (lainnya.this, pemenuhankaryawan.class);
                   startActivity(intent);
               } else {
                   sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                   String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                   final StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + nik_baru,
                           new com.android.volley.Response.Listener<String>() {
                               @Override
                               public void onResponse(String response) {

                                   try {
                                       JSONObject obj = new JSONObject(response);
                                       JSONArray movieArray = obj.getJSONArray("data");

                                       for (int i = 0; i < movieArray.length(); i++) {

                                           JSONObject movieObject = movieArray.getJSONObject(i);

                                           if (movieObject.getString("dept_struktur").equals("Warehouse Operation")) {
                                               Intent intent = new Intent(lainnya.this, ojt_wop_user.class);
                                               startActivity(intent);
                                           } else if (text_jabatan.getText().toString().equals("258") ||
                                                   text_jabatan.getText().toString().equals("259") ||
                                                   text_jabatan.getText().toString().equals("263") ||
                                                   text_jabatan.getText().toString().equals("264")) {
                                               Intent intent = new Intent(lainnya.this, ojt_spvretail_user.class);
                                               startActivity(intent);
                                           } else if (text_jabatan.getText().toString().equals("253")) {
                                               Intent intent = new Intent(lainnya.this, ojt_am_user.class);
                                               startActivity(intent);
                                           } else if (text_jabatan.getText().toString().equals("250") ||
                                                   text_jabatan.getText().toString().equals("251")) {
                                               Intent intent = new Intent(lainnya.this, ojt_cm_user.class);
                                               startActivity(intent);
                                           } else {
                                               Intent intent = new Intent(lainnya.this, ojt_user.class);
                                               startActivity(intent);
                                           }
                                           hideDialog();

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
                           )
                   );
                   RequestQueue requestQueue = Volley.newRequestQueue(lainnya.this);
                   requestQueue.add(stringRequest);
               }
            }
        });

        absen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(lainnya.this, menu_absen_manual.class);
                    startActivity(i);
            }
        });

        uniform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Maaf, anda tidak berhak mengajukan seragam", Toast.LENGTH_SHORT).show();
                } else if ("2".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Maaf, anda tidak berhak mengajukan seragam", Toast.LENGTH_SHORT).show();
                } else if ("3".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Maaf, anda tidak berhak mengajukan seragam", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(lainnya.this, seragam.class);
                    startActivity(i);
                }
            }
        });

        kontak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(lainnya.this, kontak_hr.class);
                startActivity(i);
            }
        });

        mutasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Maaf, anda tidak berhak mengajukan Rotasi", Toast.LENGTH_SHORT).show();
                } else if ("2".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Maaf, anda tidak berhak mengajukan Rotasi", Toast.LENGTH_SHORT).show();
                } else if ("3".equalsIgnoreCase(txt_alpha.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Maaf, anda tidak berhak mengajukan Rotasi", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(lainnya.this, menu_mutasi.class);
                    startActivity(i);
                }
            }
        });

        depo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(lainnya.this, listdepo.class);
                    startActivity(i);
            }
        });


        StringRequest stringRequest7 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/SK/index_atasan?jabatan_struktur=" + text_jabatan.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            int number7 = 0;

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                final approveskmodel movieItem = new approveskmodel(
                                        movieObject.getString("id"),
                                        movieObject.getString("submit_date"),
                                        movieObject.getString("no_urut"),
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("jabatan_karyawan"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("keperluan"),
                                        movieObject.getString("analisa"),
                                        movieObject.getString("status_atasan"),
                                        movieObject.getString("status_hrd"),
                                        movieObject.getString("lokasi_struktur"));

                                if (!txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (movieObject.getString("status_atasan").contains("0") && movieObject.getString("lokasi_struktur").equals(txt_lokasi.getText().toString()))
                                        number7++;
                                    {
                                        skcount.setText(String.valueOf(number7));
                                    }
                                } else if (txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (movieObject.getString("status_atasan").contains("0"))
                                        number7++;
                                    {
                                        skcount.setText(String.valueOf(number7));
                                    }
                                }

                                movieItemList7.add(movieItem);
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

        stringRequest7.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest7);

        StringRequest stringRequest8 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi/index_atasan?jabatan_struktur=" + text_jabatan.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            int number8 = 0;

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                final mutasilistmodel movieItem = new mutasilistmodel(
                                        movieObject.getString("id_mutasi_rotasi"),
                                        movieObject.getString("tanggal_pengajuan"),
                                        movieObject.getString("permintaan"),
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("nama_karyawan_mutasi"),
                                        movieObject.getString("lokasi_awal"),
                                        movieObject.getString("status_atasan"),
                                        "0");

                                if (!txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (movieObject.getString("status_atasan").contains("0") && movieObject.getString("lokasi_awal").equals(txt_lokasi.getText().toString()))
                                        number8++;
                                    {
                                        mutasicount.setText(String.valueOf(number8));
                                    }
                                } else if (txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (movieObject.getString("status_atasan").contains("0"))
                                        number8++;
                                    {
                                        mutasicount.setText(String.valueOf(number8));
                                    }
                                }

                                movieItemList8.add(movieItem);
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

        stringRequest8.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.getCache().clear();
        requestQueue2.add(stringRequest8);

        StringRequest stringRequest9 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Absen_manual2/index_atasan?jabatan_struktur=" + text_jabatan.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            int number9 = 0;

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                absenmanualmodel movieItem = new absenmanualmodel(
                                        movieObject.getString("id_absen"),
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("lokasi_struktur"),
                                        movieObject.getString("jenis_absen"),
                                        movieObject.getString("tanggal_absen"),
                                        movieObject.getString("waktu_absen"),
                                        movieObject.getString("ket_absen"),
                                        movieObject.getString("status"),
                                        movieObject.getString("tanggal"),
                                        movieObject.getString("status_2"),
                                        movieObject.getString("tanggal_2"));

                                if (!txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (movieObject.getString("status").contains("0") && movieObject.getString("lokasi_struktur").equals(txt_lokasi.getText().toString()))
                                        number9++;
                                    {
                                        absenmanual.setText(String.valueOf(number9));
                                    }
                                } else if (txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (movieObject.getString("status").contains("0"))
                                        number9++;
                                    {
                                        absenmanual.setText(String.valueOf(number9));
                                    }
                                }

                                movieItemList9.add(movieItem);
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

        stringRequest9.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue3 = Volley.newRequestQueue(this);
        requestQueue3.getCache().clear();
        requestQueue3.add(stringRequest9);
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
                            lainnya.this);
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
                                    Intent intent = new Intent(lainnya.this, MainActivity.class);
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
}