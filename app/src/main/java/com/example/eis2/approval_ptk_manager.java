package com.example.eis2;

import static android.view.View.GONE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.eis2.Item.ptkManagerModel;
import com.example.eis2.daily_activity_snd.weekly_planner_new;
import com.google.android.material.navigation.NavigationView;
import com.example.eis2.SearchSpinner.SearchableSpinner;

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
import java.util.concurrent.atomic.AtomicLong;

public class approval_ptk_manager extends AppCompatActivity {

    ListView list;
    List<ptkManagerModel> ptkmodelList = new ArrayList<>();
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;

    String tanggal1, tanggal2;
    DrawerLayout dLayout;

    SearchableSpinner status;
    ImageButton lihat, filtering;

    String URL;

    private Calendar date;
    private SimpleDateFormat dateFormatter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_ptk_manager);
        HttpsTrustManager.allowAllSSL();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        status = findViewById(R.id.status);
        lihat = findViewById(R.id.lihat);
        filtering = findViewById(R.id.filtering);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) findViewById(R.id.navigation); // initiate a Navigation View
        View headerView = navView.getHeaderView(0);
        final TextView hari = headerView.findViewById(R.id.hari);
        final TextView kondisi = headerView.findViewById(R.id.kondisi);
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // Set to the first day of the week (Monday)

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tanggal1 = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 6); // 6 days after Monday is Sunday
        tanggal2 = dateFormat.format(calendar.getTime());


        AtomicLong mLastClickTime = new AtomicLong();

        status.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                status.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    status.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            status.onTouch(v,event);
            status.setEnabled(false);

            return true;
        });

        filtering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preventTwoClick(v);
                final Dialog dialog = new Dialog(approval_ptk_manager.this);
                dialog.setContentView(R.layout.filter_range);

                EditText edit_tanggal = dialog.findViewById(R.id.edit_tanggal);
                EditText sampai_tanggal = dialog.findViewById(R.id.sampai_tanggal);

                Button batal = dialog.findViewById(R.id.batal);
                Button ok = dialog.findViewById(R.id.ok);

                final Calendar currentDate = Calendar.getInstance();
                Calendar twoDaysAgo = (Calendar) currentDate.clone();
                twoDaysAgo.add(Calendar.DATE, 0);

                date = Calendar.getInstance();

                edit_tanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventTwoClick(v);
                        final Calendar currentDate = Calendar.getInstance();
                        Calendar twoDaysAgo = (Calendar) currentDate.clone();

                        date = Calendar.getInstance();

                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth );

                                edit_tanggal.setText(dateFormatter.format(date.getTime()));
                            }
                        };
                        DatePickerDialog datePickerDialog = new  DatePickerDialog(approval_ptk_manager.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(twoDaysAgo.getTimeInMillis());
                        datePickerDialog.show();


                    }
                });

                sampai_tanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventTwoClick(v);
                        final Calendar currentDate = Calendar.getInstance();
                        Calendar twoDaysAgo = (Calendar) currentDate.clone();

                        date = Calendar.getInstance();

                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth );

                                sampai_tanggal.setText(dateFormatter.format(date.getTime()));
                            }
                        };
                        DatePickerDialog datePickerDialog = new  DatePickerDialog(approval_ptk_manager.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(twoDaysAgo.getTimeInMillis());
                        datePickerDialog.show();


                    }
                });


                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventTwoClick(v);
                        dialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventTwoClick(v);

                        if(edit_tanggal.getText().toString().length() == 0){
                            edit_tanggal.setError("Wajib Di isi");
                        } else if(sampai_tanggal.getText().toString().length() == 0){
                            sampai_tanggal.setError("Wajib Di isi");
                        } else {
                            dialog.dismiss();

                            tanggal1 = convertFormat2(edit_tanggal.getText().toString());
                            tanggal2 = convertFormat2(sampai_tanggal.getText().toString());

                            if(status.getSelectedItem().toString().equals("Open")){
                                cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Ptk/index_manager?date1=" + tanggal1 + "&date2=" +tanggal2);
                            } else if(status.getSelectedItem().toString().equals("Approved")){
                                cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Ptk/index_manager_approval?status_manager=1&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
                            } else  if(status.getSelectedItem().toString().equals("Not Approved")){
                                cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Ptk/index_manager_approval?status_manager=2&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
                            }

                        }
                    }
                });



                dialog.show();
            }
        });



        lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lihat.setEnabled(false);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        lihat.setEnabled(true);
                    }
                },1500);

                if(status.getSelectedItem().toString().equals("Open")){
                    cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Ptk/index_manager?date1=" + tanggal1 + "&date2=" +tanggal2);
                } else if(status.getSelectedItem().toString().equals("Approved")){
                    cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Ptk/index_manager_approval?status_manager=1&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
                } else  if(status.getSelectedItem().toString().equals("Not Approved")){
                    cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Ptk/index_manager_approval?status_manager=2&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
                }
            }
        });


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
                            approval_ptk_manager.this);
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
                                    Intent intent = new Intent(approval_ptk_manager.this, MainActivity.class);
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });

        list = findViewById(R.id.list);


    }

    private void cekdata(String URL) {
        ptkmodelList.clear();
        pDialog = new ProgressDialog(approval_ptk_manager.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        list.setVisibility(View.VISIBLE);

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                ptkManagerModel cdt = new ptkManagerModel(
                                        movieObject.getString("no_pengajuan"),
                                        movieObject.getString("submit_date"),
                                        movieObject.getString("nik_pengajuan"),
                                        movieObject.getString("nama_jabatan"),
                                        movieObject.getString("depo_ptk"),
                                        movieObject.getString("dept_ptk"),
                                        movieObject.getString("analisa"),
                                        movieObject.getString("tenaga_kerja"),
                                        movieObject.getString("status_atasan"),
                                        movieObject.getString("status_hrd"),
                                        movieObject.getString("status_manager"),
                                        movieObject.getString("tanggal_manager"),
                                        movieObject.getString("no_ptk"));

                                ptkmodelList.add(cdt);


                                hideDialog();
                                ListViewAdapterPTK adapter = new ListViewAdapterPTK(ptkmodelList, getApplicationContext());
                                list.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                System.out.println("Status = " + status.getSelectedItem().toString());



                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                        if(status.getSelectedItem().toString().equals("Open")){
                                            Intent i = new Intent(approval_ptk_manager.this, form_ptk_manager.class);
                                            String ids = ((ptkManagerModel) parent.getItemAtPosition(position)).getId();
                                            String analisa = ((ptkManagerModel) parent.getItemAtPosition(position)).getAnalisa();
                                            String jabatan_karyawan = ((ptkManagerModel) parent.getItemAtPosition(position)).getJabatan_karyawan();
                                            String tenaga_kerja = ((ptkManagerModel) parent.getItemAtPosition(position)).getTenaga_kerja();

                                            i.putExtra("id_pengajuan", ids);
                                            i.putExtra("analisa", analisa);
                                            i.putExtra("jabatan_karyawan", jabatan_karyawan);
                                            i.putExtra("tenaga_kerja", tenaga_kerja);

                                            startActivity(i);
                                        }

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
                        list.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Maaf, Data Belum Ada", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(approval_ptk_manager.this);
        requestQueue.add(stringRequest);
    }

    public class ListViewAdapterPTK extends ArrayAdapter<ptkManagerModel> {
        private List<ptkManagerModel> ptkmodelList;

        private Context context;

        public ListViewAdapterPTK(List<ptkManagerModel> ptkmodelList, Context context) {
            super(context, R.layout.list_item_ptk_manager, ptkmodelList);
            this.ptkmodelList = ptkmodelList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_ptk_manager, null, true);

            TextView tanggal_2 = listViewItem.findViewById(R.id.tanggal_2);
            TextView jabatan = listViewItem.findViewById(R.id.jabatan);
            TextView nomor = listViewItem.findViewById(R.id.nomor);
            TextView depo = listViewItem.findViewById(R.id.depo);

            TextView depart = listViewItem.findViewById(R.id.depart);
            TextView analisa = listViewItem.findViewById(R.id.analisa);
            TextView ptk = listViewItem.findViewById(R.id.ptk);
            TextView tanggal_manager = listViewItem.findViewById(R.id.tanggal_manager);
            TextView nomor_ptk = listViewItem.findViewById(R.id.nomor_ptk);

            ImageView atasan = listViewItem.findViewById(R.id.atasan);
            ImageView hrd = listViewItem.findViewById(R.id.hrd);
            ImageView manager = listViewItem.findViewById(R.id.manager);

            LinearLayout layout_tanggal = listViewItem.findViewById(R.id.layout_tanggal);
            LinearLayout noPtkLayout = listViewItem.findViewById(R.id.noPtkLayout);


            ptkManagerModel ptkManagerModel = getItem(position);

            tanggal_2.setText(convertFormat(ptkManagerModel.getSubmit_date()));
            jabatan.setText(ptkManagerModel.getJabatan_karyawan());
            depo.setText(ptkManagerModel.getDepo_ptk());

            depart.setText(ptkManagerModel.getDept_ptk());
            analisa.setText(ptkManagerModel.getAnalisa());
            ptk.setText(ptkManagerModel.getTenaga_kerja());

            if(!status.getSelectedItem().toString().equals("Open")){
                layout_tanggal.setVisibility(View.VISIBLE);
                tanggal_manager.setText(convertFormat(ptkManagerModel.getTanggal_manager()));
            } else {
                layout_tanggal.setVisibility(View.GONE);
            }

            if(!status.getSelectedItem().toString().equals("Approved")){
                noPtkLayout.setVisibility(View.GONE);
            } else {
                noPtkLayout.setVisibility(View.VISIBLE);
                nomor_ptk.setText(ptkManagerModel.getNo_ptk());
            }

            nomor.setText(ptkManagerModel.getId());
            if (ptkManagerModel.getStatus_atasan().equals("0")) {
                atasan.setImageResource(R.drawable.btn_open);
            } else if (ptkManagerModel.getStatus_atasan().equals("1")) {
                atasan.setImageResource(R.drawable.btn_aprv);
            } else if (ptkManagerModel.getStatus_atasan().equals("2")) {
                atasan.setImageResource(R.drawable.btn_notaprv);
            } else if (ptkManagerModel.getStatus_atasan().equals("3")) {
                atasan.setImageResource(R.drawable.btn_hangus);
            }

            if (ptkManagerModel.getStatus_hrd().equals("0")) {
                hrd.setImageResource(R.drawable.btn_open);
            } else if (ptkManagerModel.getStatus_hrd().equals("1")) {
                hrd.setImageResource(R.drawable.btn_aprv);
            } else if (ptkManagerModel.getStatus_hrd().equals("2")) {
                hrd.setImageResource(R.drawable.btn_notaprv);
            } else if (ptkManagerModel.getStatus_hrd().equals("3")) {
                hrd.setImageResource(R.drawable.btn_hangus);
            }

            if (ptkManagerModel.getStatus_manager().equals("0")) {
                manager.setImageResource(R.drawable.btn_open);
            } else if (ptkManagerModel.getStatus_manager().equals("1")) {
                manager.setImageResource(R.drawable.btn_aprv);
            } else if (ptkManagerModel.getStatus_manager().equals("2")) {
                manager.setImageResource(R.drawable.btn_notaprv);
            } else if (ptkManagerModel.getStatus_manager().equals("3")) {
                manager.setImageResource(R.drawable.btn_hangus);
            }


            return listViewItem;

        }
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
        if(status.getSelectedItem().toString().equals("Open")){
            cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Ptk/index_manager?date1=" + tanggal1 + "&date2=" +tanggal2);
        } else if(status.getSelectedItem().toString().equals("Approved")){
            cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Ptk/index_manager_approval?status_manager=1&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
        } else  if(status.getSelectedItem().toString().equals("Not Approved")){
            cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Ptk/index_manager_approval?status_manager=2&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
        return convetDateFormat.format(date);
    }

    public static void preventTwoClick(final View view){
        view.setEnabled(false);
        view.postDelayed(
                ()-> view.setEnabled(true),
                2000
        );
    }

    public static String convertFormat2(String inputDate) {
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


}