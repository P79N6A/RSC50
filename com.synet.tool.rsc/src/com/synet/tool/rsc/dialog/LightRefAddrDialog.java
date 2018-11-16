package com.synet.tool.rsc.dialog;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.table.KTableEditorDialog;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.service.EnumIedType;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.util.RuleType;

public class LightRefAddrDialog extends KTableEditorDialog {
	
	private Tb1046IedEntity iedEntity;
	private Tb1048PortEntity portEntity;
	private Combo combo;
	private String[] items;
	private String oldData;
	private List<?> mmsList;
	private BeanDaoService beanDao;

	public LightRefAddrDialog(Shell parentShell, Object item) {
		super(parentShell, item);
		portEntity = (Tb1048PortEntity) item;
		iedEntity = portEntity.getTb1047BoardByF1047Code().getTb1046IedByF1046Code();
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		initData();
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, gridData, 1);
		composite.setLayout(SwtUtil.getGridLayout(2));
		SwtUtil.createLabel(composite, "  光强参引：", SwtUtil.bt_gd);
		combo = SwtUtil.createCombo(composite,  new GridData(400, SWT.DEFAULT));
		combo.setItems(items);
		combo.setText(oldData);
		return super.createDialogArea(parent);
	}
	
	private boolean isBayIed() {
		int typ = portEntity.getTb1047BoardByF1047Code().getTb1046IedByF1046Code().getF1046Type();
		return EnumIedType.isBayIED(typ);
	}
	
	private boolean isSubIed() {
		int typ = portEntity.getTb1047BoardByF1047Code().getTb1046IedByF1046Code().getF1046Type();
		return EnumIedType.isSubIED(typ);
	}
	
	private void initData() {
		if (isBayIed()) {
			beanDao = BeanDaoImpl.getInstance();
			MmsfcdaService mmsfcdaService = new MmsfcdaService();
			mmsList = mmsfcdaService.getByDataType(iedEntity, RuleType.IED_STATE);
			int size = mmsList.size();
			items = new String[size];
			for (int i = 0; i < size; i++) {
				Tb1058MmsfcdaEntity tb1058MmsfcdaEntity = (Tb1058MmsfcdaEntity)mmsList.get(i);
				items[i] = tb1058MmsfcdaEntity.getF1058Desc() + ":" + tb1058MmsfcdaEntity.getF1058RefAddr();
			}
		} else if (isSubIed()) {
			PoutEntityService poutEntityService = new PoutEntityService();
			mmsList = poutEntityService.getByDataType(iedEntity, RuleType.IED_STATE);
			int size = mmsList.size();
			items = new String[size];
			for (int i = 0; i < size; i++) {
				Tb1061PoutEntity tb1061PoutEntity = (Tb1061PoutEntity)mmsList.get(i);
				items[i] = tb1061PoutEntity.getF1061Desc() + ":" + tb1061PoutEntity.getF1061RefAddr();
			}
		}
		Tb1006AnalogdataEntity analogData = portEntity.getTb1006AnalogdataByF1048Code();
		if(analogData == null) {
			oldData = "*初始值为空*";
		} else {
			String mmsFcda = analogData.getF1006AddRef();
			if(mmsFcda == null) {
				oldData = "*初始值为空*";
			} else {
				oldData = analogData.getF1006AddRef();
			}
		}
	}
	
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			int selectionIndex = combo.getSelectionIndex();
			if(selectionIndex > -1) {
				String dataCode = null;
				if (isBayIed()) {
					Tb1058MmsfcdaEntity tb1058MmsfcdaEntity = (Tb1058MmsfcdaEntity) mmsList.get(selectionIndex);
					dataCode = tb1058MmsfcdaEntity.getDataCode();
				} else if (isSubIed()) {
					Tb1061PoutEntity poutEntity = (Tb1061PoutEntity) mmsList.get(selectionIndex);
					dataCode = poutEntity.getDataCode();
				}
				if (dataCode != null) {
					Tb1006AnalogdataEntity analogData = (Tb1006AnalogdataEntity)
							beanDao.getObject(Tb1006AnalogdataEntity.class, "f1006Code", dataCode);
					portEntity.setTb1006AnalogdataByF1048Code(analogData);
					analogData.setParentCode(portEntity.getF1048Code());
					beanDao.update(portEntity);
					beanDao.update(analogData);
				}
			}
		}
		super.buttonPressed(buttonId);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("光强参引选择"); 
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(550, 180);
	}
	
}
