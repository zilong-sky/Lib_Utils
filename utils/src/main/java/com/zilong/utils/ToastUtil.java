package com.zilong.utils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {
    private static Toast toast;
    public static int layoutId = R.layout.toast_view;

    //只弹一个
    //bug， 看不见的时候，其实没有结束，看起来像是点击没反应，其实等一会真正销毁了，会重新创建显示
    public static void showToast(String text) {
        TextView tv_msg;
        if (toast == null) {
            View view = LayoutInflater.from(LibUtils.getContext()).inflate(layoutId, null);
            tv_msg = view.findViewById(R.id.tv_message_toast);
            //如果为空 则创建
            toast = new Toast(LibUtils.getContext());
            toast.setView(view);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            tv_msg = toast.getView().findViewById(R.id.tv_message_toast);
        }
        tv_msg.setText(text);
        toast.show();
    }


    //每次显示新的
    //bug，如果频繁点击，会出现一直弹框
    public static void showNewToast(String text) {
        //永远创建新的
        toast = new Toast(LibUtils.getContext());
        View view = LayoutInflater.from(LibUtils.getContext()).inflate(layoutId, null);
        TextView tv_msg = view.findViewById(R.id.tv_message_toast);
        tv_msg.setText(text);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}









