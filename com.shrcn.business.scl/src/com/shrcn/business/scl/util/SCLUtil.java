/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Menu;

import com.shrcn.business.scl.action.VTConfigAction;
import com.shrcn.business.scl.action.VTExcelAction;
import com.shrcn.business.scl.action.VTFullScreenAction;
import com.shrcn.business.scl.action.VTPrintAction;
import com.shrcn.business.scl.action.VTRefreshAction;
import com.shrcn.business.scl.das.NamespaceFindVisitor;
import com.shrcn.business.scl.table.VTViewTable;

/**
 * @author 孙春颖
 * @version 1.0, 2014-5-28
 */
public class SCLUtil {
	
	private static final Pattern LNCLASS_PATTERN = Pattern.compile("[A-Z]{4}");
	private static final Pattern LNINST_PATTERN = Pattern.compile("\\d+");
	private static final Pattern LNCLASSINST_PATTERN = Pattern.compile("[A-Z]{4}\\d+$");
	
	/**
	 * 根据LN组合名获取prefix,lnClass,inst
	 * 
	 * @param lnName
	 * @return
	 */
	public static String[] getLNInfo(String lnName) {
		String prefix = null;
		String lnClass = null;
		String lnInst = null;
		int lnClassLength = 4; // lnClass名称长度
		if ("LLN0".equalsIgnoreCase(lnName))
			return new String[] { null, lnName, "" };
		if (Pattern.matches("[A-Z]{4}", lnName))
			return new String[] { null, lnName, "" };
		String[] tmpArr = lnName.split("[A-Z]{4}\\d+");
		if (tmpArr.length == 0) {
			tmpArr = lnName.split("\\d+");
			lnClass = tmpArr[0];
			lnInst = lnName.substring(lnClassLength);
		} else {
			Matcher matcher = LNCLASSINST_PATTERN.matcher(lnName);
			if (matcher.find()) {
				String lnClassInst = matcher.group();
				prefix = lnName.substring(0,
						lnName.length() - lnClassInst.length());
				matcher = LNCLASS_PATTERN.matcher(lnClassInst);
				if (matcher.find()) {
					lnClass = matcher.group();
				}
				matcher = LNINST_PATTERN.matcher(lnClassInst);
				if (matcher.find()) {
					lnInst = matcher.group();
				}
			}
		}
		return new String[] { prefix, lnClass, lnInst };
	}

	public static void createVTViewMenu(VTViewTable table) {
		MenuManager mmgr = new MenuManager();
		mmgr.add(new VTFullScreenAction(table));
		mmgr.add(new VTRefreshAction(table));
		mmgr.add(new VTExcelAction(table));
		mmgr.add(new VTPrintAction(table));
		mmgr.add(new Separator());
		mmgr.add(new VTConfigAction(table));
		Menu menu = mmgr.createContextMenu(table);
		table.setMenu(menu);
	}

	/**
	 * 1、补充IED新增的schema namespace定义
	 * 2、避免文件节点出现空的命名空间。
	 * 
	 * @param rootEl
	 */
	public static void updateRootNS(Element rootEl) {
		// 补充IED新增的schema namespace定义
		NamespaceFindVisitor visitor = new NamespaceFindVisitor();
		rootEl.accept(visitor);
		for (Namespace nsp : visitor.getNsList()) {
			rootEl.add(nsp);
		}
	}
}
