/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual DeviIntervalTreeDataFactoryce Develop System.
 */
package com.shrcn.business.scl.das.navg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;

import com.shrcn.business.scl.check.IntervalPermission;
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
import com.shrcn.business.scl.model.navgtree.PermissionEntry;
import com.shrcn.business.scl.model.navgtree.PowerTransformerEntry;
import com.shrcn.business.scl.model.navgtree.SubEquipmentEntry;
import com.shrcn.business.scl.model.navgtree.SubFunctionEntry;
import com.shrcn.business.scl.model.navgtree.SubstationEntry;
import com.shrcn.business.scl.model.navgtree.TapChangerEntry;
import com.shrcn.business.scl.model.navgtree.TransformerWindingEntry;
import com.shrcn.business.scl.model.navgtree.VoltageLevelEntry;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.ArrayUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.dbxapi.io.ScdSaxParser;
import com.shrcn.found.dbxapi.io.ScdSection;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.xmldb.XMLDBHelper;


public class IntervalTableDataFactory{
	
	/**
	 * 单例对象
	 */
	private static volatile IntervalTableDataFactory instance = new IntervalTableDataFactory();
	private LnInstMap lnMap = LnInstMap.getInstance();
	private List<INaviTreeEntry> treeData = null;
	private int inst = 0;
	private boolean modified;
	
	/**
	 * 单例模式私有构造函数
	 */
	private IntervalTableDataFactory(){
		
	}
	
