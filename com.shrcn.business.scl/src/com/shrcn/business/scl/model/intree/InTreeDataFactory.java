/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.intree;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.progress.IElementCollector;

import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.scl.check.SignalsChecker;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.intree.IInnerTreeEntry.DA_FC;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-5
 */
/*
 * 修改历史
 * $Log: InTreeDataFactory.java,v $
 * Revision 1.38  2013/11/13 02:09:06  cxc
 * update:每次加载时清除存储的ldName，lnName，doName，daName信息
 *
 * Revision 1.37  2013/11/13 00:18:09  cxc
 * update:增加为关联过的信号打上标记的功能
 *
 * Revision 1.36  2012/09/03 07:05:58  cchun
 * Update:为InTreeEntry增加序号属性
 *
 * Revision 1.35  2012/03/09 07:35:52  cchun
 * Update:规范prefix和daName属性用法
 *
 * Revision 1.34  2012/01/17 08:50:28  cchun
 * Update:使用更加安全的xpath形式
 *
 * Revision 1.33  2011/10/31 09:04:21  cchun
 * Update:清理注释
 *
 * Revision 1.32  2011/08/24 08:15:50  cchun
 * Update:按虚端子类型过滤
 *
 * Revision 1.31  2010/11/08 08:32:54  cchun
 * Update: 清除没有用到的代码
 *
 * Revision 1.30  2010/11/08 07:14:03  cchun
 * Update:清理引用
 *
 * Revision 1.29  2010/09/07 10:00:24  cchun
 * Update:更换过时接口
 *
 * Revision 1.28  2010/04/21 06:45:34  cchun
 * Fix Bug:修复DO节点xpath错误
 *
 * Revision 1.27  2010/04/16 06:57:04  cchun
 * Update:修改Struct属性数据类型错误
 *
 * Revision 1.26  2010/04/12 01:56:16  cchun
 * Update:修改提示
 *
 * Revision 1.25  2010/03/29 02:44:41  cchun
 * Update:提交
 *
 * Revision 1.24  2010/03/16 12:16:51  cchun
 * Update: 更新
 *
 * Revision 1.23  2009/12/21 06:36:23  cchun
 * Update:整理代码，纠正elements()用法错误
 *
 * Revision 1.22  2009/11/13 07:18:12  cchun
 * Update:完善关联表格功能
 *
 * Revision 1.21  2009/11/06 07:22:11  cchun
 * Fix Bug:修复了内部信号树DO中出现了struct嵌套导致的bug
 *
 * Revision 1.20  2009/10/14 04:05:09  cchun
 * Fix Bug:修改了MX下出现mag.f.i的问题
 *
 * Revision 1.19  2009/08/17 07:24:28  cchun
 * Update:修改对Struct属性数据的处理，将树层次拉直为一级(合并)
 *
 * Revision 1.18  2009/08/17 06:16:36  cchun
 * Update:添加对Struct类型DA的处理
 *
 * Revision 1.17  2009/08/05 05:47:51  cchun
 * Update:合并代码
 *
 * Revision 1.16  2009/08/03 11:56:56  cchun
 * Update:合并信号关联增加上移、下移功能
 *
 * Revision 1.14.2.2  2009/08/03 11:33:57  cchun
 * Update:为内部信号增加ST,MX过滤功能
 *
 * Revision 1.14.2.1  2009/07/28 01:34:11  cchun
 * Update:合并代码
 *
 * Revision 1.15  2009/07/27 06:37:11  cchun
 * UPdate:清除缓存
 *
 * Revision 1.14  2009/07/08 04:13:40  cchun
 * Update:为适应LN下无实例化后的DOI的情况，特增加从数据模板处获取内部信号值
 *
 * Revision 1.13  2009/06/18 08:06:55  lj6061
 * 内部外部信号图标
 *
 * Revision 1.12  2009/06/18 00:43:03  cchun
 * Fix Bug:修改fillLNByType()方法中XQuery语句bug
 *
 * Revision 1.11  2009/06/11 07:48:00  cchun
 * 添加可拖拽图标
 *
 * Revision 1.10  2009/05/19 09:24:48  cchun
 * Fix Bug:解决新增数据集后出现空指针的bug
 *
 * Revision 1.9  2009/05/18 09:44:23  cchun
 * Update:添加内部视图根据LD过滤功能
 *
 * Revision 1.8  2009/05/18 05:58:40  cchun
 * Update:添加LN属性
 *
 * Revision 1.7  2009/05/12 06:09:04  cchun
 * Update:添加节点描述，修改DO信息为IED实例化后的数据
 *
 * Revision 1.6  2009/05/08 12:07:18  cchun
 * Update:完善外部、内部信号视图
 *
 * Revision 1.5  2009/05/07 10:31:59  lj6061
 * 修改getDataType为null
 *
 * Revision 1.3  2009/05/07 03:03:12  cchun
 * Update:修改懒加载实现
 *
 * Revision 1.2  2009/05/06 11:33:09  cchun
 * Update:优化LN加载效率
 *
 * Revision 1.1  2009/05/06 06:39:03  cchun
 * Add:添加内部信号树节点对象
 *
 */
