package com.mahmoud.bashir.evomdriverapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.Storage.SharedPrefranceManager;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Activity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.driver_pic) CircleImageView driver_pic;
    @BindView(R.id.driver_name) TextView driver_name;
    @BindView(R.id.driver_NID) TextView driver_NID;
    @BindView(R.id.driver_address) TextView driver_address;
    @BindView(R.id.car_sort) TextView car_sort;
    @BindView(R.id.car_no) TextView car_no;


    DatabaseReference reference;
    FirebaseAuth auth;

    String CUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        // init toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //init firebase
        auth = FirebaseAuth.getInstance();
        CUID=auth.getCurrentUser().getUid();

        reference= FirebaseDatabase.getInstance().getReference().child("Users");


        if (isNetworkConnected()){

            reference.child("Drivers").child(CUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        String pro_pic = dataSnapshot.child("driver_image").getValue().toString();
                        String full_name = dataSnapshot.child("full_name").getValue().toString();
                        String national_id = dataSnapshot.child("national_ID").getValue().toString();

                        String CarModel = dataSnapshot.child("CarModel").getValue().toString();
                        String CarNumber = dataSnapshot.child("CarNumber").getValue().toString();


                        Picasso.get().load(pro_pic).into(driver_pic);
                        driver_name.setText(full_name);
                        driver_NID.setText(national_id);

                        car_sort.setText(CarModel);
                        car_no.setText(CarNumber);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else {

            String pro_pic = SharedPrefranceManager.getInastance(this).getimageUri();
            String full_name = SharedPrefranceManager.getInastance(this).getFullName();
            String national_id = SharedPrefranceManager.getInastance(this).getDriverNID();
            String CarModel = SharedPrefranceManager.getInastance(this).getCarModel();
            String CarNumber = SharedPrefranceManager.getInastance(this).getCarNumber();

            Picasso.get().load(pro_pic).into(driver_pic);
            driver_name.setText(full_name);
            driver_NID.setText(national_id);

            car_sort.setText(CarModel);
            car_no.setText(CarNumber);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}