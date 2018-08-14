/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.dom4j.Element;

import com.shrcn.business.scl.SCLConstants;
import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.scl.history.HistoryManager;
import com.shrcn.business.scl.history.MarkInfo.DevType;
import com.shrcn.business.scl.history.MarkInfo.OperType;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;


/**
 * 导入IID部分 导入对应IID文件的IED部分 并检查LNodeType是否存在 存在替换不存在插入
 * 判断是都统一建模，统一建模不对DA DO处理
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-7-8
 */
/*
 * 修改历史
 * $Log: IIDActionDao.java,v $
 * Revision 1.1  2013/03/29 09:36:20  cchun
 * Add:创建
 *
 * Revision 1.7  2013/03/11 07:12:28  cchun
 * Update:修改importIID()添加信号关联检查提示
 *
 * Revision 1.6  2012/04/19 10:24:19  cchun
 * Refactor:清除引用
 *
 * Revision 1.5  2012/03/09 07:35:51  cchun
 * Update:规范prefix和daName属性用法
 *
 * Revision 1.4  2012/01/13 08:40:03  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.3  2011/04/12 09:17:32  cchun
 * Update:sct.properties改为在第一次调用时加载
 *
 * Revision 1.2  2011/02/22 06:04:45  cchun
 * Update:去掉无用方法，增加getIedName()
 *
 * Revision 1.1  2010/11/08 03:57:04  cchun
 * Refactor:移动IIDActionDao类到common项目
 *
 * Revision 1.8  2010/11/08 03:08:14  cchun
 * Refactor:整理代码，去掉不必要的封装
 *
 * Revision 1.7  2010/11/04 08:33:14  cchun
 * Fix Bug:修改DataTypeTemplateDao为静态调用，避免数据不同步错误
 *
 * Revision 1.6  2010/10/18 02:40:56  cchun
 * Update:清理引用
 *
 * Revision 1.5  2010/09/29 09:33:48  cchun
 * Refactor:移动通用方法至IEDDAO类
 *
 * Revision 1.4  2010/09/07 10:01:53  cchun
 * Update:更换过时接口
 *
 * Revision 1.3  2010/09/03 03:37:11  cchun
 * Refactor:使用公共方法；清理注释
 *
 * Revision 1.2  2010/04/23 03:22:57  cchun
 * Update:历史记录接口添加oid参数
 *
 * Revision 1.1  2010/03/02 07:49:05  cchun
 * Add:添加重构代码
 *
 * Revision 1.24  2010/02/08 10:41:35  cchun
 * Refactor:完成第一阶段重构
 *
 * Revision 1.23  2010/01/21 08:47:20  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.22  2009/12/23 08:13:59  lj6061
 * 删除重复常量类，统一常量引用
 *
 * Revision 1.21  2009/12/21 06:36:25  cchun
 * Update:整理代码，纠正elements()用法错误
 *
 * Revision 1.20  2009/12/18 04:19:34  lj6061
 * 修正Elements()方法,调整代码
 *
 * Revision 1.19  2009/12/18 03:07:46  lj6061
 * 修正Elements()方法
 *
 * Revision 1.18  2009/12/14 10:26:45  wyh
 * 增加友好提示
 *
 * Revision 1.17  2009/12/10 07:32:49  cchun
 * Update:补充历史记录
 *
 * Revision 1.16  2009/12/09 06:06:53  wyh
 * 增加操作：替换IED
 *
 * Revision 1.15  2009/12/09 03:11:07  lj6061
 * 修改IID导入时关联引用检查BUg
 *
 * Revision 1.14  2009/12/09 02:57:32  lj6061
 * 修改IID导入时关联引用检查BUg
 *
 * Revision 1.13  2009/12/02 03:30:32  lj6061
 * 整理选项配置代码
 *
 * Revision 1.12  2009/11/10 07:02:50  lj6061
 * 导入IID文件时进行Inputs检查
 *
 * Revision 1.11  2009/11/02 05:09:03  lj6061
 * 整理代码 删除无用部分
 *
 * Revision 1.10  2009/10/21 06:16:21  cchun
 * Update:将Berkeley DB引用的dll从library工程中去掉，解决平台不兼容的问题
 *
 * Revision 1.9  2009/09/21 05:59:47  lj6061
 * 删除: Xpath带有空格
 *
 * Revision 1.8  2009/08/27 02:26:10  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.7  2009/08/18 09:40:26  cchun
 * Update:合并代码
 *
 * Revision 1.4.2.4  2009/08/14 03:09:03  lj6061
 * 给DOI实例对话框添加注释
 *
 * Revision 1.4.2.3  2009/08/07 03:38:46  lj6061
 * 修改插入节点方法
 *
 * Revision 1.4.2.2  2009/08/06 02:07:32  lj6061
 * 替换通过Doc方法插入节点
 *
 * Revision 1.4.2.1  2009/07/24 07:11:09  cchun
 * Update:切换数据库Sedna
 *
 * Revision 1.4  2009/07/16 07:12:58  lj6061
 * 整理代码
 * 1.删除未被引用的对象和方法
 * 2 修正空指针的异常
 *
 * Revision 1.3  2009/07/16 06:34:04  lj6061
 * 整理代码
 * 1.删除未被引用的对象和方法
 * 2 修正空指针的异常
 *
 * Revision 1.2  2009/07/14 07:59:12  lj6061
 * 添加测试用例
 *
 * Revision 1.1  2009/07/09 05:38:31  lj6061
 * 导入IID文件
 *
 */
