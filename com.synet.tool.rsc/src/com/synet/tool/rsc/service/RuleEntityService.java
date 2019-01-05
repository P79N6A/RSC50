package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.io.scd.SclUtil;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1054RcbEntity;
import com.synet.tool.rsc.model.Tb1055GcbEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;
import com.synet.tool.rsc.util.RuleType;

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
			monitor.beginTask(prefix + " 正在向Rcb应用辨识规则", 4);
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
			monitor.worked(1);
			monitor.setTaskName(prefix + " 正在向Pin应用辨识规则");
		}
		applyRulesToPins();
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
			List<Tb1058MmsfcdaEntity> mmsFcdas = (List<Tb1058MmsfcdaEntity>) beanDao.getListByCriteria(Tb1058MmsfcdaEntity.class, "tb1054RcbByF1054Code", rcb);
			for (Tb1058MmsfcdaEntity mmsFcda : mmsFcdas) {
				String dataCode = mmsFcda.getDataCode();
				int oldType = mmsFcda.getF1058Type();
				Rule type = getRuleByMmsfcda(dataset, mmsFcda);
				if (type != null) {
					mmsFcda.setF1058Type(type.getId());
					if (SclUtil.isStData(dataCode)) {
						if (RuleType.STRAP.include(type)) {
							mmsfcdaService.addStrap(mmsFcda);
						} else if (RuleType.STRAP.include(oldType)) {
							mmsfcdaService.removeStrap(mmsFcda);
						}
						mmsfcdaService.updateStateF1011No(dataCode, type.getId());
					} else if (SclUtil.isAlgData(dataCode)) {
						mmsfcdaService.updateAnalogF1011No(dataCode, type.getId());
					}
					beanDao.update(mmsFcda);
				}
			}
		}
	}
	
	public void applyRulesToGcb() {
		List<Tb1055GcbEntity> cbs = (List<Tb1055GcbEntity>) beanDao.getListByCriteria(Tb1055GcbEntity.class, "tb1046IedByF1046Code", iedEntity);
		for (Tb1055GcbEntity cb : cbs) {
			String dataset = cb.getDataset();
			List<Tb1061PoutEntity> fcdas = poutEntityService.getPoutEntityByCb(cb);
			applyRulesToPouts(dataset, fcdas);
		}
	}

	private void applyRulesToPouts(String dataset, List<Tb1061PoutEntity> fcdas) {
		List<Tb1061PoutEntity> fcdasUpdate = new ArrayList<>(); 
		for (Tb1061PoutEntity pout : fcdas) {
			if (pout == null) {
				continue;
			}
			String dataCode = pout.getDataCode();
			int oldType = pout.getF1061Type();
			Rule type = getRuleByPout(dataset, pout);
			if (type != null) {
				if (SclUtil.isStData(dataCode)) {
					mmsfcdaService.updateStateF1011No(dataCode, type.getId());
					if (RuleType.STRAP.include(type)) {
						poutEntityService.addStrap(pout);
					} else if (RuleType.STRAP.include(oldType)) {
						poutEntityService.removeStrap(pout);
					}
				} else if (SclUtil.isAlgData(dataCode)) {
					mmsfcdaService.updateAnalogF1011No(dataCode, type.getId());
				}
				pout.setF1061Type(type.getId());
				fcdasUpdate.add(pout);
			}
		}
		beanDao.updateBatch(fcdasUpdate);
	}
	
	public void applyRulesToSvcb() {
		List<Tb1056SvcbEntity> cbs = (List<Tb1056SvcbEntity>) beanDao.getListByCriteria(Tb1056SvcbEntity.class, "tb1046IedByF1046Code", iedEntity);
		for (Tb1056SvcbEntity cb : cbs) {
			String dataset = cb.getDataset();
			List<Tb1061PoutEntity> fcdas = poutEntityService.getPoutEntityByCb(cb);
			applyRulesToPouts(dataset, fcdas);
		}
	}
	
	public void applyRulesToPins() {
		List<Tb1062PinEntity> pins = (List<Tb1062PinEntity>) beanDao.getListByCriteria(Tb1062PinEntity.class, "tb1046IedByF1046Code", iedEntity);
		List<Tb1062PinEntity> pinsUpdate = new ArrayList<>(); 
		for (Tb1062PinEntity pin : pins) {
			Rule type = F1011_NO.getType("", pin.getF1062RefAddr(), pin.getF1062Desc(), rules);
			if (type != null) {
				pin.setF1011No(type.getId());
				pinsUpdate.add(pin);
			}
		}
		beanDao.updateBatch(pinsUpdate);
	}
}
