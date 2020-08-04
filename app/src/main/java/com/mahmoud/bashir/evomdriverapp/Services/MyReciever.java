package com.mahmoud.bashir.evomdriverapp.Services;

import android.app.Application;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.bashir.evomdriverapp.fragments.Requests_Fragment;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MyReciever extends BroadcastReceiver {

    private static final String TAG = "MyReciever";

    String message;
    DatabaseReference reference;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " +context.getApplicationContext());


        String user_Token = intent.getStringExtra("user_Token");
        String   CUID = intent.getStringExtra("CUID");
        double Mylat = intent.getDoubleExtra("Mylat", 1);
        double Mylng = intent.getDoubleExtra("Mylng", 1);



        if (intent.getAction().equals("request")) {
            NotificationManager manag = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
           // manag.cancel(order_id);

            context.startActivity(new Intent(context, Requests_Fragment.class)
                    .putExtra("MyLat",Mylat+"")
                    .putExtra("MyLng",Mylng+"")
                    .addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
            );
        }
    }

}
