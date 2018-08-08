/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.dxf;

import java.io.PrintWriter;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-6-15
 */
/**
 * $Log: AbstractExporter.java,v $
 * Revision 1.2  2012/07/02 02:15:54  cchun
 * Refactor:实现IDXFExporter接口
 *
 * Revision 1.1  2012/06/18 09:38:15  cchun
 * Refactor:转移项目
 *
 * Revision 1.2  2011/06/24 06:40:14  cchun
 * Update:整理代码
 *
 * Revision 1.1  2011/06/17 06:32:53  cchun
 * Add:dxf导出API
 *
 */
 public abstract class AbstractExporter implements IDXFExporter {
	
	protected PrintWriter w;
	protected IProgressMonitor progress;
	private static final int progressTotal = 3;

	protected void incProgressCount(int step) {
		if (progress != null)
			progress.worked(step);
	}

	/* (non-Javadoc)
	 * @see com.shrcn.sct.dxf.IDXFExporter#write(java.io.PrintWriter, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void write(PrintWriter w, IProgressMonitor monitor) {
		this.w = w;
		this.progress = monitor;
		if (progress != null)
			progress.beginTask("开始导出", progressTotal);
		doWrite();
		if (progress != null)
			progress.done();
	}

	protected abstract void doWrite();
}