public class InTreeDataFactory {
	
	/** 单例对象 */
	private static volatile InTreeDataFactory instance = new InTreeDataFactory();
	//数据模板DO信息缓存
	private Map<String, Element> doNodeCache = new HashMap<String, Element>();
	//数据模板LNodeType信息缓存
	private Map<String, Element> lnTypeCache = new HashMap<String, Element>();
	private boolean onlyGGIO = true;
	private boolean loadAll = false;
	private String currIEDName = null;
	
	//记录ln，do，da的名字
	private String lnName = null;
	private String doName = null;
	private String daName = null;
	
	
	
	//查询出来的intAddr信息缓存
	private List<String> innerAddrs = new ArrayList<String>();

	private static final MessageFormat LNODETYPE_PATH = new MessageFormat("/scl:SCL/scl:DataTypeTemplates/scl:LNodeType[@id=''{0}'']");
	private static final MessageFormat DOTYPE_PATH = new MessageFormat("/scl:SCL/scl:DataTypeTemplates/scl:DOType[@id=''{0}'']");
	private static final MessageFormat DATYPE_PATH = new MessageFormat("/scl:SCL/scl:DataTypeTemplates/scl:DAType[@id=''{0}'']");

	/**
	 * 单例模式私有构造函数
	 */
	private InTreeDataFactory(){
	}

	/**
	 * 获取单例对象
	 */
	public static InTreeDataFactory getInstance(){
		if(null == instance) {
			synchronized (InTreeDataFactory.class) {
				if(null == instance) {
					instance = new InTreeDataFactory();
				}
			}
		}
		return instance;
	}

	private String getAttribute(Element node, String attName) {
		return node.attributeValue(attName);
	}
	
	/**
	 * 按iedName, ldInst构造内部信号树
	 * @param iedName
	 * @param ldInst
	 * @return
	 */
	public List<IInnerTreeEntry> createTreeData(String iedName, String ldInst, boolean isAll) {
		List<IInnerTreeEntry> treeData = new ArrayList<IInnerTreeEntry>();
		if(null == iedName)
			return treeData;
		
		String ldXPath = "/scl:SCL/scl:IED[@name='" + iedName + 
			"']/scl:AccessPoint/scl:Server/scl:LDevice";
		if (!isAll) {
			ldXPath = ldXPath + "[@inst='" + ldInst + "']";
		}
		List<Element> lds = XMLDBHelper.selectNodes(ldXPath);
		for (Element ld : lds) {
			IInnerTreeEntry entryLD = createLDTreeEntry(iedName, ld);
			treeData.add(entryLD);
		}
		
		//记录当前IED名称
		this.currIEDName = iedName;
		this.lnName = "";
		this.doName = "";
		this.daName = "";
		return treeData;
	}
	
