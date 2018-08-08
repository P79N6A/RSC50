/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.table;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-4
 */
/**
 * $Log: RTableContentProvider.java,v $
 * Revision 1.1  2013/03/29 09:37:33  cchun
 * Add:创建
 *
 * Revision 1.1  2011/01/04 09:25:58  cchun
 * Refactor:将table框架移动到common项目中
 *
 * Revision 1.1  2010/03/09 07:37:51  cchun
 * Add:添加远动配置插件
 *
 */
public class RTableContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object input) {
		if(input.getClass().isArray())
			return (Object[])input;
		if (input instanceof List)
			return ((List<?>) input).toArray();
		else
			return new Object[0];
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}