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
import android.widget.ArrayAdapter;
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
import com.example.eis2.Item.approvalcutitahunan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.eis2.cuti.txt_location;
import static com.example.eis2.cuti.txt_nomor_jabatan;
import static com.example.eis2.dinas.txt_jabatan2;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;

public class approval_cuti_tahunan_yes extends AppCompatActivity {
    public static ListView list_item_cuti;
    private List<approvalcutitahunan> movieItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_cuti_tahunan_yes);
        HttpsTrustManager.allowAllSSL();

        list_item_cuti = findViewById(R.id.list_item_cuti);
        movieItemList = new ArrayList<>();
        approvedinas();
    }

    private void approvedinas() {
        String id = text_jabatan.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/cuti_tahunan/index_atasan?jabatan_struktur=" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final approvalcutitahunan movieItem = new approvalcutitahunan(
                                        movieObject.getString("id_sisa_cuti"),
                                        movieObject.getString("tanggal_pengajuan"),
                                        movieObject.getString("nama_karyawan_struktur"),
                                        movieObject.getString("start_cuti_tahunan"),
                                        movieObject.getString("lokasi_struktur"),
                                        movieObject.getString("ket_tambahan_tahunan"),
                                        movieObject.getString("opsi_cuti_tahunan"),
                                        movieObject.getString("status_cuti_tahunan"),
                                        movieObject.getString("feedback_cuti_tahunan"));

                                movieItemList.add(movieItem);

                                ListViewAdapterCutiTahunan adapter = new ListViewAdapterCutiTahunan(movieItemList, getApplicationContext());

                                if (!movieObject.getString("lokasi_struktur").equalsIgnoreCase(txt_lokasi.getText().toString()) ||
                                        (!movieObject.getString("status_cuti_tahunan").equalsIgnoreCase("1"))) {
                                    movieItemList.remove(movieItem);
                                    adapter.notifyDataSetChanged();
                                } else if (movieObject.getString("lokasi_struktur").equalsIgnoreCase("pusat")) {
                                    adapter.notifyDataSetChanged();
                                }

                                list_item_cuti.setAdapter(adapter);
                                list_item_cuti.setStackFromBottom(true);
                            }
                            Collections.sort(movieItemList, new Comparator<approvalcutitahunan>() {
                                public int compare(approvalcutitahunan o1, approvalcutitahunan o2) {
                                    if (o1.getId_sisa_cuti() == null || o2.getId_sisa_cuti() == null)
                                        return 0;
                                    return o1.getId_sisa_cuti().compareTo(o2.getId_sisa_cuti());
                                }
                            });
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
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            };
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static class ListViewAdapterCutiTahunan extends ArrayAdapter<approvalcutitahunan> {

        List<approvalcutitahunan> movieItemList;

        private Context context;

        public ListViewAdapterCutiTahunan(List<approvalcutitahunan> movieItemList, Context context) {
            super(context, R.layout.list_view_approval_tahunan, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_view_approval_tahunan, null, true);

            listViewItem.setBackground(getContext().getDrawable(R.drawable.border));


            TextView id = listViewItem.findViewById(R.id.id);
            TextView waktu = listViewItem.findViewById(R.id.waktu);
            TextView nama = listViewItem.findViewById(R.id.nama);
            TextView tanggal = listViewItem.findViewById(R.id.tanggal);
            TextView lokasi = listViewItem.findViewById(R.id.lokasi);

            TextView keterangan = listViewItem.findViewById(R.id.keterangan);
            TextView opsi = listViewItem.findViewById(R.id.opsi);
            TextView status1 = listViewItem.findViewById(R.id.status1);
            TextView feedBack1 = listViewItem.findViewById(R.id.feedback1);

            approvalcutitahunan movieItem = getItem(position);

            id.setText(movieItem.getId_sisa_cuti());
            waktu.setText(movieItem.getTanggal_pengajuan());
            nama.setText(movieItem.getNama_karyawan_struktur());
            tanggal.setText(movieItem.getStart_cuti_tahunan());
            lokasi.setText(movieItem.getLokasi_struktur());

            keterangan.setText(movieItem.getKet_tambahan_tahunan());
            opsi.setText(movieItem.getOpsi_cuti_tahunan());
            status1.setText(movieItem.getStatus_cuti_tahunan());
            feedBack1.setText(movieItem.getFeedback_cuti_tahunan());


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
