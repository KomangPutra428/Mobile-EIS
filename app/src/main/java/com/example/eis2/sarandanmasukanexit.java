package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.example.eis2.Item.CDTmodel;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.Utility;
import com.example.eis2.Item.jawabanexit;
import com.example.eis2.Item.sarandanmasukanexxitmodel;
import com.example.eis2.Item.soalmodel;
import com.example.eis2.SearchSpinner.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.eis2.Item.LoginItem.KEY_JABATAN;
import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;

public class sarandanmasukanexit extends AppCompatActivity {
    ArrayList<sarandanmasukanexxitmodel> sarandanmasukanexxitmodelList = new ArrayList<>();
    ListView list;
    ImageButton selesai;
    DataExitMasuk adapter;
    ProgressDialog pDialog;
    EditText tambahan, ketidaksesuaian;
    List<jawabanexit> soalexitmodels = new ArrayList<>();

    RadioButton opsi1, opsi2, opsi3, opsi4;
    RadioGroup alasanresignpilihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sarandanmasukanexit);
        HttpsTrustManager.allowAllSSL();

        list = findViewById(R.id.list);
        selesai = findViewById(R.id.simpankritik);
        alasanresignpilihan = findViewById(R.id.alasanresignpilihan);
        tambahan = findViewById(R.id.tambahan);
        ketidaksesuaian = findViewById(R.id.ketidaksesuaian);
        opsi1 = findViewById(R.id.opsi1);
        opsi2 = findViewById(R.id.opsi2);
        opsi3 = findViewById(R.id.opsi3);
        opsi4 = findViewById(R.id.opsi4);

        sarandanmasukanexxitmodelList.add(new sarandanmasukanexxitmodel("1", ""));
        sarandanmasukanexxitmodelList.add(new sarandanmasukanexxitmodel("2", ""));
        sarandanmasukanexxitmodelList.add(new sarandanmasukanexxitmodel("3", ""));
        adapter = new DataExitMasuk(sarandanmasukanexxitmodelList, sarandanmasukanexit.this);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Utility.setListViewHeightBasedOnChildren(list);

    }

    private void postsarandanmasukan() {
        pDialog = new ProgressDialog(sarandanmasukanexit.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
            }
        };
        handler.postDelayed(r, 200);

        final int x = sarandanmasukanexxitmodelList.size() - 1;
        for (int i = 0; i <= x; i++) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final int finalI = i;
            final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_exitinterviewsaran",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (finalI == x) {
                                postfinal();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Maaf Ada Kesalahan", Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", "admin", "Databa53");
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    String nik_baru = getIntent().getStringExtra(KEY_NIK);

                    params.put("nik_baru", menu_exitinterview.nik.getText().toString());
                    params.put("nomor_saran", adapter.getItem(finalI).getNomor_saran());
                    params.put("saran", adapter.getItem(finalI).getSaran());



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

            RequestQueue requestQueue2 = Volley.newRequestQueue(sarandanmasukanexit.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void postfinal() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_exitinterviewfinal",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "Data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Maaf Ada Kesalahan", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("nik_baru", menu_exitinterview.nik.getText().toString());
                if(opsi1.isChecked()){
                    params.put("alasan_exit", "Mendapat Pekerjaan Baru");
                } else if(opsi2.isChecked()){
                    params.put("alasan_exit", "Melanjutkan Study");
                } else if(opsi3.isChecked()){
                    params.put("alasan_exit", "Berwiraswasta");
                } else if(opsi4.isChecked()){
                    params.put("alasan_exit", "Kesehatan");
                }
                params.put("tambahan_exit", tambahan.getText().toString());
                params.put("ketidaksesuai_exit", ketidaksesuaian.getText().toString());



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

        RequestQueue requestQueue2 = Volley.newRequestQueue(sarandanmasukanexit.this);
        requestQueue2.add(stringRequest2);
    }

    public class DataExitMasuk extends ArrayAdapter<sarandanmasukanexxitmodel> {

        private class ViewHolder {
            SearchableSpinner jawaban;
            EditText keterangantambahan;
            ArrayList<String> Jawaban;
        }
        List<sarandanmasukanexxitmodel> dataModels;
        private Context context;

        public DataExitMasuk(List<sarandanmasukanexxitmodel> dataModels, Context context) {
            super(context, R.layout.listitem_sarandanmasukan, dataModels);
            this.dataModels = dataModels;
            this.context = context;

        }

        public int getCount() {
            return dataModels.size();
        }

        public sarandanmasukanexxitmodel getItem(int position) {
            return dataModels.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            sarandanmasukanexxitmodel movieItem = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.listitem_sarandanmasukan, parent, false);

                viewHolder.jawaban = (SearchableSpinner) convertView.findViewById(R.id.jawaban);
                viewHolder.keterangantambahan = (EditText) convertView.findViewById(R.id.keterangantambahan);
                viewHolder.Jawaban = new ArrayList<>();

                convertView.setTag(viewHolder);

                selesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position == 0){
                            if (viewHolder.keterangantambahan.getText().toString().length() == 0) {
                                Toast.makeText(getApplicationContext(), "Isi salah satu keterangan minimal 1", Toast.LENGTH_SHORT).show();
                            }
                        } else if (alasanresignpilihan.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(getApplicationContext(), "Silahkan Pilih Salah Satu Alasan", Toast.LENGTH_LONG).show();
                        } else if (tambahan.getText().toString().length() == 0){
                            tambahan.setError("Silahkan isi tambahan");
                        } else if (ketidaksesuaian.getText().toString().length() == 0){
                            ketidaksesuaian.setError("Silahkan isi ketidak sesuaian");
                        } else {
                            postsarandanmasukan();
                        }
                    }
                });

                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_soalexit", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        viewHolder.Jawaban.add("== Pilih Jawaban ==");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String id = jsonObject1.getString("id");
                                    String soal = jsonObject1.getString("soal");
                                    viewHolder.Jawaban.add(id + ". " + soal);

                                }
                            }
                            viewHolder.jawaban.setTitle("Pilih Jawaban");
                            viewHolder.jawaban.setAdapter(new ArrayAdapter<String>(sarandanmasukanexit.this, android.R.layout.simple_spinner_dropdown_item, viewHolder.Jawaban));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                RequestQueue requestQueue = Volley.newRequestQueue(sarandanmasukanexit.this);
                requestQueue.add(stringRequest);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.jawaban.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String split = viewHolder.jawaban.getSelectedItem().toString();
                    String[] splited_text2 = split.split("\\.");
                    split = splited_text2[0];
                    System.out.println("Hasil = " + split);


                    getItem(position).setNomor_saran(split);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            viewHolder.keterangantambahan.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    getItem(position).setSaran(s.toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getItem(position).setSaran(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    getItem(position).setSaran(s.toString());
                }
            });


            return convertView;

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