/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.shrcn.business.scl.history.HistoryManager;
import com.shrcn.business.scl.history.MarkInfo.IedCfg;
import com.shrcn.business.scl.history.MarkInfo.OperType;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.valid.BasicTypeValidator;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.dialog.MessageDialog;
import com.shrcn.found.ui.util.TaskManager;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 采样值控制块数据访问类。
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-8-13
 */
/**
 * $Log: SMVControlDao.java,v $
 * Revision 1.4  2012/09/03 07:56:14  cchun
 * Refactor:简化程序
 *
 * Revision 1.3  2012/01/17 08:50:23  cchun
 * Update:使用更加安全的xpath形式
 *
 * Revision 1.2  2010/08/18 10:03:14  cchun
 * Update:去掉不必要的null判断
 *
 * Revision 1.1  2010/08/18 08:23:45  cchun
 * Refactor:采样值控制块重构
 *
 */
public class SMVControlDao extends BlockDao {

	private String ldXpath;
	private Element eleLDevice;

	
	public SMVControlDao(String iedName, String ldInst) {
		super(iedName, ldInst);
		init();
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		this.ldXpath = SCL.getLDXPath(iedName, ldInst);
		eleLDevice = XMLDBHelper.selectSingleNode(ldXpath);
	}
	
	@Override
	public List<Element> getEleData() {
		return getAllSVCB();
	}


    /**
	 * 重新加载
	 */
	@Override
	public void reLoad() {
		init();
	}

