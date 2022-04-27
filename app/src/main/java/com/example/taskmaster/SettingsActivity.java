package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    public static final String USERNAME = "username";
    private EditText mUsernameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button submit_btn=findViewById(R.id.btn_submit);
        mUsernameText = findViewById(R.id.username);;
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

            // save the text to shared preferences
            preferenceEditor.putString(USERNAME, address);
            preferenceEditor.apply();

            Toast.makeText(this, "Address Saved", Toast.LENGTH_SHORT).show();
        }
    }
