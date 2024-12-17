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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.eis2.Item.approvalcutikhusus;
import com.example.eis2.Item.approvalcutitahunan;
import com.example.eis2.Item.approvaldinasnonfullmodel;
import com.example.eis2.Item.mutasilistmodel;
import com.google.android.material.navigation.NavigationView;
import com.example.eis2.SearchSpinner.SearchableSpinner;

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
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class approval_mutasi extends AppCompatActivity {
    public static ListView list;
    private List<mutasilistmodel> movieItemList;
    SearchableSpinner spinner;
    ProgressDialog pDialog;
    ImageButton lihat;
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_mutasi);
        HttpsTrustManager.allowAllSSL();

        list = findViewById(R.id.list);
        spinner = (SearchableSpinner) findViewById(R.id.jenisizin);
        lihat = (ImageButton) findViewById(R.id.lihat);
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
        setNavigationDrawer();
        String[] jenisizin = {"Open", "Approved", "Not Approved", "Hangus", "Semua"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, jenisizin);
        spinner.setAdapter(adapter);

        lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approvedmutasi();
            }
        });
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
                            approval_mutasi.this);
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
                                    Intent intent = new Intent(approval_mutasi.this, MainActivity.class);
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

    @Override
    protected void onResume() {
        super.onResume();
        movieItemList = new ArrayList<>();
        approvedmutasi();
    }

    private void approvedmutasi() {
        pDialog = new ProgressDialog(approval_mutasi.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        String jabatan = text_jabatan.getText().toString();
        movieItemList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi/index_atasan?jabatan_struktur=" + jabatan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final mutasilistmodel movieItem = new mutasilistmodel(
                                        convertFormat(movieObject.getString("tanggal_pengajuan")),
                                        movieObject.getString("id_mutasi_rotasi"),
                                        movieObject.getString("permintaan"),
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("nama_karyawan_mutasi"),
                                        movieObject.getString("lokasi_awal"),
                                        movieObject.getString("status_atasan"),
                                        movieObject.getString("status_manager"));

                                movieItemList.add(movieItem);

                                hideDialog();

                                if(spinner.getSelectedItem().toString().equals("Open")) {
                                    if (!movieObject.getString("status_atasan").equals("0")) {
                                        movieItemList.remove(movieItem);
                                    }
                                } if(spinner.getSelectedItem().toString().equals("Approved")) {
                                    if (!movieObject.getString("status_atasan").equals("1")) {
                                        movieItemList.remove(movieItem);
                                    }
                                }  if(spinner.getSelectedItem().toString().equals("Not Approved")) {
                                    if (!movieObject.getString("status_atasan").equals("2")) {
                                        movieItemList.remove(movieItem);
                                    }
                                } if(spinner.getSelectedItem().toString().equals("Hangus")) {
                                    if (!movieObject.getString("status_atasan").equals("3")) {
                                        movieItemList.remove(movieItem);
                                    }
                                } if(spinner.getSelectedItem().toString().equals("Semua")) {
                                    if (!movieItemList.contains(movieItem)) {
                                        movieItemList.add(movieItem);
                                    }
                                }
                                ListViewAdapterMutasi adapter = new ListViewAdapterMutasi(movieItemList, getApplicationContext());

                                list.setAdapter(adapter);

                                if(!txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (!movieObject.getString("lokasi_awal").equalsIgnoreCase(txt_lokasi.getText().toString())) {
                                        movieItemList.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                                if(spinner.getSelectedItem().toString().equals("Open")) {
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                                            Intent i = new Intent(approval_mutasi.this, form_mutasi.class);
                                            String nik_baru = ((mutasilistmodel) parent.getItemAtPosition(position)).getId_mutasi_rotasi();
                                            i.putExtra(KEY_NIK, nik_baru);
                                            startActivity(i);
                                        }
                                    });
                                } else {
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                                        }
                                    });
                                }

                                Collections.sort(movieItemList, new Comparator<mutasilistmodel>() {
                                    public int compare(mutasilistmodel o1, mutasilistmodel o2) {
                                        if (o2.getTanggal_pengajuan() == null || o1.getTanggal_pengajuan() == null)
                                            return 0;
                                        return o2.getTanggal_pengajuan().compareTo(o1.getTanggal_pengajuan());
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
                )
        );
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

    public static class ListViewAdapterMutasi extends ArrayAdapter<mutasilistmodel> {

        List<mutasilistmodel> movieItemList;

        private Context context;

        public ListViewAdapterMutasi(List<mutasilistmodel> movieItemList, Context context) {
            super(context, R.layout.list_item_mutasirotasi, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_mutasirotasi, null, true);

            TextView tanggal_2 = listViewItem.findViewById(R.id.tanggal_2);
            TextView id = listViewItem.findViewById(R.id.id);
            TextView nik = listViewItem.findViewById(R.id.nik);
            TextView nama = listViewItem.findViewById(R.id.nama);
            TextView lokasi = listViewItem.findViewById(R.id.lokasi);
            TextView permintaan = listViewItem.findViewById(R.id.permintaan);
            ImageView approval1 = listViewItem.findViewById(R.id.approval1);
            ImageView approval_hr = listViewItem.findViewById(R.id.approval_hr);


            mutasilistmodel movieItem = getItem(position);

            tanggal_2.setText(movieItem.getTanggal_pengajuan());
            id.setText(movieItem.getId_mutasi_rotasi());
            nik.setText(movieItem.getNik_baru());
            nama.setText(movieItem.getNama_karyawan_mutasi());
            lokasi.setText(movieItem.getLokasi_awal());
            permintaan.setText(movieItem.getPermintaan());

            if(movieItem.getStatus_manager().equals("0")){
                approval_hr.setImageResource(R.drawable.btn_open);
            } else if(movieItem.getStatus_manager().equals("1")){
                approval_hr.setImageResource(R.drawable.btn_aprv);
            } else if(movieItem.getStatus_manager().equals("2")){
                approval_hr.setImageResource(R.drawable.btn_notaprv);
            } else if(movieItem.getStatus_manager().equals("3")){
                approval_hr.setImageResource(R.drawable.btn_hangus);
            }

            if(movieItem.getStatus_atasan().equals("0")){
                approval1.setImageResource(R.drawable.btn_open);
            } else if(movieItem.getStatus_atasan().equals("1")){
                approval1.setImageResource(R.drawable.btn_aprv);
            } else if(movieItem.getStatus_atasan().equals("2")){
                approval1.setImageResource(R.drawable.btn_notaprv);
            } else if(movieItem.getStatus_atasan().equals("3")){
                approval1.setImageResource(R.drawable.btn_hangus);
            }
            return listViewItem;
        }
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