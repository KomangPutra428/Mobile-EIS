package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.approvalfulldaymodel;
import com.example.eis2.Item.approvalnonfullmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.izin.txt_jabatan;
import static com.example.eis2.izin.txt_nomor;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class nonfull_yesapprove extends AppCompatActivity {
    public ListView listok;
    private List<approvalnonfullmodel> movieItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nonfull_yesapprove);
        HttpsTrustManager.allowAllSSL();
        listok = findViewById(R.id.list);
        movieItemList = new ArrayList<>();

        fullday();
    }

    private void fullday() {
        String lokasi = text_jabatan.getText().toString().trim();
        System.out.println("Test" + lokasi);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/izin_non_full_day/index_atasan?jabatan_struktur=" + lokasi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final approvalnonfullmodel movieItem = new approvalnonfullmodel(
                                        movieObject.getString("id_non_full"),
                                        movieObject.getString("no_pengajuan_non_full"),
                                        movieObject.getString("tanggal_pengajuan"),
                                        movieObject.getString("lokasi_struktur"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("jenis_non_full"),
                                        movieObject.getString("tanggal_non_full"),
                                        movieObject.getString("ket_tambahan_non_full"),
                                        movieObject.getString("status_non_full"),
                                        movieObject.getString("feedback_non_full"));

                                movieItemList.add(movieItem);
                                ListViewAdapterApprovalNonFull adapter = new ListViewAdapterApprovalNonFull(movieItemList, getApplicationContext());

                                if (!movieObject.getString("lokasi_struktur").equalsIgnoreCase(txt_lokasi.getText().toString()) ||
                                        (!movieObject.getString("status_non_full").equalsIgnoreCase("1"))) {
                                    movieItemList.remove(movieItem);
                                    adapter.notifyDataSetChanged();
                                } else if (movieObject.getString("lokasi_struktur").equalsIgnoreCase("pusat")){
                                    adapter.notifyDataSetChanged();
                                }
                                Collections.sort(movieItemList, new Comparator<approvalnonfullmodel>() {
                                    public int compare(approvalnonfullmodel o1, approvalnonfullmodel o2) {
                                        if (o1.getId_non_full() == null || o2.getId_non_full() == null)
                                            return 0;
                                        return o1.getId_non_full().compareTo(o2.getId_non_full());
                                    }
                                });
                                listok.setAdapter(adapter);
                                listok.setStackFromBottom(true);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Belum ada pengajuan", Toast.LENGTH_SHORT).show();
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
        requestQueue.add(stringRequest);
    }
    public class ListViewAdapterApprovalNonFull extends ArrayAdapter<approvalnonfullmodel> {
        private List<approvalnonfullmodel> movieItemList;

        private Context context;

        public ListViewAdapterApprovalNonFull(List<approvalnonfullmodel> movieItemList, Context context) {
            super(context, R.layout.list_item_approval_nonfull, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }
        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_approval_nonfull, null, true);

            listViewItem.setBackground(getContext().getDrawable(R.drawable.border));

            TextView id = listViewItem.findViewById(R.id.id);
            TextView nama = listViewItem.findViewById(R.id.nama);
            TextView lokasi = listViewItem.findViewById(R.id.lokasi);
            TextView jenis = listViewItem.findViewById(R.id.jenis);
            TextView tanggal = listViewItem.findViewById(R.id.tanggal);
            TextView keterangan = listViewItem.findViewById(R.id.keterangan);
            TextView status = listViewItem.findViewById(R.id.status);
            TextView feedback = listViewItem.findViewById(R.id.feedback);

            approvalnonfullmodel movieItem = movieItemList.get(position);

            id.setText(movieItem.getId_non_full());
            nama.setText(movieItem.getNama_karyawan_struktur());
            lokasi.setText(movieItem.getLokasi_struktur());
            jenis.setText(movieItem.getJenis_non_full());
            tanggal.setText(movieItem.getTanggal_non_full());
            keterangan.setText(movieItem.getKet_tambahan_non_full());
            status.setText(movieItem.getStatus_non_full());
            feedback.setText(movieItem.getFeedback_non_full());

            if ("1".equals(status.getText().toString())) {
                status.setText("Approve");
                this.notifyDataSetChanged();
            }

            return listViewItem;
        }
    }
    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());

        
        super.onDestroy();
    }
}
