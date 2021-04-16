package com.example.planningplus;

import java.util.ArrayList;

public class User {
    public String username, password;
    public Double homeAddressLatitude, homeAddressLongitude, workAddressLatitude, workAddressLongitude;
    public boolean isStudent;
    public ArrayList<Task> tasks;
    public ArrayList<Plan> plans;
    public ArrayList<Tag> tags;
    public ArrayList<AssignedTask> assignedTasks;
    public ArrayList<NotificationPP> notificationPPS;
    public User(String username,
                String password,
                Double homeAddressLatitude,
                Double homeAddressLongitude,
                Double workAddressLatitude,
                Double workAddressLongitude,
                Boolean isStudent) {
        this.username = username;
        this.password = password;
        this.homeAddressLatitude = homeAddressLatitude;
        this.homeAddressLongitude = homeAddressLongitude;
        this.workAddressLatitude = workAddressLatitude;
        this.workAddressLongitude = workAddressLongitude;
        this.isStudent = isStudent;
        this.tasks = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.plans = new ArrayList<>();
        this.assignedTasks = new ArrayList<>();
        this.notificationPPS = new ArrayList<>();
    }
    public User() {}
}
