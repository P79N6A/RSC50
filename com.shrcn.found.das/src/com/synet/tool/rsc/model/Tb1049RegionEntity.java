package com.synet.tool.rsc.model;

import java.util.List;


/**
 * 区域
 * Created by chunc on 2018/8/7.
 */
public class Tb1049RegionEntity extends Deletable {
    private String f1049Code;
    private String f1049Name;
    private String f1049Desc;
    private Integer f1049Area;
    private Tb1041SubstationEntity tb1041SubstationByF1041Code;
    private List<Tb1050CubicleEntity> tb1050CubiclesByF1049Code;

    public String getF1049Code() {
        return f1049Code;
    }

    public void setF1049Code(String f1049Code) {
        this.f1049Code = f1049Code;
    }

    public String getF1049Name() {
        return f1049Name;
    }

    public void setF1049Name(String f1049Name) {
        this.f1049Name = f1049Name;
    }

    public String getF1049Desc() {
        return f1049Desc;
    }

    public void setF1049Desc(String f1049Desc) {
        this.f1049Desc = f1049Desc;
    }

    public Integer getF1049Area() {
        return f1049Area;
    }

    public void setF1049Area(Integer f1049Area) {
        this.f1049Area = f1049Area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1049RegionEntity that = (Tb1049RegionEntity) o;

        if (f1049Area != null ? !f1049Area.equals(that.f1049Area) : that.f1049Area != null) return false;
        if (f1049Code != null ? !f1049Code.equals(that.f1049Code) : that.f1049Code != null) return false;
        if (f1049Desc != null ? !f1049Desc.equals(that.f1049Desc) : that.f1049Desc != null) return false;
        if (f1049Name != null ? !f1049Name.equals(that.f1049Name) : that.f1049Name != null) return false;
        if (tb1041SubstationByF1041Code != null ? !tb1041SubstationByF1041Code.equals(that.tb1041SubstationByF1041Code) : that.tb1041SubstationByF1041Code != null) return false;
        if (tb1050CubiclesByF1049Code != null ? !tb1050CubiclesByF1049Code.equals(that.tb1050CubiclesByF1049Code) : that.tb1050CubiclesByF1049Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1049Code != null ? f1049Code.hashCode() : 0;
        result = 31 * result + (f1049Name != null ? f1049Name.hashCode() : 0);
        result = 31 * result + (f1049Desc != null ? f1049Desc.hashCode() : 0);
        result = 31 * result + (f1049Area != null ? f1049Area.hashCode() : 0);
        result = 31 * result + (tb1041SubstationByF1041Code != null ? tb1041SubstationByF1041Code.hashCode() : 0);
        result = 31 * result + (tb1050CubiclesByF1049Code != null ? tb1050CubiclesByF1049Code.hashCode() : 0);
        return result;
    }

    public Tb1041SubstationEntity getTb1041SubstationByF1041Code() {
        return tb1041SubstationByF1041Code;
    }

    public void setTb1041SubstationByF1041Code(Tb1041SubstationEntity tb1041SubstationByF1041Code) {
        this.tb1041SubstationByF1041Code = tb1041SubstationByF1041Code;
    }

	public List<Tb1050CubicleEntity> getTb1050CubiclesByF1049Code() {
		return tb1050CubiclesByF1049Code;
	}

	public void setTb1050CubiclesByF1049Code(
			List<Tb1050CubicleEntity> tb1050CubiclesByF1049Code) {
		this.tb1050CubiclesByF1049Code = tb1050CubiclesByF1049Code;
	}

}
