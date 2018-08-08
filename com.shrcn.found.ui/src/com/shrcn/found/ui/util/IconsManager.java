/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.event.Context;
import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.common.event.IEventHandler;

/**
 * 此类是系统图标统一管理工具类。之所以引入缓存，是为了保证每个图标对象
 * 只被创建一次，避免出现org.eclipse.swt.SWTError: No more handles。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-7-13
 */
/**
 * $Log: IconsManager.java,v $
 * Revision 1.3  2013/08/26 06:33:24  cchun
 * Refactor:Constants.getIconPath()->Constants.getToolIconPath()
 * Fix Bug:修复refreshCache()缺陷
 *
 * Revision 1.2  2013/04/06 05:25:25  cchun
 * Refactor:直接使用refreshCache()
 *
 * Revision 1.1  2013/03/29 09:36:48  cchun
 * Add:创建
 *
 * Revision 1.4  2011/09/02 07:11:34  cchun
 * Update:添加getEqpImage()
 *
 * Revision 1.3  2011/01/05 07:37:37  cchun
 * Update:将LN关联改用图标提示
 *
 * Revision 1.2  2010/12/14 03:05:39  cchun
 * Refactor:重构SCT事件管理框架，将事件名称定义提取到独立的class中
 *
 * Revision 1.1  2010/11/12 08:56:53  cchun
 * Update:移动class位置
 *
 * Revision 1.2  2010/10/18 02:37:57  cchun
 * Update:清理引用
 *
 * Revision 1.1  2010/07/13 07:01:04  cchun
 * Update:添加树节点图标管理类
 *
 */
public class IconsManager implements IEventHandler {

	public static final String dirName = "icons";
	public static final String iconDir = Constants.ICONS_DIR + File.separator + dirName;
	private static IconsManager mgr = null;
	
	private Map<String, Image> imgCache = new HashMap<String, Image>();
	
	private IconsManager() {}
	
	/**
	 * 单例方法
	 * @return
	 */
	public static final IconsManager getInstance() {
		if(mgr == null) {
			mgr = new IconsManager();
			EventManager.getDefault().registEventHandler(mgr);
		}
		return mgr;
	}
	
	/**
	 * 根据文件名获取图形对象
	 * @param icon
	 * @return
	 */
	public Image getImage(String icon) {
		Image img = null;
		if (icon.startsWith(dirName)) {
			icon = icon.substring(dirName.length() + 1);
		}
		if(imgCache.containsKey(icon)) {
			img = imgCache.get(icon);
		} else {
			img = loadIconImage(icon);
		}
		return img;
	}
	
	/**
	 * 获取备图标
	 * @param name
	 * @return
	 */
	public Image getEqpImage(String name) {
		Image img = null;
		if(imgCache.containsKey(name)) {
			img = imgCache.get(name);
		} else {
			img = loadEqpImage(name);
		}
		return img;
	}
	
	/**
	 * 根据文件名获取图形描述对象
	 * @param icon
	 * @return
	 */
	public ImageDescriptor getImageDescriptor(String icon) {
		return ImageDescriptor.createFromImage(getImage(icon));
	}
	
	/**
	 * 刷新设备图标缓存
	 * @param name
	 */
	public void refreshEqpImage(String name) {
		removeFromCache(name);
		loadEqpImage(name);
	}
	
	/**
	 * 刷新普通图片缓存
	 * @param icon
	 */
	public void refreshCache(String icon) {
		removeFromCache(icon);
		loadIconImage(icon);
	}

	private void removeFromCache(String icon) {
		if (imgCache.containsKey(icon)) {
			Image img = imgCache.get(icon);
			imgCache.remove(icon);
			img.dispose();
			img = null;
		}
	}
	
	private Image loadIconImage(String icon) {
		Image img = ImageDescriptor.createFromFile(null,
				Constants.ICONS_DIR + File.separator + icon).createImage();
		if (img != null)
			imgCache.put(icon, img);
		return img;
	}
	
	private Image loadEqpImage(String name) {
		Image img = ImageDescriptor.createFromFile(null,
				Constants.getToolIconPath(name)).createImage();
		if (img != null)
			imgCache.put(name, img);
		return img;
	}

	@Override
	public void execute(Context context) {
		String property = context.getEventName();
		if (property.equals(EventConstants.SYS_REFRESH_ICONS)) {
			String icon = (String) context.getData();
			refreshCache("icons" + File.separator + icon + Constants.SUFFIX_GIF);
		}
	}
}
