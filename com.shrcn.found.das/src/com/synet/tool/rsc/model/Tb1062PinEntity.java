package com.synet.tool.rsc.model;


/**
 * 接收虚端子
 * Created by chunc on 2018/8/7.
 */
public class Tb1062PinEntity {
    private String f1062Code;
    private String f1062RefAddr;
    private String f1062Desc;
    private int f1062IsUsed;
    private Tb1046IedEntity tb1046IedByF1046Code;
    private Tb1064StrapEntity tb1064StrapByF1064Code;

    public String getF1062Code() {
        return f1062Code;
    }

    public void setF1062Code(String f1062Code) {
        this.f1062Code = f1062Code;
    }

    public String getF1062RefAddr() {
        return f1062RefAddr;
    }

    public void setF1062RefAddr(String f1062RefAddr) {
        this.f1062RefAddr = f1062RefAddr;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1062PinEntity that = (Tb1062PinEntity) o;

        if (f1062IsUsed != that.f1062IsUsed) return false;
        if (f1062Code != null ? !f1062Code.equals(that.f1062Code) : that.f1062Code != null) return false;
        if (f1062Desc != null ? !f1062Desc.equals(that.f1062Desc) : that.f1062Desc != null) return false;
        if (f1062RefAddr != null ? !f1062RefAddr.equals(that.f1062RefAddr) : that.f1062RefAddr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1062Code != null ? f1062Code.hashCode() : 0;
        result = 31 * result + (f1062RefAddr != null ? f1062RefAddr.hashCode() : 0);
        result = 31 * result + (f1062Desc != null ? f1062Desc.hashCode() : 0);
        result = 31 * result + f1062IsUsed;
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
