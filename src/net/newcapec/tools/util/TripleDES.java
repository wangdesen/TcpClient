package net.newcapec.tools.util;


//import java.io.ByteArrayOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.newcapec.tools.util.DataTools;

public class TripleDES {
	//private static final String TDES_Algorithm = "DESede";//表示3DES
	//private static final String DES_Algorithm = "DES/ECB/NoPadding";//表示DES
	//private static final String hexString = "0123456789ABCDEF";

	/**
	 * 标准3des加密
	 * @param buf
	 * @param offset
	 * @param count
	 * @param keys
	 * @return
	 */
	public static byte[] EncryptSingleDes11(byte[] buf, int offset, int count, byte[] keys) {
			
		int num = count;
        if (count % 8 != 0)
        {
            num = count / 8 + count % 8 == 0 ? 0 : 8;
        }
        
        byte[] temp = new byte[num];
        System.arraycopy(buf, offset, temp, 0, count);
        
        //加密
        byte[] bytret = encryptModeDes(keys,temp);
        //byte[] bytret = DES3.encryptMode(keys, temp);
        
		return bytret;
	}
	
	/**
	 * 标准3des解密
	 * @param buf
	 * @param offset
	 * @param count
	 * @param keys
	 * @return
	 */
	public static byte[] DecryptSingleDes11(byte[] buf, int offset, int count, byte[] keys) {
			
		int num = count;
        if (count % 8 != 0)
        {
            num = count / 8 + count % 8 == 0 ? 0 : 8;
        }
        
        byte[] temp = new byte[num];
        System.arraycopy(buf, offset, temp, 0, count);
        
        //解密
        byte[] bytret = decryptModeDes(keys,temp);
        //byte[] bytret = DES3.decryptMode(keys, temp);
        
		return bytret;
	}
	
