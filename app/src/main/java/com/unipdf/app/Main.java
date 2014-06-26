package com.unipdf.app;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;

import com.unipdf.app.services.FileCrawlerService;
import com.unipdf.app.services.FileReceiver;

import java.io.File;

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
