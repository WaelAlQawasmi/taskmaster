package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Bundle extras = getIntent().getExtras();
     String   Titel= extras.getString("Titel").toUpperCase(Locale.ROOT);
        String   description= extras.getString("description").toUpperCase(Locale.ROOT);
        String   states= extras.getString("states").toUpperCase(Locale.ROOT);

        TextView Title=findViewById(R.id.title);
        Title.setText(Titel);

        TextView disc=findViewById(R.id.description);
        disc.setText(description);

        TextView statesV=findViewById(R.id.states);
        statesV.setText(states);
    }
}