	private IInnerTreeEntry createLDTreeEntry(String iedName, Element ld) {
		String ldInst = ld.attributeValue("inst");
		String ldXPath = "/scl:SCL/scl:IED[@name='" + iedName + 
				"']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']";
		String xpath = "./*/scl:Inputs/scl:ExtRef";
		List<Element> extRefs = DOM4JNodeHelper.selectNodes(ld, xpath);
		if(innerAddrs.size()>0) {
			innerAddrs.clear();
		}
		if (extRefs.size()>0) {
			for (Element ele : extRefs) {
				innerAddrs.add(ele.attributeValue("intAddr"));
			}
		}
		String desc = getAttribute(ld, "desc");
		IInnerTreeEntry entryLD = new InTreeEntry(ldInst, ldXPath, 
				ImageConstants.LDEVICE, ldInst, IInnerTreeEntry.LD_ENTRY);
		entryLD.setDesc(desc);
		fillLDTreeData(ld, entryLD);
		return entryLD;
	}

	/**
	 * 为指定LD节点添加子节点(LN)信息
	 * @param elLD scd文件LDevice节点
	 * @param entryLD LD树节点
	 */
	private void fillLDTreeData(Element elLD, IInnerTreeEntry entryLD) {
		SCTProperties proper = SCTProperties.getInstance();
		String ldXPath = entryLD.getXPath();
		List<?> lns = elLD.elements();
		int num = 1;
		for (Object obj : lns) {
			Element ln = (Element) obj;
			String tagName = ln.getName();
			if (!"LN0".equals(tagName) && !"LN".equals(tagName))
				continue;
			String prefix = getAttribute(ln, "prefix");
			String lnClass = getAttribute(ln, "lnClass");
			String inst = getAttribute(ln, "inst");
			String lnType = getAttribute(ln, "lnType");
			String desc = getAttribute(ln, "desc");

			if (onlyGGIO && !proper.isVTLnClass(lnClass))
				continue;
			
			String name = SCL.getLnName(ln);
			String xpath = ldXPath + SCL.getLNXPath(ln);
			
			IInnerTreeEntry entryLN = new InTreeEntry(name, xpath, ImageConstants.LNODE, name, IInnerTreeEntry.LN_ENTRY, num);
			num ++;
			entryLN.setDesc(desc);
			entryLN.setDataType(lnType);
			entryLN.setAttribute(IInnerTreeEntry.LN_PRIFIX, prefix);
			entryLN.setAttribute(IInnerTreeEntry.LN_CLASS, lnClass);
			entryLN.setAttribute(IInnerTreeEntry.LN_INST, inst);
			entryLD.addChild(entryLN);
		}
	}
	
	private Element getLNNodeByEntry(IInnerTreeEntry entryLN) {
		IInnerTreeEntry entryLD = (IInnerTreeEntry) entryLN.getParent();
		Element ndLD = XMLDBHelper.selectSingleNode(entryLD.getXPath());
		String prefix = entryLN.getAttribute(IInnerTreeEntry.LN_PRIFIX);
		String lnClass = entryLN.getAttribute(IInnerTreeEntry.LN_CLASS);
		String inst = entryLN.getAttribute(IInnerTreeEntry.LN_INST);
		return DOM4JNodeHelper.selectSingleNode(ndLD, "." + SCL.getLNXPath(prefix, lnClass, inst));
	}
	
