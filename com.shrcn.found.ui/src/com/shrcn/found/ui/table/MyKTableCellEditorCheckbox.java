package com.shrcn.found.ui.table;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.CheckableCellRenderer;

/**
 * A cell editor that expects a Boolean cell value
 * and simply switches this value. It has no control, it
 * just changes the value in the model and forces a cell
 * redraw.<p>
 * NOTE: This implementation makes the whole cell area sensible. 
 * It is activated by a RETURN, a SPACE or a single mouse click.
 * <p>
 * Note: If you need this behavior, but wish to have only a part
 * of the cell area that is sensible (like che checkbox that must
 * be clicked, independently of how big the cell area is), look at
 * KTableCellEditorCheckbox2.
 * 
 * @see de.kupzog.ktable.editors.KTableCellEditorCheckbox2
 * @see de.kupzog.ktable.cellrenderers.CheckableCellRenderer
 * 
 * @author Lorenz Maierhofer <lorenz.maierhofer@logicmindguide.com>
 */
public class MyKTableCellEditorCheckbox extends KTableCellEditor {
	 private Point m_Active;
	 private int m_hAlign, m_vAlign;
	
	 public MyKTableCellEditorCheckbox() {
		Rectangle imgBounds = CheckableCellRenderer.IMAGE_CHECKED.getBounds();
        Point sensible = new Point(imgBounds.width, imgBounds.height);

        this.m_Active = sensible;
        this.m_hAlign = SWTX.ALIGN_HORIZONTAL_CENTER;
        this.m_vAlign = SWTX.ALIGN_VERTICAL_CENTER;
	 }
	 
	 public MyKTableCellEditorCheckbox(Point activeArea, int hAlign, int vAlign) {
//	        super(activeArea,hAlign,vAlign);
		 if (activeArea==null || 
	                !isValidHAlignment(hAlign) || !isValidVAlignment(vAlign))
	            throw new ClassCastException("Check the parameters given to KTableCellEditorCheckbox2!");
	            
	        m_Active = activeArea;
	        m_hAlign = hAlign;
	        m_vAlign = vAlign;
	    }
	 
	  /**
		 * Activates the editor at the given position.
		 * Instantly closes the editor and switch the boolean content value.
		 * @param row
		 * @param col
		 * @param rect
		 */
		public void open(KTable table, int col, int row, Rectangle rect) {
			m_Table = table;
			m_Model = table.getModel();
			m_Rect = rect;
			m_Row = row;
			m_Col = col;
			
			close(true);
			
			GC gc = new GC(m_Table);
			m_Table.updateCell(m_Col, m_Row);
			gc.dispose();
		}
	/**
	 * Simply switches the boolean value in the model!
	 */
	public void close(boolean save) {
	    if (save) {
	        Object o = m_Model.getContentAt(m_Col, m_Row);
//	        m_Model.setContentAt(m_Col, m_Row, String.valueOf(o));
	        if ((o instanceof Boolean)){
	        	//!
	        	boolean newVal = !((Boolean)o).booleanValue();
		        
		        m_Model.setContentAt(m_Col, m_Row, new Boolean(newVal));
	        }else{
	        	m_Model.setContentAt(m_Col, m_Row, o);
	        }
//	            throw new ClassCastException("CheckboxCellEditor needs a Boolean content!");
	    }
	    super.close(save);
	}
	
	 /**
     * Checks wether the given horizontal alignment parameter is valid.
     * @param align The alignment to check
     * @return True if the alignment value is valid.
     */
    private boolean isValidHAlignment(int align) {
        if (align==SWTX.ALIGN_HORIZONTAL_CENTER || 
            align==SWTX.ALIGN_HORIZONTAL_LEFT  || 
            align==SWTX.ALIGN_HORIZONTAL_RIGHT)
            return true;
        return false;
    }
    
    /**
     * Checks wether the given vertical alignment parameter is valid.
     * @param align The alignment to check
     * @return True if the alignment value is valid.
     */
    private boolean isValidVAlignment(int align) {
        if (align==SWTX.ALIGN_VERTICAL_TOP || 
            align==SWTX.ALIGN_VERTICAL_CENTER  || 
            align==SWTX.ALIGN_VERTICAL_BOTTOM)
            return true;
        return false;
    }
    
    /**
	 * Is called when an activation is triggered via a mouse click.<p> 
	 * If false is returned, the editor does not get activated.<p>
	 * All coordinates must be relative to the KTable.
     * @param clickLocation The point where the mouseclick occured.
     * @return Returns true if the editor activation should happen.
	 */
	public boolean isApplicable(int eventType, KTable table, int col, int row, 
			Point clickLocation, String keyInput, int stateMask) {
		if (eventType == SINGLECLICK) {
			// compute active location inside the cellBoundary:
			Rectangle active = new Rectangle(0, 0, m_Active.x, m_Active.y);
			Rectangle cellBoundary = table.getCellRect(col, row);
			if (cellBoundary.width<active.width) active.width = cellBoundary.width;
			if (cellBoundary.height<active.height) active.height = cellBoundary.height;
			
			if (m_hAlign == SWTX.ALIGN_HORIZONTAL_LEFT)
				active.x = cellBoundary.x;
			else if (m_hAlign == SWTX.ALIGN_HORIZONTAL_RIGHT)
				active.x = cellBoundary.x+cellBoundary.width-active.width;
			else // center
				active.x = cellBoundary.x+(cellBoundary.width-active.width)/2;
			
			if (m_vAlign == SWTX.ALIGN_VERTICAL_TOP)
				active.y = cellBoundary.y;
			else if (m_vAlign == SWTX.ALIGN_VERTICAL_BOTTOM) 
				active.y = cellBoundary.y+cellBoundary.height-active.height;
			else 
				active.y = cellBoundary.y+(cellBoundary.height-active.height)/2;
			
			// check if clickLocation is inside the specified active area:
			if (active.contains(clickLocation))
				return true;
			return false;
		} else
			return true;
	}
	
	 /**
     * This editor does not have a control, it only switches 
     * the boolean value on activation!
     * @see de.kupzog.ktable.KTableCellEditor#createControl()
     */
    protected Control createControl() {
		return null;
	}

    /**
     * This implementation does nothing!
     * @see de.kupzog.ktable.KTableCellEditor#setContent(java.lang.Object)
     */
    public void setContent(Object content) {
    }

    /**
	 * @return Returns a value indicating on which actions 
	 * this editor should be activated.
	 */
	public int getActivationSignals() {
	    return SINGLECLICK | KEY_RETURN_AND_SPACE;
	}
    
}
