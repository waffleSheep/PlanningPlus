package com.example.planningplus;

public class Tag {
    public String tagName;
    public double days;
    //0 to 24 hours
    public double hour;
    public int dataCounted;
    public Tag(String tagName){
        this.tagName = tagName;
        this.days = 0;
        this.hour = 0;
        this.dataCounted = 0;
    }
    public Tag() {}
}
