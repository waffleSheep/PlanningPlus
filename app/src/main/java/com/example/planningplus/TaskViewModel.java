package com.example.planningplus;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class TaskViewModel extends ViewModel {
    // Task Stuff
    public MutableLiveData<String> days = new MutableLiveData<>("0"),
            hours = new MutableLiveData<>("0"),
            taskTitle = new MutableLiveData<>(""),
            taskDescription = new MutableLiveData<>(""),
            taskDeadlineDate = new MutableLiveData<>(""),
            taskDeadlineTime = new MutableLiveData<>("");
    public MutableLiveData<ArrayList<String>> tempTags = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<Boolean>> tempOptions = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<String>> specificSubTasks = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<Boolean> RSLCalculated = new MutableLiveData<>(false);
    public MutableLiveData<String> RSLIntent = new MutableLiveData<>(null);
    public MutableLiveData<String> RSLSubTaskName = new MutableLiveData<>(null);
    // Plan Stuff

    // Assigned Task Stuff
    public MutableLiveData<Boolean> assignedState = new MutableLiveData<>(false);
    public MutableLiveData<ArrayList<String>> userNames = new MutableLiveData<>(new ArrayList<>());

    // Notifications

}