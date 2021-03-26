package com.example.planningplus;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AuthenticationViewModel extends ViewModel {
    public MutableLiveData<String> username = new MutableLiveData<>(""),
            password = new MutableLiveData<>(""),
            confirmPassword = new MutableLiveData<>("");
    public MutableLiveData<Double> homeAddressLatitude = new MutableLiveData<>(1.361658542823889),
            homeAddressLongitude = new MutableLiveData<>(103.80224837281581),
            workAddressLatitude = new MutableLiveData<>(1.361658542823889),
            workAddressLongitude = new MutableLiveData<>(103.80224837281581);
    public MutableLiveData<Boolean> isStudent = new MutableLiveData<>(false);
}
