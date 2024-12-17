package com.example.eis2.Item;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class stopService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent background = new Intent(context, MyService.class);
        context.stopService(background);
    }
}
