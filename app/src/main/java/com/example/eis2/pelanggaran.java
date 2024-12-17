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
import android.graphics.Color;
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
import com.example.eis2.Item.kontrakmodel;
import com.example.eis2.Item.pelanggaranmodel;
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

public class pelanggaran extends AppCompatActivity {
    public static ListView list;
    private List<pelanggaranmodel> movieItemList;
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;
    DrawerLayout dLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelanggaran);
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
        list = findViewById(R.id.list);
        movieItemList = new ArrayList<>();
        setNavigationDrawer();
//        getPunishment();
    }

//    private void getPunishment() {
//        pDialog = new ProgressDialog(pelanggaran.this);
//        showDialog();
//        pDialog.setContentView(R.layout.progress_dialog);
//        pDialog.getWindow().setBackgroundDrawableResource(
//                android.R.color.transparent
//        );
//        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
//        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/punishment/index?nik_baru=" + nik_baru,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            final JSONArray movieArray = obj.getJSONArray("data");
//
//                            JSONObject movieObject = null;
//                            for (int i = 0; i < movieArray.length(); i++) {
//
//                                movieObject = movieArray.getJSONObject(i);
//
//                                final pelanggaranmodel movieItem = new pelanggaranmodel(
//                                        movieObject.getString("no_surat"),
//                                        movieObject.getString("punishment_historical_violance"),
//                                        movieObject.getString("pelanggaran_historical_violance"),
//                                        movieObject.getString("warning_note_historical_violance"),
//                                        movieObject.getString("warning_start_historical_violance"),
//                                        movieObject.getString("warning_end_historical_violance"),
//                                        movieObject.getString("status_dokumen"));
//
//
//                                movieItemList.add(movieItem);
//
//                                hideDialog();
//
//                                ListViewAdapterPelanggaran adapter = new ListViewAdapterPelanggaran(movieItemList, getApplicationContext());
//
//                                Collections.sort(movieItemList, new Comparator<pelanggaranmodel>() {
//                                    public int compare(pelanggaranmodel o1, pelanggaranmodel o2) {
//                                        if (o2.getWarning_start_historical_violance() == null || o1.getWarning_start_historical_violance() == null)
//                                            return 0;
//                                        return o2.getNo_surat().compareTo(o1.getNo_surat());
//                                    }
//                                });
//
//                                list.setAdapter(adapter);
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), "Belum ada Pengajuan", Toast.LENGTH_SHORT).show();
//                        hideDialog();
//                    }
//                })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<String, String>();
//                String creds = String.format("%s:%s", "admin", "Databa53");
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                params.put("Authorization", auth);
//                return params;
//            }
//        };
//
//        stringRequest.setRetryPolicy(
//                new DefaultRetryPolicy(
//                        500000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                ));
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }

//    public class ListViewAdapterPelanggaran extends ArrayAdapter<pelanggaranmodel> {
//
//        List<pelanggaranmodel> movieItemList;
//
//        private Context context;
//
//        public ListViewAdapterPelanggaran(List<pelanggaranmodel> movieItemList, Context context) {
//            super(context, R.layout.list_item_pelanggaran, movieItemList);
//            this.movieItemList = movieItemList;
//            this.context = context;
//        }
//
//        @SuppressLint("NewApi")
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            LayoutInflater inflater = LayoutInflater.from(context);
//
//            View listViewItem = inflater.inflate(R.layout.list_item_pelanggaran, null, true);
//
//            TextView tanggal_2 = listViewItem.findViewById(R.id.tanggal_2);
//
//            TextView no = listViewItem.findViewById(R.id.no);
//            TextView sanksi = listViewItem.findViewById(R.id.sanksi);
//            TextView pelanggaran = listViewItem.findViewById(R.id.pelanggaran);
//            TextView keterangan = listViewItem.findViewById(R.id.keterangan);
//            TextView efektif = listViewItem.findViewById(R.id.efektif);
//            TextView berakhir = listViewItem.findViewById(R.id.berakhir);
//            TextView sanksistatus = listViewItem.findViewById(R.id.sanksistatus);
//
//            pelanggaranmodel movieItem = getItem(position);
//
//
//            tanggal_2.setText(convertFormat(movieItem.getWarning_start_historical_violance()));
//            no.setText(movieItem.getNo_surat());
//            sanksi.setText(movieItem.getPunishment_historical_violance());
//            pelanggaran.setText(movieItem.getPelanggaran_historical_violance());
//            keterangan.setText(movieItem.getWarning_note_historical_violance());
//
//            efektif.setText(movieItem.getWarning_start_historical_violance());
//            berakhir.setText(movieItem.getWarning_end_historical_violance());
//            sanksistatus.setText(movieItem.getStatus_dokumen());
//
//
//            return listViewItem;
//        }
//    }

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
                            pelanggaran.this);
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
                                    Intent intent = new Intent(pelanggaran.this, MainActivity.class);
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        return convetDateFormat.format(date);
    }
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());


        super.onDestroy();
    }
}