package com.example.planningplus;

public class Plan {
    public Task associatedTask;
    public String timeRequired, planTime, planTitle, planDescription;
    public boolean hasProximityAlert;
    public Double latitude, longitude;
    public Plan(String timeRequired, String planTime, String planTitle, String planDescription, boolean hasProximityAlert, Double latitude, Double longitude){
        this.timeRequired = timeRequired;
        this.planTime = planTime;
        this.planTitle = planTitle;
        this.planDescription = planDescription;
        this.hasProximityAlert = hasProximityAlert;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Plan(){}
}
