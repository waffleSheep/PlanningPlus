package com.example.planningplus;

public class Notification {
    public String question;
    public boolean haveAnswer;
    public String answer;
    public Task relatedTask;
    public Notification(String question, boolean haveAnswer, String answer, Task relatedTask){
        this.question = question;
        this.haveAnswer = haveAnswer;
        this.answer = answer;
        this.relatedTask = relatedTask;
    }
}
