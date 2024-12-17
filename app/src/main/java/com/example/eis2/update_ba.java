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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.eis2.Item.refundmodel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class update_ba extends AppCompatActivity {
    EditText no_ba, tanggal, dibuat;
    ListView list;
    ProgressDialog pDialog;
    List<refundmodel> refundmodelList;
    ImageButton pengajuan;
    SharedPreferences sharedPreferences;
    DrawerLayout dLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ba);
        HttpsTrustManager.allowAllSSL();

        no_ba = findViewById(R.id.no_ba);
        dibuat = findViewById(R.id.dibuat);
        tanggal = findViewById(R.id.tanggal);
        list = findViewById(R.id.list);
        pengajuan = findViewById(R.id.pengajuan);
        refundmodelList = new ArrayList<>();
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
                            update_ba.this);
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
                                    Intent intent = new Intent(update_ba.this, MainActivity.class);
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

        pengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(update_ba.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/refund/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                                refreshActivity();
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
                        String id = getIntent().getStringExtra(KEY_NIK);

                        params.put("id", id);
                        params.put("status_ba", "1");
                        params.put("no_ba", no_ba.getText().toString());
                        params.put("tanggal_ba", date(tanggal.getText().toString()));


                        return params;
                    }

                };
                stringRequest.setRetryPolicy(
                        new DefaultRetryPolicy(
                                500000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        ));
                RequestQueue requestQueue = Volley.newRequestQueue(update_ba.this);
                requestQueue.add(stringRequest);
            }
        });
        getData();
        getName();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/lokasi/index_kode?namadepo=" + txt_lokasi.getText().toString(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    JSONObject movieObject = movieArray.getJSONObject(i);
                                    String kodedepo = movieObject.getString("depo_kode");


                                    String id = getIntent().getStringExtra(KEY_NIK);


                                    Date tanggal = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("MM", Locale.getDefault());
                                    int formattedDate = Integer.parseInt(df.format(tanggal));

                                    Date tahun = Calendar.getInstance().getTime();
                                    SimpleDateFormat df2 = new SimpleDateFormat("yyyy", Locale.getDefault());
                                    String formattedDate2 = df2.format(tahun);

                                    no_ba.setText(id+"/"+kodedepo+ "/" +getRomanNumeral(formattedDate)+"/"+formattedDate2);

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

    private void getName() {
        String id = getIntent().getStringExtra(KEY_NIK);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/refund/index_refundid?no_pengajuan=" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(0);
                                String tanggal2 = movieObject.getString("submit_date");
                                String nik = movieObject.getString("nik_pengajuan");
                                tanggal.setText(convertFormat(tanggal2));

                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + nik,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject obj = new JSONObject(response);
                                                        JSONArray movieArray = obj.getJSONArray("data");
                                                        for (int i = 0; i < movieArray.length(); i++) {
                                                            JSONObject movieObject = movieArray.getJSONObject(i);

                                                            dibuat.setText(movieObject.getString("nama_karyawan_struktur"));

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
                                    RequestQueue requestQueue = Volley.newRequestQueue(update_ba.this);
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

    private void getData() {
        pDialog = new ProgressDialog(update_ba.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        String id = getIntent().getStringExtra(KEY_NIK);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/refund/index_refundid?no_pengajuan=" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final refundmodel movieItem = new refundmodel(
                                        movieObject.getString("submit_date"),
                                        movieObject.getString("no_pengajuan"),
                                        movieObject.getString("nik"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("tanggal_absen"),
                                        movieObject.getString("absen_awal"),
                                        movieObject.getString("absen_akhir"),
                                        movieObject.getString("ket"),
                                        movieObject.getString("status_refund"),
                                        movieObject.getString("status_ba"),
                                        movieObject.getString("status_pengajuan"),
                                        movieObject.getString("ket_pengajuan"));


                                refundmodelList.add(movieItem);

                                hideDialog();

                                ListViewAdapterRefund adapter = new ListViewAdapterRefund(refundmodelList, getApplicationContext());

                                list.setAdapter(adapter);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Belum ada Pengajuan", Toast.LENGTH_SHORT).show();
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

    public class ListViewAdapterRefund extends ArrayAdapter<refundmodel> {

        List<refundmodel> movieItemList;

        private Context context;

        public ListViewAdapterRefund(List<refundmodel> movieItemList, Context context) {
            super(context, R.layout.list_item_refundlist, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_refundlist, null, true);

            TextView tanggalid = listViewItem.findViewById(R.id.tanggalid);
            TextView id = listViewItem.findViewById(R.id.id);
            final TextView nik = listViewItem.findViewById(R.id.nik);

            final TextView nama = listViewItem.findViewById(R.id.nama);
            final TextView jabatan = listViewItem.findViewById(R.id.jabatan);
            TextView tglabsen = listViewItem.findViewById(R.id.tglabsen);

            TextView absenawal = listViewItem.findViewById(R.id.absenawal);
            TextView absenakhir = listViewItem.findViewById(R.id.absenakhir);
            TextView keterangan = listViewItem.findViewById(R.id.keterangan);
            TextView textketerangan = listViewItem.findViewById(R.id.textketerangan);
            TextView keteranganpengajuantext = listViewItem.findViewById(R.id.keteranganpengajuantext);
            TextView keteranganpengajuan = listViewItem.findViewById(R.id.keteranganpengajuan);

            TextView status1 = listViewItem.findViewById(R.id.status1);
            TextView status2 = listViewItem.findViewById(R.id.status2);
            TextView status3 = listViewItem.findViewById(R.id.status3);

            TextView titikdua8 = listViewItem.findViewById(R.id.titikdua8);
            TextView titikdua9 = listViewItem.findViewById(R.id.titikdua9);
            TextView titikdua10 = listViewItem.findViewById(R.id.titikdua10);
            TextView titikdua11 = listViewItem.findViewById(R.id.titikdua11);
            TextView titikdua30 = listViewItem.findViewById(R.id.titikdua30);

            ImageView statusrefund = listViewItem.findViewById(R.id.statusrefund);
            ImageView statusba = listViewItem.findViewById(R.id.statusba);
            ImageView statusbpengajuan = listViewItem.findViewById(R.id.statusbpengajuan);

            keterangan.setVisibility(View.GONE);
            statusrefund.setVisibility(View.GONE);
            statusba.setVisibility(View.GONE);
            statusbpengajuan.setVisibility(View.GONE);
            keteranganpengajuantext.setVisibility(View.GONE);
                    keteranganpengajuan.setVisibility(View.GONE);
            textketerangan.setVisibility(View.GONE);
            status1.setVisibility(View.GONE);
            status2.setVisibility(View.GONE);
            status3.setVisibility(View.GONE);

            titikdua8.setVisibility(View.GONE);
            titikdua9.setVisibility(View.GONE);
            titikdua10.setVisibility(View.GONE);
            titikdua11.setVisibility(View.GONE);
            titikdua30.setVisibility(View.GONE);

            refundmodel movieItem = getItem(position);

            tanggalid.setText((convertFormat(movieItem.getSubmit_date())));
            id.setText(movieItem.getNo_pengajuan());
            nik.setText(movieItem.getNik());

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + movieItem.getNik(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    nama.setText(movieObject.getString("nama_karyawan_struktur"));
                                    jabatan.setText(movieObject.getString("jabatan_karyawan"));


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
            RequestQueue requestQueue = Volley.newRequestQueue(update_ba.this);
            requestQueue.add(stringRequest);

            tglabsen.setText(tanggal(movieItem.getTanggal_absen()));

            absenawal.setText(movieItem.getAbsen_awal());
            absenakhir.setText(movieItem.getAbsen_akhir());
            keterangan.setText(movieItem.getKet());

            keteranganpengajuan.setText(movieItem.getKet_pengajuan());




            if (movieItem.getStatus_refund().equals("0")) {
                statusrefund.setImageResource(R.drawable.btn_open);
            } else if (movieItem.getStatus_refund().equals("1")) {
                statusrefund.setImageResource(R.drawable.btn_aprv);
            } else if (movieItem.getStatus_refund().equals("2")) {
                statusrefund.setImageResource(R.drawable.btn_notaprv);
            } else if (movieItem.getStatus_refund().equals("3")) {
                statusrefund.setImageResource(R.drawable.btn_hangus);
            }

            if (movieItem.getStatus_ba().equals("0")) {
                statusba.setImageResource(R.drawable.btn_open);
            } else if (movieItem.getStatus_ba().equals("1")) {
                statusba.setImageResource(R.drawable.btn_aprv);
            } else if (movieItem.getStatus_ba().equals("2")) {
                statusba.setImageResource(R.drawable.btn_notaprv);
            } else if (movieItem.getStatus_ba().equals("3")) {
                statusba.setImageResource(R.drawable.btn_hangus);
            }

            if (movieItem.getStatus_pengajuan().equals("0")) {
                statusbpengajuan.setImageResource(R.drawable.btn_open);
            } else if (movieItem.getStatus_pengajuan().equals("1")) {
                statusbpengajuan.setImageResource(R.drawable.btn_aprv);
            } else if (movieItem.getStatus_pengajuan().equals("2")) {
                statusbpengajuan.setImageResource(R.drawable.btn_notaprv);
            } else if (movieItem.getStatus_pengajuan().equals("3")) {
                statusbpengajuan.setImageResource(R.drawable.btn_hangus);
            }

            return listViewItem;
        }
    }

    public static String getRomanNumeral(int nomor) {

        if (nomor > 0 && nomor < 4000) {

            final LinkedHashMap<Integer, String> numberLimits =
                    new LinkedHashMap<>();

            numberLimits.put(1, "I");
            numberLimits.put(2, "II");
            numberLimits.put(3, "III");
            numberLimits.put(4, "IV");
            numberLimits.put(5, "V");
            numberLimits.put(6, "VI");
            numberLimits.put(7, "VII");
            numberLimits.put(8, "VIII");
            numberLimits.put(9, "IX");
            numberLimits.put(10, "X");
            numberLimits.put(11, "XI");
            numberLimits.put(12, "XII");


            String romanNumeral = "";

            while (nomor > 0) {
                int highestFound = 0;
                for (Map.Entry<Integer, String> current : numberLimits.entrySet()){
                    if (current.getKey() <= nomor) {
                        highestFound = current.getKey();
                    }
                }
                romanNumeral += numberLimits.get(highestFound);
                nomor -= highestFound;
            }

            return romanNumeral;

        } else {
            throw new UnsupportedOperationException(nomor
                    + " is not a valid Roman numeral.");
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

    public static String tanggal(String inputDate) {
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
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
    public void refreshActivity() {
        Intent i = new Intent(this, list_ba.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

        
        super.onDestroy();
    }
}