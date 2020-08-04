package com.mahmoud.bashir.evomdriverapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.adapters.History_wallet_adpt;
import com.mahmoud.bashir.evomdriverapp.pojo.DriverHistory_Model;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Wallet_Activity extends AppCompatActivity {

    @BindView(R.id.rec_wallet)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.wallet_val)
    TextView wallet_val;

    //firebase
    DatabaseReference reference,wallet_value;
    FirebaseAuth auth;
    String CUID;

    //adapter
    History_wallet_adpt history_wallet_adpt;

    //model
    DriverHistory_Model driverHistory_model;
    List<DriverHistory_Model> mlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        ButterKnife.bind(this);


        // init toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        //init firebase
        //driver reference
        auth = FirebaseAuth.getInstance();
        CUID = auth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("History");
        wallet_value = FirebaseDatabase.getInstance().getReference().child("Wallet");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        wallet_value.child("Drivers").child(CUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    String wallet_value = dataSnapshot.child("wallet_value").getValue().toString();
                    if (!wallet_value.equals(null)){
                        wallet_val.setText(wallet_value);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
        reference.child("Drivers").child(CUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String id = snapshot.child("id").getValue().toString();
                        String name =snapshot.child("name").getValue().toString();
                        String phone=snapshot.child("phone").getValue().toString();
                        String user_lat=snapshot.child("user_lat").getValue().toString();
                        String user_lng=snapshot.child("user_lng").getValue().toString();
                        String dest_lat=snapshot.child("dest_lat").getValue().toString();
                        String dest_lng=snapshot.child("dest_lng").getValue().toString();
                        String time=snapshot.child("time").getValue().toString();
                        String deviceToken=snapshot.child("deviceToken").getValue().toString();
                        String requestStatus=snapshot.child("requestStatus").getValue().toString();
                        driverHistory_model = new DriverHistory_Model(id,name,phone,user_lat,user_lng,dest_lat,dest_lng,time,deviceToken,requestStatus);

                        mlist.add(driverHistory_model);
                        history_wallet_adpt=new History_wallet_adpt(Wallet_Activity.this,mlist);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.setAdapter(history_wallet_adpt);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}