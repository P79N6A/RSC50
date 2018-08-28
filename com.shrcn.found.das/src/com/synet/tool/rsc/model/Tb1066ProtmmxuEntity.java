package com.synet.tool.rsc.model;

/**
 * 保护采样
 * Created by chunc on 2018/8/7.
 */
public class Tb1066ProtmmxuEntity {
    private String f1066Code;
    private String f1067Code;
    private int f1066Type;
    private Tb1067CtvtsecondaryEntity tb1067CtvtsecondaryByF1067Code;
    private Tb1046IedEntity tb1046IedByF1046CodeProt;
    private Tb1006AnalogdataEntity f1006CodeA;
    private Tb1006AnalogdataEntity f1006CodeB;
    private Tb1006AnalogdataEntity f1006CodeC;

    public String getF1066Code() {
        return f1066Code;
    }

    public void setF1066Code(String f1066Code) {
        this.f1066Code = f1066Code;
    }

    public String getF1067Code() {
        return f1067Code;
    }

    public void setF1067Code(String f1067Code) {
        this.f1067Code = f1067Code;
    }

    public int getF1066Type() {
        return f1066Type;
    }

    public void setF1066Type(int f1066Type) {
        this.f1066Type = f1066Type;
    }

    public Tb1006AnalogdataEntity getF1006CodeA() {
		return f1006CodeA;
	}

	public void setF1006CodeA(Tb1006AnalogdataEntity f1006CodeA) {
		this.f1006CodeA = f1006CodeA;
	}

	public Tb1006AnalogdataEntity getF1006CodeB() {
		return f1006CodeB;
	}

	public void setF1006CodeB(Tb1006AnalogdataEntity f1006CodeB) {
		this.f1006CodeB = f1006CodeB;
	}

	public Tb1006AnalogdataEntity getF1006CodeC() {
		return f1006CodeC;
	}

	public void setF1006CodeC(Tb1006AnalogdataEntity f1006CodeC) {
		this.f1006CodeC = f1006CodeC;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1066ProtmmxuEntity that = (Tb1066ProtmmxuEntity) o;

        if (f1066Type != that.f1066Type) return false;
        if (f1006CodeA != null ? !f1006CodeA.equals(that.f1006CodeA) : that.f1006CodeA != null) return false;
        if (f1006CodeB != null ? !f1006CodeB.equals(that.f1006CodeB) : that.f1006CodeB != null) return false;
        if (f1006CodeC != null ? !f1006CodeC.equals(that.f1006CodeC) : that.f1006CodeC != null) return false;
        if (f1066Code != null ? !f1066Code.equals(that.f1066Code) : that.f1066Code != null) return false;
        if (f1067Code != null ? !f1067Code.equals(that.f1067Code) : that.f1067Code != null) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = f1066Code != null ? f1066Code.hashCode() : 0;
        result = 31 * result + (f1067Code != null ? f1067Code.hashCode() : 0);
        result = 31 * result + f1066Type;
        result = 31 * result + (f1006CodeA != null ? f1006CodeA.hashCode() : 0);
        result = 31 * result + (f1006CodeB != null ? f1006CodeB.hashCode() : 0);
        result = 31 * result + (f1006CodeC != null ? f1006CodeC.hashCode() : 0);
        return result;
    }

    public Tb1067CtvtsecondaryEntity getTb1067CtvtsecondaryByF1067Code() {
        return tb1067CtvtsecondaryByF1067Code;
    }

    public void setTb1067CtvtsecondaryByF1067Code(Tb1067CtvtsecondaryEntity tb1067CtvtsecondaryByF1067Code) {
        this.tb1067CtvtsecondaryByF1067Code = tb1067CtvtsecondaryByF1067Code;
    }

    public Tb1046IedEntity getTb1046IedByF1046CodeProt() {
        return tb1046IedByF1046CodeProt;
    }

    public void setTb1046IedByF1046CodeProt(Tb1046IedEntity tb1046IedByF1046CodeProt) {
        this.tb1046IedByF1046CodeProt = tb1046IedByF1046CodeProt;
    }
}
