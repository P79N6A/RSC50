/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das.navg;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.navgtree.BDAEntry;
import com.shrcn.business.scl.model.navgtree.DAEntry;
import com.shrcn.business.scl.model.navgtree.DATypeEntry;
import com.shrcn.business.scl.model.navgtree.DOEntry;
import com.shrcn.business.scl.model.navgtree.DOTypeEntry;
import com.shrcn.business.scl.model.navgtree.DataTemplatesEntry;
import com.shrcn.business.scl.model.navgtree.EnumEntry;
import com.shrcn.business.scl.model.navgtree.EnumValEntry;
import com.shrcn.business.scl.model.navgtree.INaviTreeEntry;
import com.shrcn.business.scl.model.navgtree.LNEntry;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.ArrayUtil;
import com.shrcn.found.dbxapi.io.ScdSaxParser;
import com.shrcn.found.dbxapi.io.ScdSection;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-1-24
 */
/**
 * $Log: TemplateTreeDataFactory.java,v $
 * Revision 1.1  2013/03/29 09:38:31  cchun
 * Add:创建
 *
 * Revision 1.2  2011/11/03 02:34:24  cchun
 * Update:指定精确的节点名称，避免因为icd文件中含有<Private>导致程序出错
 *
 * Revision 1.1  2011/09/09 07:40:09  cchun
 * Refactor:转移包位置
 *
 * Revision 1.3  2011/03/08 07:13:03  cchun
 * Add:添加,增加对数据模板的过滤功能
 *
 * Revision 1.2  2011/01/27 01:03:01  cchun
 * Add:聂国勇增加，增加数据模板的详细显示功能
 *
 * Revision 1.1  2011/01/24 08:36:57  cchun
 * Update:将数据模板和IED视图分离
 *
 */
public class TemplateTreeDataFactory {
	
	private TemplateTreeDataFactory() {}
	
	public static List<INaviTreeEntry> create() {
		List<INaviTreeEntry> treeData = new ArrayList<INaviTreeEntry>();
		DataTemplatesEntry dataTempEntry = new DataTemplatesEntry("数据模板", 
				SCL.XPATH_DATATYPETEMPLATES, ImageConstants.PARENT, "数据模板");
		fillDataTemplates(dataTempEntry);
		treeData.add(dataTempEntry);
		return treeData;
	}
	
	/**
	 * 填充数据模板数据
	 * @param dataTempEntry
	 */
	private static void fillDataTemplates(INaviTreeEntry dataTempEntry) {
		Element dataTypeTemp = null;
		if (Constants.FINISH_FLAG) {
			dataTypeTemp = XMLDBHelper.selectSingleNode(dataTempEntry.getXPath());
		} else {
			dataTypeTemp = ScdSaxParser.getInstance().getPart(ScdSection.DataTypeTemplates);
		}
		if (dataTypeTemp == null)
			return;
		List<?> elements = dataTypeTemp.elements();
		for (Object obj : elements) {
			Element nd = (Element) obj;
			INaviTreeEntry entry = createEntry(nd);
			if (entry != null)
				dataTempEntry.addChild(entry);
		}
	}
	
	/**
	 * 根据数据模板创建树节点信息
	 * @param nd
	 * @return
	 */
	private static INaviTreeEntry createEntry(Element nd) {
		String ndName = nd.getName();
		String id = nd.attributeValue("id");
		if (ndName.equals("LNodeType")) {
			String lnClass = nd.attributeValue("lnClass");
			LNEntry lnEntry = new LNEntry(id, lnClass, ImageConstants.LN_TXT, UIConstants.DATATEMPLATE_EDITOR_ID);
			List<?> elements = nd.elements("DO");
			for (Object obj : elements) {
				Element doNd = (Element) obj;
				DOEntry doEntry = new DOEntry(
						doNd.attributeValue("name"),
						doNd.attributeValue("type"),
						ImageConstants.DOI_TXT);
				lnEntry.addChild(doEntry);
			}
			return lnEntry;
		} else if (ndName.equals("DOType")) {
			String cdc = nd.attributeValue("cdc");
			DOTypeEntry doTypeEntry = new DOTypeEntry(id, cdc, ImageConstants.DO_TXT);
			List<?> elements = nd.selectNodes("./*[name()='SDO' or name()='DA']");
			for (Object obj : elements) {
				Element daNd = (Element) obj;
				String type = daNd.attributeValue("type");
				DAEntry daEntry = new DAEntry(
						daNd.attributeValue("name"),
						(type!=null)?type:daNd.attributeValue("bType"),
						ImageConstants.DAI_TXT);
				doTypeEntry.addChild(daEntry);
			}
			return doTypeEntry;
		} else if (ndName.equals("DAType")) {
			DATypeEntry daTypeEntry = new DATypeEntry(id, "", ImageConstants.DA_TXT);
			List<?> elements = nd.elements("BDA");
			for (Object obj : elements) {
				Element bdaNd = (Element) obj;
				String type = bdaNd.attributeValue("type");
				BDAEntry bdaEntry = new BDAEntry(
						bdaNd.attributeValue("name"),
						(type!=null)?type:bdaNd.attributeValue("bType"),
						ImageConstants.BDA_TXT);
				daTypeEntry.addChild(bdaEntry);
			}
			return daTypeEntry;
		} else if (ndName.equals("EnumType")) {
			EnumEntry enumEntry = new EnumEntry(id, null, ImageConstants.E_TXT);
			List<?> elements = nd.elements("EnumVal");
			for (Object obj : elements) {
				Element enumNd = (Element) obj;
				EnumValEntry valEntry = new EnumValEntry(
						enumNd.getTextTrim(),
						enumNd.attributeValue("ord"),
						ImageConstants.V_TXT);
				enumEntry.addChild(valEntry);
			}
			return enumEntry;
		}
		return null;
	}
	
	/**
	 * 按名称过滤
	 * @param data
	 * @param name IED、数据类型名称
	 * @return
	 */
	public static List<INaviTreeEntry> getFiltTreeData(List<INaviTreeEntry> data, String name) {
		String[] names = name.split(",");
		List<INaviTreeEntry> newdata = new ArrayList<INaviTreeEntry>();
		for (INaviTreeEntry group : data) {
			ITreeEntry group1 = group.copy();
			newdata.add((INaviTreeEntry)group1);
			for (ITreeEntry item : group.getChildren()) {
				if (ArrayUtil.contains(item.getName(), names)) {
					group1.addChild(item);
				}
			}
		}
		return newdata;
	}
}
