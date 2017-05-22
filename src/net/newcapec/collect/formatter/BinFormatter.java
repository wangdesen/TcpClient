package net.newcapec.collect.formatter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 序列化类，支持二进制、tlv格式
 *
 * @author 陈飞飞
 */
public class BinFormatter {
    //最大报文长度
    private final int MAXMESSAGELENGTH = 1024;

    /**
     * Tag结束标签
     */
    public final short FINAL_TAG = (short) 0xFFFF;

    /**
     * 将对象序列化为二进制格式数组
     *
     * @param obj 要序列化的对象
     * @return 对象序列化后二进制数组
     */
    public byte[] serialize(Object obj)
            throws UnsupportedEncodingException, IllegalArgumentException, IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        if (fields == null || fields.length <= 0) {
            return null;
        }

        List<byte[]> fieldsBuf = new ArrayList<>();
        for (Field field : fields) {
            // 获取访问权限
            field.setAccessible(true);
            if (!(field.isAnnotationPresent(BinarySerializable.class)))
                continue;

            fieldsBuf.add(null);

            BinarySerializable att = field.getAnnotation(BinarySerializable.class);

            byte[] fBuf = fieldEncode(field.getType(), field.get(obj), att);
            fieldsBuf.set(att.Order() - 1, fBuf);
        }

