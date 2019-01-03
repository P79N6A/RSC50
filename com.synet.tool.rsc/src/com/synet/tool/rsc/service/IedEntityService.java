package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.io.parser.ParserUtil;
import com.synet.tool.rsc.model.IM103IEDBoardEntity;
import com.synet.tool.rsc.model.TB1085ProtFuncEntity;
import com.synet.tool.rsc.model.TB1086DefectFuncREntity;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1026StringdataEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1052CoreEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1054RcbEntity;
import com.synet.tool.rsc.model.Tb1055GcbEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1057SgcbEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1059SgfcdaEntity;
import com.synet.tool.rsc.model.Tb1060SpfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1070MmsserverEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;
import com.synet.tool.rsc.util.F1011_NO;

public class IedEntityService extends BaseService {
	
	/**
	 * 增量删除IED
	 * @param iedName
	 */
	public void deleteOldIed(String iedName) {
		// 删除ied
		Tb1046IedEntity ied = getIedEntityByDevName(iedName);
		beanDao.markDeleted(ied);
		// 删除Pins
		beanDao.markDeleted(Tb1062PinEntity.class, "tb1046IedByF1046Code", ied);
		// 删除Sets
		beanDao.markDeleted(Tb1057SgcbEntity.class, "tb1046IedByF1046Code", ied);
		deleteIedSubEntity(Tb1059SgfcdaEntity.class, "tb1057SgcbByF1057Code.tb1046IedByF1046Code", ied);
		// 删除Params
		beanDao.markDeleted(Tb1060SpfcdaEntity.class, "tb1046IedByF1046Code", ied);
		// 删除Rcbs
		beanDao.markDeleted(Tb1054RcbEntity.class, "tb1046IedByF1046Code", ied);
		beanDao.markDeleted(Tb1058MmsfcdaEntity.class, "tb1046IedByF1046Code", ied);
		// 删除Gooses
		beanDao.markDeleted(Tb1055GcbEntity.class, "tb1046IedByF1046Code", ied);
		// 删除Smvs
		beanDao.markDeleted(Tb1056SvcbEntity.class, "tb1046IedByF1046Code", ied);
		// 删除Pouts
		beanDao.markDeleted(Tb1061PoutEntity.class, "tb1046IedByF1046Code", ied);
		// 删除压板
		beanDao.markDeleted(Tb1064StrapEntity.class, "tb1046IedByF1046Code", ied);
		// 删除实时库（st、ang）
		beanDao.markDeleted(Tb1006AnalogdataEntity.class, "tb1046IedByF1046Code", ied);
		beanDao.markDeleted(Tb1016StatedataEntity.class, "tb1046IedByF1046Code", ied);
		String hql = "update " + Tb1026StringdataEntity.class.getName() +
				" set deleted=1" +
				" where parentCode='" + ied.getF1046Code() + "'";
		hqlDao.updateByHql(hql, null);
		// 删除Inputs（虚回路、逻辑链路、物理回路与逻辑链路关系）
		beanDao.markDeleted(Tb1063CircuitEntity.class, "tb1046IedByF1046CodeIedRecv", ied);
		beanDao.markDeleted(Tb1063CircuitEntity.class, "tb1046IedByF1046CodeIedSend", ied);
		beanDao.markDeleted(Tb1065LogicallinkEntity.class, "tb1046IedByF1046CodeIedRecv", ied);
		beanDao.markDeleted(Tb1065LogicallinkEntity.class, "tb1046IedByF1046CodeIedSend", ied);
		deleteIedSubEntity(Tb1073LlinkphyrelationEntity.class, "tb1065LogicallinkByF1065Code.tb1046IedByF1046CodeIedRecv", ied);
		deleteIedSubEntity(Tb1073LlinkphyrelationEntity.class, "tb1065LogicallinkByF1065Code.tb1046IedByF1046CodeIedSend", ied);
		// 删除板卡、端口、线缆、芯线
		beanDao.markDeleted(Tb1047BoardEntity.class, "tb1046IedByF1046Code", ied);
		deleteIedSubEntity(Tb1048PortEntity.class, "tb1047BoardByF1047Code.tb1046IedByF1046Code", ied);
		// 删除保护功能
		beanDao.markDeleted(TB1085ProtFuncEntity.class, "tb1046ByF1046CODE", ied);
		deleteIedSubEntity(TB1086DefectFuncREntity.class, "tb1085ByF1085CODE.tb1046ByF1046CODE", ied);
		// 删除一二次关联关系（开关刀闸状态、保护测量值）	// 由于相应的模拟量、状态量已标记为删除，故不作处理
	}
	
