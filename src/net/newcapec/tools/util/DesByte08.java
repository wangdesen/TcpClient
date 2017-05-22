package net.newcapec.tools.util;

import net.newcapec.tools.struct.HsmInterfaceRet;

/**
 * 专用算法
 * 注意：**********************************************************
 * 用>>或者>>>时的结果和C#的>>都不一致，必须用DataTools.MoveRight方法
 * 左移是一致的
 */
public class DesByte08 {

	// 私有静态变量====================================================================
	private class eDesCRPTY {
		public final static int DES_ENCRYPT = 1;// 加密
		public final static int DES_DECRYPT = 2;// 解密
	}

	// S-function table 8,64
	private static byte[][] SFun_Tab = { // S-1
			{ 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, 0, 15, 7,
					4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14, 8,
					13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4,
					9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 },
			// S-2
			{ 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, 3, 13, 4,
					7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7, 11,
					10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3,
					15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 },
			// S-3
			{ 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, 13, 7, 0,
					9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4, 9, 8,
					15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9,
					8, 7, 4, 15, 14, 3, 11, 5, 2, 12 },
			// S-4
			{ 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, 13, 8, 11,
					5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9, 0, 12,
					11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1,
					13, 8, 9, 4, 5, 11, 12, 7, 2, 14 },
			// S-5
			{ 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, 14, 11, 2,
					12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1, 11, 10,
					13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1, 14,
					2, 13, 6, 15, 0, 9, 10, 4, 5, 3 },
			// S-6
			{ 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, 10, 15, 4,
					2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14, 15, 5, 2,
					8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9, 5, 15,
					10, 11, 14, 1, 7, 6, 0, 8, 13 },
			// S-7
			{ 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, 13, 0, 11,
					7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11, 13,
					12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4,
					10, 7, 9, 5, 0, 15, 14, 2, 3, 12 },
			// S-8
			{ 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, 1, 15, 13,
					8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4, 1, 9,
					12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10,
					8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } };

	
	//私有静态方法====================================================================
	
	/**
	 * 
	 * @param s 输入输出
	 * @return
	 */
	private static byte[] DoFirstChange(byte[] s)   // change s
     {
         int i, j;
         byte[] tmp = { 0, 0, 0, 0, 0, 0, 0, 0 };
         for (i = 0; i < 8; ++i)
         {
             for (j = 0; j < 8; ++j)
             {            	 
                 tmp[7 - j] |= (byte)(((DataTools.MoveRight(s[i],j)) & 0x01) << i);                 
             }
         }
         for (i = 0; i < 4; ++i)
         {
             s[i] = tmp[2 * i + 1];
             s[i + 4] = tmp[2 * i];
         }
         return s;
     }
	 
	/**
	 * 
	 * @param s 输入输出
	 * @return
	 */
	private static byte[] DoLastChange(byte[] s)
    {
        int i, j;
        byte[] tmp = { 0, 0, 0, 0, 0, 0, 0, 0 };
        for (i = 0; i < 8; ++i)
        {
            for (j = 0; j < 4; ++j)
            {
                tmp[i] |= (byte)(DataTools.MoveRight((byte)((s[j] << (7 - i)) & 0x80) , (2 * j + 1)));
                tmp[i] |= (byte)(DataTools.MoveRight((byte)((s[j + 4] << (7 - i)) & 0x80) , (2 * j)) );
            }
        }
        DataTools.copyByte(tmp, 0, s, 0, 8);
        return s;
    }
	
	/**
	 * 
	 * @param keyS 输入
	 * @return
	 */
    private static byte[] MakeKey(byte[] keyS)   // Key ->C, D
    {
        int i, j;
        byte[] tmp = { 0, 0, 0, 0, 0, 0, 0, 0 };
        byte[] keyD = { 0, 0, 0, 0, 0, 0, 0, 0 };
        //D = C + 4;
        for (i = 0; i < 8; ++i)
        {
            for (j = 0; j < 8; ++j)
            {
                tmp[7 - j] |= (byte)(DataTools.MoveRight((byte)(keyS[i] & (0x01 << j)) , j) << i);
            }
        }
        for (i = 0; i < 4; i++)
        {
            keyD[i] = tmp[i];
            keyD[i + 4] = tmp[6 - i];
        }
        keyD[3] &= 0xf0;
        keyD[3 + 4] = (byte)((keyD[3 + 4] & 0x0f) << 4);
        return keyD;
    }
    
    /**
     * 
     * @param s 输入输出
     * @param n
     * @return
     */
    public static byte[] DoLeft(byte[] s, int n)  // left shift s n bits
    {
        byte L, t;
        int i;

        L = (byte)(0xff << (8 - n));
        t = (byte)(DataTools.MoveRight((byte)(s[0] & L) , 4));
        s[3] |= t;
        for (i = 0; i < 3; i++)
        {
            s[i] <<= n;
            t = (byte)(DataTools.MoveRight((byte)(s[i + 1] & L) , (8 - n)));
            s[i] |= t;
        }
        s[3] = (byte)(s[3] << n);
        return s;
    }
    
