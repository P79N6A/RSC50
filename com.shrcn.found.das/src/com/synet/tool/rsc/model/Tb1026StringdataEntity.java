package com.synet.tool.rsc.model;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1026StringdataEntity {
    private String f1026Code;
    private String f1026Desc;
    private Integer f1026Safelevel;
    private String parentCode;
    private int f1011No;
    private String f1026Byname;
    private String f0008Name;
    private String f0009Name;
    private int f1026Calcflag;
    private String f1026Picname;
    private int f1026Ispdr;
    private Integer f1026Pdrno;
    private Integer f1026Issta;

    public String getF1026Code() {
        return f1026Code;
    }

    public void setF1026Code(String f1026Code) {
        this.f1026Code = f1026Code;
    }

    public String getF1026Desc() {
        return f1026Desc;
    }

    public void setF1026Desc(String f1026Desc) {
        this.f1026Desc = f1026Desc;
    }

    public Integer getF1026Safelevel() {
        return f1026Safelevel;
    }

    public void setF1026Safelevel(Integer f1026Safelevel) {
        this.f1026Safelevel = f1026Safelevel;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public int getF1011No() {
        return f1011No;
    }

    public void setF1011No(int f1011No) {
        this.f1011No = f1011No;
    }

    public String getF1026Byname() {
        return f1026Byname;
    }

    public void setF1026Byname(String f1026Byname) {
        this.f1026Byname = f1026Byname;
    }

    public String getF0008Name() {
        return f0008Name;
    }

    public void setF0008Name(String f0008Name) {
        this.f0008Name = f0008Name;
    }

    public String getF0009Name() {
        return f0009Name;
    }

    public void setF0009Name(String f0009Name) {
        this.f0009Name = f0009Name;
    }

    public int getF1026Calcflag() {
        return f1026Calcflag;
    }

    public void setF1026Calcflag(int f1026Calcflag) {
        this.f1026Calcflag = f1026Calcflag;
    }

    public String getF1026Picname() {
        return f1026Picname;
    }

    public void setF1026Picname(String f1026Picname) {
        this.f1026Picname = f1026Picname;
    }

    public int getF1026Ispdr() {
        return f1026Ispdr;
    }

    public void setF1026Ispdr(int f1026Ispdr) {
        this.f1026Ispdr = f1026Ispdr;
    }

    public Integer getF1026Pdrno() {
        return f1026Pdrno;
    }

    public void setF1026Pdrno(Integer f1026Pdrno) {
        this.f1026Pdrno = f1026Pdrno;
    }

    public Integer getF1026Issta() {
        return f1026Issta;
    }

    public void setF1026Issta(Integer f1026Issta) {
        this.f1026Issta = f1026Issta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1026StringdataEntity that = (Tb1026StringdataEntity) o;

        if (f1011No != that.f1011No) return false;
        if (f1026Calcflag != that.f1026Calcflag) return false;
        if (f1026Ispdr != that.f1026Ispdr) return false;
        if (f0008Name != null ? !f0008Name.equals(that.f0008Name) : that.f0008Name != null) return false;
        if (f0009Name != null ? !f0009Name.equals(that.f0009Name) : that.f0009Name != null) return false;
        if (f1026Byname != null ? !f1026Byname.equals(that.f1026Byname) : that.f1026Byname != null) return false;
        if (f1026Code != null ? !f1026Code.equals(that.f1026Code) : that.f1026Code != null) return false;
        if (f1026Desc != null ? !f1026Desc.equals(that.f1026Desc) : that.f1026Desc != null) return false;
        if (f1026Issta != null ? !f1026Issta.equals(that.f1026Issta) : that.f1026Issta != null) return false;
        if (f1026Pdrno != null ? !f1026Pdrno.equals(that.f1026Pdrno) : that.f1026Pdrno != null) return false;
        if (f1026Picname != null ? !f1026Picname.equals(that.f1026Picname) : that.f1026Picname != null) return false;
        if (f1026Safelevel != null ? !f1026Safelevel.equals(that.f1026Safelevel) : that.f1026Safelevel != null)
            return false;
        if (parentCode != null ? !parentCode.equals(that.parentCode) : that.parentCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1026Code != null ? f1026Code.hashCode() : 0;
        result = 31 * result + (f1026Desc != null ? f1026Desc.hashCode() : 0);
        result = 31 * result + (f1026Safelevel != null ? f1026Safelevel.hashCode() : 0);
        result = 31 * result + (parentCode != null ? parentCode.hashCode() : 0);
        result = 31 * result + f1011No;
        result = 31 * result + (f1026Byname != null ? f1026Byname.hashCode() : 0);
        result = 31 * result + (f0008Name != null ? f0008Name.hashCode() : 0);
        result = 31 * result + (f0009Name != null ? f0009Name.hashCode() : 0);
        result = 31 * result + f1026Calcflag;
        result = 31 * result + (f1026Picname != null ? f1026Picname.hashCode() : 0);
        result = 31 * result + f1026Ispdr;
        result = 31 * result + (f1026Pdrno != null ? f1026Pdrno.hashCode() : 0);
        result = 31 * result + (f1026Issta != null ? f1026Issta.hashCode() : 0);
        return result;
    }

}
