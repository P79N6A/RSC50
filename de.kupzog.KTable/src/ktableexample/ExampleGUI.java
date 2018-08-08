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

package ktableexample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.menu.KTablePara;
import de.kupzog.ktable.menu.MyMenuKTable;
import de.kupzog.ktable.menu.TextModelMenu;

/**
 * KTable example GUI.<p>
 * Demonstrates some usages of KTable.
 */

public class ExampleGUI {
	public static void main(String[] args) {
		// create a shell...
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText("KTable examples");
		
		// put a tab folder in it...
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		
		createTextTable(tabFolder);
	
		// display the shell...
		shell.setSize(600,600);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}


    /**
     * Constructs a simple text table. 
     * The table model is directly created from the interface KTableModel.
     * Editors are KTableCellEditorText and KTableCellEditorCombo.<p>
     * NOTE that this shows how to set an Excel-like mouse cursor for the 
     * KTable. If setCursor() is used, the cursor is not preserved and will 
     * be swiched back to the default cursor when a resize cursor or something
     * else is shown. 
     */
    private static void createTextTable(TabFolder tabFolder) {
		TabItem item1 = new TabItem(tabFolder, SWT.NONE);
		item1.setText("Text Table");
		Composite comp1 = new Composite(tabFolder, SWT.NONE);
		item1.setControl(comp1);
		comp1.setLayout(new FillLayout());
		KTable table = new KTable(comp1,  SWT.FULL_SELECTION|SWT.MULTI | SWT.V_SCROLL 
				| SWT.H_SCROLL  | SWTX.EDIT_ON_KEY);
		//б,?,?б??.
		List<String> headTitle = new ArrayList<String>();
		//headTitle.add("");
		headTitle.add("");
		headTitle.add("?");
		headTitle.add("");
		/*
		,?
		headTitle.addAll(Collection<? extends String>);
		*/
		
		//,?д洢,???ArrayList<Object>,?洢Stringboolean.
		//?,洢?бб
		List<ArrayList<Object>> listContent = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> cell=new ArrayList<Object>();
		cell.add("1");
		cell.add(false);
		cell.add("3");
		listContent.add(cell);
		
		//ЩCombox,ЩCombox???.
		HashMap<Integer,ArrayList<String>> mapComboxCol= new HashMap<Integer,ArrayList<String>>();
		ArrayList<String> comboValue = new ArrayList<String>();
		comboValue.add("");
		comboValue.add("");
		comboValue.add("?");
		comboValue.add("");
		//key?к.title?
		mapComboxCol.put(new Integer(0), comboValue);
		
		//Щ???,洢?к.0?
		List<Integer> listNoEditCol=new ArrayList<Integer>();
		listNoEditCol.add(new Integer(2));
		
		//????,key?к,Value?,???.?Checkbox?
		HashMap<Integer,Object> defaultValue =new HashMap<Integer,Object>();
		defaultValue.put(new Integer(0),"");
		defaultValue.put(new Integer(1), true);
		
		//ЩCheckbox?,洢Checkbox?к.
		List<Integer> listCheckboxCol = new ArrayList<Integer>();
		listCheckboxCol.add(new Integer(1));
		//listCheckboxCol.add(new Integer(3));
		
		//,б??е?,??,.
		List<String> listHeadTitleRow = new ArrayList<String>();
		listHeadTitleRow.add("?");
		
		/*??:?裬?null,??д?null
		 * д?úЩ?set
		 * :
		headTitle:б
		listContent:
		false:?б
		listHeadTitleRow:?б
		listCheckboxCol:?ЩCheckbox
		mapComboxCol:?ЩCombox
		listNoEditCol:?Щ???
		defaultValue:??е??
		*/
		KTablePara kTablePara = new KTablePara(headTitle,listContent,false,null,listCheckboxCol,mapComboxCol,
				listNoEditCol,defaultValue);
		
		/*??:?裬?null,??д?null
		 * д?úЩ?set
		 * ?????
		 * :
		:
		listContent:
		false:?б
		listHeadTitleRow:?б
		listCheckboxCol:?ЩCheckbox
		mapComboxCol:?ЩCombox
		listNoEditCol:?Щ???
		defaultValue:??е??
		*/
		//KTablePara kTablePara = new KTablePara(4,listContent,true,null,listCheckboxCol,mapComboxCol,
		//		listNoEditCol,defaultValue);
		
		TextModelMenu textModel = new TextModelMenu(kTablePara);
		
		//?????????????
		KTable table1 = new MyMenuKTable(table, textModel).getKTable();
		
		
		
    }
    
    

}
