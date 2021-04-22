package com.example.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String msgTitle = remoteMessage.getNotification().getTitle();
        String msgBody = remoteMessage.getNotification().getBody();

        Map<String,String> dataMap = remoteMessage.getData();

        String position = dataMap.get("position");
        String requirement = dataMap.get("requirement");

        FCMNotifications(msgTitle,msgBody,position,requirement);
    }
    private void FCMNotifications(String title,String body,String position,String requirement) {

        //Build Notification Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"ID");
        builder.setSmallIcon(R.drawable.ic_stat_name2);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setAutoCancel(true);

        //Build PendingIntent
        Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("requirement",requirement);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        //Build Notification Manager Based on API Version Level
        int id =(int)System.currentTimeMillis();
        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("ID", "demo", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(id,builder.build());
        }else {
            notificationManager.notify(id,builder.build());
        }





    }
}