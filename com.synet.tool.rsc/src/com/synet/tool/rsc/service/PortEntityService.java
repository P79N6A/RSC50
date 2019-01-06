package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;

public class PortEntityService extends BaseService{

	public Tb1048PortEntity existsEntity(Tb1048PortEntity entity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("", entity.getTb1047BoardByF1047Code());
		params.put("", entity.getF1048Direction());
		params.put("", entity.getF1048No());
		params.put("", entity.getF1048Plug());
		return (Tb1048PortEntity) beanDao.getObject(Tb1048PortEntity.class, params);
	}
	
	/**
	 * 根据装置Name，板卡编号，端口编号查询端口信息
	 * @param devName
	 * @param slot
	 * @param f1048No
	 * @return
	 */
	public Tb1048PortEntity getPortEntity(String devName, String slot,String portNo) {
		if (devName == null || slot == null || portNo == null) 
			return null;
		Tb1046IedEntity ied = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Name", devName);
		if (ied != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tb1046IedByF1046Code",ied);
			params.put("f1047Slot", slot);
			Tb1047BoardEntity boardEntity = (Tb1047BoardEntity) beanDao.getObject(Tb1047BoardEntity.class, params);
			if (boardEntity != null) {
				return getPortEntity(boardEntity, portNo);
			}
		}
		return null;
	}
	
	/**
	 * 获取板卡对应端口
	 * @param boardEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1048PortEntity> getByBoard(Tb1047BoardEntity boardEntity) {
		return (List<Tb1048PortEntity>) beanDao.getListByCriteria(
				Tb1048PortEntity.class, "tb1047BoardByF1047Code", boardEntity);
	}
	
	/**
	 * 获取板卡对应端口数
	 * @param boardEntity
	 * @return
	 */
	public int getCountByBoard(Tb1047BoardEntity boardEntity) {
		return hqlDao.getCount(Tb1048PortEntity.class, "tb1047BoardByF1047Code", boardEntity);
	}
	
	/**
	 * 获取板卡对应端口
	 * @param boardEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1048PortEntity> getByBoardList(List<Tb1047BoardEntity> boardEntitys) {
		return (List<Tb1048PortEntity>) hqlDao.selectInObjects(Tb1048PortEntity.class,
				"tb1047BoardByF1047Code", boardEntitys);
	}

	public Tb1048PortEntity getPortEntity(Tb1047BoardEntity board, String portNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1047BoardByF1047Code", board);
		params.put("f1048No", portNo);
		return (Tb1048PortEntity) beanDao.getObject(Tb1048PortEntity.class, params);
	}

	public void addPort(Tb1047BoardEntity board, Tb1048PortEntity port) {
		port.setF1048Code(rscp.nextTbCode(DBConstants.PR_PORT));
		port.setTb1047BoardByF1047Code(board);
		beanDao.insert(port);
	}

	public void deletePort(Tb1047BoardEntity board, String portNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1047BoardByF1047Code", board);
		params.put("f1048No", portNo);
		String hql = "update " + Tb1048PortEntity.class.getName() +
				" set deleted=1" +
				" where tb1047BoardByF1047Code=:tb1047BoardByF1047Code and f1048No=:f1048No";
		hqlDao.updateByHql(hql, params);
	}

	public void updatePort(Tb1047BoardEntity board, String portNo, Tb1048PortEntity portNew) {
		if (portNew.getF1048Desc() != null) {
			Tb1048PortEntity portOld = getPortEntity(board, portNo);
			portOld.setF1048Desc(portNew.getF1048Desc());
			beanDao.update(portOld);
		}
	}
}
