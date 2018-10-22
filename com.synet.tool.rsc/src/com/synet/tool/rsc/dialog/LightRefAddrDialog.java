package com.synet.tool.rsc.dialog;

import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.ui.table.KTableEditorDialog;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.service.MmsfcdaService;

public class LightRefAddrDialog extends KTableEditorDialog {
	
	private Tb1048PortEntity portEntity;
	private Combo combo;
	private String[] items;
	private String oldData;
	private List<Tb1058MmsfcdaEntity> mmsList;
	private BeanDaoService beanDao;

	public LightRefAddrDialog(Shell parentShell, Object item) {
		super(parentShell, item);
		portEntity = (Tb1048PortEntity) item;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		initData();
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, gridData, 1);
		composite.setLayout(SwtUtil.getGridLayout(2));
		SwtUtil.createLabel(composite, "光强参引：", SwtUtil.bt_gd);
		combo = SwtUtil.createCombo(composite,  SwtUtil.bt_hd);
		combo.setItems(items);
		combo.setText(oldData);
		return super.createDialogArea(parent);
	}
	
	private void initData() {
		beanDao = BeanDaoImpl.getInstance();
		MmsfcdaService mmsfcdaService = new MmsfcdaService();
		String[] names = DictManager.getInstance().getDictNames("DS_AIN");
		mmsList = mmsfcdaService.getMmsdcdaByDataSet(portEntity.getTb1047BoardByF1047Code()
				.getTb1046IedByF1046Code(), names);
		
		int size = mmsList.size();
		items = new String[size];
		for (int i = 0; i < size; i++) {
			items[i] = mmsList.get(i).getF1058RefAddr();
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
				Tb1058MmsfcdaEntity tb1058MmsfcdaEntity = mmsList.get(selectionIndex);
				String dataCode = tb1058MmsfcdaEntity.getDataCode();
				Tb1006AnalogdataEntity analogData = (Tb1006AnalogdataEntity)
						beanDao.getObject(Tb1006AnalogdataEntity.class, "f1006Code", dataCode);
				portEntity.setTb1006AnalogdataByF1048Code(analogData);
				analogData.setParentCode(portEntity.getF1048Code());
				
				beanDao.update(portEntity);
				beanDao.update(analogData);
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
		return new Point(280, 150);
	}
	
}
