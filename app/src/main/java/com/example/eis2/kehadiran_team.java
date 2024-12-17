package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.eis2.Item.keteranganmodel;
import com.example.eis2.Item.keteranganteammodel;
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
import static java.util.Calendar.DAY_OF_MONTH;

public class kehadiran_team extends AppCompatActivity {

    EditText tanggalawal, tanggalakhir, periodeedit, namaedit;
    private List<keteranganmodel> movieItemList;
    ListView list;
    ImageButton ceklihat, tambah, hapus;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    DrawerLayout dLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kehadiran_team);
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
        tanggalawal = (EditText) findViewById(R.id.tanggalawal);
        tanggalakhir = (EditText) findViewById(R.id.tanggalakhir);
        periodeedit = (EditText) findViewById(R.id.periodeedit);
        namaedit = (EditText) findViewById(R.id.namaedit);
        setNavigationDrawer();
        tambah = (ImageButton) findViewById(R.id.tambah);
        hapus = (ImageButton) findViewById(R.id.hapus);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tanggalakhir.setVisibility(View.VISIBLE);
                hapus.setVisibility(View.VISIBLE);

            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tanggalakhir.setVisibility(View.GONE);
                hapus.setVisibility(View.GONE);

            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat( "dd-MMMM-yyyy" );
        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.DAY_OF_MONTH, myCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        tanggalawal.setText(sdf.format(myCalendar.getTime()));

        SimpleDateFormat sdf2 = new SimpleDateFormat( "dd-MMMM-yyyy" );
        final Calendar myCalendar2 = Calendar.getInstance();
        tanggalakhir.setText( sdf2.format((new Date())));

        ceklihat = (ImageButton) findViewById(R.id.ceklihat);


        getName();

        ceklihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String awal = tanggalawal.getText().toString();
                String akhir = tanggalakhir.getText().toString();
                System.out.println("hasil1 =" + awal);
                System.out.println("hasil2 =" + akhir);
                periodeedit.setText(awal + " - " + akhir);
                absensi();
                list.setVisibility(View.GONE);
            }
        });

        String awal = tanggalawal.getText().toString();
        String akhir = tanggalakhir.getText().toString();
        System.out.println("hasil1 =" + awal);
        System.out.println("hasil2 =" + akhir);


        periodeedit.setText(awal + " - " + akhir);

        final DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
            private void updateLabel() {
                String myFormat = "dd-MMMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                tanggalawal.setText(sdf.format(myCalendar.getTime()));
            }
        };
        tanggalawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(kehadiran_team.this, datePickerDialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(DAY_OF_MONTH)).show();
            }
        });


        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
            private void updateLabel() {
                String myFormat = "dd-MMMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                tanggalakhir.setText(sdf.format(myCalendar2.getTime()));
                tanggalakhir.getText().toString();
            }
        };
        tanggalakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(kehadiran_team.this, date2, myCalendar2
                        .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(DAY_OF_MONTH)).show();
            }
        });

        movieItemList = new ArrayList<>();
        list = findViewById(R.id.list);

        absensi();

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
                            kehadiran_team.this);
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
                                    Intent intent = new Intent(kehadiran_team.this, MainActivity.class);
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

    private void absensi() {
        movieItemList.clear();
        pDialog = new ProgressDialog(kehadiran_team.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        String nik_baru = getIntent().getStringExtra(KEY_NIK);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/absensi/index?shift_day="+ convertFormat2(tanggalawal.getText().toString()) +"&shift_day_2=" + convertFormat2(tanggalakhir.getText().toString()) + "&badgenumber=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final keteranganmodel movieItem = new keteranganmodel(
                                        movieObject.getString("shift_day"),
                                        movieObject.getString("F1"),
                                        movieObject.getString("depo_f1"),
                                        movieObject.getString("F4"),
                                        movieObject.getString("depo_f4"),
                                        movieObject.getString("ket_absensi"),
                                        movieObject.getString("name"));
                                movieItemList.add(movieItem);

                            }
                            ListViewAdapter adapter = new ListViewAdapter(movieItemList, getApplicationContext());
                            list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                            Collections.sort(movieItemList, new Comparator<keteranganmodel>() {
                                public int compare(keteranganmodel o1, keteranganmodel o2) {
                                    if (o1.getShift_day() == null || o2.getShift_day() == null)
                                        return 0;
                                    return o2.getShift_day().compareTo(o1.getShift_day());
                                }
                            });
                            hideDialog();
                            list.setVisibility(View.VISIBLE);

                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        list.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Maaf, anda belum pernah absen", Toast.LENGTH_SHORT).show();
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

    public static class ListViewAdapter extends ArrayAdapter<keteranganmodel> {

        List<keteranganmodel> movieItemList;

        private Context context;

        public ListViewAdapter(List<keteranganmodel> movieItemList, Context context) {
            super(context, R.layout.list_item, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item, null, true);

            TextView date = listViewItem.findViewById(R.id.date);

            TextView depof1 = listViewItem.findViewById(R.id.depof1);
            TextView f1 = listViewItem.findViewById(R.id.f1);
            TextView depof4 = listViewItem.findViewById(R.id.depof4);
            TextView f4 = listViewItem.findViewById(R.id.f4);
            TextView telat = listViewItem.findViewById(R.id.telat);

            keteranganmodel movieItem = getItem(position);

            date.setText(convertFormat(movieItem.getShift_day()));
            depof1.setText(movieItem.getDepo_f1());
            f1.setText(tanggal(movieItem.getF1()));
            depof4.setText(movieItem.getDepo_f4());
            f4.setText(tanggal(movieItem.getF4()));
            telat.setText(movieItem.getKet_absensi());

            if ("null".equalsIgnoreCase(f1.getText().toString())) {
                f1.setBackgroundColor(Color.YELLOW);
            }
            if ("null".equalsIgnoreCase(depof1.getText().toString())) {
                depof1.setBackgroundColor(Color.YELLOW);
            }
            if ("null".equalsIgnoreCase(depof4.getText().toString())) {
                depof4.setBackgroundColor(Color.YELLOW);
            }
            if ("null".equalsIgnoreCase(f4.getText().toString())) {
                f4.setBackgroundColor(Color.YELLOW);
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

    public static String convertFormat2(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return convetDateFormat.format(date);
    }

    public static String tanggal(String inputDate) {
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
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