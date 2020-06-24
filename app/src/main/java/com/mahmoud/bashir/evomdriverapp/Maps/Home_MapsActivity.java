package com.mahmoud.bashir.evomdriverapp.Maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.Services.Notification_Receiver;
import com.mahmoud.bashir.evomdriverapp.Storage.SharedPrefranceManager;
import com.mahmoud.bashir.evomdriverapp.ViewModel.Driver_Status_ViewModel;
import com.mahmoud.bashir.evomdriverapp.fragments.Requests_Fragment;
import com.mahmoud.bashir.evomdriverapp.ui.Login_Activity;
import com.mahmoud.bashir.evomdriverapp.ui.Profile_Activity;
import com.mahmoud.bashir.evomdriverapp.ui.Wallet_Activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home_MapsActivity extends AppCompatActivity implements OnMapReadyCallback , NavigationView.OnNavigationItemSelectedListener
        , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap , DMap;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.Switch_orders_btn) Switch Switch_orders_btn;
    @BindView(R.id.txt_status) TextView txt_status;
    @BindView(R.id.go_btn) TextView go_btn;
    @BindView(R.id.floatingActionButton) FloatingActionButton floatingActionButton;

    @BindView(R.id.rel_go) RelativeLayout rel_go;
    @BindView(R.id.rel_user_info) RelativeLayout rel_user_info;
    @BindView(R.id.user_img) ImageView user_img;
    @BindView(R.id.call_to_user) ImageView call_to_user;
    @BindView(R.id.user_name) TextView user_name;
    @BindView(R.id.update_curr_km) TextView update_curr_km;


    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };


    //view Model
    Driver_Status_ViewModel driver_status_viewModel;


    DatabaseReference driver_ref;
    FirebaseAuth auth;

    String CUID;


    //for Notification
    // alarm manager term
    AlarmManager manager;
    Intent myintent;
    PendingIntent pendingIntent;

    String st="offline";

    // customer Latlng info;
    Double customer_lat,customer_lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        ButterKnife.bind(this);

        checkPermissions();

        // init views
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setContentInsetsAbsolute(toolbar.getContentInsetLeft(), 200);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // custom drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //init firebase
        //driver reference
        auth = FirebaseAuth.getInstance();
        driver_ref = FirebaseDatabase.getInstance().getReference().child("Nearby_drivers");

        //viewModel
        driver_status_viewModel = ViewModelProviders.of(this).get(Driver_Status_ViewModel.class);

        //Driver ID on Firebase
        CUID = auth.getCurrentUser().getUid();


        String FCUID = getIntent().getStringExtra("FCUID");
        Toast.makeText(this, "CUID : "+auth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "fcuid : "+FCUID, Toast.LENGTH_LONG).show();


        Switch_orders_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    if (lastLocation != null){
                        if (isNetworkConnected()){
                            driver_status_viewModel.status.setValue("online");
                            txt_status.setText("online");
                        }else{
                            Toast.makeText(Home_MapsActivity.this, "please check your internet...!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    if (lastLocation != null){
                        driver_status_viewModel.status.setValue("offline");
                        txt_status.setText("offline");
                        UpdateDriverStatus("offline");
                        driver_ref.child(CUID).removeValue();
                    }
                }
            }
        });

        floatingActionButton.setOnClickListener(view -> {
            Intent i = new Intent(Home_MapsActivity.this, Requests_Fragment.class);
            if (lastLocation != null){
            startActivity(i);
            }

        });

        go_btn.setOnClickListener(view -> {
            Toast.makeText(this, "go button !!", Toast.LENGTH_SHORT).show();
        });

        call_to_user.setOnClickListener(view -> {
            dialContactPhone("+201096589671");
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            // get current location
             mMap.setMyLocationEnabled(true);
             mMap.getUiSettings().setMapToolbarEnabled(false);
        }
        if (lastLocation !=null){
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }

        getData_Intent();
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // بيعمل تحديث للوكيشن كل كام ثانية
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000); // 5 second
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY); // بتخليه يقرا اللوكيشن اسرع

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        lastLocation = location;

        LatLng mylatLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(mylatLng).title("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylatLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomBy(13));
        if (lastLocation != null){
            SetNotify();
            if (customer_lat !=null){
                LatLng cust_latLng = new LatLng(customer_lat,customer_lng);
                mMap.addMarker(new MarkerOptions().position(cust_latLng).title("Customer Position").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));

                update_curr_km.setText("2345.556 km");
            }
        }
    }

    private void SetNotify(){

        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        myintent = new Intent(Home_MapsActivity.this, Notification_Receiver.class);

        myintent.putExtra("Mylat", lastLocation.getLatitude());
        myintent.putExtra("Mylng", lastLocation.getLongitude());

        pendingIntent = PendingIntent.getBroadcast(Home_MapsActivity.this, 0, myintent, 0);

        //manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+6000,pendingIntent);

        driver_status_viewModel.getDriverStatus().observe(Home_MapsActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("online")){
                    UpdateDriverStatus("online");
                    manager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 6000, 6000, pendingIntent);
                    st=s;
                }else if (s.equals("offline")){
                    manager.cancel(pendingIntent);
                    st="offline";
                }
            }
        });

    }

    public void getData_Intent(){

        try {

            if (!getIntent().getStringExtra("accept").equals(null) && getIntent().getStringExtra("accept").equals("accept") ){

                rel_user_info.setVisibility(View.VISIBLE);
                rel_go.setVisibility(View.VISIBLE);
                update_curr_km.setVisibility(View.VISIBLE);

                toolbar.setVisibility(View.GONE);
                txt_status.setVisibility(View.GONE);
                Switch_orders_btn.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.GONE);

                customer_lat = Double.parseDouble(getIntent().getStringExtra("cust_lat"));
                customer_lng = Double.parseDouble(getIntent().getStringExtra("cust_lng"));


            }
        }catch (Exception e){
            e.fillInStackTrace();
        Toast.makeText(this, "some Errors!!!!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void UpdateDriverStatus(String status){
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",CUID);
        map.put("lat",lastLocation.getLatitude());
        map.put("lng",lastLocation.getLongitude());
        map.put("status",status);
        driver_ref.child(CUID).updateChildren(map);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile_nav){
            Intent i = new Intent(Home_MapsActivity.this, Profile_Activity.class);
            startActivity(i);
        }else if (id == R.id.wallet_nav){
            Intent i = new Intent(Home_MapsActivity.this, Wallet_Activity.class);
            startActivity(i);
        } else if (id == R.id.logout_nav) {
            Intent i = new Intent(Home_MapsActivity.this, Login_Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            SharedPrefranceManager.getInastance(this).clearUser();
            if (lastLocation != null){
                manager.cancel(pendingIntent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Checks the dynamically-controlled permissions and requests missing permissions from end user.
     */
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                break;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    @Override
    protected void onStop() {
        super.onStop();
        driver_status_viewModel.status.setValue("offline");
        if (lastLocation != null){
            if (st.equals("online")){
            manager.cancel(pendingIntent);
        }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        driver_status_viewModel.status.setValue("offline");
        if (lastLocation != null){
            if (st.equals("online")){
                manager.cancel(pendingIntent);
            }
        }
    }
}