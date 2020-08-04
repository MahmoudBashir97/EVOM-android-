package com.mahmoud.bashir.evomdriverapp.pojo;

public class RequestStatus {

    String requestStatus;
    String driver_name;

    public RequestStatus(String requestStatus, String driver_name) {
        this.requestStatus = requestStatus;
        this.driver_name = driver_name;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }
}
