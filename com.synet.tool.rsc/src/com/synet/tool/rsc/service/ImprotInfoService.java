package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM101IEDListEntity;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.IM103IEDBoardEntity;
import com.synet.tool.rsc.model.IM104StatusInEntity;
import com.synet.tool.rsc.model.IM105BoardWarnEntity;
import com.synet.tool.rsc.model.IM106PortLightEntity;
import com.synet.tool.rsc.model.IM107TerStrapEntity;
import com.synet.tool.rsc.model.IM108BrkCfmEntity;
import com.synet.tool.rsc.model.IM109StaInfoEntity;
import com.synet.tool.rsc.model.IM110LinkWarnEntity;

public class ImprotInfoService extends BaseService {
	
	public IM100FileInfoEntity getFileInfoEntityByFileName(String fileName) {
		return (IM100FileInfoEntity) beanDao.getObject(IM100FileInfoEntity.class, "fileName", fileName);
	}
	
	public void deleteDataByFile(Class<?> clazz, IM100FileInfoEntity fileInfoEntity) {
		beanDao.deleteAll(clazz, "fileInfoEntity", fileInfoEntity);
	}
	
	@SuppressWarnings("unchecked")
	public IM100FileInfoEntity existsEntity(IM100FileInfoEntity entity) {
		List<IM100FileInfoEntity> list = (List<IM100FileInfoEntity>) beanDao.getListByCriteria(IM100FileInfoEntity.class, "filePath", entity.getFilePath());
		if (list != null && list.size() > 0) 
			return list.get(0);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public IM103IEDBoardEntity existsEntity(IM103IEDBoardEntity entity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fileInfoEntity", entity.getFileInfoEntity());
		params.put("boardIndex", entity.getBoardIndex());
		params.put("boardType", entity.getBoardType());
		params.put("boardCode", entity.getBoardCode());
		List<IM103IEDBoardEntity> list = (List<IM103IEDBoardEntity>) beanDao.getListByCriteria(IM103IEDBoardEntity.class, params);
		if (list != null && list.size() > 0) 
			return list.get(0);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<IM100FileInfoEntity> getFileInfoEntityList(Integer fileType) {
		return (List<IM100FileInfoEntity>) beanDao.getListByCriteria(IM100FileInfoEntity.class, "fileType", fileType);
	}
	
//	@SuppressWarnings("unchecked")
//	public List<IM101IEDListEntity> getIEDListEntityList(IM100FileInfoEntity fileInfoEntity) {
//		return (List<IM101IEDListEntity>) getFileItems(IM101IEDListEntity.class,  fileInfoEntity);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<IM102FibreListEntity> getFibreListEntityList(IM100FileInfoEntity fileInfoEntity) {
//		return (List<IM102FibreListEntity>) getFileItems(IM102FibreListEntity.class,  fileInfoEntity);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<IM103IEDBoardEntity> getIEDBoardEntityList(IM100FileInfoEntity fileInfoEntity) {
//		return (List<IM103IEDBoardEntity>) getFileItems(IM103IEDBoardEntity.class,  fileInfoEntity);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<IM104StatusInEntity> getStatusInEntityList(IM100FileInfoEntity fileInfoEntity) {
//		return (List<IM104StatusInEntity>) getFileItems(IM104StatusInEntity.class,  fileInfoEntity);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<IM105BoardWarnEntity> getBoardWarnEntityList(IM100FileInfoEntity fileInfoEntity) {
//		return (List<IM105BoardWarnEntity>) getFileItems(IM105BoardWarnEntity.class,  fileInfoEntity);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<IM106PortLightEntity> getPortLightEntityList(IM100FileInfoEntity fileInfoEntity) {
//		return (List<IM106PortLightEntity>) getFileItems(IM106PortLightEntity.class,  fileInfoEntity);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<IM107TerStrapEntity> getTerStrapEntityList(IM100FileInfoEntity fileInfoEntity) {
//		return (List<IM107TerStrapEntity>) getFileItems(IM107TerStrapEntity.class,  fileInfoEntity);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<IM108BrkCfmEntity> getBrkCfmEntityList(IM100FileInfoEntity fileInfoEntity) {
//		return (List<IM108BrkCfmEntity>) getFileItems(IM108BrkCfmEntity.class,  fileInfoEntity);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<IM109StaInfoEntity> getStaInfoEntityList(IM100FileInfoEntity fileInfoEntity) {
//		return (List<IM109StaInfoEntity>) getFileItems(IM109StaInfoEntity.class, fileInfoEntity);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<IM110LinkWarnEntity> getLinkWarnEntityList(IM100FileInfoEntity fileInfoEntity) {
//		return (List<IM110LinkWarnEntity>) getFileItems(IM110LinkWarnEntity.class, fileInfoEntity);
//	}

	public List<?> getFileItems(Class<?> itemClass, IM100FileInfoEntity fileInfoEntity) {
		return beanDao.getListByCriteria(itemClass, "fileInfoEntity", fileInfoEntity);
	}
}
