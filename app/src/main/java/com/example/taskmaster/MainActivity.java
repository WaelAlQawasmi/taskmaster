package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="RES" ;
    private final View.OnClickListener mClickMeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            // create intent for navigation
            Intent startSecondActivityIntent = new Intent(getApplicationContext(), AddTask.class);
            // start / navigate to the next activity
            startActivity(startSecondActivityIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button AddtaskButton = findViewById(R.id.Addtask);
        Button myTasks = findViewById(R.id.MyTasks);
        Button Task1=findViewById(R.id.taskOne);
        Button Task2=findViewById(R.id.tasktwo);
        Button Task3=findViewById(R.id.taskthree);
        Intent TaskDeatles=new Intent(getApplicationContext(),TaskDetailActivity.class);
        Task1.setOnClickListener(view -> {
            TaskDeatles.putExtra("Titel", Task1.getText());
            startActivity(TaskDeatles);

        });

        Task2.setOnClickListener(view -> {
            TaskDeatles.putExtra("Titel", Task2.getText());
            startActivity(TaskDeatles);

        });

        Task3.setOnClickListener(view -> {
            TaskDeatles.putExtra("Titel", Task3.getText());
            startActivity(TaskDeatles);

        });

        myTasks.setOnClickListener(view -> {
            Intent AllTasksActivityIntent = new Intent(getApplicationContext(), AllTasks.class);

            startActivity(AllTasksActivityIntent);

        });



        AddtaskButton.setOnClickListener(mClickMeButtonListener);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:

                navigateToSettings();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToSettings() {

    Intent settingsIntent = new Intent(this, SettingsActivity.class);
   startActivity(settingsIntent);
    }
    @Override
    protected void onResume() {
        super.onResume();

        setUserName();
    }
    private void setUserName() {
        TextView MYTASKS=findViewById(R.id.Uname);
        // get text out of shared preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // set text on text view address widget
     MYTASKS.setText(sharedPreferences.getString(SettingsActivity.USERNAME, "No USERNAME Set"));
    }

    @Override
    protected void onStart() {
        super.onStart();  //just type on start
    }
}