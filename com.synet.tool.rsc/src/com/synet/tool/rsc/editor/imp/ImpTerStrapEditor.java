/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM107TerStrapEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
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
		btImport = SwtUtil.createPushButton(container, "导入压板与虚端子", btData);
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
		
		btImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doImport();
				DialogHelper.showAsynInformation("导入成功！");
			}
		});
	}

	@SuppressWarnings("unchecked")
	protected void doImport() {
		List<IM107TerStrapEntity> list = (List<IM107TerStrapEntity>) table.getInput();
		if (list == null || list.size() <= 0)
			return;
		for (IM107TerStrapEntity entity : list) {
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
									Tb1064StrapEntity strapEntity = (Tb1064StrapEntity) strapEntityService.getById(Tb1062PinEntity.class, 
											statedataEntity.getParentCode());
									if (strapEntity != null) {
										pinEntity.setTb1064StrapByF1064Code(strapEntity);
										pinEntityService.save(pinEntity);
										entity.setMatched(DBConstants.MATCHED_OK);
									}
								}
							}
						}
					}
				} else if ("开出".equals(vpType)){
					Tb1061PoutEntity poutEntity = poutEntityService.getPoutEntity(entity.getDevName(), entity.getVpRefAddr());
					if (poutEntity != null) {
						Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getStrapRefAddr());
						if (mmsfcdaEntity != null) {
							Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) statedataService.getById(Tb1016StatedataEntity.class,
									mmsfcdaEntity.getDataCode());
							if (statedataEntity != null) {
								if (statedataEntity.getParentCode() != null) {
									Tb1064StrapEntity strapEntity = (Tb1064StrapEntity) strapEntityService.getById(Tb1062PinEntity.class, 
											statedataEntity.getParentCode());
									if (strapEntity != null) {
										poutEntity.setTb1064StrapByF1064Code(strapEntity);
										poutEntityService.save(poutEntity);
										entity.setMatched(DBConstants.MATCHED_OK);
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			improtInfoService.update(entity);
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
			checkData(list);
			table.setInput(list);
		}
	}

	private void checkData(List<IM107TerStrapEntity> list) {
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
									Tb1064StrapEntity strapEntity = (Tb1064StrapEntity) strapEntityService.getById(Tb1062PinEntity.class, 
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
									Tb1064StrapEntity strapEntity = (Tb1064StrapEntity) strapEntityService.getById(Tb1062PinEntity.class, 
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
