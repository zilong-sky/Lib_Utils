package com.zilong.utils;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    private final static int pswd = 6;
    private static Calendar mCalendar = Calendar.getInstance();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static DecimalFormat moneyFormat = new DecimalFormat(",##0.00");//金额格式化


    /**
     * 格式化金额  1,234.00
     *
     * @param money
     * @return
     */
    public static String formatMoney(Object money) {
        if (null == money) {
            return null;
        }
        if (money instanceof Double) {
            return moneyFormat.format((Double) money);
        } else if (money instanceof Long) {
            return moneyFormat.format((Long) money);
        } else if (money instanceof Integer || money instanceof Float) {
            return moneyFormat.format((Double) money);
        } else if (money instanceof String) {
            return moneyFormat.format(Double.parseDouble((String) money));
        } else {
            return null;
        }
    }



    /**
     * 判断输入的是否是数字 Pattern 正则表达式
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否是汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }


    public static String calendarToDateString(Calendar c) {
        return DateFormat.format("yyyy-MM-dd", c).toString();
    }

    public static String calendarToMonthDay(Calendar c) {
        return (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH)
                + "日";
    }

    public static String getFormatDate(Calendar c) {
        DecimalFormat df = new DecimalFormat("00");
        String month = df.format(c.get(Calendar.MONTH) + 1);
        String day = df.format(c.get(Calendar.DAY_OF_MONTH));
        return month + "月" + day + "日";
    }

    /**
     * 格式化两位数
     *
     * @param num
     * @return
     */
    public static String getFormatNumber(int num) {
        DecimalFormat df = new DecimalFormat("00");
        return df.format(num);
    }

    /**
     * 判断是否为手机号
     *
     * @param tel 判断的字符串
     * @return 是为手机号:true  不是手机号:false
     */
    public static boolean isPhoneNum(String tel) {
        if (TextUtils.isEmpty(tel)) {
            return false;
        }
        return tel.matches("^[1][3-9]\\d{9}$");
    }

    /**
     * 校验密码合法性
     *
     * @param password
     * @return
     */
    public static boolean isPasswordValid(String password) {
        return Pattern.compile("^[a-zA-Z0-9]{6,12}$").matcher(password).matches();
    }


    /**
     * 隐藏手机号中间四位数
     *
     * @param phone
     * @return
     */
    public static String hidePhoneNumber(String phone) {
        if (isPhoneNum(phone)) {
            // 隐藏中间四位
            return new StringBuilder().append(phone.substring(0, 3)).append("****").append(phone.substring(phone.length() - 4)).toString();
        }
        return "";
    }

    /**
     * 日期转化为 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getStr2Date(Date date) {
        return sdf.format(date);
    }

    /**
     * yyyy-MM-dd转化为日期
     *
     * @param strDate
     * @return
     */
    public static Date getDate2Str(String strDate) {
        return sdf.parse(strDate, new ParsePosition(0));
    }

    /**
     * 字符串转换成int类型
     */
    public static int StrToInt(String str) {
        int num = 0;
        try {
            if (TextUtils.isEmpty(str)) {
                num = 0;
            } else {
                num = Integer.parseInt(str);
            }
            return num;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 字符串转换成long类型
     */
    public static long StrToLong(String str) {
        long num = 0;
        try {
            if (TextUtils.isEmpty(str)) {
                num = 0;
            } else {
                num = Long.parseLong(str);
            }
            return num;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 字符串转换成double类型
     */
    public static double StrToDouble(String str) {
        double num = 0;
        try {
            if (TextUtils.isEmpty(str)) {
                num = 0;
            } else {
                num = Double.parseDouble(str);
            }
            return num;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 字符串转换成float类型
     */
    public static float StrToFloat(String str) {
        float num = 0;
        try {
            if (TextUtils.isEmpty(str)) {
                num = 0;
            } else {
                num = Float.parseFloat(str);
            }
            return num;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 字数超过六个自动缩小
     */
    public static void setText(String str, TextView textView, Context context) {
        if (str.length() > 5) {
            textView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14,
                    context.getResources().getDisplayMetrics()));
        } else {
            textView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
                    context.getResources().getDisplayMetrics()));
        }
        textView.setText(str);
    }

    // 判断是否符合身份证号码的规范
    public static boolean isIDCard(String IDCard) {
        if (IDCard != null) {
            String IDCardRegex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)";
            return IDCard.matches(IDCardRegex);
        }
        return false;
    }

    /**
     * 格式化四位数生日
     *
     * @param birth
     */
    public static String formatBirth(String birth) {
        if (!TextUtils.isEmpty(birth) && birth.length() == 4) {

            return birth.substring(0, 2) + "." + birth.substring(2);
        }
        return "";
    }

    /**
     * 格式化 银行卡
     * 加空格
     *
     * @param num
     * @return
     */
    public static String formatBankCardNum(String num) {
        if (!TextUtils.isEmpty(num)) {
            return num.replaceAll("\\d{4}(?!$)", "$0 ");
        }
        return "";
    }

    //信用卡号校验算法
    public static boolean isBankNum(String number) {
        if (TextUtils.isEmpty(number) || number.length() < 16) {
            return false;
        }
        int sumOdd = 0;
        int sumEven = 0;
        int length = number.length();
        int[] wei = new int[length];
        for (int i = 0; i < number.length(); i++) {
            wei[i] = Integer.parseInt(number.substring(length - i - 1, length
                    - i));// 从最末一位开始提取，每一位上的数值
        }
        for (int i = 0; i < length / 2; i++) {
            sumOdd += wei[2 * i];
            if ((wei[2 * i + 1] * 2) > 9)
                wei[2 * i + 1] = wei[2 * i + 1] * 2 - 9;
            else
                wei[2 * i + 1] *= 2;
            sumEven += wei[2 * i + 1];
        }
        if ((sumOdd + sumEven) % 10 == 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 通过url获取参数
     *
     * @param url
     * @return
     */
    public static Map<String, String> getParamsFromUrl(String url) {
        Map<String, String> map = new HashMap<>();
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] params = temp.split("&");
        for (String str : params) {
            String[] keyValue = str.split("=");
            if (keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            } else if (keyValue.length == 1) {
                map.put(keyValue[0], "");
            }
        }
        return map;
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /**
     * 正则表达式判断 Email邮箱 是否合法
     * 判断是否是邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
