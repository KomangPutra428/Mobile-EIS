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
import android.media.Image;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.jadwalinterviewmodel;
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

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;
import static java.util.Calendar.DAY_OF_MONTH;

public class history_interview extends AppCompatActivity {

    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    private List<jadwalinterviewmodel> movieItemList;
    SearchableSpinner spinner;
    ListView list;
    ProgressDialog pDialog;
    EditText tanggal, tanggalakhir;
    ImageButton tambah, hapus, lihat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_interview);
        HttpsTrustManager.allowAllSSL();

        list = findViewById(R.id.list);
        movieItemList = new ArrayList<>();
        setNavigationDrawer();
        spinner = (SearchableSpinner) findViewById(R.id.jenisizin);

        tanggal = (EditText) findViewById(R.id.tanggal);
        tanggalakhir = (EditText) findViewById(R.id.tanggalakhir);

        tambah = (ImageButton) findViewById(R.id.tambah);
        hapus = (ImageButton) findViewById(R.id.hapus);
        lihat = (ImageButton) findViewById(R.id.lihat);

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

        String[] jenisizin = {"Disarankan", "Dipertimbangkan", "Tidak Disarankan", "Hangus", "Semua"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, jenisizin);
        spinner.setAdapter(adapter);

        int selectionPosition= adapter.getPosition("Dipertimbangkan");
        spinner.setSelection(selectionPosition);

        lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jadwalinterviewlist();
            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        final Calendar myCalendar2 = Calendar.getInstance();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        tanggal.setText(formattedDate);

        String untildate=tanggal.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd MMMM yyyy", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime( dateFormat.parse(untildate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add( Calendar.DATE, 7);
        String convertedDate=dateFormat.format(cal.getTime());
        tanggalakhir.setText(convertedDate);


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
                String myFormat = "dd MMMM yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                tanggal.setText(sdf.format(myCalendar.getTime()));
            }
        };
        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(history_interview.this, datePickerDialog, myCalendar
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
                String myFormat = "dd MMMM yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                tanggalakhir.setText(sdf.format(myCalendar2.getTime()));
                tanggalakhir.getText().toString();
            }
        };
        tanggalakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(history_interview.this, date2, myCalendar2
                        .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(DAY_OF_MONTH)).show();
            }
        });

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
        pDialog = new ProgressDialog(history_interview.this);
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
                                        movieObject.getString("id_interview"),
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
                                String nik_baru = sharedPreferences.getString(KEY_NIK ,null);

                                final ListViewAdapterInterview adapter = new ListViewAdapterInterview(movieItemList, getApplicationContext());
                                list.setAdapter(adapter);

                                if (!movieObject.getString("nama_pewawancara_1").equals(nik_baru) &&
                                        !movieObject.getString("nama_pewawancara_2").equals(nik_baru) &&
                                        !movieObject.getString("nama_pewawancara_").equals(nik_baru)){
                                    movieItemList.remove(movieItem);
                                    adapter.notifyDataSetChanged();
                                }

                                String dtStart = tanggal(movieObject.getString("tanggal_interview"));
                                SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                                Date date1 = format.parse(dtStart);

                                String dtend = tanggal.getText().toString();
                                Date date2 = format.parse(dtend);

                                String dtend2 = tanggalakhir.getText().toString();
                                Date date3 = format.parse(dtend2);

                                if(spinner.getSelectedItem().toString().equals("Disarankan")) {
                                    if (!movieObject.getString("kesimpulan").equals("1")) {
                                        movieItemList.remove(movieItem);
                                    }
                                }  if(spinner.getSelectedItem().toString().equals("Dipertimbangkan")) {
                                    if (!movieObject.getString("kesimpulan").equals("2")) {
                                        movieItemList.remove(movieItem);
                                    }
                                } if(spinner.getSelectedItem().toString().equals("Tidak Disarankan")) {
                                    if (!movieObject.getString("kesimpulan").equals("3")) {
                                        movieItemList.remove(movieItem);
                                    }
                                } if(spinner.getSelectedItem().toString().equals("Hangus")) {
                                    if (!movieObject.getString("kesimpulan").equals("4")) {
                                        movieItemList.remove(movieItem);
                                    }
                                } if(spinner.getSelectedItem().toString().equals("Semua")) {
                                    if (!movieItemList.contains(movieItem)) {
                                        movieItemList.add(movieItem);
                                    }
                                }

                                if(date2.after(date1)){
                                    movieItemList.remove(movieItem);
                                }

                                if(date3.before(date1)){
                                    movieItemList.remove(movieItem);
                                }



                                if (!movieObject.getString("status").equals("1")) {
                                    movieItemList.remove(movieItem);
                                    adapter.notifyDataSetChanged();
                                }

                                hideDialog();
                            }

                                Collections.sort(movieItemList, new Comparator<jadwalinterviewmodel>() {
                                    public int compare(jadwalinterviewmodel o1, jadwalinterviewmodel o2) {
                                        if (o2.getTanggal_interview() == null || o1.getTanggal_interview() == null)
                                            return 0;
                                        return o2.getTanggal_interview().compareTo(o1.getTanggal_interview());
                                    }
                                });
                            } catch(JSONException | ParseException e){
                                e.printStackTrace();
                            }
                        }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Maaf, anda belum ada riwayat interview", Toast.LENGTH_SHORT).show();
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_jadwalinterview, null, true);

            TextView tanggal_2 = listViewItem.findViewById(R.id.tanggal_2);
            TextView id2 = listViewItem.findViewById(R.id.id2);

            LinearLayout buttons = listViewItem.findViewById(R.id.buttons);

            final TextView posisi2 = listViewItem.findViewById(R.id.posisi2);
            TextView no_ktp2 = listViewItem.findViewById(R.id.no_ktp2);
            TextView nama_lengkap2 = listViewItem.findViewById(R.id.nama_lengkap2);

            TextView tanggal_interview2 = listViewItem.findViewById(R.id.tanggal_interview2);
            TextView jam_interview2 = listViewItem.findViewById(R.id.jam_interview2);
            TextView lokasi_interview2 = listViewItem.findViewById(R.id.lokasi_interview2);
            ImageView kesimpulan = listViewItem.findViewById(R.id.kesimpulan);
            ImageButton buttonubah = listViewItem.findViewById(R.id.buttonubah);
            ImageButton cetak = listViewItem.findViewById(R.id.cetak);
            final jadwalinterviewmodel movieItem = movieItemList.get(position);
            TextView text = listViewItem.findViewById(R.id.text);
            buttons.setVisibility(View.GONE);

            text.setText("Lampiran CV");

            tanggal_2.setText(movieItem.getTanggal_interview());
            id2.setText(movieItem.getId_interview());
            posisi2.setText(movieItem.getPosisi());
            no_ktp2.setText(movieItem.getNo_ktp());
            nama_lengkap2.setText(movieItem.getNama_lengkap());
            tanggal_interview2.setText(movieItem.getTanggal_interview());
            jam_interview2.setText(movieItem.getJam_interview());
            lokasi_interview2.setText(movieItem.getLokasi_interview());

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/jabatan/index?no_jabatan_karyawan="+ posisi2.getText().toString(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");

                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    posisi2.setText(movieObject.getString("jabatan_karyawan"));

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Maaf Ada Kesalahan", Toast.LENGTH_SHORT).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(history_interview.this);
            requestQueue.add(stringRequest);

            buttonubah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent (history_interview.this, form_interview_update.class);
                    String nik_baru = movieItem.getNo_ktp();
                    i.putExtra(KEY_NIK, nik_baru);
                    startActivity(i);
                }
            });

            cetak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://assessment.tvip.co.id/usr/cv/downloadcv/" + movieItem.getNo_ktp();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            if(movieItem.getKesimpulan().equals("1")){
                kesimpulan.setImageResource(R.drawable.btn_disarankan);
                buttonubah.setVisibility(View.GONE);
            }

            if(movieItem.getKesimpulan().equals("2")){
                kesimpulan.setImageResource(R.drawable.btn_dipertimbangkan);
            }

            if(movieItem.getKesimpulan().equals("3")){
                kesimpulan.setImageResource(R.drawable.btn_tidakdisarankan);
            }

            if(movieItem.getKesimpulan().equals("4")){
                kesimpulan.setImageResource(R.drawable.btn_hangus_interview);
                buttonubah.setVisibility(View.GONE);
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
                            history_interview.this);
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
                                    Intent intent = new Intent(history_interview.this, MainActivity.class);
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
        jadwalinterviewlist();
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