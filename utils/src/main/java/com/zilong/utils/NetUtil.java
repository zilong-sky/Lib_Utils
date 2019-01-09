package com.zilong.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * @author zilong
 * @date 2016/4/1 14:54.
 */
public class NetUtil {
    private static Context mContext = LibUtils.getContext();


    /**
     * 判断网络是否连接
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        // TODO: 2018/11/21  
        //需要调用者 获取  ACCESS_NETWORK_STATE权限
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    /**
     * 判断是否是wifi网络
     */
    public static boolean isNetworkWifi() {

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        // TODO: 2018/11/21  
        //需要调用者 获取  ACCESS_NETWORK_STATE权限
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (null != info && info.getType() == ConnectivityManager.TYPE_WIFI)
            return true;
        return false;
    }
}
