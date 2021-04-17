package com.example.planningplus;

import android.app.Notification;

public class NotificationPP {
    public String text1, text2;
    public Long time;
    public NotificationPP(String text1, String text2, Long time){
        this.text1 = text1;
        this.text2 = text2;
        this.time = time;
    }

    public NotificationPP(){}
}
