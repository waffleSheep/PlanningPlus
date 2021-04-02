package com.example.planningplus;

public class Tag {
    public String tagName;
    public String timeRequired;
    public String deadline;
    public Tag(String tagName){
        this.tagName = tagName;
        this.timeRequired = "";
        this.deadline = "";
    }
    public Tag() {}
}
