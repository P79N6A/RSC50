/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.util;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.sf.feeling.swt.win32.extension.widgets.ShellWrapper;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.ArrayUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.UIConstants;

/**
 * 
 * @author 聂国勇(mailto:nguoyong@shrcn.com)
 * @version 1.0, 2011-1-24
 */
/**
 * $Log: SwtUtil.java,v $
 * Revision 1.19  2013/11/08 11:19:56  cchun
 * Update:添加createWarnLabel()
 *
 * Revision 1.18  2013/09/26 12:48:16  cchun
 * Update:设置combo可见长度
 *
 * Revision 1.17  2013/09/25 01:25:21  cchun
 * Update:createFileSelector()使用统一控件
 *
 * Revision 1.16  2013/07/18 13:34:57  cchun
 * Update:修改createLabelCombo
 *
 * Revision 1.15  2013/07/03 11:57:51  cchun
 * Refactor:移动常量到common
 *
 * Revision 1.14  2013/06/26 00:23:19  cxc
 * Update：修改图片获取方式
 *
 * Revision 1.13  2013/06/08 10:30:39  cchun
 * Update:修改对齐距离
 *
 * Revision 1.12  2013/05/30 06:55:15  cchun
 * Update:添加GridData
 *
 * Revision 1.11  2013/05/28 13:27:18  cchun
 * Update:调整布局
 *
 * Revision 1.10  2013/05/24 07:21:27  scy
 * Update：将下拉列表设置为只读
 *
 * Revision 1.9  2013/05/20 03:12:44  cchun
 * Update:添加createText()
 *
 * Revision 1.8  2013/05/17 05:29:12  cxc
 * Update:修改createComposite() layout
 *
 * Revision 1.7  2013/05/17 05:04:06  cchun
 * Fix Bug:设置控件字体
 *
 * Revision 1.6  2013/05/07 01:33:22  cchun
 * Delete:删除createLabel(Composite parent, GridData griddata, String name)
 *
 * Revision 1.5  2013/04/15 00:49:24  scy
 * Update：单选List方法增加滚动条
 *
 * Revision 1.4  2013/04/07 12:24:58  cchun
 * Refactor:清理引用
 *
 * Revision 1.3  2013/04/07 02:29:09  cchun
 * Update:添加背景色设置
 *
 * Revision 1.2  2013/04/02 13:29:15  cchun
 * Add:添加界面相关方法
 *
 * Revision 1.1  2013/03/29 09:36:45  cchun
 * Add:创建
 *
 * Revision 1.36  2013/03/27 00:46:07  cchun
 * Update:添加注释
 *
 * Revision 1.35  2013/03/08 03:03:26  cchun
 * Refactor:提炼SwtUtil.setTheme()
 *
 * Revision 1.34  2013/03/04 09:29:58  cchun
 * Refactor:center()改名为moveToCenter()
 *
 * Revision 1.33  2013/03/04 07:34:14  scy
 * Add：增加对话框居中方法。
 *
 * Revision 1.32  2012/12/03 06:17:28  cchun
 * Update:添加createCombo()
 *
 * Revision 1.31  2012/11/28 06:08:21  cchun
 * Update:添加createContextMenu()
 *
 * Revision 1.30  2012/11/22 08:04:11  cchun
 * Refactor:createText()
 *
 * Revision 1.29  2012/11/20 05:54:07  cchun
 * Update:为button添加图标
 *
 * Revision 1.28  2012/11/13 01:21:53  cchun
 * Update:添加createTabFolder()和createCheckbox()
 *
 * Revision 1.27  2012/11/12 00:54:46  cchun
 * Refactor:封装getDefaultShell()
 *
 * Revision 1.26  2012/11/09 05:37:15  cchun
 * Update:添加getGridLayout()
 *
 * Revision 1.25  2012/11/06 09:28:17  cchun
 * Update:添加createMultiList()和createCombo()
 *
 * Revision 1.24  2012/11/06 03:14:35  cchun
 * Update:添加createRadioButton(0
 *
 * Revision 1.23  2012/11/01 08:01:15  cchun
 * Update:添加createList()
 *
 * Revision 1.22  2012/10/30 05:28:14  cchun
 * Update:增加控件方法
 *
 * Revision 1.21  2012/10/25 11:06:59  cchun
 * Update:增加控件创建方法
 *
 * Revision 1.20  2012/08/28 03:53:09  cchun
 * Refactor:统一文件选择对话框接口
 *
 * Revision 1.19  2012/06/14 08:53:37  cchun
 * Update:添加createLabelText(),createLabelCombo()
 *
 * Revision 1.18  2012/04/11 08:48:16  cchun
 * Update:添加createLabelCombo()
 *
 * Revision 1.17  2012/02/29 07:13:03  cchun
 * Update:添加getTextWidth()
 *
 * Revision 1.16  2012/01/04 07:23:06  cchun
 * Fix Bug:修复createFileSelector()中变量path为空导致的异常
 *
 * Revision 1.15  2011/12/07 09:26:21  cchun
 * Update:添加文本提示功能
 *
 * Revision 1.14  2011/11/21 09:43:41  cchun
 * Update:添加createHalfEditor()
 *
 * Revision 1.13  2011/09/19 08:28:05  cchun
 * Update:修改createFileSelector()使路径最后部分可见
 *
 * Revision 1.12  2011/09/02 07:11:51  cchun
 * Update:清理引用
 *
 * Revision 1.11  2011/08/28 10:00:31  cchun
 * Update:添加双击选择
 *
 * Revision 1.10  2011/08/28 03:20:09  cchun
 * Update:添加createSearchBox()
 *
 * Revision 1.9  2011/08/25 10:00:27  cchun
 * Update:简化createLabelText（）对齐逻辑
 *
 * Revision 1.8  2011/08/11 10:14:14  cchun
 * Update:补充注释
 *
 * Revision 1.7  2011/08/02 07:12:52  cchun
 * Update:为界面表格添加背景色
 *
 * Revision 1.6  2011/07/27 07:38:08  cchun
 * Update:添加createFileSelector(),setVisible()
 *
 * Revision 1.5  2011/04/06 08:22:43  cchun
 * Update:添加createTitleLabel()方法
 *
 * Revision 1.4  2011/03/07 08:17:26  cchun
 * Update:聂国勇修改，修改createCom方法
 *
 * Revision 1.3  2011/03/02 07:03:36  cchun
 * Add:聂国勇增加，增加面板操作
 *
 * Revision 1.2  2011/02/23 05:39:47  cchun
 * Update:添加createLabelText()
 *
 * Revision 1.1  2011/01/25 03:51:19  cchun
 * Update:整理代码
 *
 * Revision 1.1  2011/01/25 02:05:22  cchun
 * Add:聂国勇增加，增加同时添加多个逻辑节点功能
 *
 */
