/**
 * Copyright (c) 2007-2017 泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.action.imp;

import static com.synet.tool.rsc.ExcelConstants.IM101_IED_LIST;
import static com.synet.tool.rsc.ExcelConstants.IM102_FIBRE_LIST;
import static com.synet.tool.rsc.ExcelConstants.IM103_IED_BOARD;
import static com.synet.tool.rsc.ExcelConstants.IM104_STATUS_IN;
import static com.synet.tool.rsc.ExcelConstants.IM105_BOARD_WARN;
import static com.synet.tool.rsc.ExcelConstants.IM106_PORT_LIGHT;
import static com.synet.tool.rsc.ExcelConstants.IM107_TER_STRAP;
import static com.synet.tool.rsc.ExcelConstants.IM108_BRK_CFM;
import static com.synet.tool.rsc.ExcelConstants.IM109_STA_INFO;
import static com.synet.tool.rsc.ExcelConstants.IM110_LINK_WARN;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_BRD;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_BRK;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_FIB;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_IED;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_PORT;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_ST;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_STA;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_STRAP;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_WRN;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_LINKW;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
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
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.dialog.ChooseTableColDialog;
import com.synet.tool.rsc.dialog.ChooseTableHeadDialog;
import com.synet.tool.rsc.excel.EnumFileType;
import com.synet.tool.rsc.excel.ExcelImporter;
import com.synet.tool.rsc.excel.ImportConfigFactory;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM101IEDListEntity;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.IM103IEDBoardEntity;
import com.synet.tool.rsc.model.IM104StatusInEntity;
import com.synet.tool.rsc.model.IM105BoardWarnEntity;
import com.synet.tool.rsc.model.IM106PortLightEntity;
import com.synet.tool.rsc.model.IM107TerStrapEntity;
import com.synet.tool.rsc.model.IM108BrkCfmEntity;
import com.synet.tool.rsc.model.IM109StaInfoEntity;
import com.synet.tool.rsc.model.IM110LinkWarnEntity;
import com.synet.tool.rsc.service.ImprotInfoService;


 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-7
 */
public class ImportExcelAction extends BaseImportAction {
	
	private ImprotInfoService improtInfoService;
	
	private static Map<String, String> excelMap = new HashMap<>();
	static {
		excelMap.put(IM101_IED_LIST, ET_IMP_IED);
		excelMap.put(IM102_FIBRE_LIST, ET_IMP_FIB);
		excelMap.put(IM103_IED_BOARD, ET_IMP_BRD);
		excelMap.put(IM104_STATUS_IN, ET_IMP_ST);
		excelMap.put(IM105_BOARD_WARN, ET_IMP_WRN);
		excelMap.put(IM106_PORT_LIGHT, ET_IMP_PORT);
		excelMap.put(IM107_TER_STRAP, ET_IMP_STRAP);
		excelMap.put(IM108_BRK_CFM, ET_IMP_BRK);
		excelMap.put(IM109_STA_INFO, ET_IMP_STA);
		excelMap.put(IM110_LINK_WARN, ET_IMP_LINKW);
	}
			
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
		ConfigEditorInput input = new ConfigEditorInput(title, "bay.gif", excelMap.get(title), data);
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