    /**
     * 
     * @param s 输入输出
     * @param n
     * @return
     */
    private static byte[] MyrShift(byte[] s)    // right shift s 1 bits
    {
        byte l = (byte)(s[0] & 1);
        byte l1 = (byte)(s[1] & 1);
        s[0] = DataTools.MoveRight(s[0],1);
        s[1] = DataTools.MoveRight(s[1],1);
        s[1] |= (byte)(l << 7);
        l = (byte)(s[2] & 1);
        s[2] = DataTools.MoveRight(s[2],1);
        s[2] |= (byte)(l1 << 7);
        s[3] = DataTools.MoveRight(s[3],1);
        s[3] |= (byte)(l << 7);
        if ((s[3] & 0xf) != 0)
        {
            s[0] |= 0x80;
            s[3] &= 0xf0;
        }
        return s;
    }
    
    /**
     * 
     * @param s 输入输出
     * @param n
     * @return
     */
    private static byte[] DoRight(byte[] s, int n)    // right shift s n bits
    {
    	byte[] borg = new byte[s.length];
    	DataTools.copyByte(s, 0, borg, 0, s.length);
    	
        for (int i = 0; i < n; ++i)
        {
            byte[] btemp = MyrShift(borg);
            //清空borg 重新赋值
            borg = new byte[s.length]; 
            DataTools.copyByte(btemp, 0, borg, 0, btemp.length);
        }
        return borg;
    }
	
    /**
     * 
     * @param s 输入
     * @param r 输出
     * @return
     */
    private static byte[] EExpand(byte[] s, byte[] r)
    {
        r[0] = (byte)(
        		((s[3] & 0x01) << 7) | 
        		DataTools.MoveRight((byte)(s[0] & 0xf8) , 1) | 
        		DataTools.MoveRight((byte)(s[0] & 0x18) , 3)
        		);
        r[1] = (byte)(
        		((s[0] & 0x07) << 5) | 
        		((s[0] & 0x01) << 3) |
        		DataTools.MoveRight((byte)(s[1] & 0x80) , 3) | 
        		DataTools.MoveRight((byte)(s[1] & 0xe0) , 5)
        		);
        r[2] = (byte)(
        		((s[1] & 0x18) << 3) | 
        		((s[1] & 0x1f) << 1) | 
        		DataTools.MoveRight((byte)(s[2] & 0x80) , 7)
        		);
        r[3] = (byte)(
        		((s[1] & 0x01) << 7) | 
        		DataTools.MoveRight((byte)(s[2] & 0xf8) , 1) | 
        		DataTools.MoveRight((byte)(s[2] & 0x18) , 3)
        		);
        r[4] = (byte)(
        		((s[2] & 0x07) << 5) | 
        		((s[2] & 0x01) << 3) |
        		DataTools.MoveRight((byte)(s[3] & 0x80) , 3) | 
        		DataTools.MoveRight((byte)(s[3] & 0xe0) , 5)
        		);
        r[5] = (byte)(
        		((s[3] & 0x18) << 3) | 
        		((s[3] & 0x1f) << 1) | 
        		DataTools.MoveRight((byte)(s[0] & 0x80) , 7)
        		);
        
        return r;
    }
    
    /**
     * 
     * @param s 输入输出
     * @return
     */
    private static byte[] PChang(byte[] s)
    {
        int i;
        byte[] t = new byte[4];
        t[0] = (byte)(
        		((s[1] & 0x01) << 7) | 
        		((s[0] & 0x02) << 5) | 
        		((s[2] & 0x10) << 1) |
        		((s[2] & 0x08) << 1) | 
        		(s[3] & 0x08) | 
        		DataTools.MoveRight((byte)(s[1] & 0x10) , 2) |
        		DataTools.MoveRight((byte)(s[3] & 0x10) , 3) | 
        		DataTools.MoveRight((byte)(s[2] & 0x80) , 7)
            );
        t[1] = (byte)(
        		(s[0] & 0x80) | 
        		((s[1] & 0x02) << 5) | 
        		((s[2] & 0x02) << 4) |
        		DataTools.MoveRight((byte)(s[3] & 0x40) , 2) | 
        		(s[0] & 0x08) | 
        		DataTools.MoveRight((byte)(s[2] & 0x40) , 4) |
        		(s[3] & 0x02) | 
        		DataTools.MoveRight((byte)(s[1] & 0x40) , 6)
            );
        t[2] = (byte)(
        		((s[0] & 0x40) << 1) | 
        		((s[0] & 0x01) << 6) | 
        		((s[2] & 0x01) << 5) |
        		((s[1] & 0x04) << 2) | 
        		((s[3] & 0x01) << 3) | 
        		DataTools.MoveRight((byte)(s[3] & 0x20) , 3) |
        		DataTools.MoveRight((byte)(s[0] & 0x20) , 4) | 
        		DataTools.MoveRight((byte)(s[1] & 0x80) , 7)
            );
        t[3] = (byte)(
        		((s[2] & 0x20) << 2) | 
        		((s[1] & 0x08) << 3) | 
        		((s[3] & 0x04) << 3) |
        		((s[0] & 0x04) << 2) | 
        		((s[2] & 0x04) << 1) | 
        		DataTools.MoveRight((byte)(s[1] & 0x20) , 3) |
        		DataTools.MoveRight((byte)(s[0] & 0x10) , 3) | 
        		DataTools.MoveRight((byte)(s[3] & 0x80) , 7)
            );
        
        //DataTools.copyByte(t,1,s,1,4);
        for (i = 0; i < 4; ++i)
        {
            s[i] = t[i];
        }
        return s;
    }
    
