/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor.imp;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.dialog.ExportIedDialog;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM107TerStrapEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.PinEntityService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.service.StatedataService;
import com.synet.tool.rsc.service.StrapEntityService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.util.DateUtils;

/**
 * 导入信息->压板与虚端子关联表 树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpTerStrapEditor extends ExcelImportEditor {
	
	private PinEntityService pinEntityService;
	private PoutEntityService poutEntityService;
	private MmsfcdaService mmsfcdaService;
	private StatedataService statedataService;
	private StrapEntityService strapEntityService;
	
	public ImpTerStrapEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		map = new HashMap<String, IM100FileInfoEntity>();
		pinEntityService = new PinEntityService();
		poutEntityService = new PoutEntityService();
		mmsfcdaService = new MmsfcdaService();
		statedataService = new StatedataService();
		strapEntityService = new StrapEntityService();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(SwtUtil.getGridLayout(2));
	
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 150;
		gridData.verticalSpan = 2;
		titleList = SwtUtil.createList(container, gridData);
		GridData btData = new GridData();
		btData.horizontalAlignment = SWT.RIGHT;
		Composite btComp = SwtUtil.createComposite(container, btData, 3);
		btExport = SwtUtil.createPushButton(btComp, "导出Excel", new GridData());
		btCheck = SwtUtil.createPushButton(btComp, "冲突检查", new GridData());
		btImport = SwtUtil.createPushButton(btComp, "导入压板与虚端子", new GridData());
		table =TableFactory.getTerStrapTable(container);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected void addListeners() {
		SwtUtil.addMenus(titleList, new DeleteFileAction(titleList, IM107TerStrapEntity.class));
		titleList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] selects = titleList.getSelection();
				if (selects != null && selects.length > 0) {
					loadFileItems(selects[0]);
				}
			}
		});
		
		btExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exportExcel();
			}
		});
		
		btCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//冲突检查
				checkConflict();
			}
		});
		
		btImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				importData();
			}
		});
	}

	protected void exportExcel() {
		ExportIedDialog dialog = new ExportIedDialog(getShell());
		if (dialog.open() == IDialogConstants.OK_ID) {
			final String filePath = dialog.getFilePath();
			final List<Tb1046IedEntity> ieds = dialog.getIeds();
			if (filePath == null) return;
			ProgressManager.execute(new IRunnableWithProgress() {
				
				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException,
				InterruptedException {
					IField[] vfields = getExportFields();
					if (ieds != null && ieds.size() > 0) {
						monitor.beginTask("开始导出", ieds.size());
						long start = System.currentTimeMillis();
						for (Tb1046IedEntity ied : ieds) {
							if (monitor.isCanceled()) {
								break;
							}
							monitor.setTaskName("正在导出装置[" + ied.getF1046Name() + "]数据");
							//导出压板部分
							List<Object> strapList = getStrapData(ied);
							if (strapList.size() > 0) {
								String dateStr = DateUtils.getDateStr(new Date(), DateUtils.DATE_DAY_PATTERN_);
								String fileName = filePath + "/" + ied.getF1046Name() + "压板与虚端子关联表(压板部分)" + dateStr + ".xlsx";
								exportTemplateExcel(fileName, "压板与虚端子关联表", vfields, strapList);
							}
							//导出虚端子部分
							List<Object> vpList = getVpData(ied);
							if (vpList.size() > 0) {
								String dateStr = DateUtils.getDateStr(new Date(), DateUtils.DATE_DAY_PATTERN_);
								String fileName = filePath + "/" + ied.getF1046Name() + "压板与虚端子关联表(虚端子部分)" + dateStr + ".xlsx";
								exportTemplateExcel(fileName, "压板与虚端子关联表", vfields, vpList);
							}
							
							monitor.worked(1);
						}
						long time = (System.currentTimeMillis() - start) / 1000;
						monitor.setTaskName("导出耗时：" + time + "秒");
						monitor.done();
						
					}
				}
			}, true);
		}
	}
	
	private List<Object> getStrapData(Tb1046IedEntity ied) {
		DictManager dictManager = DictManager.getInstance();
		List<Object> list = new ArrayList<>();
		List<Tb1058MmsfcdaEntity> mmsList = mmsfcdaService.getMmsfcdaByIed(ied);
		if (mmsList != null && mmsList.size() > 0) {
			for (Tb1058MmsfcdaEntity mms : mmsList) {
				if (mms.getParentCode() != null) {
					Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) statedataService.getById(Tb1016StatedataEntity.class,
							mms.getDataCode());
					if (statedataEntity != null) {
						if (statedataEntity.getParentCode() != null) {
							Tb1064StrapEntity strapEntity = (Tb1064StrapEntity) strapEntityService.getById(Tb1064StrapEntity.class, 
									statedataEntity.getParentCode());
							if (strapEntity != null) {
								IM107TerStrapEntity entity = new IM107TerStrapEntity();
								entity.setDevName(ied.getF1046Name());
								entity.setDevDesc(ied.getF1046Desc());
								entity.setStrapRefAddr(mms.getF1058RefAddr());
								entity.setStrapDesc(mms.getF1058Desc());
								entity.setStrapType(dictManager.getNameById("F1011_NO", strapEntity.getF1064Type()));
								list.add(entity);
							}
						}
					}
				}
			}
		}
		return list;
	}
	
	private List<Object> getVpData(Tb1046IedEntity ied) {
		List<Object> list = new ArrayList<>();
		//开入
		List<Tb1062PinEntity> pinList = pinEntityService.getByIed(ied);
		if (pinList != null && pinList.size() > 0) {
			for (Tb1062PinEntity pinEntity : pinList) {
				IM107TerStrapEntity entity = new IM107TerStrapEntity();
				entity.setDevName(ied.getF1046Name());
				entity.setDevDesc(ied.getF1046Desc());
				entity.setVpRefAddr(pinEntity.getF1062RefAddr());
				entity.setVpDesc(pinEntity.getF1062Desc());
				entity.setVpType("开入");
				list.add(entity);
			}
		}
		//开出
		List<Tb1061PoutEntity> poutList = poutEntityService.getByIed(ied);
		if (poutList != null && poutList.size() > 0) {
			for (Tb1061PoutEntity poutEntity : poutList) {
				IM107TerStrapEntity entity = new IM107TerStrapEntity();
				entity.setDevName(ied.getF1046Name());
				entity.setDevDesc(ied.getF1046Desc());
				entity.setVpRefAddr(poutEntity.getF1061RefAddr());
				entity.setVpDesc(poutEntity.getF1061Desc());
				entity.setVpType("开出");
				list.add(entity);
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doImport() {
		List<IM107TerStrapEntity> list = (List<IM107TerStrapEntity>) table.getInput();
		if (list == null || list.size() <= 0)
			return;
		for (IM107TerStrapEntity entity : list) {
			if (!entity.isOverwrite()) {
				continue;
			}
			String devName = entity.getDevName();
			String vpRefAddr = entity.getVpRefAddr();
			String vpType = entity.getVpType();
			String strapRefAddr = entity.getStrapRefAddr();
			String endMsg = devName + "->" + vpRefAddr;
			try {
				if ("开入".equals(vpType)) {
					Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(devName, vpRefAddr);
					if (pinEntity != null) {
						Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(devName, strapRefAddr);
						if (mmsfcdaEntity != null) {
							Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) statedataService.getById(Tb1016StatedataEntity.class,
									mmsfcdaEntity.getDataCode());
							if (statedataEntity != null) {
								if (statedataEntity.getParentCode() != null) {
									Tb1064StrapEntity strapEntity = (Tb1064StrapEntity) strapEntityService.getById(Tb1064StrapEntity.class, 
											statedataEntity.getParentCode());
									if (strapEntity != null) {
										pinEntity.setTb1064StrapByF1064Code(strapEntity);
										pinEntityService.save(pinEntity);
										entity.setMatched(DBConstants.MATCHED_OK);
									}
								}
							} else {
								String msg = "FCDA对应的数据点不存在：" + endMsg;
								appendError("导入压板与虚端子", "FCDA数据点检查", msg);
							}
						}
					} else {
						String msg = "开入虚端子不存在:" + endMsg;
						appendError("导入压板与虚端子", "虚端子检查", msg);
					}
				} else if ("开出".equals(vpType)){
					Tb1061PoutEntity poutEntity = poutEntityService.getPoutEntity(devName, vpRefAddr);
					if (poutEntity != null) {
						Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(devName, strapRefAddr);
						if (mmsfcdaEntity != null) {
							Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) statedataService.getById(Tb1016StatedataEntity.class,
									mmsfcdaEntity.getDataCode());
							if (statedataEntity != null) {
								if (statedataEntity.getParentCode() != null) {
									Tb1064StrapEntity strapEntity = (Tb1064StrapEntity) strapEntityService.getById(Tb1064StrapEntity.class, 
											statedataEntity.getParentCode());
									if (strapEntity != null) {
										poutEntity.setTb1064StrapByF1064Code(strapEntity);
										poutEntityService.save(poutEntity);
										entity.setMatched(DBConstants.MATCHED_OK);
									}
								}
							} else {
								String msg = "FCDA对应的数据点不存在：" + endMsg;
								appendError("导入压板与虚端子", "FCDA数据点检查", msg);
							}
						}
					} else {
						String msg = "开出虚端子不存在:" + endMsg;
						appendError("导入压板与虚端子", "虚端子检查", msg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initData() {
		table.setInput(new ArrayList<>());
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE107);
		if (fileInfoEntities != null && fileInfoEntities.size() > 0) {
			List<String> items = new ArrayList<>();
			for (IM100FileInfoEntity fileInfoEntity : fileInfoEntities) {
				map.put(fileInfoEntity.getFileName(), fileInfoEntity);
				items.add(fileInfoEntity.getFileName());
			}
			if (items.size() > 0) {
				titleList.setItems(items.toArray(new String[0]));
				titleList.setSelection(0);
				loadFileItems(items.get(0));
			}
		}
	}
	
	private void loadFileItems(String filename) {
		List<IM107TerStrapEntity> list = improtInfoService.getTerStrapEntityList(map.get(filename));
		if (list != null) {
			table.setInput(list);
		}
	}

	@SuppressWarnings("unchecked")
	protected void checkData() {
		List<IM107TerStrapEntity> list = (List<IM107TerStrapEntity>) table.getInput();
		if (list == null || list.size() <= 0) return;
		for (IM107TerStrapEntity entity : list) {
			if (entity.getMatched() == DBConstants.MATCHED_OK) {
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			}
			try {
				String vpType = entity.getVpType();
				if ("开入".equals(vpType)) {
					Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(entity.getDevName(), entity.getVpRefAddr());
					if (pinEntity != null) {
						Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getStrapRefAddr());
						if (mmsfcdaEntity != null) {
							Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) statedataService.getById(Tb1016StatedataEntity.class,
									mmsfcdaEntity.getDataCode());
							if (statedataEntity != null) {
								if (statedataEntity.getParentCode() != null) {
									Tb1064StrapEntity strapEntity = (Tb1064StrapEntity) strapEntityService.getById(Tb1064StrapEntity.class, 
											statedataEntity.getParentCode());
									if (strapEntity != null && pinEntity.getTb1064StrapByF1064Code() == null) {
										entity.setConflict(DBConstants.NO);
										entity.setOverwrite(true);
										continue;
									}
								}
							}
						}
					}
					entity.setConflict(DBConstants.YES);
					entity.setOverwrite(false);
				} else if ("开出".equals(vpType)){
					Tb1061PoutEntity poutEntity = poutEntityService.getPoutEntity(entity.getDevName(), entity.getVpRefAddr());
					if (poutEntity != null) {
						Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getStrapRefAddr());
						if (mmsfcdaEntity != null) {
							Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) statedataService.getById(Tb1016StatedataEntity.class,
									mmsfcdaEntity.getDataCode());
							if (statedataEntity != null) {
								if (statedataEntity.getParentCode() != null) {
									Tb1064StrapEntity strapEntity = (Tb1064StrapEntity) strapEntityService.getById(Tb1064StrapEntity.class, 
											statedataEntity.getParentCode());
									if (strapEntity != null && poutEntity.getTb1064StrapByF1064Code() == null) {
										entity.setConflict(DBConstants.NO);
										entity.setOverwrite(true);
										continue;
									}
								}
							}
						}
					}
					entity.setConflict(DBConstants.YES);
					entity.setOverwrite(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
