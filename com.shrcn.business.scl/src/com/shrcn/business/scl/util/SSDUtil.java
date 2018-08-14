/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.util;

import java.util.List;

import org.dom4j.Element;

import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.scl.filter.impl.OIDFilter;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 吴云华(mailto:wyh@shrcn.com)
 * @version 1.0, 2009-8-26
 */
/*
 * 修改历史
 * $Log: SSDUtil.java,v $
 * Revision 1.1  2013/03/29 09:36:30  cchun
 * Add:创建
 *
 * Revision 1.5  2011/10/21 08:24:08  cchun
 * Fix Bug:修复OIDFilter使用错误
 *
 * Revision 1.4  2010/09/07 10:00:23  cchun
 * Update:更换过时接口
 *
 * Revision 1.3  2010/07/28 06:56:38  cchun
 * Update:为导出功能添加SCL属性信息
 *
 * Revision 1.2  2010/01/19 09:02:28  lj6061
 * add:统一国际化工程
 *
 * Revision 1.1  2009/08/27 06:43:18  wyh
 * 导出SSD文件操作
 *
 */
public class SSDUtil {
	
	public static void export(final String fileName){
		Element root = DOM4JNodeHelper.createSCLNode("SCL"); //$NON-NLS-1$
		if(SCTProperties.getInstance().needsVersions()) {
			root.addAttribute("revision", "A");
			root.addAttribute("version", "2007");
		}
		Element header = root.addElement("Header"); //$NON-NLS-1$
		header.addAttribute("id", "SHR") //$NON-NLS-1$ //$NON-NLS-2$
			  .addAttribute("version", "1.0") //$NON-NLS-1$ //$NON-NLS-2$
			  .addAttribute("toolID", "EASY50") //$NON-NLS-1$ //$NON-NLS-2$
			  .addAttribute("nameStructure", "IEDName"); //$NON-NLS-1$ //$NON-NLS-2$
		
		List<Element> subs = XMLDBHelper.selectNodes(SCL.XPATH_SUBSTATION);
		for (Element sub : subs) {
			if (sub != null)
				root.add(sub.createCopy());
		}
		
		new OIDFilter().filter(root.getDocument());
		XMLFileManager.writeDoc(root.getDocument(), fileName);
	}
	
}
