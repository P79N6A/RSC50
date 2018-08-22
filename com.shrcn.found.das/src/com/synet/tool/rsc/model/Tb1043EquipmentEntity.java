package com.synet.tool.rsc.model;

import java.util.Set;


/**
 * 互感器
 * Created by chunc on 2018/8/7.
 */
public class Tb1043EquipmentEntity {
    private String f1043Code;
    private String f1043Name;
    private String f1043Desc;
    private String f1016Code;
    private int f1043IsVirtual;
    private int f1043Type;
    private Tb1016StatedataEntity tb1016StatedataEntity;
    private Tb1042BayEntity tb1042BayByF1042Code;
    private Set<Tb1044TerminalEntity> tb1044TerminalsByF1043Code;
    private Set<Tb1067CtvtsecondaryEntity> tb1067SecondarysByF1043Code;

    public String getF1043Code() {
        return f1043Code;
    }

    public void setF1043Code(String f1043Code) {
        this.f1043Code = f1043Code;
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
        if (f1043Code != null ? !f1043Code.equals(that.f1043Code) : that.f1043Code != null) return false;
        if (f1043Desc != null ? !f1043Desc.equals(that.f1043Desc) : that.f1043Desc != null) return false;
        if (f1043Name != null ? !f1043Name.equals(that.f1043Name) : that.f1043Name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1043Code != null ? f1043Code.hashCode() : 0;
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

	public Set<Tb1044TerminalEntity> getTb1044TerminalsByF1043Code() {
		return tb1044TerminalsByF1043Code;
	}

	public void setTb1044TerminalsByF1043Code(
			Set<Tb1044TerminalEntity> tb1044TerminalsByF1043Code) {
		this.tb1044TerminalsByF1043Code = tb1044TerminalsByF1043Code;
	}

	public Set<Tb1067CtvtsecondaryEntity> getTb1067SecondarysByF1043Code() {
		return tb1067SecondarysByF1043Code;
	}

	public void setTb1067SecondarysByF1043Code(
			Set<Tb1067CtvtsecondaryEntity> tb1067SecondarysByF1043Code) {
		this.tb1067SecondarysByF1043Code = tb1067SecondarysByF1043Code;
	}

	public Tb1016StatedataEntity getTb1016StatedataEntity() {
		return tb1016StatedataEntity;
	}

	public void setTb1016StatedataEntity(Tb1016StatedataEntity tb1016StatedataEntity) {
		this.tb1016StatedataEntity = tb1016StatedataEntity;
	}

	public String getF1016Code() {
		return f1016Code;
	}

	public void setF1016Code(String f1016Code) {
		this.f1016Code = f1016Code;
	}
}
