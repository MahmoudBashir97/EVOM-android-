package com.mahmoud.bashir.evomdriverapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmoud.bashir.evomdriverapp.Maps.Home_MapsActivity;
import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.Storage.SharedPrefranceManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Car_Info_Activity extends AppCompatActivity {

    @BindView(R.id.next_btn) Button next_btn;
    @BindView(R.id.etd_car_model)
    EditText etd_car_model;
    @BindView(R.id.etd_car_no) EditText etd_car_no;


    DatabaseReference reference;
    FirebaseAuth auth;

    String CUID,uri,fname,dte,Nat_ID,e_mail,phone_no,devicetoken,FCUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car__info);

        ButterKnife.bind(this);

        //init firebase
        auth = FirebaseAuth.getInstance();
        // CUID=auth.getCurrentUser().getUid();


        CUID = getIntent().getStringExtra("CUID");
        FCUID = getIntent().getStringExtra("FCUID");
        uri = getIntent().getStringExtra("uri");
        fname = getIntent().getStringExtra("fname");
        dte = getIntent().getStringExtra("birthdate");
        Nat_ID = getIntent().getStringExtra("NatID");
        e_mail = getIntent().getStringExtra("e_mail");
        phone_no = getIntent().getStringExtra("phone_no");
        devicetoken = getIntent().getStringExtra(devicetoken);


        Toast.makeText(this, ""+FCUID, Toast.LENGTH_SHORT).show();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        next_btn.setOnClickListener(view -> {

            Intent i = new Intent(Car_Info_Activity.this, Home_MapsActivity.class);

            if (TextUtils.isEmpty(etd_car_model.getText().toString())) {
                etd_car_model.setError("Please Enter your Car Model...!");
                etd_car_model.requestFocus();
            } else if (TextUtils.isEmpty(etd_car_no.getText().toString())) {
                etd_car_model.setError("Please Enter your Car Number...!");
                etd_car_model.requestFocus();
            } else {

                HashMap<String, Object> map = new HashMap<>();
                map.put("CarModel", etd_car_model.getText().toString());
                map.put("CarNumber", etd_car_no.getText().toString());

                reference.child("Drivers").child(FCUID).updateChildren(map);
                i.putExtra("FCUID",FCUID);
                startActivity(i);
                finish();
                SharedPrefranceManager.getInastance(this).saveCarInfo(etd_car_model.getText().toString(), etd_car_no.getText().toString());
                SharedPrefranceManager.getInastance(this).saveUser(CUID,fname, e_mail, phone_no, Nat_ID, devicetoken, uri);
            }

        });

    }
}