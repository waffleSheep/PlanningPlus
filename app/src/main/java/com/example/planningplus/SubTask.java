package com.example.planningplus;

public class SubTask {
    public String timeRequired, subTaskDeadline, subTaskTitle, subTaskDescription;
    public SubTask(String timeRequired, String subTaskDeadline, String subTaskTitle, String subTaskDescription){
        this.timeRequired = timeRequired;
        this.subTaskDeadline = subTaskDeadline;
        this.subTaskTitle = subTaskTitle;
        this.subTaskDescription = subTaskDescription;
    }
    public SubTask() {}
}
