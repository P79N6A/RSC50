/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 
 Author: Friederich Kupzog  
 fkmk@kupzog.de
 www.kupzog.de/fkmk
 */
package de.kupzog.ktable.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableDefaultModel;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.editors.KTableCellEditorCheckbox2;
import de.kupzog.ktable.editors.KTableCellEditorCombo;
import de.kupzog.ktable.editors.KTableCellEditorText;
import de.kupzog.ktable.renderers.CheckableCellRenderer;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import de.kupzog.ktable.renderers.RadioCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;

/**
 * ??TextModel?.
 * 
 * @author Friederich Kupzog
 */
public class TextModelMenu extends KTableDefaultModel {

    private HashMap content = new HashMap();
    
    private KTablePara kTablePara;
    private List<String> headTitleCol = new ArrayList<String>();
    private List<ArrayList<Object>> listContent =new ArrayList<ArrayList<Object>>();
    //CheckBox?
    private List<Integer> listCheckboxCol = new ArrayList<Integer>();
    // Combox ?
    private HashMap<Integer,ArrayList<String>> mapComboxCol = new HashMap<Integer,ArrayList<String>>();;
    //??
    private List<Integer> listNoEditCol = new ArrayList<Integer>();
    //??,???и
    private boolean hasRowHead = true;
    //???
    private List<String> headTitleRow = new ArrayList<String>();
    //е??
    private HashMap<Integer,Object> defaultValue = new HashMap<Integer,Object>();
    //?,?
    private int columnNum;
    
    private final FixedCellRenderer m_fixedRenderer =
        new FixedCellRenderer(FixedCellRenderer.STYLE_FLAT  |
            TextCellRenderer.INDICATION_FOCUS_ROW);
    
    private final TextCellRenderer m_textRenderer = 
        new TextCellRenderer(TextCellRenderer.INDICATION_FOCUS_ROW);
    
//    private KTableCellRenderer m_CheckableRenderer = 
//        new CheckableCellRenderer(
//                CheckableCellRenderer.INDICATION_CLICKED | 
//                CheckableCellRenderer.INDICATION_FOCUS);
    private KTableCellRenderer m_CheckableRenderer = 
        new RadioCellRenderer(
                CheckableCellRenderer.INDICATION_CLICKED | 
                CheckableCellRenderer.INDICATION_FOCUS);

   
    public List<String> getHeadTitle(){
    	return this.headTitleCol;
    }
    public List<ArrayList<Object>> getTcontent(){
    	return this.listContent;
    }
    public KTablePara getKTablePara(){
    	return this.kTablePara;
    }
    