    /**
     * 
     * @param s 输入输出
     * @param begin
     * @param ns
     * @return
     */
    private static byte FindS(byte[] s, int begin, int ns)
    {
        int col = 0, num = 0, index = 0;
        if (ns == 1 || ns == 5)
        {
            col = DataTools.MoveRight((byte)(s[begin] & 0x80) , 6) | DataTools.MoveRight((byte)(s[begin] & 0x04) , 2);
            num = DataTools.MoveRight((byte)(s[begin] & 0x78) , 3);
            index = col * 16 + num;
        }
        if (ns == 2 || ns == 6)
        {
            col = (s[begin] & 0x02) | DataTools.MoveRight((byte)(s[begin + 1] & 0x10) , 4);
            num = ((s[begin] & 0x01) << 3) | DataTools.MoveRight((byte)(s[begin + 1] & 0xe0) , 5);
            index = col * 16 + num;
        }
        if (ns == 3 || ns == 7)
        {
            col = DataTools.MoveRight((byte)(s[begin + 1] & 0x08) , 2) | DataTools.MoveRight((byte)(s[begin + 2] & 0x40) , 6);
            num = ((s[begin + 1] & 0x07) << 1) | DataTools.MoveRight((byte)(s[begin + 2] & 0x80) , 7);
            index = col * 16 + num;
        }
        if (ns == 4 || ns == 8)
        {
            col = DataTools.MoveRight((byte)(s[begin + 2] & 0x20) , 4) | (s[begin + 2] & 0x01);
            num = DataTools.MoveRight((byte)(s[begin + 2] & 0x1e) , 1);
            index = col * 16 + num;
        }
        return SFun_Tab[ns - 1][index];
    }
    
    /**
     * 
     * @param s 输入
     * @param r 输入输出
     * @return
     */
    private static byte[] DoSfun(byte[] s, byte[] r)
    {
        r[0] = (byte)((FindS(s, 0, 1) << 4) | FindS(s, 0, 2));
        r[1] = (byte)((FindS(s, 0, 3) << 4) | FindS(s, 0, 4));
        r[2] = (byte)((FindS(s, 3, 5) << 4) | FindS(s, 3, 6));
        r[3] = (byte)((FindS(s, 3, 7) << 4) | FindS(s, 3, 8));
        
        return r;
    }
	 
    /**
     * 
     * @param R 输入
     * @param K 输入
     * @param m 输入输出
     * @return
     */
    private static byte[] FFun(byte[] R, byte[] K, byte[] m)
    {
        int i;
        byte[] t = new byte[6];

        t = EExpand(R, t);
        
        for (i = 0; i < 6; ++i)
        {
            t[i] ^= K[i];
        }
        m = DoSfun(t, m);
        m = PChang(m);
        
        return m;
    }
    
    /**
     * 
     * @param L 输入输出
     * @param R 输入输出
     * @param K 输入
     */
    private static byte[][] DoMut(byte[] L, byte[] R, byte[] K)
    {
        int i;
        byte[] t = new byte[4];

        t = FFun(R, K, t);
        for (i = 0; i < 4; ++i)
        {
            t[i] ^= L[i];
            L[i] = R[i];
            R[i] = t[i];
        }
        
        byte[][] bRet = new byte[2][];
        bRet[0] = L;
        bRet[1] = R;
        return bRet;
    }
    
