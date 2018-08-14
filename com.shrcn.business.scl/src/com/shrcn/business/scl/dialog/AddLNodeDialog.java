/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.dialog;

import java.util.ArrayList;
import java.util.Comparator;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;

import com.shrcn.business.xml.schema.EnumLNUtil;
import com.shrcn.business.xml.schema.LnClass;
import com.shrcn.found.ui.app.WrappedTitleAreaDialog;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * 
 * @author 聂国勇(mailto:nguoyong@shrcn.com)
 * @version 1.0, 2011-1-21
 */
/**
 * $Log: AddLNodeDialog.java,v $ Revision 1.1 2013/03/29 09:37:37 cchun Add:创建
 * 
 * Revision 1.1 2011/09/15 08:39:00 cchun Refactor:创建公用对话框包
 * 
 * Revision 1.1 2011/07/13 08:45:17 cchun Refactor:移动到common插件
 * 
 * Revision 1.2 2011/01/25 03:51:17 cchun Update:整理代码
 * 
 * Revision 1.1 2011/01/25 02:05:22 cchun Add:聂国勇增加，增加同时添加多个逻辑节点功能
 * 
 */
public class AddLNodeDialog extends WrappedTitleAreaDialog {
	private Object[] checkedObjs = null;
	private CheckboxTreeViewer fTreeViewer;
	private ILabelProvider fLabelProvider;
	private ITreeContentProvider fContentProvider;
	private ViewerComparator fComparator;
	private Object fInput;

	private class LNodeLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			return super.getImage(element);
		}

		@Override
		public String getText(Object element) {
			if (element instanceof LnClass) {
				String name = ((LnClass) element).getName();
				String desc = ((LnClass) element).getDesc();
				return name + "--" + desc;
			}
			return "";
		}

	}

	private class LNodeContentProvider implements ITreeContentProvider {
		public Object[] getChildren(Object parentElement) {
			if (parentElement != null && parentElement instanceof LnClass) {
				return ((LnClass) parentElement).getChildren().toArray();
			}
			return null;
		}

		public Object getParent(Object element) {
			if (element instanceof LnClass) {
				return ((LnClass) element).getParent();
			}
			return null;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof LnClass) {
				return ((LnClass) element).getChildren().size() > 0;
			}
			return false;
		}

		@SuppressWarnings("rawtypes")
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ArrayList)
				return ((ArrayList) inputElement).toArray();
			return null;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class LNodeComparator extends ViewerComparator {

		@Override
		public int category(Object element) {
			return super.category(element);
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			return super.compare(viewer, e1, e2);
		}

		@Override
		protected Comparator<?> getComparator() {
			return super.getComparator();
		}

		@Override
		public boolean isSorterProperty(Object element, String property) {
			return super.isSorterProperty(element, property);
		}

		@Override
		public void sort(Viewer viewer, Object[] elements) {
			super.sort(viewer, elements);
		}
	}

	public AddLNodeDialog(Shell parentShell) {
		super(parentShell);
		fLabelProvider = new LNodeLabelProvider();
		fContentProvider = new LNodeContentProvider();
		fComparator = new LNodeComparator();
		fInput = EnumLNUtil.getXMLLNs();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("设置逻辑节点");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("设置逻辑节点");
		setMessage("设置逻辑节点");
		Composite composite = SwtUtil.createComposite(parent, new GridData(
				GridData.FILL_BOTH), 1);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite treeComposite = SwtUtil.createComposite(composite, gridData,
				1);
		createTreeViewer(treeComposite);
		return super.createDialogArea(parent);
	}

	private void createTreeViewer(Composite parent) {
		fTreeViewer = new ContainerCheckedTreeViewer(parent, SWT.BORDER);
		fTreeViewer.getControl()
				.setLayoutData(new GridData(GridData.FILL_BOTH));
		fTreeViewer.setContentProvider(fContentProvider);
		fTreeViewer.setLabelProvider(fLabelProvider);
		fTreeViewer.setComparator(fComparator);
		fTreeViewer.setInput(fInput);
	}

	public Object[] getCheckedElements() {
		return checkedObjs;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID)
			checkedObjs = fTreeViewer.getCheckedElements();
		else
			checkedObjs = null;
		super.buttonPressed(buttonId);
	}
}
