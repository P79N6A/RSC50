/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.util;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.ActionSetRegistry;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;

import com.shrcn.found.common.log.SCTLogger;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-11
 */
/**
 * $Log: ExtensionUtil.java,v $
 * Revision 1.1  2013/03/29 09:36:30  cchun
 * Add:创建
 *
 * Revision 1.2  2010/11/23 06:11:49  cchun
 * Update:添加clearNoUseBar(),getExtensions(),removeBar()
 *
 * Revision 1.1  2010/03/16 12:17:04  cchun
 * Update: 更新
 *
 */
public class ExtensionUtil {
	
	/**
	 * 根据扩展点名称得到扩展点定义对象
	 * @param extensionPointID
	 * @return
	 */
	public static IExtensionPoint getExtensionPoint(String extensionPointID) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(extensionPointID); //$NON-NLS-1$
		if (point == null) {
			SCTLogger.error("找不到扩展点" + extensionPointID);
			return null;
		} else {
			return point;
		}
	}
	
	/**
	 * 删除RCP工程中无用的菜单和工具栏
	 */
	public static void clearNoUseBar() {
		ActionSetRegistry reg = WorkbenchPlugin.getDefault().getActionSetRegistry();
		IActionSetDescriptor[] actionSets = reg.getActionSets();
		String[] actionSetIds = new String[] {"org.eclipse.ui.edit.text.actionSet.navigation",
				"org.eclipse.ui.edit.text.actionSet.convertLineDelimitersTo",
				"org.eclipse.ui.edit.text.actionSet.annotationNavigation",
				"org.eclipse.ui.edit.text.actionSet.presentation"};
		for (String actionSetId : actionSetIds) {
			removeBar(reg, actionSets, actionSetId);
		}
	}

	/**
	 * 清除指定的aciton集合
	 * @param reg
	 * @param actionSets
	 * @param actionSetId
	 */
	private static void removeBar(ActionSetRegistry reg,
			IActionSetDescriptor[] actionSets, String actionSetId) {
		for (int i = 0; i < actionSets.length; i++) {
			if (!actionSets[i].getId().equals(actionSetId))
				continue;
			IExtension ext = actionSets[i].getConfigurationElement()
					.getDeclaringExtension();
			reg.removeExtension(ext, new Object[] { actionSets[i] });
		}
	}
	
	/**
	 * 获取扩展点元信息
	 * @param extensionId 扩展点id
	 * @return
	 */
	public static IExtension[] getExtensions(String extensionId) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(extensionId);
		return point == null ? null : point.getExtensions();
	}
}
