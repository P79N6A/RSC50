/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.util;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.internal.CompareDialog;
import org.eclipse.compare.internal.CompareUIPlugin;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.dialog.FileInputDialog;
import com.shrcn.found.ui.dialog.MessageDialog;
import com.shrcn.found.ui.dialog.ShellUntill;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-2-3
 */
/**
 * $Log: DialogHelper.java,v $
 * Revision 1.8  2013/11/18 00:55:15  cchun
 * Update:记录文件夹上次访问路径
 *
 * Revision 1.7  2013/11/07 02:41:23  cchun
 * Udpate:恢复showAsynError()为同步处理
 *
 * Revision 1.6  2013/11/04 09:30:09  cchun
 * 修改表格数据类型校验的错误, 如果类型错误, 就退回原来的值
 *
 * Revision 1.5  2013/09/25 01:20:37  cchun
 * Update:增加resetFile()
 *
 * Revision 1.4  2013/07/26 06:21:35  cchun
 * Update:增加exportExcel()
 *
 * Revision 1.3  2013/05/20 11:41:50  cchun
 * Update:添加getSaveFilePath()
 *
 * Revision 1.2  2013/04/07 12:24:57  cchun
 * Refactor:清理引用
 *
 * Revision 1.1  2013/03/29 09:36:50  cchun
 * Add:创建
 *
 * Revision 1.17  2013/03/26 02:08:30  cchun
 * Update:添加showConfirm()
 *
 * Revision 1.16  2012/11/20 06:09:27  cchun
 * Update:selectExcelFile()恢复
 *
 * Revision 1.15  2012/11/14 05:45:56  cchun
 * Refactor:修改selectExcelFile()参数
 *
 * Revision 1.14  2012/11/12 00:54:46  cchun
 * Refactor:封装getDefaultShell()
 *
 * Revision 1.13  2012/08/28 03:52:12  cchun
 * Update:添加文件选择对话框接口
 *
 * Revision 1.12  2012/01/13 08:39:55  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.11  2011/07/27 07:35:01  cchun
 * Update:添加showCompare()
 *
 * Revision 1.10  2011/07/22 03:13:13  cchun
 * Update:修改selectDirectory()入参
 *
 * Revision 1.9  2011/07/01 05:18:17  cchun
 * Fix Bug:修复dir为空的bug
 *
 * Revision 1.8  2011/06/27 09:09:53  cchun
 * Update:添加文件、文件夹选择方法
 *
 * Revision 1.7  2011/06/07 01:48:55  cchun
 * Update:将异步改成同步
 *
 * Revision 1.6  2011/04/07 09:28:03  cchun
 * Refactor:修改接口
 *
 * Revision 1.5  2010/12/29 06:46:56  cchun
 * Update:添加selectExcelFile()
 *
 * Revision 1.4  2010/09/09 08:15:21  cchun
 * Update:去掉常量
 *
 * Revision 1.3  2010/08/13 07:45:18  cchun
 * Update:添加selectDirectory()
 *
 * Revision 1.2  2010/03/29 02:44:42  cchun
 * Update:提交
 *
 * Revision 1.1  2010/02/03 07:16:55  cchun
 * Refactor:重构常用对话框工具类
 *
 */
public class DialogHelper {

	/**
	 * 显示信息提示框
	 * @param msg
	 */
	public static void showInformation(final String msg) {
	    MessageDialog.openInformation(getShell(), "信息", msg);
	}

	private static Shell getShell() {
		return SwtUtil.getDefaultShell();
	}
	
	/**
	 * 显示警告提示框
	 * @param msg
	 */
	public static void showWarning(final String msg) {
		MessageDialog.openWarning(getShell(), "警告", msg);
	}
	
	/**
	 * 显示错误提示框
	 * @param msg
	 */
	public static void showError(final String msg) {
		MessageDialog.openError(getShell(), "错误", msg);
	}
	
	/**
	 * 显示信息提示框
	 * @param msg
	 */
	public static void showAsynInformation(final String msg) {
		Display.getDefault().syncExec(new Runnable(){
			@Override
			public void run() {
				MessageDialog.openInformation(getShell(), "信息", msg);
			}
		});
	}
	
	/**
	 * 显示警告提示框
	 * @param msg
	 */
	public static void showAsynWarning(final String msg) {
		Display.getDefault().syncExec(new Runnable(){
			@Override
			public void run() {
				MessageDialog.openWarning(getShell(), "警告", msg);
			}
		});
	}
	
