package com.mahmoud.bashir.evomdriverapp.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.adapters.Requests_adpt;
import com.mahmoud.bashir.evomdriverapp.pojo.Request_Model;

public class Requests_Fragment extends AppCompatActivity {

    RecyclerView rec_requests;
    Requests_adpt requests_adpt;

    DatabaseReference reference;
    FirebaseDatabase database;
    FirebaseAuth auth;

    String CUID;
    double driver_lat,driver_lng;

    //for Requests_Data
    Request_Model request_model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_requests);

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

        requests_adpt = new Requests_adpt(this,driver_lat,driver_lng);
        rec_requests.setAdapter(requests_adpt);



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}