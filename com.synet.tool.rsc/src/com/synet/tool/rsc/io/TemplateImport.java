package com.synet.tool.rsc.io;

import java.io.File;
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
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.tool.found.das.SessionService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.das.SessionRsc;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.service.PinEntityService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.service.StatedataService;

public class TemplateImport implements IImporter{
	
	private StatedataService statedataService;
	private BeanDaoImpl beanDao;
	private Tb1046IedEntity tb1046IedEntity;
	private PoutEntityService poutEntityService;
	private PinEntityService pinEntityService;
	
	public TemplateImport(Tb1046IedEntity tb1046IedEntity) {
		this.tb1046IedEntity = tb1046IedEntity;
		statedataService = new StatedataService();
		poutEntityService = new PoutEntityService();
		pinEntityService = new PinEntityService();
		beanDao = BeanDaoImpl.getInstance();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(IProgressMonitor monitor) {
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
			saveIed(rootElement);
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
		} catch (DocumentException e) {
			ConsoleManager.getInstance().append("文件读取失败" + e.getMessage());
			e.printStackTrace();
		}
	}

	private void saveStates(Element elementSts) {
		try {
			List<Element> stList = elementSts.elements();
			List<Element> poutList = new ArrayList<>();
			List<Element> strapList = new ArrayList<>();
			List<Element> mmsList = new ArrayList<>();
			SessionService service = SessionRsc.getInstance();
			Session _session = service.get();
			Connection conn = _session.connection();
			conn.setAutoCommit(false);
			PreparedStatement pState = conn.prepareStatement("update TB1016_StateData set F1011_NO=? where F1016_ADDREF=? and F1046_CODE=?");
			for (Element elementSt : stList) {
				String ref = elementSt.attributeValue("ref");
				String f1011NoStr = elementSt.attributeValue("f1011No");
				String typeStr = elementSt.attributeValue("type");
				int f1011No = Integer.parseInt(f1011NoStr);
				int type = Integer.parseInt(typeStr);
				pState.setInt(1, f1011No);
				pState.setString(2, ref);
				pState.setString(3, tb1046IedEntity.getF1046Code());
				pState.addBatch();
				if (DBConstants.DTYPE_POUT == type) {
					poutList.add(elementSt);
				} else { 
					if (DBConstants.DTYPE_STRAP == type) {
						strapList.add(elementSt);
					}
					mmsList.add(elementSt);
				}
			}
			pState.executeBatch();
			conn.commit();
			savePoutType(poutList);
			saveStrapType(strapList);
			saveMmsType(mmsList);
		} catch (SQLException e) {
			ConsoleManager.getInstance().append("数据导入错误" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void savePoutType(List<Element> poutList) {
		try {
			SessionService service = SessionRsc.getInstance();
			Session _session = service.get();
			Connection conn = _session.connection();
			conn.setAutoCommit(false);
			PreparedStatement pState = conn.prepareStatement("update TB1061_POUT set F1061_Type=? where F1061_RefAddr=? and F1046_CODE=?");
			for (Element elementSt : poutList) {
				String ref = elementSt.attributeValue("ref");
				String f1011NoStr = elementSt.attributeValue("f1011No");
				int f1011No = Integer.parseInt(f1011NoStr);
				pState.setInt(1, f1011No);
				pState.setString(2, ref);
				pState.setString(3, tb1046IedEntity.getF1046Code());
				pState.addBatch();
			}
			pState.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			ConsoleManager.getInstance().append("数据导入错误" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void saveMmsType(List<Element> mmsList) {
		try {
			SessionService service = SessionRsc.getInstance();
			Session _session = service.get();
			Connection conn = _session.connection();
			conn.setAutoCommit(false);
			PreparedStatement pState = conn.prepareStatement("update TB1058_MMSFCDA set F1058_Type=? where F1058_RefAddr=? and F1046_CODE=?");
			for (Element elementSt : mmsList) {
				String ref = elementSt.attributeValue("ref");
				String f1011NoStr = elementSt.attributeValue("f1011No");
				int f1011No = Integer.parseInt(f1011NoStr);
				pState.setInt(1, f1011No);
				pState.setString(2, ref);
				pState.setString(3, tb1046IedEntity.getF1046Code());
				pState.addBatch();
			}
			pState.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			ConsoleManager.getInstance().append("数据导入错误" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void saveStrapType(List<Element> strapList) {
		try {
			SessionService service = SessionRsc.getInstance();
			Session _session = service.get();
			Connection conn = _session.connection();
			conn.setAutoCommit(false);
			PreparedStatement pState = conn.prepareStatement("update TB1064_Strap a set a.F1064_TYPE=? where a.F1064_CODE=" +
					"(select b.Parent_CODE from TB1016_StateData b where b.F1016_ADDREF=? and b.F1046_CODE=?)");
			for (Element elementSt : strapList) {
				String ref = elementSt.attributeValue("ref");
				String f1011NoStr = elementSt.attributeValue("f1011No");
				int f1011No = Integer.parseInt(f1011NoStr);
				pState.setInt(1, f1011No);
				pState.setString(2, ref);
				pState.setString(3, tb1046IedEntity.getF1046Code());
				pState.addBatch();
			}
			pState.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			ConsoleManager.getInstance().append("数据导入错误" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void saveAnalogs(Element elementAlgs) {
		try {
			List<Element> algList = elementAlgs.elements();
			List<Element> poutList = new ArrayList<>();
			List<Element> mmsList = new ArrayList<>();
			SessionService service = SessionRsc.getInstance();
			Session _session = service.get();
			Connection conn = _session.connection();
			conn.setAutoCommit(false);
			PreparedStatement pState = conn.prepareStatement("update TB1006_AnalogData set F1011_NO=? where F1006_ADDREF=? and F1046_CODE=?");
			for (Element elementSt : algList) {
				String ref = elementSt.attributeValue("ref");
				String f1011NoStr = elementSt.attributeValue("f1011No");
				String typeStr = elementSt.attributeValue("type");
				int f1011No = Integer.parseInt(f1011NoStr);
				int type = Integer.parseInt(typeStr);
				pState.setInt(1, f1011No);
				pState.setString(2, ref);
				pState.setString(3, tb1046IedEntity.getF1046Code());
				pState.addBatch();
				if (DBConstants.DTYPE_POUT == type) {
					poutList.add(elementSt);
				} else { 
					mmsList.add(elementSt);
				}
			}
			pState.executeBatch();
			conn.commit();
			saveMmsType(mmsList);
			savePoutType(poutList);
		} catch (SQLException e) {
			ConsoleManager.getInstance().append("数据导入错误" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void savePins(Element elementPin) {
		try {
			List<Element> algList = elementPin.elements();
			SessionService service = SessionRsc.getInstance();
			Session _session = service.get();
			Connection conn = _session.connection();
			conn.setAutoCommit(false);
			PreparedStatement pState = conn.prepareStatement("update TB1062_PIN set F1011_NO=? where F1062_RefAddr=? and F1046_CODE=?");
			for (Element elementSt : algList) {
				String ref = elementSt.attributeValue("ref");
				String f1011NoStr = elementSt.attributeValue("f1011No");
				int f1011No = Integer.parseInt(f1011NoStr);
				pState.setInt(1, f1011No);
				pState.setString(2, ref);
				pState.setString(3, tb1046IedEntity.getF1046Code());
				pState.addBatch();
			}
			pState.executeBatch();
			conn.commit();
		} catch (SQLException e) {
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
		String convChk1 = elementPinEntity.attributeValue("convChk1");
		String convChk2 = elementPinEntity.attributeValue("convChk2");
		Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(tb1046IedEntity.getF1046Name(), f1062RefAddr);
		if(pinEntity != null) {
			if (strapEntity == null) {
				System.err.println("压板为空：" + strapRefAddr);
			}
			pinEntity.setTb1064StrapByF1064Code(strapEntity);
			Tb1063CircuitEntity circuitEntity = (Tb1063CircuitEntity) beanDao.getObject(Tb1063CircuitEntity.class, "tb1062PinByF1062CodePRecv", pinEntity);
			Tb1061PoutEntity poutEntityChk1 = poutEntityService.getPoutEntity(tb1046IedEntity.getF1046Name(), convChk1);
			Tb1061PoutEntity poutEntityChk2 = poutEntityService.getPoutEntity(tb1046IedEntity.getF1046Name(), convChk2);
			circuitEntity.setTb1061PoutByF1061CodeConvChk1(poutEntityChk1);
			circuitEntity.setTb1061PoutByF1061CodeConvChk2(poutEntityChk2);
			beanDao.update(circuitEntity);
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
		String warnRefAddr = elementBoardEntity.attributeValue("warnRefAddr");
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
		updateStateDataByRefAddr(warnRefAddr, boardEntity.getF1047Code());
		return boardEntity;
	}

	private void saveIed(Element rootElement) {
		String warnRefAddr = rootElement.attributeValue("warnRefAddr");
		updateStateDataByRefAddr(warnRefAddr, tb1046IedEntity.getF1046Code());
	}
	
	private void updateStateDataByRefAddr(String warnRefAddr, String code) {
		if(StringUtil.isEmpty(warnRefAddr)) {
			return;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("f1058RefAddr", warnRefAddr);
		params.put("tb1046IedByF1046Code", tb1046IedEntity);
		Tb1058MmsfcdaEntity mmsfcdaEntity = (Tb1058MmsfcdaEntity) beanDao.getObject(Tb1058MmsfcdaEntity.class, params);
		if(mmsfcdaEntity == null) {
			return;
		}
		Tb1016StatedataEntity statedataEntity = statedataService.getStateDataByCode(mmsfcdaEntity.getDataCode());
		if(statedataEntity == null) {
			return;
		}
		if(statedataEntity != null) {
			statedataEntity.setParentCode(code);
			mmsfcdaEntity.setParentCode(code);
			beanDao.update(statedataEntity);
			beanDao.update(mmsfcdaEntity);
		}
	}
	
	private void updateAlgDataByRefAddr(String warnRefAddr, String code) {
		if(StringUtil.isEmpty(warnRefAddr)) {
			return;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("f1058RefAddr", warnRefAddr);
		params.put("tb1046IedByF1046Code", tb1046IedEntity);
		Tb1058MmsfcdaEntity mmsfcdaEntity = (Tb1058MmsfcdaEntity) beanDao.getObject(Tb1058MmsfcdaEntity.class, params);
		if(mmsfcdaEntity == null) {
			return;
		}
		Tb1006AnalogdataEntity algdataEntity = (Tb1006AnalogdataEntity)beanDao.getObject(Tb1006AnalogdataEntity.class, "f1006Code", mmsfcdaEntity.getDataCode());
		if(algdataEntity == null) {
			return;
		}
		if(algdataEntity != null) {
			algdataEntity.setParentCode(code);
			mmsfcdaEntity.setParentCode(code);
			beanDao.update(algdataEntity);
			beanDao.update(mmsfcdaEntity);
		}
	}

}
