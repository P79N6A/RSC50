package com.synet.tool.rsc.editor.imp;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.view.LEVEL;
import com.shrcn.found.ui.view.Problem;
import com.synet.tool.rsc.editor.BaseConfigEditor;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.util.ProblemManager;

public class ExcelImportEditor extends BaseConfigEditor {

	protected ImprotInfoService improtInfoService;
	protected Map<String, IM100FileInfoEntity> map;
	protected org.eclipse.swt.widgets.List titleList;
	protected Button btImport;
	protected Button btCheck;
	//导入信息
	protected ProblemManager pmgr = ProblemManager.getInstance();
	protected int errorCount;
	protected int warningCount; 

	public ExcelImportEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	@Override
	public void refresh() {
		initData();
	}
	
	/**
	 * 冲突检查
	 */
	protected void checkConflict() {
		ProgressManager.execute(new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						checkData();
						table.refresh();
					}
				});
			}
		});
	}
	
	protected void checkData() {
		
	}
	
	/**
	 * 导入数据
	 */
	protected void importData() {
		ProgressManager.execute(new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						clearMsgCount();
						doImport();
//						console.append(titleList.getSelection()[0] + "导入完毕！");
						printMsg(titleList.getSelection()[0] + "导入完毕");
					}
				});
			}
		});
	}
	
	protected void doImport(){
		
	}
	
	/**
	 * 清除导入异常记录
	 */
	protected void clearMsgCount() {
		errorCount = 0;
		warningCount = 0;
	}
	
	protected void appendError(String title, String subType, String msg) {
		SCTLogger.error(msg);
		pmgr.append(new Problem(0, LEVEL.ERROR, title, subType, "", msg));
		errorCount++;
	}
	
	protected void appendWarning(String title, String subType, String msg) {
		SCTLogger.error(msg);
		pmgr.append(new Problem(0, LEVEL.WARNING, title, subType, "", msg));
		warningCount++;
	}
	
	/**
	 * 打印导入异常信息
	 */
	protected void printMsg(String msg) {
		if (msg == null) 
			msg ="";
		if (errorCount != 0) {
			msg += "，错误：" + errorCount;
		}
		if (warningCount != 0) {
			msg += "，警告：" + warningCount;
		}
		msg += "。";
		console.append(msg);
	}
}