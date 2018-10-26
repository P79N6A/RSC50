package com.synet.tool.rsc.service;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.io.scd.SclUtil;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1054RcbEntity;
import com.synet.tool.rsc.model.Tb1055GcbEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;

public class RuleEntityService {
	
	private BeanDaoService beanDao = BeanDaoImpl.getInstance();
	private PoutEntityService poutEntityService = new PoutEntityService();
	private MmsfcdaService mmsfcdaService = new MmsfcdaService();
	
	private Rule[] rules;
	private Tb1046IedEntity iedEntity;
	
	public RuleEntityService(Tb1046IedEntity iedEntity, Rule[] rules) {
		this.iedEntity = iedEntity;
		this.rules = rules;
	}
	
	public void applyRulesToIED(IProgressMonitor monitor) {
		String prefix = "装置 " + iedEntity.getF1046Name();
		if (monitor != null) {
			monitor.beginTask(prefix + " 正在向Rcb应用辨识规则", 3);
		}
		applyRulesToRcb();
		if (monitor != null) {
			monitor.worked(1);
			monitor.setTaskName(prefix + " 正在向Gcb应用辨识规则");
		}
		applyRulesToGcb();
		if (monitor != null) {
			monitor.worked(1);
			monitor.setTaskName(prefix + " 正在向Svcb应用辨识规则");
		}
		applyRulesToSvcb();
		if (monitor != null) {
			monitor.done();
		}
	}
	
	private Rule getRuleByMmsfcda(String dataset, Tb1058MmsfcdaEntity mmsFcda) {
		return F1011_NO.getType(dataset, mmsFcda.getF1058RefAddr(), mmsFcda.getF1058Desc(), rules);
	}

	private Rule getRuleByPout(String dataset, Tb1061PoutEntity poutEntity) {
		return F1011_NO.getType(dataset, poutEntity.getF1061RefAddr(), poutEntity.getF1061Desc(), rules);
	}
	
	public void applyRulesToRcb() {
		List<Tb1054RcbEntity> rcbs = (List<Tb1054RcbEntity>) beanDao.getListByCriteria(Tb1054RcbEntity.class, "tb1046IedByF1046Code", iedEntity);
		for (Tb1054RcbEntity rcb : rcbs) {
			String dataset = rcb.getF1054Dataset();
			List<Tb1058MmsfcdaEntity> mmsFcdas = rcb.getTb1058MmsfcdasByF1054Code();
			for (Tb1058MmsfcdaEntity mmsFcda : mmsFcdas) {
				int f1058DataType = mmsFcda.getF1058DataType();
				String dataCode = mmsFcda.getDataCode();
				Rule type = getRuleByMmsfcda(dataset, mmsFcda);
				if (type != null) {
					if (DBConstants.DATA_ST == f1058DataType) {
						mmsfcdaService.updateStateF1011No(dataCode, type);
						if (SclUtil.isStrap(dataset)) {
							mmsfcdaService.updateStrapF1011No(dataCode, type);
						}
					} else if (DBConstants.DATA_MX == f1058DataType) {
						mmsfcdaService.updateAnalogF1011No(dataCode, type);
					}
				}
			}
		}
	}
	
	public void applyRulesToGcb() {
		List<Tb1055GcbEntity> cbs = (List<Tb1055GcbEntity>) beanDao.getListByCriteria(Tb1055GcbEntity.class, "tb1046IedByF1046Code", iedEntity);
		for (Tb1055GcbEntity cb : cbs) {
			String dataset = cb.getDataset();
			List<Tb1061PoutEntity> fcdas = cb.getTb1061PoutsByCbCode();
			applyRulesToPouts(dataset, fcdas);
		}
	}

	private void applyRulesToPouts(String dataset, List<Tb1061PoutEntity> fcdas) {
		for (Tb1061PoutEntity fcda : fcdas) {
			String dataCode = fcda.getDataCode();
			Rule type = getRuleByPout(dataset, fcda);
			if (type != null) {
				if (dataCode.startsWith(DBConstants.PR_State)) {
					mmsfcdaService.updateStateF1011No(dataCode, type);
				} else if (dataCode.startsWith(DBConstants.PR_Analog)) {
					mmsfcdaService.updateAnalogF1011No(dataCode, type);
				}
				// 更新对侧接收虚端子
				poutEntityService.updatePinF1011No(fcda, type);
			}
		}
	}
	
	public void applyRulesToSvcb() {
		List<Tb1056SvcbEntity> cbs = (List<Tb1056SvcbEntity>) beanDao.getListByCriteria(Tb1056SvcbEntity.class, "tb1046IedByF1046Code", iedEntity);
		for (Tb1056SvcbEntity cb : cbs) {
			String dataset = cb.getDataset();
			List<Tb1061PoutEntity> fcdas = cb.getTb1061PoutsByCbCode();
			applyRulesToPouts(dataset, fcdas);
		}
	}
	
}