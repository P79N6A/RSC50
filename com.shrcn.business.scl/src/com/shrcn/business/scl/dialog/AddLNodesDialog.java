package com.shrcn.business.scl.dialog;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import com.shrcn.business.xml.schema.EnumLNUtil;
import com.shrcn.business.xml.schema.LnClass;
import com.shrcn.found.ui.app.WrappedTitleAreaDialog;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.treetable.FixedTreeTableAdapterFactory;
import com.shrcn.found.ui.treetable.TreeTable;
import com.shrcn.found.ui.treetable.TreeTableViewer;
import com.shrcn.found.ui.util.SwtUtil;

public class AddLNodesDialog extends WrappedTitleAreaDialog {
	
	private List<LnClass> values;
	private TreeTable treeTable;
	private IField[] fields = new IField[] {new LNNameField(),new LNDescField(), new ADDLNNumField()};
	
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public AddLNodesDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("添加逻辑节点");
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	protected Control createDialogArea(Composite parent) {
		setTitle("添加逻辑节点");
		setMessage("请为要添加的逻辑节点输入所需数目，然后确定。");
		Composite composite = SwtUtil.createComposite(parent, new GridData(GridData.FILL_BOTH), 1);
		createTreeViewer(composite);
		return super.createDialogArea(parent);
	}
	
	private void createTreeViewer(Composite parent){
        treeTable = new TreeTable(parent,SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION, fields,
        		new FixedTreeTableAdapterFactory(AddLNTreeTableAdapter.instance));
        treeTable.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
        List<LnClass> fInput = EnumLNUtil.getXMLLNs();
		treeTable.setInput(fInput.toArray(new LnClass[fInput.size()]));
		initCellEditors();		
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
	public void initCellEditors() {
		treeTable.getTreeViewer().setColumnProperties(
				new String[] { "LNName", "LNdesc", "num" });
		CellEditor[] cellEditor = new CellEditor[3];
		cellEditor[0] = null;
		cellEditor[1] = null;
		cellEditor[2] = new TextCellEditor(treeTable.getTreeViewer().getTree());
		treeTable.getTreeViewer().setCellEditors(cellEditor);
		treeTable.getTreeViewer().setCellModifier(
				new MyCellModifier(treeTable.getTreeViewer()));
		Text text = (Text) cellEditor[2].getControl();
		text.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				String inStr = e.text;
				if (inStr.length() > 0) {
					e.doit = NumberUtils.isDigits(inStr);
				}
			}
		});
	}
	
	public class MyCellModifier implements ICellModifier{
		private TreeTableViewer treeTableViewer;

		public MyCellModifier(TreeTableViewer tv) {
			this.treeTableViewer = tv;
		}

		public boolean canModify(Object element, String property) {
			LnClass data = (LnClass) element;
			if ((data.getChildren().size() > 0) && property.equals("num"))
				return false;
			else
				return true;
		}

		public Object getValue(Object element, String property) {
			LnClass data = (LnClass) element;
			if (property.equals("num")) {
				return String.valueOf(data.getNum());
			} else {
				throw new RuntimeException("错误的列别名" + property);
			}
		}

		public void modify(Object element, String property, Object value) {
			TreeItem item = (TreeItem) element;
			LnClass ln = (LnClass) item.getData();
			if (property.equals("num")) {
				String newValue = (String) value;
				if (newValue.trim().equals(""))
					return;
				int newNum = Integer.parseInt(newValue);
				ln.setNum(newNum);
				List<LnClass> childrens = ln.getParent().getChildren();
				int num = 0;
				for (LnClass children : childrens) {
					num += children.getNum();
				}
				ln.getParent().setNum(num);
			} else {
				throw new RuntimeException("错误的列别名" + property);
			}
			treeTableViewer.update(ln, null);
			treeTableViewer.update(ln.getParent(), null);
		}
	}
	
	public List<LnClass> getValues() {
		return values;
	}
	
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			values = new ArrayList<LnClass>();
			LnClass[] fInput = (LnClass[])treeTable.getTreeViewer().getInput();
			for (LnClass flnClass : fInput) {
				int fnum = flnClass.getNum();
				if (fnum > 0) {
					List<LnClass> fln = flnClass.getChildren();
					for (LnClass ln : fln) {
						int num = ln.getNum();
						if (num > 0) {
							values.add(ln);
						}
					}
				}
			}
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(550, 400);
	}
}
