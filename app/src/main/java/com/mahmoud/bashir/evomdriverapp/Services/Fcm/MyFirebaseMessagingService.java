package com.mahmoud.bashir.evomdriverapp.Services.Fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mahmoud.bashir.evomdriverapp.Maps.Home_MapsActivity;
import com.mahmoud.bashir.evomdriverapp.R;


import static androidx.core.app.NotificationCompat.PRIORITY_MAX;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String LOG_TAG = "MyFirebaseMessaging";
    public static final String MESSAGE = "request";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(LOG_TAG, remoteMessage.getData().toString() + "");

        Log.d(LOG_TAG, "onMessageReceived: ");
        String request = remoteMessage.getData().get("requestStatus");

        if (request.equals("Pending")){

            String senderName = remoteMessage.getData().get("name");
            GetRequest(senderName);
        }
    }

    private void GetRequest( String senderName){

        Intent reciveMessage = new Intent(getApplicationContext(), MyReceiver.class)
                .setAction("deliver_Request");

        PendingIntent pendingIntentAccept = PendingIntent.getBroadcast(this, 3, reciveMessage, PendingIntent.FLAG_UPDATE_CURRENT);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Home_MapsActivity.class);
        stackBuilder.addNextIntent(reciveMessage);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        3,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.app.Notification build = new NotificationCompat.Builder(this, MESSAGE)
                .setSmallIcon(R.drawable.ic_baseline_done_all_24)
                .setPriority(PRIORITY_MAX)
                .setContentTitle(String.format("You have new deliver Request \n From", senderName))
                .addAction(R.mipmap.ic_launcher, "View", pendingIntentAccept)
                .setVibrate(new long[3000])
                .setChannelId(MESSAGE)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(MESSAGE, MESSAGE, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(2, build);
    }

}
