package com.synet.tool.rsc.entity;

import java.util.List;

import com.synet.tool.rsc.model.IM105BoardWarnEntity;
import com.synet.tool.rsc.model.IM108BrkCfmEntity;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.IM103IEDBoardEntity;
import com.synet.tool.rsc.model.IM101IEDListEntity;
import com.synet.tool.rsc.model.IM106PortLightEntity;
import com.synet.tool.rsc.model.IM109StaInfoEntity;
import com.synet.tool.rsc.model.IM107TerStrapEntity;

/**
 * 
 * 导入信息
 *
 */
public class ImpInfoData {
	
	private IM101IEDListEntity iedList;
	private List<IM102FibreListEntity> fibreLists;
	private List<IM103IEDBoardEntity> iedBoards;
	private List<StatusIn> statusIns;
	private List<IM105BoardWarnEntity> boardWarns;
	private List<IM106PortLightEntity> portLights;
	private List<IM107TerStrapEntity> terStraps;
	private List<IM108BrkCfmEntity> brkCfms;
	private List<IM109StaInfoEntity> staInfos;
	
	public IM101IEDListEntity getIedList() {
		return iedList;
	}
	public void setIedList(IM101IEDListEntity iedList) {
		this.iedList = iedList;
	}
	public List<IM102FibreListEntity> getFibreLists() {
		return fibreLists;
	}
	public void setFibreLists(List<IM102FibreListEntity> fibreLists) {
		this.fibreLists = fibreLists;
	}
	public List<IM103IEDBoardEntity> getIedBoards() {
		return iedBoards;
	}
	public void setIedBoards(List<IM103IEDBoardEntity> iedBoards) {
		this.iedBoards = iedBoards;
	}
	public List<StatusIn> getStatusIns() {
		return statusIns;
	}
	public void setStatusIns(List<StatusIn> statusIns) {
		this.statusIns = statusIns;
	}
	public List<IM105BoardWarnEntity> getBoardWarns() {
		return boardWarns;
	}
	public void setBoardWarns(List<IM105BoardWarnEntity> boardWarns) {
		this.boardWarns = boardWarns;
	}
	public List<IM106PortLightEntity> getPortLights() {
		return portLights;
	}
	public void setPortLights(List<IM106PortLightEntity> portLights) {
		this.portLights = portLights;
	}
	public List<IM107TerStrapEntity> getTerStraps() {
		return terStraps;
	}
	public void setTerStraps(List<IM107TerStrapEntity> terStraps) {
		this.terStraps = terStraps;
	}
	public List<IM108BrkCfmEntity> getBrkCfms() {
		return brkCfms;
	}
	public void setBrkCfms(List<IM108BrkCfmEntity> brkCfms) {
		this.brkCfms = brkCfms;
	}
	public List<IM109StaInfoEntity> getStaInfos() {
		return staInfos;
	}
	public void setStaInfos(List<IM109StaInfoEntity> staInfos) {
		this.staInfos = staInfos;
	}
	
}
