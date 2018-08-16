package com.synet.tool.rsc.model;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1058MmsfcdaEntity {
    private String f1058Code;
    private String f1058RefAddr;
    private int f1058Index;
    private String f1058Desc;
    private int f1058DataType;
    private String dataCode;
    private Tb1046IedEntity tb1046IedByF1046Code;
    private Tb1054RcbEntity tb1054RcbByF1054Code;

    public String getF1058Code() {
        return f1058Code;
    }

    public void setF1058Code(String f1058Code) {
        this.f1058Code = f1058Code;
    }

    public String getF1058RefAddr() {
        return f1058RefAddr;
    }

    public void setF1058RefAddr(String f1058RefAddr) {
        this.f1058RefAddr = f1058RefAddr;
    }

    public int getF1058Index() {
        return f1058Index;
    }

    public void setF1058Index(int f1058Index) {
        this.f1058Index = f1058Index;
    }

    public String getF1058Desc() {
        return f1058Desc;
    }

    public void setF1058Desc(String f1058Desc) {
        this.f1058Desc = f1058Desc;
    }

    public int getF1058DataType() {
        return f1058DataType;
    }

    public void setF1058DataType(int f1058DataType) {
        this.f1058DataType = f1058DataType;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1058MmsfcdaEntity that = (Tb1058MmsfcdaEntity) o;

        if (f1058DataType != that.f1058DataType) return false;
        if (f1058Index != that.f1058Index) return false;
        if (dataCode != null ? !dataCode.equals(that.dataCode) : that.dataCode != null) return false;
        if (f1058Code != null ? !f1058Code.equals(that.f1058Code) : that.f1058Code != null) return false;
        if (f1058Desc != null ? !f1058Desc.equals(that.f1058Desc) : that.f1058Desc != null) return false;
        if (f1058RefAddr != null ? !f1058RefAddr.equals(that.f1058RefAddr) : that.f1058RefAddr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1058Code != null ? f1058Code.hashCode() : 0;
        result = 31 * result + (f1058RefAddr != null ? f1058RefAddr.hashCode() : 0);
        result = 31 * result + f1058Index;
        result = 31 * result + (f1058Desc != null ? f1058Desc.hashCode() : 0);
        result = 31 * result + f1058DataType;
        result = 31 * result + (dataCode != null ? dataCode.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }

    public Tb1054RcbEntity getTb1054RcbByF1054Code() {
        return tb1054RcbByF1054Code;
    }

    public void setTb1054RcbByF1054Code(Tb1054RcbEntity tb1054RcbByF1054Code) {
        this.tb1054RcbByF1054Code = tb1054RcbByF1054Code;
    }
}
