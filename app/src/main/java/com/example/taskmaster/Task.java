package com.example.taskmaster;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity

public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "title")
    String title;
    @ColumnInfo(name = "body")
    String body;
    @ColumnInfo(name = "state")
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
