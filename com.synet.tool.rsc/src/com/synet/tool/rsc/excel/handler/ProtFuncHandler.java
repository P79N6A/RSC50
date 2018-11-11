package com.synet.tool.rsc.excel.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFComment;

import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.HqlDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.model.TB1085ProtFuncEntity;
import com.synet.tool.rsc.model.TB1086DefectFuncREntity;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.util.RscObjectUtils;

public class ProtFuncHandler extends RscSheetHandler {

	private TB1086DefectFuncREntity defectFunc;
	private String iedName;
	private String dataRef;
	private Tb1046IedEntity iedEntity;
	private Tb1006AnalogdataEntity algData;
	private Tb1016StatedataEntity stData;
	private BeanDaoService beanDao;
	private HqlDaoService hqlDao;
	
	public ProtFuncHandler() {
		this.errorMsg = new ArrayList<>();
		this.beanDao = BeanDaoImpl.getInstance();
		this.hqlDao = HqlDaoImpl.getInstance();
	}
	
	@Override
	public void startRow(int rowNum) {
		super.startRow(rowNum);
		if (rowNum > 0) {
			this.defectFunc = RscObjectUtils.createDefectFunc();
		}
	}
	
	@Override
	public void endRow(int rowNum) {
		if (rowNum < 1) {
			return;
		}
		if (defectFunc == null) {
			String error = "第" + (rowNum + 1) + "行";
			errorMsg.add(error);
		} else if (defectFunc.getTb1085ByF1085CODE() != null) {
			result.add(defectFunc);
		}
		super.endRow(rowNum);
	}
	
	@Override
	public void cell(String cellReference, String formattedValue,
			XSSFComment comment) {
		super.cell(cellReference, formattedValue, comment);
		if (currentRow > 0 && !isEmpty(formattedValue)) {
			saveValue(currentCol, formattedValue);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void saveValue(int col, String value) {
		switch(col) {
			case 0: 
			case 1:
				break;
			case 2:
				this.iedName = value;
				this.iedEntity = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Name", iedName);
				break;
			case 3:
				this.dataRef = value;
				Map<String, Object> params = new HashMap<>();
				params.put("tb1046IedByF1046Code", iedEntity);
				if (dataRef.indexOf("$ST$") > 0) {
					params.put("f1016AddRef", dataRef);
					this.stData = (Tb1016StatedataEntity) beanDao.getObject(Tb1016StatedataEntity.class, params);
					defectFunc.setTb1006BySTCODE(stData);
				} else if (dataRef.indexOf("$MX$") > 0) {
					params.put("f1006AddRef", dataRef);
					this.algData = (Tb1006AnalogdataEntity) beanDao.getObject(Tb1006AnalogdataEntity.class, params);
					defectFunc.setTb1006ByMXCODE(algData);
				}
				break;
			case 4:
				break;
			case 5:
				Map<String, Object> params1 = new HashMap<>();
				params1.put("ied", iedEntity);
				params1.put("func", value);
				String hql = "from " + TB1085ProtFuncEntity.class.getName() + " where tb1046ByF1046CODE=:ied and tb1804ByF1084CODE.f1084DESC=:func";
				TB1085ProtFuncEntity protFunc = (TB1085ProtFuncEntity) hqlDao.getObject(hql, params1);
				defectFunc.setTb1085ByF1085CODE(protFunc);
				break;
			default:
				break;
		}
	}

}
