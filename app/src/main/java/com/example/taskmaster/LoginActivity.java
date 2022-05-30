package com.example.taskmaster;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.example.taskmaster.MainActivity;

public class LoginActivity extends AppCompatActivity {
    public static final String USERNAME = "username";
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        final TextView signUpPrompt = findViewById(R.id.signupIntent);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        configureAmplify();

        signUpPrompt.setOnClickListener(view -> {
            Intent navigateToSignUpIntent = new Intent(this, signupActivity.class);
            startActivity(navigateToSignUpIntent);
        });

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginButton.setEnabled(true);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
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

    private void login(String email, String password) {
        Amplify.Auth.signIn(
                email,
                password,
                result -> {


                    if(result.isSignInComplete() ){
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
                        preferenceEditor.putString(USERNAME, email);
                        Log.i(TAG,"Sign in succeededes" );
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        authSession("onCreate");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));}
                    else{
                        Log.i(TAG,"Sign in error" );
                    }
                },
                error -> Log.e(TAG, error.toString())
        );
    }


    private void authSession(String method) {
        Amplify.Auth.fetchAuthSession(
                result -> Log.i(TAG, "Auth Session => " + method + result.toString()),
                error -> Log.e(TAG, error.toString())
        );
    }
}