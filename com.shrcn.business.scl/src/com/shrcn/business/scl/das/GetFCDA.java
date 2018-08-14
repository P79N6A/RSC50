/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import static com.shrcn.found.common.Constants.DOLLAR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.model.FCDAEntry;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 普洪涛(mailto:pht@shrcn.com)
 * @version 1.0, 2009-4-17
 */
/*
 * 修改历史
 * $Log: GetFCDA.java,v $
 * Revision 1.6  2011/12/21 06:49:40  cchun
 * Refactor:Class合并
 *
 * Revision 1.5  2011/11/30 11:08:00  cchun
 * Refactor:重构GetData构造函数
 *
 * Revision 1.4  2011/11/03 02:34:22  cchun
 * Update:指定精确的节点名称，避免因为icd文件中含有<Private>导致程序出错
 *
 * Revision 1.3  2011/09/21 01:24:47  cchun
 * Update:避免ldInst=null引发异常
 *
 * Revision 1.2  2011/08/11 06:45:25  cchun
 * Refactor:重构部分方法逻辑
 *
 * Revision 1.1  2010/12/20 02:14:49  cchun
 * Refactor:转移包位置
 *
 * Revision 1.5  2010/11/04 08:33:14  cchun
 * Fix Bug:修改DataTypeTemplateDao为静态调用，避免数据不同步错误
 *
 * Revision 1.4  2010/10/18 02:40:55  cchun
 * Update:清理引用
 *
 * Revision 1.3  2010/09/03 03:36:35  cchun
 * Refactor:使用公共方法；添加非空判断
 *
 * Revision 1.2  2010/07/28 07:25:50  cchun
 * Update:修改数据集加载为异步模式
 *
 * Revision 1.1  2010/03/02 07:49:05  cchun
 * Add:添加重构代码
 *
 * Revision 1.31  2010/01/21 08:47:22  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.30  2009/12/21 06:36:26  cchun
 * Update:整理代码，纠正elements()用法错误
 *
 * Revision 1.29  2009/09/29 03:27:03  lj6061
 * update:dU下Val不存在时空指针
 *
 * Revision 1.28  2009/09/21 05:59:48  lj6061
 * 删除: Xpath带有空格
 *
 * Revision 1.27  2009/09/10 03:44:59  lj6061
 * 修改数据集部分跨LD查询FCDA
 *
 * Revision 1.23.2.2  2009/09/09 08:05:51  lj6061
 * 修改 由于跨LD引发的无法修改和显示问题
 *
 * Revision 1.23.2.1  2009/08/25 06:17:15  lj6061
 * 合并主干代码，
 *
 * Revision 1.26  2009/08/25 06:06:37  lj6061
 * fix:不在LN0下的FCDA显示
 * fix:空异常
 * fix:修改du描述时创建DO
 *
 * Revision 1.25  2009/08/18 09:40:26  cchun
 * Update:合并代码
 *
 * Revision 1.23  2009/06/26 01:04:02  pht
 * 给重要方法添加注释。
 *
 * Revision 1.22  2009/06/10 09:09:04  pht
 * getDesc()和getDu()
 *
 * Revision 1.21  2009/06/10 08:46:52  pht
 * getDesc()和getDu()
 *
 * Revision 1.20  2009/06/10 05:29:53  pht
 * 查询desc,du是否存在时，不查询数据库，从ldevice中查询。
 *
 * Revision 1.19  2009/06/09 11:52:24  pht
 * getDesc()和getDu()复合型取最外层的。
 *
 * Revision 1.18  2009/06/09 09:37:22  pht
 * 当对应LN为空时，将fcda显示。
 *
 * Revision 1.17  2009/06/09 09:26:18  pht
 * 查询sAddr
 *
 * Revision 1.16  2009/06/09 06:28:56  pht
 * 修改getSAddr()和getDU(),中当daName为空，doName分层的情况。
 *
 * Revision 1.15  2009/05/25 12:46:28  cchun
 * 修改FCDA查询
 *
 * Revision 1.14  2009/05/23 08:27:09  cchun
 * Refactor:重构变量位置
 *
 * Revision 1.13  2009/05/19 09:23:21  cchun
 * Update:修改数据集保存步骤
 *
 * Revision 1.12  2009/05/19 08:30:31  cchun
 * Update:保存数据集之前判断位置
 *
 * Revision 1.11  2009/05/18 09:45:23  cchun
 * update:修改null指针异常
 *
 * Revision 1.10  2009/05/18 06:07:42  cchun
 * Update:添加goose关联和dataset定义功能
 *
 * Revision 1.9  2009/05/11 12:39:12  pht
 * Add function queryLDevice()
 *
 * Revision 1.8  2009/05/04 07:41:24  pht
 * Add function getDataTypeTemplates()
 * 	获得doi时，如果ln下面没有，则根据lnType到DataTypeTemplates下面去找对应的desc。
 *
 * Revision 1.7  2009/04/28 02:30:27  pht
 * 加了对prefix字段存在，但又为空时的条件判断。
 *
 * Revision 1.6  2009/04/27 09:34:05  pht
 * modify getSAddr()方法。
 *
 * Revision 1.5  2009/04/27 05:52:50  pht
 * 在显示表的数据时，将对应数据的lnClass,lnInst,prefix取出，因为后面可以据此判断对应的fcda,仅根据fcda,无法拆分出对应的lnClass,lnInst,prefix
 *
 * Revision 1.4  2009/04/24 07:28:13  pht
 * lnInst为null时，界面不显示此字段。
 *
 * Revision 1.3  2009/04/24 07:24:21  pht
 * prefix为null时，界面不显示此字段。
 *
 * Revision 1.2  2009/04/24 07:10:51  pht
 * 把修改后的类移动包的位置
 *
 * Revision 1.8  2009/04/24 06:59:33  pht
 * 去掉打印内容。
 *
 * Revision 1.7  2009/04/24 06:48:53  pht
 * 取出FCDA
 *
 * Revision 1.5  2009/04/22 10:51:20  pht
 * getDesc更改，使它更通用。
 *
 * Revision 1.4  2009/04/20 07:30:52  pht
 * 取数据的方式改为：先将数据取到内存中，再从内存中读数据。这样，时间优化 为原来的1%
 *
 */
