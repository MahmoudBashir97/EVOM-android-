package com.mahmoud.bashir.evomdriverapp.Storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefranceManager {
    Context context;
    private static final String SHARED_PREF_USER = "Driver_EVOM_app";

    private static SharedPrefranceManager sharedPrefranceManager;

    private SharedPrefranceManager(Context context) {
        this.context = context;
    }

    public synchronized static SharedPrefranceManager getInastance(Context context){
        if (sharedPrefranceManager == null){
            sharedPrefranceManager = new SharedPrefranceManager(context);
        }
        return sharedPrefranceManager;
    }


    public void UserLogin(String driveremail){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        editor.putString("DriverEmail", driveremail);


        editor.putBoolean("userLogged", true);
        editor.apply();
    }

    //--------------- user -------------//
    public void saveUser(String CUID,String fname,String driveremail,String driverPhone ,String Nat_ID,String driverToken,String imageUri) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();

        editor.putString("CUID",CUID);
        editor.putString("FullName",fname);
        editor.putString("DriverEmail", driveremail);
        editor.putString("DriverPhone", driverPhone);
        editor.putString("DriverNID", Nat_ID);
        editor.putString("DriverToken", driverToken);
        editor.putString("ImageUri", imageUri);


        editor.putBoolean("userLogged", true);

        editor.apply();
    }

    public void saveCarInfo(String carModel,String carNumber){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();

        editor.putString("carModel",carModel);
        editor.putString("carNumber", carNumber);


        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("userLogged", false);
    }

    public String getCUID() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString("CUID", "");
    }

    public String getFullName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString("FullName", "");
    }

    public String getDriverEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString("DriverEmail", "");
    }
    public String getDriverPhone() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString("DriverPhone", "");
    }
    public String getDriverNID() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString("DriverNID", "");
    }

    public String getDriverToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString("DriverToken", "");
    }

    public String getimageUri() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString("imageUri", "");
    }


    public String getCarModel(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString("carModel", "");
    }

    public String getCarNumber(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString("carNumber", "");
    }

    public void clearUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("userLogged", false);
        editor.clear();
        editor.apply();
    }
}
