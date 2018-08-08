/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.swt.graphics.Image;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.tree.ConfigTreeAdapter;
import com.shrcn.found.ui.util.IconsManager;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;
import com.synet.tool.rsc.util.BayIEDEntry;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-11-12
 */
/**
 * $Log: EcfgTreeAdapter.java,v $
 * Revision 1.1  2013/11/12 05:54:00  cchun
 * Add:导航树类
 *
 */
public class EcfgTreeAdapter extends ConfigTreeAdapter {

	@Override
	public Image getImage(Object element) {
		if(element != null && element instanceof BayIEDEntry) {
			BayIEDEntry iedEntry = (BayIEDEntry) element;
			String icon = iedEntry.getIcon();
			if (!StringUtil.isEmpty(icon) && iedEntry.isSyned()) {
				Image image = IconsManager.getInstance().getImage(icon);
				DecorationOverlayIcon dicon = new DecorationOverlayIcon(image, 
						new ImageDescriptor[]{null,null,null, 
						ImgDescManager.getImageDesc(ImageConstants.SYNCED8)});
				return dicon.createImage();
			}
		}
		return super.getImage(element);
	}
}