final public class SwtUtil {

	public static final GridData bt_gd = new GridData(80, SWT.DEFAULT);
	public static final GridData bt_hd = new GridData(140, SWT.DEFAULT);
	public static final GridData hf_gd = new GridData(GridData.FILL_HORIZONTAL);
	public static final GridData vf_gd = new GridData(GridData.FILL_VERTICAL);
	public static final GridData bf_gd = new GridData(GridData.FILL_BOTH);
	
	private static final int w = 1; // 表格间隙宽度
	private static IconsManager iconmgr = IconsManager.getInstance();
	
	public static final PaintListener PAINT_LISTENER = new PaintListener() {  
        public void paintControl(PaintEvent e) {
            GC gc = e.gc;  
            gc.setForeground(UIConstants.BLUE);  
            // 在文本框内实现加一条底线  
            // gc.drawRectangle(e.x - 1, e.y - 1, e.width + 1, e.height);  
            // 修改文本框整个边框颜色  
            gc.drawRectangle(e.x, e.y, e.width - 1, e.height - 1);
            gc = null;  
        }  
    };

    public static void setContainerBg(final Composite child) {
		child.setBackground(UIConstants.Content_BG);
		child.setBackgroundMode(SWT.INHERIT_DEFAULT);
	}
	
	/**
	 * 获取当前Shell。
	 * @return
	 */
	public static Shell getDefaultShell() {
//		return Display.getDefault().getActiveShell();
		return new Shell();
	}
	
	/**
	 * 得到表格布局。
	 * @param cols
	 * @param w
	 * @return
	 */
	public static GridLayout getGridLayout(int cols, int w) {
		GridLayout layout = new GridLayout(cols, false);
		layout.marginBottom = w;
		layout.marginLeft = w;
		layout.marginRight = w;
		layout.marginTop = w;
		layout.marginWidth = w;
		layout.verticalSpacing = w;
		layout.horizontalSpacing = w;
		return layout;
	}
	
	/**
	 * 得到表格布局。
	 * @param cols
	 * @return
	 */
	public static GridLayout getGridLayout(int cols) {
		return getGridLayout(cols, w);
	}
	
