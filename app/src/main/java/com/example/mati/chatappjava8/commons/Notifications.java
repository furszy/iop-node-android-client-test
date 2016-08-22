package com.example.mati.chatappjava8.commons;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.example.mati.chatappjava8.IntentConstants;
import com.example.mati.chatappjava8.R;
import com.example.mati.chatappjava8.chat.ChatActivity2;

import java.util.Random;

/**
 * Created by mati on 17/08/16.
 */
public class Notifications {


    public static void pushNotification(Context context, String content, String senderPk){
        Intent intent = new Intent(context, ChatActivity2.class);
        intent.putExtra(IntentConstants.PROFILE_RECEIVER_NOTIFICATION_PK,senderPk);
        PendingIntent pi = PendingIntent
                .getActivity(context, 0, intent,  PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder builder = new Notification.Builder(context).setTicker("ticker")
                .setContentTitle("New Message arrived")
                .setSubText(content)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.in_message_bg)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.YELLOW, 3000, 3000)
                .setContentIntent(pi);
        Random random = new Random();
        NotificationManager notificationManager = (NotificationManager)context.
                getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(random.nextInt(),builder.build());
    }
}
