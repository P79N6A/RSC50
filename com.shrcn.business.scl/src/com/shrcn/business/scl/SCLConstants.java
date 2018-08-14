/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.scl;

import java.io.File;

import com.shrcn.business.scl.table.Messages;
import com.shrcn.business.scl.table.VTTableField;
import com.shrcn.found.common.Constants;

/**
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-5-27
 */
/**
 * $log$
 */
public interface SCLConstants {
	
	String DICT_PATH 				= "com/shrcn/business/scl/dict.properties";

	/** 建模方式 统一建模。非统一建模，默认为非*/
	String UNIFIED_TEMPLET 		= "1"; //$NON-NLS-1$
	String NOT_UNIFIED_TEMPLET 	= "0"; //$NON-NLS-1$
	
	// EVENT
	String APPEND_PROBLEM			= "append problems";
	String CLEAR_PROBLEM			= "clear problems";
	
	String EQUIP_GRAPH_SELECTED 		= "select a graph";
	String EQUIP_GRAPH_REMOVED 			= "remove selected graph";
	String EQUIP_GRAPH_INSERTED 		= "insert a graph";
	String BUSBAR_GRAPH_INSERTED 		= "insert a busbar";
	String BUSBAR_GRAPH_REMOVED 		= "remove a busbar";
	String BAY_GRAPH_IMPORT 			= "import template bay graph";
	String EQUIP_GRAPH_ADD_LNODE 		= "add logic node";
	String EQUIP_GRAPH_REMOVE_LNODE 	= "remove logic node";
	String EQUIP_GRAPH_RELEATE_LNODE 	= "releate logic node";
	String EQUIP_GRAPH_ISRELEATE 		= "is releate logic node";
	String EQUIP_NODE_ISRELEATE 		= "tree is releate logic node";
	String EQUIP_GRAPH_RELEASE 			= "release logic node";
	String CLONE_FIGURES 				= "clone figures";
	String COPY_FIGURES 				= "copy figures";
	String PASTE_FIGURES 				= "paste figures";
	String DATATEMPLATE_REFRESH			= "refresh datatemplate navigator";
	String SAVE_GRAPH					= "save single line graph";

	//
	String pviewId						= "com.shrcn.sct.iec61850.view.ProblemView";
	String PREF_VTMODEL_FIELDS			= "com.shrcn.sct.iec61850.table.VTViewTableModel.fields";
	String EXCEL_NAME_VT 				= "VTView.xls";
	String EXCEL_PKG_VT		 			= "/com/shrcn/business/scl/table/print/";
	String EXCEL_FILE_VT		 			= Constants.cfgDir + File.separator + EXCEL_NAME_VT;
	
	VTTableField[] VTTABLE_FIELDS 		= new VTTableField[]{
			new VTTableField(Messages.getString("VTViewTableModel.NO."), 0),
			new VTTableField(Messages.getString("VTViewTableModel.send.ied"), 1),
			new VTTableField(Messages.getString("VTViewTableModel.goose.desc"), 2),
			new VTTableField(Messages.getString("VTViewTableModel.data.attr"), 3),
			new VTTableField(Messages.getString("VTViewTableModel.logic.connect"), 4),
			new VTTableField(Messages.getString("VTViewTableModel.in.terminal.no"), 5),
			new VTTableField(Messages.getString("VTViewTableModel.data.desc"), 6),
			new VTTableField(Messages.getString("VTViewTableModel.data.attr"), 7),
			new VTTableField(Messages.getString("VTViewTableModel.curr.ied"), 8),
			new VTTableField(Messages.getString("VTViewTableModel.out.goose.desc"), 9),
			new VTTableField(Messages.getString("VTViewTableModel.data.attr"), 10),
			new VTTableField(Messages.getString("VTViewTableModel.out.terminal.no"), 11),
			new VTTableField(Messages.getString("VTViewTableModel.logic.connect"), 12),
			new VTTableField(Messages.getString("VTViewTableModel.receive.ied"), 13),
			new VTTableField(Messages.getString("VTViewTableModel.data.attr"), 14)
	};
	
	String DICT_VIRTUALBAY_ID			= "DICT_VIRTUALBAY";
}
