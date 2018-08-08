/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.view;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.internal.EditorReference;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.part.EditorPart;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.util.FileManager;
import com.shrcn.found.ui.util.StringCompareItem;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-1-28
 */
/**
 * $Log: ViewManager.java,v $
 * Revision 1.2  2013/04/07 12:24:58  cchun
 * Refactor:清理引用
 *
 * Revision 1.1  2013/03/29 09:36:45  cchun
 * Add:创建
 *
 * Revision 1.29  2012/04/19 10:25:29  cchun
 * Refactor:清除引用
 *
 * Revision 1.28  2012/02/29 07:13:40  cchun
 * Refactor:为closeEditor()添加参数editorId
 *
 * Revision 1.27  2011/12/02 03:47:28  cchun
 * Fix Bug:修改openEditor()查找处理
 *
 * Revision 1.26  2011/09/22 08:53:55  cchun
 * Update:添加文本比较方法
 *
 * Revision 1.25  2011/09/20 08:42:46  cchun
 * Update:为showCompare()增加字符集参数
 *
 * Revision 1.24  2011/09/20 06:54:46  cchun
 * Update:支持任意文本比较
 *
 * Revision 1.23  2011/09/19 08:28:25  cchun
 * Update:添加showCompare()
 *
 * Revision 1.22  2011/08/16 11:32:16  cchun
 * Fix Bug:修复类型转换异常
 *
 * Revision 1.21  2011/03/29 07:20:35  cchun
 * Update:添加异常处理
 *
 * Revision 1.20  2011/03/02 08:30:04  cchun
 * Update:添加existsView()
 *
 * Revision 1.19  2011/02/21 03:32:14  cchun
 * Update:恢复existsEditor(String title)
 *
 * Revision 1.18  2011/02/19 02:40:10  cchun
 * Update:聂国勇修改，修改判断是否是已打开编辑器的方法
 *
 * Revision 1.17  2011/01/28 02:41:43  cchun
 * Update:添加existsEditor()
 *
 * Revision 1.16  2011/01/13 03:24:39  cchun
 * Update:添加单线图编辑器操作方法
 *
 * Revision 1.15  2011/01/10 09:20:25  cchun
 * Fix Bug:修复ViewManager中强制数据类型转换错误
 *
 * Revision 1.14  2011/01/07 09:45:33  cchun
 * Fix Bug:修复showView()逻辑错误
 *
 * Revision 1.13  2010/12/29 06:47:30  cchun
 * Update:规范代码
 *
 * Revision 1.12  2010/11/23 06:45:31  cchun
 * Update:添加null判断
 *
 * Revision 1.11  2010/10/26 13:06:39  cchun
 * Update:添加对editor种类判断
 *
 * Revision 1.10  2010/10/26 09:13:57  cchun
 * Refactor:重构hideView();添加null判断
 *
 * Revision 1.9  2010/10/14 03:50:52  cchun
 * Add:增加closeEditor()
 *
 * Revision 1.8  2010/09/14 08:25:39  cchun
 * Update:添加findEditor()
 *
 * Revision 1.7  2010/09/06 04:51:48  cchun
 * Update;添加findEditor()
 *
 * Revision 1.6  2010/08/20 03:58:22  cchun
 * Fix Bug:修改编辑器重命名bug
 *
 * Revision 1.5  2010/08/10 03:40:24  cchun
 * Update:添加日志，并增加renameEditor()
 *
 * Revision 1.4  2010/03/29 02:44:42  cchun
 * Update:提交
 *
 * Revision 1.3  2010/03/09 08:41:39  cchun
 * Update:添加打开编辑器方法
 *
 * Revision 1.2  2010/02/08 10:41:06  cchun
 * Refactor:完成第一阶段重构
 *
 * Revision 1.1  2010/01/28 07:28:17  cchun
 * Refactor:将view操作相关方法移动到ViewManager中
 *
 */
public abstract class ViewManager {
	
	/**
	 * 隐藏视图
	 */
	public static void hideView(String id) {
		IWorkbenchPage activePage = getWorkBenchPage();
		if (activePage == null)
			return;
		IViewPart view = activePage.findView(id);
		if (view != null) {
			activePage.hideView(view);
		}
	}
	
	public static void hideView(String...views) {
		for(String viewId : views) {
			ViewManager.hideView(viewId);
		}
	}

	/**
	 * 显示视图
	 */
	public static boolean showView(String id) {
		boolean isShow = false;
		IWorkbenchPage activePage = getWorkBenchPage();
		if (activePage == null)
			return false;
		IViewPart view = activePage.findView(id);
		try {
			if (view == null) {
				activePage.showView(id);
			} else{
				activePage.activate(view);
			}
			activePage.bringToTop(view);
			isShow = true;
		} catch (PartInitException e) {
			SCTLogger.error("打开视图" + id +
					"出错：" + e.getMessage());
			isShow = false;
		}
		return isShow;
	}
	
	public static boolean showView(String...ids) {
		boolean isShow = false;
		for(String viewId : ids) {
			isShow = ViewManager.showView(viewId);
		}
		return isShow;
	}

