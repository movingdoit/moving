package org.base.component.utils;

import java.math.BigDecimal;
/**
 * 
 * @ClassName: CalculateUtilsExpand 
 * @Description: 
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2013-8-31 上午10:50:33
 *
 */
public class CalculateUtilsExpand extends CalculateUtils{

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字全部舍弃。
	 * @param v1 被除数
	 * @param v2  除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double divDOWN(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}
}
