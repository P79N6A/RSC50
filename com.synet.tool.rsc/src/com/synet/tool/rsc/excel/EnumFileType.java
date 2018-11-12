package com.synet.tool.rsc.excel;

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

public enum EnumFileType {

	IED_LIST(101, "设备台账", IM101IEDListEntity.class),
	FIBRE_LIST(102, "光缆清册", IM102FibreListEntity.class),
	IED_BOARD(103, "装置板卡端口描述", IM103IEDBoardEntity.class),
	STATUS_IN(104, "开入信号映射表", IM104StatusInEntity.class),
	BOARD_WARN(105, "告警与板卡关联表", IM105BoardWarnEntity.class),
	PORT_LIGHT(106, "光强与端口关联表", IM106PortLightEntity.class),
	TER_STRAP(107, "压板与虚端子关联表", IM107TerStrapEntity.class),
	BRK_CFM(108, "跳合闸反校关联表", IM108BrkCfmEntity.class),
	STA_INFO(109, "监控信息点表", IM109StaInfoEntity.class),
	LINK_WARN(110, "告警与链路关联表", IM110LinkWarnEntity.class),
	FIBRE_LIST_NEW(111, "新光缆清册", IM102FibreListEntity.class)
	;
	
	private int id;
	private String title;
	private Class<?> entityClass;
	
	EnumFileType(int id, String title, Class<?> entityClass) {
		this.id = id;
		this.title = title;
		this.entityClass = entityClass;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}
	
	public static EnumFileType getByTitle(String title) {
		for (EnumFileType temp : EnumFileType.values()) {
			if (title.equals(temp.title)) {
				return temp;
			}
		}
		return null;
	}
	
	public static EnumFileType getById(int id) {
		for (EnumFileType temp : EnumFileType.values()) {
			if (id == temp.id) {
				return temp;
			}
		}
		return null;
	}
}
