package com.example.taskmaster;
public class Task {
    String title;
    String body;
    String state;

    public String getBody() {
        return body;
    }
    public String getTitle() {
        return title;
    }


    public String getState() {
        return state;
    }



    public Task(String title, String body, String state) {
    this.title = title;
    this.body = body;
    this.state = state;
  }





}
