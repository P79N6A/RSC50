/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */
package com.shrcn.found.common.util;

/**
 * @author 孙春颖
 * @version 1.0, 2014-5-28
 */
public class FloatUtil {

	/**
	 * 返回fmin参数，精度为precision
	 * 
	 * @param fmin
	 * @param precision
	 * @return
	 */
	public static float getFloatPrecision(float fmin, int precision) {
		return (float) (Math.round(fmin * Math.pow(10, precision)) / Math
				.pow(10, precision));
	}

	public static float getFloatPrecision(String strminv, int precision) {
		float fmin = Float.parseFloat(strminv);
		return (float) (Math.round(fmin * Math.pow(10, precision)) / Math
				.pow(10, precision));
	}

}
