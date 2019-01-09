package com.zilong.utils;

import android.content.Context;

public class LibUtils {
    private static Context mContet;

    public static void init(Context context) {
        mContet = context;
    }

    public static Context getContext() {
        return mContet;
    }
}