	private void deleteIedSubEntity(Class<?> clazz, String fname, Tb1046IedEntity ied) {
		String hql = "update " + clazz.getName() + " set deleted=1" +
				" where " + fname + "=:ied";
		Map<String, Object> params = new HashMap<>();
		params.put("ied", ied);
		hqlDao.updateByHql(hql, params);
	}
	
	/**
	 * 增量更新IED
	 * @param upInfo
	 */
	public void updateIed(Map<String, String> upInfo) {
		String iedName = upInfo.get("name");
		Tb1046IedEntity ied = getIedEntityByDevName(iedName);
		iedName = upInfo.get("newName");
		ied.setF1046Name(iedName);
		ied.setF1046Desc(upInfo.get("newDesc"));
		ied.setF1046Model(upInfo.get("type"));
		ied.setF1046Manufacturor(upInfo.get("manufacturer"));
		ied.setF1046ConfigVersion(upInfo.get("configVersion"));
		String vtcrc = upInfo.get("crc");
		ied.setF1046Crc(vtcrc);
		// A/B
		int aOrb = iedName.endsWith("B") ? 2 : 1;
		ied.setF1046AorB(aOrb);
		// 装置类型
		String iedtype = upInfo.get("iedtype");
		EnumIedType eIedType = EnumIedType.getTypeByDesc(iedtype);
		if (eIedType != null) {
			ied.setF1046Type(eIedType.getTypes()[0]);
		} else {
			ied.setF1046Type(0);
		}
		String ipA = upInfo.get("ipA");
		String ipB = upInfo.get("ipB");
		if (ipA != null) {
			ied.setF1046aNetIp(ipA);
		}
		if (ipB != null) {
			ied.setF1046bNetIp(ipB);
		}
		beanDao.update(ied);
		updateCommInfo(ied);
	}
	
	private void updateCommInfo(Tb1046IedEntity ied) {
		// 虚端子CRC
		String hql = "update " + Tb1026StringdataEntity.class.getName() + " set f1026Desc='" + ied.getF1046Crc() +
				"' where parentCode='" + ied.getF1046Code() + "'";
		hqlDao.updateByHql(hql, null);
		// 通信服务
		Tb1070MmsserverEntity mmsServer = (Tb1070MmsserverEntity) 
				beanDao.getObject(Tb1070MmsserverEntity.class, "tb1046IedByF1046Code", ied);
		mmsServer.setF1070IpA(ied.getF1046aNetIp());
		mmsServer.setF1070IpB(ied.getF1046bNetIp());
		mmsServer.setF1070IedCrc(ied.getF1046Crc());
		beanDao.update(mmsServer);
	}

	/**
	 * 根据Difference.msg保存新增ied信息
	 * @param added
	 */
	public void addNewIed(Map<String, String> added) {
		Tb1046IedEntity ied = new Tb1046IedEntity();
		String iedName = added.get("name");
		ied.setF1046Name(iedName);
		ied.setF1046Desc(added.get("desc"));
		ied.setF1046Model(added.get("type"));
		ied.setF1046Manufacturor(added.get("manufacturer"));
		ied.setF1046ConfigVersion(added.get("configVersion"));
		String vtcrc = added.get("crc");
		ied.setF1046Crc(vtcrc);
		// code
		String iedCode = rscp.nextTbCode(DBConstants.PR_IED);
		ied.setF1046Code(iedCode);
		// A/B
		int aOrb = iedName.endsWith("B") ? 2 : 1;
		ied.setF1046AorB(aOrb);
		ied.setF1046IsVirtual(0);
		ied.setF1046boardNum(0);
		
		String iedtype = added.get("iedtype");
		EnumIedType eIedType = EnumIedType.getTypeByDesc(iedtype);
		if (eIedType != null) {
			ied.setF1046Type(eIedType.getTypes()[0]);
		} else {
			ied.setF1046Type(0);
		}
		String ipA = added.get("ipA");
		String ipB = added.get("ipB");
		if (ipA != null) {
			ied.setF1046aNetIp(ipA);
		}
		if (ipB != null) {
			ied.setF1046bNetIp(ipB);
		}
		beanDao.insert(ied);
		addCommInfo(ied);
	}
	
