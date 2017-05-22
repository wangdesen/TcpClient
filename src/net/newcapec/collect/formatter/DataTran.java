package net.newcapec.collect.formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通用数据转化类
 *
 * @author 陈飞飞 数据类型转换
 */
public class DataTran {
    /**
     * bcd转化为hex
     */
    public static int bCDToInt(byte bcdByte) {
        return (bcdByte >> 4) * 10 + (bcdByte & 0x0F);
    }

    /**
     * hex转化为bcd
     */
    public static byte intToBCD(int i) {
        byte b = (byte) (i % 256);
        return (byte) (((b / 10) << 4) | (b % 10));
    }

    /**
     * hex字符转化为串字节数组
     *
     * @param s 原始字符串
     * @return 目标字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        s = s.replace(" ", "");
        byte[] buffer = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2) {
            buffer[i / 2] =(byte)Integer.parseInt(s.substring(i, i + 2), 16);
        }
        return buffer;
    }

    /**
     * 字节数组转换为hex字符串
     *
     * @param data   原始字节数组
     * @param offset 起始下标
     * @param size   数组长度
     * @return 目标字符串
     */
    public static String byteArrayToHexString(byte[] data, int offset, int size) {
        StringBuilder sb = new StringBuilder(size * 2);

        for (int i = offset; i < offset + size; i++) {
            sb.append(String.format("%02x", data[i]));
        }

        return sb.toString().toUpperCase();
    }

    /**
     * 右补位，左对齐
     *
     * @param oriStr 原字符串
     * @param len    目标字符串长度
     * @param alexin 补位字符
     * @return 目标字符串
     */
    public static String padRight(String oriStr, int len, byte alexin) {
        return padRight(oriStr, len, (char) alexin);
    }

    /**
     * 右补位，左对齐
     *
     * @param oriStr 原字符串
     * @param len    目标字符串长度
     * @param alexin 补位字符
     * @return 目标字符串
     */
    public static String padRight(String oriStr, int len, char alexin) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len - oriStr.length(); i++) {
            stringBuilder.append(alexin);
        }

        return oriStr + stringBuilder.toString();
    }

    /**
     * 左补位，右对齐
     *
     * @param oriStr 原字符串
     * @param len    目标字符串长度
     * @param alexin 补位字符
     * @return 目标字符串
     */
    public static String padLeft(String oriStr, int len, byte alexin) {
        return padLeft(oriStr, len, (char) alexin);
    }

    /**
     * 左补位，右对齐
     *
     * @param oriStr 原字符串
     * @param len    目标字符串长度
     * @param alexin 补位字符
     * @return 目标字符串
     */
    public static String padLeft(String oriStr, int len, char alexin) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len - oriStr.length(); i++) {
            stringBuilder.append(alexin);
        }

        return stringBuilder.toString() + oriStr;
    }

    /**
     * 时间转换为字符串
     *
     * @param dt 时间
     * @param tf 转换格式
     * @return 转换后得时间字符串
     */
    public static String getLocalDateTimeStr(Date dt, TimeFormat tf) {
        SimpleDateFormat formatter = new SimpleDateFormat(tf.name());
        return formatter.format(dt);
    }

    /**
     * 转换制定格式字符串为Date
     *
     * @param dt 源时间字符串
     * @param tf 时间格式
     * @return 时间
     * @throws ParseException 数据异常
     */
    public static Date getLocalDateTime(String dt, TimeFormat tf) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(tf.name());
        return formatter.parse(dt);
    }

    /**
     * 转换制定格式字符串为Date 数据异常，取当前时间
     *
     * @param dt 源时间字符串
     * @param tf 时间格式
     * @return 时间
     */
    public static Date getLocalDateTimeNoException(String dt, TimeFormat tf) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(tf.name());
            return formatter.parse(dt);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param value
     * @return
     */
    public static boolean isStrNull(String value) {
        if (null == value || value.isEmpty())
            return true;
        return false;
    }
}
