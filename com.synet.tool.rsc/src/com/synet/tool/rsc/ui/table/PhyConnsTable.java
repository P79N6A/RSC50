package com.synet.tool.rsc.ui.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.DefaultKTable;
import com.shrcn.tool.found.das.HqlDaoService;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;

public class PhyConnsTable extends DevKTable {

	private HqlDaoService hqlDao;
	private Tb1065LogicallinkEntity logicLink;
	
	public PhyConnsTable(Composite parent, TableConfig config) {
		super(parent, config);
		hqlDao = HqlDaoImpl.getInstance();
	}

	protected void initUI() {
		tablemodel = new DevKTableModel(this, config);
		table = new DefaultKTable(parent, tablemodel);	
	}
	
	@Override
	public void removeSelected() {
		List<Object> selections = getSelections();
		for (Object obj : selections) {
			Tb1053PhysconnEntity phys = (Tb1053PhysconnEntity) obj;
			String hql = "delete from " + Tb1073LlinkphyrelationEntity.class.getName() + 
					" where tb1053PhysconnByF1053Code=:phyconn and tb1065LogicallinkByF1065Code=:logicLink";
			Map<String, Object> params = new HashMap<>();
			params.put("phyconn", phys);
			params.put("logicLink", logicLink);
			hqlDao.updateByHql(hql, params);
		}
		int[] selectRowNums = getSelectRowNums();
		List<?> input = getInput();
		for (int i = selectRowNums.length - 1; i > -1; i--) {
			input.remove(selectRowNums[i]-1);
		}
		refresh();
	}
	
	@Override
	protected void initOthers() {
		super.initOthers();
		actions.add(new DelPhyConnsAction(this));
	}
	
	public void setLogicLink(Tb1065LogicallinkEntity logicLink) {
		this.logicLink = logicLink;
	}

	class DelPhyConnsAction extends Action {
		
		DevKTable table;
		
		public DelPhyConnsAction(DevKTable table) {
			setText("解除关联(&D)");
			this.table = table;
		}
		
		@Override
		public void run() {
			table.removeSelected();
		}
	}
}
