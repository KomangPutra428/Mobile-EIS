package com.example.eis2;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.LoginItem;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.passwordmodel;
import com.example.eis2.firebase.checking_foreground;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.Item.LoginItem.KEY_PASSWORD;
import static com.example.eis2.izin.txt_nomor;
import static com.example.eis2.menu.txt_alpha;


public class MainActivity extends AppCompatActivity {

    public static EditText editTextnik_baru;
    EditText editTextpassword;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    ImageButton qr, login;
//    UpdateManager mUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpsTrustManager.allowAllSSL();




//        mUpdateManager = UpdateManager.Builder(MainActivity.this).mode(UpdateManagerConstant.IMMEDIATE);
//        mUpdateManager.start();

        editTextnik_baru = (EditText) findViewById(R.id.editTextnik_baru);
        editTextpassword = (EditText) findViewById(R.id.editTextpassword);
        login = (ImageButton) findViewById(R.id.login);
        qr = (ImageButton) findViewById(R.id.qrlogin);

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.initiateScan();
            }
        });


        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        sharedPreferences.contains(KEY_NIK);

        if (sharedPreferences.contains(KEY_NIK)) {
            pDialog = new ProgressDialog(MainActivity.this);
            showDialog();
            pDialog.setContentView(R.layout.progress_dialog);
            pDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            String nik_baru = sharedPreferences.getString(KEY_NIK, null);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + nik_baru,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("status").equals("true")) {
                                    hideDialog();
                                    Intent intent = new Intent(MainActivity.this, menu.class);
                                    startActivity(intent);
                                } else {
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
                            hideDialog();
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
        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (editTextnik_baru.getText().toString().length() == 0 && editTextpassword.getText().toString().length() == 0) {
                    editTextnik_baru.setError("NIK diperlukan!");
                    editTextpassword.setError("Password diperlukan!");
                } else if (editTextnik_baru.getText().toString().length() == 0) {
                    editTextnik_baru.setError("NIK diperlukan!");
                } else if (editTextpassword.getText().toString().length() == 0) {
                    editTextpassword.setError("Password diperlukan!");
                } else {
                    sendLogin();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();
            }else{
                try{
                    JSONObject object = new JSONObject(result.getContents());
                    System.out.println(object.getString("nik"));
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();

                    final String nik = result.getContents();
                    pDialog = new ProgressDialog(MainActivity.this);
                    showDialog();
                    pDialog.setContentView(R.layout.progress_dialog);
                    pDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + result.getContents(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    hideDialog();
                                    Intent intent = new Intent(MainActivity.this, qrpassword.class);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_NIK, nik);
                                    editor.apply();
                                    startActivity(intent);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    hideDialog();
                                    Toast.makeText(getApplicationContext(), "Hasil tidak ditemukan atau jaringan error", Toast.LENGTH_SHORT).show();
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
                    stringRequest2.setRetryPolicy(
                            new DefaultRetryPolicy(
                                    500000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                            )
                    );

                    RequestQueue requestQueue2 = Volley.newRequestQueue(this);
                    requestQueue2.add(stringRequest2);
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void sendLogin() {
        pDialog = new ProgressDialog(MainActivity.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + editTextnik_baru.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                JSONArray movieArray = obj.getJSONArray("data");

                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    if (movieObject.getString("password").equals(md5(editTextpassword.getText().toString()))) {
                                        Toast.makeText(getApplicationContext(), "Selamat Datang", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(MainActivity.this, menu.class);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(KEY_NIK, editTextnik_baru.getText().toString());
                                        editor.apply();
                                        hideDialog();
                                        startActivity(intent);
                                    }
                                    else if (!movieObject.getString("password").equals(md5(editTextpassword.getText().toString()))) {
                                        hideDialog();
                                        Toast.makeText(getApplicationContext(), "password tidak sama", Toast.LENGTH_LONG).show();
                                    }
                                }

                            } else {
                                hideDialog();
                                Toast.makeText(MainActivity.this, "NIK Tidak sesuai", Toast.LENGTH_SHORT).show();
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
                            System.out.println("Alasan " + error);
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("Anda yakin untuk keluar dari aplikasi ini ?");
        alertDialogBuilder
                .setMessage("Klik Ya untuk keluar!")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public String md5(String s) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(s.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2*hash.length);
            for(byte b : hash)
            {
                sb.append(String.format("%02x", b&0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(setting.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(setting.class.getName()).log(Level.SEVERE, null, ex);
        }
        return digest;
    }
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

        
        super.onDestroy();
    }

}
