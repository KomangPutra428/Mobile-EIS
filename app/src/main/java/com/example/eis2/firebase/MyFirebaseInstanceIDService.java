package com.example.eis2.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.eis2.R;
import com.example.eis2.approval_cuti_khusus;
import com.example.eis2.approval_cuti_tahunan;
import com.example.eis2.approval_dinas_full;
import com.example.eis2.approval_dinas_nonfull;
import com.example.eis2.approval_fullday;
import com.example.eis2.approval_nonfull;
import com.example.eis2.menu;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.security.SecureRandom;
import java.util.Random;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = "MyService";
    NotificationCompat.Builder notifBuilder;

    Intent intent;
    private static final String CHANNEL_ID ="101";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From : " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload : " + remoteMessage.getData());
        }
        if (remoteMessage.getData() != null) {
            String title = remoteMessage.getData().get("title"); //will hold FCM Title
            String message = remoteMessage.getData().get("message"); //will hold FCM message body
            sendNotification(title, message);
        }
    }

    private void sendNotification(String title, String message) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "My Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            // Optionally, set additional channel settings here

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        if (message.contains("Anda Sudah Di Approve")||
                message.contains("Anda Tidak Di Approve")){
            intent = new Intent(this, menu.class);
        } else if (message.contains("Sakit") || message.contains("CDT")) {
            intent = new Intent(this, approval_fullday.class);
        } else if (message.contains("Datang Telat") || message.contains("Pulang Cepat") || message.contains("Keluar Kantor")) {
            intent = new Intent(this, approval_nonfull.class);
        } else if (message.contains("Dinas Full Day")) {
            intent = new Intent(this, approval_dinas_full.class);
        } else if (message.contains("Dinas Non Full Day")) {
            intent = new Intent(this, approval_dinas_nonfull.class);
        } else if (message.contains("Cuti Tahunan")) {
            intent = new Intent(this, approval_cuti_tahunan.class);
        } else if (message.contains("Cuti Khusus")) {
            intent = new Intent(this, approval_cuti_khusus.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // This flag ensures the activity is brought to the front
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(createRandomCode(7), PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Hallo Apa Kabar")
                .setContentText(message)
                .setAutoCancel(true); // Automatically dismiss the notification when clicked
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(createRandomCode(7), notification); // Use a unique notification ID

    }

    public int createRandomCode(int codeLength) {
        char[] chars = "1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < codeLength; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return Integer.parseInt(sb.toString());
    }

}

