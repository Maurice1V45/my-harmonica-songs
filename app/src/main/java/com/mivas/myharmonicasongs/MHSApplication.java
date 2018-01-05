package com.mivas.myharmonicasongs;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mivas.myharmonicasongs.util.AnalyticUtils;

/**
 * Application class.
 */
public class MHSApplication extends Application {

    private static MHSApplication instance;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

        // save the instance
        instance = this;

        // initialize Active Android and database
        ActiveAndroid.initialize(this);

        // initialize Firebase
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        AnalyticUtils.setUserProperties();
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

    public boolean isTablet() {
        return getResources().getBoolean(R.bool.is_tablet);
    }

    public FirebaseAnalytics getFirebaseAnalytics() {
        return firebaseAnalytics;
    }
}
