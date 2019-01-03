package com.synet.tool.rsc.incr.ssd;

import java.util.List;
import java.util.Map;

import com.shrcn.found.common.util.StringUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.incr.EnumConflict;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;

public class StaConflictHandler extends BaseConflictHandler {

	public StaConflictHandler(Difference diff) {
		super(diff);
		
	}

	@Override
	public void handleAdd() {
	}

	@Override
	public void handleDelete() {
	}

	@Override
	public void handleUpate() {
		int bayNum = getBayNum();
		monitor.beginTask("开始增量导入SSD配置", bayNum + 1);

		Tb1041SubstationEntity substation = (Tb1041SubstationEntity) 
				getByName(Tb1041SubstationEntity.class, "f1041Name");
		Map<String, String> updateInfo = getUpdateInfo();
		String newName = updateInfo.get("name");
		String newDesc = updateInfo.get("desc");
		if (!StringUtil.isEmpty(newName)) {
			substation.setF1041Name(newName);
		}
		if (!StringUtil.isEmpty(newDesc)) {
			substation.setF1041Desc(newDesc);
		}
		if (updateInfo.size() > 0) {
			beanDao.update(substation);
		}
		
		monitor.done();
	}
	
	protected EnumConflict getChildHandlerType() {
		return null;
	}
	
	private int getBayNum() {
		int num = 0;
		List<Difference> diffs = diff.getChildren();
		for (Difference volDiff : diffs) {
			num += volDiff.getChildren().size();
		}
		return num + diffs.size();
	}


}
