package com.example.eis2;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_lokasi;

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
import android.os.Looper;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaderFactory;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.eis2.Item.approvalcutitahunan;
import com.example.eis2.Item.gratifikasimodel;
import com.example.eis2.Item.pelanggaranmodel;
import com.example.eis2.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class list_gratifikasi extends AppCompatActivity {
    DrawerLayout dLayout;
    private Calendar date;
    SharedPreferences sharedPreferences;
    private SimpleDateFormat dateFormatter;

    ListViewAdapterGratifikasi adapter;
    ProgressDialog pDialog;
    ListView list_gratifikasi;
    String date1, date2;
    ImageButton filterButton;

    List<gratifikasimodel> gratifikasimodelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_gratifikasi);
        list_gratifikasi = findViewById(R.id.list_gratifikasi);
        filterButton = findViewById(R.id.filter);
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
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

        toolbar.setTitle("List Gratifikasi");

        setNavigationDrawer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());

        date1 = currentDateandTime;
        date2 = currentDateandTime;
        getAllData();

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterButton.setEnabled(false);
                Dialog filter = new Dialog(list_gratifikasi.this);
                filter.setContentView(R.layout.filter);
                filter.setCancelable(false);
                filter.show();

                Button batal = filter.findViewById(R.id.batal);
                Button lihat = filter.findViewById(R.id.lihat);

                EditText tanggal_awal = filter.findViewById(R.id.tanggal_awal);
                EditText tanggal_akhir = filter.findViewById(R.id.tanggal_akhir);

                tanggal_awal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar currentDate = Calendar.getInstance();

                        date = currentDate.getInstance();

                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth);

                                tanggal_awal.setText(dateFormatter.format(date.getTime()));
                                date1 = convertFormatTanggalDate(tanggal_awal.getText().toString());
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(list_gratifikasi.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });

                tanggal_akhir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar currentDate = Calendar.getInstance();

                        date = currentDate.getInstance();

                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth);

                                tanggal_akhir.setText(dateFormatter.format(date.getTime()));
                                date2 = convertFormatTanggalDate(tanggal_akhir.getText().toString());

                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(list_gratifikasi.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filter.dismiss();
                    }
                });

                lihat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(tanggal_awal.getText().toString().length() == 0){
                            tanggal_awal.setError("Wajib Di isi");
                        } else if(tanggal_akhir.getText().toString().length() == 0){
                            tanggal_akhir.setError("Wajib Di isi");
                        } else {
                            filter.dismiss();
                            getAllData();
                        }
                    }
                });
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        filterButton.setEnabled(true);
                    }
                }, 2000); // 2 seconds delay

            }
        });

    }

    public static String convertFormatTanggalDate(String inputDate) {
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

    private void getAllData() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        pDialog = new ProgressDialog(list_gratifikasi.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        System.out.println("https://hrd.tvip.co.id/rest_server/pengajuan/gratifikasi/index?nik_baru="+ nik_baru+"&date="+date1+"&date2="+date2);
        adapter = new ListViewAdapterGratifikasi(gratifikasimodelList, getApplicationContext());
        adapter.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/gratifikasi/index?nik_baru="+ nik_baru+"&date="+date1+"&date2="+date2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hideDialog();
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            int number = 0;

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                final gratifikasimodel movieItem = new gratifikasimodel(
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("nomorLaporan"),
                                        movieObject.getString("jabatan"),
                                        movieObject.getString("departement"),
                                        movieObject.getString("pemberi"),
                                        movieObject.getString("nominal"),
                                        movieObject.getString("keterangan"),
                                        movieObject.getString("fotoBukti"),
                                        movieObject.getString("tglKejadian"));
                                gratifikasimodelList.add(movieItem);
                                list_gratifikasi.setVisibility(View.VISIBLE);
                                list_gratifikasi.setAdapter(adapter);
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
                        list_gratifikasi.setVisibility(View.GONE);
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
            };
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

    public class ListViewAdapterGratifikasi extends ArrayAdapter<gratifikasimodel> {

        List<gratifikasimodel> movieItemList2;

        private Context context;

        public ListViewAdapterGratifikasi(List<gratifikasimodel> movieItemList2, Context context) {
            super(context, R.layout.list_item_gratifikasi, movieItemList2);
            this.movieItemList2 = movieItemList2;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_gratifikasi, null, true);

            TextView noForm = listViewItem.findViewById(R.id.noForm);
            TextView nama_penerima = listViewItem.findViewById(R.id.nama_penerima);
            TextView jabatan = listViewItem.findViewById(R.id.jabatan);
            TextView pemberi = listViewItem.findViewById(R.id.pemberi);
            TextView nominal = listViewItem.findViewById(R.id.nominal);
            TextView keterangan = listViewItem.findViewById(R.id.keterangan);
            ImageView gambarbukti = listViewItem.findViewById(R.id.gambarbukti);
            TextView tanggal_kejadian = listViewItem.findViewById(R.id.tanggal_kejadian);


            MaterialButton lihatbutton = listViewItem.findViewById(R.id.lihatbutton);

            gratifikasimodel movieItem = getItem(position);

            noForm.setText("Nomor Form : " + movieItem.getNomorLaporan());
            nama_penerima.setText(movieItem.getNama_karyawan_struktur());
            jabatan.setText(movieItem.getJabatan());
            pemberi.setText(movieItem.getPemberi());
            nominal.setText(movieItem.getNominal());

            tanggal_kejadian.setText(convertTanggal(movieItem.getTglKejadian()));

            keterangan.setText(movieItem.getKeterangan());

            lihatbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showZoomedImage(movieItem.getFotobukti());
                }
            });



            return listViewItem;
        }
    }

    private void showZoomedImage(String fotobukti) {
        String imageUrl = "https://hrd.tvip.co.id/lapor/upload/" + fotobukti; // Replace with your actual URL logic

        ZoomImageDialogFragment dialogFragment = ZoomImageDialogFragment.newInstance(imageUrl);
        dialogFragment.show(getSupportFragmentManager(), "ZoomImageDialogFragment");
    }

    private void setNavigationDrawer() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        dLayout = (DrawerLayout) findViewById(R.id.dl_kontrakkaryawan); // initiate a DrawerLayout
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
                            list_gratifikasi.this);
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
                                    Intent intent = new Intent(list_gratifikasi.this, MainActivity.class);
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

    public class BasicAuthorization implements LazyHeaderFactory {
        private final String username;
        private final String password;

        public BasicAuthorization(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override public String buildHeader() {
            return "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
        }
    }

    public static String convertTanggal(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "Tidak Ada Tanggal";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return convetDateFormat.format(date);
    }
}