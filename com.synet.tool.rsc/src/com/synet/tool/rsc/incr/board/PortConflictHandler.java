package com.synet.tool.rsc.incr.board;

import com.alibaba.fastjson.JSONObject;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.compare.board.PortCompare;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.service.PortEntityService;

public class PortConflictHandler extends BaseConflictHandler {
	
	private PortEntityService portServ = new PortEntityService();
	private Tb1047BoardEntity board;

	public PortConflictHandler(Difference diff) {
		super(diff);
		this.board = (Tb1047BoardEntity) diff.getParent().getData();
	}

	@Override
	public void handleAdd() {
		Tb1048PortEntity port = getPort();
		portServ.addPort(board, port);
	}

	@Override
	public void handleDelete() {
		portServ.deletePort(board, diff.getName());
	}

	@Override
	public void handleUpate() {
		Tb1048PortEntity port = getPort();
		portServ.updatePort(board, diff.getName(), port);
	}

	private Tb1048PortEntity getPort() {
		return JSONObject.parseObject(diff.getMsg(), Tb1048PortEntity.class);
	}
	
	@Override
	public void mergeDifference() {
		String oldName = diff.getName();
		String newName = diff.getNewName();
		Difference diffDest = null;
		for (Difference temp : diff.getParent().getChildren()) {
			if (temp.getName().equals(newName)) {
				diffDest = temp;
				break;
			}
		}
		if (diffDest == null) {
			return;
		}
		Tb1048PortEntity portOld = portServ.getPortEntity(board, oldName);
		Tb1048PortEntity portNew = JSONObject.parseObject(diffDest.getMsg(), Tb1048PortEntity.class);
		Difference diffNew = new PortCompare(null, portOld , portNew, monitor).execute();
		diffNew.setName(oldName);
		diffNew.setNewName(newName);
		diffNew.setDesc(portOld.getF1048Desc());
		diffNew.setNewDesc(portNew.getF1048Desc());
		diffNew.setOp(OP.RENAME);
		this.diff = diffNew;
	}

}
