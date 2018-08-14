/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.business.scl.das;

import java.util.List;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.navgtree.DoNode;
import com.shrcn.business.scl.model.navgtree.LNInfo;
import com.shrcn.business.scl.util.ReceivedInputBuilder;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-6-1
 */
/**
 * $Log: DescDao.java,v $
 * Revision 1.1  2013/03/29 09:36:24  cchun
 * Add:创建
 *
 * Revision 1.15  2012/03/09 07:35:51  cchun
 * Update:规范prefix和daName属性用法
 *
 * Revision 1.14  2011/11/22 09:20:58  cchun
 * Update:封装updateDoDesc()用于统一修改DO描述
 *
 * Revision 1.13  2011/09/16 10:14:58  cchun
 * Update:修改描述更新逻辑
 *
 * Revision 1.12  2011/08/15 08:33:11  cchun
 * Update:清理注释
 *
 * Revision 1.11  2011/08/10 06:26:59  cchun
 * Fix Bug:修改xpath
 *
 * Revision 1.10  2011/08/10 01:11:24  cchun
 * Fix Bug:修复描述导入为SDI添加了dU的bug
 *
 * Revision 1.9  2011/08/09 02:39:50  cchun
 * Update:SDI不需要dU
 *
 * Revision 1.8  2011/04/27 07:40:34  cchun
 * Fix Bug:修复导入虚端子描述时没有更新DOI的desc和dU的bug
 * Revision 1.7 2010/12/29 06:43:28 cchun Update:整理代码
 * 
 * Revision 1.6 2010/12/20 02:32:24 cchun Refactor:删除数据库异常处理
 * 
 * Revision 1.5 2010/11/08 08:32:22 cchun Update: 清除没有用到的代码
 * 
 * Revision 1.4 2010/11/04 09:26:45 cchun Update:添加依据描述更新描述
 * 
 * Revision 1.3 2010/09/07 09:59:33 cchun Update:更换过时接口
 * 
 * Revision 1.2 2010/09/03 02:37:59 cchun Update:修改数据项描述时按doName进行修改
 * 
 * Revision 1.1 2010/06/18 09:49:22 cchun Add:添加描述导出、导入功能
 * 
 */
public class DescDao extends IEDEditDAO {

	protected String lDeviceXpath;

	/**
	 * @param iedName
	 * @param apName
	 * @param ldInst
	 */
	public DescDao(String iedName, String apName, String ldInst) {
		super(iedName, apName, ldInst);
		lDeviceXpath = super.iedXPath + "/scl:AccessPoint";
		if (apName != null && apName.trim().length() > 0) {
			lDeviceXpath += "[@name='" + apName + "']";
		}
		lDeviceXpath += "//LDevice";
		if (ldInst != null && ldInst.trim().length() > 0) {
			lDeviceXpath += "[@inst='" + ldInst + "']";
		}
	}

	public void plusAttribute(String select, String attName, String value) {
		XMLDBHelper.appendAttribute(select, attName, value);
	}

	public void updateDoDescAttr(String newName, LNInfo lnInfo,
			DoNode doNode) {
		StringBuffer querySelect = new StringBuffer(lDeviceXpath);
		organiseDoQuery(querySelect, lnInfo, doNode);
		XMLDBHelper.saveAttribute(querySelect.toString(), "desc", newName); //$NON-NLS-1$
	}

	public void updateSDIDescAttr(String newName, LNInfo lnInfo, DoNode doNode) {
		StringBuffer querySelect = new StringBuffer(lDeviceXpath);
		organiseSDIQuery(querySelect, lnInfo, doNode);
		XMLDBHelper.saveAttribute(querySelect.toString(), "desc", newName); //$NON-NLS-1$
	}

	public void updateDaValue(String newName, LNInfo lnInfo, DoNode doNode) {
		StringBuffer ldSelect = new StringBuffer(lDeviceXpath);
		organiseDoQuery(ldSelect, lnInfo, doNode);
		StringBuffer doSelect = new StringBuffer(ldSelect.toString());
		// 修改DOI的dU值
		doSelect.append("/*[@name=\"dU\"]/Val");

		if (XMLDBHelper.existsNode(doSelect.toString())) {
			XMLDBHelper.update(doSelect.toString(), newName); 		// 修改dU
		} else {
			appendDu2DoElement(doSelect, newName, lnInfo, doNode);	// 添加dU
		}
	}
	
	private void appendDu2DoElement(StringBuffer querySelect, String newName,
			LNInfo lnInfo, DoNode doNode) {
		Element daiNode = constructDaDuElement(newName);

		// 清空select StringBuffer
		querySelect.delete(0, querySelect.length());
		querySelect.append(lDeviceXpath);
		organiseDoQuery(querySelect, lnInfo, doNode);
		XMLDBHelper.insertAsLast(querySelect.toString(), daiNode);
	}

