package com.zilong.lib_utils;

import android.app.Application;

import com.zilong.utils.LibUtils;

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LibUtils.init(this);
    }
}
