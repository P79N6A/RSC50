/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.table;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;

import com.shrcn.business.scl.SCLConstants;
import com.shrcn.business.scl.das.VTReportDAO;
import com.shrcn.business.scl.model.SignalRelation;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.common.util.TimeCounter;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.table.DefaultKTableModel;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.util.UIPreferences;

import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.TextCellRenderer;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-10-30
 */
/**
 * $Log: VTViewTableModel.java,v $
 * Revision 1.12  2013/08/07 06:34:43  cchun
 * Fix Bug:修复单元格合并缺陷
 *
 * Revision 1.2  2013/08/05 07:57:30  cchun
 * Update:输出虚端子做合并单元格处理
 *
 * Revision 1.1  2013/06/28 08:45:24  cchun
 * Update:添加模型检查和虚端子关联查看
 *
 * Revision 1.10  2011/11/24 11:45:53  cchun
 * Refactor:使用统一的基础类
 *
 * Revision 1.9  2011/08/28 06:28:43  cchun
 * Update:整理代码
 *
 * Revision 1.8  2011/08/02 07:14:54  cchun
 * Update:为界面表格添加背景色
 *
 * Revision 1.7  2011/02/24 08:11:27  cchun
 * Update:尝试合并一对多开出虚端子
 *
 * Revision 1.6  2010/09/27 07:13:40  cchun
 * Update:统一开入、开出信号关联对象
 *
 * Revision 1.5  2010/09/03 03:50:42  cchun
 * Update:增加列定制持久化
 *
 * Revision 1.4  2010/05/21 03:32:22  cchun
 * Update:完善“全屏”、“打印”列定制功能
 *
 * Revision 1.3  2010/05/20 11:07:51  cchun
 * Update:增加列调整功能
 *
 * Revision 1.2  2010/04/19 09:08:32  cchun
 * Update:避免单元格出现":"
 *
 * Revision 1.1  2010/03/02 07:49:36  cchun
 * Add:添加重构代码
 *
 * Revision 1.7  2010/01/21 08:47:54  gj
 * Update:完成UI插件的国际化字符串资源提取
 *
 * Revision 1.6  2009/12/09 08:59:39  cchun
 * Update:修改IED名称描述格式
 *
 * Revision 1.5  2009/11/19 08:28:55  cchun
 * Update:完成信号关联打印功能
 *
 * Revision 1.4  2009/11/17 05:59:04  cchun
 * Update:添加刷新函数
 *
 * Revision 1.3  2009/11/13 09:38:14  cchun
 * Update:增加开入端子描述
 *
 * Revision 1.2  2009/11/13 07:18:41  cchun
 * Update:完善关联表格功能
 *
 * Revision 1.1  2009/11/04 02:19:35  cchun
 * Add:虚端子查看表格
 *
 */
public class VTViewTableModel extends DefaultKTableModel {

	//下列序号对应的列值可以自动生成
	protected static final int IDX_SEQUENCE = 0;		//序号
	protected static final int IDX_IEDNAME = 8;		//当前装置
	protected static final int IDX_INLINE = 4;		//进逻辑连线
	protected static final int IDX_OUTLINE = 12;		//出逻辑连线
	protected static final int IDX_IN_INDEX = 5;		//开入端子序号(按逻辑节点分组)
	protected static final int IDX_OUT_INDEX = 11;	//开出端子序号(按数据集分组)
	//下列序号对应的列值需从数据库查询
	protected static final int IDX_OUT_IEDNAME = 1;
	protected static final int IDX_OUT_DESC = 2;
	protected static final int IDX_OUT_REF = 3;
	protected static final int IDX_TIN_REF_DESC = 6;
	protected static final int IDX_TIN_REF = 7;
	protected static final int IDX_TOUT_DESC = 9;
	protected static final int IDX_TOUT_REF = 10;
	protected static final int IDX_IN_IEDNAME = 13;
	protected static final int IDX_IN_REF = 14;
	/** 表头 */
	protected VTTableField[] fields = SCLConstants.VTTABLE_FIELDS;
	//端子前缀
	protected static final String PREFIX_IN = "IN_"; //$NON-NLS-1$
	protected static final String PREFIX_OUT = "OUT_"; //$NON-NLS-1$
	/** 列数 */
	protected int itemSize = 2;
	//组名颜色
	protected Color groupColor = new Color(null, 0, 0, 255);
	//分组字体
	protected Font groupFont = new Font(null, Messages.getString("VTViewTableModel.font"), 10, SWT.NORMAL); //$NON-NLS-1$
	//装置字体
	protected Font iedFont = new Font(null, Messages.getString("VTViewTableModel.font"), 12, SWT.BOLD); //$NON-NLS-1$
	
