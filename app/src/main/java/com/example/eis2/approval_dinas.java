package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
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
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.approvaldinasfull;
import com.example.eis2.Item.approvaldinasnonfullmodel;
import com.example.eis2.Item.approvalfulldaymodel;
import com.example.eis2.Item.dinasfulldaymodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.eis2.cuti.txt_location;
import static com.example.eis2.dinas.txt_jabatan2;
import static com.example.eis2.dinas.txt_nomor_jab;
import static com.example.eis2.izin.txt_nomor;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class approval_dinas extends AppCompatActivity {
    ImageButton fullday, nonfullday;
    private List<approvalfulldaymodel> movieItemList;
    private List<approvaldinasnonfullmodel> movieItemList2;

    TextView countfull, countnonfull;
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_dinas);
        countfull = (TextView) findViewById(R.id.countfull);
        countnonfull = (TextView) findViewById(R.id.countnonfull);
        fullday = (ImageButton) findViewById(R.id.fullday);
        fullday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(approval_dinas.this, approval_dinas_full.class);
                startActivity(i);
            }
        });
        nonfullday = (ImageButton) findViewById(R.id.nonfullday);
        nonfullday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(approval_dinas.this, approval_dinas_nonfull.class);
                startActivity(i);
            }
        });
        getDetail();
        movieItemList = new ArrayList<>();
        getDetail2();
        movieItemList2 = new ArrayList<>();
    }
    private void getDetail() {
        String lokasi = text_jabatan.getText().toString().trim();

        pDialog = new ProgressDialog(approval_dinas.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/dinas_full_day/index_atasan?jabatan_struktur="+ lokasi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            int number = 0;

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                approvalfulldaymodel movieItem = new approvalfulldaymodel(
                                        movieObject.getString("id_full_day"),
                                        movieObject.getString("no_pengajuan_full_day"),
                                        movieObject.getString("tanggal_pengajuan"),
                                        movieObject.getString("lokasi_struktur"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("jenis_full_day"),
                                        movieObject.getString("start_full_day"),
                                        movieObject.getString("karyawan_pengganti"),
                                        movieObject.getString("ket_tambahan"),
                                        movieObject.getString("status_full_day"),
                                        movieObject.getString("feedback_full_day"));

                                if(!txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (movieObject.getString("status_full_day").contains("0") && (movieObject.getString("lokasi_struktur").equalsIgnoreCase(txt_lokasi.getText().toString())))
                                        number++;
                                    {
                                        countfull.setText(String.valueOf(number));
                                    }
                                } else if(txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (movieObject.getString("status_full_day").contains("0"))
                                        number++;
                                    {
                                        countfull.setText(String.valueOf(number));
                                    }
                                }

                                hideDialog();

                                movieItemList.add(movieItem);

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
                        Toast.makeText(getApplicationContext(), "Maaf ada kesalahan", Toast.LENGTH_SHORT).show();
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
    private void getDetail2() {
        String lokasi = text_jabatan.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/dinas_non_full_day/index_atasan?jabatan_struktur="+ lokasi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            int number = 0;

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                final approvaldinasnonfullmodel movieItem = new approvaldinasnonfullmodel(
                                        movieObject.getString("id_non_full"),
                                        movieObject.getString("tanggal_pengajuan"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("lokasi_struktur"),
                                        movieObject.getString("jenis_non_full"),
                                        movieObject.getString("tanggal_non_full"),
                                        movieObject.getString("ket_tambahan_non_full"),
                                        movieObject.getString("status_non_full"),
                                        movieObject.getString("feedback_non_full"));
                                if(!txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (movieObject.getString("status_non_full").contains("0") && (movieObject.getString("lokasi_struktur").equalsIgnoreCase(txt_lokasi.getText().toString())))
                                        number++;
                                    {
                                        countnonfull.setText(String.valueOf(number));
                                    }
                                } else if(txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (movieObject.getString("status_non_full").contains("0"))
                                        number++;
                                    {
                                        countnonfull.setText(String.valueOf(number));
                                    }
                                }

                                    movieItemList2.add(movieItem);

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
                        Toast.makeText(getApplicationContext(), "Maaf ada kesalahan", Toast.LENGTH_SHORT).show();
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

