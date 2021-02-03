package com.bardisammar.elsalamcity.notifecation.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.bardisammar.elsalamcity.R;


public class NotifecationHelper extends ContextWrapper {


    private NotificationManager mManager;
    public static final String EDMIT_ID = "com.bardisammar.elsalamcity";
    public static final String CHANNEL_NAME = "ANDROID CHANNEL";
    public NotifecationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChanal();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChanal() {
        NotificationChannel notificationChannel=new NotificationChannel(EDMIT_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManger().createNotificationChannel(notificationChannel);


    }

    public NotificationManager getManger() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder geteatChannelNotification(String title, String body, PendingIntent content
    , Uri soundUri) {
        return new Notification.Builder(getApplicationContext(), EDMIT_ID)
                .setContentIntent(content)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.logo)
                .setSound(soundUri)
                .setAutoCancel(true);
    }

}
