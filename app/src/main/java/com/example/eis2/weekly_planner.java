package com.example.eis2;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.izin.txt_nomor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.planmodels;
import com.example.eis2.Item.tanggalmodel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

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
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class weekly_planner extends AppCompatActivity {
    RecyclerView draft;
    ArrayList<tanggalmodel> tanggalmodels = new ArrayList<>();
    ListViewAdapterDraft adapterDraft;
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;
    MaterialCardView list_realization;
    TextView add;
    private SimpleDateFormat dateFormatter;
    private Calendar date;
    Button hapus, simpan;
    CheckBox checkall;
    List<String> list = new ArrayList<>();


    MaterialToolbar dailynBar;
    NavigationView navigation;
    DrawerLayout drawer_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_planner);
        HttpsTrustManager.allowAllSSL();
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        draft = findViewById(R.id.draft);
        list_realization = findViewById(R.id.list_realization);
        add = findViewById(R.id.add);
        hapus = findViewById(R.id.hapus);
        simpan = findViewById(R.id.simpan);
        checkall = findViewById(R.id.checkall);
        dailynBar = findViewById(R.id.dailynBar);
        navigation = findViewById(R.id.navigation);
        drawer_layout = findViewById(R.id.drawer_layout);

        dailynBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(Gravity.LEFT);
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
                            weekly_planner.this);
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
                                    Intent intent = new Intent(weekly_planner.this, MainActivity.class);
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


        checkall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    getDataDraft();
                    for(tanggalmodel person:tanggalmodels){
                        person.setSelected(true);
                        adapterDraft.notifyDataSetChanged();

                        adapterDraft = new ListViewAdapterDraft(weekly_planner.this, tanggalmodels);
                        draft.setItemViewCacheSize(tanggalmodels.size());
                        draft.setAdapter(adapterDraft);

                    }


                } else if (!b) {
                    list.clear();
                    for(tanggalmodel person:tanggalmodels){
                        person.setSelected(false);
                        adapterDraft.notifyDataSetChanged();

                        adapterDraft = new ListViewAdapterDraft(weekly_planner.this, tanggalmodels);
                        draft.setItemViewCacheSize(tanggalmodels.size());
                        draft.setAdapter(adapterDraft);
                    }
                }

            }
        });


        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tanggalmodels.size() == 0){
                    Toast.makeText(getApplicationContext(), "Data Masih Kosong", Toast.LENGTH_LONG).show();
                } else if(list.size() == 0){
                    Toast.makeText(getApplicationContext(), "Silahkan Checklist Terlebih Dahulu", Toast.LENGTH_LONG).show();
                } else {
                    hapusall();

                }
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tanggalmodels.size() == 0){
                    Toast.makeText(getApplicationContext(), "Data Masih Kosong", Toast.LENGTH_LONG).show();
                } else if(list.size() == 0){
                    Toast.makeText(getApplicationContext(), "Silahkan Checklist Terlebih Dahulu", Toast.LENGTH_LONG).show();
                } else {
                    postall();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(weekly_planner.this);
                dialog.setContentView(R.layout.add_daily);
                dialog.show();

                final EditText pilihtanggal = dialog.findViewById(R.id.pilihtanggal);
                final AutoCompleteTextView catatan = dialog.findViewById(R.id.catatan);
                final EditText jam_mulai = dialog.findViewById(R.id.jam_mulai);
                final AutoCompleteTextView durasi_jam = dialog.findViewById(R.id.durasi_jam);
                final AutoCompleteTextView kategori = dialog.findViewById(R.id.kategori);

                final ArrayList<String> kategori_list = new ArrayList<>();
                final ArrayList<String> Keterangan_list = new ArrayList<>();

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                StringRequest keterangan = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_History?nik_baru=" + nik_baru,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String kategori = jsonObject1.getString("ket_plan");
                                            Keterangan_list.add(kategori);

                                        }
                                    }
                                    catatan.setAdapter(new ArrayAdapter<String>(weekly_planner.this, android.R.layout.simple_expandable_list_item_1, Keterangan_list));
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
                keterangan.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestketerangan = Volley.newRequestQueue(weekly_planner.this);
                requestketerangan.add(keterangan);




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
                                    kategori.setAdapter(new ArrayAdapter<String>(weekly_planner.this, android.R.layout.simple_expandable_list_item_1, kategori_list));
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
                kota.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestkota = Volley.newRequestQueue(weekly_planner.this);
                requestkota.add(kota);

                String jam [] = {"1 Jam", "2 Jam",
                        "3 Jam", "4 Jam",
                        "5 Jam", "6 Jam",
                        "7 Jam", "8 Jam",};

                durasi_jam.setAdapter(new ArrayAdapter<String>(weekly_planner.this, android.R.layout.simple_dropdown_item_1line, jam));

                Button batal = dialog.findViewById(R.id.batal);
                Button tambahkan = dialog.findViewById(R.id.tambahkan);

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tambahkan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pilihtanggal.getText().toString().length() == 0) {
                            pilihtanggal.setError("Pilih Tanggal");
                        } else if (catatan.getText().toString().length() == 0) {
                            catatan.setError("Isi Uraian Pekerjaan");
                        } else if (jam_mulai.getText().toString().length() == 0) {
                            jam_mulai.setError("Isi Start Time");
                        } else if (kategori.getText().toString().length() == 0) {
                            kategori.setError("Isi Kategori");
                        } else if (durasi_jam.getText().toString().length() == 0) {
                            durasi_jam.setError("Isi Durasi Jam");
                        } else {
                            dialog.dismiss();
                            pDialog = new ProgressDialog(weekly_planner.this);
                            showDialog();
                            pDialog.setContentView(R.layout.progress_dialog);
                            pDialog.getWindow().setBackgroundDrawableResource(
                                    android.R.color.transparent
                            );
                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_Daily",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            hideDialog();
                                            Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                            getDraft();

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
                                    String nik_baru = sharedPreferences.getString(KEY_NIK, null);

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

                                    String kategori_id = kategori.getText().toString();
                                    String[] splited_text = kategori_id.split("\\.");
                                    kategori_id = splited_text[0];

                                    params.put("nik_baru", nik_baru);
                                    params.put("jabatan", menu.text_jabatan.getText().toString());
                                    params.put("date", convertFormat2(pilihtanggal.getText().toString()));
                                    params.put("kategori", kategori_id);
                                    params.put("start", jam_mulai.getText().toString());
                                    params.put("end", strDate);
                                    params.put("ket_plan", catatan.getText().toString());

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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(weekly_planner.this);
                            requestQueue2.add(stringRequest2);
                        }
                    }
                });

                pilihtanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar currentDate = Calendar.getInstance();
                        Calendar today = (Calendar) currentDate.clone();
                        today.add(Calendar.DATE, 0);

                        date = currentDate.getInstance();

                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth);

                                pilihtanggal.setText(dateFormatter.format(date.getTime()));
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(weekly_planner.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
                        datePickerDialog.show();
                    }
                });

                pilihtanggal.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_get_lastId?nik_baru=" + nik_baru + "&tanggal=" + convertFormat2(pilihtanggal.getText().toString()),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            JSONArray movieArray = obj.getJSONArray("data");

                                            for (int i = 0; i < movieArray.length(); i++) {

                                                JSONObject movieObject = movieArray.getJSONObject(i);
                                                jam_mulai.setText(movieObject.getString("end"));


                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        jam_mulai.setText("08:00:00");
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
                        RequestQueue requestQueue = Volley.newRequestQueue(weekly_planner.this);
                        requestQueue.add(stringRequest);
                    }
                });


            }
        });


        list_realization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), saved_dailyactivity.class);
                startActivity(intent);
            }
        });

    }

    private void getDataDraft() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_draft_nik?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                list.add(movieObject.getString("id"));


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Belum Ada List Activity", Toast.LENGTH_SHORT).show();
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void hapusall() {
        pDialog = new ProgressDialog(weekly_planner.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
               // jam_awal.clear();
                hideDialog();
                Toast.makeText(getApplicationContext(), "data sudah dihapus", Toast.LENGTH_LONG).show();
                getDraft();
            }
        }, 3000);
        for (int i = 0; i < list.size(); i++) {

            final int finalI1 = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_hapus_per_id",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

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

                    params.put("id", list.get(finalI1).toString());


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
            RequestQueue requestQueue2 = Volley.newRequestQueue(weekly_planner.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void postall() {
        pDialog = new ProgressDialog(weekly_planner.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideDialog();
                list.clear();
                // jam_awal.clear();
                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), saved_dailyactivity.class);
                startActivity(i);
            }
        }, 3000);
        for (int i = 0; i <= list.size(); i++) {
            final int finalI1 = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_simpan_per_id",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

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

                        params.put("id", list.get(finalI1).toString());



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
                RequestQueue requestQueue2 = Volley.newRequestQueue(weekly_planner.this);
                requestQueue2.add(stringRequest2);

        }
    }

    private void getDraft() {
        checkall.setChecked(false);
        tanggalmodels.clear();

        pDialog = new ProgressDialog(weekly_planner.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        adapterDraft = new ListViewAdapterDraft(weekly_planner.this, tanggalmodels);
        draft.setLayoutManager(new LinearLayoutManager(weekly_planner.this));
        draft.setAdapter(adapterDraft);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_tanggal?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                tanggalmodel movieItem = new tanggalmodel(
                                        movieObject.getString("id"),
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("date"));
                                tanggalmodels.add(movieItem);


                                hideDialog();

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
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(weekly_planner.this);
        requestQueue.add(stringRequest);

    }

    public class ListViewAdapterDraft extends RecyclerView.Adapter<ListViewAdapterDraft.ViewProcessHolder> implements CompoundButton.OnCheckedChangeListener {
        Context context;
        private ArrayList<tanggalmodel> tanggalmodels;

        public ListViewAdapterDraft(Context context, ArrayList<tanggalmodel> tanggalmodels) {
            this.context = context;
            this.tanggalmodels = tanggalmodels;
            //        this.onItemClick = onItemCheckListener;
        }

        @Override
        public ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_draft, parent, false); //memanggil layout list recyclerview
            //ViewProcessHolder processHolder = new ViewProcessHolder(view);

            ViewProcessHolder processHolder = new ViewProcessHolder(view);
            return processHolder;
        }

        @Override
        public void onBindViewHolder(final ViewProcessHolder holder, @SuppressLint("RecyclerView") final int position) {

            final tanggalmodel data = tanggalmodels.get(position);
            holder.tanggal_date.setText(convertFormat(data.getDate()));

            holder.checktanggal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tanggalmodels.get(position).getSelected()) {
                        tanggalmodels.get(position).setSelected(false);
                    } else {
                        tanggalmodels.get(position).setSelected(true);
                    }

                }
            });

            if(data.getSelected() == true){
                holder.checktanggal.setChecked(true);
            } else {
                holder.checktanggal.setChecked(false);
            }

            holder.checktanggal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(b) {
                        for(planmodels person:holder.planmodelsList){
                            person.setSelected(true);
                            holder.adapterKeteranganDraft.notifyDataSetChanged();
                        }
                    } else if (!b) {
                        checkall.setChecked(false);
                        for(planmodels person:holder.planmodelsList){
                            person.setSelected(false);
                            holder.adapterKeteranganDraft.notifyDataSetChanged();
                        }
                    }
                }
            });

            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            String nik_baru = sharedPreferences.getString(KEY_NIK, null);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_tanggal_nik?nik_baru=" + nik_baru + "&tanggal=" + data.getDate(),
                    new Response.Listener<String>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");

                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    planmodels movieItem = new planmodels(
                                            movieObject.getString("id"),
                                            movieObject.getString("date"),
                                            movieObject.getString("start"),
                                            movieObject.getString("start_realisasi"),
                                            movieObject.getString("end"),
                                            movieObject.getString("end_realisasi"),
                                            movieObject.getString("ket_plan"),
                                            movieObject.getString("ket_realisasi"),
                                            movieObject.getString("status"),
                                            movieObject.getString("pengganti"),
                                            movieObject.getString("nama_kategori"),
                                            movieObject.getString("draft"));
                                    holder.planmodelsList.add(movieItem);

                                    holder.adapterKeteranganDraft = new ListViewAdapterKeteranganDraft(weekly_planner.this, holder.planmodelsList);
                                    holder.listdata.setLayoutManager(new LinearLayoutManager(weekly_planner.this));
                                    holder.listdata.setAdapter(holder.adapterKeteranganDraft);
                                    holder.adapterKeteranganDraft.notifyDataSetChanged();

                                    if(data.getSelected() == true){
                                        for(planmodels person:holder.planmodelsList) {
                                            person.setSelected(true);
                                            holder.adapterKeteranganDraft.notifyDataSetChanged();
                                        }
                                    } else {
                                        for(planmodels person:holder.planmodelsList){
                                            person.setSelected(false);
                                            holder.adapterKeteranganDraft.notifyDataSetChanged();
                                        }
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
            RequestQueue requestQueue = Volley.newRequestQueue(weekly_planner.this);
            requestQueue.add(stringRequest);
        }

        @Override
        public int getItemCount() {
            return tanggalmodels.size();
        }

        public tanggalmodel getItem(int position) {
            return tanggalmodels.get(position);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }

        public class ViewProcessHolder extends RecyclerView.ViewHolder {

            TextView tanggal_date;
            Context context;

            CheckBox checktanggal;
            View itemView;

            ArrayList<planmodels> planmodelsList;
            RecyclerView listdata;
            ListViewAdapterKeteranganDraft adapterKeteranganDraft;

            RecyclerView.LayoutManager mManager;


            public ViewProcessHolder(View itemView) {
                super(itemView);

                this.itemView = itemView;

                context = itemView.getContext();
                planmodelsList = new ArrayList<>();

                listdata = (RecyclerView) itemView.findViewById(R.id.listdata);
                tanggal_date = (TextView) itemView.findViewById(R.id.tanggal_date);
                checktanggal = (CheckBox) itemView.findViewById(R.id.checktanggal);

                mManager = new LinearLayoutManager(weekly_planner.this, LinearLayoutManager.VERTICAL, false);
                listdata.setLayoutManager(mManager);
                listdata.setHasFixedSize(true);





            }
        }
    }

    public class ListViewAdapterKeteranganDraft extends RecyclerView.Adapter<ListViewAdapterKeteranganDraft.ViewProcessHolder> implements CompoundButton.OnCheckedChangeListener {
        Context context;
        private ArrayList<planmodels> planmodels;

        public ListViewAdapterKeteranganDraft(Context context, ArrayList<planmodels> planmodels) {
            this.context = context;
            this.planmodels = planmodels;
        }

        @Override
        public ListViewAdapterKeteranganDraft.ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_draft_keterangan, parent, false); //memanggil layout list recyclerview

            ListViewAdapterKeteranganDraft.ViewProcessHolder processHolder = new ListViewAdapterKeteranganDraft.ViewProcessHolder(view);
            return processHolder;
        }

        @Override
        public void onBindViewHolder(final ListViewAdapterKeteranganDraft.ViewProcessHolder holder, @SuppressLint("RecyclerView") final int position) {

            final planmodels data = planmodels.get(position);
            if(data.getStart().equals("00:00:00")){
                holder.edit_planner.setVisibility(View.VISIBLE);
            } else {
                holder.edit_planner.setVisibility(View.GONE);
            }

            holder.edit_planner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(weekly_planner.this);
                    dialog.setContentView(R.layout.add_daily);
                    dialog.show();



                    final EditText pilihtanggal = dialog.findViewById(R.id.pilihtanggal);
                    final AutoCompleteTextView catatan = dialog.findViewById(R.id.catatan);
                    final EditText jam_mulai = dialog.findViewById(R.id.jam_mulai);
                    final AutoCompleteTextView durasi_jam = dialog.findViewById(R.id.durasi_jam);
                    final AutoCompleteTextView kategori = dialog.findViewById(R.id.kategori);

                    final ArrayList<String> kategori_list = new ArrayList<>();
                    final ArrayList<String> Keterangan_list = new ArrayList<>();

                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                    String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                    StringRequest keterangan = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_History?nik_baru=" + nik_baru,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getString("status").equals("true")) {
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                String kategori = jsonObject1.getString("ket_plan");
                                                Keterangan_list.add(kategori);

                                            }
                                        }
                                        catatan.setAdapter(new ArrayAdapter<String>(weekly_planner.this, android.R.layout.simple_expandable_list_item_1, Keterangan_list));
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
                    keterangan.setRetryPolicy(new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestketerangan = Volley.newRequestQueue(weekly_planner.this);
                    requestketerangan.add(keterangan);




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
                                        kategori.setAdapter(new ArrayAdapter<String>(weekly_planner.this, android.R.layout.simple_expandable_list_item_1, kategori_list));
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
                    kota.setRetryPolicy(new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestkota = Volley.newRequestQueue(weekly_planner.this);
                    requestkota.add(kota);

                    String jam [] = {"1 Jam", "2 Jam",
                            "3 Jam", "4 Jam",
                            "5 Jam", "6 Jam",
                            "7 Jam", "8 Jam",};

                    durasi_jam.setAdapter(new ArrayAdapter<String>(weekly_planner.this, android.R.layout.simple_dropdown_item_1line, jam));

                    Button batal = dialog.findViewById(R.id.batal);
                    Button tambahkan = dialog.findViewById(R.id.tambahkan);

                    batal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    jam_mulai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Calendar mcurrentTime = Calendar.getInstance();
                            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                            int minute = mcurrentTime.get(Calendar.MINUTE);

                            TimePickerDialog mTimePicker;
                            mTimePicker = new TimePickerDialog(weekly_planner.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    jam_mulai.setText(String.format("%02d:%02d:00", selectedHour, selectedMinute));

                                }
                            }, hour, minute, true);//Yes 24 hour time
                            mTimePicker.setTitle("Pilih Jam");
                            mTimePicker.show();
                        }
                    });

                    tambahkan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (pilihtanggal.getText().toString().length() == 0) {
                                pilihtanggal.setError("Pilih Tanggal");
                            } else if (catatan.getText().toString().length() == 0) {
                                catatan.setError("Isi Uraian Pekerjaan");
                            } else if (jam_mulai.getText().toString().length() == 0 || jam_mulai.getText().toString().equals("00:00:00")) {
                                jam_mulai.setError("Isi Start Time");
                            } else if (kategori.getText().toString().length() == 0) {
                                kategori.setError("Isi Kategori");
                            } else if (durasi_jam.getText().toString().length() == 0) {
                                durasi_jam.setError("Isi Durasi Jam");
                            } else {
                                dialog.dismiss();
                                pDialog = new ProgressDialog(weekly_planner.this);
                                showDialog();
                                pDialog.setContentView(R.layout.progress_dialog);
                                pDialog.getWindow().setBackgroundDrawableResource(
                                        android.R.color.transparent
                                );
                                StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_edit_per_id",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                hideDialog();
                                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                                getDraft();

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
                                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

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

                                        String kategori_id = kategori.getText().toString();
                                        String[] splited_text = kategori_id.split("\\.");
                                        kategori_id = splited_text[0];

                                        params.put("id", data.getId());
                                        params.put("date", convertFormat2(pilihtanggal.getText().toString()));
                                        params.put("start", jam_mulai.getText().toString());
                                        params.put("end", strDate);

                                        params.put("kategori", kategori_id);
                                        params.put("ket_plan", catatan.getText().toString());

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
                                RequestQueue requestQueue2 = Volley.newRequestQueue(weekly_planner.this);
                                requestQueue2.add(stringRequest2);
                            }
                        }
                    });

                    pilihtanggal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Calendar currentDate = Calendar.getInstance();
                            Calendar today = (Calendar) currentDate.clone();
                            today.add(Calendar.DATE, 0);

                            date = currentDate.getInstance();

                            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                    date.set(year, monthOfYear, dayOfMonth);

                                    pilihtanggal.setText(dateFormatter.format(date.getTime()));
                                }
                            };
                            DatePickerDialog datePickerDialog = new DatePickerDialog(weekly_planner.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                            datePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
                            datePickerDialog.show();
                        }
                    });

                    pilihtanggal.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                            String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_get_lastId?nik_baru=" + nik_baru + "&tanggal=" + convertFormat2(pilihtanggal.getText().toString()),
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                JSONObject obj = new JSONObject(response);
                                                JSONArray movieArray = obj.getJSONArray("data");

                                                for (int i = 0; i < movieArray.length(); i++) {

                                                    JSONObject movieObject = movieArray.getJSONObject(i);
                                                    jam_mulai.setText(movieObject.getString("end"));


                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            jam_mulai.setText("08:00:00");
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
                            RequestQueue requestQueue = Volley.newRequestQueue(weekly_planner.this);
                            requestQueue.add(stringRequest);
                        }
                    });
                }
            });


            holder.keterangan.setText(data.getKet_plan());
            holder.jam.setText(data.getNama_kategori() + " • " + data.getStart() + " - " + data.getEnd());
            holder.edit.setVisibility(View.GONE);
            holder.checkdetail.setTag(position);
            holder.checkdetail.setOnCheckedChangeListener(this);
            holder.checkdetail.setChecked(planmodels.get(position).getSelected());
            holder.checkdetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (planmodels.get(position).getSelected()) {
                        planmodels.get(position).setSelected(false);
                        list.remove(data.getId());
                    } else {
                        list.add(data.getId());
                        planmodels.get(position).setSelected(true);

                    }
                }
            });
            if(holder.checkdetail.isChecked()){
                list.add(data.getId());
            } else {
                list.remove(data.getId());
            }

            holder.checkdetail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(b) {
                        list.add(data.getId());
                    } else if (!b) {
                        list.remove(data.getId());
                        checkall.setChecked(false);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return planmodels.size();
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public int getItemViewType(int position){
            return position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }

        public class ViewProcessHolder extends RecyclerView.ViewHolder {

            TextView jam, keterangan;
            Context context;

            CheckBox checkdetail;
            View itemView;

            ImageView edit, edit_planner;



            public ViewProcessHolder(View itemView) {
                super(itemView);

                this.itemView = itemView;

                context = itemView.getContext();

                jam = (TextView) itemView.findViewById(R.id.jam);
                keterangan = (TextView) itemView.findViewById(R.id.keterangan);

                checkdetail = (CheckBox) itemView.findViewById(R.id.checkdetail);
                edit = (ImageView) itemView.findViewById(R.id.edit);

                edit_planner = (ImageView) itemView.findViewById(R.id.edit_planner);

            }
        }
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDraft();
    }
}