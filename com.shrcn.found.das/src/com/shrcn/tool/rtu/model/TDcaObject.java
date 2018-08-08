/**
 * Copyright (c) 2007-2014 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.rtu.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author 孙春颖
 * @version 1.0, 2014-10-20
 */
public class TDcaObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5974882066454413038L;

	private int id;
	private int objectID;
	private String description;
	private String funcType;
	private String pointType;
	private int functionPointOffset;
	private int functionPointNum;
	private int startAddr;

	private TDcaiedModbus ied;
	private TDcaiedAnswer iedAnswer;
	
	private Set<TDcaMx> psMx = new HashSet<TDcaMx>();
	private Set<TDcaSt> psSt = new HashSet<TDcaSt>();
	private Set<TDcaCo> psCo = new HashSet<TDcaCo>();
	private Set<TDcaSp> psSp = new HashSet<TDcaSp>();
	private Set<TDcaSg> psSg = new HashSet<TDcaSg>();
	private Set<TDcaSe> psSe = new HashSet<TDcaSe>();

	private List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
	
	/** . */
	private int iedId;// 仅用于Modbus导出。
	
	private int anwIedId;
	
	/** Answer导出 **/
	private String sendBuffer;
	private String sendCheckType;
	private String recvCheckType;
	private int sendCheckStart;
	private int sendCheckLen;
	private int sendCheckOffset;
	private int sendAdrsOffset;
	private int sendAdrsLen;
	private int receiveLen;
	private int recvCheckStart;
	private int recvCheckLen;
	private int recvCheckOffset;
	private int recvAdrsOffset;
	private int recvAdrsLen;
	private String  askData;
	
	
	
	public TDcaObject(){}
	
	public TDcaObject(int id, int iedId, int objectID, String funcType, String pointType, int functionPointOffset, int functionPointNum, int startAddr, String description){
		this.id = id;
		this.objectID = objectID;
		this.funcType = funcType;
		this.pointType = pointType;
		this.functionPointNum = functionPointNum;
		this.functionPointOffset = functionPointOffset;
		this.startAddr = startAddr;
		this.description = description;
		this.iedId = iedId;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFuncType() {
		return funcType;
	}

	public void setFuncType(String funcType) {
		this.funcType = funcType;
	}

	public String getPointType() {
		return pointType;
	}

	public void setPointType(String pointType) {
		this.pointType = pointType;
	}

	public int getFunctionPointOffset() {
		return functionPointOffset;
	}

	public void setFunctionPointOffset(int functionPointOffset) {
		this.functionPointOffset = functionPointOffset;
	}

	public int getFunctionPointNum() {
		return functionPointNum;
	}

	public void setFunctionPointNum(int functionPointNum) {
		this.functionPointNum = functionPointNum;
	}

	public int getStartAddr() {
		return startAddr;
	}

	public void setStartAddr(int startAddr) {
		this.startAddr = startAddr;
	}

	public TDcaiedModbus getIed() {
		return ied;
	}

	public void setIed(TDcaiedModbus ied) {
		this.ied = ied;
	}

	public Set<TDcaMx> getPsMx() {
		return psMx;
	}

	public void setPsMx(Set<TDcaMx> psMx) {
		this.psMx = psMx;
	}

	public Set<TDcaSt> getPsSt() {
		return psSt;
	}

	public void setPsSt(Set<TDcaSt> psSt) {
		this.psSt = psSt;
	}

	public Set<TDcaCo> getPsCo() {
		return psCo;
	}

	public void setPsCo(Set<TDcaCo> psCo) {
		this.psCo = psCo;
	}

	public Set<TDcaSp> getPsSp() {
		return psSp;
	}

	public void setPsSp(Set<TDcaSp> psSp) {
		this.psSp = psSp;
	}

	public Set<TDcaSg> getPsSg() {
		return psSg;
	}

	public void setPsSg(Set<TDcaSg> psSg) {
		this.psSg = psSg;
	}

	public Set<TDcaSe> getPsSe() {
		return psSe;
	}

	public void setPsSe(Set<TDcaSe> psSe) {
		this.psSe = psSe;
	}

	public void addMxPoint(TDcaMx pmx) {
		getPsMx().add(pmx);
		pmx.setObj(this);
	}

	public void addStPoint(TDcaSt pst) {
		getPsSt().add(pst);
		pst.setObj(this);
	}

	public void addCoPoint(TDcaCo pco) {
		getPsCo().add(pco);
		pco.setObj(this);
	}

	public void addSpPoint(TDcaSp psp) {
		getPsSp().add(psp);
		psp.setObj(this);
	}

	public void addSgPoint(TDcaSg psg) {
		getPsSg().add(psg);
		psg.setObj(this);
	}

	public void addSePoint(TDcaSe pse) {
		getPsSe().add(pse);
		pse.setObj(this);
	}
	
	public int getObjectID() {
		return objectID;
	}

	public void setObjectID(int objectID) {
		this.objectID = objectID;
	}

	public TDcaObject copy() {
		TDcaObject obj = new TDcaObject();
		obj.setObjectID(getObjectID());
		obj.setDescription(getDescription());
		obj.setFunctionPointNum(getFunctionPointNum());
		obj.setFunctionPointOffset(getFunctionPointOffset());
		obj.setFuncType(getFuncType());
		obj.setPointType(getPointType());
		obj.setStartAddr(getStartAddr());
		return obj;
	}

	public List<Map<String, Object>> getDcaMaps() {
		return map;
	}
	
	public void setDcaMaps(List<Map<String, Object>> map){
		this.map = map;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIedId() {
		return iedId;
	}

	public void setIedId(int iedId) {
		this.iedId = iedId;
	}

	public String getSendBuffer() {
		return sendBuffer;
	}

	public void setSendBuffer(String sendBuffer) {
		this.sendBuffer = sendBuffer;
	}

	public String getSendCheckType() {
		return sendCheckType;
	}

	public void setSendCheckType(String sendCheckType) {
		this.sendCheckType = sendCheckType;
	}

	public String getRecvCheckType() {
		return recvCheckType;
	}

	public void setRecvCheckType(String recvCheckType) {
		this.recvCheckType = recvCheckType;
	}

	public int getSendCheckStart() {
		return sendCheckStart;
	}

	public void setSendCheckStart(int sendCheckStart) {
		this.sendCheckStart = sendCheckStart;
	}

	public int getSendCheckLen() {
		return sendCheckLen;
	}

	public void setSendCheckLen(int sendCheckLen) {
		this.sendCheckLen = sendCheckLen;
	}

	public int getSendCheckOffset() {
		return sendCheckOffset;
	}

	public void setSendCheckOffset(int sendCheckOffset) {
		this.sendCheckOffset = sendCheckOffset;
	}

	public int getSendAdrsOffset() {
		return sendAdrsOffset;
	}

	public void setSendAdrsOffset(int sendAdrsOffset) {
		this.sendAdrsOffset = sendAdrsOffset;
	}

	public int getSendAdrsLen() {
		return sendAdrsLen;
	}

	public void setSendAdrsLen(int sendAdrsLen) {
		this.sendAdrsLen = sendAdrsLen;
	}

	public int getReceiveLen() {
		return receiveLen;
	}

	public void setReceiveLen(int receiveLen) {
		this.receiveLen = receiveLen;
	}

	public int getRecvCheckStart() {
		return recvCheckStart;
	}

	public void setRecvCheckStart(int recvCheckStart) {
		this.recvCheckStart = recvCheckStart;
	}

	public int getRecvCheckLen() {
		return recvCheckLen;
	}

	public void setRecvCheckLen(int recvCheckLen) {
		this.recvCheckLen = recvCheckLen;
	}

	public int getRecvCheckOffset() {
		return recvCheckOffset;
	}

	public void setRecvCheckOffset(int recvCheckOffset) {
		this.recvCheckOffset = recvCheckOffset;
	}

	public int getRecvAdrsOffset() {
		return recvAdrsOffset;
	}

	public void setRecvAdrsOffset(int recvAdrsOffset) {
		this.recvAdrsOffset = recvAdrsOffset;
	}

	public int getRecvAdrsLen() {
		return recvAdrsLen;
	}

	public void setRecvAdrsLen(int recvAdrsLen) {
		this.recvAdrsLen = recvAdrsLen;
	}
	
	
	public void copyTo(TDcaObject obj) {
		obj.setSendBuffer(sendBuffer);
		obj.setSendCheckLen(sendCheckLen);
		obj.setSendCheckOffset(sendCheckOffset);
		obj.setSendCheckStart(sendCheckStart);
		obj.setSendCheckType(sendCheckType);
		obj.setSendAdrsLen(sendAdrsLen);
		obj.setSendAdrsOffset(sendAdrsOffset);
		obj.setReceiveLen(receiveLen);
		obj.setRecvAdrsLen(recvAdrsLen);
		obj.setRecvAdrsOffset(recvAdrsOffset);
		obj.setRecvCheckLen(recvCheckLen);
		obj.setRecvCheckOffset(recvCheckOffset);
		obj.setRecvCheckStart(recvCheckStart);
		obj.setRecvCheckType(recvCheckType);
	}

//	public AskData getAskData() {
//		return askData;
//	}
//
//	public void setAskData(AskData askData) {
//		this.askData = askData;
//	}
//
//	public AskReceive getAskrecv() {
//		return askrecv;
//	}
//
//	public void setAskrecv(AskReceive askrecv) {
//		this.askrecv = askrecv;
//	}

	public int getAnwIedId() {
		return anwIedId;
	}

	public void setAnwIedId(int anwIedId) {
		this.anwIedId = anwIedId;
	}

	public TDcaiedAnswer getIedAnswer() {
		return iedAnswer;
	}

	public void setIedAnswer(TDcaiedAnswer iedAnswer) {
		this.iedAnswer = iedAnswer;
	}

	public String getAskData() {
		return askData;
	}

	public void setAskData(String askData) {
		this.askData = askData;
	}

	
}
