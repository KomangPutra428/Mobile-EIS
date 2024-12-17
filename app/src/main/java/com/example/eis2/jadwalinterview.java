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
import android.net.Uri;
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
import com.example.eis2.Item.approvaldinasfull;
import com.example.eis2.Item.jadwalinterviewmodel;
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
import java.util.Locale;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;

public class jadwalinterview extends AppCompatActivity {
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    private List<jadwalinterviewmodel> movieItemList;
    ListView list;
    ProgressDialog pDialog;
    private SearchView searchView;
    TextView date;
    Button nextbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwalinterview);
        HttpsTrustManager.allowAllSSL();


        date = (TextView) findViewById(R.id.date);
        list = findViewById(R.id.list);
        movieItemList = new ArrayList<>();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        nextbutton = (Button) findViewById(R.id.nextbutton);

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        date.setText(formattedDate);

        date.setVisibility(View.GONE);
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
        searchView = findViewById(R.id.simpleSearchView);


        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (jadwalinterview.this, jadwalinterview_7days.class);
                startActivity(i);
            }
        });

        jadwalinterviewlist();
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


    private void jadwalinterviewlist() {
        movieItemList.clear();
        pDialog = new ProgressDialog(jadwalinterview.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/website/jadwal/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                jadwalinterviewmodel movieItem = new jadwalinterviewmodel(
                                        movieObject.getString("id"),
                                        movieObject.getString("posisi"),
                                        movieObject.getString("no_ktp"),
                                        movieObject.getString("nama_lengkap"),
                                        tanggal(movieObject.getString("tanggal_interview")),
                                        movieObject.getString("jam_interview"),
                                        movieObject.getString("lokasi_interview"),
                                        movieObject.getString("status"),
                                        movieObject.getString("nama_pewawancara_1"),
                                        movieObject.getString("nama_pewawancara_2"),
                                        movieObject.getString("nama_pewawancara_"),
                                        movieObject.getString("kesimpulan"));
                                movieItemList.add(movieItem);

                                if(!tanggal(movieObject.getString("tanggal_interview")).equals(date.getText().toString())){
                                    movieItemList.remove(movieItem);
                                }

                                if(movieObject.getString("status").equals("1")){
                                    movieItemList.remove(movieItem);
                                }

                                hideDialog();


                                final ListViewAdapterInterview adapter = new ListViewAdapterInterview(movieItemList, getApplicationContext());

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

                                list.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                Collections.sort(movieItemList, new Comparator<jadwalinterviewmodel>() {
                                    public int compare(jadwalinterviewmodel o1, jadwalinterviewmodel o2) {
                                        if (o2.getTanggal_interview() == null || o1.getTanggal_interview() == null)
                                            return 0;
                                        return o2.getTanggal_interview().compareTo(o1.getTanggal_interview());
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
                        Toast.makeText(getApplicationContext(), "Maaf, anda belum pernah mengajukan izin non full", Toast.LENGTH_SHORT).show();
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

    public class ListViewAdapterInterview extends ArrayAdapter<jadwalinterviewmodel> {

        private List<jadwalinterviewmodel> movieItemList;

        private Context context;

        public ListViewAdapterInterview(List<jadwalinterviewmodel> movieItemList, Context context) {
            super(context, R.layout.list_item_jadwalinterview, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView ( final int position, View convertView, ViewGroup parent){

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_jadwalinterview, null, true);

            TextView tanggal_2 = listViewItem.findViewById(R.id.tanggal_2);
            TextView id2 = listViewItem.findViewById(R.id.id2);

            TextView posisi2 = listViewItem.findViewById(R.id.posisi2);
            TextView no_ktp2 = listViewItem.findViewById(R.id.no_ktp2);
            TextView nama_lengkap2 = listViewItem.findViewById(R.id.nama_lengkap2);

            TextView tanggal_interview2 = listViewItem.findViewById(R.id.tanggal_interview2);
            TextView jam_interview2 = listViewItem.findViewById(R.id.jam_interview2);
            TextView lokasi_interview2 = listViewItem.findViewById(R.id.lokasi_interview2);
            ImageButton cetak = listViewItem.findViewById(R.id.cetak);
            ImageButton interview = listViewItem.findViewById(R.id.interview);
            final jadwalinterviewmodel movieItem = movieItemList.get(position);

            cetak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(jadwalinterview.this, ubahjadwal.class);
                    String nik_baru = movieItem.getId_interview();
                    i.putExtra(KEY_NIK, nik_baru);
                    startActivity(i);
                }
            });

            tanggal_2.setText(movieItem.getTanggal_interview());
            id2.setText(movieItem.getId_interview());
            posisi2.setText(movieItem.getPosisi());
            no_ktp2.setText(movieItem.getNo_ktp());
            nama_lengkap2.setText(movieItem.getNama_lengkap());
            tanggal_interview2.setText(movieItem.getTanggal_interview());
            jam_interview2.setText(movieItem.getJam_interview());
            lokasi_interview2.setText(movieItem.getLokasi_interview());

            if(movieItem.getNama_pewawancara_1().equals("null")){
                interview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Maaf, nama pewawancara belum ada", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                interview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(jadwalinterview.this, form_interview.class);
                        String nik_baru = movieItem.getId_interview();
                        i.putExtra(KEY_NIK, nik_baru);
                        startActivity(i);
                    }
                });
            }

            return listViewItem;
        }
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
                            jadwalinterview.this);
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
                                    Intent intent = new Intent(jadwalinterview.this, MainActivity.class);
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

    private void showDialog () {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog () {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());


        super.onDestroy();
    }
}