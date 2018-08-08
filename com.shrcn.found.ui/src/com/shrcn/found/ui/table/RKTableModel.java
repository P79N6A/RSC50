/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.table;

import static de.kupzog.ktable.renderers.DefaultCellRenderer.INDICATION_FOCUS_ROW;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.shrcn.business.ui.NetPortUtil;
import com.shrcn.business.ui.NetSelectorEditor;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.enums.EnumCellEditor;
import com.shrcn.found.ui.enums.EnumDataType;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ImgDescManager;
import com.shrcn.found.ui.util.UICheckUtil;

import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.renderers.CheckableCellRenderer;
import de.kupzog.ktable.renderers.DefaultCellRenderer;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-11-28
 */
/**
 * $Log: RTUKTableModel.java,v $
 * Revision 1.26  2013/11/12 05:57:25  cchun
 * Update:修改doGetCellRenderer()，添加image和percent
 *
 * Revision 1.25  2013/11/04 09:30:08  cchun
 * 修改表格数据类型校验的错误, 如果类型错误, 就退回原来的值
 *
 * Revision 1.24  2013/09/26 00:36:16  scy
 * Update：对提示标签增加条件判断，避免出现越界异常。
 *
 * Revision 1.23  2013/09/23 02:18:46  cchun
 * Update:修复doGetTooltipAt()逻辑错误
 *
 * Revision 1.22  2013/09/18 07:21:39  scy
 * Update：将贴士签内容扩展可以显示对象的某属性值
 *
 * Revision 1.21  2013/08/22 05:52:21  cxc
 * update：修改被修改单元格判断条件
 *
 * 
 * Revision 1.19  2013/08/14 09:36:10  cchun
 * Update:调整列宽
 *
 * Revision 1.18  2013/08/05 04:50:33  scy
 * Update：增加A、B网不可同时为一个网口的校验
 *
 * Revision 1.17  2013/07/26 06:28:55  cchun
 * Fix Bug:修复doGetTooltipAt()越界异常
 *
 * Revision 1.16  2013/07/22 05:46:55  cchun
 * Update:增加对comment属性的支持
 *
 * Revision 1.15  2013/07/18 13:37:53  cchun
 * Update:修改序号从0开始
 *
 * Revision 1.14  2013/07/03 08:03:38  scy
 * Update：增加校验网口个数
 *
 * Revision 1.13  2013/06/18 12:39:58  cchun
 * Update:修复render字体异常
 *
 * Revision 1.12  2013/06/14 01:42:32  cchun
 * Update:增加render
 *
 * Revision 1.11  2013/06/09 06:34:56  cchun
 * Fix Bug:修复doSetContentAt()、doGetContentAt()缺陷
 *
 * Revision 1.10  2013/06/08 10:37:15  cchun
 * Update:增加编辑功能
 *
 * Revision 1.9  2013/06/07 09:41:50  cchun
 * Update:使列可隐藏
 *
 * Revision 1.8  2013/06/05 11:06:37  cchun
 * Update:添加进度处理
 *
 * Revision 1.7  2013/05/22 03:25:28  cxc
 * Update：修改getCustomEditor
 *
 * Revision 1.6  2013/04/26 02:14:31  zsy
 * Update:增加表格对checkbox的处理
 *
 * Revision 1.5  2013/04/25 07:40:05  zsy
 * Update:增加getCustomEditor方法，完善doGetCellEditor方法
 *
 * Revision 1.4  2013/04/18 11:42:25  zsy
 * Update:解决文件中部分数据不存在index属性问题
 *
 * Revision 1.3  2013/04/17 07:30:28  scy
 * Update：修改表格第一列的值为当前行号
 *
 * Revision 1.2  2013/04/12 05:15:07  cchun
 * Refactor:提取saveCellValue()
 *
 * Revision 1.1  2013/03/29 09:54:56  cchun
 * Add:创建
 *
 * Revision 1.5  2013/03/20 00:39:01  cchun
 * Update:固定首列
 *
 * Revision 1.4  2012/12/06 02:43:54  cchun
 * Fix Bug:避免hibernate调用错误，单元格数据修改使用BeanDao.updateProperty()
 *
 * Revision 1.3  2012/11/29 08:58:02  cchun
 * Fix Bug:修复列宽错误
 *
 * Revision 1.2  2012/11/28 12:16:08  cchun
 * Update:改用getFixedHeaderColumnCount()
 *
 * Revision 1.1  2012/11/28 12:04:02  cchun
 * Add:ktable复用类
 *
 */
public class RKTableModel extends DefaultKTableModel {

	protected PercentRenderer m_percentRenderer = new PercentRenderer();
	protected ChangeCellRenderer m_changeRenderer = new ChangeCellRenderer(INDICATION_FOCUS_ROW);
	protected XOTable table;
	protected String tableName;
	protected String tableDesc;
	protected TableConfig config;
	protected IField[] fields;
	protected IField[] visibleFields;
	private List<Integer> dirtyRows = new ArrayList<Integer>();
	
