package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.google.android.material.navigation.NavigationView;
import com.example.eis2.SearchSpinner.SearchableSpinner;

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
import java.util.concurrent.atomic.AtomicLong;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu_interview.dept3;
import static com.example.eis2.mutasi.employee;

public class formpengajuan extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    DrawerLayout dLayout;
    SearchableSpinner jabatan, lokasi, departement, analisa, penyediaan;
    ArrayList<String> jabatans = new ArrayList<>();
    ArrayList<String> lokasis = new ArrayList<>();
    ArrayList<String> depart = new ArrayList<>();
    ProgressDialog pDialog;

    ImageButton pengajuan;
    EditText nopengajuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formpengajuan);
        HttpsTrustManager.allowAllSSL();

        jabatan = (SearchableSpinner) findViewById(R.id.jabatan);
        lokasi = (SearchableSpinner) findViewById(R.id.lokasi);
        departement = (SearchableSpinner) findViewById(R.id.departement);
        analisa = (SearchableSpinner) findViewById(R.id.analisa);
        penyediaan = (SearchableSpinner) findViewById(R.id.penyediaan);
        nopengajuan = (EditText) findViewById(R.id.nopengajuan);
        pengajuan = (ImageButton) findViewById(R.id.pengajuan);

        AtomicLong mLastClickTime = new AtomicLong();

        penyediaan.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                penyediaan.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    penyediaan.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            penyediaan.onTouch(v,event);
            penyediaan.setEnabled(false);

            return true;
        });

        analisa.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                analisa.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    analisa.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            analisa.onTouch(v,event);
            analisa.setEnabled(false);

            return true;
        });

        departement.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                departement.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    departement.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            departement.onTouch(v,event);
            departement.setEnabled(false);

            return true;
        });

        lokasi.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                lokasi.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lokasi.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            lokasi.onTouch(v,event);
            lokasi.setEnabled(false);

            return true;
        });

        jabatan.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                jabatan.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    jabatan.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            jabatan.onTouch(v,event);
            jabatan.setEnabled(false);

            return true;
        });

        pengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastNumber();
            }
        });

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        StringRequest biodata = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);


                                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/jabatan/index_departement?dept_jabatan_karyawan=" + movieObject.getString("dept_jabatan_karyawan"), new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        jabatans.add("PILIH JABATAN");
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getString("status").equals("true")) {
                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                    String id = jsonObject1.getString("no_jabatan_karyawan");
                                                    String jabatan = jsonObject1.getString("jabatan_karyawan");
                                                    jabatans.add(id + " (" + jabatan + ") ");

                                                }
                                            }
                                            jabatan.setTitle("Pilih karyawan");
                                            jabatan.setAdapter(new ArrayAdapter<String>(formpengajuan.this, android.R.layout.simple_spinner_dropdown_item, jabatans));
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
                                        ));
                                RequestQueue requestQueue = Volley.newRequestQueue(formpengajuan.this);
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
        biodata.setRetryPolicy(
                new DefaultRetryPolicy(
                        7200000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueuebiodata = Volley.newRequestQueue(this);
        requestQueuebiodata.add(biodata);




        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/lokasi/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        lokasis.add("PILIH LOKASI");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String depo = null;
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    depo = jsonObject1.getString("depo_nama");
                                    lokasis.add(depo);

                                }
                            }
                            lokasi.setTitle("Pilih Lokasi");
                            lokasi.setAdapter(new ArrayAdapter<String>(formpengajuan.this, android.R.layout.simple_spinner_dropdown_item, lokasis));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Maaf ada kesalahan", Toast.LENGTH_SHORT).show();
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
            };
        };
        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest2);

        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/departement/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        depart.add("PILIH DEPARTEMENT");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String depo = null;
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    depo = jsonObject1.getString("nama_departement");
                                    depart.add(depo);

                                }
                            }
                            departement.setTitle("Pilih Lokasi");
                            departement.setAdapter(new ArrayAdapter<String>(formpengajuan.this, android.R.layout.simple_spinner_dropdown_item, depart));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Maaf ada kesalahan", Toast.LENGTH_SHORT).show();
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
            };
        };
        stringRequest3.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue3 = Volley.newRequestQueue(this);
        requestQueue3.add(stringRequest3);


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
                            formpengajuan.this);
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
                                    Intent intent = new Intent(formpengajuan.this, MainActivity.class);
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

    private void getLastNumber() {
        pDialog = new ProgressDialog(this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Ptk/index_nomor", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject finalObject3 = jsonArray.getJSONObject(jsonArray.length() -1);
                            int a =finalObject3.getInt("no_pengajuan");
                            String padded = String.format("%04d" , a + 1);

                            nopengajuan.setText(padded);
                            post();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                hideDialog();
                Toast.makeText(getApplicationContext(), "Maaf ada kesalahan", Toast.LENGTH_SHORT).show();
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

    private void post() {
        pDialog = new ProgressDialog(this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/Ptk/index",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                        formpengajuan.this.finish();
                    }
                }, new Response.ErrorListener() {
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
            };

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString(KEY_NIK ,null);

                String nama = jabatan.getSelectedItem().toString();
                String[] splited_text2 = nama.split(" \\(");
                nama = splited_text2[0];
                nama = nama.replace("(", "");
                
                params.put("no_pengajuan", nopengajuan.getText().toString());
                params.put("nik_pengajuan", nik_baru);
                params.put("jabatan_karyawan", text_jabatan.getText().toString());

                params.put("jabatan_ptk", nama);
                params.put("depo_ptk", lokasi.getSelectedItem().toString());
                params.put("dept_ptk", departement.getSelectedItem().toString());

                params.put("analisa", analisa.getSelectedItem().toString());
                params.put("tenaga_kerja", penyediaan.getSelectedItem().toString());



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

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

        
        super.onDestroy();
    }
}