package com.shrcn.business.scl.dialog;

import com.shrcn.business.xml.schema.LnClass;
import com.shrcn.found.ui.model.FieldBase;

public class LNDescField extends FieldBase {
	
	public LNDescField() {
		super("逻辑节点描述", 200);
	}
	
	public String getTextValue(Object object) {
		if (object instanceof LnClass) {
			String desc = ((LnClass) object).getDesc();
			return desc;
		} else
			return "";
	}
}
