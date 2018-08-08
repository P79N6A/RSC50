package com.shrcn.tool.rtu.model;
/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 
 * @author 孙春颖
 * @version 1.0, 2013 7 15
 */
/**
 * $Log: TDcaGroup.java,v $
 * Revision 1.9  2013/09/02 00:55:25  cxc
 * update:修改copy方法
 *
 * Revision 1.8  2013/08/30 02:14:54  cxc
 * fix bug:修复103采集excel导出出错的问题
 *
 * Revision 1.7  2013/08/09 06:29:54  cxc
 * update:解决表格中排序的问题
 *
 * Revision 1.6  2013/08/05 12:40:22  cxc
 * Update：添加获取points方法
 *
 * Revision 1.5  2013/08/01 00:33:58  cxc
 * Update：去掉dbType属性
 *
 * Revision 1.4  2013/07/24 12:08:09  cxc
 * Update：修改
 *
 * Revision 1.3  2013/07/18 03:48:59  scy
 * Update：修改T_DcaNet、T_DcaCom、T_Dcaied103、T_DcaGroup
 *
 * Revision 1.2  2013/07/17 09:19:08  scy
 * Update：调整103DCA数据结构
 *
 * Revision 1.1  2013/07/16 06:36:21  scy
 * Add：创建
 *
 */
public class TDcaGroup implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5367060269058857353L;
	private int id;
	private int groupID;
	private String description;
	private int pointOffset;
	private int pointNumber;
	private int iedOffset;

	private TDcaied103 ied;

	private Set<TDcaMx> psMx = new HashSet<TDcaMx>();
	private Set<TDcaSt> psSt = new HashSet<TDcaSt>();
	private Set<TDcaCo> psCo = new HashSet<TDcaCo>();
	private Set<TDcaSp> psSp = new HashSet<TDcaSp>();
	private Set<TDcaSg> psSg = new HashSet<TDcaSg>();
	private Set<TDcaSe> psSe = new HashSet<TDcaSe>();
	
	private List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
	
	public TDcaied103 getIed() {
		return ied;
	}
	
	public void setIed(TDcaied103 ied) {
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

	public TDcaGroup() {
	}

	public TDcaGroup(int id) {
		setId(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPointOffset() {
		return pointOffset;
	}

	public void setPointOffset(int pointOffset) {
		this.pointOffset = pointOffset;
	}

	public int getPointNumber() {
		return pointNumber;
	}

	public void setPointNumber(int pointNumber) {
		this.pointNumber = pointNumber;
	}

	public int getIedOffset() {
		return iedOffset;
	}

	public void setIedOffset(int iedOffset) {
		this.iedOffset = iedOffset;
	}

	public void addMxPoint(TDcaMx pmx) {
		getPsMx().add(pmx);
		pmx.setGroup(this);
	}

	public void addStPoint(TDcaSt pst) {
		getPsSt().add(pst);
		pst.setGroup(this);
	}

	public void addCoPoint(TDcaCo pco) {
		getPsCo().add(pco);
		pco.setGroup(this);
	}

	public void addSpPoint(TDcaSp psp) {
		getPsSp().add(psp);
		psp.setGroup(this);
	}

	public void addSgPoint(TDcaSg psg) {
		getPsSg().add(psg);
		psg.setGroup(this);
	}

	public void addSePoint(TDcaSe pse) {
		getPsSe().add(pse);
		pse.setGroup(this);
	}
	
	public TDcaGroup copy() {
		TDcaGroup group = new TDcaGroup();
		group.setDescription(getDescription());
		group.setGroupID(getGroupID());
		group.setIedOffset(getIedOffset());
		group.setPointNumber(getPointNumber());
		group.setPointOffset(getPointOffset());
		group.setIed(getIed());
		return group;
	}
	
	public List<Map<String, Object>> getDca103Maps() {
		return map;
	}
	
	public void setDca103Maps(List<Map<String, Object>> map){
		this.map = map;
	}
}
