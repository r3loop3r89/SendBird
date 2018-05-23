package com.shra1.sendbird;

import android.app.Application;

import com.sendbird.android.SendBird;

/**
 * Created by Shrawan WABLE on 3/20/2018.
 */

public class App extends Application {
    public static final String APP_ID = "D70B3C26-312B-41D8-AD0B-B882F698C8E8";

    @Override
    public void onCreate() {
        super.onCreate();
        SendBird.init(APP_ID, this);
    }
}
