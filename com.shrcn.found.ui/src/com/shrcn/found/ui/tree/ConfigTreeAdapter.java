/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.tree;

import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.util.IconsManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-2
 */
/**
 * $Log: ConfigTreeAdapter.java,v $
 * Revision 1.4  2013/06/08 10:37:14  cchun
 * Update:增加编辑功能
 *
 * Revision 1.3  2013/04/07 12:27:34  cchun
 * Update:完成基础界面框架
 *
 * Revision 1.2  2013/04/06 05:34:18  cchun
 * Add:导航树基础类
 *
 * Revision 1.1  2013/04/03 00:34:37  cchun
 * Update:转移tree控件至found工程
 *
 * Revision 1.1  2013/04/02 13:27:04  cchun
 * Add:界面类
 *
 */
public class ConfigTreeAdapter {

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			List<?> input = (List<?>) inputElement;
			return input.toArray();
		}
		return new Object[0];// 空数组
	}
	
	public Object[] getChildren(Object parentElement) {
		ITreeEntry entry = (ITreeEntry) parentElement;
		List<ITreeEntry> list = entry.getChildren();
		if (list == null) {
			return new Object[0];
		}
		return list.toArray();
	}
	
	public boolean hasChildren(Object element) {
		ITreeEntry entry = (ITreeEntry) element;
		List<ITreeEntry> list = entry.getChildren();
		return !(list == null || list.isEmpty());
	}
	
	public Object getParent(Object element) {
		if(element instanceof ITreeEntry) {
			return ((ITreeEntry)element).getParent();
		}
		return null;
	}
	
	public String getText(Object element) {
		if (!(element instanceof ITreeEntry)){
			  return "Loading..."; //$NON-NLS-1$
			}
		ITreeEntry entry = (ITreeEntry)element;
		String name = entry.getName();
		String desc = entry.getDesc();
		return StringUtil.getLabel(name, desc);
	}
	
	public String getToolTipText(Object element) {
		ITreeEntry entry = (ITreeEntry) element;
		return StringUtil.emptyToNull(entry.getDesc());
	}
	
	public Image getImage(Object element) {
		if(element != null && element instanceof ITreeEntry){
			ITreeEntry entry = (ITreeEntry)element;
			String icon = entry.getIcon();
			if (!StringUtil.isEmpty(icon))
				return IconsManager.getInstance().getImage(icon);
		}
		return null;
	}
}
