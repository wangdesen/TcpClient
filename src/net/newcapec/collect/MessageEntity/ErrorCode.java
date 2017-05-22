package net.newcapec.collect.MessageEntity;

/**
 * Created by ff on 2017/5/15.
 */
public class ErrorCode {
    public static int Success=0x0000;

    public static String toString(int code)
    {
        switch (code)
        {
            case 0x0000:
                return "成功";
            case 0x0001:
                return "不支持的报文类型";
            case 0x0002:
                return "未经验证终端,非法访问,连接将被关闭";
            case 0x0003:
                return "内容值不合法";
            case 0x0004:
                return "非信任IP保留";
            case 0x0005:
                return "服务器忙";
            case 0x0010:
                return "终端无法验证合法性，拒绝连接(数据库无法连接)";
            case 0x0011:
                return "终端不存在（认证ID不存在）";
            case 0x0012:
                return "请求拒绝，密钥不正确（认证码不正确）";
            case 0xFFFF:
                return "未知错误";
            default:
                return String.format("未定义错误：%04x",code);
        }
    }
}
