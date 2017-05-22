package net.newcapec.collect.tcpClient;


/**
 * TCP 报文
 */
public class TcpMessage {
    /**
     * 报文长度
     */
    private int length;
    /**
     * 报文标识
     */
    private int messageID;
    /**
     * 同步序号
     */
    private int messageSN;
    /**
     * 数据
     */
    private byte[] message;

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the messageID
     */
    public int getMessageID() {
        return messageID;
    }

    /**
     * @param messageID the messageID to set
     */
    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    /**
     * @return the messageSN
     */
    public int getMessageSN() {
        return messageSN;
    }

    /**
     * @param messageSN the messageSN to set
     */
    public void setMessageSN(int messageSN) {
        this.messageSN = messageSN;
    }

    /**
     * @return the message
     */
    public byte[] getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(byte[] message) {
        this.message = message;
    }

    public TcpMessage() {

    }
}

