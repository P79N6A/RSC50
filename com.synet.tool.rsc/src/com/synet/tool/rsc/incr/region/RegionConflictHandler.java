package com.synet.tool.rsc.incr.region;

import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.compare.region.RegionCompare;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.service.RegionEntityService;

public class RegionConflictHandler extends BaseConflictHandler {
	
	private RegionEntityService regionServ = new RegionEntityService();

	public RegionConflictHandler(Difference diff) {
		super(diff);
	}

	@Override
	public void setData() {
		String name = (OP.RENAME==diff.getOp()) ? diff.getNewName() : diff.getName();
		Tb1049RegionEntity region = regionServ.getRegionEntity(name);
		diff.setData(region);
	}
	
	@Override
	public void handleAdd() {
		Tb1049RegionEntity region = getRegion();
		regionServ.addRegion(region);
	}

	@Override
	public void handleDelete() {
		regionServ.deleteRegion(diff.getName());
	}

	@Override
	public void handleUpate() {
		Tb1049RegionEntity region = getRegion();
		regionServ.updateRegion(diff.getName(), region);
	}

	private Tb1049RegionEntity getRegion() {
		Tb1049RegionEntity region = JSONObject.parseObject(diff.getMsg(), Tb1049RegionEntity.class);
		return region;
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
		Tb1049RegionEntity regionOld = regionServ.getRegionEntity(oldName);
		Tb1049RegionEntity regionNew = JSONObject.parseObject(diffDest.getMsg(), Tb1049RegionEntity.class);
		Set<Tb1050CubicleEntity> portNewSet = new HashSet<>();
		regionNew.setTb1050CubiclesByF1049Code(portNewSet);
		for (Difference temp : diffDest.getChildren()) {
			Tb1050CubicleEntity portNew = JSONObject.parseObject(temp.getMsg(), Tb1050CubicleEntity.class);
			portNewSet.add(portNew);
		}
		Difference diffNew = new RegionCompare(null, regionOld , regionNew, monitor).execute();
		diffNew.setName(oldName);
		diffNew.setNewName(newName);
		diffNew.setDesc(regionOld.getF1049Desc());
		diffNew.setNewDesc(regionNew.getF1049Desc());
		diffNew.setOp(OP.RENAME);
		this.diff = diffNew;
	}

}