	/**
	 * 创建TabFolder。
	 * @param parent
	 * @param style
	 * @return
	 */
	public static CTabFolder createTabFolder(Composite parent, int style) {
		CTabFolder tabFolder = new CTabFolder(parent, style);
		tabFolder.setSelectionBackground(UIConstants.TAB_BACK);
		tabFolder.setFont(UIConstants.FONT_HEADER);
		setContainerBg(tabFolder);
		return tabFolder;
	}
	
	/**
	 * 添加TabItem。
	 * @param tabFolder
	 * @param title
	 * @param control
	 */
	public static void addTabItem(CTabFolder tabFolder, String title, Control control) {
		final CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setFont(UIConstants.FONT_HEADER);
		tabItem.setText(title);
		tabItem.setControl(control);
	}
	
	/**
	 * 创建列表控件。
	 * @param parent
	 * @param gridData
	 * @return
	 */
	public static List createMultiList(Composite parent, GridData gridData) {
		List list = new List(parent, SWT.MULTI|SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
		list.setFont(UIConstants.FONT_CONTENT);
		list.setLayoutData(gridData);
		return list;
	}
	
	/**
	 * 创建列表控件。
	 * @param parent
	 * @param gridData
	 * @return
	 */
	public static List createList(Composite parent, GridData gridData) {
		List list = new List(parent, SWT.MULTI|SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
		list.setFont(UIConstants.FONT_CONTENT);
		list.setLayoutData(gridData);
		return list;
	}
	
	/**
	 * 创建下拉列表控件。
	 * @param parent
	 * @param gridData
	 * @return
	 */
	public static Combo createCombo(Composite parent, GridData gridData) {
		return createCombo(parent, SWT.BORDER, gridData);
	}
	/**
	 * 创建只读下拉列表控件。
	 * @param parent
	 * @param gridData
	 * @return
	 */
	public static Combo createCombo(Composite parent, GridData gridData,boolean readOnly) {
		if(readOnly){
			return createCombo(parent, SWT.BORDER|SWT.READ_ONLY, gridData);
		}else{
			return createCombo(parent, SWT.BORDER, gridData);
		}
	}
	
	/**
	 * 创建下拉列表控件（可指定样式）。
	 * @param parent
	 * @param style
	 * @param gridData
	 * @return
	 */
	public static Combo createCombo(Composite parent, int style, GridData gridData) {
		Combo combo = new Combo(parent, style);
		combo.setFont(UIConstants.FONT_CONTENT);
		combo.setLayoutData(gridData);
		combo.setVisibleItemCount(10);
		return combo;
	}
	
    /**
     * 创建标签。
     * 
     * @param parent
     * @param name
     * @param gridData
     * @return
     */
    public static Label createLabel(Composite parent, String name, GridData gridData) {
		return createLabel(parent, name, null, null, UIConstants.FONT_HEADER, gridData);
    }
    
    /**
     * 创建标签。
     * 
     * @param parent
     * @param image
     * @param gridData
     * @return
     */
    public static Label createLabel(Composite parent, Image image, GridData gridData) {
		return createLabel(parent, null, image, null, null, gridData);
    }
    
    public static Label createLabel(Composite parent, String name, Font font, GridData gridData) {
		return createLabel(parent, name, null, null, font, gridData);
    }
    
    public static Label createWarnLabel(Composite parent, String name, GridData gridData) {
		return createLabel(parent, name, null, UIConstants.RED, null, gridData);
    }
    
    public static Label createLabel(Composite parent, String name, Image image, Color color, Font font, GridData gridData) {
		Label label = new Label(parent, SWT.NONE);
		if (name != null) {
			label.setText(name);
		} 
		if (image != null) {
			label.setImage(image);
		} 
		if (font != null) {
			label.setFont(font);
		} 
		if (color != null) {
			label.setForeground(color);
		}  
		if (gridData != null) {
			label.setLayoutData(gridData);
		}
		return label;
    }
    
    /**
     * 创建Group控件。
     * 
     * @param parent
     * @param name
     * @return
     */
    public static Group createGroup(Composite parent, String name, GridData gridData) {
    	final Group group = new Group(parent, SWT.NONE);
    	setContainerBg(group);
        group.setFont(UIConstants.FONT_HEADER);
        group.setLayoutData(gridData);
        group.setText(name);
        return group;
    }
    
    /**
	 * 创建button控件。
	 * 
	 * @param parent
	 * @param rect
	 * @param name
	 * @return
	 */
    public static Button createPushButton(Composite parent, String name, GridData gridData,String icon,Font font) {
        final Button button = new Button(parent, SWT.PUSH);
        if (name !=null) {
        	button.setText(name);
		}
        if (gridData !=null) {
        	button.setLayoutData(gridData);
		}
        if (icon !=null) {
        	button.setImage(iconmgr.getImage(icon));
		}
        if (font !=null) {
        	button.setFont(font);
		}
        return button;
    }
    
    /**
     * 创建带图标的button。
     * @param parent
     * @param name
     * @param gridData
     * @param icon
     * @return
     */
    public static Button createPushButton(Composite parent, String name, GridData gridData, String icon) {
        return createPushButton(parent,name,gridData,icon,null);
    }  
    public static Button createPushButton(Composite parent, String name, GridData gridData) {
    	return createPushButton(parent, name, gridData,null,null);
    }
    public static Button createPushButton(Composite parent, String name, GridData gridData,Font font) {
         return createPushButton(parent,name,gridData,null,font);
     }  
    
    /**
     * 创建单选按钮。
     * 
     * @param parent
     * @param name
     * @param gridData
     * @return
     */
    public static Button createRadioButton(Composite parent, String name, GridData gridData) {
        final Button button = new Button(parent, SWT.RADIO);
        button.setText(name);
        button.setFont(UIConstants.FONT_CONTENT);
        button.setBackground(UIConstants.Content_BG);
        button.setLayoutData(gridData);
        return button;
    }
    
    /**
     * 创建单选按钮。
     * 
     * @param parent
     * @param name
     * @param gridData
     * @return
     */
    public static Button createToggleButton(Composite parent, String name, GridData gridData) {
    	final Button button = new Button(parent, SWT.TOGGLE);
    	button.setText(name);
    	button.setFont(UIConstants.FONT_CONTENT);
    	button.setLayoutData(gridData);
    	return button;
    }
    
    /**
     * 创建CheckBox。
     * @param parent
     * @param name
     * @param gridData
     * @return
     */
    public static Button createCheckBox(Composite parent, String name, GridData gridData) {
        final Button button = new Button(parent, SWT.CHECK);
        button.setText(name);
        button.setFont(UIConstants.FONT_CONTENT);
        button.setBackground(UIConstants.Content_BG);
        button.setLayoutData(gridData);
        return button;
    }
    
    /**
     * 创建微调按钮
     * @param parent
     * @param gridData
     * @return
     */
    public static Spinner createSpinner(Composite parent, GridData gridData) {
    	final Spinner spinner = new Spinner(parent, SWT.BORDER);
    	spinner.setFont(UIConstants.FONT_CONTENT);
    	spinner.setLayoutData(gridData);
        return spinner;
    }
    
    public static Button createButton(Composite parent,GridData griddata,int style, String name){
        final Button button = new Button(parent,style);
        button.setLayoutData(griddata);
        button.setText(name);
        button.setFont(UIConstants.FONT_HEADER);
        return button;
    }
    
    public static Button createLabelButton(Composite parent,String buttonTxt,String labelTxt){
    	final Composite child = new Composite(parent,SWT.NONE);
    	GridLayout layout = new GridLayout(2,true);
        child.setLayout(layout);  
        Label label = new Label(child,SWT.NONE);
        label.setLayoutData(new GridData(GridData.BEGINNING));
        label.setText(labelTxt);
        final Button button = new Button(child,SWT.PUSH);
        button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        button.setText(buttonTxt);
        return button;
    }
    
    /**
     * 创建标题标签
     * @param parent
     * @param griddata
     * @param name
     * @return
     */
    public static Label createTitleLabel(Composite parent, String name) {
		final Label label = new Label(parent, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		label.setText(name);
		label.setFont(UIConstants.FONT_HEADER);
		return label;
	}
    
    public static Text createText(Composite parent, GridData griddata, String defaultValue){
        final Text text = new Text(parent, SWT.BORDER);
        text.setLayoutData(griddata);
        text.setText(defaultValue);
        return text;
    }
    
	/**
	 * 创建文本框。
	 * @param parent
	 * @param gridData
	 * @return
	 */
	public static Text createText(Composite parent, GridData gridData) {
		return createText(parent, "", gridData);
	}
	
	/**
	 * 创建文本框。
	 * @param parent
	 * @param content
	 * @param gridData
	 * @return
	 */
	public static Text createText(Composite parent, String content, GridData gridData) {
		return createText(parent, SWT.BORDER, content, gridData);
    }
	
    /**
     * 创建文本框。
     * @param parent
     * @param griddata
     * @param style
     * @return
     */
    public static Text createText(Composite parent, int style, GridData griddata){
        return createText(parent, style, "", griddata);
    }
	
	/**
	 * 创建文本框。
	 * @param parent
	 * @param style
	 * @param content
	 * @param gridData
	 * @return
	 */
	public static Text createText(Composite parent, int style, String content, GridData gridData) {
		Text txt = new Text(parent, style | SWT.BORDER); 
		txt.setText(content);
		txt.setFont(UIConstants.FONT_CONTENT);
		txt.setLayoutData(gridData);
		return txt;
	}
    
    /**
     * 创建多行文本框
     * @param parent
     * @param griddata
     * @param defaultValue
     * @return
     */
    public static Text createMultiText(Composite parent, String content, GridData griddata) {
    	return createMultiText(parent, SWT.NONE, content, griddata);
    }
    
    /**
     * 创建多行文本框
     * @param parent
     * @param griddata
     * @param style
     * @return
     */
    public static Text createMultiText(Composite parent, int style, GridData griddata) {
        return createMultiText(parent, style, "", griddata);
    }
    
    public static Text createMultiText(Composite parent, int style, String content, GridData griddata){
    	 final Text text = new Text(parent, SWT.BORDER | SWT.MULTI | style);
         text.setLayoutData(griddata);
         text.setFont(UIConstants.FONT_CONTENT);
         text.setText(content);
         return text;
    }
    
    public static Text createLabelText(Composite parent, String labelName, GridData griddata) {
		createLabel(parent, labelName, null);
    	return createText(parent, griddata);
	}
    
    /**
     * 创建带有标签的文本框（水平填充）
     * @param parent
     * @param lbTxt
     * @return
     */
    public static Text createLabelText(Composite parent, String lbTxt) {
    	return createLabelText(parent, lbTxt, new GridData(GridData.FILL_HORIZONTAL));
    }

    public static Text createLabelText(Composite parent, String labelName, String content, int lw, int tw) {
    	final GridData gdLabel = new GridData(lw, SWT.DEFAULT);
    	final GridData gdText = new GridData(tw, SWT.DEFAULT);
		createLabel(parent, labelName, gdLabel);
		return createText(parent, content, gdText);
    }
    
    public static Combo createLabelCombo(Composite parent, String lbTxt, String[] items, int lw, int cw) {
    	final GridData gdLabel = new GridData(lw, SWT.DEFAULT);
    	final GridData gdCombo = new GridData(cw, SWT.DEFAULT);

    	final Label lbName = createLabel(parent, lbTxt, gdLabel);
		lbName.setLayoutData(gdLabel);
		
		Combo comb = createCombo(parent, SWT.BORDER | SWT.READ_ONLY, gdCombo);
		comb.setItems(items);
		return comb;
    }
    
    /**
     * 创建带有标签的下拉框（水平填充）
     * @param parent
     * @param lbTxt
     * @return
     */
    public static Combo createLabelCombo(Composite parent, String lbTxt, String[] items) {
    	final Label lbFCDA = new Label(parent, SWT.NONE);
		lbFCDA.setText(lbTxt);
		lbFCDA.setFont(UIConstants.FONT_HEADER);
		
		Combo comb = createCombo(parent, SWT.BORDER | SWT.READ_ONLY, new GridData(GridData.FILL_HORIZONTAL));
		comb.setItems(items);
		return comb;
    }
    public static Composite createTextButton(Composite parent, GridData griddata, String textName) {
        Composite child = createComposite(parent,new GridData(GridData.FILL_HORIZONTAL),4);
    	final Text text = new Text(child,SWT.BORDER);
    	griddata.horizontalSpan=3;
        text.setLayoutData(griddata);
        createButton(child,new GridData(GridData.END),SWT.PUSH,textName);
        return child;
    }
    
    public static Composite createTextFileButton(Composite parent,GridData griddata,final String[] filters){    
        Composite child = new Composite(parent,SWT.NONE);
        child.setLayout(new GridLayout(2,false));
        child.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        final Text text = new Text(child,SWT.BORDER);
        text.setLayoutData(griddata);
        final Button button = createButton(child,new GridData(GridData.END),SWT.PUSH,"...");
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	final String pathName = DialogHelper.selectFile(button.getShell(), 
            			SWT.OPEN, filters);
        		if (!StringUtil.isEmpty(pathName))
                	text.setText(pathName);
            }
        
        });
        return child;
    }
    
