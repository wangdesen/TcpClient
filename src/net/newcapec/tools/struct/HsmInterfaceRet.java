package net.newcapec.tools.struct;

/**
 * 加密设备通用接口返回值类
 * 
 * @author Administrator
 * 
 */
public class HsmInterfaceRet {
	// 返回值0成功 其他失败
	public int nRet;
	public boolean boolRet;
	public byte[] bytRet;
	public String outData;
	public String errMsg;

	public HsmInterfaceRet() {
		nRet = -1;
		boolRet = false;
		bytRet = null;
		outData = "";
		errMsg = "";
	}
}
