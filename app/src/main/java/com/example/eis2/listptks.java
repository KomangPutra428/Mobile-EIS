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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.eis2.Item.CDTmodel;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.ptkManagerModel;
import com.example.eis2.Item.ptkmodel;
import com.google.android.material.button.MaterialButton;
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

public class listptks extends AppCompatActivity {
    ListView list;
    List<ptkmodel> ptkmodelList = new ArrayList<>();
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    SearchView caridata;
    DrawerLayout dLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listptks);
        HttpsTrustManager.allowAllSSL();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

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
                            listptks.this);
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
                                    Intent intent = new Intent(listptks.this, MainActivity.class);
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });

        list = findViewById(R.id.list);
        caridata = findViewById(R.id.caridata);

    }

    public class ListViewAdapterPTK extends ArrayAdapter<ptkmodel> {
        private List<ptkmodel> ptkmodelList;

        private Context context;

        public ListViewAdapterPTK(List<ptkmodel> ptkmodelList, Context context) {
            super(context, R.layout.list_item_ptk, ptkmodelList);
            this.ptkmodelList = ptkmodelList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_ptk, null, true);

            TextView tanggal_2 = listViewItem.findViewById(R.id.tanggal_2);
            TextView jabatan = listViewItem.findViewById(R.id.jabatan);
            TextView level = listViewItem.findViewById(R.id.level);
            TextView depo = listViewItem.findViewById(R.id.depo);

            TextView depart = listViewItem.findViewById(R.id.depart);
            TextView analisa = listViewItem.findViewById(R.id.analisa);
            TextView ptk = listViewItem.findViewById(R.id.ptk);
            TextView nomor = listViewItem.findViewById(R.id.nomor);
            TextView textpengajuan = listViewItem.findViewById(R.id.textpengajuan);

            ImageView atasan = listViewItem.findViewById(R.id.atasan);
            ImageView hrd = listViewItem.findViewById(R.id.hrd);

            Button cancel = listViewItem.findViewById(R.id.cancel);
            LinearLayout pengajuanLayout = listViewItem.findViewById(R.id.pengajuanLayout);

            pengajuanLayout.setVisibility(View.VISIBLE);

            ptkmodel ptkmodel = getItem(position);

            if(ptkmodel.getStatus_cancel().equals("0")){
                textpengajuan.setText("Active");
                cancel.setVisibility(View.VISIBLE);
            } else {
                textpengajuan.setText("Cancelled");
            }

            tanggal_2.setText(ptkmodel.getSubmit_date());
            jabatan.setText(ptkmodel.getJabatan_karyawan());
            level.setText(ptkmodel.getLevel_jabatan());
            depo.setText(ptkmodel.getDepo_ptk());

            depart.setText(ptkmodel.getDept_ptk());
            analisa.setText(ptkmodel.getAnalisa());
            ptk.setText(ptkmodel.getTenaga_kerja());
            nomor.setText(ptkmodel.getNo_pengajuan());

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(listptks.this, form_ptk_cancel.class);
                    String ids = ptkmodel.getNo_pengajuan();
                    String analisa = ptkmodel.getAnalisa();
                    String jabatan_karyawan = ptkmodel.getJabatan_karyawan();
                    String tenaga_kerja = ptkmodel.getTenaga_kerja();

                    i.putExtra("id_pengajuan", ids);
                    i.putExtra("analisa", analisa);
                    i.putExtra("jabatan_karyawan", jabatan_karyawan);
                    i.putExtra("tenaga_kerja", tenaga_kerja);

                    startActivity(i);
                }
            });

            if (ptkmodel.getStatus_atasan().equals("0")) {
                atasan.setImageResource(R.drawable.btn_open);
            } else if (ptkmodel.getStatus_atasan().equals("1")) {
                atasan.setImageResource(R.drawable.btn_aprv);
            } else if (ptkmodel.getStatus_atasan().equals("2")) {
                atasan.setImageResource(R.drawable.btn_notaprv);
            } else if (ptkmodel.getStatus_atasan().equals("3")) {
                atasan.setImageResource(R.drawable.btn_hangus);
            }

            if (ptkmodel.getStatus_hrd().equals("0")) {
                hrd.setImageResource(R.drawable.btn_open);
            } else if (ptkmodel.getStatus_hrd().equals("1")) {
                hrd.setImageResource(R.drawable.btn_aprv);
            } else if (ptkmodel.getStatus_hrd().equals("2")) {
                hrd.setImageResource(R.drawable.btn_notaprv);
            } else if (ptkmodel.getStatus_hrd().equals("3")) {
                hrd.setImageResource(R.drawable.btn_hangus);
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
    @Override
    protected void onResume() {
        super.onResume();
        ptkmodelList.clear();
        pDialog = new ProgressDialog(listptks.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK ,null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Ptk/index_nik?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                ptkmodel cdt = new ptkmodel(
                                        movieObject.getString("id"),
                                        movieObject.getString("submit_date"),
                                        movieObject.getString("nik_pengajuan"),
                                        movieObject.getString("jabatan_karyawan"),
                                        movieObject.getString("level_jabatan"),
                                        movieObject.getString("depo_ptk"),
                                        movieObject.getString("dept_ptk"),
                                        movieObject.getString("analisa"),
                                        movieObject.getString("tenaga_kerja"),
                                        movieObject.getString("status_atasan"),
                                        movieObject.getString("status_hrd"),
                                        movieObject.getString("no_pengajuan"),
                                        movieObject.getString("status_cancel"));

                                ptkmodelList.add(cdt);

                                hideDialog();
                                final ListViewAdapterPTK adapter = new ListViewAdapterPTK(ptkmodelList, getApplicationContext());
                                list.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                list.setVisibility(View.VISIBLE);


                                caridata.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextChange(String nextText) {
                                        adapter.getFilter().filter(nextText);
                                        return false;
                                    }
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
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
                        hideDialog();
                        list.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Maaf, anda belum pernah mengajukan PTK", Toast.LENGTH_SHORT).show();
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
}