package net.newcapec.collect.tcpClient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.newcapec.collect.formatter.DataTran;
import net.newcapec.collect.formatter.Encoder;
import net.newcapec.tools.util.DES3;

import java.util.List;

/**
 * 对象封装
 */
final class MessageEncoder extends MessageToMessageEncoder<TcpMessage> {

    /* (non-Javadoc)
     * @see io.netty.handler.codec.MessageToMessageEncoder#encode(io.netty.channel.ChannelHandlerContext, java.lang.Object, java.util.List)
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, TcpMessage message, List<Object> out) throws Exception {
        if (message == null) {
            return;
        }

        ByteBuf sendBuf = null;
        try {
            byte[] sBuf = new byte[message.getMessage().length + 14];

            byte[] tmp = Encoder.getBytes(sBuf.length, 4);//长度
            System.arraycopy(tmp, 0, sBuf, 0, 4);
            tmp = Encoder.getBytes(message.getMessageID(), 2);//报文标识
            System.arraycopy(tmp, 0, sBuf, 4, 2);
            tmp = Encoder.getBytes(message.getMessageSN(), 4);//报文序号
            System.arraycopy(tmp, 0, sBuf, 6, 4);

            System.arraycopy(message.getMessage(), 0, sBuf, 10, message.getMessage().length);

            if (message.getMessageID() == 0001)
                tmp = DES3.CalcDesMac32_PBOC(TcpParam.primaryKey, sBuf, sBuf.length - 4);
            else
                tmp = DES3.CalcDesMac32_PBOC(TcpParam.tmpKey, sBuf, sBuf.length - 4);

            System.arraycopy(tmp, 0, sBuf, sBuf.length - 4, 4);

            sendBuf = Unpooled.buffer();
            sendBuf.writeBytes(sBuf);

            ctx.write(sendBuf);
        } catch (Exception e) {
            Log.getInstance().write(String.format("发送报文异常,报文标识:%04x,报文:%s,异常:%s", message.getMessageID(),
                    DataTran.byteArrayToHexString(message.getMessage(), 0, message.getMessage().length), MyException.getStackTrace(e)));
        } finally {
            ctx.fireChannelReadComplete();
        }
    }

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelHandlerAdapter#channelReadComplete(io.netty.channel.ChannelHandlerContext)
     */
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            ctx.close();
        } catch (Exception ex) {
        }
    }

}
