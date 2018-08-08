/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui;

import java.awt.Component;
import java.awt.Container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.sf.feeling.swt.win32.extension.widgets.ThemeConstants;

import de.kupzog.ktable.SWTX;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-5
 */
/**
 * $Log: UIConstants.java,v $
 * Revision 1.11  2013/09/26 12:54:37  cchun
 * Update:支持combobox
 *
 * Revision 1.10  2013/09/26 12:47:32  cchun
 * Update:修改combo样式
 *
 * Revision 1.9  2013/07/02 06:37:45  cchun
 * Refactor:ROW_ODD_BACK改用WHITE代替
 *
 * Revision 1.8  2013/06/09 06:28:39  cchun
 * Update:添加颜色
 *
 * Revision 1.7  2013/05/30 06:55:39  cchun
 * Update:修改常量
 *
 * Revision 1.6  2013/05/28 13:27:20  cchun
 * Update:调整布局
 *
 * Revision 1.5  2013/04/23 08:19:37  cchun
 * Update:修改标题栏背景色
 *
 * Revision 1.4  2013/04/12 09:01:30  scy
 * Update:修改页面背景色为默认值
 *
 * Revision 1.3  2013/04/07 02:27:00  cchun
 * Update:修改PANEL_BACK
 *
 * Revision 1.2  2013/04/02 13:26:15  cchun
 * Update:添加THEME
 *
 * Revision 1.1  2013/03/29 09:36:54  cchun
 * Add:创建
 *
 * Revision 1.16  2013/02/05 02:37:24  scy
 * Add：增加下拉列表样式的变量
 *
 * Revision 1.15  2012/11/28 12:01:43  cchun
 * Update:修改常量Ktable_LastFill
 *
 * Revision 1.14  2012/11/06 09:27:48  cchun
 * Update:添加计算器字体
 *
 * Revision 1.13  2012/11/02 09:35:15  cchun
 * Update:添加TABLE_STYLE
 *
 * Revision 1.12  2012/11/01 07:58:12  cchun
 * Update:修改系统字体
 *
 * Revision 1.11  2012/10/25 11:05:43  cchun
 * Update:增加界面统一字体常量
 *
 * Revision 1.10  2012/07/02 01:21:28  cchun
 * Update:添加鼠标常量
 *
 * Revision 1.9  2012/06/18 09:36:09  cchun
 * Update:添加颜色和字体
 *
 * Revision 1.8  2011/11/18 09:14:06  cchun
 * Update:添加IED_COLOR
 *
 * Revision 1.7  2011/08/24 09:33:33  cchun
 * Update:改成自动滚动
 *
 * Revision 1.6  2011/08/16 11:31:50  cchun
 * Update:增加控件大小常量定义
 *
 * Revision 1.5  2011/08/02 07:12:34  cchun
 * Update:为界面表格添加背景色
 *
 * Revision 1.4  2011/08/02 03:24:28  cchun
 * Update:添加表格、对话框常量
 *
 * Revision 1.3  2011/07/19 05:42:17  cchun
 * Refactor:去掉对designer.jar的依赖
 *
 * Revision 1.2  2011/03/25 10:01:22  cchun
 * Refactor:使用字体常量
 *
 * Revision 1.1  2011/03/25 09:55:50  cchun
 * Refactor:重命名
 *
 * Revision 1.7  2011/03/07 08:16:17  cchun
 * Update:聂国勇修改，增加黄色显示
 *
 * Revision 1.6  2010/09/03 02:29:56  cchun
 * Update:添加颜色常量
 *
 * Revision 1.5  2010/08/18 08:28:59  cchun
 * Update:添加颜色常量
 *
 * Revision 1.4  2010/08/10 03:35:38  cchun
 * Refactor:提取变量
 *
 * Revision 1.3  2010/04/21 06:36:16  cchun
 * Update:将颜色改成RGB
 *
 * Revision 1.2  2010/03/29 02:44:45  cchun
 * Update:提交
 *
 * Revision 1.1  2010/03/09 08:42:49  cchun
 * Add:添加颜色常量
 *
 */
