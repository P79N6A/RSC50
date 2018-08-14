/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.business.scl.filter.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;

import com.shrcn.business.scl.filter.Filter;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.navgtree.BayEntry;
import com.shrcn.business.scl.model.navgtree.INaviTreeEntry;
import com.shrcn.business.scl.model.navgtree.SubstationEntry;
import com.shrcn.business.scl.model.navgtree.VoltageLevelEntry;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-9-6
 */
/**
 * $Log: BayFilter.java,v $
 * Revision 1.1  2013/03/29 09:38:10  cchun
 * Add:创建
 *
 * Revision 1.1  2010/09/14 08:10:55  cchun
 * Refactor:添加filter包
 *
 */
public class BayFilter implements Filter<Object> {
	private static BayFilter bayFilter = null;

	private BayFilter() {
	}

	public static BayFilter newInstance() {
		if (bayFilter == null) {
			synchronized (BayFilter.class) {
				if (bayFilter == null) {
					bayFilter = new BayFilter();
				}
			}
		}
		return bayFilter;
	}

	/* (non-Javadoc)
	 * @see com.shrcn.sct.filter.Filter#filter(java.lang.Object)
	 */
	@Override
	public void filter(Object t) {
		if (!(t instanceof Map))
			return;
		@SuppressWarnings("unchecked")
		Map<INaviTreeEntry, Element> mpITreeEntry = (Map<INaviTreeEntry, Element>) t;
		Set<INaviTreeEntry> sITreeEntry = mpITreeEntry.keySet();
		Iterator<INaviTreeEntry> iter = sITreeEntry.iterator();
		boolean onlyBay = true;
		while (iter.hasNext()) {
			INaviTreeEntry obj = iter.next();
			if (!(obj instanceof BayEntry) || SCL.isBusbarEntry(obj)) {
				onlyBay = false;
			}
		}
		if (!onlyBay) {
			iter = sITreeEntry.iterator();
			while (iter.hasNext()) {
				INaviTreeEntry obj = iter.next();
				if (SCL.isBusbarEntry(obj))
					continue;
				if ((obj instanceof BayEntry)
						|| (obj instanceof VoltageLevelEntry)
						|| (obj instanceof SubstationEntry)) {
					iter.remove();
				}
			}
		}
	}
}
