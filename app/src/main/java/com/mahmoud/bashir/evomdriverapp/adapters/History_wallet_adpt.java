package com.mahmoud.bashir.evomdriverapp.adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.pojo.DriverHistory_Model;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class History_wallet_adpt extends RecyclerView.Adapter<History_wallet_adpt.ViewHolder> {

    Context context;
    List<DriverHistory_Model> mlist;

    public History_wallet_adpt(Context context, List<DriverHistory_Model> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_single_item_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DriverHistory_Model m = mlist.get(position);
        holder.user_name_done.setText(m.getName());
        String sp[] = m.getTime().split(" ");
        String date = sp[0] ;
        holder.date_trip_done.setText(date);

        holder.destination_done.setText(getAddressName(Double.parseDouble(m.getDest_lat()),Double.parseDouble(m.getDest_lng())));

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView user_name_done,date_trip_done,destination_done,price_done;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name_done = itemView.findViewById(R.id.user_name_done);
            date_trip_done = itemView.findViewById(R.id.date_trip_done);
            destination_done = itemView.findViewById(R.id.destination_done);
            price_done = itemView.findViewById(R.id.price_done);


        }
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
                result = address.getAdminArea() + "-"+address.getSubAdminArea();

                Log.i("result = ", "" + result);
                // Toast.makeText(getApplicationContext(), "Your Location  NAME is -" + result , Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
