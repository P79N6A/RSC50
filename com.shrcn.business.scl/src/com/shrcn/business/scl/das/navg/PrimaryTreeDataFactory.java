/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das.navg;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;

import com.shrcn.business.scl.SCLConstants;
import com.shrcn.business.scl.common.DefaultInfo;
import com.shrcn.business.scl.common.EnumEquipType;
import com.shrcn.business.scl.common.LnInstMap;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.navgtree.BayEntry;
import com.shrcn.business.scl.model.navgtree.ConductingEquipmentEntry;
import com.shrcn.business.scl.model.navgtree.FunctionEntry;
import com.shrcn.business.scl.model.navgtree.GeneralEquipmentEntry;
import com.shrcn.business.scl.model.navgtree.INaviTreeEntry;
import com.shrcn.business.scl.model.navgtree.LNodeEntry;
import com.shrcn.business.scl.model.navgtree.PowerTransformerEntry;
import com.shrcn.business.scl.model.navgtree.SubEquipmentEntry;
import com.shrcn.business.scl.model.navgtree.SubFunctionEntry;
import com.shrcn.business.scl.model.navgtree.SubstationEntry;
import com.shrcn.business.scl.model.navgtree.TapChangerEntry;
import com.shrcn.business.scl.model.navgtree.TransformerWindingEntry;
import com.shrcn.business.scl.model.navgtree.VoltageLevelEntry;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.common.util.ArrayUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.dbxapi.io.ScdSaxParser;
import com.shrcn.found.dbxapi.io.ScdSection;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-5-13
 */
