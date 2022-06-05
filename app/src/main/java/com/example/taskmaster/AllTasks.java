package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AllTasks extends AppCompatActivity {
    private static final String TAG = "55";
    List<com.amplifyframework.datastore.generated.model.Task> tasksDetales = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    setContentView(R.layout.activity_all_tasks);
//        initialiseData();
//        RecyclerView recyclerView = findViewById(R.id.recycle_view);
//
//        // create an adapter
//        ViewAdapter customRecyclerViewAdapter = new ViewAdapter(
//         tasksDetales, position -> {
//
//            Intent TaskDeatles=new Intent(getApplicationContext(),TaskDetailActivity.class);
//            TaskDeatles.putExtra("Titel", tasksDetales.get(position).getTitle());
//            startActivity(TaskDeatles);
//            Toast.makeText(
//                    AllTasks.this,
//
//                    "The Task  => " + tasksDetales.get(position).getTitle()+" clicked", Toast.LENGTH_SHORT).show();
//
//
//        });

        // set adapter on recycler view
//        recyclerView.setAdapter(customRecyclerViewAdapter);

        // set other important properties
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void initialiseData() {
//        tasksDetales.add(new Task("TASK 1", "GO TO", "COMPLETE"));
//        tasksDetales.add(new Task("TASK2", "GO 8TO", "assigned"));
//        tasksDetales.add(new Task("TASK 3", "GO 2TO", "COMPLETE"));
//
//        tasksDetales.add(new Task("TASK 4", "GO 1TO", "COMPLETE"));
//        tasksDetales.add(new Task("TASK 7", "GO 8TO", "assigned"));
//        tasksDetales.add(new Task("TASK 52", "GO TO", " in progress"));
    }
}