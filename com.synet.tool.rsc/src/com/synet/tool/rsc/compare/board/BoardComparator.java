package com.synet.tool.rsc.compare.board;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.compare.ObjectComparator;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.util.IndexedJSONUtil;

public class BoardComparator extends ObjectComparator {
	
	private Map<String, List<Tb1047BoardEntity>> bdsMapSrc;
	private Map<String, List<Tb1047BoardEntity>> bdsMapDest; 

	public BoardComparator(Map<String, List<Tb1047BoardEntity>> bdsMapSrc, Map<String, List<Tb1047BoardEntity>> bdsMapDest, IProgressMonitor monitor) {
		super(monitor);
		this.bdsMapSrc = bdsMapSrc;
		this.bdsMapDest = bdsMapDest;
	}
	
	@Override
	public List<Difference> execute() {
		List<Difference> result = new ArrayList<>();
		Set<String> keySet = bdsMapSrc.keySet();
		monitor.beginTask("正在比较装置板卡...", keySet.size());
		for (String key : keySet) {
			monitor.setTaskName("正在比较装置" + key);
			String[] temp = key.split(":");
			String iedName = temp[0];			
			String iedDesc = temp.length > 1 ? temp[1] : "";
			Difference diffIED = new Difference(null, "装置板卡", iedName, "", OP.UPDATE);
			diffIED.setDesc(iedDesc);
			List<Tb1047BoardEntity> bdsListSrc = bdsMapSrc.get(key);
			List<Tb1047BoardEntity> bdsListDest = bdsMapDest.get(key);
			Iterator<Tb1047BoardEntity> iterator = bdsListSrc.iterator();
			Map<String, Object> destMap = CompareUtil.getChildrenMapByAtt(bdsListDest, "f1047Slot");
			while (iterator.hasNext()) {
				Tb1047BoardEntity bdSrc = iterator.next();
				String slot = bdSrc.getF1047Slot();
				Tb1047BoardEntity bdDest = (Tb1047BoardEntity) destMap.get(slot);
				if (bdDest == null) {
					CompareUtil.addDiffByAttName(diffIED, "Port", bdSrc, "f1048No", "f1048Desc", OP.DELETE);
				} else {
					destMap.remove(slot);
					if (CompareUtil.matchMd5(IndexedJSONUtil.getBoardJson(bdSrc), IndexedJSONUtil.getBoardJson(bdDest))) {
						continue;
					}
					new BoardCompare(diffIED, bdSrc, bdDest, monitor).execute();
				}
			}
			if (destMap.size() > 0) {
				for (Object obj : destMap.values()) {
					Tb1047BoardEntity bdDest = (Tb1047BoardEntity) obj;
					CompareUtil.addDiffByAttName(diffIED, "Port", bdDest, "f1048No", "f1048Desc", OP.ADD);
				}
			}
			if (diffIED.getChildren().size() > 0) {
				result.add(diffIED);
			}
			monitor.worked(1);
		}
		monitor.done();
		return result;
	}

}
