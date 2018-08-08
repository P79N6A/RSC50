/**
 * Copyright (c) 2007, 2008 ?????.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Device Customization Platform System.
 */

package de.kupzog.ktable.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * KTable?,??,
 *
 * @author ?
 * @date 2008-7-4
 */
public class KTablePara {
	//?;
    private List<String> headTitleCol = new ArrayList<String>();
    //,??
    private List<ArrayList<Object>> listContent = new ArrayList<ArrayList<Object>>();;
	//CheckBox?
    private List<Integer> listCheckboxCol = new ArrayList<Integer>();
    //Combox?
    private HashMap<Integer,ArrayList<String>> mapComboxCol = new HashMap<Integer,ArrayList<String>>();
    //??
    private List<Integer> listNoEditCol = new ArrayList<Integer>();
    //??,???и
    private boolean hasRowHead = true;
    //???
    private List<String> headTitleRow = new ArrayList<String>();
    //е??
    private HashMap<Integer,Object> mapDefaultValue = new HashMap<Integer,Object>();
    //?,?
    private int columnNum;
    
    
    public List<String> getHeadTitleCol() {
		return headTitleCol;
	}

	public void setHeadTitleCol(List<String> headTitleCol) {
		this.headTitleCol = headTitleCol;
	}

	public List<ArrayList<Object>> getListContent() {
		return listContent;
	}

	public void setListContent(List<ArrayList<Object>> listContent) {
		this.listContent = listContent;
	}

	public HashMap<Integer, ArrayList<String>> getMapComboxCol() {
		return mapComboxCol;
	}

	public void setMapComboxCol(HashMap<Integer, ArrayList<String>> mapComboxCol) {
		this.mapComboxCol = mapComboxCol;
	}

	public List<Integer> getListCheckboxCol() {
		return listCheckboxCol;
	}

	public void setListCheckboxCol(List<Integer> listCheckboxCol) {
		this.listCheckboxCol = listCheckboxCol;
	}

	public List<Integer> getListNoEditCol() {
		return listNoEditCol;
	}

	public void setListNoEditCol(List<Integer> listNoEditCol) {
		this.listNoEditCol = listNoEditCol;
	}

	public boolean isHasRowHead() {
		return hasRowHead;
	}

	public void setHasRowHead(boolean hasRowHead) {
		this.hasRowHead = hasRowHead;
	}
	public List<String> getHeadTitleRow() {
		return headTitleRow;
	}
	
	public void setHeadTitleRow(List<String> headTitleRow) {
		this.headTitleRow = headTitleRow;
	}

	public HashMap<Integer, Object> getMapDefaultValue() {
		return mapDefaultValue;
	}

	public void setMapDefaultValue(HashMap<Integer, Object> mapDefaultValue) {
		this.mapDefaultValue = mapDefaultValue;
	}

	public int getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}

	//??
	/*??:?裬?null,??д?null
	 * :
	headTitleCol:б
	listContent:
	hasRowHead:?б
	headTitleRow:?б
	listCheckboxCol:?ЩCheckbox
	mapComboxCol:?ЩCombox
	listNoEditCol:?Щ???
	defaultValue:??е??
	*/
	public KTablePara(List<String> headTitleCol,
			          List<ArrayList<Object>> listContent,
			          boolean hasRowHead,
			          List<String> headTitleRow,
			          List<Integer> listCheckboxCol,
			          HashMap<Integer,ArrayList<String>> mapComboxCol,
			          List<Integer> listNoEditCol,
			          HashMap<Integer,Object> defaultValue){
		this.headTitleCol = headTitleCol;
		this.listContent = listContent;
		this.listCheckboxCol = listCheckboxCol;
		this.mapComboxCol = mapComboxCol;
		this.listNoEditCol = listNoEditCol;
		this.hasRowHead = hasRowHead;
		this.headTitleRow = headTitleRow;
		this.mapDefaultValue = defaultValue;
	}
	
	//??
	/*??:?裬?null,??д?null
	 * :
	headTitleCol:б
	listContent:
	hasRowHead:?б
	
	*/
	public KTablePara(List<String> headTitleCol,
			          List<ArrayList<Object>> listContent,
			          boolean hasRowHead
			          ){
		this.headTitleCol = headTitleCol;
		this.listContent = listContent;
		this.hasRowHead = hasRowHead;
		this.listCheckboxCol = null;
		this.mapComboxCol = null;
		this.listNoEditCol = null;
		this.headTitleRow = null;
		this.mapDefaultValue = null;
	}
	
	//?,?
	/*??:?裬?null,??д?null?????
	 * :
	columnNum:
	listContent:
	hasRowHead:?б
	headTitleRow:?б
	listCheckboxCol:?ЩCheckbox
	mapComboxCol:?ЩCombox
	listNoEditCol:?Щ???
	defaultValue:??е??
	*/
	public KTablePara(int columnNum,
	                  List<ArrayList<Object>> listContent,
	                  boolean hasRowHead,
	                  List<String> headTitleRow,
	                  List<Integer> listCheckboxCol,
	                  HashMap<Integer,ArrayList<String>> mapComboxCol,
	                  List<Integer> listNoEditCol,
	                  HashMap<Integer,Object> defaultValue){
		this.columnNum = columnNum;
		this.listContent = listContent;
		this.listCheckboxCol = listCheckboxCol;
		this.mapComboxCol = mapComboxCol;
		this.listNoEditCol = listNoEditCol;
		this.hasRowHead = hasRowHead;
		this.headTitleRow = headTitleRow;
		this.mapDefaultValue = defaultValue;
	}
	
	//?,?
	/*??:?裬?null,??д?null?????
	 * :
	columnNum:
	listContent:
	hasRowHead:?б
	
	*/
	public KTablePara(int columnNum,
	                  List<ArrayList<Object>> listContent,
	                  boolean hasRowHead
	                  ){
		this.columnNum = columnNum;
		this.listContent = listContent;
		this.hasRowHead = hasRowHead;
		this.listCheckboxCol = null;
		this.mapComboxCol = null;
		this.listNoEditCol = null;
		this.headTitleRow = null;
		this.mapDefaultValue = null;
	}
	

}


