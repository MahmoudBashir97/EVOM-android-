package com.mahmoud.bashir.evomdriverapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.mahmoud.bashir.evomdriverapp.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        // init toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}