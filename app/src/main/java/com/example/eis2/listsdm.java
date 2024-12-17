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
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.example.eis2.Item.hardcopymodel;
import com.example.eis2.Item.sdmmodel;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;

public class listsdm extends AppCompatActivity {
    public static Datasdm adapter;
    Button tambah, simpan;
    static ListView list;
    static ArrayList<sdmmodel> sdmmodels;
    ProgressDialog pDialog;

    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listsdm);
        HttpsTrustManager.allowAllSSL();

        tambah = findViewById(R.id.tambah);
        simpan = findViewById(R.id.simpan);
        list = findViewById(R.id.list);

        sdmmodels = new ArrayList<>();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(listsdm.this, formsdm.class);
                startActivity(i);
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postalatkerja();
            }
        });

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
                            listsdm.this);
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
                                    Intent intent = new Intent(listsdm.this, MainActivity.class);
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

    private void postalatkerja() {
        pDialog = new ProgressDialog(listsdm.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
            }
        };
        handler.postDelayed(r, 200);

        final int x = sdmmodels.size() - 1;
        for (int i = 0; i <= x; i++) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final int finalI = i;


            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_sdm",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (finalI == x) {
                                update();
                            }
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
                    String nik_baru = sharedPreferences.getString(KEY_NIK, null);

                    params.put("nik_baru", nik_baru);
                    params.put("jabatan_sdm", adapter.getItem(finalI).getJabatan());
                    params.put("jumlah_sdm", adapter.getItem(finalI).getJumlah());

                    params.put("jenis_kelamin_sdm", adapter.getItem(finalI).getJeniskelamin());
                    params.put("promosi_sdm", adapter.getItem(finalI).getPromosi());
                    params.put("mutasi_sdm", adapter.getItem(finalI).getMutasi());
                    params.put("demosi_sdm", adapter.getItem(finalI).getDemosi());

                    params.put("sp1_sdm", adapter.getItem(finalI).getSuratperingatan1());
                    params.put("sp2_sdm", adapter.getItem(finalI).getSuratperingatan2());
                    params.put("sp3_sdm", adapter.getItem(finalI).getSuratperingatan3());

                    params.put("keterangan_sdm", adapter.getItem(finalI).getKeterangan());

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
            RequestQueue requestQueue = Volley.newRequestQueue(listsdm.this);
            requestQueue.add(stringRequest);
        }
    }

    private void update() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_statussdm",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                        listsdm.this.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
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
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                params.put("nik", nik_baru);


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

    public static class Datasdm extends ArrayAdapter<sdmmodel> {

        private class ViewHolder {
            TextView jabatan, jumlah, kelamin, promosi, mutasi, demosi,
                    suratperingatan1, suratperingatan2, suratperingatan3, keterangan;
            Button hapus;


        }
        List<sdmmodel> dataModels2;
        private Context context;

        public Datasdm(List<sdmmodel> dataModels2, Context context) {
            super(context, R.layout.list_item_sdm, dataModels2);
            this.dataModels2 = dataModels2;
            this.context = context;

        }

        public int getCount() {
            return dataModels2.size();
        }

        public sdmmodel getItem(int position) {
            return dataModels2.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final sdmmodel movieItem = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_sdm, parent, false);

                viewHolder.jabatan = (TextView) convertView.findViewById(R.id.jabatan);
                viewHolder.jumlah = (TextView) convertView.findViewById(R.id.jumlah);
                viewHolder.kelamin = (TextView) convertView.findViewById(R.id.kelamin);
                viewHolder.promosi = (TextView) convertView.findViewById(R.id.promosi);
                viewHolder.mutasi = (TextView) convertView.findViewById(R.id.mutasi);

                viewHolder.demosi = (TextView) convertView.findViewById(R.id.demosi);
                viewHolder.suratperingatan1 = (TextView) convertView.findViewById(R.id.suratperingatan1);
                viewHolder.suratperingatan2 = (TextView) convertView.findViewById(R.id.suratperingatan2);
                viewHolder.suratperingatan3 = (TextView) convertView.findViewById(R.id.suratperingatan3);
                viewHolder.keterangan = (TextView) convertView.findViewById(R.id.keterangan);

                viewHolder.hapus = (Button) convertView.findViewById(R.id.hapus);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.jabatan.setText(movieItem.getJabatan());
            viewHolder.jumlah.setText(movieItem.getJumlah());
            viewHolder.kelamin.setText(movieItem.getJeniskelamin());
            viewHolder.promosi.setText(movieItem.getPromosi());
            viewHolder.mutasi.setText(movieItem.getMutasi());

            viewHolder.demosi.setText(movieItem.getDemosi());
            viewHolder.suratperingatan1.setText(movieItem.getSuratperingatan1());
            viewHolder.suratperingatan2.setText(movieItem.getSuratperingatan2());
            viewHolder.suratperingatan3.setText(movieItem.getSuratperingatan3());
            viewHolder.keterangan.setText(movieItem.getKeterangan());


            viewHolder.hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataModels2.remove(position);
                    adapter = new Datasdm(dataModels2, context);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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