/*
 * 修改历史
 * $Log: PrimaryTreeDataFactory.java,v $
 * Revision 1.1  2013/03/29 09:38:32  cchun
 * Add:创建
 *
 * Revision 1.6  2012/03/22 03:04:38  cchun
 * Refactor:统一LNodeEntry初始化方法
 *
 * Revision 1.5  2012/03/09 09:52:09  cchun
 * Fix Bug:修复LNode节点名称为空的bug
 *
 * Revision 1.4  2012/03/09 07:35:53  cchun
 * Update:规范prefix和daName属性用法
 *
 * Revision 1.3  2012/01/17 08:50:28  cchun
 * Update:使用更加安全的xpath形式
 *
 * Revision 1.2  2012/01/16 05:59:51  cchun
 * Fix Bug:修复createSCLNode()添加子节点没有detach()的bug
 *
 * Revision 1.1  2011/09/09 07:40:09  cchun
 * Refactor:转移包位置
 *
 * Revision 1.10  2011/09/08 09:23:01  cchun
 * Update:调整格式
 *
 * Revision 1.9  2011/01/07 02:21:08  cchun
 * Fix Bug:修复母线类型信息丢失的bug
 *
 * Revision 1.8  2011/01/06 08:47:11  cchun
 * Update:增加按设备名过滤处理
 *
 * Revision 1.7  2010/12/31 05:21:44  cchun
 * Update:修改方法名为首字母小写
 *
 * Revision 1.6  2010/12/29 08:47:53  cchun
 * Fix Bug:添加对电压等级节点类型的判断
 *
 * Revision 1.5  2010/12/29 06:30:43  cchun
 * Update:增加过滤方法
 *
 * Revision 1.4  2010/11/12 08:57:53  cchun
 * Update:使用统一图标
 *
 * Revision 1.3  2010/09/17 09:29:57  cchun
 * Refactor:修改格式
 *
 * Revision 1.2  2010/03/29 02:57:47  cchun
 * Update:添加母线标识
 *
 * Revision 1.1  2010/03/02 07:49:59  cchun
 * Add:添加重构代码
 *
 * Revision 1.28  2010/02/09 03:19:34  cchun
 * Update:为LNodeEntry属性赋值
 *
 * Revision 1.27  2010/02/08 10:41:39  cchun
 * Refactor:完成第一阶段重构
 *
 * Revision 1.26  2010/01/21 08:48:50  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.25  2009/12/23 08:13:56  lj6061
 * 删除重复常量类，统一常量引用
 *
 * Revision 1.24  2009/10/21 06:16:20  cchun
 * Update:将Berkeley DB引用的dll从library工程中去掉，解决平台不兼容的问题
 *
 * Revision 1.23  2009/09/22 08:55:59  lj6061
 * 对Ln的路径设置保证关联解除关联路径一致
 *
 * Revision 1.21  2009/09/18 08:37:54  lj6061
 * 通过granded节点区分接地和隔离
 *
 * Revision 1.20  2009/09/10 11:41:14  lj6061
 * 统一变压器显示节点类型
 *
 * Revision 1.19  2009/09/09 01:41:46  lj6061
 * 添加导入导出典型间隔
 *
 * Revision 1.18  2009/08/28 01:34:11  cchun
 * Refactor:重构包路径
 *
 * Revision 1.17  2009/08/27 02:40:36  lj6061
 * 修改设备类型文件位置
 *
 * Revision 1.16  2009/08/27 02:26:20  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.15  2009/08/20 09:36:05  cchun
 * Update:去掉无效注释
 *
 * Revision 1.14  2009/08/20 03:34:34  cchun
 * Update:修改导航树及其右键菜单
 *
 * Revision 1.13  2009/08/19 02:53:27  cchun
 * Update:添加单线图绘制菜单
 *
 * Revision 1.12  2009/08/18 09:40:30  cchun
 * Update:合并代码
 *
 * Revision 1.9.2.4  2009/08/07 03:38:45  lj6061
 * 修改插入节点方法
 *
 * Revision 1.9.2.3  2009/08/06 02:07:31  lj6061
 * 替换通过Doc方法插入节点
 *
 * Revision 1.9.2.2  2009/07/31 09:41:16  cchun
 * Update:修改命名空间处理方式
 *
 * Revision 1.9.2.1  2009/07/24 07:11:16  cchun
 * Update:切换数据库Sedna
 *
 * Revision 1.9  2009/07/16 06:31:39  lj6061
 * 整理代码
 * 1.删除未被引用的对象和方法
 * 2 修正空指针的异常
 *
 * Revision 1.8  2009/07/09 06:21:38  lj6061
 * 修正替换插入的问题
 *
 * Revision 1.7  2009/07/09 05:37:52  lj6061
 * 修改插入和替换节点方法，避免了非法字符
 *
 * Revision 1.6  2009/06/16 08:33:02  lj6061
 * 对空异常处理
 *
 * Revision 1.5  2009/06/15 09:39:55  lj6061
 * 更改代码中的对电压处理
 *
 * Revision 1.4  2009/06/11 04:06:27  cchun
 * Update:重构StringUtil路径
 *
 * Revision 1.3  2009/06/10 01:48:07  lj6061
 * 添加判断NUll
 *
 * Revision 1.2  2009/06/08 10:52:22  lj6061
 * 根据Schema添加全局的Lninst
 *
 * Revision 1.1  2009/06/02 05:27:13  hqh
 * 新建factory包
 *
 * Revision 1.10  2009/06/01 01:47:23  lj6061
 * 添加问Ln的Xpath的具体判断
 *
 * Revision 1.9  2009/05/31 10:58:58  lj6061
 * 添加复制粘贴操作
 *
 * Revision 1.8  2009/05/27 08:01:04  lj6061
 * 添加重命名
 *
 * Revision 1.7  2009/05/27 04:46:29  lj6061
 * 添加一次信息图片
 *
 * Revision 1.6  2009/05/26 07:44:46  lj6061
 * 去除接入点菜单
 *
 * Revision 1.5  2009/05/26 06:17:35  lj6061
 * 默认菜单新建SSD
 *
 * Revision 1.3  2009/05/25 06:08:00  pht
 * 获得一次设备树的数据。
 *
 * Revision 1.2  2009/05/23 11:29:45  lj6061
 * 添加节点菜单事件
 *
 * Revision 1.12  2009/05/22 10:15:54  lj6061
 * 修改节点顺序
 *
 * Revision 1.11  2009/05/22 09:48:22  hqh
 * 添加菜单项
 *
 * Revision 1.10  2009/05/22 09:42:54  hqh
 * 构造方法参数
 *
 * Revision 1.9  2009/05/22 09:33:45  lj6061
 * 修改一次节点
 *
 * Revision 1.8  2009/05/22 09:19:02  lj6061
 * 修改一次节点
 *
 * Revision 1.7  2009/05/22 03:04:00  lj6061
 * 修改节点属性添加优先级
 *
 * Revision 1.6  2009/05/21 05:43:05  lj6061
 * 为树添加邮件插入事件
 *
 * Revision 1.5  2009/05/19 08:55:01  lj6061
 * 修改图片地址
 *
 * Revision 1.4  2009/05/19 06:35:59  lj6061
 * 导入提示信息Bug
 *
 * Revision 1.3  2009/05/19 05:55:22  lj6061
 * SSD文件的打开操作
 *
 * Revision 1.2  2009/05/19 05:27:41  lj6061
 * SSD文件导入的打开操作
 *
 * Revision 1.1  2009/05/18 07:08:13  lj6061
 * 导入1次信息
 *
 */
public class PrimaryTreeDataFactory {
	