	/** 是否序号从0开始. */
	private boolean isFromZero = true;
	
	public RKTableModel(XOTable table, TableConfig config) {
		super();
//		m_headerRenderer.setFont(UIConstants.FONT_HEADER);
		m_fixedRenderer.setFont(UIConstants.FONT_CONTENT);
		m_textRenderer.setFont(UIConstants.FONT_CONTENT);
		m_percentRenderer.setFont(UIConstants.FONT_CONTENT);
		m_changeRenderer.setFont(UIConstants.FONT_CONTENT);
		this.table = table;
		this.tableName = config.getName();
		this.tableDesc = config.getDesc();
		this.config = config;
		this.fields = config.getFields();
		this.visibleFields = getVisibleFields(fields);
	}
	
	/**
	 * 获取列名
	 * @param col
	 * @return
	 */
	public String getFieldName(int col) {
		return visibleFields[col].getName();
	}
	
	/**
	 * 获取可见列数组
	 * @param fields
	 * @return
	 */
	protected IField[] getVisibleFields(IField[] fields) {
		List<IField> visibleFields = new ArrayList<IField>();
		for(IField field : fields) {
			if(field.isVisible()) {
				visibleFields.add(field);
			}
		}
		return visibleFields.toArray(new IField[visibleFields.size()]);
	}
	
