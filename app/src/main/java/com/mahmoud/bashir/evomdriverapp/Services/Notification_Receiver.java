package com.mahmoud.bashir.evomdriverapp.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.bashir.evomdriverapp.R;

import java.text.DecimalFormat;
import java.util.List;


public class Notification_Receiver extends BroadcastReceiver {

    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser user;
    double Mylat,Mylng,user_lat,user_lng;
    LatLng mylatLng,latLngEnd;
    String status_req;


    Location location1 = new Location("");
    Location location2 = new Location("");
    int distance;

    @Override
    public void onReceive(Context context, Intent intent) {

        Mylat = intent.getDoubleExtra("Mylat",1.0);
        Mylng = intent.getDoubleExtra("Mylng",1.0);
        String  CUID = intent.getStringExtra("CUID");
        status_req = intent.getStringExtra("requestStatus");
        user_lat = intent.getDoubleExtra("user_lat",1.0);
        user_lng = intent.getDoubleExtra("user_lng",1.0);

        if (status_req.equals("Pending")){
            latLngEnd = new LatLng(user_lat,user_lng);

            mylatLng = new LatLng(Mylat,Mylng);

            int d = CalculationByDistance(mylatLng,latLngEnd);

            if(d <= 80 && d >= 0 ){ Notification_fun(context,d); }
        }

        getRequestsNot(context,CUID);
       // Notification_fun(context,520);
    }

    public void Notification_fun (Context context ,int distance){

        Intent reciveMessage = new Intent(context, MyReciever.class)
                .putExtra("Mylat",Mylat)
                .putExtra("Mylng",Mylng)
                .setAction("request");

        PendingIntent pendingIntentAccept = PendingIntent.getBroadcast(context, 2, reciveMessage, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("EVOM Driver app")
                .setContentText(" هناك طلب توصيل على بعد "+ distance+ " كيلومتر ")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info")
                .setVibrate(new long[3000])
                .addAction(R.drawable.ic_baseline_done_all_24, "View", pendingIntentAccept);

        NotificationManager notificationManager= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());

    }
    public int CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 10000;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        //return Radius * c;
        return kmInDec;
    }

    public void getRequestsNot(Context context,String CUID){

    }
}
