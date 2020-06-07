package com.mahmoud.bashir.evomdriverapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.adapters.Requests_adpt;

public class Requests_Fragment extends Fragment {

    RecyclerView rec_requests;
    Requests_adpt requests_adpt;

    public Requests_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_requests, container, false);
        //init views
        rec_requests=v.findViewById(R.id.rec_requests);

        //recyclerView
        rec_requests.setHasFixedSize(true);
        rec_requests.setLayoutManager(new LinearLayoutManager(getContext()));

        requests_adpt = new Requests_adpt(getContext());
        rec_requests.setAdapter(requests_adpt);

        return v;
    }
}