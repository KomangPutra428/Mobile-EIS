package com.example.eis2;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.permissions;
import static com.example.eis2.menu.txt_lokasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class gratifikasi extends AppCompatActivity {

    MaterialButton batal, simpan;
    String nik_penerima;
    SweetAlertDialog Success;
    SharedPreferences sharedPreferences;

    AutoCompleteTextView penerima;
    EditText jabatan, departement, pemberi, nominal, keterangan, tgl_kejadian;

    private List<String> nik_baru_list = new ArrayList<>();
    private List<String> nama_karyawan_list = new ArrayList<>();
    private List<String> jabatan_list = new ArrayList<>();
    private List<String> departement_list = new ArrayList<>();
    private SimpleDateFormat dateFormatter;

    ProgressDialog pDialog;

    LinearLayout fotogratifikasiview;
    TextView textupload2;
    ImageView uploadgambar2;

    Bitmap bitmap;
    private Calendar date;
    String tanggal;

    final int CODE_GALLERY_REQUEST = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gratifikasi);
        penerima = findViewById(R.id.penerima);
        jabatan = findViewById(R.id.jabatan);
        departement = findViewById(R.id.departement);
        tgl_kejadian = findViewById(R.id.tgl_kejadian);

        pemberi = findViewById(R.id.pemberi);
        nominal = findViewById(R.id.nominal);
        keterangan = findViewById(R.id.keterangan);

        fotogratifikasiview = findViewById(R.id.fotogratifikasiview);
        textupload2 = findViewById(R.id.textupload2);
        uploadgambar2 = findViewById(R.id.uploadgambar2);

        batal = findViewById(R.id.batal);
        simpan = findViewById(R.id.simpan);

        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());


        tgl_kejadian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();

                date = currentDate.getInstance();

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);

                        tgl_kejadian.setText(dateFormatter.format(date.getTime()));
                        tanggal = convertFormatTanggalDate(tgl_kejadian.getText().toString());
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(gratifikasi.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });





        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (penerima.getText().toString().length() == 0) {
                    penerima.setError("Wajib Di Isi");
                } else if (!nama_karyawan_list.contains(penerima.getText().toString())) {
                    penerima.setError("Mohon Di Isi Dengan Benar");
                    penerima.setText("");
                    jabatan.setText("");
                    departement.setText("");
                } else if (bitmap == null){
                    new SweetAlertDialog(gratifikasi.this, SweetAlertDialog.WARNING_TYPE)
                            .setContentText("Foto Bukti Wajib Dilampirkan")
                            .setConfirmText("OK")
                            .show();
                } else if (pemberi.getText().toString().length() == 0){
                    pemberi.setError("Wajib Di Isi");
                } else if (nominal.getText().toString().length() == 0){
                    nominal.setError("Wajib Di Isi");
                } else if (keterangan.getText().toString().length() == 0){
                    keterangan.setError("Wajib Di Isi");
                } else {
                    post();
                }
            }
        });

        nominal.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String previous = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Store the current text before changes
                previous = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed here
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Avoid infinite loop and unnecessary formatting
                if (!s.toString().equals(current)) {
                    // Remove the TextWatcher to prevent multiple triggers
                    nominal.removeTextChangedListener(this);

                    try {
                        String input = s.toString().replaceAll("[^\\d]", ""); // Remove non-digit characters
                        if (!input.isEmpty()) {
                            long parsedNumber = Long.parseLong(input);
                            // Format the number
                            String formattedNumber = NumberFormat.getNumberInstance(new Locale("id", "ID"))
                                    .format(parsedNumber);
                            current = formattedNumber;
                            // Set the formatted number back to EditText
                            nominal.setText(formattedNumber);
                            nominal.setSelection(formattedNumber.length()); // Position the cursor before " IDR"
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    // Re-add the TextWatcher
                    nominal.addTextChangedListener(this);
                }
            }
        });

        penerima.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Find the corresponding index in the original sz_name list
                int selectedIndex = nama_karyawan_list.indexOf(selectedItem);

                if (selectedIndex != -1) {
                    // Update the UI components with the selected item data
                    jabatan.setText(jabatan_list.get(selectedIndex));
                    departement.setText(departement_list.get(selectedIndex));
                    nik_penerima = nik_baru_list.get(selectedIndex);
                } else {
                    // info_layout.setVisibility(View.GONE);
                    Log.e("AutoComplete", "Item not found in the list");
                }
            }
        });

        fotogratifikasiview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(gratifikasi.this,
                        permissions(),
                        CODE_GALLERY_REQUEST);
            }
        });

        getLokasi();

    }

    private void post() {
        pDialog = new ProgressDialog(gratifikasi.this);
        showDialog();
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server/pengajuan/gratifikasi",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                String message = jsonObject.getString("message");
                                uploadGambar(message);


                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString(KEY_NIK, null);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                params.put("namaPenerima", nik_penerima);
                params.put("jabatan", jabatan.getText().toString());
                params.put("departement", departement.getText().toString());
                params.put("dtmCreated", currentDateandTime);

                params.put("pemberi", pemberi.getText().toString());
                params.put("nominal", nominal.getText().toString());
                params.put("keterangan", keterangan.getText().toString());
                params.put("userCreated", nik_baru);
                params.put("tglKejadian", tanggal);



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
        RequestQueue requestQueue2 = Volley.newRequestQueue(gratifikasi.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadGambar(String message) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload_gratifikasi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");
                            if (status.contains("Success")) {
                                hideDialog();
                                Success = new SweetAlertDialog(gratifikasi.this, SweetAlertDialog.SUCCESS_TYPE);
                                Success.setContentText("Data Sudah Ditambahkan");
                                Success.setCancelable(false);
                                Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        finish();
                                    }
                                });
                                Success.show();
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

                params.put("nama_foto", message);
                params.put("foto", imagetoString(bitmap));


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

        if (requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(path);
                bitmap = handleSamplingAndRotationBitmap(getApplicationContext(), path);
                uploadgambar2.setImageBitmap(bitmap);
                uploadgambar2.setVisibility(View.VISIBLE);
                textupload2.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(gratifikasi.this, "Gambar sudah diupload", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap handleSamplingAndRotationBitmap(final Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        // Rotate the bitmap if necessary
        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    private String imagetoString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imageType = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageType, Base64.DEFAULT);
    }

    private void getLokasi() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?lokasi_struktur=" + txt_lokasi.getText().toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String nik = jsonObject1.getString("nik_baru");
                            String karyawan = jsonObject1.getString("nama_karyawan_struktur");
                            String dept = jsonObject1.getString("dept_struktur");
                            String jabatan = jsonObject1.getString("jabatan_karyawan");

                            nik_baru_list.add(nik);
                            nama_karyawan_list.add(karyawan);
                            jabatan_list.add(jabatan);
                            departement_list.add(dept);



                        }
                    }
                    penerima.setAdapter(new ArrayAdapter<String>(gratifikasi.this, android.R.layout.simple_spinner_dropdown_item, nama_karyawan_list));
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
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public static String convertFormatTanggalDate(String inputDate) {
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


}