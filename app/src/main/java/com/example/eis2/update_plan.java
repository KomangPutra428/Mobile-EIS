package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class update_plan extends AppCompatActivity {
    TextView tanggal_date, jam, keterangan;
    EditText jam_mulai, catatan;
    Button hapus, simpan;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;

    AutoCompleteTextView durasi_jam, kategori_kegiatan;

    String start_jam, end_jam, keterangan_plan, kategorikegiatan;

    MaterialButton restore;

    MaterialToolbar dailynBar;
    NavigationView navigation;
    DrawerLayout drawer_layout;

    String jams [] = {"1 Jam", "2 Jam",
            "3 Jam", "4 Jam",
            "5 Jam", "6 Jam",
            "7 Jam", "8 Jam",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_plan);

        tanggal_date = findViewById(R.id.tanggal_date);
        jam = findViewById(R.id.jam);
        keterangan = findViewById(R.id.keterangan);

        kategori_kegiatan = findViewById(R.id.kategori_kegiatan);

        jam_mulai = findViewById(R.id.jam_mulai);
        durasi_jam = findViewById(R.id.durasijam);
        catatan = findViewById(R.id.catatan);

        hapus = findViewById(R.id.hapus);
        simpan = findViewById(R.id.simpan);

        dailynBar = findViewById(R.id.dailynBar);
        navigation = findViewById(R.id.navigation);
        drawer_layout = findViewById(R.id.drawer_layout);

        restore = findViewById(R.id.restore);

        final ArrayList<String> kategori_list = new ArrayList<>();


        StringRequest kota = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_kategori",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String id = jsonObject1.getString("id");
                                    String kategori = jsonObject1.getString("kategori");
                                    kategori_list.add(id + ". " +kategori);

                                }
                            }
                            kategori_kegiatan.setAdapter(new ArrayAdapter<String>(update_plan.this, android.R.layout.simple_expandable_list_item_1, kategori_list));
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
        kota.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestkota = Volley.newRequestQueue(update_plan.this);
        requestkota.add(kota);

        kategori_kegiatan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    kategori_kegiatan.showDropDown();
            }
        });

        kategori_kegiatan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                kategori_kegiatan.showDropDown();
                return false;
            }
        });

        dailynBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(Gravity.LEFT);
            }
        });

        durasi_jam.setAdapter(new ArrayAdapter<String>(update_plan.this, android.R.layout.simple_dropdown_item_1line, jams));

        durasi_jam.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    durasi_jam.showDropDown();
            }
        });

        durasi_jam.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                durasi_jam.showDropDown();
                return false;
            }
        });

        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jam_mulai.setText(start_jam);
                durasi_jam.setText(end_jam);
                catatan.setText(keterangan_plan);
                kategori_kegiatan.setText(kategorikegiatan);
            }
        });

        jam_mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(update_plan.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        jam_mulai.setText(String.format("%02d:%02d:00", selectedHour, selectedMinute));

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Pilih Jam");
                mTimePicker.show();
            }
        });

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
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
                            update_plan.this);
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
                                    Intent intent = new Intent(update_plan.this, MainActivity.class);
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
                    drawer_layout.closeDrawers(); // close the all open Drawer Views
                    return true;
                }
                return false;
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(update_plan.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_hapus_per_id",
                        new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    hideDialog();
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Data Sudah Dihapus", Toast.LENGTH_LONG).show();
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

                            params.put("id", getIntent().getStringExtra("ID"));


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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(update_plan.this);
                    requestQueue2.add(stringRequest2);

            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (durasi_jam.getText().toString().length() == 0){
                    durasi_jam.setError("Isi Durasi Jam");
                } else if(catatan.getText().toString().length() == 0){
                    catatan.setError("Silahkan Isi Plan");
                } else if(kategori_kegiatan.getText().toString().length() == 0){
                    kategori_kegiatan.setError("Isi Kategori");
                } else {
                    pDialog = new ProgressDialog(update_plan.this);
                    showDialog();
                    pDialog.setContentView(R.layout.progress_dialog);
                    pDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_update_plan",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    hideDialog();
                                    Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    hideDialog();
                                    Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                                    finish();
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

                            String durasijam = durasi_jam.getText().toString();
                            String[] splited_text2 = durasijam.split(" ");
                            durasijam = splited_text2[0];
                            durasijam = durasijam.replace(" ", "");

                            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            final String currentDateandTime = jam_mulai.getText().toString();
                            Date date = null;
                            try {
                                date = sdf.parse(currentDateandTime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            final Calendar calendar = Calendar.getInstance();

                            calendar.setTime(date);
                            calendar.add(Calendar.HOUR, Integer.parseInt(durasijam));

                            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                            String strDate = dateFormat.format(calendar.getTime());

                            String kategori_id = kategori_kegiatan.getText().toString();
                            String[] splited_text = kategori_id.split("\\.");
                            kategori_id = splited_text[0];

                            params.put("id", getIntent().getStringExtra("ID"));
                            params.put("kategori", kategori_id);

                            params.put("start", jam_mulai.getText().toString());
                            params.put("end", strDate);
                            params.put("ket_plan", catatan.getText().toString());

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
                    RequestQueue requestQueue = Volley.newRequestQueue(update_plan.this);
                    requestQueue.add(stringRequest);
                }


            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_id?id=" + getIntent().getStringExtra("ID"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                tanggal_date.setText(tanggalhari(movieObject.getString("date")));
                                jam.setText(movieObject.getString("nama_kategori") + " • " + movieObject.getString("start") + " - " + movieObject.getString("end"));
                                keterangan.setText(movieObject.getString("ket_plan"));

                                kategori_kegiatan.setText(movieObject.getString("kategori") + ". " + movieObject.getString("nama_kategori"));
                                jam_mulai.setText(movieObject.getString("start"));
                                catatan.setText(movieObject.getString("ket_plan"));

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                                Date startDate = simpleDateFormat.parse(movieObject.getString("start"));
                                Date endDate = simpleDateFormat.parse(movieObject.getString("end"));

                                long difference = endDate.getTime() - startDate.getTime();
                                if(difference<0)
                                {
                                    Date dateMax = simpleDateFormat.parse("24:00:00");
                                    Date dateMin = simpleDateFormat.parse("00:00:00");
                                    difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
                                }
                                int days = (int) (difference / (1000*60*60*24));
                                int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                                int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                                durasi_jam.setText(String.valueOf(hours) + " Jam");

                                start_jam = movieObject.getString("start");
                                end_jam = String.valueOf(hours) + " Jam";
                                keterangan_plan = movieObject.getString("ket_plan");
                                kategorikegiatan = movieObject.getString("kategori") + ". " + movieObject.getString("nama_kategori");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Belum ada Pengajuan", Toast.LENGTH_SHORT).show();
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static String tanggalhari(String inputDate) {
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        return convetDateFormat.format(date);
    }

    private void showDialog () {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog () {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}