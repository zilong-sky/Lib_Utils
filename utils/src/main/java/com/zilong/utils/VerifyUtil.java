package com.zilong.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 数据 检验 通用工具类
 * Create by zilong on 2018/8/17
 */
public class VerifyUtil {


    //校验密码是否包含数字
    public static boolean checkContainNum(String secret) {
        if (TextUtils.isEmpty(secret)) {
            return false;
        }
        String pwdCheck = "^(.*\\d+.*)$";
        return Pattern.compile(pwdCheck).matcher(secret).matches();
    }

    //校验密码是否包含字母
    public static boolean checkContainLetter(String secret) {
        if (TextUtils.isEmpty(secret)) {
            return false;
        }
        String pwdCheck = "^(.*[a-zA-Z]+.*)$";
        return Pattern.compile(pwdCheck).matcher(secret).matches();
    }

    //校验特殊字符书否合规
    public static boolean checkContainSpecialChar(String secret) {
        if (TextUtils.isEmpty(secret)) {
            return false;
        }
        String pwdCheck = "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9~!@#$%^&*()_+{}|:\"<>?`\\-=\\[\\]\\\\;',./]{6,20})$";
        return true;
    }

    //校验密码 合规
    public static boolean checkSecret(String secret) {
        if (TextUtils.isEmpty(secret)) {
            return false;
        }
        //6~20  必须含数字英文    可以包含特殊字符
        String pwdCheck = "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9~!@#$%^&*()_+{}|:\"<>?`\\-=\\[\\]\\\\;',./]{6,20})$";

        return Pattern.compile(pwdCheck).matcher(secret).matches();
    }


    //校验邮箱 合规
    public static boolean checkEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        String emailCheck = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

        return Pattern.compile(emailCheck).matcher(email).matches();
    }

    //校验邮箱是否包含大写字母
    public static boolean checkEmailContainUpper(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        String newEmail = email.toLowerCase();
        if(!email.equals(newEmail)){
            return true;
        }
        return false;
    }

}
