package com.example.planningplus;

public class Plan {
    public String timeRequired, planTime, planTitle, planDescription;
    public Plan(String timeRequired, String planTime, String planTitle, String planDescription){
        this.timeRequired = timeRequired;
        this.planTime = planTime;
        this.planTitle = planTitle;
        this.planDescription = planDescription;
    }
    public Plan(){}
}
