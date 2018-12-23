/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.found.common.Constants;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.io.ied.Context;
import com.synet.tool.rsc.io.parser.OnlySubstationParser;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1044TerminalEntity;
import com.synet.tool.rsc.model.Tb1045ConnectivitynodeEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.service.SubstationService;
import com.synet.tool.rsc.util.ProblemManager;
import com.synet.tool.rsc.util.ProjectFileManager;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-14
 */
public class SSDImporter implements IImporter {

	private String scdPath;
	private RSCProperties rscp = RSCProperties.getInstance();
	private ProjectFileManager prjFileMgr = ProjectFileManager.getInstance();
	private BeanDaoService beanDao = BeanDaoImpl.getInstance();
	private SubstationService staServ = new SubstationService();
	private Map<String, Tb1042BayEntity> bayCache = new HashMap<>();
	
	public SSDImporter(String scdPath) {
		this.scdPath = scdPath;
		clearHistory();
	}
	
	private void clearHistory() {
		beanDao.deleteAll(Tb1043EquipmentEntity.class);
		beanDao.deleteAll(Tb1044TerminalEntity.class);
		beanDao.deleteAll(Tb1045ConnectivitynodeEntity.class);
		beanDao.deleteAll(Tb1067CtvtsecondaryEntity.class);
		beanDao.deleteAll(Tb1066ProtmmxuEntity.class);
	}

	@Override
	public void execute(IProgressMonitor monitor) {
		long begin = System.currentTimeMillis();
		ConsoleManager console = ConsoleManager.getInstance();
		XMLDBHelper.loadDocument(Constants.DEFAULT_SCD_DOC_NAME, scdPath);
		FileManipulate.copyByChannel(scdPath, ProjectManager.getProjectSsdPath());
		prjFileMgr.renameScd(Constants.CURRENT_PRJ_NAME, scdPath);
		Context context = new Context();
		OnlySubstationParser parser = new OnlySubstationParser(context, monitor);
		parser.parse();
		long t = (System.currentTimeMillis() - begin) / 1000;
		console.append("SSD导入耗时 " + t + " 秒。");
		ProblemManager.getInstance().append(context.getProblems());
	}

}

