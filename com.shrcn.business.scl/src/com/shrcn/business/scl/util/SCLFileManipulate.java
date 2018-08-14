package com.shrcn.business.scl.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.shrcn.business.scl.SCLConstants;
import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.scl.das.CRCInfoDao;
import com.shrcn.business.scl.das.DataTypeTemplateDao;
import com.shrcn.business.scl.das.HistoryDAO;
import com.shrcn.business.scl.history.HistoryManager;
import com.shrcn.business.scl.history.VersionSequence;
import com.shrcn.business.scl.model.Hitem;
import com.shrcn.business.scl.ui.HItemEditDialog;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.common.util.TimeCounter;
import com.shrcn.found.file.fpk.FileItem;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.xmldb.XMLDBHelper;

public class SCLFileManipulate {


	/** 文件能否保存开关。主接线图模不一致时，不允许保存。 */
	private static boolean allowSave = true;
	
	private static boolean isReturn = false;
	

//	/**
//	 * 为指定文件添加CRC后缀(需要先进行是否需要CRC检查的判断)
//	 * 
//	 * @param fPath
//	 * @return
//	 */
//	private static String addCRCSuffix(String fPath, final IProgressMonitor monitor){
//		File f = new File(fPath);
//		if (!f.exists() || !SCTProperties.getInstance().needsCRCCheck()) {
//			return fPath;
//		}
//		monitor.beginTask("增加CRC后缀...", 4);
//		String crcCode = FileManipulate.getCRC32Code(fPath);
//		String newPath = FileManipulate.addSuffix(fPath, crcCode);
//		String graphPath = getSCDGraphPath(fPath);
//		String newGraphPath = FileManipulate.addSuffix(graphPath, crcCode);
//		monitor.worked(2);
//		if (newPath == null)
//			return null;
//		FileManipulate.copyByChannel(f, new File(newPath));
//		monitor.worked(1);
//		if (!f.delete())
//			SCTLogger.error(fPath + "删除失败！");
//		if (newGraphPath == null)
//			return newPath;
//		File gf = new File(graphPath);
//		FileManipulate.copyByChannel(gf, new File(newGraphPath));
//		monitor.worked(1);
//		if (!gf.delete())
//			SCTLogger.error(graphPath + "删除失败！");
//		return newPath;
//	}
	
	/***
	 * 保存工程文件
	 */
	public static void save() {
		allowSave = true;
		String fPath = Constants.oldFile;
		if (fPath != null && new File(fPath).isFile()) {// 新建的文件已经保存,则直接保存，不需要弹出保存对话框
			if (Constants.HisVer){
				if (DialogHelper.showConfirm("您打开的历史版本，保存后是否替换主工程文件内容？", "是", "否")){
					fPath = Constants.mainFile;
				} else {
					if (DialogHelper.showConfirm("是否另存为其它工程文件？选择是则继续，选择否则不保存", "是", "否")){
						saveAs();
						return;
					} else {
						return;
					}
				}
			}
			// 保存单线图，放置saveFile(fPath)语句之前，这样能将拓扑也写入文件中
			saveSingleLine(fPath, true);
			if (!allowSave)
				return;
			if (hasHistory() && !saveHistory()) {
				return;
			}
			final String fileName = fPath;
			IRunnableWithProgress prog = new IRunnableWithProgress() {
				@Override
				public void run(final IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					saveFile(fileName, monitor);
					monitor.done();
				}
			};
			ProgressManager.execute(prog);
		} else {
			String fPath1 = DialogHelper.selectFile(Display.getCurrent().getActiveShell(), SWT.SAVE, Constants.SCD_FILENAME);
			Constants.oldFile = fPath1;
			// 点击取消时 name = null
			if (!StringUtil.isEmpty(fPath1)) {
				if (!fPath1.endsWith(".scd")) { //$NON-NLS-1$
					fPath1 += ".scd"; //$NON-NLS-1$
				}
				// 保存单线图，放置saveFile(fPath)语句之前，这样能将拓扑也写入文件中
				saveSingleLine(fPath1, true);
				if (!allowSave){
					return;
				}
				if (hasHistory() && !saveHistory()){
					return;
				}
				final String filePath = fPath1;
				IRunnableWithProgress prog = new IRunnableWithProgress() {
					@Override
					public void run(final IProgressMonitor monitor)
							throws InvocationTargetException, InterruptedException {
						saveFile(filePath, monitor);
//						Constants.oldFile = addCRCSuffix(filePath, monitor);
						monitor.done();
					}
				};
				ProgressManager.execute(prog);
			} 
		}
	}
	
