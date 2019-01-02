package com.synet.tool.rsc.model;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1070MmsserverEntity extends Deletable {
    private String f1070Code;
    private String f1070IpA;
    private String f1070IpB;
    private String f1070IedCrc;
    private String f1070CrcPath;
    private Tb1046IedEntity tb1046IedByF1046Code;

    public String getF1070Code() {
        return f1070Code;
    }

    public void setF1070Code(String f1070Code) {
        this.f1070Code = f1070Code;
    }

    public String getF1070IpA() {
        return f1070IpA;
    }

    public void setF1070IpA(String f1070IpA) {
        this.f1070IpA = f1070IpA;
    }

    public String getF1070IpB() {
        return f1070IpB;
    }

    public void setF1070IpB(String f1070IpB) {
        this.f1070IpB = f1070IpB;
    }

    public String getF1070IedCrc() {
		return f1070IedCrc;
	}

	public void setF1070IedCrc(String f1070IedCrc) {
		this.f1070IedCrc = f1070IedCrc;
	}

	public String getF1070CrcPath() {
		return f1070CrcPath;
	}

	public void setF1070CrcPath(String f1070CrcPath) {
		this.f1070CrcPath = f1070CrcPath;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1070MmsserverEntity that = (Tb1070MmsserverEntity) o;

        if (f1070Code != null ? !f1070Code.equals(that.f1070Code) : that.f1070Code != null) return false;
        if (f1070IpA != null ? !f1070IpA.equals(that.f1070IpA) : that.f1070IpA != null) return false;
        if (f1070IpB != null ? !f1070IpB.equals(that.f1070IpB) : that.f1070IpB != null) return false;
        if (f1070IedCrc != null ? !f1070IedCrc.equals(that.f1070IedCrc) : that.f1070IedCrc != null) return false;
        if (f1070IedCrc != null ? !f1070IedCrc.equals(that.f1070IedCrc) : that.f1070IedCrc != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1070Code != null ? f1070Code.hashCode() : 0;
        result = 31 * result + (f1070IpA != null ? f1070IpA.hashCode() : 0);
        result = 31 * result + (f1070IpB != null ? f1070IpB.hashCode() : 0);
        result = 31 * result + (f1070IedCrc != null ? f1070IedCrc.hashCode() : 0);
        result = 31 * result + (f1070IedCrc != null ? f1070IedCrc.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }
}
