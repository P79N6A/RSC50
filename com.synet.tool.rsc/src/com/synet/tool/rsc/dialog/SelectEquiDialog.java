package com.synet.tool.rsc.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.service.EquipmentEntityService;
import com.synet.tool.rsc.util.DataUtils;

/**
 * 选择互感器次级
 * @author Administrator
 *
 */
public class SelectEquiDialog  extends WrappedDialog{

	private Tb1042BayEntity bayEntity;
	private EquipmentEntityService service;
	private Combo comboEqu;
	private String[] itemsEqu;
	private List<Tb1043EquipmentEntity> result;
	private Tb1043EquipmentEntity select = null;
	private Combo comboBay;
	private String[] itemsBay;
	private List<Tb1042BayEntity> allBay;
	private Composite composite;
	
	protected SelectEquiDialog(Shell parentShell) {
		super(parentShell);
	}
	
	public SelectEquiDialog(Shell parentShell, Tb1042BayEntity bayEntity) {
		super(parentShell);
		this.bayEntity = bayEntity;
		this.service = new EquipmentEntityService();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		initData();
		GridData gridData = new GridData(GridData.FILL_BOTH);
		composite = SwtUtil.createComposite(parent, gridData, 1);
		composite.setLayout(SwtUtil.getGridLayout(2));
		SwtUtil.createLabel(composite, "间隔：", SwtUtil.bt_gd);
		comboBay = SwtUtil.createCombo(composite, SwtUtil.bt_hd);
		comboBay.setItems(itemsBay);
		comboBay.select(0);
		SwtUtil.createLabel(composite, "互感器：", SwtUtil.bt_gd);
		comboEqu = SwtUtil.createCombo(composite, SwtUtil.bt_hd);
		comboEqu.setItems(itemsEqu);
		comboEqu.select(0);
		addListener();
		return composite;
	}
	
	
	private void addListener() {
		comboBay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = service.getEquipmentEntitysByBayEntity(allBay.get(comboBay.getSelectionIndex()));
				int size = result.size();
				itemsEqu = new String[size];
				for (int i = 0; i < size; i++) {
					itemsEqu[i] = result.get(i).getF1043Name();
				}
				comboEqu.setItems(itemsEqu);
				comboEqu.select(0);
			}
		});
		
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		int size;
		if(bayEntity == null) {
			allBay = (List<Tb1042BayEntity>) BeanDaoImpl.getInstance().getAll(Tb1042BayEntity.class);
			if(!DataUtils.listNotNull(allBay)) {
				return;
			}
			size = allBay.size();
			itemsBay = new String[size];
			for (int i = 0; i < size; i++) {
				itemsBay[i] = allBay.get(i).getF1042Name();
			}
			result = service.getEquipmentEntitysByBayEntity(allBay.get(0));
			size = result.size();
			itemsEqu = new String[size];
			for (int i = 0; i < size; i++) {
				itemsEqu[i] = result.get(i).getF1043Name();
			}
		} else {
			itemsBay = new String[]{bayEntity.getF1042Desc()};
			result = service.getEquipmentEntitysByBayEntity(bayEntity);
			size = result.size();
			itemsEqu = new String[size];
			for (int i = 0; i < size; i++) {
				itemsEqu[i] = result.get(i).getF1043Name();
			}
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("互感器选择");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(270, 150);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId) {
			select = result.get(comboEqu.getSelectionIndex());
		}
		super.buttonPressed(buttonId);
	}

	public Tb1043EquipmentEntity getSelect() {
		return select;
	}
	
}
