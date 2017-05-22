package net.newcapec.tools.util;


/**
 * bcd码到BYTE的相互转换
 * 
 */
public class BcdConvert {

	public static byte[] NumBufToBcdBuf(byte[] numBuf, int len, byte[] bcdBuf) 
	{
		// int bdcLen = (len + 1) / 2;
		// 位数不足时左补零方式填充
		byte data = 0, data1 = 0x55, data2 = 0;
		if ((len & 0x01) == 0x01)
			data1 = 0;

		for (int i = 0, j = 0; i < len; i++) {
			data = numBuf[i];
			if (data >= 0x61) // 'a'
			{
				data2 = (byte) (data - 0x61 + 10);
			} else if (data >= 0x41) // 'A'
			{
				data2 = (byte) (data - 0x41 + 10);
			} else if (data >= 0x30) {
				data2 = (byte) (data - 0x30);
			} else {
				data2 = 0;
			}

			if (data1 == 0x55) {
				data1 = data2;
			} else {
				bcdBuf[j++] = (byte) ((data1 << 4) | data2);
				data1 = 0x55;
			}
		}
		return bcdBuf;
	}

	public static byte[] NumBufToBcdBuf(byte[] numBuf, int beginN, int len, byte[] bcdBuf, int beginB) 
	{
		// int bdcLen = (len + 1) / 2;
		// 位数不足时左补零方式填充
		byte data = 0, data1 = 0x55, data2 = 0;
		if ((len & 0x01) == 0x01)
			data1 = 0;

		for (int i = 0, j = 0; i < len; i++) {
			data = numBuf[beginN + i];
			if (data >= 0x61) // 'a'
			{
				data2 = (byte) (data - 0x61 + 10);
			} else if (data >= 0x41) // 'A'
			{
				data2 = (byte) (data - 0x41 + 10);
			} else if (data >= 0x30) {
				data2 = (byte) (data - 0x30);
			} else {
				data2 = 0;
			}

			if (data1 == 0x55) {
				data1 = data2;
			} else {
				bcdBuf[beginB + j++] = (byte) ((data1 << 4) | data2);
				data1 = 0x55;
			}
		}
		return bcdBuf;
	}

	public static byte[] BcdBufToNumBuf(byte[] bcdBuf, int len, byte[] numBuf) 
	{
		// int numLen = len * 2;
		int data = 0;
		// 位数不足时左补零方式填充
		for (int i = 0, j = 0; i < len; i++) {
			data = (bcdBuf[i] & 0xF0) >> 4;
			if (data >= 0x0A) {
				data = data - 0x0A + 0x41;
			} else {
				data = data + 0x30;
			}
			numBuf[j++] = (byte) data;
			data = (bcdBuf[i] & 0x0F);
			if (data >= 0x0A) {
				data = data - 0x0A + 0x41;
			} else {
				data = data + 0x30;
			}
			numBuf[j++] = (byte) data;
		}
		return numBuf;
	}

	/**
	 * 
	 * @param bcdBuf
	 * @param beginB
	 * @param len
	 * @param numBuf inout
	 * @param beginN
	 * @return
	 */
	public static byte[] BcdBufToNumBuf(byte[] bcdBuf, int beginB, int len, byte[] numBuf, int beginN) 
	{
		// int numLen = len * 2;
		int data = 0;
		// 位数不足时左补零方式填充
		for (int i = 0, j = beginN; i < len; i++) {
			data = (bcdBuf[beginB + i] & 0xF0) >> 4;
			if (data >= 0x0A) {
				data = data - 0x0A + 0x41;
			} else {
				data = data + 0x30;
			}
			numBuf[j++] = (byte) data;
			data = (bcdBuf[beginB + i] & 0x0F);
			if (data >= 0x0A) {
				data = data - 0x0A + 0x41;
			} else {
				data = data + 0x30;
			}
			numBuf[j++] = (byte) data;
		}
		return numBuf;
	}

	public static byte[] NumStringToBcdBuf(String numS, byte[] bcdBuf, int begin) 
	{
		int strLen = numS.length();
		if ((strLen % 2) != 0) {
			DataTools.PadLeft(numS, ' ', strLen + 1);
		}
		byte[] numBuf = DataTools.StringToUTF8(numS);
		byte byteH, byteL;
		for (int i = 0, j = begin; i < strLen; i += 2, j++) {
			if (numBuf[i] >= 0x61) // 'a'
				byteH = (byte) (numBuf[i] - 0x61 + 10);
			else if (numBuf[i] >= 0x41) // 'A'
				byteH = (byte) (numBuf[i] - 0x41 + 10);
			else if (numBuf[i] >= 0x30)
				byteH = (byte) (numBuf[i] - 0x30);
			else
				byteH = 0;

			if (numBuf[i + 1] >= 0x61) // 'a'
				byteL = (byte) (numBuf[i + 1] - 0x61 + 10);
			else if (numBuf[i + 1] >= 0x41) // 'A'
				byteL = (byte) (numBuf[i + 1] - 0x41 + 10);
			else if (numBuf[i + 1] >= 0x30)
				byteL = (byte) (numBuf[i + 1] - 0x30);
			else
				byteL = 0;

			bcdBuf[j] = (byte) (((byteH & 0xF) << 4) | (byteL & 0xF));
		}
		return bcdBuf;
	}

    public static int BcdBufToNumBuf(byte[] bufS, int begin, int byteLen, int num)
    {
        num = 0;
        int i;
        byte tmpByte;
        for (i = 0; i < byteLen; i++)
        {
            tmpByte = (byte)((bufS[begin + i] & 0xF0) >> 4);
            if (tmpByte > 0x9) break;
            
            num = num * 10 + tmpByte;
            tmpByte = (byte)(bufS[begin + i] & 0x0F);
            if (tmpByte > 0x9)
                break;
            else
                num = num * 10 + tmpByte;
        }
        return num;
    }

    /**
     * 0-9999的数据转换成长度为2位的BCD码，例用在和终端交换数据的报文长度描述
     * @param data 需要转换的数据
     * @param bufD 转换后的目的Byte数组
     * @param begin 从第几位填写
     */
    public static byte[] IntToBcdBuf2(int data, byte[] bufD, int begin)
    {
        int dataTmp = data / 100;
        bufD[begin + 0] = (byte)(((dataTmp / 10) << 4) | (dataTmp % 10));
        dataTmp = data % 100;
        bufD[begin + 1] = (byte)(((dataTmp / 10) << 4) | (dataTmp % 10));
        
        return bufD;
    }
    
}
