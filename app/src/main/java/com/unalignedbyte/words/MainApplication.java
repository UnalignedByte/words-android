package com.unalignedbyte.words;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.unalignedbyte.words.model.WordsImporter;
import com.unalignedbyte.words.utils.Utils;

import io.fabric.sdk.android.Fabric;

/**
 * Created by CVas on 2/24/2018.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setupAnalytics();
        setupUtils();
    }

    private void setupAnalytics() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
    }

    private void setupUtils()
    {
        Utils.get().setContext(this);
        WordsImporter.get(this).reloadExternalDirectory();
    }
}
