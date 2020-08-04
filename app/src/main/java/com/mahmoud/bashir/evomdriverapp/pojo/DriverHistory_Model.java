package com.mahmoud.bashir.evomdriverapp.pojo;

public class DriverHistory_Model {
    String id;
    String name;
    String phone;
    String user_lat;
    String user_lng;
    String dest_lat;
    String dest_lng;
    String time;
    String deviceToken;
    String requestStatus;

    public DriverHistory_Model(String id, String name, String phone, String user_lat, String user_lng, String dest_lat, String dest_lng, String time, String deviceToken, String requestStatus) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.user_lat = user_lat;
        this.user_lng = user_lng;
        this.dest_lat = dest_lat;
        this.dest_lng = dest_lng;
        this.time = time;
        this.deviceToken = deviceToken;
        this.requestStatus = requestStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_lat() {
        return user_lat;
    }

    public void setUser_lat(String user_lat) {
        this.user_lat = user_lat;
    }

    public String getUser_lng() {
        return user_lng;
    }

    public void setUser_lng(String user_lng) {
        this.user_lng = user_lng;
    }

    public String getDest_lat() {
        return dest_lat;
    }

    public void setDest_lat(String dest_lat) {
        this.dest_lat = dest_lat;
    }

    public String getDest_lng() {
        return dest_lng;
    }

    public void setDest_lng(String dest_lng) {
        this.dest_lng = dest_lng;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
