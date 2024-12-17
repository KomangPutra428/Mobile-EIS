package com.example.eis2;

import static com.example.eis2.Item.LoginItem.KEY_NIK;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.eis2.Item.Utility;
import com.example.eis2.Item.planmodels;
import com.example.eis2.Item.realizationmodels;
import com.example.eis2.Item.tanggalmodel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class realization_plan extends AppCompatActivity {
    ImageView filtering;
    ListView list_realization;
    List<tanggalmodel> tanggalmodels = new ArrayList<>();
    List<tanggalmodel> tanggalmodels_2 = new ArrayList<>();

    ListViewAdapterRealization adapterRealization;
    ListViewAdapterTodoList adapter;
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;
    private SimpleDateFormat dateFormatter;
    private Calendar date;
    String Tanggal, Tanggal2;
    Chip total;

    TextView tanggal_plan, tanggal_realisasi;
    MaterialToolbar dailynBar;
    NavigationView navigation;
    DrawerLayout drawer_layout;
    MaterialCardView realization, pilihstatus;

    ListView toDoList;

    String status;

    TabLayout tablayout;

    BottomSheetBehavior sheetBehavior;
    View bottom_sheet;
    BottomSheetDialog sheetDialog;

    TabItem summaryCount, toDoListCount;
    TextView pilihrealisasi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realization_plan);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Tanggal = sdf.format(new Date());
        Tanggal2 = sdf.format(new Date());
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        pilihrealisasi = findViewById(R.id.pilihrealisasi);
        total = findViewById(R.id.total);
        status = "Semua";


        HttpsTrustManager.allowAllSSL();

        list_realization = findViewById(R.id.list_realization);
        filtering = findViewById(R.id.filtering);
        pilihstatus = findViewById(R.id.pilihstatus);


        tanggal_realisasi = findViewById(R.id.tanggal_realisasi);

        dailynBar = findViewById(R.id.dailynBar);
        navigation = findViewById(R.id.navigation);
        drawer_layout = findViewById(R.id.drawer_layout);

        toDoList = findViewById(R.id.toDoList);
        realization = findViewById(R.id.realization);

        summaryCount = findViewById(R.id.summaryCount);
        toDoListCount = findViewById(R.id.toDoListCount);


        dailynBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(Gravity.LEFT);
            }
        });
        tablayout = findViewById(R.id.tablayout);

        pilihstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });


        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                if(position == 0){
                    toDoList.setVisibility(View.VISIBLE);
                    list_realization.setVisibility(View.GONE);
                    pilihstatus.setVisibility(View.GONE);
                } else {
                    toDoList.setVisibility(View.GONE);
                    list_realization.setVisibility(View.VISIBLE);
                    pilihstatus.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 0){
                    toDoList.setVisibility(View.VISIBLE);
                    list_realization.setVisibility(View.GONE);
                    pilihstatus.setVisibility(View.GONE);
                } else {
                    toDoList.setVisibility(View.GONE);
                    list_realization.setVisibility(View.VISIBLE);
                    pilihstatus.setVisibility(View.VISIBLE);
                }

            }
        });




        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
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
                            realization_plan.this);
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
                                    Intent intent = new Intent(realization_plan.this, MainActivity.class);
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
                    drawer_layout.closeDrawers(); // close the all open Drawer Views
                    return true;
                }
                return false;
            }
        });

        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        filtering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(realization_plan.this);
                dialog.setContentView(R.layout.pilih_tanggal);
                dialog.show();

                Button batal = dialog.findViewById(R.id.batal);
                Button ok = dialog.findViewById(R.id.ok);

                final EditText edittanggal = dialog.findViewById(R.id.edittanggal);
                final EditText editsampaitanggal = dialog.findViewById(R.id.editsampaitanggal);

                edittanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar currentDate = Calendar.getInstance();

                        date = currentDate.getInstance();

                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth);

                                edittanggal.setText(dateFormatter.format(date.getTime()));
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(realization_plan.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });

                editsampaitanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar currentDate = Calendar.getInstance();

                        date = currentDate.getInstance();

                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth);

                                editsampaitanggal.setText(dateFormatter.format(date.getTime()));
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(realization_plan.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edittanggal.getText().toString().length() == 0){
                            edittanggal.setError("Isi Tanggal");
                        } else {
                            dialog.dismiss();
                            Tanggal = tanggalhari(edittanggal.getText().toString());
                            Tanggal2 = tanggalhari(editsampaitanggal.getText().toString());
                            getDraft(Tanggal, Tanggal2);
                            getRealisasi(status);
                            getCountList(Tanggal, Tanggal2);
                        }
                    }
                });
            }
        });

    }

    private void getRealisasi(final String status) {
        list_realization.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        if(Tanggal2.equals("")){
            Tanggal2 = Tanggal;
        }
        tanggalmodels_2.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_per_range_status?nik_baru="+ nik_baru + "&tanggal="+ Tanggal + "&tanggal2=" + Tanggal2 + "&status=" + status,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        list_realization.setVisibility(View.VISIBLE);

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                tanggalmodel movieItem = new tanggalmodel(
                                        movieObject.getString("id"),
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("date"));
                                tanggalmodels_2.add(movieItem);

                                hideDialog();

                                adapterRealization = new ListViewAdapterRealization(tanggalmodels_2, getApplicationContext());
                                list_realization.setAdapter(adapterRealization);
                                adapterRealization.notifyDataSetChanged();




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
//        tanggal_realisasi.setText(convertFormatHari(tanggalhari));
//

    }

    private void getDraft(String tanggalhari, String tanggalselanjutnya) {
        pDialog = new ProgressDialog(realization_plan.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        tanggalmodels.clear();

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        if(Tanggal2.equals("")){
            Tanggal2 = Tanggal;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_per_range_status_not?nik_baru="+ nik_baru + "&tanggal="+ Tanggal + "&tanggal2=" + Tanggal2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                tanggalmodel movieItem = new tanggalmodel(
                                        movieObject.getString("id"),
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("date"));
                                tanggalmodels.add(movieItem);

                                hideDialog();

                                adapter = new ListViewAdapterTodoList(tanggalmodels, getApplicationContext());
                                toDoList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();




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

    public class ListViewAdapterTodoList extends ArrayAdapter<tanggalmodel> {

        private class ViewHolder {
            TextView tanggal_plan;
            ListView list_dailyactivity;
            List<planmodels> planmodelss = new ArrayList<>();
            ListViewAdapterDraft adapterDraft;
        }
        List<tanggalmodel> dataModels2;
        private Context context;

        public ListViewAdapterTodoList(List<tanggalmodel> dataModels2, Context context) {
            super(context, R.layout.list_todolist, dataModels2);
            this.dataModels2 = dataModels2;
            this.context = context;

        }

        public int getCount() {
            return dataModels2.size();
        }

        public tanggalmodel getItem(int position) {
            return dataModels2.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final tanggalmodel movieItem = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_todolist, parent, false);

                viewHolder.tanggal_plan = (TextView) convertView.findViewById(R.id.tanggal_plan);
                viewHolder.list_dailyactivity = (ListView) convertView.findViewById(R.id.list_dailyactivity);


                viewHolder.planmodelss = new ArrayList<>();
                convertView.setTag(viewHolder);

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_realization?nik_baru=" + nik_baru+ "&tanggal=" +  movieItem.getDate(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    JSONArray movieArray = obj.getJSONArray("data");

                                    int plan = 0;

                                    for (int i = 0; i < movieArray.length(); i++) {

                                        JSONObject movieObject = movieArray.getJSONObject(i);

                                        planmodels movieItem = new planmodels(
                                                movieObject.getString("id"),
                                                movieObject.getString("date"),
                                                movieObject.getString("start"),
                                                movieObject.getString("start_realisasi"),
                                                movieObject.getString("end"),
                                                movieObject.getString("end_realisasi"),
                                                movieObject.getString("ket_plan"),
                                                movieObject.getString("ket_realisasi"),
                                                movieObject.getString("status"),
                                                movieObject.getString("pengganti"),
                                                movieObject.getString("nama_kategori"),
                                                movieObject.getString("draft"));
                                        viewHolder.planmodelss.add(movieItem);

                                        hideDialog();


                                        viewHolder.adapterDraft = new ListViewAdapterDraft(viewHolder.planmodelss, getApplicationContext());
                                        viewHolder.list_dailyactivity.setAdapter(viewHolder.adapterDraft);
                                        viewHolder.adapterDraft.notifyDataSetChanged();

                                        if(movieObject.getString("draft").equals("0") || (movieObject.getString("draft").equals("2"))){
                                            viewHolder.adapterDraft.remove(movieItem);
                                            viewHolder.adapterDraft.notifyDataSetChanged();
                                        }
                                        Utility.setListViewHeightBasedOnChildren(viewHolder.list_dailyactivity);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                viewHolder.planmodelss.clear();

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
                RequestQueue requestQueue = Volley.newRequestQueue(realization_plan.this);
                requestQueue.add(stringRequest);


            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tanggal_plan.setText(convertFormat(movieItem.getDate()));

            viewHolder.list_dailyactivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String tanggal = ((planmodels) parent.getItemAtPosition(position)).getDate();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String tanggal1 = sdf.format(new Date());


                    SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d1 = null;
                    try {
                        d1 = sdformat.parse(tanggal);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Date d2 = null;
                    try {
                        d2 = sdformat.parse(tanggal1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -1);
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    System.out.println(cal.getTime());

                    String formatted = format1.format(cal.getTime());
                    System.out.println(formatted);

                    if (tanggal.equals(formatted)){
                        Intent i = new Intent(getBaseContext(), detail_daily.class);
                        String ids = ((planmodels) parent.getItemAtPosition(position)).getId();
                        i.putExtra("id", ids);
                        startActivity(i);
                    } else if(d1.compareTo(d2) < 0){
                        Toast.makeText(getApplicationContext(), "Plan Hari Kemarin Tidak Bisa Di Realisasi", Toast.LENGTH_SHORT).show();
                    } else if(d1.compareTo(d2) > 0) {
                        Toast.makeText(getApplicationContext(), "Plan Hari Besok Tidak Bisa Di Realisasi", Toast.LENGTH_SHORT).show();
                    } else if(d1.compareTo(d2) == 0){
                        Intent i = new Intent(getBaseContext(), detail_daily.class);
                        String ids = ((planmodels) parent.getItemAtPosition(position)).getId();
                        i.putExtra("id", ids);
                        startActivity(i);
                    }
                }
            });

            return convertView;

        }
    }

    public class ListViewAdapterDraft extends ArrayAdapter<planmodels> {

        private class ViewHolder {
            TextView jam, keterangan;
            ImageView edit;
        }
        List<planmodels> dataModels2;
        private Context context;

        public ListViewAdapterDraft(List<planmodels> dataModels2, Context context) {
            super(context, R.layout.list_item_plan, dataModels2);
            this.dataModels2 = dataModels2;
            this.context = context;

        }

        public int getCount() {
            return dataModels2.size();
        }

        public planmodels getItem(int position) {
            return dataModels2.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final planmodels movieItem = getItem(position);
            final ListViewAdapterDraft.ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ListViewAdapterDraft.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_plan, parent, false);

                viewHolder.jam = (TextView) convertView.findViewById(R.id.jam);
                viewHolder.keterangan = (TextView) convertView.findViewById(R.id.keterangan);

                viewHolder.edit = (ImageView) convertView.findViewById(R.id.edit);

                convertView.setTag(viewHolder);


            } else {
                viewHolder = (ListViewAdapterDraft.ViewHolder) convertView.getTag();
            }
            viewHolder.jam.setText(movieItem.getNama_kategori() + " • " + movieItem.getStart() + " - " + movieItem.getEnd());

            viewHolder.keterangan.setText(movieItem.getKet_plan());

            String tanggal = movieItem.getDate();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String tanggal1 = sdf.format(new Date());

            SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = null;
            try {
                d1 = sdformat.parse(tanggal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date d2 = null;
            try {
                d2 = sdformat.parse(tanggal1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println(cal.getTime());

            String formatted = format1.format(cal.getTime());
            System.out.println(formatted);

            if(formatted.equals(movieItem.getDate())) {
                viewHolder.edit.setVisibility(View.VISIBLE);
            } else if(d1.compareTo(d2) < 0) {
                viewHolder.edit.setVisibility(View.GONE);
            } else if(d1.compareTo(d2) > 0){
                viewHolder.edit.setVisibility(View.GONE);
            } else {
                viewHolder.edit.setVisibility(View.VISIBLE);
            }

            return convertView;

        }
    }


    public class ListViewAdapterRealization extends ArrayAdapter<tanggalmodel> {

        private class ViewHolder {
            TextView tanggal_realisasi;
            ListView list_realization;
            List<realizationmodels> planmodelsList;
            ListViewAdapterRealizationStatus adapterRealizationStatus;
            Chip total;
        }
        List<tanggalmodel> dataModels2;
        private Context context;

        public ListViewAdapterRealization(List<tanggalmodel> dataModels2, Context context) {
            super(context, R.layout.list_realization_done, dataModels2);
            this.dataModels2 = dataModels2;
            this.context = context;

        }

        public int getCount() {
            return dataModels2.size();
        }

        public tanggalmodel getItem(int position) {
            return dataModels2.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final tanggalmodel movieItem = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_realization_done, parent, false);

                viewHolder.tanggal_realisasi = (TextView) convertView.findViewById(R.id.tanggal_realisasi);
                viewHolder.list_realization = (ListView) convertView.findViewById(R.id.list_realization);
                viewHolder.planmodelsList = new ArrayList<>();
                viewHolder.total = (Chip) convertView.findViewById(R.id.total);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            viewHolder.tanggal_realisasi.setText(convertFormat(movieItem.getDate()));


            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            String nik_baru = sharedPreferences.getString(KEY_NIK, null);

            viewHolder.planmodelsList.clear();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_realization_plan?nik_baru=" + nik_baru+ "&tanggal=" +  movieItem.getDate() + "&status=" + status,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                int number = 0;
                                int number1 = 0;
                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    realizationmodels movieItem = new realizationmodels(
                                            movieObject.getString("id"),
                                            movieObject.getString("date"),
                                            movieObject.getString("start"),
                                            movieObject.getString("start_realisasi"),
                                            movieObject.getString("end"),
                                            movieObject.getString("end_realisasi"),
                                            movieObject.getString("ket_plan"),
                                            movieObject.getString("ket_realisasi"),
                                            movieObject.getString("status"),
                                            movieObject.getString("pengganti"),
                                            movieObject.getString("nama_kategori"));
                                    viewHolder.planmodelsList.add(movieItem);
                                    viewHolder.adapterRealizationStatus = new ListViewAdapterRealizationStatus(viewHolder.planmodelsList, getApplicationContext());
                                    viewHolder.list_realization.setAdapter(viewHolder.adapterRealizationStatus);
                                    Utility.setListViewHeightBasedOnChildren(viewHolder.list_realization);

                                    viewHolder.total.setText("Total = " + String.valueOf(viewHolder.planmodelsList.size()));


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
                            viewHolder.planmodelsList.clear();

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
            RequestQueue requestQueue = Volley.newRequestQueue(realization_plan.this);
            requestQueue.add(stringRequest);
            return convertView;

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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return convetDateFormat.format(date);
    }

    public static String convertFormatHari(String inputDate) {
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public static String tanggalhari(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
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

    @Override
    protected void onResume() {
        super.onResume();
        getCountList(Tanggal, Tanggal2);
        getRealisasi(status);
        getDraft(Tanggal, Tanggal2);
    }

    private void getCountList(String tanggal, String tanggal2) {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_count_plan?nik_baru="+ nik_baru + "&tanggal="+ Tanggal + "&tanggal2=" + Tanggal2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);

                                tablayout.getTabAt(0).setText("To Do List Plan " + movieObject.getString("COUNT(nik_baru)"));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tablayout.getTabAt(0).setText("To Do List Plan 0");
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_count_realization_plan?nik_baru="+ nik_baru + "&tanggal="+ Tanggal + "&tanggal2=" + Tanggal2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);

                                tablayout.getTabAt(1).setText("Summary " + movieObject.getString("COUNT(nik_baru)"));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tablayout.getTabAt(1).setText("Summary 0");
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
        stringRequest2.setRetryPolicy(
                new DefaultRetryPolicy(
                        7200000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest2);
    }

    private void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.pilih_status, null);

        ChipGroup pilihan = view.findViewById(R.id.pilihan);
        Button tampilkan = view.findViewById(R.id.tampilkan);

        pilihan.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);

                if (chip != null) {
                    status = chip.getText().toString();
                } else {

                }
            }
        });
        tampilkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_realization.setAdapter(null);
                getRealisasi(status);
                sheetDialog.dismiss();

                pilihrealisasi.setText("Pilih Realisasi : " + status);

            }
        });





        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        sheetDialog = new BottomSheetDialog(this);
        sheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            sheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        sheetDialog.show();
        sheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sheetDialog = null;
            }
        });
    }

    public static class ListViewAdapterRealizationStatus extends ArrayAdapter<realizationmodels> {

        public List<realizationmodels> planmodelsList;
        private Context context;

        public ListViewAdapterRealizationStatus(List<realizationmodels> planmodelsList, Context context) {
            super(context, R.layout.list_realization, planmodelsList);
            this.planmodelsList = planmodelsList;
            this.context = context;

        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_realization, null, true);

            TextView jam_plan = listViewItem.findViewById(R.id.jam_plan);
            TextView plan = listViewItem.findViewById(R.id.plan);
            TextView jam_result = listViewItem.findViewById(R.id.jam_result);
            TextView result = listViewItem.findViewById(R.id.result);
            TextView status = listViewItem.findViewById(R.id.status);
            TextView pengganti = listViewItem.findViewById(R.id.pengganti);

            MaterialCardView cardview = listViewItem.findViewById(R.id.cardview);
            MaterialCardView note = listViewItem.findViewById(R.id.note);

            LinearLayout pengganti_layout = listViewItem.findViewById(R.id.pengganti_layout);
            LinearLayout result_linear = listViewItem.findViewById(R.id.result_linear);
            LinearLayout pengganti_pekerjaan_layout = listViewItem.findViewById(R.id.pengganti_pekerjaan_layout);



            final realizationmodels movieItem = getItem(position);

            pengganti.setText(movieItem.getPengganti());
            jam_plan.setText(movieItem.getNama_kategori() + " • " + movieItem.getStart() + " - " + movieItem.getEnd());
            plan.setText(movieItem.getKet_plan());

            jam_result.setText("Result : " + movieItem.getStart_realisasi() + " - " + movieItem.getEnd_realisasi());
            result.setText(movieItem.getKet_realisasi());

            if(movieItem.getStatus().equals("2")){
                pengganti_pekerjaan_layout.setVisibility(View.VISIBLE);
                pengganti_layout.setVisibility(View.VISIBLE);
                status.setText("Tidak");
                status.setBackgroundColor(Color.parseColor("#FFF1F1"));
                status.setTextColor(Color.parseColor("#FB4141"));

                cardview.setStrokeColor(Color.parseColor("#FEC0C0"));
                result_linear.setBackgroundColor(Color.parseColor("#FFF1F1"));
                note.setStrokeColor(Color.parseColor("#FEC0C0"));
            }

            return listViewItem;

        }
    }
}