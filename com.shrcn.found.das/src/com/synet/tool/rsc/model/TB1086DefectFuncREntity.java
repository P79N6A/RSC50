package com.synet.tool.rsc.model;


public class TB1086DefectFuncREntity {
	private String f1086CODE;
//	private String f1086OBJSTCODE;
//	private String f1086OBJMXCODE;
	private int f1086DefectType;
	private int f1086SubType;
	private int f1086DefectLevel;
	private TB1085ProtFuncEntity tb1085ByF1085CODE;
	private Tb1006AnalogdataEntity tb1006ByMXCODE;
	private Tb1016StatedataEntity tb1006BySTCODE;
	
	public String getF1086CODE() {
		return f1086CODE;
	}
	public void setF1086CODE(String f1086code) {
		f1086CODE = f1086code;
	}
	public String getF1086OBJCODE() {
		if (tb1006ByMXCODE != null) {
			return tb1006ByMXCODE.getF1006Code();
		} else if (tb1006BySTCODE != null) {
			return tb1006BySTCODE.getF1016Code();
		} else {
			return null;
		}
	}
//	
//	public Object getF1086OBJ() {
//		if (f1086OBJCODE!=null) {
//			BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
//			if (f1086OBJCODE.startsWith("State")) {
//				return beanDao.getById(Tb1016StatedataEntity.class, f1086OBJCODE);
//			} else if (f1086OBJCODE.startsWith("Analog")) {
//				return beanDao.getById(Tb1006AnalogdataEntity.class, f1086OBJCODE);
//			}
//		}
//		return null;
//	}
	public String getF1086OBJRef() {
//		Object obj = getF1086OBJ();
//		if (obj != null) {
//			if (obj instanceof Tb1016StatedataEntity) {
//				return ((Tb1016StatedataEntity)obj).getF1016AddRef();
//			} else if (obj instanceof Tb1006AnalogdataEntity) {
//				return ((Tb1006AnalogdataEntity)obj).getF1006AddRef();
//			}
//		}
//		return null;
		if (tb1006ByMXCODE != null) {
			return tb1006ByMXCODE.getF1006AddRef();
		} else if (tb1006BySTCODE != null) {
			return tb1006BySTCODE.getF1016AddRef();
		} else {
			return null;
		}
	}
	public String getF1086OBJDesc() {
//		Object obj = getF1086OBJ();
//		if (obj != null) {
//			if (obj instanceof Tb1016StatedataEntity) {
//				return ((Tb1016StatedataEntity)obj).getF1016Desc();
//			} else if (obj instanceof Tb1006AnalogdataEntity) {
//				return ((Tb1006AnalogdataEntity)obj).getF1006Desc();
//			}
//		}
//		return null;
		if (tb1006ByMXCODE != null) {
			return tb1006ByMXCODE.getF1006Desc();
		} else if (tb1006BySTCODE != null) {
			return tb1006BySTCODE.getF1016Desc();
		} else {
			return null;
		}
	}
//	public void setF1086OBJCODE(String f1086objcode) {
//		f1086OBJCODE = f1086objcode;
//	}
	public int getF1086DefectType() {
		return f1086DefectType;
	}
	public void setF1086DefectType(int f1086DefectType) {
		this.f1086DefectType = f1086DefectType;
	}
	public int getF1086SubType() {
		return f1086SubType;
	}
	public void setF1086SubType(int f1086SubType) {
		this.f1086SubType = f1086SubType;
	}
	public int getF1086DefectLevel() {
		return f1086DefectLevel;
	}
	public void setF1086DefectLevel(int f1086DefectLevel) {
		this.f1086DefectLevel = f1086DefectLevel;
	}
	public TB1085ProtFuncEntity getTb1085ByF1085CODE() {
		return tb1085ByF1085CODE;
	}
	public void setTb1085ByF1085CODE(TB1085ProtFuncEntity tb1085ByF1085CODE) {
		this.tb1085ByF1085CODE = tb1085ByF1085CODE;
	}
	public Tb1006AnalogdataEntity getTb1006ByMXCODE() {
		return tb1006ByMXCODE;
	}
	public void setTb1006ByMXCODE(Tb1006AnalogdataEntity tb1006ByMXCODE) {
		this.tb1006ByMXCODE = tb1006ByMXCODE;
	}
	public Tb1016StatedataEntity getTb1006BySTCODE() {
		return tb1006BySTCODE;
	}
	public void setTb1006BySTCODE(Tb1016StatedataEntity tb1006BySTCODE) {
		this.tb1006BySTCODE = tb1006BySTCODE;
	}
	
}
