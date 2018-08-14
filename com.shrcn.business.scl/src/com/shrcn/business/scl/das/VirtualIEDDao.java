/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das;

import org.dom4j.Document;
import org.dom4j.Element;

import com.shrcn.business.scl.history.HistoryManager;
import com.shrcn.business.scl.history.MarkInfo.DevType;
import com.shrcn.business.scl.history.MarkInfo.OperType;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-1-7
 */
/**
 * $Log: VirtualIEDDao.java,v $
 * Revision 1.1  2013/03/29 09:36:20  cchun
 * Add:创建
 *
 * Revision 1.3  2011/07/27 07:28:27  cchun
 * Refactor:移动importDataTypes()到DataTemplateDao
 *
 * Revision 1.2  2011/01/10 09:17:07  cchun
 * Update:规范虚拟IED文件
 *
 * Revision 1.1  2011/01/07 09:45:08  cchun
 * Add:添加虚拟IED数据操作类
 *
 */
public class VirtualIEDDao {
	
	private static VirtualIEDDao dao = new VirtualIEDDao();
	private Element elementIED = null;
	private Element datatypeTemplates = null;
	
	private VirtualIEDDao() {
		String package1 = VirtualIEDDao.class.getPackage().getName();
		String path = package1.replaceAll("\\.", "\\/") + "/virtual.icd";
		Document doc = XMLFileManager.loadXMLFile(VirtualIEDDao.class, path);
		Element root = doc.getRootElement();
		elementIED = root.element("IED"); 
		datatypeTemplates = root.element("DataTypeTemplates");
	}
	
	public static VirtualIEDDao getInstance() {
		if (dao == null)
			dao = new VirtualIEDDao();
		return dao;
	}
	
	/**
	 * 保存虚拟IED到xmldb
	 * @param subNet
	 * @param iedName
	 * @param apName
	 */
	public void addIED(String subNet, String iedName, String apName) {
		// 通信
		CommunicationDAO.insertMMS(subNet, iedName, apName);
		// IED
		insertIED(iedName);
		// DataTypeTemplates
		insertDataTypes();
	}
	
	/**
	 * 保存IED节点到xmldb
	 * @param iedName
	 */
	private void insertIED(String iedName) {
		elementIED.addAttribute("name", iedName);
		XMLDBHelper.insertAfter("/scl:SCL/scl:Communication", elementIED); //$NON-NLS-1$
		HistoryManager.getInstance().markDevChanged(DevType.IED, OperType.ADD, iedName, null);
	}
	
	/**
	 * 保存虚拟装置数据模板
	 */
	private void insertDataTypes() {
		if (XMLDBHelper.existsNode(SCL.XPATH_DATATYPETEMPLATES + "/scl:LNodeType[@id='VIRTUAL_LD0_LLN0']"))
			return;
		DataTemplateDao.importDataTypes(datatypeTemplates);
	}
}
