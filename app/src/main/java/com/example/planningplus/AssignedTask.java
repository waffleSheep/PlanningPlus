package com.example.planningplus;

import java.util.ArrayList;

public class AssignedTask {
    public Task task;
    public ArrayList<String> assignees;

    public AssignedTask(Task task, ArrayList<String> assignees){
        this.task = task;
        this.assignees = assignees;
    }

    public AssignedTask() {}
}
