/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import java.util.List;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.EditorConfigData;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 物理信息模型树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class PhysicalModelEditor extends BaseConfigEditor {
	
	
	public PhysicalModelEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		editArea.setLayout(SwtUtil.getGridLayout(1));
		table =TableFactory.getAreaListTable(editArea);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		
	}
	
	protected void addListeners() {
	}

	@Override
	public void initData() {
		List<Tb1049RegionEntity> list= (List<Tb1049RegionEntity>) beandao.getAll(Tb1049RegionEntity.class);
		if (list != null) {
			table.setInput(list);
		}
	}
}