	/**
	 * 加密
	 */
	private static byte[] encryptModeDes(byte[] keybyte, byte[] src) {
		try {
			// 根据给定的字节数组和算法构造一个密钥
			SecretKey deskey = new SecretKeySpec(keybyte, "DES");
			// 加密
			Cipher c1 = Cipher.getInstance("DES/ECB/NoPadding");
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 */
	private static byte[] decryptModeDes(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, "DES");
			// 解密
			Cipher c1 = Cipher.getInstance("DES/ECB/NoPadding");
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}
	
//	private static byte[] encryptMode3Des(byte[] keybyte, byte[] src) {
//		try {
//			// 根据给定的字节数组和算法构造一个密钥
//			SecretKey deskey = new SecretKeySpec(keybyte, TDES_Algorithm);
//			// 加密
//			Cipher c1 = Cipher.getInstance(TDES_Algorithm);
//			c1.init(Cipher.ENCRYPT_MODE, deskey);
//			return c1.doFinal(src);
//		} catch (java.security.NoSuchAlgorithmException e1) {
//			e1.printStackTrace();
//		} catch (javax.crypto.NoSuchPaddingException e2) {
//			e2.printStackTrace();
//		} catch (java.lang.Exception e3) {
//			e3.printStackTrace();
//		}
//		return null;
//	}

//	private static byte[] decryptMode3Des(byte[] keybyte, byte[] src) {
//		try {
//			// 生成密钥
//			SecretKey deskey = new SecretKeySpec(keybyte, TDES_Algorithm);
//			// 解密
//			Cipher c1 = Cipher.getInstance(TDES_Algorithm);
//			c1.init(Cipher.DECRYPT_MODE, deskey);
//			return c1.doFinal(src);
//		} catch (java.security.NoSuchAlgorithmException e1) {
//			e1.printStackTrace();
//		} catch (javax.crypto.NoSuchPaddingException e2) {
//			e2.printStackTrace();
//		} catch (java.lang.Exception e3) {
//			e3.printStackTrace();
//		}
//		return null;
//	}

	/**
	 * 密钥分散
	 * @param SourceData
	 * @param srcIndex
	 * @param CKKey
	 * @return
	 */
//	private static byte[] GetSubKey(byte[] SourceData, int srcIndex, byte[] CKKey)
//    {
//        int i;
//        byte[] subKey = new byte[16];
//        byte[] data =new byte[8];
//        for(i =0;i<8;i++)
//        {
//            data[i] = SourceData[i + srcIndex];
//        }
//
//        byte[] data1 = encryptMode3Des(CKKey,data);
//
//        DataTools.copyByte(data1,0,subKey,0,8);
//        
//        for(i=0;i<8;i++)
//        {
//            data[i] = (byte)(~SourceData[i + srcIndex]);
//        }
//
//        byte[] data2 = encryptMode3Des(CKKey, data);
//
//        DataTools.copyByte(data2,0,subKey,8,8);
//        return subKey;       
//    }
	
	private static byte[] Exor8Bytes( byte[] Input1,int Input1Index, byte[] Input2,int Input2Index)
    {
        byte i;
        byte[] Output = new byte[8];
        for (i = 0; i < 8; i++)
        {
            Output[i] = (byte)(Input1[i+Input1Index] ^ Input2[i+Input2Index]);
        }
        return Output;
    }
	
	/**
	 * 计算pboc mac
	 * @param bKey
	 * @param MacData
	 * @param count
	 * @return
	 */
	public static byte[] CalcDesMac32_PBOC(byte[] bKey,byte[] MacData,int count)
    {
		 byte[] macIV = new byte[8];
         byte[] bMacKey = new byte[16];
         //byte[] mac = new byte[4];
         int uMod = (count/8 + 1);
         byte[] bData = new byte[uMod * 8];
         System.arraycopy(MacData, 0, bData, 0, count);
         bData[count] = (byte) 0x80;
         System.arraycopy(bKey, 0, bMacKey, 0, 8);
         System.arraycopy(bKey, 0, bMacKey, 8, 8);
         
		//String skey = DataTools.ByteArrayToString(bKey);
		//String smacIV = DataTools.ByteArrayToString(macIV);
		//String sData = DataTools.ByteArrayToString(bData);
		
		byte[] bytOutData = CalcPBOCMac(bKey,macIV,bData,uMod*8);
		//String sOutData = DataTools.ByteArrayToString(bytOutData);
		
		return bytOutData;
    }
	
	public static byte[] CalcPBOCMac( byte[] pvSubKeys,byte[] pvCbcBlock,byte[] pbInputData,int eNBytes)
    {
        byte[] abDataBlock =new byte [8];
        byte[] abCbcBlock =new byte [8];
        byte[] subKey = new byte[8];
        byte[] pbMac32Out = new byte[4];

       //java byte数组默认初始化为0 
        int iBlock, i;

        //	calculating a MAC makes only sense with CBC mode activated
        //	if the caller did not provide storage for a CBC buffer, we use an internal one
        if (pvCbcBlock == null)
        {
            pvCbcBlock = abCbcBlock;
        }

        iBlock = eNBytes/8;
        // if requre pading
        if(iBlock*8 < eNBytes)
        {
	        iBlock ++;
	        pbInputData[eNBytes] = (byte)0x80;               
        }
        
        for( i = 0; i < iBlock; i++ )
        {		    
            abDataBlock = Exor8Bytes(pbInputData,i * 8, pvCbcBlock, 0);

            DataTools.copyByte(pvSubKeys, 0, subKey, 0, 8);
            byte[] tmpData = encryptModeDes(subKey, abDataBlock);		    
            DataTools.copyByte(tmpData,0, pvCbcBlock,0, 8);
            DataTools.copyByte(tmpData, 0, abDataBlock, 0, 8);
        }

        if(pvSubKeys.length == 8){
        	
        }else{
        	DataTools.copyByte(pvSubKeys, 8, subKey, 0, 8);
             byte[] tmpData = decryptModeDes(subKey, abDataBlock);
             DataTools.copyByte(tmpData, 0, abDataBlock, 0, 8);

             DataTools.copyByte(pvSubKeys, 0, subKey, 0, 8);
             tmpData = encryptModeDes(subKey, abDataBlock);
             DataTools.copyByte(tmpData, 0, abDataBlock, 0, 8);
        }

        DataTools.copyByte(abDataBlock,0, pbMac32Out,0, 4);
        return pbMac32Out;
    }
	
//	private static String encode(String str) {
//		// 根据默认编码获取字节数组
//		byte[] bytes = str.getBytes();
//		StringBuilder sb = new StringBuilder(bytes.length * 2);
//
//		// 将字节数组中每个字节拆解成2位16进制整数
//		for (int i = 0; i < bytes.length; i++) {
//			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
//			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
//		}
//		return sb.toString();
//	}

//	private static String decode(String bytes) {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream(
//				bytes.length() / 2);
//		// 将每2位16进制整数组装成一个字节
//		for (int i = 0; i < bytes.length(); i += 2)
//			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
//					.indexOf(bytes.charAt(i + 1))));
//		return new String(baos.toByteArray());
//	}

	/**
	 * 字节转十六进制字符
	 * @param b
	 * @return
	 */
//	private static String byte2hex(byte[] b) {
//		String hs = "";
//		String stmp = "";
//		for (int n = 0; n < b.length; n++) {
//			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
//			if (stmp.length() == 1)
//				hs = hs + "0" + stmp;
//			else
//				hs = hs + stmp;
//			if (n < b.length - 1)
//				hs = hs + ":";
//		}
//		return hs.toUpperCase();
//	}

	

}