package com.example.eis2.Item;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Restarter extends BroadcastReceiver {
    Context context;
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context, "Notifikasi Di Aktifkan", Toast.LENGTH_SHORT).show();

            // context.startService(new Intent(context,MyService.class));
        }
    }

