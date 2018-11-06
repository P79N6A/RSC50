package com.synet.tool.rsc.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.DialogHelper;

/**
 * 安措配置
 * @author 36576
 *
 */
public abstract class SafetyMeasureEditor extends BaseConfigEditor{

	public SafetyMeasureEditor(Composite container, IEditorInput input) {
		super(container, input);
		// TODO Auto-generated constructor stub
	}
	
	protected IField[] getExportFields() {
		List<IField> list = new ArrayList<>();
		IField[] fields = table.getExportFields();
		if (fields != null) {
			for (IField field : fields) {
				if (field.getTitle().contains("代码")) {
					continue;
				}
				list.add(field);
			}
		}
		return list.toArray(new IField[0]);
	}
	
	@SuppressWarnings("unchecked")
	protected void exportData() {
		try {
			String fileName = DialogHelper.getSaveFilePath("保存", "", new String[]{"*.xlsx"});
			if (fileName == null) {
				return;
			}
			List<Object> list = (List<Object>) table.getInput();
			exportTemplateExcel(fileName, table.getTableDesc(), getExportFields(), list);
		} catch (Exception e) {
			console.append("导出失败！");
		}
	}

}
