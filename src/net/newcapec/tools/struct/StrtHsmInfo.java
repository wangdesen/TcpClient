package net.newcapec.tools.struct;

import java.util.Map;

/**
 * 加密设备信息
 *
 */
public class StrtHsmInfo
{	
	/**
	 * 加密机唯一标识ID
	 */
    public int hsmID;
    
	/**
	 * 加密机类型
	 */
    public int hsmCompany;
    
    /**
     * 加密机名称
     */
    public String hsmName;
    
    /**
     * 加密机ip
     */
    public String hsmIP;
    
    /**
     * 加密机端口
     */
    public int hsmPort;
    
    /**
     * 加密机超时时间
     */
    public int hsmTimeout;
    
    /**
     * 扩展参数
     */
    public String hsmExt;

    /**
     * 加密机状态 0 正常 1 不通
     */
    public int hsmStatus;
    
    public static StrtHsmInfo mapToClass(Map<String, Object> map)
    {
    	if(map == null ) return null;
    	if(map.size() == 0 ) return new StrtHsmInfo();
    	
    	StrtHsmInfo tmp = new StrtHsmInfo();
    	
    	tmp.hsmID = Integer.parseInt(map.get("HSMID") + "");
    	tmp.hsmCompany = Integer.parseInt(map.get("HSMCOMPANY") + "");
    	tmp.hsmName = map.get("HSMNAME") + "";
    	tmp.hsmIP = map.get("HSMIP") + "";
    	tmp.hsmPort = Integer.parseInt(map.get("HSMPORT") + "");
    	tmp.hsmTimeout = Integer.parseInt(map.get("HSMTIMEOUT") + "");
    	tmp.hsmExt = map.get("HSMEXT") + "";
    	tmp.hsmStatus = Integer.parseInt(map.get("HSMSTATUS") + "");		
    			
    	return tmp;
    }
    
    public String ToString()
    {
        String strTmp = "";

        strTmp += hsmID;
        strTmp += "|";
        strTmp += hsmCompany;
        strTmp += "|";
        strTmp += hsmIP;
        strTmp += "|";
        strTmp += hsmPort;
        strTmp += "|";
        strTmp += hsmTimeout;
        strTmp += "|";
        strTmp += hsmExt;
        
        return strTmp;
    }
}