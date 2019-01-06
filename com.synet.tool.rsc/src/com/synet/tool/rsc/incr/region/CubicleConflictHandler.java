package com.synet.tool.rsc.incr.region;

import com.alibaba.fastjson.JSONObject;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.compare.region.CubicleCompare;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.service.CubicleEntityService;

public class CubicleConflictHandler extends BaseConflictHandler {
	
	private CubicleEntityService cubicleServ = new CubicleEntityService();
	private Tb1049RegionEntity region;

	public CubicleConflictHandler(Difference diff) {
		super(diff);
		this.region = (Tb1049RegionEntity) diff.getParent().getData();
	}

	@Override
	public void handleAdd() {
		Tb1050CubicleEntity cubicle = getCubicle();
		cubicleServ.addCubicle(region, cubicle);
	}

	@Override
	public void handleDelete() {
		cubicleServ.deleteCubicle(region, diff.getName());
	}

	@Override
	public void handleUpate() {
		Tb1050CubicleEntity cubicle = getCubicle();
		cubicleServ.updateCubicle(region, diff.getName(), cubicle);
	}

	private Tb1050CubicleEntity getCubicle() {
		return JSONObject.parseObject(diff.getMsg(), Tb1050CubicleEntity.class);
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
		Tb1050CubicleEntity cubicleOld = cubicleServ.getCubicleEntity(region, oldName);
		Tb1050CubicleEntity cubicleNew = JSONObject.parseObject(diffDest.getMsg(), Tb1050CubicleEntity.class);
		Difference diffNew = new CubicleCompare(null, cubicleOld , cubicleNew, monitor).execute();
		diffNew.setName(oldName);
		diffNew.setNewName(newName);
		diffNew.setDesc(cubicleOld.getF1050Desc());
		diffNew.setNewDesc(cubicleNew.getF1050Desc());
		diffNew.setOp(OP.RENAME);
		this.diff = diffNew;
	}

}
