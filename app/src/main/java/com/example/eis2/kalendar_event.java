package com.example.eis2;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.eis2.Item.HttpsTrustManager;
import com.example.eis2.Item.Restarter;
import com.example.eis2.Item.approvaldinasfull;
import com.example.eis2.Item.eventmodel;
import com.example.eis2.Item.izinnonfullmodel;
import com.google.android.material.navigation.NavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.eis2.Item.LoginItem.KEY_NIK;
import static com.example.eis2.menu.txt_alpha;

public class kalendar_event extends AppCompatActivity {
    MaterialCalendarView simpleCalendarView;
    TextView tanggal2;
    private List<CalendarDay> events = new ArrayList<>();
    SharedPreferences sharedPreferences;
    DrawerLayout dLayout;
    ListView list;
    private List<eventmodel> event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalendar_event);
        HttpsTrustManager.allowAllSSL();

        setNavigationDrawer();

        list = findViewById(R.id.dailyView1);
        event = new ArrayList<>();

        TextView empty=(TextView)findViewById(R.id.empty);
        list.setEmptyView(empty);


        getEvents();
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

        simpleCalendarView = (MaterialCalendarView) findViewById(R.id.simpleCalendarView);
        tanggal2 = (TextView) findViewById(R.id.tanggal);

        simpleCalendarView.setSelectedDate(CalendarDay.today());


        makeJsonObjectRequest();

        checkcalendar();

        simpleCalendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return day.getCalendar().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
            }

            @Override
            public void decorate(DayViewFacade view) {
                int merah = getResources().getColor(R.color.red);
                view.addSpan(new ForegroundColorSpan(merah));
            }
        });

        simpleCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, final CalendarDay date) {
                event.clear();
                tanggal2.setText("Belum ada Event");
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/event/index",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    JSONArray movieArray = obj.getJSONArray("data");

                                    for (int i = 0; i < movieArray.length(); i++) {

                                        JSONObject movieObject = movieArray.getJSONObject(i);

                                        eventmodel movieItem = new eventmodel(
                                                movieObject.getString("name"),
                                                movieObject.getString("birth_date"));

                                        event.add(movieItem);

                                        ListViewAdapterEvent adapter = new ListViewAdapterEvent(event, getApplicationContext());


                                        DateFormat df1 = new SimpleDateFormat("MMMM");
                                        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

                                        String dtStart = movieObject.getString("birth_date");
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                                        String bulan_awal = df1.format(date.getDate());
                                        System.out.println("Bulan = " + bulan_awal);

                                        String tanggal_awal = df2.format(date.getDate());
                                        System.out.println("Tanggal awal = " + tanggal_awal);

                                        String date = tanggal_awal;
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        Date convertedDate = dateFormat.parse(date);
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(convertedDate);
                                        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

                                        Date tanggal = c.getTime();
                                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                        String inActiveDate = format1.format(tanggal);

                                        Date tanggal1 = format.parse(dtStart);
                                        Date tanggal2 = format.parse(tanggal_awal);
                                        Date tanggal3 = format.parse(inActiveDate);

                                        System.out.println("Tanggal awal = " + tanggal2 + " dan Tanggal Akhir = " + tanggal3);


                                        if(tanggal2.after(tanggal1)){
                                            event.remove(movieItem);
                                        }

                                        if(tanggal3.before(tanggal1)){
                                            event.remove(movieItem);
                                        }


                                        list.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();

                                        Collections.sort(event, new Comparator<eventmodel>() {
                                            public int compare(eventmodel o1, eventmodel o2) {
                                                if (o2.getBirth_date() == null || o1.getBirth_date() == null)
                                                    return 0;
                                                return o2.getBirth_date().compareTo(o1.getBirth_date());
                                            }
                                        });

                                    }
                                } catch (JSONException | ParseException e) {
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
                RequestQueue requestQueue = Volley.newRequestQueue(kalendar_event.this);
                requestQueue.add(stringRequest);


            }
        });

        simpleCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, boolean selected) {
                DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                String tanggal_awal = df2.format(date.getDate());

                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/event/index?birth_date=" + tanggal_awal,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    JSONArray movieArray = obj.getJSONArray("data");

                                    for (int i = 0; i < movieArray.length(); i++) {

                                        JSONObject movieObject = movieArray.getJSONObject(i);

                                        tanggal2.setText(movieObject.getString("name"));

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                tanggal2.setText("Belum ada Event");
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
                RequestQueue requestQueue = Volley.newRequestQueue(kalendar_event.this);
                requestQueue.add(stringRequest);
            }
        });

    }

    private void getEvents() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/event/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                eventmodel movieItem = new eventmodel(
                                        movieObject.getString("name"),
                                        movieObject.getString("birth_date"));

                                event.add(movieItem);

                                ListViewAdapterEvent adapter = new ListViewAdapterEvent(event, getApplicationContext());


                                String dtStart = movieObject.getString("birth_date");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                Date date1 = format.parse(dtStart);

                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.MONTH, 1);
                                calendar.set(Calendar.DAY_OF_MONTH, 1);
                                calendar.add(Calendar.DATE, -1);

                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.add(Calendar.MONTH, 0);
                                calendar2.set(Calendar.DAY_OF_MONTH, 1);
                                calendar2.add(Calendar.DATE, 0);

                                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                Date firstDayOfMonth = calendar2.getTime();
                                Date lastDayOfMonth = calendar.getTime();

                                String pertama = sdf.format(firstDayOfMonth);
                                Date date2 = format.parse(pertama);

                                String kedua = sdf.format(lastDayOfMonth);
                                Date date3 = format.parse(kedua);

                                System.out.println("from data            : " + date1);
                                System.out.println("Today: " + date2);
                                System.out.println("Last Day            : " + date3);

                                if(date2.after(date1)){
                                    event.remove(movieItem);
                                }

                                if(date3.before(date1)){
                                    event.remove(movieItem);
                                }

                                list.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                        final String tanggal = ((eventmodel) parent.getItemAtPosition(position)).getBirth_date();

                                        System.out.println("Test :" + tanggal);
                                        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/event/index?birth_date=" + tanggal,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                        try {
                                                            JSONObject obj = new JSONObject(response);
                                                            JSONArray movieArray = obj.getJSONArray("data");

                                                            for (int i = 0; i < movieArray.length(); i++) {

                                                                JSONObject movieObject = movieArray.getJSONObject(i);

                                                                tanggal2.setText(movieObject.getString("name"));

                                                            }

                                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                                            Date datetanggal = format.parse(tanggal);
                                                            simpleCalendarView.setSelectedDate(datetanggal);

                                                        } catch (JSONException | ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        tanggal2.setText("Belum ada Event");
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
                                        RequestQueue requestQueue = Volley.newRequestQueue(kalendar_event.this);
                                        requestQueue.add(stringRequest);

                                    }
                                });

                                Collections.sort(event, new Comparator<eventmodel>() {
                                    public int compare(eventmodel o1, eventmodel o2) {
                                        if (o2.getBirth_date() == null || o1.getBirth_date() == null)
                                            return 0;
                                        return o2.getBirth_date().compareTo(o1.getBirth_date());
                                    }
                                });

                            }
                        } catch (JSONException | ParseException e) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static class ListViewAdapterEvent extends ArrayAdapter<eventmodel> {
        private List<eventmodel> event;

        private Context context;

        public ListViewAdapterEvent(List<eventmodel> event, Context context) {
            super(context, R.layout.list_item_event, event);
            this.event = event;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_event, null, true);

            TextView name = listViewItem.findViewById(R.id.name);
            TextView date = listViewItem.findViewById(R.id.date);

            eventmodel movieItem = event.get(position);

            name.setText(movieItem.getName());
            date.setText(convertFormat2(movieItem.getBirth_date()));

            return listViewItem;
        }
    }

    private void setNavigationDrawer() {
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
                }
                else if (itemId == R.id.nav_exit) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            kalendar_event.this);
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
                                    Intent intent = new Intent(kalendar_event.this, MainActivity.class);
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
    }

    private void checkcalendar() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        System.out.println("Current time => " + formattedDate);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/event/index?birth_date=" + formattedDate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final String keterangan = movieObject.getString("name");
                                tanggal2.setText(keterangan);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tanggal2.setText("Belum Ada Event");
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
                        7200000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(kalendar_event.this);
        requestQueue.add(stringRequest);
    }

    private void makeJsonObjectRequest() {

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/event/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final String keterangan = movieObject.getString("birth_date");
                                Date date = simpleDateFormat.parse(keterangan);

                                CalendarDay day = CalendarDay.from(date);
                                events.add(day);
                            }

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                        EventDecorator eventDecorator = new EventDecorator(Color.RED, events);
                        simpleCalendarView.addDecorator(eventDecorator);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tanggal2.setText("Belum Ada Event");
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
                        7200000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(kalendar_event.this);
        requestQueue.add(stringRequest);

    }


    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd");
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

    public static String convertFormat2(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return convetDateFormat.format(date);
    }

    @Override
    protected void onDestroy() {
        int beta = Integer.parseInt(txt_alpha.getText().toString());


        super.onDestroy();
    }

//    public static String convertFormat3(String inputDate) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss zXXX yyyy");
//        Date date = null;
//        try {
//            date = simpleDateFormat.parse(inputDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        if (date == null) {
//            return "";
//        }
//        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        return convetDateFormat.format(date);
//    }
}