	public static String saveAs() {
		allowSave = true;
		FileDialog fDlg = new FileDialog(Display.getCurrent().getActiveShell(),
				SWT.SAVE);
		fDlg.setFilterExtensions(new String[] { Constants.SCD_FILENAME,
				Constants.SSD_FILENAME });
		if (Constants.oldFile != null && !Constants.HisVer) {
			String oldFile = Constants.oldFile;
			File f = new File(oldFile);
			String filename = f.getName();
			fDlg.setFilterPath(filename);
		}
		final String fPath = fDlg.open();
		// 点击取消时 name = null
		if (fPath != null) {
			if (new File(fPath).exists()
					&& !DialogHelper.showConfirm("文件已存在，确定要覆盖吗？", "是", "否")) {
				return null;
			}
			Constants.HisVer = false;
			Constants.mainFile = null;
			IRunnableWithProgress prog = new IRunnableWithProgress() {
				@Override
				public void run(final IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					final String filePath = fPath;
					monitor.beginTask("保存单线图...", 1);
					// 导出.graph文件(放在同目录下)，放置saveFile(fPath)语句之前，这样能将拓扑也写入文件中
					saveSingleLine(filePath, true);
					monitor.worked(1);
					
					if (!allowSave){
						monitor.done();
						return;
					}
					if (filePath.endsWith(".scd")) { //$NON-NLS-1$
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								if (hasHistory() && !saveHistory()) {
									monitor.done();
									isReturn = true; 
								}
							}
						});
						if (!isReturn)
							saveFile(filePath, monitor);
//						filePath = addCRCSuffix(filePath, monitor);
						Constants.oldFile = filePath;
					}
					if (filePath.endsWith(".ssd")) { //$NON-NLS-1$
						// 导出普通格式的ssd文件
						SSDUtil.export(filePath);
					}
					monitor.done();
				}
			};
			ProgressManager.execute(prog);
		}
		return fPath;
	}
	
	public static boolean hasHistory() {
		return HistoryManager.getInstance().hasHistory();
	}

	public static boolean saveHistory() {
		HItemEditDialog hdlg = new HItemEditDialog(Display.getCurrent().getActiveShell());
		if (hdlg.open() == HItemEditDialog.OK) {
			Hitem hitem = hdlg.getHitem();
			HistoryDAO.addHitem(hitem);
			HistoryManager.getInstance().reset();
			saveVersions(hitem,true);
			return true;
		}
		return false;
	}
	
	public static void saveVersions(Hitem hitem,boolean flag) {
		String fileName = Constants.oldFile;
		if (Constants.HisVer){
			fileName = Constants.mainFile;
		}
		String temp = Constants.getVerFile(fileName);
		List<Element> verItems = readVerItems(temp);
		if (verItems==null || verItems.isEmpty()){
			verItems = new ArrayList<Element>();
			Element his = getInitVersion();
			verItems.add(his);
		}
		if (flag){
			//计算发布版本的下一个版本号
			Element lastEle = verItems.get(verItems.size()-1);
			VersionSequence sequence = VersionSequence.getInstance();
			String version = "";
			String revision = "";
			String ver = lastEle.attributeValue("version");
			String reVer = lastEle.attributeValue("revision");
			float fver = Float.parseFloat(ver);
			float freVer = Float.parseFloat(reVer);
			float[] next = sequence.nextReVer(fver,freVer);
			version = Float.toString(next[0]);
			revision = Float.toString(next[1]);
			hitem.setVersion(version);
			hitem.setRevision(revision);
		}
		if (hitem!=null){
			verItems.add(DOM4JNodeHelper.parseText2Node(hitem.asXML()));
		}
		saveXml(verItems,temp);
	}

	public static Element getInitVersion(){
		Date date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Element his = DOM4JNodeHelper.createSCLNode("Hitem");
		his.addAttribute("version", "1.0");
		his.addAttribute("revision", "1.0");
		his.addAttribute("when", dateformat.format(date)); 
		his.addAttribute("who", "Admin");
		his.addAttribute("what", "无");
		his.addAttribute("why", "初始提交");
		return his;
	}
	
	/**
	 * 根据配置文件中选项，基于非统一建模的工程，保存时清理模板
	 */
	private static void clearTemplet() {
		if (SCTProperties.getInstance().getTempletModel().equals(SCLConstants.NOT_UNIFIED_TEMPLET)) {
			DataTypeTemplateDao.clearTemplates();// 清理模板中无用的类型
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					EventManager.getDefault().notify(SCLConstants.DATATEMPLATE_REFRESH, "");
				}});
		}
	} 
	
	public static void saveFile(final String outpath, final IProgressMonitor monitor){
		monitor.beginTask(Messages.getString("FileManipulate.checking_project"), 2);
		// 根据配置文件中选项，基于非统一建模的工程，保存时清理模板
		clearTemplet();
		if (monitor != null)
			monitor.worked(1);
		TimeCounter.end("清空模板");
		if (monitor != null)
			monitor.worked(1);
		if (SCTProperties.getInstance().needsCRCCheck()) {
			if (!Constants.XQUERY) { // VTD方式需先保存修改内容
				FileManipulate.initDirs(Constants.tempDir);
				String temppath = Constants.tempDir + new File(outpath).getName();
				XMLDBHelper.exportFormatedDoc(temppath);
			}
			CRCInfoDao.calcCRCCode(monitor);
		}/* else {
			CRCInfoDao.clearCRCCode();
		}*/
		monitor.setTaskName("保存scd文件...");
		XMLDBHelper.exportFormatedDoc(outpath);
		TimeCounter.end("保存文件");
	}
	
	/**
	 * 保存单线图，由于
	 * @param filepath
	 * @param saveAs
	 */
	public static void saveSingleLine(String filepath, boolean saveAs){
		// 保存单线图
		EventManager.getDefault().notify(SCLConstants.SAVE_GRAPH, saveAs, filepath);
	}

	public static void setAllowSave(boolean allowSave) {
		SCLFileManipulate.allowSave = allowSave;
	}
	
	/**
	 * 根据SCD文件路径获取对应的graph文件路径
	 * @param scdPath
	 * @return
	 */
	public static String getSCDGraphPath(String scdPath) {
		scdPath = scdPath.substring(0, scdPath.lastIndexOf('.'));
		scdPath = scdPath.concat(".graph");
		return scdPath;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<Element> readVerItems(String fileName) {
		File file = new File(fileName);
		Document doc = null;
		List<Element> rel = new ArrayList<Element>();
		if (!file.exists()) {
			return rel;
		} else {
			doc = XMLFileManager.loadXMLFile(fileName);
			if (doc==null) return rel;
			Element rootEle = doc.getRootElement();
			if (rootEle==null) return rel;
			rel = rootEle.elements("Hitem");
		}
		return rel;
	}

	private static void saveXml(List<Element> items, String fileName) {
		Document doc = DocumentHelper.createDocument();
		Element elRoot = doc.addElement("History");
		String mainFile = Constants.oldFile;
		if (Constants.HisVer){
			mainFile = Constants.mainFile;
		}
		File file =  new File(mainFile);
		String temp = Constants.getVerPath(mainFile);
		int i=0;
		for (Element element : items) {
			elRoot.add((Element)element.clone());
			String ver = String.valueOf(i);
			String newName = temp+File.separator+file.getName()+Constants.verName+ver;
			File file1 = new File(newName);
			if (!file1.exists()){
				FileItem.copy(Constants.oldFile, newName);
			}
			i++;
		}
		XMLFileManager.saveUTF8Document(doc, fileName);
		
	}
}
