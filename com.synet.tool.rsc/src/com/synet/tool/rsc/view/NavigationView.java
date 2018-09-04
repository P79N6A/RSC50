package com.synet.tool.rsc.view;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.common.event.Context;
import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.common.valid.NewNameValidator;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.file.util.ZipUtil;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.app.MenuToolFactory;
import com.shrcn.found.ui.dialog.InputDialog;
import com.shrcn.found.ui.editor.ConfigEditorInput;
import com.shrcn.found.ui.editor.EditorConfigData;
import com.shrcn.found.ui.model.ConfigTreeEntry;
import com.shrcn.found.ui.model.IEDEntry;
import com.shrcn.found.ui.tree.TreeViewerBuilder;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.ANavigationView;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.dialog.HistoryProjectDialog;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.ui.EcfgTreeViewer;
import com.synet.tool.rsc.util.DataUtils;
import com.synet.tool.rsc.util.NavgTreeFactory;
import com.synet.tool.rsc.util.ProjectFileManager;

public class NavigationView extends ANavigationView {
	
	public static final String ID = UIConstants.View_Navg_ID;

	private ProjectManager prjmgr = ProjectManager.getInstance();
	private ProjectFileManager prjFileMgr = ProjectFileManager.getInstance();
	private NavgTreeFactory treeFactory = NavgTreeFactory.getInstance();
	
	protected void createTV(Composite parent){
		TreeViewerBuilder treeBuilder = treeFactory.getTreeBuilder();
		cfgViewer = new EcfgTreeViewer(parent, treeBuilder);
	}

	protected ConfigEditorInput getInput(ConfigTreeEntry configEntry, IEDEntry iedEntry, String editorId) {
//		BayIEDEntry bayEntry = (BayIEDEntry) iedEntry;
//		EditorConfigData data = new EditorConfigData(bayEntry.getName(), null, 0, ((IEDEntry)bayEntry.getParent()).getType());
		EditorConfigData data = new EditorConfigData(configEntry.getName(), null, 0, configEntry.getName());
		data.setData(configEntry.getData());
		return new ConfigEditorInput(configEntry.getName(), configEntry.getIcon(), editorId, data);
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		cfgViewer.getControl().setFocus();
	}
	
	@Override
	public void execute(Context context) {
		super.execute(context);
		String event = context.getEventName();
		Object data = context.getData();
//		if (RscEventConstants.PROJECT_NEW.equals(event)) {
//			String priName = (String) data;
//			prjmgr.initDb(priName);
//			loadProject();
//		} else if (RscEventConstants.PROJECT_OPEN.equals(event)) {
//			String priName = (String) data;
//			prjmgr.openDb(priName);
//			loadProject();
//		}
		MenuToolFactory.getInstance().refreshMenuTools();
	}
	
	
	/**
	 * 创建工程。
	 */
	@Override
	protected void createProject() {
		NewNameValidator validator = new NewNameValidator(prjFileMgr.getHistoryItems());
		InputDialog dlg = new InputDialog(SwtUtil.getDefaultShell(), "新建工程", "请输入工程名称", "", validator);
		if (IDialogConstants.OK_ID == dlg.open()) {
			closeProject();
			final String prjName = dlg.getValue();
			Constants.CURRENT_PRJ_NAME  = prjName;
			prjFileMgr.addProject(prjName, null);
			prjFileMgr.setClosed(false);
			ProgressManager.execute(new IRunnableWithProgress() {
				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					monitor.beginTask("正在创建...", 3);
					monitor.worked(1);
					prjmgr.initDb(prjName);
					prjmgr.openDb(prjName);
					initIntervalDict();
					monitor.worked(1);
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							loadProject();
							EventManager.getDefault().notify(EventConstants.CLEAR_CONFIG, null);
							monitor.done();
						}});
				}
			});
		}
	}

	
	/**
	 * 打开工程。
	 */
	@Override
	protected void openProject() {
		HistoryProjectDialog dlg = new HistoryProjectDialog(SwtUtil.getDefaultShell());
		if (IDialogConstants.OK_ID == dlg.open()) {
			closeProject();
			final String prjName = dlg.getValue();
			Constants.CURRENT_PRJ_NAME  = prjName;
			prjFileMgr.setClosed(false);
			ProgressManager.execute(new IRunnableWithProgress() {
				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					monitor.beginTask("正在打开...", 3);
					monitor.worked(1);
					prjmgr.openDb(prjName);
					initIntervalDict();
					monitor.worked(1);
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							loadProject();
							EventManager.getDefault().notify(EventConstants.CLEAR_CONFIG, null);
							monitor.done();
						}});
				}
			});
		}
	}
	

	@Override
	public void loadProject() {
		treeFactory.loadProject();
		cfgViewer.setInput(treeFactory.getProjectData());
		cfgViewer.expandAll();
	}

	/**
	 * 初始化间隔字典
	 */
	private void initIntervalDict() {
		BeanDaoImpl instance = BeanDaoImpl.getInstance();
		@SuppressWarnings("unchecked")
		List<Tb1042BayEntity> allBay = (List<Tb1042BayEntity>) instance.getAll(Tb1042BayEntity.class);
		if(!DataUtils.listNotNull(allBay)) {
			return;
		}
		for (Tb1042BayEntity tb1042BayEntity : allBay) {
			DictManager.getInstance().addItemByType("ALL_INTERVAL", tb1042BayEntity.getF1042Name());
		}
	}

	@Override
	protected void closeProject() {
		Constants.CURRENT_PRJ_NAME  = null;
		prjFileMgr.setClosed(true);
		prjmgr.closeDB();
		loadProject();
		EventManager.getDefault().notify(EventConstants.CLEAR_CONFIG, null);
	}

	@Override
	protected void exportProject() {
		final String path = DialogHelper.selectFile(SwtUtil.getDefaultShell(), SWT.SAVE, "*.data");
		ProgressManager.execute(new IRunnableWithProgress() {
			@Override
			public void run(final IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
				monitor.beginTask("正在打开...", 3);
				monitor.worked(1);
				ZipUtil.zip(ProjectManager.getProjectDir(Constants.CURRENT_PRJ_NAME), path);
				monitor.worked(1);
				ConsoleManager.getInstance().append("当前工程已导出至 " + path);
				monitor.done();
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
					}});
			}
		});
	}

	@Override
	protected void importProject() {
		final String path = DialogHelper.selectFile(SwtUtil.getDefaultShell(), SWT.OPEN, "*.data");
		final String prjName = FileManipulate.getName(path);
		ProgressManager.execute(new IRunnableWithProgress() {
			@Override
			public void run(final IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
				monitor.beginTask("正在导入...", 3);
				monitor.worked(1);
				ZipUtil.unzip(path, ProjectManager.getProjectDir(prjName));
				Constants.CURRENT_PRJ_NAME  = prjName;
				prjFileMgr.addProject(prjName, null);
				prjFileMgr.setClosed(false);
				prjmgr.openDb(prjName);
				initIntervalDict();
				monitor.worked(1);
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						loadProject();
						EventManager.getDefault().notify(EventConstants.CLEAR_CONFIG, null);
						monitor.done();
					}});
			}
		});
	}
	
}