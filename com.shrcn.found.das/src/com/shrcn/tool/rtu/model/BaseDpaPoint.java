/**
 * Copyright (c) 2007-2010 chenchun.
 * All rights reserved. This program is an application based on tcp/ip.
 */
package com.shrcn.tool.rtu.model;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;

/**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2012-2-9
 */
/**
 * $Log: BaseDpaPoint.java,v $
 * Revision 1.20  2013/12/19 01:00:42  cxc
 * Update:修改entryID为int型
 *
 * Revision 1.19  2013/10/14 00:22:47  cxc
 * update:为了满足104采集中五防顺控点的配置在dpa点中增加infoType，pointNum属性
 *
 * Revision 1.18  2013/08/20 08:33:21  cxc
 * fix bug:修复61850采集描述不显示的问题
 *
 * Revision 1.17  2013/08/19 11:34:20  cxc
 * update:去掉转发表中的dattype字段
 *
 * Revision 1.16  2013/08/08 02:52:47  cxc
 * update:设置属性时增加dca为空的判断
 *
 * Revision 1.15  2013/05/16 10:27:32  dlh
 * 解决当修改实时库实点和虚点描述为非法字符时，导出RTU和Configs文件时异常的问题。
 *
 * Revision 1.14  2013/03/04 05:07:27  scy
 * Update：描述信息直接从DCA获取，不再手动设置。
 *
 * Revision 1.13  2012/12/10 00:58:32  cchun
 * Fix Bug:解决导入rtu后转发表没有描述的问题
 *
 * Revision 1.12  2012/12/06 02:44:51  cchun
 * Fix Bug:修复getRef()方法getLd()为null异常
 *
 * Revision 1.11  2012/11/30 12:32:18  cchun
 * Update:添加getRef()
 *
 * Revision 1.10  2012/11/29 00:50:45  cchun
 * Update: 为AO表添加dattype字段
 *
 * Revision 1.9  2012/11/26 08:41:19  cchun
 * Fix Bug:补充setObj()
 *
 * Revision 1.8  2012/11/19 12:15:23  cchun
 * Update:getDcaPoint()添加对空点的处理
 *
 * Revision 1.7  2012/11/19 07:24:00  cchun
 * Update:添加saddr,entryid,groupid
 *
 * Revision 1.6  2012/11/16 09:31:48  cchun
 * Update:增加getDcaPoint()
 *
 * Revision 1.5  2012/11/13 12:51:33  cchun
 * Update:去掉fc，object -> obj
 *
 * Revision 1.4  2012/11/13 11:22:05  cchun
 * Refactor:统一序号属性名为seqnum
 *
 * Revision 1.3  2012/11/13 10:44:14  cchun
 * Update:修改对象复制方法
 *
 * Revision 1.2  2012/11/09 09:35:38  cchun
 * Update:完成dpa模型sql及其映射
 *
 * Revision 1.1  2012/10/25 11:25:47  cchun
 * Refactor:方便hibernate初始化，移动位置
 *
 * Revision 1.2  2012/10/22 07:43:28  cchun
 * Update:根据应用修改实体Bean
 *
 * Revision 1.1  2012/10/17 08:09:23  cchun
 * Add:映射实体类
 *
 */
public abstract class BaseDpaPoint extends RowData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
    private String dbtype;
    private TDcaMx mx;
    private TDcaSt st;
    private TDcaCo co;
    private TDcaSp sp;
    private TDcaSg sg;
    private TDcaSe se;
    
    private int saddr;
    private int entryid;
    private String groupid;
    private String privateInfo;
    private String des;
    
    private TDpaobject obj;
    
    public BaseDpaPoint() {
    }
    
    public BaseDpaPoint(int id) {
    	this.id = id;
    }
    
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public TDpaobject getObj() {
		return obj;
	}

	public void setObj(TDpaobject obj) {
		this.obj = obj;
	}

	public String getDbtype() {
		return dbtype;
	}
	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public TDcaMx getMx() {
		return mx;
	}

	public void setMx(TDcaMx mx) {
		this.mx = mx;
	}

	public TDcaSt getSt() {
		return st;
	}

	public void setSt(TDcaSt st) {
		this.st = st;
	}

	public TDcaCo getCo() {
		return co;
	}

	public void setCo(TDcaCo co) {
		this.co = co;
	}

	public TDcaSp getSp() {
		return sp;
	}

	public void setSp(TDcaSp sp) {
		this.sp = sp;
	}

	public TDcaSg getSg() {
		return sg;
	}

	public void setSg(TDcaSg sg) {
		this.sg = sg;
	}

	public TDcaSe getSe() {
		return se;
	}

	public void setSe(TDcaSe se) {
		this.se = se;
	}

    public int getSaddr() {
		return saddr;
	}

	public void setSaddr(int saddr) {
		this.saddr = saddr;
	}


	public int getEntryid() {
		return entryid;
	}

	public void setEntryid(int entryid) {
		this.entryid = entryid;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	 
	public String getPrivateInfo() {
		return privateInfo;
	}

	public void setPrivateInfo(String privateInfo) {
		this.privateInfo = privateInfo;
	}

	public void setIeddesc(String ieddesc) {
	}

	public void setIedtype(String iedtype) {
	}

	public String getIeddesc() {
		String value = getDescription();
		String[] result =value.split(" ");
		if(result.length>=2){
			return result[1];
		}
		return value;
	}

	public String getIedtype() {
		String value = getDescription();
		String[] result =value.split(" ");
		if(result.length>=2){
			return result[0];
		}
		return value;
	}

	public BaseDcaPoint getDcaPoint() {
		if (StringUtil.isEmpty(getDbtype()))
			return null;
		return (BaseDcaPoint) ObjectUtil.getProperty(this, getDbtype()
				.toLowerCase());
	}

	public String getRef() {
		BaseDcaPoint dcap = getDcaPoint();
		BeanDaoService beanDao= BeanDaoImpl.getInstance();
		if (dcap != null) {
			String dcaref = dcap.getRef();
			if (dcap.getLd() == null) {
				if (StringUtil.isEmpty(dcaref)) {
					BaseCalcExpr expr = (BaseCalcExpr) beanDao.getObject(BaseCalcExpr.class, dcap.getFc().toLowerCase(), dcap);
					return (expr==null) ? "" : expr.getDataId();
				} else {
					return dcaref;
				} 
			} else {
				return dcap.getLd().getLdname() + "/" + dcaref;
			}
		}
		return "";
	}
	
	public void setRef(String ref) {
	}
	
	public String getDescription() {
		BaseDcaPoint dcap = getDcaPoint();
		return dcap == null ? "" : dcap.getDescription();
	}

	public void setDescription(String desc) {
		BaseDcaPoint dcap = getDcaPoint();
		if(dcap == null){
			return;
		}
		dcap.setDescription(desc);
	}
	
	public String getDattype() {
		BaseDcaPoint dcap = getDcaPoint();
		return dcap == null ? "" : dcap.getDattype();
	}

	public void setDattype(String dattype) {
		BaseDcaPoint dcap = getDcaPoint();
		if(dcap == null){
			return;
		}
		dcap.setDattype(dattype);
	}
	
	public BaseDpaPoint copy() {
		return (BaseDpaPoint) ObjectUtil.duplicate(this);
	}
	
	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public abstract void setPointNum(int pointNum);
    public abstract void setInfoType(int infoType);
}
