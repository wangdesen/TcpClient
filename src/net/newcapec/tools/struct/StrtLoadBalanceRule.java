package net.newcapec.tools.struct;

import java.util.Map;


/**
 * 加密设备负载均衡规则
 *
 */
public class StrtLoadBalanceRule
{	
    /**
     * 规则Id
     */
    public int RuleCode;

    /**
     * 规则名称
     */
    public String RuleName;

    /**
     * 规则
     */
    public String RuleValue;

    /**
     * 规则上下文
     */
    public String RuleContent;

    public static StrtLoadBalanceRule mapToClass(Map<String, Object> map)
    {
    	StrtLoadBalanceRule tmp = new StrtLoadBalanceRule();
    	
    	tmp.RuleCode = Integer.parseInt(map.get("RULECODE") + "");
    	tmp.RuleName = map.get("RULENAME") + "";
    	tmp.RuleValue = map.get("RULEVALUE") + "";
    	tmp.RuleContent = map.get("RULECONTENT") + "";	
    			
    	return tmp;
    }
    
}