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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.approvalcutitahunan;
import com.example.eis2.Item.approvaldinasfull;
import com.example.eis2.Item.approveskmodel;
import com.example.eis2.Item.suratketeranganmodel;
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
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class approval_sk extends AppCompatActivity {
    ListView list;
    private List<approveskmodel> movieItemList;
    private SearchView simpleSearchView;
    ProgressDialog pDialog;

    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_sk);
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
        simpleSearchView = findViewById(R.id.simpleSearchView);
        list = findViewById(R.id.list);
        setNavigationDrawer();
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
                            approval_sk.this);
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
                                    Intent intent = new Intent(approval_sk.this, MainActivity.class);
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
        getSK();
    }

    private void getSK() {
        String lokasi = text_jabatan.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/SK/index_atasan?jabatan_struktur=" + lokasi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final approveskmodel movieItem = new approveskmodel(
                                        movieObject.getString("id"),
                                        movieObject.getString("submit_date"),
                                        movieObject.getString("no_urut"),
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("jabatan_karyawan"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("keperluan"),
                                        movieObject.getString("analisa"),
                                        movieObject.getString("status_atasan"),
                                        movieObject.getString("status_hrd"),
                                        movieObject.getString("lokasi_struktur"));

                                movieItemList.add(movieItem);

                                ListViewAdapterApprovalSK adapter = new ListViewAdapterApprovalSK(movieItemList, getApplicationContext());

                                Collections.sort(movieItemList, new Comparator<approveskmodel>() {
                                    public int compare(approveskmodel o1, approveskmodel o2) {
                                        if (o1.getStatus_atasan() == null || o2.getStatus_atasan() == null)
                                            return 0;
                                        return o1.getStatus_atasan().compareTo(o2.getStatus_atasan());
                                    }
                                });

                                if (!movieObject.getString("lokasi_struktur").equalsIgnoreCase(txt_lokasi.getText().toString())) {
                                    movieItemList.remove(movieItem);
                                    adapter.notifyDataSetChanged();
                                } else if (movieObject.getString("lokasi_struktur").equalsIgnoreCase("pusat")) {
                                    adapter.notifyDataSetChanged();
                                }
                                adapter.notifyDataSetChanged();

                                list.setAdapter(adapter);

                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                                        final String idsk = ((approveskmodel) parent.getItemAtPosition(position)).getId();
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                approval_sk.this);
                                        alertDialogBuilder.setTitle("Anda ingin melakukan approval ?");
                                        alertDialogBuilder
                                                .setMessage("Klik Ya untuk approval!")
                                                .setCancelable(false)
                                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        pDialog = new ProgressDialog(approval_sk.this);
                                                        showDialog();
                                                        pDialog.setContentView(R.layout.progress_dialog);
                                                        pDialog.getWindow().setBackgroundDrawableResource(
                                                                android.R.color.transparent
                                                        );

                                                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/SK/index",
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        hideDialog();
                                                                        Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                                                                    }
                                                                },
                                                                new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        if (error instanceof NetworkError) {
                                                                            Toast.makeText(getApplicationContext(), "maaf ada kesalahan", Toast.LENGTH_LONG).show();
                                                                        }
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

                                                                params.put("id", idsk);
                                                                params.put("status_atasan", "1");


                                                                return params;
                                                            }

                                                        };

                                                        RequestQueue requestQueue = Volley.newRequestQueue(approval_sk.this);
                                                        requestQueue.add(stringRequest);
                                                    }
                                                })
                                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();

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
            };
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

    public static class ListViewAdapterApprovalSK extends ArrayAdapter<approveskmodel> {

        List<approveskmodel> movieItemList;

        private Context context;

        public ListViewAdapterApprovalSK(List<approveskmodel> movieItemList, Context context) {
            super(context, R.layout.list_item_approvalsk, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_approvalsk, null, true);

            TextView tanggalid = listViewItem.findViewById(R.id.tanggalid);
            TextView nik = listViewItem.findViewById(R.id.nik);
            TextView kebutuhan = listViewItem.findViewById(R.id.kebutuhan);
            TextView kebutuhan1 = listViewItem.findViewById(R.id.kebutuhan1);
            TextView jab = listViewItem.findViewById(R.id.jab);
            TextView name = listViewItem.findViewById(R.id.name);

            ImageView approvalatasan = listViewItem.findViewById(R.id.approvalatasan);
            ImageView approvalhrd = listViewItem.findViewById(R.id.approvalhrd);

            approveskmodel movieItem = getItem(position);
            tanggalid.setText(convertFormat(movieItem.getSubmit_date()));
            name.setText(movieItem.getNama_karyawan_struktur());
            jab.setText(movieItem.getJabatan_karyawan());
            nik.setText(movieItem.getNik_baru());
            kebutuhan.setText(movieItem.getKeperluan());
            kebutuhan1.setText(movieItem.getAnalisa());

            if (movieItem.getStatus_atasan().equals("0")) {
                approvalatasan.setImageResource(R.drawable.btn_open);
            } else if (movieItem.getStatus_atasan().equals("1")) {
                approvalatasan.setImageResource(R.drawable.btn_aprv);
            } else if (movieItem.getStatus_atasan().equals("2")) {
                approvalatasan.setImageResource(R.drawable.btn_notaprv);
            } else if (movieItem.getStatus_atasan().equals("3")) {
                approvalatasan.setImageResource(R.drawable.btn_hangus);
            }
            if (movieItem.getStatus_hrd().equals("0")) {
                approvalhrd.setImageResource(R.drawable.btn_open);
            } else if (movieItem.getStatus_hrd().equals("1")) {
                approvalhrd.setImageResource(R.drawable.btn_aprv);
            } else if (movieItem.getStatus_hrd().equals("2")) {
                approvalhrd.setImageResource(R.drawable.btn_notaprv);
            } else if (movieItem.getStatus_hrd().equals("3")) {
                approvalhrd.setImageResource(R.drawable.btn_hangus);
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
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());


        super.onDestroy();
    }
}

//    private void getSK() {
//

//    }