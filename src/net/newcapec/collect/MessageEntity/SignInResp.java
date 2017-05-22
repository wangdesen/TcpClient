package net.newcapec.collect.MessageEntity;

import net.newcapec.collect.formatter.BinarySerializable;
import net.newcapec.collect.formatter.TimeFormat;

/**
 * Created by ff on 2017/5/15.
 */
public class SignInResp {
    @BinarySerializable(Order = 1, Size = 2)
    public int ResultCode;
    @BinarySerializable(Order = 2, Format = TimeFormat.yyyyMMddHHmmss)
    public java.util.Date Date;
    @BinarySerializable(Order = 3, Size = 1)
    public int EncryptionType;
    @BinarySerializable(Order = 4, Size = 1)
    public int KeyFlag;
    @BinarySerializable(Order = 5, Size = 16)
    public byte[] MasterKey;
    @BinarySerializable(Order = 6, Size = 16)
    public byte[] TempCheckKey;
    @BinarySerializable(Order = 7, Size = 16)
    public byte[] TempEncKey;
    //@BinarySerializable(Order = 8, Size = 2)
    public int ResponseCode;
}
