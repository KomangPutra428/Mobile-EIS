package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
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
import com.example.eis2.Item.namanikmodel;
import com.example.eis2.Item.revisiteammodel;
import com.google.android.material.navigation.NavigationView;

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
import static com.example.eis2.menu.txt_dept;
import static com.example.eis2.menu.txt_lokasi;
import static com.example.eis2.spv_absensi.txt_jabatan_struktur;

public class revisishift extends AppCompatActivity {
    ListView list_team;
    private List<absensiteammodel> team;
    private SearchView searchView;
    ImageButton cari;
    SharedPreferences sharedPreferences;
    DrawerLayout dLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisishift);
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
        list_team = findViewById(R.id.list);
        searchView = findViewById(R.id.simpleSearchView);
        cari = (ImageButton)findViewById(R.id.cari);
        setNavigationDrawer();
        team = new ArrayList<>();
        loadTeam();
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
                            revisishift.this);
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
                                    Intent intent = new Intent(revisishift.this, MainActivity.class);
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

    private void loadTeam() {
        final StringRequest stringRequest1 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/team/index?jabatan_struktur=" +text_jabatan.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            final JSONObject object = new JSONObject(response);
                            JSONArray movieArray = object.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final absensiteammodel movieItem = new absensiteammodel(
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("jabatan_karyawan"),
                                        movieObject.getString("lokasi_struktur"),
                                        movieObject.getString("dept_struktur"));
                                team.add(movieItem);

                                final ListViewAdapterTeam adapter = new ListViewAdapterTeam(team, getApplicationContext());
                                list_team.setAdapter(adapter);
                                if (!txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (!movieObject.getString("lokasi_struktur").equalsIgnoreCase(txt_lokasi.getText().toString())) {
                                        team.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

                                list_team.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                                        Intent i = new Intent(revisishift.this, shifting_team.class);
                                        String nik_baru = ((absensiteammodel)parent.getItemAtPosition(position)).getNikbaru();
                                        i.putExtra(KEY_NIK, nik_baru);
                                        startActivity(i);
                                    }
                                });

                            }
                            Collections.sort(team, new Comparator<absensiteammodel>() {
                                public int compare(absensiteammodel o1, absensiteammodel o2) {
                                    if (o1.getJabatan_struktur() == null || o2.getJabatan_struktur() == null)
                                        return 0;
                                    return o1.getJabatan_struktur().compareTo(o2.getJabatan_struktur());
                                }
                            });


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
        stringRequest1.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest1);
    }

    public class ListViewAdapterTeam extends ArrayAdapter<absensiteammodel> {

        public List<absensiteammodel> team;
        private Context context;
        TextView nikbaru;
        TextView lokasi;

        public ListViewAdapterTeam(List<absensiteammodel> team, Context context) {
            super(context, R.layout.list_item_revisi_team, team);
            this.team = team;
            this.context = context;

        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_revisi_team, null, true);

            nikbaru = listViewItem.findViewById(R.id.nikbaru);
            TextView nama = listViewItem.findViewById(R.id.nama);
            TextView jabatan = listViewItem.findViewById(R.id.jabatan1);
            lokasi = listViewItem.findViewById(R.id.lokasi1);
            TextView department = listViewItem.findViewById(R.id.depart);


            final absensiteammodel movieItem = getItem(position);

            nikbaru.setText(movieItem.getNikbaru());
            nama.setText(movieItem.getNama_karyawan_struktur());
            jabatan.setText(movieItem.getJabatan_struktur());
            lokasi.setText(movieItem.getLokasi_struktur());
            department.setText(movieItem.getDept_struktur());


            return listViewItem;
        }
    }
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

        
        super.onDestroy();
    }
}