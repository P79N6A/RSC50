package com.synet.tool.rsc.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.io.scd.SclUtil;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.service.AnalogdataService;
import com.synet.tool.rsc.service.BoardEntityService;
import com.synet.tool.rsc.service.PinEntityService;
import com.synet.tool.rsc.service.PortEntityService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.service.StatedataService;
import com.synet.tool.rsc.util.DataUtils;

public class TemplateExport{
	
	private PinEntityService pinEntityService;
	private StatedataService statedataService;
	private AnalogdataService analogdataService;
	private Tb1046IedEntity tb1046IedEntity;
	private PortEntityService portEntityService;
	private BoardEntityService boardEntityService;
	private Set<String> poutDataCodes;

	public TemplateExport(Tb1046IedEntity tb1046IedEntity) {
		pinEntityService = new PinEntityService();
		statedataService= new StatedataService();
		analogdataService= new AnalogdataService();
		portEntityService = new PortEntityService();
		boardEntityService = new BoardEntityService();
		this.tb1046IedEntity = tb1046IedEntity;
		this.poutDataCodes = new HashSet<>();
	}
	
	public void execute() {
		String f1046Manufacturor = tb1046IedEntity.getF1046Manufacturor();
		String f1046Model = tb1046IedEntity.getF1046Model();
		String f1046ConfigVersion = tb1046IedEntity.getF1046ConfigVersion();
		if(f1046Manufacturor.isEmpty() || f1046Model.isEmpty() || f1046ConfigVersion.isEmpty()) {
			DialogHelper.showAsynInformation("数据配置不完善，无法生成模版");
			return;
		}
		String fileName = f1046Manufacturor + f1046Model + f1046ConfigVersion;
		String pathTemplate = Constants.tplDir.substring(0, Constants.tplDir.length()-1);
		File fileTemplate = new File(pathTemplate);
		if(!fileTemplate.exists()) {
			fileTemplate.mkdir();
		}
		String path = Constants.tplDir + fileName + ".xml";
		File file = new File(path);
		if(file.exists()) {
			ConsoleManager.getInstance().append("模板 " + fileName + " 将被覆盖！");
		}
		createXml(createDocument(), path);
	}
	
	public Document createDocument() {
		Document doc = DocumentHelper.createDocument();
		Element elementIedEntity = doc.addElement("Tb1046IedEntity");
		iedAddAttr(tb1046IedEntity, elementIedEntity);
		//获取当前装置下所有板卡
		List<Tb1047BoardEntity> boards = boardEntityService.getByIed(tb1046IedEntity);
		Element elementBoards = elementIedEntity.addElement("Boards");
		for (Tb1047BoardEntity tb1047BoardEntity : boards) {
			Element elementBoardEntity = elementBoards.addElement("Tb1047BoardEntity");
			boardAddAttr(tb1047BoardEntity, elementBoardEntity);
			//获取当前板卡所有端口
			List<Tb1048PortEntity> ports = portEntityService.getByBoard(tb1047BoardEntity);
			for (Tb1048PortEntity tb1048PortEntity : ports) {
				Element elementPortEntity = elementBoardEntity.addElement("Tb1048PortEntity");
				portAddAttr(tb1048PortEntity, elementPortEntity);
			}
		}
		
		//获取装置下所有虚端子压板
		Element elementStraps = elementIedEntity.addElement("Straps");
		Element elementPouts = elementStraps.addElement("Pouts");
		Element elementPins = elementStraps.addElement("Pins");
		PoutEntityService poutEntityService = new PoutEntityService();
		List<Tb1061PoutEntity> pouts = poutEntityService.getByIed(tb1046IedEntity);
		for (Tb1061PoutEntity pout : pouts) {
			poutDataCodes.add(pout.getDataCode());
			Tb1064StrapEntity strap = pout.getTb1064StrapByF1064Code();
			if (strap == null) {
				continue;
			}
			Element elementPoutEntity = elementPouts.addElement("Tb1061PoutEntity");
			elementPoutEntity.addAttribute("f1061RefAddr", pout.getF1061RefAddr());
			addStrapRef(elementPoutEntity, strap);
		}
		List<Tb1062PinEntity> pins = (List<Tb1062PinEntity>) BeanDaoImpl
				.getInstance().getListByCriteria(Tb1062PinEntity.class, "tb1046IedByF1046Code", tb1046IedEntity);
		for (Tb1062PinEntity pin : pins) {
			Tb1064StrapEntity strap = pin.getTb1064StrapByF1064Code();
			if (strap == null) {
				continue;
			}
			Element elementPinEntity = elementPins.addElement("Tb1062PinEntity");
			elementPinEntity.addAttribute("f1062RefAddr", pin.getF1062RefAddr());
			addStrapRef(elementPinEntity, strap);
		}
		addDataTypes(elementIedEntity);
		return doc;
	}
	
