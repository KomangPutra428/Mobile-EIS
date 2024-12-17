package com.example.eis2.firebase;

import static android.text.Html.FROM_HTML_MODE_LEGACY;
import static android.text.Html.fromHtml;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import com.example.eis2.Item.MyService;
import com.example.eis2.R;
import com.example.eis2.approval_cuti_khusus;
import com.example.eis2.approval_cuti_tahunan;
import com.example.eis2.approval_dinas;
import com.example.eis2.approval_dinas_full;
import com.example.eis2.approval_dinas_nonfull;
import com.example.eis2.approval_fullday;
import com.example.eis2.approval_nonfull;
import com.example.eis2.menu;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyFirebaseServices extends FirebaseMessagingService {
    private static final String CHANNEL_ID ="101";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            showNotification(title, body);
        }
    }

    private void showNotification(String title,String message) {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews customLayout = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);

//        customLayout.setTextViewText(R.id.notificationTitle, title);
//        customLayout.setTextViewText(R.id.notificationMessage, message);

        TextView messaging = new TextView(MyFirebaseServices.this);
        String pesan = "<b>" + message + "</b>";
        messaging.setText(Html.fromHtml(pesan));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Push_NotifiCation",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }
        if(message.contains("Sudah Di Approve") || message.contains("Tidak Di Approve") || message.contains("Hangus")){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(getBitmapFromVectorDrawable(this, R.drawable.icon))
                    .setContentTitle(Html.fromHtml(title))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(messaging.getText().toString())))
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH); // Dismiss the notification when clicked
            Notification notification = builder.build();
            notificationManager.notify(createRandomCode(7), notification);
        } else if (message.contains("SAKIT") || (message.contains("SA") || message.contains("CDT"))) {
                Intent intent = new Intent(getApplicationContext(), approval_fullday.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(getBitmapFromVectorDrawable(this, R.drawable.icon))
                    .setContentTitle(Html.fromHtml(title))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(messaging.getText().toString()))) // Set the custom layout
                    .setAutoCancel(true) // Dismiss the notification when clicked
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

                Notification notification = builder.build();
                notificationManager.notify(createRandomCode(7), notification);
            } else if (message.contains("DATANG TELAT") || message.contains("DT") || message.contains("PULANG CEPAT") || message.contains("PC") || message.contains("KELUAR KANTOR") || message.contains("KK")) {
                 Intent intent = new Intent(this, approval_nonfull.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(getBitmapFromVectorDrawable(this, R.drawable.icon))
                    .setContentTitle(Html.fromHtml(title))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(messaging.getText().toString()))) // Set the custom layout
                    .setAutoCancel(true) // Dismiss the notification when clicked
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

                Notification notification = builder.build();
                notificationManager.notify(createRandomCode(7), notification);
            } else if (message.contains("DINAS FULL DAY")) {
                Intent intent = new Intent(this, approval_dinas_full.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(getBitmapFromVectorDrawable(this, R.drawable.icon))
                    .setContentTitle(Html.fromHtml(title))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(messaging.getText().toString()))) // Set the custom layout
                    .setAutoCancel(true) // Dismiss the notification when clicked
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

                Notification notification = builder.build();
                notificationManager.notify(createRandomCode(7), notification);
            } else if (message.contains("DINAS NON FULL DAY") || message.contains("Non Full Day DN")) {
                Intent intent = new Intent(this, approval_dinas_nonfull.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);



            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(getBitmapFromVectorDrawable(this, R.drawable.icon))
                    .setContentTitle(Html.fromHtml(title))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(messaging.getText().toString()))) // Set the custom layout
                    .setAutoCancel(true) // Dismiss the notification when clicked
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

                Notification notification = builder.build();
                notificationManager.notify(createRandomCode(7), notification);
            } else if (message.contains("CUTI TAHUNAN")) {
                 Intent intent = new Intent(this, approval_cuti_tahunan.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(getBitmapFromVectorDrawable(this, R.drawable.icon))
                    .setContentTitle(Html.fromHtml(title))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(messaging.getText().toString()))) // Set the custom layout
                    .setAutoCancel(true) // Dismiss the notification when clicked
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

                 Notification notification = builder.build();
                 notificationManager.notify(createRandomCode(7), notification);
            } else if (message.contains("CUTI KHUSUS")) {
                 Intent intent = new Intent(this, approval_cuti_khusus.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(getBitmapFromVectorDrawable(this, R.drawable.icon))
                    .setContentTitle(Html.fromHtml(title))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(messaging.getText().toString()))) // Set the custom layout
                    .setAutoCancel(true) // Dismiss the notification when clicked
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

                 Notification notification = builder.build();
                 notificationManager.notify(createRandomCode(7), notification);

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(getBitmapFromVectorDrawable(this, R.drawable.icon))
                    .setContentTitle(Html.fromHtml(title))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(messaging.getText().toString()))) // Set the custom layout
                    .setAutoCancel(true) // Dismiss the notification when clicked
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            Notification notification = builder.build();
            notificationManager.notify(createRandomCode(7), notification);
        }


    };

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
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


}

