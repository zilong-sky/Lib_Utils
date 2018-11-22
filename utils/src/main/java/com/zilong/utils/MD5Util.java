package com.zilong.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;


/**
 * 加密类
 */
public class MD5Util {

    //加的盐
    public static final String SALT = "HXWcjvQWVG1wI4FQBLZpQ3pWj48AV63d";

    //常规加密 (十六进制大写)
    public static String encoderByMd5(String buf) {
        try {
            MessageDigest digist = MessageDigest.getInstance("MD5");
            byte[] rs = digist.digest(buf.getBytes("utf-8"));
            StringBuffer digestHexStr = new StringBuffer();
            for (int i = 0; i < 16; i++) {
                digestHexStr.append(byteHEX(rs[i]));
            }
            return digestHexStr.toString();
        } catch (Exception e) {
            Log.e("MD5Util",e.toString());
        }
        return null;

    }

    //常规加密 (十六进制小写)
    public static String encoderByMd5_2(String plaintext) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = plaintext.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加盐的md5值。这样即使被拖库，仍然可以有效抵御彩虹表攻击
     *
     * @param inbuf 需做md5的字符串
     * @return
     */
    public static String encodeByMd5AndSalt(String inbuf) {
        return encoderByMd5(encoderByMd5(inbuf) + SALT);
    }

    public static String byteHEX(byte ib) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }


    private final static String START_WITH = "9F|";
    private final static String END_WITH = "LCS";

    //带前后缀 然后加盐,加密
    public static String makeSign(TreeMap<String, String> map) {
        StringBuffer sb = new StringBuffer();
        sb.append(START_WITH);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getValue() + "|");
        }
        sb.append(END_WITH);
        //生成签名
        return encodeByMd5AndSalt(sb.toString());

    }


}
