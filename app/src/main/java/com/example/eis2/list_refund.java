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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
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
import com.example.eis2.Item.resignmodel;
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
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;

public class list_refund extends AppCompatActivity {
    ListView list;
    List<refundmodel> refundmodelList;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    DrawerLayout dLayout;
    SearchView simpleSearchView;
    ImageButton cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_refund);
        HttpsTrustManager.allowAllSSL();

        list = findViewById(R.id.list);
        cari = findViewById(R.id.cari);
        refundmodelList = new ArrayList<>();
        simpleSearchView = findViewById(R.id.simpleSearchView);
        cari.setVisibility(View.GONE);
        getResign();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        list = findViewById(R.id.list);

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
                            list_refund.this);
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
                                    Intent intent = new Intent(list_refund.this, MainActivity.class);
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

    private void getResign() {
        pDialog = new ProgressDialog(list_refund.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/refund/index_nikatasan?nik_pengajuan=" + nik_baru,
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

                                final ListViewAdapterRefund adapter = new ListViewAdapterRefund(refundmodelList, getApplicationContext());

//                                Collections.sort(refundmodelList, new Comparator<refundmodel>() {
//                                    public int compare(refundmodel o1, refundmodel o2) {
//                                        if (o2.getSubmit_date() == null || o1.getSubmit_date() == null)
//                                            return 0;
//                                        return o2.getSubmit_date().compareTo(o1.getSubmit_date());
//                                    }
//                                });


                                list.setAdapter(adapter);



                                simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextChange(String nextText) {
                                        adapter.getFilter().filter(nextText);
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextSubmit(final String query) {
                                        adapter.getFilter().filter(query);
                                        return false;
                                    }
                                });


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

            ImageView statusrefund = listViewItem.findViewById(R.id.statusrefund);
            ImageView statusba = listViewItem.findViewById(R.id.statusba);
            ImageView statusbpengajuan = listViewItem.findViewById(R.id.statusbpengajuan);

            TextView keteranganpengajuan = listViewItem.findViewById(R.id.keteranganpengajuan);

            refundmodel movieItem = getItem(position);

            tanggalid.setText((convertFormat(movieItem.getSubmit_date())));
            id.setText(movieItem.getNo_pengajuan());
            nik.setText(movieItem.getNik());
            nama.setText(movieItem.getNama_karyawan_struktur());

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + movieItem.getNik(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    JSONObject movieObject = movieArray.getJSONObject(i);

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
            RequestQueue requestQueue = Volley.newRequestQueue(list_refund.this);
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());


        super.onDestroy();
    }
}