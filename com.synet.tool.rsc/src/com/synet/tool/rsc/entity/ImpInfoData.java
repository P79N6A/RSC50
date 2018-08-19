package com.synet.tool.rsc.entity;

import java.util.List;

/**
 * 
 * 导入信息
 *
 */
public class ImpInfoData {
	
	private IEDList iedList;
	private List<FibreList> fibreLists;
	private List<IEDBoard> iedBoards;
	private List<StatusIn> statusIns;
	private List<BoardWarn> boardWarns;
	private List<PortLight> portLights;
	private List<TerStrap> terStraps;
	private List<BrkCfm> brkCfms;
	private List<StaInfo> staInfos;
	
	public IEDList getIedList() {
		return iedList;
	}
	public void setIedList(IEDList iedList) {
		this.iedList = iedList;
	}
	public List<FibreList> getFibreLists() {
		return fibreLists;
	}
	public void setFibreLists(List<FibreList> fibreLists) {
		this.fibreLists = fibreLists;
	}
	public List<IEDBoard> getIedBoards() {
		return iedBoards;
	}
	public void setIedBoards(List<IEDBoard> iedBoards) {
		this.iedBoards = iedBoards;
	}
	public List<StatusIn> getStatusIns() {
		return statusIns;
	}
	public void setStatusIns(List<StatusIn> statusIns) {
		this.statusIns = statusIns;
	}
	public List<BoardWarn> getBoardWarns() {
		return boardWarns;
	}
	public void setBoardWarns(List<BoardWarn> boardWarns) {
		this.boardWarns = boardWarns;
	}
	public List<PortLight> getPortLights() {
		return portLights;
	}
	public void setPortLights(List<PortLight> portLights) {
		this.portLights = portLights;
	}
	public List<TerStrap> getTerStraps() {
		return terStraps;
	}
	public void setTerStraps(List<TerStrap> terStraps) {
		this.terStraps = terStraps;
	}
	public List<BrkCfm> getBrkCfms() {
		return brkCfms;
	}
	public void setBrkCfms(List<BrkCfm> brkCfms) {
		this.brkCfms = brkCfms;
	}
	public List<StaInfo> getStaInfos() {
		return staInfos;
	}
	public void setStaInfos(List<StaInfo> staInfos) {
		this.staInfos = staInfos;
	}
	
}
