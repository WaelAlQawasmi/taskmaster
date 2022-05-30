package com.example.taskmaster;

import android.content.Intent;
import android.os.Bundle;
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

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class signupActivity extends AppCompatActivity {

    public static final String EMAIL = "email";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button signup_button = findViewById(R.id.signup_button);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        final TextView signup=findViewById(R.id.loginIntent);

        signup.setOnClickListener(view -> {
            Intent NavagatSignUp=new Intent(this,LoginActivity.class);
            startActivity(NavagatSignUp);
        });

        signup_button.setOnClickListener(view -> {
            Intent NavagatSignUp=new Intent(this,LoginActivity.class);
            startActivity(NavagatSignUp);
        });



        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND) {
                    signup_button.setEnabled(true);

                }
                return false;
            }
        });



        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            loadingProgressBar.setVisibility(View.VISIBLE);

            signup(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());

            }
        });
    }

    public void signup(String email,String password){
        AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .userAttribute(AuthUserAttributeKey.nickname(), "wael k q")
                .build();
        Amplify.Auth.signUp(email, password, options,
                result -> {Log.i("AuthQuickStart", "Result: " + result.toString());

                    Intent intent = new Intent(signupActivity.this, VerificationActivity.class);
                    intent.putExtra(EMAIL, email);
                    startActivity(intent);

                    finish();
                },
                error -> Log.e("AuthQuickStart", "Sign up failed", error)
        );
    }

    private void updateUiWithUser( ) {;
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), "welcome", Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}