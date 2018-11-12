package com.synet.tool.rsc.excel;

import static com.synet.tool.rsc.RSCConstants.ET_IMP_BRD;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_BRK;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_FIB;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_FIBNew;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_IED;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_LINKW;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_PORT;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_ST;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_STA;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_STRAP;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_WRN;

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
import com.synet.tool.rsc.model.IM111FibreListEntity;

public enum EnumFileType {

	IED_LIST(101, "设备台账", ET_IMP_IED, IM101IEDListEntity.class),
	FIBRE_LIST(102, "光缆清册", ET_IMP_FIB, IM102FibreListEntity.class),
	IED_BOARD(103, "装置板卡端口描述", ET_IMP_BRD, IM103IEDBoardEntity.class),
	STATUS_IN(104, "开入信号映射表", ET_IMP_ST, IM104StatusInEntity.class),
	BOARD_WARN(105, "告警与板卡关联表", ET_IMP_WRN, IM105BoardWarnEntity.class),
	PORT_LIGHT(106, "光强与端口关联表", ET_IMP_PORT, IM106PortLightEntity.class),
	TER_STRAP(107, "压板与虚端子关联表", ET_IMP_STRAP, IM107TerStrapEntity.class),
	BRK_CFM(108, "跳合闸反校关联表", ET_IMP_BRK, IM108BrkCfmEntity.class),
	STA_INFO(109, "监控信息点表", ET_IMP_STA, IM109StaInfoEntity.class),
	LINK_WARN(110, "告警与链路关联表", ET_IMP_LINKW, IM110LinkWarnEntity.class),
	FIBRE_LIST_NEW(111, "新光缆清册", ET_IMP_FIBNew, IM111FibreListEntity.class)
	;
	
	private int id;
	private String title;
	private String editorId;
	private Class<?> entityClass;
	
	EnumFileType(int id, String title, String editorId, Class<?> entityClass) {
		this.id = id;
		this.title = title;
		this.editorId = editorId;
		this.entityClass = entityClass;
	}
	
	public int getId() {
		return id;
	}


	public String getTitle() {
		return title;
	}


	public String getEditorId() {
		return editorId;
	}


	public Class<?> getEntityClass() {
		return entityClass;
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
