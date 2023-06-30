package com.drago.craze;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;

public class CrazeApp extends Application {
    String CREATED_TAG = "CREATED_TAG";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(CREATED_TAG, "onCreate: Application created");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
