package com.synet.tool.rsc.model;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1093VoltagekkEntity {
    private String f1093Code;
    private String f1067Code;
    private String f1093Desc;
    private String f1093KkNo;
    private Tb1067CtvtsecondaryEntity tb1067CtvtsecondaryByF1067Code;

    public String getF1093Code() {
        return f1093Code;
    }

    public void setF1093Code(String f1093Code) {
        this.f1093Code = f1093Code;
    }

    public String getF1067Code() {
        return f1067Code;
    }

    public void setF1067Code(String f1067Code) {
        this.f1067Code = f1067Code;
    }

    public String getF1093Desc() {
        return f1093Desc;
    }

    public void setF1093Desc(String f1093Desc) {
        this.f1093Desc = f1093Desc;
    }

    public String getF1093KkNo() {
        return f1093KkNo;
    }

    public void setF1093KkNo(String f1093KkNo) {
        this.f1093KkNo = f1093KkNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1093VoltagekkEntity that = (Tb1093VoltagekkEntity) o;

        if (f1067Code != null ? !f1067Code.equals(that.f1067Code) : that.f1067Code != null) return false;
        if (f1093Code != null ? !f1093Code.equals(that.f1093Code) : that.f1093Code != null) return false;
        if (f1093Desc != null ? !f1093Desc.equals(that.f1093Desc) : that.f1093Desc != null) return false;
        if (f1093KkNo != null ? !f1093KkNo.equals(that.f1093KkNo) : that.f1093KkNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1093Code != null ? f1093Code.hashCode() : 0;
        result = 31 * result + (f1067Code != null ? f1067Code.hashCode() : 0);
        result = 31 * result + (f1093Desc != null ? f1093Desc.hashCode() : 0);
        result = 31 * result + (f1093KkNo != null ? f1093KkNo.hashCode() : 0);
        return result;
    }

    public Tb1067CtvtsecondaryEntity getTb1067CtvtsecondaryByF1067Code() {
        return tb1067CtvtsecondaryByF1067Code;
    }

    public void setTb1067CtvtsecondaryByF1067Code(Tb1067CtvtsecondaryEntity tb1067CtvtsecondaryByF1067Code) {
        this.tb1067CtvtsecondaryByF1067Code = tb1067CtvtsecondaryByF1067Code;
    }
}
