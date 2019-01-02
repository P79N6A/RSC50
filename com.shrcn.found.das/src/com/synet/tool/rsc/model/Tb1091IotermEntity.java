package com.synet.tool.rsc.model;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1091IotermEntity extends Deletable {
    private String f1091Code;
    private String f1046Code;
    private String f1091Desc;
    private String f1091TermNo;
    private String f1091CircNo;
    private Tb1046IedEntity tb1046IedByF1046Code;

    public String getF1091Code() {
        return f1091Code;
    }

    public void setF1091Code(String f1091Code) {
        this.f1091Code = f1091Code;
    }

    public String getF1046Code() {
        return f1046Code;
    }

    public void setF1046Code(String f1046Code) {
        this.f1046Code = f1046Code;
    }

    public String getF1091Desc() {
        return f1091Desc;
    }

    public void setF1091Desc(String f1091Desc) {
        this.f1091Desc = f1091Desc;
    }

    public String getF1091TermNo() {
        return f1091TermNo;
    }

    public void setF1091TermNo(String f1091TermNo) {
        this.f1091TermNo = f1091TermNo;
    }

    public String getF1091CircNo() {
        return f1091CircNo;
    }

    public void setF1091CircNo(String f1091CircNo) {
        this.f1091CircNo = f1091CircNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1091IotermEntity that = (Tb1091IotermEntity) o;

        if (f1046Code != null ? !f1046Code.equals(that.f1046Code) : that.f1046Code != null) return false;
        if (f1091CircNo != null ? !f1091CircNo.equals(that.f1091CircNo) : that.f1091CircNo != null) return false;
        if (f1091Code != null ? !f1091Code.equals(that.f1091Code) : that.f1091Code != null) return false;
        if (f1091Desc != null ? !f1091Desc.equals(that.f1091Desc) : that.f1091Desc != null) return false;
        if (f1091TermNo != null ? !f1091TermNo.equals(that.f1091TermNo) : that.f1091TermNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1091Code != null ? f1091Code.hashCode() : 0;
        result = 31 * result + (f1046Code != null ? f1046Code.hashCode() : 0);
        result = 31 * result + (f1091Desc != null ? f1091Desc.hashCode() : 0);
        result = 31 * result + (f1091TermNo != null ? f1091TermNo.hashCode() : 0);
        result = 31 * result + (f1091CircNo != null ? f1091CircNo.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }
}
