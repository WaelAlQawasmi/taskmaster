package com.example.taskmaster;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

    @Dao
    public interface TasksDao {

        @Query("SELECT * FROM task")
        List<Task> getAll();

        @Query("SELECT * FROM task WHERE id = :id")
        Task getStudentByID(Long id);

        @Insert
        Long insertStudent(Task Task);

}
