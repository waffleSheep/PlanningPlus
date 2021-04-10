package com.example.planningplus;

public class Tag {
    public String tagName;
    public int days;
    //0 to 24 hours
    public int hour;
    public Tag(String tagName){
        this.tagName = tagName;
        this.days = 0;
        this.hour = 0;
    }
    public Tag() {}
}
