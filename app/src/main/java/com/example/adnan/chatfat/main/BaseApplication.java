package com.example.adnan.chatfat.main;

import android.app.Application;

import com.sendbird.android.SendBird;
import com.example.adnan.chatfat.utils.preferenceUtils;

public class BaseApplication extends Application {

    private static final String APP_ID = "0131A2A6-6451-4306-9823-952706A177B0";

    @Override
    public void onCreate() {
        super.onCreate();

        preferenceUtils.init(getApplicationContext());
        SendBird.init(APP_ID, getApplicationContext());
    }
}
