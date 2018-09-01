package com.synet.tool.rsc.entity;

import java.util.List;

import com.synet.tool.rsc.model.BoardWarnEntity;
import com.synet.tool.rsc.model.BrkCfmEntity;
import com.synet.tool.rsc.model.FibreListEntity;
import com.synet.tool.rsc.model.IEDBoardEntity;
import com.synet.tool.rsc.model.IEDListEntity;
import com.synet.tool.rsc.model.PortLightEntity;
import com.synet.tool.rsc.model.StaInfoEntity;
import com.synet.tool.rsc.model.TerStrapEntity;

/**
 * 
 * 导入信息
 *
 */
public class ImpInfoData {
	
	private IEDListEntity iedList;
	private List<FibreListEntity> fibreLists;
	private List<IEDBoardEntity> iedBoards;
	private List<StatusIn> statusIns;
	private List<BoardWarnEntity> boardWarns;
	private List<PortLightEntity> portLights;
	private List<TerStrapEntity> terStraps;
	private List<BrkCfmEntity> brkCfms;
	private List<StaInfoEntity> staInfos;
	
	public IEDListEntity getIedList() {
		return iedList;
	}
	public void setIedList(IEDListEntity iedList) {
		this.iedList = iedList;
	}
	public List<FibreListEntity> getFibreLists() {
		return fibreLists;
	}
	public void setFibreLists(List<FibreListEntity> fibreLists) {
		this.fibreLists = fibreLists;
	}
	public List<IEDBoardEntity> getIedBoards() {
		return iedBoards;
	}
	public void setIedBoards(List<IEDBoardEntity> iedBoards) {
		this.iedBoards = iedBoards;
	}
	public List<StatusIn> getStatusIns() {
		return statusIns;
	}
	public void setStatusIns(List<StatusIn> statusIns) {
		this.statusIns = statusIns;
	}
	public List<BoardWarnEntity> getBoardWarns() {
		return boardWarns;
	}
	public void setBoardWarns(List<BoardWarnEntity> boardWarns) {
		this.boardWarns = boardWarns;
	}
	public List<PortLightEntity> getPortLights() {
		return portLights;
	}
	public void setPortLights(List<PortLightEntity> portLights) {
		this.portLights = portLights;
	}
	public List<TerStrapEntity> getTerStraps() {
		return terStraps;
	}
	public void setTerStraps(List<TerStrapEntity> terStraps) {
		this.terStraps = terStraps;
	}
	public List<BrkCfmEntity> getBrkCfms() {
		return brkCfms;
	}
	public void setBrkCfms(List<BrkCfmEntity> brkCfms) {
		this.brkCfms = brkCfms;
	}
	public List<StaInfoEntity> getStaInfos() {
		return staInfos;
	}
	public void setStaInfos(List<StaInfoEntity> staInfos) {
		this.staInfos = staInfos;
	}
	
}
