package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.eis2.Item.keteranganmodel;
import com.example.eis2.Item.rekapabsensiclass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.spv_absensi.txt_jabatan_struktur;
import static com.example.eis2.spv_absensi.txt_lokasi_struktur;

public class rekapabsensi extends AppCompatActivity {
    ListView list_team;
    private List<rekapabsensiclass> team;
    private static RequestQueue mQueue;
    ProgressDialog pDialog;
    TextView firstdate, lastdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekapabsensi);
        HttpsTrustManager.allowAllSSL();
        list_team = findViewById(R.id.list_team);
        mQueue = Volley.newRequestQueue(rekapabsensi.this);
        firstdate = (TextView) findViewById(R.id.firstdate);
        lastdate = (TextView) findViewById(R.id.lastdate);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tanggalawal = simpleDateFormat.format(calendar.getTime());
        firstdate.setText(tanggalawal);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String tanggalakhir = sdf2.format(new Date());
        lastdate.setText(tanggalakhir);

        team = new ArrayList<>();
        loadPlayer();
    }

    private void loadPlayer() {
        pDialog = new ProgressDialog(rekapabsensi.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        String jabatan = txt_jabatan_struktur.getText().toString().trim();

        System.out.println("test 111 = " + jabatan);

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/team/index?jabatan_struktur=" + jabatan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject object = new JSONObject(response);
                            JSONArray movieArray = object.getJSONArray("data");

                            JSONObject movieObject2 = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject2 = movieArray.getJSONObject(i);

                                final rekapabsensiclass movieItem = new rekapabsensiclass(
                                        movieObject2.getString("nik_baru"),
                                        movieObject2.getString("nama_karyawan_struktur"),
                                        movieObject2.getString("lokasi_struktur"));
                                team.add(movieItem);

                                ListViewAdapterTeam adapter = new ListViewAdapterTeam(team, getApplicationContext());
                                list_team.setAdapter(adapter);

                                if (!movieObject2.getString("lokasi_struktur").equalsIgnoreCase(txt_lokasi_struktur.getText().toString())) {
                                    team.remove(movieItem);
                                }

                                if (movieObject2.getString("lokasi_struktur").equalsIgnoreCase("pusat")) {
                                    team.remove(movieItem);
                                    team.add(movieItem);
                                }

                                Collections.sort(team, new Comparator<rekapabsensiclass>() {
                                    public int compare(rekapabsensiclass o1, rekapabsensiclass o2) {
                                        if (o1.getNama_karyawan_struktur() == null || o2.getNama_karyawan_struktur() == null)
                                            return 0;
                                        return o1.getNama_karyawan_struktur().compareTo(o2.getNama_karyawan_struktur());
                                    }
                                });
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
                        Toast.makeText(getApplicationContext(), "Maaf, ada kesalahan", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest1);

    }

    public class ListViewAdapterTeam extends ArrayAdapter<rekapabsensiclass> {

        public List<rekapabsensiclass> team;
        private Context context;

        public ListViewAdapterTeam(List<rekapabsensiclass> team, Context context) {
            super(context, R.layout.list_item_rekapabsensi, team);
            this.team = team;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            final View listViewItem = inflater.inflate(R.layout.list_item_rekapabsensi, null);

            rekapabsensiclass movieItem = team.get(position);
            System.out.println("position =" + position);

            listViewItem.setBackground(getContext().getDrawable(R.drawable.border));
            TextView nikbaruteam = listViewItem.findViewById(R.id.nik);
            TextView nama = listViewItem.findViewById(R.id.nama);
            final TextView telat = listViewItem.findViewById(R.id.telat);
            final TextView alfa = listViewItem.findViewById(R.id.alfa);
            final TextView sick = listViewItem.findViewById(R.id.sakit);
            final TextView hadir = listViewItem.findViewById(R.id.hadir);
            final TextView f1 = listViewItem.findViewById(R.id.F1);
            final TextView f4 = listViewItem.findViewById(R.id.F4);
            final TextView cutikhusus = listViewItem.findViewById(R.id.cutikhusus);
            final TextView cutitahunan = listViewItem.findViewById(R.id.cutitahunan);


            nikbaruteam.setText(movieItem.getNik_baru());
            nama.setText(movieItem.getNama_karyawan_struktur());

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String firstdate = simpleDateFormat.format(calendar.getTime());
            System.out.println("Test tanggal 1 =" + firstdate);

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String lastdate = sdf2.format(new Date());
            System.out.println("Test tanggal 2 =" + lastdate);

            int a = getPosition(getItem(getCount() -1));
            System.out.println("terakhir =" + a);

            String nik_baru = getItem(position).getNik_baru();
            System.out.println("nik222 =" + nik_baru);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/absensi/index?shift_day="+firstdate+"&shift_day_2="+lastdate+"&badgenumber="+nik_baru,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");
                                int number = 0;
                                int number1 = 0;
                                int number2 = 0;
                                int number3 = 0;
                                int number4 = 0;
                                int number5 = 0;
                                int number6 = 0;
                                int number7 = 0;

                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    movieObject.getString("shift_day");
                                    movieObject.getString("F1");
                                    movieObject.getString("depo_f1");
                                    movieObject.getString("F4");
                                    movieObject.getString("depo_f4");
                                    movieObject.getString("ket_absensi");

                                    if (movieObject.getString("ket_absensi").contains("TL"))
                                        number1++; {
                                        telat.setText(String.valueOf(number1));
                                    }

                                    if (movieObject.getString("ket_absensi").contains("AL"))
                                        number2++; {
                                        alfa.setText(String.valueOf(number2));
                                    }

                                    if (movieObject.getString("ket_absensi").contains("SA"))
                                        number3++; {
                                        sick.setText(String.valueOf(number3));
                                    }

                                    if (movieObject.getString("ket_absensi").contains("HD"))
                                        number++;  {
                                        hadir.setText(String.valueOf(number));
                                        hideDialog();
                                    }
                                    if (movieObject.getString("ket_absensi").contains("TD F1"))
                                        number4++; {
                                        f1.setText(String.valueOf(number4));
                                    }
                                    if (movieObject.getString("ket_absensi").contains("TD F4"))
                                        number5++; {
                                        f4.setText(String.valueOf(number5));
                                    }
                                    if (movieObject.getString("ket_absensi").contains("CK"))
                                        number6++; {
                                        cutikhusus.setText(String.valueOf(number6));
                                    }
                                    if (movieObject.getString("ket_absensi").contains("CU"))
                                        number7++; {
                                        cutitahunan.setText(String.valueOf(number7));
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
            mQueue.add(stringRequest);
            stringRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
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
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

        
        super.onDestroy();
    }
}
