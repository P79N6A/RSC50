package com.synet.tool.rsc.model;

/**
 * 互感器次级-输出虚端子 关系类
 * Created by chunc on 2018/8/7.
 */
public class Tb1074SVCTVTRelationEntity extends Deletable {
    private String f1074Code;
    private Tb1067CtvtsecondaryEntity tb1067CtvtsecondaryByF1067Code;
    private Tb1061PoutEntity f1061Code;
    
    public Tb1074SVCTVTRelationEntity() {
	}
    
    public Tb1074SVCTVTRelationEntity(Tb1067CtvtsecondaryEntity tb1067CtvtsecondaryByF1067Code, Tb1061PoutEntity f1061Code) {
		this.tb1067CtvtsecondaryByF1067Code = tb1067CtvtsecondaryByF1067Code;
		this.f1061Code = f1061Code;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1074SVCTVTRelationEntity that = (Tb1074SVCTVTRelationEntity) o;

        if (tb1067CtvtsecondaryByF1067Code != null ? !tb1067CtvtsecondaryByF1067Code.equals(that.tb1067CtvtsecondaryByF1067Code) : that.tb1067CtvtsecondaryByF1067Code != null) return false;
        if (f1061Code != null ? !f1061Code.equals(that.f1061Code) : that.f1061Code != null) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = f1074Code != null ? f1074Code.hashCode() : 0;
        result = 31 * result + (tb1067CtvtsecondaryByF1067Code != null ? tb1067CtvtsecondaryByF1067Code.hashCode() : 0);
        result = 31 * result + (f1061Code != null ? f1061Code.hashCode() : 0);
        return result;
    }

	public Tb1067CtvtsecondaryEntity getTb1067CtvtsecondaryByF1067Code() {
        return tb1067CtvtsecondaryByF1067Code;
    }

    public void setTb1067CtvtsecondaryByF1067Code(Tb1067CtvtsecondaryEntity tb1067CtvtsecondaryByF1067Code) {
        this.tb1067CtvtsecondaryByF1067Code = tb1067CtvtsecondaryByF1067Code;
    }

	public String getF1074Code() {
		return f1074Code;
	}

	public void setF1074Code(String f1074Code) {
		this.f1074Code = f1074Code;
	}

	public Tb1061PoutEntity getF1061Code() {
		return f1061Code;
	}

	public void setF1061Code(Tb1061PoutEntity f1061Code) {
		this.f1061Code = f1061Code;
	}

}
