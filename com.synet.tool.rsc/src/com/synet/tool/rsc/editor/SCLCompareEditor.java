package com.synet.tool.rsc.editor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sf.excelutils2007.ExcelUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.treetable.FixedTreeTableAdapterFactory;
import com.shrcn.found.ui.treetable.TreeTable;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.FileDialogHelper;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.util.TaskManager;
import com.shrcn.found.ui.view.ConsoleManager;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.dialog.ConflictHandleDialog;
import com.synet.tool.rsc.editor.tree.DescField;
import com.synet.tool.rsc.editor.tree.DiffTreeTableAdapter;
import com.synet.tool.rsc.editor.tree.MsgField;
import com.synet.tool.rsc.editor.tree.NameField;
import com.synet.tool.rsc.editor.tree.NewDescField;
import com.synet.tool.rsc.editor.tree.NewNameField;
import com.synet.tool.rsc.editor.tree.OpField;
import com.synet.tool.rsc.editor.tree.TypeField;
import com.synet.tool.rsc.incr.IncrementImportor;
import com.synet.tool.rsc.util.ExcelFileManager2007;

public class SCLCompareEditor extends BaseConfigEditor {

	private Label lb;
	private Composite cmpDiff;
	private Button btnExport;
	private Button btnImport;
	private TreeTable treetable;
	
	private List<Difference> diffs;
	
	public SCLCompareEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(new GridLayout(1, false));
		
		this.lb = SwtUtil.createLabel(container, "经比较未发现任何差异！", new GridData(GridData.FILL_HORIZONTAL));

		this.cmpDiff = new Composite(container, SWT.NONE);
		cmpDiff.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout1 = new GridLayout(1, false);
		layout1.marginLeft = -10;
		layout1.marginRight = 0;
		cmpDiff.setLayout(layout1);
		
		final Composite cmpTools = new Composite(cmpDiff, SWT.NONE);
		cmpTools.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final GridLayout layout = new GridLayout(2, false);
		layout.marginLeft = -5;
		layout.marginRight = -5;
		cmpTools.setLayout(layout);
		GridData btData = new GridData(GridData.END);
		btData.widthHint = 120;
		btnExport = SwtUtil.createPushButton(cmpTools, "导出Excel", btData);
		btnExport.setImage(ImgDescManager.getImageDesc(ImageConstants.EXCEL).createImage());
		btnImport = SwtUtil.createPushButton(cmpTools, "增量导入", btData);
		btnImport.setImage(ImgDescManager.getImageDesc(ImageConstants.IMPORT).createImage());

