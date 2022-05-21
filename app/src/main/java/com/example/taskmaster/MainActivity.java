package com.example.taskmaster;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {


    //private static final String TAG ="RES" ;
    //List<Task> tasksDetales = new ArrayList<>();
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String TASK_ID = "taskId";
    public static final String DATA = "data";

    private Handler handler;
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


        getAndSowData();
        Button AddtaskButton = findViewById(R.id.Addtask);
        Button myTasks = findViewById(R.id.MyTasks);

        myTasks.setOnClickListener(view -> {
            Log.i("wgu","gooo");
            Intent AllTasksActivityIntent = new Intent(getApplicationContext(), AllTasks.class);

            startActivity(AllTasksActivityIntent);

        });



        handler = new Handler(Looper.getMainLooper(), msg -> {
            String data = msg.getData().getString(DATA);
            String taskId = msg.getData().getString(TASK_ID);
          Toast.makeText(this, "The Toast Works => " + data, Toast.LENGTH_SHORT).show();
            Log.i( " NoTask SESS", "Query");
            return true;
        });
//        addteams("team1");
//        addteams("team2");
//        addteams("team3");

        AddtaskButton.setOnClickListener(mClickMeButtonListener);



    }

    private void addteams(String name){
         Team team = Team.builder()
                .name(name)
                .build();

        // Data store save
        Amplify.DataStore.save(team,
                successTeam -> {
                    Log.i(TAG, "Saved task: " + successTeam.item().getName());

                },
                error -> Log.e(TAG, "Could not save task to DataStore", error)
        );

//        Amplify.API.mutate(
//                ModelMutation.create(team),
//                success -> Log.i(TAG, "Saved itemApi: " + success.getData().getName()),
//                error -> Log.e(TAG, "Could not save item to API", error)
//        );
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
    List<Task> taskBD= new ArrayList<>();
    @Override
    protected void onStart() {
        super.onStart();

// CALL CONFIGER CLASS
        configureAmplify();

        getAndSowData();


        //just type on start
        RecyclerView recyclerView = findViewById(R.id.recycle_view);

        // create an adapter
        ViewAdapter customRecyclerViewAdapter = new ViewAdapter(
                taskBD, position -> {

            Intent TaskDeatles=new Intent(getApplicationContext(),TaskDetailActivity.class);
            TaskDeatles.putExtra("Titel", taskBD.get(position).getTitle());
            TaskDeatles.putExtra("description", taskBD.get(position).getDescription());
            TaskDeatles.putExtra("states", taskBD.get(position).getStatus());
            startActivity(TaskDeatles);
            Toast.makeText(
                    MainActivity.this,

                    "The Task  => " + taskBD.get(position).getTitle()+" clicked", Toast.LENGTH_SHORT).show();


        });

        // set adapter on recycler view
        recyclerView.setAdapter(customRecyclerViewAdapter);

        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void getAndSowData() {

        configureAmplify();





        Amplify.API.query(
                ModelQuery.list(Task.class),
                response -> {
                    for (Task task : response.getData()) {
                        taskBD.add(task);
                        Log.i(task.getTitle()+ " NoTask SESS", "Query");
                    }
                    Bundle bundle = new Bundle();


                    Message message = new Message();
                    message.setData(bundle);
;
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );



        Amplify.DataStore.observe(Task.class,
                started -> {
                    Log.i(TAG, "Observation began.");
                },
                change -> {
                    Log.i(TAG, change.item().toString());

                    Bundle bundle = new Bundle();
                    bundle.putString(DATA, change.item().toString());

                    Message message = new Message();
                    message.setData(bundle);

//                    handler.sendMessage(message);
                },
                failure -> Log.e(TAG, "Observation failed.", failure),
                () -> Log.i(TAG, "Observation complete.")
        );


    }


    private void configureAmplify() {
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());

            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e(TAG, "Could not initialize Amplify", e);
        }
    }
}