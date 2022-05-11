package com.example.taskmaster;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 1)
public abstract class TasksDatabase extends RoomDatabase {
    public abstract TasksDao TasksDea();

    //1
    private static TasksDatabase TasksDatabase; //declaration for the instance

    //2
    public TasksDatabase() {

    }

    //3
    public static synchronized TasksDatabase getInstance(Context context) {

        if(TasksDatabase == null)
        {
            TasksDatabase = Room.databaseBuilder(context,
                    TasksDatabase.class, "TasksDatabase").allowMainThreadQueries().build();
        }
        return TasksDatabase;
    }
}