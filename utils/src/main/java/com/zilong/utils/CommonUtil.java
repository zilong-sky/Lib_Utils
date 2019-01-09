package com.zilong.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 其他无法分类的一些工具方法
 * create by zilong on 2018/7/4  10:51
 */
public class CommonUtil {
    private static Context mContext = LibUtils.getContext();
    private static long lastClickTime = 0;//上次点击时间
    private static long DIFF = 1000;//默认点击间隔1秒
    private static int lastButtonId = -1;//上次点击的viewid


    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(-1, DIFF);
    }

    public static boolean isFastDoubleClick(int buttonId) {
        return isFastDoubleClick(buttonId, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     *
     * @param diff 点击间隔,毫秒
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            return true;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }


    /**
     * 判断一个程序是否显示在前端,根据测试此方法执行效率在11毫秒,无需担心此方法的执行效率
     *
     * @return true---&gt;在前端,false---&gt;不在前端
     */
    public static boolean isApplicationShowing() {
        boolean result = false;
        android.app.ActivityManager am = (android.app.ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
            if (appProcesses != null) {
                for (android.app.ActivityManager.RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
                    if (runningAppProcessInfo.processName.equals(mContext.getPackageName())) {
                        int status = runningAppProcessInfo.importance;
                        if (status == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
                                || status == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                            result = true;
                        }
                    }
                }
            }
        }
        return result;
    }


    /**
     * 判断是否存在虚拟按键
     *
     * @return
     */
    public boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = mContext.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

}
