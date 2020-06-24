package com.mahmoud.bashir.evomdriverapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.mahmoud.bashir.evomdriverapp.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Verify_Phone_Activity extends AppCompatActivity {

    @BindView(R.id.driver_phone_no) EditText driver_phone_no;
    @BindView(R.id.etd_code) EditText etd_code;
    @BindView(R.id.ccp) CountryCodePicker ccp;
    @BindView(R.id.send_btn) Button send_btn;
    @BindView(R.id.lin_ph) LinearLayout lin_ph;


    String selectedCode="+20";
    private String verificationID;
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    FirebaseDatabase database;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify__phone);
        ButterKnife.bind(this);


        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();


        // to select country code
        ccp.setDefaultCountryUsingNameCode("(EG) +20");
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selectedCode = ccp.getSelectedCountryCodeWithPlus();
            }
        });

         send_btn.setOnClickListener(v ->{
             String phone = driver_phone_no.getText().toString();
             if (phone.isEmpty() || driver_phone_no.length() < 10) {
                 driver_phone_no.setError("Please Enter right phone number start with 1_ _ _ _");
                 driver_phone_no.requestFocus();
                 lin_ph.setVisibility(View.VISIBLE);
             }else{
                 lin_ph.setVisibility(View.GONE);
                 etd_code.setVisibility(View.VISIBLE);
                 String full_phone = selectedCode + phone;
                 sendVerificationCode(full_phone);

                 Toast.makeText(this, ""+full_phone, Toast.LENGTH_SHORT).show();

                // if (send_btn.getText().equals("Verify")) {
                     //verify transaction
                     String Code = etd_code.getText().toString();
                     if (Code.isEmpty() || Code.length() < 6) {
                         driver_phone_no.setError("Please Enter right Code");
                         driver_phone_no.requestFocus();
                     } else {
                         verifycode(Code, full_phone);
                     }
             }

         });

    }

    private void verifycode(String code,String phone){
        try { PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationID,code);
            signWithCredential(credential,phone);
        }catch (Exception e){
            Toast toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    private void signWithCredential(PhoneAuthCredential credential, final String phone) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            userid = firebaseUser.getUid();

                            Intent intent = new Intent(Verify_Phone_Activity.this, SignUp_Activity.class);
                            intent.putExtra("id",userid);
                            intent.putExtra("phone_no",phone);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(Verify_Phone_Activity.this, "You can not register with this phone number!!", Toast.LENGTH_SHORT).show();
                            driver_phone_no.setVisibility(View.VISIBLE);
                            etd_code.setVisibility(View.GONE);
                            send_btn.setText("Send");
                        }
                    }
                });
    }

    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID=s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String codesms=phoneAuthCredential.getSmsCode();
            if (codesms !=null){
                verifycode(codesms,null);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Verify_Phone_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}