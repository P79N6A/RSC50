package com.synet.tool.rsc.model;

import java.util.List;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1054RcbEntity {
    private String f1054Code;
    private String f1054Rptid;
    private String f1054Dataset;
    private String f1054DsDesc;
    private int f1054IsBrcb;
    private int f1054CbType;
    private Tb1046IedEntity tb1046IedByF1046Code;
    private List<Tb1058MmsfcdaEntity> tb1058MmsfcdasByF1054Code;

    public String getF1054Code() {
        return f1054Code;
    }

    public void setF1054Code(String f1054Code) {
        this.f1054Code = f1054Code;
    }

    public String getF1054Rptid() {
        return f1054Rptid;
    }

    public void setF1054Rptid(String f1054Rptid) {
        this.f1054Rptid = f1054Rptid;
    }

    public String getF1054Dataset() {
        return f1054Dataset;
    }

    public void setF1054Dataset(String f1054Dataset) {
        this.f1054Dataset = f1054Dataset;
    }

    public String getF1054DsDesc() {
        return f1054DsDesc;
    }

    public void setF1054DsDesc(String f1054DsDesc) {
        this.f1054DsDesc = f1054DsDesc;
    }

    public int getF1054IsBrcb() {
        return f1054IsBrcb;
    }

    public void setF1054IsBrcb(int f1054IsBrcb) {
        this.f1054IsBrcb = f1054IsBrcb;
    }

    public int getF1054CbType() {
        return f1054CbType;
    }

    public void setF1054CbType(int f1054CbType) {
        this.f1054CbType = f1054CbType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1054RcbEntity that = (Tb1054RcbEntity) o;

        if (f1054CbType != that.f1054CbType) return false;
        if (f1054IsBrcb != that.f1054IsBrcb) return false;
        if (f1054Code != null ? !f1054Code.equals(that.f1054Code) : that.f1054Code != null) return false;
        if (f1054Dataset != null ? !f1054Dataset.equals(that.f1054Dataset) : that.f1054Dataset != null) return false;
        if (f1054DsDesc != null ? !f1054DsDesc.equals(that.f1054DsDesc) : that.f1054DsDesc != null) return false;
        if (f1054Rptid != null ? !f1054Rptid.equals(that.f1054Rptid) : that.f1054Rptid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1054Code != null ? f1054Code.hashCode() : 0;
        result = 31 * result + (f1054Rptid != null ? f1054Rptid.hashCode() : 0);
        result = 31 * result + (f1054Dataset != null ? f1054Dataset.hashCode() : 0);
        result = 31 * result + (f1054DsDesc != null ? f1054DsDesc.hashCode() : 0);
        result = 31 * result + f1054IsBrcb;
        result = 31 * result + f1054CbType;
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }
    public List<Tb1058MmsfcdaEntity> getTb1058MmsfcdasByF1054Code() {
        return tb1058MmsfcdasByF1054Code;
    }

    public void setTb1058MmsfcdasByF1054Code(List<Tb1058MmsfcdaEntity> tb1058MmsfcdasByF1054Code) {
        this.tb1058MmsfcdasByF1054Code = tb1058MmsfcdasByF1054Code;
    }
}