    public static Composite createComposite(Composite parent, GridData griddata,int num){
    	final Composite child = new Composite(parent, SWT.NONE);
    	GridLayout layout = new GridLayout(num, false);
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.marginTop = 0;
        layout.marginBottom = 0;
        layout.marginWidth = 4;
        layout.marginHeight = 4;
        child.setLayout(layout);  
        child.setLayoutData(griddata);
        setContainerBg(child);
    	return child;
    }
    
    public static Group createGroup(Composite parent,GridData griddata,int num,String name){
        final Group group = new Group(parent,SWT.NONE);
        GridLayout layout = new GridLayout(num, false);
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.marginTop = 0;
        layout.marginBottom = 0;
        layout.marginWidth = 4;
        layout.marginHeight = 4;
        group.setLayout(layout);        
        group.setLayoutData(griddata);
        group.setFont(UIConstants.FONT_HEADER);
        setContainerBg(group);
        group.setText(name);
        return group;
    }
    
    public static CTabFolder createTab(Composite parent, GridData griddata, String[] name) {
		CTabFolder tab = new CTabFolder(parent, SWT.TAB);
		tab.setLayoutData(griddata);
		for (int i = 0; i < name.length; i++) {
			Composite comsite = new Composite(tab, SWT.NONE);
			comsite.setLayout(new GridLayout());
			CTabItem item = new CTabItem(tab, SWT.NONE);
			item.setControl(comsite);
			item.setText(name[i]);
		}
		return tab;
	}
    
