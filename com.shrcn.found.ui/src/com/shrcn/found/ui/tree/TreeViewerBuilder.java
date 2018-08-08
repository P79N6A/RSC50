/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.progress.IElementCollector;

import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.ui.action.ConfigAction;
import com.shrcn.found.ui.action.MenuAction;
import com.shrcn.found.ui.model.ConfigTreeEntry;
import com.shrcn.found.ui.model.IEDEntry;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.util.IconsManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-2
 */
/**
 * $Log: TreeViewerBuilder.java,v $
 * Revision 1.8  2013/08/14 03:43:10  cchun
 * Update:为fillMenus()添加null判断
 *
 * Revision 1.7  2013/05/17 08:33:04  cchun
 * Update:添加节点排序
 *
 * Revision 1.6  2013/04/24 06:00:20  zsy
 * Update: 执行setIcon()
 *
 * Revision 1.5  2013/04/17 01:38:36  scy
 * Fix Bug:修改菜单不能正确显示问题
 *
 * Revision 1.4  2013/04/16 05:00:30  cchun
 * Update:优化菜单处理逻辑
 *
 * Revision 1.3  2013/04/12 09:13:22  scy
 * Update：实现多级菜单显示
 *
 * Revision 1.2  2013/04/06 11:59:04  cchun
 * Fix Bug:为copyTo()添加parent
 *
 * Revision 1.1  2013/04/06 05:34:19  cchun
 * Add:导航树基础类
 *
 * Revision 1.1  2013/04/03 00:34:37  cchun
 * Update:转移tree控件至found工程
 *
 * Revision 1.1  2013/04/02 13:27:05  cchun
 * Add:界面类
 *
 */
public class TreeViewerBuilder {

	private Map<Class<?>, List<ITreeEntry>> treeCache = new HashMap<Class<?>, List<ITreeEntry>>();
	private Map<Class<?>, MenuManager> menuCache = new HashMap<Class<?>, MenuManager>();
	private String bundleID;

	private TreeViewerBuilder(Document doc) {
		parseConfig(doc);
	}
	
	public static TreeViewerBuilder create(Document doc) {
		return new TreeViewerBuilder(doc);
	}
	
	public static TreeViewerBuilder create(Class<?> baseClass, String path) {
		return create(XMLFileManager.loadXMLFile(baseClass, path));
	}
	
	private void parseConfig(Document doc) {
		if (doc == null)
			return;
		Element root = doc.getRootElement();
		bundleID = root.attributeValue("bundleID");
		List<?> configs = root.elements();
		for (Object obj : configs) {
			Element config = (Element) obj;
			String cfgClassName = config.attributeValue("class");
			if(cfgClassName ==null || cfgClassName == ""){
				continue;
			}
			Class<?> cfgClass = ObjectUtil.getClassByName(bundleID, cfgClassName);
			// 配置项
			List<Element> cfgitems = DOM4JNodeHelper.selectNodes(config, "./Configs/CfgItem");
			List<ITreeEntry> entries = new ArrayList<ITreeEntry>();
			getCfgEntries(null, cfgitems, entries);
			treeCache.put(cfgClass, entries);
			// 右键菜单
			List<Element> menus = DOM4JNodeHelper.selectNodes(config, "./Popup/MenuItem");
			MenuManager popMenu = new MenuManager();
			menuCache.put(cfgClass, popMenu);
			getCfgMenu(popMenu, menus);
		}
	}
	
