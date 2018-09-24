package com.synet.tool.rsc.model;


/**
 * 互感器次级
 * Created by chunc on 2018/8/7.
 */
public class Tb1067CtvtsecondaryEntity {
    private String f1067Code;
    private String f1067Name;
    private Integer f1067Index;
    private String f1067Type;
    private String f1067TermNo;
    private String f1067CircNo;
    private String f1067Desc;
    private Tb1043EquipmentEntity tb1043EquipmentByF1043Code;

    public String getF1067Code() {
        return f1067Code;
    }

    public void setF1067Code(String f1067Code) {
        this.f1067Code = f1067Code;
    }

    public Integer getF1067Index() {
        return f1067Index;
    }

    public void setF1067Index(Integer f1067Index) {
        this.f1067Index = f1067Index;
    }

    public String getF1067Type() {
        return f1067Type;
    }

    public void setF1067Type(String f1067Type) {
        this.f1067Type = f1067Type;
    }

	public String getF1067CircNo() {
		return f1067CircNo;
	}

	public void setF1067CircNo(String f1067CircNo) {
		this.f1067CircNo = f1067CircNo;
	}

	public String getF1067Desc() {
		return f1067Desc;
	}

	public void setF1067Desc(String f1067Desc) {
		this.f1067Desc = f1067Desc;
	}

	public String getF1067Name() {
		return f1067Name;
	}

	public void setF1067Name(String f1067Name) {
		this.f1067Name = f1067Name;
	}

	public String getF1067TermNo() {
		return f1067TermNo;
	}

	public void setF1067TermNo(String f1067TermNo) {
		this.f1067TermNo = f1067TermNo;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1067CtvtsecondaryEntity that = (Tb1067CtvtsecondaryEntity) o;

        if (f1067CircNo != null ? !f1067CircNo.equals(that.f1067CircNo) : that.f1067CircNo != null) return false;
        if (f1067Desc != null ? !f1067Desc.equals(that.f1067Desc) : that.f1067Desc != null) return false;
        if (f1067Code != null ? !f1067Code.equals(that.f1067Code) : that.f1067Code != null) return false;
        if (f1067Index != null ? !f1067Index.equals(that.f1067Index) : that.f1067Index != null) return false;
        if (f1067Name != null ? !f1067Name.equals(that.f1067Name) : that.f1067Name != null) return false;
        if (f1067Type != null ? !f1067Type.equals(that.f1067Type) : that.f1067Type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1067Code != null ? f1067Code.hashCode() : 0;
        result = 31 * result + (f1067Index != null ? f1067Index.hashCode() : 0);
        result = 31 * result + (f1067Name != null ? f1067Name.hashCode() : 0);
        result = 31 * result + (f1067Type != null ? f1067Type.hashCode() : 0);
        result = 31 * result + (f1067CircNo != null ? f1067CircNo.hashCode() : 0);
        result = 31 * result + (f1067Desc != null ? f1067Desc.hashCode() : 0);
        return result;
    }

    public Tb1043EquipmentEntity getTb1043EquipmentByF1043Code() {
        return tb1043EquipmentByF1043Code;
    }

    public void setTb1043EquipmentByF1043Code(Tb1043EquipmentEntity tb1043EquipmentByF1043Code) {
        this.tb1043EquipmentByF1043Code = tb1043EquipmentByF1043Code;
    }

}