	/**
	 * 显示错误提示框
	 * @param msg
	 */
	public static void showAsynError(final String msg) {
		Display.getDefault().syncExec(new Runnable(){
			@Override
			public void run() {
				MessageDialog.openError(getShell(), "错误", msg);
			}
		});
	}
	
	/**
	 * 显示错误提示框
	 * @param msg
	 */
	public static void showErrorAsyn(final String msg) {
		Display.getDefault().asyncExec(new Runnable(){
			@Override
			public void run() {
				MessageDialog.openError(getShell(), "错误", msg);
			}
		});
	}
	
	/**
	 * 显示确认提示框
	 * @param msg
	 */
	public static boolean showConfirm(String msg) {
		return MessageDialog.openConfirm(getShell(), "确认", msg);
	}
	
	/**
	 * 显示确认提示框。
	 * @param msg
	 * @param strOk
	 * @param strCancel
	 * @return
	 */
	public static boolean showConfirm(String msg, String strOk, String strCancel) {
		MessageDialog dialog = new MessageDialog(getShell(), "确认", null, 
				msg, MessageDialog.QUESTION, new String[] { strOk, strCancel }, 0); // OK is the default
	    return dialog.open() == 0;
	}
	
	/**
	 * 选择文件夹路径
	 * @param shell
	 * @param defaultDir 缺省路径
	 * @return
	 */
	public static String selectDirectory(Shell shell, String defaultDir) {
		DirectoryDialog folderdlg = new DirectoryDialog(shell);
		if (StringUtil.isEmpty(defaultDir))
			defaultDir = UIPreferences.newInstance().getInfo(DirectoryDialog.class.getName());
		// 初始化路径"/" 转换
		folderdlg.setFilterPath(defaultDir);
		folderdlg.setText("文件夹选择");
		folderdlg.setMessage("请选择相应的文件夹");
		String path = folderdlg.open();
		if (!StringUtil.isEmpty(path))
			UIPreferences.newInstance().setInfo(DirectoryDialog.class.getName(), path);
		return path;
	}
	
	/**
	 * 选择文件夹路径
	 * @param shell
	 * @return
	 */
	public static String selectDirectory(Shell shell, int type, String title, String msg) {
		DirectoryDialog folderdlg = new DirectoryDialog(shell, type);
		folderdlg.setText(title);
		folderdlg.setMessage(msg);
		String defaultDir = UIPreferences.newInstance().getInfo(DirectoryDialog.class.getName());
		folderdlg.setFilterPath(defaultDir);
		String path = folderdlg.open();
		if (!StringUtil.isEmpty(path))
			UIPreferences.newInstance().setInfo(DirectoryDialog.class.getName(), path);
		return path;
	}
	
