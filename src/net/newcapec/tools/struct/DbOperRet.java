package net.newcapec.tools.struct;

import java.util.Map;

/**
 * 加密设备通用接口返回值类
 * 
 * 
 */
public class DbOperRet {
	// 返回值0成功 其他失败
	public int nRet;
	public boolean boolRet;
	public String outMsg;
	public String errMsg;
	//扩展参数1 2 3 
	public String ExtPara1;
	public String ExtPara2;
	public String ExtPara3;
	//定制参数
	public Map<String,Object> map;
	public Map<String, Map<String,Object>> hashTerm;
	
	public DbOperRet() {
		nRet = -1;
		boolRet = false;
		outMsg = "";
		errMsg = "";
		ExtPara1 = "";
		ExtPara2 = "";
		ExtPara3 = "";
		
		map = null;
		hashTerm = null;
	}
}
