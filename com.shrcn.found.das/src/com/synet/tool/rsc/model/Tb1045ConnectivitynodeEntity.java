package com.synet.tool.rsc.model;


/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1045ConnectivitynodeEntity {
    private String f1045Code;
    private String f1045Name;
    private String f1045Desc;

    public String getF1045Code() {
        return f1045Code;
    }

    public void setF1045Code(String f1045Code) {
        this.f1045Code = f1045Code;
    }

    public String getF1045Name() {
        return f1045Name;
    }

    public void setF1045Name(String f1045Name) {
        this.f1045Name = f1045Name;
    }

    public String getF1045Desc() {
        return f1045Desc;
    }

    public void setF1045Desc(String f1045Desc) {
        this.f1045Desc = f1045Desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1045ConnectivitynodeEntity that = (Tb1045ConnectivitynodeEntity) o;

        if (f1045Code != null ? !f1045Code.equals(that.f1045Code) : that.f1045Code != null) return false;
        if (f1045Desc != null ? !f1045Desc.equals(that.f1045Desc) : that.f1045Desc != null) return false;
        if (f1045Name != null ? !f1045Name.equals(that.f1045Name) : that.f1045Name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1045Code != null ? f1045Code.hashCode() : 0;
        result = 31 * result + (f1045Name != null ? f1045Name.hashCode() : 0);
        result = 31 * result + (f1045Desc != null ? f1045Desc.hashCode() : 0);
        return result;
    }
}
