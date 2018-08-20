package com.synet.tool.rsc.model;


/**
 * 屏柜
 * Created by chunc on 2018/8/7.
 */
public class Tb1050CubicleEntity {
    private String f1050Code;
//    private String f1049Code;
    private String f1050Name;
    private String f1050Desc;
    private Tb1049RegionEntity tb1049RegionByF1049Code;
//    private Collection<Tb1051CableEntity> tb1051CablesByF1050Code;
//    private Collection<Tb1051CableEntity> tb1051CablesByF1050Code_0;
//    private Collection<Tb1052CoreEntity> tb1052CoresByF1050Code;

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

//        if (f1049Code != null ? !f1049Code.equals(that.f1049Code) : that.f1049Code != null) return false;
        if (f1050Code != null ? !f1050Code.equals(that.f1050Code) : that.f1050Code != null) return false;
        if (f1050Desc != null ? !f1050Desc.equals(that.f1050Desc) : that.f1050Desc != null) return false;
        if (f1050Name != null ? !f1050Name.equals(that.f1050Name) : that.f1050Name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1050Code != null ? f1050Code.hashCode() : 0;
//        result = 31 * result + (f1049Code != null ? f1049Code.hashCode() : 0);
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

//    public Collection<Tb1051CableEntity> getTb1051CablesByF1050Code() {
//        return tb1051CablesByF1050Code;
//    }
//
//    public void setTb1051CablesByF1050Code(Collection<Tb1051CableEntity> tb1051CablesByF1050Code) {
//        this.tb1051CablesByF1050Code = tb1051CablesByF1050Code;
//    }
//
//    public Collection<Tb1051CableEntity> getTb1051CablesByF1050Code_0() {
//        return tb1051CablesByF1050Code_0;
//    }
//
//    public void setTb1051CablesByF1050Code_0(Collection<Tb1051CableEntity> tb1051CablesByF1050Code_0) {
//        this.tb1051CablesByF1050Code_0 = tb1051CablesByF1050Code_0;
//    }
//
//    public Collection<Tb1052CoreEntity> getTb1052CoresByF1050Code() {
//        return tb1052CoresByF1050Code;
//    }
//
//    public void setTb1052CoresByF1050Code(Collection<Tb1052CoreEntity> tb1052CoresByF1050Code) {
//        this.tb1052CoresByF1050Code = tb1052CoresByF1050Code;
//    }
}
