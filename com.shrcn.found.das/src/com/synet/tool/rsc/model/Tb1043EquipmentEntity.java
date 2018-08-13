package com.synet.tool.rsc.model;


/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1043EquipmentEntity {
    private String f1043Code;
    private String f1042Code;
    private String f1043Name;
    private String f1043Desc;
    private int f1043IsVirtual;
    private int f1043Type;
    private Tb1042BayEntity tb1042BayByF1042Code;
    
    private Tb1016StatedataEntity statedataEntity;

    public String getF1043Code() {
        return f1043Code;
    }

    public void setF1043Code(String f1043Code) {
        this.f1043Code = f1043Code;
    }

    public String getF1042Code() {
        return f1042Code;
    }

    public void setF1042Code(String f1042Code) {
        this.f1042Code = f1042Code;
    }

    public String getF1043Name() {
        return f1043Name;
    }

    public void setF1043Name(String f1043Name) {
        this.f1043Name = f1043Name;
    }

    public String getF1043Desc() {
        return f1043Desc;
    }

    public void setF1043Desc(String f1043Desc) {
        this.f1043Desc = f1043Desc;
    }

    public int getF1043IsVirtual() {
        return f1043IsVirtual;
    }

    public void setF1043IsVirtual(int f1043IsVirtual) {
        this.f1043IsVirtual = f1043IsVirtual;
    }

    public int getF1043Type() {
        return f1043Type;
    }

    public void setF1043Type(int f1043Type) {
        this.f1043Type = f1043Type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1043EquipmentEntity that = (Tb1043EquipmentEntity) o;

        if (f1043IsVirtual != that.f1043IsVirtual) return false;
        if (f1043Type != that.f1043Type) return false;
        if (f1042Code != null ? !f1042Code.equals(that.f1042Code) : that.f1042Code != null) return false;
        if (f1043Code != null ? !f1043Code.equals(that.f1043Code) : that.f1043Code != null) return false;
        if (f1043Desc != null ? !f1043Desc.equals(that.f1043Desc) : that.f1043Desc != null) return false;
        if (f1043Name != null ? !f1043Name.equals(that.f1043Name) : that.f1043Name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1043Code != null ? f1043Code.hashCode() : 0;
        result = 31 * result + (f1042Code != null ? f1042Code.hashCode() : 0);
        result = 31 * result + (f1043Name != null ? f1043Name.hashCode() : 0);
        result = 31 * result + (f1043Desc != null ? f1043Desc.hashCode() : 0);
        result = 31 * result + f1043IsVirtual;
        result = 31 * result + f1043Type;
        return result;
    }

    public Tb1042BayEntity getTb1042BayByF1042Code() {
        return tb1042BayByF1042Code;
    }

    public void setTb1042BayByF1042Code(Tb1042BayEntity tb1042BayByF1042Code) {
        this.tb1042BayByF1042Code = tb1042BayByF1042Code;
    }

	public Tb1016StatedataEntity getStatedataEntity() {
		return statedataEntity;
	}

	public void setStatedataEntity(Tb1016StatedataEntity statedataEntity) {
		this.statedataEntity = statedataEntity;
	}

}
