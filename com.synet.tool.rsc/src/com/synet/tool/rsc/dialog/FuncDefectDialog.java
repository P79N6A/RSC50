package com.synet.tool.rsc.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.excel.handler.ProtFuncHandler;
import com.synet.tool.rsc.model.TB1084FuncClassEntity;
import com.synet.tool.rsc.model.TB1085ProtFuncEntity;
import com.synet.tool.rsc.model.TB1086DefectFuncREntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.service.TB1085ProtFuncService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;
import com.synet.tool.rsc.util.ExcelReaderUtil;

public class FuncDefectDialog extends WrappedDialog {

	private Button btnAdd;
	private Button btnDel;
	private DevKTable table;
	private org.eclipse.swt.widgets.List lsFunc;
	private Button btnImpDef;
	private Button btnDelDef;
	private Tb1046IedEntity iedEntity;
	private BeanDaoService beanDao;
	private TB1085ProtFuncService protFuncService;
	private RSCProperties rscp = RSCProperties.getInstance();
	
	private Map<String, TB1084FuncClassEntity> classMap;
	private List<TB1085ProtFuncEntity> protFuncs;

	public FuncDefectDialog(Shell parentShell, Tb1046IedEntity iedEntity) {
		super(parentShell);
		this.iedEntity = iedEntity;
		this.beanDao = BeanDaoImpl.getInstance();
		this.protFuncService = new TB1085ProtFuncService();
		this.classMap = new HashMap<>();
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		GridData griddata = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, griddata, 2);
		GridData gdLeft = new GridData(260, GridData.FILL_HORIZONTAL);
		Composite compLeft = SwtUtil.createComposite(composite, gdLeft, 2);
		btnAdd = SwtUtil.createButton(compLeft, SwtUtil.bt_gd, SWT.BUTTON1, "添加");
		btnDel = SwtUtil.createButton(compLeft, SwtUtil.bt_gd, SWT.BUTTON1, "删除");
		GridData gdLeft_3 = new GridData(260, GridData.FILL_HORIZONTAL);
		gdLeft_3.horizontalSpan = 2;
		lsFunc = SwtUtil.createList(compLeft, gdLeft_3);
		Composite compRight = SwtUtil.createComposite(composite, griddata, 2);
		btnImpDef = SwtUtil.createButton(compRight, SwtUtil.bt_gd, SWT.BUTTON1, "导入");
		btnDelDef = SwtUtil.createButton(compRight, SwtUtil.bt_gd, SWT.BUTTON1, "删除");
		GridData griddata_2 = new GridData(GridData.FILL_BOTH);
		griddata_2.horizontalSpan = 2;
		table = TableFactory.getFunDefectTable(compRight);
		table.getTable().setLayoutData(griddata_2);
		initData();
		addListeners();
		return parent;
	}
	
	class AddFuncAction extends Action {
		
		public AddFuncAction(String title) {
			setText(title);
		}
		
		@Override
		public void run() {
			String desc = getText();
			String[] funcs = lsFunc.getItems();
			if (Arrays.asList(funcs).contains(desc)) {
				DialogHelper.showWarning(desc + "功能已存在！");
				return;
			}
			TB1084FuncClassEntity funcClass = classMap.get(desc);
			TB1085ProtFuncEntity protFunc = new TB1085ProtFuncEntity();
			protFunc.setF1085CODE(rscp.nextTbCode(DBConstants.PR_FUN));
			protFunc.setTb1046ByF1046CODE(iedEntity);
			protFunc.setTb1804ByF1084CODE(funcClass);
			beanDao.insert(protFunc);
			protFuncs.add(protFunc);
			lsFunc.add(desc);
			lsFunc.select(funcs.length);
			table.setInput(new ArrayList<TB1086DefectFuncREntity>());
		}
	}
	
	private void loadFunClass(int selIdx) {
		lsFunc.select(selIdx);
		List<TB1086DefectFuncREntity> funDefList = protFuncService.getFunDefByFunc(protFuncs.get(selIdx));
		table.setInput(funDefList);
	}
	
	private void removeFunClass() {
		int[] selectionIndices = lsFunc.getSelectionIndices();
		if (selectionIndices == null || selectionIndices.length < 1) {
			return;
		}
		if (!DialogHelper.showConfirm("确定删除所选功能及其关联配置吗？")) {
			return;
		}
		Arrays.sort(selectionIndices);
		for (int i=selectionIndices.length-1; i > -1; i++) {
			int index = selectionIndices[i];
			TB1085ProtFuncEntity protFuncEntity = protFuncs.get(index);
			beanDao.deleteAll(TB1086DefectFuncREntity.class, "tb1085ByF1085CODE", protFuncEntity);
			beanDao.delete(protFuncEntity);
			table.setInput(new ArrayList<TB1086DefectFuncREntity>());
			lsFunc.remove(i);
		}
	}
	
	private void initData() {
		this.protFuncs = protFuncService.getByIed(iedEntity);
		for (TB1085ProtFuncEntity protFunc : protFuncs) {
			lsFunc.add(protFunc.getTb1804ByF1084CODE().getF1084DESC());
		}
		if (lsFunc.getItemCount() > 0) {
			loadFunClass(0);
		}
		List<TB1084FuncClassEntity> funcClassAll = protFuncService.getFuncClassAll();
		final Action[] actions = new Action[funcClassAll.size()];
		int i=0;
		for (TB1084FuncClassEntity funcClass : funcClassAll) {
			String desc = funcClass.getF1084DESC();
			classMap.put(desc, funcClass);
			actions[i] = new AddFuncAction(desc);
			i++;
		}
		final Menu popupMenu = new Menu(btnAdd);
		btnAdd.setMenu(popupMenu);
		popupMenu.setVisible(false);
		btnAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
//				if (e.button != 1) {
					for (final Action action : actions) {
						for (MenuItem item : popupMenu.getItems()) {
							if (action instanceof com.shrcn.found.ui.util.Separator) {
							} else if (action.getText().equals(item.getText())) {
								item.setEnabled(action.isEnabled());
							}
						}
					}
					popupMenu.setVisible(true);
//				}
			}
		});
		for (final Action action : actions) {
			if (action instanceof com.shrcn.found.ui.util.Separator) {
				new MenuItem(popupMenu, SWT.SEPARATOR);
				continue;
			} 
			MenuItem item = new MenuItem(popupMenu, SWT.POP_UP);
			item.setText(action.getText());
			ImageDescriptor imageDescriptor = action.getImageDescriptor();
			if (imageDescriptor != null)
				item.setImage(imageDescriptor.createImage());
			item.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					action.run();
				}
			});
		}
	}

	private void addListeners() {
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj =  e.getSource();
				ConsoleManager console = ConsoleManager.getInstance();
				if(obj == btnDel) {
					removeFunClass();
					console.append("功能类型已删除。");
				} else if(obj == btnImpDef) {
					List<TB1086DefectFuncREntity> defs = (List<TB1086DefectFuncREntity>) ExcelReaderUtil.importByHandler(new ProtFuncHandler());
					if (defs != null && defs.size()>0) {
						beanDao.insertBatch(defs);
						table.setInput(defs);
						console.append("告警功能关联导入成功。");
					}
				} else if(obj == btnDelDef) {
					if (DialogHelper.showConfirm("确定删除所选功能关联关系吗？")) {
						table.removeSelected();
						console.append("告警功能关联已删除。");
					}
				} else if(obj == lsFunc) {
					int selIdx = lsFunc.getSelectionIndex();
					if(selIdx == -1) {
						return;
					}
					loadFunClass(selIdx);
				}
			}
		};
		lsFunc.addSelectionListener(listener);
		btnImpDef.addSelectionListener(listener);
		btnDelDef.addSelectionListener(listener);
		btnAdd.addSelectionListener(listener);
		btnDel.addSelectionListener(listener);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("告警功能关联");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "关闭", false);
//		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(1250, 850);
	}
	
//	@SuppressWarnings("unchecked")
//	@Override
//	protected void buttonPressed(int buttonId) {
//		if (buttonId == IDialogConstants.OK_ID) {
//			boolean confirm = DialogHelper.showConfirm("确定要保存改动？（若无改动请点击\"取消\"按钮）");
//			if(confirm) {//修改rule.xml文件
//				String selName = lsFunc.getItem(lsFunc.getSelectionIndex());
//				ruleManager.modify((List<Rule>) table.getInput(), selName);
//				RSCProperties.getInstance().setCurrentRule(selName);
//				ruleManager.reLoad();
//				RuleType.initDicts();
//			}
//		}
//		super.buttonPressed(buttonId);
//	}
}
