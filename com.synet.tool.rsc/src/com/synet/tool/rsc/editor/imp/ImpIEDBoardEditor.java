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

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM103IEDBoardEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.service.BoardEntityService;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;
import com.synet.tool.rsc.util.RscObjectUtils;

import de.kupzog.ktable.KTableCellSelectionListener;

/**
 * 导入信息->装置板卡端口描述 树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpIEDBoardEditor extends ExcelImportEditor {
	
	private DevKTable tableComp;

	private IedEntityService iedEntityService;
	private BoardEntityService boardEntityService;
	private List<IM103IEDBoardEntity> boards; // 用户选择装置型号板卡
	
	public ImpIEDBoardEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		iedEntityService = new IedEntityService();
		boardEntityService = new BoardEntityService();
		map = new HashMap<String, IM100FileInfoEntity>();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(SwtUtil.getGridLayout(2));
		
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 150;
		gridData.verticalSpan = 3;
		titleList = SwtUtil.createList(container, gridData);
		
		table = TableFactory.getIEDBoardTable(container);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		GridData btData = new GridData();
		btData.horizontalAlignment = SWT.RIGHT;
		btImport = SwtUtil.createPushButton(container, "导入板卡", btData);
		tableComp = TableFactory.getIEDCompListTable(container);
		tableComp.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected void addListeners() {
		SwtUtil.addMenus(titleList, new DeleteFileAction(titleList, IM103IEDBoardEntity.class));
		titleList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] selects = titleList.getSelection();
				if (selects != null && selects.length > 0) {
					loadFileItems(selects[0]);
				}
			}
		});
		table.getTable().addCellSelectionListener(new KTableCellSelectionListener() {
			@Override
			public void fixedCellSelected(int col, int row, int statemask) {
			}
			@Override
			public void cellSelected(int col, int row, int statemask) {
				if (row > 0) {
					refreshBompIEDTable();
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
	
	protected void doImport() {
		List<Tb1046IedEntity> ieds = (List<Tb1046IedEntity>) tableComp.getInput();
		for (Tb1046IedEntity ied : ieds) {
			if (!ied.isOverwrite()) {
				continue;
			}
			// 清除原有板卡和端口
			List<Tb1047BoardEntity> iedBoards = boardEntityService.getByIed(ied);
			if (iedBoards != null && iedBoards.size() > 0) {
				for (Tb1047BoardEntity iedBoard : iedBoards) {
					boardEntityService.clearBoardPorts(iedBoard);
				}
				beandao.deleteAll(Tb1047BoardEntity.class, "tb1046IedByF1046Code", ied);
			}
			// 创建板卡和端口
			int boardNum = 0;
			for (IM103IEDBoardEntity entity : boards) {
				IM103IEDBoardEntity tempIEDBoard = improtInfoService.existsEntity(entity);
				if (tempIEDBoard == null){
					continue;
				}
				String portNumStr = entity.getPortNum();
//				int portNum = 0;
//				if (portNumStr != null) {
//					try {
//						portNum = Integer.valueOf(portNumStr);
//					} catch(Exception e) {
//					}
//				}
				Tb1047BoardEntity boardEntity = RscObjectUtils.createBoardEntity();
				boardEntity.setTb1046IedByF1046Code(ied);
				boardEntity.setF1047Slot(entity.getBoardIndex());
				boardEntity.setF1047Desc(entity.getBoardModel());
				boardEntity.setF1047Type(entity.getBoardType());
//				if (portNum < 1) { // 只导入端口数>0的板卡
//					continue;
//				}
				boardEntityService.insert(boardEntity);
				boardNum++;
				if (!StringUtil.isEmpty(portNumStr)) {
					List<Tb1048PortEntity> portList = new ArrayList<>();
					String[] ports = portNumStr.split(",");
					for (String port : ports) {
						Tb1048PortEntity portEntity = RscObjectUtils.createPortEntity();
						portEntity.setTb1047BoardByF1047Code(boardEntity);
						portEntity.setF1048No(port);
						portEntity.setF1048Direction(DBConstants.DIRECTION_RT);
						portEntity.setF1048Plug(DBConstants.PLUG_FC);
						portList.add(portEntity);
					}
					beandao.insertBatch(portList);
				}
//				if (portNum > 0) {
//					List<Tb1048PortEntity> ports = new ArrayList<>();
//					char A = 'A';
//					for (int i = 0; i < portNum; i++) {
//						char c = (char) (A + i);
//						Tb1048PortEntity portEntity = RscObjectUtils.createPortEntity();
//						portEntity.setTb1047BoardByF1047Code(boardEntity);
//						portEntity.setF1048No("" + c);
//						portEntity.setF1048Direction(DBConstants.DIRECTION_RT);
//						portEntity.setF1048Plug(DBConstants.PLUG_FC);
//						ports.add(portEntity);
//					}
//					beandao.insertBatch(ports);
//				}
			}
			// 更新板卡数量
			ied.setF1046boardNum(boardNum);
			beandao.update(ied);
		}
	}

	private boolean isMatch(Tb1047BoardEntity iedboard, IM103IEDBoardEntity board) {
		if (!iedboard.getF1047Slot().equals(board.getBoardIndex()))
			return false;
		if (!iedboard.getF1047Desc().equals(board.getBoardModel()))
			return false;
		if (!iedboard.getF1047Type().equals(board.getBoardType()))
			return false;
		int portNum = hqldao.getCount(Tb1048PortEntity.class, "tb1047BoardByF1047Code", iedboard);
		int bpnum = 0;
		try {
			bpnum = Integer.parseInt(board.getPortNum());
		} catch(Exception e) {
		}
		return portNum == bpnum;
	}
	
	private List<IM103IEDBoardEntity> getIedBoards(IM103IEDBoardEntity board) {
		List<IM103IEDBoardEntity> boards = new ArrayList<>();
		List<IM103IEDBoardEntity> inputs = (List<IM103IEDBoardEntity>) table.getInput();
		for (IM103IEDBoardEntity input : inputs) {
			if (input.getDevName().equals(board.getDevName())) {
				boards.add(input);
			}
		}
		return boards;
	}
	
	@Override
	public void initData() {
		table.setInput(new ArrayList<>());
		tableComp.setInput(new ArrayList<>());
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE103);
		if (fileInfoEntities != null && fileInfoEntities.size() > 0) {
			List<String> items = new ArrayList<>();
			for (IM100FileInfoEntity fileInfoEntity : fileInfoEntities) {
				map.put(fileInfoEntity.getFileName(), fileInfoEntity);
				items.add(fileInfoEntity.getFileName());
			}
			if (items.size() > 0) {
				IEditorInput editinput = getInput();
				int sel = 0;
				Object data = editinput.getData();
				if (data != null && data instanceof String) {
					String filename = (String) data;
					sel = items.indexOf(filename);
				}
				
				titleList.setItems(items.toArray(new String[0]));
				titleList.setSelection(sel);
				loadFileItems(items.get(sel));
			}
		}
	}
	
	private void loadFileItems(String filename) {
		IM100FileInfoEntity fileInfoEntity = map.get(filename);
		if (fileInfoEntity == null) {
			DialogHelper.showAsynError("文名错误！");
		} else {
			List<IM103IEDBoardEntity> list = improtInfoService.getIEDBoardEntityList(fileInfoEntity);
			if (list != null) {
				table.setInput(list);
			}
		}
	}

	private void refreshBompIEDTable() {
		IM103IEDBoardEntity iedBoard = (IM103IEDBoardEntity) table.getSelection();
		boards = getIedBoards(iedBoard);
		List<Tb1046IedEntity> ieds = iedEntityService.getIedByIM103IEDBoard(iedBoard);
		for (Tb1046IedEntity ied : ieds) {
			if (ied.getF1046boardNum() < 1) {
				ied.setConflict(DBConstants.NO);
				ied.setOverwrite(false);
			} else {
				if (ied.getF1046boardNum() != boards.size()) {
					ied.setConflict(DBConstants.YES);
					ied.setOverwrite(true);
				} else {
					List<Tb1047BoardEntity> iedboards = boardEntityService.getByIed(ied);
					boolean match = true;
					for (Tb1047BoardEntity iedboard : iedboards) {
						boolean find = false;
						for (IM103IEDBoardEntity board : boards) {
							if (isMatch(iedboard, board)) {
								find = true;
								break;
							}
						}
						if (!find) {
							match = false;
							break;
						}
					}
					ied.setConflict(match ? DBConstants.NO : DBConstants.YES);
					ied.setOverwrite(!match);
				}
			}
		}
		tableComp.setInput(ieds);
	}
}
