/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.ui.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.shrcn.found.common.log.SCTLogger;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-3-3
 */
/*
 * 修改历史
 * $Log: ImgDescManager.java,v $
 * Revision 1.5  2013/09/25 01:24:52  cchun
 * Update:增加setImageDesc()
 *
 * Revision 1.4  2013/06/28 08:55:02  cchun
 * Refactor:重构
 *
 * Revision 1.3  2013/06/26 00:22:47  cxc
 * Update：增加ImageDescriptor的获取方法
 *
 * Revision 1.2  2013/05/16 13:54:43  cchun
 * Update:添加关于对话框
 *
 * Revision 1.1  2013/03/29 09:36:48  cchun
 * Add:创建
 *
 * Revision 1.13  2012/11/22 08:03:55  cchun
 * Update:添加ABOUT_RTU30
 *
 * Revision 1.12  2012/09/13 11:35:35  cchun
 * Update:增加EXCEL
 *
 * Revision 1.11  2012/09/03 07:19:38  cchun
 * Update:增加SORT
 *
 * Revision 1.10  2012/04/11 08:45:49  cchun
 * Update:增加图标COMPARE
 *
 * Revision 1.9  2011/09/06 08:26:19  cchun
 * Update:修改按钮图标
 *
 * Revision 1.8  2011/07/27 07:37:33  cchun
 * Update:添加图标
 *
 * Revision 1.7  2011/06/27 03:10:08  cchun
 * Update:添加远动“关于”菜单
 *
 * Revision 1.6  2011/03/18 08:38:47  cchun
 * Update:添加图标
 *
 * Revision 1.5  2011/03/02 08:28:51  cchun
 * Add:添加图标detail.gif
 *
 * Revision 1.4  2011/02/25 07:38:26  cchun
 * Update:添加图标
 *
 * Revision 1.3  2010/12/13 02:09:54  cchun
 * Add:checkbox图片
 *
 * Revision 1.2  2010/11/30 01:08:01  cchun
 * Update:添加图标
 *
 * Revision 1.1  2010/11/12 09:32:01  cchun
 * Refactor:修改IconManager类名为ImgDescManager，并移动至common项目
 *
 * Revision 1.4  2010/11/12 08:58:26  cchun
 * Update:使用统一图标
 *
 * Revision 1.3  2010/10/18 02:37:57  cchun
 * Update:清理引用
 *
 * Revision 1.2  2010/07/28 07:20:02  cchun
 * Update:添加图表
 *
 * Revision 1.1  2010/03/02 07:49:54  cchun
 * Add:添加重构代码
 *
 * Revision 1.23  2010/01/21 08:47:52  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.22  2009/12/09 07:19:23  cchun
 * Update:修改图片名称
 *
 * Revision 1.21  2009/12/09 07:17:30  cchun
 * Update:添加历史开关菜单
 *
 * Revision 1.20  2009/12/09 05:57:34  wyh
 * 增加图片
 *
 * Revision 1.19  2009/11/19 09:33:20  cchun
 * Update:增加纸型选择打印功能
 *
 * Revision 1.18  2009/11/19 08:28:55  cchun
 * Update:完成信号关联打印功能
 *
 * Revision 1.17  2009/08/10 08:56:57  hqh
 * 添加解除关联图标常量
 *
 * Revision 1.16.2.2  2009/08/10 08:31:59  hqh
 * 添加解除关联图标常量
 *
 * Revision 1.16.2.1  2009/08/10 08:18:39  hqh
 * 添加导出图标
 *
 * Revision 1.16  2009/07/03 09:14:29  lj6061
 * 添加全部展开图片
 *
 * Revision 1.15  2009/06/16 07:00:11  lj6061
 * 修改另存为提示信息
 *
 * Revision 1.14  2009/06/11 02:21:36  lj6061
 * 修改历史图片
 *
 * Revision 1.13  2009/06/10 01:52:10  lj6061
 * 添加打开历史工程
 *
 * Revision 1.12  2009/06/08 00:44:57  hqh
 * 添加另存为常量
 *
 * Revision 1.11  2009/06/02 05:26:01  hqh
 * 添加编辑器图片
 *
 * Revision 1.10  2009/05/31 01:30:35  hqh
 * 添加aboutDialog图片
 *
 * Revision 1.9  2009/05/28 06:08:19  lj6061
 * 添加复制粘贴图片
 *
 * Revision 1.8  2009/05/27 07:36:41  cchun
 * Update:添加schema校验功能
 *
 * Revision 1.7  2009/05/25 03:55:21  lj6061
 * 添加Open图标
 *
 * Revision 1.6  2009/05/25 03:51:02  lj6061
 * 修改定值添加图片
 *
 * Revision 1.4  2009/05/20 06:52:50  lj6061
 * 区分保存按钮
 *
 * Revision 1.3  2009/05/19 11:13:51  cchun
 * 添加FCDA按钮图片
 *
 * Revision 1.2  2009/05/19 08:28:53  cchun
 * Update:添加按钮图片
 *
 * Revision 1.1  2009/03/31 05:56:36  cchun
 * 添加ui包代码
 *
 */
public class ImgDescManager {
	
	private static IconsManager iconMgr = IconsManager.getInstance();
	private static Map<String, ImageDescriptor> cache = new HashMap<String, ImageDescriptor>();
	
	static {
		Field[] fields = ImageConstants.class.getDeclaredFields();
		final int MOD_EXPECTED = Modifier.PUBLIC | Modifier.STATIC;
		final int MOD_MASK = MOD_EXPECTED | Modifier.FINAL;
		try {
			for (Field f : fields) {
				if ((f.getModifiers() & MOD_MASK) != MOD_EXPECTED)
					continue;
				String icon = (String)f.get(null);
				ImageDescriptor imgdesc = iconMgr.getImageDescriptor(icon);
				cache.put(icon, imgdesc);
			}
		} catch (IllegalArgumentException e) {
			SCTLogger.error(e.getMessage());
		} catch (IllegalAccessException e) {
			SCTLogger.error(e.getMessage());
		}
	}
	
	public static ImageDescriptor getImageDesc(String icon) {
		return cache.get(icon);
	}
	
	public static void setImageDesc(Action action, String icon) {
		action.setImageDescriptor(getImageDesc(icon));
	}
	
	public static Image createImage(String icon) {
		return iconMgr.getImage(icon);
	}
}
