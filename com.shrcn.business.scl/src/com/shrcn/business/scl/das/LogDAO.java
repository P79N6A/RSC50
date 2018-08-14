/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.util.List;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.shrcn.business.scl.history.HistoryManager;
import com.shrcn.business.scl.history.MarkInfo.IedCfg;
import com.shrcn.business.scl.history.MarkInfo.OperType;
import com.shrcn.found.common.valid.BasicTypeValidator;
import com.shrcn.found.ui.dialog.MessageDialog;
import com.shrcn.found.ui.util.TaskManager;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * log 数据操作类
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-4-27
 */

public class LogDAO extends BlockDao {

	private List<Element> data = null;
	private String[] dsName = null;

	public LogDAO(String iedName, String ldInst) {
		super(iedName, ldInst);
		// 初始化界面时候取数据
		reLoad();
	}

	/**
	 * 获得LD的路径
	 */
	private String getLdXpath() {
		String ldXPath = iedXPath + "/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		return ldXPath;
	}

	/**
	 * 获得GSE控制的路径
	 */
	private String getSelect (int row){
		return getLdXpath() + "/scl:LN0/scl:LogControl[" + row + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	
	@Override
	public void reLoad() {
		this.data = getLogControl();
		this.dsName  = getDataSetName();
	}
	
	public void delete(int row) {
		//删除表格Log
		String select = getSelect(row);
		String logName = XMLDBHelper.getAttributeValue(select+ "/@name");
		XMLDBHelper.removeNodes(select);
		
		String ref = getCbRef("LG", logName);
		HistoryManager.getInstance().markIedCfgChanged(IedCfg.LogControl, OperType.DELETE, ref);
	}

	public void addNew(Element ele) {
		if (ele != null) {
			String logPath = getLdXpath() + "/scl:LN0"; //$NON-NLS-1$
			insertLogNode(ele, logPath);
		}
	}

	@Override
	public List<Element> getEleData() {
		return this.data;
	}
	
	/**
	 * 获取当前IED下所有的LN0的LogControl
	 * 
	 * @return items
	 */
	private List<Element> getLogControl() {
		String gsePath = getLdXpath() + "/scl:LN0/scl:LogControl"; //$NON-NLS-1$
		List<Element> items = XMLDBHelper.selectNodes(gsePath);
		return items;
	}
	/**
	 * 获取当前IED下所有的LN0的GSEControl
	 * 
	 * @return DataSet 
	 */
	public String[] getDataSetName() {
		String dsPath = getLdXpath() + "/scl:LN0/scl:DataSet/@name"; //$NON-NLS-1$
		List<String> names = XMLDBHelper.getAttributeValues( dsPath);
		return names.toArray(new String[names.size()]);
	}
	
     
	/*
	 * (non-Javadoc)
	 * @see com.shrcn.sct.das.BlockDao#update(int, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void update(final int row, final String name, final String oldValue, final String value, final boolean isMust) {
		if (!BasicTypeValidator.checkEmpty(value) && isMust) {
			MessageDialog.openError(null, Messages.getString("GSEDAO.warning"), Messages.getString("GSEDAO.null_warning")); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			String select = getSelect(row);
			boolean isSubAttr = false;
			String[] subAttrs = new String[] {"dchg","qchg","dupd","period"};
			for (String subAttr : subAttrs) {
				if (subAttr.equals(name)) {
					isSubAttr = true;
					break;
				}
			}
			if (isSubAttr)
				select += "/scl:TrgOps";
			XMLDBHelper.saveAttribute(select, name, value);
			
			String ref = getCbRef("LG", XMLDBHelper.getAttributeValue(select + "/@name"));
			HistoryManager.getInstance().markIedCfgUpdate(IedCfg.LogControl, ref, name, oldValue, value);
		}
	}
	
	/**
	 * 新建GOOSE控制块时，添加数据库
	 * @param ele  GOOSE控制块节点
	 * @param select 添加进数据库的路径
	 */
	private void insertLogNode(final Element ele, final String select) {
		TaskManager.addTask(new Job("") { //$NON-NLS-1$
			protected IStatus run(IProgressMonitor monitor) {
				XMLDBHelper.insertAsLast(select, ele);

				String ref = getCbRef("LG", ele.attributeValue("name"));
				HistoryManager.getInstance().markIedCfgChanged(IedCfg.LogControl, OperType.ADD, ref);
				return Status.OK_STATUS;
			}
		});
	}
	
	public void setLdInst(String ldInst) {
		this.ldInst = ldInst;
	}

	public String[] getDsName() {
		return this.dsName;
	}

	public void setIedName(String iedName) {
		this.iedName = iedName;
	}
	
}
