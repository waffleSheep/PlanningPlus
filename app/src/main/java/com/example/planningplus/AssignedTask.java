package com.example.planningplus;

import java.util.ArrayList;

public class AssignedTask {
    public Task task;
    public ArrayList<String> assignees;
    public ArrayList<Boolean> completed;

    public AssignedTask(Task task, ArrayList<String> assignees){
        this.task = task;
        this.assignees = assignees;
        this.completed = new ArrayList<>();
        for(String i : assignees)
            completed.add(false);
    }

    public AssignedTask() {}
}
