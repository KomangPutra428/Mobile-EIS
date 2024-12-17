package com.example.eis2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.eis2.Item.rekapabsensiclass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.spv_absensi.txt_jabatan_struktur;
import static com.example.eis2.spv_absensi.txt_lokasi_struktur;
import static java.util.Calendar.DAY_OF_MONTH;

public class rekapcuti extends AppCompatActivity {
    ListView list_team;
    private List<rekapabsensiclass> team;
    private static RequestQueue mQueue;
    ProgressDialog pDialog;
    TextView tanggalawal, tanggalakhir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekapcuti);
        HttpsTrustManager.allowAllSSL();

        list_team = findViewById(R.id.list_team);
        mQueue = Volley.newRequestQueue(rekapcuti.this);
        tanggalawal = (EditText) findViewById(R.id.tanggalawal);
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.DAY_OF_MONTH, myCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        tanggalawal.setText(sdf.format(myCalendar.getTime()));


        final DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
            private void updateLabel() {
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                tanggalawal.setText(sdf.format(myCalendar.getTime()));
                team.clear();
                loadPlayer();

            }
        };
        tanggalawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(rekapcuti.this, datePickerDialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(DAY_OF_MONTH)).show();
            }
        });


        tanggalakhir = (EditText) findViewById(R.id.tanggalakhir);
        SimpleDateFormat sdf2 = new SimpleDateFormat( "yyyy-MM-dd" );
        final Calendar myCalendar2 = Calendar.getInstance();
        tanggalakhir.setText( sdf2.format((new Date())));

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                tanggalakhir.setText(sdf.format(myCalendar2.getTime()));
                tanggalakhir.getText().toString();
                team.clear();
                loadPlayer();
            }
        };
        tanggalakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(rekapcuti.this, date2, myCalendar2
                        .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(DAY_OF_MONTH)).show();
            }
        });

        team = new ArrayList<>();
        loadPlayer();

    }


    private void loadPlayer() {
        pDialog = new ProgressDialog(rekapcuti.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        String jabatan = txt_jabatan_struktur.getText().toString().trim();

        System.out.println("test 111 = " + jabatan);

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/team/index?jabatan_struktur=" + jabatan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject object = new JSONObject(response);
                            JSONArray movieArray = object.getJSONArray("data");

                            JSONObject movieObject2 = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject2 = movieArray.getJSONObject(i);

                                final rekapabsensiclass movieItem = new rekapabsensiclass(
                                        movieObject2.getString("nik_baru"),
                                        movieObject2.getString("nama_karyawan_struktur"),
                                        movieObject2.getString("lokasi_struktur"));
                                team.add(movieItem);

                                ListViewAdapterTeam adapter = new ListViewAdapterTeam(team, getApplicationContext());
                                list_team.setAdapter(adapter);

                                if (!movieObject2.getString("lokasi_struktur").equalsIgnoreCase(txt_lokasi_struktur.getText().toString())) {
                                    team.remove(movieItem);
                                }

                                if (movieObject2.getString("lokasi_struktur").equalsIgnoreCase("pusat")) {
                                    team.remove(movieItem);
                                    team.add(movieItem);
                                }

                                hideDialog();
                                Collections.sort(team, new Comparator<rekapabsensiclass>() {
                                    public int compare(rekapabsensiclass o1, rekapabsensiclass o2) {
                                        if (o1.getNama_karyawan_struktur() == null || o2.getNama_karyawan_struktur() == null)
                                            return 0;
                                        return o1.getNama_karyawan_struktur().compareTo(o2.getNama_karyawan_struktur());
                                    }
                                });


                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Maaf, ada kesalahan", Toast.LENGTH_SHORT).show();
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
        requestQueue.add(stringRequest1);
    }
    public class ListViewAdapterTeam extends ArrayAdapter<rekapabsensiclass> {

        public List<rekapabsensiclass> team;
        private Context context;

        public ListViewAdapterTeam(List<rekapabsensiclass> team, Context context) {
            super(context, R.layout.list_item_rekapcuti, team);
            this.team = team;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            final View listViewItem = inflater.inflate(R.layout.list_item_rekapcuti, null);

            rekapabsensiclass movieItem = team.get(position);
            System.out.println("position =" + position);

            listViewItem.setBackground(getContext().getDrawable(R.drawable.border));
            TextView nikbaruteam = listViewItem.findViewById(R.id.nik);
            TextView nama = listViewItem.findViewById(R.id.nama);
            final TextView cutikhusus = listViewItem.findViewById(R.id.cutikhusus);
            final TextView cutitahunan = listViewItem.findViewById(R.id.cutitahunan);

            nikbaruteam.setText(movieItem.getNik_baru());
            nama.setText(movieItem.getNama_karyawan_struktur());

            String nik_baru = getItem(position).getNik_baru();
            System.out.println("nik222 =" + nik_baru);

            String awal = tanggalawal.getText().toString().trim();
            String akhir = tanggalakhir.getText().toString().trim();

            this.addAll();

            System.out.println("nik222 =" + awal);
            System.out.println("nik222 =" + akhir);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/absensi/index?shift_day="+awal+"&shift_day_2="+akhir+"&badgenumber="+nik_baru,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray movieArray = obj.getJSONArray("data");

                                int number6 = 0;
                                int number7 = 0;

                                for (int i = 0; i < movieArray.length(); i++) {

                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                    movieObject.getString("ket_absensi");

                                    if (movieObject.getString("ket_absensi").contains("CK"))
                                        number6++; {
                                        cutikhusus.setText(String.valueOf(number6));
                                    }
                                    if (movieObject.getString("ket_absensi").contains("CU"))
                                        number7++; {
                                        cutitahunan.setText(String.valueOf(number7));
                                    }
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
            mQueue.add(stringRequest);

            return listViewItem;
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