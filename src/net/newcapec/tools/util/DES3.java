package net.newcapec.tools.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 标准3DES算法实现
 */
public class DES3 {
	private static final String Algorithm = "DESede";// 定义加密算法,可用DES,DESede,Blowfish

	// keybyte 必须是24字节 如果是16字节，内部自动处理 3DES加密
	// 数据 8字节的倍数
	public static byte[] encryptMode3DES(byte[] keybyte, byte[] src) {
		try {
			if (keybyte.length == 16) {
				byte[] k = new byte[24];
				System.arraycopy(keybyte, 0, k, 0, 16);
				System.arraycopy(keybyte, 0, k, 16, 8);

				keybyte = new byte[24];
				System.arraycopy(k, 0, keybyte, 0, 24);
			}

			// 根据给定的字节数组和算法构造一个密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance("TripleDES/ECB/NoPadding");

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

	// keybyte 必须是24字节 如果是16字节，内部自动处理 3DES解密
	// 数据 8字节的倍数
	public static byte[] decryptMode3DES(byte[] keybyte, byte[] src) {
		try {
			if (keybyte.length == 16) {
				byte[] k = new byte[24];
				System.arraycopy(keybyte, 0, k, 0, 16);
				System.arraycopy(keybyte, 0, k, 16, 8);

				keybyte = new byte[24];
				System.arraycopy(k, 0, keybyte, 0, 24);
			}

			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance("TripleDES/ECB/NoPadding");

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

	/**
	 * 单DES加密
	 * @param keybyte
	 * @param src 数据 8字节的倍数
	 * @return
	 */
	private static byte[] encryptMode1DES(byte[] keybyte, byte[] src) {
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
	 * 单DES解密
	 * @param keybyte
	 * @param src 数据 8字节的倍数
	 * @return
	 */
	private static byte[] decryptMode1DES(byte[] keybyte, byte[] src) {
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
	
	/**
	 * 密钥散列 根据分散次数分散多次
	 * 
	 * @param SourceData
	 * @param srcIndex
	 * @param CKKey
	 * @return
	 */
	public static byte[] GetSubKey_Mult(byte[] SourceData, int srcIndex, byte[] CKKey) {
		int iKeyDvsNum = 0;
		if (SourceData != null) {
			iKeyDvsNum = SourceData.length / 8;
		}

		byte[] subKey1 = new byte[16];
		byte[] subKey2 = new byte[16];
		byte[] subKey3 = new byte[16];
		byte[] subString1 = new byte[8];
		byte[] subString2 = new byte[8];
		byte[] subString3 = new byte[8];

		if (iKeyDvsNum == 1) {
			DataTools.copyByte(SourceData, 0, subString1, 0, 8);
			subKey1 = GetSubKey(subString1, srcIndex, CKKey);
		} else if (iKeyDvsNum == 2) {
			DataTools.copyByte(SourceData, 0, subString1, 0, 8);
			DataTools.copyByte(SourceData, 8, subString2, 0, 8);
			subKey1 = GetSubKey(subString1, srcIndex, CKKey);
			subKey2 = GetSubKey(subString2, srcIndex, subKey1);
			DataTools.copyByte(subKey2, 0, subKey1, 0, 16);
		} else if (iKeyDvsNum == 3) {
			DataTools.copyByte(SourceData, 0, subString1, 0, 8);
			DataTools.copyByte(SourceData, 8, subString2, 0, 8);
			DataTools.copyByte(SourceData, 16, subString3, 0, 8);
			subKey1 = GetSubKey(subString1, srcIndex, CKKey);
			subKey2 = GetSubKey(subString2, srcIndex, subKey1);
			subKey3 = GetSubKey(subString3, srcIndex, subKey2);
			DataTools.copyByte(subKey3, 0, subKey1, 0, 16);
		} else {
			// 不分散
			DataTools.copyByte(CKKey, 0, subKey1, 0, 16);
		}

		return subKey1;
	}

	/**
	 * 密钥散列
	 * 
	 * @param SourceData
	 * @param srcIndex
	 * @param CKKey
	 * @return
	 */
	public static byte[] GetSubKey(byte[] SourceData, int srcIndex, byte[] CKKey) {
		int i;
		byte[] subKey = new byte[16];
		byte[] data = new byte[8];
		for (i = 0; i < 8; i++) {
			data[i] = SourceData[i + srcIndex];
		}

		byte[] data1 = encryptMode3DES(CKKey, data);

		DataTools.copyByte(data1, 0, subKey, 0, 8);

		for (i = 0; i < 8; i++) {
			data[i] = (byte) (~SourceData[i + srcIndex]);
		}

		byte[] data2 = encryptMode3DES(CKKey, data);

		DataTools.copyByte(data2, 0, subKey, 8, 8);
		return subKey;
	}

	/**
	 * 计算4字节mac
	 * 
	 * @param pvSubKeys
	 * @param pvCbcBlock
	 * @param pbInputData
	 * @param eNBytes
	 * @param macLen
	 *            输出mac结果的字节长度 传4或8
	 * @return
	 */
	public static byte[] CalcPBOCMac(byte[] pvSubKeys, byte[] pvCbcBlock,byte[] pbInputData, int eNBytes, int macLen) {
		byte[] abDataBlock = new byte[8];
		byte[] abCbcBlock = new byte[8];
		byte[] subKey = new byte[8];
		byte[] pbMac32Out = new byte[macLen];

		// java byte数组默认初始化为0
		int iBlock, i;

		// calculating a MAC makes only sense with CBC mode activated
		// if the caller did not provide storage for a CBC buffer, we use an
		// internal one
		if (pvCbcBlock == null) {
			pvCbcBlock = abCbcBlock;
		}

		iBlock = eNBytes / 8;
		// if requre pading
		if (iBlock * 8 < eNBytes) {
			iBlock++;
			pbInputData[eNBytes] = (byte) 0x80;
		}

		for (i = 0; i < iBlock; i++) {
			abDataBlock = Exor8Bytes(pbInputData, i * 8, pvCbcBlock, 0);
			DataTools.copyByte(pvSubKeys, 0, subKey, 0, 8);
			byte[] tmpData = encryptMode1DES(subKey, abDataBlock);	
			DataTools.copyByte(tmpData, 0, pvCbcBlock, 0, 8);
			DataTools.copyByte(tmpData, 0, abDataBlock, 0, 8);
		}

		if (pvSubKeys.length == 8) {

		} else {
			DataTools.copyByte(pvSubKeys, 8, subKey, 0, 8);
			byte[] tmpData = decryptMode1DES(subKey, abDataBlock);
			DataTools.copyByte(tmpData, 0, abDataBlock, 0, 8);

			DataTools.copyByte(pvSubKeys, 0, subKey, 0, 8);
			tmpData = encryptMode1DES(subKey, abDataBlock);
			DataTools.copyByte(tmpData, 0, abDataBlock, 0, 8);
		}

		DataTools.copyByte(abDataBlock, 0, pbMac32Out, 0, macLen);
		return pbMac32Out;
	}

	private static byte[] Exor8Bytes(byte[] Input1, int Input1Index,byte[] Input2, int Input2Index) {
		byte i;
		byte[] Output = new byte[8];
		for (i = 0; i < 8; i++) {
			Output[i] = (byte) (Input1[i + Input1Index] ^ Input2[i + Input2Index]);
		}
		return Output;
	}

	/**
	 * 计算pboc mac
	 * 
	 * @param bKey
	 * @param MacData
	 * @param count
	 * @return
	 */
	public static byte[] CalcDesMac32_PBOC(byte[] bKey, byte[] MacData,int count) {
		byte[] macIV = new byte[8];
		//byte[] bMacKey = new byte[16];
		int uMod = (count / 8 + 1);
		byte[] bData = new byte[uMod * 8];

		System.arraycopy(MacData, 0, bData, 0, count);
		bData[count] = (byte) 0x80;
		//System.arraycopy(bKey, 0, bMacKey, 0, 8);
		//System.arraycopy(bKey, 0, bMacKey, 8, 8);

		byte[] bytOutData = CalcPBOCMac(bKey, macIV, bData, uMod * 8, 4);

		return bytOutData;
	}

    /**
     * 验证MAC是否通过
     *
     * @param buffer         输入数据流
     * @param count          输入数据流个数
     * @param key            加密密钥
     * @param destinationMAC 目标MA
     * @param destMacOffset  目标MAC 偏移量
     * @return 校验结果
     */
    public static boolean compareMac(byte[] buffer, int count, byte[] key, byte[] destinationMAC, int destMacOffset)
            throws Exception {
        byte[] mac = CalcDesMac32_PBOC(key, buffer, count);
        for (int i = 0; i < 4; i++) {
            if (mac[i] != destinationMAC[i + destMacOffset]) {
                return false;
            }
        }

        return true;
    }

}