	/**
	 * 为指定LN节点添加子节点信息
	 * @param entryLN LN节点对象
	 */
	public void fillLNTreeData(String ldName, IInnerTreeEntry entryLN, IElementCollector collector, IProgressMonitor monitor) {
		String xpath = entryLN.getXPath();
		String dataType = entryLN.getDataType();
		Element lnNode = getLNNodeByEntry(entryLN);
		lnName = entryLN.getName();
		String name = null;
		IInnerTreeEntry entryLN_FC = null; 
		if(null != monitor)
			monitor.beginTask("Loading", DA_FC.values().length);
		clearCaches();
		for (DA_FC fc : DA_FC.values()) {
			if (!isLoadAll() && (fc != DA_FC.ST && fc != DA_FC.MX))
				continue;
			name = fc.toString();
			entryLN_FC = new InTreeEntry(name, xpath, (name.equals("ST")||name.equals("MX"))?ImageConstants.DRAGABLE:ImageConstants.FC, 
					name, IInnerTreeEntry.LN_FC_ENTRY);
			entryLN_FC.setDataType(dataType);
			if (!fillLNByType(ldName, entryLN_FC, lnNode, fc))
				return;
			if (entryLN_FC.getChildren().size() > 0) {
				entryLN.addChild(entryLN_FC);
				if (null != monitor) {
					monitor.worked(1);
					if (null != collector)
						collector.add(entryLN_FC, monitor);
				}
			}
		}
		if (null != monitor)
			monitor.done();
	}
	
	/**
	 * 按照功能约束(fc)为LN树节点添加子节点(DOI)信息
	 * @param entryLNFC
	 * @param lnNode
	 * @param dafc
	 */
	private boolean fillLNByType(String ldName, IInnerTreeEntry entryLNFC, Element lnNode, DA_FC dafc) {
		String lnXPath = entryLNFC.getXPath();
		String lntString = entryLNFC.getDataType();
		List<Element> dois = DOM4JNodeHelper.selectNodes(lnNode, "./*[name()='DOI']");
		for(Element doi:dois) {
			String name = getAttribute(doi, "name");
			String desc = SCL.getDOIDesc(doi);
			if(!isLoadAll() && SignalsChecker.isExcludedDO(name))
				continue;
			IInnerTreeEntry entryDOI = new InTreeEntry(name, 
					lnXPath + "/scl:DOI[@name='" + name + "']",ImageConstants.DO,			
					name, IInnerTreeEntry.DO_ENTRY);
			doName = name;
			String ref = ldName + "/" + lnName + "." + doName;
			setSglRef(entryDOI,ref);
			
			
			
			Element lnTypeNode = getLnTypeNode(lntString);
			if(null == lnTypeNode) {
				DialogHelper.showAsynError("类型为" + lntString + "的LN缺少定义.");
				return false;
			}
			Element doNode = DOM4JNodeHelper.selectSingleNode(lnTypeNode, 
								"./*[name()='DO'][@name='" + name + "']");
			if(null == doNode) {
				DialogHelper.showAsynError("类型为" + lntString + "的LN下不存在名为" + name + "的DO.");
				return false;
			}
			String type = doNode.attributeValue("type");
			entryDOI.setDataType(type);
			entryDOI.setDesc(desc);
			fillDOTreeData(ldName, entryDOI, dafc);
			
			if(entryDOI.getChildren().size() > 0)
				entryLNFC.addChild(entryDOI);
		}
		// 为适应LN下无实例化后的DOI的情况，特增加从数据模板处获取内部信号值
		if(dois.size() == 0) {
			String lnType = getAttribute(lnNode, "lnType");
			Element lnTypeNode = getLnTypeNode(lnType);
			dois = DOM4JNodeHelper.selectNodes(lnTypeNode, "./*[name()='DO']");
			for(Element DO:dois) {
				String name = getAttribute(DO, "name");
				String desc = getAttribute(DO, "desc");
				IInnerTreeEntry entryDOI = new InTreeEntry(name, 
						lnXPath + "/scl:DOI[@name='" + name + "']",ImageConstants.DO,			
						name, IInnerTreeEntry.DO_ENTRY);
				doName = name;
				String ref = ldName + "/" + lnName + "." + doName;
				setSglRef(entryDOI,ref);
				
				String type = getAttribute(DO, "type");
				entryDOI.setDataType(type);
				entryDOI.setDesc(desc);
				fillDOTreeData(ldName, entryDOI, dafc);
				if(entryDOI.getChildren().size() > 0)
					entryLNFC.addChild(entryDOI);
			}
		}
		return true;
	}
	
