/**
 * Copyright (c) 2007-2017 泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action.imp;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;

import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.ui.editor.ConfigEditorInput;
import com.shrcn.found.ui.editor.EditorConfigData;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ProgressManager;
import com.synet.tool.rsc.dialog.ChooseTableColDialog;
import com.synet.tool.rsc.dialog.ChooseTableHeadDialog;
import com.synet.tool.rsc.excel.EnumFileType;
import com.synet.tool.rsc.excel.ExcelImporter;
import com.synet.tool.rsc.excel.ImportConfigFactory;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.service.ImprotInfoService;


 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-7
 */
public class ImportExcelAction extends BaseImportAction {
	
	private ImprotInfoService improtInfoService;
	
	public ImportExcelAction(String title) {
		super(title);
	}

	@Override
	public void run() {
		improtInfoService = new ImprotInfoService();
		final String filePath = DialogHelper.getSaveFilePath(getTitle() + "文件", "", new String[]{"*.xlsx"});
		if (filePath == null || "".equals(filePath)){
			return;
		}
		//检查文件
		String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
		final IM100FileInfoEntity fileInfoEntity = improtInfoService.getFileInfoEntityByFileName(fileName);
		if (fileInfoEntity != null) {
			boolean b =  DialogHelper.showConfirm("文件已存在，是否覆盖？");
			if (!b) {//不覆盖，退出
				return;
			}
		}
		ChooseTableHeadDialog headDialog = new ChooseTableHeadDialog(getShell());
		headDialog.setExcelFilPath(filePath);
		if (headDialog.open() == 0) {
			ChooseTableColDialog colDialog = new ChooseTableColDialog(getShell());
			//表头信息
			Map<Integer, String> excelColName = headDialog.getExcelColName();
			final int excelHeadRow = headDialog.getTableHeadRow();
			if (excelColName == null) {
				DialogHelper.showAsynError("文件异常");
				return;
			}
			colDialog.setExcelColMap(excelColName);
			String[] fields = ImportConfigFactory.getExcelFields(getTitle());
			if (fields == null) {
				DialogHelper.showAsynError("系统异常");
				return;
			}
			colDialog.setFields(fields);
			if (colDialog.open() == 0) {
				final Map<Integer, String> excelColInfo = colDialog.getMap();
				ProgressManager.execute(new IRunnableWithProgress() {
					
					@Override
					public void run(final IProgressMonitor monitor) throws InvocationTargetException,
							InterruptedException {
						monitor.beginTask("正在导入...", 3);
						if (fileInfoEntity != null) {
							monitor.setTaskName("正在删除原始数据");
							deleteFile(fileInfoEntity);
						}
						monitor.worked(1);
						monitor.setTaskName("正在导入数据");
						final boolean b = ExcelImporter.importExcelData(monitor, getTitle(), filePath, excelHeadRow, excelColInfo);
						
						//跳转界面
						Display.getDefault().asyncExec(new Runnable() {
							
							@Override
							public void run() {
								if (b) {
									openImportEditor(filePath);
								} else {
									DialogHelper.showAsynError("导入失败，请检查文件格式");
								}
							}
						});
					}
				});
			}
		}
	}
	
	private void openImportEditor(String filePath) {
		String filename = filePath.substring(filePath.lastIndexOf("\\") + 1);
		String title = getTitle();
		EditorConfigData data = new EditorConfigData(title, null, 0, title);
		data.setData(filename);
		ConfigEditorInput input = new ConfigEditorInput(title, "bay.gif", 
				EnumFileType.getByTitle(title).getEditorId(), data);
		EventManager.getDefault().notify(EventConstants.OPEN_CONFIG, input);
		EventManager.getDefault().notify(EventConstants.REFRESH_EIDTOR, input);
	}
	
	/***
	 * 删除文件，并删除文件下的数据
	 */
	private void deleteFile(IM100FileInfoEntity fileInfoEntity) {
		if (fileInfoEntity == null)
			return;
		int fileType = fileInfoEntity.getFileType();
		Class<?> entityClass = EnumFileType.getById(fileType).getEntityClass();
		improtInfoService.deleteDataByFile(entityClass, fileInfoEntity);
		improtInfoService.delete(fileInfoEntity);
	}

}

