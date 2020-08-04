package com.mahmoud.bashir.evomdriverapp.adapters;

public interface AcceptRequest_Interface {

    void OnClickAccept(double user_lat,double user_lng,double dest_lat,double dest_lng,String user_phone,String user_name);
    void OnClickreject(double user_lat,double user_lng,double dest_lat,double dest_lng,String user_phone,String user_name);
}
