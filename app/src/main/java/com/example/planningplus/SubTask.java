package com.example.planningplus;

public class SubTask {
    public String subTaskTitle;
    public boolean completed;
    public SubTask(String subTaskTitle){
        this.subTaskTitle = subTaskTitle;
        this.completed = false;
    }
    public SubTask() {}
}
