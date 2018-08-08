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
 * @version 1.0, 2012-6-29
 */
/**
 * $Log: IDXFExporter.java,v $
 * Revision 1.1  2012/07/02 02:17:21  cchun
 * Add:重构后的dxf输出class
 *
 */
public interface IDXFExporter {

	public void write(PrintWriter w, IProgressMonitor monitor);

}