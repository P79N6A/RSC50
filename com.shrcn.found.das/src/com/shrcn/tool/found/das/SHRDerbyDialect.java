/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.found.das;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-10-12
 */
/**
 * $Log: SHRDerbyDialect.java,v $
 * Revision 1.1  2012/10/17 08:03:55  cchun
 * Add:数据库访问接口
 *
 */
public class SHRDerbyDialect extends org.hibernate.dialect.DerbyDialect {
	
	@Override
	public String getSequenceNextValString(final String sequenceName) {
		return "values next value for " + sequenceName;
	}
}
