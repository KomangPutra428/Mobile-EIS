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
import com.example.eis2.Item.cutitahunanmodel;
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

public class tabel_cuti_tahunan extends AppCompatActivity {

    EditText tanggal, tanggalakhir;
    ImageButton lihat, tambah, hapus;
    ProgressDialog pDialog;

    ListView list;
    private List<cutitahunanmodel> CutiTahunanList;
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabel_cuti_tahunan);
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
        tambah = (ImageButton) findViewById(R.id.tambah);
        hapus = (ImageButton) findViewById(R.id.hapus);

        setNavigationDrawer();


        list = findViewById(R.id.list);
        CutiTahunanList = new ArrayList<>();

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


        tanggal = (EditText) findViewById(R.id.tanggal);
        lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tanggal.getText().toString().length() == 0){
                    tanggal.setError("Masukkan Tanggal");
                } else {
                    listcutitahunan();
                }

            }
        });
        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar date;
                final SimpleDateFormat dateFormatter;
                dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

                final Calendar currentDate = Calendar.getInstance();

                date = currentDate.getInstance();

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);

                        tanggal.setText(dateFormatter.format(date.getTime()));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(tabel_cuti_tahunan.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


        tanggalakhir = (EditText) findViewById(R.id.tanggalakhir);
        tanggalakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar date;
                final SimpleDateFormat dateFormatter;
                dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

                final Calendar currentDate = Calendar.getInstance();

                date = currentDate.getInstance();

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);

                        tanggalakhir.setText(dateFormatter.format(date.getTime()));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(tabel_cuti_tahunan.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
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
                            tabel_cuti_tahunan.this);
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
                                    Intent intent = new Intent(tabel_cuti_tahunan.this, MainActivity.class);
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

    private void listcutitahunan() {
        CutiTahunanList.clear();
        pDialog = new ProgressDialog(tabel_cuti_tahunan.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/cuti_tahunan/index?nik_baru=" + nik_baru ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                cutitahunanmodel movieItem = new cutitahunanmodel(
                                        movieObject.getString("tanggal_deadline"),
                                        movieObject.getString("id_sisa_cuti"),
                                        movieObject.getString("tanggal_absen"),
                                        movieObject.getString("opsi_cuti_tahunan"),
                                        movieObject.getString("ket_tambahan_tahunan"),
                                        movieObject.getString("status_cuti_tahunan"),
                                        movieObject.getString("tanggal_cuti_tahunan"),
                                        movieObject.getString("feedback_cuti_tahunan"),
                                        movieObject.getString("status_cuti_tahunan_2"),
                                        movieObject.getString("tanggal_cuti_tahunan_2"),
                                        movieObject.getString("feedback_cuti_tahunan_2"));

                                CutiTahunanList.add(movieItem);

                                hideDialog();
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
                                        CutiTahunanList.remove(movieItem);
                                    }
                                } else {
                                    if(date2.after(date1)){
                                        CutiTahunanList.remove(movieItem);
                                    }
                                    if(date3.before(date1)){
                                        CutiTahunanList.remove(movieItem);
                                    }

                                }

                                ListViewAdapterCutiTahunan adapter = new ListViewAdapterCutiTahunan(CutiTahunanList, getApplicationContext());

                                list.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                Collections.sort(CutiTahunanList, new Comparator<cutitahunanmodel>() {
                                    public int compare(cutitahunanmodel o1, cutitahunanmodel o2) {
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
                        Toast.makeText(getApplicationContext(), "Maaf, anda belum pernah mengajukan cuti khusus", Toast.LENGTH_SHORT).show();
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

    public class ListViewAdapterCutiTahunan extends ArrayAdapter<cutitahunanmodel> {

        private List<cutitahunanmodel> CutiTahunanList;

        private Context context;

        public ListViewAdapterCutiTahunan(List<cutitahunanmodel> CutiTahunanList, Context context) {
            super(context, R.layout.list_item_cuti_tahunan, CutiTahunanList);
            this.CutiTahunanList = CutiTahunanList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView ( final int position, View convertView, ViewGroup parent){

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_cuti_tahunan, null, true);

            TextView tanggal_2 = listViewItem.findViewById(R.id.tanggalid);
            TextView id = listViewItem.findViewById(R.id.id);

            TextView tidakhadir = listViewItem.findViewById(R.id.tidakhadir);
            TextView deadline = listViewItem.findViewById(R.id.deadline);
            TextView condition = listViewItem.findViewById(R.id.condition);

            TextView keterangan = listViewItem.findViewById(R.id.keterangan);
            TextView feedback1 = listViewItem.findViewById(R.id.feedback1);
            TextView feedback2 = listViewItem.findViewById(R.id.feedback2);

            ImageView approval1 = listViewItem.findViewById(R.id.approval1);
            ImageView approval2 = listViewItem.findViewById(R.id.approval2);



            cutitahunanmodel movieItem = CutiTahunanList.get(position);

            tanggal_2.setText(convertFormat(movieItem.getTanggal_absen()));
            id.setText(movieItem.getId_sisa_cuti());
            tidakhadir.setText(convertFormat(movieItem.getTanggal_absen()));
            deadline.setText(convertFormat2(movieItem.getTanggal_deadline()));
            condition.setText(movieItem.getOpsi_cuti_tahunan());
            keterangan.setText(movieItem.getKet_tambahan_tahunan());
            feedback1.setText(movieItem.getFeedback_cuti_tahunan());
            feedback2.setText(movieItem.getFeedback_cuti_tahunan_2());



            if ("0".equalsIgnoreCase(movieItem.getStatus_cuti_tahunan())) {
                approval1.setImageResource(R.drawable.btn_open);
            }
            if ("1".equalsIgnoreCase(movieItem.getStatus_cuti_tahunan())) {
                approval1.setImageResource(R.drawable.btn_aprv);
            }
            if ("2".equalsIgnoreCase(movieItem.getStatus_cuti_tahunan())) {
                approval1.setImageResource(R.drawable.btn_notaprv);
            }
            if ("3".equalsIgnoreCase(movieItem.getStatus_cuti_tahunan())) {
                approval1.setImageResource(R.drawable.btn_hangus);
            }

            if ("0".equalsIgnoreCase(movieItem.getStatus_cuti_tahunan_2())) {
                approval2.setImageResource(R.drawable.btn_open);
            }
            if ("1".equalsIgnoreCase(movieItem.getStatus_cuti_tahunan_2())) {
                approval1.setImageResource(R.drawable.btn_aprv);
            }
            if ("2".equalsIgnoreCase(movieItem.getStatus_cuti_tahunan_2())) {
                approval1.setImageResource(R.drawable.btn_notaprv);
            }
            if ("3".equalsIgnoreCase(movieItem.getStatus_cuti_tahunan_2())) {
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
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