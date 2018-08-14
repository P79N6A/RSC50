/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 根据监控后台要求，要为每个设备自动添加上oid属性。
 * 第一次修改：oid len 24 -> 22
 * 第二次修改：oid len 22 -> 16
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-3
 */
/**
 * $Log: EquipmentSequence.java,v $
 * Revision 1.1  2013/03/29 09:36:28  cchun
 * Add:创建
 *
 * Revision 1.3  2010/05/25 09:02:22  cchun
 * Update:oid长度从22改成16
 *
 * Revision 1.2  2010/05/17 05:56:25  cchun
 * Update:将yyyy改成yy
 *
 * Revision 1.1  2010/03/03 09:58:21  cchun
 * Update:为一次设备添加oid
 *
 */
public class EquipmentSequence {

	/**
	 * 生成设备oid属性值
	 * @return
	 */
	public static String getOid() {
		SimpleDateFormat format = new SimpleDateFormat("MMddhhmmssSSS");
		String time_part = format.format(new Date());
		double d = Math.random();
		String random_part = new Integer((int)(d * 1000)).toString();
		return time_part + random_part;
	}
}
