package net.newcapec.collect.formatter;

import java.lang.annotation.*;

/**
 * @author 陈飞飞
 * 序列化属性
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BinarySerializable{

	/**
	 * 序列化次序，从1开始
	 * 缺省值：0
	 */
	public int Order() default 0;			

	/**
	 * 序列化后的字节数 或 数值类型数组元素的字节数
	 * 缺省值：0
	 */
	public int Size() default 0;

	/**
	 * 日期转换类格式符
	 * 缺省值：yyyyMMddHHmmss
	 */
	public TimeFormat Format() default TimeFormat.yyyyMMddHHmmss;

	/**
	 * 数字值字节格式 BCD/HEX，
	 * ps:字段类型需为long
	 * 缺省值：HEX
	 */
	public NumericType NumType() default NumericType.HEX;

	/**
	 * 字符串编码格式，us-ascii,gbk,utf-8
	 * 缺省值：us-ascii
	 */
	public String CodeName() default "us-ascii";
	
	/**
	 * 字符串对齐模式
	 * 缺省值：PaddingMode.None 不对奇（右补0x00）
	 */
	public PaddingMode PadMode() default PaddingMode.None;

	/**
	 * 字符串补位时填充字符
	 * 缺省值：0
	 */
	public byte PadByte() default 0;

	/**
	 * 字节数组是否存在前导长度域
	 * 缺省值：false
	 */
	public boolean HasLeaderCharacter() default false;

	/**
	 * 字节数组前导长度域的长度
	 * 缺省值：0
	 */
	public ArrayLeadingBytes ArrayLeadByte() default ArrayLeadingBytes.Zero;

	/**
	 * 是否抛出时间字段数据异常，false时：数据乱码，取默认时间1988-2-22 00:00:00
	 * 缺省值：true
	 */
	public boolean ThrowTimeFormatException() default true;

	/**
	 * tlv的标签值
	 * 缺省值：0xFFFF结束Tag
	 * ps:此时以下属性无效(Size,Order,PadMode,PadByte,HasLeaderCharacter,ArrayLeadByte)
	 */
	public int Tag() default 0xFFFF;

    /**
     * 是否为自定义对象
     */
	public boolean IsEntity() default false;
}


