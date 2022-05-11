package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG ="RES" ;
    //List<Task> tasksDetales = new ArrayList<>();

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
        List<Task> tasksDetales= TasksDatabase.getInstance(getApplicationContext()).TasksDea().getAll();
        myTasks.setOnClickListener(view -> {
          Log.i("wgu","gooo");
            Intent AllTasksActivityIntent = new Intent(getApplicationContext(), AllTasks.class);

            startActivity(AllTasksActivityIntent);

        });



        AddtaskButton.setOnClickListener(mClickMeButtonListener);



        RecyclerView recyclerView = findViewById(R.id.recycle_view);

        // create an adapter
        ViewAdapter customRecyclerViewAdapter = new ViewAdapter(
                tasksDetales, position -> {

            Intent TaskDeatles=new Intent(getApplicationContext(),TaskDetailActivity.class);
            TaskDeatles.putExtra("Titel", tasksDetales.get(position).getTitle());
            startActivity(TaskDeatles);
            Toast.makeText(
                    MainActivity.this,

                    "The Task  => " + tasksDetales.get(position).getTitle()+" clicked", Toast.LENGTH_SHORT).show();


        });

        // set adapter on recycler view
        recyclerView.setAdapter(customRecyclerViewAdapter);

        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

//    private void initialiseData() {
//        tasksDetales.add(new Task("TASK 1", "GO TO", "COMPLETE"));
//        tasksDetales.add(new Task("TASK2", "GO 8TO", "assigned"));
//        tasksDetales.add(new Task("TASK 3", "GO 2TO", "COMPLETE"));
//
//        tasksDetales.add(new Task("TASK 4", "GO 1TO", "COMPLETE"));
//        tasksDetales.add(new Task("TASK 7", "GO 8TO", "assigned"));
//        tasksDetales.add(new Task("TASK 52", "GO TO", " in progress"));
//    }
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