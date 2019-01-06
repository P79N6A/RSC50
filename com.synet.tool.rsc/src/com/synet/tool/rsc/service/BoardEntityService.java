package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;

public class BoardEntityService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<Tb1047BoardEntity> getByIed(Tb1046IedEntity iedEntity) {
		Map<String, Object> params = new HashMap<>();
		params.put("ied", iedEntity);
		String hql = "from " + Tb1047BoardEntity.class.getName() + 
				" where tb1046IedByF1046Code=:ied and deleted=0";
		return (List<Tb1047BoardEntity>) hqlDao.getListByHql(hql, params);
	}
	
	public Tb1047BoardEntity existsEntity(Tb1047BoardEntity entity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1046IedByF1046Code", entity.getTb1046IedByF1046Code());
		params.put("f1047Slot", entity.getF1047Slot());
		return (Tb1047BoardEntity) beanDao.getObject(Tb1047BoardEntity.class, params);
		
	}
	
	public Tb1047BoardEntity getBoardEntity(String devName, String slot) {
		Tb1046IedEntity ied = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Name", devName);
		if (ied != null) {
			return getBoardEntity(ied, slot);
		}
		return null;
	}
	
	public Tb1047BoardEntity getBoardEntity(Tb1046IedEntity ied, String slot) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1046IedByF1046Code",ied);
		params.put("f1047Slot", slot);
		return (Tb1047BoardEntity) beanDao.getObject(Tb1047BoardEntity.class, params);
	}
	
	public void addBoard(Tb1046IedEntity ied, Tb1047BoardEntity board) {
		board.setF1047Code(rscp.nextTbCode(DBConstants.PR_BOARD));
		board.setTb1046IedByF1046Code(ied);
		beanDao.insert(board);
	}
	
	public void deleteBoard(Tb1046IedEntity ied, String slot) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1046IedByF1046Code",ied);
		params.put("f1047Slot", slot);
		String hql = "update " + Tb1047BoardEntity.class.getName() + 
				" set deleted=1 where tb1046IedByF1046Code=:tb1046IedByF1046Code and f1047Slot=:f1047Slot";
		hqlDao.updateByHql(hql, params);
	}
	
	public void updateBoard(Tb1046IedEntity ied, String slot, Tb1047BoardEntity boardNew) {
		Tb1047BoardEntity boardOld = getBoardEntity(ied, slot);
		if (boardNew.getF1047Desc() != null) {
			boardOld.setF1047Desc(boardNew.getF1047Desc());
		}
		if (boardNew.getF1047Type() != null) {
			boardOld.setF1047Type(boardNew.getF1047Type());
		}
		beanDao.update(boardOld);
	}
}