    /**
     * 创建文件选择控件
     * @param parent
     * @param title
     * @param extensions
     * @return
     */
    public static Text createFileSelector(final Composite pane, String title, final String...extensions) {
    	pane.setLayout(getGridLayout(3, 5));
		createLabel(pane, title, null);
    	final Text txtPath = createText(pane, hf_gd);
    	txtPath.setEditable(false);
    	final Button bt = createPushButton(pane, "...", null);
    	bt.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			String path = DialogHelper.selectFile(pane.getShell(), SWT.OPEN, extensions);
    			if (path == null)
    				return;
    			if (!new File(path).exists()) {
					DialogHelper.showWarning("当前文件不存在，请重新选择！");
					return;
				}
    			txtPath.setText(path);
    			txtPath.setSelection(path.length());
    		}
    	});
    	return txtPath;
	}
    
    public static Text createDirectorySelector(final Composite pane, String title, final String msg) {
//    	pane.setLayout(getGridLayout(3, 5));
		createLabel(pane, title, null);
    	final Text txtPath = createText(pane, hf_gd);
    	txtPath.setEditable(false);
    	final Button bt = createPushButton(pane, "...", null);
    	bt.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			String path = DialogHelper.selectDirectory(pane.getShell(), SWT.OPEN, "选择", msg);
    			if (path == null)
    				return;
    			if (!new File(path).exists()) {
					DialogHelper.showWarning("当前文件夹不存在，请重新选择！");
					return;
				}
    			txtPath.setText(path);
    			txtPath.setSelection(path.length());
    		}
    	});
    	return txtPath;
	}
    
    /**
     * 控制是否可见
     * @param con
     * @param visible
     */
    public static void setVisible(Control con, boolean visible) {
    	con.setVisible(visible);
    	((GridData)con.getLayoutData()).exclude = !visible;
    	con.getParent().layout();
    }
    
    /**
     * 为树形表格行设置背景色
     * @param tree
     */
    public static void setTreeItemBgColors(Tree tree) {
    	TreeItem[] items = tree.getItems();
		for (int row = 0; row < items.length; row++) {
			TreeItem item = items[row];
			item.setBackground(UIConstants.getRowColor(row + 1));
		}
    }
    
    /**
     * 为表格行设置背景色
     * @param table
     */
    public static void setTableItemBgColors(Table table) {
    	TableItem[] items = table.getItems();
		for (int row = 0; row < items.length; row++) {
			TableItem item = items[row];
			item.setBackground(UIConstants.getRowColor(row + 1));
		}
    }
    
    /**
     * 创建搜索框
     * @param parent
     * @return
     */
    public static Text createSearchBox(Composite parent) {
    	final Composite composite = new Composite(parent, SWT.BORDER);
		composite.setBackground(UIConstants.WHITE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		composite.setLayout(gridLayout);

		final Text text = new Text(composite, SWT.NONE);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		text.setBackground(UIConstants.WHITE);

		final Label label = new Label(composite, SWT.NONE);
		label.setBackground(UIConstants.WHITE);
		label.setImage(IconsManager.getInstance().getImage(ImageConstants.DETAIL));
		
		text.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				text.selectAll();
			}
		});
		return text;
    }
    
    /**
     * 创建后半部分修改框
     * @param parent
     * @param lbDesc
     * @param txDesc
     * @return
     */
    public static Control[] createHalfEditor(Composite parent ,String lbDesc ,String txDesc){
    	final Composite composite = new Composite(parent, SWT.BORDER);
		composite.setBackground(UIConstants.WHITE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		composite.setLayout(gridLayout);
		
	    final Label label = new Label(composite, SWT.NONE);
		label.setBackground(UIConstants.Content_BG);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
		label.setLayoutData(gridData);
		label.setText(lbDesc);
		
		final Text text = new Text(composite, SWT.NONE);
		text.setLayoutData( new GridData(SWT.FILL, SWT.FILL, true, true));
		text.setBackground(UIConstants.WHITE);
		text.setText(txDesc);
		
		text.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				text.selectAll();
			}
		});
		return new Control[]{label,text};
    }
    
    public static void addTextProposal(Text text, String[] choices) {
    	addProposal(text, new TextContentAdapter(), choices);
    }
    
    public static void addComboProposal(Combo combo, String[] choices) {
    	addProposal(combo, new ComboContentAdapter(), choices);
    }
	
	public static void addProposal(Control control, IControlContentAdapter adapter, String[] choices) {
		char[] autoActivationCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_.".toCharArray();
		KeyStroke keyStroke = null;
		try {
//			keyStroke = KeyStroke.getInstance("Alt+/");
			keyStroke = KeyStroke.getInstance(".");
		} catch (ParseException e) {
			SCTLogger.error("", e);
		}  
		// assume that myTextControl has already been created in some way  
		MyContentProposalProvider myProvider = new MyContentProposalProvider(choices);
		myProvider.setFiltering(true);
		new ContentProposalAdapter(control, adapter, myProvider, keyStroke, autoActivationCharacters);
	}
	
	/**
	 * 根据指定控件获取文本宽度。
	 * @param string
	 * @param control
	 * @return
	 */
	public static int getTextWidth(String string, Control control) {
		int width = 0;
		GC gc = new GC(control);
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			width += gc.getAdvanceWidth(c);
		}
		gc.dispose();
		return width;
	}

	/**
	 * 根据指定字体获取文本宽度。
	 * @param string
	 * @param font
	 * @return
	 */
	public static int getTextWidth(String string, Font font) {
		if (StringUtil.isEmpty(string))
			return 0;
		int width = 0;
		Shell shell = new Shell();
		Label label = new Label(shell, SWT.NONE);
		label.setFont(font);
		GC gc = new GC(label);
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			width += gc.getAdvanceWidth(c);
		}
		gc.dispose();
		label.dispose();
		shell.dispose();
		return width;
	}
	
	/**
	 * 创建菜单。
	 * @param control
	 * @param actions
	 */
	public static void createContextMenu(Control control, final boolean hide, final Action...actions) {
		// 更新节点上下文菜单
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(control);
		control.setMenu(menu);
		menuManager.addMenuListener(new IMenuListener(){
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				MenuManager menuMgr = (MenuManager)manager;
				if (hide) {
					menuMgr.removeAll();
					for (Action action : actions) {
						if (action instanceof com.shrcn.found.ui.util.Separator) {
							menuMgr.add(new Separator());
						} else if (action.isEnabled()) {
							action.setEnabled(action.isEnabled());
							menuMgr.add(action);
						}
					}
				} else {
					menuMgr.updateAll(true);
				}
       }});
	}
	
	public static void createContextMenu(Control control, final Action...actions) {
		createContextMenu(control, true, actions);
	}
	
	/**
	 * 创建菜单（解决swtextension界面中无法弹出的bug）
	 * @param popupMenu
	 */
	public static void addMenus(Control control, final Action...actions) {
		final Menu popupMenu = new Menu(control);
		control.setMenu(popupMenu);
		popupMenu.setVisible(false);
		control.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button != 1) {
					for (final Action action : actions) {
						for (MenuItem item : popupMenu.getItems()) {
							if (action instanceof com.shrcn.found.ui.util.Separator) {
							} else if (action.getText().equals(item.getText())) {
								item.setEnabled(action.isEnabled());
							}
						}
					}
					popupMenu.setVisible(true);
				}
			}
		});
		for (final Action action : actions) {
			if (action instanceof com.shrcn.found.ui.util.Separator) {
				new MenuItem(popupMenu, SWT.SEPARATOR);
				continue;
			} 
			MenuItem item = new MenuItem(popupMenu, SWT.POP_UP);
			item.setText(action.getText());
			ImageDescriptor imageDescriptor = action.getImageDescriptor();
			if (imageDescriptor != null)
				item.setImage(imageDescriptor.createImage());
			item.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					action.run();
				}
			});
		}
	}
	
	/**
	 * 添加工具栏按钮下拉菜单
	 * @param toolbar
	 * @param actions
	 */
	public static void addToolItemMenus(ToolBar toolbar, String itIcon, final Action...actions) {
		ToolItem filterItem = new ToolItem(toolbar, SWT.DROP_DOWN);
		filterItem.setImage(ImgDescManager.getImageDesc(itIcon).createImage());
		filterItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Widget item = event.widget;
				if (item != null) {
					int style = item.getStyle();
					if ((style & SWT.DROP_DOWN) != 0) {
						if (event.detail == 4) { // on drop-down button
							ToolItem ti = (ToolItem) item;

							final MenuManager menuManager = new MenuManager();
							Menu menu = menuManager.createContextMenu(ti.getParent());
							menuManager.addMenuListener(new IMenuListener(){
								@Override
								public void menuAboutToShow(IMenuManager manager) {
									MenuManager menuMgr = (MenuManager)manager;
									menuMgr.removeAll();
									for (Action action : actions) {
										if (action instanceof com.shrcn.found.ui.util.Separator) {
											menuMgr.add(new Separator());
										} else if (action.isEnabled()) {
											action.setEnabled(action.isEnabled());
											menuMgr.add(action);
										}
									}
								}
							});

							// position the menu below the drop down item
							Point point = ti.getParent().toDisplay(
									new Point(event.x, event.y));
							menu.setLocation(point.x, point.y); // waiting for SWT
							// 0.42
							menu.setVisible(true);
						}
					}
				}
				
			}
		});
	}
	
	/**
	 * 居中显示对话框
	 * 
	 * @param dialog
	 */
	public static void moveToCenter(Shell shell) {
		Rectangle clRect = Display.getCurrent().getClientArea();
		Rectangle bound = shell.getBounds(); 
		shell.setLocation((clRect.width - bound.width) >> 1,
				(clRect.height - bound.height) >> 1);
	}
	
	/**
	 * 修改窗口样式。
	 * @param shell
	 * @param theme ThemeConstants.STYLE_VISTA
	 * 				ThemeConstants.STYLE_OFFICE2007
	 * 				ThemeConstants.STYLE_GLOSSY
	 */
	public static void setTheme(Shell shell, String theme) {
		ShellWrapper wrapper = new ShellWrapper(shell);
		wrapper.installTheme(theme);
	}
		
	/**
	 * 切换树节点折叠、展开状态。
	 * @param treeViewer
	 */
	public static void changeExpandStatus(TreeViewer treeViewer) {
		StructuredSelection selection = (StructuredSelection) treeViewer.getSelection();
		Object entity = selection.getFirstElement();
		boolean expanded = ArrayUtil.contains(treeViewer.getExpandedElements(), entity);
		if (expanded)
			treeViewer.collapseToLevel(entity, 1);
		else
			treeViewer.expandToLevel(entity, 1);
	}
	
	/**
	 * 自动调整大小并居中
	 * @param shell
	 */
	public static void packToCenter(Shell shell) {
		shell.pack();
		moveToCenter(shell);
	}
	
	/**
	 * 复制到剪贴板
	 * @param text
	 */
	public static void copyToClipBoard(final String text) {
		Clipboard clipboard = new Clipboard(Display.getDefault());
    	clipboard.setContents(new Object[] { text },
    			new Transfer[] { TextTransfer.getInstance() });
	}
	
	/**
	 * 获取剪贴板内容
	 * @return
	 */
	public static String getClipBoardContent() {
		Clipboard clipboard = new Clipboard(Display.getDefault());
		return (String) clipboard.getContents(TextTransfer.getInstance());
	}
	
	/**
	 * 判断界面是否启动。
	 * @return
	 */
	public static boolean hasUI() {
		return org.eclipse.core.runtime.Platform.isRunning();
	}
	
	/**
	 * 是否为Linux环境。
	 * @return
	 */
	public static boolean isLinux() {
		String osname = System.getProperty("os.name");
		return osname.toLowerCase().indexOf("windows") < 0;
	}
	
	/**
	 * 设置文本框焦点，并定位光标至最后。
	 * @param txt
	 */
	public static void focusText(Text txt) {
		txt.forceFocus();
		txt.setSelection(txt.getText().length());
	}
}