	@Override
	public void update(int row, String name, String oldValue, String value, boolean isMust) {
		if (!BasicTypeValidator.checkEmpty(value) && isMust) {
			MessageDialog.openError(null, Messages.getString("SMVControlDAO.warning"),
					Messages.getString("SMVControlDAO.null_warning"));
			return;
		}
		saveBaseAttr(row, name, value,  isMust);
		if(name.equals("name")) {
			updateSMVRelateInfo(oldValue, value);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Element> getAllSVCB(){
		return eleLDevice.element("LN0").elements("SampledValueControl");
	}
	
	/**
	 * 获取SVCB xpath
	 * @param svName
	 * @return
	 */
	private String getSMVXPath(String svName) {
		return ldXpath + "/scl:LN0/scl:SampledValueControl[@name='" + svName + "']";
	}
	
	/**
	 * 获取SVCB xpath
	 * @param svName
	 * @return
	 */
	private String getSMVXPath(int row) {
		return ldXpath + "/scl:LN0/scl:SampledValueControl[" + row + "]";
	}
	
	/**
	 * 保存基本属性
	 * @param svName
	 * @param attr
	 * @param newValue
	 */
	private void saveBaseAttr(int row, String name, String value, boolean isMust) {
		// 更新内存
		Element sampleCtrl = getControlElement(row);
		if (sampleCtrl == null)
			return;
		String ref = getCbRef("SV", sampleCtrl.attributeValue("name"));
		String oldValue = sampleCtrl.attributeValue(name);
		if (oldValue != null && oldValue.equals(value))
			return;
		sampleCtrl.addAttribute(name, value);
		// 更新xmldb
		String xpath = getSMVXPath(row);
		XMLDBHelper.saveAttribute(xpath, name, value);
		HistoryManager.getInstance().markIedCfgUpdate(IedCfg.SampledValueControl, ref, name, oldValue, value);
		if(!"confRev".equals(name))
			updateConfRev(xpath);
	}
	
	/**
	 * 保存选择属性
	 * @param svName
	 * @param attr
	 * @param newValue
	 */
	public void saveOptsAttr(int row , String attr, String newValue) {
		// 更新内存
		Element sampleCtrl = getControlElement(row);
		Element smvOpts = sampleCtrl.element("SmvOpts");
		String oldValue = smvOpts.attributeValue(attr);
		if(oldValue != null && oldValue.equals(newValue))
			return;
		smvOpts.addAttribute(attr, newValue);
		// 更新xmldb
		String optsXpath = getSMVXPath(row) + "/scl:SmvOpts";
		XMLDBHelper.saveAttribute(optsXpath, attr, newValue);
		
		String ref = getCbRef("SV", sampleCtrl.attributeValue("name"));
		HistoryManager.getInstance().markIedCfgUpdate(IedCfg.SampledValueControl, ref, attr, oldValue, newValue);
		updateConfRev(getSMVXPath(row));
	}
	
	/**
	 * 查询采样值控制块xml信息节点
	 * @return
	 */
	public Element getControlElement(String svName) {
		return DOM4JNodeHelper.selectSingleNode(eleLDevice, "./*[name()='LN0']/*[name()='SampledValueControl'][@name='" + 
				svName + "']");
	}
	
	
	/**
	 * 查询采样值控制块xml信息节点
	 * @return
	 */
	public Element getControlElement(int row) {
		return DOM4JNodeHelper.selectSingleNode(eleLDevice, "./*[name()='LN0']/*[name()='SampledValueControl'][" + 
				row + "]");
	}
	
	/**
	 * 得到当前LD下所有采样控制块名称
	 * @return
	 */
	public List<String> getSamCtrlNameList() {
		List<String> listName = new ArrayList<String>();
		List<?> listSam = eleLDevice.element("LN0").elements("SampledValueControl"); //$NON-NLS-1$ //$NON-NLS-2$
		for (Object obj : listSam) {
			Element samp = (Element) obj;
			listName.add(samp.attributeValue("name")); //$NON-NLS-1$
		}
		return listName;
	}
	
	/**
	 * 得到所有数据集名称
	 * @return
	 */
	public List<String> getDataSetList() {
		List<String> listName = new ArrayList<String>();
		String xpath = ".//*[name()='DataSet']"; //$NON-NLS-1$
		List<Element> listDataSet = DOM4JNodeHelper.selectNodes(eleLDevice, xpath);
		for (Element e : listDataSet) {
			listName.add(e.attributeValue("name")); //$NON-NLS-1$
		}
		return listName;

	}
	
	/**
	 * 得到该控制块的可选配置属性节点
	 * @param sampleCtrl
	 * @return
	 */
	public Element getSmvOpts(Element sampleCtrl) {
		return sampleCtrl.element("SmvOpts"); //$NON-NLS-1$
	}

	/**
	 * 获取采样值中某个采样值的IEDName
	 * @param sampleCtrl
	 * @return
	 */
	public List<String> getIedNameList(Element sampleCtrl) {
		List<String> listName = new ArrayList<String>();
		List<?> listIed = sampleCtrl.elements("IEDName"); //$NON-NLS-1$
		for (Object obj : listIed) {
			Element iedElement = (Element) obj;
			listName.add(iedElement.getText());
		}
		return listName;
	}
	
	/**
	 * 删除指定IED
	 * @param svName SVCB名称
	 * @param row 表格行号
	 */
	public void deleteIedName(int currentRow, int row) {
		// 更新内存
		Element sampleCtrl = getControlElement(currentRow);
		List<?> listIed = sampleCtrl.elements("IEDName"); //$NON-NLS-1$
		listIed.remove(row - 1);
		// 更新xmldb
		String xpath = getSMVXPath(currentRow) + "/scl:IEDName[" + row + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		String ref = getCbRef("SV", sampleCtrl.attributeValue("name")) + "$IEDName$" + XMLDBHelper.getNodeValue(xpath);
		HistoryManager.getInstance().markIedCfgChanged(IedCfg.SampledValueControl, OperType.DELETE, ref);
		updateConfRev(getSMVXPath(row));
		
		XMLDBHelper.removeNodes(xpath);
	}
	
	/**
	 * 向指定SVCB中添加IED
	 * @param iedName
	 */
	@SuppressWarnings("unchecked")
	public void addIedName(int row, String iedName){
		Element ndIEDName = DOM4JNodeHelper.createSCLNode("IEDName"); //$NON-NLS-1$
		ndIEDName.setText(iedName);
		// 更新内存
		Element sampleCtrl = getControlElement(row);
		List<Element> listIed = sampleCtrl.elements("IEDName"); //$NON-NLS-1$
		listIed.add(ndIEDName);
		// 更新xmldb
		String xpath = getSMVXPath(row);
		XMLDBHelper.insertAsLast(xpath, ndIEDName);
		
		String ref = getCbRef("SV", sampleCtrl.attributeValue("name")) + "$IEDName$" + iedName;
		HistoryManager.getInstance().markIedCfgChanged(IedCfg.SampledValueControl, OperType.ADD, ref);
		updateConfRev(xpath);
	}
	
	/**
	 * 更新IED
	 * @param svName
	 * @param row
	 * @param newName
	 */
	public void updateIEDName(int currentRow, int row, String newName){
		// 更新内存
		Element sampleCtrl = getControlElement(currentRow);
		List<?> listIed = sampleCtrl.elements("IEDName"); //$NON-NLS-1$
		Element ndIED = (Element) listIed.get(row - 1);
		ndIED.setText(newName);
		// 更新xmldb
		String xpath = getSMVXPath(currentRow) + "/scl:IEDName[" + row + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		XMLDBHelper.update(xpath, newName);
	}
	
	/**
	 * 增加一个SVCB
	 * @param sampleCtrl
	 */
	public void addSMV(Element sampleCtrl) {
		// 更新xmldb
		String lnXPath = ldXpath + "/scl:LN0";
		String[] nodeTypes = new String[]{"DataSet", "ReportControl", "LogControl", "DOI",
				"Inputs", "Log", "GSEControl", "SampledValueControl"};
		XMLDBHelper.insertAfterType(lnXPath, nodeTypes, sampleCtrl);
		// 更新内存
		init();
		
		String ref = getCbRef("SV", sampleCtrl.attributeValue("name"));
		HistoryManager.getInstance().markIedCfgChanged(IedCfg.SampledValueControl, OperType.ADD, ref);
	}
	
	/**
	 * 删除指定SVCB
	 * @param svName
	 */
	public void deleteSMV(String svName) {
		// 更新内存
		Element eleLN0 = eleLDevice.element("LN0");
		List<?> listSam = eleLN0.elements("SampledValueControl"); //$NON-NLS-1$ //$NON-NLS-2$
		for (Object obj : listSam) {
			Element samp = (Element) obj;
			String cbName = samp.attributeValue("name");
			if(svName.endsWith(cbName)) {
				eleLN0.remove(samp);
				
				String ref = getCbRef("SV", cbName);
				HistoryManager.getInstance().markIedCfgChanged(IedCfg.SampledValueControl, OperType.DELETE, ref);
			}
		}
		// 更新xmldb
		XMLDBHelper.removeNodes(getSMVXPath(svName));
	}
	
	/**
	 * 删除和指定SVCB相关联的信息（通信）
	 * @param svName
	 */
	public void deleteSMVRelateInfo(String svName) {
		// 更新xmldb
		String xpath = "/scl:SCL/scl:Communication/scl:SubNetwork/scl:ConnectedAP[@iedName='" + iedName //$NON-NLS-1$
				+ "']/scl:SMV[@ldInst='" + ldInst + "'][@cbName='" //$NON-NLS-1$ //$NON-NLS-2$
				+ svName + "']"; //$NON-NLS-1$
		XMLDBHelper.removeNodes(xpath);
	}
	
	/**
	 * 更该通信配置中的SNV控制块名称
	 * @param oldValue
	 * @param value
	 */
	private void updateSMVRelateInfo(final String oldValue, final String value){
		TaskManager.addTask(new Job("") { //$NON-NLS-1$
			protected IStatus run(IProgressMonitor monitor) {
				String xpath = "/scl:SCL/scl:Communication/scl:SubNetwork/scl:ConnectedAP[@iedName='" + iedName //$NON-NLS-1$
				+ "']/scl:SMV[@ldInst='" + ldInst + "'][@cbName='" //$NON-NLS-1$ //$NON-NLS-2$
				+ oldValue + "']";
				XMLDBHelper.saveAttribute(xpath, "cbName", value); //$NON-NLS-1$
				return Status.OK_STATUS;
			}
		});
	}

}
