package com.zilong.utils;

import android.content.Context;

public class LibUtils {
    private static Context mContetxt;

    public static void init(Context context) {
        mContetxt = context;
    }

    public static Context getContext() {
        return mContetxt;
    }
}
