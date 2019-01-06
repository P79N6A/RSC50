package com.synet.tool.rsc.incr.board;

import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.compare.board.BoardCompare;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.service.BoardEntityService;

public class BoardConflictHandler extends BaseConflictHandler {
	
	private BoardEntityService boardServ = new BoardEntityService();
	private Tb1046IedEntity ied;

	public BoardConflictHandler(Difference diff) {
		super(diff);
		this.ied = (Tb1046IedEntity) diff.getParent().getData();
	}

	@Override
	public void setData() {
		String name = (OP.RENAME==diff.getOp()) ? diff.getNewName() : diff.getName();
		Tb1047BoardEntity board = boardServ.getBoardEntity(ied, name);
		diff.setData(board);
	}
	
	@Override
	public void handleAdd() {
		Tb1047BoardEntity board = getBoard();
		boardServ.addBoard(ied, board);
	}

	@Override
	public void handleDelete() {
		boardServ.deleteBoard(ied, diff.getName());
	}

	@Override
	public void handleUpate() {
		Tb1047BoardEntity board = getBoard();
		boardServ.updateBoard(ied, diff.getName(), board);
	}

	private Tb1047BoardEntity getBoard() {
		Tb1047BoardEntity board = JSONObject.parseObject(diff.getMsg(), Tb1047BoardEntity.class);
		return board;
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
		Tb1047BoardEntity boardOld = boardServ.getBoardEntity(ied, oldName);
		Tb1047BoardEntity boardNew = JSONObject.parseObject(diffDest.getMsg(), Tb1047BoardEntity.class);
		Set<Tb1048PortEntity> portNewSet = new HashSet<>();
		boardNew.setTb1048PortsByF1047Code(portNewSet);
		for (Difference temp : diffDest.getChildren()) {
			Tb1048PortEntity portNew = JSONObject.parseObject(temp.getMsg(), Tb1048PortEntity.class);
			portNewSet.add(portNew);
		}
		Difference diffNew = new BoardCompare(null, boardOld , boardNew, monitor).execute();
		diffNew.setName(oldName);
		diffNew.setNewName(newName);
		diffNew.setDesc(boardOld.getF1047Desc());
		diffNew.setNewDesc(boardNew.getF1047Desc());
		diffNew.setOp(OP.RENAME);
		this.diff = diffNew;
	}

}
