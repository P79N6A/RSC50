package com.shrcn.business.scl.util;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.shrcn.business.scl.ui.SCTEditorInput;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.editor.BasicEditPart;
import com.shrcn.found.ui.view.ViewManager;

public class SclViewManager extends ViewManager {

	/**
	 * 打开编辑器
	 * @param editorInput
	 */
	public static void openEditor(SCTEditorInput editorInput) {
		IWorkbenchPage workbenchPage = getWorkBenchPage();
		if (editorInput == null || workbenchPage == null
				|| existsEditor(editorInput))
			return;
		String editorID = editorInput.getEditorId();
		IEditorPart editor = findEditor(editorID, editorInput);
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
	}
	
	/**
	 * 判断是否存在相同标题的editor
	 * @param title
	 * @return
	 */
	public static boolean existsEditor(SCTEditorInput openInput) {
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
				if(editorPart.getEditorInput() == null || !(editorPart.getEditorInput() instanceof SCTEditorInput))
					continue;
				SCTEditorInput openedInput = (SCTEditorInput)editorPart.getEditorInput();
				if (editorPart.getTitle().equals(openInput.getName()) &&
						openInput.getEditorId().equals(openedInput.getEditorId())) {
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
	 * 关闭单线图编辑器
	 */
	public static void closeSingleEditor() {
		IWorkbenchPage page = ViewManager.getWorkBenchPage();
		if (page == null)
			return;
		try {
			IEditorPart editorPart = null;
			for (IEditorReference editorReference : page.getEditorReferences()) {
				IEditorInput editorInput = editorReference.getEditorInput();
				if (editorInput != null && editorInput instanceof SCTEditorInput 
						&& ((SCTEditorInput)editorInput).getEditorId().equals(UIConstants.SINGLE_LINE_EDITOR_ID)) {
					editorPart = page.findEditor(editorReference.getEditorInput());
					page.bringToTop(editorPart);
					page.closeEditor(editorPart, false);
					editorPart = null;
					break;
				}
			}
		} catch (PartInitException e) {
			SCTLogger.error("关闭单线图编辑器出错：" + e.getMessage());
		}
	}
	
	/**
	 * 修改editor标题
	 * @param newName
	 */
	public static void renameEditor(String oldName, String newName) {
		IWorkbenchPage activePage = getWorkBenchPage();
		if (activePage == null)
			return;
		try {
			BasicEditPart editorPart = null;
			for(IEditorReference ref : activePage.getEditorReferences()) {
				IEditorInput input = ref.getEditorInput();
				editorPart = (BasicEditPart) activePage.findEditor(input);
				if(editorPart.getPartName().equals(oldName)) {
					editorPart.setPartName(newName);
				}
			}
		} catch (PartInitException e) {
			SCTLogger.error(e.getMessage());
		}
	}
	
	/**
	 * 保存主接线图
	 */
	public static void saveSingleEditor() {
		IWorkbenchPage page = getWorkBenchPage();
		if (page == null)
			return;
		try {
			IEditorPart editorPart = null;
			for (IEditorReference editorReference : page.getEditorReferences()) {
				IEditorInput editorInput = editorReference.getEditorInput();
				if (editorInput != null && editorInput instanceof SCTEditorInput 
						&& ((SCTEditorInput)editorInput).getEditorId().equals(UIConstants.SINGLE_LINE_EDITOR_ID)) {
					editorPart = page.findEditor(editorInput);
					page.saveEditor(editorPart, false);
					break;
				}
			}
		} catch (PartInitException e) {
			SCTLogger.error("保存主接线图出错。", e);
		}
	}
}
