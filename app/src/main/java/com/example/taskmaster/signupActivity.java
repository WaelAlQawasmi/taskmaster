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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup();
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

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                usernameEditText.getText().toString();
                passwordEditText.getText().toString();

            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    usernameEditText.getText().toString();
                    passwordEditText.getText().toString();

                }
                return false;
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEditText.getText().toString();

            }
        });
    }

    public void signup(){
        AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), "wael@o.com")
                .build();
        Amplify.Auth.signUp("wael@o.com", "Password123", options,
                result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
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