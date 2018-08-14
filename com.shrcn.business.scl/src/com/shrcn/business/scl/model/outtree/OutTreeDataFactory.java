/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.outtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.progress.IElementCollector;

import com.shrcn.business.scl.das.SignalDAO;
import com.shrcn.business.scl.das.VTReportDAO;
import com.shrcn.business.scl.enums.EnumCtrlBlock;
import com.shrcn.business.scl.model.OutSingalFCDA;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.util.ArrayUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-8
 */
/*
 * 修改历史
 * $Log: OutTreeDataFactory.java,v $
 * Revision 1.17  2013/11/18 02:22:09  cxc
 * update:只与当前装置有关连的外部开出信号打上标记
 *
 * Revision 1.16  2013/11/13 00:20:52  cxc
 * update:增加为关联过的信号打上标记的功能
 *
 * Revision 1.15  2013/11/07 12:26:12  cchun
 * Update:外部信号中相角不参与信号关联
 *
 * Revision 1.14  2012/09/03 07:18:32  cchun
 * Update:为FCDAEntry增加序号属性
 *
 * Revision 1.13  2011/08/25 09:58:14  cchun
 * Update:不比较描述
 *
 * Revision 1.12  2011/05/09 11:31:12  cchun
 * Update:将FCDA过滤改成LD过滤
 *
 * Revision 1.11  2011/05/06 09:35:58  cchun
 * Fix Bug:增加LD层次
 *
 * Revision 1.10  2010/12/29 06:45:37  cchun
 * Update:添加对描述的过滤
 *
 * Revision 1.9  2010/12/21 07:30:30  cchun
 * Update:修改节点图标
 *
 * Revision 1.8  2010/11/12 08:56:32  cchun
 * Update:使用统一图标
 *
 * Revision 1.7  2010/10/19 07:08:42  cchun
 * Update:将外部信号视图数据集名改成对应的控制块名
 *
 * Revision 1.6  2009/10/23 07:22:49  cchun
 * Update:适应跨LD的情况
 *
 * Revision 1.5  2009/06/18 08:06:55  lj6061
 * 内部外部信号图标
 *
 * Revision 1.4  2009/06/11 07:49:02  cchun
 * 添加可拖拽图标
 *
 * Revision 1.3  2009/05/25 08:23:39  cchun
 * 添加采样值关联拖拽
 *
 * Revision 1.2  2009/05/12 06:09:14  cchun
 * Update:添加节点描述
 *
 * Revision 1.1  2009/05/08 12:07:17  cchun
 * Update:完善外部、内部信号视图
 *
 */
public class OutTreeDataFactory {
	/**
	 * 单例对象
	 */
	private static volatile OutTreeDataFactory instance = new OutTreeDataFactory();
	// 记录当前在关联的ied
	private String currIEDName;
	// 信号类型
	private EnumCtrlBlock signalType;
	
	/**
	 * 单例模式私有构造函数
	 */
	private OutTreeDataFactory(){
	}

