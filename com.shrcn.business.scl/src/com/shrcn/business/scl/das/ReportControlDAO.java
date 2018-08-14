/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.shrcn.business.scl.history.HistoryManager;
import com.shrcn.business.scl.history.MarkInfo.IedCfg;
import com.shrcn.business.scl.history.MarkInfo.OperType;
import com.shrcn.business.scl.model.EmReportAttribute;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.valid.BasicTypeValidator;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.dialog.MessageDialog;
import com.shrcn.found.ui.util.TaskManager;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-4-20
 */
/*
 * 修改历史
 * $Log: ReportControlDAO.java,v $
 * Revision 1.1  2013/03/29 09:36:23  cchun
 * Add:创建
 *
 * Revision 1.22  2012/09/03 07:03:53  cchun
 * Refactor:report数据访问类
 *
 * Revision 1.21  2012/03/09 07:35:50  cchun
 * Update:规范prefix和daName属性用法
 *
 * Revision 1.20  2012/01/13 08:40:02  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.19  2010/12/13 07:03:47  cchun
 * Refactor:重构数据保存方法
 *
 * Revision 1.18  2010/12/07 09:37:45  cchun
 * Update:去掉访问点过滤
 *
 * Revision 1.17  2010/08/18 10:30:17  cchun
 * Update:添加null判断和修旧属性值是否相等判断
 *
 * Revision 1.16  2010/05/26 08:10:31  cchun
 * Update:修改构造函数参数
 *
 * Revision 1.15  2009/12/18 08:48:22  lj6061
 * 修正 获得IEd节点语句
 *
 * Revision 1.14  2009/12/18 08:03:58  cchun
 * Update:还原ReportDAO设计
 *
 * Revision 1.13  2009/12/11 05:26:47  cchun
 * Update:添加修改历史标记
 *
 * Revision 1.12  2009/12/10 07:50:21  lj6061
 * 修正由于数据集增加，删除，重命名，引发的关联改变
 *
 * Revision 1.11  2009/11/17 03:23:43  lj6061
 * 报告控制块添加ClientLN功能
 *
 * Revision 1.10  2009/08/18 09:36:10  cchun
 * Update:合并代码
 *
 * Revision 1.9.4.1  2009/07/24 07:10:41  cchun
 * Update:切换数据库Sedna
 *
 * Revision 1.9  2009/05/20 04:47:44  cchun
 * Fix Bug:更新xmldb的同时需更新内存数据
 *
 * Revision 1.8  2009/05/05 12:07:16  cchun
 * Update:修改数据集查询方式为绝对路径
 *
 * Revision 1.7  2009/04/29 02:23:34  cchun
 * 统一数据操作对象接口
 *
 * Revision 1.6  2009/04/23 07:39:04  cchun
 * Refactor:重构属性保存方法
 *
 * Revision 1.5  2009/04/22 12:20:21  cchun
 * Update:添加报告属性保存方法
 *
 * Revision 1.4  2009/04/22 08:17:08  cchun
 * Update:修改ReportControl按LN过滤
 *
 * Revision 1.3  2009/04/22 02:23:11  cchun
 * Update:更新ReportControlDAO
 *
 * Revision 1.2  2009/04/20 11:00:48  cchun
 * Update:实现ReportControlDAO
 *
 * Revision 1.1  2009/04/20 09:03:34  cchun
 * Update:添加LNDAO，ReportControlDAO
 *
 */
public class ReportControlDAO extends BlockDao {
	
	public static final String REPORT_TAG = "ReportControl";
	private Element  lnData   = null;
	private List<Element> data = null;
	private String[] names = null;
	private String[] avaiblDatSets = null;
	private Map<String, String[]> allIedLd = new HashMap<String, String[]>();
	private String iedName;
	private int currentRow;
	
	public ReportControlDAO(String iedName, String ldInst,
			Element lnData) {
		super(iedName, ldInst);
		this.lnData = lnData;
		reLoad();
	}

	@Override
	public List<Element> getEleData() {
		return  this.data;
	}

	
	/**
	 * 更新属性
	 * row 选中的行
	 * name 要修改的属性的，名称
	 * oldValue 修改前的值
	 * value 修改后的值
	 * isMUst 是否可为空
	 */
	@Override
	public void update(final int row, final String name, final String oldValue,
			final String value, final boolean isMust) {
		if (!BasicTypeValidator.checkEmpty(value) && isMust) {
			MessageDialog.openError(null, Messages.getString("ReportControlDAO.warning"), 
					Messages.getString("ReportControlDAO.null_warning")); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			String select = getRCXPath(row);
			XMLDBHelper.saveAttribute(select, name, value);
			String ref = getCbRef("RP", XMLDBHelper.getAttributeValue(select + "/@name"));
			HistoryManager.getInstance().markIedCfgUpdate(IedCfg.ReportControl, ref, name, oldValue, value);
			if(!"confRev".equals(name))
				updateConfRev(select);
		}
	}
		
	@Override
	public void reLoad() {
		this.data = queryRCs();
		this.avaiblDatSets = queryAvaibleDatSet();
	}
	
