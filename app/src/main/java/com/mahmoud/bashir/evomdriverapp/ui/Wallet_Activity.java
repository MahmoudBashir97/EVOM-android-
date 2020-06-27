package com.mahmoud.bashir.evomdriverapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.adapters.History_wallet_adpt;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Wallet_Activity extends AppCompatActivity {

    @BindView(R.id.rec_wallet)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //adapter
    History_wallet_adpt history_wallet_adpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        ButterKnife.bind(this);


        // init toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        history_wallet_adpt=new History_wallet_adpt(this);

        recyclerView.setAdapter(history_wallet_adpt);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}