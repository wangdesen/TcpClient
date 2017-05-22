package net.newcapec.collect.tcpClient;

import net.newcapec.collect.MessageEntity.CheckMac1AndGetMac2;
import net.newcapec.collect.MessageEntity.CheckTac;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ff on 2017/5/16.
 */
public class Test {
    private static int sendNum=0;
    private static int failNum=0;
        public static void main(String[] args) {
            final TcpClinetProxy tcpClinetProxy = new TcpClinetProxy("192.168.61.196", 9992, 1, "C0176662B2C0A986E822439D229B2200", 30,"C:\\TcpClient");
            //long begintick=new Date().getTime();
            //ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
            
            ExecutorService executorService = Executors.newFixedThreadPool(20);
            
            for (int i = 0; i < 30; i++) {
                final int index = i;
                try {
                    Thread.sleep(index * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.execute(new Runnable() {
                //cachedThreadPool.execute(new Runnable() {
                    public void run() {
                        while (true) {
                        	    System.out.println("发送数：" + sendNum);
                        	    System.out.println("失败数：" + failNum);
                            try {
                            	long begintick=new Date().getTime();
                                CheckMac1AndGetMac(tcpClinetProxy);
                                //checkTac(tcpClinetProxy);
                                    Log.getInstance().write(String.format("耗时 %d 发送:%d,失败:%d",(new Date().getTime()- begintick),sendNum,failNum));
                            } catch (Exception e) {
                                System.out.println(e.getMessage() + MyException.getStackTrace(e));
                            }
                        }
                    }
                });
            }
            executorService.shutdown();
        }

    public static void CheckMac1AndGetMac(TcpClinetProxy tcpClinetProxy)
    {
        CheckMac1AndGetMac2 data=new CheckMac1AndGetMac2();
        data.Command=0x5844;
        data.CardASN=0x030405060708090AL;
        data.Random=0x0A0B0C0D;
        data.WalletType=0x00;
        data.Balance=0x0000ABCD;
        data.OpCount=0x00000066;
        data.OpFare=0x00000109;
        data.Poscode=0x0371F0000014L;
        data.OpDate=20140814125058L;
        data.Mac1=0x67D2BC70;
        data.CustomersFlag=000000000001;
        try {
            long begin=new Date().getTime();
            tcpClinetProxy.checkMac1AndGetMac(data);
            sendNum++;
            Log.getInstance().write(String.format("MAC耗时:%dms",new Date().getTime()-begin));
        } catch (Exception e) {
            failNum++;
            e.printStackTrace();
        }

    }

    public static void checkTac(TcpClinetProxy tcpClinetProxy)
    {
        CheckTac data=new CheckTac();
        data.Command=0x5845;
        data.CardASN=0x131405060708090AL;
        data.MakeCardID=0x0000000000000000L;
        data.WalletType=0x00;
        data.Balance=0x00000001;
        data.OpCount=0x00000001;
        data.OpFare=0x00000001;
        data.Poscode=0x0371F0000014L;
        data.OpDate=20151211145958L;
        data.TacType=00;
        data.Tac=0xF6C9F106;
        try {
            long begin=new Date().getTime();
            tcpClinetProxy.checkTac(data);
            sendNum++;
            Log.getInstance().write(String.format("Tac耗时:%dms",new Date().getTime()-begin));
        } catch (Exception e) {
            failNum++;
            e.printStackTrace();
        }

    }
}
