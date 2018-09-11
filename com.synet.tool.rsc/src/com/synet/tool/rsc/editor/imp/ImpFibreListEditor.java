/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.editor.BaseConfigEditor;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 导入信息->光缆清册树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpFibreListEditor extends BaseConfigEditor {
	
	private ImprotInfoService improtInfoService;
	private Map<String, IM100FileInfoEntity> map;
	private org.eclipse.swt.widgets.List titleList;
	
	public ImpFibreListEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		map = new HashMap<String, IM100FileInfoEntity>();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(SwtUtil.getGridLayout(2));
		
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 150;
		titleList = SwtUtil.createList(container, gridData);
		table =TableFactory.getFibreListTable(container);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected void addListeners() {
		titleList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] selects = titleList.getSelection();
				if (selects != null && selects.length > 0) {
					IM100FileInfoEntity fileInfoEntity = map.get(selects[0]);
					if (fileInfoEntity == null) {
						DialogHelper.showAsynError("文名错误！");
					} else {
						List<IM102FibreListEntity> list = improtInfoService.getFibreListEntityList(fileInfoEntity);
						if (list != null && list.size()> 0) {
							table.setInput(list);
						}
					}
					System.out.println(selects[0]);
				}
				super.widgetSelected(e);
			}
		});
	}

	@Override
	public void initData() {
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE102);
		if (fileInfoEntities != null && fileInfoEntities.size() > 0) {
			List<String> items = new ArrayList<>();
			for (IM100FileInfoEntity fileInfoEntity : fileInfoEntities) {
				map.put(fileInfoEntity.getFileName(), fileInfoEntity);
				items.add(fileInfoEntity.getFileName());
			}
			if (items.size() > 0) {
				titleList.setItems(items.toArray(new String[0]));
				titleList.setSelection(0);
				
				List<IM102FibreListEntity> list = improtInfoService.getFibreListEntityList(map.get(items.get(0)));
				if (list != null && list.size()> 0) {
					table.setInput(list);
				}
			}
		}
	}
}
