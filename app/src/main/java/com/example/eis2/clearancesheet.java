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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class clearancesheet extends AppCompatActivity {
    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    RecyclerView listmenu;
    private List<menuresignmodel> kuisionermodels = new ArrayList<>(10);
    ListViewAdapterSheet adapterResign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clearancesheet);
        listmenu = findViewById(R.id.listmenu);

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
                            clearancesheet.this);
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
                                    Intent intent = new Intent(clearancesheet.this, MainActivity.class);
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

        kuisionermodels.add(new menuresignmodel("Finance Accounting", "Open"));
        kuisionermodels.add(new menuresignmodel("Warehouse Operation", "Open"));
        kuisionermodels.add(new menuresignmodel("Human Resource Departement", "Open"));
        kuisionermodels.add(new menuresignmodel("Information Communication & Tech", "Open"));
        kuisionermodels.add(new menuresignmodel("General Affair", "Open"));
        kuisionermodels.add(new menuresignmodel("Quality Management System", "Open"));

        adapterResign = new ListViewAdapterSheet(clearancesheet.this, kuisionermodels);
        listmenu.setLayoutManager(new LinearLayoutManager(clearancesheet.this));
        listmenu.setAdapter(adapterResign);
    }

    public class ListViewAdapterSheet extends RecyclerView.Adapter<ListViewAdapterSheet.ViewProcessHolder> {

        Context context;
        private ArrayList<menuresignmodel> mItems; //memanggil modelData

        public ListViewAdapterSheet(Context context, List<menuresignmodel> item) {
            this.context = context;
            this.mItems = (ArrayList<menuresignmodel>) item;
        }

        @Override
        public ListViewAdapterSheet.ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_kuisioner, parent, false); //memanggil layout list recyclerview

            ListViewAdapterSheet.ViewProcessHolder processHolder = new ListViewAdapterSheet.ViewProcessHolder(view);
            return processHolder;
        }

        @Override
        public void onBindViewHolder(final ListViewAdapterSheet.ViewProcessHolder holder, @SuppressLint("RecyclerView") int position) {


            final menuresignmodel data = mItems.get(position);
            holder.keterangan.setText(data.getKeterangan());
            holder.status.setText(data.getStatus());

            if (position == 0) {
                holder.tvTopLine.setVisibility(View.INVISIBLE);
                holder.statustext.setVisibility(View.VISIBLE);
            }

            if (position == 5) {
                holder.line.setVisibility(View.INVISIBLE);
            }


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
                line =  (TextView) itemView.findViewById(R.id.line);
                keterangan = (TextView) itemView.findViewById(R.id.keterangan);
                tvTopLine = (TextView) itemView.findViewById(R.id.tvTopLine);
                statustext = (TextView) itemView.findViewById(R.id.statustext);
                status = (TextView) itemView.findViewById(R.id.status);
                tvDot = (TextView) itemView.findViewById(R.id.tvDot);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(status.getText().toString().equals("Open")){
                            new SweetAlertDialog(clearancesheet.this, SweetAlertDialog.WARNING_TYPE)
                                    .setContentText("Untuk proses clearance sheet, harap hubungi departement " + keterangan.getText().toString())
                                    .setConfirmText("OK")
                                    .show();
                        } else {
                            new SweetAlertDialog(clearancesheet.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setContentText("Proses clearance sheet dari departement "+ keterangan.getText().toString()+ ", sudah selesai".toString())
                                    .setConfirmText("OK")
                                    .show();
                        }

                    }
                });
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
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


                                        if(keterangan.getText().toString().equals("Finance Accounting")) {
                                            if (movieObject.getString("status_fa").equals("1")) {
                                                status.setText("Closed");
                                                tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                            }
                                        }

                                        if(keterangan.getText().toString().equals("Warehouse Operation")) {
                                            if (movieObject.getString("status_wop").equals("1")) {
                                                status.setText("Closed");
                                                tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                            }
                                        }

                                        if(keterangan.getText().toString().equals("Human Resource Departement")) {
                                            if (movieObject.getString("status_hrd").equals("1")) {
                                                status.setText("Closed");
                                                tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                            }
                                        }

                                        if(keterangan.getText().toString().equals("Information Communication & Tech")) {
                                            if (movieObject.getString("status_ict").equals("1")) {
                                                status.setText("Closed");
                                                tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                            }
                                        }

                                        if(keterangan.getText().toString().equals("General Affair")) {
                                            if (movieObject.getString("status_ga").equals("1")) {
                                                status.setText("Closed");
                                                tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                            }
                                        }

                                        if(keterangan.getText().toString().equals("Quality Management System")) {
                                            if (movieObject.getString("status_qms").equals("1")) {
                                                status.setText("Closed");
                                                tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
                                            }
                                        }
                                    }

                                } catch (JSONException e){
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
                RequestQueue requestQueue = Volley.newRequestQueue(clearancesheet.this);
                requestQueue.add(stringRequest);

            }


        }
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