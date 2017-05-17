package com.mivas.myharmonicasongs;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

public class MHSApplication extends Application {

    private static MHSApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        // save the instance
        instance = this;

        // initialize Active Android and database
        ActiveAndroid.initialize(this);
    }

    /**
     * Returns the instance of the Application.
     *
     * @return The instance of the Application
     */
    public static MHSApplication getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        // dispose of Active Android
        ActiveAndroid.dispose();
    }
}
