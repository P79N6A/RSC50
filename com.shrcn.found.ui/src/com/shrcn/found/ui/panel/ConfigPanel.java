/**
 * Copyright (c) 2007-2012 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.panel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.shrcn.business.ui.AbstractValueSelector;
import com.shrcn.business.ui.NetSelector;
import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.model.Form;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.FormFieldUtil;
import com.shrcn.found.ui.util.ICheckHandler;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.util.UICheckUtil;

/**
 * 根据配置文件生成界面，界面采用gridLayout布局 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-10-29
 */
/**
 * $Log: ConfigPanel.java,v $
 * Revision 1.9  2013/11/06 00:40:55  cchun
 * 增加网口选择的修改事件
 *
 * Revision 1.8  2013/11/05 13:58:57  cchun
 * 修改界面切换，不修改也提示保存对话框的问题
 *
 * Revision 1.7  2013/09/26 12:54:38  cchun
 * Update:支持combobox
 *
 * Revision 1.6  2013/08/12 03:38:22  scy
 * Update：更新框架的校验检查仅通过datType属性配置。
 *
 * Revision 1.5  2013/07/18 13:39:14  cchun
 * Udpate:增加panel编辑方式
 *
 * Revision 1.4  2013/07/11 00:47:30  scy
 * Update：增加dispose及getDefaultValue方法
 *
 * Revision 1.3  2013/05/17 05:04:27  cchun
 * Fix Bug:调整Text宽度
 *
 * Revision 1.2  2013/04/16 02:42:05  zsy
 * Update:修改默认值显示问题
 *
 * Revision 1.1  2013/03/29 09:55:09  cchun
 * Add:创建
 *
 * Revision 1.13  2013/03/28 03:02:06  cchun
 * Refactor:1、修复校验bug；2、修改构造方法参数
 *
 * Revision 1.12  2013/03/04 05:15:08  scy
 * Update：1、高级模式与简单模式界面切换功能实现；
 * 		2、增加一个colnum初始化方法，简单模式为2否则根据配置文件获取。
 *
 * Revision 1.11  2013/02/05 02:37:54  scy
 * Update：修改下拉列表样式
 *
 * Revision 1.10  2013/01/07 03:59:04  cchun
 * Update:调整输入控件宽度为125
 *
 * Revision 1.9  2013/01/06 12:12:16  cchun
 * Update:添加滚动条
 *
 * Revision 1.8  2012/12/10 03:08:42  cchun
 * Update:添加输入校验
 *
 * Revision 1.7  2012/11/09 09:35:11  cchun
 * Refactor:添加IConfigInput接口
 *
 * Revision 1.6  2012/11/08 12:30:12  cchun
 * Update:增加combo控件
 *
 * Revision 1.5  2012/10/30 07:42:25  cchun
 * Update:调整格式
 *
 * Revision 1.4  2012/10/30 05:28:51  cchun
 * Update:使用统一的控件创建方法
 *
 * Revision 1.3  2012/10/29 08:54:17  cchun
 * Update:修改初始化处理
 *
 */
public class ConfigPanel extends Composite implements IConfigInput {
	
	protected HashMap<String, Control> mapContrlEdit = new HashMap<String, Control>();
	protected HashMap<String, IField> mapFieldEdit = new HashMap<String, IField>();
	protected Form form;

	protected ScrolledComposite sc;
	private Composite cmp;

	protected Object obj;
	protected Map<String, String> map;
	
	private boolean isSetObj = false;
	private boolean isSimp = true;
	private int colnum;
	private static final int simp_colnum = 2;
	
	private ICheckHandler checkObserver = new ICheckHandler() {
		private String preMsg = null;
		@Override
		public void showCheckResult(String msg) {
			if (msg != null && preMsg == null) {
				DialogHelper.showErrorAsyn(msg);
				preMsg = msg;
			}
		}
		@Override
		public void hideCheckResult() {
			preMsg = null;
		}};
	
	/**
	 * 
	 * @param parent 父类容器
	 * @param filePath 读取的配置文件的路径 为com/shrcn/tool/rtu/dpa104/logicied.xml这一形式
	 * @param obj 界面对应的对象
	 */
	public ConfigPanel(Composite parent, Form form) {
		super(parent, SWT.NONE);
		this.form = form;
		initColnum();
		createContent();
	}
	
	protected void initColnum(){
		if (form.isHasExd())
			this.colnum = simp_colnum;
		else
			this.colnum = form.getColumns();
	}

	/**
	 * 创建panel内部控件,付初值
	 */
	public void createContent() {
		setLayout(new FillLayout());
		sc = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setExpandVertical(true);
		sc.setExpandHorizontal(true);
		sc.setAlwaysShowScrollBars(false); 
		createControls();
	}