	/**
	 * 获取单例对象
	 */
	public static IntervalTableDataFactory getInstance(){
		if(null == instance) {
			synchronized (IntervalTableDataFactory.class) {
				if(null == instance) {
					instance = new IntervalTableDataFactory();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 打开文件时候初始化间隔权限树信息
	 * 
	 */
	public List<IntervalPermission> createPrimaryData() {
		// 重新加载时候刷新
		List<IntervalPermission> intervalList = new ArrayList<IntervalPermission>();
		lnMap.clearMap();
		Element subNode = null;
		if (Constants.FINISH_FLAG) {
			subNode = XMLDBHelper.selectSingleNode(SCL.XPATH_SUBSTATION);
		} else {
			subNode = ScdSaxParser.getInstance().getPart(ScdSection.Substation);
		}
		if (subNode != null) {
			String subName = subNode.attributeValue("name"); //$NON-NLS-1$
			String voltageXpath = "./VoltageLevel"; //$NON-NLS-1$ //$NON-NLS-2$
			List<Element> voltageLevels = DOM4JNodeHelper.selectNodes(subNode, voltageXpath);
			for(Element voltageLevel:voltageLevels){
				String voltageName =voltageLevel.attributeValue("name");
				String bayXpath ="./Bay";
				List<Element> bays = DOM4JNodeHelper.selectNodes(voltageLevel, bayXpath);
				for(Element bay:bays){
					String bayName =bay.attributeValue("name");
					IntervalPermission intervalPermission = new IntervalPermission();
					intervalPermission.setSubname(subName);
					intervalPermission.setLevel(voltageName);
					intervalPermission.setInterval(bayName);
					intervalPermission.setBrowse("浏览");
					intervalPermission.setChange("修改");
					intervalList.add(intervalPermission);
				}
			}		
		}
		return intervalList;
	}
	
	
	/**
	 * 打开文件时候初始化间隔权限树信息
	 * 
	 */
	public List<IntervalPermission> createPrimaryDataByXML(String sName) {
		// 重新加载时候刷新
		List<IntervalPermission> intervalList = new ArrayList<IntervalPermission>();
		lnMap.clearMap();
		Element subNode = null;
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		subNode = DOM4JNodeHelper.selectSingleNode(root, "./substation[@name='"+sName+"']");
		if (subNode != null) {
			String subName = subNode.attributeValue("name"); //$NON-NLS-1$
			String voltageXpath = "./userGroup[@name='管理员']/VoltageLevel"; //$NON-NLS-1$ //$NON-NLS-2$
			List<Element> voltageLevels = DOM4JNodeHelper.selectNodes(subNode, voltageXpath);
			for(Element voltageLevel:voltageLevels){
				String voltageName =voltageLevel.attributeValue("name");
				String bayXpath ="./Bay";
				List<Element> bays = DOM4JNodeHelper.selectNodes(voltageLevel, bayXpath);
				for(Element bay:bays){
					String bayName =bay.attributeValue("name");
					IntervalPermission intervalPermission = new IntervalPermission();
					intervalPermission.setSubname(subName);
					intervalPermission.setLevel(voltageName);
					intervalPermission.setInterval(bayName);
					intervalPermission.setBrowse("浏览");
					intervalPermission.setChange("修改");
					intervalList.add(intervalPermission);
				}
			}		
		}
		return intervalList;
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

	private INaviTreeEntry resolveLNode(INaviTreeEntry entry) {
		PermissionEntry pEntry1 = new PermissionEntry("浏览");
		entry.addChild(pEntry1);
		PermissionEntry pEntry2 = new PermissionEntry("修改");
		entry.addChild(pEntry2);
		return entry;
		
	}
	private void fillBayTreeDate(INaviTreeEntry subEntry, Element node) {
		List<ITreeEntry> entryList = subEntry.getChildren();
		INaviTreeEntry bayEntry = null;
		if (null == entryList)
			return;
		for (ITreeEntry entry : entryList) {
			if (!(entry instanceof BayEntry))
				continue;
			bayEntry = resolveLNode((INaviTreeEntry)entry);
			
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
			String name =power.attributeValue("name"); //$NON-NLS-1$
			// 以此判断变压器图标
			String type =power.attributeValue("type"); //$NON-NLS-1$
			String entryPath = powerXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry powEntry =new PowerTransformerEntry(name ,entryPath,ImageConstants.TRANSFORMER2,DefaultInfo.SUBS_POWER);
			powEntry.setType(type);
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
			String entryPath = generalXpath+"[@name='"+name +"']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry genEntry =new GeneralEquipmentEntry(name ,entryPath,ImageConstants.FOLDER,DefaultInfo.SUBS_GENERAL);
			genEntry.setType(type);
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
			String entryPath = fucXpath+"[@name='"+name +"']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry funEntry =new FunctionEntry(name ,entryPath,ImageConstants.FUNCTION,DefaultInfo.SUBS_FUNCTION);
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
		for (Element power : list) {
			String name =power.attributeValue("name"); //$NON-NLS-1$
			String entryPath = volLevXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry volLEntry = new VoltageLevelEntry(name, entryPath, ImageConstants.VOLTAGE, DefaultInfo.SUBS_VOLTAGEL);
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
		String xPath = parent.getXPath();
		List<Element> list = new ArrayList<Element>();
		String bayXpath = xPath + "/Bay"; //$NON-NLS-1$
		list = DOM4JNodeHelper.selectNodes(node, bayXpath);
		for (Element bay : list) {
			String name = bay.attributeValue("name"); //$NON-NLS-1$
			String entryPath = bayXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			BayEntry bayEntry = new BayEntry(name, entryPath,
					ImageConstants.BAY, DefaultInfo.SUBS_BAY);
			Element isBusbar = DOM4JNodeHelper.selectSingleNode(bay, "./Private[@type='" + DefaultInfo.BUSBAR + "']");
			if(null != isBusbar)
				bayEntry.setBusbar(true);
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
			String entryPath = tranXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry trnEntry = new TransformerWindingEntry(name ,entryPath,ImageConstants.TRANSFORM, DefaultInfo.SUBS_TRANSFORM);
			trnEntry.setType(type);
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
			String type =power.attributeValue("type"); //$NON-NLS-1$
			String entryPath = tabXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry tabEntry =new TapChangerEntry(name ,entryPath,ImageConstants.FOLDER, DefaultInfo.SUBS_TABCHG);
			tabEntry.setType(type);
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
			String name =power.attributeValue("name"); //$NON-NLS-1$
			String entryPath = subEXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry subEEntry =new SubEquipmentEntry(name ,entryPath,ImageConstants.SUB_EQUP, DefaultInfo.SUBS_SUBEQ);
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
			String entryPath = CondEXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry conEntry =new ConductingEquipmentEntry(name ,entryPath,null, DefaultInfo.SUBS_CONDUCT);
			conEntry.setType(type);
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
			String entryPath = subFunXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry powEntry =new SubFunctionEntry(name ,entryPath,ImageConstants.SUB_FUNCTION, DefaultInfo.SUBS_SUBFUN);
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
			if(name.equals("grounded")&& parent.getType().equals(EnumEquipType.DIS)){ //$NON-NLS-1$
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
