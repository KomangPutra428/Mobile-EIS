package com.example.eis2;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_lokasi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.example.eis2.Item.mutasilistmodel;
import com.example.eis2.SearchSpinner.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
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
import java.util.concurrent.atomic.AtomicLong;

public class approval_mutasi_manager extends AppCompatActivity {
    SearchableSpinner status;
    ImageButton filtering, lihat;
    ListView list;
    private Calendar date;
    private SimpleDateFormat dateFormatter;
    private List<mutasilistmodel> movieItemList = new ArrayList<>();


    ProgressDialog pDialog;

    String tanggal1, tanggal2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_mutasi_manager);
        status = findViewById(R.id.status);
        filtering = findViewById(R.id.filtering);
        lihat = findViewById(R.id.lihat);
        list = findViewById(R.id.list);

        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tanggal1 = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 6); // 6 days after Monday is Sunday
        tanggal2 = dateFormat.format(calendar.getTime());

        AtomicLong mLastClickTime = new AtomicLong();

        status.setOnTouchListener((v, event) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime.get() < 1000) {
                status.setEnabled(false);
                return false;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    status.setEnabled(true);
                }
            }, 3000);
            mLastClickTime.set(SystemClock.elapsedRealtime());
            event.setAction(MotionEvent.ACTION_UP);
            status.onTouch(v,event);
            status.setEnabled(false);

            return true;
        });

        lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lihat.setEnabled(false);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        lihat.setEnabled(true);
                    }
                },1500);

                if(status.getSelectedItem().toString().equals("Open")){
                    cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi/index_manager?kondisi=0&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
                } else if(status.getSelectedItem().toString().equals("Approved")){
                    cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi/index_manager?kondisi=1&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
                } else  if(status.getSelectedItem().toString().equals("Not Approved")){
                    cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi/index_manager?kondisi=2&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
                }
            }
        });

        filtering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preventTwoClick(v);
                final Dialog dialog = new Dialog(approval_mutasi_manager.this);
                dialog.setContentView(R.layout.filter_range);

                EditText edit_tanggal = dialog.findViewById(R.id.edit_tanggal);
                EditText sampai_tanggal = dialog.findViewById(R.id.sampai_tanggal);

                Button batal = dialog.findViewById(R.id.batal);
                Button ok = dialog.findViewById(R.id.ok);

                final Calendar currentDate = Calendar.getInstance();
                Calendar twoDaysAgo = (Calendar) currentDate.clone();
                twoDaysAgo.add(Calendar.DATE, 0);

                date = Calendar.getInstance();

                edit_tanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventTwoClick(v);
                        final Calendar currentDate = Calendar.getInstance();
                        Calendar twoDaysAgo = (Calendar) currentDate.clone();

                        date = Calendar.getInstance();

                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth );

                                edit_tanggal.setText(dateFormatter.format(date.getTime()));
                            }
                        };
                        DatePickerDialog datePickerDialog = new  DatePickerDialog(approval_mutasi_manager.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(twoDaysAgo.getTimeInMillis());
                        datePickerDialog.show();


                    }
                });

                sampai_tanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventTwoClick(v);
                        final Calendar currentDate = Calendar.getInstance();
                        Calendar twoDaysAgo = (Calendar) currentDate.clone();

                        date = Calendar.getInstance();

                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth );

                                sampai_tanggal.setText(dateFormatter.format(date.getTime()));
                            }
                        };
                        DatePickerDialog datePickerDialog = new  DatePickerDialog(approval_mutasi_manager.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(twoDaysAgo.getTimeInMillis());
                        datePickerDialog.show();


                    }
                });


                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventTwoClick(v);
                        dialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventTwoClick(v);

                        if(edit_tanggal.getText().toString().length() == 0){
                            edit_tanggal.setError("Wajib Di isi");
                        } else if(sampai_tanggal.getText().toString().length() == 0){
                            sampai_tanggal.setError("Wajib Di isi");
                        } else {
                            dialog.dismiss();

                            tanggal1 = convertFormat2(edit_tanggal.getText().toString());
                            tanggal2 = convertFormat2(sampai_tanggal.getText().toString());

                            if(status.getSelectedItem().toString().equals("Open")){
                                cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi/index_manager?kondisi=0&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
                            } else if(status.getSelectedItem().toString().equals("Approved")){
                                cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi/index_manager?kondisi=1&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
                            } else  if(status.getSelectedItem().toString().equals("Not Approved")){
                                cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi/index_manager?kondisi=2&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
                            }
                        }
                    }
                });



                dialog.show();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(status.getSelectedItem().toString().equals("Open")){
                    Intent i = new Intent(approval_mutasi_manager.this, form_mutasi_manager.class);
                    String id_mutasi_rotasi = ((mutasilistmodel) parent.getItemAtPosition(position)).getId_mutasi_rotasi();
                    i.putExtra("id", id_mutasi_rotasi);
                    startActivity(i);
                }
            }
        });




    }

    private void cekdata(String s) {
        pDialog = new ProgressDialog(approval_mutasi_manager.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        String jabatan = text_jabatan.getText().toString();
        movieItemList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject movieObject = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                movieObject = movieArray.getJSONObject(i);

                                final mutasilistmodel movieItem = new mutasilistmodel(
                                        convertFormat(movieObject.getString("tanggal_pengajuan")),
                                        movieObject.getString("id_mutasi_rotasi"),
                                        movieObject.getString("permintaan"),
                                        movieObject.getString("nik_baru"),
                                        movieObject.getString("nama_karyawan_mutasi"),
                                        movieObject.getString("lokasi_awal"),
                                        movieObject.getString("status_atasan"),
                                        movieObject.getString("status_manager"));

                                movieItemList.add(movieItem);

                                hideDialog();


                                ListViewAdapterMutasi adapter = new ListViewAdapterMutasi(movieItemList, getApplicationContext());
                                list.setVisibility(View.VISIBLE);
                                list.setAdapter(adapter);
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
                        hideDialog();
                        list.setVisibility(View.GONE);
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
            }
        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        7200000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static class ListViewAdapterMutasi extends ArrayAdapter<mutasilistmodel> {

        List<mutasilistmodel> movieItemList;

        private Context context;

        public ListViewAdapterMutasi(List<mutasilistmodel> movieItemList, Context context) {
            super(context, R.layout.list_item_mutasirotasi, movieItemList);
            this.movieItemList = movieItemList;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_mutasirotasi, null, true);

            TextView tanggal_2 = listViewItem.findViewById(R.id.tanggal_2);
            TextView id = listViewItem.findViewById(R.id.id);
            TextView nik = listViewItem.findViewById(R.id.nik);
            TextView nama = listViewItem.findViewById(R.id.nama);
            TextView lokasi = listViewItem.findViewById(R.id.lokasi);
            TextView permintaan = listViewItem.findViewById(R.id.permintaan);
            ImageView approval1 = listViewItem.findViewById(R.id.approval1);
            ImageView approval_hr = listViewItem.findViewById(R.id.approval_hr);


            mutasilistmodel movieItem = getItem(position);

            tanggal_2.setText(movieItem.getTanggal_pengajuan());
            id.setText(movieItem.getId_mutasi_rotasi());
            nik.setText(movieItem.getNik_baru());
            nama.setText(movieItem.getNama_karyawan_mutasi());
            lokasi.setText(movieItem.getLokasi_awal());
            permintaan.setText(movieItem.getPermintaan());

            if(movieItem.getStatus_atasan().equals("0")){
                approval1.setImageResource(R.drawable.btn_open);
            } else if(movieItem.getStatus_atasan().equals("1")){
                approval1.setImageResource(R.drawable.btn_aprv);
            } else if(movieItem.getStatus_atasan().equals("2")){
                approval1.setImageResource(R.drawable.btn_notaprv);
            } else if(movieItem.getStatus_atasan().equals("3")){
                approval1.setImageResource(R.drawable.btn_hangus);
            }

            if(movieItem.getStatus_manager().equals("0")){
                approval_hr.setImageResource(R.drawable.btn_open);
            } else if(movieItem.getStatus_manager().equals("1")){
                approval_hr.setImageResource(R.drawable.btn_aprv);
            } else if(movieItem.getStatus_manager().equals("2")){
                approval_hr.setImageResource(R.drawable.btn_notaprv);
            } else if(movieItem.getStatus_manager().equals("3")){
                approval_hr.setImageResource(R.drawable.btn_hangus);
            }
            return listViewItem;
        }
    }

    public static void preventTwoClick(final View view){
        view.setEnabled(false);
        view.postDelayed(
                ()-> view.setEnabled(true),
                2000
        );
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        return convetDateFormat.format(date);
    }

    public static String convertFormat2(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return convetDateFormat.format(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(status.getSelectedItem().toString().equals("Open")){
            cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi/index_manager?kondisi=0&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
        } else if(status.getSelectedItem().toString().equals("Approved")){
            cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi/index_manager?kondisi=1&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
        } else  if(status.getSelectedItem().toString().equals("Not Approved")){
            cekdata("https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi/index_manager?kondisi=2&" + "date1=" + tanggal1 + "&date2=" +tanggal2);
        }
    }

}