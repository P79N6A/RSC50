package com.synet.tool.rsc.model;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1022FaultconfigEntity extends Deletable {
    private String f1022Code;
    private int f1011No;
    private int f1022Faultlevel;
    private Integer f1022T1;
    private Integer f1022T2;
    private Integer f1022K;

    public String getF1022Code() {
        return f1022Code;
    }

    public void setF1022Code(String f1022Code) {
        this.f1022Code = f1022Code;
    }

    public int getF1011No() {
        return f1011No;
    }

    public void setF1011No(int f1011No) {
        this.f1011No = f1011No;
    }

    public int getF1022Faultlevel() {
        return f1022Faultlevel;
    }

    public void setF1022Faultlevel(int f1022Faultlevel) {
        this.f1022Faultlevel = f1022Faultlevel;
    }

    public Integer getF1022T1() {
        return f1022T1;
    }

    public void setF1022T1(Integer f1022T1) {
        this.f1022T1 = f1022T1;
    }

    public Integer getF1022T2() {
        return f1022T2;
    }

    public void setF1022T2(Integer f1022T2) {
        this.f1022T2 = f1022T2;
    }

    public Integer getF1022K() {
        return f1022K;
    }

    public void setF1022K(Integer f1022K) {
        this.f1022K = f1022K;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1022FaultconfigEntity that = (Tb1022FaultconfigEntity) o;

        if (f1011No != that.f1011No) return false;
        if (f1022Faultlevel != that.f1022Faultlevel) return false;
        if (f1022Code != null ? !f1022Code.equals(that.f1022Code) : that.f1022Code != null) return false;
        if (f1022K != null ? !f1022K.equals(that.f1022K) : that.f1022K != null) return false;
        if (f1022T1 != null ? !f1022T1.equals(that.f1022T1) : that.f1022T1 != null) return false;
        if (f1022T2 != null ? !f1022T2.equals(that.f1022T2) : that.f1022T2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1022Code != null ? f1022Code.hashCode() : 0;
        result = 31 * result + f1011No;
        result = 31 * result + f1022Faultlevel;
        result = 31 * result + (f1022T1 != null ? f1022T1.hashCode() : 0);
        result = 31 * result + (f1022T2 != null ? f1022T2.hashCode() : 0);
        result = 31 * result + (f1022K != null ? f1022K.hashCode() : 0);
        return result;
    }
}
