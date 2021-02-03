package com.bardisammar.elsalamcity.notifecation;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.commen.Commen;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Bitmap image;
    private NotificationManager mManager;
    public static final String EDMIT_ID = "com.bardisammar.elsalamcity";
    public static final String CHANNEL_NAME = "ANDROID CHANNEL";

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String click_action = remoteMessage.getData().get("click_action");
        final String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");



        String urip;
        if (remoteMessage.getData().size() > 0) {

            urip = remoteMessage.getData().get("image");
            Commen.print(urip, "masseg");
            try {
                URL url = new URL(urip);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException ignored) {

            }


            Intent intent = new Intent(click_action);
            intent.putExtra("title", title);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    1, intent, PendingIntent.FLAG_UPDATE_CURRENT);


            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            final NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getApplicationContext(), EDMIT_ID)
                            .setSound(uri)

                            .setContentTitle(title)
                            .setContentText(body)
                            .setStyle(new NotificationCompat.BigPictureStyle()
                                    .bigPicture(image))
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent);


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.logo);

            } else {
                builder.setSmallIcon(R.drawable.logo);
            }

            NotificationManager notificationManager =
                    (NotificationManager)
                            getSystemService(Context.NOTIFICATION_SERVICE);
            int id = (int) System.currentTimeMillis();

            if (Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel=new NotificationChannel(EDMIT_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(false);
                notificationChannel.enableVibration(true);
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

                getManger().createNotificationChannel(notificationChannel);
            /*
                NotificationChannel channel = new NotificationChannel(NotifecationHelper.EDMIT_ID, NotifecationHelper.CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH);
                channel.enableVibration(true);
                channel.enableLights(true);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

                notificationManager.createNotificationChannel(channel);*/
            }
            notificationManager.notify(id, builder.build());


        }
        Commen.count++;

    }

    public NotificationManager getManger() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

}
