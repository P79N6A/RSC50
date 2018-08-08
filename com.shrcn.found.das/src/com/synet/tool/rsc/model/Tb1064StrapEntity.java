package com.synet.tool.rsc.model;

import java.util.Collection;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1064StrapEntity {
    private String f1064Code;
    private String f1046Code;
    private int f1021No;
    private String f1064Num;
    private String f1064Desc;
    private String f1042CodeRelatedBay;
    private Collection<Tb1061PoutEntity> tb1061PoutsByF1064Code;
    private Collection<Tb1062PinEntity> tb1062PinsByF1064Code;
    private Tb1046IedEntity tb1046IedByF1046Code;

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

    public int getF1021No() {
        return f1021No;
    }

    public void setF1021No(int f1021No) {
        this.f1021No = f1021No;
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

        if (f1021No != that.f1021No) return false;
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
        result = 31 * result + f1021No;
        result = 31 * result + (f1064Num != null ? f1064Num.hashCode() : 0);
        result = 31 * result + (f1064Desc != null ? f1064Desc.hashCode() : 0);
        result = 31 * result + (f1042CodeRelatedBay != null ? f1042CodeRelatedBay.hashCode() : 0);
        return result;
    }

    public Collection<Tb1061PoutEntity> getTb1061PoutsByF1064Code() {
        return tb1061PoutsByF1064Code;
    }

    public void setTb1061PoutsByF1064Code(Collection<Tb1061PoutEntity> tb1061PoutsByF1064Code) {
        this.tb1061PoutsByF1064Code = tb1061PoutsByF1064Code;
    }

    public Collection<Tb1062PinEntity> getTb1062PinsByF1064Code() {
        return tb1062PinsByF1064Code;
    }

    public void setTb1062PinsByF1064Code(Collection<Tb1062PinEntity> tb1062PinsByF1064Code) {
        this.tb1062PinsByF1064Code = tb1062PinsByF1064Code;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }
}
