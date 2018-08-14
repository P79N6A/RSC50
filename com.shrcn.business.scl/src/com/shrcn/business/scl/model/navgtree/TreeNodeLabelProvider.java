/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.model.navgtree;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.shrcn.found.ui.util.IconsManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-8-12
 */
/**
 * $Log: TreeNodeLabelProvider.java,v $
 * Revision 1.1  2013/03/29 09:35:20  cchun
 * Add:创建
 *
 * Revision 1.1  2011/09/15 08:40:28  cchun
 * Refactor:修改归属包
 *
 * Revision 1.3  2011/05/30 09:50:12  cchun
 * Update:清理引用
 *
 * Revision 1.2  2010/11/12 08:56:31  cchun
 * Update:使用统一图标
 *
 * Revision 1.1  2010/08/12 07:29:31  cchun
 * Add:添加provider类
 *
 */
public class TreeNodeLabelProvider implements ILabelProvider {

	/**
	 * 取得图片
	 */
	public Image getImage(Object element) {
		if(element != null && element instanceof TreeNode){
			TreeNode entry = (TreeNode)element;
			String icon = entry.getIconName();
			if(icon != null){
				return IconsManager.getInstance().getImage(icon);
			}
		}
		return null;
	}

	/**
	 * 取得标签文字, 为名称
	 */
	public String getText(Object element) {
		TreeNode entry = (TreeNode) element;
		return entry.getName();
	}

	public void addListener(ILabelProviderListener arg0) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	public void removeListener(ILabelProviderListener arg0) {
	}

}
