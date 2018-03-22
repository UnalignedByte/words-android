package com.unalignedbyte.words;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.unalignedbyte.words.model.WordsImporter;

import io.fabric.sdk.android.Fabric;

/**
 * Created by CVas on 2/24/2018.
 */

public class MainApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupAnalytics();
        setupUtils();
        context = getApplicationContext();
    }

    private void setupAnalytics() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
    }

    private void setupUtils()
    {
        WordsImporter.get(this).reloadExternalDirectory();
    }
}