public class UIConstants {
	
	private UIConstants(){}
	
	public static final String View_Navg_ID 	= "com.shrcn.sct.common.navigationview";
	public static final String View_Content_ID 	= "com.shrcn.sct.common.contentview";
	public static final String View_Info_ID 	= "com.shrcn.sct.common.infoview";
	public static final String View_Console_ID  = "com.shrcn.found.ui.view.ConsoleView";
	public static final String View_Commsg_ID 	= "com.shrcn.sct.common.commsgview";
	public static final String PERSP_SCT_ID 	= "com.shrcn.sct.editperspective";
	
	public static final String View_IECDebug_ID	= "";
	
	public static final String HISTORY_EDITOR_ID 		= "com.shrcn.sct.iec61850.editor.HistoryEditor"; //$NON-NLS-1$
	public static final String COMMUNICATION_EDITOR_ID 	= "com.shrcn.sct.iec61850.editor.CommunicationEditor"; //$NON-NLS-1$
	public static final String IED_CONFIGURE_EDITOR_ID 	= "com.shrcn.sct.iec61850.editor.IEDConfigureEditor"; //$NON-NLS-1$
	public static final String DATATEMPLATE_EDITOR_ID 	= "com.shrcn.sct.iec61850.editor.DataTemplateEditor"; //$NON-NLS-1$
	public static final String SINGLE_LINE_EDITOR_ID 	= "com.shrcn.sct.graph.editor.SingleLineEditor"; //$NON-NLS-1$
	public static final String SINGLE_LINE_PANEL 		= "com.shrcn.sct.graph.ui.SingleLinePanel"; //$NON-NLS-1$
	public static final String DATAFLOW_EDITOR_ID 		= "com.shrcn.sct.draw.IEDGraphEditor"; //$NON-NLS-1$
	
	public static final String USER_CONFIG_EDITOR_ID 	= "com.shrcn.sct.iec61850.editor.usercfg.UserCfgEditor"; //$NON-NLS-1$	
	
	public static final String LCD_ID					= "com.shrcn.tool.imc.ui.LCDEditor";
	
	private static final Display display = Display.getCurrent();

	public static final String THEME = ThemeConstants.STYLE_GLOSSY;//STYLE_VISTA;//STYLE_OFFICE2007;
	
	public static final int TABLE_STYLE = SWT.FULL_SELECTION|SWT.MULTI|SWT.BORDER;