	/**
	 * 选择文件夹下指定后缀的文件
	 * @param shell
	 * @param title
	 * @param msg
	 * @param extensions
	 * @return
	 */
	public static List<String> selectDirFiles(Shell shell, String title, String msg, String[] extensions) {
		String dir = selectDirectory(shell, SWT.OPEN, title, msg);
		List<String> pathes = new ArrayList<String>();
		if (dir == null)
			return pathes;
		File fDir = new File(dir);
		if (!fDir.exists())
			return pathes;
		File[] fFiles = fDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dirT, String name) {
				if (name.toUpperCase().endsWith(".ICD"))
					return true;
				return false;
			}});
		for (File f : fFiles) {
			pathes.add(f.getAbsolutePath());
		}
		return pathes;
	}
	
	/**
	 * 选择文件夹下指定后缀的文件
	 * @param shell
	 * @param title
	 * @param msg
	 * @param extensions
	 * @return
	 */
	public static List<String> selectDirFiles2(Shell shell, String title, String msg, final String[] extensions) {
		String dir = selectDirectory(shell, SWT.OPEN, title, msg);
		List<String> pathes = new ArrayList<String>();
		if (dir == null)
			return pathes;
		File fDir = new File(dir);
		if (!fDir.exists())
			return pathes;
		File[] fFiles = fDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dirT, String name) {
				for (int i = 0; i < extensions.length; i++) {
				if (name.endsWith(extensions[i]))
					return true;
				}
				return false;
			}});
		for (File f : fFiles) {
			pathes.add(f.getAbsolutePath());
		}
		return pathes;
	}
	
	/**
	 * 选择多个文件路径
	 * @param shell
	 * @param extensions
	 * @return
	 */
	public static List<String> selectFiles(Shell shell, String...extensions) {
		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
		fileDialog.setFilterExtensions(extensions);
		fileDialog.open();
		String filterPath = fileDialog.getFilterPath();
		String[] names = fileDialog.getFileNames();
		List<String> pathes = new ArrayList<String>();
		for(String name : names) {
			pathes.add(filterPath + File.separator + name);
		}
		return pathes;
	}
	
	/**
	 * 选择文件路径
	 * @param shell
	 * @param type
	 * @param extensions
	 * @return
	 */
	public static String selectFile(Shell shell, int type, String...extensions) {
		return selectFile(shell, "", type, extensions);
	}
	
	public static String selectFile(Shell shell, String title, int type, String...extensions) {
		return selectFile(shell, title, type, extensions, null);
	}
	
	public static String selectFile(Shell shell, String title, int type, String[] exts, String[] extNames) {
		Shell shellNew = new Shell();
		FileDialog fileDialog = new FileDialog(shellNew, type);
		fileDialog.setText(title);
		fileDialog.setFilterExtensions(exts);
		fileDialog.setFilterNames(extNames);
		ShellUntill.centerShell(shellNew);
		return fileDialog.open();
	}
	
	/**
	 * 
	 * @param shell
	 * @param type
	 * @param exts
	 * @param defFileName 默认文件名
	 * @return
	 */
	public static String selectFile(Shell shell, int type, String[] exts, String defFileName) {
		Shell shellNew = new Shell();
		FileDialog fileDialog = new FileDialog(shellNew, type);
		fileDialog.setFilterExtensions(exts);
		fileDialog.setFileName(defFileName);
		ShellUntill.centerShell(shellNew);
		return fileDialog.open();
	}
	
	public static String resetFile(Shell shell, String title, String filePath, String...extensions) {
		FileInputDialog fileDialog = new FileInputDialog(shell, filePath, title, extensions);
		if (fileDialog.open() == FileInputDialog.OK) {
			return fileDialog.getFilePath();
		}
		return filePath;
	}
	
	/**
	 * 得到保存路径
	 * @param title
	 * @param defaultName
	 * @param exts
	 * @return
	 */
	public static String getSaveFilePath(String title, String defaultName, String...exts) {
		Shell shell = getShell();
		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setText(title);
		fileDialog.setFilterExtensions(exts);
		fileDialog.setFileName(defaultName);
		return fileDialog.open();
	}
	
	/**
	 * 显示字符串比较对话框
	 * @param txt1
	 * @param txt2
	 */
	public static void showCompare(String title1, final String txt1, String title2, final String txt2) {
		CompareEditorInput editorInput = createCompareInput(title1, txt1,
				title2, txt2);   
		  
//		editorInput.setTitle("文件比较");  
//		CompareUI.openCompareEditor(editorInput);  // 打开对比Editor   
		CompareUI.openCompareDialog(editorInput); // 弹出对比Dialog 
	}
	
	private static int result = -1;
	public static int showCompareDlg(String title1, String txt1, String title2, String txt2) {
		CompareUIPlugin plugin= CompareUIPlugin.getDefault();
		if (plugin == null)
			return result;
		final CompareEditorInput editorInput = createCompareInput(title1, txt1,
				title2, txt2);
		if (plugin.compareResultOK(editorInput, null)) {
			Runnable runnable = new Runnable() {
				public void run() {
					CompareDialog dialog = new CompareDialog(getShell(), editorInput);
					result = dialog.open();
				}
			};
			Display.getDefault().syncExec(runnable);
		}
		return result;
	}

	private static CompareEditorInput createCompareInput(String title1,
			final String txt1, String title2, final String txt2) {
		CompareConfiguration config = new CompareConfiguration();   
		config.setProperty(CompareConfiguration.SHOW_PSEUDO_CONFLICTS, Boolean.TRUE);   
		config.setProperty(CompareConfiguration.USE_OUTLINE_VIEW, Boolean.TRUE);   
		
		// left   
		config.setLeftEditable(false);   
		config.setLeftLabel(title1);   
		  
		// right   
		config.setRightEditable(false);   
		config.setRightLabel(title2);  

		CompareEditorInput editorInput = new CompareEditorInput(config) {   
			StringCompareItem icd = new StringCompareItem(txt1);
			StringCompareItem scd = new StringCompareItem(txt2);
		       
		    @Override  
		    protected Object prepareInput(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {   
		        return new DiffNode(null, Differencer.PSEUDO_CONFLICT, null, icd, scd);   
		    }   
		};
		return editorInput;
	}
}
