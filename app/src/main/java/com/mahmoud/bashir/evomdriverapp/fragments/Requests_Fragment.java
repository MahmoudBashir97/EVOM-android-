package com.mahmoud.bashir.evomdriverapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.bashir.evomdriverapp.Api_Interface.api_Interface;
import com.mahmoud.bashir.evomdriverapp.Maps.Home_MapsActivity;
import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.Storage.SharedPrefranceManager;
import com.mahmoud.bashir.evomdriverapp.adapters.AcceptRequest_Interface;
import com.mahmoud.bashir.evomdriverapp.adapters.Requests_adpt;
import com.mahmoud.bashir.evomdriverapp.pojo.Request_Model;
import com.mahmoud.bashir.evomdriverapp.pojo.RequestStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Requests_Fragment extends AppCompatActivity implements AcceptRequest_Interface {



    //FCM section
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAWTfqi_A:APA91bHiuXVv9PZxm24FNqKLHN1Te6qz4OH9KsgJ9Vdzv-BoA-rrc8Sow2mo0E4AFHPbBamwgugD7vczzur1S-n1vKN58QbWMrXgBXHmb1osHnZbpX82ThaE7SP_n2wohQH67vx9c_Ma";
    final private String contentType = "application/json";
    final String TAGI = "NOTIFICATION TAG";
    String BaseURL="https://fcm.googleapis.com/";
    String driver_device_Token;

    api_Interface anInterface;




    RecyclerView rec_requests;
    Requests_adpt requests_adpt;

    DatabaseReference reference;
    FirebaseDatabase database;
    FirebaseAuth auth;

    String CUID;
    double driver_lat,driver_lng;

    //for Requests_Data
    Request_Model request_model;
    List<Request_Model> mlist = new ArrayList<>();
    RequestStatus requestStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_requests);


        //getIntent Value
        driver_lat = Double.parseDouble(getIntent().getStringExtra("MyLat").toString());
        driver_lng = Double.parseDouble(getIntent().getStringExtra("MyLng").toString());

        //init views
        rec_requests=findViewById(R.id.rec_requests);

        //recyclerView
        rec_requests.setHasFixedSize(true);
        rec_requests.setLayoutManager(new LinearLayoutManager(this));

        // init views
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setContentInsetsAbsolute(toolbar.getContentInsetLeft(), 200);


        //init firebase
        auth = FirebaseAuth.getInstance();
        CUID= auth.getCurrentUser().getUid();

        reference= FirebaseDatabase.getInstance().getReference().child("Requests");

        add_data_to_Rec();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void add_data_to_Rec(){
        reference.child("Drivers").child(CUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot:
                            dataSnapshot.getChildren()) {

                        String requestStatus = snapshot.child("requestStatus").getValue().toString();

                        if (requestStatus.equals("Pending")){

                            String id = snapshot.child("id").getValue().toString();
                            String name = snapshot.child("name").getValue().toString();
                            String phone = snapshot.child("phone").getValue().toString();
                            String deviceToken = snapshot.child("deviceToken").getValue().toString();
                            double user_lat = Double.parseDouble(snapshot.child("user_lat").getValue().toString());
                            double user_lng = Double.parseDouble(snapshot.child("user_lng").getValue().toString());
                            double dest_lat = Double.parseDouble(snapshot.child("dest_lat").getValue().toString());
                            double dest_lng = Double.parseDouble(snapshot.child("dest_lng").getValue().toString());
                            String time = snapshot.child("time").getValue().toString();

                            request_model = new Request_Model( id,
                                    name,
                                    phone,
                                    deviceToken,
                                    user_lat,
                                    user_lng,
                                    dest_lat,
                                    dest_lng,
                                    time);

                            mlist.add(request_model);
                            requests_adpt = new Requests_adpt(Requests_Fragment.this,mlist,driver_lat,driver_lng,Requests_Fragment.this);
                        }
                    }
                    rec_requests.setAdapter(requests_adpt);
                   // requests_adpt.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void OnClickAccept(double user_lat, double user_lng, double dest_lat, double dest_lng, String user_phone, String user_name) {

       /* Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        anInterface=retrofit.create(api_Interface.class);*/
        String driverName = SharedPrefranceManager.getInastance(this).getFullName();



        Intent i = new Intent(Requests_Fragment.this, Home_MapsActivity.class);
        i.putExtra("accept","accept");
        i.putExtra("user_lat",user_lat+"");
        i.putExtra("user_lng",user_lng+"");
        i.putExtra("dest_lat",dest_lat+"");
        i.putExtra("dest_lng",dest_lng+"");
        i.putExtra("user_phone",user_phone+"");
        i.putExtra("user_name",user_name+"");



        HashMap<String , Object> map = new HashMap<>();
        map.put("requestStatus","accept");

        reference.child("Drivers").child(CUID).child(user_phone).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    reference.child("Customers").child(user_phone).child(CUID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                requestStatus = new RequestStatus("accept",""+driverName);
                              /*  Call<RequestStatus> call = anInterface.storedata(requestStatus);
                                call.enqueue(new Callback<RequestStatus>() {
                                    @Override
                                    public void onResponse(Call<RequestStatus> call, Response<RequestStatus> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<RequestStatus> call, Throwable t) {
                                        Toast.makeText(Requests_Fragment.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });*/
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void OnClickreject(double user_lat, double user_lng, double dest_lat, double dest_lng, String user_phone, String user_name) {

       /* Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        anInterface=retrofit.create(api_Interface.class);
*/
        String driverName = SharedPrefranceManager.getInastance(this).getFullName();



        HashMap<String , Object> map = new HashMap<>();
        map.put("requestStatus","reject");

        reference.child("Drivers").child(CUID).child(user_phone).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    reference.child("Customers").child(user_phone).child(CUID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                requestStatus = new RequestStatus("reject",""+driverName);

                                /* Call<RequestStatus> call = anInterface.storedata(requestStatus);
                                call.enqueue(new Callback<RequestStatus>() {
                                    @Override
                                    public void onResponse(Call<RequestStatus> call, Response<RequestStatus> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<RequestStatus> call, Throwable t) {
                                        Toast.makeText(Requests_Fragment.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });*/

                                reference.child("Drivers").child(CUID).child(user_phone).removeValue();
                                reference.child("Customers").child(user_phone).child(CUID).removeValue();
                                add_data_to_Rec();

                            }
                        }
                    });
                }
            }
        });
    }
}