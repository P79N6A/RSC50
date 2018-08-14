package com.shrcn.business.scl.dialog;

import com.shrcn.business.xml.schema.LnClass;
import com.shrcn.found.ui.model.FieldBase;


/**
 * 逻辑节点的名称和描述
 */
public class LNNameField extends FieldBase {
	
	public LNNameField() {
		super("逻辑节点名称", 100);
	}

	public String getTextValue(Object object) {
		if (object instanceof LnClass) {
			String name = ((LnClass) object).getName();
			return name;
		} else
			return "";
	}
}
