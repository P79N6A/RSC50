package com.synet.tool.rsc.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.IProgressMonitor;
import org.hibernate.Session;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.tool.found.das.SessionService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.das.SessionRsc;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.service.AnalogdataService;
import com.synet.tool.rsc.service.PinEntityService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.service.StatedataService;
import com.synet.tool.rsc.util.SqlHelper;

public class TemplateImportFast implements IImporter{

	private static final String tempSql = Constants.tempDir + "/tmlimp.sql";
	private StatedataService statedataService;
	private AnalogdataService analogdataService;
	private BeanDaoImpl beanDao;
	private Tb1046IedEntity tb1046IedEntity;
	private PoutEntityService poutEntityService;
	private PinEntityService pinEntityService;
	private BufferedWriter sqlWriter;
	
	public TemplateImportFast(Tb1046IedEntity tb1046IedEntity) {
		this.tb1046IedEntity = tb1046IedEntity;
		statedataService = new StatedataService();
		analogdataService = new AnalogdataService();
		poutEntityService = new PoutEntityService();
		pinEntityService = new PinEntityService();
		beanDao = BeanDaoImpl.getInstance();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(IProgressMonitor monitor) {
		FileManipulate.initDir(Constants.tempDir);
		try {
			sqlWriter = new BufferedWriter(new FileWriter(new File(tempSql)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String f1046Manufacturor = tb1046IedEntity.getF1046Manufacturor();
		String f1046Model = tb1046IedEntity.getF1046Model();
		String f1046ConfigVersion = tb1046IedEntity.getF1046ConfigVersion();
		if(f1046Manufacturor.isEmpty() || f1046Model.isEmpty() || f1046ConfigVersion.isEmpty()) {
			DialogHelper.showAsynInformation("数据配置不完善，无法查找模版");
			return;
		}
		String fileName = f1046Manufacturor + f1046Model + f1046ConfigVersion;
		String path = Constants.tplDir + fileName + ".xml";
		File file = new File(path);
		if(!file.exists()) { //判断是否存在模版
			DialogHelper.showAsynInformation("当前设备类型模版不存在！");
			return;
		}
		
		SAXReader reader = new SAXReader();
		reader.setEncoding("UTF-8");
		try {
			Document document = reader.read(new File(path));
			Element rootElement = document.getRootElement();
			Element elementBoards = rootElement.element("Boards");
			List<Element> elementsBoardEntity = elementBoards.elements("Tb1047BoardEntity");
			for (Element elementBoardEntity : elementsBoardEntity) {
				Tb1047BoardEntity boardEntity = saveBoard(elementBoardEntity);
				List<Element> elementsPortEntity = elementBoardEntity.elements("Tb1048PortEntity");
				for (Element elementPortEntity : elementsPortEntity) {
					savePort(boardEntity, elementPortEntity);
				}
			}
			Element elementStraps = rootElement.element("Straps");
			Element elementPouts = elementStraps.element("Pouts");
			List<Element> elementsPoutEntity = elementPouts.elements("Tb1061PoutEntity");
			for (Element elementPoutEntity : elementsPoutEntity) {
				savePout(elementPoutEntity);
			}
			Element elementPins = elementStraps.element("Pins");
			List<Element> elementsPinEntity = elementPins.elements("Tb1062PinEntity");
			for (Element elementPinEntity : elementsPinEntity) {
				savePin(elementPinEntity);
			}
			Element elementDType = rootElement.element("DataType");
			Element elementSts = elementDType.element("State");
			Element elementAlgs = elementDType.element("Analog");
			Element elementPin = elementDType.element("Pin");
			saveStates(elementSts);
			saveAnalogs(elementAlgs);
			savePins(elementPin);
			sqlWriter.flush();
			sqlWriter.close();
			SqlHelper.runScript(new FileInputStream(tempSql));
		} catch (DocumentException e) {
			ConsoleManager.getInstance().append("文件读取失败" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveStates(Element elementSts) {
			List<Element> stList = elementSts.elements();
			List<Element> poutList = new ArrayList<>();
			List<Element> strapList = new ArrayList<>();
			List<Element> mmsList = new ArrayList<>();
//			SessionService service = SessionRsc.getInstance();
//			PreparedStatement pState = conn.prepareStatement("update TB1016_StateData set F1011_NO=? where F1016_ADDREF=? and F1046_CODE=?");
			try {
				for (Element elementSt : stList) {
					String ref = elementSt.attributeValue("ref");
					String f1011NoStr = elementSt.attributeValue("f1011No");
					String typeStr = elementSt.attributeValue("type");
					int f1011No = Integer.parseInt(f1011NoStr);
					int type = Integer.parseInt(typeStr);
					if (DBConstants.DTYPE_POUT == type) {
						poutList.add(elementSt);
					} else { 
						if (DBConstants.DTYPE_STRAP == type) {
							strapList.add(elementSt);
						}
						mmsList.add(elementSt);
					}
					sqlWriter.write("update TB1016_STATEDATA set F1011_NO=" + f1011No +
							" where F1016_ADDREF='" + ref +
							"' and F1046_CODE='" + tb1046IedEntity.getF1046Code() +
							"';\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			savePoutType(poutList);
			saveStrapType(strapList);
			saveMmsType(mmsList);
	}
	
	private void savePoutType(List<Element> poutList) {
		try {
			SessionService service = SessionRsc.getInstance();
			Session _session = service.get();
			Connection conn = _session.connection();
			conn.setAutoCommit(false);
			PreparedStatement pState = conn.prepareStatement("update TB1061_POUT set F1061_Type=? where F1061_RefAddr=? and F1046_CODE=?");
			int i = 0;
			for (Element elementSt : poutList) {
				String ref = elementSt.attributeValue("ref");
				String f1011NoStr = elementSt.attributeValue("f1011No");
				int f1011No = Integer.parseInt(f1011NoStr);
				pState.setInt(1, f1011No);
				pState.setString(2, ref);
				pState.setString(3, tb1046IedEntity.getF1046Code());
				pState.addBatch();
				i++;
				if (i % 50 == 0) {
					pState.executeBatch();
					conn.commit();
				}
			}
			if (i % 50 > 0) {
				pState.executeBatch();
				conn.commit();
			}
		} catch (SQLException e) {
			ConsoleManager.getInstance().append("数据导入错误" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void saveMmsType(List<Element> mmsList) {
		try {
//			SessionService service = SessionRsc.getInstance();
//			Session _session = service.get();
//			Connection conn = _session.connection();
//			conn.setAutoCommit(false);
//			PreparedStatement pState = conn.prepareStatement("update TB1058_MMSFCDA set F1058_Type=? where F1058_RefAddr=? and F1046_CODE=?");
			for (Element elementSt : mmsList) {
				String ref = elementSt.attributeValue("ref");
				String f1011NoStr = elementSt.attributeValue("f1011No");
				int f1011No = Integer.parseInt(f1011NoStr);
//				pState.setInt(1, f1011No);
//				pState.setString(2, ref);
//				pState.setString(3, tb1046IedEntity.getF1046Code());
//				pState.addBatch();
				sqlWriter.write("update TB1058_MMSFCDA set F1058_Type=" + f1011No +
						" where F1058_RefAddr='" + ref +
						"' and F1046_CODE='" + tb1046IedEntity.getF1046Code() +
						"';\n");
			}
		} catch (IOException e) {
			ConsoleManager.getInstance().append("数据导入错误" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void saveStrapType(List<Element> strapList) {
		try {
//			SessionService service = SessionRsc.getInstance();
//			Session _session = service.get();
//			Connection conn = _session.connection();
//			conn.setAutoCommit(false);
//			PreparedStatement pState = conn.prepareStatement("update TB1064_STRAP a set a.F1064_TYPE=? where a.F1064_CODE=" +
//					"(select b.Parent_CODE from TB1016_StateData b where b.F1016_ADDREF=? and b.F1046_CODE=?)");
			for (Element elementSt : strapList) {
				String ref = elementSt.attributeValue("ref");
				String f1011NoStr = elementSt.attributeValue("f1011No");
				int f1011No = Integer.parseInt(f1011NoStr);
//				pState.setInt(1, f1011No);
//				pState.setString(2, ref);
//				pState.setString(3, tb1046IedEntity.getF1046Code());
//				pState.addBatch();
				sqlWriter.write("update TB1064_STRAP a set a.F1064_TYPE=" + f1011No +
						" where a.F1064_CODE=" +
					"(select b.Parent_CODE from TB1016_StateData b where b.F1016_ADDREF='" + ref +
					"' and b.F1046_CODE='" + tb1046IedEntity.getF1046Code() +
					"');\n");
			}
		} catch (IOException e) {
			ConsoleManager.getInstance().append("数据导入错误" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void saveAnalogs(Element elementAlgs) {
		try {
			List<Element> algList = elementAlgs.elements();
			List<Element> poutList = new ArrayList<>();
			List<Element> mmsList = new ArrayList<>();
//			SessionService service = SessionRsc.getInstance();
//			Session _session = service.get();
//			Connection conn = _session.connection();
//			conn.setAutoCommit(false);
//			PreparedStatement pState = conn.prepareStatement("update TB1006_AnalogData set F1011_NO=? where F1006_ADDREF=? and F1046_CODE=?");
//			int i = 0;
			for (Element elementSt : algList) {
				String ref = elementSt.attributeValue("ref");
				String f1011NoStr = elementSt.attributeValue("f1011No");
				String typeStr = elementSt.attributeValue("type");
				int f1011No = Integer.parseInt(f1011NoStr);
				int type = Integer.parseInt(typeStr);
//				pState.setInt(1, f1011No);
//				pState.setString(2, ref);
//				pState.setString(3, tb1046IedEntity.getF1046Code());
//				pState.addBatch();
				if (DBConstants.DTYPE_POUT == type) {
					poutList.add(elementSt);
				} else { 
					mmsList.add(elementSt);
				}
				sqlWriter.write("update TB1006_ANALOGDATA set F1011_NO=" + f1011No +
						" where F1006_ADDREF='" + ref +
						"' and F1046_CODE='" + tb1046IedEntity.getF1046Code() +
						"';\n");
			}
			saveMmsType(mmsList);
			savePoutType(poutList);
		} catch (IOException e) {
			ConsoleManager.getInstance().append("数据导入错误" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void savePins(Element elementPin) {
		try {
			List<Element> algList = elementPin.elements();
//			SessionService service = SessionRsc.getInstance();
//			Session _session = service.get();
//			Connection conn = _session.connection();
//			conn.setAutoCommit(false);
//			PreparedStatement pState = conn.prepareStatement("update TB1062_PIN set F1011_NO=? where F1062_RefAddr=? and F1046_CODE=?");
//			int i = 0;
			for (Element elementSt : algList) {
				String ref = elementSt.attributeValue("ref");
				String f1011NoStr = elementSt.attributeValue("f1011No");
				int f1011No = Integer.parseInt(f1011NoStr);
//				pState.setInt(1, f1011No);
//				pState.setString(2, ref);
//				pState.setString(3, tb1046IedEntity.getF1046Code());
//				pState.addBatch();
				sqlWriter.write("update TB1062_PIN set F1011_NO=" + f1011No +
						" where F1062_RefAddr='" + ref +
						"' and F1046_CODE='" + tb1046IedEntity.getF1046Code() +
						"';\n");
			}
		} catch (IOException e) {
			ConsoleManager.getInstance().append("数据导入错误" + e.getMessage());
			e.printStackTrace();
		}
	}

	private void savePin(Element elementPinEntity) {
		String strapRefAddr = elementPinEntity.attributeValue("strapRefAddr");
		Map<String, Object> params = new HashMap<>();
		params.put("f1016AddRef", strapRefAddr);
		params.put("tb1046IedByF1046Code", tb1046IedEntity);
		Tb1016StatedataEntity stateEntity = (Tb1016StatedataEntity) beanDao.getObject(Tb1016StatedataEntity.class, params);
		if(stateEntity == null) {
			return;
		}
		Tb1064StrapEntity strapEntity = (Tb1064StrapEntity) beanDao.getObject(Tb1064StrapEntity.class, "f1064Code", stateEntity.getParentCode());
		String f1062RefAddr = elementPinEntity.attributeValue("f1062RefAddr");
		Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(tb1046IedEntity.getF1046Name(), f1062RefAddr);
		if(pinEntity != null) {
			pinEntity.setTb1064StrapByF1064Code(strapEntity);
			pinEntityService.update(pinEntity);
		}
	}

	private void savePout(Element elementPoutEntity) {
		String strapRefAddr = elementPoutEntity.attributeValue("strapRefAddr");
		Map<String, Object> params = new HashMap<>();
		params.put("f1016AddRef", strapRefAddr);
		params.put("tb1046IedByF1046Code", tb1046IedEntity);
		Tb1016StatedataEntity stateEntity = (Tb1016StatedataEntity) beanDao.getObject(Tb1016StatedataEntity.class, params);
		if(stateEntity == null) {
			return;
		}
		Tb1064StrapEntity strapEntity = (Tb1064StrapEntity) beanDao.getObject(Tb1064StrapEntity.class, "f1064Code", stateEntity.getParentCode());
		String f1061RefAddr = elementPoutEntity.attributeValue("f1061RefAddr");
		Tb1061PoutEntity poutEntity = poutEntityService.getPoutEntity(tb1046IedEntity.getF1046Name(), f1061RefAddr);
		if(poutEntity != null) {
			if (strapEntity == null) {
				System.err.println("压板为空：" + strapRefAddr);
			}
			poutEntity.setTb1064StrapByF1064Code(strapEntity);
			poutEntityService.update(poutEntity);
		}
	}

	private void savePort(Tb1047BoardEntity boardEntity, Element elementPortEntity) {
		String f1048No = elementPortEntity.attributeValue("f1048No");
		String f1048Desc = elementPortEntity.attributeValue("f1048Desc");
		String f1048Direction = elementPortEntity.attributeValue("f1048Direction");
		String f1048Plug = elementPortEntity.attributeValue("f1048Plug");
		String fbRefAddr = elementPortEntity.attributeValue("fbRefAddr");
		
		Map<String, Object> params = new HashMap<>();
		params.put("f1048No", f1048No);
		params.put("tb1047BoardByF1047Code", boardEntity);
		Tb1048PortEntity portEntity = (Tb1048PortEntity) beanDao.getObject(Tb1048PortEntity.class, params);
		if (portEntity == null) {
			String portCode = RSCProperties.getInstance().nextTbCode(DBConstants.PR_PORT);
			portEntity = new Tb1048PortEntity(portCode, f1048No, f1048Desc,
					Integer.parseInt(f1048Direction), Integer.parseInt(f1048Plug));
		}
		portEntity.setTb1047BoardByF1047Code(boardEntity);
		beanDao.save(portEntity);
		updateAlgDataByRefAddr(fbRefAddr, portEntity.getF1048Code());
	}

	private Tb1047BoardEntity saveBoard(Element elementBoardEntity) {
		String f1047Slot = elementBoardEntity.attributeValue("f1047Slot");
		String f1047Desc = elementBoardEntity.attributeValue("f1047Desc");
		String f1047Type = elementBoardEntity.attributeValue("f1047Type");
		Map<String, Object> params = new HashMap<>();
		params.put("f1047Slot", f1047Slot);
		params.put("f1047Desc", f1047Desc);
		params.put("f1047Type", f1047Type);
		params.put("tb1046IedByF1046Code", tb1046IedEntity);
		Tb1047BoardEntity boardEntity = (Tb1047BoardEntity) beanDao.getObject(Tb1047BoardEntity.class, params);
		if (boardEntity == null) {
			String boardCode = RSCProperties.getInstance().nextTbCode(DBConstants.PR_BOARD);
			boardEntity = new Tb1047BoardEntity(boardCode, f1047Slot, f1047Desc, f1047Type);
		}
		boardEntity.setTb1046IedByF1046Code(tb1046IedEntity);
		beanDao.save(boardEntity);
		
		Element elWarns = elementBoardEntity.element("Warnings");
		if (elWarns != null) {
			List<Element> warnList = elWarns.elements("Item");
			if (warnList != null) {
				for (Element warnEl : warnList) {
					String warnRefAddr = warnEl.attributeValue("warnRefAddr");
					updateStateDataByRefAddr(warnRefAddr, boardEntity.getF1047Code());
				}
			}
		}
		return boardEntity;
	}

	private void updateStateDataByRefAddr(String warnRefAddr, String code) {
		if(StringUtil.isEmpty(warnRefAddr)) {
			return;
		}
		statedataService.updateStateParentCode(tb1046IedEntity, warnRefAddr, code);
	}
	
	private void updateAlgDataByRefAddr(String warnRefAddr, String code) {
		if(StringUtil.isEmpty(warnRefAddr)) {
			return;
		}
		analogdataService.updateAnologParentCode(tb1046IedEntity, warnRefAddr, code);
	}

}