    /**
     * Initialize the base implementation.
     */
    public TextModelMenu(KTablePara kTablePara) {
        // before initializing, you probably have to set some member values
        // to make all model getter methods work properly.
        initialize();
        
        this.kTablePara = kTablePara; 
        this.headTitleCol =kTablePara.getHeadTitleCol();
        this.listContent = kTablePara.getListContent();
        this.listCheckboxCol = kTablePara.getListCheckboxCol();
        this.mapComboxCol = kTablePara.getMapComboxCol();
        this.listNoEditCol = kTablePara.getListNoEditCol();
        this.hasRowHead = kTablePara.isHasRowHead();
        this.headTitleRow = kTablePara.getHeadTitleRow();
        this.defaultValue = kTablePara.getMapDefaultValue();
        this.columnNum = kTablePara.getColumnNum();
        // we don't want the default foreground color on text cells,
        // so we change it:
        m_textRenderer.setForeground(
                Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
    }
    
    // Content:
    public Object doGetContentAt(int col, int row) {
        if (headTitleCol.size()!=0){
	    	Iterator iterator=headTitleCol.iterator();
	    	if (row == 0){
	            return headTitleCol.get(col).toString();  
	    	}else{
	    		//?и,??
	    		Object erg =  content.get(col + "/" + row);
	            if (erg != null){
	                return erg;
	            //?,?listChnlAnalogItemXmlLocal е
	            }else{
	            	//???????
	            	/*if (listCheckboxCol.contains(new Integer(col))){
	            		if (hasRowHead==true){
			            	//??
				    		if (col!=0 ){
				    		    return listContent.get(row-1).get(col-1);
				    		//??
				    		}else{
				    		    return row;
				    		}
		            	}else{
		            		return listContent.get(row-1).get(col);
		            	}	
	            	}else{*/
		            	if (hasRowHead==true){
			            	//??
				    		if (col!=0 ){
				    		    return listContent.get(row-1).get(col-1);
				    		//??
				    		}else{
				    			if(headTitleRow==null){
				    		        return row;
				    			}else{
				    				return headTitleRow.get(row-1);
				    			}
				    		}
		            	}else{
		            		return listContent.get(row-1).get(col);
		            	}
	            	//}	
	            }	
	    	}
	    //headTitleCol.size()==0
        }else{
        	Object erg =  content.get(col + "/" + row);
            if (erg != null){
                return erg;
            //?,?listChnlAnalogItemXmlLocal е
            }else{
            	if (hasRowHead==true){
	            	//??
		    		if (col!=0 ){
		    		    return listContent.get(row).get(col-1);
		    		//??
		    		}else{
		    			//??
		    			if(headTitleRow==null){
		    		        return row+1;
		    		    //??    
		    			}else{
		    				return headTitleRow.get(row);
		    			}
		    		}
            	}else{
            		return listContent.get(row).get(col);
            	}
            }
        }
    	
    }

    /*
     * overridden from superclass
     */
    public KTableCellEditor doGetCellEditor(int col, int row) {
    	if (col<getFixedColumnCount() || row<getFixedRowCount())
    		return null;
    	//Combox
    	if(mapComboxCol!=null){
	    	if (mapComboxCol.keySet().contains(new Integer(col))){
	    		KTableCellEditorCombo e = new KTableCellEditorCombo();
	            int itemSize =  mapComboxCol.get(new Integer(col)).size();
	            String [] items = new String [itemSize];
	            for(int i=0;i<itemSize;i++){
	            	items[i] = mapComboxCol.get(new Integer(col)).get(i);
	            }
	            e.setItems(items);
	            return e;
	    	}
    	}
    	
    	//Checkbox
    	if(listCheckboxCol!=null){
	    	if (listCheckboxCol.contains(new Integer(col))){
	    		Rectangle imgBounds = CheckableCellRenderer.IMAGE_CHECKED.getBounds();
	            Point sensible = new Point(imgBounds.width, imgBounds.height);
	            return new KTableCellEditorCheckbox2(sensible, SWTX.ALIGN_HORIZONTAL_CENTER, SWTX.ALIGN_VERTICAL_CENTER);
	    	}
    	}
    	
    	//???
    	if(listNoEditCol!=null){
	    	if(listNoEditCol.contains(new Integer(col))){
	    		return null;
	    	}
    	}
    	
        return new KTableCellEditorText();
    	
    }

    /*
     * overridden from superclass
     */
    public void doSetContentAt(int col, int row, Object value) {
        content.put(col + "/" + row, value);
        //???μ
        updateDataToList(content);
        
    }

    /**
     * ??μlistContent
     *
     * @param:
     *
     * @return:
     *
     */
    public void updateDataToList(HashMap content){
    	Iterator iterator=content.keySet().iterator();
    	Object  key=iterator.next();
    	String strKey=key.toString();
    	int temp=strKey.indexOf("/");
    	int col=Integer.parseInt(strKey.substring(0,temp));
    	int row=Integer.parseInt(strKey.substring(temp+1));
    	
    	for(int i=0;i<content.size();i++){
    		Object value=content.get(key);
    		//?
    		if(hasRowHead==true){
    			if(this.columnNum==0){
		    		//????μ?.
		    		listContent.get(row-1).remove(col-1);
		    		listContent.get(row-1).add(col-1,value);
    			}else{
    				//????μ?.
		    		listContent.get(row).remove(col-1);
		    		listContent.get(row).add(col-1,value);
    			}
    		//
    		}else{
    			if(this.columnNum==0){
	    			//????μ?.
		    		listContent.get(row-1).remove(col);
		    		listContent.get(row-1).add(col,value);
    			}else{
    				//????μ?.
		    		listContent.get(row).remove(col);
		    		listContent.get(row).add(col,value);
    			}
    		}
    	}
    	content.clear();
    }
    
    // Table size:
    public int doGetRowCount() {
    	//if(this.columnNum==0){
            return listContent.size()+getFixedRowCount();
    	/*}else{
    		return listContent.size();
    	}*/
    }

    public int getFixedHeaderRowCount() {
        if(this.columnNum==0){
    	    return 1;
        }else{
    	    return 0;    	
    	}
    }

    public int doGetColumnCount() {
    	if (columnNum==0){
    		if (hasRowHead==true){
	            return headTitleCol.size()-1+getFixedColumnCount();
    		}else{
    			return headTitleCol.size();
    		}
    	}else{
    		return columnNum;
    	}
    		
    }

    public int getFixedHeaderColumnCount() {
    	if (hasRowHead==true){
            return 1;
    	}else{
    		return 0;
    	}
    }
    
    /* (non-Javadoc)
     * @see de.kupzog.ktable.KTableModel#getFixedSelectableRowCount()
     */
    public int getFixedSelectableRowCount() {
        return 0;
    }

    /* (non-Javadoc)
     * @see de.kupzog.ktable.KTableModel#getFixedSelectableColumnCount()
     */
    public int getFixedSelectableColumnCount() {
        return 0;
    }

    public boolean isColumnResizable(int col) {
        return true;
    }

    public boolean isRowResizable(int row) {
        return true;
    }

    public int getRowHeightMinimum() {
        return 18;
    }
    
    // Rendering
    public KTableCellRenderer doGetCellRenderer(int col, int row) {
        if (isFixedCell(col, row))
            return m_fixedRenderer;
        if(listCheckboxCol!=null){
	        if (listCheckboxCol.contains(new Integer(col))){
	        	return m_CheckableRenderer;
	        }
        }
        return m_textRenderer;
    }

    /* (non-Javadoc)
     * @see de.kupzog.ktable.KTableModel#belongsToCell(int, int)
     */
    public Point doBelongsToCell(int col, int row) {
        // no cell spanning:
        return null;
    }

    /* (non-Javadoc)
     * @see de.kupzog.ktable.KTableDefaultModel#getInitialColumnWidth(int)
     */
    public int getInitialColumnWidth(int column) {
        return 90;
    }

    /* (non-Javadoc)
     * @see de.kupzog.ktable.KTableDefaultModel#getInitialRowHeight(int)
     */
    public int getInitialRowHeight(int row) {
    	if (row==0) return 22;
    	return 18;
    }
}

