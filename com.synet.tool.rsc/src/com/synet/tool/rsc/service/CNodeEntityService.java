package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1045ConnectivitynodeEntity;

public class CNodeEntityService extends BaseService {

	/**
	 * 添加CNode（增量/非增量）
	 * @param cName
	 * @param disInfo
	 * @return
	 */
	public void addCNode(String cName, Map<String, String> disInfo) {
		Tb1045ConnectivitynodeEntity conn = new Tb1045ConnectivitynodeEntity();
		conn.setF1045Code(rscp.nextTbCode(DBConstants.PR_CNode));
		conn.setF1045Name(cName);
		conn.setF1045Desc(disInfo.get("pathName"));
		beanDao.insert(conn);
	}
	
	/**
	 * 增量删除CNode
	 * @param cName
	 */
	public void deleteCNode(String cName) {
		String hql = "update " + Tb1045ConnectivitynodeEntity.class.getName() + 
				" set deleted=1 where f1045Name=:f1045Name";
		Map<String, Object> params = new HashMap<>();
		params.put("f1045Name", cName);
		hqlDao.updateByHql(hql, params);
	}

	/**
	 * 增量修改CNode
	 * @param bayName
	 * @param disInfo
	 */
	public void updateCNode(String cName, Map<String, String> disInfo) {
		if (disInfo.get("pathName") != null) {
			Tb1045ConnectivitynodeEntity conn = getCNodeEntity(cName);
			conn.setF1045Desc(disInfo.get("pathName"));
			beanDao.update(conn);
		}
	}

	public Tb1045ConnectivitynodeEntity getCNodeEntity(String cName) {
		Tb1045ConnectivitynodeEntity conn = (Tb1045ConnectivitynodeEntity) 
				beanDao.getObject(Tb1045ConnectivitynodeEntity.class, "f1045Name", cName);
//		System.out.println(beanDao.getAll(Tb1045ConnectivitynodeEntity.class));
		return conn;
	}
}
