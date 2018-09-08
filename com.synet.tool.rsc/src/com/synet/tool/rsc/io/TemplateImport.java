package com.synet.tool.rsc.io;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.common.Constants;
import com.shrcn.found.ui.util.DialogHelper;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.service.BoardEntityService;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.PinEntityService;
import com.synet.tool.rsc.service.PortEntityService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.service.StrapEntityService;

public class TemplateImport implements IImporter{

	@Override
	@SuppressWarnings("unchecked")
	public void execute() {
		final String path = DialogHelper.selectFile(new Shell(), SWT.OPEN, Constants.XML_FILENAME);
		System.out.println(path);
		Document doc = DocumentHelper.createDocument();
		IedEntityService iedEntityService = new IedEntityService();
		//获取所有的IED
		List<Tb1046IedEntity> allIedList = (List<Tb1046IedEntity>) iedEntityService.getAll(Tb1046IedEntity.class);
		for (Tb1046IedEntity tb1046IedEntity : allIedList) {
			Element elementIedEntity = doc.addElement("Tb1046IedEntity");
			elementIedEntity.addAttribute("f1046Manufacturor", tb1046IedEntity.getF1046Manufacturor());
			elementIedEntity.addAttribute("f1046Model", tb1046IedEntity.getF1046Model());
			elementIedEntity.addAttribute("f1046ConfigVersion", tb1046IedEntity.getF1046ConfigVersion());
			elementIedEntity.addAttribute("warnRefAddr", getDevWarnRefAddr(tb1046IedEntity));
			BoardEntityService boardEntityService = new BoardEntityService();
			//获取当前装置下所有板卡
			List<Tb1047BoardEntity> boards = boardEntityService.getByIed(tb1046IedEntity);
			Element elementBoards = elementIedEntity.addElement("Boards");
			for (Tb1047BoardEntity tb1047BoardEntity : boards) {
				Element elementBoardEntity = elementBoards.addElement("Tb1047BoardEntity");
				elementBoardEntity.addAttribute("f1047Slot", tb1047BoardEntity.getF1047Slot());
				elementBoardEntity.addAttribute("f1047Desc", tb1047BoardEntity.getF1047Desc());
				elementBoardEntity.addAttribute("f1047Type", tb1047BoardEntity.getF1047Type());
				elementBoardEntity.addAttribute("warnRefAddr", getBordWarnRefAddr(tb1047BoardEntity));
				
				PortEntityService portEntityService = new PortEntityService();
				//获取当前板卡所有端口
				List<Tb1048PortEntity> ports = portEntityService.getByBoard(tb1047BoardEntity);
				for (Tb1048PortEntity tb1048PortEntity : ports) {
					Element elementPortEntity = elementBoardEntity.addElement("Tb1048PortEntity");
					elementPortEntity.addAttribute("f1048No", tb1048PortEntity.getF1048No());
					elementPortEntity.addAttribute("f1048Desc", tb1048PortEntity.getF1048Desc());
					elementPortEntity.addAttribute("f1048Direction", tb1048PortEntity.getF1048Direction()+"");
					elementPortEntity.addAttribute("f1048Plug", tb1048PortEntity.getF1048Plug()+"");
					elementPortEntity.addAttribute("fbRefAddr", getFbRefAddr(tb1048PortEntity));
				}
			}
			StrapEntityService strapEntityService = new StrapEntityService();
			//获取装置下所有虚端子压板
			List<Tb1064StrapEntity> straps = strapEntityService.getByIed(tb1046IedEntity);
			Element elementStraps = elementIedEntity.addElement("Straps");
			Element elementPouts = elementStraps.addElement("Pouts");
			Element elementPins = elementStraps.addElement("Pins");
			
			PoutEntityService poutEntityService = new PoutEntityService();
			//获取当前压板所有开出虚端子
			List<Tb1061PoutEntity> pouts = poutEntityService.getByStraps(straps);
			for (Tb1061PoutEntity tb1061PoutEntity : pouts) {
				Element elementPoutEntity = elementPouts.addElement("Tb1061PoutEntity");
				elementPoutEntity.addAttribute("f1061RefAddr", tb1061PoutEntity.getF1061RefAddr());
				elementPoutEntity.addAttribute("strapRefAddr", getStrapRefAddr());
			}
			
			PinEntityService pinEntityService = new PinEntityService();
			//获取当前压板所有开入虚端子
			List<Tb1062PinEntity> pins = pinEntityService.getByStraps(straps);
			for (Tb1062PinEntity tb1062PinEntity : pins) {
				Element elementPinEntity = elementPins.addElement("Tb1062PinEntity");
				elementPinEntity.addAttribute("f1062RefAddr", tb1062PinEntity.getF1062RefAddr());
				elementPinEntity.addAttribute("strapRefAddr", getStrapRefAddr());
			}
		}
		
	}

	private String getStrapRefAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取端口光强信号参引
	 * @param tb1048PortEntity
	 * @return
	 */
	private String getFbRefAddr(Tb1048PortEntity tb1048PortEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取板卡告警信号参引
	 * @param tb1047BoardEntity
	 * @return
	 */
	private String getBordWarnRefAddr(Tb1047BoardEntity tb1047BoardEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取装置告警信号参引
	 * @param tb1046IedEntity
	 * @return 信号参引
	 */
	private String getDevWarnRefAddr(Tb1046IedEntity tb1046IedEntity) {
		// TODO Auto-generated method stub
		return null;
	}

}
