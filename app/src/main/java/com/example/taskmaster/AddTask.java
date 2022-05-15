package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddTask extends AppCompatActivity {
    String[] states = { "new","assigned", "in progress", "complete"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button myTasks = findViewById(R.id.submit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner spin = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, states);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        myTasks.setOnClickListener(view -> {


            Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
            String selected = mySpinner.getSelectedItem().toString();

            EditText title = findViewById(R.id.title);
            String titleName = title.getText().toString();

            EditText body = findViewById(R.id.body);
            String bodyName  =body.getText().toString();

            Task task = new Task(titleName,bodyName,selected);
            Long newStudentId = TasksDatabase.getInstance(getApplicationContext()).TasksDea().insertStudent(task);

            Context context = getApplicationContext();
            CharSequence text = "submit!"+newStudentId+selected;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        });
    }
}