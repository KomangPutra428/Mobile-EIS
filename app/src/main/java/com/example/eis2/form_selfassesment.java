package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.example.eis2.Item.jawaban8model;
import com.example.eis2.Item.menuresignmodel;
import com.example.eis2.Item.soalcovidmodel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.eis2.Item.LoginItem.KEY_NIK;

public class form_selfassesment extends AppCompatActivity {
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;
    List<soalcovidmodel> soalcovidmodels = new ArrayList<>();
    List<jawaban8model> jawaban8models = new ArrayList<>();
    CardView linearcheckbox;

    ListView listsoal, checkboxes;

    Button simpan, batal;
    ListViewAdapterSoalCovid adapter;
    ListViewAdapterJawabanCovid adapter2;

    TextView keterangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_selfassesment);
        HttpsTrustManager.allowAllSSL();

        simpan = findViewById(R.id.simpan);
        batal = findViewById(R.id.batal);
        listsoal = findViewById(R.id.listsoal);
        keterangan = findViewById(R.id.keterangan);
        checkboxes = findViewById(R.id.checkboxes);
        linearcheckbox = findViewById(R.id.linearcheckbox);
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

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = soalcovidmodels.size() - 1;
                int c = jawaban8models.size() - 1;
                for (int i = 0; i <= x; i++) {
                    if (adapter.getItem(i).getIdjawaban() == null) {
                        Toast.makeText(getApplicationContext(), "Silahkan pilih jawaban terlebih dahulu", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (x == i) {
                        for (int b = 0; b <= c; b++) {
                            System.out.println("Hasil Looping 2 = " + c);

                            if (adapter2.getItem(b).getSelected() == true) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        postassessment();
                                    }
                                }, 5000);
                                pDialog = new ProgressDialog(form_selfassesment.this);
                                showDialog();
                                pDialog.setContentView(R.layout.progress_dialog);
                                pDialog.getWindow().setBackgroundDrawableResource(
                                        android.R.color.transparent
                                );
                                pDialog.setCancelable(false);


                                break;
                            } else {
                                if (b == c) {
                                    Toast.makeText(getApplicationContext(), "Silahkan pilih jawaban terlebih dahulu", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }
                }

//                            }

//                            System.out.println("Hasil Looping = " + i);

//                                }


            }

        });


        setNavigationDrawer();

        pDialog = new ProgressDialog(form_selfassesment.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/assessment/index_soal",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;



                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final soalcovidmodel movieItem = new soalcovidmodel(
                                        movieObject.getString("no_pertanyaan"),
                                        movieObject.getString("pertanyaan"));

                                soalcovidmodels.add(movieItem);

                                if(movieObject.getString("no_pertanyaan").equals("8")){
                                    soalcovidmodels.remove(movieItem);
                                    keterangan.setText(movieObject.getString("pertanyaan"));
                                }


                                adapter = new ListViewAdapterSoalCovid(soalcovidmodels, getApplicationContext());
                                listsoal.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }


                            hideDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/assessment/index_jawaban?id=8",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;



                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final jawaban8model movieItem = new jawaban8model(
                                        movieObject.getString("id"),
                                        movieObject.getString("jawaban"));

                                jawaban8models.add(movieItem);

                                adapter2 = new ListViewAdapterJawabanCovid(jawaban8models, getApplicationContext());
                                checkboxes.setAdapter(adapter2);
                                adapter2.notifyDataSetChanged();
                                Utility.setListViewHeightBasedOnChildren(checkboxes);

                            }


                            hideDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        linearcheckbox.setVisibility(GONE);
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest2);
    }

    private void postassessment() {

        final int x = soalcovidmodels.size() - 1;

        System.out.println("jumlah jawaban = " + x);
        for (int i = 0; i <= x; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final int finalI = i;

            final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/Assessment/index",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (finalI == x) {
                                postassessment2();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

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

                    params.put("nik_baru", nik_baru);
                    params.put("pertanyaan", adapter.getItem(finalI).getIdpertanyaan());
                    params.put("id_jawaban", adapter.getItem(finalI).getIdjawaban());

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

            RequestQueue requestQueue2 = Volley.newRequestQueue(getBaseContext());
            requestQueue2.add(stringRequest2);
        }
    }

    private void postassessment2() {
        final int x = jawaban8models.size() - 1;

        for (int i = 0; i <= x; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final int finalI = i;
            if(adapter2.getItem(i).getSelected() == false){
                if (finalI == x) {
                    hideDialog();
                    Intent intent = new Intent(getBaseContext(), hasil_assessment.class);
                    startActivity(intent);
                }
            } else {
                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/Assessment/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (finalI == x) {
                                    hideDialog();
                                    Intent intent = new Intent(getBaseContext(), hasil_assessment.class);
                                    startActivity(intent);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

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

                        params.put("nik_baru", nik_baru);
                        params.put("pertanyaan", "8");
                        params.put("id_jawaban", adapter2.getItem(finalI).getId());

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

                RequestQueue requestQueue2 = Volley.newRequestQueue(getBaseContext());
                requestQueue2.add(stringRequest2);
            }


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
                            form_selfassesment.this);
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
                                    Intent intent = new Intent(form_selfassesment.this, MainActivity.class);
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
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public class ListViewAdapterSoalCovid extends ArrayAdapter<soalcovidmodel> {

        private class ViewHolder {
            TextView keterangan;
            RadioGroup jawaban;
            LinearLayout linearcheckbox;

        }
        List<soalcovidmodel> dataModels;
        private Context context;

        public ListViewAdapterSoalCovid(List<soalcovidmodel> dataModels, Context context) {
            super(context, R.layout.list_soal_covi, dataModels);
            this.dataModels = dataModels;
            this.context = context;

        }

        public int getCount() {
            return dataModels.size();
        }

        public soalcovidmodel getItem(int position) {
            return dataModels.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final soalcovidmodel movieItem = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_soal_covi, parent, false);

                viewHolder.keterangan = (TextView) convertView.findViewById(R.id.keterangan);
                viewHolder.jawaban = (RadioGroup) convertView.findViewById(R.id.jawaban);
                viewHolder.linearcheckbox = (LinearLayout) convertView.findViewById(R.id.linearcheckbox);



                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/assessment/index_jawaban?id="+ movieItem.getNo_pertanyaan(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    JSONArray movieArray = obj.getJSONArray("data");

                                    for (int i = 0; i < movieArray.length(); i++) {

                                        final JSONObject movieObject = movieArray.getJSONObject(i);

                                        final RadioButton radioButton = new RadioButton(getBaseContext());
                                        radioButton.setText(movieObject.getString("jawaban"));
                                        final String msg = movieObject.getString("id");

                                        if(movieItem.getNo_pertanyaan().equals("8")){
                                            final CheckBox checkBox = new CheckBox(getBaseContext());
                                            checkBox.setText(movieObject.getString("jawaban"));
                                            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                            checkBox.setTag(position);
                                            checkBox.setChecked(dataModels.get(position).getSelected());
                                            viewHolder.linearcheckbox.addView(checkBox);

                                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if(checkBox.isChecked()) {
                                                        dataModels.get(position).setSelected(true);
                                                        getItem(position).setIdjawaban(msg);
                                                        getItem(position).setIdpertanyaan(getItem(position).getNo_pertanyaan());
                                                        getItem(position).setSelected(true);
                                                        dataModels.add(movieItem);
                                                        dataModels.remove(position);

                                                        notifyDataSetChanged();
                                                        for (int i = 0; i < dataModels.size(); i++) {
                                                            Toast.makeText(getApplicationContext(), "Hasil = " + adapter.getItem(i).getIdjawaban(), Toast.LENGTH_SHORT).show();

                                                        }


                                                        if(dataModels.get(position).getSelected()){
                                                            dataModels.get(position).setSelected(false);
                                                        }
                                                    }

                                                }
                                            });


                                        } else {
                                            viewHolder.linearcheckbox.setVisibility(GONE);

                                            viewHolder.jawaban.addView(radioButton);
                                            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if(radioButton.isChecked()) {
                                                        try {
                                                            dataModels.get(position).setSelected(true);
                                                            getItem(position).setIdjawaban(movieObject.getString("id"));
                                                            getItem(position).setIdpertanyaan(getItem(position).getNo_pertanyaan());
                                                            adapter.notifyDataSetChanged();

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }


                                                    }
                                                }
                                            });
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

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
                RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                requestQueue.add(stringRequest);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.keterangan.setText(movieItem.getPertanyaan());


            return convertView;

        }
    }

    public class ListViewAdapterJawabanCovid extends ArrayAdapter<jawaban8model> {

        private class ViewHolder {
            CheckBox checkbox;

        }
        List<jawaban8model> dataModels;
        private Context context;
        SparseBooleanArray mCheckStates;


        public ListViewAdapterJawabanCovid(List<jawaban8model> dataModels, Context context) {
            super(context, R.layout.list_checkboxes, dataModels);
            this.dataModels = dataModels;
            this.context = context;
            mCheckStates = new SparseBooleanArray(dataModels.size());


        }

        public int getCount() {
            return dataModels.size();
        }

        public jawaban8model getItem(int position) {
            return dataModels.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final jawaban8model movieItem = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_checkboxes, parent, false);

                viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.checkbox.setText(movieItem.getJawaban());

            viewHolder.checkbox.setTag(position);
            viewHolder.checkbox.setChecked(mCheckStates.get(position, false));

            viewHolder.checkbox.setChecked(dataModels.get(position).getSelected());
            viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (dataModels.get(position).getSelected()){
                        dataModels.get(position).setSelected(false);
                    }
                    else {
                        dataModels.get(position).setSelected(true);
                    }

                }
            });


            return convertView;

        }
    }
}