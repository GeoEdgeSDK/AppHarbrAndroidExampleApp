package com.appharbr.example.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.appharbr.sdk.configuration.AHSdkConfiguration;
import com.appharbr.sdk.engine.AppHarbr;
import com.appharbr.sdk.engine.InitializationFailureReason;
import com.appharbr.sdk.engine.listeners.OnAppHarbrInitializationCompleteListener;

public class MainApplication extends Application {

    private final String API_KEY = "api_key";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initAppHarbr();
    }

    private void initAppHarbr() {
        AHSdkConfiguration ahSdkConfiguration = new AHSdkConfiguration.Builder(API_KEY).build();
        AppHarbr.initialize(getApplicationContext(),
                ahSdkConfiguration,
                new OnAppHarbrInitializationCompleteListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("LOG", "AppHarbr SDK Initialized Successfully");
                    }

                    @Override
                    public void onFailure(@NonNull InitializationFailureReason reason) {
                        Log.e("LOG", "AppHarbr SDK Initialization Failed: " + reason.getReadableHumanReason());
                    }
                });
    }
}
