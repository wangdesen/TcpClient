package net.newcapec.collect.tcpClient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import net.newcapec.collect.formatter.DataTran;
import net.newcapec.collect.formatter.Decoder;
import net.newcapec.tools.util.DES3;

/**
 * 报文基本解析类
 */
class MessageDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * @param maxFrameLength      最大报文长度
     * @param lengthFieldOffset   长度域起始标识
     * @param lengthFieldLength   长度域的长度
     * @param lengthAdjustment    长度域修正
     * @param initialBytesToStrip 长度域忽略字节
     */
    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
                          int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.netty.handler.codec.LengthFieldBasedFrameDecoder#decode(io.netty.
     * channel.ChannelHandlerContext, io.netty.buffer.ByteBuf)
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) {
        byte[] data = null;
        try {
            ByteBuf frame = (ByteBuf) super.decode(ctx, in);
            if (frame == null)
                return null;

            data = new byte[frame.readableBytes()];
            frame.readBytes(data);
            frame.release();

            int len = Decoder.toInt32(data, 0, 4);
            if (len != data.length) {
                in.clear();
                return null;
            }
            TcpMessage tcpMessage = new TcpMessage();
            tcpMessage.setLength(len);//长度
            tcpMessage.setMessageID(Decoder.toInt32(data, 4, 2));//报文标识
            tcpMessage.setMessageSN(Decoder.toInt32(data, 6, 4));//报文序号

            //报文mac验证
            if (tcpMessage.getMessageID() == 0x8001) {
                if (!DES3.compareMac(data, len - 4, TcpParam.primaryKey, data, len - 4)) {
                    in.clear();
                    Log.getInstance().write(String.format("接收报文Mac验证错误,报文标识:%04x,报文:%s", tcpMessage.getMessageID(), DataTran.byteArrayToHexString(data, 0, data.length)));
                    return null;
                }
            } else {
                if (!DES3.compareMac(data, len - 4, TcpParam.tmpKey, data, len - 4)) {
                    in.clear();
                    Log.getInstance().write(String.format("接收报文Mac验证错误,报文标识:%04x,报文:%s", tcpMessage.getMessageID(), DataTran.byteArrayToHexString(data, 0, data.length)));
                    return null;
                }
            }

            //消息
            byte[] message = new byte[len - 14];
            System.arraycopy(data, 10, message, 0, message.length);

            tcpMessage.setMessage(DataTran.hexStringToByteArray(new String(message,"ascii")));

            return tcpMessage;
        } catch (Exception e) {
            if (null != data) {
                Log.getInstance().write(String.format("解析接收报文异常,报文%s,异常:%s", DataTran.byteArrayToHexString(data, 0, data.length), MyException.getStackTrace(e)));
            } else {
                Log.getInstance().write(String.format("解析接收报文异常,异常:%s", MyException.getStackTrace(e)));
            }
            in.clear();
            return null;
        }
    }
}