	/**
	 * 添加IED通信相关配置
	 * @param ied
	 */
	public void addCommInfo(Tb1046IedEntity ied) {
		// 通信状态点
		addCommState(ied);
		// 虚端子CRC
		addCRCData(ied);
		// 通信服务
		addMMSServer(ied);
	}
	
	/**
	 * 添加通信状态点
	 * @param ied
	 */
	public void addCommState(Tb1046IedEntity ied) {
		Tb1016StatedataEntity stIedComm = ParserUtil.createStatedata(ied.getF1046Name() + "通信状态点", "", ied.getF1046Code(), ied, F1011_NO.IED_COMM.getId());
		beanDao.insert(stIedComm);
	}
	
	/**
	 * 添加虚端子CRC
	 * @param ied
	 */
	public void addCRCData(Tb1046IedEntity ied) {
		Tb1026StringdataEntity strData = new Tb1026StringdataEntity();
		strData.setF1011No(F1011_NO.VT_CRC.getId());
		strData.setF1026Code(rscp.nextTbCode(DBConstants.PR_String));
		strData.setF1026Desc(ied.getF1046Crc());
		strData.setParentCode(ied.getF1046Code());
		beanDao.insert(strData);
	}
	
	/**
	 * 保存mms通信信息
	 * @param ied
	 */
	public void addMMSServer(Tb1046IedEntity ied) {
		Tb1070MmsserverEntity mmsServer = new Tb1070MmsserverEntity();
		mmsServer.setF1070Code(rscp.nextTbCode(DBConstants.PR_MMSSvr));
		mmsServer.setTb1046IedByF1046Code(ied);
		mmsServer.setF1070IpA(ied.getF1046aNetIp());
		mmsServer.setF1070IpB(ied.getF1046bNetIp());
		mmsServer.setF1070IedCrc(ied.getF1046Crc());
		mmsServer.setF1070CrcPath("LD0/LPHD$SP$IEDPinCrc$setVal");
		beanDao.insert(mmsServer);
	}
	
	private void addIedSubInfos(Tb1046IedEntity ied) {
		
	}
	
	/**
	 * 根据类型和间隔获取IED
	 * @param types
	 * @param bayEntity
	 * @return
	 */
	public List<Tb1046IedEntity> getIedByTypesAndBay(int[] types, Tb1042BayEntity bayEntity) {
		//所有间隔下的合并单元
		List<Tb1046IedEntity> iedEntityByTypes = getIedEntityByTypes(types);
		if(bayEntity == null){
			return iedEntityByTypes;
		}
		List<Tb1046IedEntity> result = new ArrayList<>();
		for (Tb1046IedEntity tb1046IedEntity : iedEntityByTypes) {
			//匹配对应的间隔
			String f1042Code = tb1046IedEntity.getTb1042BaysByF1042Code().getF1042Code();
			if(f1042Code.equals(bayEntity.getF1042Code())) {
				result.add(tb1046IedEntity);
			}
		}
		return result;
	}
	
	/**
	 * 删除IED及其关联对象（仅适用于交换机、光配单元、采集器）
	 * @param iedEntity
	 */
	public void deleteTb1046IedEntity(Tb1046IedEntity iedEntity) {
		deleteBoards(iedEntity);
		beanDao.delete(iedEntity);
	}

