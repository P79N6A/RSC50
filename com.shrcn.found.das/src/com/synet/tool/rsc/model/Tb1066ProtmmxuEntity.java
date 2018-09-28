package com.synet.tool.rsc.model;

/**
 * 次级-模拟量 关系模型
 * Created by chunc on 2018/8/7.
 */
public class Tb1066ProtmmxuEntity {
    private String f1066Code;
    private Tb1067CtvtsecondaryEntity tb1067CtvtsecondaryByF1067Code;
    private Tb1006AnalogdataEntity f1006Code;
    
    public Tb1066ProtmmxuEntity() {
	}
    
    public Tb1066ProtmmxuEntity(Tb1067CtvtsecondaryEntity tb1067CtvtsecondaryByF1067Code,
    		Tb1006AnalogdataEntity f1006Code) {
		this.tb1067CtvtsecondaryByF1067Code = tb1067CtvtsecondaryByF1067Code;
		this.f1006Code = f1006Code;
	}

    public String getF1066Code() {
        return f1066Code;
    }

    public void setF1066Code(String f1066Code) {
        this.f1066Code = f1066Code;
    }


	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1066ProtmmxuEntity that = (Tb1066ProtmmxuEntity) o;

        if (tb1067CtvtsecondaryByF1067Code != null ? !tb1067CtvtsecondaryByF1067Code.equals(that.tb1067CtvtsecondaryByF1067Code) : that.tb1067CtvtsecondaryByF1067Code != null) return false;
        if (f1006Code != null ? !f1006Code.equals(that.f1006Code) : that.f1006Code != null) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = f1066Code != null ? f1066Code.hashCode() : 0;
        result = 31 * result + (tb1067CtvtsecondaryByF1067Code != null ? tb1067CtvtsecondaryByF1067Code.hashCode() : 0);
        result = 31 * result + (f1006Code != null ? f1006Code.hashCode() : 0);
        return result;
    }

    public Tb1006AnalogdataEntity getF1006Code() {
		return f1006Code;
	}

	public void setF1006Code(Tb1006AnalogdataEntity f1006Code) {
		this.f1006Code = f1006Code;
	}

	public Tb1067CtvtsecondaryEntity getTb1067CtvtsecondaryByF1067Code() {
        return tb1067CtvtsecondaryByF1067Code;
    }

    public void setTb1067CtvtsecondaryByF1067Code(Tb1067CtvtsecondaryEntity tb1067CtvtsecondaryByF1067Code) {
        this.tb1067CtvtsecondaryByF1067Code = tb1067CtvtsecondaryByF1067Code;
    }

}