	/**
	 * 创建控件.
	 * 
	 * @param isSimp
	 *            是否为简单模式.
	 */
	protected void createControls() {
		cmp = SwtUtil.createComposite(sc, null, 1);
		sc.setContent(cmp);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = colnum * 3;
		gridLayout.horizontalSpacing = 5;
		gridLayout.verticalSpacing = 9;
		cmp.setLayout(gridLayout);
		
		IField[] fields = form.getFields();
		for(int i = 0; i < fields.length; i++) {
			final IField field = fields[i];
			String desc = field.getTitle();
			String input = field.getEditor();
			boolean isInd = isSimp ? !field.isExd() : true;
			String defaultV = StringUtil.nullToEmpty(field.getDefaultValue());
			// 占位
			if ("separator".equals(desc)) {
				new Label(cmp, SWT.NONE);
				new Label(cmp, SWT.NONE);
				new Label(cmp, SWT.NONE);
				continue;
			}
			// 标题
			if(!StringUtil.isEmpty(desc) && !"checkbox".equalsIgnoreCase(input)){
				GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				Label lbl = SwtUtil.createLabel(cmp, desc, gridData);
				setInd(lbl, gridData, isInd);
			}
			// 输入控件
			Control editCtrl = null;
			String remark = field.getRemark();
			if (remark == null)
				remark = "";
			String dict = field.getDictType();
			int width = field.getWidth();
			GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gridData.widthHint = width<0 ? 125 : width;
			int cbStyle = UIConstants.cmb_style;
			int cbItems = 8;
			boolean readonly = field.isReadonly();
			
			if (!StringUtil.isEmpty(dict) && !"checkbox".equalsIgnoreCase(input)) {
				String[] items = DictManager.getInstance().getDictNames(dict);
				editCtrl = new Combo(cmp, cbStyle);
				((Combo) editCtrl).setItems(items);
				((Combo) editCtrl).setText(defaultV);
				((Combo) editCtrl).setVisibleItemCount(cbItems);
			} else if (input.equalsIgnoreCase("combo") || input.equalsIgnoreCase("combobox")) {
				cbStyle = input.equalsIgnoreCase("combobox") ? UIConstants.cmbbox_style : cbStyle;
				editCtrl = new Combo(cmp, cbStyle);
				((Combo) editCtrl).setText(defaultV);
				((Combo) editCtrl).setVisibleItemCount(cbItems);
				if (!StringUtil.isEmpty(dict)) {
					String[] items = DictManager.getInstance().getDictNames(dict);
					((Combo) editCtrl).setItems(items);
				}
			} else if (input.equalsIgnoreCase("text") || input.equalsIgnoreCase("none")) {
				editCtrl = new Text(cmp, SWT.BORDER);
				((Text) editCtrl).setText(defaultV);
				if (input.equalsIgnoreCase("none")) {
					((Text) editCtrl).setEnabled(false);
				}
				gridData.widthHint += 20;
			}  else if (input.equalsIgnoreCase("password")) {
				editCtrl = new Text(cmp, SWT.BORDER|SWT.PASSWORD);
				((Text) editCtrl).setText(defaultV);
				gridData.widthHint += 20;
			} else if (input.startsWith("netSelector")) {
				editCtrl = new NetSelector(cmp, input);
				gridData.widthHint += 50;
			} else if (input.startsWith("valueSelector")) {
				String editorClass = field.getEditorClass();
				editCtrl = (Control) ObjectUtil.newInstance(getClass(), editorClass,
						new Class<?>[]{Composite.class, String.class}, new Object[] {cmp, input});
				gridData.widthHint += 50;
			} else if (input.equalsIgnoreCase("checkbox")) {
				editCtrl = new Button(cmp, SWT.CHECK);
				((Button)editCtrl).setText(desc);
				gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
			} else if (input.equalsIgnoreCase("button")) {
				editCtrl = new Button(cmp, SWT.PUSH);
				((Button)editCtrl).setText(desc);
				gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
			}
			editCtrl.setLayoutData(gridData);
			setInd(editCtrl, gridData, isInd);
			if (readonly) {
				if (input.equalsIgnoreCase("text"))
					((Text) editCtrl).setEditable(false);
				else
					editCtrl.setEnabled(false);
			}
			editCtrl.setFont(UIConstants.FONT_CONTENT);
			mapContrlEdit.put(field.getName(), editCtrl);
			mapFieldEdit.put(field.getName(), field);
			// 备注
			if (remark != null && !"checkbox".equalsIgnoreCase(input)) {
				GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				Label lbl = SwtUtil.createLabel(cmp, remark, gd);
				setInd(lbl, gd, isInd);
			}
			addSaveListener(editCtrl, field);
		}
		Point size = cmp.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		sc.setMinSize(size);
//		sc.getParent().pack(true);
	}
	
