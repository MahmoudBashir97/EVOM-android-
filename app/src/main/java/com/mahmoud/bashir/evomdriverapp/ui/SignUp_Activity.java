package com.mahmoud.bashir.evomdriverapp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.mahmoud.bashir.evomdriverapp.Maps.Home_MapsActivity;
import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.Storage.SharedPrefranceManager;
import com.mahmoud.bashir.evomdriverapp.ViewModel.Uri_ViewModel;
import com.mahmoud.bashir.evomdriverapp.pojo.SignUp_Model;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp_Activity extends AppCompatActivity {

    @BindView(R.id.driver_pic) CircleImageView driver_pic;
    @BindView(R.id.full_name) TextInputEditText full_name;
    @BindView(R.id.date_pic) TextInputEditText date_pic;
    @BindView(R.id.national_id) TextInputEditText national_id;
    @BindView(R.id.email_driver) TextInputEditText email_driver;
    @BindView(R.id.pass_driver) TextInputEditText pass_driver;
    @BindView(R.id.sign_up_btn) Button sign_up_btn;
    @BindView(R.id.upload_NID) TextView upload_NID;
    @BindView(R.id.upload_err) TextView upload_err;
    @BindView(R.id.to_login) TextView to_login;



    private static final int IMAGE_REQUEST=1;
    private static final int Gallerypick=1;
    private static final int TAKE_A_PIC=121;



   // firebase
    private StorageReference UserprofileImage,UserNational_ID_Image;
    StorageReference storageReference;

    private Uri imageuri;
    private StorageTask uploadtask;

    DatabaseReference Users_ref,Verify_ref;
    FirebaseAuth mAuth;
    String CUID;

    Uri Nayional_ID_URi;
    String uri;

    final Calendar mycalendar = Calendar.getInstance();

    SignUp_Model signUp_model;

    String phone_no="+201096589671",muri,Nat_ID_Pic;
    Uri_ViewModel uri_viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        //init firebase
        mAuth = FirebaseAuth.getInstance();
        //CUID=mAuth.getCurrentUser().getUid();


        uri_viewModel = ViewModelProviders.of(this).get(Uri_ViewModel.class);
        Users_ref = FirebaseDatabase.getInstance().getReference().child("Users");
        UserprofileImage = FirebaseStorage.getInstance().getReference().child("Drivers Profile");
        UserNational_ID_Image = FirebaseStorage.getInstance().getReference().child("Drivers Profile").child("National ID");


        //get data from Intent
       // phone_no = getIntent().getStringExtra("phone_no");


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mycalendar.set(Calendar.YEAR,year);
                mycalendar.set(Calendar.MONTH,month);
                mycalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        date_pic.setOnClickListener(view -> {
            new DatePickerDialog(SignUp_Activity.this,date,mycalendar.get(Calendar.MONTH),
                    mycalendar.get(Calendar.DAY_OF_MONTH),
                    mycalendar.get(Calendar.YEAR)).show();
        });


        driver_pic.setOnClickListener(view -> {
            openImage();
        });
        upload_NID.setOnClickListener(view -> {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i,TAKE_A_PIC);
        });

        sign_up_btn.setOnClickListener(view -> {
            Intent i = new Intent(SignUp_Activity.this, Car_Info_Activity.class);

            String fname =full_name.getText().toString() ;
            String dte =date_pic.getText().toString();
            String Nat_ID=national_id.getText().toString();
            String e_mail = email_driver.getText().toString();
            String p_ass= pass_driver.getText().toString();

            if (imageuri == null){
                upload_err.setVisibility(View.VISIBLE);
            }else if (fname.isEmpty()){
                full_name.setError("Please Enter your full name...!");
                full_name.requestFocus();
            } else if (full_name.length() < 16) {
                full_name.setError("Please Enter right full name with length above 16 character...!");
                full_name.requestFocus();
            } else if (dte.isEmpty()) {
                date_pic.setError("Please Enter your Date Birth...!");
                date_pic.requestFocus();
            } else if (Nat_ID.isEmpty()) {
                national_id.setError("Please Enter your National ID...!");
                national_id.requestFocus();
            } else if (national_id.length() < 14 || national_id.length() > 14) {
                national_id.setError("Please Enter only 14 number...!");
                national_id.requestFocus();
            } else if (Nayional_ID_URi == null) {
                Toast.makeText(this, "please upload your National ID Pic!", Toast.LENGTH_SHORT).show();
            } else if (e_mail.isEmpty()) {
                email_driver.setError("Please Enter your email ...!");
                email_driver.requestFocus();
            } else if (p_ass.isEmpty() || pass_driver.length() < 6) {
                pass_driver.setError("Please Enter your Password...!");
                pass_driver.requestFocus();
            }  else {
                mAuth.createUserWithEmailAndPassword(e_mail,p_ass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){

                                    String devicetoken= FirebaseInstanceId.getInstance().getToken();
                                    String currentuserID=mAuth.getCurrentUser().getUid();

                                    uploadimage(fname, dte, Nat_ID, e_mail, phone_no);
                                    uploadNationalIDImage();
                                    upload_err.setVisibility(View.GONE);

                                    i.putExtra("CUID",CUID);
                                    i.putExtra("FCUID",currentuserID);
                                    i.putExtra("uri", uri);
                                    i.putExtra("fname",fname);
                                    i.putExtra("birthdate",dte);
                                    i.putExtra("NatID",Nat_ID);
                                    i.putExtra("email",e_mail);
                                    i.putExtra("phone_no",phone_no);
                                    i.putExtra("devicetoken",devicetoken);

                                    startActivity(i);
                                    finish();

                                }else{
                                    Toast.makeText(SignUp_Activity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });

        to_login.setOnClickListener(view -> {
            Intent i = new Intent(SignUp_Activity.this,Login_Activity.class);
            startActivity(i);
            finish();
        });
    }


    private void openImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    // uploading driver pic
    private void uploadimage(String fname,String dte,String Nat_ID,String e_mail,String phone_no){
        if (imageuri!=null){
            final StorageReference filereference=UserprofileImage.child(System.currentTimeMillis()+
                    ","+getFileExtension(imageuri));
            uploadtask=filereference.putFile(imageuri);
            uploadtask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Uri downloadUri= (Uri) task.getResult();
                         muri=downloadUri.toString();
                         CUID=mAuth.getCurrentUser().getUid();

                        // uri_viewModel.muri.setValue(muri);
                        signUp_model = new SignUp_Model(CUID, fname, dte, Nat_ID, e_mail, phone_no,muri,Nat_ID_Pic);
                        Users_ref.child("Drivers").child(CUID).setValue(signUp_model);

                    }else {
                        Toast.makeText(SignUp_Activity.this, "Failed!", Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(SignUp_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(SignUp_Activity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadNationalIDImage(){
        if (Nayional_ID_URi!=null){
            final StorageReference filereference=UserNational_ID_Image.child(System.currentTimeMillis()+
                    ","+getFileExtension(Nayional_ID_URi));
            uploadtask=filereference.putFile(Nayional_ID_URi);
            uploadtask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Uri downloadUri= (Uri) task.getResult();
                        String muri=downloadUri.toString();
                        //uri_viewModel.uri.setValue(muri);
                        String devicetoken = FirebaseInstanceId.getInstance().getToken();

                        HashMap<String,Object> map=new HashMap<>();
                        map.put("driverNational_ID",muri);
                        map.put("device_Tokens",devicetoken);
                        map.put("CarModel",".........");
                        map.put("CarNumber",".........");
                        Users_ref.child("Drivers").child(CUID).updateChildren(map);

                        tocheckVerify();

                    }else {
                        Toast.makeText(SignUp_Activity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(SignUp_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(SignUp_Activity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_A_PIC && resultCode == RESULT_OK) {
            Toast.makeText(this, ""+data.getDataString(), Toast.LENGTH_SHORT).show();
            Nayional_ID_URi = data.getData();
            uri = data.getDataString();
            upload_NID.setText("Uploaded");

        }else if (requestCode==IMAGE_REQUEST && resultCode== Activity.RESULT_OK
                && data !=null && data.getData() !=null) {
            imageuri = data.getData();
            Picasso.get().load(imageuri).into(driver_pic);
            upload_err.setVisibility(View.GONE);
            /*if (uploadtask != null && uploadtask.isInProgress()) {
                Toast.makeText(SignUp_Activity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadimage();
            }*/
        }

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date_pic.setText(sdf.format(mycalendar.getTime()));

    }

    private void tocheckVerify(){
        Verify_ref = FirebaseDatabase.getInstance().getReference().child("Admin");
        HashMap<String,Object> m = new HashMap<>();
        m.put("status","verified");
        m.put("id",CUID);
        Verify_ref.child("new_Drivers").child(CUID).setValue(m);
    }
}