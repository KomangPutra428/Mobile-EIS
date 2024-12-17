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
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.izinnonfullmodel;
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

public class tabel_izin_non_full_day extends AppCompatActivity {
    EditText tanggal, tanggalakhir;
    ImageButton lihat, tambah, hapus;
    ListView list;
    private List<izinnonfullmodel> IzinNonFullList;
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;
    DrawerLayout dLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabel_izin_non_full_day);
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
        lihat = (ImageButton) findViewById(R.id.lihat);
        hapus = (ImageButton) findViewById(R.id.hapus);
        tambah = (ImageButton) findViewById(R.id.tambah);
        setNavigationDrawer();
        tanggal = (EditText) findViewById(R.id.tanggal);
        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar date;
                final SimpleDateFormat dateFormatter;
                dateFormatter = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());

                final Calendar currentDate = Calendar.getInstance();

                date = currentDate.getInstance();

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);

                        tanggal.setText(dateFormatter.format(date.getTime()));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(tabel_izin_non_full_day.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

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

        tanggalakhir = (EditText) findViewById(R.id.tanggalakhir);
        tanggalakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar date;
                final SimpleDateFormat dateFormatter;
                dateFormatter = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());

                final Calendar currentDate = Calendar.getInstance();

                date = currentDate.getInstance();

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);

                        tanggalakhir.setText(dateFormatter.format(date.getTime()));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(tabel_izin_non_full_day.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        list = findViewById(R.id.list);
        IzinNonFullList = new ArrayList<>();

        lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tanggal.getText().toString().length() == 0){
                    tanggal.setError("Masukkan Tanggal");
                } else {
                    loadPlayer();
                }

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
                            tabel_izin_non_full_day.this);
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
                                    Intent intent = new Intent(tabel_izin_non_full_day.this, MainActivity.class);
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

    private void loadPlayer() {
        IzinNonFullList.clear();
        pDialog = new ProgressDialog(tabel_izin_non_full_day.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/izin_non_full_day/index?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                izinnonfullmodel movieItem = new izinnonfullmodel(
                                        movieObject.getString("tanggal_deadline"),
                                        movieObject.getString("no_pengajuan_non_full"),
                                        movieObject.getString("jenis_non_full"),
                                        movieObject.getString("tanggal_absen"),
                                        movieObject.getString("ket_tambahan_non_full"),
                                        movieObject.getString("status_non_full"),
                                        movieObject.getString("tanggal_approval_non_full"),
                                        movieObject.getString("feedback_non_full"),
                                        movieObject.getString("status_non_full_2"),
                                        movieObject.getString("tanggal_approval_non_full_2"),
                                        movieObject.getString("feedback_non_full_2"));

                                IzinNonFullList.add(movieItem);

                                hideDialog();


                                ListViewAdapterIzinNonFull adapter = new ListViewAdapterIzinNonFull(IzinNonFullList, getApplicationContext());

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

                                if(dtend2 == null){
                                    if(!date2.equals(date1)){
                                        IzinNonFullList.remove(movieItem);
                                    }
                                } else {
                                    if(date2.after(date1)){
                                        IzinNonFullList.remove(movieItem);
                                    }
                                    if(date3.before(date1)){
                                        IzinNonFullList.remove(movieItem);
                                    }

                                }
                                list.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                Collections.sort(IzinNonFullList, new Comparator<izinnonfullmodel>() {
                                    public int compare(izinnonfullmodel o1, izinnonfullmodel o2) {
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

    public static class ListViewAdapterIzinNonFull extends ArrayAdapter<izinnonfullmodel> {
        private List<izinnonfullmodel> izinnonlist;

        private Context context;

        public ListViewAdapterIzinNonFull(List<izinnonfullmodel> izinnonlist, Context context) {
            super(context, R.layout.list_item_rekapabsensi, izinnonlist);
            this.izinnonlist = izinnonlist;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_nonfull, null, true);

            TextView tanggalid = listViewItem.findViewById(R.id.tanggalid);
            TextView id = listViewItem.findViewById(R.id.id);
            TextView jenis_izin = listViewItem.findViewById(R.id.jenis_izin);
            TextView tidakhadir = listViewItem.findViewById(R.id.tidakhadir);

            TextView deadline = listViewItem.findViewById(R.id.deadline);
            TextView karyawan = listViewItem.findViewById(R.id.karyawan);
            karyawan.setVisibility(View.GONE);
            TextView keterangan = listViewItem.findViewById(R.id.keterangan);
            TextView feedback1 = listViewItem.findViewById(R.id.feedback1);
            TextView karyawab = listViewItem.findViewById(R.id.karyawab);
            TextView titikdua5 = listViewItem.findViewById(R.id.titikdua5);


            TextView feedback2 = listViewItem.findViewById(R.id.feedback2);
            ImageView approval1 = listViewItem.findViewById(R.id.approval1);
            ImageView approval2 = listViewItem.findViewById(R.id.approval2);
            LinearLayout karyawalinear = listViewItem.findViewById(R.id.karyawalinear);

            karyawab.setVisibility(View.GONE);
            karyawan.setVisibility(View.GONE);
            karyawalinear.setVisibility(View.GONE);
            titikdua5.setVisibility(View.GONE);

            izinnonfullmodel movieItem = izinnonlist.get(position);

            tanggalid.setText(convertFormat(movieItem.getTanggal_absen()));
            id.setText(movieItem.getNo_pengajuan_non_full());
            jenis_izin.setText(movieItem.getJenis_non_full());
            tidakhadir.setText(convertFormat(movieItem.getTanggal_absen()));

            deadline.setText(convertFormat2(movieItem.getTanggal_deadline()));
//            karyawan.setText("Karyawan Pengganti = " + izin.getKaryawan_pengganti());
            keterangan.setText(movieItem.getKet_tambahan_non_full());

            feedback1.setText(movieItem.getFeedback_non_full());
            feedback2.setText(movieItem.getFeedback_non_full_2());

            if (movieItem.getStatus_non_full().equals("0")) {
                approval1.setImageResource(R.drawable.btn_open);
            } else if (movieItem.getStatus_non_full().equals("1")) {
                approval1.setImageResource(R.drawable.btn_aprv);
            } else if (movieItem.getStatus_non_full().equals("2")) {
                approval1.setImageResource(R.drawable.btn_notaprv);
            } else if (movieItem.getStatus_non_full().equals("3")) {
                approval1.setImageResource(R.drawable.btn_hangus);
            }

            if (movieItem.getStatus_non_full_2().equals("0")) {
                approval2.setImageResource(R.drawable.btn_open);
            } else if (movieItem.getStatus_non_full_2().equals("1")) {
                approval2.setImageResource(R.drawable.btn_aprv);
            } else if (movieItem.getStatus_non_full_2().equals("2")) {
                approval2.setImageResource(R.drawable.btn_notaprv);
            } else if (movieItem.getStatus_non_full_2().equals("3")) {
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

    public static String tanggal(String inputDate) {
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