	/**
	 * 保存字段值（默认不保存）。
	 * @param control
	 * @param field
	 */
	protected void saveField(Control control, IField field) {
		String name = field.getName();
		String value = FormFieldUtil.getControlValue(control, field);
		if (obj != null) {
			ObjectUtil.setProperty(obj, name, value);
		}
		if (map != null) {
			map.put(name, value);
		}
	}
	
	/**
	 * 设置显示屏蔽.
	 * 
	 * @param control
	 *            控件对象.
	 * @param gd
	 *            布局.
	 * @param isInd
	 *            是否显示.
	 */
	private void setInd(Control control, GridData gd, boolean isInd) {
		control.setVisible(isInd);
		gd.exclude = !isInd;
	}
	
	/**
	 * 更新显示模式.
	 * 
	 * @param isSimp
	 *            是否为简单模式.
	 */
	public void updateMode() {
		Map<String, String> mapValues = getMapValues();
		disposeAllControl(cmp);
		createControls();
		setControlValue(mapValues);
	}
	
	/**
	 * 销毁cmp及其子控件
	 * 
	 * @param cmp
	 *            待销毁控件.
	 */
	private void disposeAllControl(Composite cmp) {
		Control[] childs = cmp.getChildren();
		for (Control child : childs) {
			child.dispose();
		}
		cmp.dispose();
	}

	@Override
	public void dispose() {
		disposeAllControl(cmp);
		super.dispose();
	}

	/**
	 * 给界面付初值
	 */
	public void setDefaults() {
		isSetObj = true;
		setControlValue(null);
		isSetObj = false;
	}
	
	/**
	 * 为控件赋值.
	 * 
	 * @param mapValues
	 *            控件与数据对应映射.
	 */
	private void setControlValue(Map<String, String> mapValues) {
		Iterator<String> iterator = mapFieldEdit.keySet().iterator();
		while (iterator.hasNext()) {
			String fieldName = iterator.next();
			Control control = mapContrlEdit.get(fieldName);
			IField field = mapFieldEdit.get(fieldName);
			String value = (mapValues == null ? field.getDefaultValue() : mapValues.get(fieldName));
			if (value != null)
				FormFieldUtil.setControlValue(control, field, value);
		}
	}
	
	public Map<String, String> getDefaultValue() {
		Map<String, String> map = new HashMap<String, String>();
		Iterator<String> iterator = mapFieldEdit.keySet().iterator();
		while (iterator.hasNext()) {
			String fieldName = iterator.next();
			IField field = mapFieldEdit.get(fieldName);
			String name = field.getName();
		    String value = field.getDefaultValue();
		    map.put(name, value);
		}
		return map;
	}
	
	/**
	 * 设置obj
	 */
	public void setObject(Object obj) {
		this.obj = obj;
		isSetObj = true;
		initValues();
		isSetObj = false;
	}

	/**
	 * 根据object的属性值，设置界面上的值
	 * @param obj
	 */
	protected void initValues() {
		if (obj == null)
			return;
		Iterator<String> iterator = mapFieldEdit.keySet().iterator();
		try {
			while (iterator.hasNext()) {
				String fieldName = iterator.next();
				final Control control = mapContrlEdit.get(fieldName);
			    final IField field = mapFieldEdit.get(fieldName);
				String name = field.getName();
				Object v = ObjectUtil.getProperty(obj, name);
				String value = "" + (v==null?"":v);
			    FormFieldUtil.setControlValue(control, field, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置map
	 */
	public void setMap(Map<String, String> map) {
		this.map = map;
		initMapValues();
	}

	/**
	 * 根据map的属性值，设置界面上的值
	 */
	protected void initMapValues() {
		if (map == null)
			return;
		Iterator<String> iterator = mapFieldEdit.keySet().iterator();
		try {
			while (iterator.hasNext()) {
				String fieldName = iterator.next();
				final Control control = mapContrlEdit.get(fieldName);
			    final IField field = mapFieldEdit.get(fieldName);
				String name = field.getName();
				String value = "" + map.get(name);
			    FormFieldUtil.setControlValue(control, field, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void addSaveListener(final Control control, final IField field) {
		ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!isSetObj) {
					saveField(control, field);
				}
			}
		};
		FocusListener focuslistener = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (!isSetObj) {
					if (e.getSource() instanceof Text) {
						Text txt = (Text) e.getSource();
						String value = txt.getText();
						String msg = UICheckUtil.checkDataType(field, value);
						if (msg != null) {
							checkObserver.showCheckResult(msg);
							return;
						} else {
							checkObserver.hideCheckResult();
						}
					}
					saveField(control, field);
				}
			}
		};
		
		// 添加值修改事件
		if (control instanceof Combo) {
			// 下拉框添加修改事件
			Combo combo = (Combo) control;
			combo.addModifyListener(modifyListener);
		} else if (control instanceof Text) {
			// 文本添加修改事件
			Text text = (Text) control;
			text.addFocusListener(focuslistener);
		} else if (control instanceof NetSelector) {
			// 网口选择添加修改事件
			NetSelector selector = (NetSelector) control;
			selector.addModifyListener(modifyListener);
		} else if (control instanceof AbstractValueSelector) {
			// 网口选择添加修改事件
			AbstractValueSelector selector = (AbstractValueSelector) control;
			selector.addModifyListener(modifyListener);
		} else if (control instanceof Button) {
			// 按钮添加修改事件
			Button button = (Button) control;
			button.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					saveField(control, field);
				}
			});
		} 
	}
	
