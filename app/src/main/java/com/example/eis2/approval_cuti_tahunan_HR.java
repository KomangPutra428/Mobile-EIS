package com.example.eis2;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_lokasi;

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
import com.example.eis2.Item.approvalcutitahunan;
import com.example.eis2.Item.approvalcutitahunanhr;
import com.example.eis2.Item.ptkManagerModel;
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

public class approval_cuti_tahunan_HR extends AppCompatActivity {
    ListView list;
    private List<approvalcutitahunanhr> approvalcutitahunanList = new ArrayList<>();
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
        setContentView(R.layout.activity_approval_cuti_tahunan_hr);

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
                final Dialog dialog = new Dialog(approval_cuti_tahunan_HR.this);
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
                        DatePickerDialog datePickerDialog = new  DatePickerDialog(approval_cuti_tahunan_HR.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));
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
                        DatePickerDialog datePickerDialog = new  DatePickerDialog(approval_cuti_tahunan_HR.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));
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
                                cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Cuti_tahunan/index_manager_hr?status="+"0"+"&date1=" + tanggal1 + "&date2=" +tanggal2);
                            } else if(status.getSelectedItem().toString().equals("Approved")){
                                cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Cuti_tahunan/index_manager_hr?status="+"1"+ "&date1=" + tanggal1 + "&date2=" +tanggal2);
                            } else  if(status.getSelectedItem().toString().equals("Not Approved")){
                                cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Cuti_tahunan/index_manager_hr?status="+"2"+"&date1=" + tanggal1 + "&date2=" +tanggal2);
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
                    cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Cuti_tahunan/index_manager_hr?status="+"0"+"&date1=" + tanggal1 + "&date2=" +tanggal2);
                } else if(status.getSelectedItem().toString().equals("Approved")){
                    cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Cuti_tahunan/index_manager_hr?status="+"1"+ "&date1=" + tanggal1 + "&date2=" +tanggal2);
                } else  if(status.getSelectedItem().toString().equals("Not Approved")){
                    cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Cuti_tahunan/index_manager_hr?status="+"2"+"&date1=" + tanggal1 + "&date2=" +tanggal2);
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
                            approval_cuti_tahunan_HR.this);
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
                                    Intent intent = new Intent(approval_cuti_tahunan_HR.this, MainActivity.class);
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

    private void cekdata(String s) {
        approvalcutitahunanList.clear();
        pDialog = new ProgressDialog(approval_cuti_tahunan_HR.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        System.out.println("Link Cuti Manager = " + s);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        list.setVisibility(View.VISIBLE);
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final approvalcutitahunanhr movieItem = new approvalcutitahunanhr(
                                        movieObject.getString("id_sisa_cuti"),
                                        movieObject.getString("tanggal_pengajuan"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("start_cuti_tahunan"),
                                        movieObject.getString("lokasi_struktur"),
                                        movieObject.getString("ket_tambahan_tahunan"),
                                        movieObject.getString("opsi_cuti_tahunan"),
                                        movieObject.getString("status_cuti_tahunan"),
                                        movieObject.getString("feedback_cuti_tahunan"),
                                        movieObject.getString("feedback_cuti_manager"),
                                        movieObject.getString("status_cuti_manager"));

                                approvalcutitahunanList.add(movieItem);

                                hideDialog();

                                ListViewAdapterCutiTahunanApprovalHR adapter = new ListViewAdapterCutiTahunanApprovalHR(approvalcutitahunanList, getApplicationContext());
                                list.setAdapter(adapter);

                                if(status.getSelectedItem().toString().equals("Open")) {
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                                            Intent i = new Intent(approval_cuti_tahunan_HR.this, form_cuti_tahunan_hr.class);
                                            String id_sisa_cuti = ((approvalcutitahunanhr) parent.getItemAtPosition(position)).getId_sisa_cuti();
                                            i.putExtra("id_cutitahunan", id_sisa_cuti);
                                            startActivity(i);
                                        }
                                    });
                                } else {
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

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

                        hideDialog();
                        list.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), String.valueOf("Belum Ada Pengajuan"), Toast.LENGTH_SHORT).show();
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public static class ListViewAdapterCutiTahunanApprovalHR extends ArrayAdapter<approvalcutitahunanhr> {

        List<approvalcutitahunanhr> movieItemList;

        private Context context;

        public ListViewAdapterCutiTahunanApprovalHR(List<approvalcutitahunanhr> movieItemList, Context context) {
            super(context, R.layout.list_view_approval_tahunan_hr, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            final View listViewItem = inflater.inflate(R.layout.list_view_approval_tahunan_hr, null, true);

            TextView tanggalid = listViewItem.findViewById(R.id.tanggalid);

            TextView id = listViewItem.findViewById(R.id.id);
            TextView name = listViewItem.findViewById(R.id.name);
            TextView tidakhadir = listViewItem.findViewById(R.id.tidakhadir);
            TextView keterangan = listViewItem.findViewById(R.id.keterangan);
            TextView option = listViewItem.findViewById(R.id.option);
            TextView feedback2 = listViewItem.findViewById(R.id.feedback2);
            ImageView approval1 = listViewItem.findViewById(R.id.approval1);

            TextView feedback_manager = listViewItem.findViewById(R.id.feedback_manager);
            ImageView approval_manager = listViewItem.findViewById(R.id.approval_manager);

            approvalcutitahunanhr movieItem = getItem(position);

            tanggalid.setText(convertFormat(movieItem.getStart_cuti_tahunan()));
            id.setText(movieItem.getId_sisa_cuti());
            name.setText(movieItem.getNama_karyawan_struktur());
            tidakhadir.setText(tanggal(movieItem.getStart_cuti_tahunan()));
            keterangan.setText(movieItem.getKet_tambahan_tahunan());
            option.setText(movieItem.getOpsi_cuti_tahunan());
            feedback2.setText(movieItem.getFeedback_cuti_tahunan());

            if(movieItem.getFeedback_cuti_manager().equals("null")){
                feedback_manager.setText("");
            } else {
                feedback_manager.setText(movieItem.getFeedback_cuti_manager());
            }

            if (movieItem.getStatus_cuti_tahunan().equals("0")) {
                approval1.setImageResource(R.drawable.btn_open);
            } else if (movieItem.getStatus_cuti_tahunan().equals("1")) {
                approval1.setImageResource(R.drawable.btn_aprv);
            } else if (movieItem.getStatus_cuti_tahunan().equals("2")) {
                approval1.setImageResource(R.drawable.btn_notaprv);
            } else if (movieItem.getStatus_cuti_tahunan().equals("3")) {
                approval1.setImageResource(R.drawable.btn_hangus);
            }

            if (movieItem.getStatus_cuti_manager().equals("0")) {
                approval_manager.setImageResource(R.drawable.btn_open);
            } else if (movieItem.getStatus_cuti_manager().equals("1")) {
                approval_manager.setImageResource(R.drawable.btn_aprv);
            } else if (movieItem.getStatus_cuti_manager().equals("2")) {
                approval_manager.setImageResource(R.drawable.btn_notaprv);
            } else if (movieItem.getStatus_cuti_manager().equals("3")) {
                approval_manager.setImageResource(R.drawable.btn_hangus);
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        return convetDateFormat.format(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(status.getSelectedItem().toString().equals("Open")){
            cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Cuti_tahunan/index_manager_hr?status="+"0"+"&date1=" + tanggal1 + "&date2=" +tanggal2);
        } else if(status.getSelectedItem().toString().equals("Approved")){
            cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Cuti_tahunan/index_manager_hr?status="+"1"+ "&date1=" + tanggal1 + "&date2=" +tanggal2);
        } else  if(status.getSelectedItem().toString().equals("Not Approved")){
            cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Cuti_tahunan/index_manager_hr?status="+"2"+"&date1=" + tanggal1 + "&date2=" +tanggal2);
        }
    }


}