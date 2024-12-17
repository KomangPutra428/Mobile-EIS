package com.example.eis2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.getNo_pengajuan_full;
import com.example.eis2.Item.getNo_pengajuan_non_full;
import com.google.android.material.navigation.NavigationView;
import com.example.eis2.SearchSpinner.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.izin.txt_nomor;
import static com.example.eis2.menu.txt_alpha;

public class pengajuan_resign extends AppCompatActivity {
    EditText nopengajuan, tanggal, keterangan;
    private Calendar date;
    private SimpleDateFormat dateFormatter;
    ImageButton uploadbutton, pengajuan;
    SearchableSpinner tipikalresign, alasanresign;
    ArrayList<String> alasan = new ArrayList<>();
    Bitmap bitmap;
    ImageView gambar1;

    DrawerLayout dLayout;
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        Calendar twoDaysAgo = (Calendar) currentDate.clone();
        twoDaysAgo.add(Calendar.DATE, 30);

        date = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                tanggal.setText(dateFormatter.format(date.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(pengajuan_resign.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());

        datePickerDialog.show();

    }

    public void today() {
        final Calendar currentDate = Calendar.getInstance();
        Calendar twoDaysAgo = (Calendar) currentDate.clone();
        twoDaysAgo.add(Calendar.DATE, 0);

        date = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                tanggal.setText(dateFormatter.format(date.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(pengajuan_resign.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());

        datePickerDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuan_resign);
        nopengajuan = findViewById(R.id.nopengajuan);
        tanggal = findViewById(R.id.tanggal);
        tipikalresign = findViewById(R.id.tipikalresign);
        alasanresign = findViewById(R.id.alasanresign);
        uploadbutton = findViewById(R.id.uploadbutton);
        gambar1 = findViewById(R.id.gambar1);
        pengajuan = findViewById(R.id.pengajuan);
        keterangan = findViewById(R.id.keterangan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });

        pengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipikalresign.getSelectedItem().toString().equals("PILIH ALASAN")) {
                    Toast.makeText(getApplicationContext(), "Silahkan Pilih Alasan", Toast.LENGTH_SHORT).show();
                } else if (alasanresign.getSelectedItem().toString().equals("PILIH ALASAN")) {
                    Toast.makeText(getApplicationContext(), "Silahkan Pilih Alasan", Toast.LENGTH_SHORT).show();
                } else if (tanggal.getText().toString().length() == 0) {
                    tanggal.setError("Masukkan Tanggal!");
                } else if (gambar1.getDrawable() == null) {
                    Toast.makeText(getApplicationContext(), "Upload gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else if (keterangan.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Silahkan Pilih Alasan", Toast.LENGTH_SHORT).show();
                } else {
                    getNo();
                }
            }
        });

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tipikalresign.getSelectedItem().toString().equals("Case")) {
                    today();
                } else if (tipikalresign.getSelectedItem().toString().equals("Non Case")) {
                    showDateTimePicker();
                }
            }
        });

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) findViewById(R.id.navigation); // initiate a Navigation View
        View headerView = navView.getHeaderView(0);
        final TextView hari = headerView.findViewById(R.id.hari);
        final TextView kondisi = headerView.findViewById(R.id.kondisi);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                hari.setText(currentDateandTime);
                handler.postDelayed(this, 1000);

                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

                if (timeOfDay >= 0 && timeOfDay < 9) {
                    kondisi.setText("Selamat Pagi");
                } else if (timeOfDay >= 9 && timeOfDay < 15) {
                    kondisi.setText("Selamat Siang");
                } else if (timeOfDay >= 15 && timeOfDay < 18) {
                    kondisi.setText("Selamat Sore");
                } else {
                    kondisi.setText("Selamat Malam");
                }
            }
        };
        runnable.run();
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
                if (itemId == R.id.nav_home) {
                    Intent i = new Intent(getApplicationContext(), menu.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                } else if (itemId == R.id.password) {
                    Intent i = new Intent(getApplicationContext(), setting.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                } else if (itemId == R.id.ketentuan) {
                    Intent i = new Intent(getApplicationContext(), ketentuan.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                } else if (itemId == R.id.nav_exit) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            pengajuan_resign.this);
                    alertDialogBuilder.setTitle("Anda yakin untuk Logout ?");
                    alertDialogBuilder
                            .setMessage("Klik Ya untuk keluar!")
                            .setCancelable(false)
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                public void onClick(DialogInterface dialog, int id) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    Intent intent = new Intent(pengajuan_resign.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else if (itemId == R.id.nav_calendar) {
                    Intent i = new Intent(getApplicationContext(), kalendar_event.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                }
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.commit(); // commit the changes
                    dLayout.closeDrawers(); // close the all open Drawer Views
                    return true;
                }
                return false;
            }
        });

        dateFormatter = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());

        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(pengajuan_resign.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        });

        tipikalresign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipikalresign.setTitle("Pilih Alasan");
                String caseresign = tipikalresign.getItemAtPosition(tipikalresign.getSelectedItemPosition()).toString();

                if (caseresign.equals("PILIH ALASAN")) {
                    System.out.println();
                } else {
                    alasan.clear();
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_keterangan_case?case=" + tipikalresign.getSelectedItem().toString(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            alasan.add("PILIH ALASAN");
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("true")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String reasons = jsonObject1.getString("alasan_resign");

                                        alasan.add(reasons);

                                    }
                                }
                                alasanresign.setTitle("Pilih Alasan");
                                alasanresign.setAdapter(new ArrayAdapter<String>(pengajuan_resign.this, android.R.layout.simple_spinner_dropdown_item, alasan));
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
                            }) {
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
                    RequestQueue requestQueue = Volley.newRequestQueue(pengajuan_resign.this);
                    requestQueue.add(stringRequest);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getNo() {
        pDialog = new ProgressDialog(this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_nomorid", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject finalObject3 = jsonArray.getJSONObject(0);

                            String nomor = finalObject3.getString("no_pengajuan");

                            int as = Integer.parseInt(nomor);

                            int count = as + 1;


                            String padded = String.format("%04d" , count);
                            nopengajuan.setText(padded);

                            postresign();


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
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }


        };

        request.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(request);
    }

    private void postresign() {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/resign/index_resign_baru",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        uploadgambar();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

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

                params.put("no_pengajuan", nopengajuan.getText().toString());
                params.put("nik_baru", nik_baru);
                params.put("tanggal_pengajuan", convertFormat(tanggal.getText().toString()));
                params.put("alasan_resign", alasanresign.getSelectedItem().toString());

                params.put("klarifikasi_resign", tipikalresign.getSelectedItem().toString());
                params.put("ket_resign", keterangan.getText().toString());
                params.put("jabatan", menu.text_jabatan.getText().toString());
                params.put("dokumen_resign", nopengajuan.getText().toString() + "_" + nik_baru + ".jpeg");


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
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadgambar() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/uploadresign.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");
                            if (status.contains("Success")) {
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "data sudah dimasukkan", Toast.LENGTH_LONG).show();
                                pengajuan_resign.this.finish();
                            } else if (status.contains("Image not uploaded")){
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "Maaf ada kesalahan", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), "Maaf ada kesalahan", Toast.LENGTH_SHORT).show();
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

                String pengajuan = nopengajuan.getText().toString();
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                String gambar = imagetoString(bitmap);

                params.put("nik", pengajuan + "_" + nik_baru);
                params.put("foto", gambar);


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

        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest2);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(path);
                bitmap = BitmapFactory.decodeStream(inputStream);
                gambar1.setImageBitmap(bitmap);
                gambar1.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(pengajuan_resign.this, "Gambar sudah diupload", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imagetoString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
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
    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
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
    public void onBackPressed() {
        finish();
    }
}



