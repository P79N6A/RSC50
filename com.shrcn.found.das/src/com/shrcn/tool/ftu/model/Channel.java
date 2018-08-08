package com.shrcn.tool.ftu.model;

public class Channel {

	private int id;
	private String chnName;
	private String iedName;
//	private String socketRul;
	private int iedNumber;
	private byte iec101type;
	private byte mod;
	private int iedOffset;
	private byte dataBit;
	private byte stopBit;
	private byte parity;
	private byte singleAckSupported;
	private byte maxFCBCount;
	private byte infoAddressLength;
	private byte cotLength;
	private byte linkAddrLength;//链路地址长度
	private byte asduAddressLength;//公共地址长度
	private byte clearCOS;
	private byte clearSOE;
	private byte timeMode;
	private byte giRespClass;
	private byte sendDoActterm;
	private String portName;
	private int baudRate;
	private int linkAddress;
	private int statusPoint;
	private int soeBuffSize;
	private int selectTimeout;
	private int recvTimeout;
	private int backScanInterval;
	private int integrityInterval;
	private long serviceList;
	private short maxFrameLength;
	private LogicIed logicIed;
	
	public Channel() {
	}
	
	public Channel(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
 
	public LogicIed getLogicIed() {
		return logicIed;
	}
	
	public void setLogicIed(LogicIed logicIed) {
		this.logicIed = logicIed;
	}

	public int getIedNumber() {
		return iedNumber;
	}


//	public String getSocketRul() {
//		return socketRul;
//	}
//
//	public void setSocketRul(String socketRul) {
//		this.socketRul = socketRul;
//	}




	public String getPortName() {
		return portName;
	}


	public void setPortName(String portName) {
		this.portName = portName;
	}


	public int getBaudRate() {
		return baudRate;
	}


	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}


	public int getLinkAddress() {
		return linkAddress;
	}


	public void setLinkAddress(int linkAddress) {
		this.linkAddress = linkAddress;
	}


	public int getStatusPoint() {
		return statusPoint;
	}


	public void setStatusPoint(int statusPoint) {
		this.statusPoint = statusPoint;
	}


	public int getSoeBuffSize() {
		return soeBuffSize;
	}


	public void setSoeBuffSize(int soeBuffSize) {
		this.soeBuffSize = soeBuffSize;
	}


	public int getSelectTimeout() {
		return selectTimeout;
	}


	public void setSelectTimeout(int selectTimeout) {
		this.selectTimeout = selectTimeout;
	}


	public int getRecvTimeout() {
		return recvTimeout;
	}


	public void setRecvTimeout(int recvTimeout) {
		this.recvTimeout = recvTimeout;
	}


	public int getBackScanInterval() {
		return backScanInterval;
	}


	public void setBackScanInterval(int backScanInterval) {
		this.backScanInterval = backScanInterval;
	}


	public int getIntegrityInterval() {
		return integrityInterval;
	}


	public void setIntegrityInterval(int integrityInterval) {
		this.integrityInterval = integrityInterval;
	}


	public long getServiceList() {
		return serviceList;
	}


	public void setServiceList(long serviceList) {
		this.serviceList = serviceList;
	}


	public short getMaxFrameLength() {
		return maxFrameLength;
	}


	public void setMaxFrameLength(short maxFrameLength) {
		this.maxFrameLength = maxFrameLength;
	}



	public void setIedNumber(byte iedNumber) {
		this.iedNumber = iedNumber;
	}
	

	public String getChnName() {
		return chnName;
	}
	
	public void setChnName(String chnName) {
		this.chnName = chnName;
	}
	
	public String getIedName() {
		return iedName;
	}
	
	public void setIedName(String iedName) {
		this.iedName = iedName;
	}

	public byte getIec101type() {
		return iec101type;
	}

	public void setIec101type(byte iec101type) {
		this.iec101type = iec101type;
	}

	public byte getMod() {
		return mod;
	}

	public void setMod(byte mod) {
		this.mod = mod;
	}

	public int getIedOffset() {
		return iedOffset;
	}

	public void setIedOffset(int iedOffset) {
		this.iedOffset = iedOffset;
	}

	public byte getDataBit() {
		return dataBit;
	}

	public void setDataBit(byte dataBit) {
		this.dataBit = dataBit;
	}

	public byte getStopBit() {
		return stopBit;
	}

	public void setStopBit(byte stopBit) {
		this.stopBit = stopBit;
	}

	public byte getParity() {
		return parity;
	}

	public void setParity(byte parity) {
		this.parity = parity;
	}

	public byte getSingleAckSupported() {
		return singleAckSupported;
	}

	public void setSingleAckSupported(byte singleAckSupported) {
		this.singleAckSupported = singleAckSupported;
	}

	public byte getMaxFCBCount() {
		return maxFCBCount;
	}

	public void setMaxFCBCount(byte maxFCBCount) {
		this.maxFCBCount = maxFCBCount;
	}

	public byte getInfoAddressLength() {
		return infoAddressLength;
	}

	public void setInfoAddressLength(byte infoAddressLength) {
		this.infoAddressLength = infoAddressLength;
	}

	public byte getCotLength() {
		return cotLength;
	}

	public void setCotLength(byte cotLength) {
		this.cotLength = cotLength;
	}

	public byte getLinkAddrLength() {
		return linkAddrLength;
	}

	public void setLinkAddrLength(byte linkAddrLength) {
		this.linkAddrLength = linkAddrLength;
	}

	public byte getAsduAddressLength() {
		return asduAddressLength;
	}

	public void setAsduAddressLength(byte asduAddressLength) {
		this.asduAddressLength = asduAddressLength;
	}

	public byte getClearCOS() {
		return clearCOS;
	}

	public void setClearCOS(byte clearCOS) {
		this.clearCOS = clearCOS;
	}

	public byte getClearSOE() {
		return clearSOE;
	}

	public void setClearSOE(byte clearSOE) {
		this.clearSOE = clearSOE;
	}

	public byte getTimeMode() {
		return timeMode;
	}

	public void setTimeMode(byte timeMode) {
		this.timeMode = timeMode;
	}

	public byte getGiRespClass() {
		return giRespClass;
	}

	public void setGiRespClass(byte giRespClass) {
		this.giRespClass = giRespClass;
	}

	public byte getSendDoActterm() {
		return sendDoActterm;
	}

	public void setSendDoActterm(byte sendDoActterm) {
		this.sendDoActterm = sendDoActterm;
	}

	public void setIedNumber(int iedNumber) {
		this.iedNumber = iedNumber;
	}
	
	
}
