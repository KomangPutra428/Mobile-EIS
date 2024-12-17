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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.eis2.Item.absensiteammodel;
import com.example.eis2.Item.approvalcutitahunan;
import com.example.eis2.Item.approvaldinasfull;
import com.example.eis2.Item.keteranganmodel;
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
import java.util.Locale;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.dinas.txt_jabatan2;
import static com.example.eis2.dinas.txt_nomor_jab;
import static com.example.eis2.izin.txt_jabatan;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class approval_dinas_full extends AppCompatActivity {
    SearchableSpinner spinner;
    ImageButton lihat;
    private List<approvaldinasfull> movieItemList;
    ListView list;
    ProgressDialog pDialog;
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_dinas_full);
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

        spinner = (SearchableSpinner) findViewById(R.id.jenisizin);
        lihat = (ImageButton) findViewById(R.id.lihat);
        list = findViewById(R.id.list);
        setNavigationDrawer();


        String[] jenisizin = {"Open", "Approved", "Not Approved", "Hangus", "Semua"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, jenisizin);
        spinner.setAdapter(adapter);

        lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dinasfull();
            }
        });
        movieItemList = new ArrayList<>();

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
                            approval_dinas_full.this);
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
                                    Intent intent = new Intent(approval_dinas_full.this, MainActivity.class);
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
        dinasfull();
    }

    private void dinasfull() {
        pDialog = new ProgressDialog(approval_dinas_full.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        System.out.println("hasil - " + text_jabatan.getText().toString());
        movieItemList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/dinas_full_day/index_atasan?jabatan_struktur=" + text_jabatan.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final approvaldinasfull movieItem = new approvaldinasfull(
                                        movieObject.getString("id_full_day"),
                                        movieObject.getString("tanggal_pengajuan"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("lokasi_struktur"),
                                        movieObject.getString("jenis_full_day"),
                                        movieObject.getString("start_full_day"),
                                        movieObject.getString("karyawan_pengganti"),
                                        movieObject.getString("ket_tambahan"),
                                        movieObject.getString("status_full_day"),
                                        movieObject.getString("feedback_full_day"));

                                movieItemList.add(movieItem);

                                if(spinner.getSelectedItem().toString().equals("Open")) {
                                    if (!movieObject.getString("status_full_day").equals("0")) {
                                        movieItemList.remove(movieItem);
                                    }
                                } if(spinner.getSelectedItem().toString().equals("Approved")) {
                                    if (!movieObject.getString("status_full_day").equals("1")) {
                                        movieItemList.remove(movieItem);
                                    }
                                }  if(spinner.getSelectedItem().toString().equals("Not Approved")) {
                                    if (!movieObject.getString("status_full_day").equals("2")) {
                                        movieItemList.remove(movieItem);
                                    }
                                } if(spinner.getSelectedItem().toString().equals("Hangus")) {
                                    if (!movieObject.getString("status_full_day").equals("3")) {
                                        movieItemList.remove(movieItem);
                                    }
                                } if(spinner.getSelectedItem().toString().equals("Semua")) {
                                    if (!movieItemList.contains(movieItem)) {
                                        movieItemList.add(movieItem);
                                    }
                                }

                                ListViewAdapterDinasFullDay adapter = new ListViewAdapterDinasFullDay(movieItemList, getApplicationContext());

                                hideDialog();
//                                Collections.sort(movieItemList, new Comparator<approvaldinasfull>() {
//                                    public int compare(approvaldinasfull o1, approvaldinasfull o2) {
//                                        if (o2.getStart_full_day() == null || o1.getStart_full_day() == null)
//                                            return 0;
//                                        return o2.getStart_full_day().compareTo(o1.getStart_full_day());
//                                    }
//                                });
                                if(!txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (!movieObject.getString("lokasi_struktur").equalsIgnoreCase(txt_lokasi.getText().toString())) {
                                        movieItemList.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                                if(spinner.getSelectedItem().toString().equals("Open")) {
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                            Intent i = new Intent(approval_dinas_full.this, form_dinas_full.class);
                                            String nik_baru = ((approvaldinasfull) parent.getItemAtPosition(position)).getId_full_day();
                                            i.putExtra(KEY_NIK, nik_baru);
                                            startActivity(i);
                                            System.out.println("Test :" + nik_baru);
                                        }
                                    });
                                } else {
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                                        }
                                    });
                                }

                                list.setAdapter(adapter);
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
                        hideDialog();
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static class ListViewAdapterDinasFullDay extends ArrayAdapter<approvaldinasfull> {

        List<approvaldinasfull> movieItemList;

        private Context context;

        public ListViewAdapterDinasFullDay(List<approvaldinasfull> movieItemList, Context context) {
            super(context, R.layout.list_item_dinas_approval_full_day, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_dinas_approval_full_day, null, true);

            TextView id = listViewItem.findViewById(R.id.id);
            TextView nama = listViewItem.findViewById(R.id.nama);
            TextView jenis = listViewItem.findViewById(R.id.jenis);
            TextView tanggalizin = listViewItem.findViewById(R.id.tanggal);
            TextView karyawanpengganti = listViewItem.findViewById(R.id.karyawan);
            TextView keterangan = listViewItem.findViewById(R.id.keterangan);
            TextView feedbackatasan1 = listViewItem.findViewById(R.id.feedback);
            TextView tanggal_2 = listViewItem.findViewById(R.id.tanggal_2);
            ImageView approval1 = listViewItem.findViewById(R.id.approval1);

            approvaldinasfull movieItem = getItem(position);

            tanggal_2.setText(convertFormat(movieItem.getStart_full_day()));
            id.setText(movieItem.getId_full_day());
            nama.setText(movieItem.getNama_karyawan_struktur());
            jenis.setText(movieItem.getJenis_full_day());
            tanggalizin.setText(tanggal(movieItem.getStart_full_day()));
            karyawanpengganti.setText(movieItem.getKaryawan_pengganti());
            keterangan.setText(movieItem.getKet_tambahan());
            feedbackatasan1.setText(movieItem.getFeedback_full_day());

            if(movieItem.getStatus_full_day().equals("0")){
                approval1.setImageResource(R.drawable.btn_open);
            } else if(movieItem.getStatus_full_day().equals("1")){
                approval1.setImageResource(R.drawable.btn_aprv);
            } else if(movieItem.getStatus_full_day().equals("2")){
                approval1.setImageResource(R.drawable.btn_notaprv);
            } else if(movieItem.getStatus_full_day().equals("3")){
                approval1.setImageResource(R.drawable.btn_hangus);
            }

            return listViewItem;
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
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