	private void getCfgMenu(MenuManager popMenu, List<?> menus) {
		for (Object obj1 : menus) {
			Element menu = (Element) obj1;
			String title = menu.attributeValue("title");
			String icon = menu.attributeValue("icon");
			String menuClass = menu.attributeValue("class");
			Object obj = null;
			if (menuClass == null) {
				MenuManager subMenu = new MenuManager(title, title);
				getCfgMenu(subMenu, menu.elements());
				popMenu.add(subMenu);
			} else if (Separator.class.getName().equals(menuClass)) {
				obj = new Separator();
			} else {
				obj = ObjectUtil.newInstance(bundleID, menuClass, new Class[] {String.class}, title);
			}
			if (obj == null) {
				continue;
			}
			if (obj instanceof Separator) {
				popMenu.add((Separator) obj);
			} else {
				IAction action = (IAction) obj;
				if (!StringUtil.isEmpty(icon)) {
					action.setImageDescriptor(IconsManager.getInstance().getImageDescriptor(icon));
					if (action instanceof MenuAction) {
						((MenuAction)action).setIcon(icon);
					}
				}
				popMenu.add(action);
			}
		}
	}

	private ConfigTreeEntry getConfigEntry(Element item) {
		String name = item.attributeValue("name");
		String desc = item.attributeValue("desc");
		String icon = item.attributeValue("icon");
		String editorId = item.attributeValue("editorId");
		ConfigTreeEntry configTreeEntry = new ConfigTreeEntry(name, desc, icon, editorId);
		int index = DOM4JNodeHelper.getAttributeInt(item, "index");
		configTreeEntry.setIndex(index);
		return configTreeEntry;
	}
	
	private void getCfgEntries(ITreeEntry parent, List<?> cfgitems, List<ITreeEntry> entries) {
		for (Object obj : cfgitems) {
			Element item = (Element) obj;
			ITreeEntry itemNew = getConfigEntry(item);
			itemNew.setParent(parent);
			entries.add(itemNew);
			List<?> subitems = item.elements();
			if (subitems.size() > 0) {
				getCfgEntries(itemNew, subitems, itemNew.getChildren());
			}
		}
	}
	
	private void copyTo(ITreeEntry parent, List<ITreeEntry> source, List<ITreeEntry> target, IElementCollector collector, IProgressMonitor monitor) {
		for (ITreeEntry item : source) {
			ITreeEntry itemNew = item.copy();
			target.add(itemNew);
			itemNew.setParent(parent);
			List<ITreeEntry> subItems = item.getChildren();
			if (subItems.size() > 0) {
				copyTo(itemNew, subItems, itemNew.getChildren(), collector, monitor);
			}
			if(null != collector && parent instanceof IEDEntry)
				collector.add(itemNew, monitor);
		}
	}
	
	/**
	 * 设置配置项。
	 * @param iedEntry
	 */
	public void fillCfgItems(ITreeEntry iedEntry, IElementCollector collector, IProgressMonitor monitor) {
		List<ITreeEntry> cachedItems = treeCache.get(iedEntry.getClass());
		if (cachedItems == null)
			return;
		copyTo(iedEntry, cachedItems, iedEntry.getChildren(), collector, monitor);
	}
	
	/**
	 * 设置树节点菜单
	 * @param treeViewer
	 * @param menuManager
	 */
	public void fillMenus(ITreeEntry selEntry, MenuManager menuManager) {
		if (selEntry == null)
			return;
		MenuManager popMenu = menuCache.get(selEntry.getClass());
		if (popMenu != null) {
			setEntries(selEntry, menuManager, popMenu);
		}
	}

	private void setEntries(ITreeEntry selEntry, MenuManager menuManager, MenuManager popMenu) {
		IContributionItem[] items = popMenu.getItems();
		for (IContributionItem item : items) {
			if (item instanceof ActionContributionItem) {
				ConfigAction cfgAction = (ConfigAction) ((ActionContributionItem) item).getAction();
				cfgAction.setSelEntry(selEntry);
				menuManager.add(cfgAction);
			} else if (item instanceof Separator) {
				menuManager.add(item);
			} else if (item instanceof MenuManager) {
				MenuManager menu = (MenuManager) item;
				MenuManager mng = new MenuManager(menu.getMenuText());
				menuManager.add(mng);
				setEntries(selEntry, mng, menu);
			} 
		}
	}
}