	@SuppressWarnings("unchecked")
	private List<Element> queryRCs() {
		return lnData.elements(REPORT_TAG);
	}
	
	private String[] queryAvaibleDatSet() {
		List<String> allDatSet = new ArrayList<String>();
		
		String selectLN0 = iedXPath + "/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst +
				"']/scl:LN0/scl:DataSet/@name";
		List<String> datSetsLN0 = XMLDBHelper.getAttributeValues(selectLN0);
		String selectLN = iedXPath + "/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst +
				"']/scl:LN/scl:DataSet/@name";
		List<String> datSetsLN = XMLDBHelper.getAttributeValues(selectLN);
		if(datSetsLN0.size()>0)
			allDatSet.addAll(datSetsLN0);
		if(datSetsLN.size()>0)
			allDatSet.addAll(datSetsLN);
		return allDatSet.toArray(new String[allDatSet.size()]);
	}
	
	/**
	 * 保存报告属性
	 * @param rcName 报告名称
	 * @param att 属性类型
	 * @param value 属性值
	 */
	public void saveRCAttribute(int row, EmReportAttribute att, String value) {
		String attName = att.getAttName();
		String ndXpath = getRCXPath(row) + att.getSubPath();
		Element curRcNode = XMLDBHelper.selectSingleNode(getRCXPath(row));
		if(curRcNode == null)
			return;
		Element subNode = DOM4JNodeHelper.selectSingleNode(curRcNode, "." + att.getSubPath());
		String oldValue = subNode.attributeValue(attName);
		if(oldValue != null && oldValue.equals(value))
			return;
		subNode.addAttribute(attName, value);
		XMLDBHelper.saveAttribute(ndXpath, attName, value);
		
		String ref = getCbRef("RP", curRcNode.attributeValue("name"));
		HistoryManager.getInstance().markIedCfgUpdate(IedCfg.ReportControl, ref, attName, oldValue, value);
		updateConfRev(getRCXPath(row));
	}
	
	/**
	 * 获取报告xpath
	 * @param ln LN节点
	 * @param rcName 报告名称
	 * @return 报告xpath
	 */
	public String getRCXPath(String rcName) {
		return iedXPath + "/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst +
			"']" + SCL.getLNXPath(lnData) + "/scl:ReportControl[@name='" + rcName + "']";
	}
	
	/**
	 * 获取报告xpath
	 * @param ln LN节点
	 * @param row 行号
	 * @return 报告xpath
	 */
	public String getRCXPath(int row) {
		if (row < 1)
			row = 1;
		return iedXPath + "/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst +
			"']" + SCL.getLNXPath(lnData) + "/scl:ReportControl["+ row +"]";
	}
	
	/**
	 * 获取表格内的iedName下拉选项
	 * @return
	 */
	public void initIedName(){
		List<Element> ieds = getIeds();
		
		for (Element ied : ieds) {
			String tempIedName = ied.attributeValue("name");
			if(iedName.equals(tempIedName)) continue;
			String[] lds = ied.getText().split(" ");
			allIedLd.put(tempIedName, lds);
		}
	}

	public List<Element> getIeds() {
		List<Element> ieds = new ArrayList<Element>();
		if (Constants.XQUERY) {
			String xquery = "for $ied in " + XMLDBHelper.getDocXPath() +
					"/SCL/IED return " +
					"<IED name='{$ied/@name}'>" +
					"{" +
					"	$ied/AccessPoint[1]/Server/LDevice/@inst/string()" +
					"}" +
					"</IED>";
			ieds = XMLDBHelper.queryNodes(xquery);
		} else {
			List<Element> list =  XMLDBHelper.selectNodesOnlyAtts(SCL.XPATH_IED, "IED");
			for (Element iedEl : list) {
				String name = iedEl.attributeValue("name");
				Element iedCpEl = DOM4JNodeHelper.createSCLNode("IED");
				iedCpEl.addAttribute("name", name);
				
				List<String> insts = XMLDBHelper.getAttributeValues(SCL.XPATH_IED + "[@name='"+ name +"']/scl:AccessPoint/scl:Server/scl:LDevice/@inst");
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (String inst : insts) {
					if (i > 0)
						sb.append(" ");
					sb.append(inst);
					i++;
				}
				iedCpEl.addText(sb.toString());
				ieds.add(iedCpEl);
			}
		}
		return ieds;
	}
	
	/**
	 * 插入ClientLn到RptEnabled
	 * @param clientLN
	 */
	public void insertClientLn(final Element clientLN) {
		TaskManager.addTask(new Job("") {
			protected IStatus run(IProgressMonitor monitor) {
				String select = getRCXPath(currentRow) + "/scl:RptEnabled";
				XMLDBHelper.insertAsLast(select, clientLN);
				
				String ref = getCbRef("RP", XMLDBHelper.getAttributeValue(getRCXPath(currentRow) + "/@name"))
						+ "$ClientLN";
				HistoryManager.getInstance().markIedCfgChanged(IedCfg.ReportControl, OperType.ADD, ref);
				updateConfRev(getRCXPath(currentRow));
				return Status.OK_STATUS;
			}
		});
	}
	
	
	/**
	 * 删除ChiletLn
	 * @param row
	 */
	public void deleteClientLn(int row) {
		String select = getRCXPath(currentRow)+ "/scl:RptEnabled/scl:ClientLN[" + row + "]";
		XMLDBHelper.removeNodes(select);

		String ref = getCbRef("RP", XMLDBHelper.getAttributeValue(getRCXPath(currentRow) + "/@name"))
				+ "$ClientLN";
		HistoryManager.getInstance().markIedCfgChanged(IedCfg.ReportControl, OperType.DELETE, ref);
		updateConfRev(getRCXPath(currentRow));
	}
	
