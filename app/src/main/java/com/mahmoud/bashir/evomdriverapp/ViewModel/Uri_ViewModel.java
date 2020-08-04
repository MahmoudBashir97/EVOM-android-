package com.mahmoud.bashir.evomdriverapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Uri_ViewModel extends ViewModel {

    public MutableLiveData<String> uri = new MutableLiveData<>();
    public MutableLiveData<String> muri = new MutableLiveData<>();

    public LiveData<String> getUri (){
        return uri;
    }

    public LiveData<String> getMUri (){
        return muri;
    }
}
