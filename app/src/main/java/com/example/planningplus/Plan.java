package com.example.planningplus;

public class Plan implements Comparable<Plan>{
    public String timeRequired, planTimeDate, planTimeTime, planTitle, planDescription;
    public boolean hasProximityAlert;
    public Double latitude, longitude;
    public Plan(String planTimeDate, String planTimeTime, String planTitle, String planDescription, boolean hasProximityAlert, Double latitude, Double longitude){
        this.planTimeDate = planTimeDate;
        this.planTimeTime = planTimeTime;
        this.planTitle = planTitle;
        this.planDescription = planDescription;
        this.hasProximityAlert = hasProximityAlert;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Plan(){}

    @Override
    public int compareTo(Plan o) {
        return this.planTimeTime.compareTo(o.planTimeTime);
    }
}
