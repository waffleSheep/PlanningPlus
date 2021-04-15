package com.example.planningplus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Task implements Comparable<Task>{
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

    @Override
    public int compareTo(Task o) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date thisDate = simpleDateFormat.parse(this.taskDeadline);
            Date otherDate = simpleDateFormat.parse(o.taskDeadline);
            if(thisDate.after(otherDate)){
                return 1;
            }
            else if(thisDate.equals(otherDate)){
                return  0;
            }
            else{
                return -1;
            }
        } catch (ParseException e) {
            return -1;
        }
    }
}
