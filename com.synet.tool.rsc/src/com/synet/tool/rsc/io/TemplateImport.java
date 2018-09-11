package com.synet.tool.rsc.io;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.shrcn.found.common.Constants;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.PinEntityService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.service.StatedataService;

public class TemplateImport implements IImporter{
	
	private MmsfcdaService mmsfcdaService;
	private StatedataService statedataService;
	private BeanDaoImpl beanDao;
	private Tb1046IedEntity tb1046IedEntity;
	private PoutEntityService poutEntityService;
	private PinEntityService pinEntityService;
	
	public TemplateImport(Tb1046IedEntity tb1046IedEntity) {
		this.tb1046IedEntity = tb1046IedEntity;
		mmsfcdaService = new MmsfcdaService();
		statedataService = new StatedataService();
		poutEntityService = new PoutEntityService();
		pinEntityService = new PinEntityService();
		beanDao = BeanDaoImpl.getInstance();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
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
				saveBoard(elementBoardEntity);
				List<Element> elementsPortEntity = elementBoardEntity.elements("Tb1048PortEntity");
				for (Element elementPortEntity : elementsPortEntity) {
					savePort(elementPortEntity);
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
		} catch (DocumentException e) {
			ConsoleManager.getInstance().append("文件读取失败");
			e.printStackTrace();
		}
	}

	private void savePin(Element elementPinEntity) {
		String f1062RefAddr = elementPinEntity.attributeValue("f1062RefAddr");
		String strapRefAddr = elementPinEntity.attributeValue("strapRefAddr");
		Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(tb1046IedEntity.getF1046Name(), f1062RefAddr);
		Tb1016StatedataEntity statedataEntity = getStateDataByRefAddr(strapRefAddr);
		saveCode(statedataEntity, pinEntity.getF1062Code());
	}

	private void savePout(Element elementPoutEntity) {
		String f1061RefAddr = elementPoutEntity.attributeValue("f1061RefAddr");
		String strapRefAddr = elementPoutEntity.attributeValue("strapRefAddr");
		Tb1061PoutEntity poutEntity = poutEntityService.getPoutEntity(tb1046IedEntity.getF1046Name(), f1061RefAddr);
		Tb1016StatedataEntity statedataEntity = getStateDataByRefAddr(strapRefAddr);
		saveCode(statedataEntity, poutEntity.getF1061Code());
	}

	private void savePort(Element elementPortEntity) {
		String f1048No = elementPortEntity.attributeValue("f1048No");
		String f1048Desc = elementPortEntity.attributeValue("f1048Desc");
		String f1048Direction = elementPortEntity.attributeValue("f1048Direction");
		String f1048Plug = elementPortEntity.attributeValue("f1048Plug");
		String fbRefAddr = elementPortEntity.attributeValue("fbRefAddr");
		String portCode = RSCProperties.getInstance().nextTbCode(DBConstants.PR_PORT);
		Tb1048PortEntity portEntity = new Tb1048PortEntity(portCode, f1048No, f1048Desc,
				Integer.parseInt(f1048Direction), Integer.parseInt(f1048Plug));
		beanDao.save(portEntity);
		Tb1016StatedataEntity statedataEntity = getStateDataByRefAddr(fbRefAddr);
		saveCode(statedataEntity, portEntity.getF1048Code());
	}

	private void saveBoard(Element elementBoardEntity) {
		String f1047Slot = elementBoardEntity.attributeValue("f1047Slot");
		String f1047Desc = elementBoardEntity.attributeValue("f1047Desc");
		String f1047Type = elementBoardEntity.attributeValue("f1047Type");
		String warnRefAddr = elementBoardEntity.attributeValue("warnRefAddr");
		String boardCode = RSCProperties.getInstance().nextTbCode(DBConstants.PR_BOARD);
		Tb1047BoardEntity boardEntity = new Tb1047BoardEntity(boardCode, f1047Slot, f1047Desc, f1047Type);
		beanDao.save(boardEntity);
		Tb1016StatedataEntity statedataEntity = getStateDataByRefAddr(warnRefAddr);
		saveCode(statedataEntity, boardEntity.getF1047Code());
	}

	private void saveIed(Element rootElement) {
		String warnRefAddr = rootElement.attributeValue("warnRefAddr");
		Tb1016StatedataEntity statedataEntity = getStateDataByRefAddr(warnRefAddr);
		saveCode(statedataEntity, tb1046IedEntity.getF1046Code());
	}
	
	private void saveCode(Tb1016StatedataEntity statedataEntity, String code) {
		if(statedataEntity != null) {
			statedataEntity.setParentCode(code);
			beanDao.save(statedataEntity);
		}
	}

	private Tb1016StatedataEntity getStateDataByRefAddr(String warnRefAddr) {
		if(warnRefAddr == null) {
			return null;
		}
		Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(warnRefAddr);
		if(mmsfcdaEntity == null) {
			return null;
		}
		Tb1016StatedataEntity statedataEntity = statedataService.getStateDataByCode(mmsfcdaEntity.getDataCode());
		return statedataEntity;
	}

}
