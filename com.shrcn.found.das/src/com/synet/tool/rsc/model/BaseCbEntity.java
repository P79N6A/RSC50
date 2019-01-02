package com.synet.tool.rsc.model;

import java.io.Serializable;
import java.util.List;

public abstract class BaseCbEntity extends Deletable implements Serializable {
	
	private static final long serialVersionUID = -8850845257864591506L;
	
	protected String cbCode;
    protected String cbName;
    protected String cbId;
    protected String macAddr;
    protected String vlanid;
    protected String vlanPriority;
    protected String appid;
    protected String dataset;
    protected String dsDesc;
    protected String f1071Code;
    protected Tb1046IedEntity tb1046IedByF1046Code;
    protected List<Tb1061PoutEntity> tb1061PoutsByCbCode;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseCbEntity that = (BaseCbEntity) o;

        if (appid != null ? !appid.equals(that.appid) : that.appid != null) return false;
        if (cbName != null ? !cbName.equals(that.cbName) : that.cbName != null) return false;
        if (cbId != null ? !cbId.equals(that.cbId) : that.cbId != null) return false;
        if (cbCode != null ? !cbCode.equals(that.cbCode) : that.cbCode != null) return false;
        if (dataset != null ? !dataset.equals(that.dataset) : that.dataset != null) return false;
        if (dsDesc != null ? !dsDesc.equals(that.dsDesc) : that.dsDesc != null) return false;
        if (macAddr != null ? !macAddr.equals(that.macAddr) : that.macAddr != null) return false;
        if (vlanPriority != null ? !vlanPriority.equals(that.vlanPriority) : that.vlanPriority != null)
            return false;
        if (vlanid != null ? !vlanid.equals(that.vlanid) : that.vlanid != null) return false;
        if (f1071Code != null ? !f1071Code.equals(that.f1071Code) : that.f1071Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cbCode != null ? cbCode.hashCode() : 0;
        result = 31 * result + (cbName != null ? cbName.hashCode() : 0);
        result = 31 * result + (cbId != null ? cbId.hashCode() : 0);
        result = 31 * result + (macAddr != null ? macAddr.hashCode() : 0);
        result = 31 * result + (vlanid != null ? vlanid.hashCode() : 0);
        result = 31 * result + (vlanPriority != null ? vlanPriority.hashCode() : 0);
        result = 31 * result + (appid != null ? appid.hashCode() : 0);
        result = 31 * result + (dataset != null ? dataset.hashCode() : 0);
        result = 31 * result + (dsDesc != null ? dsDesc.hashCode() : 0);
        result = 31 * result + (f1071Code != null ? f1071Code.hashCode() : 0);
        return result;
    }

	public String getCbCode() {
		return cbCode;
	}

	public void setCbCode(String cbCode) {
		this.cbCode = cbCode;
	}

	public String getCbName() {
		return cbName;
	}

	public void setCbName(String cbName) {
		this.cbName = cbName;
	}

	public String getCbId() {
		return cbId;
	}

	public void setCbId(String cbId) {
		this.cbId = cbId;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}

	public String getVlanid() {
		return vlanid;
	}

	public void setVlanid(String vlanid) {
		this.vlanid = vlanid;
	}

	public String getVlanPriority() {
		return vlanPriority;
	}

	public void setVlanPriority(String vlanPriority) {
		this.vlanPriority = vlanPriority;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public String getDsDesc() {
		return dsDesc;
	}

	public void setDsDesc(String dsDesc) {
		this.dsDesc = dsDesc;
	}

	public String getF1071Code() {
		return f1071Code;
	}

	public void setF1071Code(String f1071Code) {
		this.f1071Code = f1071Code;
	}

	public Tb1046IedEntity getTb1046IedByF1046Code() {
		return tb1046IedByF1046Code;
	}

	public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
		this.tb1046IedByF1046Code = tb1046IedByF1046Code;
	}

	public List<Tb1061PoutEntity> getTb1061PoutsByCbCode() {
		return tb1061PoutsByCbCode;
	}

	public void setTb1061PoutsByCbCode(List<Tb1061PoutEntity> tb1061PoutsByCbCode) {
		this.tb1061PoutsByCbCode = tb1061PoutsByCbCode;
	}
    
    
    
}
