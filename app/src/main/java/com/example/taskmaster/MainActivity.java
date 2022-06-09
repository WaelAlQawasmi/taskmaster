package com.example.taskmaster;

import static android.content.ContentValues.TAG;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Note;
import com.amplifyframework.datastore.generated.model.Task;

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
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.ads.rewarded.RewardedAd;
public class MainActivity extends AppCompatActivity {
    private RewardedAd mRewardedAd;
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String TASK_ID = "taskId";
    public static final String DATA = "data";
    private InterstitialAd mInterstitialAd;
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
        // TO SET USERNAME
        setUserName();
        // TO configureAmplify
        configureAmplify();
        //configureAmplify();
        // TO QUERY DATA
        getAndSowData();

        authSession("onCreate");


        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("openMyApp")

                .addProperty("Successful", true)
                .addProperty("ProcessDuration", 792)
                .build();

        Amplify.Analytics.recordEvent(event);

        Button AddtaskButton = findViewById(R.id.Addtask);
        Button myTasks = findViewById(R.id.MyTasks);

        Button InterstitialAd2 = findViewById(R.id.InterstitialAd);
        Button RewardedAd2 = findViewById(R.id.RewardedAd);


        RewardedAd2.setOnClickListener(view -> {
            showReward();
        });

        InterstitialAd2.setOnClickListener(view -> {
            show();
        });
        myTasks.setOnClickListener(view -> {

            Log.i("wgu", "gooo");
            Intent AllTasksActivityIntent = new Intent(getApplicationContext(), AllTasks.class);

            startActivity(AllTasksActivityIntent);

        });


        handler = new Handler(Looper.getMainLooper(), msg -> {
            String data = msg.getData().getString(DATA);
            String taskId = msg.getData().getString(TASK_ID);
            Toast.makeText(this, "The Toast Works => " + data, Toast.LENGTH_SHORT).show();
            Log.i(" NoTask SESS", "Query");
            return true;
        });


//      addteams("team1");
//        addteams("team2");
//       addteams("team3");

        AddtaskButton.setOnClickListener(mClickMeButtonListener);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });


    }


    @Override
    protected void onResume() {
        super.onResume();
        getAndSowData();
        setUserName();
        authSession("onCreate");

    }

    private void setUserName() {
        getAndSowData();

        TextView MYTASKS = findViewById(R.id.Uname);
        // get text out of shared preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // set text on text view address widget
        MYTASKS.setText(sharedPreferences.getString(LoginActivity.USERNAME, "No USERNAME Set"));
        MYTASKS.setText(sharedPreferences.getString(SettingsActivity.USERNAME, "No USERNAME Set"));
    }

    List<Task> taskBD = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();


        getAndSowData();


    }

    private void getAndSowData() {


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        Amplify.API.query(
                ModelQuery.list(Task.class, Task.TEAM_TASK_ID.eq(sharedPreferences.getString(SettingsActivity.MYTEAM, "No USERNAME Set"))),
                response -> {
                    if (response.getData() != null) {
                        taskBD.clear();
                        for (Task task : response.getData()) {
                            taskBD.add(task);
                            Log.i(task.getTitle() + " NoTask SESS", "Query");
                        }
                    }
                    Bundle bundle = new Bundle();


                    Message message = new Message();
                    message.setData(bundle);

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


        //just type on start
        RecyclerView recyclerView = findViewById(R.id.recycle_view);

        // create an adapter
        ViewAdapter customRecyclerViewAdapter = new ViewAdapter(
                taskBD, position -> {

            Intent TaskDeatles = new Intent(getApplicationContext(), TaskDetailActivity.class);
            TaskDeatles.putExtra("Titel", taskBD.get(position).getTitle());
            TaskDeatles.putExtra("description", taskBD.get(position).getDescription());
            TaskDeatles.putExtra("location", taskBD.get(position).getLocation());
            TaskDeatles.putExtra("states", taskBD.get(position).getStatus());

            TaskDeatles.putExtra("imgurl", taskBD.get(position).getImageurl());

            Log.i(TAG, "img url: " + taskBD.get(position).getImageurl());
            startActivity(TaskDeatles);
            Toast.makeText(
                    MainActivity.this,

                    "The Task  => " + taskBD.get(position).getTitle() + " clicked", Toast.LENGTH_SHORT).show();


        });

        // set adapter on recycler view
        recyclerView.setAdapter(customRecyclerViewAdapter);

        // set other important properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


    ///////////////////////MENU//////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getAndSowData();

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        getAndSowData();

        switch (item.getItemId()) {
            case R.id.action_settings:

                navigateToSettings();
                return true;

            case R.id.logout:

                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void navigateToSettings() {
        getAndSowData();

        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }


    ///////////////////////MENU   END//////////////////////


    //    private void initialiseData() {
//        tasksDetales.add(new Task("TASK 1", "GO TO", "COMPLETE"));
//        tasksDetales.add(new Task("TASK2", "GO 8TO", "assigned"));
//        tasksDetales.add(new Task("TASK 3", "GO 2TO", "COMPLETE"));
//
//        tasksDetales.add(new Task("TASK 4", "GO 1TO", "COMPLETE"));
//        tasksDetales.add(new Task("TASK 7", "GO 8TO", "assigned"));
//        tasksDetales.add(new Task("TASK 52", "GO TO", " in progress"));
//    }


    private void addteams(String name) {
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

    private void authSession(String method) {
        Amplify.Auth.fetchAuthSession(
                result -> Log.i(TAG, "Auth Session => " + method + result.toString()),
                error -> Log.e(TAG, error.toString())
        );
    }


    private void logout() {
        Amplify.Auth.signOut(
                () -> {
                    Log.i(TAG, "Signed out successfully");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    authSession("logout");
                    finish();
                },
                error -> Log.e(TAG, error.toString())
        );

    }


    public void configureAmplify() {
        getAndSowData();

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


    public void showReward() {

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });

//        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//            @Override
//            public void onAdShowedFullScreenContent() {
//                // Called when ad is shown.
//                Log.d(TAG, "Ad was shown.");
//            }
//
//            @Override
//            public void onAdFailedToShowFullScreenContent(AdError adError) {
//                // Called when ad fails to show.
//                Log.d(TAG, "Ad failed to show.");
//            }
//
//            @Override
//            public void onAdDismissedFullScreenContent() {
//                // Called when ad is dismissed.
//                // Set the ad reference to null so you don't show the ad a second time.
//                Log.d(TAG, "Ad was dismissed.");
//                mRewardedAd = null;
//            }
//        });

        if (mRewardedAd != null) {

            mRewardedAd.show(this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }




    public  void show() {

        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when fullscreen content is dismissed.
                Log.d("TAG", "The ad was dismissed.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when fullscreen content failed to show.
                Log.d("TAG", "The ad failed to show.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                // Make sure to set your reference to null so you don't
                // show it a second time.
                mInterstitialAd = null;
                Log.d("TAG", "The ad was shown.");
            }
        });

        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }


}