	/**
	 * 根据lnType属性获取数据模板中LNodeType节点
	 * @param lnType
	 * @return LNodeType节点
	 */
	private Element getLnTypeNode(String lnType) {
		String lnTypeXPath = LNODETYPE_PATH.format(new Object[]{lnType});
		if(lnTypeCache.containsKey(lnTypeXPath))
			return lnTypeCache.get(lnTypeXPath);
		Element lnTypeNode = XMLDBHelper.selectSingleNode(lnTypeXPath);
		lnTypeCache.put(lnTypeXPath, lnTypeNode);
		return lnTypeNode;
	}
	
	private Element getDoTypeNode(String doType) {
		String doTypeXPath = DOTYPE_PATH.format(new Object[]{doType});
		Element dotype = null;
		if(doNodeCache.containsKey(doTypeXPath))
			dotype = doNodeCache.get(doTypeXPath).createCopy();
		else {
			dotype = XMLDBHelper.selectSingleNode(doTypeXPath);
			doNodeCache.put(doTypeXPath, dotype.createCopy());
		}
		return dotype;
	}
	
	private Element getStructTypeNode(String doType) {
		String structDAPath = DATYPE_PATH.format(new Object[]{doType});
		Element dotype = null;
		if(doNodeCache.containsKey(structDAPath))
			dotype = doNodeCache.get(structDAPath).createCopy();
		else {
			dotype = XMLDBHelper.selectSingleNode(structDAPath);
			doNodeCache.put(structDAPath, dotype.createCopy());
		}
		return dotype;
	}
	
	/**
	 * 清除缓存
	 */
	public void clearCaches() {
		lnTypeCache.clear();
		doNodeCache.clear();
	}

	
	/**
	 * 为指定DO节点添加子节点信息
	 * @param entryDO DO节点对象
	 */
	private void fillDOTreeData(String ldName, IInnerTreeEntry entryDO, DA_FC dafc) {
		String dataType = entryDO.getDataType();
		Element dotype = getDoTypeNode(dataType);
		// 判断DO是否存在
		if(null == dotype) {
			SCTLogger.error("类型为" + dataType + "的DO缺少定义！");
			return;
		}
		if(entryDO.getDesc() == null) {
			String desc = getAttribute(dotype, "desc");
			entryDO.setDesc(desc);
		}
		String doTypeXPath = DOTYPE_PATH.format(new Object[]{dataType});
		
		List<?> dais = dotype.elements();
		for (Object obj : dais) {
			Element dai = (Element) obj;
			String daType = dai.getName();
			String name = getAttribute(dai, "name");
			String bType = getAttribute(dai, "bType");
			IInnerTreeEntry entryDAI = null;
			if("DA".equals(daType)) {
				String fc = getAttribute(dai, "fc");
				if(!dafc.toString().equals(fc))
					continue;
				entryDAI = new InTreeEntry(name, doTypeXPath + "/scl:DA[@name='" + name + 
						"']", ImageConstants.INPUT_SIG, name, IInnerTreeEntry.DA_ENTRY);
				
				daName = name;
				String ref = ldName + "/" + lnName + "." + doName + "." + daName;
				setSglRef(entryDAI,ref);
				
				
				entryDO.addChild(entryDAI);
				if(isStruct(bType)) {  // 结构体
					entryDAI.setDataType(getAttribute(dai, "type"));
					fillStructTreeData(ldName, entryDAI, dafc);
					entryDO.removeChild(entryDAI);
				} else {			  // 简单类型
					entryDAI.setDataType(bType);
				}
			}
			if("SDO".equals(daType)) {
				entryDAI = new InTreeEntry(name, doTypeXPath + "/scl:SDO[@name='" + name + 
						"']", ImageConstants.INPUT_SIG, name, IInnerTreeEntry.SDO_ENTRY);
				entryDAI.setDataType(getAttribute(dai, "type"));
				fillDOTreeData(ldName, entryDAI, dafc);
				if(entryDAI.getChildren().size() > 0)
					entryDO.addChild(entryDAI);
			}
		}
	}
	
