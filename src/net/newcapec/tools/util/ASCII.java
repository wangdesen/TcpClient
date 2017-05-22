package net.newcapec.tools.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class ASCII {
	
	public static byte[] GetBytes(String s) {
		
		byte[] tBytes = null;
		
		try {
			tBytes = s.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return tBytes;
	}
	
	public static String GetString(byte[] b) {
		
		String s = null;
		
		s = new String(b,Charset.forName("US-ASCII"));
		 
		return s;
	}
	
}
