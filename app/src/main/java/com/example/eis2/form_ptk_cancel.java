package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class form_ptk_cancel extends AppCompatActivity {

    EditText jabatan, analisa, tenaga, keterangantambahan, nomor_pengajuan;
    ImageButton approve;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_ptk_cancel);
        HttpsTrustManager.allowAllSSL();
        approve = findViewById(R.id.approve);

        jabatan = findViewById(R.id.jabatan);
        analisa = findViewById(R.id.analisa);
        tenaga = findViewById(R.id.tenaga);
        keterangantambahan = findViewById(R.id.keterangantambahan);
        nomor_pengajuan = findViewById(R.id.nomor_pengajuan);



        jabatan.setText(getIntent().getStringExtra("jabatan_karyawan"));
        analisa.setText(getIntent().getStringExtra("analisa"));
        tenaga.setText(getIntent().getStringExtra("tenaga_kerja"));
        nomor_pengajuan.setText(getIntent().getStringExtra("id_pengajuan"));

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(form_ptk_cancel.this);
                showDialog();
                pDialog.setContentView(R.layout.progress_dialog);
                pDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server/pengajuan/Ptk/index_cancel",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "sudah di update", Toast.LENGTH_LONG).show();
                                form_ptk_cancel.this.finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "maaf ada kesalahan", Toast.LENGTH_LONG).show();
                            }
                        }
                ) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        String creds = String.format("%s:%s", "admin", "Databa53");
                        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                        params.put("Authorization", auth);
                        return params;
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        String idkaryawan = getIntent().getStringExtra("id_pengajuan");

                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String formattedDate = df.format(c);
                        params.put("no_pengajuan", idkaryawan);

                        params.put("status", "1");

                        params.put("tanggal_cancel", formattedDate);
                        params.put("ket_cancel", keterangantambahan.getText().toString());

                        System.out.println("Params Cancel = " + params);
                        return params;
                    }

                };
                stringRequest.setRetryPolicy(
                        new DefaultRetryPolicy(
                                500000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        ));
                RequestQueue requestQueue = Volley.newRequestQueue(form_ptk_cancel.this);
                requestQueue.add(stringRequest);
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
}