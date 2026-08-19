package com.teknobirader.philipshome;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class LauncherActivity extends Activity {
    private LauncherView launcherView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        launcherView = new LauncherView(this);
        setContentView(launcherView);
    }

    @Override protected void onResume() {
        super.onResume();
        if (launcherView != null) launcherView.refreshApps();
    }
}