public class GetFCDA {
	
	private String iedName;
	private String ldInst;
	private String iedXpath;
	private String datXPath;
	private Element lDevice;
	
	public GetFCDA(String iedName, String ldInst, String lnXpath,
			String name) {
		this.iedName = iedName;
		this.ldInst = ldInst;
		this.iedXpath = SCL.getIEDXPath(iedName);
		this.datXPath = iedXpath + "/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" //$NON-NLS-1$
			+ ldInst + "']" + lnXpath + "/scl:DataSet[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		lDevice = queryLDevice();
	}
	
	/**
	 * 初始化获取LD
	 * 
	 * @return
	 */
	private Element queryLDevice() {
		String query = iedXpath
				+ "/scl:AccessPoint/scl:Server/scl:LDevice[@inst=\"" + ldInst + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return XMLDBHelper.selectSingleNode(query);
	}

	/**
	 * 通过LD名称获取LD，在通过FCDA定义LD时使用
	 * 
	 * @return
	 */
	private Element queryLDevice(String ldInst) {
		String query = iedXpath
				+ "/scl:AccessPoint/scl:Server/scl:LDevice[@inst=\"" + ldInst + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return XMLDBHelper.selectSingleNode(query);

	}
	
	public void addFCDA(FCDAEntry entry) {
		XMLDBHelper.insertAsLast(datXPath, entry.toElemnt());
	}
	
