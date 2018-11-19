package com.synet.tool.rsc.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.table.KTableEditorDialog;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;
import com.synet.tool.rsc.service.DefaultService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;
import com.synet.tool.rsc.ui.table.PhyConnsTable;

public class PhyConnsViewDialog extends KTableEditorDialog {
	
	private PhyConnsTable table;
	private Tb1065LogicallinkEntity logicLink;

	public PhyConnsViewDialog(Shell parentShell, Object item) {
		super(parentShell, item);
		this.logicLink = (Tb1065LogicallinkEntity) item;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		table = (PhyConnsTable) TableFactory.getPyhConnsViewTable(parent);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		initData();
		return parent;
	}
	
	private void initData() {
		table.setLogicLink(logicLink);
		BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
		List<Tb1073LlinkphyrelationEntity> relations = (List<Tb1073LlinkphyrelationEntity>) 
				beanDao.getListByCriteria(Tb1073LlinkphyrelationEntity.class, "tb1065LogicallinkByF1065Code", logicLink);
		if (relations != null && relations.size() > 0) {
			List<Tb1053PhysconnEntity> list = new ArrayList<>();
			for (Tb1073LlinkphyrelationEntity relation : relations) {
				list.add(relation.getTb1053PhysconnByF1053Code());
			}
			table.setInput(list);
		}
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("物理回路列表"); //$NON-NLS-1$
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(880, 510);
	}

}
