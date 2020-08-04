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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amalbit.trail.Route;
import com.amalbit.trail.RouteOverlayView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.Services.Notification_Receiver;
import com.mahmoud.bashir.evomdriverapp.Storage.SharedPrefranceManager;
import com.mahmoud.bashir.evomdriverapp.ViewModel.Driver_Status_ViewModel;
import com.mahmoud.bashir.evomdriverapp.fragments.CompleteMissionFragment;
import com.mahmoud.bashir.evomdriverapp.fragments.Requests_Fragment;
import com.mahmoud.bashir.evomdriverapp.pojo.CustomerHistory_Model;
import com.mahmoud.bashir.evomdriverapp.pojo.DriverHistory_Model;
import com.mahmoud.bashir.evomdriverapp.ui.Login_Activity;
import com.mahmoud.bashir.evomdriverapp.ui.Profile_Activity;
import com.mahmoud.bashir.evomdriverapp.ui.Settings_Activity;
import com.mahmoud.bashir.evomdriverapp.ui.Wallet_Activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class Home_MapsActivity extends AppCompatActivity implements OnMapReadyCallback , NavigationView.OnNavigationItemSelectedListener
        , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {


    private static final String TAG = "Home_MapsActivity";
    
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

    @BindView(R.id.tips_noRequests) RelativeLayout tips_noRequests;

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


    DatabaseReference driver_ref,missionRef,reference,wallet_ref,history_ref;
    FirebaseAuth auth;

    String CUID;


    //for Notification
    // alarm manager term
    AlarmManager manager;
    Intent myintent;
    PendingIntent pendingIntent;

    String st="offline",drivingst="outHold";

    // customer Latlng info;
    Double customer_lat,customer_lng,dest_lat,dest_lng;
    String user_phone , Inuser_name,ph_no;
    int wallet_value=-1;

    LatLng mylatLng;
    Route normalOverlayPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.drawer_main);

        Paper.init(this);


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
        missionRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        wallet_ref = FirebaseDatabase.getInstance().getReference().child("Wallet");
        history_ref = FirebaseDatabase.getInstance().getReference().child("History");

        //viewModel
        driver_status_viewModel = ViewModelProviders.of(this).get(Driver_Status_ViewModel.class);

        //Driver ID on Firebase
        CUID = auth.getCurrentUser().getUid();


        String FCUID = getIntent().getStringExtra("FCUID");


        Switch_orders_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    if (lastLocation != null){
                        if (isNetworkConnected()){
                            driver_status_viewModel.status.setValue("online");
                            txt_status.setText(R.string.online_w);
                            tips_noRequests.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(Home_MapsActivity.this, "please check your internet...!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    if (lastLocation != null){
                        driver_status_viewModel.status.setValue("offline");
                        txt_status.setText(R.string.offline_w);
                        UpdateDriverStatus("offline",drivingst);
                        tips_noRequests.setVisibility(View.VISIBLE);
                        driver_ref.child(CUID).removeValue();
                    }
                }
            }
        });

        floatingActionButton.setOnClickListener(view -> {
            Intent i = new Intent(Home_MapsActivity.this, Requests_Fragment.class);
            if (lastLocation != null){

               i.putExtra("MyLat",lastLocation.getLatitude()+"");
               i.putExtra("MyLng",lastLocation.getLongitude()+"");
               startActivity(i);
            }

        });


        call_to_user.setOnClickListener(view -> {
            if (!ph_no.equals(null)){
            dialContactPhone(ph_no);
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.white_map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            // get current location
            /*if (lastLocation !=null) {
                LatLng mylatLng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(mylatLng).title("موقعي الحالي").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mylatLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            }*/


        }
        driver_status_viewModel.InHoldstatus.setValue("outHold");
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
            Location mloc=LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (mloc != null) {
                LatLng loc = new LatLng(mloc.getLatitude(),mloc.getLongitude());
                mMap.addMarker(new MarkerOptions().position(loc).title("موقعي الحالي").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
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

        mylatLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.clear();


        driver_status_viewModel.getInHoldstatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {


                if (s.equals("outHold")){
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(mylatLng).title("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(mylatLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomBy(15));
                }
            }
        });

        if (lastLocation != null){

            SetNotify();

            if (customer_lat !=null){

                /*RouteOverlayView mRouteOverlayView = new RouteOverlayView(this);
                List<LatLng> mRoute = new ArrayList<>();

                mRoute.add(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()));
                mRoute.add(new LatLng(customer_lat,customer_lng));
                mRoute.add(new LatLng(dest_lat,dest_lng));

                mMap.setOnMapLoadedCallback(() -> {
                    normalOverlayPolyline = new Route.Builder(mRouteOverlayView)
                            .setRouteType(RouteOverlayView.RouteType.PATH)
                            .setCameraPosition(mMap.getCameraPosition())
                            .setProjection(mMap.getProjection())
                            .setLatLngs(mRoute)
                            .setBottomLayerColor(Color.BLUE)
                            .setTopLayerColor(Color.RED)
                            .create();

                    normalOverlayPolyline.getPath();
                });

                mMap.setOnCameraMoveListener(() -> {
                            mRouteOverlayView.onCameraMove(mMap.getProjection(), mMap.getCameraPosition());
                        }
                );*/


                LatLng cust_latLng = new LatLng(customer_lat,customer_lng);

                mMap.addMarker(new MarkerOptions().position(cust_latLng).title("Customer Position").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location)));

                String ds = ""+CalculationByDistance(mylatLng,cust_latLng);

                update_curr_km.setText(ds+" km");
            }
        }
    }

    private void SetNotify(){

        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        myintent = new Intent(Home_MapsActivity.this, Notification_Receiver.class);



        //manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+6000,pendingIntent);

        driver_status_viewModel.getDriverStatus().observe(Home_MapsActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("online")){
                    UpdateDriverStatus("online",drivingst);
                    st=s;
                    //Toast.makeText(Home_MapsActivity.this, ""+s + "  CUID : "+ CUID, Toast.LENGTH_SHORT).show();


                    reference = FirebaseDatabase.getInstance().getReference().child("Requests");

                    reference.child("Drivers").child(CUID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.hasChildren()){

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                    String requestStatus = snapshot.child("requestStatus").getValue().toString();

                                    if (snapshot.child("requestStatus").getValue().toString().equals("Pending")){


                                        double user_lat = Double.parseDouble(snapshot.child("user_lat").getValue().toString());
                                        double user_lng = Double.parseDouble(snapshot.child("user_lng").getValue().toString());

                                        myintent.putExtra("Mylat", lastLocation.getLatitude());
                                        myintent.putExtra("Mylng", lastLocation.getLongitude());
                                        myintent.putExtra("CUID",CUID);
                                        myintent.putExtra("requestStatus",requestStatus);
                                        myintent.putExtra("user_lat",user_lat);
                                        myintent.putExtra("user_lng",user_lng);

                                        pendingIntent = PendingIntent.getBroadcast(Home_MapsActivity.this, 0, myintent, 0);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+6000,pendingIntent);

                }else if (s.equals("offline")){

                    st="offline";
                    manager.cancel(pendingIntent);
                }
            }
        });

    }

    public void getData_Intent(){

        try {

            if (!getIntent().getStringExtra("accept").equals(null) && getIntent().getStringExtra("accept").equals("accept") ){

                driver_status_viewModel.InHoldstatus.setValue("inHold");
                driver_status_viewModel.status.setValue("online");
                txt_status.setText(R.string.online_w);
                tips_noRequests.setVisibility(View.GONE);
                drivingst = "inHold";

                rel_user_info.setVisibility(View.VISIBLE);
                rel_go.setVisibility(View.VISIBLE);
                update_curr_km.setVisibility(View.VISIBLE);

                toolbar.setVisibility(View.GONE);
                txt_status.setVisibility(View.GONE);
                Switch_orders_btn.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.GONE);

                customer_lat = Double.parseDouble(getIntent().getStringExtra("user_lat"));
                customer_lng = Double.parseDouble(getIntent().getStringExtra("user_lng"));
                dest_lat = Double.parseDouble(getIntent().getStringExtra("dest_lat"));
                dest_lng = Double.parseDouble(getIntent().getStringExtra("dest_lng"));
                user_phone =getIntent().getStringExtra("user_phone");
                Inuser_name = getIntent().getStringExtra("user_name");

                user_name.setText(Inuser_name);
                ph_no = user_phone;


                // here you have 3 status of moving 1- start 2-arrived to user location 3- arrived to user destination and complete mission

                go_btn.setOnClickListener(view -> {
                    if (lastLocation != null && customer_lat != null){

                        // draw polyline between driver and user loc , driver and user dest
                        //
                        //
                        //
                        //

                        if (go_btn.getText().equals("GO")){
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("requestStatus","start");
                        missionRef.child("Drivers").child(CUID).child(user_phone).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    missionRef.child("Customers").child(user_phone).child(CUID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                go_btn.setBackgroundResource(R.drawable.arrived_to_user_loc);
                                                go_btn.setText("arrived");
                                                driver_status_viewModel.InHoldstatus.setValue("inHold");
                                                drivingst="inHold";
                                            }
                                            }

                                    });
                                }
                            }
                        });
                    }else if (go_btn.getText().equals("arrived")){
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("requestStatus","arrived");
                            missionRef.child("Drivers").child(CUID).child(user_phone).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        missionRef.child("Customers").child(user_phone).child(CUID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    go_btn.setBackgroundResource(R.drawable.arrived_to_user_dest);
                                                    go_btn.setText("Done");
                                                    driver_status_viewModel.InHoldstatus.setValue("inHold");
                                                }
                                            }

                                        });
                                    }
                                }
                            });
                        } else if (go_btn.getText().equals("Done")) {
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("requestStatus","complete");
                            missionRef.child("Drivers").child(CUID).child(user_phone).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        missionRef.child("Customers").child(user_phone).child(CUID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){


                                                    rel_go.setVisibility(View.GONE);
                                                    go_btn.setVisibility(View.GONE);
                                                    tips_noRequests.setVisibility(View.VISIBLE);
                                                    drivingst = "outHold";
                                                    rel_user_info.setVisibility(View.GONE);
                                                    update_curr_km.setVisibility(View.GONE);
                                                    toolbar.setVisibility(View.VISIBLE);
                                                    txt_status.setVisibility(View.VISIBLE);
                                                    Switch_orders_btn.setVisibility(View.VISIBLE);
                                                    floatingActionButton.setVisibility(View.VISIBLE);

                                                    driver_status_viewModel.InHoldstatus.setValue("outHold");

                                                    // add to history
                                                    DatatoHistory();
                                                    // get wallet value and add price trip to wallet value
                                                    AddWalletValue();

                                                    // also check if there is fee for users
                                                    // Complete fragment Appear
                                                    CompleteMissionFragment missionFragment = new CompleteMissionFragment(user_phone);
                                                    Intent i = new Intent(Home_MapsActivity.this,missionFragment.getClass());
                                                    startActivity(i);



                                                }
                                            }

                                        });
                                    }
                                }
                            });

                        }

                    }

                });


            }else{
                //OUTHOLD
                driver_status_viewModel.InHoldstatus.setValue("outHold");
            }
        }catch (Exception e){
            e.fillInStackTrace();
        }
    }

    private void AddWalletValue() {
        wallet_ref.child("Drivers").child(CUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChildren()){
                        wallet_value = Integer.parseInt(dataSnapshot.child("wallet_value").getValue().toString());

                        if (wallet_value != -1){

                            wallet_value = 50 + wallet_value;

                            HashMap<String,Object> map = new HashMap<>();
                            map.put("wallet_value",wallet_value);
                            wallet_ref.child("Drivers").child(CUID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "onComplete: Value Added Successfully");
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

    private void DatatoHistory() {
        missionRef.child("Drivers").child(CUID).child(user_phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    String id = dataSnapshot.child("id").getValue().toString();
                    String name =dataSnapshot.child("name").getValue().toString();
                    String phone=dataSnapshot.child("phone").getValue().toString();
                    String user_lat=dataSnapshot.child("user_lat").getValue().toString();
                    String user_lng=dataSnapshot.child("user_lng").getValue().toString();
                    String dest_lat=dataSnapshot.child("dest_lat").getValue().toString();
                    String dest_lng=dataSnapshot.child("dest_lng").getValue().toString();
                    String time=dataSnapshot.child("time").getValue().toString();
                    String deviceToken=dataSnapshot.child("deviceToken").getValue().toString();
                    String requestStatus=dataSnapshot.child("requestStatus").getValue().toString();
                    DriverHistory_Model driverHistory_model = new DriverHistory_Model(id,name,phone,user_lat,user_lng,dest_lat,dest_lng,time,deviceToken,requestStatus);

                    missionRef.child("Customers").child(user_phone).child(CUID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {


                                String driverId = dataSnapshot.child("driverId").getValue().toString();
                                String driverName =dataSnapshot.child("driverName").getValue().toString();
                                String driverPhone=dataSnapshot.child("driverPhone").getValue().toString();
                                String driverImage=dataSnapshot.child("driverImage").getValue().toString();
                                String driver_lat=dataSnapshot.child("driver_lat").getValue().toString();
                                String driver_lng=dataSnapshot.child("driver_lng").getValue().toString();
                                String dest_lat=dataSnapshot.child("dest_lat").getValue().toString();
                                String dest_lng=dataSnapshot.child("dest_lng").getValue().toString();
                                String time=dataSnapshot.child("time").getValue().toString();
                                String driverToken=dataSnapshot.child("driverToken").getValue().toString();
                                String requestStatus=dataSnapshot.child("requestStatus").getValue().toString();
                                String driverCarNumber=dataSnapshot.child("driverCarNumber").getValue().toString();

                                CustomerHistory_Model customerHistory_model = new CustomerHistory_Model(driverId,driverName,driverPhone,driverImage,driver_lat,driver_lng,dest_lat,dest_lng
                                ,time,driverToken,requestStatus,driverCarNumber);

                                history_ref.child("Drivers").child(CUID).child(user_phone).setValue(driverHistory_model);
                                history_ref.child("Customers").child(user_phone).child(CUID).setValue(customerHistory_model);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void UpdateDriverStatus(String status,String drivingst){
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",CUID);
        map.put("lat",lastLocation.getLatitude());
        map.put("lng",lastLocation.getLongitude());
        map.put("status",status);
        map.put("driving_st",drivingst);
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
        }else if (id == R.id.setting_nav){
            Intent i = new Intent(Home_MapsActivity.this, Settings_Activity.class);
            startActivity(i);
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
            //manager.cancel(pendingIntent);
        }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        driver_status_viewModel.status.setValue("offline");
        if (lastLocation != null){
            if (st.equals("online")){
               // manager.cancel(pendingIntent);
            }
        }
    }




    public double CalculationByDistance(LatLng StartP, LatLng EndP)
    {
        int Radius = 10000;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        //return Radius * c;
        return meter;
    }

    public void addOverlay(LatLng place){
        GroundOverlay groundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
        .position(place,100)
        .transparency(0.5f)
        .zIndex(3)
        .image(BitmapDescriptorFactory.fromResource(R.drawable.icon_location))
        );
    }
}