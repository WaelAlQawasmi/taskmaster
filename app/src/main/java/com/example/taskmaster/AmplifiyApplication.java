package com.example.taskmaster;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

public class AmplifiyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        configureAmplify();

    }

    private static final String TAG = AmplifiyApplication.class.getSimpleName();

    private void configureAmplify() {
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());

            // setUserName();
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());

            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e(TAG, "Could not initialize Amplify", e);
        }
    }
}

