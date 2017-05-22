package net.newcapec.tools.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class DataTools {

	// Hex help
	private static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1',
			(byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
			(byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
			(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };

	/**
	 * convert a byte arrary to hex string
	 * 
	 * @param raw
	 *            byte arrary
	 * @param len
	 *            lenght of the arrary.
	 * @return hex string.
	 */
	public static String getHexString(byte[] raw, int len) {
		byte[] hex = new byte[2 * len];
		int index = 0;
		int pos = 0;

		for (byte b : raw) {
			if (pos >= len)
				break;

			pos++;
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}

		return new String(hex);
	}

	public static void copyByte(byte[] src, int srcOffset, byte[] dst,
			int dstOffset, int count) {
		if (dstOffset + count > dst.length) {
			throw new RuntimeException("目标字节数组所分配的长度不够");
		}
		if (srcOffset + count > src.length) {
			throw new RuntimeException("源字节数组的长度与要求复制的长度不符");
		}
		for (int i = 0; i < count; i++) {
			dst[dstOffset + i] = src[srcOffset + i];
		}
	}

	public static String readString(byte[] bytes, int offset, int count,
			String charset) {
		try {
			String s = "";
			byte[] b = new byte[count];
			copyByte(bytes, offset, b, 0, count);
			s = new String(b, charset);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将一个char字符转换成一个字节类型
	 * 
	 * @param c
	 * @return
	 */
	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	public static byte[] getAPDUResData(byte[] resAllData) {
		byte[] res = new byte[resAllData.length - 2];
		System.arraycopy(resAllData, 0, res, 0, res.length);
		return res;
	}

	public static byte[] getAPDUResCode(byte[] resAllData) {
		byte[] res = new byte[2];
		System.arraycopy(resAllData, resAllData.length - 2, res, 0, res.length);
		return res;
	}

	public static boolean checkSuccess(byte[] resAllData) {
		byte[] res = new byte[2];
		System.arraycopy(resAllData, resAllData.length - 2, res, 0, res.length);
		return DataTools.getHexString(res, res.length).equals("9000");
	}

	/**
	 * 把16进制字符串转换成字节数组
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	/**
	 * ascii字节数组转换成为十六进制字符串
	 */
	public static String AsciiByteArrayToString(byte[] raw) {
		String s = null;
		
		s = new String(raw,Charset.forName("US-ASCII"));
		
		return s.toUpperCase();
	}
	
	/**
	 * 字节数组转换成为十六进制字符串
	 */
	public static String ByteArrayToString(byte[] raw) {
		byte[] hex = new byte[2 * raw.length];
		int index = 0;
		int pos = 0;

		for (byte b : raw) {
			if (pos >= raw.length)
				break;

			pos++;
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}

		return new String(hex);
	}

	/**
	 * 十六进制字符串转换成为字节数组
	 */
	public static byte[] StringToByteArray(String data) {
		String hex = data.toUpperCase();
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	/**
	 * 十六进制字符串转换成为ascii字节数组
	 */
	public static byte[] StringToAsciiByteArray(String s) {
		byte[] tBytes = null;
		
		try {
			tBytes = s.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return tBytes;
	}

	// 转换成16进制字符串
	public static String bytesToHexString(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + "";
		}
		return hs.toUpperCase();
	}

	// 16进制字符串转换成字节
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		String hexString = "0123456789ABCDEF";
		return (byte) hexString.indexOf(c);
	}
	
	/**
	 * 基于位移的 byte[]转化成int
	 * 
	 * @param byte[] bytes
	 * @return int number
	 */
	public static int ByteArrayToInt(byte[] bytes) {
		int value;
		value = (bytes[0] & 0xFF) | ((bytes[1] << 8) & 0xFF00)
				| ((bytes[2] << 16) & 0xFF0000) | ((bytes[3] << 24) & 0xFF000000);
		return value;
	}
	
	/**
	 * 基于位移的4字节 byte[]转化成long
	 * 
	 * @param byte[] bytes 4
	 * @return int number
	 */
	public static long ByteArrayToLong(byte[] bytes4) {
		long value = 0;
		
		String strOrg = DataTools.ByteArrayToString(bytes4).toUpperCase();
		
		String stemp = "";
		
		stemp = strOrg.substring(0,1);
		value += (long)(ToInt32(stemp,16))*256*256*256*16 ;//16的7次方
		strOrg = strOrg.substring(1);
		
		stemp = strOrg.substring(0,1);
		value += (long)(ToInt32(stemp,16))*256*256*256 ;//16的6次方
		strOrg = strOrg.substring(1);
		
		stemp = strOrg.substring(0,1);
		value += (long)(ToInt32(stemp,16))*256*256 * 16 ;//16的5次方
		strOrg = strOrg.substring(1);
		
		stemp = strOrg.substring(0,1);
		value += (long)(ToInt32(stemp,16))*256*256 ;//16的4次方
		strOrg = strOrg.substring(1);
		
		stemp = strOrg.substring(0,1);
		value += (long)(ToInt32(stemp,16))*256 * 16 ;//16的3次方
		strOrg = strOrg.substring(1);
		
		stemp = strOrg.substring(0,1);
		value += (long)(ToInt32(stemp,16))*256 ;//16的2次方
		strOrg = strOrg.substring(1);
		
		stemp = strOrg.substring(0,1);
		value += (long)(ToInt32(stemp,16))*16 ;//16的1次方
		strOrg = strOrg.substring(1);
		
		stemp = strOrg.substring(0,1);
		value += (long)(ToInt32(stemp,16));//16的0次方
		
		return value;
	}
	
	/*
	 * byte[]转换为int，大端
	 */
	public static int ByteArrayToInt_bigEndian(byte[] bytes) {
		int value;
		value = (bytes[3] & 0xFF) | ((bytes[2] << 8) & 0xFF00)
				| ((bytes[1] << 16) & 0xFF0000) | ((bytes[0] << 24) & 0xFF000000);
		return value;
	}

	/*
	 * byte[]转换为int，大端
	 */
	public static short ByteArrayToShort_bigEndian(byte[] bytes) {
		short value;
		value = (short) ((bytes[1] & 0xFF) | ((bytes[0] << 8) & 0xFF00));
		return value;
	}

	// int 转字节数组
	public static byte[] IntToByteArray_bigEndian(int number) {
		byte[] abyte = new byte[4];
		// "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
		abyte[3] = (byte) (0xff & number);
		// ">>"右移位，若为正数则高位补0，若为负数则高位补1
		abyte[2] = (byte) ((0xff00 & number) >> 8);
		abyte[1] = (byte) ((0xff0000 & number) >> 16);
		abyte[0] = (byte) ((0xff000000 & number) >> 24);

		return abyte;
	}

	/**
	 * int 转字节数组
	 * @param number
	 * @return
	 */
	public static byte[] IntToByteArray(int number) {
		byte[] abyte = new byte[4];
		// "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
		abyte[0] = (byte) (0xff & number);
		// ">>"右移位，若为正数则高位补0，若为负数则高位补1
		abyte[1] = (byte) ((0xff00 & number) >> 8);
		abyte[2] = (byte) ((0xff0000 & number) >> 16);
		abyte[3] = (byte) ((0xff000000 & number) >> 24);

		return abyte;
	}

	
	/**
	 * long 转字节数组
	 * @param number
	 * @return
	 */
	public static byte[] LongToByteArray(long x) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		
		buffer.putLong(0, x);  
	    
		return buffer.array();  
	}
	
	// short 转字节数组
	public static byte[] ShortToByteArray_bigEndian(int number) {
		byte[] abyte = new byte[2];
		// "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
		abyte[1] = (byte) (0xff & number);
		// ">>"右移位，若为正数则高位补0，若为负数则高位补1
		abyte[0] = (byte) ((0xff00 & number) >> 8);

		return abyte;
	}

	// short 转字节数组
	public static byte[] ShortToByteArray(int number) {
		byte[] abyte = new byte[2];
		// "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
		abyte[0] = (byte) (0xff & number);
		// ">>"右移位，若为正数则高位补0，若为负数则高位补1
		abyte[1] = (byte) ((0xff00 & number) >> 8);

		return abyte;
	}

	/**
	 * 基于arraycopy合并两个byte[] 数组
	 * 
	 * @param byte[] bytes1
	 * @param byte[] bytes2
	 * @return byte[] bytes3
	 */
	public byte[] CombineTowBytes(byte[] bytes1, byte[] bytes2) {
		byte[] bytes3 = new byte[bytes1.length + bytes2.length];
		System.arraycopy(bytes1, 0, bytes3, 0, bytes1.length);
		System.arraycopy(bytes2, 0, bytes3, bytes1.length, bytes2.length);
		return bytes3;
	}
	
	/**
	 * 字符串转数字
	 * @param s 字符串
	 * @param radix 进制
	 * @return
	 */
	public static int ToInt32(String s,int radix){
		int n = 0;
		if(radix == 10){
			n = Integer.parseInt(s,radix);
		}else if(radix == 16){
			n = Integer.parseInt(s,radix);
		}else{
			
		}
		return n;
	}
	
	/**
	 * 字符串转数字
	 * @param s 字符串
	 * @param radix 进制
	 * @return
	 */
	public static int ToInt32(String s){
		int n = 0;
	
		n = Integer.parseInt(s,10);
	
		return n;
	}
	
	/**
	 * 字符串或汉字 转unicode字符
	 * @param str
	 * @return
	 */
	public static String StringToUnicode(String str){
		//JAVA中的UTF-16LE 相当于C#中的Encoding.Unicode.Getbytes
		byte[] bytes = str.getBytes(Charset.forName("UTF-16LE"));
		return ByteArrayToString(bytes);
	}
	
	/**
	 * 字符串或汉字 转unicode字符 转 字符串或汉字
	 * @param str
	 * @return
	 */
	public static String UnicodeToString(String str){
		//JAVA中的UTF-16LE 相当于C#中的Encoding.Unicode.Getbytes
		byte[] bytes = DataTools.StringToByteArray(str);
		str = new String(bytes,Charset.forName("UTF-16LE"));
		return str;
	}
	
	/**
	 * 字符串或汉字 转UTF8字节
	 * @param str
	 * @return
	 */
	public static byte[] StringToUTF8(String str){
		//JAVA中的UTF-8 相当于C#中的Encoding.UTF8.Getbytes
		byte[] bytes = str.getBytes(Charset.forName("UTF-8"));
		return bytes;
	}
	
	/**
	 * 字符串或汉字 转UTF8字符 转 字符串或汉字
	 * @param str
	 * @return
	 */
	public static String UTF8ToString(byte[] UTF8bytes){
		//JAVA中的UTF-8 相当于C#中的Encoding.UTF8.Getbytes
		String str = new String(UTF8bytes,Charset.forName("UTF-8"));
		return str;
	}
	
	/**
	 * 字符串或汉字转 哈希字节值 16字节
	 * @param str
	 * @return
	 */
	public static String StringToMd5HashValue(String str){
		try{
			//JAVA中的UTF-16LE 相当于C#中的Encoding.Unicode.Getbytes
			byte[] by = str.getBytes("UTF-16LE");  
	        MessageDigest det = MessageDigest.getInstance("MD5");  
	        byte[] bb = det.digest(by); 
	        return DataTools.ByteArrayToString(bb);
		}catch(Exception ex){
			return "";
		}
	}
	
	/**
	 * 字符串或汉字转 哈希字节值 16字节
	 * @param str
	 * @return
	 */
	public static Map<String,Object> StringToMd5HashValue_Map(String str){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("BOOLRET", "FALSE");
		map.put("MSG", "");
		
		try{
			//JAVA中的UTF-16LE 相当于C#中的Encoding.Unicode.Getbytes
			byte[] by = str.getBytes("UTF-16LE");  
	        MessageDigest det = MessageDigest.getInstance("MD5");  
	        byte[] bb = det.digest(by); 
	        
	        map.put("BOOLRET", "TRUE");
	        map.put("MSG", DataTools.ByteArrayToString(bb));
			return map;			
		}catch(Exception ex){
			map.put("MSG", "计算HASH数据异常," + ex.getMessage());
			return map;
		}
	}
	
	/**
	 * 字符串或汉字转 哈希字节值 16字节 GBK编码集
	 * @param str
	 * @return
	 */
	public static String StringToMd5HashValue_GBK(String str){
		try{
			//JAVA中的UTF-16LE 相当于C#中的Encoding.Default.Getbytes
			byte[] by = str.getBytes("GBK");  
	        MessageDigest det = MessageDigest.getInstance("MD5");  
	        byte[] bb = det.digest(by); 
	        return DataTools.ByteArrayToString(bb);
		}catch(Exception ex){
			return "";
		}
	}
	
	public static String PadRight(String t, char c, int n){
		StringBuilder sb = new StringBuilder();
		sb.append(t);
		int g = n - t.length(); 
		for(int i=0;i<g;i++) sb.append(c); 
		
		return sb.toString();
	}

	public static String PadLeft(String t, char c, int n){
		StringBuilder sb = new StringBuilder();
		sb.append(t);
		int g = n - t.length(); 
		for(int i=0;i<g;i++) sb.insert(0,c); 
		
		return sb.toString();
	}
	
	/**  
     * byte字节转二进制串
     * @param bytes 1字节
     * @return 二进制字符串  8位
     */  
    public static String ByteToBinaryStr(byte bytes)
    {   
    	String outstr = "";
    	String S = "0123456789ABCDEF";
    	String[] ArrBinary = {"0000","0001","0010","0011",
			    			  "0100","0101","0110","0111",
			    			  "1000","1001","1010","1011",
			    			  "1100","1101","1110","1111",
			    			 };
    	String strHex = String.format("%02X", bytes);
    	for(int i = 0;i < strHex.length();i++)
    	{
    		String strTemp = strHex.substring(i, i + 1);
    		for(int j = 0;j < S.length();j++)
    		{
    			if(strTemp.equals(S.substring(j,j + 1)))
    			{
    				outstr += ArrBinary[j];
    				break;
    			}
    		}
    	}
 
        return outstr;   
    }   
    
    /**  
     * 二进制串转16进制串
     * @param BinaryStr 8位
     * @return 1字节 
     */ 
    public static String BinaryStrToHexStr(String BinaryStr)
    {   
    	String outstr = "";
    	String S = "0123456789ABCDEF";
    	String[] ArrBinary = {"0000","0001","0010","0011",
			    			  "0100","0101","0110","0111",
			    			  "1000","1001","1010","1011",
			    			  "1100","1101","1110","1111",
			    			 };
    	String strHex = BinaryStr;
    	for(int i = 0;i < strHex.length()/4;i++)
    	{
    		String strTemp = strHex.substring(4 * i, 4 * i + 4);
    		for(int j = 0;j < ArrBinary.length;j++)
    		{
    			if(strTemp.equals(ArrBinary[j]))
    			{
    				outstr += S.substring(j,j + 1);
    				break;
    			}
    		}
    	}
    	
    	return outstr;  
    }   

    
	/**
	 * JAVA版实现byte数据右移操作  
	 * 注意：用>>或者>>>时的结果和C#都不一致，必须用该方法
	 * @param bytes 有符号类型 -128~127
	 * @param n 移动次数 n<16
	 * @return 返回有符号类型 
	 */
	public static byte MoveRight(byte bytes,int n)
	{
		//处理思路：转换成2进制进行操作 高位强制为0
		//Integer.toBinaryString(127); 
		//1111111-->01111111-->00000000 00000000 00000000 01111111
		//Integer.toBinaryString(-128);//11111111111111111111111110000000
		
		if(n == 0)
		{
			return bytes;
		}
		
		//8位
		String ss = ByteToBinaryStr(bytes);	
		
		//左边补足32位 防止右移截取异常 补0
		ss = DataTools.PadLeft(ss,'0',32);
		//右移n位
		ss = ss.substring(0,ss.length() - n);
		//取后8位
		ss = ss.substring(ss.length() - 8,ss.length());
		//强制高位为0 无符号
		ss = "0" + ss.substring(1);	
		
		byte b = (byte)Integer.parseInt(ss, 2);//2进制转10进制
		
		return b;
	}
	
	/**
	 * JAVA版实现byte数据左移操作   直接用<<也行 结果和C#是一致的
	 * @param bytes 有符号类型 -128~127
	 * @param n 移动次数 n<16
	 * @return 返回有符号类型
	 */
	public static byte MoveLeft(byte bytes,int n)
	{
		//处理思路：转换成2进制进行操作
		//Integer.toBinaryString(127); 
		//1111111-->01111111-->01111111 00000000 00000000 00000000
		//Integer.toBinaryString(-128);//11111111111111111111111110000000
		
		if(n == 0)
		{
			return bytes;
		}
		
		//8位
		String ss = ByteToBinaryStr(bytes);	
		
		//左移n位 在右边补n个0
		ss = DataTools.PadRight(ss,'0',ss.length() + n);
		//取后8位
		ss = ss.substring(ss.length() - 8,ss.length());

		byte b = (byte)Integer.parseInt(ss, 2);//2进制转10进制
		
		return b;
	}
	/**
	 * byte数组合并
	 * @param byte_1
	 * @param byte_2
	 * @return
	 */
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){  
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];  
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);  
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);  
        return byte_3;  
    }
	
}
