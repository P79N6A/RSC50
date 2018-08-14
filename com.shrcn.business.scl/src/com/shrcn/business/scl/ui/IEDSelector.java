/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.ui;

import java.util.List;

import org.dom4j.Element;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.business.scl.das.IEDDAO;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-22
 */
/**
 * $Log: IEDSelector.java,v $
 * Revision 1.1  2013/06/28 08:45:31  cchun
 * Update:添加模型检查和虚端子关联查看
 *
 * Revision 1.3  2011/09/20 06:31:55  cchun
 * Update:增加selectByName()
 *
 * Revision 1.2  2011/07/27 07:47:20  cchun
 * Update:增加refresh(),selectAll()
 *
 * Revision 1.1  2011/07/22 03:19:23  cchun
 * Add:IED选择表格组件
 *
 */
public class IEDSelector {

	private Composite container;
	private List<Element> ieds;
	private CIDTable table;
	private CIDModel tbmodel;
	
	public IEDSelector(Composite container, List<Element> ieds) {
		this.container = container;
		this.ieds = ieds;
		initComponents();
	}
	
	public IEDSelector(Composite container) {
		this(container, IEDDAO.getAllIEDWithCRC());
	}
	
	public void reloadAll() {
		tbmodel.setItems(IEDDAO.getAllIEDWithCRC());
		table.redraw();
	}

	/**
	 * 初始化界面
	 */
	private void initComponents() {
		container.setLayout(new FillLayout());
		this.tbmodel = new CIDModel(ieds);
		this.table = new CIDTable(tbmodel, container);
	}
	
	/**
	 * 获取用户选择的IED名称
	 * @return
	 */
	public List<String> getSelectedIEDs() {
		return table.getSelectedIEDs();
	}
	
	/**
	 * 刷新
	 */
	public void refresh() {
		table.redraw();
	}
	
	/**
	 * 全选
	 */
	public void selectAll() {
		table.setSelectAll(true);
	}
	
	/**
	 * 选中指定IED
	 * @param iedName
	 */
	public void selectByName(String iedName) {
		table.selectByName(iedName);
	}
}
