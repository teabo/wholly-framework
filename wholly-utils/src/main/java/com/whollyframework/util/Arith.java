package com.whollyframework.util;

import java.math.BigDecimal;

public class Arith {

	/**
	 * 
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * 
	 * @param scale
	 *            小数点后保留几位
	 * 
	 * @return 四舍五入后的结果
	 * 
	 */
	public static double round(double v, int scale) {

		if (scale < 0) {

			throw new IllegalArgumentException(

			"The scale must be a positive integer or zero");

		}

		BigDecimal b = new BigDecimal(Double.toString(v));// import
															// java.math.BigDecimal;

		BigDecimal one = new BigDecimal("1");

		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}
	/**
	 * 
	 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
	 * 
	 * 确的浮点数运算，包括加减乘除和四舍五入。
	 * 
	 */
}
