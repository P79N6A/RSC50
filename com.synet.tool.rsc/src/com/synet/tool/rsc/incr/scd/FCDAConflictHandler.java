package com.synet.tool.rsc.incr.scd;

import java.util.Map;

import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.incr.EnumConflict;
import com.synet.tool.rsc.model.BaseCbEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1054RcbEntity;
import com.synet.tool.rsc.model.Tb1057SgcbEntity;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.service.SgfcdaEntityService;
import com.synet.tool.rsc.service.SpfcdaEntityService;

public class FCDAConflictHandler extends BaseConflictHandler {

	private SpfcdaEntityService spfcdaServ = new SpfcdaEntityService();
	private SgfcdaEntityService sgfcdaServ = new SgfcdaEntityService();
	private MmsfcdaService mmsfcdaServ = new MmsfcdaService();
	private PoutEntityService poutServ = new PoutEntityService();
	private EnumConflict conflictType;
	private Tb1046IedEntity ied;
	private String fcdaRef;
	
	public FCDAConflictHandler(Difference diff) {
		super(diff);
		Difference type = diff.getParent().getParent();
		conflictType = EnumConflict.getByType(type.getType());
		this.ied = (Tb1046IedEntity) type.getParent().getData();
		this.fcdaRef = diff.getName();
	}

	@Override
	public void handleAdd() {
		Map<String, String> disInfo = getDisInfo();
		Object pdata = diff.getParent().getData();
		switch (conflictType) {
		case Params:
			spfcdaServ.addSpfcda(ied, fcdaRef, disInfo);
			break;
		case Sets:
			sgfcdaServ.addSgfcda((Tb1057SgcbEntity) pdata, fcdaRef, disInfo);
			break;
		case Rcbs:
			mmsfcdaServ.addFcda((Tb1054RcbEntity) pdata, fcdaRef, disInfo);
			break;
		case Gooses:
			poutServ.addFcda((BaseCbEntity) pdata, fcdaRef, disInfo);
			break;
		case Smvs:
			poutServ.addFcda((BaseCbEntity) pdata, fcdaRef, disInfo);
			break;
		default:
			break;
		}
	}

	@Override
	public void handleDelete() {
		Object pdata = diff.getParent().getData();
		switch (conflictType) {
		case Params:
			spfcdaServ.deleteSpfcda(ied, fcdaRef);
			break;
		case Sets:
			sgfcdaServ.deleteSgfcda((Tb1057SgcbEntity) pdata, fcdaRef);
			break;
		case Rcbs:
			mmsfcdaServ.deleteFcda((Tb1054RcbEntity) pdata, fcdaRef);
			break;
		case Gooses:
			poutServ.deleteFcda((BaseCbEntity) pdata, fcdaRef);
			break;
		case Smvs:
			poutServ.deleteFcda((BaseCbEntity) pdata, fcdaRef);
			break;
		default:
			break;
		}
	}

	@Override
	public void handleUpate() {
		Map<String, String> upInfo = getUpdateInfo();
		Object pdata = diff.getParent().getData();
		switch (conflictType) {
		case Params:
			spfcdaServ.updateSpfcda(ied, fcdaRef, upInfo);
			break;
		case Sets:
			sgfcdaServ.updateSgfcda((Tb1057SgcbEntity) pdata, fcdaRef, upInfo);
			break;
		case Rcbs:
			mmsfcdaServ.updateFcda((Tb1054RcbEntity) pdata, fcdaRef, upInfo);
			break;
		case Gooses:
			poutServ.updateFcda((BaseCbEntity) pdata, fcdaRef, upInfo);
			break;
		case Smvs:
			poutServ.updateFcda((BaseCbEntity) pdata, fcdaRef, upInfo);
			break;
		default:
			break;
		}
	}
}
