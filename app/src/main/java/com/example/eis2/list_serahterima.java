package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.eis2.Item.approvaldinasfull;
import com.example.eis2.Item.resignteammodel;
import com.example.eis2.Item.serahterimamodels;
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
import static com.example.eis2.menu.txt_lokasi;

public class list_serahterima extends AppCompatActivity {
    SearchView caridata;
    ListView list;
    private List<serahterimamodels> serahterimamodelsList;
    ProgressDialog pDialog;
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_serahterima);
        HttpsTrustManager.allowAllSSL();
        setNavigationDrawer();

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

        caridata = (SearchView) findViewById(R.id.caridata);
        list = findViewById(R.id.list);

        serahterimamodelsList = new ArrayList<>();
    }

    private void getData() {
        pDialog = new ProgressDialog(list_serahterima.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        serahterimamodelsList.clear();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_penerima?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final serahterimamodels movieItem = new serahterimamodels(
                                        movieObject.getString("id"),
                                        movieObject.getString("submit_date"),
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("nik_penerima_1"),
                                        movieObject.getString("status_penerima_1"),
                                        movieObject.getString("tanggal_penerima_1"),
                                        movieObject.getString("nik_penerima_2"),
                                        movieObject.getString("status_penerima_2"),
                                        movieObject.getString("tanggal_penerima_2"));

                                serahterimamodelsList.add(movieItem);

                                hideDialog();

                                final ListViewAdapterSerahTerima adapter = new ListViewAdapterSerahTerima(serahterimamodelsList, getApplicationContext());
                                list.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                        Intent i = new Intent(list_serahterima.this, form_serahterima.class);
                                        String nik_baru = ((serahterimamodels) parent.getItemAtPosition(position)).getId();
                                        i.putExtra(KEY_NIK, nik_baru);
                                        startActivity(i);
                                    }
                                });

                                caridata.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        adapter.getFilter().filter(query);
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        adapter.getFilter().filter(newText);
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
                            list_serahterima.this);
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
                                    Intent intent = new Intent(list_serahterima.this, MainActivity.class);
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

    public class ListViewAdapterSerahTerima extends ArrayAdapter<serahterimamodels> {

        List<serahterimamodels> serahterimamodelsList;

        private Context context;

        public ListViewAdapterSerahTerima(List<serahterimamodels> serahterimamodelsList, Context context) {
            super(context, R.layout.list_itemserahterima, serahterimamodelsList);
            this.serahterimamodelsList = serahterimamodelsList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_itemserahterima, null, true);
            TextView tanggal_2 = listViewItem.findViewById(R.id.tanggal_2);
            TextView nikbaru = listViewItem.findViewById(R.id.nikbaru);
            final TextView namakaryawan = listViewItem.findViewById(R.id.namakaryawan);
            final TextView jab = listViewItem.findViewById(R.id.jab);
            final TextView depart = listViewItem.findViewById(R.id.depart);

            TextView nikpenerima_1 = listViewItem.findViewById(R.id.nikpenerima_1);
            final TextView namapenerima_1 = listViewItem.findViewById(R.id.namapenerima_1);
            ImageView approval1 = listViewItem.findViewById(R.id.approval1);

            TextView nikpenerima_2 = listViewItem.findViewById(R.id.nikpenerima_2);
            final TextView namapenerima_2 = listViewItem.findViewById(R.id.namapenerima_2);
            ImageView approval2 = listViewItem.findViewById(R.id.approval2);

            serahterimamodels movieItem = getItem(position);
            namakaryawan.setText(movieItem.getNama_karyawan_struktur());

            if(movieItem.getStatus_penerima_1().equals("0")){
                approval1.setImageResource(R.drawable.btn_open);
            } else if(movieItem.getStatus_penerima_1().equals("1")){
                approval1.setImageResource(R.drawable.btn_aprv);
            } else if(movieItem.getStatus_penerima_1().equals("2")){
                approval1.setImageResource(R.drawable.btn_notaprv);
            } else if(movieItem.getStatus_penerima_1().equals("3")){
                approval1.setImageResource(R.drawable.btn_hangus);
            }

            if(movieItem.getStatus_penerima_2().equals("0")){
                approval2.setImageResource(R.drawable.btn_open);
            } else if(movieItem.getStatus_penerima_2().equals("1")){
                approval2.setImageResource(R.drawable.btn_aprv);
            } else if(movieItem.getStatus_penerima_2().equals("2")){
                approval2.setImageResource(R.drawable.btn_notaprv);
            } else if(movieItem.getStatus_penerima_2().equals("3")){
                approval2.setImageResource(R.drawable.btn_hangus);
            }

            tanggal_2.setText(convertFormat(movieItem.getSubmit_date()));
            nikbaru.setText(movieItem.getNik_baru());

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + movieItem.getNik_baru(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    jab.setText(movieObject.getString("jabatan_karyawan"));
                                    depart.setText(movieObject.getString("dept_struktur"));

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
            RequestQueue requestQueue = Volley.newRequestQueue(list_serahterima.this);
            requestQueue.add(stringRequest);

            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + movieItem.getNik_penerima_1(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    namapenerima_1.setText(movieObject.getString("nama_karyawan_struktur"));

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(list_serahterima.this);
            requestQueue2.add(stringRequest2);

            StringRequest stringRequest3 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + movieItem.getNik_penerima_2(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    namapenerima_2.setText(movieObject.getString("nama_karyawan_struktur"));

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
            RequestQueue requestQueue3 = Volley.newRequestQueue(list_serahterima.this);
            requestQueue3.add(stringRequest3);



            nikpenerima_1.setText(movieItem.getNik_penerima_1());

            nikpenerima_2.setText(movieItem.getNik_penerima_2());



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

    @Override
    protected void onResume() {
        super.onResume();
        serahterimamodelsList = new ArrayList<>();
        getData();

    }
}