package com.example.eis2.firebase;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class checking_foreground extends Service {

    private static final String TAG = "MyBackgroundService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Your method that you want to run continuously
        startYourMethod();

        // Keep the service running indefinitely
        return START_STICKY;
    }

    private void startYourMethod() {
        // Implement your continuous method here
       Intent i = new Intent(checking_foreground.this, MyFirebaseServices.class);
       startService(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

