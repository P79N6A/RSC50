package com.synet.tool.rsc.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.shrcn.found.common.Constants;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.view.ConsoleManager;
import com.sun.org.apache.regexp.internal.recompile;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.service.BoardEntityService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.PinEntityService;
import com.synet.tool.rsc.service.PortEntityService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.service.StatedataService;
import com.synet.tool.rsc.service.StrapEntityService;
import com.synet.tool.rsc.util.DataUtils;

public class TemplateExport{
	
	private MmsfcdaService mmsfcdaService;
	private StatedataService statedataService;
	private Tb1046IedEntity tb1046IedEntity;
	private PortEntityService portEntityService;
	private BoardEntityService boardEntityService;
	private StrapEntityService strapEntityService;

	public TemplateExport(Tb1046IedEntity tb1046IedEntity) {
		mmsfcdaService = new MmsfcdaService();
		statedataService= new StatedataService();
		portEntityService = new PortEntityService();
		boardEntityService = new BoardEntityService();
		strapEntityService = new StrapEntityService();
		this.tb1046IedEntity = tb1046IedEntity;
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
		if(file.exists()) { //判断是否存在模版
			
			return;
		} else {
			try {
				boolean createNewFile = file.createNewFile();
				if(!createNewFile) {
					DialogHelper.showAsynInformation("创建文件失败：" + path);
					return;
				}
			} catch (IOException e) {
				DialogHelper.showAsynInformation("创建文件失败");
				e.printStackTrace();
				return;
			}
		}
		
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
		List<Tb1064StrapEntity> straps = strapEntityService.getByIed(tb1046IedEntity);
		Element elementStraps = elementIedEntity.addElement("Straps");
		Element elementPouts = elementStraps.addElement("Pouts");
		Element elementPins = elementStraps.addElement("Pins");
		PoutEntityService poutEntityService = new PoutEntityService();
		PinEntityService pinEntityService = new PinEntityService();
		List<Tb1064StrapEntity> param  = new ArrayList<>();
		for (Tb1064StrapEntity tb1064StrapEntity : straps) {
			param.add(tb1064StrapEntity);
			//获取当前压板所有开出虚端子
			List<Tb1061PoutEntity> pouts = poutEntityService.getByStraps(param);
			for (Tb1061PoutEntity tb1061PoutEntity : pouts) {
				Element elementPoutEntity = elementPouts.addElement("Tb1061PoutEntity");
				poutAddAttr(tb1064StrapEntity, tb1061PoutEntity, elementPoutEntity);
			}
			
			//获取当前压板所有开入虚端子
			List<Tb1062PinEntity> pins = pinEntityService.getByStraps(param);
			for (Tb1062PinEntity tb1062PinEntity : pins) {
				Element elementPinEntity = elementPins.addElement("Tb1062PinEntity");
				pinAddAttr(tb1064StrapEntity, tb1062PinEntity, elementPinEntity);
			}
			param.clear();
		}
		createXml(doc, path);
	}
	
	private void createXml(Document doc, String path) {
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

	private void pinAddAttr(Tb1064StrapEntity tb1064StrapEntity, Tb1062PinEntity tb1062PinEntity,
			Element elementPinEntity) {
		elementPinEntity.addAttribute("f1062RefAddr", tb1062PinEntity.getF1062RefAddr());
		elementPinEntity.addAttribute("strapRefAddr", getRefAddr(tb1064StrapEntity.getF1046Code()));
	}

	private void poutAddAttr(Tb1064StrapEntity tb1064StrapEntity, Tb1061PoutEntity tb1061PoutEntity,
			Element elementPoutEntity) {
		elementPoutEntity.addAttribute("f1061RefAddr", tb1061PoutEntity.getF1061RefAddr());
		elementPoutEntity.addAttribute("strapRefAddr", getRefAddr(tb1064StrapEntity.getF1046Code()));
	}

	private void portAddAttr(Tb1048PortEntity tb1048PortEntity,
			Element elementPortEntity) {
		elementPortEntity.addAttribute("f1048No", tb1048PortEntity.getF1048No());
		elementPortEntity.addAttribute("f1048Desc", tb1048PortEntity.getF1048Desc());
		elementPortEntity.addAttribute("f1048Direction", tb1048PortEntity.getF1048Direction()+"");
		elementPortEntity.addAttribute("f1048Plug", tb1048PortEntity.getF1048Plug()+"");
		elementPortEntity.addAttribute("fbRefAddr", getRefAddr(tb1048PortEntity.getF1048Code()));
	}

	private void boardAddAttr(Tb1047BoardEntity tb1047BoardEntity,
			Element elementBoardEntity) {
		elementBoardEntity.addAttribute("f1047Slot", tb1047BoardEntity.getF1047Slot());
		elementBoardEntity.addAttribute("f1047Desc", tb1047BoardEntity.getF1047Desc());
		elementBoardEntity.addAttribute("f1047Type", tb1047BoardEntity.getF1047Type());
		elementBoardEntity.addAttribute("warnRefAddr", getRefAddr(tb1047BoardEntity.getF1047Code()));
	}

	private void iedAddAttr(Tb1046IedEntity tb1046IedEntity,
			Element elementIedEntity) {
		elementIedEntity.addAttribute("f1046Manufacturor", tb1046IedEntity.getF1046Manufacturor());
		elementIedEntity.addAttribute("f1046Model", tb1046IedEntity.getF1046Model());
		elementIedEntity.addAttribute("f1046ConfigVersion", tb1046IedEntity.getF1046ConfigVersion());
		elementIedEntity.addAttribute("warnRefAddr", getRefAddr(tb1046IedEntity.getF1046Code()));
	}

	@SuppressWarnings("unchecked")
	private String getRefAddr(String code) {
		List<Tb1016StatedataEntity> statedataEntityList = (List<Tb1016StatedataEntity>) 
				statedataService.getListByCriteria(Tb1016StatedataEntity.class, 
						"parentCode", code);
		if(DataUtils.listNotNull(statedataEntityList)) {
			List<Tb1058MmsfcdaEntity> mmsfcdaEntityList = (List<Tb1058MmsfcdaEntity>) 
					mmsfcdaService.getListByCriteria(Tb1058MmsfcdaEntity.class, 
							"dataCode", statedataEntityList.get(0).getF1016Code());
			if(DataUtils.listNotNull(mmsfcdaEntityList)) {
				return mmsfcdaEntityList.get(0).getF1058RefAddr();
			}
		}
		return "";
	}

}
