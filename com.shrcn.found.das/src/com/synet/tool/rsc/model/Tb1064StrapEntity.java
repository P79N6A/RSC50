package com.synet.tool.rsc.model;


/**
 * 保护压板
 * Created by chunc on 2018/8/7.
 */
public class Tb1064StrapEntity {
    private String f1064Code;
    private String f1046Code;
    private int f1064Type;
    private String f1064Num;
    private String f1064Desc;
    private String f1042CodeRelatedBay;
    private Tb1046IedEntity tb1046IedByF1046Code;
    private Tb1058MmsfcdaEntity tb1058MmsByF1058Code;

    public String getF1064Code() {
        return f1064Code;
    }

    public void setF1064Code(String f1064Code) {
        this.f1064Code = f1064Code;
    }

    public String getF1046Code() {
        return f1046Code;
    }

    public void setF1046Code(String f1046Code) {
        this.f1046Code = f1046Code;
    }

    public String getF1064Num() {
        return f1064Num;
    }

    public void setF1064Num(String f1064Num) {
        this.f1064Num = f1064Num;
    }

    public String getF1064Desc() {
        return f1064Desc;
    }

    public void setF1064Desc(String f1064Desc) {
        this.f1064Desc = f1064Desc;
    }

    public String getF1042CodeRelatedBay() {
        return f1042CodeRelatedBay;
    }

    public void setF1042CodeRelatedBay(String f1042CodeRelatedBay) {
        this.f1042CodeRelatedBay = f1042CodeRelatedBay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1064StrapEntity that = (Tb1064StrapEntity) o;

        if (f1042CodeRelatedBay != null ? !f1042CodeRelatedBay.equals(that.f1042CodeRelatedBay) : that.f1042CodeRelatedBay != null)
            return false;
        if (f1046Code != null ? !f1046Code.equals(that.f1046Code) : that.f1046Code != null) return false;
        if (f1064Code != null ? !f1064Code.equals(that.f1064Code) : that.f1064Code != null) return false;
        if (f1064Desc != null ? !f1064Desc.equals(that.f1064Desc) : that.f1064Desc != null) return false;
        if (f1064Num != null ? !f1064Num.equals(that.f1064Num) : that.f1064Num != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1064Code != null ? f1064Code.hashCode() : 0;
        result = 31 * result + (f1046Code != null ? f1046Code.hashCode() : 0);
        result = 31 * result + (f1064Num != null ? f1064Num.hashCode() : 0);
        result = 31 * result + (f1064Desc != null ? f1064Desc.hashCode() : 0);
        result = 31 * result + (f1042CodeRelatedBay != null ? f1042CodeRelatedBay.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }

	public Tb1058MmsfcdaEntity getTb1058MmsByF1058Code() {
		return tb1058MmsByF1058Code;
	}

	public void setTb1058MmsByF1058Code(Tb1058MmsfcdaEntity tb1058MmsByF1058Code) {
		this.tb1058MmsByF1058Code = tb1058MmsByF1058Code;
	}

	public int getF1064Type() {
		return f1064Type;
	}

	public void setF1064Type(int f1064Type) {
		this.f1064Type = f1064Type;
	}
}
