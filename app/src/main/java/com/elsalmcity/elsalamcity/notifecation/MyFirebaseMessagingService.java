package com.elsalmcity.elsalamcity.notifecation;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.elsalmcity.elsalamcity.R;

import com.elsalmcity.elsalamcity.commen.Commen;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Bitmap bitmapm;
    Bitmap image;
    private static final String TAG = "MyFirebaseMessagingService";

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {


        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                bitmapm=bitmap;

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };


        String click_action = remoteMessage.getData().get("click_action");
        final String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        String urip = "";
        if (remoteMessage.getData().size() > 0) {

            urip = remoteMessage.getData().get("image");
            Commen.print(urip, "masseg");
            try {
                URL url = new URL(urip);
                 image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch(IOException e) {
                System.out.println(e);
            }

        }
      /*  if (!TextUtils.isEmpty(urip)) {
            final String finalUrip = urip;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                         @Override
                                                         public void run() {
                                                             Picasso.with(getBaseContext()
                                                             ).load(finalUrip).into(target);


                                                         }
                                                     }
            );
        }
*//*        ImageRequest imageRequest = new ImageRequest(urip, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Commen.print(response.toString(), "resopns");

           //     bitmapm = response;
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingltion.getInstance(this).addToRequest(imageRequest);*/


        Intent intent = new Intent(click_action);
        intent.putExtra("title", title);
        String channel_id = "web_app_channel";

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                1, intent,  PendingIntent.FLAG_UPDATE_CURRENT);


        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), channel_id)
                        .setSound(uri)

                        .setContentTitle(title)
                        .setContentText(body)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(image))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                        /*    .setLargeIcon(bitmapm)
                             .setPriority(Notification.PRIORITY_MAX)
                             .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                             .setAutoCancel(true)
     //                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                             .setOnlyAlertOnce(true)*/



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.logoonc);

        } else {
            builder.setSmallIcon(R.drawable.baseline_facebook_24);
        }

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
        int id = (int) System.currentTimeMillis();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("TAC", "demo",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(id, builder.build());

    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        String tokenRefresh = FirebaseInstanceId.getInstance().getToken();

//        UpdateToken(tokenRefresh);

    }
//
//    private void UpdateToken(final String tokenRefresh) {
//
//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        DatabaseReference ref = db.getReference("Tokens");
//        Token data = new Token(tokenRefresh, true);
//        ref.child(tokenRefresh).setValue(data);
//
//    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