    /**
     * 
     * @param dir
     * @param C 输入输出
     * @param D 输入输出
     * @param n
     * @param K 输入输出
     */
    private static  byte[][] SetKey(int dir, byte[] C, byte[] D, int n, byte[] K)
    {
        K = new byte[6];
        
        if (dir == eDesCRPTY.DES_ENCRYPT)
        {
            C = DoLeft(C, n);
            D = DoLeft(D, n);
        }
        K[0] = (byte)(
				((C[1] & 4) << 5) | 
				DataTools.MoveRight((byte)(C[2] & 0X80) , 1) | 
				(C[1] & 0X20) |
				((C[2] & 1) << 4) | 
				DataTools.MoveRight((byte)(C[0] & 0X80) , 4) | 
				DataTools.MoveRight((byte)(C[0] & 8) , 1) |
				DataTools.MoveRight((byte)(C[0] & 0X20) , 4) | 
				DataTools.MoveRight((byte)(C[3] & 0X10) , 4)
        		);
        K[1] = (byte)(
        		((C[1] & 2) << 6) | 
        		((C[0] & 4) << 4) | 
        		((C[2] & 8) << 2) |
        		DataTools.MoveRight((byte)(C[1] & 0X40) , 2) | 
        		((C[2] & 2) << 2) | 
        		DataTools.MoveRight((byte)(C[2] & 0x20) , 3) |
        		DataTools.MoveRight((byte)(C[1] & 0X10) , 3) | 
        		DataTools.MoveRight((byte)(C[0] & 0X10) , 4)
        		);
        K[2] = (byte)(
        		((C[3] & 0x40) << 1) | 
        		((C[0] & 0X01) << 6) | 
        		((C[1] & 0X01) << 5) |
	            ((C[0] & 2) << 3) | 
	            DataTools.MoveRight((byte)(C[3] & 0X20) , 2) | 
	            DataTools.MoveRight((byte)(C[2] & 0x10) , 2) |
	            DataTools.MoveRight((byte)(C[1] & 0X08) , 2) | 
	            DataTools.MoveRight((byte)(C[0] & 0X40) , 6)
	            );
        K[3] = (byte)(
        		((D[1] & 0x08) << 4) | 
        		((D[2] & 0X01) << 6) | 
        		(D[0] & 0X20) |
        		DataTools.MoveRight((byte)(D[1] & 0x80) , 3) | 
        		DataTools.MoveRight((byte)(D[2] & 0X20) , 2) | 
        		DataTools.MoveRight((byte)(D[3] & 0x20) , 3) |
        		DataTools.MoveRight((byte)(D[0] & 0X40) , 5) | 
        		DataTools.MoveRight((byte)(D[1] & 0X10) , 4)
	            );
        K[4] = (byte)(
        		((D[2] & 0x02) << 6) | 
        		DataTools.MoveRight((byte)(D[2] & 0X80) , 1) | 
        		((D[0] & 0X08) << 2) |
	            (D[2] & 0x10) | 
	            ((D[1] & 0X01) << 3) | 
	            DataTools.MoveRight((byte)(D[2] & 0x08) , 1) |
	            DataTools.MoveRight((byte)(D[1] & 0X20) , 4) | 
	            DataTools.MoveRight((byte)(D[3] & 0X10) , 4)
	            );
        K[5] = (byte)(
        		((D[0] & 0x04) << 5) | 
        		DataTools.MoveRight((byte)(D[3] & 0X80) , 1) | 
        		//((D[2] & 0X40)) >> 1 |//注：原有c#代码是这么写的 ，感觉)位置不对  纠正了
        		DataTools.MoveRight((byte)(D[2] & 0X40) , 1) |
	            ((D[1] & 0x04) << 2) | 
	            ((D[2] & 0X04) << 1) | 
	            ((D[0] & 0x01) << 2) |
	            DataTools.MoveRight((byte)(D[0] & 0X80) , 6) | 
	            DataTools.MoveRight((byte)(D[0] & 0X10) , 4)
	            );
        
        if (dir == eDesCRPTY.DES_DECRYPT)
        {
            C = DoRight(C, n);
            D = DoRight(D, n);
        }
        
        byte[][] bRet = new byte[3][];
        bRet[0] = C;
        bRet[1] = D;
        bRet[2] = K;
        return bRet;
    }
    
    /**
     * 
     * @param key 输入
     * @param bufS 输入输出
     */
    private static byte[] DesEncrypt(byte[] key, byte[] bufS)
    {
        int i;
        int[] nLoop = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };
        byte[] C0 = new byte[4];
        byte[] D0 = new byte[4];
        byte[] L = new byte[4];
        byte[] R = new byte[4];
        byte[] K = new byte[6];
        byte c;

        bufS = DoFirstChange(bufS);
        //L = s1; R = s1 + 4;
        DataTools.copyByte(key, 0, C0, 0, 4);
        DataTools.copyByte(key, 4, D0, 0, 4);
        DataTools.copyByte(bufS, 0, L, 0, 4);
        DataTools.copyByte(bufS, 4, R, 0, 4);
        for (i = 0; i < 16; ++i)
        {
            byte[][] bytetmp = SetKey(eDesCRPTY.DES_ENCRYPT, C0, D0, nLoop[i], K);
            C0 = bytetmp[0];
            D0 = bytetmp[1];
            K = bytetmp[2];
            
            byte[][] bytes = DoMut(L, R, K);
            L = bytes[0];
            R = bytes[1];
        }
        DataTools.copyByte(L, 0, bufS, 0, 4);
        DataTools.copyByte(R, 0, bufS, 4, 4);
        for (i = 0; i < 4; ++i)
        {
            c = R[i];
            bufS[i + 4] = L[i];
            bufS[i] = c;
        }
        bufS = DoLastChange(bufS);
        
