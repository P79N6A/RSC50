package com.synet.tool.rsc.model;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1092PowerkkEntity extends Deletable {
    private String f1092Code;
    private String f1046Code;
    private String f1092Desc;
    private String f1092KkNo;
    private Tb1046IedEntity tb1046IedByF1046Code;

    public String getF1092Code() {
        return f1092Code;
    }

    public void setF1092Code(String f1092Code) {
        this.f1092Code = f1092Code;
    }

    public String getF1046Code() {
        return f1046Code;
    }

    public void setF1046Code(String f1046Code) {
        this.f1046Code = f1046Code;
    }

    public String getF1092Desc() {
        return f1092Desc;
    }

    public void setF1092Desc(String f1092Desc) {
        this.f1092Desc = f1092Desc;
    }

    public String getF1092KkNo() {
        return f1092KkNo;
    }

    public void setF1092KkNo(String f1092KkNo) {
        this.f1092KkNo = f1092KkNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1092PowerkkEntity that = (Tb1092PowerkkEntity) o;

        if (f1046Code != null ? !f1046Code.equals(that.f1046Code) : that.f1046Code != null) return false;
        if (f1092Code != null ? !f1092Code.equals(that.f1092Code) : that.f1092Code != null) return false;
        if (f1092Desc != null ? !f1092Desc.equals(that.f1092Desc) : that.f1092Desc != null) return false;
        if (f1092KkNo != null ? !f1092KkNo.equals(that.f1092KkNo) : that.f1092KkNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1092Code != null ? f1092Code.hashCode() : 0;
        result = 31 * result + (f1046Code != null ? f1046Code.hashCode() : 0);
        result = 31 * result + (f1092Desc != null ? f1092Desc.hashCode() : 0);
        result = 31 * result + (f1092KkNo != null ? f1092KkNo.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }
}
