package com.synet.tool.rsc.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1022FaultconfigEntity;
import com.synet.tool.rsc.model.Tb1026StringdataEntity;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1044TerminalEntity;
import com.synet.tool.rsc.model.Tb1045ConnectivitynodeEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1052CoreEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1054RcbEntity;
import com.synet.tool.rsc.model.Tb1055GcbEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1057SgcbEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1059SgfcdaEntity;
import com.synet.tool.rsc.model.Tb1060SpfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1068ProtctrlEntity;
import com.synet.tool.rsc.model.Tb1069RcdchannelaEntity;
import com.synet.tool.rsc.model.Tb1070MmsserverEntity;
import com.synet.tool.rsc.model.Tb1071DauEntity;
import com.synet.tool.rsc.model.Tb1072RcdchanneldEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;
import com.synet.tool.rsc.model.Tb1092PowerkkEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;

public abstract class AbstractExportDataHandler {
	
	protected static final int TB1006_INDEX = 1006;
	protected static final int TB1016_INDEX = 1016;
	protected static final int TB1022_INDEX = 1022;
	protected static final int TB1026_INDEX = 1026;
	protected static final int TB1041_INDEX = 1041;
	protected static final int TB1042_INDEX = 1042;
	protected static final int TB1043_INDEX = 1043;
	protected static final int TB1044_INDEX = 1044;
	protected static final int TB1045_INDEX = 1045;
	protected static final int TB1046_INDEX = 1046;
	protected static final int TB1047_INDEX = 1047;
	protected static final int TB1048_INDEX = 1048;
	protected static final int TB1049_INDEX = 1049;
	protected static final int TB1050_INDEX = 1050;
	protected static final int TB1051_INDEX = 1051;
	protected static final int TB1052_INDEX = 1052;
	protected static final int TB1053_INDEX = 1053;
	protected static final int TB1054_INDEX = 1054;
	protected static final int TB1055_INDEX = 1055;
	protected static final int TB1056_INDEX = 1056;
	protected static final int TB1057_INDEX = 1057;
	protected static final int TB1058_INDEX = 1058;
	protected static final int TB1059_INDEX = 1059;
	protected static final int TB1060_INDEX = 1060;
	protected static final int TB1061_INDEX = 1061;
	protected static final int TB1062_INDEX = 1062;
	protected static final int TB1063_INDEX = 1063;
	protected static final int TB1064_INDEX = 1064;
	protected static final int TB1065_INDEX = 1065;
	protected static final int TB1066_INDEX = 1066;
	protected static final int TB1067_INDEX = 1067;
	protected static final int TB1068_INDEX = 1068;
	protected static final int TB1069_INDEX = 1069;
	protected static final int TB1070_INDEX = 1070;
	protected static final int TB1071_INDEX = 1071;
	protected static final int TB1072_INDEX = 1072;
	protected static final int TB1073_INDEX = 1073;
	protected static final int TB1090_INDEX = 1090;
	protected static final int TB1091_INDEX = 1091;
	protected static final int TB1092_INDEX = 1092;
	protected static final int TB1093_INDEX = 1093;
	
	protected ExportConnManager exportConnManager;
	protected Connection connect;
	protected BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
	
	public abstract boolean exportData(ConnParam connParam);
	