	/**
	 * 替换节点
	 * @param modify
	 * @param row
	 * @param string
	 */
	public void updateLN(int row, String attName, String modify) {
		String select = getRCXPath(currentRow)+ "/scl:RptEnabled/scl:ClientLN[" + row + "]";
		
		String ref = getCbRef("RP", XMLDBHelper.getAttributeValue(getRCXPath(currentRow) + "/@name"))
				+ "$ClientLN[" + row + "]";
		String oldValue = XMLDBHelper.getAttributeValue(select + "/@" + attName);
		HistoryManager.getInstance().markIedCfgUpdate(IedCfg.ReportControl, ref, attName, oldValue, modify);
		updateConfRev(getRCXPath(currentRow));

		XMLDBHelper.saveAttribute(select, attName, modify);
	}
	
	/**
	 * 更新LN
	 * @param row
	 * @param ele
	 */
	public void updateLN(int row, Element ele) {
		String select = getRCXPath(currentRow)+ "/scl:RptEnabled/scl:ClientLN[" + row + "]";
		XMLDBHelper.replaceNode(select, ele);
		
		String ref = getCbRef("RP", XMLDBHelper.getAttributeValue(getRCXPath(currentRow) + "/@name"))
				+ "$ClientLN[" + row + "]";
		HistoryManager.getInstance().markIedCfgChanged(IedCfg.ReportControl, OperType.REPLACE, ref);
		updateConfRev(getRCXPath(currentRow));
	}

	/**
	 * 上移
	 * @param selIdx
	 */
	public void moveUp(int selIdx) {
		XMLDBHelper.moveUp(getRCXPath(currentRow) + "/scl:RptEnabled/scl:ClientLN", selIdx);
		
		String ref = getCbRef("RP", XMLDBHelper.getAttributeValue(getRCXPath(currentRow) + "/@name"))
				+ "$ClientLN[" + selIdx + "]";
		HistoryManager.getInstance().markIedCfgChanged(IedCfg.ReportControl, OperType.MOVE_UP, ref);
		updateConfRev(getRCXPath(currentRow));
	}
	
	/**
	 * 下移
	 * @param selIdx
	 */
	public void moveDown(int selIdx) {
		XMLDBHelper.moveDown(getRCXPath(currentRow) + "/scl:RptEnabled/scl:ClientLN", selIdx);

		String ref = getCbRef("RP", XMLDBHelper.getAttributeValue(getRCXPath(currentRow) + "/@name"))
				+ "$ClientLN[" + selIdx + "]";
		HistoryManager.getInstance().markIedCfgChanged(IedCfg.ReportControl, OperType.MOVE_DOWN, ref);
		updateConfRev(getRCXPath(currentRow));
	}
	
	/**
	 * 获取当前AP,LD对应的LN name
	 * 
	 * @param iedXPath
	 * @return LD inst值数组
	 */
	public String[] getRCNames() {
		return this.names;
	}

	public String getIedXPath() {
		return iedXPath;
	}

	public void setIedXPath(String iedXPath) {
		this.iedXPath = iedXPath;
	}

	public String getLdInst() {
		return ldInst;
	}

	public void setLdInst(String ldInst) {
		this.ldInst = ldInst;
	}

	public String[] getAvaiblDatSets() {
		return avaiblDatSets;
	}

	public Element getLnData() {
		return lnData;
	}

	public void setLnData(Element lnData) {
		this.lnData = lnData;
	}

	@SuppressWarnings("unchecked")
	public List<Element> getClientLn() {
		List<Element> lnList = new ArrayList<Element>();
		Element rcData = XMLDBHelper.selectSingleNode(getRCXPath(currentRow));
		if (rcData != null) {
			Element rptEnabled = rcData.element("RptEnabled"); //$NON-NLS-1$
			if (rptEnabled != null) {
				lnList.addAll(rptEnabled.elements("ClientLN")); //$NON-NLS-1$
			}
		}
		return lnList;
	}

	public String[] getAllIedName() {
		return allIedLd.keySet().toArray(new String[allIedLd.size()]);
	}

	public String[] getAllLdName(String iedName) {
		return allIedLd.get(iedName);
	}

	public void setIedName(String iedName) {
		this.iedName = iedName;
	}

	public void setLnName(String lnName) {
		this.lnName = lnName;
	}

	public void setCurrentRow(int currentRow) {
		this.currentRow = currentRow;
	}
}
