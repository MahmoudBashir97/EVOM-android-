package com.mahmoud.bashir.evomdriverapp.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.adapters.Requests_adpt;

public class Requests_Fragment extends FragmentActivity {

    RecyclerView rec_requests;
    Requests_adpt requests_adpt;

    public Requests_Fragment() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_requests);

        //init views
        rec_requests=findViewById(R.id.rec_requests);

        //recyclerView
        rec_requests.setHasFixedSize(true);
        rec_requests.setLayoutManager(new LinearLayoutManager(this));

        requests_adpt = new Requests_adpt(this);
        rec_requests.setAdapter(requests_adpt);
    }
}