	/**
	 * 查找视图
	 * @param window
	 * @param id
	 * @return
	 */
	public static IViewPart findView(String id) {
		final IWorkbenchPage activePage = getWorkBenchPage();
		if (activePage == null)
			return null;
		IViewPart findView = activePage.findView(id);
		try {
			if (findView == null) {
				findView = activePage.showView(id);
			} else {
				activePage.activate(findView);
			}
			activePage.bringToTop(findView);
		} catch (PartInitException e) {
			SCTLogger.error("查找视图" + id +
					"出错：" + e.getMessage());
		}
		return findView;
	}
	
	/**
	 * 判断view是否已经打开。
	 * @param viewId
	 * @return
	 */
	public static boolean existsView(String viewId) {
		IWorkbenchPage activePage = getWorkBenchPage();
		if (activePage == null)
			return false;
		return activePage.findView(viewId) != null;
	}
	
	/**
	 * 判断是否存在指定id的editor
	 * @param id
	 * @return
	 * @throws PartInitException 
	 */
	public static IEditorPart findEditor(String id) {
		IWorkbenchPage activePage = getWorkBenchPage();
		if (activePage == null)
			return null;
		try {
			IEditorPart editorPart = null;
			for(IEditorReference reference : activePage.getEditorReferences()) {
				EditorReference ref = (EditorReference) reference;
				IEditorInput input = ref.getEditorInput();
				editorPart = activePage.findEditor(input);
				if (ref.getId().equals(id))
					return editorPart;
			}
		} catch (PartInitException e) {
			SCTLogger.warn("视图窗口初始化异常：", e);
		}
		return null;
	}
	
	/**
	 * 根据cfgInput name找相应的IEditorPart
	 * @param id
	 * @param cfgInput
	 * @return
	 * @throws PartInitException
	 */
	public static IEditorPart findEditor(String id, IEditorInput cfgInput) {
		IWorkbenchPage activePage = getWorkBenchPage();
		if (activePage == null)
			return null;
		IEditorPart editorPart = null;
		try {
			for (IEditorReference reference : activePage.getEditorReferences()) {
				EditorReference ref = (EditorReference) reference;
				IEditorInput input = ref.getEditorInput();
				editorPart = activePage.findEditor(input);
				if (ref.getId().equals(id)
						&& input.getName().equalsIgnoreCase(cfgInput.getName()))
					return editorPart;
			}
		} catch (PartInitException e1) {
			SCTLogger.error("查找编辑器" + id + "出错：" + e1.getMessage());
		}
		return null;
	}
	
	/**
	 * 得到系统IWorkbenchPage
	 * @return
	 */
	public static IWorkbenchPage getWorkBenchPage() {
		IWorkbenchWindow[] workbenchWindows = PlatformUI.getWorkbench().getWorkbenchWindows();
		return (workbenchWindows == null || workbenchWindows.length == 0) ? null : workbenchWindows[0].getActivePage();
	}
	
	/**
	 * 关闭当前应用窗口中所有editor
	 */
	public static void closeAllEditors() {
		IWorkbenchPage activePage = getWorkBenchPage();
		if (activePage != null) {
			activePage.closeAllEditors(true);
		}
	}
	
	/**
	 * 关闭当前应用窗口中所有指定类型之外的editor
	 * @param editors
	 */
	public static void closeAllEditorsExcept(Class<?>...editors) {
		WorkbenchPage activePage = (WorkbenchPage)getWorkBenchPage();
		if (activePage != null) {
			List<IEditorReference> refList = new ArrayList<IEditorReference>();
			IEditorReference[] refs = activePage.getAllEditorReferences();
			for (IEditorReference ref : refs) {
				boolean excluded = false;
				for (Class<?> eCls : editors) {
					if (ref.getEditor(false).getClass() == eCls) {
						excluded = true;
						break;
					}
				}
				if (!excluded) {
					refList.add(ref);
				}
			}
			activePage.closeEditors(refList.toArray(new IEditorReference[0]), true);
		}
	}
	
	/**
	 * 关闭指定编辑器
	 * @param editorId
	 * @param editorRefName
	 */
	public static void closeEditor(String editorId, String editorRefName){
		IWorkbenchPage workbenchPage = getWorkBenchPage();
		if (workbenchPage == null)
			return;
		IEditorReference[] editorReferences = workbenchPage
				.getEditorReferences();
		for (IEditorReference editorRef : editorReferences) {
			String refname = editorRef.getPartName();
			String refEditorId = editorRef.getId();
			if ((editorRefName==null || editorRefName.equals(refname))
					&& editorId.equals(refEditorId)) {
				IEditorPart editor = editorRef.getEditor(true);
				if (editor != null)
					workbenchPage.closeEditor(editor, true);
			}
		}
	}
	
