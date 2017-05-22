package net.newcapec.collect.MessageEntity;

import net.newcapec.collect.formatter.BinFormatter;
import net.newcapec.collect.formatter.BinarySerializable;
import net.newcapec.collect.formatter.NumericType;

/**
 * Created by ff on 2017/5/15.
 */
public class CommonResponse {
    /**
     * 应答码
     */
    @BinarySerializable(Order = 1, Size = 2, NumType = NumericType.BCD)
    public long ResultCode;
    /**
     * 错误码 正确：0064
     */
    @BinarySerializable(Order = 2, Size = 2)
    public int ErrorCode;
    /**
     * 返回信息长度
     */
    @BinarySerializable(Order = 3, Size = 2, NumType = NumericType.BCD)
    public long MessageLength;

    /**
     * 返回信息
     */
    public String Message;

    @Override
    public String toString() {
        return BinFormatter.getString(this);
    }
}