	/**
	 * 单例对象
	 */
	private static volatile PrimaryTreeDataFactory instance = new PrimaryTreeDataFactory();
	private LnInstMap lnMap = LnInstMap.getInstance();
	private List<INaviTreeEntry> treeData = null;
	private int inst = 0;
	private boolean modified;
	
	/**
	 * 单例模式私有构造函数
	 */
	private PrimaryTreeDataFactory(){
		
	}
	
	/**
	 * 获取单例对象
	 */
	public static PrimaryTreeDataFactory getInstance(){
		if(null == instance) {
			synchronized (PrimaryTreeDataFactory.class) {
				if(null == instance) {
					instance = new PrimaryTreeDataFactory();
				}
			}
		}
		return instance;
	}
	
	/**
	 * DOM4j取出SSD文件节点插入SSD文件并解析
	 * 
	 * @return
	 * @throws DocumentException 
	 * @throws XmlException 
	 */
	public void importPrimaryData(String filePath,boolean isNew) throws DocumentException {
		// 重新加载时候刷新
		lnMap.clearMap();
		treeData = new ArrayList<INaviTreeEntry>();
		Element node = readXml(filePath);
		Element subNode = DOM4JNodeHelper.selectSingleNode(node, SCL.XPATH_SUBSTATION);
		if (subNode != null) {
			String subName = subNode.attributeValue("name"); //$NON-NLS-1$
			String desc = subNode.attributeValue("desc"); //$NON-NLS-1$
			String subXpath = SCL.XPATH_SUBSTATION + "[@name='" + subName + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry subEntry = new SubstationEntry(subName, desc, subXpath, ImageConstants.SUBSTATION, DefaultInfo.SUBS_ROOT);
			fillSubTreeData(subEntry, node);
			treeData.add(subEntry);
			insertSSDinSCD(subNode,isNew);
		}	
	}
	
	/**
	 * 打开文件时候初始化一次设备树信息
	 * 
	 */
	public List<INaviTreeEntry> createPrimaryData() {
		// 重新加载时候刷新
		lnMap.clearMap();
		treeData = new ArrayList<INaviTreeEntry>();
		Element subNode = null;
		if (Constants.FINISH_FLAG) {
			subNode = XMLDBHelper.selectSingleNode(SCL.XPATH_SUBSTATION);
		} else {
			subNode = ScdSaxParser.getInstance().getPart(ScdSection.Substation);
		}
		if (subNode != null) {
			Element node = createSCLNode(subNode);
			String subName = subNode.attributeValue("name"); //$NON-NLS-1$
			String desc = subNode.attributeValue("desc"); //$NON-NLS-1$
			String subXpath = SCL.XPATH_SUBSTATION + "[@name='" + subName+ "']"; //$NON-NLS-1$ //$NON-NLS-2$
//			String subXpath = SCL.XPATH_SUBSTATION;
			INaviTreeEntry subEntry = new SubstationEntry(subName, desc, subXpath, ImageConstants.SUBSTATION, DefaultInfo.SUBS_ROOT);
			fillSubTreeData(subEntry, node);
			treeData.add(subEntry);
		}
		return treeData;
	}
	
	/**
	 * 按间隔名称过滤
	 * @param data
	 * @param bayName 间隔名称（可用逗号分隔多个值）
	 * @return
	 */
	public static List<INaviTreeEntry> getFiltTreeData(List<INaviTreeEntry> data, String bayName) {
		String[] bayNames = bayName.split(",");
		List<INaviTreeEntry> newdata = new ArrayList<INaviTreeEntry>();
		for (INaviTreeEntry sub : data) {
			INaviTreeEntry sub1 = (INaviTreeEntry) sub.copy();
			newdata.add(sub1);
			for (ITreeEntry vol : sub.getChildren()) {
				if (!(vol instanceof VoltageLevelEntry)) { // 可能存在Function节点
					sub1.addChild(vol);
					continue;
				}
				ITreeEntry vol1 = vol.copy();
				sub1.addChild(vol1);
				for (ITreeEntry bay : vol.getChildren()) {
					if (!(bay instanceof BayEntry)) { // 可能存在Function节点
						vol1.addChild(bay);
						continue;
					}
					BayEntry bay1 = (BayEntry) bay.copy();
					bay1.setBusbar(((BayEntry)bay).isBusbar());
					for (ITreeEntry eqp : bay.getChildren()) {
						if (ArrayUtil.contains(eqp.getName(), bayNames)) {
							bay1.addChild(eqp);
						}
					}
					if (ArrayUtil.contains(bay.getName(), bayNames)
							|| bay1.getChildren().size() > 0) {
						vol1.addChild(bay1);
					}
				}
			}
		}
		return newdata;
	}
	
