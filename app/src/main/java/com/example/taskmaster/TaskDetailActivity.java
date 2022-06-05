package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Locale;

import com.amplifyframework.core.Amplify;
import com.bumptech.glide.Glide;

public class TaskDetailActivity extends AppCompatActivity {

    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Bundle extras = getIntent().getExtras();
        String Titel = extras.getString("Titel").toUpperCase(Locale.ROOT);
        String location = extras.getString("location").toUpperCase(Locale.ROOT);
        String description = extras.getString("description").toUpperCase(Locale.ROOT);
        String states = extras.getString("states").toUpperCase(Locale.ROOT);
        String imgurl = extras.getString("imgurl");

        TextView Title = findViewById(R.id.title);
        Title.setText(Titel);

        TextView disc = findViewById(R.id.description);
        disc.setText(description);

        TextView statesV = findViewById(R.id.states);
        statesV.setText(states);

        TextView taskLocation = findViewById(R.id.location);
        taskLocation.setText(location);

        ImageView IM = findViewById(R.id.imageView2);
        if (imgurl != null) {

            Amplify.Storage.downloadFile(
                    imgurl,
                    new File(getApplicationContext().getFilesDir() + imgurl),
                    result -> {
                        Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
                        Log.i(TAG, "Successfully downloaded: " + result.getFile().getName());

                        Bitmap bm = BitmapFactory.decodeFile(getApplicationContext().getFilesDir() + imgurl);
                        IM.setImageBitmap(bm);
                    },
                    error -> Log.e(TAG,  "Download Failure", error)
            );

            Glide.with(TaskDetailActivity.this).load( getApplicationContext().getFilesDir()).into(IM);
        }
        else {
            IM.setVisibility(View.INVISIBLE);
        }

    }
}
