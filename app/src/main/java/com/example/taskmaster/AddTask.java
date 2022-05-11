package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button myTasks = findViewById(R.id.submit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myTasks.setOnClickListener(view -> {

            EditText title = findViewById(R.id.title);
            String titleName = title.getText().toString();

            EditText body = findViewById(R.id.body);
            String bodyName  =body.getText().toString();

            Task task = new Task(titleName,bodyName,"asign");
            Long newStudentId = TasksDatabase.getInstance(getApplicationContext()).TasksDea().insertStudent(task);

            Context context = getApplicationContext();
            CharSequence text = "submit!"+newStudentId;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        });
    }
}