package de.kupzog.ktable.menu;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.actions.ActionGroup;
import de.kupzog.ktable.KTable;

/**
 * KTable?,KTable,?,м,??
 *
 * @author ?
 * @date 2008-7-1
 */

public class MyActionGroup extends ActionGroup {
	private KTable kTable;
	private TextModelMenu textModel;
	private PasteAction pasteAction = new PasteAction();
	private AddAction addAction = new AddAction();
	private RemoveAction removeAction = new RemoveAction();
	private CutAction cutAction = new CutAction();
	private int row;

	// Action???TableViewer??
	public MyActionGroup(KTable kTable, TextModelMenu textModel) {
		this.kTable = kTable;
		this.textModel = textModel;
	}

	

	// ??MenuAction
	public void fillContextMenu(IMenuManager mgr) {
		// Action???
		MenuManager menuManager = (MenuManager) mgr;
		menuManager.add(addAction);
		menuManager.add(removeAction);
        menuManager.add(cutAction);
        menuManager.add(pasteAction);
        pasteAction.setEnabled(false);
        if (textModel.getKTablePara().getHeadTitleRow()!= null){
        	System.out.println("textModel.getKTablePara().getHeadTitleRow().size():"+textModel.getKTablePara().getHeadTitleRow().size());
        	addAction.setEnabled(false);
        	removeAction.setEnabled(false);
        	cutAction.setEnabled(false);
        	pasteAction.setEnabled(false);
        }
		// Menu?table?menuKTable???
		
        Menu menu = menuManager.createContextMenu(kTable);
		kTable.setMenu(menu);
	}

	

	// ?Action
	private class AddAction extends Action {
		public AddAction() {
			//setHoverImageDescriptor(getImageDesc("project.gif"));// ??μ?
			setText("");
		}

		public void run() {
			//private final HashMap<Integer,String> defaultValue = new HashMap<Integer,String>();
			
			ArrayList<Object> cell=new ArrayList<Object>();
			if(textModel.getKTablePara().getColumnNum()==0)
				if (textModel.getKTablePara().isHasRowHead()==true){
					for(int i=1;i<=textModel.getHeadTitle().size();i++){
						addItem(cell, i);
					}
				}else{
					for(int i=0;i<textModel.getHeadTitle().size();i++){
						addItem(cell, i);
					}
				}
			else{
				if (textModel.getKTablePara().isHasRowHead()==true){
					for(int i=1;i<=textModel.getColumnCount();i++){
						addItem(cell, i);
					}
				}else{
					for(int i=0;i<textModel.getColumnCount();i++){
						addItem(cell, i);
					}
				}
			}
			
			textModel.getTcontent().add(cell);
			kTable.redraw();
		}

		/**
		 * ???У???
		 *
		 * @param:
		 *
		 * @return:
		 *
		 */
		private void addItem(ArrayList<Object> cell, int i) {
			if (textModel.getKTablePara().getListCheckboxCol()!=null){
				if(textModel.getKTablePara().getListCheckboxCol().contains(new Integer(i))){
					if (textModel.getKTablePara().getMapDefaultValue()!=null){	
					    if (textModel.getKTablePara().getMapDefaultValue().containsKey(new Integer(i))){
							cell.add(textModel.getKTablePara().getMapDefaultValue().get(new Integer(i)));
						}else{
							cell.add(false);
						}
					}else{
					    cell.add(false);
					}    
				}else{
					if(textModel.getKTablePara().getMapDefaultValue()!=null){
						if (textModel.getKTablePara().getMapDefaultValue().containsKey(new Integer(i))){
							cell.add(textModel.getKTablePara().getMapDefaultValue().get(new Integer(i)));
						}else{
							cell.add("");
						}
					}else{
						cell.add("");
					}
				}
					
			}else if (textModel.getKTablePara().getMapDefaultValue()!=null){
					
			    if (textModel.getKTablePara().getMapDefaultValue().containsKey(new Integer(i))){
					cell.add(textModel.getKTablePara().getMapDefaultValue().get(new Integer(i)));
				}else{
					cell.add("");
				}
				 
			}else{
			    cell.add("");
			}
		}

	}
	
	/**
	 * ??
	 *
	 * @author ?
	 * @date 2008-7-1
	 */
	private class RemoveAction extends Action {
		
		public RemoveAction() {
			//setHoverImageDescriptor(getImageDesc("remove.gif"));// ??μ?
			// ?Ч??μ??????Ч??????
			//setDisabledImageDescriptor(getImageDesc("disremove.gif"));
			setText("?");
		}

		// ?δ?????
		public void run() {
			//,KTable?SWT.FULL_SELECTION.
			int[] row=kTable.getRowSelection();
			if(textModel.getKTablePara().getColumnNum()==0){
	    	    textModel.getTcontent().remove(row[0]-1);
			}else{
				textModel.getTcontent().remove(row[0]);
			}
			kTable.redraw();
			

		}
	}
	
	/**
	 * в?
	 *
	 * @author ?
	 * @date 2008-7-1
	 */
	public class CutAction extends Action{
		public CutAction(){
			setText("");
		}
		public void run(){
			int[] selectRow=kTable.getRowSelection();
			kTable.setSelection(0, selectRow[0], true);
			pasteAction.setEnabled(true);
			row=selectRow[0];
		}
	}
	/**
	 * ??
	 *
	 * @author ?
	 * @date 2008-7-1
	 */
	public class PasteAction extends Action{
		public PasteAction(){
			setText("?");
		}
		public void run(){
			if(textModel.getKTablePara().getColumnNum()==0){
				ArrayList<Object> array=textModel.getTcontent().get(row-1);
				int[] rowSelect=kTable.getRowSelection();
				int rowPaste=rowSelect[0];
				textModel.getTcontent().remove(row-1);
				textModel.getTcontent().add(rowPaste-1, array);
				pasteAction.setEnabled(false);
				kTable.redraw();
			}else{
				ArrayList<Object> array=textModel.getTcontent().get(row);
				int[] rowSelect=kTable.getRowSelection();
				int rowPaste=rowSelect[0];
				textModel.getTcontent().remove(row);
				textModel.getTcontent().add(rowPaste, array);
				pasteAction.setEnabled(false);
				kTable.redraw();
			}
		}
	}

	

	

	// ?巽Action???ToolBarManager
	public void fillActionToolBars(ToolBarManager actionBarManager) {
		// Action????Action
		//Action refreshAction = new RefreshAction();
		Action addAction = new AddAction();
		Action removeAction = new RemoveAction();
		//Action selAllAction = new SelectAllAction();
		//Action deselAction = new DeselectAction();

		// ??ToolBarManager,add(action)
		// ????????????Action?
		// ActionContributionItem???д?
		//actionBarManager.add(createContributionItem(refreshAction));
		actionBarManager.add(createContributionItem(addAction));
		actionBarManager.add(createContributionItem(removeAction));
		//actionBarManager.add(createContributionItem(selAllAction));
		//actionBarManager.add(createContributionItem(deselAction));

		actionBarManager.update(true);// 1???κ??
	}

	// Action?ActionContributionItem??Action?
	// ToolBarManagerMenuManager??ActionContributionItem??
	// ??ToolBarManageradd(IAction)???
	private IContributionItem createContributionItem(IAction action) {
		ActionContributionItem aci = new ActionContributionItem(action);
		aci.setMode(ActionContributionItem.MODE_FORCE_TEXT);// ??+
		return aci;
	}

	// ???ImageDescriptor
	private ImageDescriptor getImageDesc(String fileName) {
		try {
			URL url = new URL("file:icons/" + fileName);
			return ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

}