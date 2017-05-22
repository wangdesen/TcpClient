package net.newcapec.collect.tcpClient;

/**
 * Created by ff on 2017/5/15.
 */
class TcpParam {

    /**
     * 服务器IP
     */
    public static String IP;
    /**
     * 服务器端口
     */
    public static int port;
    /**
     * 认证ID
     */
    public static int appID;
    /**
     * 认证密钥
     */
    public static byte[] primaryKey;
    /**
     * 临时密钥
     */
    public static byte[] tmpKey = new byte[16];
    /**
     * 超时时间
     */
    public static int TimeOut=10;

    /**
     * 等待队列最大值
     */
    public static int QueueMaxNum=100;
}
