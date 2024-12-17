package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.approvaldinasnonfullmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.eis2.dinas.txt_jabatan2;
import static com.example.eis2.dinas.txt_nomor_jab;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class approval_dinas_nonfull_yes extends AppCompatActivity {
    public static ListView list;
    private List<approvaldinasnonfullmodel> movieItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_dinas_nonfull_yes);
        HttpsTrustManager.allowAllSSL();
        list = findViewById(R.id.list);
        movieItemList = new ArrayList<>();

        approvedinas();
    }

    private void approvedinas() {
        String jabatan = text_jabatan.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/dinas_non_full_day/index_atasan?jabatan_struktur=" + jabatan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

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

                                movieItemList.add(movieItem);

                                ListViewAdapterDinasNonFullDay adapter = new ListViewAdapterDinasNonFullDay(movieItemList, getApplicationContext());

                                if (!movieObject.getString("lokasi_struktur").equalsIgnoreCase(txt_lokasi.getText().toString()) ||
                                        (!movieObject.getString("status_non_full").equalsIgnoreCase("1"))) {
                                    movieItemList.remove(movieItem);
                                    adapter.notifyDataSetChanged();
                                } else if (movieObject.getString("lokasi_struktur").equalsIgnoreCase("pusat")) {
                                    adapter.notifyDataSetChanged();
                                }

                                Collections.sort(movieItemList, new Comparator<approvaldinasnonfullmodel>() {
                                    public int compare(approvaldinasnonfullmodel o1, approvaldinasnonfullmodel o2) {
                                        if (o1.getId_non_full() == null || o2.getId_non_full() == null)
                                            return 0;
                                        return o1.getId_non_full().compareTo(o2.getId_non_full());
                                    }
                                });

                                list.setAdapter(adapter);
                                list.setStackFromBottom(true);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Belum ada Pengajuan", Toast.LENGTH_SHORT).show();
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static class ListViewAdapterDinasNonFullDay extends ArrayAdapter<approvaldinasnonfullmodel> {

        List<approvaldinasnonfullmodel> movieItemList;

        private Context context;

        public ListViewAdapterDinasNonFullDay(List<approvaldinasnonfullmodel> movieItemList, Context context) {
            super(context, R.layout.list_view_dinas_nonfull, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_view_dinas_nonfull, null, true);

            listViewItem.setBackground(getContext().getDrawable(R.drawable.border));

            TextView id = listViewItem.findViewById(R.id.id);
//            TextView tanggal_pengajuan = listViewItem.findViewById(R.id.tanggal_pengajuan);
            TextView nama = listViewItem.findViewById(R.id.nama);
            TextView lokasi = listViewItem.findViewById(R.id.lokasi);
            TextView jenis = listViewItem.findViewById(R.id.jenis);
            TextView tanggalizin = listViewItem.findViewById(R.id.tanggalizin);
            TextView keterangan = listViewItem.findViewById(R.id.keterangan);
            TextView status1 = listViewItem.findViewById(R.id.status1);
            TextView feedbackatasan1 = listViewItem.findViewById(R.id.feedbackatasan1);

            approvaldinasnonfullmodel movieItem = getItem(position);

            id.setText(movieItem.getId_non_full());
//            tanggal_pengajuan.setText(movieItem.getTanggal_pengajuan());
            nama.setText(movieItem.getNama_karyawan_struktur());
            lokasi.setText(movieItem.getLokasi_struktur());
            jenis.setText(movieItem.getJenis_non_full());
            tanggalizin.setText(movieItem.getTanggal_non_full());
            keterangan.setText(movieItem.getKet_tambahan_non_full());
            status1.setText(movieItem.getStatus_non_full());
            feedbackatasan1.setText(movieItem.getFeedback_non_full());

            if ("1".equals(status1.getText().toString())) {
                status1.setText("Approved");
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