	protected List<Entry<String, List<SignalRelation>>> inputs = new ArrayList<Entry<String, List<SignalRelation>>>();
	protected List<Entry<String, List<SignalRelation>>> outputs = new ArrayList<Entry<String, List<SignalRelation>>>();
	protected String iedName = null;
	protected int inSize = -1;		//开入所占行数
	protected int outSize = -1;		//开出所占行数
	protected int[] inStartIndexs = null;	//开入关联每组开始元素在整个表格中的行号
	protected int[] outStartIndexs = null;	//开出关联每组开始元素在整个表格中的行号
	protected UIPreferences sctPref = UIPreferences.newInstance();
	
	public VTViewTableModel() {
	}
	
	public VTViewTableModel(String iedName) {
		super();
		this.iedName = iedName;
		init();
	}
	
	/**
	 * 加载表格数据
	 */
	private void reload() {
		TimeCounter.begin();
		inputs.clear();
		outputs.clear();
		IRunnableWithProgress openProgress = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				monitor.setTaskName("正在查询数据......");
				VTReportDAO reptDao = VTReportDAO.getInstance();
				reptDao.setMonitor(monitor);
				inputs.addAll(reptDao.getInputRelations(iedName).entrySet());
				outputs.addAll(reptDao.getOutputRelations(iedName).entrySet());
			}};
		ProgressManager.execute(openProgress, false);
		
		inSize = getTotalSize(inputs);
		outSize = getTotalSize(outputs);
		inStartIndexs = getStartIndexs(inputs);
		outStartIndexs = getStartIndexs(outputs);
		itemSize = Math.max(inSize, outSize);
		TimeCounter.end("解析数据");
	}
	
	/**
	 * 加载初始化数据
	 */
	protected void init() {
		reload();
		initVisible();
	}
	
	protected void initVisible() {
		List<VTTableField> lstVisible = new ArrayList<VTTableField>();
		for (VTTableField field : fields) {
			if (isVisible(field.getValNum())) {
				lstVisible.add(field);
			}
		}
		fields = lstVisible.toArray(new VTTableField[lstVisible.size()]);
	}
	
	private boolean isVisible(int num) {
		boolean isVisible = false;
		String visibleFields = getVisibleFieldStr();
		if (visibleFields == null)
			return isVisible;
		String[] nums = visibleFields.split(",");
		List<String> lst = Arrays.asList(nums);
		return lst.contains(String.valueOf(num));
	}
	
	/**
	 * 刷新数据
	 */
	public void refresh() {
		reload();
	}
	
	protected int getTotalSize(List<Entry<String, List<SignalRelation>>> data) {
		int size = data.size();
		for (Entry<String, List<SignalRelation>> it : data) {
			List<SignalRelation> value = it.getValue();
			size += value.size();
		}
		return size;
	}
	
	/**
	 * 得到每组在整个表格中的起始行号
	 * @param data
	 * @return
	 */
	protected int[] getStartIndexs(List<Entry<String, List<SignalRelation>>> data) {
		int[] indexs = new int[data.size()];
		List<SignalRelation> preValue = null;
		int i = 0;
		int curIdx = 0;
		for(Entry<String, List<SignalRelation>> it : data) {
			List<SignalRelation> value = (List<SignalRelation>)it.getValue();
			if(null == preValue) {
				curIdx = 1;
			} else {
				curIdx += preValue.size() + 1;
			}
			preValue = value;
			indexs[i] = curIdx;
			i++;
		}
		return indexs;
	}
	
	/**
	 * 不允许编辑
	 */
	@Override
	public KTableCellEditor doGetCellEditor(int col, int row) {
		return null;
	}

	/**
	 * 获取IED列号
	 * @return
	 */
	protected int getValueIndex(int modelIdx) {
		int index = -1;
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getValNum() == modelIdx) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	/**
	 * 合并单元格
	 */
	@Override
	public Point doBelongsToCell(int col, int row) {
		int iedNameNewIdx = getValueIndex(IDX_IEDNAME);
		if(col == iedNameNewIdx && iedNameNewIdx > -1) {
			if(row > 0 && row <= doGetRowCount())
				return new Point(iedNameNewIdx, 1);
		}
		if (isOutAttCol(col) && (row > 0 && row < getRowCount())) {
			Object preValue = getContentAt(col, row - 1);
			Object value = getContentAt(col, row);
			if (value.equals(preValue) && !StringUtil.isEmpty(String.valueOf(value)))
				return new Point(col, row - 1);
		} // 暂不合并一对多的输出端子信息
		return super.doBelongsToCell(col, row);
	}
	
	protected boolean isOutAttCol(int col) {
		return col<getColumnCount() && (col==IDX_TOUT_DESC || col==IDX_TOUT_REF);
	}

	/**
	 * 取得表格单元绘制器
	 */
	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		if (isFixedCell(col, row))
			return m_fixedRenderer;
		col = fields[col].getValNum();
		TextCellRenderer m_textRenderer = new TextCellRenderer(
				TextCellRenderer.INDICATION_FOCUS);
		if(col == IDX_IEDNAME || col == IDX_INLINE || col == IDX_OUTLINE) {
			m_textRenderer.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER
					| SWTX.ALIGN_VERTICAL_CENTER);
			m_textRenderer.setForeground(groupColor);
			if(col == IDX_IEDNAME) {
				m_textRenderer.setFont(iedFont);
			}
		}
		if((col == IDX_IN_INDEX || col == IDX_OUT_INDEX) && isGroupRow(col, row)) {
			m_textRenderer.setForeground(groupColor);
			m_textRenderer.setFont(groupFont);
		}
		m_textRenderer.setBackground(UIConstants.getRowColor(row));
		return m_textRenderer;
	}
	
	@Override
	public Object doGetContentAt(int col, int row) {
		if(row == 0) {
			return fields[col].getHead();
		} else {
			col = fields[col].getValNum();
			switch(col) {
				case 0: return row;
				case IDX_INLINE: // 判断流向箭头前后是否有数据，有则输出箭头，否则输出空字串
					String preV = getModelValueAt(col - 1, row);
					if("".equals(preV)) //$NON-NLS-1$
						return ""; //$NON-NLS-1$
				case IDX_OUTLINE:
					String nextV = getModelValueAt(col + 1, row);
					if("".equals(nextV)) //$NON-NLS-1$
						return ""; //$NON-NLS-1$
					return "----->"; //$NON-NLS-1$
				default:
					return getModelValueAt(col, row);
			}
		}
	}
	
	/**
	 * 判断是否处于分组行
	 * @param col
	 * @param row
	 * @return
	 */
	private boolean isGroupRow(int col, int row) {
		int[] startIndexs = null;
		if(col < IDX_IEDNAME) {
			startIndexs = inStartIndexs;
		} else {
			startIndexs = outStartIndexs;
		}
		for(int index : startIndexs) {
			if(index == row) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 得到当前元素所在分组序号
	 * @param col
	 * @param row
	 * @return
	 */
	private int getGroupNum(int col, int row) {
		int[] startIndexs = null;
		if(col < IDX_IEDNAME) {
			startIndexs = inStartIndexs;
		} else {
			startIndexs = outStartIndexs;
		}
		int groupNum = -1;
		for(int i=0; i<startIndexs.length; i++) {
			if(startIndexs[i] > row) {
				groupNum = i - 1;
				break;
			}
		}
		if(groupNum == -1)
			groupNum = startIndexs.length - 1;
		return groupNum;
	}
	
	/**
	 * 计算单元格内容
	 * @param col
	 * @param row
	 * @return
	 */
	private String getModelValueAt(int col, int row) {
		if(col < IDX_IEDNAME) {
			if(row > inSize)
				return ""; //$NON-NLS-1$
			int groupNum = getGroupNum(col, row);
			Entry<String, List<SignalRelation>> group = inputs.get(groupNum);
			if(isGroupRow(col, row)) {
				if(col == IDX_IN_INDEX)
					return group.getKey();
				else
					return ""; //$NON-NLS-1$
			} else {
				List<SignalRelation> relations = group.getValue();
				if (groupNum >= inStartIndexs.length)
					return "";
				int startIndex = inStartIndexs[groupNum];
				SignalRelation relation = relations.get(row - startIndex - 1);
				switch(col) {
					case IDX_OUT_IEDNAME:
						return getCellValue(relation.getDisplayIEDName());
					case IDX_OUT_DESC: 
						return getCellValue(relation.getExtDODesc());
					case IDX_OUT_REF: 
						return getCellValue(relation.getExtRef());
					case IDX_TIN_REF_DESC:
						return getCellValue(relation.getIntAddrDesc());
					case IDX_TIN_REF:
						return getCellValue(relation.getIntAddr());
					case IDX_IN_INDEX: 
						return PREFIX_IN + (row - startIndex);
					default:
						return ""; //$NON-NLS-1$
				}
			}
		} else if(col > IDX_IEDNAME) {
			if(row > outSize)
				return ""; //$NON-NLS-1$
			int groupNum = getGroupNum(col, row);
			if (groupNum >= outputs.size())
				return "";
			Entry<String, List<SignalRelation>> group = outputs.get(groupNum);
			String dsName = group.getKey();
			if(isGroupRow(col, row)) {
				if(col == IDX_OUT_INDEX) {
					return dsName;
				} else {
					return ""; //$NON-NLS-1$
				}
			} else {
				List<SignalRelation> relations = group.getValue();
				int startIndex = outStartIndexs[groupNum];
				int i = row - startIndex - 1;
				if (i >= relations.size())
					return "";
				SignalRelation relation = relations.get(i);
				switch(col) {
					case IDX_TOUT_DESC :
						return getCellValue(relation.getExtDODesc());
					case IDX_TOUT_REF :
						return getCellValue(relation.getExtRef());
					case IDX_IN_IEDNAME :
						return getCellValue(relation.getDisplayInIEDName());
					case IDX_IN_REF :
						return getCellValue(relation.getIntAddr());
					case IDX_OUT_INDEX:
						return PREFIX_OUT + (i + 1);
					default:
						return ""; //$NON-NLS-1$
				}
			}
		} else {
			return iedName;
		}
	}

	/**
	 * 获取单元格合法字符串(不能为null)
	 * @param value
	 * @return
	 */
	private String getCellValue(String value) {
		return (value == null)?"":value; //$NON-NLS-1$
	}
	
	@Override
	public int getFixedHeaderColumnCount() {
		return 0;
	}
	
	@Override
	public int getFixedHeaderRowCount() {
		return 1;
	}
	
	/**
	 * 得到表格的行数
	 */
	public int doGetRowCount() {
		return itemSize + 1;
	}
	
	/**
	 * 得到表格的列数
	 */
	public int doGetColumnCount() {
		return fields.length;
	}
	
	/**
	 * 得到初始化的行宽
	 */
	public int getInitialColumnWidth(int column) {
		column = fields[column].getValNum();
		int width = 80;
		switch(column) {
			case IDX_SEQUENCE: 
				width = 39; 
				break;
			case IDX_OUT_DESC: 
			case IDX_OUT_REF: 
			case IDX_TIN_REF_DESC: 
			case IDX_TIN_REF: 
			case IDX_TOUT_DESC:
			case IDX_TOUT_REF: 
			case IDX_IN_REF: 
				width = 120; 
				break;
		}
		return width;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		groupColor.dispose();
		groupFont.dispose();
		iedFont.dispose();
	}

	public List<Entry<String, List<SignalRelation>>> getInputs() {
		return inputs;
	}

	public List<Entry<String, List<SignalRelation>>> getOutputs() {
		return outputs;
	}

	public String getIedName() {
		return iedName;
	}

	public VTTableField[] getFields() {
		return fields;
	}

	public void setFields(VTTableField[] fields) {
		this.fields = fields;
	}
	
	public String getVisibleFieldStr() {
		return sctPref.getInfo(SCLConstants.PREF_VTMODEL_FIELDS);
	}
	
}
