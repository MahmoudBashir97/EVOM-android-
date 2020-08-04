package com.mahmoud.bashir.evomdriverapp.fragments;

import android.graphics.drawable.Animatable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.bashir.evomdriverapp.R;

import java.util.HashMap;


public class CompleteMissionFragment extends Fragment {

    String user_ph;
    int wallet_value=-1;
    DatabaseReference reference;
    FirebaseAuth mauth;

    public CompleteMissionFragment(String user_ph) {
        // Required empty public constructor
        this.user_ph=user_ph;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_complete_mission, container, false);
        ImageView  imageView = v.findViewById(R.id.imageView);
        RelativeLayout rel_congrats = v.findViewById(R.id.rel_congrats);
        RelativeLayout rel_to_wallet = v.findViewById(R.id.rel_to_wallet);
        RelativeLayout rel_add_user_fare = v.findViewById(R.id.rel_add_user_fare);
        Button add_btn = v.findViewById(R.id.add_btn);
        TextView add_to_user_wallet = v.findViewById(R.id.add_to_user_wallet);
        EditText edt_fare_Value = v.findViewById(R.id.edt_fare_Value);

        ((Animatable) imageView.getDrawable()).start();


        reference = FirebaseDatabase.getInstance().getReference().child("Wallet");


        add_to_user_wallet.setOnClickListener(view -> {

            rel_congrats.setAnimation(outToLeftAnimation());
            rel_to_wallet.setAnimation(outToLeftAnimation());
            rel_congrats.setVisibility(View.GONE);
            rel_to_wallet.setVisibility(View.GONE);
            rel_add_user_fare.setVisibility(View.VISIBLE);
            add_btn.setVisibility(View.VISIBLE);
            rel_add_user_fare.setAnimation(inFromRightAnimation());
            add_btn.setAnimation(inFromRightAnimation());


        });

        add_btn.setOnClickListener(view ->{
            if (TextUtils.isEmpty(edt_fare_Value.getText().toString())){
                edt_fare_Value.setError("Please Enter valid value!");
                edt_fare_Value.requestFocus();
            } else if (edt_fare_Value.getText().toString().equals("0")) {
                edt_fare_Value.setError("Please Enter valid value!");
                edt_fare_Value.requestFocus();
            }else {

                reference.child("Customers").child(user_ph).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            if (dataSnapshot.hasChildren()){
                                wallet_value = Integer.parseInt(dataSnapshot.child("wallet_value").getValue().toString());

                                if (wallet_value != -1){

                                    wallet_value = Integer.parseInt(edt_fare_Value.getText().toString()) + wallet_value;

                                    HashMap<String,Object> map = new HashMap<>();
                                    map.put("wallet_value",wallet_value);
                                    reference.child("Customers").child(user_ph).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Value Added Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        return v;
    }


    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(1500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }


    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(1500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(1500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }


    private Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(1500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }

}