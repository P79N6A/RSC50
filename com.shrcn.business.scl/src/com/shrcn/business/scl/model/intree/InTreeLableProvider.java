/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.intree;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.util.IconsManager;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-6
 */
/*
 * 修改历史
 * $Log: InTreeLableProvider.java,v $
 * Revision 1.3  2013/11/13 08:57:39  cxc
 * update:修改图标的位置
 *
 * Revision 1.2  2013/11/13 00:19:08  cxc
 * update:修改获取图片的方法
 *
 * Revision 1.1  2011/09/15 08:40:24  cchun
 * Refactor:修改归属包
 *
 * Revision 1.4  2011/08/12 03:27:30  cchun
 * Update:修改getText()
 *
 * Revision 1.3  2010/11/12 08:59:08  cchun
 * Update:使用统一图标，消除org.eclipse.swt.SWTError: No more handles
 *
 * Revision 1.2  2010/09/21 01:05:31  cchun
 * Update:清理注释
 *
 * Revision 1.1  2010/03/02 07:48:56  cchun
 * Add:添加重构代码
 *
 * Revision 1.7  2010/01/21 08:47:59  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.6  2009/05/23 08:26:33  cchun
 * Refactor:重构变量位置
 *
 * Revision 1.5  2009/05/19 08:38:28  cchun
 * Update:添加冒号常量
 *
 * Revision 1.4  2009/05/12 06:12:24  cchun
 * Update:添加节点描述，与editor关闭事件处理
 *
 * Revision 1.3  2009/05/07 03:04:04  cchun
 * Update:修改懒加载实现
 *
 * Revision 1.2  2009/05/06 06:44:52  cchun
 * Update:修改isLabelProperty方法
 *
 * Revision 1.1  2009/05/06 06:40:10  cchun
 * Add:添加内部信号树视图
 *
 */
public class InTreeLableProvider implements ILabelProvider {

	/**
	 * 取得图片
	 */
	public Image getImage(Object element) {
		if(element != null && element instanceof IInnerTreeEntry){
			IInnerTreeEntry entry = (IInnerTreeEntry)element;
			String icon = entry.getIcon();
			boolean haveSglRef = entry.isHavaSglRef();
			if(icon != null){
				if (haveSglRef) {
					Image image = IconsManager.getInstance().getImage(icon);
					DecorationOverlayIcon dicon = new DecorationOverlayIcon(image, new ImageDescriptor[] { null, null,
									ImgDescManager.getImageDesc(ImageConstants.HAVE_SGLREF) , null});
					return dicon.createImage();
				} else {
					return IconsManager.getInstance().getImage(icon);
				}
			}
		}
		return null;
	}

	/**
	 * 取得标签文字, 为名称+描述
	 * @param obj
	 */
	public String getText(Object obj) {
		if (!(obj instanceof IInnerTreeEntry)){
		  return "Loading..."; //$NON-NLS-1$
		}
		IInnerTreeEntry entry = (IInnerTreeEntry)obj;
		String name = entry.getName();
		String desc = entry.getDesc();
		return StringUtil.getLabel(name, desc);
	}

	public void addListener(ILabelProviderListener arg0) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object arg0, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener arg0) {
	}

}
