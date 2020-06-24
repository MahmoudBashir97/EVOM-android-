package com.mahmoud.bashir.evomdriverapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mahmoud.bashir.evomdriverapp.Maps.Home_MapsActivity;
import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.Storage.SharedPrefranceManager;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Login_Activity extends AppCompatActivity {

    @BindView(R.id.login_btn) Button login_btn;
    @BindView(R.id.login_email) TextInputEditText login_email;
    @BindView(R.id.login_pass) TextInputEditText login_pass;
    @BindView(R.id.to_signup) TextView to_signup;


    DatabaseReference userRef;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if(SharedPrefranceManager.getInastance(this).isLoggedIn()){
            startActivity(new Intent(this,Home_MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }

        //init firebase
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");


        login_btn.setOnClickListener(view -> {
            Intent i = new Intent(Login_Activity.this, Home_MapsActivity.class);

            String e_mail = login_email.getText().toString();
            String p_ass = login_pass.getText().toString();

            if (e_mail.isEmpty()){
                login_email.setError("Please Enter your email correctly...!");
                login_email.requestFocus();
            }else if (p_ass.isEmpty()){
                login_pass.setError("Please Enter your password correctly...!");
                login_pass.requestFocus();
            } else if (login_pass.length() < 6) {
                login_pass.setError("Please Enter your email correctly with length above 6 characters...!");
                login_pass.requestFocus();
            }else {
                final ProgressDialog pd = new ProgressDialog(this);
                pd.setTitle("Sign In!");
                pd.setMessage("Please wait");
                pd.setCanceledOnTouchOutside(false);
                pd.show();

                auth.signInWithEmailAndPassword(e_mail, p_ass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    String currentuserID=auth.getCurrentUser().getUid();
                                    i.putExtra("FCUID",currentuserID);
                                    startActivity(i);
                                    finish();
                                    SharedPrefranceManager.getInastance(Login_Activity.this).UserLogin(e_mail);
                                    pd.dismiss();
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(Login_Activity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        to_signup.setOnClickListener(view -> {
            Intent i = new Intent(Login_Activity.this,SignUp_Activity.class);
            startActivity(i);
            finish();
        });
    }
}