        return mergeBuffer(fieldsBuf);
    }

    private byte[] fieldEncode(Class<?> fieldTypeCode, Object fieldValue, BinarySerializable att)
            throws UnsupportedEncodingException {
        byte[] buf = null;
        switch (fieldTypeCode.getName()) {
            case "byte":
                buf = Encoder.getBytes((byte) fieldValue, att.Size());
                break;
            case "short":
                buf = Encoder.getBytes((short) fieldValue, att.Size());
                break;
            case "int":
                buf = Encoder.getBytes((int) fieldValue, att.Size());
                break;
            case "long":
                buf = Encoder.getBytes((long) fieldValue, att.Size(), att.NumType());
                break;
            case "java.util.Date":
                buf = Encoder.getBytes((Date) fieldValue, att.Format());
                break;
            case "java.lang.String":
                String fieldValueStr = (String) fieldValue;
                if (DataTran.isStrNull(fieldValueStr)) {
                    fieldValueStr = "";
                }
                buf = Encoder.getBytes(fieldValueStr, att.CodeName(), att.Size(), att.PadMode(), att.PadByte());
                break;
            case "[B":
                byte[] arr = (byte[]) fieldValue;
                if (null == arr)
                    arr = new byte[0];
                List<byte[]> fieldsBuf2 = new ArrayList<>();
                if (att.HasLeaderCharacter()) {
                    switch (att.ArrayLeadByte()) {
                        case One:
                            fieldsBuf2.add(new byte[]{(byte) arr.length});
                            break;
                        case Two:
                            fieldsBuf2.add(Encoder.getBytes(arr.length, 2));
                            break;
                        case Zero:
                        default:
                            break;
                    }
                }

                fieldsBuf2.add(arr);
                buf = mergeBuffer(fieldsBuf2);
                break;
            default: {
                if (att.IsEntity())
                    if (fieldTypeCode.isArray()) {
                        Object[] objects = (Object[]) fieldValue;
                        if (objects == null)
                            break;
                        List<byte[]> data = new ArrayList<>();
                        for (Object o : objects
                                ) {
                            try {
                                data.add(serialize(o));
                            } catch (IllegalAccessException e) {
                            }
                        }
                        buf = mergeBuffer(data);
                    } else {
                        try {
                            buf = serialize(fieldValue);
                        } catch (IllegalAccessException e) {
                        }
                    }
            }
            break;
        }

        return buf;
    }

    /**
     * 将二进制格式数组反序列化为对象
     *
     * @param c      反序列化对象类型
     * @param buf    要反序列化的二进制数组
     * @param offset 要反序列化的二进制数组起始下标
     * @return 反序列化后对象
     */
    public <T> T deserialize(Class<T> c, byte[] buf, int offset) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, UnsupportedEncodingException {
        T obj = c.newInstance();

        Field[] fields = obj.getClass().getDeclaredFields();
        if (fields == null || fields.length <= 0) {
            return null;
        }

        List<Field> fieldList = new ArrayList<>();
        List<BinarySerializable> attList = new ArrayList<>();

        for (Field field : fields) {
            fieldList.add(field);
            attList.add(field.getAnnotation(BinarySerializable.class));
        }

        int pos = offset;
        for (int n = 0; n < fieldList.size(); n++) {
            Field field = fieldList.get(n);
            BinarySerializable att = attList.get(n);
            if (field == null || att == null)
                continue;

            field.setAccessible(true);

            DecodeObject dObject = fieldDecode(buf, pos, field.getType(), att);
            pos = dObject.pos;
            field.set(obj, dObject.object);
        }

        return obj;
    }

    private DecodeObject fieldDecode(byte[] buf, int offset, Class<?> fieldTypeCode, BinarySerializable att)
            throws UnsupportedEncodingException {
        Object obj = null;

        switch (fieldTypeCode.getName()) {
            case "byte":
                obj = buf[offset];
                offset++;
                break;
            case "short":
                obj = Decoder.toInt16(buf, offset, att.Size());
                offset += att.Size();
                break;
            case "int":
                obj = Decoder.toInt32(buf, offset, att.Size());
                offset += att.Size();
                break;
            case "long":
                obj = Decoder.toInt64(buf, offset, att.Size(), att.NumType());
                offset += att.Size();
                break;
            case "java.util.Date":
                obj = Decoder.toDateTime(buf, offset, att.Format(), att.ThrowTimeFormatException());
                offset += att.Format().toString().length() / 2;
                break;
            case "java.lang.String":
                obj = Decoder.toString(buf, offset, att.Size(), att.CodeName(), att.PadMode(), att.PadByte());
                offset += att.Size();
                break;
            case "[B":
                int arrayLen = att.Size();
                if (att.HasLeaderCharacter()) {
                    switch (att.ArrayLeadByte()) {
                        case One:
                            arrayLen = buf[offset];
                            offset++;
                            break;
                        case Two:
                            arrayLen = Decoder.toInt16(buf, offset, 2);
                            offset += 2;
                            break;
                        case Zero:
                        default:
                            break;
                    }
                }

                byte[] fieldsBuf2 = new byte[arrayLen];
                System.arraycopy(buf, offset, fieldsBuf2, 0, arrayLen);

                offset += arrayLen;
                obj = fieldsBuf2;
                break;
            default:
                break;
        }

        return new DecodeObject(obj, offset);
    }

    /**
     * 反序列化字段对象 辅助返回类
     */
    class DecodeObject {
        Object object = null;
        int pos = 0;

        DecodeObject(Object object, int pos) {
            this.object = object;
            this.pos = pos;
        }
    }

    private byte[] mergeBuffer(List<byte[]> data) {
        if (data != null && data.size() > 0) {
            byte[] tmp = new byte[MAXMESSAGELENGTH];
            int len = 0;
            for (byte[] bs : data) {
                if (bs != null && bs.length > 0) {
                    System.arraycopy(bs, 0, tmp, len, bs.length);
                    len += bs.length;
                }
            }

            byte[] rByte = new byte[len];
            System.arraycopy(tmp, 0, rByte, 0, len);

            return rByte;
        }

        return null;
    }

    /**
     * 对象toString
     *
     * @param obj 字符串对象
     * @return 字符串
     */
    public static String getString(Object obj) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            if (fields == null || fields.length <= 0) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            for (Field field : fields) {
                field.setAccessible(true);

                if (null == field.get(obj))
                    sb.append(field.getName()).append(":null,");
                else {
                    if (field.getType().getName().equals("[B")) {
                        byte[] data = (byte[]) field.get(obj);
                        sb.append(field.getName()).append(":").append(DataTran.byteArrayToHexString(data, 0, data.length)).append(",");
                    } else {
                        sb.append(field.getName()).append(":").append(field.get(obj).toString()).append(",");
                    }
                }
            }

            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 将对象序列化为二进制格式数组 TLV格式
     *
     * @param obj 要序列化的对象
     * @return 对象序列化后二进制数组
     */
    public byte[] serializeTLV(Object obj)
            throws UnsupportedEncodingException, IllegalArgumentException, IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        if (fields == null || fields.length <= 0) {
            return null;
        }

        List<byte[]> fieldsBuf = new ArrayList<>();

        int order = 0;

        for (Field field : fields) {
            field.setAccessible(true);
            if (!(field.isAnnotationPresent(BinarySerializable.class)))
                continue;

            BinarySerializable att = field.getAnnotation(BinarySerializable.class);
            if (att.Tag() == FINAL_TAG)
                continue;

            fieldsBuf.add(null);

            byte[] data = fieldEncodeTLV(field.getType(), field.get(obj), att);
            byte[] tlvData = new byte[data.length + 3];
            System.arraycopy(Encoder.getBytes(att.Tag(), 2), 0, tlvData, 0, 2);
            tlvData[2] = (byte) data.length;
            System.arraycopy(data, 0, tlvData, 3, data.length);

            fieldsBuf.set(order, tlvData);
            order++;
        }

        fieldsBuf.add(null);
        fieldsBuf.set(order, new byte[]{(byte) 0xFF, (byte) 0xFF});

        return mergeBuffer(fieldsBuf);
    }

    private byte[] fieldEncodeTLV(Class<?> fieldTypeCode, Object fieldValue, BinarySerializable att)
            throws UnsupportedEncodingException {
        byte[] buf = null;
        switch (fieldTypeCode.getName()) {
            case "byte":
                buf = Encoder.getBytes((byte) fieldValue, 1);
                break;
            case "short":
                if (att.Size() > 0) {
                    buf = Encoder.getBytes((short) fieldValue, att.Size());
                } else {
                    buf = Encoder.getBytes((short) fieldValue, 2);
                }
                break;
            case "int":
                if (att.Size() > 0) {
                    buf = Encoder.getBytes((int) fieldValue, att.Size());
                } else {
                    buf = Encoder.getBytes((int) fieldValue, 4);
                }
                break;
            case "long":
                if (att.Size() > 0) {
                    buf = Encoder.getBytes((long) fieldValue, att.Size(), att.NumType());
                } else {
                    buf = Encoder.getBytes((long) fieldValue, 8, att.NumType());
                }
                break;
            case "java.util.Date":
                buf = Encoder.getBytes((Date) fieldValue, att.Format());
                break;
            case "java.lang.String": {
                String fieldValueStr = (String) fieldValue;
                if (fieldValueStr == null || fieldValueStr.length() <= 0) {
                    fieldValueStr = "";
                }
                buf = Encoder.getBytes(fieldValueStr, att.CodeName());
            }
            break;
            case "[B":
                buf = (byte[]) fieldValue;
                break;
            default:
                break;
        }

        // 组合
        return buf;
    }

    /**
     * 将二进制格式数组反序列化为对象 TLV格式
     *
     * @param c      反序列化对象类型
     * @param buf    要反序列化的二进制数组
     * @param offset 要反序列化的二进制数组起始下标
     * @return 反序列化后对象
     */
    public <T> T deSerializeTLV(Class<T> c, byte[] buf, int offset) throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, UnsupportedEncodingException {
        T obj = c.newInstance();

        Field[] fields = obj.getClass().getDeclaredFields();
        if (fields == null || fields.length <= 0) {
            return null;
        }

        Map<Integer, Field> fieldMap = new HashMap<>();
        Map<Integer, BinarySerializable> attMap = new HashMap<>();

        for (Field field : fields) {
            BinarySerializable att = field.getAnnotation(BinarySerializable.class);
            if (null != att) {
                fieldMap.put(att.Tag(), field);
                attMap.put(att.Tag(), att);
            }
        }

        int pos = offset;
        for (; offset < buf.length; ) {
            int tag = Decoder.toInt32(buf, pos, 2);
            pos += 2;

            if (tag == FINAL_TAG || !fieldMap.containsKey(tag)) {
                pos += 1;
                break;
            }

            int len = 0x000000FF & buf[pos];
            pos++;

            Field field = fieldMap.get(tag);
            BinarySerializable att = attMap.get(tag);

            field.setAccessible(true);

            field.set(obj, fieldDecodeTLV(buf, pos, field.getType(), att, len));
            pos += len;
        }

        ((TlvBase) obj).Length = pos;

        return obj;
    }

    private Object fieldDecodeTLV(byte[] buf, int offset, Class<?> fieldTypeCode, BinarySerializable att, int len)
            throws UnsupportedEncodingException {
        Object obj = null;

        switch (fieldTypeCode.getName()) {
            case "byte":
                obj = buf[offset];
                break;
            case "short":
                obj = Decoder.toInt16(buf, offset, len);
                break;
            case "int":
                obj = Decoder.toInt32(buf, offset, len);
                break;
            case "long":
                obj = Decoder.toInt64(buf, offset, len, att.NumType());
                break;
            case "java.util.Date":
                obj = Decoder.toDateTime(buf, offset, att.Format(), att.ThrowTimeFormatException());
                break;
            case "java.lang.String":
                obj = Decoder.toString(buf, offset, len, att.CodeName());
                break;
            case "[B":
                byte[] fieldsBuf2 = new byte[len];
                System.arraycopy(buf, offset, fieldsBuf2, 0, len);
                obj = fieldsBuf2;
                break;
            default:
                break;
        }

        return obj;
    }


    /**
     * 将对象序列化为二进制格式数组 TLV格式 银联
     *
     * @param obj 要序列化的对象
     * @return 对象序列化后二进制数组
     */
    public byte[] serializeTLVUP(Object obj)
            throws UnsupportedEncodingException, IllegalArgumentException, IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        if (fields == null || fields.length <= 0) {
            return null;
        }

        List<byte[]> fieldsBuf = new ArrayList<>();

        int order = 0;

        for (Field field : fields) {
            field.setAccessible(true);
            if (!(field.isAnnotationPresent(BinarySerializable.class)))
                continue;

            BinarySerializable att = field.getAnnotation(BinarySerializable.class);
            if (att.Tag() == FINAL_TAG)
                continue;

            fieldsBuf.add(null);

            byte[] value = fieldEncodeTLVUP(field.getType(), field.get(obj), att);

            byte[] reBuf = new byte[value.length + 3];
            System.arraycopy(Encoder.getBytes(att.Tag(), 2), 0, reBuf, 0, 2);
            if (att.Size() > 0)
                reBuf[2] = (byte) att.Size();
            else
                reBuf[2] = (byte) value.length;
            System.arraycopy(value, 0, reBuf, 3, value.length);

            fieldsBuf.set(order, reBuf);
            order++;
        }

        return mergeBuffer(fieldsBuf);
    }

    private byte[] fieldEncodeTLVUP(Class<?> fieldTypeCode, Object fieldValue, BinarySerializable att)
            throws UnsupportedEncodingException {
        byte[] buf = null;
        switch (fieldTypeCode.getName()) {
            case "byte":
                buf = Encoder.getBytes((byte) fieldValue, 1);
                break;
            case "short":
                if (att.Size() > 0) {
                    buf = Encoder.getBytes((short) fieldValue, att.Size());
                } else {
                    buf = Encoder.getBytes((short) fieldValue, 2);
                }
                break;
            case "int":
                if (att.Size() > 0) {
                    buf = Encoder.getBytes((int) fieldValue, att.Size());
                } else {
                    buf = Encoder.getBytes((int) fieldValue, 4);
                }
                break;
            case "long":
                if (att.Size() > 0) {
                    buf = Encoder.getBytesUP((long) fieldValue, att.Size(), att.NumType());
                } else {
                    buf = Encoder.getBytesUP((long) fieldValue, 8, att.NumType());
                }
                break;
            case "java.util.Date":
                buf = Encoder.getBytes((Date) fieldValue, att.Format());
                break;
            case "java.lang.String": {
                String fieldValueStr = (String) fieldValue;
                if (fieldValueStr == null || fieldValueStr.length() <= 0) {
                    fieldValueStr = "";
                }
                buf = Encoder.getBytes(fieldValueStr, att.CodeName());
            }
            break;
            case "[B":
                buf = (byte[]) fieldValue;
                break;
            default:
                break;
        }

        // 组合
        return buf;
    }

}
