package com.synet.tool.rsc.model;



/**
 * 输出虚端子
 * Created by chunc on 2018/8/7.
 */
public class Tb1061PoutEntity {
    private String f1061Code;
    private String cbCode;
    private String f1061RefAddr;
    private int f1061Index;
    private int f1011No;
    private String f1061Desc;
    private String dataCode;
    private Tb1046IedEntity tb1046IedByF1046Code;
    private BaseCbEntity cbEntity;
    private Tb1064StrapEntity tb1064StrapByF1064Code;
    private Tb1016StatedataEntity stdata;
    private Tb1006AnalogdataEntity algdata;
    
    public Tb1061PoutEntity() {
	}
    
    public Tb1061PoutEntity(String f1061Desc) {
		this.f1061Desc = f1061Desc;
	}
    
    public String getF1061Code() {
        return f1061Code;
    }

    public void setF1061Code(String f1061Code) {
        this.f1061Code = f1061Code;
    }

    public String getCbCode() {
        return cbCode;
    }

    public void setCbCode(String cbCode) {
        this.cbCode = cbCode;
    }

    public String getF1061RefAddr() {
        return f1061RefAddr;
    }

    public void setF1061RefAddr(String f1061RefAddr) {
        this.f1061RefAddr = f1061RefAddr;
    }

    public int getF1061Index() {
        return f1061Index;
    }

    public void setF1061Index(int f1061Index) {
        this.f1061Index = f1061Index;
    }

    public int getF1011No() {
		return f1011No;
	}

	public void setF1011No(int f1011No) {
		this.f1011No = f1011No;
	}

	public String getF1061Desc() {
        return f1061Desc;
    }

    public void setF1061Desc(String f1061Desc) {
        this.f1061Desc = f1061Desc;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public Tb1016StatedataEntity getStdata() {
		return stdata;
	}

	public void setStdata(Tb1016StatedataEntity stdata) {
		this.stdata = stdata;
	}

	public Tb1006AnalogdataEntity getAlgdata() {
		return algdata;
	}

	public void setAlgdata(Tb1006AnalogdataEntity algdata) {
		this.algdata = algdata;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1061PoutEntity that = (Tb1061PoutEntity) o;

        if (f1061Index != that.f1061Index) return false;
        if (f1011No != that.f1011No) return false;
        if (cbCode != null ? !cbCode.equals(that.cbCode) : that.cbCode != null) return false;
        if (dataCode != null ? !dataCode.equals(that.dataCode) : that.dataCode != null) return false;
        if (f1061Code != null ? !f1061Code.equals(that.f1061Code) : that.f1061Code != null) return false;
        if (f1061Desc != null ? !f1061Desc.equals(that.f1061Desc) : that.f1061Desc != null) return false;
        if (f1061RefAddr != null ? !f1061RefAddr.equals(that.f1061RefAddr) : that.f1061RefAddr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1061Code != null ? f1061Code.hashCode() : 0;
        result = 31 * result + (cbCode != null ? cbCode.hashCode() : 0);
        result = 31 * result + (f1061RefAddr != null ? f1061RefAddr.hashCode() : 0);
        result = 31 * result + f1061Index;
        result = 31 * result + f1011No;
        result = 31 * result + (f1061Desc != null ? f1061Desc.hashCode() : 0);
        result = 31 * result + (dataCode != null ? dataCode.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }

    public BaseCbEntity getCbEntity() {
		return cbEntity;
	}

	public void setCbEntity(BaseCbEntity cbEntity) {
		this.cbEntity = cbEntity;
	}

	public Tb1064StrapEntity getTb1064StrapByF1064Code() {
        return tb1064StrapByF1064Code;
    }

    public void setTb1064StrapByF1064Code(Tb1064StrapEntity tb1064StrapByF1064Code) {
        this.tb1064StrapByF1064Code = tb1064StrapByF1064Code;
    }
}
