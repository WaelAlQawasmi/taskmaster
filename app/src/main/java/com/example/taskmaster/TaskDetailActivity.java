package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.predictions.models.LanguageType;
import com.bumptech.glide.Glide;

public class TaskDetailActivity extends AppCompatActivity {

    private static final String TAG = "";
    private final MediaPlayer mp = new MediaPlayer();
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

        Button transtale= findViewById(R.id.translate);
        Button speach= findViewById(R.id.speech);
        speach.setOnClickListener(view -> {
            Amplify.Predictions.convertTextToSpeech(
                    description,
                    result -> playAudio(result.getAudioData()),
                    error -> Log.e("MyAmplifyApp failed ", "Conversion failed", error)
            );
        });
        transtale.setOnClickListener(view -> {
            Amplify.Predictions.translateText(
                    description,
                    LanguageType.ENGLISH,
                    LanguageType.ARABIC,
                    result -> disc.setText( result.getTranslatedText()),
                    error -> Log.e("MyAmplifyApp", "Translation failed", error)
            );
        });
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


    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e("MyAmplifyApp", "Error writing audio file", error);
        }
    }
}
