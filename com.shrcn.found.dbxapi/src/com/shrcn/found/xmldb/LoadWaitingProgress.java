/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.shrcn.found.common.Constants;
import com.shrcn.found.dbxapi.io.ScdSaxParser;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-11
 */
public class LoadWaitingProgress implements IRunnableWithProgress {
	
	private String title;

	public LoadWaitingProgress(String title) {
		this.title = title;
	}
	
	public LoadWaitingProgress() {
		this("处理中，请稍候......");
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		int waitedTime = 0, TIME_OUT = 100;
		monitor.beginTask(title, TIME_OUT);
		while (!Constants.FINISH_FLAG) {
			Thread.sleep(500);
			monitor.worked(1);
			waitedTime++;
			if (waitedTime > TIME_OUT-1) {
				waitedTime = 0;
				monitor.beginTask(title, TIME_OUT);
			}
		}
		ScdSaxParser.getInstance().clearCache();
		monitor.done();
	}
	
}

