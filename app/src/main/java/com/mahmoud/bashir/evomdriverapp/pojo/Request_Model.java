package com.mahmoud.bashir.evomdriverapp.pojo;

public class Request_Model {
    String user_id;
    String user_name;
    String user_phone;
    String deviceToken;
    double user_lat;
    double user_lng;
    double destination_lat;
    double destination_lng;
    String time;

    public Request_Model(String user_id, String user_name, String user_phone, String deviceToken, double user_lat, double user_lng, double destination_lat, double destination_lng, String time) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.deviceToken = deviceToken;
        this.user_lat = user_lat;
        this.user_lng = user_lng;
        this.destination_lat = destination_lat;
        this.destination_lng = destination_lng;
        this.time = time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public double getUser_lat() {
        return user_lat;
    }

    public void setUser_lat(double user_lat) {
        this.user_lat = user_lat;
    }

    public double getUser_lng() {
        return user_lng;
    }

    public void setUser_lng(double user_lng) {
        this.user_lng = user_lng;
    }

    public double getDestination_lat() {
        return destination_lat;
    }

    public void setDestination_lat(double destination_lat) {
        this.destination_lat = destination_lat;
    }

    public double getDestination_lng() {
        return destination_lng;
    }

    public void setDestination_lng(double destination_lng) {
        this.destination_lng = destination_lng;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
