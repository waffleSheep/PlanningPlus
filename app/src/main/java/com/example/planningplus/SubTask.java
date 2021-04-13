package com.example.planningplus;

import java.util.Random;

public class SubTask {
    public String subTaskTitle;
    public boolean completed;
    public Long id;
    public SubTask(String subTaskTitle){

        this.subTaskTitle = subTaskTitle;
        this.completed = false;

        Random random = new Random();
        this.id = random.nextLong();
    }
    public SubTask() {}
}
