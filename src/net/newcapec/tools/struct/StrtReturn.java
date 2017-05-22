package net.newcapec.tools.struct;

import java.util.Map;

/**
 * 返回信息类
 * 
 * 
 */
public class StrtReturn {
	
	public boolean bRet = false;
	public String errMsg = "";
	public Map<String,Map<String, Object>> map = null;
	
	public StrtReturn() {
		bRet = false;
		errMsg = "";
		map = null;
	}
}
