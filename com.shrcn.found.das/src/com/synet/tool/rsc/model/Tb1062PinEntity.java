package com.synet.tool.rsc.model;


/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1062PinEntity {
    private String f1062Code;
    private String f1046Code;
    private String f1062RefAddr;
    private String f1020No;
    private String f1062Desc;
    private int f1062IsUsed;
    private String f1064Code;
    private Tb1046IedEntity tb1046IedByF1046Code;
    private Tb1064StrapEntity tb1064StrapByF1064Code;

    public String getF1062Code() {
        return f1062Code;
    }

    public void setF1062Code(String f1062Code) {
        this.f1062Code = f1062Code;
    }

    public String getF1046Code() {
        return f1046Code;
    }

    public void setF1046Code(String f1046Code) {
        this.f1046Code = f1046Code;
    }

    public String getF1062RefAddr() {
        return f1062RefAddr;
    }

    public void setF1062RefAddr(String f1062RefAddr) {
        this.f1062RefAddr = f1062RefAddr;
    }

    public String getF1020No() {
        return f1020No;
    }

    public void setF1020No(String f1020No) {
        this.f1020No = f1020No;
    }

    public String getF1062Desc() {
        return f1062Desc;
    }

    public void setF1062Desc(String f1062Desc) {
        this.f1062Desc = f1062Desc;
    }

    public int getF1062IsUsed() {
        return f1062IsUsed;
    }

    public void setF1062IsUsed(int f1062IsUsed) {
        this.f1062IsUsed = f1062IsUsed;
    }

    public String getF1064Code() {
        return f1064Code;
    }

    public void setF1064Code(String f1064Code) {
        this.f1064Code = f1064Code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1062PinEntity that = (Tb1062PinEntity) o;

        if (f1062IsUsed != that.f1062IsUsed) return false;
        if (f1020No != null ? !f1020No.equals(that.f1020No) : that.f1020No != null) return false;
        if (f1046Code != null ? !f1046Code.equals(that.f1046Code) : that.f1046Code != null) return false;
        if (f1062Code != null ? !f1062Code.equals(that.f1062Code) : that.f1062Code != null) return false;
        if (f1062Desc != null ? !f1062Desc.equals(that.f1062Desc) : that.f1062Desc != null) return false;
        if (f1062RefAddr != null ? !f1062RefAddr.equals(that.f1062RefAddr) : that.f1062RefAddr != null) return false;
        if (f1064Code != null ? !f1064Code.equals(that.f1064Code) : that.f1064Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1062Code != null ? f1062Code.hashCode() : 0;
        result = 31 * result + (f1046Code != null ? f1046Code.hashCode() : 0);
        result = 31 * result + (f1062RefAddr != null ? f1062RefAddr.hashCode() : 0);
        result = 31 * result + (f1020No != null ? f1020No.hashCode() : 0);
        result = 31 * result + (f1062Desc != null ? f1062Desc.hashCode() : 0);
        result = 31 * result + f1062IsUsed;
        result = 31 * result + (f1064Code != null ? f1064Code.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }

    public Tb1064StrapEntity getTb1064StrapByF1064Code() {
        return tb1064StrapByF1064Code;
    }

    public void setTb1064StrapByF1064Code(Tb1064StrapEntity tb1064StrapByF1064Code) {
        this.tb1064StrapByF1064Code = tb1064StrapByF1064Code;
    }
}
