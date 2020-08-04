package com.mahmoud.bashir.evomdriverapp.Services.Fcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.mahmoud.bashir.evomdriverapp.fragments.Requests_Fragment;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MyReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive: " + intent.getAction());

                         if (intent.getAction().equals("deliver_Request")) {
                             context.startActivity(new Intent(context, Requests_Fragment.class)
                                     .addFlags(FLAG_ACTIVITY_NEW_TASK));
                         }
    }
}