public class IIDActionDao {
	
	private Element root = null;
	private String iedName = null;
	private List<String> allName = IEDDAO.getIEDNames();
	private List<String> iedforinputscheck = new ArrayList<String>();;

	private String replacedIEDName = null;
	
	public IIDActionDao(String filePath) {
		root  = DOM4JNodeHelper.getImportElement(filePath);
	}
	public IIDActionDao(Element root) {
		this.root  = root;
	}
	/**
	 * 根据配置文件中选项，基于非统一建模的工程，保存时清理模板
	 */
	private boolean isUnified(){
		if (SCTProperties.getInstance().getTempletModel().equals(SCLConstants.NOT_UNIFIED_TEMPLET))
			return false;
		else
			return true;
	} 
	
	/**
	 * 插入IED部分
	 */
	private void insertIED(){
		Element elementIED = (Element) 
		root.selectSingleNode("/*[name()='SCL']/*[name()='IED']"); //$NON-NLS-1$
		if(elementIED == null) return;
		iedName  = elementIED.attributeValue("name"); //$NON-NLS-1$
		if (iedName == null)
			return;
		
		// 如果是由替换IED引用的操作，则iedName改为被替换IED的名称
		if(replacedIEDName != null){
			elementIED.attribute("name").setText(replacedIEDName); //$NON-NLS-1$
			iedName = replacedIEDName;
		}
		
		String select = "/scl:SCL/scl:IED[@name='" + iedName + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		if (XMLDBHelper.existsNode(select)) {
			XMLDBHelper.replaceNode(select, elementIED);
		} else {
			select = SCL.XPATH_COMMUNICATION;
			XMLDBHelper.insertAfter(select, elementIED);
		}
	}
	
	/**
	 * 替换或插入LNodeType
	 * 
	 * @throws XmlException
	 */
	private void replaceLNodeType(Element dataTypeTemplates){
		List<?> lNodeType = dataTypeTemplates.elements("LNodeType"); //$NON-NLS-1$
		for (Object object : lNodeType) {
			Element ele = (Element) object;
			String id = ele.attributeValue("id"); //$NON-NLS-1$
			String select = "/scl:SCL/scl:DataTypeTemplates/scl:LNodeType[@id='" + id + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			boolean lnodeExist = XMLDBHelper.existsNode(select);
			if (!lnodeExist) {
				XMLDBHelper.insertAfter("/scl:SCL/scl:DataTypeTemplates/scl:LNodeType[last()]", ele); //$NON-NLS-1$
			} else {
				XMLDBHelper.replaceNode(select,ele);
			}
		}
	}
	
