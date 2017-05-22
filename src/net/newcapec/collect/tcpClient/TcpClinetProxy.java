package net.newcapec.collect.tcpClient;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import net.newcapec.collect.MessageEntity.*;
import net.newcapec.collect.formatter.BinFormatter;
import net.newcapec.collect.formatter.DataTran;
import net.newcapec.collect.formatter.Encoder;
import net.newcapec.tools.util.DES3;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


/**
 * Tcp客户端接口
 * Created by ff on 2017/5/15.
 */
@ChannelHandler.Sharable
public class TcpClinetProxy extends ChannelHandlerAdapter implements Thread.UncaughtExceptionHandler, Runnable {
    /**
     * 报文序号
     */
    private static int messageSN = 1;
    
    /**
     * 超时次数
     */
    private static int timeOutCount=0;

    /**
     * @return the messageSN
     */
    private static synchronized int getMessageSN() {
        return messageSN++;
    }

    /**
     * private static TcpClinetProxy instance = null;
     * private static final Object obj = new Object();
     * <p>
     * public static TcpClinetProxy getInstance() {
     * if (null == instance) {
     * synchronized (obj) {
     * if (null == instance) {
     * instance = new TcpClinetProxy();
     * }
     * }
     * }
     * return instance;
     * }
     * <p>
     * private TcpClinetProxy() {
     * chs = new ChannelHandler[1];
     * chs[0] = this;
     * }
     */

    private ChannelHandlerContext ctx;
    private ChannelHandler[] chs;
    private BinFormatter bin = new BinFormatter();

    // 结果及同步

    private Map<Integer, SyncResult> syncResultMap = new ConcurrentHashMap<>();

    //签到同步
    private boolean LoginFlag = false;


    /**
     * 启动tcp客户端
     *
     * @param ip      服务器IP
     * @param port    服务器端口
     * @param appID   认证ID
     * @param key     认证密钥 库内密文
     * @param timeOut 超时时间 单位 s
     * @param logPath 日志路径
     */
    public TcpClinetProxy(String ip, int port, int appID, byte[] key, int timeOut,String logPath) {
        try {
            chs = new ChannelHandler[1];
            chs[0] = this;

            TcpParam.IP = ip;
            TcpParam.port = port;
            TcpParam.appID = appID;
            byte[] swtichKey = new byte[]{0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11};
            TcpParam.primaryKey = DES3.decryptMode3DES(swtichKey, key);

            Log.getInstance().start(logPath);

            //签到
            signIn();

            Thread upThread = new Thread(this);
            upThread.setDaemon(true);
            upThread.setName("加密机TCP客户端线程");
            upThread.start();
        } catch (Exception e) {
            Log.getInstance().write("初始化加密机TCP客户端线程时异常：" + MyException.getStackTrace(e));
        }
    }

