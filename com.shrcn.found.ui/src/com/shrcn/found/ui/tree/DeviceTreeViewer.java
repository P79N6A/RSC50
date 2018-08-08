/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;

import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-6-8
 */
/**
 * $Log: DeviceTreeViewer.java,v $
 * Revision 1.2  2013/11/12 05:58:04  cchun
 * Refactor:增加构造函数，支持继承
 *
 * Revision 1.1  2013/06/08 10:37:14  cchun
 * Update:增加编辑功能
 *
 */
public class DeviceTreeViewer extends TreeViewer {

	protected Tree tree = null;
	protected Point selectedPoint;
	protected TreeViewerBuilder treeBuilder;
	protected ConfigTreeAdapter adapter;
	
	public DeviceTreeViewer(Composite parent, int style, TreeViewerBuilder treeBuilder) {
		this(parent, style, treeBuilder, new ConfigTreeAdapter());
	}
	
	public DeviceTreeViewer(Composite parent, int style, TreeViewerBuilder treeBuilder, ConfigTreeAdapter adapter) {
		super(parent, style);
		this.treeBuilder = treeBuilder;
		this.adapter = adapter;
		this.tree = getTree();
		tree.setFont(UIConstants.FONT_CONTENT);
		ITreeContentProvider contentProvider = ((style & SWT.VIRTUAL)==0) ? 
				new DeviceContentProvider(adapter) : new ConfigContentProvider(adapter);
		setContentProvider(contentProvider);
        setLabelProvider(new ConfigLabelProvider(adapter));
        setSorter(new EntryIndexSorter());
        
        addListeners();
	}
	
	protected void addListeners() {
		final DeviceTreeViewer treeViewer = this;
		final Tree tree = getTree();
		// 监听鼠标动作
		tree.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent evt) {
				SwtUtil.changeExpandStatus(treeViewer);		// 折叠、展开
			}
			@Override
			public void mouseDown(MouseEvent evt) {
				selectedPoint = new Point(evt.x, evt.y);	// 记录点击坐标
			}
			@Override
			public void mouseUp(MouseEvent evt) {
				selectedPoint = null;
			}
		});
		// 更新节点上下文菜单
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(tree);
		tree.setMenu(menu);
		menuManager.addMenuListener(new IMenuListener(){
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				MenuManager menuMgr = (MenuManager)manager;
				menuMgr.removeAll();
				treeBuilder.fillMenus(treeViewer.getSelTreeEntry(), menuMgr);
       }});
	}
	
	/**
	 * 得到选择的树节点
	 * 
	 * @return
	 */
	public ITreeEntry getSelTreeEntry() {
		IStructuredSelection selection = (IStructuredSelection) getSelection();
		if (null != selectedPoint && null != getItemAt(selectedPoint)) { // 精确定位
			Object data = getItemAt(selectedPoint).getData();
			if (data instanceof ITreeEntry)
				return (ITreeEntry) data;
		} else {
			Object firstElement = selection.getFirstElement();
			if (firstElement instanceof ITreeEntry)
				return (ITreeEntry)firstElement;
		}
		return null;
	}
	
	/**
	 * 返回所有选中节点
	 */
	public List<ITreeEntry> getSelTreeEntries() {
		IStructuredSelection selection = (IStructuredSelection) getSelection();
		List<ITreeEntry> alEnts = new ArrayList<ITreeEntry>();
		Iterator<?> it = selection.iterator();
		while (it.hasNext())
			alEnts.add(((ITreeEntry) it.next()));
		return alEnts;
	}
	
	/**
	 * 选中指定节点
	 * @param entry
	 */
	public void setSelection(ITreeEntry selEntry) {
		IStructuredSelection selection = new StructuredSelection(new ITreeEntry[] {selEntry});
		setSelection(selection, true);
	}
}
