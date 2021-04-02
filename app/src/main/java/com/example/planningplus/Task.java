package com.example.planningplus;

import java.util.ArrayList;

public class Task {
    //time required in milliseconds
    //deadline will be in the form dd-mm-yyyy-hh-ss
    public String timeRequired, taskTitle, taskDescription, taskDeadline;
    public ArrayList<SubTask> subTasks;
    public ArrayList<Tag> tags;
    public boolean isAlertNeeded;
    public boolean isProximityAlertUsed;
    public String startTime;
    public double latitude;
    public double longitude;
    public Task(String timeRequired,
                String taskTitle,
                String taskDescription,
                String taskDeadline,
                boolean isAlertNeeded,
                boolean isProximityAlertUsed,
                String startTime,
                double latitude,
                double longitude){
        this.timeRequired = timeRequired;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskDeadline = taskDeadline;
        this.isAlertNeeded = isAlertNeeded;
        this.isProximityAlertUsed = isProximityAlertUsed;
        this.startTime = startTime;
        this.latitude = latitude;
        this.longitude = longitude;
        subTasks = new ArrayList<>();
        tags = new ArrayList<>();
    }
    public Task(){}
}
