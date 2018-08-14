/**
 * Copyright (c) 2007, 2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das.navg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

import com.shrcn.business.scl.common.DefaultInfo;
import com.shrcn.business.scl.das.IEDDAO;
import com.shrcn.business.scl.model.IED;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.navgtree.BayEntry;
import com.shrcn.business.scl.model.navgtree.DeviceEntry;
import com.shrcn.business.scl.model.navgtree.IEDEntry;
import com.shrcn.business.scl.model.navgtree.INaviTreeEntry;
import com.shrcn.business.scl.model.navgtree.SubstationEntry;
import com.shrcn.business.scl.model.navgtree.VoltageLevelEntry;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.ArrayUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 数据工厂
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2015-12-23
 */
public class UserCfgTreeDataFactory {
	
	private static volatile UserCfgTreeDataFactory instance = new UserCfgTreeDataFactory();
	
	private UserCfgTreeDataFactory(){}

	public static UserCfgTreeDataFactory getInstance() {
		if(null == instance) {
			synchronized (UserCfgTreeDataFactory.class) {
				if(null == instance) {
					instance = new UserCfgTreeDataFactory();
				}
			}
		}
		return instance;
	}

	public int createUserCfgData(List<INaviTreeEntry> treeData) {
		Element subNode = XMLDBHelper.selectSingleNode(SCL.XPATH_SUBSTATION);
		if (subNode != null && subNode.hasContent()) {
			Element node = createSCLNode(subNode);
			String subName = subNode.attributeValue("name"); //$NON-NLS-1$
			String subXpath = SCL.XPATH_SUBSTATION + "[@name='" + subName+ "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry subEntry = new SubstationEntry(subName, subXpath,ImageConstants.SUBSTATION, DefaultInfo.SUBS_ROOT);
			fillSubTreeData(subEntry, node);
			treeData.add(subEntry);
			return 4;
		} else {
			// 加载装置列表
			fillIEDTreeItems(treeData, IEDDAO.getAllNameDescsByTypeOrder());
			return 2;
		}
	}
	
	private void fillSubTreeData(INaviTreeEntry subEntry, Element node) {
		String xPath =subEntry.getXPath();
		List<Element> list = new ArrayList<Element>();
		String volLevXpath = xPath + "/VoltageLevel"; //$NON-NLS-1$
		list = DOM4JNodeHelper.selectNodes(node, volLevXpath);
		for (Element power : list) {
			String name = power.attributeValue("name"); //$NON-NLS-1$
			String entryPath = volLevXpath + "[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
			INaviTreeEntry volLEntry = new VoltageLevelEntry(name, entryPath, ImageConstants.VOLTAGE, DefaultInfo.SUBS_VOLTAGEL);
			subEntry.addChild(volLEntry);
			
			resolveBay(volLEntry, power);
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
		list = DOM4JNodeHelper.selectNodes(node, "./Bay");
		for (Element bay : list) {
			String name = bay.attributeValue("name"); //$NON-NLS-1$
			
			BayEntry bayEntry = new BayEntry(name, xPath + "/Bay[@name='" + name + "']", ImageConstants.BAY, DefaultInfo.SUBS_BAY);
			parent.addChild(bayEntry);
			Element isBusbar = DOM4JNodeHelper.selectSingleNode(bay, "./Private[@type='" + DefaultInfo.BUSBAR + "']");
			if (null != isBusbar)
				bayEntry.setBusbar(true);
			
			List<String> iedNames = DOM4JNodeHelper.getAttributeValues(bay, "./*/scl:LNode/@iedName");
			List<String> deviceNames = new ArrayList<String>();
			for (String name1 : iedNames) {
				if (!name1.equals("None")&& !deviceNames.contains(name1)) {
					deviceNames.add(name1);
					INaviTreeEntry subEty = new IEDEntry(name1, SCL.XPATH_IED + "[@name='" + name1 + "']", 
							ImageConstants.IED, "", UIConstants.USER_CONFIG_EDITOR_ID);
					bayEntry.addChild(subEty);
				}
			}
		}
		return parent;
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
	 * 加载装置
	 * @param nodeEntry
	 */
	private void fillIEDTreeItems(List<INaviTreeEntry> treeData, List<IED> ieds) {
		Collections.sort(ieds, new Comparator<IED>() {
			@Override
			public int compare(IED o1, IED o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		DeviceEntry iedRoot = new DeviceEntry("装置",
				SCL.XPATH_IED, ImageConstants.PARENT, "装置", null);
		treeData.add(iedRoot);
		for (IED ied : ieds) {
			String iedName = ied.getName();
			String label = StringUtil.getLabel(iedName, ied.getDesc());
			String icon = "VIRTUAL_IED".equals(ied.getType()) ? ImageConstants.IED_V : ImageConstants.IED;
			String tooltip = "名称：" + iedName + "    描述：" + ied.getDesc() +
					"\n型号：" + ied.getType() + "    厂商：" + ied.getManufacturer() +
					"\n版本：" + ied.getConfigVersion();
			INaviTreeEntry subEty = new IEDEntry(label, SCL.XPATH_IED + "[@name='" + iedName + "']", 
					icon, tooltip, UIConstants.USER_CONFIG_EDITOR_ID);
			iedRoot.addChild(subEty);
		}
	}

	public static List<INaviTreeEntry> getFiltTreeData(
			List<INaviTreeEntry> data, String name) {
		String[] names = name.split(",");
		List<INaviTreeEntry> newdata = new ArrayList<INaviTreeEntry>();
		for (INaviTreeEntry group : data) {
			if (group instanceof SubstationEntry) {
				ITreeEntry group1 = group.copy();
				newdata.add((INaviTreeEntry) group1);
				for (ITreeEntry volEntry : group.getChildren()) {
					ITreeEntry group2 = volEntry.copy();
					group1.addChild((INaviTreeEntry) group2);
					for (ITreeEntry bayEntry : volEntry.getChildren()) {
						ITreeEntry group3 = bayEntry.copy();
						group2.addChild((INaviTreeEntry) group3);
						for (ITreeEntry item : bayEntry.getChildren()) {
							if (ArrayUtil.contains(item.getName(), names)) {
								group3.addChild(item);
							}
						}
						removeChild(group2, group3);
					}
					removeChild(group1, group2);
				}
			}else if(group instanceof DeviceEntry){
				ITreeEntry group1 = group.copy();
				newdata.add((INaviTreeEntry) group1);
				for (ITreeEntry item : group.getChildren()) {
					if (ArrayUtil.contains(item.getName(), names)) {
						group1.addChild(item);
					}
				}
				
			}
			
		}
		return newdata;
	}

	private static void removeChild(ITreeEntry group1, ITreeEntry group2) {
		if (group2.getChildren() == null || group2.getChildren().size() == 0)
			group1.removeChild(group2);
	}

}
