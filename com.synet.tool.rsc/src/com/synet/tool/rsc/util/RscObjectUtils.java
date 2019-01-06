package com.synet.tool.rsc.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.TB1086DefectFuncREntity;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;
import com.synet.tool.rsc.model.Tb1092PowerkkEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;
import com.synet.tool.rsc.service.DefaultService;

public class RscObjectUtils {
	
	private static RSCProperties rscp = RSCProperties.getInstance();
	private static DefaultService defaultService = new DefaultService();
	
	public static Tb1090LineprotfiberEntity createTb1090() {
		Tb1090LineprotfiberEntity entity = new Tb1090LineprotfiberEntity();
		entity.setF1090Code(rscp.nextTbCode(DBConstants.PR_LINEPF));
		entity.setTb1046IedByF1046Code(new Tb1046IedEntity());
		return entity;
	}
	
	public static Tb1091IotermEntity createTb1091() {
		Tb1091IotermEntity entity = new Tb1091IotermEntity();
		entity.setF1091Code(rscp.nextTbCode(DBConstants.PR_IOTERM));
		entity.setTb1046IedByF1046Code(new Tb1046IedEntity());
		return entity;
	}
	
	public static Tb1092PowerkkEntity createTb1092() {
		Tb1092PowerkkEntity entity = new Tb1092PowerkkEntity();
		entity.setF1092Code(rscp.nextTbCode(DBConstants.PR_POWERKK));
		entity.setTb1046IedByF1046Code(new Tb1046IedEntity());
		return entity;
	}
	
	public static Tb1093VoltagekkEntity createTb1093() {
		Tb1093VoltagekkEntity entity = new Tb1093VoltagekkEntity();
		entity.setF1093Code(rscp.nextTbCode(DBConstants.PR_VOLTAGEKK));
		entity.setTb1067CtvtsecondaryByF1067Code(new Tb1067CtvtsecondaryEntity());
		return entity;
	}
	
	public static Tb1049RegionEntity createRegion() {
		Tb1049RegionEntity regionEntity = new Tb1049RegionEntity();
		regionEntity.setF1049Code(rscp.nextTbCode(DBConstants.PR_REGION));
		regionEntity.setTb1041SubstationByF1041Code(getSubstation());
		regionEntity.setTb1050CubiclesByF1049Code(new HashSet<Tb1050CubicleEntity>());
		return regionEntity;
	}
	
	public static Tb1050CubicleEntity createCubicle() {
		Tb1050CubicleEntity cubicleEntity = new Tb1050CubicleEntity();
		cubicleEntity.setF1050Code(rscp.nextTbCode(DBConstants.PR_CUBICLE));
		cubicleEntity.setTb1049RegionByF1049Code(new Tb1049RegionEntity());
		return cubicleEntity;
	}
	
	public static Tb1051CableEntity createCable() {
		Tb1051CableEntity cableEntity = new Tb1051CableEntity();
		cableEntity.setF1051Code(rscp.nextTbCode(DBConstants.PR_CABLE));
		cableEntity.setTb1041SubstationByF1041Code(getSubstation());
		cableEntity.setTb1050CubicleByF1050CodeA(new Tb1050CubicleEntity());
		cableEntity.setTb1050CubicleByF1050CodeB(new Tb1050CubicleEntity());
		return cableEntity;
	}
	
	public static Tb1053PhysconnEntity createPhysconn() {
		Tb1053PhysconnEntity physconnEntity = new Tb1053PhysconnEntity();
		physconnEntity.setF1053Code(rscp.nextTbCode(DBConstants.PR_PHYSCONN));
		physconnEntity.setTb1048PortByF1048CodeA(new Tb1048PortEntity());
		physconnEntity.setTb1048PortByF1048CodeB(new Tb1048PortEntity());
		return physconnEntity;
	}
	
	public static TB1086DefectFuncREntity createDefectFunc() {
		TB1086DefectFuncREntity defectFunc = new TB1086DefectFuncREntity();
		defectFunc.setF1086CODE(rscp.nextTbCode(DBConstants.PR_FUNDEF));
		return defectFunc;
	}

	@SuppressWarnings("unchecked")
	public static Tb1041SubstationEntity getSubstation() {
		List<Tb1041SubstationEntity> list = (List<Tb1041SubstationEntity>) defaultService.getAll(Tb1041SubstationEntity.class);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public static Tb1047BoardEntity createBoardEntity(){
		Tb1047BoardEntity boardEntity = new Tb1047BoardEntity();
		boardEntity.setF1047Code(rscp.nextTbCode(DBConstants.PR_BOARD));
		return boardEntity;
	}
	
	public static Tb1048PortEntity createPortEntity() {
		Tb1048PortEntity portEntity = new Tb1048PortEntity();
		portEntity.setF1048Code(rscp.nextTbCode(DBConstants.PR_PORT));
		return portEntity;
	}
}
