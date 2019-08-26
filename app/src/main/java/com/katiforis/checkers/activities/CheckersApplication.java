package com.katiforis.checkers.activities;

import android.app.Application;
import android.content.Context;

import io.reactivex.plugins.RxJavaPlugins;

public class CheckersApplication extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        RxJavaPlugins.setErrorHandler(throwable -> {});
    }

    public static Context getAppContext() {
        return appContext;
    }
}