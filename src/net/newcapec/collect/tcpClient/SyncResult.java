package net.newcapec.collect.tcpClient;

import net.newcapec.collect.MessageEntity.CommonResponse;

import java.util.Date;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ff on 2017/5/16. 返回结果
 */
public class SyncResult {
    /**
     * 消息序列号
     */
    public int messageSN;
    /**
     *
     */
    public Condition condition;
    public Lock lock;
    /**
     * 应答消息
     */
    public CommonResponse commonResponse;

    public Date createDT;

    public SyncResult()
    {
        lock=new ReentrantLock();
        condition=lock.newCondition();
        createDT=new Date();
    }
}
