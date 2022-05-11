package com.example.taskmaster;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

    @Dao
    public interface TasksDao {

        @Query("SELECT * FROM Task")
        List<Task> getAll();

        @Query("SELECT * FROM Task WHERE id = :id")
        Task getStudentByID(Long id);

        @Insert
        Long insertStudent(Task Task);

}
