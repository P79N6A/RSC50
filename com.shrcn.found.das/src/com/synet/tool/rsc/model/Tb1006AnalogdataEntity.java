package com.synet.tool.rsc.model;

/**
 * 模拟量
 * Created by chunc on 2018/8/7.
 */
public class Tb1006AnalogdataEntity {
    private String f1006Code;
    private String f1006Desc;
    private String f1006AddRef;
    private Integer f1006Safelevel;
    private String parentCode;
    private int f1011No;
    private String f1006Byname;
    private String f0008Name;
    private String f0009Name;
    private int f1006Calcflag;
    private String f1006Picname;
    private Integer f1006Pdrmode;
    private float f1006K;
    private float f1006B;
    private float f1006Zerodband;
    private float f1006Overflow;
    private float f1006Lowflow;
    private float f1006Maxinc;
    private Float f1006Hiwarn;
    private Float f1006Lowarn;
    private Float f1006Hialarm;
    private Float f1006Loalarm;
    private int f1006Saveperiod;
    private int f1006Plantime;
    private int f1006Deadtime;
    private int f1006Alarmlevel;
    private Integer f1006Savetype;
    private Tb1046IedEntity tb1046IedByF1046Code;
//    private Tb1058MmsfcdaEntity tb1058FcdaByF1058Code;
//    private Tb1061PoutEntity tb1061PoutByF1061Code;
    
    //表格操作使用
    private boolean selected;
    
    public Tb1006AnalogdataEntity() {
	}
    
    public Tb1006AnalogdataEntity(String f1006Byname) {
		this.f1006Byname = f1006Byname;
	}

    public String getF1006Code() {
        return f1006Code;
    }

    public void setF1006Code(String f1006Code) {
        this.f1006Code = f1006Code;
    }

    public String getF1006Desc() {
        return f1006Desc;
    }

    public void setF1006Desc(String f1006Desc) {
        this.f1006Desc = f1006Desc;
    }

	public String getF1006AddRef() {
		return f1006AddRef;
	}

	public void setF1006AddRef(String f1006AddRef) {
		this.f1006AddRef = f1006AddRef;
	}

	public Integer getF1006Safelevel() {
        return f1006Safelevel;
    }

