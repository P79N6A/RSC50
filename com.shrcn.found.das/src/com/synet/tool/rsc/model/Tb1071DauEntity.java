package com.synet.tool.rsc.model;

/**
 * 前置采集单元
 * Created by chunc on 2018/8/7.
 */
public class Tb1071DauEntity {
    private String f1071Code;
    private String f1071Desc;
    private String f1071IpAddr;

    public String getF1071Code() {
        return f1071Code;
    }

    public void setF1071Code(String f1071Code) {
        this.f1071Code = f1071Code;
    }

    public String getF1071Desc() {
        return f1071Desc;
    }

    public void setF1071Desc(String f1071Desc) {
        this.f1071Desc = f1071Desc;
    }

    public String getF1071IpAddr() {
        return f1071IpAddr;
    }

    public void setF1071IpAddr(String f1071IpAddr) {
        this.f1071IpAddr = f1071IpAddr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1071DauEntity that = (Tb1071DauEntity) o;

        if (f1071Code != null ? !f1071Code.equals(that.f1071Code) : that.f1071Code != null) return false;
        if (f1071Desc != null ? !f1071Desc.equals(that.f1071Desc) : that.f1071Desc != null) return false;
        if (f1071IpAddr != null ? !f1071IpAddr.equals(that.f1071IpAddr) : that.f1071IpAddr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1071Code != null ? f1071Code.hashCode() : 0;
        result = 31 * result + (f1071Desc != null ? f1071Desc.hashCode() : 0);
        result = 31 * result + (f1071IpAddr != null ? f1071IpAddr.hashCode() : 0);
        return result;
    }
}