	@Override
	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		DefaultCellRenderer render = null;
		if (row < getFixedHeaderRowCount()) {
			return m_fixedRenderer;
		} else if (isFixedCell(col, row)) {
			render = m_fixedRenderer;
		} else {
			IField field = visibleFields[col];
			String editor = field.getEditor();
			String comment = field.getComment();
			String frender = field.getRender();
			if (EnumCellEditor.resovleByType(editor) == EnumCellEditor.CHECK) {
				render = m_checkableRenderer;
			} else if (!StringUtil.isEmpty(comment)) {
				render = "image".equals(frender) ? m_imageRenderComm : m_textRendererComm;
			} else if ("image".equals(frender)) {
				render = m_imageRender;
			} else if ("percent".equals(frender) || "percent".equals(field.getName())) {
				render = m_percentRenderer;
			} else {
				render = m_textRenderer;
			}
		}
		render.setBackground(UIConstants.getRowColor(row));
		return render;
	}
	
	@Override
	public KTableCellEditor doGetCellEditor(int col, int row) {
		if (isFixedCell(col, row))
			return null;
		DictManager dictMgr = DictManager.getInstance();
		IField field = visibleFields[col];
		String editor = field.getEditor();
		String dict = field.getDictType();
		if(!field.isEditAble())
			return null;
		if (dict != null) {
			String[] items = dictMgr.getDictNames(dict);
			return EnumCellEditor.COMBO.createKTableEditor(items);
		} else {
			KTableCellEditor kbEditor = null;
			if (EnumCellEditor.contains(editor)) {
				kbEditor = EnumCellEditor.resovleByType(editor).createKTableEditor(null);
			} else {
				kbEditor = getCustomEditor(editor);
			}
//			if (kbEditor == null)
//				kbEditor = EnumCellEditor.TEXT.createKTableEditor(null);
			return kbEditor;
		}
	}
	
	protected KTableCellEditor getCustomEditor(String editor) {
		if (editor.startsWith(NetPortUtil.NET_SELECTOR)) {
			int[] limit = NetPortUtil.getNetLimit(editor);
			return new NetSelectorEditor(limit[0], limit[1], limit[2]);
		}
		return null;
	}

	@Override
	public String doGetTooltipAt(int col, int row) {
		if (col > -1) {
			String comment = visibleFields[col].getComment();
			if (!StringUtil.isEmpty(comment)) {
				if (row > 0) {
					Object obj = items.get(row - 1);
					String txt = (String) ObjectUtil.getProperty(obj, comment);
					if (!StringUtil.isEmpty(txt)) {
						return txt;
					}
				}
				return comment;
			}
		}
		return super.doGetTooltipAt(col, row);
	}
	
	@Override
	public Object doGetContentAt(int col, int row) {
		Object obj = super.doGetContentAt(col, row);
		if (obj != null)
			return obj;
		Object value = null;
		if (row == 0) {
			value = visibleFields[col].getTitle();
			content.put(col + "/" + row, value);
			return value;
		} else if (col == 0) {
			// 解决文件中部分数据不存在index属性问题
			Object element = items.get(row - 1);
			IField field = (IField)visibleFields[col];
			value = field.getTextValue(element);
			return StringUtil.isEmpty((String)value) ? (row + getStartRow()) : value;
		} else if ((null != items) && (row >= 1) && (row < items.size() + 1)) {
			Object element = items.get(row - 1);
			IField field = (IField)visibleFields[col];
			KTableCellRenderer cellRenderer = doGetCellRenderer(col, row);
			if (cellRenderer instanceof CheckableCellRenderer || 
					cellRenderer instanceof ImageCellRenderer) {	// 图片和复选框显示
				value = ObjectUtil.getProperty(element, field.getName());
				if (cellRenderer instanceof ImageCellRenderer &&
						(value instanceof String)) {
					value = ImgDescManager.createImage((String) value);
				}
			} else {												// 普通文本显示
				value = field.getTextValue(element);
				if (value instanceof Timestamp) {
					SimpleDateFormat sdf = new SimpleDateFormat(Constants.STD_TIME_FORMAT);
					value = sdf.format(value);
				} else if(null != value && "datType".equalsIgnoreCase(field.getName())) {
					value = EnumDataType.getTypeById((String)value);
				} else {
					DictManager dictMgr = DictManager.getInstance();
					String dict = field.getDictType();
					value = (dict == null) ? value : dictMgr.getNameById(dict, (String)value);
				}
				content.put(col + "/" + row, value);
			}
			return value;
		}
		return "";
	}
	
	private int getStartRow() {
		return isFromZero ? -1 : 0;
	}
	
	public void setFromZero(boolean isFromZero) {
		this.isFromZero = isFromZero;
	}
	
	/**
	 * 根据序号获取列表值
	 * @param field
	 * @param index
	 * @return
	 */
	private String getComboText(IField field, String text) {
		String dictName = field.getDictType();
		return (dictName == null) ? text : 
			DictManager.getInstance().getIdByName(dictName, text);
	}
	
	@Override
	public void doSetContentAt(int col, int row, Object value) {
		if (row < getFixedRowCount())
			return;
		Object oldValue = doGetContentAt(col, row);
		if ((value==null) || (value.equals(oldValue)))
			return;
		dirtyRows.add(row);
		
		final Object data = items.get(row - 1);
		String newValue = null;
		IField field = visibleFields[col];
		String dictName = field.getDictType();
		String editor = field.getEditor();
		String property = field.getName();

		if (EnumCellEditor.CHECK.getType().equals(editor)) {
			table.setCellValue(data, property, value);
			return;
		}
		String msg = UICheckUtil.checkDataType(field, value.toString());
		if (EnumCellEditor.TEXT.isSame(editor) && msg != null) {
			DialogHelper.showWarning(msg);
			return;
		}
		if ((dictName != null || EnumCellEditor.COMBO.isSame(editor))) {
			newValue = getComboText(field, (String)value);
		} else {
			if (value instanceof Boolean)				// CheckBox
				newValue = "" + value;
			else
				newValue = value.toString();
		}
		
		if (oldValue != null && oldValue.equals(newValue))
			return;
		msg = table.checkCellValue(data, property, newValue);
		if(msg != null) {
			DialogHelper.showWarning(msg);
			return;
		} else {
			super.doSetContentAt(col, row, value);
			
			table.setCellValue(data, property, newValue);
			saveCellValue(data, property);
			updateRelations(data, property, oldValue, newValue);
		}
	}
	
	/**
	 * 实现表格修改数据实时存入数据库/文件内.
	 * 
	 * @param data
	 * @param property
	 */
	protected void saveCellValue(Object data, String property) {
	}
	
	protected void updateRelations(Object data, String property, Object oldValue, String newValue) {
	}
	
	public IField[] getVisibleFields() {
		return visibleFields;
	}

	public void setVisibleFields(IField[] visibleFields) {
		this.visibleFields = visibleFields;
	}

	@Override
	public int doGetColumnCount() {
		return visibleFields.length;
	}
	
	@Override
	public int getInitialColumnWidth(int col) {
		int width = 0;
		if (col < visibleFields.length)
			width = visibleFields[col].getWidth();
		if (width < 5)
			width = 100;
		return width;
	}
	
	@Override
	public int getInitialRowHeight(int row) {
		int initialRowHeight = super.getInitialRowHeight(row);
		int rowNum = 1;
//		if (row + 1 > getFixedRowCount()) {
//			int index = row - getFixedColumnCount();
//			for (int col=0; col<doGetColumnCount() && index < items.size(); col++) {
//				Object o = doGetContentAt(col, index+1);
//				String v = (o==null) ? "" : o.toString();
//				for (char c : v.toCharArray()) {
//					if (c == '\n') {
//						rowNum++;
//					}
//				}
//			}
//		}
		return initialRowHeight * rowNum;
	}

	public List<Integer> getDirtyRows() {
		return dirtyRows;
	}
	
	public boolean isDirtyRow(int row) {
		return dirtyRows.contains(row);
	}
	
	public void clearDirtyRows() {
		dirtyRows.clear();
	}
}
