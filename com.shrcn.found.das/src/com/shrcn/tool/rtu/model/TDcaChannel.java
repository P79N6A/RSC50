package com.shrcn.tool.rtu.model;

import java.util.HashSet;
import java.util.Set;

public class TDcaChannel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private int iedOffset;
	private int iedNumber;
	private String bufferSize;
	private String respTimeout;
	
	// modbus 查询间隔
	private String requestIntervel;
	private String endian;
	
	private TDca dca;
	
	// DCA103、DCAModBus
	private Set<BaseDcaied> ieds = new HashSet<BaseDcaied>();
	
	public TDcaChannel() {
		super();
	}
	
	public TDcaChannel(int id, String name, int iedOffset, int iedNumber, String requestIntervel, String respTimeOut, String endian) {
		this.id = id;
		this.name = name;
		this.iedOffset = iedOffset;
		this.iedNumber = iedNumber;
		this.requestIntervel = requestIntervel;
		this.respTimeout = respTimeOut;
		this.endian = endian;
	}

	public int getIedOffset() {
		return iedOffset;
	}

	public void setIedOffset(int iedOffset) {
		this.iedOffset = iedOffset;
	}

	public int getIedNumber() {
		return iedNumber;
	}

	public void setIedNumber(int iedNumber) {
		this.iedNumber = iedNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TDca getDca() {
		return dca;
	}

	public void setDca(TDca dca) {
		this.dca = dca;
	}

	public Set<BaseDcaied> getIeds() {
		return ieds;
	}

	public void setIeds(Set<BaseDcaied> ieds) {
		this.ieds = ieds;
	}

	public void addIed(BaseDcaied ied) {
		getIeds().add(ied);
		if (ied instanceof TDcaied103) {
			((TDcaied103) ied).setChn(this);
		} else if(ied instanceof TDcaiedModbus){
			((TDcaiedModbus) ied).setChn(this);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(String bufferSize) {
		this.bufferSize = bufferSize;
	}

	public String getRespTimeout() {
		return respTimeout;
	}

	public void setRespTimeout(String respTimeout) {
		this.respTimeout = respTimeout;
	}
	
	public String getRequestIntervel() {
		return requestIntervel;
	}

	public void setRequestIntervel(String requestIntervel) {
		this.requestIntervel = requestIntervel;
	}

	public TDcaChannel copy() {
		TDcaChannel chn = new TDcaChannel();
		chn.setName(name);
		chn.setIedOffset(iedOffset);
		chn.setIedNumber(iedNumber);
		chn.setBufferSize(bufferSize);
		chn.setRespTimeout(respTimeout);
		chn.setRequestIntervel(requestIntervel);
		chn.setEndian(endian);
		return chn;
	}
	
	public String getProcType() {
		return null;
	}

	public String getEndian() {
		return endian;
	}

	public void setEndian(String endian) {
		this.endian = endian;
	}

}