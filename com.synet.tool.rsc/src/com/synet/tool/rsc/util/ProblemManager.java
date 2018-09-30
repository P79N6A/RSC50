package com.synet.tool.rsc.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import com.shrcn.business.scl.SCLConstants;
import com.shrcn.found.ui.view.Problem;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.ViewManager;

public class ProblemManager {
	
	private static ProblemManager logger = new ProblemManager();
	
	private ProblemManager() {
	}
	
	public static ProblemManager getInstance() {
		return logger;
	}
	
	private void openView() {
		if (SwtUtil.hasUI()) {
			ViewManager.findView("com.shrcn.found.ui.view.ProblemView");
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
