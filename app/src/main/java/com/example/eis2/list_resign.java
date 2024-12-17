package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.example.eis2.Item.Trace;
import com.example.eis2.Item.resignmodel;
import com.example.eis2.Item.suratketeranganmodel;
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
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;

public class list_resign extends AppCompatActivity {
    TextView peringatan;
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;
    DrawerLayout dLayout;
    RecyclerView rvTrace;
    private List<Trace> traceList = new ArrayList<>(10);
    private TraceListAdapter adapter;
    CardView peringatanlinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_resign);
        HttpsTrustManager.allowAllSSL();
        peringatanlinear = findViewById(R.id.peringatanlinear);
        peringatan = findViewById(R.id.peringatan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        rvTrace = (RecyclerView) findViewById(R.id.rvTrace);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });
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
                            list_resign.this);
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
                                    Intent intent = new Intent(list_resign.this, MainActivity.class);
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

        getResign();

    }


    private void getResign() {
        pDialog = new ProgressDialog(list_resign.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_nik?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                traceList.add(new Trace("Tanggal Submit", convertFormat(movieObject.getString("submit_date"))));
                                traceList.add(new Trace("Tanggal Pengajuan",  tanggal(movieObject.getString("tanggal_pengajuan"))));
                                traceList.add(new Trace("Tanggal Efektif Resign", tanggal5(movieObject.getString("tanggal_efektif_resign"))));
                                traceList.add(new Trace("Approval Atasan 1", tanggal(movieObject.getString("tanggal"))));

                               if(tanggal(movieObject.getString("tanggal")).equals("Open")){
                                    traceList.add(new Trace("Approval Atasan 2", "Open"));
                                } else if (tanggal(movieObject.getString("tanggal_2")).equals("Open")) {
                                   traceList.add(new Trace("Approval Atasan 2", "Waiting"));
                                } else if (!tanggal(movieObject.getString("tanggal_2")).equals("Open")) {
                                   traceList.add(new Trace("Approval Atasan 2", tanggal(movieObject.getString("tanggal_2"))));
                                }


                                if(movieObject.getString("tanggal_efektif_resign").equals("null") || movieObject.getString("tanggal_efektif_resign").equals("") || movieObject.getString("tanggal_efektif_resign").equals("0000-00-00")){
                                    peringatanlinear.setVisibility(View.GONE);
                                } else {

                                    String red = tanggal(movieObject.getString("tanggal_efektif_resign"));
//                                    String sourceString = "<b>" + red + "</b> ";
                                    String s =  peringatan.getText().toString() + " " + "<font color=#FF0000>"+ red + "</font>";
                                    peringatanlinear.setVisibility(View.VISIBLE);
                                    peringatan.setText(Html.fromHtml(s));
                                }

                                adapter = new TraceListAdapter(list_resign.this, traceList);
                                rvTrace.setLayoutManager(new LinearLayoutManager(list_resign.this));
                                rvTrace.setAdapter(adapter);


                                hideDialog();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Belum ada Pengajuan", Toast.LENGTH_SHORT).show();
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
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public class TraceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;
        private List<Trace> traceList = new ArrayList<>(1);
        private static final int TYPE_TOP = 0x0000;
        private static final int TYPE_NORMAL= 0x0001;

        public TraceListAdapter(Context context, List<Trace> traceList) {
            inflater = LayoutInflater.from(context);
            this.traceList = traceList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(inflater.inflate(R.layout.item_trace, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder itemHolder = (ViewHolder) holder;
            itemHolder.bindHolder(traceList.get(position));

            if (getItemViewType(position) == TYPE_TOP) {
                itemHolder.tvTopLine.setVisibility(View.INVISIBLE);
                itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
            }
            if (position == 0) {
                itemHolder.statustext.setVisibility(View.VISIBLE);
            }



            if (position == 4) {
                itemHolder.line.setVisibility(View.INVISIBLE);
            }

            if(!itemHolder.tvAcceptStation.getText().toString().equals("Waiting")){
                itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
            }

            if(itemHolder.tvAcceptStation.getText().toString().equals("Waiting")){
                itemHolder.tvDot.setBackgroundResource(R.drawable.timeline_progress);
            }

            if(itemHolder.tvAcceptStation.getText().toString().equals("Open")){
                itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_normal);
            }



        }

        @Override
        public int getItemCount() {
            return traceList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_TOP;
            }
            return TYPE_NORMAL;
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvAcceptTime, tvAcceptStation, statustext;
            private TextView tvTopLine, tvDot, line;
            public ViewHolder(View itemView) {
                super(itemView);
                tvAcceptTime = (TextView) itemView.findViewById(R.id.tvAcceptTime);
                tvAcceptStation = (TextView) itemView.findViewById(R.id.tvAcceptStation);
                tvTopLine = (TextView) itemView.findViewById(R.id.tvTopLine);
                tvDot = (TextView) itemView.findViewById(R.id.tvDot);
                line = (TextView) itemView.findViewById(R.id.line);
                statustext = (TextView) itemView.findViewById(R.id.statustext);



            }

            public void bindHolder(Trace trace) {
                tvAcceptTime.setText(trace.getAcceptTime());
                tvAcceptStation.setText(trace.getAcceptStation());





            }
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
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
        if (date == null || inputDate.equals("0000-00-00")) {
            return "Open";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        return convetDateFormat.format(date);
    }

    public static String tanggal5(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null || inputDate.equals("0000-00-00")) {
            return "Waiting";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        return convetDateFormat.format(date);
    }
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

        
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}