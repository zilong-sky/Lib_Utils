package com.zilong.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * otc 数字处理工具类
 *
 * 注 ：
 *      1. 三位添加逗号格式化操作，无关精度,如需精度操作，请自定调用精度方法
 *      2. 所有数据，只接收 string，因为double会有精度问题，只认string，自己转，精度丢了，自己负责，我不管
 */
public class NumUtil {

    //格式化 逗号分隔
    //不对数据进行四舍五入等操作，只是逗号分隔
    public static String formatComma(String value) {
        if (!TextUtils.isEmpty(value)) {
            String[] values = value.split("\\.");

            char[] chars = values[0].toCharArray();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                sb.append(chars[chars.length - i - 1]);
                if ((i + 1) % 3 == 0 && i != 1 && i != chars.length - 1) {
                    sb.append(",");
                }
            }
            sb = sb.reverse();
            if (values.length == 2) {
                sb.append(".");
                sb.append(values[1]);
            }
            return sb.toString();

        }
        return "";
    }


    //默认四舍五入
    //默认 不去除 多余0
    public static String formatAccuracy(String num, int accuracy) {
        return formatAccuracy(num, accuracy, RoundingMode.DOWN, false);
    }

    /**
     * 格式化 精度
     *
     * @param num         被格式化 数据  BigDecimal保证使用的string初始化，因为如果传入的是 2.3 ，底层可能会解析成 2.29999999，导致错误
     * @param accuracy    精度
     * @param roudingMode 模式
     * @param stripZeros  是否去除（不在乎精度） 小数点末尾多余的0，例如精度3，但是数据是  2.000  去除就显示  2
     * @return
     */
    public static String formatAccuracy(String num, int accuracy, RoundingMode roudingMode, boolean stripZeros) {

        BigDecimal bg = new BigDecimal(num).setScale(accuracy, roudingMode);
        if (stripZeros) {
            return bg.stripTrailingZeros().toPlainString();
        } else {
            return bg.toPlainString();
        }
    }


    //加法
    public static BigDecimal add(String one, String two) {
        BigDecimal a = new BigDecimal(one);
        BigDecimal b = new BigDecimal(two);
        return a.add(b);
    }

    //剑法
    public static BigDecimal subtract(String one, String two) {
        BigDecimal a = new BigDecimal(one);
        BigDecimal b = new BigDecimal(two);
        return a.subtract(b);
    }

    //乘法
    public static BigDecimal multiply(String one, String two) {
        BigDecimal a = new BigDecimal(one);
        BigDecimal b = new BigDecimal(two);
        return a.multiply(b);
    }


    //默认四舍五入
    //默认 20位
    public static BigDecimal divide(String one, String two) {
        BigDecimal a = new BigDecimal(one);
        BigDecimal b = new BigDecimal(two);
        //默认 保留20位小数，足够用了，模式自己制定
        return a.divide(b, 20, RoundingMode.DOWN);
    }
    /**
     * 除法，因为有除不尽的情况，所以必须指定 舍位的模式（默认保留20位小数，我觉得够用了）
     *
     * @param one          被除数
     * @param two          除数
     * @param roundingMode
     * @return
     */
    public static BigDecimal divide(String one, String two, RoundingMode roundingMode) {
        BigDecimal a = new BigDecimal(one);
        BigDecimal b = new BigDecimal(two);
        //默认 保留20位小数，足够用了，模式自己制定
        return a.divide(b, 20, roundingMode);
    }
}