        return bufS;
    }
	 
    /**
     * 
     * @param key 输入
     * @param bufS 输入输出
     */
    private static byte[] DesDecryptor(byte[] key, byte[] bufS)
    {
        int i;
        int[] nLoop = { 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1, 1 };
        byte[] C0 = new byte[4];
        byte[] D0 = new byte[4];
        byte[] L = new byte[4];
        byte[] R = new byte[4];
        byte[] K = new byte[6];
        byte c;

        bufS = DoFirstChange(bufS);
        //L = s1; R = s1 + 4;
        DataTools.copyByte(key, 0, C0, 0, 4);
        DataTools.copyByte(key, 4, D0, 0, 4);
        DataTools.copyByte(bufS, 0, L, 0, 4);
        DataTools.copyByte(bufS, 4, R, 0, 4);
        for (i = 0; i < 16; ++i)
        {
        	byte[][] bytetmp = SetKey(eDesCRPTY.DES_DECRYPT, C0, D0, nLoop[i], K);
        	C0 = bytetmp[0];
            D0 = bytetmp[1];
            K = bytetmp[2];
            
            byte[][] bytes = DoMut(L, R, K);
            L = bytes[0];
            R = bytes[1];
        }
        DataTools.copyByte(L, 0, bufS, 0, 4);
        DataTools.copyByte(R, 0, bufS, 4, 4);
        for (i = 0; i < 4; ++i)
        {
            c = R[i];
            bufS[i + 4] = L[i];
            bufS[i] = c;
        }
        bufS = DoLastChange(bufS);
        return bufS;
    }
	 
    //基本调用方法================================================================

    /**
     * 标准的DES加密函数
     * @param key BYTE[8]->Bit[64]的密钥 in
     * @param bufS BYTE[8]的数据源 in
     * @param bufD BYTE[8]的数据结果  out
     * @return
     */
    public static byte[] Encrypt(byte[] key, byte[] bufS, byte[] bufD)
    {
        byte[] newKey;
        byte[] bufTmp = new byte[8];
        bufD = new byte[bufD.length];
        DataTools.copyByte(bufS, 0, bufTmp, 0, 8);
        newKey = MakeKey(key);       // get C,D
        bufTmp = DesEncrypt(newKey, bufTmp);
        DataTools.copyByte(bufTmp, 0, bufD, 0, 8);
        return bufD;
    }
    
    /**
     * 标准的DES解密函数
     * @param key BYTE[8]->Bit[64]的密钥 in
     * @param bufS BYTE[8]的数据源 in
     * @param bufD BYTE[8]的数据结果 out
     * @return
     */
    public static byte[] Decryptor(byte[] key, byte[] bufS, byte[] bufD)
    {
        byte[] newKey;
        byte[] bufTmp = new byte[8];
        bufD = new byte[bufD.length];
        DataTools.copyByte(bufS, 0, bufTmp, 0, 8);
        newKey = MakeKey(key);       // get C,D
        bufTmp = DesDecryptor(newKey, bufTmp);
        DataTools.copyByte(bufTmp, 0, bufD, 0, 8);
        return bufD;
    }
	
    /**
     * 单倍长Ansi9.9方法生成Mac
     * @param key BYTE[8]->Bit[64]的密钥
     * @param bufS Mac数据源
     * @param begin 基于0开始的起始位
     * @param len 有效数据长度
     * @param mac 生成的Byte[8]位Mac码 out
     * @return
     */
    public static byte[] Ansi99New(byte[] key, byte[] bufS, int begin, int len, byte[] mac)
    {
        int i, j;
        byte[] tmp8 = new byte[8];
        int fill08Len = ((int)(len + 7) / 8) * 8;
        byte[] fill08BufS = new byte[fill08Len];
        DataTools.copyByte(bufS, begin, fill08BufS, 0, len);
        mac = new byte[8];
        for (i = 0; i < fill08Len; i += 8)
        {
            for (j = 0; j < 8; j++)
            {
                mac[j] = (byte)(mac[j] ^ fill08BufS[i + j]);
            }
            DataTools.copyByte(mac, 0, tmp8, 0, 8);
            mac = Encrypt(key, tmp8, mac);            
        }
        return mac;
    }

    /**
     * 单倍长Ansi9.9方法验证Mac
     * @param key BYTE[8]->Bit[64]的密钥
     * @param bufS Mac数据源
     * @param begin 基于0开始的起始位
     * @param len 有效数据长度
     * @param mac 需要验证的Byte[8]位Mac码
     * @param newMac 新的Mac码 out1
     * @return out2
     */
    public static byte[][] Ansi99Check(byte[] key, byte[] bufS, int begin, int len, byte[] mac, byte[] newMac)
    {
    	boolean oK = false;
        int i, j;
        byte[] tmp8 = new byte[8];
        int fill08Len = ((int)(len + 7) / 8) * 8;
        byte[] fill08BufS = new byte[fill08Len];
        DataTools.copyByte(bufS, begin, fill08BufS, 0, len);
        for (i = 0; i < fill08Len; i += 8)
        {
            for (j = 0; j < 8; j++)
            {
                newMac[j] = (byte)(newMac[j] ^ fill08BufS[i + j]);
            }
            DataTools.copyByte(newMac, 0, tmp8, 0, 8);
            newMac = Encrypt(key, tmp8, newMac);            
        }
        oK = true;
        if (oK)
        {
            for (i = 0; i < 8; i++)
            {
                if (newMac[i] != mac[i])
                {
                    oK = false;
                    break;
                }
            }
        }
        
        byte[][] bRet = new byte[2][];
        bRet[0] = newMac;
        bRet[1] = new byte[]{0};
        if(oK) bRet[1] = new byte[]{1};
        
        return bRet;
    } 
	 
    /**
     * 单倍长BPI方法生成Mac
     * @param key BYTE[8]->Bit[64]的密钥
     * @param bufS Mac数据源
     * @param begin 基于0开始的起始位
     * @param len 有效数据长度
     * @param mac 生成的Byte[8]位Mac码 输入输出
     * @return
     */
    public static byte[] BPINew(byte[] key, byte[] bufS, int begin, int len, byte[] mac)
    {
        int i, j;
        byte[] tmp8 = new byte[8];
        int fill08Len = ((int)(len + 7) / 8) * 8;
        byte[] fill08BufS = new byte[fill08Len];
        DataTools.copyByte(bufS, begin, fill08BufS, 0, len);
        mac = new byte[8];
        for (i = 0; i < fill08Len; i += 8)
        {
            for (j = 0; j < 8; j++)
            {
                mac[j] = (byte)(mac[j] ^ fill08BufS[i + j]);
            }
            //DataTools.copyByte(mac,0,tmp8,0,8);
            //mac = Encrypt(primaryKey,tmp8,mac);
        }
        DataTools.copyByte(mac, 0, tmp8, 0, 8);
        mac = Encrypt(key, tmp8, mac);
        return mac;
    }
    
    /**
     * 单倍长BPI方法验证Mac
     * @param key BYTE[8]->Bit[64]的密钥
     * @param bufS Mac数据源
     * @param begin 基于0开始的起始位
     * @param len 有效数据长度
     * @param mac 需要验证的Byte[8]位Mac码
     * @param newMac 新的Mac码 out1
     * @return out2
     */
    public static HsmInterfaceRet BPICheck(byte[] key, byte[] bufS, int begin, int len, byte[] mac, byte[] newMac)
    {
    	HsmInterfaceRet hsmRet = new HsmInterfaceRet();
    	
    	boolean oK = false;
        int i, j;
        byte[] tmp8 = new byte[8];
        int fill08Len = ((int)(len + 7) / 8) * 8;
        byte[] fill08BufS = new byte[fill08Len];
        DataTools.copyByte(bufS, begin, fill08BufS, 0, len);
        for (i = 0; i < fill08Len; i += 8)
        {
            for (j = 0; j < 8; j++)
            {
                newMac[j] = (byte)(newMac[j] ^ fill08BufS[i + j]);
            }
            //DataTools.copyByte(newMac,0,tmp8,0,8);
            //mac = Encrypt(primaryKey,tmp8,newMac);
        }
        DataTools.copyByte(newMac, 0, tmp8, 0, 8);
        newMac = Encrypt(key, tmp8, newMac);
        oK = true;
        for (i = 0; i < 8; i++)
        {
            if (newMac[i] != mac[i])
            {
                oK = false;
                break;
            }
        }
        
        hsmRet.boolRet = oK;
        hsmRet.bytRet = newMac;
        return hsmRet;
    }
    
    /**
     * 单倍长银联标准MAC算法
     * @param key BYTE[8]->Bit[64]的密钥
     * @param bufS Mac数据源
     * @param begin 基于0开始的起始位
     * @param len 有效数据长度
     * @return 生成的Byte[8]位Mac码 out
     */
    public static byte[] UnionPayMacNew(byte[] key, byte[] bufS, int begin, int len, byte[] mac)
    {
        int i, j, k;
        byte[] tmp8 = new byte[8];
        int fill08Len = ((int)(len + 7) / 8) * 8;
        byte[] fill08BufS = new byte[fill08Len];
        DataTools.copyByte(bufS, begin, fill08BufS, 0, len);
        mac = new byte[8];
        //按每8个字节做异或（不管信息中的字符格式），如果最后不满8个字节，则添加“0X00”。
        for (i = 0; i < fill08Len; i += 8)
        {
            for (j = 0; j < 8; j++)
            {
                mac[j] = (byte)(mac[j] ^ fill08BufS[i + j]);
            }
        }
        DataTools.copyByte(mac, 0, tmp8, 0, 8);
        //将异或运算后的最后8个字节转换成16 个HEXDECIMAL
        String tempStr = DataTools.ByteArrayToString(tmp8);
        byte[] tempBuf1 = DataTools.StringToUTF8(tempStr.substring(0, 8));
        byte[] tempBuf2 = DataTools.StringToUTF8(tempStr.substring(8, 8 + 8));
        byte[] tempBuf = new byte[8];
        //取前8 个字节用MAK加密：
        tempBuf = Encrypt(key, tempBuf1, tempBuf);
        byte[] tempbuf3 = new byte[8];
        //将加密后的结果与后8 个字节异或
        for (k = 0; k < 8; k++)
        {
            tempbuf3[k] = (byte)(tempBuf[k] ^ tempBuf2[k]);
        }
        tempBuf = new byte[8];
        //用异或的结果再进行一次单倍长DES密钥算法运算。
        tempBuf = Encrypt(key, tempbuf3, tempBuf);
        //将运算后的结果转换成16 个HEXDECIMAL
        tempStr = DataTools.ByteArrayToString(tempBuf);
        byte[] tempbuf4 = DataTools.StringToUTF8(tempStr);
        //取前8个字节作为MAC值
        DataTools.copyByte(tempbuf4, 0, mac, 0, 8);
        
        return mac;
    }
    
    /**
     * 单倍长银联标准MAC验证Mac
     * @param key BYTE[8]->Bit[64]的密钥
     * @param bufS Mac数据源
     * @param begin 基于0开始的起始位
     * @param len 有效数据长度
     * @param orgMac 需要验证的Byte[8]位Mac码
     * @return 新的Mac码
     */
    public static HsmInterfaceRet UnionPayMacCheck(byte[] key, byte[] bufS, int begin, int len, byte[] orgMac, byte[] newMac)
    {
    	HsmInterfaceRet hsmRet = new HsmInterfaceRet();
    	
    	boolean oK = false;
        int i, j, k;
        byte[] tmp8 = new byte[8];
        int fill08Len = ((int)(len + 7) / 8) * 8;
        byte[] fill08BufS = new byte[fill08Len];
        DataTools.copyByte(bufS, begin, fill08BufS, 0, len);
        newMac = new byte[8];
        //按每8个字节做异或（不管信息中的字符格式），如果最后不满8个字节，则添加“0X00”。
        for (i = 0; i < fill08Len; i += 8)
        {
            for (j = 0; j < 8; j++)
            {
                newMac[j] = (byte)(newMac[j] ^ fill08BufS[i + j]);
            }
        }
        DataTools.copyByte(newMac, 0, tmp8, 0, 8);       
        //将异或运算后的最后8个字节转换成16 个HEXDECIMAL
        String tempStr = DataTools.ByteArrayToString(tmp8);
        byte[] tempBuf1 = DataTools.StringToUTF8(tempStr.substring(0, 8));
        byte[] tempBuf2 = DataTools.StringToUTF8(tempStr.substring(8, 8 + 8));
        byte[] tempBuf = new byte[8];
        //取前8 个字节用MAK加密：
        tempBuf = Encrypt(key, tempBuf1, tempBuf);
        byte[] tempbuf3 = new byte[8];
        //将加密后的结果与后8 个字节异或
        for (k = 0; k < 8; k++)
        {
            tempbuf3[k] = (byte)(tempBuf[k] ^ tempBuf2[k]);
        }
        tempBuf = new byte[8];
        //用异或的结果再进行一次单倍长DES密钥算法运算。
        tempBuf = Encrypt(key, tempbuf3, tempBuf);
        //将运算后的结果转换成16 个HEXDECIMAL
        tempStr = DataTools.ByteArrayToString(tempBuf);
        byte[] tempbuf4 = DataTools.StringToUTF8(tempStr);
        //取前8个字节作为MAC值
        newMac = new byte[8];
        DataTools.copyByte(tempbuf4, 0, newMac, 0, 8);
        oK = true;
        for (i = 0; i < 8; i++)
        {
            if (newMac[i] != orgMac[i])
            {
                oK = false;
                break;
            }
        }
        
        hsmRet.boolRet = oK;
        hsmRet.bytRet = newMac;
        return hsmRet;
    } 
	 
    /**
     * 标准的Ansi9.8单DES加密函数
     * @param key BYTE[8]->Bit[64]的密钥
     * @param bankCardNo BYTE[>=16]的银行卡号
     * @param len 银行卡号有效长度[16..19]
     * @param bankPass BYTE[>=6]密码
     * @param bufD BYTE[8]的加密结果 out
     * @return
     */
    public static byte[] Ansi98Encrypt(byte[] key, byte[] bankCardNo, int len, byte[] bankPass, byte[] bufD)
    {
        byte[] pinBcd = DataTools.StringToByteArray("FFFFFFFFFFFFFFFF");
        byte[] cardBcd = DataTools.StringToByteArray("FFFFFFFFFFFFFFFF");
        
        int i = 0, bankSeek = len - 13;
        //AscToBcd (pPinS,6,pinBCD+2);
        BcdConvert.NumBufToBcdBuf(bankPass, 0, 6, pinBcd, 1);
        pinBcd[0] = 0x06;
        //pinBCD[4]=pinBCD[5]=pinBCD[6]=pinBCD[7]=0xFF;
        BcdConvert.NumBufToBcdBuf(bankCardNo, bankSeek, 12, cardBcd, 2);
        for (i = 2; i < 8; i++)
        {
            pinBcd[i] ^= cardBcd[i];
        }
        bufD = Encrypt(key, pinBcd, bufD);
        return bufD;
    }

    /**
     * 标准的Ansi9.8单DES加密函数
     * @param key BYTE[8]->Bit[64]的密钥
     * @param sBankCardNo 有效长度>=16的银行卡号
     * @param bankPass 有效长度>=6的银行卡密码
     * @param bufD BYTE[8]的加密结果 out
     * @return
     */
    public static byte[] Ansi98Encrypt(byte[] key, String sBankCardNo, byte[] bankPass, byte[] bufD)
    {
        sBankCardNo = sBankCardNo.trim();
        int len = sBankCardNo.length();
        byte[] bankNoBuf = DataTools.StringToUTF8(sBankCardNo.trim());
        bufD = Ansi98Encrypt(key, bankNoBuf, len, bankPass, bufD);
        return bufD;
    }

    /**
     * 标准的Ansi9.8单DES加密函数
     * @param key BYTE[8]->Bit[64]的密钥
     * @param sBankCardNo 有效长度>=16的银行卡号
     * @param sBankPass 有效长度>=6的银行卡密码
     * @param bufD BYTE[8]的加密结果 out
     * @return
     */
    public static byte[] Ansi98Encrypt(byte[] key, String sBankCardNo, String sBankPass, byte[] bufD)
    {
        sBankCardNo = sBankCardNo.trim();
        int len = sBankCardNo.length();
        byte[] bankNoBuf = DataTools.StringToUTF8(sBankCardNo.trim());
        byte[] bankPassBuf = DataTools.StringToUTF8(DataTools.PadRight(sBankPass, '0', 6).substring(0, 6));
        bufD = Ansi98Encrypt(key, bankNoBuf, len, bankPassBuf, bufD);
        return bufD;
    }
    
    /**
     * 标准的Ansi9.8单DES解密函数
     * @param key BYTE[8]->Bit[64]的密钥
     * @param bankCardNo BYTE[>=16]的银行卡号
     * @param len 银行卡号有效长度[16..19]
     * @param bufS BYTE[8]密文密码
     * @param bankPass BYTE[8]解密后密码  inout
     * @return
     */
    public static byte[] Ansi98Decryptor(byte[] key, byte[] bankCardNo, int len, byte[] bufS, byte[] bankPass)
    {
        byte[] pinBcd = DataTools.StringToByteArray("FFFFFFFFFFFFFFFF");
        byte[] cardBcd = DataTools.StringToByteArray("FFFFFFFFFFFFFFFF");
        
        byte[] bufD = new byte[16];
        int i = 0, bankSeek = len - 13;
        BcdConvert.NumBufToBcdBuf(bankCardNo, bankSeek, 12, cardBcd, 2);
        Decryptor(key, bufS, bufD);
        pinBcd[0] = bufD[0];
        pinBcd[1] = bufD[1];
        for (i = 2; i < 8; i++)
        {
            pinBcd[i] = (byte)(bufD[i] ^ cardBcd[i]);
        }
        bufD = BcdConvert.BcdBufToNumBuf(pinBcd, 1, 3, bufD, 2);
        DataTools.copyByte(bufD, 2, bankPass, 0, 6);
        bankPass[6] = bankPass[7] = 0x20;
        return bankPass;
    }
    
    /**
     * 标准的Ansi9.8单DES加密函数
     * @param key BYTE[8]->Bit[64]的密钥
     * @param sBankCardNo 有效长度>=16的银行卡号
     * @param bankPassBuf BYTE[>=6]密码
     * @param bufD BYTE[8]的加密结果 inout
     * @return
     */
    public static byte[] Ansi98Decryptor(byte[] key, String sBankCardNo, byte[] bankPassBuf, byte[] bufD)
    {
        sBankCardNo = sBankCardNo.trim();
        int len = sBankCardNo.length();
        byte[] bankNoBuf = DataTools.StringToUTF8(sBankCardNo.trim());
        bufD = Ansi98Decryptor(key, bankNoBuf, len, bankPassBuf, bufD);
        return bufD;
    } 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
}
