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
 * GSE 数据操作类
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-4-27
 */
/*
 * 修改历史 $Log: GSEDAO.java,v $
 * 修改历史 Revision 1.1  2013/03/29 09:36:24  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.24  2012/09/03 07:03:03  cchun
 * 修改历史 Refactor:GSE数据访问类
 * 修改历史
 * 修改历史 Revision 1.23  2010/12/07 09:37:47  cchun
 * 修改历史 Update:去掉访问点过滤
 * 修改历史
 * 修改历史 Revision 1.22  2010/05/26 08:10:32  cchun
 * 修改历史 Update:修改构造函数参数
 * 修改历史
 * 修改历史 Revision 1.21  2010/01/19 09:02:30  lj6061
 * 修改历史 add:统一国际化工程
 * 修改历史
 * 修改历史 Revision 1.20  2009/12/11 05:26:48  cchun
 * 修改历史 Update:添加修改历史标记
 * 修改历史
 * 修改历史 Revision 1.19  2009/11/16 10:11:35  lj6061
 * 修改历史 删除多余的线程
 * 修改历史
 * 修改历史 Revision 1.18  2009/09/21 05:59:52  lj6061
 * 修改历史 删除: Xpath带有空格
 * 修改历史
 * 修改历史 Revision 1.17  2009/08/18 09:36:11  cchun
 * 修改历史 Update:合并代码
 * 修改历史
 * 修改历史 Revision 1.16  2009/06/18 08:35:53  lj6061
 * 修改历史 修改GSEName修改GSE
 * 修改历史
 * 修改历史 Revision 1.15  2009/06/17 03:35:51  cchun
 * 修改历史 Thread改成Job
 * 修改历史
 * 修改历史 Revision 1.14.2.1  2009/06/17 03:31:56  cchun
 * 修改历史 Thread改成Job
 * 修改历史
 * 修改历史 Revision 1.14  2009/06/11 03:51:43  lj6061
 * 修改历史 添加线程操作修改
 * 修改历史
 * 修改历史 Revision 1.13  2009/06/03 00:45:36  lj6061
 * 修改历史 添加GSE条件
 * 修改历史
 * 修改历史 Revision 1.12  2009/06/02 08:31:26  lj6061
 * 修改历史 GSE对应的级联删除
 * 修改历史
 * 修改历史 Revision 1.11  2009/05/23 12:07:10  lj6061
 * 修改历史 GSE新建时插入
 * 修改历史
 * 修改历史 Revision 1.10  2009/05/12 08:39:34  lj6061
 * 修改历史 改用行方式操作
 * 修改历史
 * 修改历史 Revision 1.9  2009/05/08 07:52:28  lj6061
 * 修改历史 提取列对应节点名
 * 修改历史
 * 修改历史 Revision 1.8  2009/05/07 03:16:02  lj6061
 * 修改历史 切换tab，datSet刷新
 * 修改历史
 * 修改历史 Revision 1.7  2009/05/06 12:40:29  lj6061
 * 修改历史 改用下标索引
 * 修改历史
 * 修改历史 Revision 1.6  2009/05/06 12:18:47  lj6061
 * 修改历史 改用下标索引
 * 修改历史
 * 修改历史 Revision 1.5  2009/05/05 06:11:01  lj6061
 * 修改历史 添加GSE功能
 * 修改历史
 * 修改历史 Revision 1.4  2009/04/29 09:41:32  lj6061
 * 修改历史 添加GSE功能
 * 修改历史
 * 修改历史 Revision 1.3  2009/04/29 02:23:35  cchun
 * 修改历史 统一数据操作对象接口
 * 修改历史
 * 修改历史 Revision 1.2  2009/04/28 07:50:52  lj6061
 * 修改历史 添加GSE功能
 * 修改历史 Revision 1.1 2009/04/27 09:45:57 lj6061 添加GSE控制页面
 * 
 */
public class GSEDAO extends BlockDao {

	private List<Element> data = null;
	private String[] dsName = null;

	public GSEDAO(String iedName, String ldInst) {
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
		return getLdXpath() + "/scl:LN0/scl:GSEControl[" + row + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * 级联删除获得GOOSE
	 */
	private String getGSESelect(String gseName) {
		return  "/scl:SCL/scl:Communication//scl:ConnectedAP[@iedName='" +iedName+"']"+ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"/scl:GSE[@cbName='"+gseName+"'][@ldInst='"+ ldInst+"']"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	@Override
	public void reLoad() {
		this.data = getGSEControl();
		this.dsName  = getDataSetName();
	}
	
	public void delete(int row) {
		//删除表格GSE
		String select = getSelect(row);
		String gseName = XMLDBHelper.getAttributeValue(select+ "/@name");
		XMLDBHelper.removeNodes(select);
		//删除表GSEControl对应通信部分的GSE
		String connSelect = getGSESelect(gseName);
		XMLDBHelper.removeNodes(connSelect);

		String ref = getCbRef("GO", gseName);
		HistoryManager.getInstance().markIedCfgChanged(IedCfg.GOOSE, OperType.DELETE, ref);
	}

	public void addNew(Element ele) {
		if (ele != null) {
			String gsePath = getLdXpath() + "/scl:LN0"; //$NON-NLS-1$
			insertGseNode(ele, gsePath);
		}
	}

	@Override
	public List<Element> getEleData() {
		return this.data;
	}
	
	/**
	 * 获取当前IED下所有的LN0的GSEControl
	 * 
	 * @return items
	 */
	private List<Element> getGSEControl() {
		String gsePath = getLdXpath() + "/scl:LN0/scl:GSEControl"; //$NON-NLS-1$
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
	

	/**
	 *  修改通信部分的GSEName 是修改GSE
	 */
	private void updateGSE(final String oldName, final String newName) {
		TaskManager.addTask(new Job("") { //$NON-NLS-1$
			protected IStatus run(IProgressMonitor monitor) {
				String select = getGSESelect(oldName);
				XMLDBHelper.saveAttribute(select, "cbName", newName); //$NON-NLS-1$
				return Status.OK_STATUS;
			}
		});
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
			XMLDBHelper.saveAttribute(select, name, value);
			if("name".equals(name))
				updateGSE(oldValue ,value);
			String ref = getCbRef("GO", XMLDBHelper.getAttributeValue(select + "/@name"));
			HistoryManager.getInstance().markIedCfgUpdate(IedCfg.GOOSE, ref, name, oldValue, value);
			if(!"confRev".equals(name))
				updateConfRev(select);
		}
	}
	
	/**
	 * 新建GOOSE控制块时，添加数据库
	 * @param ele  GOOSE控制块节点
	 * @param select 添加进数据库的路径
	 */
	private void insertGseNode(final Element ele, final String select) {
		TaskManager.addTask(new Job("") { //$NON-NLS-1$
			protected IStatus run(IProgressMonitor monitor) {
				XMLDBHelper.insertAsLast(select, ele);
				String ref = getCbRef("GO", ele.attributeValue("name"));
				HistoryManager.getInstance().markIedCfgChanged(IedCfg.GOOSE, OperType.ADD, ref);
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
