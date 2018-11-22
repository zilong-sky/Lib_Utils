package com.zilong.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;

    private static String PATH = "";
    private static final String FILE_NAME = "crash";

    //log文件的后缀名
    private static final String FILE_NAME_SUFFIX = ".trace";

    private static CrashHandler sInstance = new CrashHandler();

    //系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private Context mContext;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //构造方法私有，防止外部构造多个实例，即采用单例模式
    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    //这里主要完成初始化工作
    public void init(Context context) {
        PATH = FileUtil.getCrashCacheDir(context).getPath();
        //获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        //获取Context，方便内部使用
        mContext = context.getApplicationContext();
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            //导出异常信息到SD卡中
            dumpExceptionToSDCard(ex);
            //这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug
//            uploadExceptionToServer(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //打印出当前调用栈信息
        ex.printStackTrace();

        //先我们自己处理异常，如果自己不处理,则交给系统去结束我们的程序，
        if (!handleException(ex) && mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            //延迟退出
            try {
                thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //           // 如果需要重启
//            Intent intent = new Intent(mContext.getApplicationContext(),activity);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//                //重启应用，得使用PendingIntent
//            PendingIntent restartIntent = PendingIntent.getActivity(
//                        mContext.getApplicationContext(),
//                    0, intent,
//                        mContext.FLAG_ACTIVITY_NEW_TASK);
//                mAlarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + mRestartTime,
//                        restartIntent); // 重启应用
            ActivityManager.getInstance().AppExit();
        }

    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "程序出现异常，即将退出～", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();

        return true;
    }

    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
        //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Log.w(TAG, "sdcard unmounted,skip dump exception");
                return;
            }
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        //以当前时间创建log文件
        File file = new File(dir, FILE_NAME + time + FILE_NAME_SUFFIX);

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            //导出发生异常的时间
            pw.println(time);

            //导出手机信息
            dumpPhoneInfo(pw);

            pw.println();
            //导出异常的调用栈信息
            ex.printStackTrace(pw);

            pw.close();
        } catch (Exception e) {
            Log.e(TAG, "dump crash info failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        //应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        //android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        //cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);

        pw.print("版本: ");
        pw.println("2016/08/30 18:16 提交代码");
    }

    //上传错误信息
    private void uploadExceptionToServer(Context context, Throwable ex) throws PackageManager.NameNotFoundException {
        TreeMap<String, String> map = new TreeMap<>();
//        if (SPUtil.getInstance().isLogin()) {
//            map.put("userUuid", SPUtil.getInstance().getUserUUID());
//        } else {
//        }
        map.put("app", "Android");
        map.put("mobileModel", Build.MODEL);
        map.put("mobileSystemVersion", Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT);
        if (NetUtil.isNetworkWifi(context)) {
            map.put("networkEnvironment", "WIFI");
        } else {
            map.put("networkEnvironment", "4G");
        }
        map.put("collapseDate", simpleDateFormat.format(new Date()));

        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while (cause != null) {
            pw.println();
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        String error = writer.toString();
        int errorLenth = error.length();
        if (errorLenth > 5000) {//暂时大小限定在5000字节
            error = error.substring(0, 5000);
        }
        map.put("collapseContent", error);

        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        map.put("appVersion", pi.versionName + "_" + pi.versionCode);

    }

}