package com.example.planningplus;

public class Plan {
    public String timeRequired, planTime, planTitle, planDescription;
    public boolean hasProximityAlert;
    public Double latitude, longitude;
    public String addressName;
    public Plan(String timeRequired, String planTime, String planTitle, String planDescription, boolean hasProximityAlert, Double latitude, Double longitude, String addressName){
        this.timeRequired = timeRequired;
        this.planTime = planTime;
        this.planTitle = planTitle;
        this.planDescription = planDescription;
        this.hasProximityAlert = hasProximityAlert;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressName = addressName;
    }
    public Plan(){}
}
