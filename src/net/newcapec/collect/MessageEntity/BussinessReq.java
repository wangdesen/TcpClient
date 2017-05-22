package net.newcapec.collect.MessageEntity;

import net.newcapec.collect.formatter.BinarySerializable;
import net.newcapec.collect.formatter.NumericType;

/**
 * Created by ff on 2017/5/15.
 */
public class BussinessReq {
    @BinarySerializable(Order = 1, Size = 6, NumType = NumericType.BCD)
    public long AppID;
    @BinarySerializable(Order = 2)
    public byte[] data;

    public BussinessReq(long AppID,byte[] data)
    {
        this.AppID=AppID;
        this.data=new byte[data.length];
        System.arraycopy(data,0,this.data,0,data.length);
    }
}
