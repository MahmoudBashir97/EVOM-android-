package com.mahmoud.bashir.evomdriverapp.pojo;

public class Request_Model {
    String user_id;
    String user_name;
    String user_image;
    String current_lat;
    String current_lng;
    String destination_lat;
    String destination_lng;

    public Request_Model(String user_id, String user_name, String user_image, String current_lat, String current_lng, String destination_lat, String destination_lng) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_image = user_image;
        this.current_lat = current_lat;
        this.current_lng = current_lng;
        this.destination_lat = destination_lat;
        this.destination_lng = destination_lng;
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

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getCurrent_lat() {
        return current_lat;
    }

    public void setCurrent_lat(String current_lat) {
        this.current_lat = current_lat;
    }

    public String getCurrent_lng() {
        return current_lng;
    }

    public void setCurrent_lng(String current_lng) {
        this.current_lng = current_lng;
    }

    public String getDestination_lat() {
        return destination_lat;
    }

    public void setDestination_lat(String destination_lat) {
        this.destination_lat = destination_lat;
    }

    public String getDestination_lng() {
        return destination_lng;
    }

    public void setDestination_lng(String destination_lng) {
        this.destination_lng = destination_lng;
    }
}
