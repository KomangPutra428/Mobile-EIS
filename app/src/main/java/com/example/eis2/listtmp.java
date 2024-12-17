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
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.example.eis2.Item.absensiteammodel;
import com.example.eis2.Item.activityitem;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE;
import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_nik;

public class listtmp extends AppCompatActivity {
    List<activityitem> activityitemList;
    ListView list;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    DrawerLayout dLayout;
    ListViewAdapterActivity adapter;
    ImageButton tambah;
    Button Simpan, hapus, copy;
    CheckBox check3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listtmp);
        HttpsTrustManager.allowAllSSL();

        setNavigationDrawer();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        Simpan = findViewById(R.id.Simpan);
        tambah = findViewById(R.id.tambah);
        check3 = findViewById(R.id.check);
        hapus = findViewById(R.id.hapus);


//        check3.setVisibility(View.GONE);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });
        list = findViewById(R.id.list);
//        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }


    private void getData() {
        pDialog = new ProgressDialog(listtmp.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        activityitemList.clear();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Logactivity/index_gettmp?nik=" + nik_baru ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                activityitem movieItem = new activityitem(
                                        movieObject.getString("id"),
                                        movieObject.getString("submit_date"),
                                        movieObject.getString("nik"),
                                        movieObject.getString("status_lokasi"),
                                        movieObject.getString("lokasi"),
                                        movieObject.getString("keterangan"),
                                        movieObject.getString("lat"),
                                        movieObject.getString("lon"));
                                activityitemList.add(movieItem);


                                hideDialog();


                                adapter = new ListViewAdapterActivity(activityitemList, getApplicationContext());

                                list.setAdapter(adapter);
                                check3.setText("Checklist All" + " (Jumlah Data = " + adapter.getCount() + ")");
                                adapter.notifyDataSetChanged();

                                Collections.sort(activityitemList, new Comparator<activityitem>() {
                                    public int compare(activityitem o1, activityitem o2) {
                                        if (o2.getSubmit_date() == null || o1.getSubmit_date() == null)
                                            return 0;
                                        return o2.getSubmit_date().compareTo(o1.getSubmit_date());
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
                        check3.setText("Checklist All" + " (Jumlah Data = 0)");
                        hapus.setVisibility(View.GONE);
                        Simpan.setVisibility(View.GONE);

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
                            listtmp.this);
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
                                    Intent intent = new Intent(listtmp.this, MainActivity.class);
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

//    public class ListViewAdapterActivity extends ArrayAdapter<activityitem> {
//
//        private List<activityitem> activityitemList;
//
//        private Context context;
//
//        public ListViewAdapterActivity(List<activityitem> activityitemList, Context context) {
//            super(context, R.layout.list_item_activity, activityitemList);
//            this.activityitemList = activityitemList;
//            this.context = context;
//        }
//
//        @SuppressLint("NewApi")
//        @Override
//        public View getView (final int position, View convertView, ViewGroup parent){
//
//            LayoutInflater inflater = LayoutInflater.from(context);
//
//            View listViewItem = inflater.inflate(R.layout.list_item_activity, null, true);
//

//
//
//            activityitem movieItem = getItem(position);
//
//            tanggalid.setText(convertFormat2(movieItem.getSubmit_date()));
//            nik.setText(movieItem.getNik());
//            status_lokasi.setText(movieItem.getStatus_lokasi());
//            lokasi.setText(movieItem.getLokasi());
//            keterangan.setText(movieItem.getKeterangan());
//

//
//
//
//            return listViewItem;
//        }
//    }

    public class ListViewAdapterActivity extends ArrayAdapter<activityitem> implements CompoundButton.OnCheckedChangeListener {

        private class ViewHolder {
            CheckBox checkbox;

            TextView nik, status_lokasi, lokasi, keterangan, tanggalid;
            Button pilih;
            ArrayList<String> keterangan2;
        }
        SparseBooleanArray mCheckStates;

        List<activityitem> activityitemList;
        private Context context;
        private boolean Selected = false;

        public ListViewAdapterActivity(List<activityitem> movieItemList, Context context) {
            super(context, R.layout.list_item_activity, movieItemList);
            this.activityitemList = movieItemList;
            this.context = context;
            mCheckStates = new SparseBooleanArray(activityitemList.size());
        }
        public boolean isSelected() {
            return Selected;
        }

        public void setSelected(boolean selected) {
            Selected = selected;
        }

        public int getCount() {
            return activityitemList.size();
        }

        public activityitem getItem(int position) {
            return activityitemList.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final activityitem movieItem = getItem(position);

            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_activity, parent, false);

                viewHolder.tanggalid = (TextView) convertView.findViewById(R.id.tanggalid);
                viewHolder.nik = (TextView) convertView.findViewById(R.id.nik);
                viewHolder.status_lokasi = (TextView) convertView.findViewById(R.id.status_lokasi);
                viewHolder.lokasi = (TextView) convertView.findViewById(R.id.lokasi);
                viewHolder.keterangan = (TextView) convertView.findViewById(R.id.keterangan);

                viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
                viewHolder.pilih = (Button) convertView.findViewById(R.id.pilih);
                convertView.setTag(viewHolder);


            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if ("0".equalsIgnoreCase(movieItem.getStatus_lokasi())) {
                viewHolder.status_lokasi.setText("Eksternal");
            }
            if ("1".equalsIgnoreCase(movieItem.getStatus_lokasi())) {
                viewHolder.status_lokasi.setText("Internal");
            }

            viewHolder.checkbox.setTag(position);
            viewHolder.checkbox.setOnCheckedChangeListener(this);
            viewHolder.checkbox.setChecked(mCheckStates.get(position, false));

            viewHolder.checkbox.setChecked(activityitemList.get(position).getSelected());
            viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (activityitemList.get(position).getSelected()){
                        activityitemList.get(position).setSelected(false);
                    }
                    else {
                        activityitemList.get(position).setSelected(true);
                    }

                }
            });


            check3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                        for(activityitem person:activityitemList){
                            person.setSelected(true);
                        }
                        adapter.notifyDataSetChanged();
                    } else if (!b) {
                        for(activityitem person:activityitemList){
                            person.setSelected(false);
                        }
                        adapter.notifyDataSetChanged();
                    }

                }
            });

            Simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int i = 0; i < activityitemList.size(); i++) {
                        if (getItem(i).getSelected() == true) {
                            pDialog = new ProgressDialog(listtmp.this);
                            showDialog();
                            pDialog.setContentView(R.layout.progress_dialog);
                            pDialog.getWindow().setBackgroundDrawableResource(
                                    android.R.color.transparent
                            );
                            int itemCount = list.getCount();
                            for (int e = itemCount - 1; e >= 0; e--) {
                                if (!adapter.mCheckStates.get(e) == true) {
                                    adapter.remove(activityitemList.get(e));
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            final Handler handler = new Handler();
                            final Runnable r = new Runnable() {
                                public void run() {
                                    listpostout();
                                }
                            };
                            handler.postDelayed(r, 200);
                            break;
                        } else {
                            if (activityitemList.size() - 1 == i) {
                                Toast.makeText(getApplicationContext(), "Silahkan checklist terlebih dahulu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            }
            });

            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        for (int i = 0; i < activityitemList.size(); i++) {
                            if (getItem(i).getSelected() == true) {
                                pDialog = new ProgressDialog(listtmp.this);
                                showDialog();
                                pDialog.setContentView(R.layout.progress_dialog);
                                pDialog.getWindow().setBackgroundDrawableResource(
                                        android.R.color.transparent
                                );
                                int itemCount = list.getCount();
                                for (int e = itemCount - 1; e >= 0; e--) {
                                    if (!adapter.mCheckStates.get(e) == true) {
                                        adapter.remove(activityitemList.get(e));
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                                final Handler handler = new Handler();
                                final Runnable r = new Runnable() {
                                    public void run() {
                                        final int x = activityitemList.size() - 1;
                                        for (int i = 0; i <= x; i++) {
                                            try {
                                                Thread.sleep(600);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            final int finalI = i;
                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/hapusperid.php",
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            if (finalI == x) {
                                                                hideDialog();
                                                                Toast.makeText(getApplicationContext(), "Data Sudah Dihapus", Toast.LENGTH_LONG).show();
                                                                getData();

                                                            }

                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }) {

                                                @Override
                                                public Map<String, String> getHeaders() throws AuthFailureError {
                                                    HashMap<String, String> params = new HashMap<String, String>();
                                                    String creds = String.format("%s:%s", "admin", "Databa53");
                                                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                                                    params.put("Authorization", auth);

                                                    return params;
                                                }

                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<String, String>();
                                                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                                                    params.put("id", adapter.getItem(finalI).getId());

                                                    return params;
                                                }

                                            };
                                            stringRequest2.setRetryPolicy(
                                                    new DefaultRetryPolicy(
                                                            500000,
                                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                    )
                                            );

                                            RequestQueue requestQueue2 = Volley.newRequestQueue(listtmp.this);
                                            requestQueue2.add(stringRequest2);
                                        }
                                    }
                                };
                                handler.postDelayed(r, 200);
                                break;
                            } else {
                                if (activityitemList.size() - 1 == i) {
                                    Toast.makeText(getApplicationContext(), "Silahkan checklist terlebih dahulu", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
            });



            viewHolder.tanggalid.setText(convertFormat2(movieItem.getSubmit_date()));
            viewHolder.nik.setText(movieItem.getNik());
            viewHolder.lokasi.setText(movieItem.getLokasi());
            viewHolder.keterangan.setText(movieItem.getKeterangan());




                viewHolder.pilih.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(listtmp.this, detail_activity.class);
                    String nik_baru = adapter.getItem(position).getId();
                    i.putExtra(KEY_NIK, nik_baru);
                    startActivity(i);
                    System.out.println("nomor =" + nik_baru);
                }
            });



            return convertView;
        }


        public boolean isChecked(int position) {
            return mCheckStates.get(position, false);
        }

        public void setChecked(int position, boolean isChecked) {
            mCheckStates.put(position, isChecked);

        }

        public void toggle(int position) {
            setChecked(position, !isChecked(position));

        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {

            mCheckStates.put((Integer) buttonView.getTag(), isChecked);

        }
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
    private void showDialog () {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog () {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void listpostout() {
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
            }
        };
        handler.postDelayed(r, 200);

        final int x = activityitemList.size() - 1;
        for (int i = 0; i <= x; i++) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final int finalI = i;
            final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/Logactivity/index_real",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (finalI == x) {
                                hapus();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Maaf Ada Kesalahan", Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", "admin", "Databa53");
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                    String nik_baru = sharedPreferences.getString(KEY_NIK, null);

                    params.put("nik", nik_baru);
                    params.put("waktu_submit", adapter.getItem(finalI).getSubmit_date());
                    params.put("status_lokasi", adapter.getItem(finalI).getStatus_lokasi());
                    params.put("lokasi", adapter.getItem(finalI).getLokasi());
                    params.put("keterangan", adapter.getItem(finalI).getKeterangan());
                    params.put("lat", adapter.getItem(finalI).getLat());
                    params.put("lon", adapter.getItem(finalI).getLon());

                    return params;
                }
            };
            stringRequest2.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );

            RequestQueue requestQueue2 = Volley.newRequestQueue(listtmp.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void hapus() {
        final int x = activityitemList.size() - 1;
        for (int i = 0; i <= x; i++) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final int finalI = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/hapusperid.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (finalI == x) {
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "Data Terikirim", Toast.LENGTH_LONG).show();
                                getData();
//                                adapter.clear();
//                                getData();
//                                if(check3.isChecked()){
//                                    check3.setChecked(false);
//                                }
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", "admin", "Databa53");
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);

                    return params;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                    params.put("id", adapter.getItem(finalI).getId());

                    return params;
                }

            };
            stringRequest2.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );

            RequestQueue requestQueue2 = Volley.newRequestQueue(listtmp.this);
            requestQueue2.add(stringRequest2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityitemList = new ArrayList<>();
        getData();
    }

    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

        
        super.onDestroy();
    }
}