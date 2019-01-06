package com.synet.tool.rsc.compare.board;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.compare.ObjectCompare;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.util.IndexedJSONUtil;

public class BoardCompare extends ObjectCompare {
	
	private Tb1047BoardEntity srcBoard; 
	private Tb1047BoardEntity destBoard;
	
	public BoardCompare(Difference parentDiff, Tb1047BoardEntity srcBoard, Tb1047BoardEntity destBoard, IProgressMonitor monitor) {
		super(parentDiff, monitor);
		this.srcBoard = srcBoard;
		this.destBoard = destBoard;
	}

	@Override
	public Difference execute() {
		String jsSrc = IndexedJSONUtil.getBoardJson(srcBoard);
		String jsDest = IndexedJSONUtil.getBoardJson(destBoard);
		Difference diffBoard = CompareUtil.addUpdateDiff(parentDiff, "Board", srcBoard.getF1047Slot(), jsSrc, jsDest);
		diffBoard.setDesc(srcBoard.getF1047Desc());
		diffBoard.setNewName(destBoard.getF1047Slot());
		diffBoard.setNewDesc(destBoard.getF1047Desc());
		Iterator<Tb1048PortEntity> iterator = srcBoard.getTb1048PortsByF1047Code().iterator();
		Map<String, Object> destPortMap = CompareUtil.getChildrenMapByAtt(destBoard.getTb1048PortsByF1047Code(), "f1048No");
		while (iterator.hasNext()) {
			Tb1048PortEntity portSrc = iterator.next();
			String portNo = portSrc.getF1048No();
			Tb1048PortEntity portDest = (Tb1048PortEntity) destPortMap.get(portNo);
			if (portDest == null) {
				CompareUtil.addDiffByAttName(diffBoard, "Port", portSrc, "f1048No", "f1048Desc", OP.DELETE);
			} else {
				destPortMap.remove(portNo);
				if (CompareUtil.matchMd5(portSrc, portDest)) {
					continue;
				}
				new PortCompare(diffBoard, portSrc, portDest, monitor).execute();
			}
		}
		if (destPortMap.size() > 0) {
			for (Object obj : destPortMap.values()) {
				Tb1048PortEntity portDest = (Tb1048PortEntity) obj;
				CompareUtil.addDiffByAttName(diffBoard, "Port", portDest, "f1048No", "f1048Desc", OP.ADD);
			}
		}
		return diffBoard;
	}

}
