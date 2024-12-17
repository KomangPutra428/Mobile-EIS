package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eis2.Item.HttpsTrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;

public class hasil_assessment extends AppCompatActivity {
    TextView textdate, resiko, keteranganresiko, tekstambahan;
    SharedPreferences sharedPreferences;
    LinearLayout kalendarlenar, linearresiko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_assessment);
        HttpsTrustManager.allowAllSSL();

        textdate = findViewById(R.id.textdate);
        kalendarlenar = findViewById(R.id.kalendarlenar);

        resiko = findViewById(R.id.resiko);
        keteranganresiko = findViewById(R.id.keteranganresiko);
        linearresiko = findViewById(R.id.linearresiko);
        tekstambahan = findViewById(R.id.tekstambahan);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/assessment/index?nik_baru="+nik_baru+"&tanggal=" + currentDateandTime,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);
                                int a = Integer.parseInt(movieObject.getString("hasil"));
                                if(0 <= a && a <= 4){
                                    resiko.setText("Tingkat Resiko Rendah");
                                    keteranganresiko.setText("Boleh masuk kerja, dengan catatan suhu tubuh tidak lebih dari 37.3° Celcius, Wajib tetap melakukan Protokol Kesehatan di Tempat Kerja.");
                                    kalendarlenar.setBackgroundColor(Color.parseColor("#C8FFD7"));
                                    linearresiko.setBackgroundColor(Color.parseColor("#C8FFD7"));
                                    tekstambahan.setText("Tetap jaga kesehatan anda dan jalankan protokol\n" +
                                            "kesehatan dengan menggunakan masker, menjaga\n" +
                                            "jarak, dan tidakberkerumun.");
                                } else if (5 <= a && a <= 11){
                                    resiko.setText("Tingkat Resiko Sedang");
                                    keteranganresiko.setText("Boleh masuk kerja, dengan catatan suhu tubuh tidak lebih dari 37.3° Celcius, dan tidak menunjukan gejala. ajib tetap melakukan Protokol Kesehatan di Tempat Kerja.");
                                    kalendarlenar.setBackgroundColor(Color.parseColor("#FFFF00"));
                                    linearresiko.setBackgroundColor(Color.parseColor("#FFFF00"));
                                    tekstambahan.setText("- Diminta melaporkan hasil pengecekan mandiri (suhu dan gejala-gejala) selama 14 hari ke depan kepada atasan Ybs.\n- Mengikuti kebijakan Perusahaan yang telah ditetapkan\n- Atasan/PIC Depo harus memantau kondisi kesehatan Karyawan tsb");
                                } else if (12 <= a){
                                    resiko.setText("Tingkat Resiko Tinggi");
                                    keteranganresiko.setText("Tidak boleh masuk kerja, lakukan test swab dan isolasi mandiri/WFH. Silahkan segera hubungi fasyankes setempat untuk melakukan konsultasi dan tindakan selanjutnya.");
                                    kalendarlenar.setBackgroundColor(Color.parseColor("#FFE5E5"));
                                    linearresiko.setBackgroundColor(Color.parseColor("#FFE5E5"));
                                    tekstambahan.setText("- Diminta melaporkan kondisi selama isolasi 14 hari ke depan kepada atasan Ybs.\n- Mengikuti kebijakan Perusahaan yang telah ditetapkan\n- Atasan/PIC Depo harus memantau kondisi kesehatan Karyawan tsb");
                                }

                                String bobotjumlah = movieObject.getString("hasil");
                                textdate.setText(bobotjumlah);

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
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(getBaseContext(), lainnya.class);
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(back);
    }
}