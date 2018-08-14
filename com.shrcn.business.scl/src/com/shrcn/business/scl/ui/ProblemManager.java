/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import com.shrcn.business.scl.SCLConstants;
import com.shrcn.business.scl.check.Problem;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.ViewManager;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-12-23
 */
/**
 * $Log: ConsoleManager.java,v $
 * Revision 1.4  2013/11/06 09:16:46  cchun
 * Update:增加append()时间参数
 *
 * Revision 1.3  2013/06/14 01:36:40  cchun
 * Fix Bug:修复输出窗口为空的bug
 *
 * Revision 1.2  2013/05/20 03:12:21  cchun
 * Update:添加日志时间
 *
 * Revision 1.1  2013/03/29 09:36:45  cchun
 * Add:创建
 *
 * Revision 1.1  2010/12/29 06:46:22  cchun
 * Refactor:移动位置
 *
 * Revision 1.1  2010/12/23 10:11:45  cchun
 * Add:控制台日志类
 *
 */
public class ProblemManager {
	
	private static ProblemManager logger = new ProblemManager();
	
	private ProblemManager() {
	}
	
	public static ProblemManager getInstance() {
		return logger;
	}
	
	private void openView() {
		if (SwtUtil.hasUI()) {
			ViewManager.findView(SCLConstants.pviewId);
		}
	}
	
	public void append(final List<Problem> ps) {
		Display.getDefault().syncExec(new Runnable() { // 此处使用同步可避免线程冲突
			public void run() {
				openView();
				EventManager.getDefault().notify(SCLConstants.APPEND_PROBLEM, ps);
			}
		});
	}
	
	public void append(final Problem p) {
		List<Problem> ps = new ArrayList<>();
		ps.add(p);
		append(ps);
	}
	
	/**
	 * 清空控制台内容
	 */
	public void clear() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				openView();
				EventManager.getDefault().notify(SCLConstants.CLEAR_PROBLEM, null);
			}
		});
	}
}
