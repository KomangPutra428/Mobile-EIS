package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.example.eis2.Item.menuresignmodel;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;

public class menu_resign extends AppCompatActivity {
    TextView hari_ini;
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    RecyclerView listmenu;
    private List<menuresignmodel> kuisionermodels = new ArrayList<>(10);
    ListViewAdapterResign adapterResign;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_resign);
        HttpsTrustManager.allowAllSSL();

        listmenu = findViewById(R.id.listmenu);
        hari_ini = findViewById(R.id.hari_ini);


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


        setNavigationDrawer();

        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE, dd MMMM yyyy");

        String strdate1 = sdf1.format(c1.getTime());

        hari_ini.setText(strdate1);

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
                            menu_resign.this);
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
                                    Intent intent = new Intent(menu_resign.this, MainActivity.class);
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


    private void getCompare() {
        pDialog = new ProgressDialog(this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        final int alpha = Integer.parseInt(txt_alpha.getText().toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_nik?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);

                                if(movieObject.getString("status_pengajuan").equals("2")){
                                    kuisionermodels.add(new menuresignmodel("Pengajuan Resign", "Waiting"));
                                    adapterResign = new ListViewAdapterResign(menu_resign.this, kuisionermodels);
                                    listmenu.setLayoutManager(new LinearLayoutManager(menu_resign.this));
                                    listmenu.setAdapter(adapterResign);
                                } else {
                                    kuisionermodels.add(new menuresignmodel("Pengajuan Resign", "Closed"));
                                    adapterResign = new ListViewAdapterResign(menu_resign.this, kuisionermodels);
                                    listmenu.setLayoutManager(new LinearLayoutManager(menu_resign.this));
                                    listmenu.setAdapter(adapterResign);
                                }

                                kuisionermodels.add(new menuresignmodel("Status Resign", "Waiting"));



                                if(movieObject.getString("status_atasan").equals("1")){
                                    kuisionermodels.add(new menuresignmodel("Pengambilan Cuti", "Open"));
                                    kuisionermodels.add(new menuresignmodel("Exit InterView", "Open"));
                                    kuisionermodels.add(new menuresignmodel("Serah Terima", "Open"));
                                    kuisionermodels.add(new menuresignmodel("Clearance Sheet", "Open"));


                                    adapterResign = new ListViewAdapterResign(menu_resign.this, kuisionermodels);
                                    listmenu.setLayoutManager(new LinearLayoutManager(menu_resign.this));
                                    listmenu.setAdapter(adapterResign);

                                }
//                                if (4 <= alpha && alpha <= 10) {
//                                    kuisionermodels.add(new menuresignmodel("Approval Resign", ""));
//                                    kuisionermodels.add(new menuresignmodel("Karyawan Exit Interview", ""));
//                                    adapterResign = new ListViewAdapterResign(menu_resign.this, kuisionermodels);
//                                    listmenu.setLayoutManager(new LinearLayoutManager(menu_resign.this));
//                                    listmenu.setAdapter(adapterResign);
//                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        kuisionermodels.add(new menuresignmodel("Pengajuan Resign", "Waiting"));
                        kuisionermodels.add(new menuresignmodel("Status Resign", "Open"));

//                        if (4 <= alpha && alpha <= 10) {
//                            kuisionermodels.add(new menuresignmodel("Approval Resign", ""));
//                            kuisionermodels.add(new menuresignmodel("Karyawan Exit Interview", ""));
//
//                            adapterResign = new ListViewAdapterResign(menu_resign.this, kuisionermodels);
//                            listmenu.setLayoutManager(new LinearLayoutManager(menu_resign.this));
//                            listmenu.setAdapter(adapterResign);
//                        }
                        adapterResign = new ListViewAdapterResign(menu_resign.this, kuisionermodels);
                        listmenu.setLayoutManager(new LinearLayoutManager(menu_resign.this));
                        listmenu.setAdapter(adapterResign);

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
    }
    @Override
    protected void onResume() {
        super.onResume();
        kuisionermodels.clear();
        getCompare();
    }

    public class ListViewAdapterResign extends RecyclerView.Adapter<ListViewAdapterResign.ViewProcessHolder> {

        Context context;
        private ArrayList<menuresignmodel> mItems; //memanggil modelData

        public ListViewAdapterResign(Context context, List<menuresignmodel> item) {
            this.context = context;
            this.mItems = (ArrayList<menuresignmodel>) item;
        }

        @Override
        public ListViewAdapterResign.ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_kuisioner, parent, false); //memanggil layout list recyclerview

            ListViewAdapterResign.ViewProcessHolder processHolder = new ListViewAdapterResign.ViewProcessHolder(view);


            return processHolder;


        }

        @Override
        public void onBindViewHolder(final ListViewAdapterResign.ViewProcessHolder holder, @SuppressLint("RecyclerView") int position) {


            final menuresignmodel data = mItems.get(position);
            holder.keterangan.setText(data.getKeterangan());
            holder.status.setText(data.getStatus());

            holder.setIsRecyclable(false);

            if (position == 0) {
                holder.tvTopLine.setVisibility(View.INVISIBLE);
                holder.tvDot.setBackgroundResource(R.drawable.timeline_progress);
                holder.statustext.setVisibility(View.VISIBLE);
            }

            if (position == mItems.size() - 1) {
                holder.line.setVisibility(View.INVISIBLE);
            }

            if(holder.keterangan.getText().toString().equals("Status Resign")) {
                if(holder.status.getText().toString().equals("Waiting")) {
                    holder.tvDot.setBackgroundResource(R.drawable.timeline_progress);
                }
            }





        }
        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }





        public class ViewProcessHolder extends RecyclerView.ViewHolder {

            TextView id, keterangan, status, tvDot, tvTopLine, statustext, line;
            Context context;

            View itemView;


            public ViewProcessHolder(View itemView) {
                super(itemView);

                this.itemView = itemView;

                context = itemView.getContext();

                id = (TextView) itemView.findViewById(R.id.id);
                keterangan = (TextView) itemView.findViewById(R.id.keterangan);
                status = (TextView) itemView.findViewById(R.id.status);
                statustext = (TextView) itemView.findViewById(R.id.statustext);
                line = (TextView) itemView.findViewById(R.id.line);
                tvDot = (TextView) itemView.findViewById(R.id.tvDot);
                tvTopLine = (TextView) itemView.findViewById(R.id.tvTopLine);

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                if(keterangan.getText().toString().equals("Pengajuan Resign")){
                    if(status.getText().toString().equals("Closed")){
                        tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                    }
                    tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                }



                String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_nik?nik_baru=" + nik_baru,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    JSONArray movieArray = obj.getJSONArray("data");
                                    for (int i = 0; i < movieArray.length(); i++) {
                                        JSONObject movieObject = movieArray.getJSONObject(i);

                                        if(keterangan.getText().toString().equals("Pengajuan Resign")){
                                            tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                        }

                                        if(movieObject.getString("status_atasan").equals("1") && movieObject.getString("status_atasan_2").equals("1")){
                                            if(keterangan.getText().toString().equals("Pengambilan Cuti")) {
                                                tvDot.setBackgroundResource(R.drawable.timeline_progress);
                                                status.setText("Waiting");
                                            }
                                        }


                                        if(keterangan.getText().toString().equals("Pengambilan Cuti")) {
                                            if (movieObject.getString("status_cuti").equals("1")) {
                                                tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                                status.setText("Closed");
                                            }
                                        }

                                        if(keterangan.getText().toString().equals("Status Resign")) {
                                            if(movieObject.getString("status_atasan").equals("1") && movieObject.getString("status_atasan_2").equals("1")) {
                                                status.setText("Closed");
                                                tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                            }
                                        }

                                        if (movieObject.getString("status_cuti").equals("1")) {
                                            if(keterangan.getText().toString().equals("Kuisioner")) {
                                                tvDot.setBackgroundResource(R.drawable.timeline_progress);
                                                status.setText("Waiting");
                                            }
                                        }

                                        if (movieObject.getString("status_kuisioner").equals("1")){
                                            if(keterangan.getText().toString().equals("Kuisioner")) {
                                                    status.setText("Closed");
                                                    tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                                }
                                        }

                                        if (movieObject.getString("status_cuti").equals("1")) {
                                            if(keterangan.getText().toString().equals("Exit InterView")){
                                                status.setText("Waiting");
                                                tvDot.setBackgroundResource(R.drawable.timeline_progress);

                                            }
                                        }

                                            if(keterangan.getText().toString().equals("Exit InterView")) {
                                            if (movieObject.getString("status_exit").equals("1")) {
                                                status.setText("Closed");
                                                tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                            }
                                        }

                                        if (movieObject.getString("status_exit").equals("1")) {
                                            if(keterangan.getText().toString().equals("Serah Terima")){
                                                status.setText("Waiting");
                                                tvDot.setBackgroundResource(R.drawable.timeline_progress);

                                            }
                                        }

                                        if(keterangan.getText().toString().equals("Serah Terima")) {
                                            if (movieObject.getString("status_serah_terima").equals("1")) {
                                                status.setText("Closed");
                                                tvDot.setBackgroundResource(R.drawable.timelline_dot_first);

                                            }
                                        }

                                        if(keterangan.getText().toString().equals("Clearance Sheet")) {
                                            if (movieObject.getString("status_serah_terima").equals("1")) {
                                                status.setText("Waiting");
                                                tvDot.setBackgroundResource(R.drawable.timeline_progress);

                                            }
                                        }

                                        if (movieObject.getString("status_clearance").equals("1")) {
                                            if(keterangan.getText().toString().equals("Clearance Sheet")){
                                                status.setText("Closed");
                                                tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                            }


                                        }
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                hideDialog();
                                            }
                                        }, 1500);

                                    }

                                } catch (JSONException e){
                                        e.printStackTrace();
                                    }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(keterangan.getText().toString().equals("Pengajuan Resign")){
                                    tvDot.setBackgroundResource(R.drawable.timeline_progress);
                                }

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1500);

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
                RequestQueue requestQueue = Volley.newRequestQueue(menu_resign.this);
                requestQueue.add(stringRequest);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        String keterangansoal = keterangan.getText().toString();
                        String statussoal = status.getText().toString();


                        if(keterangansoal.equals("Pengajuan Resign")){
                            if(statussoal.equals("Closed")){
                                new SweetAlertDialog(menu_resign.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(keterangansoal)
                                        .setContentText("Anda sudah melakukan " + keterangansoal)
                                        .show();

                            } else {
                                Intent intent = new Intent(view.getContext(), pengajuan_resign.class);
                                context.startActivity(intent);
                            }
                        }

                        if(keterangansoal.equals("Status Resign")){
                            if(statussoal.equals("Open")){

                            } else {
                                Intent intent = new Intent(view.getContext(), list_resign.class);
                                context.startActivity(intent);
                            }
                        }

                        if(keterangansoal.equals("Pengambilan Cuti")){
                            if(statussoal.equals("Closed")){
                                new SweetAlertDialog(menu_resign.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(keterangansoal)
                                        .setContentText("Anda sudah melakukan " + keterangansoal)
                                        .show();

                            } else if(statussoal.equals("Open")){

                            } else {
                                Intent intent = new Intent(view.getContext(), setting_cuti.class);
                                context.startActivity(intent);
                            }

                        }

                        if(keterangansoal.equals("Kuisioner")){
                            if(statussoal.equals("Closed")){
                                new SweetAlertDialog(menu_resign.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(keterangansoal)
                                        .setContentText("Anda sudah melakukan " + keterangansoal)
                                        .show();

                            }else if(statussoal.equals("Open")){

                            } else {
                                Intent intent = new Intent(view.getContext(), menu_kuisioner.class);
                                context.startActivity(intent);
                            }

                        }

                        if(keterangansoal.equals("Exit InterView")){
                            if(statussoal.equals("Closed")){
                                new SweetAlertDialog(menu_resign.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(keterangansoal)
                                        .setContentText("Anda sudah melakukan " + keterangansoal)
                                        .show();
                            }else if(statussoal.equals("Open")){

                            } else {
                                new SweetAlertDialog(menu_resign.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(keterangansoal)
                                        .setContentText("Harap konfirmasi\n" +
                                                "dengan atasan\n" +
                                                "untuk proses\n" +
                                                "Exit Interview")
                                        .setConfirmText("OK")
                                        .show();
                            }
                        }

                        if(keterangansoal.equals("Serah Terima")){
                            if(statussoal.equals("Closed")){
                                new SweetAlertDialog(menu_resign.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(keterangansoal)
                                        .setContentText("Anda sudah melakukan " + keterangansoal)
                                        .show();
                            }else if(statussoal.equals("Open")){

                            }else if(statussoal.equals("Waiting")){
                                Intent intent = new Intent(view.getContext(), serahterima.class);
                                context.startActivity(intent);
                            } else {
                                Intent intent = new Intent(view.getContext(), serahterima.class);
                                context.startActivity(intent);
                            }
                        }

                        if(keterangansoal.equals("Clearance Sheet")){
                            if(statussoal.equals("Open")){

                            } else {
                                Intent intent = new Intent(view.getContext(), clearancesheet.class);
                                context.startActivity(intent);
                            }
                        }



//                        if(keterangansoal.equals("Status Resign")){
//                                Intent intent = new Intent(view.getContext(), list_resign.class);
//                                context.startActivity(intent);
//                        }
//
//                        if(keterangansoal.equals("Clearance Sheet")){
//                            Intent intent = new Intent(view.getContext(), clearancesheet.class);
//                            context.startActivity(intent);
//                        }

//                        if(keterangansoal.equals("Approval Resign")){
//                            Intent intent = new Intent(view.getContext(), approval_resign.class);
//                            context.startActivity(intent);
//                        }
//
//                        if(keterangansoal.equals("Karyawan Exit Interview")){
//                            Intent intent = new Intent(view.getContext(), list_interview_karyawan.class);
//                            context.startActivity(intent);
//                        }


                    }
                });
            }

            public void setOnClickListener(View.OnClickListener onClickListener) {
                itemView.setOnClickListener(onClickListener);
            }
        }
    }
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

        
        super.onDestroy();
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}