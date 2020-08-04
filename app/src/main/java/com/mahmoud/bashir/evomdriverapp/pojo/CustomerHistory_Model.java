package com.mahmoud.bashir.evomdriverapp.pojo;

public class CustomerHistory_Model {

    String driverId;
    String driverName;
    String driverPhone;
    String driverImage;
    String driver_lat;
    String driver_lng;
    String dest_lat;
    String dest_lng;
    String time;
    String driverToken;
    String requestStatus;
    String driverCarNumber;


    public CustomerHistory_Model(String driverId, String driverName, String driverPhone, String driverImage, String driver_lat, String driver_lng, String dest_lat, String dest_lng, String time, String driverToken, String requestStatus, String driverCarNumber) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.driverImage = driverImage;
        this.driver_lat = driver_lat;
        this.driver_lng = driver_lng;
        this.dest_lat = dest_lat;
        this.dest_lng = dest_lng;
        this.time = time;
        this.driverToken = driverToken;
        this.requestStatus = requestStatus;
        this.driverCarNumber = driverCarNumber;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getDriver_lat() {
        return driver_lat;
    }

    public void setDriver_lat(String driver_lat) {
        this.driver_lat = driver_lat;
    }

    public String getDriver_lng() {
        return driver_lng;
    }

    public void setDriver_lng(String driver_lng) {
        this.driver_lng = driver_lng;
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

    public String getDriverToken() {
        return driverToken;
    }

    public void setDriverToken(String driverToken) {
        this.driverToken = driverToken;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getDriverCarNumber() {
        return driverCarNumber;
    }

    public void setDriverCarNumber(String driverCarNumber) {
        this.driverCarNumber = driverCarNumber;
    }
}
