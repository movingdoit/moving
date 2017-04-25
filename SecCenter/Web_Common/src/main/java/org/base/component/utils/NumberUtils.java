package org.base.component.utils;

import java.math.BigDecimal;

/**
 * 数字操作工具类
 *
 */
public class NumberUtils extends org.apache.commons.lang.math.NumberUtils {

	
	/**
	 * 基于特定精度，对float做四舍五入处理
	 * @param value
	 * @param scale
	 * @return
	 */
	public static float roundFloatWithScale(float value, int scale) {
		return new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue(); 
	}
	
	/**
	 * 基于特定精度，对double做四舍五入处理
	 * @param value
	 * @param scale
	 * @return
	 */
	public static double roundDoubleWithScale(double value, int scale) {
		return new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue(); 
	}
	
	/**
	 * 十进制转换成３２进制
	 * @param value
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-9-20下午12:04:22
	 */
	public static String to32Hex(Integer value){
		return toUnsignedString(value,5,digits_32);
	}
	
	final static char[] digits_32 = {
		'0' , '1' , '2' , '3' , '4' , '5' ,
		'6' , '7' , '8' , '9' , 'A' , 'B' ,
		'C' , 'D' , 'E' , 'F' , 'G' , 'H' ,
		'J' , 'K' , 'M' , 'N' , 'P' , 'Q' ,
		'R' , 'S' , 'T' , 'U' , 'V' , 'W' , 
		'X' , 'Y'
		};

	private static String toUnsignedString(int i, int shift,char[] digits) {
		char[] buf = new char[32];
		int charPos = 32;
		int radix = 1 << shift;
		int mask = radix - 1;
		do {
		buf[--charPos] = digits[i & mask];
		i >>>= shift;
		} while (i != 0);

		return new String(buf, charPos, (32 - charPos));
	}
}
