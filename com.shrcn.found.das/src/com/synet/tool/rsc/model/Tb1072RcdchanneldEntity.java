package com.synet.tool.rsc.model;

/**
 * 保护录波状态量通道
 * Created by chunc on 2018/8/7.
 */
public class Tb1072RcdchanneldEntity {
    private String f1072Code;
    private String iedCode;
    private String f1072Index;
    private int f1072Type;
    private String f1061Code;
    private String f1058Code;
    private String f1072Phase;
    private Tb1046IedEntity tb1046IedByIedCode;

    public String getF1072Code() {
        return f1072Code;
    }

    public void setF1072Code(String f1072Code) {
        this.f1072Code = f1072Code;
    }

    public String getIedCode() {
        return iedCode;
    }

    public void setIedCode(String iedCode) {
        this.iedCode = iedCode;
    }

    public String getF1072Index() {
        return f1072Index;
    }

    public void setF1072Index(String f1072Index) {
        this.f1072Index = f1072Index;
    }

    public int getF1072Type() {
        return f1072Type;
    }

    public void setF1072Type(int f1072Type) {
        this.f1072Type = f1072Type;
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

        Tb1072RcdchanneldEntity that = (Tb1072RcdchanneldEntity) o;

        if (f1072Type != that.f1072Type) return false;
        if (f1058Code != null ? !f1058Code.equals(that.f1058Code) : that.f1058Code != null) return false;
        if (f1061Code != null ? !f1061Code.equals(that.f1061Code) : that.f1061Code != null) return false;
        if (f1072Code != null ? !f1072Code.equals(that.f1072Code) : that.f1072Code != null) return false;
        if (f1072Index != null ? !f1072Index.equals(that.f1072Index) : that.f1072Index != null) return false;
        if (iedCode != null ? !iedCode.equals(that.iedCode) : that.iedCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1072Code != null ? f1072Code.hashCode() : 0;
        result = 31 * result + (iedCode != null ? iedCode.hashCode() : 0);
        result = 31 * result + (f1072Index != null ? f1072Index.hashCode() : 0);
        result = 31 * result + f1072Type;
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

	public String getF1072Phase() {
		return f1072Phase;
	}

	public void setF1072Phase(String f1072Phase) {
		this.f1072Phase = f1072Phase;
	}
}
