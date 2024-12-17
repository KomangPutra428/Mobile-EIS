package com.example.eis2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
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
import com.example.eis2.Item.DataModel;
import com.example.eis2.Item.DataModel2;
import com.example.eis2.Item.Restarter;
import com.example.eis2.SearchSpinner.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;
import static com.example.eis2.menu.txt_lokasi;
import static com.example.eis2.mutasi.demosi;
import static com.example.eis2.mutasi.department;
import static com.example.eis2.mutasi.departmentbaru;
import static com.example.eis2.mutasi.employee;
import static com.example.eis2.mutasi.jab;
import static com.example.eis2.mutasi.jabatanbaru;
import static com.example.eis2.mutasi.lokasibaru;
import static com.example.eis2.mutasi.opsi;
import static com.example.eis2.mutasi.panel;
import static com.example.eis2.mutasi.pjs;
import static com.example.eis2.mutasi.promosi;
import static com.example.eis2.mutasi.pt;
import static com.example.eis2.mutasi.ptbaru;
import static com.example.eis2.mutasi.rekomendasi;
import static com.example.eis2.mutasi.rotasi;

public class karyawan_project extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    EditText tanggalawal, tanggalakhir, karyawan, depo;
    TextView jamsekarang;
    private Calendar date;
    private SimpleDateFormat dateFormatter;
    ArrayList<String> Karyawan;
    ArrayList<String> depoarray;
    ImageButton pengajuan2;
    private ArrayList<DataModel2> dataModels = new ArrayList<>();
    ImageButton simpan;
    private DataProjectAdapter adapter;
    ListView list;
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();

        date = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                tanggalawal.setText(dateFormatter.format(date.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(karyawan_project.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    public void showDateTimePicker2() {
        final Calendar currentDate = Calendar.getInstance();

        date = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                tanggalakhir.setText(dateFormatter.format(date.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(karyawan_project.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyawan_project);
        tanggalawal = (EditText) findViewById(R.id.tanggalawal);
        tanggalakhir = (EditText) findViewById(R.id.tanggalakhir);

        jamsekarang = (TextView) findViewById(R.id.jamsekarang);

        karyawan = (EditText) findViewById(R.id.karyawan);
        depo = (EditText) findViewById(R.id.depo);

        depo.setText(lokasibaru.getSelectedItem().toString());

        pengajuan2 = (ImageButton) findViewById(R.id.pengajuan2);
        list = (ListView) findViewById(R.id.list);
        String date = new SimpleDateFormat("HH.mm.ss").format(Calendar.getInstance().getTime());

        jamsekarang.setText(date);
        simpan = (ImageButton) findViewById(R.id.simpan);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        tanggalawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });
        tanggalakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker2();
            }
        });

        pengajuan2.setVisibility(View.INVISIBLE);


        Karyawan = new ArrayList<>();
        depoarray = new ArrayList<>();

        pengajuan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tanggalawal.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Silahkan pilih tanggal", Toast.LENGTH_SHORT).show();
                } else if (tanggalakhir.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Silahkan pilih tanggal", Toast.LENGTH_SHORT).show();
                } else {
                    pDialog = new ProgressDialog(karyawan_project.this);
                    showDialog();
                    pDialog.setContentView(R.layout.progress_dialog);
                    pDialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    adapter.postproject();
                }
            }
        });

        String nama = employee.getSelectedItem().toString();
        String[] splited_text2 = nama.split(" \\(");
        nama = splited_text2[0];
        nama = nama.replace("(", "");

        karyawan.setText(nama);

        String name = karyawan.getText().toString();

        String nik = employee.getSelectedItem().toString();
        String[] splited_text3 = nik.split(" \\(");
        nik = splited_text3[1];
        nik = nik.replace(")", "");

        String lokasi = depo.getText().toString();


                    dataModels.add(new DataModel2(name, nik, lokasi));
                    adapter = new DataProjectAdapter(karyawan_project.this, dataModels);
                    list.setAdapter(adapter);
                    System.out.println("size =" + dataModels.size());

    }

    public class DataProjectAdapter extends BaseAdapter {
        Activity context;
        ArrayList<DataModel2> dataModels;
        private LayoutInflater inflater = null;

        public DataProjectAdapter(Activity context, ArrayList<DataModel2> dataModels){
            this.context = context;
            this.dataModels = dataModels;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount(){
            return dataModels.size();
        }

        @Override
        public DataModel2 getItem(int position){
            return dataModels.get(position);
        }
        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            itemView = (itemView == null) ? inflater.inflate(R.layout.list_item_project, null): itemView;

            TextView nik = itemView.findViewById(R.id.nik);
            TextView nama = itemView.findViewById(R.id.nama);
            TextView ptawal = itemView.findViewById(R.id.ptawal2);
            TextView departawal2 = itemView.findViewById(R.id.departawal2);
            TextView lokasi = itemView.findViewById(R.id.lokasi);
            TextView jabatan2 = itemView.findViewById(R.id.jabatan2);
            TextView progress = itemView.findViewById(R.id.progress);

            LinearLayout opsilinear = itemView.findViewById(R.id.opsilinear);

            TextView ptbaru2 = itemView.findViewById(R.id.ptbaru2);
            TextView departbaru2 = itemView.findViewById(R.id.departbaru2);
            TextView jabatanbaru2 = itemView.findViewById(R.id.jabatanbaru2);
            TextView tanggal2 = itemView.findViewById(R.id.tanggal2);
            TextView lokasibaru2 = itemView.findViewById(R.id.lokasibaru2);

            TextView opsi2 = itemView.findViewById(R.id.opsi2);

            String jabatan = jabatanbaru.getSelectedItem().toString();
            String[] splited_text2 = jabatan.split(" \\(");
            jabatan = splited_text2[1];
            jabatan = jabatan.replace(")", "");

            if (opsi.getVisibility() == View.VISIBLE) {
                if(panel.isChecked()) {
                    opsi2.setText("Panel");
                } else if (pjs.isChecked()) {
                    opsi2.setText("PLT");
                }
            } else {
                opsilinear.setVisibility(View.GONE);
            }

            DataModel2 model = dataModels.get(position);

            nama.setText(model.getNama());
            nik.setText(model.getNik());
            lokasi.setText(model.getDepo());
            ptawal.setText(pt.getText().toString());
            departawal2.setText(department.getText().toString());
            jabatan2.setText(jab.getText().toString());

            ptbaru2.setText(ptbaru.getSelectedItem().toString());
            departbaru2.setText(departmentbaru.getSelectedItem().toString());
            jabatanbaru2.setText(jabatan);
            tanggal2.setText(rekomendasi.getText().toString());
            lokasibaru2.setText(lokasibaru.getSelectedItem().toString());


            if(demosi.isChecked()) {
                progress.setText("Demosi");
            } else if(rotasi.isChecked()){
                progress.setText("Rotasi");
            } else if (promosi.isChecked()){
                progress.setText("Promosi");
            }

            if (adapter!=null) {
                if (adapter.getCount() > 0) {
                    pengajuan2.setVisibility(View.VISIBLE);
                } else {
                    pengajuan2.setVisibility(View.INVISIBLE);
                }
            }

            return itemView;
        }

        private void postproject() {

            for (int i = 0; i <= (getCount() -1); i++) {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int finalI = i;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/Karyawan_Project/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (finalI == (getCount() -1)) {
                                    hideDialog();
                                    Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                    karyawan_project.this.finish();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "maaf ada kesalahan", Toast.LENGTH_LONG).show();
                            }
                        }
                ) {

                    @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                        String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                        String nik = getItem(finalI).getNik();

                        params.put("nik_pengajuan", nik_baru);
                        params.put("start_date", tanggalawal.getText().toString());
                        params.put("end_date", tanggalakhir.getText().toString());

                        params.put("nik_karyawan", nik);
                        params.put("depo_karyawan", depo.getText().toString());
                        params.put("upload_dokumen", "");

                        params.put("depo_awal", mutasi.lokasi.getText().toString());
                        params.put("jabatan_awal", mutasi.nomorjabatanawal.getText().toString());
                        params.put("jabatan_akhir", mutasi.nomorjabatan.getText().toString());

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
                RequestQueue requestQueue = Volley.newRequestQueue(karyawan_project.this);
                requestQueue.add(stringRequest);
            }
        }
    }
    @Override
    public void onBackPressed()
    { }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

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