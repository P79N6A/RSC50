package com.synet.tool.rsc.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;

import com.shrcn.business.scl.SCLConstants;
import com.shrcn.found.ui.view.LEVEL;
import com.shrcn.found.ui.view.Problem;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.ViewManager;

public class ProblemManager {
	
	private static ProblemManager logger = new ProblemManager();
	private IViewPart problemView;
	
	private ProblemManager() {
	}
	
	public static ProblemManager getInstance() {
		return logger;
	}
	
	private void openView() {
		if (SwtUtil.hasUI()) {
			this.problemView = ViewManager.findView("com.shrcn.found.ui.view.ProblemView");
		}
	}
	
	public void append(final List<Problem> ps) {
		Display.getDefault().syncExec(new Runnable() { // 此处使用同步可避免线程冲突
			public void run() {
				openView();
				if (problemView != null)
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
				if (problemView != null)
					EventManager.getDefault().notify(SCLConstants.CLEAR_PROBLEM, null);
			}
		});
	}
	
	public void appendError(String title, String subType, String msg) {
		appendError(title, subType, "", msg);
	}
	
	public void appendError(String title, String subType, String ref, String msg) {
		SCTLogger.error(msg);
		append(new Problem(0, LEVEL.ERROR, title, subType, ref, msg));
	}
	
	public void appendWarning(String title, String subType, String msg) {
		appendWarning(title, subType, "", msg);
	}
	
	public void appendWarning(String title, String subType, String ref, String msg) {
		SCTLogger.error(msg);
		append(new Problem(0, LEVEL.WARNING, title, subType, ref, msg));
	}
}
