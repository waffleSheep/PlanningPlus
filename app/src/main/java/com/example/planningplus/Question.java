package com.example.planningplus;

public class Question {
    public String question;
    public boolean haveAnswer;
    public String answer;
    public Task relatedTask;
    public Question(String question, boolean haveAnswer, String answer, Task relatedTask){
        this.question = question;
        this.haveAnswer = haveAnswer;
        this.answer = answer;
        this.relatedTask = relatedTask;
    }
}