	/**
	 * 解析结构体数据
	 * @param entryDO
	 * @param dafc
	 */
	private void fillStructTreeData(String ldName, IInnerTreeEntry entryDO, DA_FC dafc) {
		String dataType = entryDO.getDataType();
		Element structType = getStructTypeNode(dataType);
		if(null == structType) {
			SCTLogger.error("类型为" + dataType + "的DA缺少定义！");
			return;
		}
		if(entryDO.getDesc() == null) {
			String desc = getAttribute(structType, "desc");
			entryDO.setDesc(desc);
		}
		String structDAPath = DATYPE_PATH.format(new Object[]{dataType});
		List<?> dais = structType.elements("BDA");
		for (Object obj : dais) {
			Element dai = (Element) obj;
			String name = getAttribute(dai, "name");
			String bType = getAttribute(dai, "bType");
			name = entryDO.getName().concat("." + name);
			InTreeEntry entryDAI = null;
			if(isStruct(bType)) {
				entryDAI = new InTreeEntry(name, structDAPath + "/scl:BDA[@name='" + name + 
						"']", ImageConstants.INPUT_SIG, name, IInnerTreeEntry.DA_ENTRY);
				daName = name;
				String ref = ldName + "/" + lnName + "." + doName + "." + daName;
				setSglRef(entryDAI,ref);
				
				entryDAI.setDataType(getAttribute(dai, "type"));
				entryDO.getParent().addChild(entryDAI);	//放到这里是为了避免空指针
				fillStructTreeData(ldName, entryDAI, dafc);
				entryDO.getParent().removeChild(entryDAI);
			} else {
				//为了和DO相区分，属性之间直接用"."分隔，而不是再加一级树节点
//				name = entryDO.getName().concat("." + name);
				entryDAI = new InTreeEntry(name, structDAPath + "/scl:BDA[@name='" + name + 
						"']", ImageConstants.INPUT_SIG, name, IInnerTreeEntry.DA_ENTRY);
				daName = name;
				String ref = ldName+"/"+lnName+"."+doName+"."+daName;
				setSglRef(entryDAI,ref);
				
				entryDAI.setDataType(bType);
				entryDO.getParent().addChild(entryDAI);
//				InTreeEntry entryDAI = (InTreeEntry) entryDO;
//				String newName = entryDO.getName().concat("." + name);
//				entryDAI.setName(newName);
//				entryDAI.setToolTip(newName);
//				entryDAI.setXpath(structDAPath + "/scl:BDA[@name='" + name + "']");
			}
//			entryDO.getParent().addChild(entryDAI);
//			structType.remove(dai);
		}
	}
	
	public boolean isStruct(String bType) {
		return "Struct".equals(bType);
	}

	public boolean isOnlyGGIO() {
		return onlyGGIO;
	}

	public void setOnlyGGIO(boolean onlyGGIO) {
		this.onlyGGIO = onlyGGIO;
	}

	public String getCurrIEDName() {
		return currIEDName;
	}

	public void setCurrIEDName(String currIEDName) {
		this.currIEDName = currIEDName;
	}

	public boolean isLoadAll() {
		return loadAll;
	}

	public void setLoadAll(boolean loadAll) {
		this.loadAll = loadAll;
	}
	
	private void setSglRef(IInnerTreeEntry entry,String ref){
		if(innerAddrs.contains(ref)){
			entry.setHaveSglRef(true);
		}else{
			entry.setHaveSglRef(false);
		}
	}
	
}
