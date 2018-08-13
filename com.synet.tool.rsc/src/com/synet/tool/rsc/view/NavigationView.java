package com.synet.tool.rsc.view;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.common.event.Context;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.editor.ConfigEditorInput;
import com.shrcn.found.ui.editor.EditorConfigData;
import com.shrcn.found.ui.model.ConfigTreeEntry;
import com.shrcn.found.ui.model.IEDEntry;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.tree.TreeViewerBuilder;
import com.shrcn.found.ui.view.ANavigationView;
import com.synet.tool.rsc.GlobalData;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.ui.EcfgTreeViewer;
import com.synet.tool.rsc.util.NavgTreeFactory;

public class NavigationView extends ANavigationView {
	
	public static final String ID = UIConstants.View_Navg_ID;

	private NavgTreeFactory treeFactory = NavgTreeFactory.getInstance();
	
	
//	private ProjectFileManager prjFileMng = ProjectFileManager.getInstance();
//	private ProjectManager prjMng = ProjectManager.getInstance();
	
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
//		if (EventConstants.PROJECT_OPEN_IMP.equals(event)) {
//			Constants.CURRENT_PRJ_PATH = DeviceDirManager.getWorkspacePrjPath(Constants.CURRENT_PRJ_NAME);
//			openPrj();
//		}
	}
	
	
	/**
	 * 创建工程。
	 */
	@Override
	protected void createProject() {
		iniTDB();
//		NewProjectDialog dlg = new NewProjectDialog(SwtUtil.getDefaultShell());
//		if (IDialogConstants.OK_ID == dlg.open()) {
//			closeProject();
//			
//			String projectName = dlg.getName();
//			String projectPath = dlg.getPath();
//			String note = dlg.getNote();
//			// 新建节点
//			prePrjOpen();
//			
//			//初始化数据库
//			Constants.CURRENT_PRJ_NAME  = projectName;
//			Constants.CURRENT_PRJ_PATH = DeviceDirManager.getProjectPath(projectName, projectPath);
//			NeoDBManager neoDbManager = NeoDBManager.getInstance();
//			neoDbManager.openDB(DeviceDirManager.getProjectFilePath());
//			prjFileMng.createProject(projectName, note, Constants.CURRENT_PRJ_PATH);
//			treeFactory.loadProject();
//			loadProject();
//			EventManager.getDefault().notify(EventConstants.CLEAR_CONFIG, null);
//		}
	}

	
	/**
	 * 打开工程。
	 */
	@Override
	protected void openProject() {
		iniTDB();
//		OpenProjectDialog dlg = new OpenProjectDialog(SwtUtil.getDefaultShell());
//		if (IDialogConstants.OK_ID == dlg.open()) {
//			closeProject();
//			
//			String name = dlg.getName();
//			String path = dlg.getPath();
//			boolean isPath = (name == null);
//			// 加载节点
//			prePrjOpen();
//			
//			if (isPath) {
//				String prjname = new File(path).getName();
//				Constants.CURRENT_PRJ_NAME = prjname;
//			} else {
//				Constants.CURRENT_PRJ_NAME = name;
//			}
//			Constants.CURRENT_PRJ_PATH = DeviceDirManager.getProjectPath(Constants.CURRENT_PRJ_NAME, path);
//			
//			// 打开工程操作
//			openPrj();
//		}
	}
	

	@Override
	public void loadProject() {
		iniTDB();
		treeFactory.loadProject();
		cfgViewer.setInput(treeFactory.getProjectData());
		cfgViewer.expandAll();
	}

	@Override
	protected void closeProject() {
		
	}
	
	@Override
	protected void addListeners() {
		cfgViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ITreeEntry selEntry = cfgViewer.getSelTreeEntry();
				
				if (selEntry == null)
					return;
				if (selEntry instanceof ConfigTreeEntry) {
					openConfig(selEntry);
				}
			}
		});
		
	}
	
	private void iniTDB() {
		String dbName = "RscData";
		ProjectManager instance = ProjectManager.getInstance();
		instance.initDb(dbName);
		instance.openDb(dbName);
	}
	
}