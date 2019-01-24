package com.zilong.utils;

import android.content.Context;

/**
 * 需要再 主app的application中 进行初始化
 * 目前的主要功能只是  获取context
 */
public class LibUtils {
    private static Context mContet;

    public static void init(Context context) {
        mContet = context;
    }

    public static Context getContext() {
        return mContet;
    }
}
