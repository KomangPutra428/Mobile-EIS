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
import android.widget.Button;
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
import com.example.eis2.Item.approvalcutikhusus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.eis2.cuti.txt_location;
import static com.example.eis2.cuti.txt_nomor_jabatan;
import static com.example.eis2.dinas.txt_jabatan2;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class approval_cuti_khusus_no extends AppCompatActivity {
    public static ListView list;
    private List<approvalcutikhusus> movieItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_cuti_khusus_no);
        HttpsTrustManager.allowAllSSL();

        list = findViewById(R.id.list);
        movieItemList = new ArrayList<>();

        approvedinas();
    }

    private void approvedinas() {
        String id = text_jabatan.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/cuti_khusus/index_atasan?jabatan_struktur=" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final approvalcutikhusus movieItem = new approvalcutikhusus(
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

                                movieItemList.add(movieItem);

                                ListViewAdapterCutiKhusus adapter = new ListViewAdapterCutiKhusus(movieItemList, getApplicationContext());

                                if (!movieObject.getString("lokasi_struktur").equalsIgnoreCase(txt_lokasi.getText().toString()) ||
                                        (!movieObject.getString("status_cuti_khusus").equalsIgnoreCase("2"))) {
                                    movieItemList.remove(movieItem);
                                    adapter.notifyDataSetChanged();
                                } else if (movieObject.getString("lokasi_struktur").equalsIgnoreCase("pusat")) {
                                    adapter.notifyDataSetChanged();
                                }

                                Collections.sort(movieItemList, new Comparator<approvalcutikhusus>() {
                                    public int compare(approvalcutikhusus o1, approvalcutikhusus o2) {
                                        if (o1.getId_cuti_khusus() == null || o2.getId_cuti_khusus() == null)
                                            return 0;
                                        return o1.getId_cuti_khusus().compareTo(o2.getId_cuti_khusus());
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

    public static class ListViewAdapterCutiKhusus extends ArrayAdapter<approvalcutikhusus> {

        List<approvalcutikhusus> movieItemList;

        private Context context;

        public ListViewAdapterCutiKhusus(List<approvalcutikhusus> movieItemList, Context context) {
            super(context, R.layout.list_view_approval_khusus, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_view_approval_khusus, null, true);

            listViewItem.setBackground(getContext().getDrawable(R.drawable.border));

            TextView id = listViewItem.findViewById(R.id.id);
            TextView nik = listViewItem.findViewById(R.id.nik);
            TextView waktu = listViewItem.findViewById(R.id.waktu);
            TextView nama = listViewItem.findViewById(R.id.nama);
            TextView tanggal = listViewItem.findViewById(R.id.tanggal);
            TextView lokasi = listViewItem.findViewById(R.id.lokasi);

            TextView keterangan = listViewItem.findViewById(R.id.keterangan);
            TextView opsi = listViewItem.findViewById(R.id.opsi);
            TextView status1 = listViewItem.findViewById(R.id.status1);
            TextView feedBack1 = listViewItem.findViewById(R.id.feedback1);

            approvalcutikhusus movieItem = getItem(position);

            id.setText(movieItem.getId_cuti_khusus());
            nik.setText(movieItem.getNik_baru());
            waktu.setText(movieItem.getTanggal_pengajuan());
            nama.setText(movieItem.getNama_karyawan_struktur());
            tanggal.setText(movieItem.getStart_cuti_khusus());
            lokasi.setText(movieItem.getLokasi_struktur());

            keterangan.setText(movieItem.getKet_tambahan_khusus());
            opsi.setText(movieItem.getJenis_cuti_khusus());
            status1.setText(movieItem.getStatus_cuti_khusus());
            feedBack1.setText(movieItem.getFeedback_cuti_khusus());

            if ("2".equals(status1.getText().toString())) {
                status1.setText("Not Approved");
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