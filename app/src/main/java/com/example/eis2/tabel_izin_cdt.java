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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.eis2.Item.CDTmodel;
import com.example.eis2.Item.Restarter;
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
import java.util.concurrent.atomic.AtomicLong;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;
import static java.util.Calendar.DAY_OF_MONTH;

public class tabel_izin_cdt extends AppCompatActivity {
    SearchableSpinner spinner;
    EditText tanggal, tanggalakhir;
    ImageButton lihat, tambah, hapus;
    ProgressDialog pDialog;
    DrawerLayout dLayout;

    ListView list;
    private List<CDTmodel> CdtList;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabel_izin_cdt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        tambah = (ImageButton) findViewById(R.id.tambah);
        hapus = (ImageButton) findViewById(R.id.hapus);
        setNavigationDrawer();
        tanggal = (EditText) findViewById(R.id.tanggal);
        tanggalakhir = (EditText) findViewById(R.id.tanggalakhir);


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

        final Calendar myCalendar = Calendar.getInstance();
        final Calendar myCalendar2 = Calendar.getInstance();

        String[] jenisizin = {"Sakit", "CDT", "Semua"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, jenisizin);
        spinner.setAdapter(adapter);

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
                tanggal.setText(sdf.format(myCalendar.getTime()));
            }
        };
        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(tabel_izin_cdt.this, datePickerDialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(DAY_OF_MONTH)).show();
            }
        });

        AtomicLong mLastClickTime = new AtomicLong();
        spinner.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                spinner.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    spinner.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            spinner.onTouch(v,event);
            spinner.setEnabled(false);

            return true;
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
            }
        };
        tanggalakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(tabel_izin_cdt.this, date2, myCalendar2
                        .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(DAY_OF_MONTH)).show();
            }
        });

        lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tanggal.getText().toString().length() == 0){
                    tanggal.setError("Masukkan Tanggal");
                } else {
                    izinfull();
                }

            }
        });

        list = findViewById(R.id.list);
        CdtList = new ArrayList<>();
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
                            tabel_izin_cdt.this);
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
                                    Intent intent = new Intent(tabel_izin_cdt.this, MainActivity.class);
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

    private void izinfull() {
        CdtList.clear();
        pDialog = new ProgressDialog(tabel_izin_cdt.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK ,null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/izin_full_day/index?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                CDTmodel cdt = new CDTmodel(
                                        movieObject.getString("id_full_day"),
                                        movieObject.getString("no_pengajuan_full_day"),
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("tanggal_deadline"),
                                        movieObject.getString("lokasi_struktur"),
                                        movieObject.getString("jenis_full_day"),
                                        movieObject.getString("tanggal_absen"),
                                        movieObject.getString("karyawan_pengganti"),
                                        movieObject.getString("ket_tambahan"),
                                        movieObject.getString("upload_full_day"),
                                        movieObject.getString("status_full_day"),
                                        movieObject.getString("status_full_day_2"),
                                        movieObject.getString("feedback_full_day"),
                                        movieObject.getString("feedback_full_day_2"));

                                CdtList.add(cdt);


                                ListViewAdapterFullday adapter = new ListViewAdapterFullday(CdtList, getApplicationContext());

                                String dtend2;
                                String dtStart = movieObject.getString("tanggal_absen");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                Date date1 = format.parse(dtStart);

                                String dtend = tanggal(tanggal.getText().toString());
                                Date date2 = format.parse(dtend);

                                if(tanggalakhir.getText().toString().length() == 0){
                                    dtend2 = tanggal(tanggal.getText().toString());
                                } else {
                                    dtend2 = tanggal(tanggalakhir.getText().toString());
                                }

                                Date date3 = format.parse(dtend2);

                                System.out.println("hasil1 = " + date1);
                                System.out.println("hasil2 = " + date2);
                                System.out.println("hasil3 = " + date3);

                                System.out.println("Tanggal = " + dtend2);


                                if(spinner.getSelectedItem().toString().equals("Sakit")) {
                                    if (!movieObject.getString("jenis_full_day").equals("SA")) {
                                        CdtList.remove(cdt);
                                        hideDialog();
                                    }
                                } if(spinner.getSelectedItem().toString().equals("CDT")) {
                                    if (!movieObject.getString("jenis_full_day").equals("CD")) {
                                        hideDialog();
                                        CdtList.remove(cdt);
                                    }
                                } if(spinner.getSelectedItem().toString().equals("Semua")) {
                                    if (!CdtList.contains(cdt)) {
                                        CdtList.add(cdt);
                                    }
                                }

                                hideDialog();

                                if(dtend2 == null){
                                    if(!date2.equals(date1)){
                                        CdtList.remove(cdt);
                                    }
                                } else {
                                    if(date2.after(date1)){
                                        CdtList.remove(cdt);
                                    }

                                    if(date3.before(date1)){
                                        CdtList.remove(cdt);
                                    }

                                }

                                list.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                Collections.sort(CdtList, new Comparator<CDTmodel>() {
                                    public int compare(CDTmodel o1, CDTmodel o2) {
                                        if (o2.getTanggal_absen() == null || o1.getTanggal_absen() == null)
                                            return 0;
                                        return o2.getTanggal_absen().compareTo(o1.getTanggal_absen());
                                    }
                                });
                            }

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "Maaf, anda belum pernah mengajukan izin full day", Toast.LENGTH_SHORT).show();
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


    public class ListViewAdapterFullday extends ArrayAdapter<CDTmodel> {
        private List<CDTmodel> izinlist;

        private Context context;

        public ListViewAdapterFullday(List<CDTmodel> izinlist, Context context) {
            super(context, R.layout.list_item_rekapabsensi, izinlist);
            this.izinlist = izinlist;
            this.context = context;
        }
        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_izin_cdt, null, true);

            TextView tanggalid = listViewItem.findViewById(R.id.tanggalid);
            TextView id = listViewItem.findViewById(R.id.id);
            TextView jenis_izin = listViewItem.findViewById(R.id.jenis_izin);
            TextView tidakhadir = listViewItem.findViewById(R.id.tidakhadir);

            TextView deadline = listViewItem.findViewById(R.id.deadline);
            TextView karyawan = listViewItem.findViewById(R.id.karyawan);
            TextView keterangan = listViewItem.findViewById(R.id.keterangan);
            TextView feedback1 = listViewItem.findViewById(R.id.feedback1);

            TextView feedback2 = listViewItem.findViewById(R.id.feedback2);
            ImageView approval1 = listViewItem.findViewById(R.id.approval1);
            ImageView approval2 = listViewItem.findViewById(R.id.approval2);

            CDTmodel izin = izinlist.get(position);


            tanggalid.setText(convertFormat(izin.getTanggal_absen()));
            id.setText(izin.getId_full_day());
            jenis_izin.setText(izin.getJenis_full_day());
            tidakhadir.setText(convertFormat(izin.getTanggal_absen()));

            deadline.setText(convertFormat2(izin.getTanggal_deadline()));
            karyawan.setText(izin.getKaryawan_pengganti());
            keterangan.setText(izin.getKet_tambahan());

            feedback1.setText(izin.getFeedback_full_day());
            feedback2.setText(izin.getFeedback_full_day_2());

            if(izin.getStatus_full_day().equals("0")){
                approval1.setImageResource(R.drawable.btn_open);
            } else if(izin.getStatus_full_day().equals("1")){
                approval1.setImageResource(R.drawable.btn_aprv);
            } else if(izin.getStatus_full_day().equals("2")){
                approval1.setImageResource(R.drawable.btn_notaprv);
            } else if(izin.getStatus_full_day().equals("3")){
                approval1.setImageResource(R.drawable.btn_hangus);
            }

            if(izin.getStatus_full_day_2().equals("0")){
                approval2.setImageResource(R.drawable.btn_open);
            } else if(izin.getStatus_full_day_2().equals("1")){
                approval2.setImageResource(R.drawable.btn_aprv);
            } else if(izin.getStatus_full_day_2().equals("2")){
                approval2.setImageResource(R.drawable.btn_notaprv);
            } else if(izin.getStatus_full_day_2().equals("3")){
                approval2.setImageResource(R.drawable.btn_hangus);
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

    public static String convertFormat2(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm:ss");
        return convetDateFormat.format(date);
    }

    private static String tanggal(String inputDate) {
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