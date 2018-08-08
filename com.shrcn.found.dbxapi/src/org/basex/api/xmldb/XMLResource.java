/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package org.basex.api.xmldb;

import org.xmldb.api.base.XMLDBException;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-11-25
 */
/**
 * $Log: XMLResource.java,v $
 * Revision 1.1  2013/12/30 01:15:28  cchun
 * Update:增加接口saveContent()
 *
 * Revision 1.1  2013/11/25 11:36:46  cchun
 * Add:新增接口
 *
 */
public interface XMLResource extends org.xmldb.api.modules.XMLResource {
	
	public boolean saveContent(final String fn) throws XMLDBException;
}
