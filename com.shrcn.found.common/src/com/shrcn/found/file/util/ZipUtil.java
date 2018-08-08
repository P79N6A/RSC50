/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.util;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import com.shrcn.found.common.Constants;

/**
 * 压缩、解压缩操作类.
 * 
 * @author 孙春颖
 * @version 1.0, 2013 2 1
 */
/**
 * $Log: ZipUtil.java,v $
 * Revision 1.1  2013/02/05 02:39:05  scy
 * Add：增加压缩解压缩的工具类
 *
 */
public class ZipUtil {

	/**
	 * 压缩文件.
	 * 
	 * @param srcPath
	 * @param zipFilepath
	 * @throws RuntimeException
	 */
	public static void zip(String srcPath, String zipFilepath)
			throws BuildException, RuntimeException {
		File file = new File(srcPath);
		if (!file.exists())
			throw new RuntimeException("找不到指定的文件或目录：" + srcPath);
		
		Project proj = new Project();
		FileSet fileSet = new FileSet();
		fileSet.setProject(proj);
		// 判断是目录还是文件
		if (file.isDirectory()) {
			fileSet.setDir(file);
		} else {
			fileSet.setFile(file);
		}
		
		Zip zip = new Zip();
		zip.setProject(proj);
		zip.setDestFile(new File(zipFilepath));
		zip.addFileset(fileSet);
		zip.setEncoding(Constants.CHARSET_UTF8);
		zip.execute();
	}

	/**
	 * 解压缩.
	 *  
	 * @param zipFilepath
	 * @param destPath
	 * @throws BuildException
	 * @throws RuntimeException
	 */
	public static void unzip(String zipFilepath, String destPath)
			throws BuildException, RuntimeException {
		if (!new File(zipFilepath).exists())
			throw new RuntimeException("找不到指定的压缩文件：" + zipFilepath);

		Project proj = new Project();
		Expand expand = new Expand();
		expand.setProject(proj);
		expand.setTaskType("unzip");
		expand.setTaskName("unzip");
		expand.setEncoding(Constants.CHARSET_UTF8);

		expand.setSrc(new File(zipFilepath));
		expand.setDest(new File(destPath));
		expand.execute();
	}
}
