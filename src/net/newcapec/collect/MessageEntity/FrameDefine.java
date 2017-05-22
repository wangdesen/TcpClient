package net.newcapec.collect.MessageEntity;

/**
 * Created by ff on 2017/5/15.
 */
public class FrameDefine {
    /**
     * 会话请求
     */
    public static final int SignInReq=0x0001;
    /**
     * 会话应答
     */
    public static final int SignInResp=0x8001;

    /**
     * 业务处理请求
     */
    public static final int BussinessReq=0x0003;
    /**
     * 业务处理应答
     */
    public static final int BussinessResp=0x8003;
}
