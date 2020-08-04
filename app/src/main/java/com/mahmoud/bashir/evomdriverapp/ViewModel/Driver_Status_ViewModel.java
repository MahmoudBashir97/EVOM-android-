package com.mahmoud.bashir.evomdriverapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Driver_Status_ViewModel extends ViewModel {

    public MutableLiveData<String> status = new MutableLiveData<>();
    public MutableLiveData<String> InHoldstatus = new MutableLiveData<>();

    public LiveData<String> getDriverStatus(){
        return status;
    }
    public LiveData<String> getInHoldstatus(){
        return InHoldstatus;
    }
}
