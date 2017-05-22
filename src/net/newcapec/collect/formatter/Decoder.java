package net.newcapec.collect.formatter;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 解码
 */
public class Decoder {

    public static String toString(byte[] bytes, int offset, int length, String codeName)
            throws UnsupportedEncodingException {
        if (codeName.equals("BCD")) {
            return DataTran.byteArrayToHexString(bytes, offset, length);
        }

        byte[] tmp = new byte[length];
        System.arraycopy(bytes, offset, tmp, 0, length);

        return new String(tmp, codeName).trim();
    }

    public static String toString(byte[] buf, int offset, int length, String codeName, PaddingMode padMode, byte padByte)
            throws UnsupportedEncodingException {
        int index = 0;
        switch (padMode) {
            case None:
                return toString(buf, offset, length, codeName);
            case PadLeft:
                for (index = 0; index < length && buf[offset + index] == padByte; index++) {
                }

                if (index >= length) {
                    return "";
                } else {
                    return toString(buf, offset + index, length - index, codeName);
                }
            case PadRight:
                for (index = length + offset; index > offset && buf[offset + index - 1] == padByte; index--) {
                }

                if (index <= offset) {
                    return "";
                } else {
                    return Decoder.toString(buf, offset, index, codeName);
                }
            default:
                return "";
        }
    }

    public static short toInt16(byte[] bytes, int offset, int length) {
        short value = 0;
        for (int i = 0; i < length; i++) {
            value += (bytes[i + offset] & 0x000000FF) << ((length - 1 - i) * 8);
        }
        return value;
    }

    public static int toInt32(byte[] bytes, int offset, int length) {
        int value = 0;
        for (int i = 0; i < length; i++) {
            value += (bytes[i + offset] & 0x000000FF) << ((length - 1 - i) * 8);
        }
        return value;
    }

    public static long toInt64(byte[] bytes, int offset, int length) {
        long value = 0;
        for (int i = 0; i < length; i++) {
            value += ((long) (bytes[i + offset] & 0x000000FF)) << ((length - 1 - i) * 8);
        }
        return value;
    }

    public static long toInt64(byte[] bytes, int offset, int length, NumericType nt) {
        if (nt == NumericType.BCD) {
            return Long.parseLong(DataTran.byteArrayToHexString(bytes, offset, length));
        } else {
            return toInt64(bytes, offset, length);
        }
    }


    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

    public static Date toDateTime(byte[] bytes, int offset, TimeFormat format) {
        Date currentTime = new Date();
        String dtStr;

        try {
            switch (format) {
                case yyyyMMddHHmmss:
                    dtStr = String.format("%04d%02d%02d%02d%02d%02d",
                            DataTran.bCDToInt(bytes[offset]) * 100 + DataTran.bCDToInt(bytes[offset + 1]),
                            DataTran.bCDToInt(bytes[offset + 2]),
                            DataTran.bCDToInt(bytes[offset + 3]),
                            DataTran.bCDToInt(bytes[offset + 4]),
                            DataTran.bCDToInt(bytes[offset + 5]),
                            DataTran.bCDToInt(bytes[offset + 6]));
                    break;
                case yyMMddHHmmss:
                    dtStr = String.format("%04d%02d%02d%02d%02d%02d",
                            2000 + DataTran.bCDToInt(bytes[offset]),
                            DataTran.bCDToInt(bytes[offset + 1]),
                            DataTran.bCDToInt(bytes[offset + 2]),
                            DataTran.bCDToInt(bytes[offset + 3]),
                            DataTran.bCDToInt(bytes[offset + 4]),
                            DataTran.bCDToInt(bytes[offset + 5]));
                    break;
                case yyyyMMdd:
                    dtStr = String.format("%04d%02d%02d%02d%02d%02d",
                            DataTran.bCDToInt(bytes[offset]) * 100 + DataTran.bCDToInt(bytes[offset + 1]),
                            DataTran.bCDToInt(bytes[offset + 2]),
                            DataTran.bCDToInt(bytes[offset + 3]),
                            currentTime.getHours(),
                            currentTime.getMinutes(),
                            currentTime.getSeconds());
                    break;
                case yyMMdd:
                    dtStr = String.format("%04d%02d%02d%02d%02d%02d",
                            2000 + DataTran.bCDToInt(bytes[offset]),
                            DataTran.bCDToInt(bytes[offset + 1]),
                            DataTran.bCDToInt(bytes[offset + 2]),
                            currentTime.getHours(),
                            currentTime.getMinutes(),
                            currentTime.getSeconds());
                    break;
                case HHmmss:
                    dtStr = String.format("%04d%02d%02d%02d%02d%02d",
                            currentTime.getYear() + 1900,
                            currentTime.getMonth(),
                            currentTime.getDay(),
                            DataTran.bCDToInt(bytes[offset]),
                            DataTran.bCDToInt(bytes[offset + 1]),
                            DataTran.bCDToInt(bytes[offset + 2]));
                    break;
                case yyMMddHHmm:
                    dtStr = String.format("%04d%02d%02d%02d%02d%02d",
                            2000 + DataTran.bCDToInt(bytes[offset]),
                            DataTran.bCDToInt(bytes[offset + 1]),
                            DataTran.bCDToInt(bytes[offset + 2]),
                            DataTran.bCDToInt(bytes[offset + 3]),
                            DataTran.bCDToInt(bytes[offset + 4]), 00);
                    break;
                default:
                    dtStr = "";
                    break;
            }

            return formatter.parse(dtStr);
        } catch (Exception e) {
            return currentTime;
        }
    }

    public static Date toDateTime(byte[] bytes, int offset, TimeFormat format, boolean ThrowTimeFormatException) {
        if (ThrowTimeFormatException) {
            return toDateTime(bytes, offset, format);
        } else {
            try {
                return Decoder.toDateTime(bytes, offset, format);
            } catch (Exception e) {
                try {
                    return formatter.parse("19880408000000");
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    // 从缓冲区取出指定的数据返回，并处理整型字节序length:在bytes占的字节数;bufLength:需返回的字节数
    private static byte[] convertBuffer(byte[] bytes, int offset, int length, int bufLength) {
        byte[] buf = new byte[bufLength];

        if (bufLength >= length) {
            System.arraycopy(bytes, offset, buf, bufLength - length, length);
        } else {
            System.arraycopy(bytes, offset + length - bufLength, buf, 0, bufLength);
        }

        return buf;
    }
}