	private Element constructDaDuElement(String msg) {
		DocumentFactory docFac = DocumentFactory.getInstance();
		Element daiNode = docFac.createElement("DAI");
		daiNode.addAttribute("name", "dU");

		Element valNode = docFac.createElement("Val");
		valNode.setText(msg);
		daiNode.add(valNode);
		return daiNode;
	}

	private void organiseDoQuery(StringBuffer querySelect, LNInfo lnInfo, DoNode doNode) {
		querySelect.append(SCL.getLNXPath(lnInfo.getPrefix(), lnInfo.getLnClass(), lnInfo.getInst()));
		if (doNode != null && !StringUtil.isEmpty(doNode.getName())) {
			querySelect.append("/DOI[@name=\"" + doNode.getName() + "\"]");
		}
	}

	private void organiseSDIQuery(StringBuffer querySelect, LNInfo lnInfo, DoNode doNode) {
		organiseDoQuery(querySelect, lnInfo, doNode);
		List<String> lstDaNames = doNode.getOtherDAName();
		if (lstDaNames != null && lstDaNames.size() > 0) {
			for (String name : lstDaNames) {
				if (StringUtil.isEmpty(name))
					continue;
				querySelect.append("/*[@name=\"" + name + "\"]");
//				break; //>???
			}
		}
	}

	/**
	 * 根据旧描述来修改描述内容
	 * 
	 * @param ln
	 *            LN元素
	 * @param doElement
	 *            DOI或SDI元素
	 */
	public void updateDescByOldDesc(Element ln, Element doElement,
			String newDesc) {
		XMLDBHelper.saveAttribute(getDOXPath(ln, doElement), "desc", newDesc);
	}

	/**
	 * 根据旧描述找到对应的DOI或SDI，然后再来修改其底下的DAI的dU
	 * 
	 * @param ln
	 * @param doElement
	 * @param newDesc
	 */
	public void updateDaiDu(Element ln, Element doElement, String newDesc) {
		String doXpath = getDOXPath(ln, doElement);
		Node du = doElement.selectSingleNode("./*[name()='DAI'][@name='dU']");
		if (du == null) {
			Element daiDu = constructDaDuElement(newDesc);
			XMLDBHelper.insertAsFirst(doXpath, daiDu);
		} else {
			XMLDBHelper.update(doXpath + "/scl:DAI[@name='dU']/scl:Val",
					newDesc);
		}
	}

	/**
	 * 根据LN来获得DOI/SDI的XPATH
	 * 
	 * @param ln
	 * @param doElement
	 * @return
	 */
	private String getDOXPath(Element ln, Element doElement) {
		StringBuffer xquery = new StringBuffer(lDeviceXpath);
		xquery.append(SCL.getLNXPath(ln));
		String doName = doElement.attributeValue("name", "");
		if (doElement.getParent().equals(ln)) {
			xquery.append("/scl:DOI[@name='" + doName + "']");
		} else {
			xquery.append(getAbsoluteXPath(ln, doElement));
		}
		return xquery.toString();
	}

	/**
	 * 获取SDI的绝对路径
	 * 
	 * @param ln
	 * @param sdiElement
	 * @return
	 */
	private String getAbsoluteXPath(Element ln, Element sdiElement) {
		String path = "";
		String tagName = sdiElement.getName();// !sdiElement.getParent().equals(ln)
		if ("LN0".equals(tagName) || "LN".equals(tagName))
			return path;
		String name = sdiElement.attributeValue("name", "");
		path += getAbsoluteXPath(ln, sdiElement.getParent()) + "/scl:"
				+ sdiElement.getName() + "[@name='" + name + "']";
		return path;
	}

	/**
	 * 替换DO描述
	 * @param path
	 * @param desc
	 * @param iedName
	 */
	public static boolean updateDoDesc(String path, String desc, String iedName) {
		if (StringUtil.isEmpty(path)) {
			return false;
		}
		ReceivedInputBuilder builder = new ReceivedInputBuilder(path, desc);
		builder.parse();
		DescDao descDao = new DescDao(iedName, null, builder.getInst());
		LNInfo lnInfo = builder.getLNInfo();
		DoNode doNode = builder.getDoNode();
		
		//若DO节点不存在，则无法修改描述
		StringBuffer querySelect = new StringBuffer(descDao.lDeviceXpath);
		descDao.organiseDoQuery(querySelect, lnInfo, doNode);
		if (XMLDBHelper.selectSingleNode(querySelect.toString()) == null)
			return false;
		
		// 更新do底下SDI描述
		if (doNode.getOtherDAName().size() > 0) {
			descDao.updateSDIDescAttr(desc, lnInfo, doNode);
		} else {
			// 更新do描述
			descDao.updateDoDescAttr(desc, lnInfo, doNode);
			// 更新do底下dU的值
			descDao.updateDaValue(desc, lnInfo, doNode);
		}
		return true;
	}
}