    /**
     * 启动tcp客户端
     *
     * @param ip      服务器IP
     * @param port    服务器端口
     * @param appID   认证ID
     * @param key     认证密钥
     * @param timeOut 超时时间 单位 s
     * @param logPath 日志路径
     */
    public TcpClinetProxy(String ip, int port, int appID, String key, int timeOut,String logPath) {
        this(ip, port, appID, DataTran.hexStringToByteArray(key), timeOut,logPath);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.
     * Thread, java.lang.Throwable)
     */
    @Override
    public void uncaughtException(Thread arg0, Throwable ex) {
        Log.getInstance().write(String.format("加密机TCP客户端线程未处理异常：", MyException.getStackTrace(ex)));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof TcpMessage))
            return;
        TcpMessage message = (TcpMessage) msg;
        try {
            switch (message.getMessageID()) {
                case FrameDefine.SignInResp: {
                    byte[] tmp = new byte[16];
                    System.arraycopy(message.getMessage(), 27, tmp, 0, 16);
                    tmp = DES3.decryptMode3DES(TcpParam.primaryKey, tmp);
                    System.arraycopy(tmp, 0, message.getMessage(), 27, 16);
                    SignInResp signInResp = bin.deserialize(SignInResp.class, message.getMessage(), 0);
                    if (signInResp.ResultCode != 0x9999) {
                        Log.getInstance().write(String.format("签到失败,服务器返回:%d-%s", signInResp.ResponseCode, ErrorCode.toString(signInResp.ResponseCode)));
                        ctx.close();
                        break;
                    }
                    Log.getInstance().write("签到成功");
                    TcpParam.tmpKey = new byte[16];
                    System.arraycopy(signInResp.TempCheckKey, 0, TcpParam.tmpKey, 0, 16);
                    LoginFlag = true;
                    break;
                }
                case FrameDefine.BussinessResp: {
                    CommonResponse commonResponse = bin.deserialize(CommonResponse.class, message.getMessage(), 8);
                    commonResponse.Message = DataTran.byteArrayToHexString(message.getMessage(), 14, (int) commonResponse.MessageLength / 2);// Decoder.toString(message.getMessage(), 14, commonResponse.MessageLength, "UNICODE");

                    //if(message.getMessageSN()%10==0)
                     //   break;

                    if (syncResultMap.containsKey(message.getMessageSN())) {
                        SyncResult syncResult = syncResultMap.get(message.getMessageSN());
                        try {
                            syncResult.lock.lock();
                            syncResult.commonResponse = commonResponse;
                            syncResult.condition.signal();
                        } finally {
                            syncResult.lock.unlock();
                        }
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        } catch (Exception e) {
            Log.getInstance().write(String.format("解析报文[%04x--%s]时异常：%s", message.getMessageID(),
                    DataTran.byteArrayToHexString(message.getMessage(), 0, message.getMessage().length), MyException.getStackTrace(e)));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx; 
        
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Log.getInstance().write("Tcp连接已关闭");
        this.ctx = null;
        LoginFlag = false;
    }

    /**
     * 签到报文
     */
    private boolean signIn() {
        try {
            TcpClient tClient = new TcpClient(chs);
            tClient.connect();

            LoginFlag = false;
            SignInReq req = new SignInReq();
            req.AppID = TcpParam.appID;
            Random random = new Random();
            req.Random = random.nextLong();

            TcpMessage tcpMessage = new TcpMessage();
            tcpMessage.setMessage(bin.serialize(req));
            tcpMessage.setMessageID(FrameDefine.SignInReq);
            tcpMessage.setMessageSN(getMessageSN());

            ctx.write(tcpMessage);
            ctx.flush();

            Log.getInstance().write(String.format("发送签到请求,客户端唯一标识：%d", TcpParam.appID));

            for (int i = 0; i < TcpParam.TimeOut * 1000 / 100; i++) {
                if (LoginFlag)
                    break;
                Thread.sleep(100);
            }
            if (!LoginFlag) {
                ctx.close();
                Log.getInstance().write("签到超时");
            }
        } catch (Exception e) {
            ctx.close();
            Log.getInstance().write("签到时异常：" + MyException.getStackTrace(e));
        }
        return false;
    }


    /**
     * 检测tcp连接 及 等待队列数量
     */
    private void CheckTcp()  throws Exception
    {
        if (!LoginFlag) {
            Thread.sleep(3 * 1000);
            if (!LoginFlag)
                throw new Exception("服务器未连接，请稍后重试");
        }
        if(syncResultMap.size()>=TcpParam.QueueMaxNum)
            throw new Exception(String.format("等待消息队列大于最大值[%d],请稍后重试",TcpParam.QueueMaxNum));
    }

    /**
     * 验证MAC1计算MAC2
     *
     * @param data 数据
     * @return 返回结果
     */
    public CommonResponse checkMac1AndGetMac(CheckMac1AndGetMac2 data) throws Exception {
        try {
            CheckTcp();

            TcpMessage tcpMessage = new TcpMessage();
            BussinessReq req = new BussinessReq(TcpParam.appID, bin.serialize(data));
            byte[] tmp = bin.serialize(req);
            tcpMessage.setMessage(Encoder.getBytes(DataTran.byteArrayToHexString(tmp, 0, tmp.length), "ascii"));
            tcpMessage.setMessageID(FrameDefine.BussinessReq);
            tcpMessage.setMessageSN(getMessageSN());

            //同步
            SyncResult syncResult = new SyncResult();
            syncResultMap.put(tcpMessage.getMessageSN(), syncResult);

            ctx.write(tcpMessage);
            ctx.flush();
            Log.getInstance().write(String.format("发送验证MAC1计算MAC2请求,客户端唯一标识：%d,MessageSN:%d,请求:%s", TcpParam.appID, tcpMessage.getMessageSN(), data.toString()));

            try {
                syncResult.lock.lock();
                if (syncResult.condition.await(TcpParam.TimeOut, TimeUnit.SECONDS)) {
                    CommonResponse commonResponse = syncResultMap.get(tcpMessage.getMessageSN()).commonResponse;
                    Log.getInstance().write(String.format("接收到验证MAC1计算MAC2应答,客户端唯一标识：%d,MessageSN:%d,应答:%s", TcpParam.appID, tcpMessage.getMessageSN(), commonResponse.toString()));
                    return commonResponse;
                }
                Log.getInstance().write("TCP连接超时！超时次数:" + timeOutCount );
                timeOutCount ++;
            } finally {
                syncResultMap.remove(tcpMessage.getMessageSN());
                syncResult.lock.unlock();
            }
            throw new Exception("验证MAC1计算MAC2调用超时，无返回");
        } catch (Exception e) {
            Log.getInstance().write("验证MAC1计算MAC2时异常：" + MyException.getStackTrace(e));
            throw e;
        }
    }

    /**
     * Tac验证
     * @param data 数据
     * @return 验证结果
     */
    public CommonResponse checkTac(CheckTac data) throws Exception {
        try {
            CheckTcp();

            TcpMessage tcpMessage = new TcpMessage();
            BussinessReq req = new BussinessReq(TcpParam.appID, bin.serialize(data));
            byte[] tmp = bin.serialize(req);
            tcpMessage.setMessage(Encoder.getBytes(DataTran.byteArrayToHexString(tmp, 0, tmp.length), "ascii"));
            tcpMessage.setMessageID(FrameDefine.BussinessReq);
            tcpMessage.setMessageSN(getMessageSN());

            //同步
            SyncResult syncResult = new SyncResult();
            syncResultMap.put(tcpMessage.getMessageSN(), syncResult);

            ctx.write(tcpMessage);
            ctx.flush();
            Log.getInstance().write(String.format("发送Tac验证请求,客户端唯一标识：%d,MessageSN:%d,请求:%s", TcpParam.appID, tcpMessage.getMessageSN(), data.toString()));

            try {
                syncResult.lock.lock();
                if (syncResult.condition.await(TcpParam.TimeOut, TimeUnit.SECONDS)) {
                    CommonResponse commonResponse = syncResultMap.get(tcpMessage.getMessageSN()).commonResponse;
                    Log.getInstance().write(String.format("接收到Tac验证应答,客户端唯一标识：%d,MessageSN:%d,应答:%s", TcpParam.appID, tcpMessage.getMessageSN(), commonResponse.toString()));
                    return commonResponse;
                }
            } finally {
                syncResultMap.remove(tcpMessage.getMessageSN());
                syncResult.lock.unlock();
            }
            throw new Exception("Tac验证调用超时，无返回");
        } catch (Exception e) {
            Log.getInstance().write("Tac验证时异常：" + MyException.getStackTrace(e));
            throw e;
        }
    }

    /*
     * 主线程 检测tcp连接是否存在
     */
    @Override
    public void run() {
        for (; ; ) {
            try {
                if (!LoginFlag) {
                    //签到
                    signIn();
                }
                Thread.sleep(1000 * 1);
            } catch (Exception e) {
                Log.getInstance().write("加密机TCP客户端线程异常：" + e.getMessage() + MyException.getStackTrace(e));
            }
        }
    }
}
