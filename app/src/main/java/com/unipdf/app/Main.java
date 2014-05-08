package com.unipdf.app;

import android.app.Application;

/**
 * Created by schotte on 17.04.14.
 */
public class Main extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        mAppContext = this;
    }

    public static Application getAppContext() {
        return mAppContext;
    }

    private static Application mAppContext;
}
