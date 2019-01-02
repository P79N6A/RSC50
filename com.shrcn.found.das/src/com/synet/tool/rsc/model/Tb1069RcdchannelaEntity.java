package com.synet.tool.rsc.model;

/**
 * 保护录波模拟量通道
 * Created by chunc on 2018/8/7.
 */
public class Tb1069RcdchannelaEntity extends Deletable {
    private String f1069Code;
    private String iedCode;
    private String f1069Index;
    private int f1069Type;
    private int f1069Phase;
    private String f1043Code;
    private String f1061Code;
    private String f1058Code;
    private Tb1046IedEntity tb1046IedByIedCode;

    public String getF1069Code() {
        return f1069Code;
    }

    public void setF1069Code(String f1069Code) {
        this.f1069Code = f1069Code;
    }

    public String getIedCode() {
        return iedCode;
    }

    public void setIedCode(String iedCode) {
        this.iedCode = iedCode;
    }

    public String getF1069Index() {
        return f1069Index;
    }

    public void setF1069Index(String f1069Index) {
        this.f1069Index = f1069Index;
    }

    public int getF1069Type() {
        return f1069Type;
    }

    public void setF1069Type(int f1069Type) {
        this.f1069Type = f1069Type;
    }

    public int getF1069Phase() {
        return f1069Phase;
    }

    public void setF1069Phase(int f1069Phase) {
        this.f1069Phase = f1069Phase;
    }

    public String getF1043Code() {
        return f1043Code;
    }

    public void setF1043Code(String f1043Code) {
        this.f1043Code = f1043Code;
    }

    public String getF1061Code() {
        return f1061Code;
    }

    public void setF1061Code(String f1061Code) {
        this.f1061Code = f1061Code;
    }

    public String getF1058Code() {
        return f1058Code;
    }

    public void setF1058Code(String f1058Code) {
        this.f1058Code = f1058Code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1069RcdchannelaEntity that = (Tb1069RcdchannelaEntity) o;

        if (f1069Phase != that.f1069Phase) return false;
        if (f1069Type != that.f1069Type) return false;
        if (f1043Code != null ? !f1043Code.equals(that.f1043Code) : that.f1043Code != null) return false;
        if (f1058Code != null ? !f1058Code.equals(that.f1058Code) : that.f1058Code != null) return false;
        if (f1061Code != null ? !f1061Code.equals(that.f1061Code) : that.f1061Code != null) return false;
        if (f1069Code != null ? !f1069Code.equals(that.f1069Code) : that.f1069Code != null) return false;
        if (f1069Index != null ? !f1069Index.equals(that.f1069Index) : that.f1069Index != null) return false;
        if (iedCode != null ? !iedCode.equals(that.iedCode) : that.iedCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1069Code != null ? f1069Code.hashCode() : 0;
        result = 31 * result + (iedCode != null ? iedCode.hashCode() : 0);
        result = 31 * result + (f1069Index != null ? f1069Index.hashCode() : 0);
        result = 31 * result + f1069Type;
        result = 31 * result + f1069Phase;
        result = 31 * result + (f1043Code != null ? f1043Code.hashCode() : 0);
        result = 31 * result + (f1061Code != null ? f1061Code.hashCode() : 0);
        result = 31 * result + (f1058Code != null ? f1058Code.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByIedCode() {
        return tb1046IedByIedCode;
    }

    public void setTb1046IedByIedCode(Tb1046IedEntity tb1046IedByIedCode) {
        this.tb1046IedByIedCode = tb1046IedByIedCode;
    }
}
