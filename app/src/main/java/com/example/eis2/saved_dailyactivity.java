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
import com.example.eis2.Item.activityitem;
import com.example.eis2.Item.keteranganlistmodel;
import com.example.eis2.Item.planmodels2;
import com.example.eis2.Item.tanggalmodel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class saved_dailyactivity extends AppCompatActivity {
    ListView list_dailyactivity;
    List<tanggalmodel> tanggalmodels = new ArrayList<>();
    ListViewAdapterDraft adapterDraft;
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;
    ImageView filtering;
    private SimpleDateFormat dateFormatter;
    private Calendar date;
    String Tanggal, Tanggal2;
    TextView tanggal;
    MaterialToolbar dailynBar;
    NavigationView navigation;
    DrawerLayout drawer_layout;
    Button copy;
    MaterialCardView linearbutton;
    List<String> kategori = new ArrayList<>();
    List<String> ket_plan = new ArrayList<>();

    List<String> start = new ArrayList<>();
    List<String> end = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_dailyactivity);
        HttpsTrustManager.allowAllSSL();
        linearbutton = findViewById(R.id.linearbutton);
        copy = findViewById(R.id.copy);
        filtering = findViewById(R.id.filtering);
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Tanggal = sdf.format(new Date());
        Tanggal2 = sdf.format(new Date());
        tanggal = findViewById(R.id.tanggal);

        dailynBar = findViewById(R.id.dailynBar);
        navigation = findViewById(R.id.navigation);
        drawer_layout = findViewById(R.id.drawer_layout);

        dailynBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(Gravity.LEFT);
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
                            saved_dailyactivity.this);
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
                                    Intent intent = new Intent(saved_dailyactivity.this, MainActivity.class);
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

        tanggal.setText(tanggalhari(Tanggal));
        list_dailyactivity = findViewById(R.id.list_dailyactivity);
        filtering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(saved_dailyactivity.this);
                dialog.setContentView(R.layout.pilih_tanggal_range);
                dialog.show();


                Button batal = dialog.findViewById(R.id.batal);
                Button ok = dialog.findViewById(R.id.ok);

                final EditText edittanggal = dialog.findViewById(R.id.edittanggal);
                final EditText edittanggalsampai = dialog.findViewById(R.id.edittanggalsampai);

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
                        DatePickerDialog datePickerDialog = new DatePickerDialog(saved_dailyactivity.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });

                edittanggalsampai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar currentDate = Calendar.getInstance();

                        date = currentDate.getInstance();

                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth);

                                edittanggalsampai.setText(dateFormatter.format(date.getTime()));
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(saved_dailyactivity.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
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
                            Tanggal = tanggal(edittanggal.getText().toString());
                            Tanggal2 = tanggal(edittanggalsampai.getText().toString());

                            getDraft(Tanggal, Tanggal2);
                            list_dailyactivity.setVisibility(View.GONE);
                            if(edittanggalsampai.getText().toString().length() == 0){
                                tanggal.setText(tanggalhari(Tanggal));
                            } else if(Tanggal.equals(Tanggal2)){
                                tanggal.setText(tanggalhari(Tanggal));
                            } else {
                                tanggal.setText(tanggalhari(Tanggal) + " - " + tanggalhari(Tanggal2));
                            }
                        }
                    }
                });
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ket_plan.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Silahkan pilih checklist terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else {
                    postCopy();
                }
            }
        });

    }

    private void postCopy() {
        pDialog = new ProgressDialog(saved_dailyactivity.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideDialog();
                kategori.clear();
                ket_plan.clear();
                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                finish();
            }
        }, 3000);
        for (int e = 0; e <= kategori.size(); e++) {
            final int finalI1 = e;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_Daily",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

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
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDateandTime = sdf.format(new Date());

                    params.put("nik_baru", nik_baru);
                    params.put("jabatan", menu.text_jabatan.getText().toString());
                    params.put("date", currentDateandTime);
                    params.put("kategori", kategori.get(finalI1).toString());
                    params.put("start", "");
                    params.put("end", "");
                    params.put("ket_plan", ket_plan.get(finalI1).toString());

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(saved_dailyactivity.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void getDraft(String tanggal, String tanggal2) {
        pDialog = new ProgressDialog(saved_dailyactivity.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        tanggalmodels.clear();

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        if(tanggal2.equals("")){
            tanggal2 = tanggal;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_per_range?nik_baru="+ nik_baru + "&tanggal="+ tanggal + "&tanggal2=" + tanggal2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        list_dailyactivity.setVisibility(View.VISIBLE);
                        linearbutton.setVisibility(View.VISIBLE);
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


                                adapterDraft = new ListViewAdapterDraft(tanggalmodels, getApplicationContext());
                                list_dailyactivity.setAdapter(adapterDraft);
                                adapterDraft.notifyDataSetChanged();

//

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
                        linearbutton.setVisibility(View.GONE);
//                        check3.setText("Checklist All" + " (Jumlah Data = 0)");
//                        hapus.setVisibility(View.GONE);
//                        Simpan.setVisibility(View.GONE);

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
    public class ListViewAdapterDraft extends ArrayAdapter<tanggalmodel> {

        private class ViewHolder {
            TextView tanggal_date;
            ListView listdata;
            List<planmodels2>planmodelsList;
            ListViewAdapterKeteranganDraft adapterKeteranganDraft;
            MaterialButton hapus;
            CheckBox checkdetail;
        }
        List<tanggalmodel> dataModels2;
        private Context context;

        public ListViewAdapterDraft(List<tanggalmodel> dataModels2, Context context) {
            super(context, R.layout.saved_daily, dataModels2);
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
                convertView = inflater.inflate(R.layout.saved_daily, parent, false);

                viewHolder.tanggal_date = (TextView) convertView.findViewById(R.id.tanggal_date);
                viewHolder.listdata = (ListView) convertView.findViewById(R.id.listdata);
                viewHolder.planmodelsList= new ArrayList<>();

                viewHolder.hapus = (MaterialButton) convertView.findViewById(R.id.hapus);
                viewHolder.hapus.setVisibility(View.GONE);
                viewHolder.checkdetail = (CheckBox) convertView.findViewById(R.id.checkdetail);



                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tanggal_date.setText(convertFormat(movieItem.getDate()));

            viewHolder.planmodelsList.clear();

            viewHolder.checkdetail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(b) {
                        for(planmodels2 person:viewHolder.planmodelsList){
                            person.setSelected(true);
                            viewHolder.adapterKeteranganDraft.notifyDataSetChanged();
                        }
                    } else if (!b) {
                        viewHolder.checkdetail.setChecked(false);
                        for(planmodels2 person:viewHolder.planmodelsList){
                            person.setSelected(false);
                            viewHolder.adapterKeteranganDraft.notifyDataSetChanged();
                        }
                    }
                }
            });

            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            String nik_baru = sharedPreferences.getString(KEY_NIK, null);
            System.out.println("Link = " + "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_tanggal_nik2?nik_baru=" + nik_baru+ "&tanggal=" +  movieItem.getDate());

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/DailyActivity_sales/index_tanggal_nik2?nik_baru=" + nik_baru+ "&tanggal=" +  movieItem.getDate(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");

                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    planmodels2 movieItem = new planmodels2(
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
                                            movieObject.getString("draft"),
                                            movieObject.getString("kategori"));
                                    viewHolder.planmodelsList.add(movieItem);

                                    viewHolder.adapterKeteranganDraft = new ListViewAdapterDraft.ListViewAdapterKeteranganDraft(viewHolder.planmodelsList, getApplicationContext());
                                    viewHolder.listdata.setAdapter(viewHolder.adapterKeteranganDraft);
                                    viewHolder.adapterKeteranganDraft.notifyDataSetChanged();
                                    Utility.setListViewHeightBasedOnChildren(viewHolder.listdata);
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
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            RequestQueue requestQueue = Volley.newRequestQueue(saved_dailyactivity.this);
            requestQueue.add(stringRequest);

            viewHolder.listdata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    String status = ((planmodels2) parent.getItemAtPosition(position)).getStatus();
                    String tanggal = ((planmodels2) parent.getItemAtPosition(position)).getDate();

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

                    if(((planmodels2) parent.getItemAtPosition(position)).getDate().equals(tanggal1)){
                        Toast.makeText(getApplicationContext(), "Plan Hari Ini Tidak Bisa Di Edit", Toast.LENGTH_SHORT).show();
                    } else if(d1.compareTo(d2) < 0) {
                        Toast.makeText(getApplicationContext(), "Plan Hari Kemarin Tidak Bisa Di Edit", Toast.LENGTH_SHORT).show();
                    } else if(status.equals("1")){
                        Toast.makeText(getApplicationContext(), "Plan Sudah Terealisasi", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("2")){
                        Toast.makeText(getApplicationContext(), "Plan Tidak Terealisasi", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(saved_dailyactivity.this, update_plan.class);
                        String ID = ((planmodels2) parent.getItemAtPosition(position)).getId();
                        i.putExtra("ID", ID);
                        startActivity(i);
                    }

                }
            });




            return convertView;

        }

        public class ListViewAdapterKeteranganDraft extends ArrayAdapter<planmodels2> implements CompoundButton.OnCheckedChangeListener {


            private class ViewHolder {
                TextView jam, keterangan;
                CheckBox checkdetail;
                ImageView edit;

            }
            SparseBooleanArray mCheckStates;
            List<planmodels2> dataModels;
            private Context context;
            private boolean Selected = false;

            public ListViewAdapterKeteranganDraft(List<planmodels2> dataModels, Context context) {
                super(context, R.layout.list_item_draft_keterangan, dataModels);
                this.dataModels = dataModels;
                this.context = context;
                mCheckStates = new SparseBooleanArray(dataModels.size());
            }
            public boolean isSelected() {
                return Selected;
            }

            public void setSelected(boolean selected) {
                Selected = selected;
            }

            public int getCount() {
                return dataModels.size();
            }

            public planmodels2 getItem(int position) {
                return dataModels.get(position);
            }

            public long getItemId(int position) {
                return 0;
            }

            @SuppressLint("NewApi")
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final planmodels2 movieItem = getItem(position);
                final ViewHolder viewHolder;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.list_item_draft_keterangan, parent, false);

                    viewHolder.keterangan = (TextView) convertView.findViewById(R.id.keterangan);
                    viewHolder.jam = (TextView) convertView.findViewById(R.id.jam);
                    viewHolder.checkdetail = (CheckBox) convertView.findViewById(R.id.checkdetail);
                    viewHolder.edit = (ImageView) convertView.findViewById(R.id.edit);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

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

                if(tanggal.equals(tanggal1)){
                    viewHolder.edit.setVisibility(View.GONE);
                } else if(d1.compareTo(d2) < 0) {
                    viewHolder.edit.setVisibility(View.GONE);
                } else if(!movieItem.getStatus().equals("0")){
                    viewHolder.edit.setVisibility(View.GONE);
                } else {
                    viewHolder.edit.setVisibility(View.VISIBLE);
                }
                viewHolder.jam.setText(movieItem.getNama_kategori() + " • " + movieItem.getStart() + " - " + movieItem.getEnd());
                viewHolder.keterangan.setText(movieItem.getKet_plan());

                viewHolder.checkdetail.setTag(position);
                viewHolder.checkdetail.setOnCheckedChangeListener(this);
                viewHolder.checkdetail.setChecked(mCheckStates.get(position, false));

                viewHolder.checkdetail.setChecked(dataModels.get(position).getSelected());

                viewHolder.checkdetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (movieItem.getSelected() == true) {
                            movieItem.setSelected(false);
                        } else {
                            movieItem.setSelected(true);
                        }
                    }
                });

                if(viewHolder.checkdetail.isChecked()){
                    kategori.add(movieItem.getKategori());
                    ket_plan.add(movieItem.getKet_plan());
                    start.add(movieItem.getStart());
                    end.add(movieItem.getEnd());
                } else {
                    kategori.remove(movieItem.getKategori());
                    ket_plan.remove(movieItem.getKet_plan());
                    start.remove(movieItem.getStart());
                    end.remove(movieItem.getEnd());
                }

                viewHolder.checkdetail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(b) {
                            kategori.add(movieItem.getKategori());
                            ket_plan.add(movieItem.getKet_plan());
                            start.add(movieItem.getStart());
                            end.add(movieItem.getEnd());
                        } else if (!b) {
                            kategori.remove(movieItem.getKategori());
                            ket_plan.remove(movieItem.getKet_plan());
                            start.remove(movieItem.getStart());
                            end.remove(movieItem.getEnd());
                        }
                    }
                });



                return convertView;

            }
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCheckStates.put((Integer) buttonView.getTag(), isChecked);

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

    public static String convertFormatReverse(String inputDate) {
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

    public static String tanggal(String inputDate) {
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

    public static String tanggalhari(String inputDate) {
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        list_dailyactivity.setVisibility(View.GONE);
        getDraft(Tanggal, Tanggal2);
    }
}