	public static final Color RED = display.getSystemColor(SWT.COLOR_RED);
	public static final Color BLUE = display.getSystemColor(SWT.COLOR_BLUE);
	public static final Color DARK_BLUE = display.getSystemColor(SWT.COLOR_DARK_BLUE);
	public static final Color BLACK = display.getSystemColor(SWT.COLOR_BLACK);
	public static final Color WHITE = display.getSystemColor(SWT.COLOR_WHITE);
	public static final Color GRAY = display.getSystemColor(SWT.COLOR_GRAY);
	public static final Color DARK_GRAY = display.getSystemColor(SWT.COLOR_DARK_GRAY);
	public static final Color YELLOW = display.getSystemColor(SWT.COLOR_YELLOW);
	public static final Color GREEN = display.getSystemColor(SWT.COLOR_GREEN);
	public static final Color LIGHT_GREEN = new Color(null, 95, 191, 95);
	// 窗口背景色
	public static final Color PANEL_BACK = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
	// 窗口前景色
	public static final Color PANEL_FRONT = display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
	// 选项卡标题背景色
	public static final Color TAB_BACK = display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT);
	// 单元格背景色(通信配置)
	public static final Color CELL_BACK = new Color(null, 216, 228, 248);
	// 粉红色
	public static final Color PINK = new Color(null, 252, 4, 152);
	// 标题背景
	public static final Color TITLE_BACK = new Color(null, 205, 193, 231);
	
	public static final Font IED_TITLE = new Font(null, "宋体", 10, SWT.BOLD);
	public static final Font IED_INFO = new Font(null, "宋体", 10, SWT.NONE);
	//信号关联信息中的字体
	public static final Font FONT20 = new Font(null, "宋体", 20, SWT.NONE);
	public static final Font FONT18 = new Font(null, "宋体", 18, SWT.NONE);
	public static final Font FONT12 = new Font(null, "宋体", 12, SWT.NONE);
	public static final Font FONT10 = new Font(null, "宋体", 10, SWT.NONE);
	
	// 界面字体
	public static final Font FONT_HEADER = new Font(null, "微软雅黑", 9, SWT.NONE);
	public static final Font FONT_CONTENT = new Font(null, "宋体", 10, SWT.NONE);
	public static final Font FONT_CONTENT1 = new Font(null, "宋体", 16, SWT.BOLD);
	public static final Font FONT_TITLE_BIG = new Font(null, "宋体", 36, SWT.NONE);
	
	public static Font font_expr = new Font(null, "Arial", 16, SWT.BOLD);
	public static Font font_operator = new Font(null, "Arial", 12, SWT.BOLD);
	public static Font font_expr_30 = new Font(null, "Arial", 12, SWT.BOLD);
	public static Font font_operator_30 = new Font(null, "Arial", 10, SWT.BOLD);
	// ktable风格
	public static final int Ktable_LastFill = SWTX.AUTO_SCROLL|SWTX.EDIT_ON_KEY|SWT.FULL_SELECTION|SWT.MULTI|SWTX.FILL_WITH_LASTCOL;
	public static final int Ktable_DummyFill = SWTX.AUTO_SCROLL|SWTX.EDIT_ON_KEY|SWT.FULL_SELECTION|SWT.MULTI|SWTX.FILL_WITH_DUMMYCOL;
	public static final int KTABLE_CELL_STYLE = SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | SWTX.AUTO_SCROLL|SWTX.EDIT_ON_KEY|SWT.MULTI|SWTX.FILL_WITH_LASTCOL;
	
	// 单双行颜色
	public static final Color ROW_ODD_BACK = WHITE;
	public static final Color ROW_EVEN_BACK =new Color(null, 244, 241, 245);//
	// 对话框大小
	public static final int Dialog_Width = 700;
	public static final int Dialog_Height = 600;
	// 控件大小
	public static final int txt_size = 150;
	public static final int cmb_size = txt_size - 17;
	// 表格列宽
	public static final int Col_W_Min = 5;
	public static final int Col_W_Default = 100;
	// IED图形背景色
	public static final Color IED_COLOR = new Color(null, 137, 231, 129);
	// 手型鼠标提示
	public static final Cursor hand =  new Cursor(null, SWT.CURSOR_HAND); 
	// 箭头鼠标提示
	public static final Cursor arrow = new Cursor(null, SWT.CURSOR_ARROW); 
	// 下拉列表样式
	public static final int cmb_style = SWT.READ_ONLY | SWT.BORDER;
	public static final int cmbbox_style = SWT.BORDER;
	// 默认风格
	public static final String CurrentTheme = ThemeConstants.STYLE_OFFICE2007;
	// 窗口背景色
	public static final Color Win_BG = new Color(null, 191, 219, 255);
	// 窗口内容背景色
	public static final Color Content_BG =new Color(null, 244, 241, 250);;//hanhouyang
	public static final java.awt.Color AWT_Content_BG = new java.awt.Color(244, 241, 250);

	
	public static final Color FPK_LBL = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
	
	/**
	 * 获取表格行背景色。（针对只有一行标题的表格）
	 * @param row
	 * @return
	 */
	public static Color getRowColor(int row) {
		return row % 2 == 0 ? UIConstants.ROW_EVEN_BACK : UIConstants.ROW_ODD_BACK;
	}
	
	/**
	 * 设置子空间背景色背景色（例如：工具栏）
	 * @param toolBar
	 */
	public static void setToolsBgs(Container toolBar) {
		for (int i=0; i<toolBar.getComponentCount(); i++) {
			Component c = toolBar.getComponent(i);
			c.setBackground(UIConstants.AWT_Content_BG);
			if (c instanceof Container) {
				setToolsBgs((Container)c);
			}
		}
	}
}
