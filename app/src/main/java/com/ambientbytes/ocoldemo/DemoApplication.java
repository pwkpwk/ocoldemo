package com.ambientbytes.ocoldemo;

import android.app.Application;

import com.ambientbytes.ocoldemo.models.MainModel;

/**
 * Custom application object - the composition root of the main model.
 * @Author Pavel Karpenko
 */
public final class DemoApplication extends Application {

    private MainModel mainModel;

    public DemoApplication() {
        mainModel = null;
    }

    public MainModel getMainModel() {
        return mainModel;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //
        // Create the main model and inject all dependencies in it.
        //
        mainModel = new MainModel();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //
        // Tear down the main model.
        //
        mainModel = null;
    }
}
