/**
 * Copyright (c) 2007, 2008 ?????.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Device Customization Platform System.
 */

package de.kupzog.ktable.menu;

import org.eclipse.jface.action.MenuManager;
import de.kupzog.ktable.KTable;

/**
 * KTable??.
 *
 * @author ?
 * @date 2008-7-1
 */
public class MyMenuKTable {
	private KTable kTable;
	private TextModelMenu textModel;
	
	public MyMenuKTable(KTable kTable, TextModelMenu textModel){
		this.kTable = kTable;
		this.textModel = textModel;
		kTable.setModel(textModel);
		//??
		MyActionGroup actionGroup=new MyActionGroup(kTable, textModel);
		actionGroup.fillContextMenu(new MenuManager());
		
	}
	public KTable getKTable(){
		return this.kTable;
	}
	
    
    
}