	protected void exprotTableDate(int tbIndex) throws SQLException {
		Class<?> clazz = getClazz(tbIndex);
		if (clazz == null) return;
		List<?> list = beanDao.getAll(clazz);
		if (list != null && list.size() > 0) {
			System.out.println("准备插入：" + tbIndex + ",数据条数：" + list.size());
			connect.setAutoCommit(false);
			for (Object obj : list) {
				exportObjectData(tbIndex, obj);
			}
			connect.commit();
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void exportObjectData(int tbIndex, Object obj) throws SQLException {
		String sql = getSql(tbIndex);
		if (sql == null) {
			throw new NullPointerException("sql is not null[]");
		}
		PreparedStatement preState = connect.prepareStatement(sql);
		setValue(preState, tbIndex, obj);
	}
	
	
	protected Class<?> getClazz(int tbIndex) {
		switch (tbIndex) {
		case TB1006_INDEX:
			return Tb1006AnalogdataEntity.class;
		case TB1016_INDEX:
			return Tb1016StatedataEntity.class;
		case TB1022_INDEX:
			return Tb1022FaultconfigEntity.class;
		case TB1026_INDEX:
			return Tb1026StringdataEntity.class;
		case TB1041_INDEX:
			return Tb1041SubstationEntity.class;
		case TB1042_INDEX:
			return Tb1042BayEntity.class;
		case TB1043_INDEX:
			return Tb1043EquipmentEntity.class;
		case TB1044_INDEX:
			return Tb1044TerminalEntity.class;
		case TB1045_INDEX:
			return Tb1045ConnectivitynodeEntity.class;
		case TB1046_INDEX:
			return Tb1046IedEntity.class;
		case TB1047_INDEX:
			return Tb1047BoardEntity.class;
		case TB1048_INDEX:
			return Tb1048PortEntity.class;
		case TB1049_INDEX:
			return Tb1049RegionEntity.class;
		case TB1050_INDEX:
			return Tb1050CubicleEntity.class;
		case TB1051_INDEX:
			return Tb1051CableEntity.class;
		case TB1052_INDEX:
			return Tb1052CoreEntity.class;
		case TB1053_INDEX:
			return Tb1053PhysconnEntity.class;
		case TB1054_INDEX:
			return Tb1054RcbEntity.class;
		case TB1055_INDEX:
			return Tb1055GcbEntity.class;
		case TB1056_INDEX:
			return Tb1056SvcbEntity.class;
		case TB1057_INDEX:
			return Tb1057SgcbEntity.class;
		case TB1058_INDEX:
			return Tb1058MmsfcdaEntity.class;
		case TB1059_INDEX:
			return Tb1059SgfcdaEntity.class;
		case TB1060_INDEX:
			return Tb1060SpfcdaEntity.class;
		case TB1061_INDEX:
			return Tb1061PoutEntity.class;
		case TB1062_INDEX:
			return Tb1062PinEntity.class;
		case TB1063_INDEX:
			return Tb1063CircuitEntity.class;
		case TB1064_INDEX:
			return Tb1064StrapEntity.class;
		case TB1065_INDEX:
			return Tb1065LogicallinkEntity.class;
		case TB1066_INDEX:
			return Tb1066ProtmmxuEntity.class;
		case TB1067_INDEX:
			return Tb1067CtvtsecondaryEntity.class;
		case TB1068_INDEX:
			return Tb1068ProtctrlEntity.class;
		case TB1069_INDEX:
			return Tb1069RcdchannelaEntity.class;
		case TB1070_INDEX:
			return Tb1070MmsserverEntity.class;
		case TB1071_INDEX:
			return Tb1071DauEntity.class;
		case TB1072_INDEX:
			return Tb1072RcdchanneldEntity.class;
		case TB1073_INDEX:
			return Tb1073LlinkphyrelationEntity.class;
		case TB1090_INDEX:
			return Tb1090LineprotfiberEntity.class;
		case TB1091_INDEX:
			return Tb1091IotermEntity.class;
		case TB1092_INDEX:
			return Tb1092PowerkkEntity.class;
		case TB1093_INDEX:
			return Tb1093VoltagekkEntity.class;
		default:
			return null;
		}
	}

	protected String getSql(int tbIndex) {
		switch (tbIndex) {
		case TB1006_INDEX:
			return getTb1006Sql();
		case TB1016_INDEX:
			return getTb1016Sql();
		case TB1022_INDEX:
			return getTb1022Sql();
		case TB1026_INDEX:
			return getTb1041Sql();
		case TB1041_INDEX:
			return getTb1041Sql();
		case TB1042_INDEX:
			return getTb1042Sql();
		case TB1043_INDEX:
			return getTb1043Sql();
		case TB1044_INDEX:
			return getTb1044Sql();
		case TB1045_INDEX:
			return getTb1045Sql();
		case TB1046_INDEX:
			return getTb1046Sql();
		case TB1047_INDEX:
			return getTb1047Sql();
		case TB1048_INDEX:
			return getTb1048Sql();
		case TB1049_INDEX:
			return getTb1049Sql();
		case TB1050_INDEX:
			return getTb1050Sql();
		case TB1051_INDEX:
			return getTb1051Sql();
		case TB1052_INDEX:
			return getTb1052Sql();
		case TB1053_INDEX:
			return getTb1053Sql();
		case TB1054_INDEX:
			return getTb1054Sql();
		case TB1055_INDEX:
			return getTb1055Sql();
		case TB1056_INDEX:
			return getTb1056Sql();
		case TB1057_INDEX:
			return getTb1057Sql();
		case TB1058_INDEX:
			return getTb1058Sql();
		case TB1059_INDEX:
			return getTb1059Sql();
		case TB1060_INDEX:
			return getTb1060Sql();
		case TB1061_INDEX:
			return getTb1061Sql();
		case TB1062_INDEX:
			return getTb1062Sql();
		case TB1063_INDEX:
			return getTb1063Sql();
		case TB1064_INDEX:
			return getTb1064Sql();
		case TB1065_INDEX:
			return getTb1065Sql();
		case TB1066_INDEX:
			return getTb1066Sql();
		case TB1067_INDEX:
			return getTb1067Sql();
		case TB1068_INDEX:
			return getTb1068Sql();
		case TB1069_INDEX:
			return getTb1069Sql();
		case TB1070_INDEX:
			return getTb1070Sql();
		case TB1071_INDEX:
			return getTb1071Sql();
		case TB1072_INDEX:
			return getTb1072Sql();
		case TB1073_INDEX:
			return getTb1073Sql();
		case TB1090_INDEX:
			return getTb1090Sql();
		case TB1091_INDEX:
			return getTb1091Sql();
		case TB1092_INDEX:
			return getTb1092Sql();
		case TB1093_INDEX:
			return getTb1093Sql();
		default:
			return null;
		}
	}

	protected void setValue(PreparedStatement preState, int tbIndex, Object obj) throws SQLException {
		switch (tbIndex) {
		case TB1006_INDEX:
			setValueByTb1006(preState, obj);
			break;
		case TB1016_INDEX:
			setValueByTb1016(preState, obj);
			break;
		case TB1022_INDEX:
			setValueByTb1022(preState, obj);
			break;
		case TB1026_INDEX:
			setValueByTb1026(preState, obj);
			break;
		case TB1041_INDEX:
			setValueByTb1041(preState, obj);
			break;
		case TB1042_INDEX:
			setValueByTb1042(preState, obj);
			break;
		case TB1043_INDEX:
			setValueByTb1043(preState, obj);
			break;
		case TB1044_INDEX:
			setValueByTb1044(preState, obj);
			break;
		case TB1045_INDEX:
			setValueByTb1045(preState, obj);
			break;
		case TB1046_INDEX:
			setValueByTb1046(preState, obj);
			break;
		case TB1047_INDEX:
			setValueByTb1047(preState, obj);
			break;
		case TB1048_INDEX:
			setValueByTb1048(preState, obj);
			break;
		case TB1049_INDEX:
			setValueByTb1049(preState, obj);
			break;
		case TB1050_INDEX:
			setValueByTb1050(preState, obj);
			break;
		case TB1051_INDEX:
			setValueByTb1051(preState, obj);
			break;
		case TB1052_INDEX:
			setValueByTb1052(preState, obj);
			break;
		case TB1053_INDEX:
			setValueByTb1053(preState, obj);
			break;
		case TB1054_INDEX:
			setValueByTb1054(preState, obj);
			break;
		case TB1055_INDEX:
			setValueByTb1055(preState, obj);
			break;
		case TB1056_INDEX:
			setValueByTb1056(preState, obj);
			break;
		case TB1057_INDEX:
			setValueByTb1057(preState, obj);
			break;
		case TB1058_INDEX:
			setValueByTb1058(preState, obj);
			break;
		case TB1059_INDEX:
			setValueByTb1059(preState, obj);
			break;
		case TB1060_INDEX:
			setValueByTb1060(preState, obj);
			break;
		case TB1061_INDEX:
			setValueByTb1061(preState, obj);
			break;
		case TB1062_INDEX:
			setValueByTb1062(preState, obj);
			break;
		case TB1063_INDEX:
			setValueByTb1063(preState, obj);
			break;
		case TB1064_INDEX:
			setValueByTb1064(preState, obj);
			break;
		case TB1065_INDEX:
			setValueByTb1065(preState, obj);
			break;
		case TB1066_INDEX:
			setValueByTb1066(preState, obj);
			break;
		case TB1067_INDEX:
			setValueByTb1067(preState, obj);
			break;
		case TB1068_INDEX:
			setValueByTb1068(preState, obj);
			break;
		case TB1069_INDEX:
			setValueByTb1069(preState, obj);
			break;
		case TB1070_INDEX:
			setValueByTb1070(preState, obj);
			break;
		case TB1071_INDEX:
			setValueByTb1071(preState, obj);
			break;
		case TB1072_INDEX:
			setValueByTb1072(preState, obj);
			break;
		case TB1073_INDEX:
			setValueByTb1073(preState, obj);
			break;
		case TB1090_INDEX:
			setValueByTb1090(preState, obj);
			break;
		case TB1091_INDEX:
			setValueByTb1091(preState, obj);
			break;
		case TB1092_INDEX:
			setValueByTb1092(preState, obj);
			break;
		case TB1093_INDEX:
			setValueByTb1093(preState, obj);
			break;
		default:
			break;
		}
	}
	
	protected abstract void setValueByTb1006(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1016(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1022(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1026(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1041(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1042(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1043(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1044(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1045(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1046(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1047(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1048(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1049(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1050(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1051(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1052(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1053(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1054(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1055(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1056(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1057(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1058(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1059(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1060(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1061(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1062(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1063(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1064(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1065(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1066(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1067(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1068(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1069(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1070(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1071(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1072(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1073(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1090(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1091(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1092(PreparedStatement preState, Object obj) throws SQLException;
	protected abstract void setValueByTb1093(PreparedStatement preState, Object obj) throws SQLException;

	
	protected abstract String getTb1006Sql();
	protected abstract String getTb1016Sql();
	protected abstract String getTb1022Sql();
	protected abstract String getTb1026Sql();
	protected abstract String getTb1041Sql();
	protected abstract String getTb1042Sql();
	protected abstract String getTb1043Sql();
	protected abstract String getTb1044Sql();
	protected abstract String getTb1045Sql();
	protected abstract String getTb1046Sql();
	protected abstract String getTb1047Sql();
	protected abstract String getTb1048Sql();
	protected abstract String getTb1049Sql();
	protected abstract String getTb1050Sql();
	protected abstract String getTb1051Sql();
	protected abstract String getTb1052Sql();
	protected abstract String getTb1053Sql();
	protected abstract String getTb1054Sql();
	protected abstract String getTb1055Sql();
	protected abstract String getTb1056Sql();
	protected abstract String getTb1057Sql();
	protected abstract String getTb1058Sql();
	protected abstract String getTb1059Sql();
	protected abstract String getTb1060Sql();
	protected abstract String getTb1061Sql();
	protected abstract String getTb1062Sql();
	protected abstract String getTb1063Sql();
	protected abstract String getTb1064Sql();
	protected abstract String getTb1065Sql();
	protected abstract String getTb1066Sql();
	protected abstract String getTb1067Sql();
	protected abstract String getTb1068Sql();
	protected abstract String getTb1069Sql();
	protected abstract String getTb1070Sql();
	protected abstract String getTb1071Sql();
	protected abstract String getTb1072Sql();
	protected abstract String getTb1073Sql();
	protected abstract String getTb1090Sql();
	protected abstract String getTb1091Sql();
	protected abstract String getTb1092Sql();
	protected abstract String getTb1093Sql();

	protected void setInt(PreparedStatement preState, int index,Integer value) throws SQLException{
		if (value != null) {
			preState.setInt(index, value);
		} else {
			preState.setInt(index, 0);
		}
	}
	
	protected void setSring(PreparedStatement preState, int index,String value) throws SQLException {
		preState.setString(index, value);
	}
	
	protected void setFloat(PreparedStatement preState, int index,Float value) throws SQLException {
		if (value != null) {
			preState.setFloat(index, value);
		} else {
			preState.setFloat(index, 0);
		}
	}
	
}
