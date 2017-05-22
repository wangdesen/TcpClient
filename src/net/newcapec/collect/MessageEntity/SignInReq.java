package net.newcapec.collect.MessageEntity;

import net.newcapec.collect.formatter.BinarySerializable;
import net.newcapec.collect.formatter.NumericType;

/**
 * Created by ff on 2017/5/15.
 */
public class SignInReq {
    @BinarySerializable(Order = 1, Size = 6, NumType = NumericType.BCD)
    public long AppID;
    @BinarySerializable(Order = 2, Size = 8)
    public long Random;
}
