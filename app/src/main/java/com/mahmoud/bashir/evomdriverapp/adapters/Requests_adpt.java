package com.mahmoud.bashir.evomdriverapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.mahmoud.bashir.evomdriverapp.Maps.Home_MapsActivity;
import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.fragments.Requests_Fragment;
import com.mahmoud.bashir.evomdriverapp.pojo.Request_Model;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class Requests_adpt extends RecyclerView.Adapter<Requests_adpt.ViewHolder> {

    Context context;
    double driver_lat;
    double driver_lng;
    List<Request_Model> mlist;

    AcceptRequest_Interface  acceptRequest_interface;

    public Requests_adpt(Context context,List<Request_Model> mlist,double driver_lat , double driver_lng,AcceptRequest_Interface acceptRequest_interface){
        this.context=context;
        this.mlist = mlist;
        this.driver_lat= driver_lat;
        this.driver_lng=driver_lng;
        this.acceptRequest_interface = acceptRequest_interface;
    }

    @NonNull
    @Override
    public Requests_adpt.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_request_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Requests_adpt.ViewHolder holder, int position) {
        Request_Model m = mlist.get(position);

        LatLng latLng_driver = new LatLng(driver_lat,driver_lng);
        LatLng latLng_cust = new LatLng(m.getUser_lat(), m.getUser_lng());
        float d = CalculationByDistance(latLng_driver,latLng_cust);

        holder.name_cust.setText(m.getUser_name());
        holder.cust_to_drv_distance.setText(d+"km");
        holder.location_cust.setText(getAddressName(latLng_cust.latitude,latLng_cust.longitude));
        holder.destination_cust.setText(getAddressName(m.getDestination_lat(),m.getDestination_lng()));

        holder.accept_btn.setOnClickListener(view -> {
            acceptRequest_interface.OnClickAccept(m.getUser_lat(),
                                            m.getUser_lng(),
                                            m.getDestination_lat(),
                                            m.getDestination_lng(),
                                            m.getUser_phone(),
                                            m.getUser_name());
        });

        holder.reject_btn.setOnClickListener(view -> {
            acceptRequest_interface.OnClickreject(m.getUser_lat(),
                    m.getUser_lng(),
                    m.getDestination_lat(),
                    m.getDestination_lng(),
                    m.getUser_phone(),
                    m.getUser_name());
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //ImageView pic_cust;
        TextView name_cust,trip_price,cust_to_drv_distance,location_cust,destination_cust;
        Button accept_btn,reject_btn;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

           // pic_cust = itemView.findViewById(R.id.pic_cust);
            name_cust = itemView.findViewById(R.id.name_cust);
            trip_price = itemView.findViewById(R.id.trip_price);
            cust_to_drv_distance = itemView.findViewById(R.id.cust_to_drv_distance);
            location_cust = itemView.findViewById(R.id.location_cust);
            destination_cust = itemView.findViewById(R.id.destination_cust);
            accept_btn = itemView.findViewById(R.id.accept_btn);
            reject_btn = itemView.findViewById(R.id.reject_btn);

        }
    }

    public int CalculationByDistance(LatLng StartP, LatLng EndP)
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
        return kmInDec;
    }

    String getAddressName (double latitude , double longitude){

        String result="";
        Locale mLocale = new Locale("ar");

        Geocoder geocoder = new Geocoder(context, mLocale);

        // LogCat: Display language = English

        Log.i("Display language = ", "" + mLocale.getDisplayLanguage());

       // Log.i("geocoder geocoder = ", "" + geocoder.toString());

        try {
            List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (null != listAddresses && listAddresses.size() > 0) {
                String _Location = listAddresses.get(0).getAddressLine(0);
                // Log.i("_Location = ", "" + _Location);

                Address address = listAddresses.get(0);

                Log.i("address = ", "" + address);
                result = address.getAdminArea() + "-"+address.getSubAdminArea()+"-"+address.getThoroughfare()+"-"+address.getSubLocality();

                Log.i("result = ", "" + result);
                // Toast.makeText(getApplicationContext(), "Your Location  NAME is -" + result , Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
