package net.newcapec.collect.MessageEntity;

import net.newcapec.collect.formatter.BinFormatter;
import net.newcapec.collect.formatter.BinarySerializable;
import net.newcapec.collect.formatter.NumericType;

/**
 * Created by ff on 2017/5/15.
 */
public class CheckTac {
    /**
     * 命令
     */
    @BinarySerializable(Order = 1, Size = 2)
    public int Command=0x5845;
    /**
     * 卡应用序列号
     */
    @BinarySerializable(Order = 2,Size = 8)
    public long CardASN;
    /**
     * 制卡商标识
     */
    @BinarySerializable(Order = 3,Size = 8)
    public long MakeCardID;
    /**
     * 钱包类型 主钱包00补助钱包01
     */
    @BinarySerializable(Order = 4,Size = 1)
    public int WalletType;
    /**
     * 钱包余额（交易后）
     */
    @BinarySerializable(Order = 5,Size = 4)
    public int Balance;
    /**
     * 交易序号（加1前）
     */
    @BinarySerializable(Order = 6,Size = 4)
    public int OpCount;
    /**
     * 交易金额
     */
    @BinarySerializable(Order = 7,Size = 4)
    public int OpFare;
    /**
     * 终端机编号
     */
    @BinarySerializable(Order = 8,Size = 6)
    public long Poscode;
    /**
     *
     */
    @BinarySerializable(Order = 9, Size = 7,NumType = NumericType.BCD)
    public long OpDate;
    /**
     * 交易日期（主机）、交易时间（主机）
     */
    @BinarySerializable(Order = 10,Size = 1)
    public int TacType;
    /**
     * Tac类型 充值00 消费01
     */
    @BinarySerializable(Order = 11,Size = 4)
    public int Tac;
    /**
     * 原始TAC值
     */
    //@BinarySerializable(Order = 12,Size = 2)
    public int CustomersFlag;
    /**
     * 一卡通企业标识的数据长度
     */
    //@BinarySerializable(Order = 13,Size = 1)
    public int CustomerIDLength;
    /**
     * 一卡通企业标识的数据 一卡通平台中的企业编号(客户代码)。一卡通平台、TSM平台、密钥前置中的此ID必须一致
     */
    //@BinarySerializable(Order=14)
    public byte[] CustomerID;

    @Override
    public String toString()
    {
        return BinFormatter.getString(this);
    }
}
