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
import com.synet.tool.rsc.model.IM111FibreListEntity;

public class ImportChecker {

	public static boolean isValid(Object obj) {
		if (obj instanceof IM101IEDListEntity) {
			IM101IEDListEntity entity = (IM101IEDListEntity) obj;
			return (entity.getDevDesc() != null);
		} else if (obj instanceof IM102FibreListEntity) {
			IM102FibreListEntity entity = (IM102FibreListEntity) obj;
			return (entity.getBoardCodeA() != null && entity.getPortCodeA() != null);
		} else if (obj instanceof IM103IEDBoardEntity) {
//			IM103IEDBoardEntity entity = (IM103IEDBoardEntity) obj;
		} else if (obj instanceof IM104StatusInEntity) {
			IM104StatusInEntity entity = (IM104StatusInEntity) obj;
			return (entity.getDevName() != null && entity.getMmsRefAddr() != null && entity.getPinRefAddr() != null);
		} else if (obj instanceof IM105BoardWarnEntity) {
			IM105BoardWarnEntity entity = (IM105BoardWarnEntity) obj;
			return (entity.getDevName() != null && entity.getAlarmRefAddr() != null);
		} else if (obj instanceof IM106PortLightEntity) {
			IM106PortLightEntity entity = (IM106PortLightEntity) obj;
			return (entity.getDevName() != null && entity.getBoardCode() != null 
					&& entity.getPortCode() != null && entity.getOpticalRefAddr() != null);
		} else if (obj instanceof IM107TerStrapEntity) {
			IM107TerStrapEntity entity = (IM107TerStrapEntity) obj;
			return (entity.getDevName() != null && entity.getStrapRefAddr() != null);
		} else if (obj instanceof IM108BrkCfmEntity) {
			IM108BrkCfmEntity entity = (IM108BrkCfmEntity) obj;
			return (entity.getDevName() != null && entity.getCmdAckVpRefAddr() != null 
					&& entity.getCmdOutVpRefAddr() != null);
		} else if (obj instanceof IM109StaInfoEntity) {
			IM109StaInfoEntity entity = (IM109StaInfoEntity) obj;
			return (entity.getDevName() != null && entity.getMmsRefAddr() != null);
		} else if (obj instanceof IM110LinkWarnEntity) {
			IM110LinkWarnEntity entity = (IM110LinkWarnEntity) obj;
			return (entity.getDevName() != null && entity.getMmsRefAddr() != null);
		} else if (obj instanceof IM111FibreListEntity) {
			IM111FibreListEntity entity = (IM111FibreListEntity) obj;
			return (entity.getBoardCodeA() != null && entity.getPortCodeA() != null);
		}
		return true;
	}
}
