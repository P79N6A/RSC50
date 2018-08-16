package com.synet.tool.rsc.model;


/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1050CubicleEntity {
    private String f1050Code;
    private String f1050Name;
    private String f1050Desc;
    private Tb1049RegionEntity tb1049RegionByF1049Code;

    public String getF1050Code() {
        return f1050Code;
    }

    public void setF1050Code(String f1050Code) {
        this.f1050Code = f1050Code;
    }

//    public String getF1049Code() {
//        return f1049Code;
//    }
//
//    public void setF1049Code(String f1049Code) {
//        this.f1049Code = f1049Code;
//    }

    public String getF1050Name() {
        return f1050Name;
    }

    public void setF1050Name(String f1050Name) {
        this.f1050Name = f1050Name;
    }

    public String getF1050Desc() {
        return f1050Desc;
    }

    public void setF1050Desc(String f1050Desc) {
        this.f1050Desc = f1050Desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1050CubicleEntity that = (Tb1050CubicleEntity) o;

        if (f1050Code != null ? !f1050Code.equals(that.f1050Code) : that.f1050Code != null) return false;
        if (f1050Desc != null ? !f1050Desc.equals(that.f1050Desc) : that.f1050Desc != null) return false;
        if (f1050Name != null ? !f1050Name.equals(that.f1050Name) : that.f1050Name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1050Code != null ? f1050Code.hashCode() : 0;
        result = 31 * result + (f1050Name != null ? f1050Name.hashCode() : 0);
        result = 31 * result + (f1050Desc != null ? f1050Desc.hashCode() : 0);
        return result;
    }

    public Tb1049RegionEntity getTb1049RegionByF1049Code() {
        return tb1049RegionByF1049Code;
    }

    public void setTb1049RegionByF1049Code(Tb1049RegionEntity tb1049RegionByF1049Code) {
        this.tb1049RegionByF1049Code = tb1049RegionByF1049Code;
    }
}
