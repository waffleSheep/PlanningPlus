package com.example.planningplus;

import java.util.ArrayList;

public class Task {
    public String timeRequired, taskDeadline, taskTitle, taskDescription;
    public ArrayList<SubTask> subTasks;
    public Task(String timeRequired, String taskDeadline, String taskTitle, String taskDescription){
        this.timeRequired = timeRequired;
        this.taskDeadline = taskDeadline;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        subTasks = new ArrayList<>();
    }
    public Task(){}
}
