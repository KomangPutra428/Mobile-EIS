package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.example.eis2.Item.Utility;
import com.example.eis2.Item.approvalcutitahunan;
import com.example.eis2.Item.soalmodel;
import com.google.android.material.navigation.NavigationView;
import com.example.eis2.SearchSpinner.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class list_soal extends AppCompatActivity {
    TextView keterangan;
    SharedPreferences sharedPreferences;
    DrawerLayout dLayout;
    ListView list;
    List<soalmodel> soalmodelList;
    ListViewAdapterSoal adapter;
    ImageButton simpan, simpankritik;
    ProgressDialog pDialog;
    LinearLayout kritikan;
    RadioButton opsi1, opsi2, opsi3, opsi4, opsi5, opsi6, opsi7, opsi8, opsi9, opsi10;
    Spinner jawaban;
    EditText alasanresign, saranperusahaan;
    RadioGroup pilihan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_soal);
        HttpsTrustManager.allowAllSSL();
        keterangan = findViewById(R.id.keterangan);
        simpan = findViewById(R.id.simpan);
        kritikan = findViewById(R.id.kritikan);
        simpankritik = findViewById(R.id.simpankritik);
        jawaban = findViewById(R.id.jawaban);
        alasanresign = findViewById(R.id.alasanresign);
        saranperusahaan = findViewById(R.id.saranperusahaan);
        pilihan = findViewById(R.id.pilihan);

        opsi1 = findViewById(R.id.opsi1);
        opsi2 = findViewById(R.id.opsi2);
        opsi3 = findViewById(R.id.opsi3);
        opsi4 = findViewById(R.id.opsi4);
        opsi5 = findViewById(R.id.opsi5);
        opsi6 = findViewById(R.id.opsi6);
        opsi7 = findViewById(R.id.opsi7);
        opsi8 = findViewById(R.id.opsi8);
        opsi9 = findViewById(R.id.opsi9);
        opsi10 = findViewById(R.id.opsi10);

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
        list = findViewById(R.id.list);

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
                            list_soal.this);
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
                                    Intent intent = new Intent(list_soal.this, MainActivity.class);
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

        String judul = getIntent().getStringExtra("title");
        if(getIntent().getStringExtra("title").equals(null)) {
            keterangan.setText("KRITIK DAN SARAN");
        }else if(getIntent().getStringExtra("title").equals("KEPEMIMPINAN DAN PERENCANAAN")){
            keterangan.setText("KEPEMIMPINAN DAN\nPERENCANAAN");
            getData();
        } else if(getIntent().getStringExtra("title").equals("PENGAKUAN DAN PENGHARGAAN")){
            keterangan.setText("PENGAKUAN DAN\nPENGHARGAAN");
            getData();
        } else {
            keterangan.setText(judul);
            getData();
        }

        if(keterangan.getText().toString().equalsIgnoreCase("KRITIK DAN SARAN")){
            kritikan.setVisibility(View.VISIBLE);
        } else {
            simpan.setVisibility(View.VISIBLE);
            list.setVisibility(View.VISIBLE);
            kritikan.setVisibility(View.GONE);

        }
        soalmodelList = new ArrayList<>();

        simpankritik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jawaban.getSelectedItem().toString().equals("-- Pilih Jawaban --")){
                    Toast.makeText(getApplicationContext(), "Silahkan Pilih Jawaban", Toast.LENGTH_LONG).show();
                } else if (pilihan.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Silahkan Pilih Salah Satu Alasan", Toast.LENGTH_LONG).show();
                } else if (alasanresign.getText().toString().length() == 0){
                    alasanresign.setError("Silahkan isi keterangan alasan resign");
                } else if (saranperusahaan.getText().toString().length() == 0){
                    saranperusahaan.setError("Silahkan isi saran perusahaan");
                } else {
                    pDialog = new ProgressDialog(list_soal.this);
                    showDialog();
                    pDialog.setContentView(R.layout.progress_dialog);
                    pDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    postkritik();
                }

            }
        });


    }

    private void postkritik() {
        final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_kritiksaran",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            hideDialog();
                            Toast.makeText(getApplicationContext(), "Data sudah dimasukkan", Toast.LENGTH_LONG).show();
                            finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Maaf Ada Kesalahan", Toast.LENGTH_LONG).show();
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

                params.put("nik_baru", nik_baru);
                if(jawaban.getSelectedItem().toString().equals("Sangat Buruk")){
                    params.put("nilai_keseluruhan", "1");
                } else if(jawaban.getSelectedItem().toString().equals("Buruk")){
                    params.put("nilai_keseluruhan", "2");
                } else if(jawaban.getSelectedItem().toString().equals("Sedang")){
                    params.put("nilai_keseluruhan", "3");
                } else if(jawaban.getSelectedItem().toString().equals("Baik")){
                    params.put("nilai_keseluruhan", "4");
                } else if(jawaban.getSelectedItem().toString().equals("Sangat Baik")){
                    params.put("nilai_keseluruhan", "5");
                }

                if(opsi1.isChecked()){
                    params.put("kategori_resign", "Tidak mampu menjalankan tugas");
                } else if(opsi2.isChecked()){
                    params.put("kategori_resign", "Mendapat pekerjaan baru");
                } else if(opsi3.isChecked()){
                    params.put("kategori_resign", "Gaji tidak sesuai");
                } else if(opsi4.isChecked()){
                    params.put("kategori_resign", "Kesehatan");
                } else if(opsi5.isChecked()){
                    params.put("kategori_resign", "Melanjutkan Studi");
                } else if(opsi6.isChecked()){
                    params.put("kategori_resign", "Tidak cocok dengan budaya perusahaan");
                } else if(opsi7.isChecked()){
                    params.put("kategori_resign", "Wiraswasta");
                } else if(opsi8.isChecked()){
                    params.put("kategori_resign", "Perhitungan incentive tidak sesuai");
                } else if(opsi9.isChecked()){
                    params.put("kategori_resign", "Masalah Keluarga");
                } else if(opsi10.isChecked()){
                    params.put("kategori_resign", "Tidak Cocok Dengan Atasan");
                }

                params.put("alasan_resign", alasanresign.getText().toString());
                params.put("saran_masukan", saranperusahaan.getText().toString());



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

        RequestQueue requestQueue2 = Volley.newRequestQueue(list_soal.this);
        requestQueue2.add(stringRequest2);
    }

    private void post() {
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
            }
        };
        handler.postDelayed(r, 200);

        final int x = soalmodelList.size() - 1;
        for (int i = 0; i <= x; i++) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final int finalI = i;
            final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_kuisioner",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (finalI == x) {
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "Data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Maaf Ada Kesalahan", Toast.LENGTH_LONG).show();
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

                    params.put("nik_baru", nik_baru);
                    params.put("id_soal", adapter.getItem(finalI).getId());
                    if(adapter.getItem(finalI).getKondisi().equals("Sangat Buruk")){
                        params.put("jawaban", "1");
                    } else if(adapter.getItem(finalI).getKondisi().equals("Buruk")){
                        params.put("jawaban", "2");
                    } else if(adapter.getItem(finalI).getKondisi().equals("Sedang")){
                        params.put("jawaban", "3");
                    } else if(adapter.getItem(finalI).getKondisi().equals("Baik")){
                        params.put("jawaban", "4");
                    } else if(adapter.getItem(finalI).getKondisi().equals("Sangat Baik")){
                        params.put("jawaban", "5");
                    }


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

            RequestQueue requestQueue2 = Volley.newRequestQueue(list_soal.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_soalkuisioner?type_soal=" + getIntent().getStringExtra("id"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final soalmodel movieItem = new soalmodel(
                                        movieObject.getString("id"),
                                        movieObject.getString("soal"));

                                soalmodelList.add(movieItem);
                                adapter = new ListViewAdapterSoal(soalmodelList, getApplicationContext());
                                list.setAdapter(adapter);
                                Utility.setListViewHeightBasedOnChildren(list);
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

    public class ListViewAdapterSoal extends ArrayAdapter<soalmodel> {

        private class ViewHolder {
            TextView keterangan;
            Spinner jawaban;
        }
        List<soalmodel> dataModels;
        private Context context;

        public ListViewAdapterSoal(List<soalmodel> dataModels, Context context) {
            super(context, R.layout.list_item_soal, dataModels);
            this.dataModels = dataModels;
            this.context = context;

        }


        public int getCount() {
            return dataModels.size();
        }

        public soalmodel getItem(int position) {
            return dataModels.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            soalmodel movieItem = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_soal, parent, false);

                viewHolder.keterangan = (TextView) convertView.findViewById(R.id.keterangan);
                viewHolder.jawaban = (Spinner) convertView.findViewById(R.id.jawaban);

                simpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (int i = 0; i < dataModels.size(); i++) {

                            if(adapter.getItem(i).getKondisi().equals("-- Pilih Jawaban --")){
                                Toast.makeText(getApplicationContext(), "Silahkan pilih jawaban terlebih dahulu", Toast.LENGTH_SHORT).show();
                                break;
                            } else {
                                System.out.println("Hasil Looping = " + i);
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if(dataModels.size() -1 == i) {
                                    pDialog = new ProgressDialog(list_soal.this);
                                    showDialog();
                                    pDialog.setContentView(R.layout.progress_dialog);
                                    pDialog.getWindow().setBackgroundDrawableResource(
                                            android.R.color.transparent
                                    );
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            post();
                                        }
                                    }, 2000);
                                }
                            }
                        }
//                        if(viewHolder.keterangan.getText().toString().length() != 0){
//                            if(viewHolder.jawaban.getSelectedItem().toString().equals("-- Pilih Jawaban --")){
//                            } else {
//                            }
//                        }

                    }
                });
                convertView.setTag(viewHolder);


            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }


            viewHolder.keterangan.setText(position + 1 +". "+movieItem.getSoal());



            viewHolder.jawaban.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    getItem(position).setKondisi(viewHolder.jawaban.getSelectedItem().toString());

//                    System.out.println("Hasil spinner = "+ viewHolder.jawaban.getAdapter().getCount());
//
//                    System.out.println("pilih = "+ viewHolder.jawaban.getSelectedItemPosition());






                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    getItem(position).setKondisi(viewHolder.jawaban.getSelectedItem().toString());

//                    System.out.println("ngga dipilih");
                }
            });


            return convertView;

        }
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