		IField[] fields = new IField[] {new TypeField(), new OpField(), new NameField(), new DescField(), 
				new NewNameField(), new NewDescField(),new MsgField()};
		this.treetable = new TreeTable(cmpDiff, SWT.MULTI | SWT.FULL_SELECTION, fields, 
				new FixedTreeTableAdapterFactory(DiffTreeTableAdapter.instance));
		treetable.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		SwtUtil.createContextMenu(treetable.getTree(), 
				new RenameAction(), new UnRenameAction(), new OperationAction(OP.NONE), 
				new OperationAction(OP.ADD), new OperationAction(OP.DELETE), new OperationAction(OP.UPDATE));
	}
	
	class RenameAction extends Action {
		
		public RenameAction() {
			setText("重命名");
		}
		
		@Override
		public void run() {
			Difference diff = getSelectedDiff();
			if (OP.DELETE != diff.getOp()) {
				DialogHelper.showWarning("只有删除状态才允许重命名！");
				return;
			}
			List<Difference> diffsAdded = new ArrayList<>();
			List<Difference> brothers = diff.getParent().getChildren();
			for (Difference brother : brothers) {
				if (diff.getType().equals(brother.getType())
						&& brother.getOp() == OP.ADD) {
					diffsAdded.add(brother);
				}
			}
			if (diffsAdded.size() < 1) {
				DialogHelper.showWarning("未增加同类型节点，无法重命名！");
				return;
			}
			ConflictHandleDialog conflictDlg = new ConflictHandleDialog(diff, diffsAdded, getShell());
			if (ConflictHandleDialog.OK == conflictDlg.open()) {
				refreshParentDiff();
			}
		}
	}
	
	class UnRenameAction extends Action {
		
		public UnRenameAction() {
			setText("取消重命名");
		}
		
		@Override
		public void run() {
			Difference diff = getSelectedDiff();
			if (OP.RENAME != diff.getOp()) {
				DialogHelper.showWarning("当前节点未做重命名操作！");
				return;
			}
			String newName = diff.getNewName();
			List<Difference> brothers = diff.getParent().getChildren();
			for (Difference brother : brothers) {
				if (diff.getType().equals(brother.getType())
						&& newName.equals(brother.getName())) {
					brother.setOp(OP.ADD);
				}
			}
			diff.setOp(OP.DELETE);
			diff.setNewName("");
			diff.setNewDesc("");
			refreshParentDiff();
		}
	}
	
	class OperationAction extends Action {
		
		private OP op;
		
		public OperationAction(OP op) {
			setText(op.getDesc());
			this.op = op;
		}
		
		@Override
		public void run() {
			Difference diff = getSelectedDiff();
			if (diff.getOp() == op) {
				return;
			}
			if (OP.RENAME == diff.getOp()) {
				DialogHelper.showWarning("不允许将重命名改为" + op.getDesc() + "！");
				return;
			}
			String newName = diff.getName();
			if (!StringUtil.isEmpty(newName)) {
				Difference diffRename = null;
				for (Difference brother : diff.getParent().getChildren()) {
					if (OP.RENAME == brother.getOp() && brother.getType().equals(diff.getType()) 
							&& newName.equals(brother.getNewName())) {
						diffRename = brother;
						break;
					}
				}
				if (diffRename != null) {
					DialogHelper.showWarning(newName + "已被" + diffRename.getName() + "用作新名称，不允许改为" + op.getDesc() + "！");
					return;
				}
			}
			diff.setOp(op);
			refreshParentDiff();
		}
	}
	
	protected void addListeners() {
		btnExport.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (diffs == null || diffs.size()==0)
					return;
				Shell shell = Display.getDefault().getActiveShell();
				final String fileName = FileDialogHelper.selectExcelFile2007(shell, SWT.SAVE);
				if (fileName == null)
					return;
				TaskManager.addTask(new Job("正在导出.....") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						ExcelUtils.addValue("diffs", diffs);
						ExcelFileManager2007.saveExcelFile(SCLCompareEditor.this.getClass(),
								RSCConstants.SCL_COMPARE, fileName);
						ConsoleManager.getInstance().append("比较结果已导出至：" + fileName);
						return Status.OK_STATUS;
					}
				});
			}
		});
		btnImport.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ProgressManager.execute(new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
							InterruptedException {
						new IncrementImportor(monitor, diffs).handle();
						ConsoleManager.getInstance().append("增量导入已完成！");
					}
				}, false);
			}
		});
	}

	@Override
	public void initData() {
		this.diffs = (List<Difference>) input.getData();
		boolean hasDiffs = diffs.size() > 0;
		if (hasDiffs) {
			SwtUtil.setVisible(lb, false);
			SwtUtil.setVisible(cmpDiff, true);
			treetable.setInput(diffs.toArray());
			treetable.getTreeViewer().expandAll();
		} else {
			SwtUtil.setVisible(lb, true);
			SwtUtil.setVisible(cmpDiff, false);
		}
	}

	private Difference getSelectedDiff() {
		return (Difference) treetable.getSelection();
	}
	
	private void refreshParentDiff() {
		Difference diff = getSelectedDiff();
		Difference diffParent = diff.getParent();
		treetable.getTreeViewer().refresh(diffParent);
	}
}