    public void setF1006Safelevel(Integer f1006Safelevel) {
        this.f1006Safelevel = f1006Safelevel;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public int getF1011No() {
        return f1011No;
    }

    public void setF1011No(int f1011No) {
        this.f1011No = f1011No;
    }

    public String getF1006Byname() {
        return f1006Byname;
    }

    public void setF1006Byname(String f1006Byname) {
        this.f1006Byname = f1006Byname;
    }

    public String getF0008Name() {
        return f0008Name;
    }

    public void setF0008Name(String f0008Name) {
        this.f0008Name = f0008Name;
    }

    public String getF0009Name() {
        return f0009Name;
    }

    public void setF0009Name(String f0009Name) {
        this.f0009Name = f0009Name;
    }

    public int getF1006Calcflag() {
        return f1006Calcflag;
    }

    public void setF1006Calcflag(int f1006Calcflag) {
        this.f1006Calcflag = f1006Calcflag;
    }

    public String getF1006Picname() {
        return f1006Picname;
    }

    public void setF1006Picname(String f1006Picname) {
        this.f1006Picname = f1006Picname;
    }

    public Integer getF1006Pdrmode() {
        return f1006Pdrmode;
    }

    public void setF1006Pdrmode(Integer f1006Pdrmode) {
        this.f1006Pdrmode = f1006Pdrmode;
    }

    public float getF1006K() {
        return f1006K;
    }

    public void setF1006K(float f1006K) {
        this.f1006K = f1006K;
    }

    public float getF1006B() {
        return f1006B;
    }

    public void setF1006B(float f1006B) {
        this.f1006B = f1006B;
    }

    public float getF1006Zerodband() {
        return f1006Zerodband;
    }

    public void setF1006Zerodband(float f1006Zerodband) {
        this.f1006Zerodband = f1006Zerodband;
    }

    public float getF1006Overflow() {
        return f1006Overflow;
    }

    public void setF1006Overflow(float f1006Overflow) {
        this.f1006Overflow = f1006Overflow;
    }

    public float getF1006Lowflow() {
        return f1006Lowflow;
    }

    public void setF1006Lowflow(float f1006Lowflow) {
        this.f1006Lowflow = f1006Lowflow;
    }

    public float getF1006Maxinc() {
        return f1006Maxinc;
    }

    public void setF1006Maxinc(float f1006Maxinc) {
        this.f1006Maxinc = f1006Maxinc;
    }

    public Float getF1006Hiwarn() {
        return f1006Hiwarn;
    }

    public void setF1006Hiwarn(Float f1006Hiwarn) {
        this.f1006Hiwarn = f1006Hiwarn;
    }

    public Float getF1006Lowarn() {
        return f1006Lowarn;
    }

    public void setF1006Lowarn(Float f1006Lowarn) {
        this.f1006Lowarn = f1006Lowarn;
    }

    public Float getF1006Hialarm() {
        return f1006Hialarm;
    }

    public void setF1006Hialarm(Float f1006Hialarm) {
        this.f1006Hialarm = f1006Hialarm;
    }

    public Float getF1006Loalarm() {
        return f1006Loalarm;
    }

    public void setF1006Loalarm(Float f1006Loalarm) {
        this.f1006Loalarm = f1006Loalarm;
    }

    public int getF1006Saveperiod() {
        return f1006Saveperiod;
    }

    public void setF1006Saveperiod(int f1006Saveperiod) {
        this.f1006Saveperiod = f1006Saveperiod;
    }

    public int getF1006Plantime() {
        return f1006Plantime;
    }

    public void setF1006Plantime(int f1006Plantime) {
        this.f1006Plantime = f1006Plantime;
    }

    public int getF1006Deadtime() {
        return f1006Deadtime;
    }

    public void setF1006Deadtime(int f1006Deadtime) {
        this.f1006Deadtime = f1006Deadtime;
    }

    public int getF1006Alarmlevel() {
        return f1006Alarmlevel;
    }

    public void setF1006Alarmlevel(int f1006Alarmlevel) {
        this.f1006Alarmlevel = f1006Alarmlevel;
    }

    public Integer getF1006Savetype() {
        return f1006Savetype;
    }

    public void setF1006Savetype(Integer f1006Savetype) {
        this.f1006Savetype = f1006Savetype;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
		return tb1046IedByF1046Code;
	}

	public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
		this.tb1046IedByF1046Code = tb1046IedByF1046Code;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1006AnalogdataEntity that = (Tb1006AnalogdataEntity) o;

        if (f1006Alarmlevel != that.f1006Alarmlevel) return false;
        if (Float.compare(that.f1006B, f1006B) != 0) return false;
        if (f1006Calcflag != that.f1006Calcflag) return false;
        if (f1006Deadtime != that.f1006Deadtime) return false;
        if (Float.compare(that.f1006K, f1006K) != 0) return false;
        if (Float.compare(that.f1006Lowflow, f1006Lowflow) != 0) return false;
        if (Float.compare(that.f1006Maxinc, f1006Maxinc) != 0) return false;
        if (Float.compare(that.f1006Overflow, f1006Overflow) != 0) return false;
        if (f1006Plantime != that.f1006Plantime) return false;
        if (f1006Saveperiod != that.f1006Saveperiod) return false;
        if (Float.compare(that.f1006Zerodband, f1006Zerodband) != 0) return false;
        if (f1011No != that.f1011No) return false;
        if (f0008Name != null ? !f0008Name.equals(that.f0008Name) : that.f0008Name != null) return false;
        if (f0009Name != null ? !f0009Name.equals(that.f0009Name) : that.f0009Name != null) return false;
        if (f1006Byname != null ? !f1006Byname.equals(that.f1006Byname) : that.f1006Byname != null) return false;
        if (f1006Code != null ? !f1006Code.equals(that.f1006Code) : that.f1006Code != null) return false;
        if (f1006Desc != null ? !f1006Desc.equals(that.f1006Desc) : that.f1006Desc != null) return false;
        if (f1006Hialarm != null ? !f1006Hialarm.equals(that.f1006Hialarm) : that.f1006Hialarm != null) return false;
        if (f1006Hiwarn != null ? !f1006Hiwarn.equals(that.f1006Hiwarn) : that.f1006Hiwarn != null) return false;
        if (f1006Loalarm != null ? !f1006Loalarm.equals(that.f1006Loalarm) : that.f1006Loalarm != null) return false;
        if (f1006Lowarn != null ? !f1006Lowarn.equals(that.f1006Lowarn) : that.f1006Lowarn != null) return false;
        if (f1006Pdrmode != null ? !f1006Pdrmode.equals(that.f1006Pdrmode) : that.f1006Pdrmode != null) return false;
        if (f1006Picname != null ? !f1006Picname.equals(that.f1006Picname) : that.f1006Picname != null) return false;
        if (f1006Safelevel != null ? !f1006Safelevel.equals(that.f1006Safelevel) : that.f1006Safelevel != null)
            return false;
        if (f1006Savetype != null ? !f1006Savetype.equals(that.f1006Savetype) : that.f1006Savetype != null)
            return false;
        if (parentCode != null ? !parentCode.equals(that.parentCode) : that.parentCode != null) return false;
        if (tb1046IedByF1046Code != null ? !tb1046IedByF1046Code.equals(that.tb1046IedByF1046Code) : that.tb1046IedByF1046Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1006Code != null ? f1006Code.hashCode() : 0;
        result = 31 * result + (f1006Desc != null ? f1006Desc.hashCode() : 0);
        result = 31 * result + (f1006AddRef != null ? f1006AddRef.hashCode() : 0);
        result = 31 * result + (f1006Safelevel != null ? f1006Safelevel.hashCode() : 0);
        result = 31 * result + (parentCode != null ? parentCode.hashCode() : 0);
        result = 31 * result + f1011No;
        result = 31 * result + (f1006Byname != null ? f1006Byname.hashCode() : 0);
        result = 31 * result + (f0008Name != null ? f0008Name.hashCode() : 0);
        result = 31 * result + (f0009Name != null ? f0009Name.hashCode() : 0);
        result = 31 * result + f1006Calcflag;
        result = 31 * result + (f1006Picname != null ? f1006Picname.hashCode() : 0);
        result = 31 * result + (f1006Pdrmode != null ? f1006Pdrmode.hashCode() : 0);
        result = 31 * result + (f1006K != +0.0f ? Float.floatToIntBits(f1006K) : 0);
        result = 31 * result + (f1006B != +0.0f ? Float.floatToIntBits(f1006B) : 0);
        result = 31 * result + (f1006Zerodband != +0.0f ? Float.floatToIntBits(f1006Zerodband) : 0);
        result = 31 * result + (f1006Overflow != +0.0f ? Float.floatToIntBits(f1006Overflow) : 0);
        result = 31 * result + (f1006Lowflow != +0.0f ? Float.floatToIntBits(f1006Lowflow) : 0);
        result = 31 * result + (f1006Maxinc != +0.0f ? Float.floatToIntBits(f1006Maxinc) : 0);
        result = 31 * result + (f1006Hiwarn != null ? f1006Hiwarn.hashCode() : 0);
        result = 31 * result + (f1006Lowarn != null ? f1006Lowarn.hashCode() : 0);
        result = 31 * result + (f1006Hialarm != null ? f1006Hialarm.hashCode() : 0);
        result = 31 * result + (f1006Loalarm != null ? f1006Loalarm.hashCode() : 0);
        result = 31 * result + f1006Saveperiod;
        result = 31 * result + f1006Plantime;
        result = 31 * result + f1006Deadtime;
        result = 31 * result + f1006Alarmlevel;
        result = 31 * result + (f1006Savetype != null ? f1006Savetype.hashCode() : 0);
        result = 31 * result + (tb1046IedByF1046Code != null ? tb1046IedByF1046Code.hashCode() : 0);
        return result;
    }

//	public Tb1058MmsfcdaEntity getTb1058FcdaByF1058Code() {
//		return tb1058FcdaByF1058Code;
//	}
//
//	public void setTb1058FcdaByF1058Code(Tb1058MmsfcdaEntity tb1058FcdaByF1058Code) {
//		this.tb1058FcdaByF1058Code = tb1058FcdaByF1058Code;
//	}
//
//	public Tb1061PoutEntity getTb1061PoutByF1061Code() {
//		return tb1061PoutByF1061Code;
//	}
//
//	public void setTb1061PoutByF1061Code(Tb1061PoutEntity tb1061PoutByF1061Code) {
//		this.tb1061PoutByF1061Code = tb1061PoutByF1061Code;
//	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
