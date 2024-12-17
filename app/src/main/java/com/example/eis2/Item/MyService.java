package com.example.eis2.Item;

import static androidx.core.app.NotificationCompat.PRIORITY_MAX;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ServiceInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eis2.R;
import com.example.eis2.menu;
import com.example.eis2.menu_mutasi;
import com.example.eis2.splash;
import com.example.eis2.spv_surat_keterangan_kerja;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.text_jabatan;
import static com.example.eis2.menu.txt_lokasi;

public class MyService extends Service {
    SharedPreferences sharedPreferences;
    private List<namanikmodel> movieItemList;

    private List<statusizinfullday> movieItemList1;
    private List<statusizinnonfullday> movieItemList2;

    private List<statusdinasfullday> movieItemList3;
    private List<statusdinasnonfullday> movieItemList4;

    private List<statuscutitahunan> movieItemList5;
    private List<statuscutikhusus> movieItemList6;

    private List<approveskmodel> movieItemList7;
    private List<mutasilistmodel> movieItemList8;

    private List<absenmanualmodel> movieItemList9;

    private Context context;
    private boolean stopService = false;
    private Runnable runnable;
    Handler handler = new Handler();
    Runnable refresh;


    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("Oke Restart");

        return null;
    }

    @Override
    public void onCreate() {
        context = this;
        Toast.makeText(getApplicationContext(), "Notifikasi Diaktifkan", Toast.LENGTH_SHORT).show();
        HttpsTrustManager.allowAllSSL();
        handler = new Handler();
        runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    // getDataNotification();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.postDelayed(this, TimeUnit.MINUTES.toMillis(4));
                }
            }
        };
        if (!stopService) {
            handler.postDelayed(runnable, TimeUnit.MINUTES.toMillis(4));
        }

        super.onCreate();
    }

    private void getDataNotification() {


                movieItemList = new ArrayList<>();
                movieItemList1 = new ArrayList<>();
                movieItemList2 = new ArrayList<>();
                movieItemList3 = new ArrayList<>();
                movieItemList4 = new ArrayList<>();
                movieItemList5 = new ArrayList<>();
                movieItemList6 = new ArrayList<>();
                movieItemList7 = new ArrayList<>();
                movieItemList8 = new ArrayList<>();
                movieItemList9 = new ArrayList<>();
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                String nik_baru = sharedPreferences.getString(KEY_NIK, null);
                System.out.println("nik baru setelah diclose = " + nik_baru);

                final StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + nik_baru,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    JSONArray movieArray = obj.getJSONArray("data");

                                    for (int i = 0; i < movieArray.length(); i++) {

                                        final JSONObject nama = movieArray.getJSONObject(i);

                                        nama.getString("nama_karyawan_struktur");
                                        nama.getString("level_jabatan_karyawan");
                                        nama.getString("lokasi_struktur");
                                        nama.getString("jabatan_struktur");
                                        nama.getString("perusahaan_struktur");

                                        System.out.println("Hasil nama =" + nama.getString("nama_karyawan_struktur"));
                                        String nik =  nama.getString("nik_baru");
                                        String jabatan_karyawan = nama.getString("jabatan_struktur");
                                        final String lokasi_karyawan = nama.getString("lokasi_struktur");


                                        int beta = Integer.parseInt(nama.getString("level_jabatan_karyawan"));
                                        if (4 <= beta && beta <= 7) {
                                            DateFormat dateFormat = new SimpleDateFormat("kk:mm:ss");
                                            Date date = new Date();
                                            System.out.println("jam = " + dateFormat.format(date));

                                            String dateStr = "20:00:00";
                                            SimpleDateFormat dateForm = new SimpleDateFormat("kk:mm:ss");
                                            Date convertedDate = new Date();
                                            try {
                                                convertedDate = dateForm.parse(dateStr);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            DateFormat dateFormat2 = new SimpleDateFormat("kk:mm:ss");

                                            String actual = dateFormat.format(date);
                                            String limit = dateFormat2.format(convertedDate);

                                            String[] parts = actual.split(":");
                                            Calendar cal1 = Calendar.getInstance();
                                            cal1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                                            cal1.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                                            cal1.set(Calendar.SECOND, Integer.parseInt(parts[2]));

                                            parts = limit.split(":");
                                            Calendar cal2 = Calendar.getInstance();
                                            cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                                            cal2.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                                            cal2.set(Calendar.SECOND, Integer.parseInt(parts[2]));

                                            cal2.add(Calendar.DATE, 1);

                                            if (cal1.after(cal2)) {
                                                Date c = Calendar.getInstance().getTime();
                                                System.out.println("Current time => " + c);

                                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                                String formattedDate = df.format(c);
                                                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Absenmobile/index_keterangan?nik_baru="+nik+"&tanggal="+formattedDate,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {

                                                            }
                                                        },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                String NOTIFICATION_CHANNEL_ID = "keterangan";
                                                                Context context = getApplicationContext();
                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                    String channelName = "keterangan";
                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                }

                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                Bundle bundle = new Bundle();
                                                                bundle.putString("fromnotif", "notif");
                                                                mIntent.putExtras(bundle);
                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                builder.setContentIntent(pendingIntent)
                                                                        .setSmallIcon(R.mipmap.logo)
                                                                        .setTicker("notif starting")
                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                        .setLights(Color.RED, 3000, 3000)
                                                                        .setAutoCancel(true)
                                                                        .setPriority(PRIORITY_MAX)
                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                        .setContentTitle("Semangat Pagi")
                                                                        .setContentText("Daily Activity anda belum di isi");

                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                notificationManager.notify(0, builder.build());
                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);                                                            }
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

                                                stringRequest2.setRetryPolicy(
                                                        new DefaultRetryPolicy(
                                                                500000,
                                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                        ));
                                                RequestQueue requestQueue2 = Volley.newRequestQueue(MyService.this);
                                                requestQueue2.add(stringRequest2);
                                            }

                                        }

                                        String lokasi = nama.getString("jabatan_struktur");
                                        final String tempat = nama.getString("lokasi_struktur");
                                        if (!lokasi.equals("jabatan")) {
                                            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/izin_full_day/index_atasan?jabatan_struktur=" + lokasi,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject obj = new JSONObject(response);
                                                                JSONArray movieArray = obj.getJSONArray("data");
                                                                int number1 = 0;

                                                                for (int i = 0; i < movieArray.length(); i++) {

                                                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                                                    statusizinfullday movieItem = new statusizinfullday(
                                                                            movieObject.getString("lokasi_struktur"),
                                                                            movieObject.getString("status_full_day"));

                                                                    System.out.println("Service =" + number1);
                                                                    if(!tempat.equals("Pusat")) {
                                                                        if (movieObject.getString("status_full_day").contains("0") && movieObject.getString("lokasi_struktur").equals(tempat))
                                                                            number1++;
                                                                        {
                                                                            if (1 <= number1) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }

                                                                        }
                                                                    } else if(tempat.equals("Pusat")) {
                                                                        if (movieObject.getString("status_full_day").contains("0"))
                                                                            number1++;
                                                                        {
                                                                            if (1 <= number1) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }

                                                                        }
                                                                    }

                                                                    movieItemList1.add(movieItem);


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

                                            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/izin_non_full_day/index_atasan?jabatan_struktur=" + lokasi,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject obj = new JSONObject(response);
                                                                JSONArray movieArray = obj.getJSONArray("data");
                                                                int number2 = 0;

                                                                for (int i = 0; i < movieArray.length(); i++) {

                                                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                                                    statusizinnonfullday movieItem = new statusizinnonfullday(
                                                                            movieObject.getString("lokasi_struktur"),
                                                                            movieObject.getString("status_non_full"));
                                                                    if(!tempat.equals("Pusat")) {
                                                                        if (movieObject.getString("status_non_full").contains("0") && movieObject.getString("lokasi_struktur").equals(tempat))
                                                                            number2++;
                                                                        {
                                                                            if (1 <= number2) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    } else if (tempat.equals("Pusat")){
                                                                        if (movieObject.getString("status_non_full").contains("0"))
                                                                            number2++;
                                                                        {
                                                                            if (1 <= number2) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    }

                                                                    movieItemList2.add(movieItem);
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

                                            StringRequest stringRequest3 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/dinas_full_day/index_atasan?jabatan_struktur=" + lokasi,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject obj = new JSONObject(response);
                                                                JSONArray movieArray = obj.getJSONArray("data");
                                                                int number3 = 0;

                                                                for (int i = 0; i < movieArray.length(); i++) {

                                                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                                                    statusdinasfullday movieItem = new statusdinasfullday(
                                                                            movieObject.getString("lokasi_struktur"),
                                                                            movieObject.getString("status_full_day"));

                                                                    if (!tempat.equals("Pusat")) {
                                                                        if (movieObject.getString("status_full_day").contains("0") && movieObject.getString("lokasi_struktur").equals(tempat))
                                                                            number3++;
                                                                        {
                                                                            if (1 <= number3) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    }else if(tempat.equals("Pusat")){
                                                                        if (movieObject.getString("status_full_day").contains("0"))
                                                                            number3++;
                                                                        {
                                                                            if (1 <= number3) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    }

                                                                    movieItemList3.add(movieItem);
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

                                            StringRequest stringRequest4 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/dinas_non_full_day/index_atasan?jabatan_struktur=" + lokasi,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject obj = new JSONObject(response);
                                                                JSONArray movieArray = obj.getJSONArray("data");
                                                                int number4 = 0;

                                                                for (int i = 0; i < movieArray.length(); i++) {

                                                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                                                    final statusdinasnonfullday movieItem = new statusdinasnonfullday(
                                                                            movieObject.getString("lokasi_struktur"),
                                                                            movieObject.getString("status_non_full"));

                                                                    if(!tempat.equals("Pusat")) {
                                                                        if (movieObject.getString("status_non_full").contains("0") && movieObject.getString("lokasi_struktur").equals(tempat))
                                                                            number4++;
                                                                        {
                                                                            if (1 <= number4) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    } else if (tempat.equals("Pusat")){
                                                                        if (movieObject.getString("status_non_full").contains("0"))
                                                                            number4++;
                                                                        {
                                                                            if (1 <= number4) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    }

                                                                    movieItemList4.add(movieItem);
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

                                            StringRequest stringRequest5 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/cuti_tahunan/index_atasan?jabatan_struktur=" + lokasi,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject obj = new JSONObject(response);
                                                                JSONArray movieArray = obj.getJSONArray("data");
                                                                int number5 = 0;

                                                                for (int i = 0; i < movieArray.length(); i++) {
                                                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                                                    statuscutitahunan movieItem = new statuscutitahunan(
                                                                            movieObject.getString("lokasi_struktur"),
                                                                            movieObject.getString("status_cuti_tahunan"));

                                                                    if(!tempat.equals("Pusat")) {
                                                                        if (movieObject.getString("status_cuti_tahunan").contains("0") && movieObject.getString("lokasi_struktur").equals(tempat))
                                                                            number5++;
                                                                        {
                                                                            if (1 <= number5) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    } else if (tempat.equals("Pusat")){
                                                                        if (movieObject.getString("status_cuti_tahunan").contains("0"))
                                                                            number5++;
                                                                        {
                                                                            if (1 <= number5) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    }

                                                                    movieItemList5.add(movieItem);

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

                                            StringRequest stringRequest6 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/cuti_khusus/index_atasan?jabatan_struktur=" + lokasi,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject obj = new JSONObject(response);
                                                                JSONArray movieArray = obj.getJSONArray("data");
                                                                int number6 = 0;

                                                                for (int i = 0; i < movieArray.length(); i++) {

                                                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                                                    statuscutikhusus movieItem = new statuscutikhusus(
                                                                            movieObject.getString("lokasi_struktur"),
                                                                            movieObject.getString("status_cuti_khusus"));

                                                                    if(!tempat.equals("Pusat")) {
                                                                        if (movieObject.getString("status_cuti_khusus").contains("0") && movieObject.getString("lokasi_struktur").equals(tempat))
                                                                            number6++;
                                                                        {
                                                                            if (1 <= number6) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    }else if (tempat.equals("Pusat")){
                                                                        if (movieObject.getString("status_cuti_khusus").contains("0"))
                                                                            number6++;
                                                                        {
                                                                            if (1 <= number6) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    }

                                                                    movieItemList6.add(movieItem);
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

                                            StringRequest stringRequest7 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/SK/index_atasan?jabatan_struktur=" + lokasi,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject obj = new JSONObject(response);
                                                                JSONArray movieArray = obj.getJSONArray("data");
                                                                int number7 = 0;

                                                                for (int i = 0; i < movieArray.length(); i++) {

                                                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                                                    final approveskmodel movieItem = new approveskmodel(
                                                                            movieObject.getString("id"),
                                                                            movieObject.getString("submit_date"),
                                                                            movieObject.getString("no_urut"),
                                                                            movieObject.getString("nik_baru"),
                                                                            movieObject.getString("jabatan_karyawan"),
                                                                            movieObject.getString("nama_karyawan_struktur"),
                                                                            movieObject.getString("keperluan"),
                                                                            movieObject.getString("analisa"),
                                                                            movieObject.getString("status_atasan"),
                                                                            movieObject.getString("status_hrd"),
                                                                            movieObject.getString("lokasi_struktur"));

                                                                    if(!tempat.equals("Pusat")) {
                                                                        if (movieObject.getString("status_atasan").contains("0") && movieObject.getString("lokasi_struktur").equals(tempat))
                                                                            number7++;
                                                                        {
                                                                            if (1 <= number7) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    } else if (tempat.equals("Pusat")){
                                                                        if (movieObject.getString("status_atasan").contains("0"))
                                                                            number7++;
                                                                        {
                                                                            if (1 <= number7) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    }

                                                                    movieItemList7.add(movieItem);
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

                                            StringRequest stringRequest8 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Mutasi_rotasi/index_atasan?jabatan_struktur=" + jabatan_karyawan,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject obj = new JSONObject(response);
                                                                JSONArray movieArray = obj.getJSONArray("data");
                                                                int number8 = 0;

                                                                for (int i = 0; i < movieArray.length(); i++) {

                                                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                                                    final mutasilistmodel movieItem = new mutasilistmodel(
                                                                            movieObject.getString("id_mutasi_rotasi"),
                                                                            movieObject.getString("tanggal_pengajuan"),
                                                                            movieObject.getString("permintaan"),
                                                                            movieObject.getString("nik_baru"),
                                                                            movieObject.getString("nama_karyawan_mutasi"),
                                                                            movieObject.getString("lokasi_awal"),
                                                                            movieObject.getString("status_atasan"),
                                                                            "0");

                                                                    if(!tempat.equals("Pusat")) {
                                                                        if (movieObject.getString("status_atasan").contains("0") && movieObject.getString("lokasi_awal").equals(lokasi_karyawan))
                                                                            number8++;
                                                                        {
                                                                            if (1 <= number8) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    } else if (tempat.equals("Pusat")){
                                                                        if (movieObject.getString("status_atasan").contains("0"))
                                                                            number8++;
                                                                        {
                                                                            if (1 <= number8) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    }

                                                                    movieItemList8.add(movieItem);
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

                                            StringRequest stringRequest9 = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/pengajuan/Absen_manual2/index_atasan?jabatan_struktur=" + jabatan_karyawan,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject obj = new JSONObject(response);
                                                                JSONArray movieArray = obj.getJSONArray("data");
                                                                int number9 = 0;

                                                                for (int i = 0; i < movieArray.length(); i++) {

                                                                    JSONObject movieObject = movieArray.getJSONObject(i);

                                                                    absenmanualmodel movieItem = new absenmanualmodel(
                                                                            movieObject.getString("id_absen"),
                                                                            movieObject.getString("nik_baru"),
                                                                            movieObject.getString("lokasi_struktur"),
                                                                            movieObject.getString("jenis_absen"),
                                                                            movieObject.getString("tanggal_absen"),
                                                                            movieObject.getString("waktu_absen"),
                                                                            movieObject.getString("ket_absen"),
                                                                            movieObject.getString("status"),
                                                                            movieObject.getString("tanggal"),
                                                                            movieObject.getString("status_2"),
                                                                            movieObject.getString("tanggal_2"));

                                                                    if(!tempat.equals("Pusat")) {
                                                                        if (movieObject.getString("status").contains("0") && movieObject.getString("lokasi_struktur").equals(lokasi_karyawan))
                                                                            number9++;
                                                                        {
                                                                            if (1 <= number9) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    } else if (tempat.equals("Pusat")){
                                                                        if (movieObject.getString("status").contains("0"))
                                                                            number9++;
                                                                        {
                                                                            if (1 <= number9) {
                                                                                String NOTIFICATION_CHANNEL_ID = "pengajuan";
                                                                                Context context = getApplicationContext();
                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                                    String channelName = "pengajuan";
                                                                                    int importance = NotificationManager.IMPORTANCE_HIGH;

                                                                                    NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                                                                                    notificationManager.createNotificationChannel(mChannel);
                                                                                }

                                                                                Intent mIntent = new Intent(getBaseContext(), menu.class);
                                                                                Bundle bundle = new Bundle();
                                                                                bundle.putString("fromnotif", "notif");
                                                                                mIntent.putExtras(bundle);
                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_CHANNEL_ID);
                                                                                builder.setContentIntent(pendingIntent)
                                                                                        .setSmallIcon(R.mipmap.logo)
                                                                                        .setTicker("notif starting")
                                                                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                                                                        .setLights(Color.RED, 3000, 3000)
                                                                                        .setAutoCancel(true)
                                                                                        .setDefaults(Notification.DEFAULT_SOUND)
                                                                                        .setContentTitle("Semangat Pagi")
                                                                                        .setContentText("Ada beberapa pengajuan belum di approve");

                                                                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                                                notificationManager.notify(0, builder.build());
                                                                                builder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE);
                                                                            }
                                                                        }
                                                                    }

                                                                    movieItemList9.add(movieItem);
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

                                            stringRequest.setRetryPolicy(
                                                    new DefaultRetryPolicy(
                                                            500000,
                                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                    )
                                            );
                                            stringRequest2.setRetryPolicy(
                                                    new DefaultRetryPolicy(
                                                            500000,
                                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                    )
                                            );
                                            stringRequest3.setRetryPolicy(
                                                    new DefaultRetryPolicy(
                                                            500000,
                                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                    )
                                            );
                                            stringRequest4.setRetryPolicy(
                                                    new DefaultRetryPolicy(
                                                            500000,
                                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                    )
                                            );
                                            stringRequest5.setRetryPolicy(
                                                    new DefaultRetryPolicy(
                                                            500000,
                                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                    )
                                            );
                                            stringRequest6.setRetryPolicy(
                                                    new DefaultRetryPolicy(
                                                            500000,
                                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                    )
                                            );
                                            stringRequest7.setRetryPolicy(
                                                    new DefaultRetryPolicy(
                                                            500000,
                                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                    )
                                            );
                                            stringRequest8.setRetryPolicy(
                                                    new DefaultRetryPolicy(
                                                            500000,
                                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                    )
                                            );
                                            stringRequest9.setRetryPolicy(
                                                    new DefaultRetryPolicy(
                                                            500000,
                                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                    )
                                            );

                                            RequestQueue requestQueue = Volley.newRequestQueue(MyService.this);
                                            requestQueue.getCache().clear();
                                            requestQueue.add(stringRequest);
                                            requestQueue.add(stringRequest2);
                                            requestQueue.add(stringRequest3);
                                            requestQueue.add(stringRequest4);
                                            requestQueue.add(stringRequest5);
                                            requestQueue.add(stringRequest6);
                                            requestQueue.add(stringRequest7);
                                            requestQueue.add(stringRequest8);
                                            requestQueue.add(stringRequest9);

                                        }

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
                RequestQueue requestQueue = Volley.newRequestQueue(MyService.this);
                requestQueue.add(stringRequest);

            }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);return START_STICKY;
    }



    @Override
    public void onTaskRemoved( Intent rootIntent ) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 100,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public ComponentName startForegroundService(final Intent service) {
        return startForegroundService(service);
    }



}