	// 用于文本信息其他校验
	protected boolean checkValue(IField field, String value, String oldValue) {
		return true;
	}
	
	/**
	 * 以Map形式得到界面值。
	 * @return
	 */
	public Map<String, String> getMapValues() {
		Map<String, String> map = new HashMap<String, String>();
		Iterator<String> iterator = mapFieldEdit.keySet().iterator();
		while (iterator.hasNext()) {
			String fieldName = iterator.next();
			Control control = mapContrlEdit.get(fieldName);
			IField field = mapFieldEdit.get(fieldName);
			String name = field.getName();
			//取出界面上的数据
		    String value = FormFieldUtil.getControlValue(control, field);
		    //存入map
		    map.put(name, value);
		}
		return map;
	}
	
	/**
	 * 以Object形式得到界面值。
	 * @return
	 */
	public Object getValues() {
		if (obj == null) {
			String className = form.getClassName();
			int p = className.indexOf("/");
			if (p > 0) {
				String bundleID = className.substring(0, p);
				className = className.substring(p + 1);
				obj = ObjectUtil.newInstance(bundleID, className, new Class<?>[]{}, new Object[]{});
			} else {
				obj = ObjectUtil.newInstance(className, new Class<?>[]{}, new Object[]{});
			}
		}
		Iterator<String> iterator = mapFieldEdit.keySet().iterator();
		while (iterator.hasNext()) {
			String fieldName = iterator.next();
			Control control = mapContrlEdit.get(fieldName);
			IField field = mapFieldEdit.get(fieldName);
			String name = field.getName();
			//取出界面上的数据
		    String value = FormFieldUtil.getControlValue(control, field);
		    //存入对象Object
		 	ObjectUtil.setProperty(obj, name, value);
		}
		return obj;
	}
	
	/**
	 * 检查用户输入的内容是否满足条件
	 * @return
	 */
	public boolean check() {
		String msg = null;
		IField[] fields = form.getFields();
		for(int i = 0; i < fields.length; i++) {
			IField field = fields[i];
			String fieldName = field.getName();
			String desc = field.getTitle();
			// 占位
			if ("separator".equals(desc)) {
				continue;
			}
			Control control = mapContrlEdit.get(fieldName);
			String value = FormFieldUtil.getControlValue(control, field);
			msg = UICheckUtil.checkDataType(field, value);
			if (msg != null) {
				break;
			}
		}
		if (msg != null) {
			checkObserver.showCheckResult(msg);
			return false;
		} else {
			checkObserver.hideCheckResult();
			return true;
		}
	}
	
	/**
	 * 给面板控件赋值。
	 * @param fieldName
	 * @param value
	 */
	public void setFieldValue(String fieldName, String value) {
		FormFieldUtil.setControlValue(
				getContrlByName(fieldName),
				getFieldByName(fieldName),
				value);
	}
	
	/**
	 * 根据名称得到对象值。
	 * @param fieldName
	 * @return
	 */
	public String getFieldValue(String fieldName) {
		return FormFieldUtil.getControlValue(getContrlByName(fieldName),
				getFieldByName(fieldName));
	}
	
	/**
	 * 根据名称得到控件
	 * @param name
	 * @return
	 */
	public Control getContrlByName(String fieldName) {
		if (mapContrlEdit.containsKey(fieldName))
			return mapContrlEdit.get(fieldName);
		else
			return null;
	}
	
	/**
	 * 根据名称得到field
	 * @param fieldName
	 * @return
	 */
	private IField getFieldByName(String fieldName) {
		return mapFieldEdit.get(fieldName);
	}

	@Override
	public void addListeners() {
	}

	@Override
	public void initConfig() {
	}

	public boolean isSimp() {
		return isSimp;
	}

	public void setSimp(boolean isSimp) {
		this.isSimp = isSimp;
		this.colnum = isSimp ? simp_colnum : form.getColumns();
		updateMode();
	}

	public void setCheckObserver(ICheckHandler checkObserver) {
		this.checkObserver = checkObserver;
	}
	
 }