	/**
	 * 打开指定编辑器
	 * @param window
	 * @param editorID
	 * @param editorInput
	 * @return
	 */
	public static IEditorPart openEditor(String editorID, IEditorInput editorInput) {
		if (editorID == null)
			return null;
		IWorkbenchPage workbenchPage = getWorkBenchPage();
		if (workbenchPage == null)
			return null;
		IEditorPart editor = workbenchPage.findEditor(editorInput);
		// 如果此编辑器已经存在，则将它设为当前的编辑器（最顶端），否则 重新打开一个编辑器
		if (editor != null) {
			workbenchPage.bringToTop(editor);
		} else {
			try {
				editor = workbenchPage.openEditor(editorInput, editorID);
			} catch (PartInitException e) {
				SCTLogger.error("打开编辑器" + editorID +
						"出错：" + e.getMessage());
			}
		}
		return editor;
	}
	
	/**
	 * 判断某种类型的editor是否存在
	 * @param editorClass
	 * @return
	 */
	public static boolean existsEditor(Class<?> editorClass) {
		IWorkbenchPage activePage = getWorkBenchPage();
		if (activePage == null)
			return false;
		try {
			EditorPart editorPart = null;
			for(IEditorReference ref : activePage.getEditorReferences()) {
				IEditorInput input = ref.getEditorInput();
				editorPart = (EditorPart) activePage.findEditor(input);
				if (editorPart == null)
					continue;
				if (editorPart.getClass() == editorClass)
					return true;
			}
		} catch (PartInitException e) {
			SCTLogger.error(e.getMessage());
		}
		return false;
	}
	
	/**
	 * 判断是否存在相同标题的editor
	 * @param title
	 * @return
	 */
	public static boolean existsEditor(String title) {
		IWorkbenchPage activePage = getWorkBenchPage();
		if (activePage == null)
			return false;
		try {
			EditorPart editorPart = null;
			for(IEditorReference ref : activePage.getEditorReferences()) {
				IEditorInput input = ref.getEditorInput();
				editorPart = (EditorPart) activePage.findEditor(input);
				if (editorPart == null)
					continue;
				if (editorPart.getTitle().equals(title)) {
					activePage.bringToTop(editorPart);
					return true;
				}
			}
		} catch (PartInitException e) {
			SCTLogger.error(e.getMessage());
		}
		return false;
	}
	
	/**
	 * 打开透视图
	 * @param perspectiveId
	 * @param activeWorkbenchWindow
	 * @throws ExecutionException
	 */
	public static void openPerspective(final String perspectiveId) {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final IWorkbenchWindow activeWorkbenchWindow = workbench.getWorkbenchWindows()[0];

		final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		IAdaptable input = null;
		if (activePage != null) {
			input = activePage.getInput();
		}
		try {
			workbench.showPerspective(perspectiveId, activeWorkbenchWindow, input);
		} catch (WorkbenchException e) {
			SCTLogger.error("不能打开透视图" + perspectiveId + ".", e);
		}
	}
	
	/**
	 * 打开文件对比Editor
	 * @param file1
	 * @param file2
	 * @param charset
	 */
	public static void showCompare(String file1, String file2, String charset) {
		String name1 = new File(file1).getName();
		String name2 = new File(file2).getName();
		showCompare(name1 + "与" + name2 +
				"比较", getCompareCfg(file1, file2), 
				FileManager.readFileByCharset(file1, charset), 
				FileManager.readFileByCharset(file2, charset));
	}
	
	/**
	 * 打开字符对比Editor
	 * @param title1
	 * @param txt1
	 * @param title2
	 * @param txt2
	 */
	public static void showCompare(String title1, final String txt1, String title2, final String txt2) {
		showCompare(title1 + "与" + title2 +
				"比较", getCompareCfg(title1, title2), txt1, txt2);
	}
	
	/**
	 * 获取比较配置
	 * @param title1
	 * @param title2
	 * @return
	 */
	private static CompareConfiguration getCompareCfg(String title1, String title2) {
		CompareConfiguration config = new CompareConfiguration();   
		config.setProperty(CompareConfiguration.SHOW_PSEUDO_CONFLICTS, Boolean.TRUE);   
		config.setProperty(CompareConfiguration.USE_OUTLINE_VIEW, Boolean.TRUE);
		// left   
		config.setLeftEditable(false);   
		config.setLeftLabel(title1);   
		// right   
		config.setRightEditable(false);   
		config.setRightLabel(title2);
		return config;
	}
	
	/**
	 * 打开比较editor
	 * @param title
	 * @param config
	 * @param txt1
	 * @param txt2
	 */
	private static void showCompare(String title, CompareConfiguration config, String txt1, String txt2) {
		final StringCompareItem icd = new StringCompareItem(txt1);
		final StringCompareItem scd = new StringCompareItem(txt2); 
		CompareEditorInput editorInput = new CompareEditorInput(config) {   
		    @Override  
		    protected Object prepareInput(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {   
		        return new DiffNode(null, Differencer.PSEUDO_CONFLICT, null, icd, scd);   
		    }   
		};   
		editorInput.setTitle(title);  		
		CompareUI.openCompareEditor(editorInput);  // 打开对比Editor   
	}
}


