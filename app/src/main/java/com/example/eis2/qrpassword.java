package com.example.eis2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.eis2.Item.passwordmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;

public class qrpassword extends AppCompatActivity {
    EditText editTextpassword;
    ProgressDialog pDialog;
    ImageButton buttonlogin;
    SharedPreferences sharedPreferences;
    private List<passwordmodel> movieItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrpassword);
        HttpsTrustManager.allowAllSSL();
        buttonlogin = (ImageButton) findViewById(R.id.login);
        editTextpassword = (EditText) findViewById(R.id.editTextpassword);
        movieItemList = new ArrayList<>();

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (editTextpassword.getText().toString().length() == 0) {
                    editTextpassword.setError("Password diperlukan!");
                } else {
                    sendLogin();
                }
            }
        });
    }

    private void sendLogin() {
        String oldpassword = editTextpassword.getText().toString();
        final String password = md5(oldpassword);
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        pDialog = new ProgressDialog(qrpassword.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        String nik_baru = sharedPreferences.getString(KEY_NIK ,null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                passwordmodel movieItem = new passwordmodel(
                                        movieObject.getString("password"));
                                if (movieObject.getString("password").equals(password)) {
                                    hideDialog();
                                    Toast.makeText(getApplicationContext(), "Selamat Datang", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(qrpassword.this, menu.class);
                                    startActivity(intent);
                                } else if (!movieObject.getString("password").equals(password)) {
                                    hideDialog();
                                    Toast.makeText(getApplicationContext(), "password tidak sama", Toast.LENGTH_LONG).show();
                                }
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
                        Toast.makeText(getApplicationContext(), "password salah atau jaringan bermasalah", Toast.LENGTH_SHORT).show();
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