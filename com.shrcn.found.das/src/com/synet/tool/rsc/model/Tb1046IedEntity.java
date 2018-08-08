package com.synet.tool.rsc.model;

import java.util.Collection;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1046IedEntity {
    private String f1046Code;
    private String f1042Code;
    private String f1050Code;
    private String f1046Name;
    private String f1046Desc;
    private String f1046Manufacturor;
    private String f1046Model;
    private String f1046ConfigVersion;
    private Integer f1046AorB;
    private int f1046IsVirtual;
    private Integer f1046Type;
    private String f1046Crc;
    private Collection<Tb1006AnalogdataEntity> tb1006AnalogdatasByF1046Code;
    private Collection<Tb1016StatedataEntity> tb1016StatedatasByF1046Code;
    private Collection<Tb1026StringdataEntity> tb1026StringdatasByF1046Code;
    private Collection<Tb1047BoardEntity> tb1047BoardsByF1046Code;
    private Collection<Tb1054RcbEntity> tb1054RcbsByF1046Code;
    private Collection<Tb1055GcbEntity> tb1055GcbsByF1046Code;
    private Collection<Tb1056SvcbEntity> tb1056SvcbsByF1046Code;
    private Collection<Tb1057SgcbEntity> tb1057SgcbsByF1046Code;
    private Collection<Tb1058MmsfcdaEntity> tb1058MmsfcdasByF1046Code;
    private Collection<Tb1060SpfcdaEntity> tb1060SpfcdasByF1046Code;
    private Collection<Tb1061PoutEntity> tb1061PoutsByF1046Code;
    private Collection<Tb1062PinEntity> tb1062PinsByF1046Code;
    private Collection<Tb1063CircuitEntity> tb1063CircuitsByF1046Code;
    private Collection<Tb1063CircuitEntity> tb1063CircuitsByF1046Code_0;
    private Collection<Tb1064StrapEntity> tb1064StrapsByF1046Code;
    private Collection<Tb1065LogicallinkEntity> tb1065LogicallinksByF1046Code;
    private Collection<Tb1065LogicallinkEntity> tb1065LogicallinksByF1046Code_0;
    private Collection<Tb1066ProtmmxuEntity> tb1066ProtmmxusByF1046Code;
    private Collection<Tb1066ProtmmxuEntity> tb1066ProtmmxusByF1046Code_0;
    private Collection<Tb1068ProtctrlEntity> tb1068ProtctrlsByF1046Code;
    private Collection<Tb1068ProtctrlEntity> tb1068ProtctrlsByF1046Code_0;
    private Collection<Tb1069RcdchannelaEntity> tb1069RcdchannelasByF1046Code;
    private Collection<Tb1070MmsserverEntity> tb1070MmsserversByF1046Code;
    private Collection<Tb1072RcdchanneldEntity> tb1072RcdchanneldsByF1046Code;
    private Collection<Tb1090LineprotfiberEntity> tb1090LineprotfibersByF1046Code;
    private Collection<Tb1091IotermEntity> tb1091IotermsByF1046Code;
    private Collection<Tb1092PowerkkEntity> tb1092PowerkksByF1046Code;

    public String getF1046Code() {
        return f1046Code;
    }

    public void setF1046Code(String f1046Code) {
        this.f1046Code = f1046Code;
    }

    public String getF1042Code() {
        return f1042Code;
    }

    public void setF1042Code(String f1042Code) {
        this.f1042Code = f1042Code;
    }

    public String getF1050Code() {
        return f1050Code;
    }

    public void setF1050Code(String f1050Code) {
        this.f1050Code = f1050Code;
    }

    public String getF1046Name() {
        return f1046Name;
    }

    public void setF1046Name(String f1046Name) {
        this.f1046Name = f1046Name;
    }

    public String getF1046Desc() {
        return f1046Desc;
    }

    public void setF1046Desc(String f1046Desc) {
        this.f1046Desc = f1046Desc;
    }

    public String getF1046Manufacturor() {
        return f1046Manufacturor;
    }

    public void setF1046Manufacturor(String f1046Manufacturor) {
        this.f1046Manufacturor = f1046Manufacturor;
    }

    public String getF1046Model() {
        return f1046Model;
    }

    public void setF1046Model(String f1046Model) {
        this.f1046Model = f1046Model;
    }

    public String getF1046ConfigVersion() {
        return f1046ConfigVersion;
    }

    public void setF1046ConfigVersion(String f1046ConfigVersion) {
        this.f1046ConfigVersion = f1046ConfigVersion;
    }

    public Integer getF1046AorB() {
        return f1046AorB;
    }

    public void setF1046AorB(Integer f1046AorB) {
        this.f1046AorB = f1046AorB;
    }

    public int getF1046IsVirtual() {
        return f1046IsVirtual;
    }

    public void setF1046IsVirtual(int f1046IsVirtual) {
        this.f1046IsVirtual = f1046IsVirtual;
    }

    public Integer getF1046Type() {
        return f1046Type;
    }

    public void setF1046Type(Integer f1046Type) {
        this.f1046Type = f1046Type;
    }

    public String getF1046Crc() {
        return f1046Crc;
    }

    public void setF1046Crc(String f1046Crc) {
        this.f1046Crc = f1046Crc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1046IedEntity that = (Tb1046IedEntity) o;

        if (f1046IsVirtual != that.f1046IsVirtual) return false;
        if (f1042Code != null ? !f1042Code.equals(that.f1042Code) : that.f1042Code != null) return false;
        if (f1046AorB != null ? !f1046AorB.equals(that.f1046AorB) : that.f1046AorB != null) return false;
        if (f1046Code != null ? !f1046Code.equals(that.f1046Code) : that.f1046Code != null) return false;
        if (f1046ConfigVersion != null ? !f1046ConfigVersion.equals(that.f1046ConfigVersion) : that.f1046ConfigVersion != null)
            return false;
        if (f1046Crc != null ? !f1046Crc.equals(that.f1046Crc) : that.f1046Crc != null) return false;
        if (f1046Desc != null ? !f1046Desc.equals(that.f1046Desc) : that.f1046Desc != null) return false;
        if (f1046Manufacturor != null ? !f1046Manufacturor.equals(that.f1046Manufacturor) : that.f1046Manufacturor != null)
            return false;
        if (f1046Model != null ? !f1046Model.equals(that.f1046Model) : that.f1046Model != null) return false;
        if (f1046Name != null ? !f1046Name.equals(that.f1046Name) : that.f1046Name != null) return false;
        if (f1046Type != null ? !f1046Type.equals(that.f1046Type) : that.f1046Type != null) return false;
        if (f1050Code != null ? !f1050Code.equals(that.f1050Code) : that.f1050Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1046Code != null ? f1046Code.hashCode() : 0;
        result = 31 * result + (f1042Code != null ? f1042Code.hashCode() : 0);
        result = 31 * result + (f1050Code != null ? f1050Code.hashCode() : 0);
        result = 31 * result + (f1046Name != null ? f1046Name.hashCode() : 0);
        result = 31 * result + (f1046Desc != null ? f1046Desc.hashCode() : 0);
        result = 31 * result + (f1046Manufacturor != null ? f1046Manufacturor.hashCode() : 0);
        result = 31 * result + (f1046Model != null ? f1046Model.hashCode() : 0);
        result = 31 * result + (f1046ConfigVersion != null ? f1046ConfigVersion.hashCode() : 0);
        result = 31 * result + (f1046AorB != null ? f1046AorB.hashCode() : 0);
        result = 31 * result + f1046IsVirtual;
        result = 31 * result + (f1046Type != null ? f1046Type.hashCode() : 0);
        result = 31 * result + (f1046Crc != null ? f1046Crc.hashCode() : 0);
        return result;
    }

    public Collection<Tb1006AnalogdataEntity> getTb1006AnalogdatasByF1046Code() {
        return tb1006AnalogdatasByF1046Code;
    }

    public void setTb1006AnalogdatasByF1046Code(Collection<Tb1006AnalogdataEntity> tb1006AnalogdatasByF1046Code) {
        this.tb1006AnalogdatasByF1046Code = tb1006AnalogdatasByF1046Code;
    }

    public Collection<Tb1016StatedataEntity> getTb1016StatedatasByF1046Code() {
        return tb1016StatedatasByF1046Code;
    }

    public void setTb1016StatedatasByF1046Code(Collection<Tb1016StatedataEntity> tb1016StatedatasByF1046Code) {
        this.tb1016StatedatasByF1046Code = tb1016StatedatasByF1046Code;
    }

    public Collection<Tb1026StringdataEntity> getTb1026StringdatasByF1046Code() {
        return tb1026StringdatasByF1046Code;
    }

    public void setTb1026StringdatasByF1046Code(Collection<Tb1026StringdataEntity> tb1026StringdatasByF1046Code) {
        this.tb1026StringdatasByF1046Code = tb1026StringdatasByF1046Code;
    }

    public Collection<Tb1047BoardEntity> getTb1047BoardsByF1046Code() {
        return tb1047BoardsByF1046Code;
    }

    public void setTb1047BoardsByF1046Code(Collection<Tb1047BoardEntity> tb1047BoardsByF1046Code) {
        this.tb1047BoardsByF1046Code = tb1047BoardsByF1046Code;
    }

    public Collection<Tb1054RcbEntity> getTb1054RcbsByF1046Code() {
        return tb1054RcbsByF1046Code;
    }

    public void setTb1054RcbsByF1046Code(Collection<Tb1054RcbEntity> tb1054RcbsByF1046Code) {
        this.tb1054RcbsByF1046Code = tb1054RcbsByF1046Code;
    }

    public Collection<Tb1055GcbEntity> getTb1055GcbsByF1046Code() {
        return tb1055GcbsByF1046Code;
    }

    public void setTb1055GcbsByF1046Code(Collection<Tb1055GcbEntity> tb1055GcbsByF1046Code) {
        this.tb1055GcbsByF1046Code = tb1055GcbsByF1046Code;
    }

    public Collection<Tb1056SvcbEntity> getTb1056SvcbsByF1046Code() {
        return tb1056SvcbsByF1046Code;
    }

    public void setTb1056SvcbsByF1046Code(Collection<Tb1056SvcbEntity> tb1056SvcbsByF1046Code) {
        this.tb1056SvcbsByF1046Code = tb1056SvcbsByF1046Code;
    }

    public Collection<Tb1057SgcbEntity> getTb1057SgcbsByF1046Code() {
        return tb1057SgcbsByF1046Code;
    }

    public void setTb1057SgcbsByF1046Code(Collection<Tb1057SgcbEntity> tb1057SgcbsByF1046Code) {
        this.tb1057SgcbsByF1046Code = tb1057SgcbsByF1046Code;
    }

    public Collection<Tb1058MmsfcdaEntity> getTb1058MmsfcdasByF1046Code() {
        return tb1058MmsfcdasByF1046Code;
    }

    public void setTb1058MmsfcdasByF1046Code(Collection<Tb1058MmsfcdaEntity> tb1058MmsfcdasByF1046Code) {
        this.tb1058MmsfcdasByF1046Code = tb1058MmsfcdasByF1046Code;
    }

    public Collection<Tb1060SpfcdaEntity> getTb1060SpfcdasByF1046Code() {
        return tb1060SpfcdasByF1046Code;
    }

    public void setTb1060SpfcdasByF1046Code(Collection<Tb1060SpfcdaEntity> tb1060SpfcdasByF1046Code) {
        this.tb1060SpfcdasByF1046Code = tb1060SpfcdasByF1046Code;
    }

    public Collection<Tb1061PoutEntity> getTb1061PoutsByF1046Code() {
        return tb1061PoutsByF1046Code;
    }

    public void setTb1061PoutsByF1046Code(Collection<Tb1061PoutEntity> tb1061PoutsByF1046Code) {
        this.tb1061PoutsByF1046Code = tb1061PoutsByF1046Code;
    }

    public Collection<Tb1062PinEntity> getTb1062PinsByF1046Code() {
        return tb1062PinsByF1046Code;
    }

    public void setTb1062PinsByF1046Code(Collection<Tb1062PinEntity> tb1062PinsByF1046Code) {
        this.tb1062PinsByF1046Code = tb1062PinsByF1046Code;
    }

    public Collection<Tb1063CircuitEntity> getTb1063CircuitsByF1046Code() {
        return tb1063CircuitsByF1046Code;
    }

    public void setTb1063CircuitsByF1046Code(Collection<Tb1063CircuitEntity> tb1063CircuitsByF1046Code) {
        this.tb1063CircuitsByF1046Code = tb1063CircuitsByF1046Code;
    }

    public Collection<Tb1063CircuitEntity> getTb1063CircuitsByF1046Code_0() {
        return tb1063CircuitsByF1046Code_0;
    }

    public void setTb1063CircuitsByF1046Code_0(Collection<Tb1063CircuitEntity> tb1063CircuitsByF1046Code_0) {
        this.tb1063CircuitsByF1046Code_0 = tb1063CircuitsByF1046Code_0;
    }

    public Collection<Tb1064StrapEntity> getTb1064StrapsByF1046Code() {
        return tb1064StrapsByF1046Code;
    }

    public void setTb1064StrapsByF1046Code(Collection<Tb1064StrapEntity> tb1064StrapsByF1046Code) {
        this.tb1064StrapsByF1046Code = tb1064StrapsByF1046Code;
    }

    public Collection<Tb1065LogicallinkEntity> getTb1065LogicallinksByF1046Code() {
        return tb1065LogicallinksByF1046Code;
    }

    public void setTb1065LogicallinksByF1046Code(Collection<Tb1065LogicallinkEntity> tb1065LogicallinksByF1046Code) {
        this.tb1065LogicallinksByF1046Code = tb1065LogicallinksByF1046Code;
    }

    public Collection<Tb1065LogicallinkEntity> getTb1065LogicallinksByF1046Code_0() {
        return tb1065LogicallinksByF1046Code_0;
    }

    public void setTb1065LogicallinksByF1046Code_0(Collection<Tb1065LogicallinkEntity> tb1065LogicallinksByF1046Code_0) {
        this.tb1065LogicallinksByF1046Code_0 = tb1065LogicallinksByF1046Code_0;
    }

    public Collection<Tb1066ProtmmxuEntity> getTb1066ProtmmxusByF1046Code() {
        return tb1066ProtmmxusByF1046Code;
    }

    public void setTb1066ProtmmxusByF1046Code(Collection<Tb1066ProtmmxuEntity> tb1066ProtmmxusByF1046Code) {
        this.tb1066ProtmmxusByF1046Code = tb1066ProtmmxusByF1046Code;
    }

    public Collection<Tb1066ProtmmxuEntity> getTb1066ProtmmxusByF1046Code_0() {
        return tb1066ProtmmxusByF1046Code_0;
    }

    public void setTb1066ProtmmxusByF1046Code_0(Collection<Tb1066ProtmmxuEntity> tb1066ProtmmxusByF1046Code_0) {
        this.tb1066ProtmmxusByF1046Code_0 = tb1066ProtmmxusByF1046Code_0;
    }

    public Collection<Tb1068ProtctrlEntity> getTb1068ProtctrlsByF1046Code() {
        return tb1068ProtctrlsByF1046Code;
    }

    public void setTb1068ProtctrlsByF1046Code(Collection<Tb1068ProtctrlEntity> tb1068ProtctrlsByF1046Code) {
        this.tb1068ProtctrlsByF1046Code = tb1068ProtctrlsByF1046Code;
    }

    public Collection<Tb1068ProtctrlEntity> getTb1068ProtctrlsByF1046Code_0() {
        return tb1068ProtctrlsByF1046Code_0;
    }

    public void setTb1068ProtctrlsByF1046Code_0(Collection<Tb1068ProtctrlEntity> tb1068ProtctrlsByF1046Code_0) {
        this.tb1068ProtctrlsByF1046Code_0 = tb1068ProtctrlsByF1046Code_0;
    }

    public Collection<Tb1069RcdchannelaEntity> getTb1069RcdchannelasByF1046Code() {
        return tb1069RcdchannelasByF1046Code;
    }

    public void setTb1069RcdchannelasByF1046Code(Collection<Tb1069RcdchannelaEntity> tb1069RcdchannelasByF1046Code) {
        this.tb1069RcdchannelasByF1046Code = tb1069RcdchannelasByF1046Code;
    }

    public Collection<Tb1070MmsserverEntity> getTb1070MmsserversByF1046Code() {
        return tb1070MmsserversByF1046Code;
    }

    public void setTb1070MmsserversByF1046Code(Collection<Tb1070MmsserverEntity> tb1070MmsserversByF1046Code) {
        this.tb1070MmsserversByF1046Code = tb1070MmsserversByF1046Code;
    }

    public Collection<Tb1072RcdchanneldEntity> getTb1072RcdchanneldsByF1046Code() {
        return tb1072RcdchanneldsByF1046Code;
    }

    public void setTb1072RcdchanneldsByF1046Code(Collection<Tb1072RcdchanneldEntity> tb1072RcdchanneldsByF1046Code) {
        this.tb1072RcdchanneldsByF1046Code = tb1072RcdchanneldsByF1046Code;
    }

    public Collection<Tb1090LineprotfiberEntity> getTb1090LineprotfibersByF1046Code() {
        return tb1090LineprotfibersByF1046Code;
    }

    public void setTb1090LineprotfibersByF1046Code(Collection<Tb1090LineprotfiberEntity> tb1090LineprotfibersByF1046Code) {
        this.tb1090LineprotfibersByF1046Code = tb1090LineprotfibersByF1046Code;
    }

    public Collection<Tb1091IotermEntity> getTb1091IotermsByF1046Code() {
        return tb1091IotermsByF1046Code;
    }

    public void setTb1091IotermsByF1046Code(Collection<Tb1091IotermEntity> tb1091IotermsByF1046Code) {
        this.tb1091IotermsByF1046Code = tb1091IotermsByF1046Code;
    }

    public Collection<Tb1092PowerkkEntity> getTb1092PowerkksByF1046Code() {
        return tb1092PowerkksByF1046Code;
    }

    public void setTb1092PowerkksByF1046Code(Collection<Tb1092PowerkkEntity> tb1092PowerkksByF1046Code) {
        this.tb1092PowerkksByF1046Code = tb1092PowerkksByF1046Code;
    }
}
