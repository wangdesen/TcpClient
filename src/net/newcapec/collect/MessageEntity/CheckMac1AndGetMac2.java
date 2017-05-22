package net.newcapec.collect.MessageEntity;

import net.newcapec.collect.formatter.BinFormatter;
import net.newcapec.collect.formatter.BinarySerializable;
import net.newcapec.collect.formatter.NumericType;

/**
 * Created by ff on 2017/5/15.
 */
public class CheckMac1AndGetMac2 {
    /**
     * 命令 XD: 验证MAC计算MAC2，固定为“5844”。
     */
    @BinarySerializable(Order = 1, Size = 2)
    public int Command = 0x5844;
    /**
     * 卡应用序列号
     */
    @BinarySerializable(Order = 2, Size = 8)
    public long CardASN;
    /**
     * 伪随机数 卡片在充值初始化时候返回的伪随机数
     */
    @BinarySerializable(Order = 3, Size = 4)
    public int Random;
    /**
     * 钱包类型  主钱包00补助钱包01
     */
    @BinarySerializable(Order = 4, Size = 1)
    public int WalletType;
    /**
     * 钱包余额 充值初始化时返回的交易余额 交易前
     */
    @BinarySerializable(Order = 5, Size = 4)
    public int Balance;
    /**
     * 电子钱包脱机/联机交易序号 卡片在充值初始化时返回的联机交易序号
     */
    @BinarySerializable(Order = 6, Size = 4)
    public int OpCount;
    /**
     * 交易金额
     */
    @BinarySerializable(Order = 7, Size = 4)
    public int OpFare;
    /**
     * 终端机编号
     */
    @BinarySerializable(Order = 8, Size = 6)
    public long Poscode;
    /**
     * 交易日期时间
     */
    @BinarySerializable(Order = 9, Size = 7, NumType = NumericType.BCD)
    public long OpDate;
    /**
     * 原始MAC1值 充值初始化时卡片返回的MAC1数据
     */
    @BinarySerializable(Order = 10, Size = 4)
    public int Mac1;
    /**
     * 多客户标记 0001，固定值
     */
    //@BinarySerializable(Order = 11, Size = 2)
    public int CustomersFlag = 0x0001;
    /**
     * 一卡通企业标识的数据长度
     */
    //@BinarySerializable(Order = 12, Size = 1)
    public int CustomerIDLength;
    /**
     * 一卡通企业标识的数据 一卡通平台中的企业编号(客户代码)。一卡通平台、TSM平台、密钥前置中的此ID必须一致
     */
    //@BinarySerializable(Order = 13)
    public byte[] CustomerID;

    @Override
    public String toString() {
        return BinFormatter.getString(this);
    }
}