	/**
	 * 替换或插入DAType
	 * 
	 * @throws XmlException
	 */
	private void replaceDAType(Element dataTypeTemplates) {
		List<?> lNodeType = dataTypeTemplates.elements("DAType"); //$NON-NLS-1$
		for (Object object : lNodeType) {
			Element ele = (Element) object;
			String id = ele.attributeValue("id"); //$NON-NLS-1$
			String select = "/scl:SCL/scl:DataTypeTemplates/scl:DAType[@id='" + id + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			boolean lnodeExist = XMLDBHelper.existsNode(select);
			if (!lnodeExist) {
				XMLDBHelper.insertAfter("/scl:SCL/scl:DataTypeTemplates/scl:DAType[last()]", ele); //$NON-NLS-1$
			} else {
				XMLDBHelper.replaceNode(select, ele);
			}
		}
	}
	
	/**
	 * 替换或插入DOType
	 * 
	 */
	private void replaceDOType(Element dataTypeTemplates){
		List<?> lNodeType = dataTypeTemplates.elements("DOType"); //$NON-NLS-1$
		for (Object object : lNodeType) {
			Element ele = (Element) object;
			String id = ele.attributeValue("id"); //$NON-NLS-1$
			String select = "/scl:SCL/scl:DataTypeTemplates/scl:DOType[@id='" + id + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			boolean lnodeExist = XMLDBHelper.existsNode(select);
			if (!lnodeExist) {
				XMLDBHelper.insertAfter("/scl:SCL/scl:DataTypeTemplates/scl:DOType[last()]", ele); //$NON-NLS-1$
			} else {
				XMLDBHelper.replaceNode(select,ele);
			}
		}
	}
	
	/**
	 * 工程中模板只存在框架，无内容的需要整体替换
	 * 
	 * @param temp
	 */
	private void replaceTemplat(Element temp){
		XMLDBHelper.replaceNode(SCL.XPATH_DATATYPETEMPLATES, temp);
	}
	
	/**
	 * 导入IID文件操作 统一建模和非统一建模操作
	 * 
	 */
	public String importIID() {
		insertIED();
		HistoryManager.getInstance().markDevChanged(DevType.IED, OperType.UPDATE, iedName, null);
		boolean isCheck = isCheckTemplats();
		if(root == null) 
			return "";
		Element dataTypeTemplates = root.element("DataTypeTemplates"); //$NON-NLS-1$
		if (isCheck) {
			if (dataTypeTemplates == null)
				return "";
			replaceLNodeType(dataTypeTemplates);
			if (!isUnified()) {// 非统一建模之处理LNodetype
				replaceDAType(dataTypeTemplates);
				replaceDOType(dataTypeTemplates);
			}
		}else{
			replaceTemplat(dataTypeTemplates);
		}
		String msg = inputscheckforied(iedName);
		if (!StringUtil.isEmpty(msg)) {
			msg = "\n\n-------IID信号关联检查开始-------"
				+ msg + "\n-------IID信号关联检查结束-------\n";
			return msg;
		}
		return "";
	}
	
	/**
	 * 库里存在的模板，如果模板信息中不存在4个类型
	 * 则有可能该工程为新建工程或不存在模板
	 * 
	 * @param Element dbTemp
	 */
	private boolean isCheckTemplats(){
		Element dbTemp =  DataTypeTemplateDao.getDataTypeTemplates();// 当前工程的模板信息
		boolean check = false;
		if (dbTemp != null) {
			int lnodeSize = dbTemp.elements("LNodeType").size(); //$NON-NLS-1$
			int doSize = dbTemp.elements("DAType").size(); //$NON-NLS-1$
			int daSize = dbTemp.elements("DOType").size(); //$NON-NLS-1$
			int enumSize = dbTemp.elements("EnumType").size(); //$NON-NLS-1$
			if (lnodeSize != 0 && doSize != 0 && daSize != 0 && enumSize != 0) {
				check = true;
			}
		}
		return check;
	}
	
