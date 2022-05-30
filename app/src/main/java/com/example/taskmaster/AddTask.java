package com.example.taskmaster;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.HashMap;

public class AddTask extends AppCompatActivity {
    String[] states = {"new", "assigned", "in progress", "complete"};
    String[] teamsName = {"team1","team2","team3"};
    HashMap<String, String> teams =new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// CALL CONFIGER CLASS
        configureAmplify();


        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    int i=0;
if( response.getData()!=null){
                    for (Team team : response.getData()) {
                        this.teams.put(team.getName(),team.getId());

                        Bundle bundle = new Bundle();
                        Message message = new Message();
                        message.setData(bundle);

                        Log.i(team.getName()+ " NoTask SESS", "Query");
                    }}



                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );





        setContentView(R.layout.activity_main2);
        Button myTasks = findViewById(R.id.submit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        Spinner spinTems = (Spinner) findViewById(R.id.teams);


        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, states);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);


        ArrayAdapter teamAdpter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, teamsName);
        teamAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTems.setAdapter(teamAdpter);


        myTasks.setOnClickListener(view -> {


            Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
            String selected = mySpinner.getSelectedItem().toString();

            Spinner mySpinnerTEAM = (Spinner) findViewById(R.id.teams);
            String selectedTeams = mySpinnerTEAM.getSelectedItem().toString();

            EditText title = findViewById(R.id.title);
            String titleName = title.getText().toString();

            EditText body = findViewById(R.id.body);
            String bodyName = body.getText().toString();


            //AWS OBJECT
            Task item = Task.builder()
                    .title(titleName)
                    .description(bodyName)
                    .status(selected)
           .teamTaskId(teams.get(selectedTeams))
                    .build();

            // Data store save
            Amplify.DataStore.save(item,
                    success -> Log.i(TAG, "Saved item: " + success.item().getTitle()),
                    error -> Log.e(TAG, "Could not save item to DataStore", error)
            );

            // Data store query
            Amplify.DataStore.query(Task.class,
                    tasks -> {
                        while (tasks.hasNext()) {
                            Task task = tasks.next();

                            Log.i(TAG, "==== Task ====");
                            Log.i(TAG, "Name: " + task.getTitle());
                        }
                    },
                    failure -> Log.e(TAG, "Could not query DataStore", failure)
            );

            Amplify.DataStore.observe(Task.class,
                    started -> {
                        Log.i(TAG, "Observation began.");
                    },
                    change -> {
                        Log.i(TAG, change.item().toString());


                    },
                    failure -> Log.e(TAG, "Observation failed.", failure),
                    () -> Log.i(TAG, "Observation complete.")
            );


            // API save to backend
//            Amplify.API.mutate(
//                    ModelMutation.create(item),
//                    success -> Log.i(TAG, "Saved itemApi: " + success.getData().getTitle()),
//                    error -> Log.e(TAG, "Could not save item to API", error)
//            );


            //ROOM
//
//            Task task = new Task(titleName,bodyName,selected);
//            Long newStudentId = TasksDatabase.getInstance(getApplicationContext()).TasksDea().insertStudent(task);
//
            Context context = getApplicationContext();
            CharSequence text = "submit! " + titleName + " " + selected;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        });





    }

    public  void configureAmplify() {
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());

            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e(TAG, "Could not initialize Amplify", e);
        }
    }
}