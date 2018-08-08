/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.app;

import java.lang.reflect.Field;

import org.eclipse.core.internal.registry.osgi.OSGIUtils;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.ObjectUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-1-11
 */
/**
 * $Log: MenuActionFactory.java,v $
 * Revision 1.2  2013/04/06 05:24:15  cchun
 * Refactor:统一使用newInstance()
 *
 * Revision 1.1  2013/03/29 09:37:42  cchun
 * Add:创建
 *
 * Revision 1.1  2011/09/15 08:38:31  cchun
 * Refactor:将公共菜单项移动到common插件
 *
 * Revision 1.4  2011/07/11 08:58:24  cchun
 * Refactor:使用类反射工具类
 *
 * Revision 1.3  2011/05/24 06:40:04  cchun
 * Fix Bug:修复ActionFactory可能找不到action的bug
 *
 * Revision 1.2  2010/12/01 08:26:57  cchun
 * Refactor:使用名称可配置的Action管理框架
 *
 * Revision 1.1  2010/11/23 06:13:48  cchun
 * Refactor:从61850项目移动到common项目
 *
 * Revision 1.2  2010/03/29 02:49:24  cchun
 * Refactor:重构方法签名
 *
 * Revision 1.1  2010/03/02 07:49:27  cchun
 * Add:添加重构代码
 *
 * Revision 1.3  2010/02/05 07:36:02  cchun
 * Refactor:将MenuAction移动到common插件下
 *
 * Revision 1.2  2010/01/21 08:47:45  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.1  2010/01/11 09:11:16  cchun
 * Update:使用自定义扩展点的方式重构菜单action
 *
 */
public class MenuActionFactory {
	
	private MenuActionFactory() {}
	
	/**
	 * 根据class类型创建实例
	 * @param clazz
	 * @return
	 */
	public static IAction create(String className, IWorkbenchWindow window) {
		Class<?> clazz = null;
		IAction action = null;
		try {
			int idx = className.indexOf('/');
			if(idx == -1) {
				clazz = Class.forName(className);
			} else {
				String bundleID = className.substring(0, idx);
				className = className.substring(idx+1);
				clazz = OSGIUtils.getDefault().getBundle(bundleID).loadClass(className);
			}
			action = (IAction) ObjectUtil.newInstance(clazz, new Class[]{IWorkbenchWindow.class}, window);
			if(null == action)
				action = (IAction) ObjectUtil.newInstance(clazz);
		} catch (ClassNotFoundException e) {
			SCTLogger.error("找不到MenuAction对应的class。", e);
		} catch (SecurityException e) {
			SCTLogger.error(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			SCTLogger.error(e.getLocalizedMessage());
		}
		return action;
	}
	
	/**
	 * 根据class名称和标题创建Action对象
	 * @param className
	 * @param text
	 * @return
	 */
	public static IAction createAction(String className, String text) {
		Class<?> clazz = null;
		IAction action = null;
		try {
			int idx = className.indexOf('/');
			if(idx == -1) {
				clazz = Class.forName(className);
			} else {
				String bundleID = className.substring(0, idx);
				className = className.substring(idx+1);
				clazz = OSGIUtils.getDefault().getBundle(bundleID).loadClass(className);
			}
			action = (IAction) ObjectUtil.newInstance(clazz, new Class[]{String.class}, text);
		} catch (ClassNotFoundException e) {
			SCTLogger.error("找不到MenuAction对应的class。", e);
		} catch (SecurityException e) {
			SCTLogger.error(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			SCTLogger.error(e.getLocalizedMessage());
		}
		return action;
	}
	
	/**
	 * 创建系统action
	 * @param actionId
	 * @param window
	 * @return
	 */
	public static IWorkbenchAction createById(String actionId, IWorkbenchWindow window) {
		IWorkbenchAction action = null;
		try {
			Field field = ActionFactory.class.getField(actionId);
			ActionFactory factory = (ActionFactory)field.get(null);
			action = factory.create(window);
		} catch (Exception e) {
			SCTLogger.error("创建系统action出错。", e); //$NON-NLS-1$
		}
		return action;
	}
}
