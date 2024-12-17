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
import com.example.eis2.Item.approvaldinasfull;

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

public class approval_dinas_full_yes extends AppCompatActivity {
    public static ListView list;
    private List<approvaldinasfull> movieItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_dinas_full_yes);
        HttpsTrustManager.allowAllSSL();
        list = findViewById(R.id.list);
        movieItemList = new ArrayList<>();
        approvedinas();
    }

    private void approvedinas() {
        String jabatan = text_jabatan.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/dinas_full_day/index_atasan?jabatan_struktur=" + jabatan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final approvaldinasfull movieItem = new approvaldinasfull(
                                        movieObject.getString("id_full_day"),
                                        movieObject.getString("tanggal_pengajuan"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("lokasi_struktur"),
                                        movieObject.getString("jenis_full_day"),
                                        movieObject.getString("start_full_day"),
                                        movieObject.getString("karyawan_pengganti"),
                                        movieObject.getString("ket_tambahan"),
                                        movieObject.getString("status_full_day"),
                                        movieObject.getString("feedback_full_day"));

                                movieItemList.add(movieItem);

                                final ListViewAdapterDinasFullDay adapter = new ListViewAdapterDinasFullDay(movieItemList, getApplicationContext());
                                if (!movieObject.getString("lokasi_struktur").equalsIgnoreCase(txt_lokasi.getText().toString()) ||
                                        (!movieObject.getString("status_full_day").equalsIgnoreCase("1"))) {
                                    movieItemList.remove(movieItem);
                                    adapter.notifyDataSetChanged();
                                } else if (movieObject.getString("lokasi_struktur").equalsIgnoreCase("pusat")) {
                                    adapter.notifyDataSetChanged();
                                }

                                Collections.sort(movieItemList, new Comparator<approvaldinasfull>() {
                                    public int compare(approvaldinasfull o1, approvaldinasfull o2) {
                                        if (o1.getId_full_day() == null || o2.getId_full_day() == null)
                                            return 0;
                                        return o1.getId_full_day().compareTo(o2.getId_full_day());
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
    public void refreshActivity() {
        Intent i = new Intent(this, approval_izin.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }


    public static class ListViewAdapterDinasFullDay extends ArrayAdapter<approvaldinasfull> {

        List<approvaldinasfull> movieItemList;

        private Context context;

        public ListViewAdapterDinasFullDay(List<approvaldinasfull> movieItemList, Context context) {
            super(context, R.layout.list_item_dinas_fullday, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_dinas_fullday, null, true);

            listViewItem.setBackground(getContext().getDrawable(R.drawable.border));

            TextView id = listViewItem.findViewById(R.id.id);
            TextView tanggaldeadline = listViewItem.findViewById(R.id.deadline);
            TextView nama = listViewItem.findViewById(R.id.nama);
            TextView lokasi = listViewItem.findViewById(R.id.lokasi);
            TextView jenis = listViewItem.findViewById(R.id.jenis);
            TextView tanggalizin = listViewItem.findViewById(R.id.tanggalizin);
            TextView karyawanpengganti = listViewItem.findViewById(R.id.karyawanpengganti);
            TextView keterangan = listViewItem.findViewById(R.id.keterangan);
            TextView status1 = listViewItem.findViewById(R.id.status1);
            TextView feedbackatasan1 = listViewItem.findViewById(R.id.feedbackatasan1);

            approvaldinasfull movieItem = getItem(position);

            id.setText(movieItem.getId_full_day());
            tanggaldeadline.setText(movieItem.getTanggal_pengajuan());
            nama.setText(movieItem.getNama_karyawan_struktur());
            lokasi.setText(movieItem.getLokasi_struktur());
            jenis.setText(movieItem.getJenis_full_day());
            tanggalizin.setText(movieItem.getStart_full_day());
            karyawanpengganti.setText(movieItem.getKaryawan_pengganti());
            keterangan.setText(movieItem.getKet_tambahan());
            status1.setText(movieItem.getStatus_full_day());
            feedbackatasan1.setText(movieItem.getFeedback_full_day());


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