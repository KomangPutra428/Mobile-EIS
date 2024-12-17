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
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.approvaldinasfull;
import com.example.eis2.Item.shifting_model;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
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

public class shifting_team extends AppCompatActivity {
    ListView list;
    EditText namaedit;
    private List<shifting_model> movieItemList;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    DrawerLayout dLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifting_team);
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
        namaedit = (EditText) findViewById(R.id.namaedit);
        setNavigationDrawer();

        getName();

        movieItemList = new ArrayList<>();

        shiftinglist();

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
                            shifting_team.this);
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
                                    Intent intent = new Intent(shifting_team.this, MainActivity.class);
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

    private void getName() {
        String nik_baru = getIntent().getStringExtra(KEY_NIK);

        System.out.println("hasil =" + nik_baru);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);
                                namaedit.setText(movieObject.getString("nama_karyawan_struktur"));

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
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void shiftinglist() {
        String nik_baru = getIntent().getStringExtra(KEY_NIK);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/shifting/index?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final shifting_model movieItem = new shifting_model(
                                        movieObject.getString("id_karyawan_shift"),
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("nama_karyawan_shift"),
                                        movieObject.getString("dept_karyawan_shift"),
                                        movieObject.getString("shift_day"),
                                        movieObject.getString("waktu_masuk"),
                                        movieObject.getString("waktu_keluar"));
                                movieItemList.add(movieItem);

                                Collections.sort(movieItemList, new Comparator<shifting_model>() {
                                    public int compare(shifting_model o1, shifting_model o2) {
                                        if (o2.getShift_day() == null || o1.getShift_day() == null)
                                            return 0;
                                        return o2.getShift_day().compareTo(o1.getShift_day());
                                    }
                                });

                                ListViewAdapterShiftingTeam adapter = new ListViewAdapterShiftingTeam(movieItemList, getApplicationContext());
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
                        Toast.makeText(getApplicationContext(), "Maaf, belum pernah absen", Toast.LENGTH_SHORT).show();
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

    public class ListViewAdapterShiftingTeam extends ArrayAdapter<shifting_model> {

        List<shifting_model> movieItemList;

        private Context context;
        public LinearLayout button;
        public ListViewAdapterShiftingTeam(List<shifting_model> movieItemList, Context context) {
            super(context, R.layout.list_item_shifting, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_shifting, null, true);

            TextView tanggalid = listViewItem.findViewById(R.id.tanggalid);
            TextView nik = listViewItem.findViewById(R.id.nik);
            TextView nama = listViewItem.findViewById(R.id.name);
            TextView depart = listViewItem.findViewById(R.id.depart);
            TextView tanggal = listViewItem.findViewById(R.id.shift);
            TextView jam = listViewItem.findViewById(R.id.jamshift);
            ImageButton edit = listViewItem.findViewById((R.id.edit));
            ImageButton hapus = listViewItem.findViewById((R.id.hapus));
            button = listViewItem.findViewById(R.id.button);


            final shifting_model movieItem = getItem(position);

            tanggalid.setText(convertFormat(movieItem.getShift_day()));
            nik.setText(movieItem.getNik_baru());
            depart.setText(movieItem.getDept_karyawan_shift());
            nama.setText(movieItem.getNama_karyawan_shift());
            tanggal.setText(tanggal(movieItem.getShift_day()));
            jam.setText(movieItem.getWaktu_masuk() + " - " + movieItem.getWaktu_keluar());

            String dtStart = convertFormat(movieItem.getShift_day());
            SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
            Date date1 = null;
            try {
                date1 = format.parse(dtStart);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            Date date2 = null;
            try {
                date2 = format.parse(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(date1.equals(date2)){
                button.setVisibility(View.GONE);
            } else if(date1.before(date2)){
                button.setVisibility(View.GONE);
            }

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(shifting_team.this, form_shift.class);
                    String nik_baru = movieItem.getId_karyawan_shift();
                    i.putExtra(KEY_NIK, nik_baru);
                    System.out.println("hasil =" + nik_baru);
                    startActivity(i);
                }
            });

            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            shifting_team.this);
                    alertDialogBuilder.setTitle("Anda yakin untuk menghapus data ini ?");
                    alertDialogBuilder
                            .setMessage("Klik Ya untuk hapus data shifting!")
                            .setCancelable(false)
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                public void onClick(DialogInterface dialog, int id) {
                                    final String ids = (movieItem.getId_karyawan_shift());
                                    System.out.println("hasil = " + id);
                                    pDialog = new ProgressDialog(shifting_team.this);
                                    showDialog();
                                    pDialog.setContentView(R.layout.progress_dialog);
                                    pDialog.getWindow().setBackgroundDrawableResource(
                                            android.R.color.transparent
                                    );

                                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/shifting/index",
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    hideDialog();
                                                    Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                                                    shifting_team.this.finish();
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

                                            params.put("id_karyawan_shift", ids);

                                            return params;
                                        }

                                    };
                                    stringRequest.setRetryPolicy(
                                            new DefaultRetryPolicy(
                                                    500000,
                                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                            ));
                                    RequestQueue requestQueue = Volley.newRequestQueue(shifting_team.this);
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

            return listViewItem;
        }
        private void hideDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

        private void showDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }
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
