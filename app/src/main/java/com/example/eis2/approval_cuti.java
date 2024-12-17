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
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.approvalcutikhusus;
import com.example.eis2.Item.approvalcutitahunan;
import com.example.eis2.Item.approvaldinasnonfullmodel;
import com.example.eis2.Item.dinasfulldaymodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.cuti.txt_location;
import static com.example.eis2.cuti.txt_nomor_jabatan;
import static com.example.eis2.dinas.txt_nomor_jab;
import static com.example.eis2.izin.txt_jabatan;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class approval_cuti extends AppCompatActivity {
    ImageButton tahunan, khusus;
    private List<approvalcutikhusus> movieItemList;
    private List<approvalcutitahunan> movieItemList2;

    TextView counttahunan, countkhusus;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_cuti);
        HttpsTrustManager.allowAllSSL();
        counttahunan = (TextView) findViewById(R.id.counttahunan);
        countkhusus = (TextView) findViewById(R.id.countkhusus);

        tahunan = (ImageButton) findViewById(R.id.tahunan);
        khusus = (ImageButton) findViewById(R.id.khusus);
        tahunan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(approval_cuti.this, approval_cuti_tahunan.class);
                startActivity(i);
            }
        });

        khusus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(approval_cuti.this, approval_cuti_khusus.class);
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

        pDialog = new ProgressDialog(approval_cuti.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/cuti_khusus/index_atasan?jabatan_struktur="+ lokasi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            int number = 0;

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                approvalcutikhusus movieItem = new approvalcutikhusus(
                                        movieObject.getString("id_cuti_khusus"),
                                        movieObject.getString("nik_cuti_khusus"),
                                        movieObject.getString("tanggal_pengajuan"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("start_cuti_khusus"),
                                        movieObject.getString("lokasi_struktur"),
                                        movieObject.getString("ket_tambahan_khusus"),
                                        movieObject.getString("jenis_cuti_khusus"),
                                        movieObject.getString("kondisi"),
                                        movieObject.getString("status_cuti_khusus"),
                                        movieObject.getString("feedback_cuti_khusus"));

                                if(!txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (movieObject.getString("status_cuti_khusus").contains("0") && (movieObject.getString("lokasi_struktur").equalsIgnoreCase(txt_lokasi.getText().toString())))
                                        number++;
                                    {
                                        countkhusus.setText(String.valueOf(number));
                                        System.out.println("nomor1 = +" + number);
                                    }
                                }
                                if(txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (movieObject.getString("status_cuti_khusus").contains("0"))
                                        number++;
                                    {
                                        countkhusus.setText(String.valueOf(number));
                                        System.out.println("nomor1 = +" + number);
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
    private void getDetail2() {
        String lokasi = text_jabatan.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/cuti_tahunan/index_atasan?jabatan_struktur="+ lokasi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            int number = 0;

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                final approvalcutitahunan movieItem = new approvalcutitahunan(
                                        movieObject.getString("id_sisa_cuti"),
                                        movieObject.getString("tanggal_pengajuan"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("start_cuti_tahunan"),
                                        movieObject.getString("lokasi_struktur"),
                                        movieObject.getString("ket_tambahan_tahunan"),
                                        movieObject.getString("opsi_cuti_tahunan"),
                                        movieObject.getString("status_cuti_tahunan"),
                                        movieObject.getString("feedback_cuti_tahunan"));

                                if(!txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (movieObject.getString("status_cuti_tahunan").contains("0") && (movieObject.getString("lokasi_struktur").equalsIgnoreCase(txt_lokasi.getText().toString())))
                                        number++;
                                    {
                                        counttahunan.setText(String.valueOf(number));
                                        System.out.println("nomor2 = +" + number);
                                    }
                                }
                                if(txt_lokasi.getText().toString().equals("Pusat")) {
                                    if (movieObject.getString("status_cuti_tahunan").contains("0"))
                                        number++;
                                    {
                                        counttahunan.setText(String.valueOf(number));
                                        System.out.println("nomor2 = +" + number);
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