	/**
	 * 读取SSD文件中的变电站后添加SCL节点
	 * 
	 */
	private Element createSCLNode(Element subEle) {
		Document doc = DocumentHelper.createDocument();
		QName qn = new QName("SCL", new Namespace("", Constants.uri)); //$NON-NLS-1$ //$NON-NLS-2$
		Element ele = doc.addElement(qn);
		ele.add(subEle.detach());
		return ele;
	}

	/**
	 * load到当前的SCD文件中
	 * 
	 * @return
	 * @throws DocumentException 
	 * @throws XmlException 
	 */
	private void insertSSDinSCD(Element subNode,boolean isNew){
		if (isNew)
			XMLDBHelper.replaceNode(SCL.XPATH_SUBSTATION, subNode);
		else
			try {
				XMLDBHelper.insertAfter(SCL.XPATH_HEAD, subNode);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	private void fillSubTreeData(INaviTreeEntry subEntry, Element node) {
		resolveLNode(subEntry,node);
		subEntry = resolvePowerTransformer(subEntry ,node);
		fillPowerTreeDate(subEntry,node);
		subEntry = resolveGeneralEquipment(subEntry,node);
		fillGeneralETreeDate(subEntry,node);
		subEntry = resolveVoltageLevel(subEntry,node);
		fillVoltLTreeDate(subEntry,node);
		subEntry = resolveFunction(subEntry,node);
		fillFunTreeDate(subEntry,node);		
	}
	
	public void fillPowerTreeDate(INaviTreeEntry subEntry, Element node) {
		List<ITreeEntry> entryList = subEntry.getChildren();
		INaviTreeEntry powerEntry =null;
		if (null == entryList) return;
		for (ITreeEntry entry : entryList) {
			if(!(entry instanceof PowerTransformerEntry)) continue;
			powerEntry = resolveLNode((INaviTreeEntry)entry,node);
			powerEntry = resolveTransformerWinding(powerEntry,node);
			fillTransfTreeData(powerEntry, node);
		}
	}

	private void fillTransfTreeData(INaviTreeEntry subEntry, Element node) {
		List<ITreeEntry> entryList = subEntry.getChildren();
		INaviTreeEntry transEntry =null;
		if (null == entryList) return;
		for (ITreeEntry entry : entryList) {
			if(!(entry instanceof TransformerWindingEntry)) continue;
			transEntry = resolveLNode((INaviTreeEntry)entry,node);
			transEntry = resolveConductingEquipment(transEntry,node);
			fillConductTreeData(transEntry, node);
			transEntry = resolveTapChanger(transEntry,node);
			fillTabTreeDate(transEntry, node);
		}
	}

	public void fillConductTreeData(INaviTreeEntry subEntry, Element node) {
		List<ITreeEntry> entryList = subEntry.getChildren();
		INaviTreeEntry conductEntry =null;
		if (null == entryList) return;
		for (ITreeEntry entry : entryList) {
			if(!(entry instanceof ConductingEquipmentEntry)) continue;
			conductEntry = resolveLNode((INaviTreeEntry)entry, node);
			resolveTerminal(conductEntry, node);
			conductEntry = resolveSubEquipment(conductEntry,node);
			fillSunETreeDate(conductEntry,node);			
		}
	}

	private void fillVoltLTreeDate(INaviTreeEntry subEntry, Element node) {
		List<ITreeEntry> entryList = subEntry.getChildren();
		INaviTreeEntry voltLEntry = null;
		if (null == entryList)
			return;
		for (ITreeEntry entry : entryList) {
			if (!(entry instanceof VoltageLevelEntry))
				continue;
			voltLEntry = resolveLNode((INaviTreeEntry)entry, node);
			voltLEntry = resolvePowerTransformer(voltLEntry, node);
			fillPowerTreeDate(voltLEntry, node);
			voltLEntry = resolveGeneralEquipment(voltLEntry, node);
			fillGeneralETreeDate(voltLEntry, node);
			voltLEntry = resolveBay(voltLEntry, node);
			fillBayTreeDate(voltLEntry, node);
			voltLEntry = resolveFunction(voltLEntry, node);
			fillFunTreeDate(voltLEntry, node);
		}
	}

	private void fillBayTreeDate(INaviTreeEntry subEntry, Element node) {
		List<ITreeEntry> entryList = subEntry.getChildren();
		INaviTreeEntry bayEntry = null;
		if (null == entryList)
			return;
		for (ITreeEntry entry : entryList) {
			if (!(entry instanceof BayEntry))
				continue;
			bayEntry = resolveLNode((INaviTreeEntry)entry, node);
			bayEntry = resolvePowerTransformer(bayEntry, node);
			fillPowerTreeDate(bayEntry, node);
			bayEntry = resolveGeneralEquipment(bayEntry, node);
			fillGeneralETreeDate(bayEntry, node);
			bayEntry = resolveConductingEquipment(bayEntry, node);
			fillConductTreeData(bayEntry, node);
			bayEntry = resolveFunction(bayEntry, node);
			fillFunTreeDate(bayEntry, node);
		}	
	}

	public void fillFunTreeDate(INaviTreeEntry subEntry, Element node) {
		List<ITreeEntry> entryList = subEntry.getChildren();
		INaviTreeEntry funLEntry =null;
		if (null == entryList) return;
		for (ITreeEntry entry : entryList) {
			if(!(entry instanceof FunctionEntry)) continue;
			funLEntry = resolveLNode((INaviTreeEntry)entry,node);
			funLEntry = resolveSubFunction(funLEntry,node);
			fillSubFunctionTreeDate(funLEntry, node);
			funLEntry = resolveGeneralEquipment(funLEntry,node);
			fillGeneralETreeDate(funLEntry, node);
			funLEntry = resolveConductingEquipment(funLEntry,node);
			fillConductTreeData(funLEntry, node);
			
		}		
	}

	private void fillSubFunctionTreeDate(INaviTreeEntry subEntry, Element node) {
		List<ITreeEntry> entryList = subEntry.getChildren();
		INaviTreeEntry sFunLEntry =null;
		if (null == entryList) return;
		for (ITreeEntry entry : entryList) {
			if(!(entry instanceof SubFunctionEntry)) continue;
			sFunLEntry = resolveLNode((INaviTreeEntry)entry,node);
			sFunLEntry = resolveGeneralEquipment(sFunLEntry,node);
			fillGeneralETreeDate(sFunLEntry, node);
			sFunLEntry = resolveConductingEquipment(sFunLEntry,node);
			fillConductTreeData(sFunLEntry, node);	
		}	
	}
	
	/**
	 *	解析普通节点
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	public void fillGeneralETreeDate(INaviTreeEntry subEntry, Element node) {
		List<ITreeEntry> entryList = subEntry.getChildren();
		if (null == entryList) return;
		for (ITreeEntry entry : entryList) {
			if(entry instanceof GeneralEquipmentEntry)
			resolveLNode((INaviTreeEntry)entry,node);
		}
	}
	/**
	 *	解析普通节点
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	private void fillTabTreeDate(INaviTreeEntry subEntry, Element node) {
		List<ITreeEntry> entryList = subEntry.getChildren();
		if (null == entryList) return;
		for (ITreeEntry entry : entryList) {
			if(entry instanceof TapChangerEntry)
				resolveLNode((INaviTreeEntry)entry,node);
		}
	}
	
	/**
	 *	解析子设备
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	private void fillSunETreeDate(INaviTreeEntry subEntry, Element node) {
		List<ITreeEntry> entryList = subEntry.getChildren();
		if (null == entryList) return;
		for (ITreeEntry entry : entryList) {
			if(entry instanceof SubEquipmentEntry)
			resolveLNode((INaviTreeEntry)entry,node);
		}
	}

	/**
	 *	解析变压器节点
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	public INaviTreeEntry resolvePowerTransformer(INaviTreeEntry parent, Element node){
		String xPath =	parent.getXPath();
		List<Element> list = new ArrayList<Element>();
		String powerXpath = xPath + "/PowerTransformer"; //$NON-NLS-1$
		list = DOM4JNodeHelper.selectNodes(node, powerXpath);
		for (Element power : list) {
			String name = power.attributeValue("name"); //$NON-NLS-1$
			// 以此判断变压器图标
			String type = power.attributeValue("type"); //$NON-NLS-1$
			String desc = power.attributeValue("desc"); //$NON-NLS-1$
			String virtual = power.attributeValue("virtual"); //$NON-NLS-1$
			String entryPath = powerXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry powEntry =new PowerTransformerEntry(name, desc, type, entryPath, ImageConstants.TRANSFORMER2,DefaultInfo.SUBS_POWER);
			if (!StringUtil.isEmpty(virtual))
				((PowerTransformerEntry) powEntry).setVirtual(Boolean.parseBoolean(virtual));
			parent.addChild(powEntry);
		}
		return parent;
	}
	
	/**
	 *	解析普通装置
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	public INaviTreeEntry resolveGeneralEquipment(INaviTreeEntry parent, Element node){
		String xPath =parent.getXPath();
		List<Element> list = new ArrayList<Element>();
		String generalXpath = xPath + "/GeneralEquipment"; //$NON-NLS-1$
		list = DOM4JNodeHelper.selectNodes(node, generalXpath);
		for (Element power : list) {
			String name =power.attributeValue("name"); //$NON-NLS-1$
			String type =power.attributeValue("type"); //$NON-NLS-1$
			String desc =power.attributeValue("desc"); //$NON-NLS-1$
			String virtual = power.attributeValue("virtual"); //$NON-NLS-1$
			String entryPath = generalXpath+"[@name='"+name +"']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry genEntry =new GeneralEquipmentEntry(name, desc, type, entryPath, ImageConstants.FOLDER,DefaultInfo.SUBS_GENERAL);
			if (!StringUtil.isEmpty(virtual))
				((GeneralEquipmentEntry) genEntry).setVirtual(Boolean.parseBoolean(virtual));
			parent.addChild(genEntry);
		}
		return parent;
	}
	
	/**
	 *	解析功能节点
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	public INaviTreeEntry resolveFunction(INaviTreeEntry parent, Element node){
		String xPath =parent.getXPath();
		List<Element> list = new ArrayList<Element>();
		String fucXpath = xPath + "/Function"; //$NON-NLS-1$
		list = DOM4JNodeHelper.selectNodes(node, fucXpath);
		for (Element power : list) {
			String name =power.attributeValue("name"); //$NON-NLS-1$
			String desc =power.attributeValue("desc"); //$NON-NLS-1$
			String type =power.attributeValue("type"); //$NON-NLS-1$
			String entryPath = fucXpath+"[@name='"+name +"']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry funEntry =new FunctionEntry(name, desc, type, entryPath,ImageConstants.FUNCTION,DefaultInfo.SUBS_FUNCTION);
			parent.addChild(funEntry);
		}
		return parent;
	}
	
	/**
	 *	解析电压等级
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	private INaviTreeEntry resolveVoltageLevel(INaviTreeEntry parent, Element node){
		String xPath =parent.getXPath();
		List<Element> list = new ArrayList<Element>();
		String volLevXpath = xPath + "/VoltageLevel"; //$NON-NLS-1$
		list = DOM4JNodeHelper.selectNodes(node, volLevXpath);
		Collections.sort(list, new Comparator<Element>() {
			@Override
			public int compare(Element o1, Element o2) {
				int o1Type = getValue(o1);
				int o2Type = getValue(o2);
				return o2Type - o1Type;
			}
			
			private int getValue(Element o1){
				String name = o1.attributeValue("name");
				return StringUtil.isEmpty(name) ? -1 : Integer.parseInt(name.split("[^\\d]")[0]);
			}
		});
		for (Element power : list) {
			String name = power.attributeValue("name"); //$NON-NLS-1$
			String desc =power.attributeValue("desc"); //$NON-NLS-1$
			String entryPath = volLevXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry volLEntry = new VoltageLevelEntry(name, desc, entryPath, ImageConstants.VOLTAGE, DefaultInfo.SUBS_VOLTAGEL);
			parent.addChild(volLEntry);
		}
		return parent;
	}
	
	/**
	 *	解析逻辑节点
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	public INaviTreeEntry resolveLNode(INaviTreeEntry parent, Element node){
		String xPath = parent.getXPath();
		List<Element> list = new ArrayList<Element>();
		String lnXpath = xPath + "/LNode"; //$NON-NLS-1$
		list = DOM4JNodeHelper.selectNodes(node, lnXpath);
		for (Element power : list) {
			String xpath = lnXpath;
			String iedName = power.attributeValue("iedName"); //$NON-NLS-1$
			String ldInst = power.attributeValue("ldInst"); //$NON-NLS-1$
			String prefix = power.attributeValue("prefix"); //$NON-NLS-1$
			String lnClass = power.attributeValue("lnClass"); //$NON-NLS-1$
			String lnInst = power.attributeValue("lnInst"); //$NON-NLS-1$
			String lnType = StringUtil.nullToEmpty(power.attributeValue("lnType")); //$NON-NLS-1$
			if (lnType.equals(DefaultInfo.nullString)) {
				lnType = ""; //$NON-NLS-1$
			}
			String name = iedName + "/" + ldInst + "." + SCL.getLnName(prefix, lnClass, lnInst) + ":" + lnType; 
			String entryPath = null;
			// iedName,ldInst可有可无字段，存在路径中需要添加
			if(iedName != null)
				xpath = xpath + "[@iedName='" + iedName + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			else
				iedName = StringUtil.nullToEmpty(iedName);
			if (ldInst != null)
				xpath = xpath + "[@ldInst='" + ldInst + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			else
				ldInst = StringUtil.nullToEmpty(ldInst);
			entryPath = xpath + SCL.getLNAtts(prefix, lnClass, lnInst);
			
			LNodeEntry lnEntry = new LNodeEntry(name, entryPath, ImageConstants.LNODE, DefaultInfo.SUBS_LNODE);
			lnEntry.setValues(iedName, ldInst, prefix, lnClass, lnInst, lnType);
			parent.addChild(lnEntry);
			findMaxLnInst(lnClass, lnInst);			
		}
		return parent;
	}
	
	/**
	 *	存放LnInst最大 按lnClass类型
	 *	@param lnClass 父类节点
	 *	@param lnInst 上级路径
	 */
	private void findMaxLnInst(String lnClass , String lnInst){
		if (lnInst == null ||lnInst.equals("")){ //$NON-NLS-1$
			lnMap.setLnInst(lnClass, 0);
			return;
		}
		try {
			inst = lnMap.getLnInst(lnClass);
			if (!StringUtil.verifyNum(lnInst))
				return;
			int dBInst = Integer.parseInt(lnInst);
			if (dBInst > inst) {
				lnMap.setLnInst(lnClass, dBInst);
			}		
		} catch (NumberFormatException e) {
			lnMap.setLnInst(lnClass, 0);
		}
	}
	
	/**
	 *	解析间隔
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	private INaviTreeEntry resolveBay(INaviTreeEntry parent, Element node){
		String[] dictIds = DictManager.getInstance().getDictIds(SCLConstants.DICT_VIRTUALBAY_ID);
		String xPath = parent.getXPath();
		List<Element> list = new ArrayList<Element>();
		String bayXpath = xPath + "/Bay"; //$NON-NLS-1$
		list = DOM4JNodeHelper.selectNodes(node, bayXpath);
		for (Element bay : list) {
			String name = bay.attributeValue("name"); //$NON-NLS-1$
			String desc = bay.attributeValue("desc"); //$NON-NLS-1$
			String type = StringUtil.nullToEmpty(DOM4JNodeHelper.getAttributeValue(bay, "./Private/@type"));
			String entryPath = bayXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			BayEntry bayEntry = new BayEntry(name, desc, entryPath, ImageConstants.BAY, DefaultInfo.SUBS_BAY);
			bayEntry.setBusbar(type.equals(DefaultInfo.BUSBAR));
			if (!StringUtil.isEmpty(type) && Arrays.asList(dictIds).contains(type))
				bayEntry.setType(type);
			parent.addChild(bayEntry);
		}
		return parent;
	}
	
	/**
	 *	解析变压器绕组
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	private INaviTreeEntry resolveTransformerWinding(INaviTreeEntry parent, Element node){
		String xPath = parent.getXPath();
		List<Element> list = new ArrayList<Element>();
		String tranXpath = xPath + "/TransformerWinding"; //$NON-NLS-1$
		list = DOM4JNodeHelper.selectNodes(node, tranXpath);
		if (list.size() == 3)
			parent.setType(EnumEquipType.PTR3);// 区分图片给定类型标识
		else if (list.size() == 2) {
			parent.setType(EnumEquipType.PTR2);// 区分图片给定类型标识
		}
		for (Element power : list) {
			String name = power.attributeValue("name"); //$NON-NLS-1$
			String type = power.attributeValue("type"); //$NON-NLS-1$
			String desc = power.attributeValue("desc"); //$NON-NLS-1$
			String virtual = power.attributeValue("virtual"); //$NON-NLS-1$
			String entryPath = tranXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry trnEntry = new TransformerWindingEntry(name, desc, type, entryPath,ImageConstants.TRANSFORM, DefaultInfo.SUBS_TRANSFORM);
			if (!StringUtil.isEmpty(virtual))
				((TransformerWindingEntry) trnEntry).setVirtual(Boolean.parseBoolean(virtual));
			parent.addChild(trnEntry);
		}
		return parent;
	}
	
	
	/**
	 *	解析TapChanger
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	private INaviTreeEntry resolveTapChanger(INaviTreeEntry parent, Element node){
		String xPath =parent.getXPath();
		List<Element> list = new ArrayList<Element>();
		String tabXpath = xPath + "/TapChanger"; //$NON-NLS-1$
		list = DOM4JNodeHelper.selectNodes(node, tabXpath);
		for (Element power : list) {
			String name =power.attributeValue("name"); //$NON-NLS-1$
			String desc =power.attributeValue("desc"); //$NON-NLS-1$
			String type =power.attributeValue("type"); //$NON-NLS-1$
			String virtual = power.attributeValue("virtual"); //$NON-NLS-1$
			String entryPath = tabXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry tabEntry =new TapChangerEntry(name, desc, type, entryPath,ImageConstants.FOLDER, DefaultInfo.SUBS_TABCHG);
			if (!StringUtil.isEmpty(virtual))
				((TapChangerEntry) tabEntry).setVirtual(Boolean.parseBoolean(virtual));
			parent.addChild(tabEntry);
		}
		return parent;
	}
	
	/**
	 *	解析子装置
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	private INaviTreeEntry resolveSubEquipment(INaviTreeEntry parent, Element node){
		String xPath =parent.getXPath();
		List<Element> list = new ArrayList<Element>();
		String subEXpath = xPath + "/SubEquipment"; //$NON-NLS-1$
		list = DOM4JNodeHelper.selectNodes(node, subEXpath);
		for (Element power : list) {
			String name = power.attributeValue("name"); //$NON-NLS-1$
			String desc = power.attributeValue("desc"); //$NON-NLS-1$
			String virtual = power.attributeValue("virtual"); //$NON-NLS-1$
			String entryPath = subEXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			SubEquipmentEntry subEEntry =new SubEquipmentEntry(name, desc, entryPath, ImageConstants.SUB_EQUP, DefaultInfo.SUBS_SUBEQ);
			if (!StringUtil.isEmpty(virtual))
				subEEntry.setVirtual(Boolean.parseBoolean(virtual));
			parent.addChild(subEEntry);
		}
		return parent;
	}
	
	/**
	 *	解析接线端
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	public INaviTreeEntry resolveConductingEquipment(INaviTreeEntry parent, Element node){
		String xPath =parent.getXPath();
		List<Element> list = new ArrayList<Element>();
		String CondEXpath = xPath + "/ConductingEquipment"; //$NON-NLS-1$
		list = DOM4JNodeHelper.selectNodes(node, CondEXpath);
		for (Element power : list) {
			String name =power.attributeValue("name"); //$NON-NLS-1$
			String type =power.attributeValue("type"); //$NON-NLS-1$
			String desc =power.attributeValue("desc"); //$NON-NLS-1$
			String virtual = power.attributeValue("virtual"); //$NON-NLS-1$
			String entryPath = CondEXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry conEntry =new ConductingEquipmentEntry(name, desc, type, entryPath, null, DefaultInfo.SUBS_CONDUCT);
			if (!StringUtil.isEmpty(virtual))
				((ConductingEquipmentEntry) conEntry).setVirtual(Boolean.parseBoolean(virtual));
			parent.addChild(conEntry);
		}
		return parent;
	}
	
	/**
	 *	解析子功能
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	private INaviTreeEntry resolveSubFunction(INaviTreeEntry parent, Element node){
		String xPath =parent.getXPath();
		List<Element> list = new ArrayList<Element>();
		String subFunXpath = xPath + "/SubFunction"; //$NON-NLS-1$
		list = DOM4JNodeHelper.selectNodes(node, subFunXpath);
		for (Element power : list) {
			String name =power.attributeValue("name"); //$NON-NLS-1$
			String desc =power.attributeValue("desc"); //$NON-NLS-1$
			String entryPath = subFunXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry powEntry =new SubFunctionEntry(name, desc ,entryPath, ImageConstants.SUB_FUNCTION, DefaultInfo.SUBS_SUBFUN);
			parent.addChild(powEntry);
		}
		return parent;
	}
	
	public List<INaviTreeEntry> getTreeData() {
		return treeData;
	}
	
		/**
	 *	解析接线端
	 *	@param parent 父类节点
	 *	@param xPath 上级路径
	 *	@return 父节点
	 */
	private INaviTreeEntry resolveTerminal(INaviTreeEntry parent, Element node){
		String xPath =parent.getXPath();
		List<Element> list = new ArrayList<Element>();
		String termXpath = xPath + "/Terminal"; //$NON-NLS-1$
		list = DOM4JNodeHelper.selectNodes(node, termXpath);
		for (Element power : list) {
			String name =power.attributeValue("cNodeName"); //$NON-NLS-1$
			if(!StringUtil.isEmpty(name) && name.equals("grounded")&& parent.getType().equals(EnumEquipType.DIS)){ //$NON-NLS-1$
				// 判断接地刀闸还是隔离开关 通过grounded，存在接地刀闸
				parent.setType(EnumEquipType.DDIS);
			}
		}
		return parent;
	}
	
	/**
	 * 通过文件路径读取XML文件
	 * 
	 * @param fileName				文件名称
	 * @return Element				XML文件的节点
	 * @throws DocumentException	抛出文件异常
	 */
	private Element readXml(String fileName) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = null;
		doc = reader.read(new File(fileName));
		return doc.getRootElement();
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
}