	// Inputs关联信号引用检查
	@SuppressWarnings("unchecked")
	public String inputscheckforied(String iedName){
		StringBuffer report = new StringBuffer();
		List<Element> ldevices = IEDDAO.queryLDevices(iedName);
		if(replacedIEDName != null){
			report.append("\t\n"+Messages.getString("IIDActionDao.for.ied")+iedName+Messages.getString("IIDActionDao.begin.signal.relation.ref.check")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		for (Element ldevice : ldevices) {
			// tInputs存在于tAnyLN中
			List<Element> lnList = new LinkedList<Element>();
			lnList.addAll(ldevice.elements("LN0")); //$NON-NLS-1$
			lnList.addAll(ldevice.elements("LN")); //$NON-NLS-1$
			for (Element ln : lnList) {
				Element inputs = ln.element("Inputs"); //$NON-NLS-1$
				if (inputs != null) {
					List<?> extRefs = inputs.elements("ExtRef"); //$NON-NLS-1$
					// ExtRef中intAddr、prefix和daName是可选的
					for (Object obj1 : extRefs) {
						Element extRef = (Element) obj1;
						String exiedName = extRef.attributeValue("iedName"); //$NON-NLS-1$
						String exldInst = extRef.attributeValue("ldInst"); //$NON-NLS-1$
						String exlnClass = extRef.attributeValue("lnClass"); //$NON-NLS-1$
						String exlnInst = extRef.attributeValue("lnInst"); //$NON-NLS-1$
						String exdoName = extRef.attributeValue("doName"); //$NON-NLS-1$
						String exdaName = extRef.attributeValue("daName"); //$NON-NLS-1$
						
						// 首先确定exiedName装置的存在性
						if(!allName.contains(exiedName)){
								if(!iedforinputscheck.contains(exiedName)){
									report.append("\t\n"+Messages.getString("IIDActionDao.doesnot.exist.in.scd") + exiedName); //$NON-NLS-1$ //$NON-NLS-2$
									iedforinputscheck.add(exiedName);
								}
							continue;
						}
						String daAtt = SCL.getDaAtts(exdaName);
						List<Element> list = getFcda(exiedName, exldInst, exlnClass, exlnInst, exdoName, daAtt);
						if(list.size() == 0) {
							report.append("\t\n"+Messages.getString("IIDActionDao.lack.ied") + exiedName + Messages.getString("IIDActionDao.doesnot.exist.fcda.like")+ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
									"  ldInst:"+exldInst+"  lnClass:"+exlnClass+ //$NON-NLS-1$ //$NON-NLS-2$
									"  lnInst:"+exlnInst+"  doName:"+exdoName+"  daName:" + exdaName); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						}
					}
				} else {
					continue;
				}
			}
		}
		iedforinputscheck.clear();
		return report.toString();
	}
	
	public List<Element> getFcda(String exiedName,String exldInst,String exlnClass,String exlnInst,String exdoName,String daAtt){
		List<Element> list = new ArrayList<Element>();
		if (Constants.XQUERY){
			String xQuery = "for $fcda in " + XMLDBHelper.getDocXPath() + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						"/scl:SCL/scl:IED[@name='" + exiedName + "']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + exldInst + "']" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						"/scl:LN0/scl:DataSet/scl:FCDA[@lnClass='" + exlnClass + "']" + //$NON-NLS-1$ //$NON-NLS-2$
						"[@lnInst='" + exlnInst + "'][@doName='" + exdoName + "']" + daAtt +
						" return $fcda"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			list = XMLDBHelper.queryNodes(xQuery);
		} else {
			String xpath = "/SCL/IED[@name='" + exiedName + "']/AccessPoint/Server/LDevice[@inst='" + exldInst +
					"']/LN0/DataSet/FCDA[@lnClass='" + exlnClass + "']" + 
						"[@lnInst='" + exlnInst + "'][@doName='" + exdoName + "']" + daAtt;
			list = XMLDBHelper.selectNodes(xpath);
		}
		return list;
	}
	public String getIedName() {
		return iedName;
	}
}
