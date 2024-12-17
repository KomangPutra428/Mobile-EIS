package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
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
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Paginator;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.Utility;
import com.example.eis2.Item.soalmodel;
import com.google.android.gms.common.data.DataHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.Item.Paginator.ITEMS_PER_PAGE;
import static com.example.eis2.menu.txt_alpha;

public class soalexitinterview extends AppCompatActivity {
    ListView list;
    List<soalexitmodel> soalexitmodels = new ArrayList<>();
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;
    ListViewAdapterSoal adapter;
    Button selesai, kembali, lanjutkan;
    Paginator p = new Paginator();
    private int totalPages =p.getTotalPages();
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soalexitinterview);
        HttpsTrustManager.allowAllSSL();
        list = findViewById(R.id.list);
        selesai = findViewById(R.id.selesai);

        kembali = findViewById(R.id.kembali);
        lanjutkan = findViewById(R.id.lanjutkan);
        getData();
    }

    private void postexit() {
        pDialog = new ProgressDialog(soalexitinterview.this);
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

        final int x = soalexitmodels.size() - 1;
        for (int i = 0; i <= x; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final int finalI = i;
            final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_exitinterview",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (finalI == x) {
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "Data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                finish();
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
                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                    String nik_baru = sharedPreferences.getString(KEY_NIK, null);

                    params.put("nik_baru", menu_exitinterview.nik.getText().toString());
                    params.put("id_soal", adapter.getItem(finalI).getId());
                    params.put("jawaban_soal", adapter.getItem(finalI).getNoJawaban());
                    params.put("keterangan", adapter.getItem(finalI).getKeteranganjawaban());



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

            RequestQueue requestQueue2 = Volley.newRequestQueue(soalexitinterview.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void getData() {
        pDialog = new ProgressDialog(soalexitinterview.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_soalexit",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;



                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final soalexitmodel movieItem = new soalexitmodel(
                                        movieObject.getString("id"),
                                        movieObject.getString("soal"),
                                        movieObject.getString("jawaban"));

                                soalexitmodels.add(movieItem);
                                adapter = new ListViewAdapterSoal(soalexitmodels, getApplicationContext());
                                list.setAdapter(adapter);
                                Utility.setListViewHeightBasedOnChildren(list);
                                adapter.notifyDataSetChanged();
                            }
                            hideDialog();
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
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public class ListViewAdapterSoal extends ArrayAdapter<soalexitmodel> {

        private class ViewHolder {
            TextView keterangan;
            Spinner jawaban;
            EditText keterangantambahan;
        }
        List<soalexitmodel> dataModels;
        private Context context;

        public ListViewAdapterSoal(List<soalexitmodel> dataModels, Context context) {
            super(context, R.layout.list_item_soalexit, dataModels);
            this.dataModels = dataModels;
            this.context = context;

        }

        public int getCount() {
            return dataModels.size();
        }

        public soalexitmodel getItem(int position) {
            return dataModels.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            soalexitmodel movieItem = getItem(position);
            final ListViewAdapterSoal.ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ListViewAdapterSoal.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_soalexit, parent, false);

                viewHolder.keterangan = (TextView) convertView.findViewById(R.id.keterangan);
                viewHolder.jawaban = (Spinner) convertView.findViewById(R.id.jawaban);
                viewHolder.keterangantambahan = (EditText) convertView.findViewById(R.id.keterangantambahan);
                if(movieItem.getJawaban().equals("1")) {
                    String[] arraySpinner = new String[]{
                            "== Pilih Jawaban ==","Sangat Memahami", "Memahami", "Kurang Memahami", "Tidak Memahami"
                    };

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arraySpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    viewHolder.jawaban.setAdapter(adapter);
                } else if (movieItem.getJawaban().equals("2")) {
                    String[] arraySpinner = new String[]{
                            "== Pilih Jawaban ==","Selalu", "Kadang"
                    };

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arraySpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    viewHolder.jawaban.setAdapter(adapter);
                } else {
                    String[] arraySpinner = new String[]{
                            "== Pilih Jawaban ==","Ya", "Tidak"
                    };

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arraySpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    viewHolder.jawaban.setAdapter(adapter);
                }


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ListViewAdapterSoal.ViewHolder) convertView.getTag();
            }


            selesai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < dataModels.size(); i++) {
                        if((getItem(i).getNoJawaban().equals("0") || getItem(i).getKeteranganjawaban() == null )|| (getItem(i).getNoJawaban().equals("0") && getItem(i).getKeteranganjawaban() == null)){
                            Toast.makeText(getApplicationContext(), "Silahkan pilih jawaban terlebih dahulu", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            System.out.println("Hasil Looping = " + i);
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if(dataModels.size() -1 == i) {
                                pDialog = new ProgressDialog(soalexitinterview.this);
                                showDialog();
                                pDialog.setContentView(R.layout.progress_dialog);
                                pDialog.getWindow().setBackgroundDrawableResource(
                                        android.R.color.transparent
                                );
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        postexit();
                                    }
                                }, 2000);
                            }
                        }
                    }
                }
            });


            viewHolder.keterangan.setText( movieItem.getId() +". "+movieItem.getSoal());
            viewHolder.jawaban.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    System.out.println("Hasil = " + viewHolder.jawaban.getSelectedItemId());
                    getItem(position).setNoJawaban(String.valueOf(viewHolder.jawaban.getSelectedItemId()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            viewHolder.keterangantambahan.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    getItem(position).setKeteranganjawaban(s.toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getItem(position).setKeteranganjawaban(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    getItem(position).setKeteranganjawaban(s.toString());
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