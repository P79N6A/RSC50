/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;

import com.shrcn.business.scl.Activator;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-8-28
 */
/**
 * $Log: PopupBundleUtil.java,v $
 * Revision 1.1  2013/03/29 09:36:28  cchun
 * Add:创建
 *
 * Revision 1.1  2011/07/14 08:05:30  cchun
 * Refactor:修改类名
 *
 * Revision 1.3  2010/08/10 08:02:23  cchun
 * Refactor:清理注释
 *
 * Revision 1.2  2010/07/23 09:29:03  cchun
 * Fix Bug:修改图标路径bug
 *
 * Revision 1.1  2009/09/03 06:49:09  cchun
 * Refactor:重构类位置
 *
 * Revision 1.2  2009/08/31 04:03:53  cchun
 * Refactor:重构类名
 *
 * Revision 1.1  2009/08/28 07:43:03  cchun
 * Refactor:重构菜单创建程序结构
 *
 */
public class PopupBundleUtil {
	 private ResourceBundle resource;
	 private static volatile PopupBundleUtil instance = new PopupBundleUtil();
	 
	 private PopupBundleUtil() {
		 resource = ResourceBundle.getBundle(
				 PopupBundleUtil.class.getPackage().getName() + ".PopupMenu", 
				 Locale.getDefault());
	 }
	 
	 public static PopupBundleUtil getInstance() {
		 if(null == instance) {
			 synchronized (PopupBundleUtil.class) {
				 if(null == instance) {
					 instance = new PopupBundleUtil();
				 }
			 }
		 }
		 return instance;
	 }
	 
	 /**
	  * 从资源文件获取设备名称
	  * @param id
	  * @return
	  */
	 public String getLabel(String id) {
		 try {
			 return resource.getString("menu" + id + ".label");
		} catch (MissingResourceException e) {
			return null;
		}
	 }
	 
	 /**
	  * 从资源文件获取设备类型
	  * @param id
	  * @return
	  */
	 public String getType(String id) {
		 try {
			 return resource.getString("menu" + id + ".type");
		 } catch (MissingResourceException e) {
			 return null;
		 }
	 }
	 
	 /**
	  * 从资源文件获取设备LNode
	  * @param id
	  * @return
	  */
	 public String[] getLNode(String id) {
		 try {
			 String lnode = resource.getString("menu" + id + ".lnode");
			 if(null != lnode)
				 return lnode.split(",");
			 else
				 return null;
		 } catch (MissingResourceException e) {
			 return null;
		 }
	 }
	 
	 /**
	  * 从资源文件获取设备Terminal
	  * @param id
	  * @return
	  */
	 public int getTerminal(String id) {
		 try {
			 String terminal = resource.getString("menu" + id + ".terminal");
			 if(null != terminal)
				 return Integer.parseInt(terminal);
			 else
				 return -1;
		 } catch (MissingResourceException e) {
			 return -1;
		 }
	 }
	 
	 /**
	  * 根据id获取图片ImageDescriptor对象
	  * @param id
	  * @return
	  */
	 public ImageDescriptor getIcon(String id) {
		 try {
			 String iconName = resource.getString("menu" + id + ".icon");
			 if(null != iconName)
				 return Activator.imageDescriptorFromPlugin("com.shrcn.sct.iec61850", iconName);
			 else
				 return null;
		 } catch (MissingResourceException e) {
			 return null;
		 }
	 }
	 
	 /**
	  * 根据id获取菜单快捷键
	  * @param id
	  * @return
	  */
	 public char getAccelerator(String id) {
		 try {
			 String key = resource.getString("menu" + id + ".key");
			 if(null != key)
				 return (char)Integer.parseInt(key);
			 else
				 return 0;
		 } catch (MissingResourceException e) {
			 return 0;
		 }
	 }
}
