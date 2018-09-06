package com.synet.tool.rsc.dialog;

import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.table.KTableEditorDialog;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.service.AnalogdataService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

public class RefAddrSelectDialog extends KTableEditorDialog {

	private DevKTable table;
	private MmsfcdaService mmsService;
	private AnalogdataService analogdataService;
	private Tb1048PortEntity portEntity;
	
	public RefAddrSelectDialog(Shell parentShell, Object item) {
		super(parentShell, item);
		mmsService = new MmsfcdaService();
		analogdataService = new AnalogdataService();
		portEntity = (Tb1048PortEntity) item;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		table = TableFactory.getMmsFcdaTable(parent);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		initData();
		return super.createDialogArea(parent);
	}
	
	private void initData() {
		List<Tb1058MmsfcdaEntity> mmsFcdaList = mmsService.getByPort(portEntity.getF1048Code());
		table.setInput(mmsFcdaList);
		table.getTable().layout();
	}
	
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == OK) {
			Tb1058MmsfcdaEntity selection = (Tb1058MmsfcdaEntity) table.getSelection();
			if(selection != null) {
				String f1048Code = portEntity.getF1048Code();
				Tb1006AnalogdataEntity anolog = analogdataService.getAnologByCodes(f1048Code);
				anolog.setParentCode(f1048Code);
				analogdataService.update(anolog);
				portEntity.setTb1006AnalogdataByF1048Code(anolog);
				analogdataService.update(portEntity);
			}
		}
		super.buttonPressed(buttonId);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("光强参引数据列表"); 
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(480, 410);
	}
}
