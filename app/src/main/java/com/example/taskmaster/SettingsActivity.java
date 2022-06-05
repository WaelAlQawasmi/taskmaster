package com.example.taskmaster;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    public static final String USERNAME = "username";
    private EditText mUsernameText;
    Spinner spinTems;
    public static final String MYTEAM = "MYTEAM";
    String[] teamsName = {"team1","team2","team3"};
    HashMap<String, String> teams =new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button submit_btn=findViewById(R.id.btn_submit);

        mUsernameText = findViewById(R.id.username);

        configureAmplify();

        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    int i=0;
if(response.getData()!=null){
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



         spinTems = (Spinner) findViewById(R.id.teams);

        ArrayAdapter teamAdpter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, teamsName);
        teamAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTems.setAdapter(teamAdpter);

        submit_btn.setOnClickListener(view -> {
            saveUserName();

        // Check if no view has focus: https://stackoverflow.com/questions/1109022/how-to-close-hide-the-android-soft-keyboard-programmatically

            View view2 = this.getCurrentFocus();
            if (view2 != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
            }
        });


}
    protected void saveUserName(){

            // get the text from the edit text
            String address = mUsernameText.getText().toString();

            // create shared preference object and set up an editor
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();


        Spinner mySpinnerTEAM = (Spinner) findViewById(R.id.teams);
        String selectedTeams = mySpinnerTEAM.getSelectedItem().toString();

            // save the text to shared preferences
            preferenceEditor.putString(USERNAME, address);
            preferenceEditor.putString(MYTEAM, teams.get(selectedTeams));
            preferenceEditor.apply();

            Toast.makeText(this, "Address Saved"+selectedTeams, Toast.LENGTH_SHORT).show();
        }


    public  void configureAmplify() {
        try {

            Amplify.configure(getApplicationContext());

            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e(TAG, "Could not initialize Amplify", e);
        }
    }
    }
