package com.zilong.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.Iterator;
import java.util.Stack;

/**
 * activity堆栈管理类
 */
public class ActivityManager {

    private static Stack<FragmentActivity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {
    }

    /**
     * 单一实例
     */
    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void pushActivity(FragmentActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        if (activity != null) {
            activityStack.add(activity);
        }
    }

    /**
     * 弹出activity
     *
     * @param activity
     */
    public void popActivity(FragmentActivity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 是否包含某个activity
     *
     * @param include
     * @return
     */
    public boolean includeActivity(Class<? extends FragmentActivity> include) {
        boolean bo = false;
        if (include != null) {
            Iterator<FragmentActivity> iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (null != activity && include.equals(activity.getClass())) {
                    bo = true;
                }
            }
        }
        return bo;
    }

    /**
     * 获取某个activity
     *
     * @param cls
     */
    public FragmentActivity getActivity(Class cls) {
        for (FragmentActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }


    /**
     * pop全部，忽略某个activity
     */
    public void popAllActivityBut(Class<? extends FragmentActivity>... ignore) {
        Log.e("ActivityManager", "activitymanager===start=======>" + activityStack.size());
        if (ignore != null) {
            for (Class<? extends FragmentActivity> clazz : ignore) {
                Iterator<FragmentActivity> iterator = activityStack.iterator();
                while (iterator.hasNext()) {
                    Activity activity = iterator.next();
                    if (null != activity && !clazz.equals(activity.getClass())) {
                        iterator.remove();
                    }
                }
            }
        }
        Log.e("ActivityManager", "activitymanager====end======>" + activityStack.size());
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public FragmentActivity currentActivity() {
        FragmentActivity activity = null;
        if (!activityStack.empty()) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        FragmentActivity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            popActivity(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(FragmentActivity activity) {
        if (activity != null) {
            activity.finish();
            popActivity(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (FragmentActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 启动一个activity
     *
     * @param clazz
     */
    public static Intent createActivityIntent(Context context, Class<? extends FragmentActivity> clazz) {
        Intent intent = new Intent(context, clazz);
        if (!(context instanceof FragmentActivity)) {
            //FLAG_ACTIVITY_NEW_TASK  如果栈还存在,就用;如果不存在,就创建新的栈
            //FLAG_ACTIVITY_CLEAR_TOP   如果目标Activity存在,那么销毁目标Activity和它之上的所有Activity，重新创建目标Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        return intent;
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
//            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.
//                    getSystemService(Context.ACTIVITY_SERVICE);
//            activityMgr.restartPackage(context.getPackageName());
//            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } catch (Exception e) {
        }
    }
}