	private void addDataTypes(Element elementIedEntity) {
		Element elDatType = elementIedEntity.addElement("DataType");
		Element elSt = elDatType.addElement("State");
		Element elAlg = elDatType.addElement("Analog");
		Element elPin = elDatType.addElement("Pin");
		List<Tb1016StatedataEntity> states = statedataService.getStatesByIed(tb1046IedEntity);
		List<Tb1006AnalogdataEntity> anologs = analogdataService.getAnologByIed(tb1046IedEntity);
		List<Tb1062PinEntity> pins = pinEntityService.getByIed(tb1046IedEntity);
		for (int i=0; i<states.size(); i++) {
			Tb1016StatedataEntity state = states.get(i);
			String f1016AddRef = state.getF1016AddRef();
			String f1016Code = state.getF1016Code();
			String parentCode = state.getParentCode();
			addDataItem(elSt, i, f1016AddRef, f1016Code, parentCode, state.getF1011No());
		}
		for (int i=0; i<anologs.size(); i++) {
			Tb1006AnalogdataEntity alg = anologs.get(i);
			String f1006AddRef = alg.getF1006AddRef();
			String f1006Code = alg.getF1006Code();
			String parentCode = alg.getParentCode();
			addDataItem(elAlg, i, f1006AddRef, f1006Code, parentCode, alg.getF1011No());
		}
		for (int i=0; i<pins.size(); i++) {
			Tb1062PinEntity alg = pins.get(i);
			String ref = alg.getF1062RefAddr();
			int f1011No = alg.getF1011No();
			Element elItem = elPin.addElement("Item");
			elItem.addAttribute("index", i+"");
			elItem.addAttribute("ref", ref);
			elItem.addAttribute("f1011No", f1011No+"");
			elItem.addAttribute("type", DBConstants.DTYPE_PIN + "");
		}
	}
	
	private void addDataItem(Element parent, int index, String ref, String dataCode, String parentCode, int f1011No) {
		if (StringUtil.isEmpty(ref)) {
			return;
		}
		Element elItem = parent.addElement("Item");
		elItem.addAttribute("index", index+"");
		elItem.addAttribute("ref", ref);
		elItem.addAttribute("f1011No", f1011No+"");
		elItem.addAttribute("type", getDType(dataCode, parentCode)+"");
	}
	
	private int getDType(String dataCode, String parentCode) {
		if (poutDataCodes.contains(dataCode)) {
			return DBConstants.DTYPE_POUT;
		} else if (SclUtil.isStrapData(parentCode)) {
			return DBConstants.DTYPE_STRAP;
		} else if (SclUtil.isStData(dataCode)) {
			return DBConstants.DTYPE_ST;
		} else if (SclUtil.isAlgData(dataCode)) {
			return DBConstants.DTYPE_ALG;
		}
		return 0;
	}

	private void addStrapRef(Element elementPoutEntity, Tb1064StrapEntity strap) {
		String strapCode = strap.getF1064Code();
		List<Tb1016StatedataEntity> states = statedataService.getStateDataByParentCode(strapCode);
		if (states!=null && states.size()>0) {
			Tb1016StatedataEntity state = states.get(0);
			elementPoutEntity.addAttribute("strapRefAddr", state.getF1016AddRef());
		} else {
			elementPoutEntity.addAttribute("strapRefAddr", "");
		}
	}
	
	public void createXml(Document doc, String path) {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        Writer out;
        try {
            out = new FileWriter(path);
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(doc);
            writer.close();
        } catch (IOException e) {
            ConsoleManager.getInstance().append("生成XML文件失败");
            e.printStackTrace();
        }
		
	}

	private void portAddAttr(Tb1048PortEntity tb1048PortEntity, Element elementPortEntity) {
		String algRefAddr = getAlgRefAddr(tb1048PortEntity.getF1048Code());
		if (StringUtil.isEmpty(algRefAddr)) {
			elementPortEntity.getParent().remove(elementPortEntity);
			return;
		}
		elementPortEntity.addAttribute("f1048No", tb1048PortEntity.getF1048No());
		elementPortEntity.addAttribute("f1048Desc", tb1048PortEntity.getF1048Desc());
		elementPortEntity.addAttribute("f1048Direction", tb1048PortEntity.getF1048Direction()+"");
		elementPortEntity.addAttribute("f1048Plug", tb1048PortEntity.getF1048Plug()+"");
		elementPortEntity.addAttribute("fbRefAddr", algRefAddr);
	}

	private void boardAddAttr(Tb1047BoardEntity tb1047BoardEntity, Element elementBoardEntity) {
		elementBoardEntity.addAttribute("f1047Slot", tb1047BoardEntity.getF1047Slot());
		elementBoardEntity.addAttribute("f1047Desc", tb1047BoardEntity.getF1047Desc());
		elementBoardEntity.addAttribute("f1047Type", tb1047BoardEntity.getF1047Type());
		Element warnEl = elementBoardEntity.addElement("Warnings");
		List<Tb1016StatedataEntity> warnList = getStRefAddr(tb1047BoardEntity.getF1047Code());
		if (warnList != null && warnList.size() > 0) {
			for (Tb1016StatedataEntity warn : warnList) {
				Element itemEl = warnEl.addElement("Item");
				itemEl.addAttribute("warnRefAddr", warn.getF1016AddRef());			}
		}
	}

	private void iedAddAttr(Tb1046IedEntity tb1046IedEntity, Element elementIedEntity) {
		elementIedEntity.addAttribute("f1046Manufacturor", tb1046IedEntity.getF1046Manufacturor());
		elementIedEntity.addAttribute("f1046Model", tb1046IedEntity.getF1046Model());
		elementIedEntity.addAttribute("f1046ConfigVersion", tb1046IedEntity.getF1046ConfigVersion());
	}
	
	@SuppressWarnings("unchecked")
	private String getAlgRefAddr(String code) {
		List<Tb1006AnalogdataEntity> algdataEntityList = (List<Tb1006AnalogdataEntity>) 
				statedataService.getListByCriteria(Tb1006AnalogdataEntity.class, "parentCode", code);
		if(DataUtils.listNotNull(algdataEntityList)) {
			return algdataEntityList.get(0).getF1006AddRef();
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	private List<Tb1016StatedataEntity> getStRefAddr(String code) {
		return (List<Tb1016StatedataEntity>) 
				statedataService.getListByCriteria(Tb1016StatedataEntity.class, "parentCode", code);
	}

}
