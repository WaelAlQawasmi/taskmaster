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

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;

public class AddTask extends AppCompatActivity {
    String[] states = {"new", "assigned", "in progress", "complete"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// CALL CONFIGER CLASS
        configureAmplify();


        setContentView(R.layout.activity_main2);
        Button myTasks = findViewById(R.id.submit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner spin = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, states);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        myTasks.setOnClickListener(view -> {


            Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
            String selected = mySpinner.getSelectedItem().toString();

            EditText title = findViewById(R.id.title);
            String titleName = title.getText().toString();

            EditText body = findViewById(R.id.body);
            String bodyName = body.getText().toString();


            //AWS OBJECT
            Task item = Task.builder()
                    .title(titleName)
                    .description(bodyName)
                    .status(selected)
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
            Amplify.API.mutate(
                    ModelMutation.create(item),
                    success -> Log.i(TAG, "Saved itemApi: " + success.getData().getTitle()),
                    error -> Log.e(TAG, "Could not save item to API", error)
            );


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