	/**
	 * 获取单例对象
	 */
	public static OutTreeDataFactory getInstance(){
		if(null == instance) {
			synchronized (OutTreeDataFactory.class) {
				if(null == instance) {
					instance = new OutTreeDataFactory();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 加载指定装置和类型的外部信号
	 * @param currIED
	 * @param signalType
	 * @return
	 */
	public List<IOutTreeEntry> createOutTreeData(String currIED, EnumCtrlBlock signalType) {
		this.signalType = signalType;
		List<IOutTreeEntry> data = new ArrayList<IOutTreeEntry>();
		List<Element> iedDatasets = SignalDAO.queryOuterIeds(currIED, signalType);
		this.currIEDName = currIED;
		for (Element ied : iedDatasets) {
			String iedName = ied.attributeValue("name");
			String iedDesc = StringUtil.nullToEmpty(ied.attributeValue("desc"));
			IOutTreeEntry iedEntry = new OutTreeEntry(iedName, ImageConstants.IED, iedName, IOutTreeEntry.IED_ENTRY);
			iedEntry.setDesc(iedDesc);
			data.add(iedEntry);
		}
		return data;
	}
	
	/**
	 * 加载装置数据集信息
	 * @param iedEntry
	 * @param collector
	 * @param monitor
	 */
	public void fillOutIedEntry(IOutTreeEntry iedEntry, IElementCollector collector, IProgressMonitor monitor) {
		List<Element> aps = SignalDAO.queryIedDataSets(iedEntry.getName(), signalType);
		if(null != monitor)
			monitor.beginTask("Loading", aps.size());
		for (Element ap : aps) {
			String apName = ap.attributeValue("name");
			String apDesc = ap.attributeValue("desc");
			IOutTreeEntry apEntry = new OutTreeEntry(apName, ImageConstants.AP, apName, IOutTreeEntry.AP_ENTRY);
			apEntry.setDesc(apDesc);
			List<?> lds = ap.elements();
			for (Object obj : lds) {
				Element ld = (Element) obj;
				String ldInst = ld.attributeValue("inst");
				String ldDesc = ld.attributeValue("desc");
				IOutTreeEntry ldEntry = new OutTreeEntry(ldInst, ImageConstants.LDEVICE, ldInst, IOutTreeEntry.LD_ENTRY);
				ldEntry.setDesc(ldDesc);
				List<?> datSets = ld.elements();
				for (Object obj1 : datSets) {
					Element datSet = (Element) obj1;
					String datName = datSet.attributeValue("datName");
					String datDesc = datSet.attributeValue("datDesc");
					OutTreeEntry cbEntry = new OutTreeEntry(datName, ImageConstants.DATASET, datDesc, IOutTreeEntry.DAT_ENTRY);
					cbEntry.setDesc(datDesc);
					cbEntry.setDatName(datName);
					cbEntry.setDatDesc(datDesc);
					ldEntry.addChild(cbEntry);
				}
				apEntry.addChild(ldEntry);
			}
			iedEntry.addChild(apEntry);
			
			if(null != monitor) {
				monitor.worked(1);
				if(null != collector)
					collector.add(apEntry, monitor);
			}
		}
		if(null != monitor)
			monitor.done();
	}
	
	/**
	 * 加载goose、smv数据集信号
	 * @param entry
	 * @param collector
	 * @param monitor
	 */
	public void fillDataSetEntry(IOutTreeEntry entry, IElementCollector collector, IProgressMonitor monitor) {
		OutTreeEntry cbEntry = (OutTreeEntry)entry;
		String datName = cbEntry.getDatName();
		String iedName = cbEntry.getRootEntry().getName();
		List<OutSingalFCDA> fcdas = SignalDAO.getFCDAs(iedName, cbEntry.getParent().getName(), datName);
		
		if(null != monitor)
			monitor.beginTask("Loading", fcdas.size());
		int num = 1;
		List<Element> extrefs = XMLDBHelper.selectNodes(SCL.getIEDXPath(currIEDName) + "//Inputs/ExtRef");
		Map<String, String> refMap = new HashMap<String, String>();
		for (Element extref : extrefs) {
			refMap.put(SCL.getNodeFullRef(extref), extref.attributeValue("iedName"));
		}
		for (OutSingalFCDA fcda : fcdas) {
			String prefix = fcda.getPrefix();
			String lnInst = fcda.getLnInst();
			String daName = fcda.getDaName();
			if (daName!=null && daName.endsWith("ang.f")) { // 排除相角
				continue;
			}
			//Fix Bug: 外部信号可能跨LDevice
			
			//ldInst/prefix$lnClass$lnInst$doName$daName  
			String sgXPath = fcda.getLdInst();
			String name = sgXPath + "/" + (prefix==null?"":prefix) 
					+ fcda.getLnClass() + (lnInst==null?"":lnInst)
					+ "$" + fcda.getFC() + "$" + fcda.getDoName() + (daName==null?"":"$" + daName);
			FCDAEntry fcdaEntry = new FCDAEntry(name, ImageConstants.OUTPUT_SIG, name, IOutTreeEntry.FCDA_ENTRY, fcda ,num);
			num++;
			fcdaEntry.setDesc(fcda.getDesc());
			String fullRef = iedName + fcda.getLdInst() + "/" + (prefix==null?"":prefix)+fcda.getLnClass()+(lnInst==null?"":lnInst)
					+"." + fcda.getDoName() + (daName==null?"":"." + daName);
			fcdaEntry.setHaveSglRef(refMap.containsKey(fullRef));
			cbEntry.addChild(fcdaEntry);
			if(null != monitor) {
				monitor.worked(1);
				if(null != collector)
					collector.add(fcdaEntry, monitor);
			}
		}
		if(null != monitor)
			monitor.done();
	}

	/**
	 * 根据条件过滤信号树数据
	 * @param data
	 * @param txtIED
	 * @param txtAP
	 * @param txtLD
	 * @param txtDS
	 * @param filt
	 * @return
	 */
	public static List<IOutTreeEntry> getFiltTreeData(List<IOutTreeEntry> data, String txtIED, String txtAP, String txtLD, String txtDS, boolean filt) {
		String[] iedNames = txtIED.split(",");
		String[] apNames = txtAP.split(",");
		String[] dsNames = txtDS.split(",");
		String[] ldNames = txtLD.split(",");
		List<IOutTreeEntry> newdata = new ArrayList<IOutTreeEntry>();
		for (IOutTreeEntry ied : data) {
			if (ArrayUtil.contains(ied.getName(), iedNames)
					|| ArrayUtil.contains(ied.getDesc(), iedNames)) {
				ITreeEntry newIed = ied.copy();
				for (ITreeEntry ap : ied.getChildren()) {
					if (!filt) {
						newIed.addChild(ap);
						continue;
					}
					if (ArrayUtil.contains(ap.getName(), apNames)) {
						ITreeEntry newAp = ap.copy();
						for (ITreeEntry ld : ap.getChildren()) {
							if (ArrayUtil.contains(ld.getName(), ldNames)) {
							ITreeEntry newLD = ld.copy();
							for (ITreeEntry ds : ld.getChildren()) {
								if (ArrayUtil.contains(ds.getName(), dsNames)) {
									ITreeEntry newDs = ds.copy();
									for (ITreeEntry fcda : ds.getChildren()) {
										ITreeEntry newFcda = fcda.copy();
										newDs.addChild(newFcda);
									}
									newLD.addChild(newDs);
								}
							}
//							if (newLD.getChildren().size() > 0)
								newAp.addChild(newLD);
							}
						}
//						if (newAp.getChildren().size() > 0)
							newIed.addChild(newAp);
					}
				}
//				if (newIed.getChildren().size() > 0)
					newdata.add((IOutTreeEntry)newIed);
			}
		}
		return newdata;
	}
	
}
