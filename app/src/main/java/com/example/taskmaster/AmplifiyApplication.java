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


    }

    private static final String TAG = AmplifiyApplication.class.getSimpleName();


}

