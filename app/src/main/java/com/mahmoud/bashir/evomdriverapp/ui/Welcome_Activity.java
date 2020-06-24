package com.mahmoud.bashir.evomdriverapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.mahmoud.bashir.evomdriverapp.Maps.Home_MapsActivity;
import com.mahmoud.bashir.evomdriverapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Welcome_Activity extends AppCompatActivity {


    @BindView(R.id.to_next)
    RelativeLayout to_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

        to_next.setOnClickListener(view -> {
            Intent i =new Intent(Welcome_Activity.this, Login_Activity.class);
            startActivity(i);
            finish();
        });
    }
}