	/**
	 * 
	 * 取得所有FCDAEntry
	 * @return 所有FCDAEntry的List
	 */
	public List<FCDAEntry> getListEntry() {
		List<FCDAEntry> listFcdaEntry = new ArrayList<FCDAEntry>();
		Element eleDataSet = XMLDBHelper.selectSingleNode(datXPath);
		if (eleDataSet == null)
			return listFcdaEntry;
		List<?> listFcda = eleDataSet.elements("FCDA");
		for (Object obj : listFcda) {
			Element fcda = (Element)obj;
			String prefix = fcda.attributeValue("prefix"); //$NON-NLS-1$
			String doName = fcda.attributeValue("doName"); //$NON-NLS-1$
			String lnInst = fcda.attributeValue("lnInst"); //$NON-NLS-1$
			String lnClass = fcda.attributeValue("lnClass"); //$NON-NLS-1$
			String ldInst = fcda.attributeValue("ldInst"); //$NON-NLS-1$
			String daName = fcda.attributeValue("daName"); //$NON-NLS-1$

			String desc = "";
			String sAddr = "";
			String du = "";
			Element ln = getLn(fcda);
			if (ln != null) {
				Element doi = SCL.getDOI(ln, doName);
				if (doi != null) {
					desc = SCL.getDOIDesc(doi, daName);
					sAddr = SCL.getSAddr(doi, daName);
					du = SCL.getDU(doi);
				}
			}
			
			FCDAEntry fcdaEntry = new FCDAEntry();
			String lnDoName = ldInst + DOLLAR + StringUtil.nullToEmpty(prefix)
					+ lnClass + StringUtil.nullToEmpty(lnInst) + DOLLAR + doName;
			fcdaEntry.setLNDoName(lnDoName);
			fcdaEntry.setDaName(StringUtil.nullToEmpty(daName)); //$NON-NLS-1$
			fcdaEntry.setDesc(StringUtil.nullToEmpty(desc)); //$NON-NLS-1$
			fcdaEntry.setSAddr(StringUtil.nullToEmpty(sAddr)); //$NON-NLS-1$
			fcdaEntry.setDu(du);
			//更新时用于定位。
			fcdaEntry.setPrefix(prefix);
			fcdaEntry.setLnClass(lnClass);
			fcdaEntry.setLnInst(lnInst);
			fcdaEntry.setDoName(doName);
			fcdaEntry.setLdInst(ldInst);

			listFcdaEntry.add(fcdaEntry);
		}
		
		return listFcdaEntry;
	}
	
	/**
	 * 获得FCDA对应的逻辑设备下的DOI信息
	 * @param fcda
	 * @return
	 */
	private Element getLn(Element fcda) {
		String ldInst = fcda.attributeValue("ldInst"); //$NON-NLS-1$
		String prefix = fcda.attributeValue("prefix"); //$NON-NLS-1$
		String lnInst = fcda.attributeValue("lnInst"); //$NON-NLS-1$
		String lnClass = fcda.attributeValue("lnClass"); //$NON-NLS-1$
		
		String preLdInst = lDevice.attributeValue("inst");
		if (!StringUtil.isEmpty(ldInst) && !preLdInst.equals(ldInst)) { //$NON-NLS-1$
			lDevice = queryLDevice(ldInst);
		}
		return DOM4JNodeHelper.selectSingleNode(lDevice, "." +
				SCL.getLNXPath(prefix, lnClass, lnInst));
	}
	
	public Map<String, FCDAEntry> getMapEntry() {
		Map<String, FCDAEntry> mapFcdaEntry = new HashMap<>();
		Element eleDataSet = XMLDBHelper.selectSingleNode(datXPath);
		if (eleDataSet == null)
			return mapFcdaEntry;
		List<?> listFcda = eleDataSet.elements("FCDA");
		for (Object obj : listFcda) {
			Element fcda = (Element)obj;
			String prefix = fcda.attributeValue("prefix"); //$NON-NLS-1$
			String doName = fcda.attributeValue("doName"); //$NON-NLS-1$
			String lnInst = fcda.attributeValue("lnInst"); //$NON-NLS-1$
			String lnClass = fcda.attributeValue("lnClass"); //$NON-NLS-1$
			String ldInst = fcda.attributeValue("ldInst"); //$NON-NLS-1$
			String daName = fcda.attributeValue("daName"); //$NON-NLS-1$

			String desc = "";
			Element ln = getLn(fcda);
			if (ln != null) {
				Element doi = SCL.getDOI(ln, doName);
				if (doi != null) {
					desc = SCL.getDOIDesc(doi, daName);
				}
			}
			
			FCDAEntry fcdaEntry = new FCDAEntry();
			String lnDoName = ldInst + DOLLAR + StringUtil.nullToEmpty(prefix)
					+ lnClass + StringUtil.nullToEmpty(lnInst) + DOLLAR + doName;
			fcdaEntry.setLNDoName(lnDoName);
			fcdaEntry.setDaName(StringUtil.nullToEmpty(daName)); //$NON-NLS-1$
			fcdaEntry.setDesc(StringUtil.nullToEmpty(desc)); //$NON-NLS-1$
			//更新时用于定位。
			fcdaEntry.setPrefix(prefix);
			fcdaEntry.setLnClass(lnClass);
			fcdaEntry.setLnInst(lnInst);
			fcdaEntry.setDoName(doName);
			fcdaEntry.setLdInst(ldInst);
			fcdaEntry.setIedName(iedName);
			
			mapFcdaEntry.put(desc, fcdaEntry);
		}
		return mapFcdaEntry;
	}
}