	/**
	 * 删除IED关联对象
	 * @param iedEntity
	 */
	@SuppressWarnings("unchecked")
	public void deleteBoards(Tb1046IedEntity iedEntity) {
		List<Tb1047BoardEntity> boards = (List<Tb1047BoardEntity>) beanDao.getListByCriteria(Tb1047BoardEntity.class, "tb1046IedByF1046Code", iedEntity);
		for (Tb1047BoardEntity board : boards) {
			List<Tb1048PortEntity> ports = (List<Tb1048PortEntity>) beanDao.getListByCriteria(Tb1048PortEntity.class, "tb1047BoardByF1047Code", board);
			for (Tb1048PortEntity port : ports) {
				beanDao.deleteAll(Tb1052CoreEntity.class, "tb1048PortByF1048CodeA", port);
				beanDao.deleteAll(Tb1052CoreEntity.class, "tb1048PortByF1048CodeB", port);
				List<Tb1053PhysconnEntity> physA = (List<Tb1053PhysconnEntity>) beanDao.getListByCriteria(Tb1053PhysconnEntity.class, "tb1048PortByF1048CodeA", port);
				List<Tb1053PhysconnEntity> physB = (List<Tb1053PhysconnEntity>) beanDao.getListByCriteria(Tb1053PhysconnEntity.class, "tb1048PortByF1048CodeB", port);
				if (physA == null) {
					physA = new ArrayList<>();
				}
				if (physB != null) {
					physA.addAll(physB);
				}
				// 删除逻辑链路与物理回路关联关系
				for (Tb1053PhysconnEntity phy : physA) {
					beanDao.deleteAll(Tb1073LlinkphyrelationEntity.class, "tb1053PhysconnByF1053Code", phy);
				}
				// 删除端口相关物理回路
				beanDao.deleteBatch(physA);
				// 更新端口光强
				Map<String, Object> params = new HashMap<>();
				params.put("iedCode", iedEntity.getF1046Code());
				params.put("portCode", port.getF1048Code());
				String hql = "update " + Tb1006AnalogdataEntity.class.getName() + " set parentCode=:iedCode " +
						"where parentCode=:portCode";
				hqlDao.updateByHql(hql, params);
			}
			beanDao.deleteBatch(ports);
		}
		beanDao.deleteBatch(boards);
	}
	
	/**
	 * 根据设备类型查找设备
	 * @param types
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1046IedEntity> getIedEntityByTypes(int[] types) {
		if(types.length < 1) {
			return new ArrayList<>();
		}
		List<Integer> lstType = new ArrayList<>();
		for (int i : types) {
			lstType.add(i);
		}
		return (List<Tb1046IedEntity>) hqlDao.selectInObjects(Tb1046IedEntity.class, "f1046Type", lstType);
	}
	
	/**
	 * 根据设备类型查找设备
	 * @param types
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1046IedEntity> getIedEntityByTypes(List<int[]> typeList) {
		if(typeList.size() <= 0) {
			return new ArrayList<>();
		}
		List<Integer> lstType = new ArrayList<>();
		for (int[] types : typeList) {
			for (int i : types) {
				lstType.add(i);
			}
		}
		if (lstType.size() <= 0) {
			return new ArrayList<>();
		}
		return (List<Tb1046IedEntity>) hqlDao.selectInObjects(Tb1046IedEntity.class, "f1046Type", lstType);
	}
	
	/**
	 * 根据所属间隔查询IED
	 * @param bayEntityCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1046IedEntity> getIedEntityByBay(Tb1042BayEntity bayEntity) {
		if(bayEntity == null) {
			return (List<Tb1046IedEntity>) beanDao.getAll(Tb1046IedEntity.class);
		}
		return (List<Tb1046IedEntity>) beanDao.getListByCriteria(Tb1046IedEntity.class, "tb1042BaysByF1042Code", bayEntity);
	}
	
	/**
	 * 更新间隔编号
	 * @param ied
	 */
	public void updateIEDBayCode(String f1046Code, String f1042Code) {
		String hql = "update " + Tb1046IedEntity.class.getName() +
				" set f1042Code=:f1042Code" + 
				" where f1046Code=:f1046Code";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("f1042Code", f1042Code);
		params.put("f1046Code", f1046Code);
		HqlDaoImpl.getInstance().updateByHql(hql, params);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1046IedEntity> getIedByIM103IEDBoard(IM103IEDBoardEntity entity) {
		Map<String, Object> params = new HashMap<>();
		if (!StringUtil.isEmpty(entity.getManufacturor()))
			params.put("f1046Manufacturor", entity.getManufacturor());
		params.put("f1046Model", entity.getDevName());
		if (!StringUtil.isEmpty(entity.getConfigVersion()))
			params.put("f1046ConfigVersion", entity.getConfigVersion());
		params.put("deleted", 0);
		return (List<Tb1046IedEntity>) beanDao.getListByCriteria(Tb1046IedEntity.class, params);
	}

	public Tb1046IedEntity getIedEntityByDevName(String devName) {
		if (devName == null)
			return null;
		Object object = beanDao.getObject(Tb1046IedEntity.class, "f1046Name", devName);
		return (Tb1046IedEntity) object;
	}
	
	public void updateBoardNum(Tb1046IedEntity ied) {
		int num = hqlDao.getCount(Tb1047BoardEntity.class, "tb1046IedByF1046Code", ied);
		ied.setF1046boardNum(num);
		beanDao.update(ied);
	}
}
