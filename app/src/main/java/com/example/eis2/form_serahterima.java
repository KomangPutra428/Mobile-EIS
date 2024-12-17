package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
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
import com.example.eis2.Item.Utility;
import com.example.eis2.Item.approvaldinasfull;
import com.example.eis2.Item.cutitahunanmodel;
import com.example.eis2.Item.serahterimaalatkerjamodel;
import com.example.eis2.Item.serahterimahardcopymodel;
import com.example.eis2.Item.serahterimaprojectmodel;
import com.example.eis2.Item.serahterimasdmmodel;
import com.example.eis2.Item.serahterimasoftcopymodel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_lokasi;

public class form_serahterima extends AppCompatActivity {
    EditText nikbaru, namakaryawan, jabatan,
            lokasi, depart, tanggalpengajuan,
            nik_penerima1, namapenerima_1, nik_penerima2, namapenerima_2, efektif;

    RadioButton diterima, tidakterima;

    TextView quest, empty1, empty2, empty3, empty4, empty5;

    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;

    ListView listserahterima, listhardcopy, listsoftcopy, listproject, listsdm;
    private List<serahterimaalatkerjamodel> serahterimaalatkerjamodels = new ArrayList<>();
    private List<serahterimahardcopymodel> serahterimahardcopymodels = new ArrayList<>();
    private List<serahterimasoftcopymodel> serahterimasoftcopymodels = new ArrayList<>();
    private List<serahterimaprojectmodel> serahterimaprojectmodels = new ArrayList<>();
    private List<serahterimasdmmodel> serahterimasdmmodels = new ArrayList<>();
    ImageButton approve, buttonshowalatkerja, buttonhidealatkerja, buttonshowhardcopy, buttonhidehardcopy,
            buttonshowsoftcopy, buttonhidesoftcopy, buttonshowproject, buttonhideproject, buttonshowsdm, buttonhidesdm;
    DrawerLayout dLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_serahterima);
        HttpsTrustManager.allowAllSSL();
        setNavigationDrawer();

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
        empty1 = findViewById(R.id.empty1);
        empty2 = findViewById(R.id.empty2);
        empty3 = findViewById(R.id.empty3);
        empty4 = findViewById(R.id.empty4);
        empty5 = findViewById(R.id.empty5);

        listhardcopy = findViewById(R.id.listhardcopy);
        listsoftcopy = findViewById(R.id.listsoftcopy);
        listproject = findViewById(R.id.listproject);
        listsdm = findViewById(R.id.listsdm);
        nikbaru = findViewById(R.id.nikbaru);
        namakaryawan = findViewById(R.id.namakaryawan);
        jabatan = findViewById(R.id.jabatan);
        approve = findViewById(R.id.approve);
        efektif = findViewById(R.id.efektif);
        diterima = findViewById(R.id.diterima);
        tidakterima = findViewById(R.id.tidakterima);
        listserahterima = findViewById(R.id.listserahterima);
        lokasi = findViewById(R.id.lokasi);
        depart = findViewById(R.id.depart);
        tanggalpengajuan = findViewById(R.id.tanggalpengajuan);
        nik_penerima1 = findViewById(R.id.nik_penerima1);
        namapenerima_1 = findViewById(R.id.namapenerima_1);
        nik_penerima2 = findViewById(R.id.nik_penerima2);
        namapenerima_2 = findViewById(R.id.namapenerima_2);

        buttonshowalatkerja = findViewById(R.id.buttonshowalatkerja);
        buttonhidealatkerja = findViewById(R.id.buttonhidealatkerja);

        buttonshowhardcopy = findViewById(R.id.buttonshowhardcopy);
        buttonhidehardcopy = findViewById(R.id.buttonhidehardcopy);

        buttonshowsoftcopy = findViewById(R.id.buttonshowsoftcopy);
        buttonhidesoftcopy = findViewById(R.id.buttonhidesoftcopy);

        buttonshowproject = findViewById(R.id.buttonshowproject);
        buttonhideproject = findViewById(R.id.buttonhideproject);

        buttonshowsdm = findViewById(R.id.buttonshowsdm);
        buttonhidesdm = findViewById(R.id.buttonhidesdm);


        buttonshowalatkerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = listserahterima.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listserahterima.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                listserahterima.setLayoutParams(params);
                Utility.setListViewHeightBasedOnChildren(listserahterima);
                buttonhidealatkerja.setVisibility(View.VISIBLE);
                buttonshowalatkerja.setVisibility(View.INVISIBLE);
            }
        });
        buttonhidealatkerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = listserahterima.getLayoutParams();
                params.height = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listserahterima.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                listserahterima.setLayoutParams(params);
                buttonshowalatkerja.setVisibility(View.VISIBLE);
                buttonhidealatkerja.setVisibility(View.INVISIBLE);
            }
        });

        buttonshowhardcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = listhardcopy.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listhardcopy.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                Utility.setListViewHeightBasedOnChildren(listhardcopy);

                listhardcopy.setLayoutParams(params);
                buttonhidehardcopy.setVisibility(View.VISIBLE);
                buttonshowhardcopy.setVisibility(View.INVISIBLE);
            }
        });
        buttonhidehardcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = listhardcopy.getLayoutParams();
                params.height = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listhardcopy.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                listhardcopy.setLayoutParams(params);
                buttonshowhardcopy.setVisibility(View.VISIBLE);
                buttonhidehardcopy.setVisibility(View.INVISIBLE);
            }
        });

        buttonshowsoftcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = listsoftcopy.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listsoftcopy.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                Utility.setListViewHeightBasedOnChildren(listsoftcopy);

                listsoftcopy.setLayoutParams(params);
                buttonhidesoftcopy.setVisibility(View.VISIBLE);
                buttonshowsoftcopy.setVisibility(View.INVISIBLE);
            }
        });
        buttonhidesoftcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = listsoftcopy.getLayoutParams();
                params.height = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listsoftcopy.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                listsoftcopy.setLayoutParams(params);
                buttonshowsoftcopy.setVisibility(View.VISIBLE);
                buttonhidesoftcopy.setVisibility(View.INVISIBLE);
            }
        });

        buttonshowproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = listproject.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listproject.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                Utility.setListViewHeightBasedOnChildren(listproject);

                listproject.setLayoutParams(params);
                buttonhideproject.setVisibility(View.VISIBLE);
                buttonshowproject.setVisibility(View.INVISIBLE);
            }
        });
        buttonhideproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = listproject.getLayoutParams();
                params.height = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listproject.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                listproject.setLayoutParams(params);
                buttonshowproject.setVisibility(View.VISIBLE);
                buttonhideproject.setVisibility(View.INVISIBLE);
            }
        });

        buttonshowsdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = listsdm.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listsdm.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                Utility.setListViewHeightBasedOnChildren(listsdm);
                listsdm.setLayoutParams(params);
                buttonhidesdm.setVisibility(View.VISIBLE);
                buttonshowsdm.setVisibility(View.INVISIBLE);
            }
        });
        buttonhidesdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = listsdm.getLayoutParams();
                params.height = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listsdm.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                }
                listsdm.setLayoutParams(params);
                buttonshowsdm.setVisibility(View.VISIBLE);
                buttonhidesdm.setVisibility(View.INVISIBLE);
            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getapproval();
            }
        });

        quest = findViewById(R.id.quest);

        quest.setPaintFlags(quest.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        String id = getIntent().getStringExtra(KEY_NIK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_penerima?id=" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);

                                nikbaru.setText(movieObject.getString("nik_baru"));
                                tanggalpengajuan.setText(convertFormat(movieObject.getString("submit_date")));
                                nik_penerima1.setText(movieObject.getString("nik_penerima_1"));
                                nik_penerima2.setText(movieObject.getString("nik_penerima_2"));

                                getBiodata();

                                getNama1();
                                getNama2();

                                getalatkerja();
                                getHardcopy();
                                getSoftCopy();
                                getProject();
                                getSdm();


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
                }
                else if (itemId == R.id.nav_exit) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            form_serahterima.this);
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
                                    Intent intent = new Intent(form_serahterima.this, MainActivity.class);
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

    private void getSdm() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_serahterimasdm?nik_baru=" + nikbaru.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final serahterimasdmmodel movieItem = new serahterimasdmmodel(
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("jabatan_sdm"),
                                        movieObject.getString("jumlah_sdm"),
                                        movieObject.getString("jenis_kelamin_sdm"),
                                        movieObject.getString("promosi_sdm"),
                                        movieObject.getString("mutasi_sdm"),
                                        movieObject.getString("demosi_sdm"),
                                        movieObject.getString("sp1_sdm"),
                                        movieObject.getString("sp2_sdm"),
                                        movieObject.getString("sp3_sdm"),
                                        movieObject.getString("keterangan_sdm"));

                                serahterimasdmmodels.add(movieItem);

                                ListViewAdapterSdm adapter = new ListViewAdapterSdm(serahterimasdmmodels, getApplicationContext());

                                listsdm.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        buttonshowsdm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ViewGroup.LayoutParams params = listsdm.getLayoutParams();
                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    listsdm.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                                }
                                Utility.setListViewHeightBasedOnChildren(listsdm);
                                listsdm.setLayoutParams(params);
                                buttonhidesdm.setVisibility(View.VISIBLE);
                                buttonshowsdm.setVisibility(View.INVISIBLE);
                                empty5.setVisibility(View.VISIBLE);
                            }
                        });
                        buttonhidesdm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ViewGroup.LayoutParams params = listsdm.getLayoutParams();
                                params.height = 0;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    listsdm.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                                }
                                listsdm.setLayoutParams(params);
                                buttonshowsdm.setVisibility(View.VISIBLE);
                                buttonhidesdm.setVisibility(View.INVISIBLE);
                                empty5.setVisibility(View.GONE);
                            }
                        });

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

    private void getProject() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_serahterimaproject?nik_baru=" + nikbaru.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final serahterimaprojectmodel movieItem = new serahterimaprojectmodel(
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("nama_project"),
                                        movieObject.getString("sdm_project"),
                                        movieObject.getString("hasil_project"),
                                        movieObject.getString("outstanding_project"),
                                        movieObject.getString("deadline_project"));

                                serahterimaprojectmodels.add(movieItem);

                                ListViewAdapterProjects adapter = new ListViewAdapterProjects(serahterimaprojectmodels, getApplicationContext());

                                listproject.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            buttonshowproject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ViewGroup.LayoutParams params = listproject.getLayoutParams();
                                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        listproject.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                                    }
                                    Utility.setListViewHeightBasedOnChildren(listproject);

                                    listproject.setLayoutParams(params);
                                    buttonhideproject.setVisibility(View.VISIBLE);
                                    buttonshowproject.setVisibility(View.INVISIBLE);
                                    empty4.setVisibility(View.VISIBLE);
                                }
                            });
                            buttonhideproject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ViewGroup.LayoutParams params = listproject.getLayoutParams();
                                    params.height = 0;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        listproject.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                                    }
                                    listproject.setLayoutParams(params);
                                    buttonshowproject.setVisibility(View.VISIBLE);
                                    buttonhideproject.setVisibility(View.INVISIBLE);
                                    empty4.setVisibility(View.GONE);
                                }
                            });

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        empty4.setVisibility(View.VISIBLE);
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

    private void getSoftCopy() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_serahterimasoftcopy?nik_baru=" + nikbaru.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final serahterimasoftcopymodel movieItem = new serahterimasoftcopymodel(
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("nama_softcopy"),
                                        movieObject.getString("no_software"),
                                        movieObject.getString("jenis_software"),
                                        movieObject.getString("keterangan_software"));

                                serahterimasoftcopymodels.add(movieItem);

                                ListViewAdapterSoftcopy adapter = new ListViewAdapterSoftcopy(serahterimasoftcopymodels, getApplicationContext());

                                listsoftcopy.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        buttonshowsoftcopy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ViewGroup.LayoutParams params = listsoftcopy.getLayoutParams();
                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    listsoftcopy.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                                }
                                Utility.setListViewHeightBasedOnChildren(listsoftcopy);

                                listsoftcopy.setLayoutParams(params);
                                buttonhidesoftcopy.setVisibility(View.VISIBLE);
                                buttonshowsoftcopy.setVisibility(View.INVISIBLE);
                                empty3.setVisibility(View.VISIBLE);
                            }
                        });
                        buttonhidesoftcopy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ViewGroup.LayoutParams params = listsoftcopy.getLayoutParams();
                                params.height = 0;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    listsoftcopy.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                                }
                                listsoftcopy.setLayoutParams(params);
                                buttonshowsoftcopy.setVisibility(View.VISIBLE);
                                buttonhidesoftcopy.setVisibility(View.INVISIBLE);
                                empty3.setVisibility(View.GONE);
                            }
                        });

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

    private void getHardcopy() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_serahterimahardcopy?nik_baru=" + nikbaru.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final serahterimahardcopymodel movieItem = new serahterimahardcopymodel(
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("nama_hardcopy"),
                                        movieObject.getString("jumlah_hardcopy"),
                                        movieObject.getString("satuan_hardcopy"),
                                        movieObject.getString("tempat_hardcopy"),
                                        movieObject.getString("keterangan_hardcopy"));

                                serahterimahardcopymodels.add(movieItem);

                                ListViewAdapterHardcopy adapter = new ListViewAdapterHardcopy(serahterimahardcopymodels, getApplicationContext());

                                listhardcopy.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        buttonshowhardcopy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ViewGroup.LayoutParams params = listhardcopy.getLayoutParams();
                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    listhardcopy.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                                }
                                Utility.setListViewHeightBasedOnChildren(listhardcopy);

                                listhardcopy.setLayoutParams(params);
                                buttonhidehardcopy.setVisibility(View.VISIBLE);
                                buttonshowhardcopy.setVisibility(View.INVISIBLE);
                                empty2.setVisibility(View.VISIBLE);
                            }
                        });
                        buttonhidehardcopy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ViewGroup.LayoutParams params = listhardcopy.getLayoutParams();
                                params.height = 0;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    listhardcopy.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                                }
                                listhardcopy.setLayoutParams(params);
                                buttonshowhardcopy.setVisibility(View.VISIBLE);
                                buttonhidehardcopy.setVisibility(View.INVISIBLE);
                                empty2.setVisibility(View.GONE);
                            }
                        });

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

    private void getalatkerja() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_serahterimaalatkerja?nik_baru=" + nikbaru.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final serahterimaalatkerjamodel movieItem = new serahterimaalatkerjamodel(
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("alat_kerja"),
                                        movieObject.getString("jumlah_alat_kerja"),
                                        movieObject.getString("satuan_alat_kerja"),
                                        movieObject.getString("kondisi_alat_kerja"),
                                        movieObject.getString("keterangan_alat_kerja"));

                                serahterimaalatkerjamodels.add(movieItem);

                                ListViewAdapterAlatkerja adapter = new ListViewAdapterAlatkerja(serahterimaalatkerjamodels, getApplicationContext());

                                listserahterima.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        buttonshowalatkerja.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ViewGroup.LayoutParams params = listserahterima.getLayoutParams();
                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    listserahterima.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                                }
                                listserahterima.setLayoutParams(params);
                                Utility.setListViewHeightBasedOnChildren(listserahterima);
                                buttonhidealatkerja.setVisibility(View.VISIBLE);
                                buttonshowalatkerja.setVisibility(View.INVISIBLE);
                                empty1.setVisibility(View.VISIBLE);

                            }
                        });
                        buttonhidealatkerja.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ViewGroup.LayoutParams params = listserahterima.getLayoutParams();
                                params.height = 0;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    listserahterima.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                                }
                                listserahterima.setLayoutParams(params);
                                buttonshowalatkerja.setVisibility(View.VISIBLE);
                                buttonhidealatkerja.setVisibility(View.INVISIBLE);
                                empty1.setVisibility(View.GONE);
                            }
                        });

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

    private void getapproval() {
        pDialog = new ProgressDialog(form_serahterima.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        String id = getIntent().getStringExtra(KEY_NIK);
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        final String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_penerima?id=" + id,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    JSONObject movieObject = movieArray.getJSONObject(i);
                                    if(movieObject.getString("nik_penerima_1").equals(nik_baru)) {
                                        approval1();
                                    } else if (movieObject.getString("nik_penerima_2").equals(nik_baru)) {
                                        approval2();
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

    private void approval2() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_approvalpenerima2",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                        form_serahterima.this.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "maaf ada kesalahan", Toast.LENGTH_LONG).show();
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
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                final String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = df.format(c);

                params.put("nik_penerima_2", nik_baru);
                if(diterima.isChecked()){
                    params.put("status_penerima_2", "1");
                    params.put("tanggal_penerima_2", formattedDate);
                } else if (tidakterima.isChecked()){
                    params.put("status_penerima_2", "1");
                    params.put("tanggal_penerima_2", formattedDate);
                }


                return params;
            }

        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(form_serahterima.this);
        requestQueue.add(stringRequest);
    }

    private void approval1() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_approvalpenerima1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                        form_serahterima.this.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "maaf ada kesalahan", Toast.LENGTH_LONG).show();
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
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                final String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = df.format(c);

                params.put("nik_penerima_1", nik_baru);
                if(diterima.isChecked()){
                    params.put("status_penerima_1", "1");
                    params.put("tanggal_penerima_1", formattedDate);
                } else if (tidakterima.isChecked()){
                    params.put("status_penerima_1", "1");
                    params.put("tanggal_penerima_1", formattedDate);
                }


                return params;
            }

        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(form_serahterima.this);
        requestQueue.add(stringRequest);
    }


    private void getNama2() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + nik_penerima2.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);

                                namapenerima_2.setText(movieObject.getString("nama_karyawan_struktur"));


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

    private void getNama1() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + nik_penerima1.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);

                                namapenerima_1.setText(movieObject.getString("nama_karyawan_struktur"));


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

    private void getBiodata() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + nikbaru.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);

                                namakaryawan.setText(movieObject.getString("nama_karyawan_struktur"));
                                jabatan.setText(movieObject.getString("jabatan_karyawan"));
                                lokasi.setText(movieObject.getString("lokasi_struktur"));

                                depart.setText(movieObject.getString("dept_struktur"));

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

    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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

    public static String date(String inputDate) {
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public static class ListViewAdapterAlatkerja extends ArrayAdapter<serahterimaalatkerjamodel> {

        List<serahterimaalatkerjamodel> movieItemList;

        private Context context;

        public ListViewAdapterAlatkerja(List<serahterimaalatkerjamodel> movieItemList, Context context) {
            super(context, R.layout.list_item_serahterima, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_serahterima, null, true);

            TextView alatkerja = listViewItem.findViewById(R.id.alatkerja);
            TextView jumlahalatkerja = listViewItem.findViewById(R.id.jumlahalatkerja);
            TextView satuanalatkerja = listViewItem.findViewById(R.id.satuanalatkerja);
            TextView kondisialatkerja = listViewItem.findViewById(R.id.kondisialatkerja);
            TextView keteranganalatkerja = listViewItem.findViewById(R.id.keteranganalatkerja);


            serahterimaalatkerjamodel movieItem = getItem(position);

            alatkerja.setText(movieItem.getAlat_kerja());
            jumlahalatkerja.setText(movieItem.getJumlah_alat_kerja());
            satuanalatkerja.setText(movieItem.getSatuan_alat_kerja());
            kondisialatkerja.setText(movieItem.getKondisi_alat_kerja());
            keteranganalatkerja.setText(movieItem.getKeterangan_alat_kerja());

            return listViewItem;
        }
    }

    public static class ListViewAdapterHardcopy extends ArrayAdapter<serahterimahardcopymodel> {

        List<serahterimahardcopymodel> movieItemList;

        private Context context;

        public ListViewAdapterHardcopy(List<serahterimahardcopymodel> movieItemList, Context context) {
            super(context, R.layout.list_items_hardcopy, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_items_hardcopy, null, true);

            TextView namahardcopy = listViewItem.findViewById(R.id.namahardcopy);
            TextView jumlahhardcopy = listViewItem.findViewById(R.id.jumlahhardcopy);
            TextView satuan_hardcopy = listViewItem.findViewById(R.id.satuan_hardcopy);
            TextView tempathardcopy = listViewItem.findViewById(R.id.tempathardcopy);
            TextView keteranganhardcopy = listViewItem.findViewById(R.id.keteranganhardcopy);


            serahterimahardcopymodel movieItem = getItem(position);

            namahardcopy.setText(movieItem.getNama_hardcopy());
            jumlahhardcopy.setText(movieItem.getJumlah_hardcopy());
            satuan_hardcopy.setText(movieItem.getSatuan_hardcopy());
            tempathardcopy.setText(movieItem.getTempat_hardcopy());
            keteranganhardcopy.setText(movieItem.getKeterangan_hardcopy());

            return listViewItem;
        }
    }

    public static class ListViewAdapterSoftcopy extends ArrayAdapter<serahterimasoftcopymodel> {

        List<serahterimasoftcopymodel> movieItemList;

        private Context context;

        public ListViewAdapterSoftcopy(List<serahterimasoftcopymodel> movieItemList, Context context) {
            super(context, R.layout.list_items_hardcopy, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_items_softcopy, null, true);

            TextView namasoftcopy = listViewItem.findViewById(R.id.namasoftcopy);
            TextView no_software = listViewItem.findViewById(R.id.no_software);
            TextView jenis_software = listViewItem.findViewById(R.id.jenis_software);
            TextView keterangan_software = listViewItem.findViewById(R.id.keterangan_software);


            serahterimasoftcopymodel movieItem = getItem(position);

            namasoftcopy.setText(movieItem.getNama_softcopy());
            no_software.setText(movieItem.getNo_software());
            jenis_software.setText(movieItem.getJenis_software());
            keterangan_software.setText(movieItem.getKeterangan_software());

            return listViewItem;
        }
    }

    public static class ListViewAdapterProjects extends ArrayAdapter<serahterimaprojectmodel> {

        List<serahterimaprojectmodel> movieItemList;

        private Context context;

        public ListViewAdapterProjects(List<serahterimaprojectmodel> movieItemList, Context context) {
            super(context, R.layout.list_items_project, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_items_project, null, true);

            TextView namaproject = listViewItem.findViewById(R.id.namaproject);
            TextView sdm_project = listViewItem.findViewById(R.id.sdm_project);
            TextView hasil_project = listViewItem.findViewById(R.id.hasil_project);
            TextView outstanding_project = listViewItem.findViewById(R.id.outstanding_project);
            TextView deadline_project = listViewItem.findViewById(R.id.deadline_project);

            serahterimaprojectmodel movieItem = getItem(position);

            namaproject.setText(movieItem.getNama_project());
            sdm_project.setText(movieItem.getSdm_project());
            hasil_project.setText(movieItem.getHasil_project());
            outstanding_project.setText(movieItem.getOutstanding_project());
            deadline_project.setText(date(movieItem.getDeadline_project()));
            return listViewItem;
        }
    }

    public static class ListViewAdapterSdm extends ArrayAdapter<serahterimasdmmodel> {

        List<serahterimasdmmodel> movieItemList;

        private Context context;

        public ListViewAdapterSdm(List<serahterimasdmmodel> movieItemList, Context context) {
            super(context, R.layout.list_items_sdm, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_items_sdm, null, true);

            TextView jabatansdm = listViewItem.findViewById(R.id.jabatansdm);
            TextView jumlah_sdm = listViewItem.findViewById(R.id.jumlah_sdm);
            TextView jenis_kelamin_sdm = listViewItem.findViewById(R.id.jenis_kelamin_sdm);
            TextView promosi_sdm = listViewItem.findViewById(R.id.promosi_sdm);
            TextView mutasi_sdm = listViewItem.findViewById(R.id.mutasi_sdm);
            TextView demosi_sdm = listViewItem.findViewById(R.id.demosi_sdm);
            TextView sp1_sdm = listViewItem.findViewById(R.id.sp1_sdm);
            TextView sp2_sdm = listViewItem.findViewById(R.id.sp2_sdm);
            TextView sp3_sdm = listViewItem.findViewById(R.id.sp3_sdm);
            TextView keterangan_sdm = listViewItem.findViewById(R.id.keterangan_sdm);

            serahterimasdmmodel movieItem = getItem(position);

            jabatansdm.setText(movieItem.getJabatan_sdm());
            jumlah_sdm.setText(movieItem.getJumlah_sdm());
            jenis_kelamin_sdm.setText(movieItem.getJenis_kelamin_sdm());
            promosi_sdm.setText(movieItem.getPromosi_sdm());
            mutasi_sdm.setText(movieItem.getMutasi_sdm());
            demosi_sdm.setText(movieItem.getDemosi_sdm());

            sp1_sdm.setText(movieItem.getSp1_sdm());
            sp2_sdm.setText(movieItem.getSp2_sdm());
            sp3_sdm.setText(movieItem.getSp3_sdm());

            keterangan_sdm.setText(movieItem.getKeterangan_sdm());



            return listViewItem;
        }
    }
}