package com.wielabs;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import android.os.Handler;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wielabs.Activities.Home;
import com.wielabs.Models.Current_Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    JSONObject data;
    Current_Product current_product;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
                data = json.getJSONObject("data");
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
        try {
            //Current_Product current_product = new Current_Product("200", "http://easyvela.esy.es/CurrentProductImages/hdd.png", "Hdd", "06/27/2020 02:00:00 pm", "20000", "200", "adsf", "asdf", "asdf");
             current_product = new Current_Product(data.getString("id"),
                     data.getString("imageurl"),
                     data.getString("title"),
                     data.getString("endtime"),
                     data.getString("mrp"),
                     data.getString("sp"),
                     data.getString("description"),
                     data.getString("imageurl2"),
                     data.getString("imageurl3"));
            Intent intent = new Intent("FILTER"); //FILTER is a string to identify this intent
            intent.putExtra("MY_KEY", current_product);
            sendBroadcast(intent);
        } catch (JSONException e){

        }

        Log.d(TAG, "From: " + remoteMessage.getFrom());

    }


    private void sendPushNotification(JSONObject json) {

        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

            Intent intent = new Intent(this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Bitmap bmp = null;
            try {
                InputStream in = new URL(imageUrl).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }

            PendingIntent morePendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    new Intent(this, Home.class),
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            String string = "<font color='#ff8b7a'>Bid now</font>";


            RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
            RemoteViews notificationLayoutBig = new RemoteViews(getPackageName(), R.layout.notification_big);
            notificationLayout.setTextViewText(R.id.notification_title, title);
            notificationLayoutBig.setTextViewText(R.id.notification_title_big, message);
            notificationLayout.setImageViewBitmap(R.id.notification_small_image, bmp);
            notificationLayoutBig.setImageViewBitmap(R.id.notification_image, bmp);

            String channelId = "notificationBidBad";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.logo)
                            .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                            .setCustomBigContentView(notificationLayoutBig)
                            .setCustomContentView(notificationLayout)
                            //.setCustomContentView(notificationLayout)
                            .addAction(android.R.drawable.ic_menu_directions, Html.fromHtml(string), morePendingIntent)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    private void sendNotification(String messageBody, String url) {
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap bmp = null;
        try {
            InputStream in = new URL(url).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PendingIntent morePendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                new Intent(this, Home.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        String string = "<font color='#ff8b7a'>Bid now</font>";

        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
        RemoteViews notificationLayoutBig = new RemoteViews(getPackageName(), R.layout.notification_big);
        notificationLayout.setTextViewText(R.id.notification_title, messageBody);
        notificationLayoutBig.setTextViewText(R.id.notification_title_big, messageBody);
        notificationLayout.setImageViewBitmap(R.id.notification_small_image, bmp);
        notificationLayoutBig.setImageViewBitmap(R.id.notification_image, bmp);

        String channelId = "notificationBidBad";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                        .setCustomBigContentView(notificationLayoutBig)
                        .setCustomContentView(notificationLayout)
                        //.setCustomContentView(notificationLayout)
                        .addAction(android.R.drawable.ic_menu_directions, Html.fromHtml(string), morePendingIntent)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}