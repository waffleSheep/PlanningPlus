package com.example.planningplus;

import java.util.ArrayList;
import java.util.Random;

public class Task {
    //time required in milliseconds
    //deadline will be in the form dd-mm-yyyy-hh-ss
    public String timeRequired, taskTitle, taskDescription, taskDeadline;
    public ArrayList<SubTask> subTasks;
    public ArrayList<Tag> tags;

    public boolean isAssigned;
    public String assignedBy;
    public Long id;

    public Task(String timeRequired,
                String taskTitle,
                String taskDescription,
                String taskDeadline,
                boolean isAssigned,
                String assignedBy){
        this.timeRequired = timeRequired;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskDeadline = taskDeadline;
        this.isAssigned = isAssigned;
        this.assignedBy = assignedBy;
        subTasks = new ArrayList<>();
        tags = new ArrayList<>();
        Random r = new Random();
        id = r.nextLong();
    }
    public Task(){}
}
