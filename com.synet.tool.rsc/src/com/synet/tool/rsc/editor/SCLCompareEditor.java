package com.synet.tool.rsc.editor;

import java.util.List;

import net.sf.excelutils2007.ExcelUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
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

import com.shrcn.found.file.excel.ExcelFileManager;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.treetable.FixedTreeTableAdapterFactory;
import com.shrcn.found.ui.treetable.TreeTable;
import com.shrcn.found.ui.util.FileDialogHelper;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.ui.util.ImgDescManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.util.TaskManager;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.editor.tree.DescField;
import com.synet.tool.rsc.editor.tree.DiffTreeTableAdapter;
import com.synet.tool.rsc.editor.tree.MsgField;
import com.synet.tool.rsc.editor.tree.NameField;
import com.synet.tool.rsc.editor.tree.NewDescField;
import com.synet.tool.rsc.editor.tree.NewNameField;
import com.synet.tool.rsc.editor.tree.OpField;
import com.synet.tool.rsc.editor.tree.TypeField;

public class SCLCompareEditor extends BaseConfigEditor {

	private Label lb;
	private Composite cmpDiff;
	private Button btnExport;
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
		cmpDiff.setLayout(new GridLayout(1, false));
		IField[] fields = new IField[] {new TypeField(), new NameField(), new DescField(), 
				new NewNameField(), new NewDescField(),new MsgField(), new OpField()};
		this.treetable = new TreeTable(cmpDiff, SWT.MULTI | SWT.FULL_SELECTION, fields, 
				new FixedTreeTableAdapterFactory(DiffTreeTableAdapter.instance));
		treetable.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		final Composite bottom = new Composite(cmpDiff, SWT.NONE);
		bottom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final GridLayout layout = new GridLayout(2, false);
		layout.marginLeft = -5;
		layout.marginRight = -5;
		bottom.setLayout(layout);
		btnExport = new Button(bottom, SWT.PUSH);
		btnExport.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, false));
		btnExport.setImage(ImgDescManager.getImageDesc(ImageConstants.EXCEL).createImage());
		btnExport.setText("导出Excel文件");
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
						ExcelFileManager.saveExcelFile(SCLCompareEditor.this.getClass(),
								RSCConstants.SCL_COMPARE, fileName);
						return Status.OK_STATUS;
					}
				});
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
}
